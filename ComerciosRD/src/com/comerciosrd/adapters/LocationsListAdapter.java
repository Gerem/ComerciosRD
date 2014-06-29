package com.comerciosrd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comerciosrd.dto.Localidad;
import com.comerciosrd.map.R;

import java.util.ArrayList;

public class LocationsListAdapter extends ArrayAdapter<Localidad> {
	Context context;
	int layoutResourceId;
	ArrayList<Localidad> data = new ArrayList<Localidad>();

	public LocationsListAdapter(Context context, int layoutResourceId,ArrayList<Localidad> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder recordHolder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			recordHolder = new RecordHolder();
			recordHolder.descriptionTxt = (TextView) row.findViewById(R.id.descriptionTxt);
			recordHolder.categoriaTxt = (TextView) row.findViewById(R.id.categoriaTxt);
			recordHolder.direccionTxt = (TextView) row.findViewById(R.id.direccionTxt);
			recordHolder.distanciaTxt = (TextView) row.findViewById(R.id.distanciaTxt);
			recordHolder.logo = (ImageView) row.findViewById(R.id.logo);
			row.setTag(recordHolder);
		} else {
			recordHolder = (RecordHolder) row.getTag();
		}

		Localidad localidad = data.get(position);
		recordHolder.descriptionTxt.setText(localidad.getDescripcion());
		recordHolder.logo.setImageBitmap(localidad.getCliente().getLogo());
		recordHolder.categoriaTxt.setText(localidad.getCategoria());
		recordHolder.direccionTxt.setText(localidad.getDireccion());
		recordHolder.distanciaTxt.setText(localidad.getDistancia().toString() + "km");
		return row;

	}

	static class RecordHolder {
		TextView descriptionTxt;
		TextView categoriaTxt;
		TextView direccionTxt;
		TextView distanciaTxt;
		ImageView logo;

	}

}
