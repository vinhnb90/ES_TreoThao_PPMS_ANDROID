package com.esolutions.esloginlib.lib;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.esolutions.esloginlib.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DepartUpdateFragment<T> extends ModuleFragment {
    private List<T> mListDepart = new ArrayList<>();
    private DepartViewEntity viewEntity;
    private boolean isShowModule = false;

    public DepartUpdateFragment() {
        super();
    }

    public DepartUpdateFragment<T> setmListDepart(List<T> mListDepart) throws Exception {
        //check empty
        if (mListDepart.isEmpty())
            return this;


        //check to String override of T class
        //if we use method toString of class T and it return not object then class T really overrided toString method
        T tClass = (T) mListDepart.get(0).getClass();
        if ((mListDepart.get(0).getClass().getClass().getMethod("toString").getDeclaringClass() == Object.class))
            throw new RuntimeException("Class " + mListDepart.get(0).getClass().getSimpleName() + " must be override method toString to show content on spinner!");


        this.mListDepart.clear();
        this.mListDepart.addAll(mListDepart);
        fillData();
        return this;
    }

    public DepartViewEntity getViewEntity() {
        return viewEntity;
    }

    public boolean isShowModule() {
        return isShowModule;
    }

    public DepartUpdateFragment<T> setShowModule(boolean showModule) {
        isShowModule = showModule;
        return this;
    }

    public DepartUpdateFragment setViewEntity(DepartViewEntity viewEntity) {
        this.viewEntity = viewEntity;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = null;
        try {
            if (viewEntity != null)
                viewRoot = viewEntity.getViewLayout();
            else
                viewRoot = inflater.inflate(R.layout.fragment_login, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fillData();
        return viewRoot;
    }

    private void fillData() {
        ArrayAdapter arrayAdapter = new ArrayAdapter<T>(viewEntity.getContext(), R.layout.row_spin_type_1, R.id.tv_spin, mListDepart);

        //set full screen rows
        Display display = ((Activity) viewEntity.getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        viewEntity.getSpDvi().setDropDownWidth(size.x);
        viewEntity.getSpDvi().setGravity(RelativeLayout.CENTER_HORIZONTAL);
        viewEntity.getSpDvi().setAdapter(arrayAdapter);
        viewEntity.getSpDvi().invalidate();
    }

    public List<T> getmListDepart() {
        return mListDepart;
    }
}