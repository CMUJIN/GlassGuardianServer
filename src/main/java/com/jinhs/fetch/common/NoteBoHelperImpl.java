package com.jinhs.fetch.common;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.mirror.MirrorClient;

@Component
public class NoteBoHelperImpl implements NoteBoHelper {
	private static final Logger LOG = Logger.getLogger(NoteBoHelperImpl.class.getSimpleName());
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	
	private NoteBo populateNoteBo(Notification notification, Credential credential, Location location, int valuation, boolean hasContent) throws IOException {
		LOG.info("Populate Note");
		
		NoteBo noteBo = new NoteBo();
		Mirror mirror = mirrorClient.getMirror(credential);
		
		//location
		noteBo.setAccuracy(location.getAccuracy());
		noteBo.setAddress(location.getAddress());
		noteBo.setDisplay_name(location.getDisplayName());
		noteBo.setLatitude(location.getLatitude());
		noteBo.setLongtitude(location.getLongitude());
		noteBo.setZip_code(geoCodingHelper.getZipCode(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue()));

		noteBo.setUser_id(notification.getUserToken());
		noteBo.setDate(new Date());
		
		noteBo.setValuation(valuation);
		
		TimelineItem timelineItem = mirror.timeline().get(notification.getItemId()) .execute();
		if(hasContent)
			noteBo.setText_note(timelineItem.getText());
		if(hasContent&&timelineItem.getAttachments() != null && timelineItem.getAttachments().size() > 0){
			// Get the first attachment
			String attachmentId = timelineItem.getAttachments().get(0).getId();
			// Get the attachment content
			InputStream stream = mirrorClient.getAttachmentInputStream(credential, timelineItem.getId(), attachmentId);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = stream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			buffer.flush();
			noteBo.setImage_note(buffer.toByteArray());
		}
		
		noteBo.setTimeline_id(timelineItem.getId());
		if(timelineItem.getAttachments()!=null)
			noteBo.setAttachment_id(timelineItem.getAttachments().get(0).getId());
		
		return noteBo;
	}
	
	@Override
	public NoteBo populateNoteBo(Notification notification,
			Credential credential, Location location) throws IOException {
		return populateNoteBo(notification, credential, location, 0, true);
	}

	@Override
	public NoteBo populateLikeNoteBo(Notification notification,
			Credential credential, Location location) throws IOException {
		return populateNoteBo(notification, credential, location, 1, false);
	}

	@Override
	public NoteBo populateDislikeNoteBo(Notification notification,
			Credential credential, Location location) throws IOException {
		return populateNoteBo(notification, credential, location, -1, false);
	}

}
