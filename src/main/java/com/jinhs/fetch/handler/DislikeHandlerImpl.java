package com.jinhs.fetch.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.jinhs.fetch.common.GeoCodingHelper;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class DislikeHandlerImpl implements DislikeHandler {
	private static final Logger LOG = Logger.getLogger(DislikeHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	ZoneRateHandler zoneRateHandler;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	@Autowired
	InsertTimelineHandler insertTimelineHandler;
	
	@Override
	public void dislike(Notification notification, Credential credential) throws IOException {
		LOG.info("Dislike operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		int isRateBefore = transService.isRateBefore(notification.getUserToken(), location.getLatitude(), location.getLongitude());
		if(isRateBefore==0){//not rated before
			transService.upsertRateRecord(notification.getUserToken(), location.getLatitude(), location.getLongitude(), -1);
			updateZoneRate(location, false, false);
		}
		if(isRateBefore==1){
			transService.upsertRateRecord(notification.getUserToken(), location.getLatitude(), location.getLongitude(), -1);
			updateZoneRate(location, false, true);
		}
		else{
			insertTimelineHandler.insertHasSameRateBefore(credential);
		}
		LOG.info("Dislike Successfully");
	}

	private void updateZoneRate(Location location, boolean isLike, boolean isModifyPreRecord) throws IOException {
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue()).getZip_code();
		zoneRateHandler.updateRateByCoordiate(location.getLatitude(), location.getLongitude(), isLike, isModifyPreRecord);
		zoneRateHandler.updateRateByZip(zipCode, isLike, isModifyPreRecord);
	}

}
