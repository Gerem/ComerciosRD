package com.comerciosrd.activities;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Cliente;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.threads.AutoCompleteTask;
import com.comerciosrd.threads.SendMailTask;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.PropertiesConstants;
import com.comerciosrd.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewLocationActivity extends FragmentActivity{
	
	private GoogleMap googleMap;
	private Localidad locationRequest;
	private AutoCompleteTextView autoCompleteView;
	private EditText locDesc;
	private EditText locAddress;
	private EditText locPhone;
	private EditText locEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_location);

		// Setting background
		Utils.setActionBarBackground(getActionBar(),PropertiesConstants.MAIN_HEADER_COLOR);		
		
		locDesc = (EditText)this.findViewById(R.id.locationDesc);
		locAddress = (EditText)this.findViewById(R.id.locAddres);
		locPhone = (EditText)this.findViewById(R.id.locPhone);
		locEmail = (EditText)this.findViewById(R.id.locEmail);
		
		autoCompleteView = (AutoCompleteTextView) this.findViewById(R.id.clienteAutoComple);
		
		AutoCompleteTask autoCompleteTask = new AutoCompleteTask(this, autoCompleteView);
		autoCompleteTask.execute();
		
		this.initMap();
		
	}
	
	public void initMap(){
		locationRequest = new Localidad();
		googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
		// Setting my current location
		googleMap.setMyLocationEnabled(true);
				
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Utils commercialMarkerUtils = new Utils();

		// Getting current location
		Utils.moveToCurrentLocation(googleMap,commercialMarkerUtils.getlocation(locationManager));		
		
		googleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				googleMap.clear();
				googleMap.addMarker(new MarkerOptions()
		        .position(point)		         
		        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));  
				locationRequest.setLatitud(point.latitude);
				locationRequest.setLongitud(point.longitude);
			}
		});
	}
	
	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_location_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.saveLocation:
			 try {
				this.sendNewLocation();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return true;
	}
	public void sendNewLocation(){
		Cliente cliente = new Cliente();
		cliente.setNombreCliente(autoCompleteView.getText().toString());
		locationRequest.setDescripcion(locDesc.getText().toString());
		locationRequest.setDireccion(locAddress.getText().toString());
		locationRequest.setTelefono(locPhone.getText().toString());
		locationRequest.setEmail(locEmail.getText().toString());
		locationRequest.setCliente(cliente);
		
		String body ="";
		body +="<p>Cliente: " 	  + locationRequest.getCliente().getNombreCliente()  + "</p></br>";
		body +="<p>Descripcion: " + locationRequest.getDescripcion()  + "</p></br>";
		body +="<p>Direccion: " + locationRequest.getDireccion()  + "</p></br>";
		body +="<p>Telefono: " 	+ locationRequest.getTelefono()  + "</p></br>";
		body +="<p>Email: "	 	+ locationRequest.getEmail()  + "</p></br>";
		body +="<p>Longitud: "	+ locationRequest.getLongitud()  + "</p></br>";
		body +="<p>Latitud: "	+ locationRequest.getLatitud()  + "</p></br>";
		
		SendMailTask mailTask = new SendMailTask(body, PropertiesConstants.EMAIL_SUBJECT, PropertiesConstants.ADMIN_EMAIL);				
		mailTask.execute();
	}
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	}

}
