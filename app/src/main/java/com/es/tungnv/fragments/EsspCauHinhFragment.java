package com.es.tungnv.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.views.R;

import java.io.File;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspCauHinhFragment extends Fragment implements View.OnClickListener{

    //region Khai báo biến
    private View rootView;
    private ImageButton ibSave, ibSend;
    private Spinner spVersion;
    private EditText etPhone, etNoiDung;
    private CheckBox cbFile;

    private String[] arrVersion = {"Hà Nội"};
    private String[] arrSymbolVersion = {"HN"};

    private EsspSqliteConnection connection;
    //endregion

    //region khởi tạo
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.essp_fragment_cauhinh, null);
            connection = EsspSqliteConnection.getInstance(EsspCauHinhFragment.this.getActivity());
            initComponent(rootView);
            initDataSpinner();
            return rootView;
        } catch(Exception ex) {
            Common.showAlertDialogGreen(EsspCauHinhFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView){
        ibSave = (ImageButton) rootView.findViewById(R.id.essp_fragment_cauhinh_ib_luu_phienban);
        ibSend = (ImageButton) rootView.findViewById(R.id.essp_fragment_cauhinh_ib_gui);
        spVersion = (Spinner) rootView.findViewById(R.id.essp_fragment_cauhinh_sp_phienban);
        etPhone = (EditText) rootView.findViewById(R.id.essp_fragment_cauhinh_etPhone);
        etNoiDung = (EditText) rootView.findViewById(R.id.essp_fragment_cauhinh_etNoiDung);
        etNoiDung = (EditText) rootView.findViewById(R.id.essp_fragment_cauhinh_etNoiDung);
        cbFile = (CheckBox) rootView.findViewById(R.id.essp_fragment_cauhinh_cbFile);

        ibSave.setOnClickListener(this);
        ibSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.essp_fragment_cauhinh_ib_luu_phienban:
                break;
            case R.id.essp_fragment_cauhinh_ib_gui:
                try {
                    if(Common.isNetworkOnline(EsspCauHinhFragment.this.getActivity())) {
                        String phone = etPhone.getText().toString();
                        String noiDung = etNoiDung.getText().toString();
                        etNoiDung.setError(null);
                        if (noiDung.isEmpty()) {
                            etNoiDung.setError("Bạn chưa nhập nội dung yêu cầu hỗ trợ");
                            etNoiDung.requestFocus();
                        } else {
                            StringBuilder sBody = new StringBuilder();
                            sBody.append("- Đơn vị: ").append(connection.getTenDviByMa(EsspCommon.getMaDviqly())).append("\n");
                            sBody.append("- Nhân viên KS: ").append(EsspCommon.getUSERNAME()).append("\n");
                            sBody.append("- Số điện thoại: ").append(phone).append("\n");
                            sBody.append("- Nội dung:\n").append(noiDung).append("\n");

                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL, EsspConstantVariables.EMAIL_SUPPORT);
                            i.putExtra(Intent.EXTRA_SUBJECT, "Lỗi chương trình KSCĐ trên MTB");
                            i.putExtra(Intent.EXTRA_TEXT, sBody.toString());

                            if (cbFile.isChecked()) {
                                String pathFolder = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                                        .append(EsspConstantVariables.PROGRAM_DATA_PATH).append(EsspConstantVariables.DATABASE_NAME).toString();
                                File fFolder = new File(pathFolder);
                                if (fFolder.exists()) {
                                    Uri path = Uri.fromFile(fFolder);
                                    i.putExtra(Intent.EXTRA_STREAM, path);
                                }
                            }
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(EsspCauHinhFragment.this.getActivity(), "Không có email của đội hỗ trợ.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Common.showAlertDialogGreen(EsspCauHinhFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Vui lòng kích hoạt mạng để sử dụng chức năng này", Color.WHITE, "OK", Color.WHITE);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }
    //endregion

    //region Khởi tạo dữ liệu
    private void initDataSpinner(){
        ArrayAdapter<String> adapterDvi = new ArrayAdapter<String>(EsspCauHinhFragment.this.getActivity(), android.R.layout.simple_list_item_1, arrVersion);
        spVersion.setAdapter(adapterDvi);
    }
    //endregion

}
