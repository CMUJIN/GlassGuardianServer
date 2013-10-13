package com.jinhs.fetch.resource.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.LocationBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.FetchCacheTaskPayload;
import com.jinhs.fetch.common.GeoCodingHelper;
import com.jinhs.fetch.handler.FetchCacheHandler;
import com.jinhs.fetch.transaction.DBTransService;

@RequestMapping("/fetchcache")
@Controller
public class FetchCacheWorker {
	private static final Logger LOG = Logger.getLogger(FetchCacheWorker.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Autowired
	GeoCodingHelper geoCodingHelper;
	
	@Autowired
	FetchCacheHandler fetchCacheHanler;
	
	@RequestMapping(method = RequestMethod.POST)
	public void process(@RequestBody String payload, HttpServletResponse httpResponse) throws IOException {
		httpResponse.getOutputStream().close();
		
		LOG.info("fetchCache");
		
		FetchCacheTaskPayload taskPayload =  new Gson().fromJson(payload, FetchCacheTaskPayload.class);
		List<NoteBo> firstGroupList = taskPayload.getFirstGroupNotes();
		LocationBo location = taskPayload.getLocation();
		String identityKey = taskPayload.getIdentityKey();
		
		List<NoteBo> noteListByCoordinate = null;
		List<NoteBo> noteListByAddress = null;
		List<NoteBo> noteListByZip = null;
		noteListByCoordinate = transService.fetchNotesByCoordinate(location.getLatitude(), location.getLongitude());

		if(location.getAddress()!=null)
			noteListByAddress = transService.fetchNotesByAddress(location.getAddress());
		
		if(location.getZipCode()!=null)
			noteListByZip =  transService.fetchNotesByZip(location.getZipCode());
		
		// insert note into cache today for fetch more operation
		LinkedList<CacheNoteBo> cacheList = populateCacheList(firstGroupList,
				noteListByCoordinate, noteListByAddress, noteListByZip, identityKey);
		fetchCacheHanler.insert(cacheList);
	}
	
	private LinkedList<CacheNoteBo> populateCacheList(List<NoteBo> firstGroupList,
			List<NoteBo> noteListByCoordinate, List<NoteBo> noteListByAddress,
			List<NoteBo> noteListByZip, String identityKey) {
		LinkedList<CacheNoteBo> cacheList = new LinkedList<CacheNoteBo>();
		HashSet<String> timelineSet = new HashSet<String>();
		for(NoteBo note:firstGroupList)
			timelineSet.add(note.getTimeline_id());
		
		processCacheNoteBoList(noteListByCoordinate, cacheList, timelineSet, identityKey);
		processCacheNoteBoList(noteListByAddress, cacheList, timelineSet, identityKey);
		processCacheNoteBoList(noteListByZip, cacheList, timelineSet, identityKey);
		return cacheList;
	}

	private void processCacheNoteBoList(List<NoteBo> noteList,
			LinkedList<CacheNoteBo> cacheList, HashSet<String> set, String identityKey) {
		if(noteList==null||noteList.size()==0)
			return;
		for (NoteBo note : noteList) {
			if (!set.contains(note.getTimeline_id())) {
				CacheNoteBo cache = new CacheNoteBo();
				cache.setIdentity_key(identityKey);
				cache.setNoteBo(note);
				cacheList.add(cache);
				set.add(identityKey);
			}
		}
	}
}
