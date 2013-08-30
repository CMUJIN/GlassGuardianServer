package com.jinhs.fetch.handler;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;

public interface FetchHandler {
	public void fetch(Notification notification, Credential credential) throws IOException;
}
