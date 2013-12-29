package com.jinhs.safeguard.handler;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

@Component
public class AudioHandler {
	private static final Logger LOG = Logger.getLogger(AudioHandler.class.getSimpleName());
	public BlobKey writeAudio(byte[] audioBytes) {
		LOG.info("store audio file");
		BlobKey blobKey = null;

		try {
			FileService fileService = FileServiceFactory.getFileService();

			AppEngineFile file = fileService.createNewBlobFile("audio/mp4");

			FileWriteChannel writeChannel = fileService.openWriteChannel(file,
					true);

			int steps = (int) Math.floor(audioBytes.length / 1000);
			int current = 0;
			for (int i = 0; i < 1000; i++) {
				writeChannel.write(ByteBuffer.wrap(Arrays.copyOfRange(
						audioBytes, current, steps + current)));
				current = current + steps;
			}
			writeChannel.write(ByteBuffer.wrap(Arrays.copyOfRange(audioBytes,
					current, audioBytes.length)));
			
			writeChannel.closeFinally();

			blobKey = fileService.getBlobKey(file);

		} catch (Exception e) {
			LOG.info(e.getMessage());
		}

		return blobKey;
	}

}
