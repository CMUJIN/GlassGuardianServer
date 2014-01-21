package com.jinhs.safeguard.domain.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.jinhs.safeguard.helper.PropertiesFileReader;
import com.jinhs.safeguard.helper.WebRequestHelper;

@RequestMapping("/auth")
@Controller
public class AuthController {
	private static final Logger LOG = Logger.getLogger(AuthController.class.getSimpleName());
	@Autowired
	UserAccountHandler accountHander;
	
	@Autowired
	ConfigureHandler configureHandler;
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView auth(){
		return new ModelAndView("redirect:"+WebRequestHelper.buildGoogleAuthURL());
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public ModelAndView getCode(
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String stateToken)
			throws IOException {
		// cancel auth or CSRF
		if (code == null||stateToken==null||!stateToken.equals(PropertiesFileReader.getStateToken()))
			return new ModelAndView("signin");

		HTTPResponse response = tokenExchangeRequest(code);
		AuthExchangeResponse exchangeResponse = new Gson().fromJson(new String(
				response.getContent()), AuthExchangeResponse.class);
		if (!isValidResponse(exchangeResponse))
			return new ModelAndView("redirect:login");

		String email = getEmailRequest(exchangeResponse);
		if (email == null)
			return new ModelAndView("signin");

		accountHander.signIn(exchangeResponse, email, code);
		List<String> emailList = configureHandler.getAlertEmailList(email);
		ModelAndView model = new ModelAndView("alertemaillist");
		model.addObject("emailList", emailList);
		model.addObject("userId", email);
		return model;
	}

	private boolean isValidResponse(AuthExchangeResponse exchangeResponse) {
		if(exchangeResponse.getAccess_token()!=null&&!exchangeResponse.getAccess_token().equals(""))
			return true;
		else 
			return false;
	}

	private String getEmailRequest(
			AuthExchangeResponse exchangeResponse)
			throws MalformedURLException, IOException {
		HTTPResponse response;
		URL url = new URL(WebURLConfig.RETRIEVE_EMAIL);
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);
		HTTPHeader header = new HTTPHeader("Authorization", "OAuth "+exchangeResponse.getAccess_token());
		request.addHeader(header);
		response = URLFetchServiceFactory.getURLFetchService().fetch(request);
		EmailInfoResponse emailResponse = new Gson().fromJson(new String(response.getContent()), EmailInfoResponse.class);
		if(emailResponse==null||emailResponse.getData()==null)
			return null;
		return emailResponse.getData().getEmail();
	}

	private HTTPResponse tokenExchangeRequest(String code) throws IOException{
			URL url = new URL(WebURLConfig.AUTH_TOKEN_EXCHANGE_URL);
			HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
			String body = WebRequestHelper.buildAuthTokenExchangeData(code);
			request.setPayload(body.getBytes("UTF-8"));
			HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
			return response;
	}
}
