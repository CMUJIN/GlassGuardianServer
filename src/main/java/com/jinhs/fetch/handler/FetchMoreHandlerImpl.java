package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.TimelineItem;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.BundleIdProcessHelper;
import com.jinhs.fetch.mirror.MirrorClient;
import com.jinhs.fetch.mirror.MirrorUtil;
import com.jinhs.fetch.mirror.TimelinePopulateHelper;
import com.jinhs.fetch.mirror.enums.CustomActionConfigEnum;
import com.jinhs.fetch.mirror.enums.MenuItemActionEnum;

@Component
public class FetchMoreHandlerImpl implements FetchMoreHandler {
	private static final Logger LOG = Logger.getLogger(FetchMoreHandlerImpl.class.getSimpleName());
	
	@Autowired
	FetchCacheHandler fetchCacheHandler;
	
	@Autowired
	MirrorClient mirrorClient;
	
	@Autowired
	MirrorUtil mirrorUtil;
	
	@Override
	public void fetchMore(String actionPayload, Credential credential)
			throws IOException {
		
		String identityKey = BundleIdProcessHelper.parseIdentityKey(actionPayload);
		int sequenceId = BundleIdProcessHelper.parseSequenceId(actionPayload)+1;
		LOG.info("FetchMore identity key is:"+identityKey+" sequence id is:"+sequenceId);
		
		List<NoteBo> noteBoList = fetchCacheHandler.getNextGroup(identityKey, sequenceId);
		
		if(noteBoList==null||noteBoList.size()==0){
			List<MenuItem> actionList = new ArrayList<MenuItem>();
			TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
			String text = "No more notes avaliable";
			TimelineItem timelineItem = mirrorUtil.populateTimeLine(text, actionList);
			mirrorClient.insertTimelineItem(credential, timelineItem);
			return;
		}
		
		String bundleId = BundleIdProcessHelper.generateBundleId(identityKey, sequenceId);
		List<MenuItem> actionList = new ArrayList<MenuItem>();
		TimelinePopulateHelper.addMenuItem(actionList, MenuItemActionEnum.DELETE);
		TimelinePopulateHelper.addCustomMenuItemWithPayload(actionList, CustomActionConfigEnum.FETCH_MORE, bundleId);
		
		for (TimelineItem timelineItem : TimelinePopulateHelper
				.populateBundleNotes(noteBoList,
						mirrorClient.getMirror(credential), sequenceId)) {
			timelineItem.setMenuItems(actionList);
			mirrorClient.insertTimelineItem(credential, timelineItem);
		}
	}
}
