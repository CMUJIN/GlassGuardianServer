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
	public int populateValuation(List<NoteBo> listByCoordinate, List<NoteBo> listByAddress, List<NoteBo> listByZip) {
		int count = 0;
		int sum = 0;
		
		for (NoteBo note : listByCoordinate) {
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
		LOG.info("count:" + count + " size:" + listByCoordinate.size());
		return (count * 100) / sum;
	}

	@Override
	public int populateValutionByCoordinate(List<NoteBo> listByCoordinate) {
		if(listByCoordinate==null)
			return -1;
		
		int count = 0;
		int sum = 0;
		
		for (NoteBo note : listByCoordinate) {
			if (note.getValuation() == 0) {
				continue;
			} else {
				sum++;
				if (note.getValuation() > 0)
					count++;
			}
		}
		if(sum==0)
			return -1;
		LOG.info("count:" + count + " size:" + listByCoordinate.size());
		return (count * 100) / sum;
	}

	@Override
	public int populateValutionByAddress(List<NoteBo> listByAddress) {
		if(listByAddress==null)
			return -1;
		
		int count = 0;
		int sum = 0;
		
		for (NoteBo note : listByAddress) {
			if (note.getValuation() == 0) {
				continue;
			} else {
				sum++;
				if (note.getValuation() > 0)
					count++;
			}
		}
		if(sum==0)
			return -1;
		LOG.info("count:" + count + " size:" + listByAddress.size());
		return (count * 100) / sum;
	}

	@Override
	public int populateValutionByZip(List<NoteBo> listByZip) {
		if(listByZip==null)
			return -1;
		
		int count = 0;
		int sum = 0;
		
		for (NoteBo note : listByZip) {
			if (note.getValuation() == 0) {
				continue;
			} else {
				sum++;
				if (note.getValuation() > 0)
					count++;
			}
		}
		if(sum==0)
			return -1;
		LOG.info("count:" + count + " size:" + listByZip.size());
		return (count * 100) / sum;
	}

}
