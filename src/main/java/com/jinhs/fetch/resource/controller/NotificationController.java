package com.jinhs.fetch.resource.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.services.mirror.model.Notification;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.gson.Gson;

@RequestMapping("/notify")
@Controller
public class NotificationController {
	private static final Logger LOG = Logger
			.getLogger(NotificationController.class.getSimpleName());

	@RequestMapping(method = RequestMethod.POST)
	public void getNotification(@RequestBody Notification request,
			HttpServletResponse httpResponse) throws IOException {

		Queue queue = QueueFactory.getDefaultQueue();
		String payload2 = new Gson().toJson(request);
		TaskOptions task = TaskOptions.Builder.withUrl("/api/worker").method(Method.POST).payload(payload2);
		queue.addAsync(task);
		
		// Respond with OK and status 200 in a timely fashion to prevent
		// redelivery
		LOG.info("Receive Notification");
		httpResponse.setContentType("text/html");
		Writer writer = httpResponse.getWriter();
		writer.append("OK");
		writer.close();
		return;
	}
}
