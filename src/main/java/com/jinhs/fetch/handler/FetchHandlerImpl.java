package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.gson.Gson;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.common.BundleIdProcessHelper;
import com.jinhs.fetch.common.DataProcessHelper;
import com.jinhs.fetch.common.FetchCacheTaskPayload;
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
	ZoneRateHandler zoneRateHandler;
	
	@Override
	public void fetch(Notification notification, Credential credential) throws IOException {
		LOG.info("Fetch operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		
		List<NoteBo> noteListByCoordinate = null;
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue());
		// query first group notes
		noteListByCoordinate = transService.fetchFirstGroupNotesByCoordinate(
				notification.getUserToken(), location.getLatitude(),
				location.getLongitude(), AppConstants.BUNDLE_SIZE);
		List<NoteBo> firstGroupNotes = extractFirstGroupNotes(noteListByCoordinate);
		
		addFetchCacheTask(notification, location, firstGroupNotes);
		
		String valuationHtml = populateRateHTML(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue(), "no address avaliable", zipCode);
		
		processTimelineResponse(credential, zipCode, firstGroupNotes,
				valuationHtml);
	}

	private void processTimelineResponse(Credential credential, String zipCode,
			List<NoteBo> firstGroupNotes, String valuationHtml)
			throws IOException {
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

	private void addFetchCacheTask(Notification notification,
			Location location, List<NoteBo> firstGroupNotes) {
		//Add new task to task queue
		FetchCacheTaskPayload payload = new FetchCacheTaskPayload();
		payload.setFirstGroupNotes(firstGroupNotes);
		payload.setUserToken(notification.getUserToken());
		payload.setLocation(location);
		
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions task = TaskOptions.Builder.withUrl("/api/fetchcache").method(Method.POST).payload(new Gson().toJson(payload));
		queue.addAsync(task);
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

	private String populateRateHTML(double latitude, double longtitude, String address, String zip_code) throws IOException {
		int rateByCoordinate = zoneRateHandler.getRateByCoordiate(latitude, longtitude);
		int rateByAddress = zoneRateHandler.getRateByAddress(address);
		int rateByZip = zoneRateHandler.getRateByZip(zip_code);
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
}
