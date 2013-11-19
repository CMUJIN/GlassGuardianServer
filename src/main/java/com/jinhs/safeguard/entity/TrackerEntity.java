package com.jinhs.safeguard.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

@Table(name="TRACKER")
@Entity
public class TrackerEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="SNAPSHOT")
	private byte[] snapshot;
	
	@Column(name="LATITUDE")
	private double latitude;
	
	@Column(name="LONGTITUDE")
	private double longtitude;
	
	@Column(name="DATE")
	private Date date;
	
	@Column(name="GUARD_ID")
	private String guardId;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

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
}
