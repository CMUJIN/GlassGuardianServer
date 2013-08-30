package com.jinhs.fetch.mirror;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.collect.Lists;

@Component
public class MirrorUtil {
	public TimelineItem populateTimeLine(String text, NotificationLevelEnum level, List<MenuItem> menuItemList){
		TimelineItem timeLineItem = new TimelineItem();
		timeLineItem.setText(text);
		timeLineItem.setNotification(new NotificationConfig().setLevel(level.getValue()));
		if(menuItemList!=null)
			timeLineItem.setMenuItems(menuItemList);
		return timeLineItem;
	}
	
	public TimelineItem populateTimeLine(String text, NotificationLevelEnum level){
		return populateTimeLine(text, level, null);
	}
	
	public Contact pupulateContact(String contact_id, String contact_name, String imageUrl){
		Contact starterProjectContact = new Contact();
		starterProjectContact.setId(contact_id);
	    starterProjectContact.setDisplayName(contact_name);
	    starterProjectContact.setImageUrls(Lists.newArrayList(imageUrl));
	    return starterProjectContact;
	}
}
