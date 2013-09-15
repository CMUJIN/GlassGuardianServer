package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import com.jinhs.fetch.bo.CacheNoteBo;
import com.jinhs.fetch.bo.NoteBo;

public interface FetchCacheHandler {
	public void insert(List<CacheNoteBo> cacheList) throws IOException;

	public List<NoteBo> getNextGroup(String identityKey, int sequenceId) throws IOException;
}
