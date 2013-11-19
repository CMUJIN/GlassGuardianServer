package com.jinhs.safeguard.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.safeguard.common.TrackerBO;
import com.jinhs.safeguard.dao.DBTransService;

@Component
public class TrackerHandler {
	@Autowired
	DBTransService dbTransService;
	
	public void saveTracker(TrackerBO	tracker){
		dbTransService.insertTracker(tracker);
	}
}
