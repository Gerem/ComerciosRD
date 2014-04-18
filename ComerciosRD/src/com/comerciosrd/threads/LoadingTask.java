package com.comerciosrd.threads;



import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.MenuItem;

import com.comerciosrd.map.R;

@SuppressLint("NewApi")
public class LoadingTask<Params> extends AsyncTask<String, Void, String> { 
	MenuItem menuItem;
	
	public LoadingTask(MenuItem menuItem){
		this.menuItem = menuItem;
		
	}
	@Override
    protected String doInBackground(String... params) {
		menuItem.setActionView(R.layout.progress_bar);
		menuItem.expandActionView();
      return null;
    }
    
	@SuppressLint("NewApi")
	@Override
    protected void onPostExecute(String result) {
      menuItem.collapseActionView();
      menuItem.setActionView(null);
    }
}
