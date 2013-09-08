package com.jinhs.fetch.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationProcessHelper {
	public static String getZipCode(String address) {
		Pattern zipPattern = Pattern.compile("(\\d{5})");
		Matcher zipMatcher = zipPattern.matcher(address);
		String zip = "00000";
		while (zipMatcher.find()) {
		    zip = zipMatcher.group(1);
		}
		return zip;
	}
}
