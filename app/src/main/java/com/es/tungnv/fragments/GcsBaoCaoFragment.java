package com.es.tungnv.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.es.tungnv.tabs.GcsDnttActivity;
import com.es.tungnv.tabs.GcsSlbtActivity;
import com.es.tungnv.tabs.GcsTtbtActivity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.views.R;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsBaoCaoFragment extends Fragment{

    private View rootView;
    private TabHost tabHost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.gcs_fragment_bcth, null);
            initComponent(rootView);
            setTabs();
            updateTab("SLBT", R.id.tabSLBT);
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView) {
        tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                try {
                    if (tabId.equals("SLBT")) {
                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Sản lượng bất thường");
                        updateTab("SLBT", R.id.tabSLBT);
                    } else if (tabId.equals("TTBT")) {
                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Trạng thái bất thường");
                        updateTab("TTBT", R.id.tabTTBT);
                    } else {
                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Điện năng tổn thất");
                        updateTab("DNTT", R.id.tabDNTT);
                    }
                } catch(Exception ex) {
                    ex.toString();
                }
            }
        });
    }

    private void setTabs() {
        try {
            addTab("SLBT", "SL bất thường", R.id.tabSLBT);
            addTab("TTBT", "TT bất thường", R.id.tabTTBT);
            addTab("DNTT", "ĐN tổn thất", R.id.tabDNTT);
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo tab"
                    , Color.WHITE, "OK", Color.RED);
        }
    }

    private void addTab(String ID, String labelId, int layout) {
        try {
            TabHost.TabSpec spec = tabHost.newTabSpec(ID);

            View tabIndicator = LayoutInflater.from(this.getActivity()).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
            TextView title = (TextView) tabIndicator.findViewById(R.id.title);
            title.setText(labelId);

            spec.setIndicator(tabIndicator);
            spec.setContent(layout);
            tabHost.addTab(spec);
        } catch(Exception ex) {
        }
    }

    Fragment frag = null;
    private void updateTab(String tabId, int placeholder) {
//        FragmentManager fragmentManager = getActivity().getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentManager fm = getFragmentManager();
        if (tabId.equals("SLBT")) {
            frag = new GcsSlbtActivity();
        } else if (tabId.equals("TTBT")) {
            frag = new GcsTtbtActivity();
        } else {
            frag = new GcsDnttActivity();
        }
        fm.beginTransaction().replace(placeholder, frag, tabId).commit();
    }

}
