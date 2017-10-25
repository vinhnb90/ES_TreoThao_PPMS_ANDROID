package com.es.tungnv.measuredistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.tungnv.views.R;

public class MtdAdapterMenu extends ArrayAdapter<String>{

	private Context context;
	private String[] listMenu;
	private int[] img;
	
	public MtdAdapterMenu(Context context, int resource, String[] listMenu, int[] img) {
		super(context, resource, listMenu);
		this.context = context;
		this.listMenu = listMenu;
		this.img = img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.essp_mtd_listview_menu, parent, false);
		}
		ImageView ivMenu = (ImageView)convertView.findViewById(R.id.ivMenu);
		TextView tvMenu = (TextView)convertView.findViewById(R.id.tvMenu);
		ivMenu.setImageResource(img[position]);
		tvMenu.setText(listMenu[position]);
		return convertView;
	}
	
}
