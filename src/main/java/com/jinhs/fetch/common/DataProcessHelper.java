package com.jinhs.fetch.common;

import java.util.List;

import com.jinhs.fetch.bo.NoteBo;

public interface DataProcessHelper {
	public String populateTextReviews(List<NoteBo> list);
	
	public int populateValuation(List<NoteBo> list);
}
