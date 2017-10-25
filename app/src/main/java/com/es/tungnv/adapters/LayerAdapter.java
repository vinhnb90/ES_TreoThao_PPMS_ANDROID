package com.es.tungnv.adapters;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.es.tungnv.views.EsspDrawImageActivity;
import com.es.tungnv.views.R;

@SuppressLint("DefaultLocale")
public class LayerAdapter extends ArrayAdapter<Integer> {

	private final Context context;

	public LayerAdapter(Context context, int resource, ArrayList<Integer> objects) {
		super(context, resource, objects);
		this.context = context;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.essp_row_layer, null);
		final int val = getItem(position);

		ImageView imvLayer = (ImageView)convertView.findViewById(R.id.essp_row_layer_iv_layer);
		
		imvLayer.setImageResource(val);

		if(position == EsspDrawImageActivity.pos_layer_show){
			convertView.setBackgroundColor(Color.CYAN);
		} else {
			convertView.setBackgroundColor(Color.GRAY);
		}
		
		return convertView;

	}
	
}
