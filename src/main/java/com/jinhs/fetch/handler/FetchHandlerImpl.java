package com.jinhs.fetch.handler;

import java.io.IOException;
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
			addFetchCacheTask(notification, location, firstGroupNoteListByCoordinate, zipCode);
			String identityKey = BundleIdProcessHelper.generateIdentityKey(firstGroupNoteListByCoordinate.get(0));
			String bundleId = BundleIdProcessHelper.generateBundleId(identityKey, 0);
			insertTimelineHandler.insertBundleTimelines(credential, firstGroupNoteListByCoordinate, bundleId, valuationHtml);
		}
		else{
			insertTimelineHandler.insertEmptyTimeline(credential, zipCode, valuationHtml);
		}
	}

	private void addFetchCacheTask(Notification notification,
			Location location, List<NoteBo> firstGroupNotes, String zipCode) {
		//Add new task to task queue
		if(firstGroupNotes==null||firstGroupNotes.size()<AppConstants.BUNDLE_SIZE)
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
		StringBuffer sb = new StringBuffer();
		sb.append("<article class=\"auto-paginate\">");
		sb.append("<ul>");
		if(rateByCoordinate>=0){
			sb.append("<li>");
			sb.append(rateByCoordinate+"% People Like This Point");
			sb.append("</li>");
		}
		if(rateByAddress>=0){
			sb.append("<li>");
			sb.append("rateByAddress+% People Like This Address");
			sb.append("</li>");
		}
		if(rateByZip>=0){
			sb.append("<li>");
			sb.append(rateByAddress+"% People Like This Area");
			sb.append("</li>");
		}
		if(!hasExistedNotes){
			sb.append("<p>");
			sb.append("No meesages avaliable");
			sb.append("</p>");
			sb.append("<p>");
			sb.append("Be the first guy to leave the message");
			sb.append("</p>");
		}
		
		sb.append("</ul>");
		sb.append("</article>");
		return sb.toString();
	}
}
