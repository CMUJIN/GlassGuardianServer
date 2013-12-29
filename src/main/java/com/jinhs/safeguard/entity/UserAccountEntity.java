package com.jinhs.safeguard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;
@Table(name="USER_ACCOUNT")
@Entity
public class UserAccountEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="ACCESS_TOKEN")
	private String accessToken;

	@Column(name="EXPIRATION_TIME")
	private double expirationTime;
	
	@Column(name="CREATION_DATE",updatable=false, insertable=false)
	private double creationDate;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public double getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(double expirationTime) {
		this.expirationTime = expirationTime;
	}

	public double getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(double creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
