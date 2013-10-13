package com.jinhs.fetch.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.mirror.model.Attachment;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.TimelinePopulateHelper;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;
import com.jinhs.fetch.mirror.enums.NotificationLevelEnum;

@Component
public class InsertTimelineHandlerImpl implements InsertTimelineHandler {
	private static final Logger LOG = Logger.getLogger(InsertTimelineHandlerImpl.class.getSimpleName());
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Override
	public void insertBundleTimelines(Credential credential,
			List<NoteBo> noteBoList, String bundleId, boolean isFirstAsCover) throws IOException {
		
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItemWithPayload(actionList, CustomActionConfigEnum.FETCH_MORE, bundleId);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		
		for (TimelineItem timelineItem : TimelinePopulateHelper
				.populateBundleNotes(noteBoList,
						mirrorClient.getMirror(credential), bundleId)) {
			if(isFirstAsCover){
				timelineItem.setIsBundleCover(true);
				timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
				isFirstAsCover=false;
			}
			timelineItem.setMenuItems(actionList);
			if(timelineItem.getAttachments()!=null){
				LOG.info("has attachment");
				Attachment attachment = timelineItem.getAttachments().get(0);
				InputStream inputStream =
						mirrorClient.getAttachmentInputStream(credential, timelineItem.getId(), attachment.getId());
				mirrorClient.insertTimelineItem(credential, timelineItem, attachment.getContentType(), inputStream);
			}
			else{
				LOG.info("no attachment");
				mirrorClient.insertTimelineItem(credential, timelineItem);
			}
		}
		LOG.info("insertBundleTimelines successfully, bundleId:"+bundleId+" size:"+noteBoList.size());
	}

	@Override
	public void insertNoMoreFetchAvaliable(Credential credential) throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		String text = "No more notes avaliable";
		insertSingleTimeline(credential, text, actionList);
		LOG.info("insertNoMoreFetchAvaliable successfully");
	}

	@Override
	public void insertEmptyTimeline(Credential credential, String zipCode,
			String valuationHtml) throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelineItem itemCover = mirrorUtil.populateTimeLineWithHtml(valuationHtml, actionList);
		itemCover.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, itemCover);
		LOG.info("insertEmptyTimeline successfully");
	}

	@Override
	public void insertBundleTimelines(Credential credential,
			List<NoteBo> noteBoList, String bundleId, String valuationHtml)
			throws IOException {
		insertBundleCover(credential, bundleId, valuationHtml);
		insertBundleTimelines(credential, noteBoList, bundleId, false);
		LOG.info("insertBundleTimelines successfully, bundleId:"+bundleId);
	}

	private void insertBundleCover(Credential credential, String bundleId,
			String valuationHtml) throws IOException {
		TimelineItem itemCover = mirrorUtil.populateTimeLineWithHtml(valuationHtml);
		
		itemCover.setBundleId(bundleId);
		itemCover.setIsBundleCover(true);
		itemCover.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, itemCover);
	}

	@Override
	public void insertFetchFirst(Credential credential, NoteBo firstNote) throws IOException {
		TimelineItem timelineItem = TimelinePopulateHelper.populateSingleNote(firstNote, mirrorClient.getMirror(credential));
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		timelineItem.setMenuItems(actionList);
		timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, timelineItem);
	}

	@Override
	public void insertNoFirstNoteAvaliable(Credential credential)
			throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		String text = "Be the first one to note";
		insertSingleTimeline(credential, text, actionList);
		LOG.info("insertNoFirstNoteAvaliable successfully");
	}
	
	private void insertSingleTimeline(Credential credential, String text, List<MenuItem> actionList) throws IOException{
		TimelineItem timelineItem = mirrorUtil.populateTimeLine(text, actionList);
		timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, timelineItem);
	}

	@Override
	public void insertHasSameRateBefore(Credential credential)
			throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		String text = "You have given same rate to this point before";
		insertSingleTimeline(credential, text, actionList);
		LOG.info("insertHasSameRateBefore successfully");
	}

}
