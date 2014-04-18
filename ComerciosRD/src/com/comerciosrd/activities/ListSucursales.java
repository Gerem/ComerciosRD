package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SearchLocationsTask;
import com.comerciosrd.utils.ComerciosRDConstants;
import com.comerciosrd.utils.ComerciosRDUtils;

public class ListSucursales extends Activity {
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ListView listView;
	private Long clientId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		listView = (ListView) findViewById(R.id.list);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Setting background
		ComerciosRDUtils.setActionBarBackground(getActionBar(),
				ComerciosRDConstants.MAIN_HEADER_COLOR);
		progressBarLL = (LinearLayout) findViewById(R.id.listLayoutForCircleLoading);

		Bundle extras = getIntent().getExtras();
		clientId = extras.getLong("clienteId");
		ComerciosRDUtils commercialMarkerUtils = new ComerciosRDUtils();
		// Buscando ubicacion actual del usuario
		double[] userLocation = commercialMarkerUtils
				.getlocation(locationManager);

		// Calling categories
		SearchLocationsTask task = new SearchLocationsTask(this, progressBarLL,
				listView, clientId, userLocation);
		task.execute();// Executing

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sucursal_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_view:
			Intent i = new Intent(this, MainActivity.class);
			i.putExtra("clienteId", clientId);
			// Comenzando la actividad
			startActivity(i);

			break;
		default:
			break;
		}

		return true;
	}
}
