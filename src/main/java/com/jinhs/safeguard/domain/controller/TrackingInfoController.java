package com.jinhs.safeguard.domain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jinhs.safeguard.common.TrackingDataBO;
import com.jinhs.safeguard.handler.DataHandler;

@RequestMapping("/trackinginfo")
@Controller
public class TrackingInfoController{
	@Autowired
	DataHandler dataHandler;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showTrackingPage(@RequestParam("userId") String userId){
		List<TrackingDataBO> dataList = dataHandler.getData(userId);
		ModelAndView model = new ModelAndView("trackinginfo");
		model.addObject("trackDataList", dataList);
		model.addObject("email", userId);
		return model;
	}
	
}
