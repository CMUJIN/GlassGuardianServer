package com.jinhs.fetch.resource.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;
import com.google.gson.Gson;
import com.jinhs.fetch.handler.DeleteHandler;
import com.jinhs.fetch.handler.DislikeHandler;
import com.jinhs.fetch.handler.FetchFirstHandler;
import com.jinhs.fetch.handler.FetchHandler;
import com.jinhs.fetch.handler.FetchMoreHandler;
import com.jinhs.fetch.handler.LikeHandler;
import com.jinhs.fetch.handler.PushHandler;
import com.jinhs.fetch.mirror.AuthUtil;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

@RequestMapping("/worker")
@Controller
public class NotificationWorker {
	private static final Logger LOG = Logger.getLogger(NotificationWorker.class
			.getSimpleName());

	@Autowired
	AuthUtil authUtil;

	@Autowired
	FetchHandler fetchHandler;

	@Autowired
	PushHandler pushHandler;

	@Autowired
	LikeHandler likeHandler;

	@Autowired
	DislikeHandler dislikeHandler;

	@Autowired
	DeleteHandler deleteHandler;
	
	@Autowired
	FetchMoreHandler fetchMoreHandler;
	
	@Autowired
	FetchFirstHandler fetchFirstHandler;

	/*
	 * @RequestMapping("/get",method = RequestMethod.GET) public void
	 * processGet() throws IOException { return; }
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void process(@RequestBody String payload, HttpServletResponse httpResponse){
		try {
			httpResponse.getOutputStream().close();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		
		Notification request = new Gson().fromJson(payload, Notification.class);
		Iterator<?> actionMapItr = request.getUserActions().iterator();
		
		Map<String, String> actionInfoMap;
		String actionType = "UNKNOWN";
		String actionPayload = "UNKNOWN";
		if(actionMapItr.hasNext()){
			actionInfoMap = (Map<String, String>)actionMapItr.next();
			actionType = actionInfoMap.get("type");
			actionPayload = actionInfoMap.get("payload");
			Iterator itr = actionInfoMap.keySet().iterator(); 
			while(itr.hasNext()){
				String key = (String) itr.next();
				LOG.info("map key: "+key+" value:"+actionInfoMap.get(key));
			}
		}
		else{
			return;
		}
		// if action type is custom, it will check payload(id), if action is
		// built-in action, only check the type
		// if multiple actions share the same built-in action type, cannot
		// distinguished by payload
		LOG.info("Got a notification with ID:"+request.getItemId());
		// Figure out the impacted user and get their credentials for API calls
		String userId = request.getUserToken();
		Credential credential = null;
		try {
			credential = authUtil.getCredential(userId);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		if (credential == null) {
			LOG.info("authentication failed, user token: "
					+ request.getUserToken());
		}

		// if action type is custom, it will check payload(id), if action is
		// built-in action, only check the type
		// if multiple actions share the same built-in action type, cannot
		// distinguished by payload
		if(actionType.equals(MenuItemActionEnum.REPLY.getValue())){
			try {
				pushHandler.push(request, credential);
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
			return;
		}
		else if(actionType.equals(MenuItemActionEnum.SHARE.getValue())){
			try {
				pushHandler.push(request, credential);
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
			return;
		}
		else if(actionType.equals(MenuItemActionEnum.CUSTOM.getValue())){
			if(actionPayload.equals(CustomActionConfigEnum.FETCH.getName())){
				try {
					fetchHandler.fetch(request, credential);
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				return;
			}
			else if(actionPayload.equals(CustomActionConfigEnum.LIKE.getName())){
				try {
					likeHandler.like(request, credential);
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				return;
			}
			else if(actionPayload.equals(CustomActionConfigEnum.DISLIKE.getName())){
				try {
					dislikeHandler.dislike(request, credential);
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				return;
			}
			else if(actionPayload.equals(CustomActionConfigEnum.FETCH_FIRST.getName())){
				try {
					fetchFirstHandler.fetchFirst(request, credential);
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				return;
			}

			if(actionPayload.startsWith(CustomActionConfigEnum.FETCH_MORE.getName())){
				try {
					fetchMoreHandler.fetchMore(actionPayload, credential);
				} catch (IOException e) {
					LOG.error(e.getMessage());
				}
				return;
			}
			
		}
	}
}
