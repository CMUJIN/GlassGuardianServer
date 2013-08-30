package com.jinhs.fetch.common;

public class StringUtil {
	public static boolean NotNullorEmpty(String str){
		if(str==null)
			return false;
		if(str.trim().equals(""))
			return false;
		return true;
	}
}
