package com.jinhs.fetch.common;

import com.google.api.services.mirror.model.Location;

public class BundleIDHelper {
	public static String generateBundleID(String userId, Location location, long sequenceId){
		StringBuffer sb = new StringBuffer();
		sb.append(userId);
		sb.append("|");
		sb.append(location.getId());
		sb.append("|");
		sb.append(sequenceId);
		return sb.toString();
	}
	
	public static String getUserId(String bundleId){
		return bundleId.split("|")[0];
	}
	
	public static String getLocationId(String bundleId){
		return bundleId.split("|")[1];
	}
}
