package com.jinhs.safeguard.common;

public class WebURLConfig {
	private WebURLConfig(){};
	public static String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	public static String AUTH_CALLBACK_URL = "https://jinhsglassguard.appspot.com/view/auth/callback";
	//public static String AUTH_CALLBACK_URL = "http://localhost:8080/view/auth/callback";
	public static String AUTH_TOKEN_EXCHANGE_URL = "https://accounts.google.com/o/oauth2/token";
	public static String AUTH_TOKEN_EXHCNAGE_CALLBACK_URL = "https://jinhsglassguard.appspot.com/view/auth/exchange";
	public static String GOOGLE_AUTH_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
	public static String RETRIEVE_EMAIL = "https://www.googleapis.com/userinfo/email?alt=json";
}
