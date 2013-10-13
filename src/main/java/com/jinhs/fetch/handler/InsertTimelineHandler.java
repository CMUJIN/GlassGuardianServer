package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.jinhs.fetch.bo.NoteBo;

public interface InsertTimelineHandler {
	public void insertBundleTimelines(Credential credential,
			List<NoteBo> noteBoList, String bundleId, boolean isFirstAsCover) throws IOException;

	public void insertNoMoreFetchAvaliable(Credential credential)
			throws IOException;

	public void insertFetchFirst(Credential credential, NoteBo firstNote) throws IOException;

	public void insertNoFirstNoteAvaliable(Credential credential)
			throws IOException;
	
	public void insertHasSameRateBefore(Credential credential)
			throws IOException;

	public void insertEmptyTimeline(Credential credential, String zipCode,
			String valuationHtml) throws IOException;

	public void insertBundleTimelines(Credential credential,
			List<NoteBo> noteBoList, String bundleId, String valuationHtml)
			throws IOException;
}
