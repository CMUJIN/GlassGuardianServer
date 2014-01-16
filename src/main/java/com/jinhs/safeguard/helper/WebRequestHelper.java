package com.jinhs.safeguard.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.jinhs.safeguard.common.WebURLConfig;

public class WebRequestHelper {
	public static String buildGoogleAuthURL(){
	    StringBuffer url = new StringBuffer();
	    url.append(WebURLConfig.GOOGLE_AUTH_URL);
	    url.append("?client_id=");
	    url.append(PropertiesFileReader.getClientId());
	    url.append("&scope=");
	    url.append(WebURLConfig.GOOGLE_AUTH_SCOPE);
	    url.append("&state=");
	    url.append(PropertiesFileReader.getStateToken());
	    url.append("&response_type=code&access_type=online&redirect_uri=");
	    url.append(WebURLConfig.AUTH_CALLBACK_URL);
	    return url.toString();
	}
	
	public static String buildAuthTokenExchangeURL(String code){
		StringBuffer url = new StringBuffer();
		url.append(WebURLConfig.AUTH_TOKEN_EXCHANGE_URL);
		url.append("client_id=");
		url.append(PropertiesFileReader.getClientId());
		url.append("&client_secret=");
		url.append(PropertiesFileReader.getClientSecret());
		url.append("&grant_type=authorization_code&code=");
		url.append(code);
		url.append("&redirect_uri=");
		url.append(WebURLConfig.AUTH_TOKEN_EXHCNAGE_CALLBACK_URL);
		return url.toString();
	}
	
	public static String buildAuthTokenExchangeData(String code){
		StringBuffer data = new StringBuffer();
		data.append("client_id=");
		data.append(PropertiesFileReader.getClientId());
		data.append("&client_secret=");
		data.append(PropertiesFileReader.getClientSecret());
		data.append("&grant_type=authorization_code&code=");
		data.append(code);
		data.append("&redirect_uri=");
		data.append(WebURLConfig.AUTH_CALLBACK_URL);
		return data.toString();
	}
	
	
	
	
}
