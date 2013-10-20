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
			TimelineItem timelineItem = populateSingleNote(note, mirrorService);
			if(timelineItem==null)
				continue;
			timelineItem.setBundleId(bundleId);
			list.add(timelineItem);
		}
		
		return list;
	}
	
	public static TimelineItem populateSingleNote(NoteBo note, Mirror mirrorService) throws IOException{
		TimelineItem item =  mirrorService.timeline().get(note.getTimeline_id()).execute();
		if(isDeletedTimeline(note.getTimeline_id(), mirrorService))
			return null;
		LOG.info("single note timeline id:"+item.getId());
		TimelineItem timelineItem = new TimelineItem();
		timelineItem.setId(note.getTimeline_id());
		timelineItem.setTitle(item.getTitle());
		timelineItem.setText(item.getText());
		timelineItem.setHtml(item.getHtml());
		timelineItem.setSpeakableText(item.getSpeakableText());
		if(item.getAttachments()!=null && item.getAttachments().size()!=0){
			List<Attachment> attList = new ArrayList<Attachment>();
			for(Attachment attch: item.getAttachments()){
				Attachment attachment = mirrorService.timeline().attachments().get(item.getId(), attch.getId()).execute();	
				attList.add(attachment);
				timelineItem.setAttachments(attList);
				LOG.info("insert attachment id:"+note.getAttachment_id());
			}
		}
		return timelineItem;
	}
	

	public static boolean isDeletedTimeline(String timelineId, Mirror mirrorService) throws IOException{
		TimelineItem item =  mirrorService.timeline().get(timelineId).execute();
		boolean result =false;
		if(item.getText()==null&&item.getHtml()==null&&item.getAttachments()==null)
			result = true;
		LOG.info("isDeletedTimeline timeline "+timelineId+" is empty "+result);
		return result;
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
		return menuValues;
	}
}
