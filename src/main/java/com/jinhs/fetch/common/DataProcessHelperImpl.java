package com.jinhs.fetch.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jinhs.fetch.bo.NoteBo;

@Component
public class DataProcessHelperImpl implements DataProcessHelper {

	@Override
	public String populateTextReviews(List<NoteBo> list) {
		StringBuffer sb = new StringBuffer();
		for(NoteBo note: list){
			if(StringUtil.NotNullorEmpty(note.getText_note())){
				sb.append(note.getText_note());
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public int populateValuation(List<NoteBo> list) {
		if(list.size()==0)
			return 0;
		int count=0;
		for(NoteBo note:list){
			if(note.getValuation()>0)
				count++;
		}
		return count/list.size();
	}

}
