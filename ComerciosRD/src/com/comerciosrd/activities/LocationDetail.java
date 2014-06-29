package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.comerciosrd.adapters.LocationDetailAdapter;
import com.comerciosrd.map.R;
import com.comerciosrd.utils.CommonUtilities;
import com.comerciosrd.utils.Utils;
import com.comerciosrd.utils.Validations;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



@SuppressLint("NewApi")
public class LocationDetail extends Activity {
	ListView list;
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		Bundle extras = getIntent().getExtras();
		setView(extras);
		Utils.setActionBarBackground(getActionBar(),CommonUtilities.MAIN_HEADER_COLOR);
		// Buscar AdView como recurso y cargar una solicitud.
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	}

	public void setView(Bundle extras) {
		if (Validations.validateIsNotNull(extras)) {
			//Consiguiendo campos..			
			String nombreCliente = extras.getString(CommonUtilities.CLIENT_NAME_FIELD);
			String nombreCategoria = extras.getString(CommonUtilities.CATEGORY_NAME_FIELD);
			String email = extras.getString(CommonUtilities.EMAIL_FIELD);
			String descripcion = extras.getString(CommonUtilities.DESCRIPTION_FIELD);
			
			final Double latitud = extras.getDouble(CommonUtilities.LATITUDE_FIELD);
			final Double longitud = extras.getDouble(CommonUtilities.LONGITUDE_FIELD);
			
			String telefono = extras.getString(CommonUtilities.PHONE_FIELD);
			String direccion = extras.getString(CommonUtilities.ADDRESS_FIELD);
			
			String categoria = nombreCategoria;
			final String[] content = { descripcion, telefono,
					direccion, email, categoria };
			
			Utils.setActionBarName(getActionBar(), nombreCliente);

			LocationDetailAdapter adapter = new LocationDetailAdapter(LocationDetail.this,content);
			list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);
			
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
				@Override   
				public void onItemClick(AdapterView parentView, View childView, int position, long id) {
					switch(position){
					case 1:
						    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + content[position]));
						    startActivity(callIntent);
						    break;
					case 3:
							Intent sendIntent = new Intent(Intent.ACTION_SEND);
							sendIntent.setType("plain/text");
							sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { content[position].toString() });
							sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
							sendIntent.putExtra(Intent.EXTRA_TEXT, "");
							startActivity(Intent.createChooser(sendIntent, ""));
							break;
					case 2:
							Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +latitud+","+longitud));
					        startActivity(navigation);
							break;
		           }  
		       }
			}); 
		}
	}

	@Override
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back_menu, menu);
		return true;
	}
	
	
}
