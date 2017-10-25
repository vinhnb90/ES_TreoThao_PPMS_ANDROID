package com.es.tungnv.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.views.R;

import java.util.ArrayList;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsQuanLySoFragment extends Fragment{

    private View rootView;
    private Button btXoa;
    private ListView lvSo;

    private GcsSqliteConnection connection;
    private ArrayList<String> arrSo;
    private ArrayAdapter<String> adapterSo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.gcs_fragment_qls, null);
            connection = new GcsSqliteConnection(this.getActivity());
            initComponent(rootView);
            initData();
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView) {
        btXoa = (Button) rootView.findViewById(R.id.gcs_fragment_qls_btXoa);
        lvSo = (ListView) rootView.findViewById(R.id.gcs_fragment_qls_lvSo);

        btXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final int len = lvSo.getCount();
                    if(len > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có chắc chắn muốn xóa?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int countDelete = 0;
                                    SparseBooleanArray checked = lvSo.getCheckedItemPositions();
                                    for (int i = len - 1; i >= 0; i--) {
                                        if (checked.get(i)) {
                                            String TEN_FILE = arrSo.get(i);
                                            if (connection.deleteSo(TEN_FILE) != -1) {
                                                countDelete++;
                                            }
                                        }
                                    }
                                    if (countDelete > 0) {
                                        initData();
                                    }
                                } catch (Exception ex) {
                                    Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi xóa sổ", Color.WHITE, "OK", Color.RED);
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi xóa sổ", Color.WHITE, "OK", Color.RED);
                }
            }
        });

    }

    private void initData() {
        try{
            arrSo = new ArrayList<>();
            Cursor c = connection.getTenFile();
            if(c.moveToFirst()){
                do {
                    arrSo.add(c.getString(0));
                } while (c.moveToNext());
                adapterSo = new ArrayAdapter<>(this.getActivity(), R.layout.gcs_row_qlso, arrSo);
                lvSo.setAdapter(adapterSo);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu", Color.WHITE, "OK", Color.RED);
        }
    }

}
