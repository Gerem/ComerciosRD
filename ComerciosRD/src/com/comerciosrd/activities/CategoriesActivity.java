package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchCategoriesTask;
import com.comerciosrd.utils.Constants;
import com.comerciosrd.utils.Utils;


@SuppressLint("NewApi")
public class CategoriesActivity extends Activity{
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		gridView = (GridView) findViewById(R.id.gridView1);
		
		// Setting background
		Utils.setActionBarBackground(getActionBar(),Constants.MAIN_HEADER_COLOR);
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		//Calling categories
		SearchCategoriesTask categoriesTask = new SearchCategoriesTask(this, progressBarLL,gridView);
		categoriesTask.execute();//Executing
		
		

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
}
