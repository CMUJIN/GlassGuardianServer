package com.jinhs.fetch.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jinhs.fetch.bo.NoteBo;

@Component
public class DataProcessHelperImpl implements DataProcessHelper {
	private static final Logger LOG = Logger
			.getLogger(DataProcessHelperImpl.class.getSimpleName());

	@Override
	public String populateTextReviews(List<NoteBo> list) {
		StringBuffer sb = new StringBuffer();
		for (NoteBo note : list) {
			if (StringUtil.NotNullorEmpty(note.getText_note())) {
				sb.append(note.getText_note());
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	@Override
	public int populateValuation(List<NoteBo> list) {
		int count = 0;
		int sum = 0;
		for (NoteBo note : list) {
			if (note.getValuation() == 0) {
				continue;
			} else {
				sum++;
				if (note.getValuation() > 0)
					count++;
			}
		}
		if(sum==0)
			return 0;
		LOG.info("count:" + count + " size:" + list.size());
		return (count * 100) / sum;
	}

}
