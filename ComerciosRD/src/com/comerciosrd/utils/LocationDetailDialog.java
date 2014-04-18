package com.comerciosrd.utils;



import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comerciosrd.map.R;

@SuppressLint("NewApi")
public class LocationDetailDialog extends DialogFragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.list_activity, container, false);
        
    }
}
