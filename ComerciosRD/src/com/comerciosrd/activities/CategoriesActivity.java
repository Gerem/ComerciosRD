package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchCategoriesTask;
import com.comerciosrd.utils.ComerciosRDConstants;
import com.comerciosrd.utils.ComerciosRDUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

@SuppressLint("NewApi")
public class CategoriesActivity extends Activity{
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private GridView gridView;
	private AdView adView;
	 /* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "a152d23767dbc27";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		gridView = (GridView) findViewById(R.id.gridView1);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.categoriesLayout);
		// Setting background
		ComerciosRDUtils.setActionBarBackground(getActionBar(),ComerciosRDConstants.MAIN_HEADER_COLOR);
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		//Calling categories
		SearchCategoriesTask categoriesTask = new SearchCategoriesTask(this, progressBarLL,gridView);
		categoriesTask.execute();//Executing
		
		// Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);
	    relativeLayout.addView(adView);
	 // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}
}
