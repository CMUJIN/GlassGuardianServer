package com.jinhs.fetch.common;

import java.io.IOException;

public interface GeoCodingHelper {
	public LightLocation getZipCode(double latitude, double longtitude) throws IOException;
}
