package com.jinhs.safeguard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.appengine.api.datastore.Key;

@Table(name="ALERT_CONFIG")
@Entity
public class AlertConfigEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ALERT_TYPE")
	private int alertType;
	
	@Column(name="ALERT_INFO")
	private String alertInfo;

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

	public int getAlertType() {
		return alertType;
	}

	public void setAlertType(int alertType) {
		this.alertType = alertType;
	}

	public String getAlertInfo() {
		return alertInfo;
	}

	public void setAlertInfo(String alertInfo) {
		this.alertInfo = alertInfo;
	}
}
