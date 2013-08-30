package com.jinhs.fetch.common;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.LocationBo;

public interface LocationBoHelper {
	public LocationBo populateLocationBo(Notification notification, Credential credential);
}
