package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.TimelinePopulateHelper;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

@Component
public class InsertTimelineHandlerImpl implements InsertTimelineHandler {
	private static final Logger LOG = Logger.getLogger(InsertTimelineHandlerImpl.class.getSimpleName());
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Override
	public void insertBundleTimelinesWithoutCover(Credential credential,
			List<NoteBo> noteBoList, String bundleId) throws IOException {
		
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItemWithPayload(actionList, CustomActionConfigEnum.FETCH_MORE, bundleId);
		
		for (TimelineItem timelineItem : TimelinePopulateHelper
				.populateBundleNotes(noteBoList,
						mirrorClient.getMirror(credential), bundleId)) {
			timelineItem.setMenuItems(actionList);
			mirrorClient.insertTimelineItem(credential, timelineItem);
		}
		LOG.info("insertBundleTimelines successfully, bundleId:"+bundleId);
	}

	@Override
	public void insertNoMoreFetchAvaliable(Credential credential) throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		String text = "No more notes avaliable";
		TimelineItem timelineItem = mirrorUtil.populateTimeLine(text, actionList);
		mirrorClient.insertTimelineItem(credential, timelineItem);
		LOG.info("insertNoMoreFetchAvaliable successfully");
	}

	@Override
	public void insertEmptyTimeline(Credential credential, String zipCode,
			String valuationHtml) throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		
		TimelineItem itemCover = mirrorUtil.populateTimeLineWithHtml(valuationHtml, actionList);
		mirrorClient.insertTimelineItem(credential, itemCover);
		LOG.info("insertEmptyTimeline successfully");
	}

	@Override
	public void insertBundleTimelines(Credential credential,
			List<NoteBo> noteBoList, String bundleId, String valuationHtml)
			throws IOException {
		insertBundleCover(credential, bundleId, valuationHtml);
		insertBundleTimelinesWithoutCover(credential, noteBoList, bundleId);
		LOG.info("insertBundleTimelines successfully, bundleId:"+bundleId);
	}

	private void insertBundleCover(Credential credential, String bundleId,
			String valuationHtml) throws IOException {
		TimelineItem itemCover = mirrorUtil.populateTimeLineWithHtml(valuationHtml);
		
		itemCover.setBundleId(bundleId);
		itemCover.setIsBundleCover(true);
		mirrorClient.insertTimelineItem(credential, itemCover);
	}

}
