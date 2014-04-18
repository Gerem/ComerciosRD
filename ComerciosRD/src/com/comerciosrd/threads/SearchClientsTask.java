package com.comerciosrd.threads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.activities.ListSucursales;
import com.comerciosrd.adapters.ClientsGridAdapter;
import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Cliente;
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

public class SearchClientsTask extends AsyncTask<Void, Void, Void>{
	private Activity context;
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ArrayList<Cliente> data;
	private GridView gridView;
	private ClientsGridAdapter customGridAdapter;
	private Long idCategory;

	
	private final String ID_CLIENTE_PK = "ID_CLIENTE_PK";
	private final String NOMBRE_CLIENTE = "NOMBRE_CLIENTE";
	private final String LOGO = "LOGO";
	
	
	public SearchClientsTask(Activity context, LinearLayout progressBarLL, GridView gridView, Long idCategory){
		this.context = context;
		this.progressBarLL = progressBarLL;
		this.gridView = gridView;
		this.idCategory = idCategory;
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			data = (ArrayList<Cliente>) ComerciosRDCacheUtils.readObject(context, ComerciosRDConstants.API_CATEGORY_MODULE + idCategory);
			if(Validations.ValidateIsNull(data)){
				JSONArray jsonArray = CallServices.callService(ComerciosRDConstants.API_URL
												   + ComerciosRDConstants.API_CLIENT_MODULE							
												   + "/?format=json&idCategoria=" + idCategory);
				data = doCliente(jsonArray);
				
			}						
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
		customGridAdapter = new ClientsGridAdapter(context, R.layout.row_grid, data);

		ComerciosRDCacheUtils.writeObject(context, ComerciosRDConstants.API_CATEGORY_MODULE + idCategory, data);
	    // SET THE ADAPTER TO THE LISTVIEW
	    gridView.setAdapter(customGridAdapter);
	    gridView.setVisibility(View.VISIBLE);
	    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView,int position, long id) {
				Long clienteId = ((Cliente) gridView.getAdapter().getItem(position)).getIdClientePk();
				Intent i = new Intent(context, ListSucursales.class);
				i.putExtra("clienteId", clienteId);
				//Comenzando la actividad
                context.startActivity(i);
			}
		});
	    // HIDE THE SPINNER AFTER LOADING FEEDS
	    progressBarLL.setVisibility(View.GONE);
	}
	public ArrayList<Cliente> doCliente(JSONArray jsonArray) throws JSONException, IOException{
		ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			Cliente cliente = new Cliente();
			cliente.setIdClientePk(obj.getLong(ID_CLIENTE_PK));
			cliente.setNombreCliente(obj.getString(NOMBRE_CLIENTE));
			//Getting LOGO
			String clientLogoUrl = ComerciosRDConstants.API_CLIENT_LOGO_PATH + obj.getString(LOGO); 
			cliente.setLogo(ComerciosRDUtils.drawableFromUrl(clientLogoUrl));
			
			listaCliente.add(cliente);
		}
		return listaCliente;
	}
}
