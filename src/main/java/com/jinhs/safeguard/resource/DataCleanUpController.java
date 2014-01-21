package com.jinhs.safeguard.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhs.safeguard.handler.DataHandler;

@RequestMapping("/cleanup")
@Controller
public class DataCleanUpController {
	@Autowired
	DataHandler dataHandler;
	
	@RequestMapping(method = RequestMethod.GET)
	public void cleanData(){
		dataHandler.cleanData();
		dataHandler.cleanTrackingLinkData();
	}
}
