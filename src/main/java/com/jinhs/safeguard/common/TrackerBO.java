package com.jinhs.safeguard.common;

import java.util.Date;

public class TrackerBO {
	private String email;
	private byte[] snapshot;
	private double latitude;
	private double longtitude;
	private Date date;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte[] getSnapshot() {
		return snapshot;
	}
	public void setSnapshot(byte[] snapshot) {
		this.snapshot = snapshot;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getGuardId() {
		return guardId;
	}
	public void setGuardId(String guardId) {
		this.guardId = guardId;
	}
	private String guardId;
}
