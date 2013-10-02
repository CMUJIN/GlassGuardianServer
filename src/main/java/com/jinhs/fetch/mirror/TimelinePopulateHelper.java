package com.jinhs.fetch.mirror;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Attachment;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

public class TimelinePopulateHelper {
	private static final Logger LOG = Logger.getLogger(TimelinePopulateHelper.class.getSimpleName());
	
	public static List<TimelineItem> populateBundleNotes(List<NoteBo> notes, Mirror mirrorService, String bundleId) throws IOException{
		LOG.info("populate bundle timelines");
		List<TimelineItem> list = new ArrayList<TimelineItem>();
		for(NoteBo note:notes){
			TimelineItem timelineItem =  mirrorService.timeline().get(note.getTimeline_id()).execute();
			timelineItem.setBundleId(bundleId);
			if(note.getAttachment_id()!=null){
				Attachment attachment = mirrorService.timeline().attachments().get(note.getTimeline_id(), note.getAttachment_id()).execute();
				List<Attachment> attList = new ArrayList<Attachment>();
				attList.add(attachment);
				timelineItem.setAttachments(attList);
				LOG.info("insert attachment id:"+note.getAttachment_id());
			}
			list.add(timelineItem);
		}
		
		return list;
	}
	
	public static TimelineItem populateSingleNote(NoteBo note, Mirror mirrorService) throws IOException{
		TimelineItem timelineItem =  mirrorService.timeline().get(note.getTimeline_id()).execute();
		if(note.getAttachment_id()!=null){
			Attachment attachment = mirrorService.timeline().attachments().get(note.getTimeline_id(), note.getAttachment_id()).execute();
			List<Attachment> attList = new ArrayList<Attachment>();
			attList.add(attachment);
			timelineItem.setAttachments(attList);
			LOG.info("insert attachment id:"+note.getAttachment_id());
		}
		return timelineItem;
	}
	
	public static void addMenuItem(List<MenuItem> menuItemList, MenuItemActionEnum action) {
		menuItemList.add(new MenuItem().setAction(action.getValue()));
	}
	
	public static void addCustomMenuItem(List<MenuItem> menuItemList, CustomActionConfigEnum config){
		addCustomMenuItemWithPayload(menuItemList, config, null);
	}
	
	public static void addCustomMenuItemWithPayload(List<MenuItem> menuItemList, CustomActionConfigEnum config, String payload){
		MenuItem customItem = new MenuItem();
		customItem.setAction(config.getType());
		if(payload!=null)//only set for fetch more
			customItem.setId(payload);
		else
			customItem.setId(config.getName());
		customItem.setValues(buildMenuValues(config));
		menuItemList.add(customItem);
	}
	
	private static List<MenuValue> buildMenuValues(CustomActionConfigEnum config) {
		List<MenuValue> menuValues = new ArrayList<MenuValue>();
		MenuValue defaultValue = new MenuValue();
		defaultValue.setDisplayName(config.getName());
		defaultValue.setState("DEFAULT");
		defaultValue.setIconUrl(config.getIconUrl());
		menuValues.add(defaultValue);

		if (config.hasPending()) {
			MenuValue pendingValue = new MenuValue();
			pendingValue.setDisplayName(config.getPendingName());
			pendingValue.setState("PENDING");
			pendingValue.setIconUrl(config.getPendingIconUrl());
			menuValues.add(defaultValue);
		}

		if (config.hasComplete()) {
			MenuValue completeValue = new MenuValue();
			completeValue.setDisplayName(config.getCompleteName());
			completeValue.setState("COMPLETE");
			completeValue.setIconUrl(config.getCompleteIconUrl());
			menuValues.add(defaultValue);
		}
		return menuValues;
	}
}
