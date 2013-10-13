package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.BundleIdProcessHelper;

@Component
public class FetchMoreHandlerImpl implements FetchMoreHandler {
	private static final Logger LOG = Logger.getLogger(FetchMoreHandlerImpl.class.getSimpleName());
	
	@Autowired
	FetchCacheHandler fetchCacheHandler;
	
	@Autowired
	InsertTimelineHandler insertTimelineHandler;
	
	@Override
	public void fetchMore(String actionPayload, Credential credential)
			throws IOException {
		
		String identityKey = BundleIdProcessHelper.parseIdentityKey(actionPayload);
		int sequenceId = BundleIdProcessHelper.parseSequenceId(actionPayload)+1;
		LOG.info("FetchMore identity key is:"+identityKey+" sequence id is:"+sequenceId);
		
		List<NoteBo> noteBoList = fetchCacheHandler.getNextGroup(identityKey, sequenceId);
		
		if(noteBoList==null||noteBoList.size()==0){
			insertTimelineHandler.insertNoMoreFetchAvaliable(credential);
			return;
		}
		
		String bundleId = BundleIdProcessHelper.generateBundleId(identityKey, sequenceId);
		insertTimelineHandler.insertBundleTimelines(credential, noteBoList, bundleId, true);
	}
}
