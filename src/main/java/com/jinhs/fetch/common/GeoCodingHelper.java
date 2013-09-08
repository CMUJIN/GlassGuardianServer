package com.jinhs.fetch.common;

import java.io.IOException;

public interface GeoCodingHelper {
	public String getZipCode(double latitude, double longtitude) throws IOException;
}
