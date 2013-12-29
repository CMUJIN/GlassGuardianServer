package com.jinhs.safeguard.handler;

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
	
	@Autowired
	AudioHandler audioHandler;
	
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
	
	private String storeAudioFile(TrackingDataBO data) {
		BlobKey blobKey = audioHandler.writeAudio(data.getAudio());
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		String url = imagesService.getServingUrl(blobKey);
		return url;
	}
	
	public List<TrackingDataBO> getData(String userId){
		return dbTransService.getData(userId);
	}
}
