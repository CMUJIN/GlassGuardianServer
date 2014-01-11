package com.jinhs.safeguard.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Test {

	public static void main(String[] args) throws IOException {
		 	AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
	        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	        s3.setRegion(usWest2);

	        String bucketName = "guardianstorage";
	        String key = "Icb5mlmWBiLyY68m5Cg39qJ5K8x8bY8cvnR9SzHo";
	        String fileName = "C:\\Users\\jinhs\\Desktop\\Banner.jpg";
	        
	        byte[] byteData = read(new File(fileName));
	        InputStream is = new ByteArrayInputStream(byteData);
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentLength(Long.valueOf(byteData.length));

	        
	        s3.putObject(new PutObjectRequest(bucketName, "fileName3.jpg", is, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
	       // PutObjectRequest o = new PutObjectRequest();
	    
	}
	
	public static byte[] read(File file) throws IOException {

	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while ( (read = ios.read(buffer)) != -1 ) {
	            ous.write(buffer, 0, read);
	        }
	    } finally { 
	        try {
	             if ( ous != null ) 
	                 ous.close();
	        } catch ( IOException e) {
	        }

	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}

}
