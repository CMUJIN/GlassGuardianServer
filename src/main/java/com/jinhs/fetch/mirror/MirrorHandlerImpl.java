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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.jinhs.fetch.common.WebUtil;

@Component
public class MirrorHandlerImpl implements MirrorHandler {
	private static final Logger LOG = Logger.getLogger(MirrorHandlerImpl.class.getSimpleName());
	
	@Autowired
	AuthUtil authUtil;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Override
	public void insertSubscription(String userId, String collection) throws IOException {

		Credential credential = authUtil.newAuthorizationCodeFlow().loadCredential(userId);
		// subscribe (only works deployed to production)
		try {
			mirrorClient.insertSubscription(credential,
					WebUtil.buildSubscribeCallBackUrl(), userId, collection);
		} catch (GoogleJsonResponseException e) {
			LOG.warn("Could not subscribe "
					+ WebUtil.buildSubscribeCallBackUrl() + " because "
					+ e.getDetails().toPrettyString());
			throw new IOException();
		}
	}

	@Override
	public void deleteSubscription(String userId, String subscriptionId) throws IOException{
		Credential credential = authUtil.newAuthorizationCodeFlow()
				.loadCredential(userId); 
		// subscribe (only works deployed to production)
		mirrorClient.deleteSubscription(credential, subscriptionId);
	}

	@Override
	public void insertItem(String userId) throws IOException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertItemWithAction(String userId) throws IOException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertContact(String userId) throws IOException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteContact(String userId) throws IOException{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertItemAllUsers(String userId) throws IOException{
		// TODO Auto-generated method stub
		
	}
	
}
