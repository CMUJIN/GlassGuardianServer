package com.jinhs.safeguard.handler;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

@Component
public class ImageHandler {
	private static final Logger LOG = Logger.getLogger(ImageHandler.class.getSimpleName());
	public BlobKey writeImage(byte[] imageBytes) {
		LOG.info("store image file");
		BlobKey blobKey = null;
		if(imageBytes==null)
			return null;
		try {
			FileService fileService = FileServiceFactory.getFileService();

			AppEngineFile file = fileService.createNewBlobFile("image/jpg");

			FileWriteChannel writeChannel = fileService.openWriteChannel(file,
					true);

			int steps = (int) Math.floor(imageBytes.length / 1000);
			int current = 0;
			for (int i = 0; i < 1000; i++) {
				writeChannel.write(ByteBuffer.wrap(Arrays.copyOfRange(
						imageBytes, current, steps + current)));
				current = current + steps;
			}
			writeChannel.write(ByteBuffer.wrap(Arrays.copyOfRange(imageBytes,
					current, imageBytes.length))); // The reason it's cut up
													// like this is because you
													// can't write the complete
													// string in one go.

			writeChannel.closeFinally();

			blobKey = fileService.getBlobKey(file);

		} catch (Exception e) {

		}

		return blobKey;
	}
}