package com.jinhs.safeguard.common;

import java.util.List;

public class AlertConfigInfoBO {
	private String email;
	private List<AlertItem> alertInfo;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<AlertItem> getAlertInfo() {
		return alertInfo;
	}
	public void setAlertInfo(List<AlertItem> alertInfo) {
		this.alertInfo = alertInfo;
	}
}
