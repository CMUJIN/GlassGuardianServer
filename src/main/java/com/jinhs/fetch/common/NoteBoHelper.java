package com.jinhs.fetch.common;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.NoteBo;

public interface NoteBoHelper {
	public NoteBo populateNoteBo(Notification notification, Credential credential, Location location) throws IOException;
	public NoteBo populateLikeNoteBo(Notification notification, Credential credential, Location location) throws IOException;
	public NoteBo populateDislikeNoteBo(Notification notification, Credential credential, Location location) throws IOException;
}
