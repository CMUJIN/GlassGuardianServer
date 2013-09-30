package com.jinhs.fetch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

@Table(name="ZONE_RATE")
@Entity
public class ZoneRateEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Column(name="LATITUDE")
	private double latitude;
	
	@Column(name="LONGTITUDE")
	private double longtitude;
	
	@Column(name="ADDRESS")
	private String address;

	@Column(name="ZIP_CODE")
	private String zip_code;
	
	@Column(name="LIKE_HIT")
	private long like_hit;
	
	@Column(name="DISLIKE_HIT")
	private long dislike_hit;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

	public long getLike_hit() {
		return like_hit;
	}

	public void setLike_hit(long like_hit) {
		this.like_hit = like_hit;
	}

	public long getDislike_hit() {
		return dislike_hit;
	}

	public void setDislike_hit(long dislike_hit) {
		this.dislike_hit = dislike_hit;
	}

}
