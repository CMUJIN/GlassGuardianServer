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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Attachment;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.ContactsListResponse;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Subscription;
import com.google.api.services.mirror.model.SubscriptionsListResponse;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.TimelineListResponse;
import com.google.common.io.ByteStreams;
import com.jinhs.fetch.mirror.enums.OperationsEnum;

/**
 * A facade for easier access to basic API operations
 * 
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 */

@Service
public class MirrorClientImpl implements MirrorClient {
	private static final Logger LOG = Logger.getLogger(MirrorClientImpl.class
			.getSimpleName());

	public Mirror getMirror(Credential credential) throws IOException {
		return new Mirror.Builder(new UrlFetchTransport(),
				new JacksonFactory(), credential).setApplicationName(
				"Quick Note").build();
	}

	public Contact insertContact(Credential credential, Contact contact)
			throws IOException {
		Mirror.Contacts contacts = getMirror(credential).contacts();
		return contacts.insert(contact).execute();
	}

	public void deleteContact(Credential credential, String contactId)
			throws IOException {
		Mirror.Contacts contacts = getMirror(credential).contacts();
		contacts.delete(contactId).execute();
	}

	public ContactsListResponse listContacts(Credential credential)
			throws IOException {
		Mirror.Contacts contacts = getMirror(credential).contacts();
		return contacts.list().execute();
	}

	public Contact getContact(Credential credential, String id)
			throws IOException {
		try {
			Mirror.Contacts contacts = getMirror(credential).contacts();
			return contacts.get(id).execute();
		} catch (GoogleJsonResponseException e) {
			LOG.warn("Could not find contact with ID " + id);
			return null;
		}
	}

	public TimelineListResponse listItems(Credential credential, long count)
			throws IOException {
		Mirror.Timeline timelineItems = getMirror(credential).timeline();
		Mirror.Timeline.List list = timelineItems.list();
		list.setMaxResults(count);
		return list.execute();
	}

	/**
	 * Subscribes to notifications on the user's timeline.
	 */
	public Subscription insertSubscription(Credential credential,
			String callbackUrl, String userId, String collection)
			throws IOException {
		LOG.info("Attempting to subscribe verify_token " + userId
				+ " with callback " + callbackUrl);

		Subscription subscription = new Subscription();
		// Alternatively, subscribe to "locations"
		subscription.setCollection(collection);
		subscription.setCallbackUrl(callbackUrl);
		subscription.setUserToken(userId);
		List<String> operations = new ArrayList<String>();
		operations.add(OperationsEnum.UPDATE.getValue());
		operations.add(OperationsEnum.INSERT.getValue());
		operations.add(OperationsEnum.DELETE.getValue());
		subscription.setOperation(operations);
		return getMirror(credential).subscriptions().insert(subscription)
				.execute();
	}

	/**
	 * Subscribes to notifications on the user's timeline.
	 */
	public void deleteSubscription(Credential credential, String id)
			throws IOException {
		getMirror(credential).subscriptions().delete(id).execute();
	}

	public void deleteAllSubscriptions(Credential credential)
			throws IOException {
		SubscriptionsListResponse subscriptions = listSubscriptions(credential);
		for (Subscription subscription : subscriptions.getItems()) {
			deleteSubscription(credential, subscription.getId());
		}
	}

	public SubscriptionsListResponse listSubscriptions(Credential credential)
			throws IOException {
		Mirror.Subscriptions subscriptions = getMirror(credential)
				.subscriptions();
		return subscriptions.list().execute();
	}

	/**
	 * Inserts a simple timeline item.
	 * 
	 * @param credential
	 *            the user's credential
	 * @param item
	 *            the item to insert
	 */
	public TimelineItem insertTimelineItem(Credential credential,
			TimelineItem item) throws IOException {
		return getMirror(credential).timeline().insert(item).execute();
	}

	/**
	 * Inserts an item with an attachment provided as a byte array.
	 * 
	 * @param credential
	 *            the user's credential
	 * @param item
	 *            the item to insert
	 * @param attachmentContentType
	 *            the MIME type of the attachment (or null if none)
	 * @param attachmentData
	 *            data for the attachment (or null if none)
	 */
	public void insertTimelineItem(Credential credential, TimelineItem item,
			String attachmentContentType, byte[] attachmentData)
			throws IOException {
		Mirror.Timeline timeline = getMirror(credential).timeline();
		timeline.insert(item,
				new ByteArrayContent(attachmentContentType, attachmentData))
				.execute();

	}

	/**
	 * Inserts an item with an attachment provided as an input stream.
	 * 
	 * @param credential
	 *            the user's credential
	 * @param item
	 *            the item to insert
	 * @param attachmentContentType
	 *            the MIME type of the attachment (or null if none)
	 * @param attachmentInputStream
	 *            input stream for the attachment (or null if none)
	 */
	public void insertTimelineItem(Credential credential, TimelineItem item,
			String attachmentContentType, InputStream attachmentInputStream)
			throws IOException {
		insertTimelineItem(credential, item, attachmentContentType,
				ByteStreams.toByteArray(attachmentInputStream));
	}

	public InputStream getAttachmentInputStream(Credential credential,
			String timelineItemId, String attachmentId) throws IOException {
		Mirror mirrorService = getMirror(credential);
		Mirror.Timeline.Attachments attachments = mirrorService.timeline()
				.attachments();
		Attachment attachmentMetadata = attachments.get(timelineItemId,
				attachmentId).execute();
		HttpResponse resp = mirrorService
				.getRequestFactory()
				.buildGetRequest(
						new GenericUrl(attachmentMetadata.getContentUrl()))
				.execute();
		return resp.getContent();
	}

	public String getAttachmentContentType(Credential credential,
			String timelineItemId, String attachmentId) throws IOException {
		Mirror.Timeline.Attachments attachments = getMirror(credential)
				.timeline().attachments();
		Attachment attachmentMetadata = attachments.get(timelineItemId,
				attachmentId).execute();
		return attachmentMetadata.getContentType();
	}

	@Override
	public Location getUserLocation(Credential credential) throws IOException {
		Location location = getMirror(credential).locations().get("latest").execute();
		if(location==null)
			LOG.info("location is null");
		formatLocation(location);
		return location;
	}

	private void formatLocation(Location location) {
		double latitude = location.getLatitude();
		double longtitude = location.getLongitude();
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_UP);
		latitude = Double.parseDouble(df.format(latitude));
		longtitude = Double.parseDouble(df.format(longtitude));
		location.setLatitude(latitude);
		location.setLongitude(longtitude);
	}
}
