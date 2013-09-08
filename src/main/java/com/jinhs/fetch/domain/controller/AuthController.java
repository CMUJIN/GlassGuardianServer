package com.jinhs.fetch.domain.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.jinhs.fetch.common.WebUtil;
import com.jinhs.fetch.mirror.AuthUtil;
import com.jinhs.fetch.mirror.NewUserBootstrapper;

@RequestMapping("/auth")
@Controller
public class AuthController {
	private static final Logger LOG = Logger.getLogger(AuthController.class.getSimpleName());
	 
	@Autowired
	AuthUtil authUtil;
	
	@Autowired
	NewUserBootstrapper newUserBootstrapper;
	 
	@RequestMapping(method=RequestMethod.GET)
	public String auth(@RequestParam(value = "code", required = false) String code, Model uiModel) throws IOException {
		// If we have a code, finish the OAuth 2.0 dance
		if (code != null) {
			LOG.info("Got a code. Attempting to exchange for access token.");

			AuthorizationCodeFlow flow = authUtil.newAuthorizationCodeFlow();
			TokenResponse tokenResponse = flow.newTokenRequest(code)
					.setRedirectUri(WebUtil.buildOAuthCallBackUrl()).execute();

			// Extract the Google User ID from the ID token in the auth response
			String userId = ((GoogleTokenResponse) tokenResponse)
					.parseIdToken().getPayload().getUserId();
			// String userId = "";
			LOG.info("Code exchange worked. User " + userId + " logged in.");

			// Set it into the session
			// AuthUtil.setUserId(req, userId);
			flow.createAndStoreCredential(tokenResponse, userId);

			// The dance is done. Do our bootstrapping stuff for this user
			newUserBootstrapper.bootstrapNewUser(userId);

			// Redirect back to index
			return "authsuccess";
		}

		// Else, we have a new flow. Initiate a new flow.
		LOG.info("No auth context found. Kicking off a new auth flow.");

		AuthorizationCodeFlow flow = authUtil.newAuthorizationCodeFlow();
		GenericUrl url = flow.newAuthorizationUrl().setRedirectUri(
				WebUtil.buildOAuthCallBackUrl());
		url.set("approval_prompt", "force");
		return "redirect:" + url.build();
	}
	}

