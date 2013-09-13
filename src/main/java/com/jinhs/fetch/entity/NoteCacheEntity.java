package com.jinhs.fetch.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.datastore.Key;

@Table(name="NOTES_CACHE")
@Entity
public class NoteCacheEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@NotNull
	@Column(name="IDENTITY_KEY")
	private String identity_key;
	
	@NotNull
	@Column(name="USER_ID")
	private String user_id;
	
	@Column(name="DATE")
	private Date date;
	
	@Column(name="LATITUDE")
	private double latitude;
	
	@Column(name="LONGTITUDE")
	private double longtitude;
	
	@Column(name="TIMELINE_ID")
	private String timeline_id;
	
	@Column(name="SEQUENCE_ID")
	private String sequence_id;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getTimeline_id() {
		return timeline_id;
	}

	public void setTimeline_id(String timeline_id) {
		this.timeline_id = timeline_id;
	}

	public String getSequence_id() {
		return sequence_id;
	}

	public void setSequence_id(String sequence_id) {
		this.sequence_id = sequence_id;
	}

	public String getIdentity_key() {
		return identity_key;
	}

	public void setIdentity_key(String identity_key) {
		this.identity_key = identity_key;
	}
}
