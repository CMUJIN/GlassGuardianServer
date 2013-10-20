package com.jinhs.fetch.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Attachment;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.HtmlContentBuilder;
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
			List<NoteBo> noteBoList, String bundleId, boolean needNotification) throws IOException {
		
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItemWithPayload(actionList, CustomActionConfigEnum.FETCH_MORE, bundleId);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		
		for (TimelineItem timelineItem : TimelinePopulateHelper
				.populateBundleNotes(noteBoList,
						mirrorClient.getMirror(credential), bundleId)) {
			if(needNotification){
				timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
				needNotification = false;
			}
			timelineItem.setMenuItems(actionList);
			if(timelineItem.getAttachments()!=null){
				
				Attachment attachment = timelineItem.getAttachments().get(0);
				LOG.info("has attachment " +attachment.getContentUrl().toString());
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
		String htmlContent = HtmlContentBuilder.populateDefaultTextHTML("No more message avaliable");
		insertSingleTimeline(credential, null, htmlContent, actionList);
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
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		timelineItem.setMenuItems(actionList);
		timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, timelineItem);
	}

	@Override
	public void insertNoFirstNoteAvaliable(double latitude, double longtitude, Credential credential)
			throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItem(actionList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		String htmlContent = HtmlContentBuilder.populateNoFirstHTML(latitude, longtitude);
		insertSingleTimeline(credential, null, htmlContent, actionList);
		LOG.info("insertNoFirstNoteAvaliable successfully");
	}
	
	private void insertSingleTimeline(Credential credential, String text, String html, List<MenuItem> actionList) throws IOException{
		TimelineItem timelineItem;
		if(html!=null)
			timelineItem = mirrorUtil.populateTimeLineWithHtml(html, actionList);
		else
			timelineItem = mirrorUtil.populateTimeLine(text, actionList);
		timelineItem.setNotification(new NotificationConfig().setLevel(NotificationLevelEnum.Default.getValue()));
		mirrorClient.insertTimelineItem(credential, timelineItem);
	}

	@Override
	public void insertHasSameRateBefore(Credential credential)
			throws IOException {
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		String htmlContent = HtmlContentBuilder.populateDefaultTextHTML("You have given same rate to this place before");
		insertSingleTimeline(credential, null, htmlContent, actionList);
		LOG.info("insertHasSameRateBefore successfully");
	}

}
