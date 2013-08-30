package com.jinhs.fetch.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class GeoCodingHelperImpl implements GeoCodingHelper {

	@Override
	public String getZipCode(long latitude, long longtitude) throws IOException {
		String url = generateUrl(latitude, longtitude);
		
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        InputStream content = response.getEntity().getContent();
        LocationResult data = new Gson().fromJson(getStringFromInputStream(content), LocationResult.class);
        return getCode(data);
	}

	private String getCode(LocationResult data) {
		for(ResultData result:data.getResults()){
        	for(AddressComponent address: result.getAddress_components()){
        		for(String type:address.getTypes()){
        			if(type.equals("postal_code")){
        				return address.getLong_name();
        			}
        		}
        	}
        }
		return null;
	}

	private String generateUrl(long latitude, long longtitude) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
		sb.append(latitude);
		sb.append(",");
		sb.append(longtitude);
		sb.append("&sensor=false");
		return sb.toString();
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
	
	class LocationResult{
		private List<ResultData> results;

		public List<ResultData> getResults() {
			return results;
		}

		public void setResults(List<ResultData> results) {
			this.results = results;
		}
	}
	class ResultData{
		private List<AddressComponent> address_components;

		public List<AddressComponent> getAddress_components() {
			return address_components;
		}

		public void setAddress_components(List<AddressComponent> address_components) {
			this.address_components = address_components;
		}
	}
	class AddressComponent{
		private String long_name;
		private String short_name;
		private List<String> types;
		public String getLong_name() {
			return long_name;
		}
		public void setLong_name(String long_name) {
			this.long_name = long_name;
		}
		public String getShort_name() {
			return short_name;
		}
		public void setShort_name(String short_name) {
			this.short_name = short_name;
		}
		public List<String> getTypes() {
			return types;
		}
		public void setTypes(List<String>types) {
			this.types = types;
		}
	}

}
