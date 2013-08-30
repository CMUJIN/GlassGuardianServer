package com.jinhs.fetch.common;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.LocationBo;

@Component
public class LocationBoHelperImpl implements LocationBoHelper {

	@Override
	public LocationBo populateLocationBo(Notification notification, Credential credential) {
		// TODO Auto-generated method stub
		return null;
	}

}
