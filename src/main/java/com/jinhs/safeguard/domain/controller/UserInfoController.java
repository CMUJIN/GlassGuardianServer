package com.jinhs.safeguard.domain.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhs.safeguard.handler.DataHandler;

@RequestMapping("/trackinginfo")
@Controller
public class UserInfoController {

	@Autowired
	DataHandler dataHandler;

	@RequestMapping(method = RequestMethod.GET)
	public String showUserInfo(ModelMap model) {
		//List<DataBO> data = dataHandler.getData();
		model.addAttribute("email", "dddd");
		model.addAttribute("latitude", "ddd");
		model.addAttribute("longtitude", "long");
		model.addAttribute("date", new Date().toString());
		model.addAttribute("picture","static/images/Desert.jpg");
		return "trackinginfo";
	}
}
