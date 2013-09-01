package com.jinhs.fetch.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.NoteBoHelperImpl;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.NotificationLevelEnum;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class DislikeHandlerImpl implements DislikeHandler {
	private static final Logger LOG = Logger.getLogger(DislikeHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	NoteBoHelperImpl noteBoHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Override
	public void dislike(Notification notification, Credential credential) throws IOException {
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null)
			throw new IOException();
		NoteBo noteBo = noteBoHelper.populateLikeNoteBo(notification, credential,location);
		transService.insertNote(noteBo);
		TimelineItem item = mirrorUtil.populateTimeLine("Dislike Successfully", NotificationLevelEnum.Default);
		mirrorClient.insertTimelineItem(credential, item);
		LOG.info("Dislike Successfully");
	}

}
