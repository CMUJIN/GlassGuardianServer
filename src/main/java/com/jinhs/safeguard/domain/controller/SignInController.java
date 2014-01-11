package com.jinhs.safeguard.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("signin.html")
@Controller
public class SignInController {
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView signIn(){
		ModelAndView model = new ModelAndView("signin");
		return model;
	}
}
