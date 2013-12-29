package com.jinhs.safeguard.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhs.safeguard.handler.EmailHandler;

@RequestMapping("/alert")
@Controller
public class AlertController {
	private static final Logger LOG = Logger.getLogger(AlertController.class.getSimpleName());
	@Autowired
	EmailHandler emailHandler;
	
	@RequestMapping(value = "{userId}",method = RequestMethod.GET)
	public void sendAlert(@PathVariable String userId, HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
		
		LOG.info("user id:"+userId);
		emailHandler.sendEmailGAE(userId);
	}
}
