package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.DataProcessHelper;
import com.jinhs.fetch.common.GeoCodingHelper;
import com.jinhs.fetch.common.LocationBoHelper;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.NotificationLevelEnum;
import com.jinhs.fetch.transaction.DBTransService;

public class FetchHandlerImpl implements FetchHandler {
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	LocationBoHelper locationBoHelper;
	
	@Autowired
	DataProcessHelper dataProcessHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	@Override
	public void fetch(Notification notification, Credential credential) throws IOException {
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null)
			throw new IOException();
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude().longValue(), location.getLongitude().longValue());
		List<NoteBo> noteList =  transService.fetchNotes(notification.getUserToken(), zipCode);
		String text = dataProcessHelper.populateTextReviews(noteList);
		String valuation = dataProcessHelper.populateValuation(noteList)+"% People Like";
		mirrorUtil.populateTimeLine(valuation+" \n"+text, NotificationLevelEnum.Default);
	}

}
