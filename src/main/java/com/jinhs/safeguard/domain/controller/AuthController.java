package com.jinhs.safeguard.domain.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.jinhs.safeguard.common.AuthExchangeResponse;
import com.jinhs.safeguard.common.EmailInfoResponse;
import com.jinhs.safeguard.common.WebURLConfig;
import com.jinhs.safeguard.handler.ConfigureHandler;
import com.jinhs.safeguard.handler.UserAccountHandler;
import com.jinhs.safeguard.helper.WebRequestHelper;

@RequestMapping("auth")
public class AuthController {
	@Autowired
	UserAccountHandler accountHander;
	
	@Autowired
	ConfigureHandler configureHandler;
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView auth(){
		return new ModelAndView("redirect:"+WebRequestHelper.buildGoogleAuthURL());
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public ModelAndView getCode(@RequestParam("code") String code) throws IOException{
		HTTPResponse response = tokenExchangeRequest(code);
		AuthExchangeResponse exchangeResponse = new Gson().fromJson(new String(response.getContent()), AuthExchangeResponse.class);
		
		EmailInfoResponse emailResponse = getEmailRequest(exchangeResponse);
		accountHander.signIn(exchangeResponse, emailResponse.getEmail());
		
		List<String> emailList = configureHandler.getAlertEmailList(emailResponse.getEmail());
		ModelAndView model = new ModelAndView("alertemaillist");
		model.addObject("emailList", emailList);
		model.addObject("userId", emailResponse.getEmail());
		return model;
	}

	private EmailInfoResponse getEmailRequest(
			AuthExchangeResponse exchangeResponse)
			throws MalformedURLException, IOException {
		HTTPResponse response;
		URL url = new URL(WebURLConfig.RETRIEVE_EMAIL);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);
		HTTPHeader header = new HTTPHeader("Authorization", "OAuth "+exchangeResponse.getAccess_token());
		request.addHeader(header);
		response = URLFetchServiceFactory.getURLFetchService().fetch(request);
		EmailInfoResponse emailResponse = new Gson().fromJson(new String(response.getContent()), EmailInfoResponse.class);
		return emailResponse;
	}

	private HTTPResponse tokenExchangeRequest(String code)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		URL url = new URL(WebURLConfig.AUTH_TOKEN_EXCHANGE_URL);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
		String body = WebRequestHelper.buildAuthTokenExchangeData(code);
		request.setPayload(body.getBytes("UTF-8"));
		HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
		return response;
	}
	
	@RequestMapping(value = "/exchange", method = RequestMethod.POST)
	public void getToken(@RequestBody String body){
		
	}
}
