package com.comerciosrd.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.comerciosrd.map.R;
import com.comerciosrd.threads.GCMRegisterTask;
import com.comerciosrd.threads.SearchCategoriesTask;
import com.comerciosrd.utils.CommonUtilities;
import com.comerciosrd.utils.ServerUtilities;
import com.comerciosrd.utils.Utils;
import com.comerciosrd.utils.WakeLocker;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.ads.AdRequest;
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
		
		progressBarLL = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		//Calling categories
		SearchCategoriesTask categoriesTask = new SearchCategoriesTask(this, progressBarLL,gridView);
		categoriesTask.execute();//Executing

		
		// Buscar AdView como recurso y cargar una solicitud.
		adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	    
	    // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
	    registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));
	    
	    this.registerGCM();
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
		
		// Setting background
		Utils.setActionBarBackground(getActionBar(),CommonUtilities.MAIN_HEADER_COLOR);
		
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
			case R.id.newLocation:
				Intent i = new Intent(this, NewLocationActivity.class);				
				// Comenzando la actividad
				startActivity(i);
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
	     try {
	         unregisterReceiver(mHandleMessageReceiver);
	         GCMRegistrar.onDestroy(this);
	     } catch (Exception e) {
	         Log.e("UnRegister Receiver Error", "> " + e.getMessage());
	     }
	      adView.destroy();
	      super.onDestroy();
    }
    private void registerGCM(){
    	
    	 // Get GCM registration id
        String regId = GCMRegistrar.getRegistrationId(this);
 
        // Check if regid already presents
        if (regId.equals("")) {
        	// Registration is not present, register now with GCM           
            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.              
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.                
               GCMRegisterTask gcmRegisterTask = new GCMRegisterTask(this, regId);
               gcmRegisterTask.execute();
            }
        }
    }
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
                       
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            WakeLocker.release();
        }
    };
}
