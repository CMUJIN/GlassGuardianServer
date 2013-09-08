package com.jinhs.fetch.mirror;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

public class TimelinePopulateHelper {
	
	
	public static List<TimelineItem> populateBundleNotes(List<NoteBo> notes, Mirror mirrorService) throws IOException{
		List<TimelineItem> list = new ArrayList<TimelineItem>();
		
		int max = 3;
		int i=0;
		for(NoteBo note:notes){
		/*TimelineItem timelineItem = new TimelineItem();
		timelineItem.setText("Hello world");
		InputStreamContent mediaContent = new InputStreamContent("image/jpeg", new ByteArrayInputStream(note.getImage_note()));*/
			if(note.getValuation()==0){
				TimelineItem timelineItem =  mirrorService.timeline().get(note.getTimeline_id()).execute();
				timelineItem.setBundleId("testbundleid");
				list.add(timelineItem);
				i++;
				if(i>=max)
					break;
			}
		}
		return list;
	}
	
	public static void addMenuItem(List<MenuItem> menuItemList, MenuItemActionEnum action) {
		menuItemList.add(new MenuItem().setAction(action.getValue()));
	}
	
	public static void addCustomMenuItem(List<MenuItem> menuItemList, CustomActionConfigEnum config){
		MenuItem customItem = new MenuItem();
		customItem.setAction(config.getType());
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
