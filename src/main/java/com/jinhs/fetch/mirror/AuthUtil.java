package com.jinhs.fetch.mirror;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.json.jackson.JacksonFactory;

@Component
public class AuthUtil {
//	public static final String GLASS_SCOPE = "https://www.googleapis.com/auth/glass.timeline "
//		      + "https://www.googleapis.com/auth/glass.location "
//		      + "https://www.googleapis.com/auth/userinfo.profile";
	public static final List<String> GLASS_SCOPE = Arrays.asList(
		"https://www.googleapis.com/auth/glass.timeline ",
		"https://www.googleapis.com/auth/glass.location ",
		"https://www.googleapis.com/auth/userinfo.profile"
	);
	
	/*
	 * if set access_type to offline, default one is online, the first time when user grant access to google api
	 * and user exchange authorization with code, there are two tokens return, 
	 * one is access token(one time token), the other is refresh token. The application needs to store
	 * refresh token for future authorization, which will not expired. Next time, exchange access token with refresh token
	 * */
	public static final String ACCESS_TYPE = "offline";
	
	public AuthorizationCodeFlow newAuthorizationCodeFlow() throws IOException {
	    FileInputStream authPropertiesStream = new FileInputStream("oauth.properties");
	    Properties authProperties = new Properties();
	    authProperties.load(authPropertiesStream);

	    String clientId = authProperties.getProperty("client_id");
	    String clientSecret = authProperties.getProperty("client_secret");
	    
//	    String clientId = authProperties.getProperty("client_id_local");
//	    String clientSecret = authProperties.getProperty("client_secret_local");
	    return new GoogleAuthorizationCodeFlow.Builder(new UrlFetchTransport(), new JacksonFactory(),
	        clientId, clientSecret, GLASS_SCOPE).setAccessType(ACCESS_TYPE)
	        .setCredentialStore(new ListableAppEngineCredentialStore()).build();
	  }
	
	/**
	   * Get the current user's ID from the session
	   * 
	   * @return string user id or null if no one is logged in
	   */
	  public String getUserId(HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    return (String) session.getAttribute("userId");
	  }

	  public void setUserId(HttpServletRequest request, String userId) {
	    HttpSession session = request.getSession();
	    session.setAttribute("userId", userId);
	  }

	  public void clearUserId(HttpServletRequest request) throws IOException {
	    // Delete the credential in the credential store
	    String userId = getUserId(request);
	    new ListableAppEngineCredentialStore().delete(userId, getCredential(userId));

	    // Remove their ID from the local session
	    request.getSession().removeAttribute("userId");
	  }

	  public Credential getCredential(String userId) throws IOException {
	    if (userId == null) {
	      return null;
	    } else {
	      return this.newAuthorizationCodeFlow().loadCredential(userId);
	    }
	  }

	  public Credential getCredential(HttpServletRequest req) throws IOException {
	    return this.newAuthorizationCodeFlow().loadCredential(getUserId(req));
	  }

	  public List<String> getAllUserIds() {
	    return new ListableAppEngineCredentialStore().listAllUsers();
	  }
}
