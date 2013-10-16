package com.jinhs.fetch.handler;

import java.util.List;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.entity.NoteCacheEntity;
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
		List<NoteCacheEntity> deleteList;
		int count = 0;
		do{
			deleteList = tranService.getDeleteCacheEntity(time.toDate()); 
			Log.info("delete cache size: "+deleteList.size());
			for(NoteCacheEntity entity:deleteList)
				tranService.deleteNoteCache(entity);
			count++;
		}
		while(deleteList!=null&&deleteList.size()!=0&&count<AppConstants.MAX_CHACE_QUERY);
	}

}
