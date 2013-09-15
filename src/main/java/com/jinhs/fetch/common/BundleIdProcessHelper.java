package com.jinhs.fetch.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jinhs.fetch.bo.NoteBo;

public class BundleIdProcessHelper {
	/**
	 * payload sample: FETCHMORE#S3#ID&U21324343243&V3432423&H-343434
	 */
	public static String generateIdentityKey(NoteBo note) {
		StringBuffer sb = new StringBuffer();
		sb.append("&U");
		sb.append(note.getUser_id());
		sb.append("&V");
		sb.append(note.getLatitude());
		sb.append("&H");
		sb.append(note.getLongtitude());
		return sb.toString();
	}

	public static String generateBundleId(String identityKey, int sequenceId) {
		StringBuffer sb = new StringBuffer();
		sb.append("FETCHMORE");
		sb.append("#S");
		sb.append(sequenceId);
		sb.append("#ID");
		sb.append(identityKey);
		return sb.toString();
	}
	

	public static String parseIdentityKey(String actionPayload) {
		Matcher matcher;
		Pattern patternIdentityKey = Pattern.compile("#ID([^<]+?)");
	    matcher = patternIdentityKey.matcher(actionPayload);
	    matcher.find();
		String identityKey = matcher.group(1);
		return identityKey;
	}

	public static int parseSequenceId(String actionPayload) {
		Pattern patternSequenceId = Pattern.compile("#S([^<]+?)#ID");
		Matcher matcher = patternSequenceId.matcher(actionPayload);
		matcher.find();
		String strId = matcher.group(1);
		int sequenceId = Integer.parseInt(strId);
		return sequenceId;
	}
}
