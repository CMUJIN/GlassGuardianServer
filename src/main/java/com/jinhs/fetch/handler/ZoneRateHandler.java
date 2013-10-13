package com.jinhs.fetch.handler;

import java.io.IOException;

public interface ZoneRateHandler {
	public int getRateByCoordiate(double latitude, double longtitude) throws IOException;
	
	public int getRateByAddress(String address) throws IOException;
	
	public int getRateByZip(String zip_code) throws IOException;
	
	public void updateRateByCoordiate(double latitude, double longtitude, boolean isLike, boolean isModifyPreRecord) throws IOException;
	
	public void updateRateByAddress(String address, boolean isLike, boolean isModifyPreRecord) throws IOException;
	
	public void updateRateByZip(String zip_code, boolean isLike, boolean isModifyPreRecord) throws IOException;
}
