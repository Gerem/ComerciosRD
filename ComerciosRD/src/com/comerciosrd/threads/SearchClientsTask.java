package com.comerciosrd.threads;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.activities.ListSucursales;
import com.comerciosrd.adapters.ClientsGridAdapter;
import com.comerciosrd.dto.Cliente;
import com.comerciosrd.map.R;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.CommonUtilities;
import com.comerciosrd.utils.Utils;



public class SearchClientsTask extends AsyncTask<Void, Void, Void>{
	private Activity context;
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ArrayList<Cliente> data;
	private GridView gridView;
	private ClientsGridAdapter customGridAdapter;
	private Long idCategory;
	private String categoryFileName;
	private boolean notOnlineNotCache=false;
	
	public SearchClientsTask(Activity context, LinearLayout progressBarLL, GridView gridView, Long idCategory){
		this.context = context;
		this.progressBarLL = progressBarLL;
		this.gridView = gridView;
		this.idCategory = idCategory;
		this.categoryFileName = "category_" + this.idCategory;
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		if(Utils.existFile(categoryFileName, context)){						
			//Searching data in cache
			data = (ArrayList<Cliente>) Utils.getArrayListFromCache(categoryFileName, context);			
		}else if(Utils.isOnline(context)){
			try {
				JSONArray jsonArray = CallServices
						.callService(CommonUtilities.API_URL
								+ CommonUtilities.API_CLIENT_MODULE							
								+ "/?format=json&idCategoria=" + idCategory +"&idEstado=1");
				data = new ArrayList<Cliente>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Cliente cliente = new Cliente();
					cliente.setIdClientePk(obj.getLong("ID_CLIENTE_PK"));
					cliente.setNombreCliente(obj.getString("NOMBRE_CLIENTE"));
					//Getting LOGO
					String clientLogoUrl = CommonUtilities.API_CLIENT_LOGO_PATH + obj.getString("LOGO"); 
					Bitmap logoBM = Utils.drawableFromUrl(clientLogoUrl);
					logoBM = Utils.getRoundedCornerBitmap(logoBM, 10);
					cliente.setLogo(logoBM);
					
					data.add(cliente);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			notOnlineNotCache = true;
			
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
			Utils.showToastMessage(context, "Necesita de una conexión a internet para cargar.");
			return;
		}
		Utils.saveArrayListToMemCache(categoryFileName, data, context);
		customGridAdapter = new ClientsGridAdapter(context, R.layout.row_grid, data);

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
}
