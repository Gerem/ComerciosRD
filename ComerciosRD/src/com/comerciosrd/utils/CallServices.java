package com.comerciosrd.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

public class CallServices {

	public CallServices() {

	}

	/***
	 * 
	 * @param query
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray callService(String query)throws ClientProtocolException, IOException, JSONException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpGet http = new HttpGet(query);

		http.setHeader("content-type", "application/json");
		HttpResponse resp = httpClient.execute(http);
		String respStr = EntityUtils.toString(resp.getEntity());

		JSONArray respJSON = new JSONArray(respStr);
		return respJSON;
	}

	public static void sendToService(String query, List<NameValuePair> postData) throws URISyntaxException, ClientProtocolException, IOException {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(query);               
        httppost.setEntity(new UrlEncodedFormEntity(postData,"UTF-8"));

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        response.getStatusLine();
	}
}
