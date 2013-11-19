package com.jinhs.safeguard.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.jinhs.safeguard.common.TrackerBO;
import com.jinhs.safeguard.handler.TrackerHandler;

@RequestMapping("/tracking")
@Controller
public class TrackingController {
	private static final Logger LOG = Logger.getLogger(TrackingController.class.getSimpleName());
	 
	@Autowired
	TrackerHandler trackerHandler;
	
	@RequestMapping(method = RequestMethod.POST)
	public void saveTracker(@RequestBody String payload, HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
	
		TrackerBO tracker = new Gson().fromJson(payload, TrackerBO.class);
		trackerHandler.saveTracker(tracker);
	}
}

