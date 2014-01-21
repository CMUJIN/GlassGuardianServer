package com.jinhs.safeguard.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinhs.safeguard.handler.DataHandler;
import com.jinhs.safeguard.handler.EmailHandler;

@RequestMapping("/alert")
@Controller
public class AlertController {
	private static final Logger LOG = Logger.getLogger(AlertController.class.getSimpleName());
	@Autowired
	EmailHandler emailHandler;
	
	@Autowired
	DataHandler dataHandler;
	
	@RequestMapping(method = RequestMethod.GET)
	public void sendAlert(@RequestParam("userId") String userId, HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
		String key = dataHandler.generateAlertLink(userId);
		LOG.info("user id:"+userId+" key:"+key);
		emailHandler.sendEmailGAE(userId, key);
	}
}
