package com.jinhs.fetch.handler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhs.fetch.bo.ZoneRateBo;
import com.jinhs.fetch.transaction.DBTransService;

@Component
public class ZoneRateHandlerImpl implements ZoneRateHandler {
	private static final Logger LOG = Logger.getLogger(ZoneRateHandlerImpl.class.getSimpleName());
	
	@Autowired
	DBTransService transService;
	
	@Override
	public int getRateByCoordiate(double latitude, double longtitude)
			throws IOException {
		ZoneRateBo rate = transService.getRateByCoordinate(latitude, longtitude);
		return calculateRate(rate);
	}

	@Override
	public int getRateByAddress(String address) throws IOException {
		ZoneRateBo rate = transService.getRateByAddress(address);
		return calculateRate(rate);
	}

	@Override
	public int getRateByZip(String zip_code) throws IOException {
		ZoneRateBo rate = transService.getRateByZip(zip_code);
		return calculateRate(rate);
	}

	@Override
	public void updateRateByCoordiate(double latitude, double longtitude, boolean isLike)
			throws IOException {
		transService.updateRateByCoordinate(latitude, longtitude, isLike);
	}

	@Override
	public void updateRateByAddress(String address, boolean isLike) throws IOException {
		transService.updateRateByAddress(address, isLike);
	}

	@Override
	public void updateRateByZip(String zip_code, boolean isLike) throws IOException {
		transService.updateRateByZip(zip_code, isLike);
	}

	

	private int calculateRate(ZoneRateBo rate) {
		if(rate==null)
			return -1;
		long like = rate.getLike_hit();
		long dislike = rate.getDislike_hit();
		if(like==0&&dislike==00)
			return -1;
		return (int)(like*100/(like+dislike));
	}
}
