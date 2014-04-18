package com.comerciosrd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comerciosrd.map.R;
import com.comerciosrd.pojos.Cliente;

import java.util.ArrayList;

public class ClientsGridAdapter extends ArrayAdapter<Cliente> {
	Context context;
	int layoutResourceId;
	ArrayList<Cliente> data = new ArrayList<Cliente>();

	public ClientsGridAdapter(Context context, int layoutResourceId,ArrayList<Cliente> data) {
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
			recordHolder.txtTitle = (TextView) row.findViewById(R.id.category_name);
			recordHolder.imageCategoria = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(recordHolder);
		} else {
			recordHolder = (RecordHolder) row.getTag();
		}

		Cliente cliente = data.get(position);
		recordHolder.txtTitle.setText(cliente.getNombreCliente());
		recordHolder.imageCategoria.setImageBitmap(cliente.getLogo());
		return row;

	}

	static class RecordHolder {
		TextView txtTitle;
		ImageView imageCategoria;

	}

}
