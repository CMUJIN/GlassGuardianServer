package com.jinhs.safeguard.handler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jinhs.safeguard.common.TrackingDataBO;

@Component
public class MediaS3StorageHandler {
	private static final String S3_AMAZON_DOMAIN = "https://s3-us-west-2.amazonaws.com/";
	private static final String S3_BUCKET_NAME = "guardianstorage";

	public String storeImage(TrackingDataBO trackingData){
		if(trackingData.getImage()==null||trackingData.getImage().length==0)
			return null;
		String fileName = "img_"+trackingData.getEmail()+new Date().getTime()+".jpg";
		return storeFile(trackingData.getImage(), S3_BUCKET_NAME, fileName);
	}
	
	public String storeAudio(TrackingDataBO trackingData){
		if(trackingData.getAudio()==null||trackingData.getAudio().length==0)
			return null;
		String fileName = "audio_"+trackingData.getEmail()+new Date().getTime()+".mp3";
		return storeFile(trackingData.getAudio(), S3_BUCKET_NAME, fileName);
	}
	
	private String storeFile(byte[] data, String bucketName, String fileName){
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        InputStream is = new ByteArrayInputStream(data);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(Long.valueOf(data.length));
        
        s3.putObject(new PutObjectRequest(bucketName, fileName, is, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        return S3_AMAZON_DOMAIN+bucketName+"/"+fileName;
	}
}
