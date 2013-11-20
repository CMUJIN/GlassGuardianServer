package com.jinhs.safeguard.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/privacypolicy")
@Controller
public class UserInfoController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String toPrivacyPolicy(){
		return "privacypolicy";
	} 
}
