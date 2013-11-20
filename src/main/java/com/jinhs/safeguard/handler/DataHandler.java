package com.jinhs.safeguard.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.common.DataBO;
import com.jinhs.safeguard.dao.DBTransService;

@Component
public class DataHandler {
	@Autowired
	DBTransService dbTransService;
	
	public void saveData(DataBO	data){
		dbTransService.insertData(data);
	}
}
