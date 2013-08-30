package com.jinhs.fetch.bo;

import java.util.Date;



public class NoteBo {
	private String user_id;
	
	private int valuation;
	
	private String text_note;
	
	private byte[] image_note;
	
	private byte[] video_note;
	
	private Date date;
	
	private double latitude;
	
	private double longtitude;
	
	private double accuracy;
	
	private String display_name;
	
	private String address;
	
	private String zip_code;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getValuation() {
		return valuation;
	}

	public void setValuation(int valuation) {
		this.valuation = valuation;
	}

	public String getText_note() {
		return text_note;
	}

	public void setText_note(String text_note) {
		this.text_note = text_note;
	}

	public byte[] getImage_note() {
		return image_note;
	}

	public void setImage_note(byte[] image_note) {
		this.image_note = image_note;
	}

	public byte[] getVideo_note() {
		return video_note;
	}

	public void setVideo_note(byte[] video_note) {
		this.video_note = video_note;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
}
