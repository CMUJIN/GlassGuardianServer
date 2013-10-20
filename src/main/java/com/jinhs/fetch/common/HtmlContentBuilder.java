package com.jinhs.fetch.common;

import java.io.IOException;

import org.apache.log4j.Logger;

public class HtmlContentBuilder {
	private static final Logger LOG = Logger.getLogger(HtmlContentBuilder.class.getSimpleName());
	
	public static String populateRateHTML(double latitude, double longtitude, String address, int rateByCoordinate, int rateByAddress, int rateByZip) throws IOException {
		String coorLikeLevel = getColorLevel(rateByCoordinate);
		String zipLikeLevel = getColorLevel(rateByZip);

		LOG.info("coorLikeLevel:"+coorLikeLevel+" zipLikeLevel"+zipLikeLevel);
		StringBuffer sb = new StringBuffer();
		
		sb.append("<article><figure>");
		sb.append("<img src=\"");
		sb.append("glass://map?w=240&h=360&marker=0;"+latitude+","+longtitude+"");
		sb.append("\" height=\"360\" width=\"240\">");
		sb.append("</figure><section>");
		
		sb.append("<div class=\"text-normal\">");
		
		if(rateByCoordinate<0&&rateByZip<0){
			sb.append("<p>No Rate Avaliable</p>");
		}
		else{
			sb.append("<p class=\"");
			sb.append(coorLikeLevel);
			sb.append("\">");
			sb.append("Place:"+rateByCoordinate+"% like");
			sb.append("</p><p class=\"");
			sb.append(zipLikeLevel);
			sb.append("\">");
			sb.append("Area:"+rateByZip+"% like");
			sb.append("</p>");
		}
		sb.append("</div>");
		sb.append(" <hr><div><p class=\"text-minor\">");
		sb.append(address);
		sb.append("</p></div>");
		sb.append("</div></section></article>");
		
		return sb.toString();
	}

	private static String getColorLevel(int rate) {
		if(rate>=0&&rate<50)
			return "red";
		else if(rate>=50&&rate<80)
			return "yellow";
		else if(rate>=80&&rate<=100)
			return "green";
		else 
			return "white";
	}
	
	public static String populateNoFirstHTML(double latitude, double longtitude){
		StringBuffer sb = new StringBuffer();
		sb.append("<article><figure>");
		sb.append("<img src=\"");
		sb.append("glass://map?w=240&h=360&marker=0;"+latitude+","+longtitude+"");
		sb.append("\" height=\"360\" width=\"240\">");
		sb.append("</figure><section><div class=\"text-auto-size\">");
		sb.append("<p>");
		sb.append("No message so far");
		sb.append("</p>");
		sb.append("<p class=\"yellow\">");
		sb.append("Be the first person to leave a footprint");
		sb.append("</p>");
		sb.append("</div></section></article>");
		return sb.toString();
	}
	
	public static String populateDefaultTextHTML(String text){
		StringBuffer sb = new StringBuffer();
		sb.append("<article>\n  <figure>\n");
		sb.append("<img src=\"http://glassfetch.appspot.com/static/images/tile360x240.png\" height=\"360\" width=\"240\">\n");
		sb.append("</figure>\n  <section>\n   ");  
		sb.append("<p class=\"text-auto-size\">\n  ");
		sb.append(text);
		sb.append(" </p>\n</section>\n</article>\n");
		return sb.toString();
	}
}
