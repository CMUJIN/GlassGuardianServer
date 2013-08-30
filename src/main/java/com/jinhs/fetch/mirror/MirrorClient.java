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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Contact;
import com.google.api.services.mirror.model.ContactsListResponse;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Subscription;
import com.google.api.services.mirror.model.SubscriptionsListResponse;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.TimelineListResponse;

/**
 * A facade for easier access to basic API operations
 * 
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 */
public interface MirrorClient{
  public Mirror getMirror(Credential credential)throws IOException;

  public Contact insertContact(Credential credential, Contact contact)throws IOException;

  public void deleteContact(Credential credential, String contactId)throws IOException;

  public ContactsListResponse listContacts(Credential credential)throws IOException;

  public Contact getContact(Credential credential, String id)throws IOException;

  public TimelineListResponse listItems(Credential credential, long count)throws IOException;

  public Subscription insertSubscription(Credential credential, String callbackUrl,
      String userId, String collection)throws IOException;

  public void deleteSubscription(Credential credential, String id)throws IOException;
  
  public void deleteAllSubscriptions(Credential credential)throws IOException;

  public SubscriptionsListResponse listSubscriptions(Credential credential)throws IOException;

  public TimelineItem insertTimelineItem(Credential credential, TimelineItem item)throws IOException;

  public void insertTimelineItem(Credential credential, TimelineItem item,
      String attachmentContentType, byte[] attachmentData)throws IOException;
  
  public void insertTimelineItem(Credential credential, TimelineItem item,
      String attachmentContentType, InputStream attachmentInputStream)throws IOException;

  public InputStream getAttachmentInputStream(Credential credential, String timelineItemId,
      String attachmentId)throws IOException;

  public String getAttachmentContentType(Credential credential, String timelineItemId,
      String attachmentId)throws IOException;
  
  public Location getUserLocation(Credential credential) throws IOException;
}
