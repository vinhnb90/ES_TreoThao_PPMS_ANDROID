package com.es.tungnv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.es.tungnv.fragments.EsspMainFragment;
import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 4/14/2016.
 */
public class EsspThietBiAdapter extends ArrayAdapter<LinkedHashMap<String, String>> {

    private final Context context;

    public EsspThietBiAdapter(Context context, int resource, ArrayList<LinkedHashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.essp_row_thietbi, null);
        final LinkedHashMap<String, String> val = getItem(position);

        TextView tvSTT = (TextView)convertView.findViewById(R.id.tvSTT);
        TextView tvLoaiTB = (TextView)convertView.findViewById(R.id.tvLoaiTB);
        TextView tvCongSuat = (TextView)convertView.findViewById(R.id.tvCongSuat);
        TextView tvSoLuong = (TextView)convertView.findViewById(R.id.tvSoLuong);
        ImageButton btnEdit = (ImageButton)convertView.findViewById(R.id.ibEdit);
        ImageButton btnClear = (ImageButton)convertView.findViewById(R.id.ibClear);

        tvSTT.setText(String.valueOf(position + 1));
        tvLoaiTB.setText(val.get("LOAI_TBI"));
        tvCongSuat.setText(val.get("CONG_SUAT"));
        tvSoLuong.setText(val.get("SO_LUONG"));

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EsspMainFragment.setDataOnEditText(context, val, position);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EsspMainFragment.deleteTB(context, Integer.parseInt(val.get("ID")), position);
            }
        });

        return convertView;

    }

}
