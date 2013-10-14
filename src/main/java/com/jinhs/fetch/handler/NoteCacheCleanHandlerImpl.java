package com.jinhs.fetch.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class NoteCacheCleanHandlerImpl implements NoteCacheCleanHandler {
	private static final Logger LOG = Logger.getLogger(NoteCacheCleanHandlerImpl.class.getSimpleName());
	@Autowired
	DBTransService tranService;
	@Override
	public void clean() {
		DateTime time = new DateTime().minusHours(4);
		
		LOG.info("clean cache before "+time.toDate());
		
		tranService.cleanNoteCache(time.toDate());
	}

}
