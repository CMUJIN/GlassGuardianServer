package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Decoder.BASE64Decoder;

import com.google.gson.Gson;

import common.ParseConfigEnum;

public class test {
	public static void main(String args[]){
		test t = new test();
		try {
			t.callParse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void callParse() throws IOException{
		String objectName = "image";
		URL url = getUrl(objectName);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("X-Parse-Application-Id", ParseConfigEnum.ApplicationID.value);
		con.setRequestProperty("X-Parse-REST-API-Key", ParseConfigEnum.RestApiKey.value);
		
		String responseCode = getStringFromInputStream(con.getInputStream());
		//System.out.println(getStringFromInputStream(con.getInputStream()));
		ParseResponse results = new Gson().fromJson(responseCode, ParseResponse.class);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] imageData = decoder.decodeBuffer(results.getResults().get(0).getData().get(0).getBase64());
		PictureHandler p = new PictureHandler();
		String outImage = "C:\\Users\\hujin\\Pictures\\";
    	String imageName = "parse_output";
		p.writeImage(imageData, outImage, imageName);
		return;
	}
	
	private URL getUrl(String objectName) throws MalformedURLException{
		/*TrackerJson tj = new TrackerJson();
		tj.setDate(1384741755537L);
		String s = new Gson().toJson(tj);
		String strUrl = "https://api.parse.com/1/classes/"+objectName+"?where="+s;*/
		String strUrl = "https://api.parse.com/1/classes/"+objectName;
		return new URL(strUrl);
	}
	
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
