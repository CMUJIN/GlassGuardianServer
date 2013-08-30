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
import com.jinhs.fetch.mirror.CustomMenuItemActionEnum;
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
		
	    LOG.info("Got a notification with ID: " + request.getItemId());
	    // Figure out the impacted user and get their credentials for API calls
	    String userId = request.getUserToken();
	    Credential credential = authUtil.getCredential(userId);
	    if(credential==null){
	    	LOG.info("authentication failed, user token: "+request.getUserToken());
	    }
	    UserAction action = request.getUserActions().get(0);
	    if(action.getPayload()!=null){
	    	LOG.info("user action is "+action.getPayload());
	    	if(action.getPayload().equals(CustomMenuItemActionEnum.FETCH.getValue())){
	    		fetchHandler.fetch(request, credential);
	    	}
	    	else if(action.getPayload().equals(CustomMenuItemActionEnum.PUSH.getValue())){
	    		pushHandler.push(request, credential);
	    	}
	    	else if(action.getPayload().equals(CustomMenuItemActionEnum.LIKE.getValue())){
	    		likeHandler.like(request, credential);
	    	}
	    	else if(action.getPayload().equals(CustomMenuItemActionEnum.DISLIKE.getValue())){
	    		dislikeHandler.dislike(request, credential);
	    	}
	    }
	    else{
	    	LOG.info("user action is "+action.getType());
	    	if(action.getType().equals(MenuItemActionEnum.SHARE.getValue())){
	    		pushHandler.push(request, credential);
	    	}
	    }
	}
}
