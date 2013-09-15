package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.common.BundleIdProcessHelper;
import com.jinhs.fetch.common.DataProcessHelper;
import com.jinhs.fetch.common.GeoCodingHelper;
import com.jinhs.fetch.common.NoteBoHelper;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.TimelinePopulateHelper;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class FetchHandlerImpl implements FetchHandler {
	private static final Logger LOG = Logger.getLogger(FetchHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	DataProcessHelper dataProcessHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	@Autowired
	FetchCacheHandler fetchCacheHanler;
	
	@Autowired
	NoteBoHelper noteBoHelper;
	
	@Override
	public void fetch(Notification notification, Credential credential) throws IOException {
		LOG.info("Fetch operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		
		List<NoteBo> noteListByCoordinate = null;
		List<NoteBo> noteListByAddress = null;
		List<NoteBo> noteListByZip = null;
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude().doubleValue(), location.getLongitude().doubleValue());
		
		
		noteListByCoordinate = transService.fetchNotesByCoordinate(notification.getUserToken(), location.getLatitude(), location.getLongitude());
		
		if(location.getAddress()!=null)
			noteListByAddress = transService.fetchNotesByAddress(notification.getUserToken(), location.getAddress());
		
		if(zipCode!=null)
			noteListByZip =  transService.fetchNotesByZip(notification.getUserToken(), zipCode);
		
		List<NoteBo> firstGroupNotes = extractFirstGroupNotes(noteListByCoordinate);
		
		// insert note into cache today for fetch more operation
		LinkedList<CacheNoteBo> cacheList = populateCacheList(
				noteListByCoordinate, noteListByAddress, noteListByZip);
		fetchCacheHanler.insert(cacheList);
		
		String valuationHtml = populateRateHTML(noteListByCoordinate,
				noteListByAddress, noteListByZip);
		
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.FETCH_MORE);
		
		TimelineItem item = mirrorUtil.populateTimeLineWithHtml(valuationHtml, actionList);
		String identityKey = BundleIdProcessHelper.generateIdentityKey(firstGroupNotes.get(0));
		int sequenceId = 0;
		item.setBundleId(BundleIdProcessHelper.generateBundleId(identityKey, sequenceId));
		item.setIsBundleCover(true);
		mirrorClient.insertTimelineItem(credential, item);
		
		for (TimelineItem timelineItem : TimelinePopulateHelper
				.populateBundleNotes(firstGroupNotes,
						mirrorClient.getMirror(credential), sequenceId)) {
			timelineItem.setMenuItems(actionList);
			mirrorClient.insertTimelineItem(credential, timelineItem);
		}
		LOG.info("fetch successfully, zipCode:"+zipCode);
	}

	private LinkedList<CacheNoteBo> populateCacheList(
			List<NoteBo> noteListByCoordinate, List<NoteBo> noteListByAddress,
			List<NoteBo> noteListByZip) {
		LinkedList<CacheNoteBo> cacheList = new LinkedList<CacheNoteBo>();
		HashSet<String> set = new HashSet<String>();

		processCacheNoteBoList(noteListByCoordinate, cacheList, set);
		processCacheNoteBoList(noteListByAddress, cacheList, set);
		processCacheNoteBoList(noteListByZip, cacheList, set);
		return cacheList;
	}

	private List<NoteBo> extractFirstGroupNotes(List<NoteBo> noteListByCoordinate) {
		List<NoteBo> firstGroup = new ArrayList<NoteBo>();
		int count = 0;
		for(NoteBo note:noteListByCoordinate){
			if(note.getValuation()==0&&count<AppConstants.BUNDLE_SIZE){
				firstGroup.add(note);
				count++;
				noteListByCoordinate.remove(note);
			}
		}
		return firstGroup;
	}

	private String populateRateHTML(List<NoteBo> noteListByCoordinate,
			List<NoteBo> noteListByAddress, List<NoteBo> noteListByZip) {
		int rateByCoordinate = dataProcessHelper.populateValutionByCoordinate(noteListByCoordinate);
		int rateByAddress = dataProcessHelper.populateValutionByAddress(noteListByAddress);
		int rateByZip = dataProcessHelper.populateValutionByZip(noteListByZip);
		StringBuffer sb = new StringBuffer();
		sb.append("<article class=\"auto-paginate\">");
		sb.append("<ul>");
		if(rateByCoordinate>=0){
			sb.append("<li>");
			sb.append(rateByCoordinate+"% People Like This Point");
			sb.append("</li>");
		}
		if(rateByAddress>=0){
			sb.append("<li>");
			sb.append("rateByAddress+% People Like This Point");
			sb.append("</li>");
		}
		if(rateByZip>=0){
			sb.append("<li>");
			sb.append(rateByAddress+"% People Like This Point");
			sb.append("</li>");
		}
		sb.append("</ul>");
		sb.append("</article>");
		return sb.toString();
	}
	
	private void processCacheNoteBoList(List<NoteBo> noteListByCoordinate,
			LinkedList<CacheNoteBo> list, HashSet<String> set) {
		for (NoteBo note : noteListByCoordinate) {
			String identity_key = BundleIdProcessHelper.generateIdentityKey(note);
			if (!set.contains(identity_key)&&note.getValuation()==0) {
				CacheNoteBo cache = new CacheNoteBo();
				cache.setIdentity_key(identity_key);
				cache.setNoteBo(note);
				list.add(cache);
				set.add(identity_key);
			}
		}
	}

}
