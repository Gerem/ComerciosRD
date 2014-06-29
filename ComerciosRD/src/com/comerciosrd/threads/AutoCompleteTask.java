package com.comerciosrd.threads;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.comerciosrd.map.R;
import com.comerciosrd.utils.CallServices;
import com.comerciosrd.utils.CommonUtilities;

public class AutoCompleteTask extends AsyncTask<Void, Void, Void>{

	private Activity context;
	private AutoCompleteTextView autoCompleteView;
	private String[] autocompleteTexts;
	public AutoCompleteTask(Activity context, AutoCompleteTextView autoCompleteView){
		this.context = context;
		this.autoCompleteView = autoCompleteView;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {

			JSONArray jsonArray = CallServices.callService(CommonUtilities.API_URL + CommonUtilities.API_CLIENT_MODULE + "/?format=json");
			autocompleteTexts = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);

				String nombCli = obj.getString("NOMBRE_CLIENTE");
				autocompleteTexts[i] = nombCli;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.my_list_item, autocompleteTexts);
		autoCompleteView.setAdapter(adapter);
	}
}
