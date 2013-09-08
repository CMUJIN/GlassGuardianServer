package com.jinhs.fetch.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.mirror.MirrorClient;

@Component
public class DeleteHandlerImpl implements DeleteHandler{
	private static final Logger LOG = Logger.getLogger(DeleteHandlerImpl.class.getSimpleName());
	@Autowired
	MirrorClient mirrorClient;
	
	@Override
	public void delete(Notification notification, Credential credential)
			throws IOException {
		LOG.info("Delete operation");
		mirrorClient.getMirror(credential).timeline().delete(notification.getItemId());
	}

}
