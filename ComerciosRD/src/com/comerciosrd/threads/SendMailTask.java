package com.comerciosrd.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.PropertiesConstants;

import android.os.AsyncTask;

public class SendMailTask extends AsyncTask<Void, Void, Void>{
	private String body,subject, to;
	public SendMailTask(String body,String subject, String to){
		this.body = body;
		this.subject = subject;
		this.to = to;
	}
	@Override
	protected Void doInBackground(Void... params) {
		try{
			// Add your data
	        List<NameValuePair> postData = new ArrayList<NameValuePair>();
	        postData.add(new BasicNameValuePair("correo", to));
	        postData.add(new BasicNameValuePair("asunto", subject));
	        postData.add(new BasicNameValuePair("cuerpo", body));
			CallServices.sendToService(PropertiesConstants.API_URL + PropertiesConstants.API_SEND_MAIL_MODULE,postData);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
