package com.jinhs.fetch.resource.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.UserAction;
import com.jinhs.fetch.handler.DislikeHandler;
import com.jinhs.fetch.handler.FetchHandler;
import com.jinhs.fetch.handler.LikeHandler;
import com.jinhs.fetch.handler.PushHandler;
import com.jinhs.fetch.mirror.AuthUtil;
import com.jinhs.fetch.mirror.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.MenuItemActionEnum;

@RequestMapping("/notify")
@Controller
public class NotificationController {
	private static final Logger LOG = Logger.getLogger(NotificationController.class.getSimpleName());
	
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
	
	@RequestMapping(method = RequestMethod.POST)
	public void getNotification(@RequestBody Notification request, HttpServletResponse httpResponse) throws IOException{
		// Respond with OK and status 200 in a timely fashion to prevent redelivery
		httpResponse.setContentType("text/html");
	    Writer writer = httpResponse.getWriter();
	    writer.append("OK");
	    writer.close();
		//if action type is custom, it will check payload(id), if action is built-in action, only check the type
	    //if multiple actions share the same built-in action type, cannot distinguished by payload
	    LOG.info("Got a notification with ID: " + request.getItemId());
	    // Figure out the impacted user and get their credentials for API calls
	    String userId = request.getUserToken();
	    Credential credential = authUtil.getCredential(userId);
	    if(credential==null){
	    	LOG.info("authentication failed, user token: "+request.getUserToken());
	    }
	    
		// if action type is custom, it will check payload(id), if action is
		// built-in action, only check the type
		// if multiple actions share the same built-in action type, cannot
		// distinguished by payload
		if (request.getUserActions().contains(
				new UserAction().setType(MenuItemActionEnum.CUSTOM.getValue())
						.setPayload(CustomActionConfigEnum.FETCH.getName()))) {
			fetchHandler.fetch(request, credential);
		} else if (request.getUserActions().contains(
				new UserAction().setType(MenuItemActionEnum.REPLY.getValue()))) {
			pushHandler.push(request, credential);
		} else if (request.getUserActions().contains(
				new UserAction().setType(MenuItemActionEnum.CUSTOM.getValue())
						.setPayload(CustomActionConfigEnum.LIKE.getName()))) {
			likeHandler.like(request, credential);
		} else if (request.getUserActions().contains(
				new UserAction().setType(MenuItemActionEnum.CUSTOM.getValue())
						.setPayload(CustomActionConfigEnum.DISLIKE.getName()))) {
			dislikeHandler.dislike(request, credential);
		}

	}
}
