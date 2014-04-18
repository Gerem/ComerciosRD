package com.comerciosrd.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comerciosrd.map.R;
import com.comerciosrd.utils.Validations;

public class LocationDetailAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] content;
	private final String[] label = { "Nombre", "Telefono", "Direcci\u00f3n", "Correo", "Categor\u00eda" };
	private final Integer[] img = { null, R.drawable.ic_action_end_call,
									R.drawable.ic_action_next_item, 
									R.drawable.ic_action_email,
									R.drawable.ic_action_go_to_today};
	
	public LocationDetailAdapter(Activity context,  String[] content) {
		super(context, R.layout.list_single, content);
		this.context = context;		
		this.content = content;		
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_single, null, true);
		
		TextView txtLabel = (TextView) rowView.findViewById(R.id.txt);
		TextView txtContent = (TextView) rowView.findViewById(R.id.content);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		
		txtLabel.setText(label[position]);
		txtContent.setText(content[position]);
		if(Validations.validateIsNotNull(img[position])){
			imageView.setImageResource(img[position]);
		}
		return rowView;
	}
}
