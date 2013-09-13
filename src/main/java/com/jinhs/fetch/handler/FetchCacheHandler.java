package com.jinhs.fetch.handler;

import java.io.IOException;
import java.util.List;

import com.jinhs.fetch.bo.NoteBo;

public interface FetchCacheHandler {
	public void insertAndRetrieve(List<NoteBo> noteListByCoordinate, List<NoteBo> noteListByAddress, List<NoteBo> noteListByZip) throws IOException;

	public List<NoteBo> getNextGroup(String key, int groupId) throws IOException;
}
