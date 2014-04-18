package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchClientsTask;
import com.comerciosrd.utils.ComerciosRDConstants;
import com.comerciosrd.utils.ComerciosRDUtils;

@SuppressLint("NewApi")
public class ClientByCategoryActivity extends Activity {
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private GridView gridView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		gridView = (GridView) findViewById(R.id.gridView1);
		
		// Setting background
		ComerciosRDUtils.setActionBarBackground(getActionBar(),ComerciosRDConstants.MAIN_HEADER_COLOR);		
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		
		Bundle extras = getIntent().getExtras();
		
		// Calling categories
		SearchClientsTask categoriesTask = new SearchClientsTask(this,progressBarLL, gridView, extras.getLong("categoryId"));
		categoriesTask.execute();// Executing

	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}
}
