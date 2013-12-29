package com.jinhs.safeguard.handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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

	public void writeImage(byte[] imageBytes, String location, String imageName) {
		ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(bis);
		} catch (IOException e) {
			Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE,
					null, e);
			return;
		}
		Iterator<?> readers = ImageIO.getImageReaders(iis);
		ImageReader reader = (ImageReader) readers.next();
		reader.setInput(iis);
		String formatName;
		BufferedImage bufferedImage;
		try {
			formatName = reader.getFormatName();
			bufferedImage = reader.read(0);
		} catch (IOException e) {
			Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE,
					null, e);
			return;
		}
		if (formatName == null || bufferedImage == null)
			return;
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(bufferedImage, null, null);
		String fileName = createFileName(location, imageName, formatName);
		File imageFile = new File(fileName);
		try {
			ImageIO.write(bufferedImage, formatName, imageFile);
		} catch (IOException e) {
			Logger.getLogger(ImageHandler.class.getName()).log(Level.SEVERE,
					null, e);
			return;
		}
	}

	private String normalizeLocation(String location) {
		if (location == null)
			return null;
		if (location.endsWith("\\"))
			return location;
		else
			return location + "\\";
	}

	private String createFileName(String location, String imageName,
			String format) {
		StringBuffer sb = new StringBuffer();
		location = normalizeLocation(location);
		sb.append(location);
		sb.append(imageName);
		sb.append(".");
		sb.append(format);
		return sb.toString();
	}
}