package com.es.tungnv.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;

/**
 * Created by TUNGNV on 3/16/2016.
 */
public class TthtCauHinhFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.ttht_fragment_cauhinh, null);
            initComponent(rootView);
        } catch (Exception ex) {
            ex.toString();
        }
        return rootView;
    }

    private void initComponent(View rootView){

    }

    @Override
    public void onClick(View v) {

    }
}
