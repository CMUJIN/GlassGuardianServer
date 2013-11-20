package test;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.Gson;
import com.jinhs.safeguard.common.DataBO;


public class test {
	public static void main(String args[]) throws ClientProtocolException, IOException{
		DataBO data = new DataBO();
		data.setEmail("test@gmail.com");
		data.setDate(new Date());
		data.setLatitude(12.232311);
		data.setLongtitude(32.3232);
		data.setSnapshot(new byte[]{1,2});
		data.setAudio(new byte[]{1,2});
		
		System.out.println(new Gson().toJson(data));
	}
}
