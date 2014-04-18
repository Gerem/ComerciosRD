package com.comerciosrd.activities;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.SetLocationTask;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.Constants;
import com.comerciosrd.utils.Utils;
import com.comerciosrd.utils.Validations;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private MenuItem menuItem;
	private GoogleMap googleMap;	
	private String[] autocompleteTexts;
	private MenuItem searchItem;
	private Long clienteId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prepareApp();

	}
	
	
	@SuppressLint("NewApi")
	
	public void prepareApp(){		
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.googleMap)).getMap();
		// Setting my current location
		googleMap.setMyLocationEnabled(true);						
		
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Utils commercialMarkerUtils = new Utils();
		// Setting background
		Utils.setActionBarBackground(getActionBar(),Constants.MAIN_HEADER_COLOR);
		// Getting current location
		Utils.moveToCurrentLocation(googleMap,
				commercialMarkerUtils.getlocation(locationManager));
		Bundle extras = getIntent().getExtras();
		clienteId = extras.getLong("clienteId");
		searchLocations();
		//callAutocomplete();
	}
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// When a menu is selected this method is triggered
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.list_view:
				super.onBackPressed();
	
				break;
			case R.id.change_map:
				Utils.alternarVista(googleMap);
				break;
	
			default:
				break;
			}
	
		return true;
	}

	public void setSearchButtonActive() {
//		AutoCompleteTextView txtEdit = (AutoCompleteTextView) findViewById(R.id.searchText);
//		txtEdit.requestFocus();
		// Setting Auto Complete array
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.my_list_item, autocompleteTexts);
//
//		txtEdit.setAdapter(adapter);

		googleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				EditText txtEdit = (EditText) findViewById(R.id.searchText);
				if (Validations.validateIsNotNull(txtEdit))
					txtEdit.clearFocus();

			}
		});
//		setEditTextAction(txtEdit);
	}
	//Method is executed when the Back Button is pressed
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AutoCompleteTextView txtEdit = (AutoCompleteTextView) findViewById(R.id.searchText);

		if (Validations.validateIsNotNull(txtEdit))
			txtEdit.clearFocus();
	}
	//Metodo que busca las localidades
	@SuppressLint("NewApi")
	private void searchLocations() {
		//AutoCompleteTextView txtEdit = (AutoCompleteTextView) findViewById(R.id.searchText);
		String query = clienteId.toString();
//
//		searchItem.setActionView(R.layout.progress_bar);
//		searchItem.expandActionView();

		SetLocationTask setLocationTask = new SetLocationTask(this, googleMap,query, searchItem);
		setLocationTask.execute();

	}

	// Calling service to auto complete
	private void callAutocomplete() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					JSONArray jsonArray = CallServices
							.callService(Constants.API_URL
									+ Constants.API_CLIENT_MODULE
									+ "/?format=json");
					autocompleteTexts = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);

						String nombCli = obj.getString("NOMBRE_CLIENTE");
						autocompleteTexts[i] = nombCli;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	@SuppressLint("NewApi")
	public void setEditTextAction(EditText txtEdit) {
		txtEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					menuItem.collapseActionView();
					menuItem.setActionView(null);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
		// In search action..
		txtEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchLocations();
					return true;
				}
				return false;
			}
		});
	}

}
