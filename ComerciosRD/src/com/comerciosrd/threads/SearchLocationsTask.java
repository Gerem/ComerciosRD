package com.comerciosrd.threads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comerciosrd.activities.LocationDetail;
import com.comerciosrd.adapters.LocationsListAdapter;
import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Categoria;
import com.comerciosrd.pojos.Cliente;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.pojos.Provincia;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.ComerciosRDCacheUtils;
import com.comerciosrd.utils.ComerciosRDConstants;
import com.comerciosrd.utils.ComerciosRDUtils;
import com.comerciosrd.utils.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchLocationsTask extends AsyncTask<Void, Void, Void>{
	private Activity context;
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ArrayList<Localidad> data;
	private ListView view;
	private double[] userLocation;
	private LocationsListAdapter customListAdapter;
	private Long idCliente;
	
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
			data = (ArrayList<Localidad>) ComerciosRDCacheUtils.readObject(context, ComerciosRDConstants.API_CLIENT_MODULE + idCliente);
			if(Validations.ValidateIsNull(data)){
				JSONArray jsonArray = CallServices.callService(ComerciosRDConstants.API_URL
													+ ComerciosRDConstants.API_LOCATION_MODULE							
													+ "/?format=json&idCliente=" + idCliente);
				data = doLocationList(jsonArray);
				
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
		customListAdapter = new LocationsListAdapter(context, R.layout.list_sucursales, data);
		
		ComerciosRDCacheUtils.writeObject(context, ComerciosRDConstants.API_CLIENT_MODULE + idCliente, data);
		
	    // SET THE ADAPTER TO THE LISTVIEW
		view.setAdapter(customListAdapter);
		view.setVisibility(View.VISIBLE);
		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView,int position, long id) {
				Localidad location = ((Localidad) view.getAdapter().getItem(position));
				Intent i = new Intent(context, LocationDetail.class );
				if(Validations.ValidateIsNull(location.getEmail()))
					i.putExtra(ComerciosRDConstants.EMAIL_FIELD, ComerciosRDConstants.N_A_FIELD);
				else
					i.putExtra(ComerciosRDConstants.EMAIL_FIELD, location.getEmail());
            	
            	i.putExtra(ComerciosRDConstants.LATITUDE_FIELD, location.getLatitud());
            	i.putExtra(ComerciosRDConstants.LONGITUDE_FIELD, location.getLongitud());
            	i.putExtra(ComerciosRDConstants.PHONE_FIELD, location.getTelefono());
            	i.putExtra(ComerciosRDConstants.ADDRESS_FIELD, location.getDireccion());
            	i.putExtra(ComerciosRDConstants.DESCRIPTION_FIELD, location.getDescripcion());		
            	i.putExtra(ComerciosRDConstants.CATEGORY_NAME_FIELD, location.getCategoria().getCategoria());
            	i.putExtra(ComerciosRDConstants.CLIENT_NAME_FIELD, location.getCliente().getNombreCliente());
            	//Comenzando la actividad
                context.startActivity(i);
			}
		});
	    // HIDE THE SPINNER AFTER LOADING FEEDS
	    progressBarLL.setVisibility(View.GONE);
	}
	public ArrayList<Localidad> doLocationList(JSONArray jsonArray) throws JSONException, IOException{
		//Logo del cliente
		String clientLogoUrl = null;	
		ArrayList<Localidad> listaLocalidad = new ArrayList<Localidad>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			Localidad location = new Localidad();
			//Informacion del cliente
			Cliente cliente = new Cliente();
			cliente.setIdClientePk(obj.getLong("ID_CLIENTE_FK"));
			cliente.setNombreCliente(obj.getString("NOMBRE_CLIENTE"));
			// Consiguiendo logo
			if(Validations.ValidateIsNull(clientLogoUrl))
				clientLogoUrl = ComerciosRDConstants.API_CLIENT_LOGO_PATH + obj.getString("LOGO");
			//Convirtiendo logo del cliente
			cliente.setLogo(ComerciosRDUtils.drawableFromUrl(clientLogoUrl));
			//Agregando el cliente al objeto localidad
			location.setCliente(cliente);
			
			Provincia provincia = new Provincia();
			provincia.setIdProvinciaPk(obj.getLong("ID_PROVINCIA_PK"));
			provincia.setNombreProvincia(obj.getString("NOMBRE_PROVINCIA"));

			Categoria categoria = new Categoria();
			categoria.setCategoria(obj.getString("NOMBRE_CATEGORIA"));
			
			location.setProvincia(provincia);
			location.setCategoria(categoria);
			
			//Informacion general de localidad
			location.setIdLocalidadPk(obj.getLong("ID_LOCALIDAD_PK"));				
			location.setLatitud(obj.getDouble("LATITUD"));
			location.setLongitud(obj.getDouble("LONGITUD"));
			location.setDireccion(obj.getString("DIRECCION"));
			location.setDescripcion(obj.getString("DESCRIPCION"));
			location.setTelefono(obj.getString("TELEFONO"));
			location.setEmail(obj.getString("EMAIL"));				
			
			//Buscando la distancia en metros
			Double distancia = ComerciosRDUtils.distanceFrom(Localidad.lat, Localidad.lng, location.getLatitud(), location.getLongitud())/1000;
			location.setDistancia(ComerciosRDUtils.roundTwoDecimals(distancia,"#.#"));
			
			listaLocalidad.add(location);				
		}
		return listaLocalidad;
	}
}
