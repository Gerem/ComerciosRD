package com.comerciosrd.threads;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.comerciosrd.activities.ClientByCategoryActivity;
import com.comerciosrd.adapters.CategoryGridAdapter;
import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Categoria;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.Constants;
import com.comerciosrd.utils.Utils;



public class SearchCategoriesTask extends AsyncTask<Void, Void, Void> {
	private Activity context;
	// CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)
	private LinearLayout progressBarLL;
	private ArrayList<Categoria> data;
	private GridView gridView;
	private CategoryGridAdapter customGridAdapter;
	private boolean notOnlineNotCache=false;
	public SearchCategoriesTask(Activity context, LinearLayout progressBarLL,
			GridView gridView) {
		this.context = context;
		this.progressBarLL = progressBarLL;
		this.gridView = gridView;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		if(Utils.existFile(Constants.CATEGORY_VIEW_NAME, context)){						
			//Searching data in cache
			data = (ArrayList<Categoria>) Utils.getArrayListFromCache(Constants.CATEGORY_VIEW_NAME, context);			
		}else if(Utils.isOnline(context)){
			try {
				JSONArray jsonArray = CallServices
						.callService(Constants.API_URL
								+ Constants.API_CATEGORY_MODULE
								+ "/?format=json&indNegocios=1");
				data = new ArrayList<Categoria>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Categoria categoria = new Categoria();
					categoria.setCategoria(obj.getString("NOMBRE_CATEGORIA"));
					categoria.setIdCategoriaPk(obj.getLong("ID_CATEGORIA_PK"));
					data.add(categoria);
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
		gridView.setVisibility(View.GONE);
	}

	@Override
	protected void onPostExecute(Void result) {	
		if(notOnlineNotCache){
			Utils.showToastMessage(context, "Necesita de una conexi�n a internet para cargar.");
			return;
		}
		Utils.saveArrayListToMemCache(Constants.CATEGORY_VIEW_NAME, data, context);
		
		customGridAdapter = new CategoryGridAdapter(context, R.layout.row_grid,data);

		// SET THE ADAPTER TO THE LISTVIEW
		gridView.setAdapter(customGridAdapter);
		gridView.setVisibility(View.VISIBLE);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parentView, View childView,int position, long id) {
				Categoria categoria = ((Categoria) gridView.getAdapter().getItem(position));
				Long categoryId = categoria.getIdCategoriaPk();
				String categoryName = categoria.getCategoria();
				Intent i = new Intent(context, ClientByCategoryActivity.class);
				i.putExtra("categoryId", categoryId);
				i.putExtra("categoryName", categoryName);
				//Comenzando la actividad
                context.startActivity(i);
			}
		});
		// HIDE THE SPINNER AFTER LOADING FEEDS
		progressBarLL.setVisibility(View.GONE);
	}
}
