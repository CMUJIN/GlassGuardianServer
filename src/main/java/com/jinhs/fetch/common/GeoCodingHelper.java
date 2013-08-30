package com.jinhs.fetch.common;

import java.io.IOException;

public interface GeoCodingHelper {
	public String getZipCode(long latitude, long longtitude) throws IOException;
}
