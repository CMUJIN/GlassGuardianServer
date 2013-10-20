package com.jinhs.fetch.mirror;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.common.collect.Lists;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.enums.NotificationLevelEnum;

@Component
public class MirrorUtil {
	public TimelineItem populateTimeLine(String text, String isSpeakableText,
			List<MenuItem> menuItemList, boolean isHtml) {
		TimelineItem timeLineItem = new TimelineItem();
		if (isSpeakableText != null)
			timeLineItem.setSpeakableText(text);
		if (isHtml)
			timeLineItem.setHtml(text);
		else
			timeLineItem.setText(text);
		timeLineItem.setNotification(new NotificationConfig()
				.setLevel(NotificationLevelEnum.Default.getValue()));
		if (menuItemList != null)
			timeLineItem.setMenuItems(menuItemList);
		return timeLineItem;
	}

	public TimelineItem populateTimeLine(String text, String isSpeakableText) {
		return populateTimeLine(text, isSpeakableText, null, false);
	}

	public TimelineItem populateTimeLine(String text) {
		return populateTimeLine(text, null, null, false);
	}

	public TimelineItem populateTimeLine(String text,
			List<MenuItem> menuItemList) {
		return populateTimeLine(text, null, menuItemList, false);
	}

	public TimelineItem populateTimeLineWithHtml(String html,
			List<MenuItem> menuItemList) {
		return populateTimeLine(html, null, menuItemList, true);
	}
	
	public TimelineItem populateTimeLineWithHtml(String html) {
		return populateTimeLine(html, null, null, true);
	}


	public Contact pupulateContact(String contact_id, String contact_name,
			String imageUrl) {
		Contact starterProjectContact = new Contact();
		starterProjectContact.setId(contact_id);
		starterProjectContact.setDisplayName(contact_name);
		starterProjectContact.setImageUrls(Lists.newArrayList(imageUrl));
		return starterProjectContact;
	}

}
