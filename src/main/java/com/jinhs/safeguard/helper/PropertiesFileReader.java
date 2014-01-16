package com.jinhs.safeguard.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileReader {
	private static Properties loadAuthPropertiesFile(){
		Properties authProperties = new Properties();;
		try {
			authProperties.load(new FileInputStream("oauth.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authProperties;
	}
	
	public static String getClientId(){
		return loadAuthPropertiesFile().getProperty("client_id");
		//return loadAuthPropertiesFile().getProperty("client_id_local");
	}
	
	public static String getClientSecret(){
		return loadAuthPropertiesFile().getProperty("client_secret");
		//return loadAuthPropertiesFile().getProperty("client_secret_local");
	}
	
	public static String getStateToken(){
		return loadAuthPropertiesFile().getProperty("state_token");
	}
}
