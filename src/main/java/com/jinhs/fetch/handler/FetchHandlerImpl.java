package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.Location;
import com.google.api.services.mirror.model.Notification;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.gson.Gson;
import com.jinhs.fetch.bo.LocationBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.common.BundleIdProcessHelper;
import com.jinhs.fetch.common.DataProcessHelper;
import com.jinhs.fetch.common.FetchCacheTaskPayload;
import com.jinhs.fetch.common.GeoCodingHelper;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class FetchHandlerImpl implements FetchHandler {
	private static final Logger LOG = Logger.getLogger(FetchHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	DataProcessHelper dataProcessHelper;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	@Autowired
	ZoneRateHandler zoneRateHandler;
	
	@Autowired
	InsertTimelineHandler insertTimelineHandler;
	
	@Override
	public void fetch(Notification notification, Credential credential) throws IOException {
		LOG.info("Fetch operation");
		Location location = mirrorClient.getUserLocation(credential);
		if(location==null){
			LOG.info("Location load failed");
			throw new IOException();
		}
		
		String zipCode = geoCodingHelper.getZipCode(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue());
		// query first group notes
		List<NoteBo> firstGroupNoteListByCoordinate = transService.fetchFirstGroupNotesByCoordinate(
				notification.getUserToken(), location.getLatitude(),
				location.getLongitude(), AppConstants.BUNDLE_SIZE);
		
		boolean hasExistedContent = firstGroupNoteListByCoordinate!=null&&firstGroupNoteListByCoordinate.size()>0;
		String valuationHtml = populateRateHTML(location.getLatitude()
				.doubleValue(), location.getLongitude().doubleValue(), "no address avaliable", zipCode, hasExistedContent);
		
		if(hasExistedContent){
			Date firstFetchDate = new Date();
			String identityKey = BundleIdProcessHelper.generateIdentityKey(firstGroupNoteListByCoordinate.get(0), firstFetchDate);
			addFetchCacheTask(notification, location, firstGroupNoteListByCoordinate, zipCode, identityKey);
			String bundleId = BundleIdProcessHelper.generateBundleId(identityKey, 0);
			insertTimelineHandler.insertBundleTimelines(credential, firstGroupNoteListByCoordinate, bundleId, valuationHtml);
		}
		else{
			insertTimelineHandler.insertEmptyTimeline(credential, zipCode, valuationHtml);
		}
	}

	private void addFetchCacheTask(Notification notification,
			Location location, List<NoteBo> firstGroupNotes, String zipCode, String identityKey) {
		//Add new task to task queue
		if(firstGroupNotes==null)
			return;
		FetchCacheTaskPayload payload = new FetchCacheTaskPayload();
		payload.setFirstGroupNotes(firstGroupNotes);
		payload.setUserToken(notification.getUserToken());
		LocationBo locationBo = new LocationBo();
		locationBo.setAddress(location.getAddress());
		locationBo.setLatitude(location.getLatitude());
		locationBo.setLongitude(location.getLongitude());
		locationBo.setZipCode(zipCode);
		payload.setLocation(locationBo);
		
		payload.setIdentityKey(identityKey);
		
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions task = TaskOptions.Builder.withUrl("/api/fetchcache").method(Method.POST).payload(new Gson().toJson(payload));
		RetryOptions retryOptions = RetryOptions.Builder.withTaskRetryLimit(0);
		task.retryOptions(retryOptions);
		queue.addAsync(task);
	}

	private String populateRateHTML(double latitude, double longtitude, String address, String zip_code, boolean hasExistedNotes) throws IOException {
		int rateByCoordinate = zoneRateHandler.getRateByCoordiate(latitude, longtitude);
		int rateByAddress = zoneRateHandler.getRateByAddress(address);
		int rateByZip = zoneRateHandler.getRateByZip(zip_code);
		
		String coorLikeLevel = getColorLevel(rateByCoordinate);
		String zipLikeLevel = getColorLevel(rateByZip);

		LOG.info("coorLikeLevel:"+coorLikeLevel+" zipLikeLevel"+zipLikeLevel);
		StringBuffer sb = new StringBuffer();
		
		sb.append("<article><figure>");
		sb.append("<img src=\"");
		sb.append("glass://map?w=240&h=360&marker=0;37.3471,-121.9312");
		sb.append("\" height=\"360\" width=\"240\">");
		sb.append("</figure><section><div class=\"text-auto-size\">");
		sb.append("<p class=\"");
		sb.append(coorLikeLevel);
		sb.append("\">");
		sb.append(rateByCoordinate+"% like this place");
		sb.append("</p><p class=\"");
		sb.append(zipLikeLevel);
		sb.append("\">");
		sb.append(rateByZip+"% like this area");
		sb.append("</p>");
		sb.append("</div></section></article>");
		
		return sb.toString();
	}

	private String getColorLevel(int rate) {
		if(rate>=0&&rate<50)
			return "red";
		else if(rate>=50&&rate<80)
			return "yellow";
		else if(rate>=80&&rate<=100)
			return "green";
		else 
			return "white";
	}
}
