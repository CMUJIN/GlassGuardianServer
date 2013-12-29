package com.jinhs.safeguard.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/hello")
@Controller
public class TestJspController{
	/*@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
 
		ModelAndView model = new ModelAndView("hello");
		model.addObject("msg", "hello world");
 
		return model;
	}*/
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView helloWorld(){
 
		ModelAndView model = new ModelAndView("hello");
		model.addObject("msg", "hello world");
 
		return model;
	}
	
}
