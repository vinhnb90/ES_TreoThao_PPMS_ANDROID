package com.es.tungnv.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.entity.GcsConfigInfo;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;
import com.es.tungnv.views.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsCauHinhFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private View rootView;
    private Button btGetBackup, btLuu;
    private CheckBox cbSl200, cbSlPhanTram, cbSlkWh;
    private SeekBar sbTren, sbDuoi;
    private EditText etTren, etDuoi, etIP;
    private TextView tvTren, tvDuoi;

    private int k = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.gcs_fragment_cauhinh, null);
            initComponent(rootView);
            initData();
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView) {
        btGetBackup = (Button) rootView.findViewById(R.id.gcs_fragment_cauhinh_bt_get_backup);
        btLuu = (Button) rootView.findViewById(R.id.gcs_fragment_cauhinh_bt_luu);
        cbSl200 = (CheckBox) rootView.findViewById(R.id.gcs_fragment_cauhinh_ck_sl200);
        cbSlPhanTram = (CheckBox) rootView.findViewById(R.id.gcs_fragment_cauhinh_ck_slphantram);
        cbSlkWh = (CheckBox) rootView.findViewById(R.id.gcs_fragment_cauhinh_ck_slkwh);
        sbTren = (SeekBar) rootView.findViewById(R.id.gcs_fragment_cauhinh_sb_tren);
        sbDuoi = (SeekBar) rootView.findViewById(R.id.gcs_fragment_cauhinh_sb_duoi);
        etTren = (EditText) rootView.findViewById(R.id.gcs_fragment_cauhinh_et_tren);
        etDuoi = (EditText) rootView.findViewById(R.id.gcs_fragment_cauhinh_et_duoi);
        etIP = (EditText) rootView.findViewById(R.id.gcs_fragment_cauhinh_et_ip);
        tvTren = (TextView) rootView.findViewById(R.id.gcs_fragment_cauhinh_tv_tren);
        tvDuoi = (TextView) rootView.findViewById(R.id.gcs_fragment_cauhinh_tv_duoi);

        btGetBackup.setOnClickListener(this);
        btLuu.setOnClickListener(this);
        cbSl200.setOnCheckedChangeListener(this);
        cbSlPhanTram.setOnCheckedChangeListener(this);
        cbSlkWh.setOnCheckedChangeListener(this);
        sbTren.setOnSeekBarChangeListener(this);
        sbDuoi.setOnSeekBarChangeListener(this);

        if(!cbSlPhanTram.isChecked()) {
            sbTren.setEnabled(false);
            sbDuoi.setEnabled(false);
        }
        if(!cbSlkWh.isChecked()) {
            etTren.setEnabled(false);
            etDuoi.setEnabled(false);
        }
    }

    private void initData(){
        try {
            GcsCommon.cfgInfo = GcsCommon.GetFileConfig();
            if (GcsCommon.cfgInfo.isWarningEnable()) {
                cbSl200.setChecked(true);
            } else {
                cbSl200.setChecked(false);
            }
            if (GcsCommon.cfgInfo.isWarningEnable2()) {
                cbSlPhanTram.setChecked(true);
            } else {
                cbSlPhanTram.setChecked(false);
            }
            if (GcsCommon.cfgInfo.isWarningEnable3()) {
                cbSlkWh.setChecked(true);
            } else {
                cbSlkWh.setChecked(false);
            }
            sbTren.setProgress(GcsCommon.cfgInfo.getVuotDinhMuc()/k);
            sbDuoi.setProgress(GcsCommon.cfgInfo.getDuoiDinhMuc()/k);
            tvTren.setText(new StringBuilder(GcsCommon.cfgInfo.getVuotDinhMuc()).append("%"));
            tvDuoi.setText(new StringBuilder(GcsCommon.cfgInfo.getDuoiDinhMuc()).append("%"));
            etTren.setText(String.valueOf(GcsCommon.cfgInfo.getVuotDinhMuc2()));
            etDuoi.setText(String.valueOf(GcsCommon.cfgInfo.getDuoiDinhMuc2()));
            etIP.setText(String.valueOf(GcsCommon.cfgInfo.getIP_SERVICE()));
        } catch(Exception ex) {
            Toast.makeText(getActivity(), "Error init data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gcs_fragment_cauhinh_bt_get_backup:
                showDialogBackup();
                break;

            case R.id.gcs_fragment_cauhinh_bt_luu:
                saveConfig();
                break;
        }
    }

    private void saveConfig() {
        GcsConfigInfo config = new GcsConfigInfo(cbSlkWh.isChecked(), cbSl200.isChecked(), cbSlPhanTram.isChecked(),
                sbTren.getProgress()*k, sbDuoi.getProgress()*k,
                Integer.parseInt(etTren.getText().toString().isEmpty()?"0":etTren.getText().toString()),
                Integer.parseInt(etDuoi.getText().toString().isEmpty()?"0":etDuoi.getText().toString()),
                etIP.getText().toString(), GcsCommon.cfgInfo.getDVIQLY(), GcsCommon.cfgInfo.getVERSION());
        String filePath = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PATH + GcsConstantVariables.CFG_FILENAME;
        File cfgFile = new File(filePath);
        if(GcsCommon.CreateFileConfig(config, cfgFile)){
            GcsCommon.cfgInfo = config;
            Toast.makeText(getActivity(), "Lưu cấu hình thành công", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.gcs_fragment_cauhinh_ck_sl200:
                if(isChecked) {
                    cbSlkWh.setChecked(false);
                    cbSlPhanTram.setChecked(false);
                    cbSl200.setChecked(true);
                    sbTren.setEnabled(false);
                    sbDuoi.setEnabled(false);
                    etTren.setEnabled(false);
                    etDuoi.setEnabled(false);
                }
                break;

            case R.id.gcs_fragment_cauhinh_ck_slphantram:
                if(isChecked) {
                    cbSlkWh.setChecked(false);
                    cbSlPhanTram.setChecked(true);
                    cbSl200.setChecked(false);
                    sbTren.setEnabled(true);
                    sbDuoi.setEnabled(true);
                    etTren.setEnabled(false);
                    etDuoi.setEnabled(false);
                }
                break;

            case R.id.gcs_fragment_cauhinh_ck_slkwh:
                if(isChecked) {
                    cbSlkWh.setChecked(true);
                    cbSlPhanTram.setChecked(false);
                    cbSl200.setChecked(false);
                    sbTren.setEnabled(false);
                    sbDuoi.setEnabled(false);
                    etTren.setEnabled(true);
                    etDuoi.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.gcs_fragment_cauhinh_sb_tren:
                tvTren.setText(new StringBuilder(progress*k).append("%"));
                break;

            case R.id.gcs_fragment_cauhinh_sb_duoi:
                tvDuoi.setText(new StringBuilder(progress*k).append("%"));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void showDialogBackup(){
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_backup);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            dialog.setCanceledOnTouchOutside(false);

            ListView lvBackup = (ListView) dialog.findViewById(R.id.gcs_dialog_backup_lv_backup);

            String sBackup = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_BACKUP_PATH;
            File fBackup = new File(sBackup);
            String[] lstBackup = fBackup.list();
            ArrayList<LinkedHashMap<String, String>> arrBackup = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (String backup : lstBackup){
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("NAME", backup);
                map.put("DATE", sdf.format(new Date((new File(sBackup + backup)).lastModified())));
                arrBackup.add(map);
            }

            AdapterBackup adapterBackup= new AdapterBackup(GcsCauHinhFragment.this.getActivity(), R.layout.gcs_row_backup, arrBackup);
            lvBackup.setAdapter(adapterBackup);

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị dữ liệu backup", Color.WHITE, "OK", Color.RED);
        }
    }

    class AdapterBackup extends ArrayAdapter<LinkedHashMap<String, String>> {

        Context context;
        int resource;
        List<LinkedHashMap<String, String>> objects;

        public AdapterBackup(Context context, int resource, List<LinkedHashMap<String, String>> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gcs_row_backup, null);
            final LinkedHashMap<String, String> val = getItem(position);

            TextView tvBackupName = (TextView) convertView.findViewById(R.id.gcs_row_backup_tvBackupName);
            TextView tvDate = (TextView) convertView.findViewById(R.id.gcs_row_backup_tvDate);
            ImageButton ibDown = (ImageButton) convertView.findViewById(R.id.gcs_row_backup_ibDown);

            tvBackupName.setText(val.get("NAME"));
            tvDate.setText(val.get("DATE"));

            ibDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        File fSRC = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_BACKUP_PATH).append(val.get("NAME")).toString());
                        File fDST = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("ESGCS.s3db").toString());
                        Common.copy(fSRC, fDST);
                        Toast.makeText(getActivity(), "Lấy lại dữ liệu thành công", Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(getActivity(),
                                "Lỗi", Color.RED, "Lỗi lấy lại file backup", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            return convertView;
        }
    }

}
