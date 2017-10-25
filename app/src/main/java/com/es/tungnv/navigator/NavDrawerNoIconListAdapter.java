package com.es.tungnv.navigator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.views.GCSMainActivity;
import com.es.tungnv.views.R;

import java.util.ArrayList;

public class NavDrawerNoIconListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> navDrawerItems;

	public NavDrawerNoIconListAdapter(Context context, ArrayList<String> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item_no_icon,null);
        }
		try {
			TextView tvSo = (TextView) convertView.findViewById(R.id.tvSo);
			TextView tvTinhTrang = (TextView) convertView.findViewById(R.id.tvTinhTrang);
			ImageView ivTick = (ImageView) convertView.findViewById(R.id.ivTick);

			tvSo.setText(navDrawerItems.get(position));
//			int daghi = GCSMainActivity.connection.getDaGhi(navDrawerItems.get(position));
			int daghi = GcsCommon.getDaGhi(GCSMainActivity.connection.getCSoAndTTR(navDrawerItems.get(position)));
			int chuaghi = GCSMainActivity.connection.getSoLuongBanGhi(navDrawerItems.get(position));
			tvTinhTrang.setText("Đã ghi " + daghi + "/" + chuaghi);
			if(daghi == chuaghi){
				ivTick.setVisibility(View.VISIBLE);
//				tvSo.setTextColor(Color.parseColor("#FF6600"));
//				tvTinhTrang.setTextColor(Color.parseColor("#FF6600"));
			} else {
				ivTick.setVisibility(View.GONE);
//				tvSo.setTextColor(Color.parseColor("#FFFFFF"));
//				tvTinhTrang.setTextColor(Color.parseColor("#FFFFFF"));
			}
		} catch(Exception ex) {
			ex.toString();
		}
        return convertView;
	}

}
