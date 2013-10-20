package com.jinhs.fetch.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class GeoCodingHelperImpl implements GeoCodingHelper {
	private static final Logger LOG = Logger.getLogger(GeoCodingHelperImpl.class.getSimpleName());
	
	@Override
	public LightLocation getZipCode(double latitude, double longtitude) throws IOException {
		URL url = generateUrl(latitude, longtitude);
		LOG.info("geolocation url:"+url.toString());
        LocationResult data = new Gson().fromJson(getStringFromInputStream(url.openStream()), LocationResult.class);
        String zipCode = getCode(data);
        String address = getAddress(data);
        LightLocation location = new LightLocation();
        location.setZip_code(zipCode);
        if(address==null)
        	location.setAddress("");
        else 
        	location.setAddress(address);
        LOG.info("address:"+address);
        return location;
	}
	private String getAddress(LocationResult data) {
		if(data==null)
			return null;
		if(data.getResults()==null||data.getResults().size()==0)
			return null;
		
		return data.getResults().get(0).getFormatted_address();
	}
	private String getCode(LocationResult data) {
		for(ResultData result:data.getResults()){
        	for(AddressComponent address: result.getAddress_components()){
        		for(String type:address.getTypes()){
        			if(type.equals("postal_code")){
        				LOG.info("zipcode:"+address.getLong_name());
        				return address.getLong_name();
        			}
        		}
        	}
        }
		return null;
	}

	private URL generateUrl(double latitude, double longtitude) {
		try {
			URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longtitude+"&sensor=false");
			return url;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
}
