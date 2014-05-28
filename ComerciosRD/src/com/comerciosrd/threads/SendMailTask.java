package com.comerciosrd.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.TextView;

import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.PropertiesConstants;
import com.comerciosrd.utils.Utils;

public class SendMailTask extends AsyncTask<Void, Void, Void>{
	private String body,subject, to;	
	private final MenuItem saveMenu;	
	private Activity context;
	public SendMailTask(String body,String subject, String to, MenuItem saveMenu, Activity context){
		this.body = body;
		this.subject = subject;
		this.to = to;		
		this.saveMenu = saveMenu;
		this.context = context;
	}
	@Override
	protected void onPreExecute() {
		saveMenu.setActionView(R.layout.progress_bar);
		saveMenu.expandActionView();
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
	@Override
	protected void onPostExecute(Void result) {	
		saveMenu.collapseActionView();
		saveMenu.setActionView(null);
		String message = context.getString(R.string.sentLocationRequest);
		Utils.showCustomToast(context, message, null);
	}

}
