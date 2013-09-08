package com.jinhs.fetch.common;


public class WebUtil {
	private static final String LOCALHOST = "http://localhost:8080/view";
	private static final String REMOTEHOST = "http://glassfetch.appspot.com/view";
	private static final String RESOURCEHOST = "https://glassfetch.appspot.com/";
	private static final String HOST = REMOTEHOST;
	//private static final String GOOGLE_PROXY_SUBSCRIPTION_SERVER = "https://mirrornotifications.appspot.com/forward?url=";
	
	public static String buildOAuthCallBackUrl(){
		return HOST+"/auth";
	}
	
	public static String buildSubscribeCallBackUrl(){
		return HOST+"/subscribecallback";
	}
	
	public static String buildNotifyCallBackUrl(){
		return "https://glassfetch.appspot.com/api/notify";
	}
	
	public static String buildWelcomeImageUrl(){
		return RESOURCEHOST+"/static/images/chipotle-tube-640x360.jpg";
	}
	
	public static String buildContactImageUrl(){
		return RESOURCEHOST+"/static/images/chipotle-tube-640x360.jpg";
	}
	
	public static String buildNotifyWorkerUrl(){
		return "http://localhost:8080/api/worker";
	}
}
