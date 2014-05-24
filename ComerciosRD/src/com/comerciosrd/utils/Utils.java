package com.comerciosrd.utils;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comerciosrd.activities.LocationDetail;
import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.types.BooleanType;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class Utils implements LocationListener{	
	private static int vista = 0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static double latitud;
    private static double longitud;
	// constant to determine which sub-activity returns
	private static final int REQUEST_CODE = 10;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
	public Utils() {
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
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
	            .getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = pixels;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
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
		if(Validations.validateListIsNotNullAndNotEmpty(locations)){
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
					
						
					map.setInfoWindowAdapter(new InfoWindowAdapter() {

				        // Use default InfoWindow frame
				        @Override
				        public View getInfoWindow(Marker arg0) {
				            return null;
				        }

				        // Defines the contents of the InfoWindow
				        @Override
				        public View getInfoContents(Marker arg0) {

				            // Getting view from the layout file info_window_layout
				            View v = context.getLayoutInflater().inflate(R.layout.infowindow_adapter, null);

				            // Getting reference to the TextView to set title
				            TextView title = (TextView) v.findViewById(R.id.infoWTitle);

				            ImageView imageView = (ImageView) v.findViewById(R.id.infoWlogo);
				            // Getting reference to the TextView to set description
				            TextView description = (TextView) v.findViewById(R.id.infoWDescription);
				            imageView.setImageBitmap(location.getCliente().getLogo());
				            // Setting the latitude
				            title.setText(arg0.getTitle());

				            // Setting the longitude
				            description.setText(arg0.getSnippet());

				            // Returning the view containing InfoWindow contents
				            return v;

				        }
				    });
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			            @Override
			            public void onInfoWindowClick(Marker marker) {            	
			            	Intent i = new Intent(context, LocationDetail.class );
			            	i.putExtra("email", location.getEmail());
			            	i.putExtra("latitud", marker.getPosition().latitude);
			            	i.putExtra("longitud", marker.getPosition().longitude);
			            	i.putExtra("telefono", location.getTelefono());
			            	i.putExtra("direccion", marker.getSnippet());
			            	i.putExtra("descripcion", marker.getTitle());		
			            	i.putExtra("nombreCategoria", location.getCategoria());
			            	i.putExtra("nombreCliente", location.getCliente().getNombreCliente());
			            	//Comenzando la actividad
			                context.startActivity(i);
			            }
			        });
					marker.showInfoWindow();
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
	      return Double.valueOf(twoDForm.format(decimal).replace(',', '.'));
	} 
	/***
	 * 
	 * @param fileName
	 * @param data
	 * @param context
	 * @throws IOException
	 */
	public static void saveArrayListToMemCache(String fileName,ArrayList<?> data, Context context){
		FileOutputStream stream = null;
		if(!existFile(fileName, context)){
			try{
			   stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);			   
			   ObjectOutputStream  dout = new ObjectOutputStream (stream);
			   
			   dout.writeObject(data);
			   dout.flush();
			   dout.close();			   
			   stream.getFD().sync();
			   stream.close();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/***
	 * This method get the array List from Cache
	 * @param fileName
	 * @param context
	 * @return
	 */
	public static ArrayList<?> getArrayListFromCache(String fileName,Context context){
		ArrayList<?> data = null;
		try{
		    FileInputStream fileIn = context.openFileInput(fileName);		    
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        
	        data = (ArrayList<?>) in.readObject();	        
	        in.close();	        
	        fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return data;
	}
	/***
	 * This method clear the cache
	 * @param context
	 */
	public static void clearCache(Context context){
		String[] fileList = context.fileList();
		for (int i = 0; i < fileList.length; i++) {
			context.deleteFile(fileList[i]);
		}
	}
	/***
	 * 
	 * @param fileName
	 * @param context
	 * @return true or false
	 */
	public static Boolean existFile(String fileName, Context context){
		Boolean returnValue = Boolean.TRUE;		
		try {
			context.openFileInput(fileName);
		} catch (FileNotFoundException e) {
			returnValue = Boolean.FALSE;
		}
        
        return returnValue;
		
	}
	public static void showToastMessage(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
	
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
	
		return false;
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
