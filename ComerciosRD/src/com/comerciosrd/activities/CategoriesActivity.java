package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchCategoriesTask;
import com.comerciosrd.utils.PropertiesConstants;
import com.comerciosrd.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

@SuppressLint("NewApi")
public class CategoriesActivity extends Activity{
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private GridView gridView;
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		gridView = (GridView) findViewById(R.id.gridView1);
		
		// Setting background
		Utils.setActionBarBackground(getActionBar(),PropertiesConstants.MAIN_HEADER_COLOR);
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		//Calling categories
		SearchCategoriesTask categoriesTask = new SearchCategoriesTask(this, progressBarLL,gridView);
		categoriesTask.execute();//Executing

		
		// Buscar AdView como recurso y cargar una solicitud.
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);

	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.	
		if(Utils.isOnline(getApplicationContext()))
			getMenuInflater().inflate(R.menu.categories_menu, menu);
		else
			Utils.showToastMessage(getApplicationContext(), "No tiene conexión a internet, Trabajará en cache.");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.update:
				Utils.clearCache(this);
				//Calling categories
				SearchCategoriesTask categoriesTask = new SearchCategoriesTask(this, progressBarLL,gridView);
				categoriesTask.execute();//Executing
				break;			
			default:
				break;
			}
	
		return true;
	}
	@Override
    public void onPause() {
      adView.pause();
      super.onPause();
    }

    @Override
    public void onResume() {
      super.onResume();
      adView.resume();
    }

    @Override
    public void onDestroy() {
      adView.destroy();
      super.onDestroy();
    }
}
