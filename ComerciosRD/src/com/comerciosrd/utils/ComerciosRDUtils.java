package com.comerciosrd.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.comerciosrd.activities.LocationDetail;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.types.BooleanType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

@SuppressLint("NewApi")
public class ComerciosRDUtils implements LocationListener{	
	private static int vista = 0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static double latitud;
    private static double longitud;
	// constant to determine which sub-activity returns
	private static final int REQUEST_CODE = 10;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
	public ComerciosRDUtils() {
		super();
	}
	/***
	 * 
	 * @param map
	 */
	public static void alternarVista(GoogleMap map) {
		vista = (vista + 1) % 4;

		switch (vista) {
		case 0:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);break;
		case 1:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);break;
		case 2:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);break;
		case 3:
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);break;
		}
	}
	/***
	 * 
	 * @param locationManager
	 * @return double[]
	 */
	public double[] getlocation(LocationManager locationManager) {

		double[] gps = new double[2];

		// Consiguiendo proveedores
		List<String> providers = locationManager.getProviders(true);
	    for (String provider : providers) {
	        locationManager.requestLocationUpdates(provider, 1000, 0,
	            new LocationListener() {

	                public void onLocationChanged(Location location) {}

	                public void onProviderDisabled(String provider) {}

	                public void onProviderEnabled(String provider) {}

	                public void onStatusChanged(String provider, int status,
	                        Bundle extras) {}
	            });
	        Location location = locationManager.getLastKnownLocation(provider);
	        if (location != null) {
	        	gps[0] = location.getLatitude();
	        	gps[1] = location.getLongitude();	            	            
	        }
	    }
		latitud = gps[0];
		longitud = gps[1];
		return gps;
	}
	/***
	 * *
	 * @param actionBar
	 * @param color
	 */
	public static void setActionBarBackground(ActionBar actionBar, String color) {
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(color)));
		
	}
	/**	 
	 * @param actionBar
	 * @param title
	 */
	public static void setActionBarName(ActionBar actionBar, String title){
		actionBar.setTitle(title);				
	}
	/**
	 * 
	 * @param map
	 * @param d
	 */
	public static void moveToCurrentLocation(GoogleMap map, double[] d) {		
		Localidad.lat = d[0];
		Localidad.lng = d[1];

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				Localidad.lat, Localidad.lng), 15));
		
	}
	/**
	 * **
	 * @param map
	 * @param locations
	 * @param context
	 */
	public static void setNewLocationToMap(GoogleMap map,List<Localidad> locations, final Activity context) {
		if(Validations.ValidateListIsNotNullAndNotEmpty(locations)){
			//Consiguiendo la localidad mas cercana
			locations = getCloserLocation(locations);
			
			for (final Localidad location : locations) {
				map.setIndoorEnabled(true);
				if(location.getIndCercano().equals(BooleanType.SI.getCode())){
					Bitmap iconBitmap = getResizedBitmap(location.getCliente().getLogo(),80,80);
					BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(iconBitmap);
					
					Marker marker = map.addMarker(new MarkerOptions()
							.position(new LatLng(location.getLatitud(), location.getLongitud()))
							.title(location.getDescripcion())
							.flat(true)
							.icon(icon)
							.snippet(location.getDireccion()));
					
					marker.showInfoWindow();				
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			            @Override
			            public void onInfoWindowClick(Marker marker) {            	
			            	Intent i = new Intent(context, LocationDetail.class );
			            	
			            	if(Validations.ValidateIsNull(location.getEmail()))
								i.putExtra(ComerciosRDConstants.EMAIL_FIELD, ComerciosRDConstants.N_A_FIELD);
							else
								i.putExtra(ComerciosRDConstants.EMAIL_FIELD, location.getEmail());
			            				            
			            	i.putExtra(ComerciosRDConstants.LATITUDE_FIELD, marker.getPosition().latitude);
			            	i.putExtra(ComerciosRDConstants.LONGITUDE_FIELD, marker.getPosition().longitude);
			            	i.putExtra(ComerciosRDConstants.PHONE_FIELD, location.getTelefono());
			            	i.putExtra(ComerciosRDConstants.ADDRESS_FIELD, marker.getSnippet());
			            	i.putExtra(ComerciosRDConstants.DESCRIPTION_FIELD, marker.getTitle());		
			            	i.putExtra(ComerciosRDConstants.CATEGORY_NAME_FIELD, location.getCategoria().getCategoria());
			            	i.putExtra(ComerciosRDConstants.CLIENT_NAME_FIELD, location.getCliente().getNombreCliente());
			            	//Comenzando la actividad
			                context.startActivity(i);
			            }
			        });
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitud(), location.getLongitud()), 14));
				}else{
					Bitmap iconBitmap = getResizedBitmap(location.getCliente().getLogo(),80,80);
					BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(iconBitmap);
					map.addMarker(new MarkerOptions()
					.position(new LatLng(location.getLatitud(), location.getLongitud()))
					.title(location.getDescripcion())					
					.snippet(location.getDireccion())
					.icon(icon));
				}
			}

					
		}
	}
	/**
	 * **
	 * @param locations
	 * @return List<Localidad>
	 */
	public static List<Localidad> getCloserLocation(List<Localidad> locations){
		int i = 0;
		int index = 0;
		double distanciaMenor = 9999999.0;
		while (i<locations.size()) {
			
			
			double distancia = distanceFrom(latitud,longitud,locations.get(i).getLatitud(),locations.get(i).getLongitud());
            if ( distancia < distanciaMenor) {
            	distanciaMenor = distancia;
            	index = i;
            }
            ++i;
        }
		locations.get(index).setIndCercano(1);
		return locations;
	}
	/**
	 * Metodo que devuelve la distancia entre usuario y localidad**
	 * @param userLat
	 * @param userLng
	 * @param locLat
	 * @param locLng
	 * @return
	 */
	public static double distanceFrom(double userLat, double userLng, double locLat, double locLng) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(locLat-userLat);
	    double dLng = Math.toRadians(locLng-userLng);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(locLat)) * Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	    int meterConversion = 1609;
	    return new Double(dist * meterConversion).floatValue();    // this will return distance
	}

	/***
	 * 
	 * @param url
	 * @return Bitmap
	 * @throws IOException
	 */
	public static Bitmap drawableFromUrl(String url) throws IOException {
	    Bitmap x = null;

	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    InputStream input = connection.getInputStream();
	    
	    x = BitmapFactory.decodeStream(input);
	    return x;
	}
	/***
	 * This method Resizes a bitmap
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return Bitmap
	 */
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	 
	public static Double roundTwoDecimals(double decimal, String format) { 
	      DecimalFormat twoDForm = new DecimalFormat(format); 
	      return Double.valueOf(twoDForm.format(decimal));
	} 
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
}
