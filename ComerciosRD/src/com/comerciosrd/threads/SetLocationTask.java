package com.comerciosrd.threads;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.MenuItem;

import com.comerciosrd.pojos.Categoria;
import com.comerciosrd.pojos.Cliente;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.pojos.Provincia;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.Constants;
import com.comerciosrd.utils.Utils;
import com.google.android.gms.maps.GoogleMap;

@SuppressLint("NewApi")
public class SetLocationTask extends AsyncTask<Void, Void, Void> {


	private final GoogleMap googleMap;
	private final String query;
	private final MenuItem menuItem;
	private final Activity context;
	private List<Localidad> locations;
	
	public SetLocationTask(Activity context, GoogleMap googleMap, String query, MenuItem menuItem) {
		this.googleMap = googleMap;
		this.query = query;
		this.context = context;
		this.menuItem = menuItem;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPreExecute() {
//		menuItem.setActionView(R.layout.progress_bar);
//		menuItem.expandActionView();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if(Utils.existFile(query, context)){						
			//Searching data in cache
			locations = (ArrayList<Localidad>) Utils.getArrayListFromCache(query, context);			
		}else{	
		try {
						
				JSONArray jsonArray = CallServices
						.callService(Constants.API_URL
								+ Constants.API_LOCATION_MODULE
								+ "/?format=json&idCliente=" + query);
	
				locations = new ArrayList<Localidad>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
	
					Localidad location = new Localidad();
	
					Cliente cliente = new Cliente();
					cliente.setIdClientePk(obj.getLong("ID_CLIENTE_FK"));
					cliente.setNombreCliente(obj.getString("NOMBRE_CLIENTE"));
					
					String clientLogoUrl = Constants.API_CLIENT_LOGO_PATH + obj.getString("LOGO"); 
					cliente.setLogo(Utils.drawableFromUrl(clientLogoUrl));
					
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
					locations.add(location);
				}
	
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return null;
	}
	@Override
    protected void onPostExecute(Void result) {
		googleMap.clear();
		Utils.setNewLocationToMap(googleMap, locations, context);
//		menuItem.collapseActionView();
//	    menuItem.setActionView(null);
    }

}
