package com.jinhs.fetch.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.NoteBoHelperImpl;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.transaction.DBTransService;

public class LikeHandlerImpl implements LikeHandler{
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	NoteBoHelperImpl noteBoHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Override
	public void like(Notification notification, Credential credential) throws IOException {
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null)
			throw new IOException();
		NoteBo noteBo = noteBoHelper.populateDislikeNoteBo(notification, credential, location);
		transService.insertNote(noteBo);
		
	}

}
