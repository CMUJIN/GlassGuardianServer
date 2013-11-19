package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
 
public class PictureHandler {
 
	private static Logger logger = Logger.getAnonymousLogger();
	
	public byte[] readImage(String location){
		File file = new File(location);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			Logger.getLogger(PictureHandler.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		
		try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
            	bos.write(buf, 0, readNum); 
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset 
                off to this byte array output stream.*/
            }
        } catch (IOException ex) {
            Logger.getLogger(PictureHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
		return bos.toByteArray();
	}
	
	public void writeImage(byte[] imageBytes, String location, String imageName){
		ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(bis);
		} catch (IOException e) {
			Logger.getLogger(PictureHandler.class.getName()).log(Level.SEVERE, null, e);
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
			Logger.getLogger(PictureHandler.class.getName()).log(Level.SEVERE, null, e);
			return;
		}
		if(formatName==null||bufferedImage==null)
			return;
		Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(bufferedImage, null, null);
        String fileName = createFileName(location, imageName, formatName);
        File imageFile = new File(fileName);
        try {
			ImageIO.write(bufferedImage, formatName, imageFile);
		} catch (IOException e) {
			Logger.getLogger(PictureHandler.class.getName()).log(Level.SEVERE, null, e);
			return;
		}
	}
	
	private String normalizeLocation(String location){
		if(location==null)
			return null;
		if(location.endsWith("\\"))
			return location;
		else
			return location+"\\";
	}
	
	private String createFileName(String location, String imageName, String format){
		StringBuffer sb = new StringBuffer();
		location = normalizeLocation(location);
		sb.append(location);
		sb.append(imageName);
		sb.append(".");
		sb.append(format);
		return sb.toString();
	}
	
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	long startTime = System.currentTimeMillis();
    	
    	
    	PictureHandler ph = new PictureHandler();
    	String inImage = "C:\\Users\\hujin\\Pictures\\test2.jpg";
    	String outImage = "C:\\Users\\hujin\\Pictures\\";
    	String fileName = "output";
    	byte[] imageBytes = ph.readImage(inImage);
    	
    	long readEnd = System.currentTimeMillis();
    	
    	if(imageBytes==null)
    		return;
    	ph.writeImage(imageBytes, outImage, fileName);
    	long endTime   = System.currentTimeMillis();
    	long totalTime = endTime - startTime;
    	
    	long readTime = readEnd - startTime;
    	System.out.println("read:"+readTime);
    	System.out.println("total:"+totalTime);
    	}
}