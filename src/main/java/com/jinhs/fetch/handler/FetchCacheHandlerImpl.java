package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class FetchCacheHandlerImpl implements FetchCacheHandler {
	private static final Logger LOG = Logger.getLogger(FetchCacheHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Override
	public void insert(List<CacheNoteBo> cacheList)
			throws IOException {
		populateCachePersistList(cacheList);
		for(CacheNoteBo cache:cacheList)
			transService.insertCacheNote(cache);
		LOG.info("insert cache");
	}

	@Override
	public List<NoteBo> getNextGroup(String identityKey, int sequenceId)
			throws IOException {
		LOG.info("get next group from cache");
		return transService.fetchNotesFromCache(identityKey, sequenceId);
	}

	private void populateCachePersistList(List<CacheNoteBo> tempTotalList) {
		int sequenceId = 1;
		for (int i = 0; i < tempTotalList.size(); i++) {
			sequenceId = i / AppConstants.BUNDLE_SIZE + 1;
			tempTotalList.get(i).setSequenceId(sequenceId);
		}
	}
}
