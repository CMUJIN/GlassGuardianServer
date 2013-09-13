package com.jinhs.fetch.resource.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;

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
		ListIterator<?> list = request.getUserActions().listIterator();
		Iterator<?> list2 = request.getUserActions().iterator();
		
		ListIterator<?> list3 = request.getUserActions().listIterator(0);
		LinkedHashMap<String, String> map = (LinkedHashMap)list2.next();
		
		Iterator itr1 = map.keySet().iterator();
		Iterator itr2 = map.values().iterator();
		LOG.info("list2 size:"+map.get("type"));
		while(itr1.hasNext())
			LOG.info("key"+itr1.next());//return keytype
		while(itr2.hasNext())
			LOG.info("value"+itr2.next());//return 
		String n3 = list3.next().getClass().getSimpleName();
		LOG.info("Receive Notification"+list.next().getClass()+"&"+"&"+n3);
		httpResponse.setContentType("text/html");
		Writer writer = httpResponse.getWriter();
		writer.append("OK");
		writer.close();
		return;
	}
}
