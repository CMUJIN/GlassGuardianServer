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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.Subscription;
import com.google.api.services.mirror.model.TimelineItem;

/**
 * Utility functions used when users first authenticate with this service
 * 
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 */
@Component
public class NewUserBootstrapperImpl implements NewUserBootstrapper {
	private static final Logger LOG = Logger
			.getLogger(NewUserBootstrapperImpl.class.getSimpleName());

	public static final String CONTACT_NAME = "FETCH";

	@Autowired
	AuthUtil authUtil;

	@Autowired
	WebUtil webUtil;

	@Autowired
	MirrorClient mirrorClient;

	@Autowired
	MirrorUtil mirrorUtil;

	public void bootstrapNewUser(String userId) throws IOException {
		Credential credential = authUtil.newAuthorizationCodeFlow()
				.loadCredential(userId);

		// Create contact
		Contact starterProjectContact = mirrorUtil.pupulateContact(
				CONTACT_NAME, CONTACT_NAME, webUtil.buildContactImageUrl());
		Contact insertedContact = mirrorClient.insertContact(credential,
				starterProjectContact);
		LOG.info("Bootstrapper inserted contact " + insertedContact.getId()
				+ " for user " + userId);

		try {
			// Subscribe to timeline updates
			Subscription subscription = mirrorClient.insertSubscription(
					credential, webUtil.buildNotifyCallBackUrl(), userId,
					CollectionEnum.TIMELINE.getValue());
			LOG.info("Bootstrapper inserted subscription "
					+ subscription.getId() + " for user " + userId);
		} catch (GoogleJsonResponseException e) {
			LOG.warn("Failed to create timeline subscription. Might be running on "
					+ "localhost. Details:" + e.getDetails().toPrettyString());
		}

		// Built in actions
		List<MenuItem> menuItemList = new ArrayList<MenuItem>();
		addMenuItem(menuItemList, MenuItemActionEnum.TOGGLE_PINNED);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.FETCH);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.PUSH);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.LIKE);
		addCustomMenuItem(menuItemList, CustomActionConfigEnum.DISLIKE);
		TimelineItem timelineItem = mirrorUtil.populateTimeLine(
				"Welcome to Fetch Pin this timeline", NotificationLevelEnum.Default,
				menuItemList);
		TimelineItem insertedItem = mirrorClient.insertTimelineItem(credential,
				timelineItem);
		LOG.info("Bootstrapper inserted welcome message "
				+ insertedItem.getId() + " for user " + userId);
	}

	private void addMenuItem(List<MenuItem> menuItemList, MenuItemActionEnum action) {
		menuItemList.add(new MenuItem().setAction(action.getValue()));
	}
	
	private void addCustomMenuItem(List<MenuItem> menuItemList, CustomActionConfigEnum config){
		MenuItem customItem = new MenuItem();
		customItem.setAction(config.getType());
		customItem.setId(config.getName());
		customItem.setValues(buildMenuValues(config));
		menuItemList.add(customItem);
	}
	
	private List<MenuValue> buildMenuValues(CustomActionConfigEnum config) {
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
