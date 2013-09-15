package com.jinhs.fetch.handler;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;

public interface FetchMoreHandler {
	public void fetchMore(String actionPayload, Credential credential) throws IOException;
}
