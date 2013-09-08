package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.DataProcessHelper;
import com.jinhs.fetch.common.GeoCodingHelper;
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
	
	@Override
	public void fetch(Notification notification, Credential credential) throws IOException {
		LOG.info("Fetch operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude().doubleValue(), location.getLongitude().doubleValue());
		List<NoteBo> noteList =  transService.fetchNotes(notification.getUserToken(), zipCode);
		String text = dataProcessHelper.populateTextReviews(noteList);
		String valuation = dataProcessHelper.populateValuation(noteList)+"% People Like";
		
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.FETCH_MORE);
		//InputStreamContent mediaContent = new InputStreamContent("image/jpeg", attachment);
		TimelineItem item = mirrorUtil.populateTimeLine(valuation, actionList);
		item.setBundleId("testbundleid");
		item.setIsBundleCover(true);
		mirrorClient.insertTimelineItem(credential, item);
		
		for(TimelineItem timelineItem: TimelinePopulateHelper.populateBundleNotes(noteList, mirrorClient.getMirror(credential))){
			mirrorClient.insertTimelineItem(credential, timelineItem);
		}
		LOG.info("fetch successfully, zipCode:"+zipCode);
	}

}
