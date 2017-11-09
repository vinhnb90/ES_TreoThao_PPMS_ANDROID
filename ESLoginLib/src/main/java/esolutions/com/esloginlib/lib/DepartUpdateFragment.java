package esolutions.com.esloginlib.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import esolutions.com.esloginlib.R;


public class DepartUpdateFragment<T> extends ModuleFragment {
    private List<T> mListDepart = new ArrayList<>();
    private DepartViewEntity viewEntity;
    private boolean isShowModule = false;

    public DepartUpdateFragment() {
        super();
    }

    public DepartUpdateFragment<T> setmListDepart(List<T> mListDepart) {
        this.mListDepart.clear();
        this.mListDepart.addAll(mListDepart);
        fillData();
        return this;
    }

    public DepartViewEntity getViewEntity() throws Exception {
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
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int pixel = Math.round(size.x * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        viewEntity.getSpDvi().setDropDownWidth(pixel - 20);
        viewEntity.getSpDvi().setGravity(RelativeLayout.CENTER_HORIZONTAL);
        viewEntity.getSpDvi().setAdapter(arrayAdapter);
    }

    public List<T> getmListDepart() {
        return mListDepart;
    }
}