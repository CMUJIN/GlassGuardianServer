package com.jinhs.fetch.common;

import java.util.List;

import com.jinhs.fetch.bo.NoteBo;

public interface DataProcessHelper {
	public String populateTextReviews(List<NoteBo> list);
	
	public int populateValuation(List<NoteBo> listByCoordinate, List<NoteBo> listByAddress, List<NoteBo> listByZip);
	
	public int populateValutionByCoordinate(List<NoteBo> listByCoordinate);
	
	public int populateValutionByAddress(List<NoteBo> listByAddress);
	
	public int populateValutionByZip(List<NoteBo> listByZip);
}
