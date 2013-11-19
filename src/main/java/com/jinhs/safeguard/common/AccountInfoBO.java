package com.jinhs.safeguard.common;

public class AccountInfoBO {
	private UserBO user;
	private AlertConfigInfoBO alertConfig;
	public UserBO getUser() {
		return user;
	}
	public void setUser(UserBO user) {
		this.user = user;
	}
	public AlertConfigInfoBO getAlertConfig() {
		return alertConfig;
	}
	public void setAlertConfig(AlertConfigInfoBO alertConfig) {
		this.alertConfig = alertConfig;
	}
}
