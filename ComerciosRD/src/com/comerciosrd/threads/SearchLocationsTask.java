package com.comerciosrd.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comerciosrd.activities.LocationDetail;
import com.comerciosrd.adapters.LocationsListAdapter;
import com.comerciosrd.dto.Categoria;
import com.comerciosrd.dto.Cliente;
import com.comerciosrd.dto.Localidad;
import com.comerciosrd.dto.Provincia;
import com.comerciosrd.map.R;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.CommonUtilities;
import com.comerciosrd.utils.Utils;
import com.comerciosrd.utils.Validations;


public class SearchLocationsTask extends AsyncTask<Void, Void, Void>{
	private Activity context;
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ArrayList<Localidad> data;
	private ListView view;
	private double[] userLocation;
	private LocationsListAdapter customListAdapter;
	private Long idCliente;
	private boolean notOnlineNotCache=false;
	
	/****
	 * 
	 * @param context
	 * @param progressBarLL
	 * @param view
	 * @param idCliente
	 * @param locationManager
	 */
	public SearchLocationsTask(Activity context, LinearLayout progressBarLL, ListView view, Long idCliente, double[] userLocation){
		this.context = context;
		this.progressBarLL = progressBarLL;
		this.view = view;
		this.userLocation = userLocation;
		this.idCliente = idCliente;
		Localidad.lat = userLocation[0];
		Localidad.lng = userLocation[1];
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			if(Utils.existFile(idCliente.toString(), context)){						
				//Searching data in cache
				data = (ArrayList<Localidad>) Utils.getArrayListFromCache(idCliente.toString(), context);
				for (Localidad location : data) {
					//Buscando la distancia en metros
					Double distancia = Utils.distanceFrom(Localidad.lat, Localidad.lng, location.getLatitud(), location.getLongitud())/1000;
					location.setDistancia(Utils.roundTwoDecimals(distancia,"#.#"));
				}
			}else if(Utils.isOnline(context)){				
					JSONArray jsonArray = CallServices.callService(CommonUtilities.API_URL
																	+ CommonUtilities.API_LOCATION_MODULE							
																	+ "/?format=json&idCliente=" + idCliente +"&idEstado=1");
					data = this.getLocationList(jsonArray);
			}else{
				notOnlineNotCache = true;				
			}
			Collections.sort(data, new Comparator<Localidad>() {
			    public int compare(Localidad l1, Localidad l2) {
			        return l1.getDistancia().compareTo(l2.getDistancia());
			    }
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressLint("NewApi")
	@Override
	protected void onPreExecute() {		
		// SHOW THE SPINNER WHILE LOADING FEEDS
		progressBarLL.setVisibility(View.VISIBLE);		
	}
	@Override
	protected void onPostExecute(Void result) {  
		if(notOnlineNotCache){
			Utils.showToastMessage(context, "Necesita de una conexi�n a internet para cargar.");
			return;
		}
		Utils.saveArrayListToMemCache(idCliente.toString(), data, context);
		
		customListAdapter = new LocationsListAdapter(context, R.layout.list_sucursales, data);
		
	    // SET THE ADAPTER TO THE LISTVIEW
		view.setAdapter(customListAdapter);
		view.setVisibility(View.VISIBLE);
		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView,int position, long id) {
				Localidad location = ((Localidad) view.getAdapter().getItem(position));
				Intent i = new Intent(context, LocationDetail.class );
            	i.putExtra("email", location.getEmail());
            	i.putExtra("latitud", location.getLatitud());
            	i.putExtra("longitud", location.getLongitud());
            	i.putExtra("telefono", location.getTelefono());
            	i.putExtra("direccion", location.getDireccion());
            	i.putExtra("descripcion", location.getDescripcion());		
            	i.putExtra("nombreCategoria", location.getCategoria());
            	i.putExtra("nombreCliente", location.getCliente().getNombreCliente());
            	//Comenzando la actividad
                context.startActivity(i);
			}
		});
	    // HIDE THE SPINNER AFTER LOADING FEEDS
	    progressBarLL.setVisibility(View.GONE);
	}
	public ArrayList<Localidad> getLocationList(JSONArray jsonArray) throws JSONException, IOException{
		Bitmap logoCliente = null;
		ArrayList<Localidad> listaLocalidad = new ArrayList<Localidad>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			Localidad location = new Localidad();
			//Informacion del cliente
			Cliente cliente = new Cliente();
			cliente.setIdClientePk(obj.getLong("ID_CLIENTE_FK"));
			cliente.setNombreCliente(obj.getString("NOMBRE_CLIENTE"));
			//Consiguiendo el logo
			String clientLogoUrl = CommonUtilities.API_CLIENT_LOGO_PATH + obj.getString("LOGO"); 
			
			if(Validations.validateIsNull(logoCliente)){
				logoCliente = Utils.drawableFromUrl(clientLogoUrl);
				logoCliente = Utils.getRoundedCornerBitmap(logoCliente, 12);
			}
			cliente.setLogo(logoCliente);
			//Agregando el cliente al objeto localidad
			location.setCliente(cliente);
			
			Provincia provincia = new Provincia();
			provincia.setIdProvinciaPk(obj.getLong("ID_PROVINCIA_PK"));
			provincia.setNombreProvincia(obj.getString("NOMBRE_PROVINCIA"));
			
			location.setProvincia(provincia);
			location.setCategoria(obj.getString("NOMBRE_CATEGORIA"));
			
			//Informacion general de localidad
			location.setIdLocalidadPk(obj.getLong("ID_LOCALIDAD_PK"));				
			location.setLatitud(obj.getDouble("LATITUD"));
			location.setLongitud(obj.getDouble("LONGITUD"));
			location.setDireccion(obj.getString("DIRECCION"));
			location.setDescripcion(obj.getString("DESCRIPCION"));
			location.setTelefono(obj.getString("TELEFONO"));
			location.setEmail(obj.getString("EMAIL"));				
			
			//Buscando la distancia en metros
			Double distancia = Utils.distanceFrom(Localidad.lat, Localidad.lng, location.getLatitud(), location.getLongitud())/1000;
			location.setDistancia(Utils.roundTwoDecimals(distancia,"#.#"));
			
			listaLocalidad.add(location);				
		}
		return listaLocalidad;
	}
}
