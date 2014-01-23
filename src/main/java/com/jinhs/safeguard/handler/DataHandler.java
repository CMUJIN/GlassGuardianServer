package com.jinhs.safeguard.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.jinhs.safeguard.common.TrackingDataBO;
import com.jinhs.safeguard.dao.DBTransService;

@Component
public class DataHandler {
	@Autowired
	DBTransService dbTransService;
	
	@Autowired
	ImageHandler imageHandler;
	
	public void saveData(TrackingDataBO	data){
		String url = storeImageFile(data);
		data.setImagePath(url);
		dbTransService.insertData(data);
	}

	private String storeImageFile(TrackingDataBO data) {
		BlobKey blobKey = imageHandler.writeImage(data.getImage());
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		String url = imagesService.getServingUrl(blobKey);
		return url;
	}
	
	public List<TrackingDataBO> getData(String userId){
		return dbTransService.getData(userId);
	}
	
	public void cleanData(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -2);
		Date startDate = cal.getTime();
		dbTransService.deleteOldData(startDate);
	}
	
	public void cleanTrackingLinkData(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -2);
		Date startDate = cal.getTime();
		dbTransService.deleteOldTrackingLinkData(startDate);
	}
	
	public String generateAlertLink(String userId){
		return dbTransService.createTrackingLinkSequence(userId);
	}
	
	public String getTrackingUserId(String key){
		return dbTransService.findTrackingLinkUserId(key);
	}
}
