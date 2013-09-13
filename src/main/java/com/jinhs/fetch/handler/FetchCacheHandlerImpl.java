package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.NoteBo;
import com.jinhs.fetch.common.AppConstants;
import com.jinhs.fetch.transaction.DBTransService;

public class FetchCacheHandlerImpl implements FetchCacheHandler {

	@Autowired
	DBTransService transService;
	
	
	@Override
	public void insertAndRetrieve(List<NoteBo> noteListByCoordinate,
			List<NoteBo> noteListByAddress, List<NoteBo> noteListByZip)
			throws IOException {
		List<List<CacheNoteBo>> cachePersistList = new ArrayList<List<CacheNoteBo>>();

		LinkedList<CacheNoteBo> tempTotalList = new LinkedList<CacheNoteBo>();
		HashSet<String> set = new HashSet<String>();

		processCacheNoteBoList(noteListByCoordinate, tempTotalList, set);
		processCacheNoteBoList(noteListByAddress, tempTotalList, set);
		processCacheNoteBoList(noteListByZip, tempTotalList, set);

		populateCachePersistList(cachePersistList, tempTotalList);

	}

	@Override
	public List<NoteBo> getNextGroup(String key, int groupId)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	private void populateCachePersistList(
			List<List<CacheNoteBo>> cachePersistList,
			LinkedList<CacheNoteBo> tempTotalList) {
		int startIndex = 0;
		int endIndex = AppConstants.BUNDLE_SIZE;
		while (endIndex <= tempTotalList.size()) {
			List<CacheNoteBo> list = tempTotalList
					.subList(startIndex, endIndex);
			cachePersistList.add(list);
			startIndex += AppConstants.BUNDLE_SIZE;
			endIndex += AppConstants.BUNDLE_SIZE;
		}
		if (startIndex < tempTotalList.size()) {
			List<CacheNoteBo> list = tempTotalList.subList(startIndex,
					tempTotalList.size());
			cachePersistList.add(list);
			startIndex += AppConstants.BUNDLE_SIZE;
			endIndex += AppConstants.BUNDLE_SIZE;
		}
	}

	private void processCacheNoteBoList(List<NoteBo> noteListByCoordinate,
			LinkedList<CacheNoteBo> list, HashSet<String> set) {
		for (NoteBo note : noteListByCoordinate) {
			String key = generateKey(note);
			if (!set.contains(key)) {
				CacheNoteBo cache = new CacheNoteBo();
				cache.setKey(key);
				cache.setNoteBo(note);
				list.add(cache);
				set.add(key);
			}
		}
	}

	private String generateKey(NoteBo note) {
		StringBuffer sb = new StringBuffer();
		sb.append("&U");
		sb.append(note.getUser_id());
		sb.append("&V");
		sb.append(note.getLatitude());
		sb.append("&H");
		sb.append(note.getLongtitude());
		sb.append("&D");
		sb.append(note.getDate());
		return sb.toString();
	}


}
