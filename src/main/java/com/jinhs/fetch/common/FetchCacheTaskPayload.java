package com.jinhs.fetch.common;

import java.util.List;

import com.google.api.services.mirror.model.Location;
import com.jinhs.fetch.bo.LocationBo;
import com.jinhs.fetch.bo.NoteBo;

public class FetchCacheTaskPayload {
	private List<NoteBo> firstGroupNotes;
	private LocationBo location;
	private String userToken;

	public List<NoteBo> getFirstGroupNotes() {
		return firstGroupNotes;
	}

	public void setFirstGroupNotes(List<NoteBo> firstGroupNotes) {
		this.firstGroupNotes = firstGroupNotes;
	}

	public LocationBo getLocation() {
		return location;
	}

	public void setLocation(LocationBo location) {
		this.location = location;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
