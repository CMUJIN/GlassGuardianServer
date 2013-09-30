package com.jinhs.fetch.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.NoteBoHelperImpl;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class PushHandlerImpl implements PushHandler {
	private static final Logger LOG = Logger.getLogger(PushHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	NoteBoHelperImpl noteBoHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Override
	public void push(Notification notification, Credential credential) throws IOException {
		LOG.info("Push operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		NoteBo noteBo = noteBoHelper.populateNoteBo(notification, credential, location);
		transService.insertNote(noteBo);
		LOG.info("push successfully");
	}

}
