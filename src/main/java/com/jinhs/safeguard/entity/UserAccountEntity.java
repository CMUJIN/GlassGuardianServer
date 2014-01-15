package com.jinhs.safeguard.entity;

import java.util.Date;

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
	
	@Column(name="REFRESH_TOKEN")
	private String refreshToken;

	@Column(name="EXPIRATION_TIME")
	private double expirationTime;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="LAST_MODIFIED_DATE")
	private Date lastModifiedDate;

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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
}
