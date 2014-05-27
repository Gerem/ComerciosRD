package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchClientsTask;
import com.comerciosrd.utils.PropertiesConstants;
import com.comerciosrd.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


@SuppressLint("NewApi")
public class ClientByCategoryActivity extends Activity {
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private GridView gridView;
	private AdView adView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		gridView = (GridView) findViewById(R.id.gridView1);
		
		// Setting background
		Utils.setActionBarBackground(getActionBar(),PropertiesConstants.MAIN_HEADER_COLOR);		
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		
		Bundle extras = getIntent().getExtras();
		
		Long categoryId = extras.getLong("categoryId");
		String categoryName = extras.getString("categoryName");
		Utils.setActionBarName(getActionBar(), categoryName);
		
		// Calling categories
		SearchClientsTask categoriesTask = new SearchClientsTask(this,progressBarLL, gridView, categoryId);
		categoriesTask.execute();// Executing
		
		// Buscar AdView como recurso y cargar una solicitud.
		this.setAdView();

	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}
	
	
	private void setAdView(){
		// Buscar AdView como recurso y cargar una solicitud.
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back_menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back_from_detail:
			super.onBackPressed();
			
			break;
		default:
			break;
		}

		return true;
	}
}
