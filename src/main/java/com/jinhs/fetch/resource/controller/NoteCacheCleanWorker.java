package com.jinhs.fetch.resource.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhs.fetch.handler.NoteCacheCleanHandler;

@RequestMapping("/cleancache")
@Controller
public class NoteCacheCleanWorker {
	private static final Logger LOG = Logger.getLogger(NoteCacheCleanWorker.class.getSimpleName());
	
	@Autowired
	NoteCacheCleanHandler cacheCleanHandler;
	
	@RequestMapping(method = RequestMethod.GET)
	public void process(HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
		LOG.info("NoteCacheCleanWorker");
		cacheCleanHandler.clean();
	}
	
}
