package com.jinhs.fetch.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/privacypolicy")
@Controller
public class PrivacyPolicyController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String toPrivacyPolicy(){
		return "privacypolicy";
	} 
}
