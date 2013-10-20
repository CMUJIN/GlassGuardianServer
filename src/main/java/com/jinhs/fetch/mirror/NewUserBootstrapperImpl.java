/*
 * Copyright (C) 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jinhs.fetch.mirror;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.Subscription;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.common.WebUtil;
import com.jinhs.fetch.mirror.enums.CollectionEnum;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

/**
 * Utility functions used when users first authenticate with this service
 * 
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 */
@Component
public class NewUserBootstrapperImpl implements NewUserBootstrapper {
	private static final Logger LOG = Logger
			.getLogger(NewUserBootstrapperImpl.class.getSimpleName());

	public static final String CONTACT_NAME = "Fetch";

	@Autowired
	AuthUtil authUtil;

	@Autowired
	MirrorClient mirrorClient;

	@Autowired
	MirrorUtil mirrorUtil;

	public void bootstrapNewUser(String userId) throws IOException {
		Credential credential = authUtil.newAuthorizationCodeFlow()
				.loadCredential(userId);

		// Create contact
		Contact contact = mirrorUtil.pupulateContact(
				CONTACT_NAME, CONTACT_NAME, WebUtil.buildContactImageUrl());
		Contact insertedContact = mirrorClient.insertContact(credential,
				contact);
		LOG.info("Bootstrapper inserted contact " + insertedContact.getId()
				+ " for user " + userId);

		try {
			// Subscribe to timeline updates
			Subscription subscription = mirrorClient.insertSubscription(
					credential, WebUtil.buildNotifyCallBackUrl(), userId,
					CollectionEnum.TIMELINE.getValue());
			LOG.info("Bootstrapper inserted subscription "
					+ subscription.getId() + " for user " + userId);
		} catch (GoogleJsonResponseException e) {
			LOG.warn("Failed to create timeline subscription. Might be running on "
					+ "localhost. Details:" + e.getDetails().toPrettyString());
		}

		// Built in actions
		List<MenuItem> menuItemList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addCustomMenuItem(menuItemList, CustomActionConfigEnum.FETCH);
		TimelinePopulateHelper.addCustomMenuItem(menuItemList, CustomActionConfigEnum.PUSH);
		TimelinePopulateHelper.addCustomMenuItem(menuItemList, CustomActionConfigEnum.LIKE);
		TimelinePopulateHelper.addCustomMenuItem(menuItemList, CustomActionConfigEnum.DISLIKE);
		TimelinePopulateHelper.addCustomMenuItem(menuItemList, CustomActionConfigEnum.FETCH_FIRST);
		TimelinePopulateHelper.addMenuItem(menuItemList, MenuItemActionEnum.TOGGLE_PINNED);
		TimelineItem timelineItem = mirrorUtil.populateTimeLine(
				"Fetch & Leave Footprint", menuItemList);
		
		URL url = new URL(WebUtil.buildHomeBackgroundImageUrl());
		InputStream attachment =  url.openStream();
		mirrorClient.insertTimelineItem(credential,
				timelineItem, "image/png", attachment);
		LOG.info("Bootstrapper inserted welcome message "
				+ timelineItem.getId() + " for user " + userId);
	}
}
