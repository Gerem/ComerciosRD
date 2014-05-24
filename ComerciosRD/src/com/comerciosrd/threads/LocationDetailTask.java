package com.comerciosrd.threads;



import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.comerciosrd.adapters.LocationDetailAdapter;
import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Categoria;
import com.comerciosrd.pojos.Cliente;
import com.comerciosrd.pojos.Localidad;
import com.comerciosrd.pojos.Provincia;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.PropertiesConstants;
import com.comerciosrd.utils.Utils;



@SuppressLint("NewApi")
public class LocationDetailTask extends AsyncTask<Void, Void, Void> {
	private final Activity context;
	private final Double lat;
	private final Double lon;
	private Localidad location;
	//private final MenuItem menuItem;
	private ListView list;
	public LocationDetailTask(Activity context,Double lat,Double lon){
		this.context = context;
		this.lat = lat;
		this.lon = lon;
		//this.menuItem = menuItem;
	}

//	@SuppressLint("NewApi")
//	@Override
//	protected void onPreExecute() {
//		menuItem.setActionView(R.layout.progress_bar);
//		menuItem.expandActionView();
//	}
	@Override
	protected Void doInBackground(Void... params) {
			try {
				JSONArray jsonArray = CallServices
						.callService(PropertiesConstants.API_URL
								+ PropertiesConstants.API_LOCATION_MODULE
								+ "/?format=json&latitud=" + lat + "&longitud="+lon);
				location = new Localidad();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Cliente cliente = new Cliente();
					cliente.setIdClientePk(obj.getLong("ID_CLIENTE_FK"));
					cliente.setNombreCliente(obj.getString("NOMBRE_CLIENTE"));
					String clientLogoUrl = PropertiesConstants.API_CLIENT_LOGO_PATH + obj.getString("LOGO"); 
					cliente.setLogo(Utils.drawableFromUrl(clientLogoUrl));
					
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
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		return null;
	}
	@Override
    protected void onPostExecute(Void result) {
		final String[] content = { location.getDescripcion(), location.getTelefono(),location.getDireccion(), location.getEmail()};		
		Utils.setActionBarName(context.getActionBar(), location.getCliente().getNombreCliente());
		LocationDetailAdapter adapter = new LocationDetailAdapter(context,content);
		list = (ListView) context.findViewById(R.id.list);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			@Override   
			public void onItemClick(AdapterView parentView, View childView, int position, long id) {
				switch(position){
				case 1:
					    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + content[position]));
					    context.startActivity(callIntent);
					    break;
				case 3:
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.setType("plain/text");
						sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { content[position].toString() });
						sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
						sendIntent.putExtra(Intent.EXTRA_TEXT, "Texto");
						context.startActivity(Intent.createChooser(sendIntent, ""));
						break;
				case 2:
						Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" +location.getLatitud()+","+location.getLongitud()));
						context.startActivity(navigation);
						break;
	           }  
	       }
		}); 
    }
}
