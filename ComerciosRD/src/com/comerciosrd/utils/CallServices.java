package com.comerciosrd.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

public class CallServices {

	
	public CallServices(){
		
	}
	/***
	 * 
	 * @param query
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray callService(String query) throws ClientProtocolException, IOException, JSONException{
		HttpClient httpClient = new DefaultHttpClient();
		 
		HttpGet http = new HttpGet(query);
		 
		http.setHeader("content-type", "application/json");
		HttpResponse resp = httpClient.execute(http);
        String respStr = EntityUtils.toString(resp.getEntity());
 
        JSONArray respJSON = new JSONArray(respStr);
        return respJSON;
	}
}
