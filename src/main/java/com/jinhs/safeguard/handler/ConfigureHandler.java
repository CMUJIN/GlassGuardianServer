package com.jinhs.safeguard.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.dao.DBTransService;

@Component
public class ConfigureHandler {
	@Autowired
	DBTransService dbTransService;
	
	public void insertAlertEmail(String userId, String email){
		if(!dbTransService.isEmailExisted(userId, email))
			dbTransService.insertAlertEmail(userId, email);
	}
	
	public void deleteAlertEmail(String userId, String email){
		dbTransService.deleteAlertEmail(userId, email);
	}
	
	public List<String> getAlertEmailList(String userId){
		return dbTransService.getAlertEmail(userId);
	}
}
