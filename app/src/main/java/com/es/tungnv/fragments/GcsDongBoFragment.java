package com.es.tungnv.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;
import com.es.tungnv.utils.SingleMediaScanner;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.GcsAsyncCallWS;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsDongBoFragment extends Fragment{

    private View rootView;
    private GcsSqliteConnection connection;

    private LinearLayout llLogin;
    private RelativeLayout rlDongBo;
    private EditText etUser;
    private EditText etPass;
    private Button btLogin, btDongBo;
    private ListView lvSo;
    private RadioButton rbGetData, rbSentData;

    private String[] arr_so_server;
    private List<String> list_so;
    private List<String> same_items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            rootView = inflater.inflate(R.layout.gcs_fragment_dongbo, null);
            connection = new GcsSqliteConnection(getActivity().getApplicationContext());
            initComponent(rootView);
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView) {
        llLogin = (LinearLayout) rootView.findViewById(R.id.gcs_include_login_llLogin);
        rlDongBo = (RelativeLayout) rootView.findViewById(R.id.gcs_include_transferdata_rlDongBo);
        etUser = (EditText) rootView.findViewById(R.id.gcs_include_login_etUser);
        etPass = (EditText) rootView.findViewById(R.id.gcs_include_login_etPass);
        btLogin = (Button) rootView.findViewById(R.id.gcs_include_login_btLogin);
        btDongBo = (Button) rootView.findViewById(R.id.gcs_include_transferdata_btDongBo);
        lvSo = (ListView) rootView.findViewById(R.id.gcs_include_transferdata_lvSo);
        rbGetData = (RadioButton) rootView.findViewById(R.id.gcs_include_transferdata_rbGetData);
        rbSentData = (RadioButton) rootView.findViewById(R.id.gcs_include_transferdata_rbSentData);

        if(!TextUtils.isEmpty(GcsCommon.getMaNvgcs())) {
            llLogin.setVisibility(View.GONE);
            rlDongBo.setVisibility(View.VISIBLE);
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    etUser.setError(null);
                    etPass.setError(null);
                    String sUser = etUser.getText().toString().trim();
                    String sPass = etPass.getText().toString().trim();
                    if(TextUtils.isEmpty(sUser)){
                        etUser.setError("Bạn chưa nhập user");
                        etUser.requestFocus();
                    } else if(TextUtils.isEmpty(sPass)){
                        etPass.setError("Bạn chưa nhập password");
                        etPass.requestFocus();
                    } else {
                        String result = (new GcsAsyncCallWS()).WS_USER_LOGIN_CALL(sUser, sPass);
                        if (result.equals("SUCCESS")) {
                            GcsCommon.setMaNvgcs(sUser);
                            llLogin.setVisibility(View.GONE);
                            rlDongBo.setVisibility(View.VISIBLE);
                            Toast.makeText(GcsDongBoFragment.this.getActivity(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Common.showAlertDialogGreen(GcsDongBoFragment.this.getActivity(), "Lỗi", Color.WHITE, "Đăng nhập thất bại", Color.WHITE, "OK", Color.WHITE);
                        }
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi đăng nhập", Color.WHITE, "OK", Color.RED);
                }
            }
        });

        btDongBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(rbGetData.isChecked()){
                        getData();
                    } else if(rbSentData.isChecked()){
                        sendData();
                    }
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ", Color.WHITE, "OK", Color.RED);
                }
            }
        });

        rbGetData.post(new Runnable() {
            @Override
            public void run() {
                rbGetData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            SyncAct("GET_DATA");
                        }
                    }
                });
            }
        });

        rbSentData.post(new Runnable() {
            @Override
            public void run() {
                rbGetData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            SyncAct("SEND_DATA");
                        }
                    }
                });
            }
        });

    }

    private void getData(){
        try {
            File file_db = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("/ESGCS.s3db").toString());
            RecreateConnSqlite();

            final String[] array_selected = spnChonSo_GetSelectedItem(lvSo);

            if(file_db.exists()){
                list_so = connection.GetAllSo();
                same_items = Common.GetSameItems(array_selected, (String[])list_so.toArray(new String[0]));
            }else{
                list_so = new ArrayList<String>();
                same_items = null;
            }

            if(same_items != null && same_items.size() > 0){
                String str_list_so = "\n";
                for(String item : same_items){
                    str_list_so += item + "\n";
                }
                new AlertDialog.Builder(GcsDongBoFragment.this.getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Lấy sổ")
                        .setMessage(new StringBuilder("Sổ lấy về bị trùng:\n").append(str_list_so).append("\nBạn có muốn xóa sổ cũ trên máy để tải sổ mới về ?").toString())
                        .setPositiveButton("Đồng ý",
                                new DialogInterface.OnClickListener() {
                                    @SuppressLint("SimpleDateFormat")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        try{
                                            backupDB();

                                            String result = (new GcsAsyncCallWS()).WS_DOWNLOAD_SO_GCS_CALL( GcsCommon.getMaDviqly(), GcsCommon.getMaNvgcs(), array_selected, "ESGCS.zip");

                                            File fileDST = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/ESGCS.zip").toString());
                                            if(!fileDST.exists())  {
                                                throw new Exception ("Tải về thất bại");
                                            } else {
                                                unpackZip("ESGCS.zip", new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/").toString());
                                                fileDST.delete();
                                                File fPhotos = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).toString());
                                                String[] sFile = fPhotos.list();
                                                for (String sF : sFile) {
                                                    File fDBPhoto = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/").append(sF).toString());
                                                    if(fDBPhoto.isFile()){
                                                        File fDBTemp = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_TEMP_PATH).append("/ESGCS.s3db").toString());
                                                        GcsCommon.copy(fDBPhoto, fDBTemp);
                                                        fDBPhoto.delete();
                                                    }
                                                }
                                            }

                                            File file_db = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("/ESGCS.s3db").toString());
                                            if(file_db.exists()){
                                                GcsSqliteConnection temp_conn = new GcsSqliteConnection(GcsDongBoFragment.this.getActivity().getApplicationContext(), "ESGCS.s3db", new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_TEMP_PATH).toString());

                                                connection.GetWritableDatabase();
                                                connection.BeginTransaction();

                                                //->Xóa sổ bị trùng trên máy
                                                connection.delete_array_so((String[])same_items.toArray(new String[0]));

                                                //-> Lấy dữ liệu trong db vừa tải từ server cho vào ArrayList
                                                ArrayList<LinkedHashMap<String, String>> list = temp_conn.ConvertDataFromSqliteToArrayList("SELECT * FROM GCS_CHISO_HHU"); // TODO ĐỔI LẠI CONNECNT TỚI db tạm

                                                //-> chèn sổ mới
                                                connection.insert_arraylist_to_db(array_selected ,list , GcsCommon.getMaDql());

                                                connection.SetTransactionSuccessful();
                                            }
                                            else{
                                                // nếu trên máy chưa có db thì copy db từ folder temp luôn
                                                File file_src = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_TEMP_PATH).append("/ESGCS.s3db").toString());
                                                GcsCommon.copy(file_src, file_db);
                                            }
                                            Common.showAlertDialogGreen(getActivity(), "Nhận sổ", Color.WHITE, result, Color.WHITE, "OK", Color.WHITE);
                                        }
                                        catch(Exception e){
                                            Common.showAlertDialogGreen(getActivity(), "Nhận sổ", Color.RED, "Nhận sổ thất bại", Color.WHITE, "OK", Color.RED);
                                        }
                                        finally {
                                            connection.EndTransaction();
                                        }
                                    }

                                })
                        .setNegativeButton("Không", null)
                        .show().getWindow().setGravity(Gravity.BOTTOM);
            }
            // ko trùng: tải về luôn
            else{
                String result = (new GcsAsyncCallWS()).WS_DOWNLOAD_SO_GCS_CALL(
                        GcsCommon.getMaDviqly(), GcsCommon.getMaNvgcs(), array_selected, "ESGCS.zip");
                // nếu ko có file tải về thì thông báo tải về thất bại
                File fileDST = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/ESGCS.zip").toString());
                if(!fileDST.exists())  {
                    throw new Exception ("Tải về thất bại");
                } else {
                    unpackZip("ESGCS.zip", new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/").toString());
                    fileDST.delete();
                    File fPhotos = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).toString());
                    String[] sFile = fPhotos.list();
                    for (String sF : sFile) {
                        File fDBPhoto = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH).append("/").append(sF).toString());
                        if(fDBPhoto.isFile()){
                            File fDBTemp = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_TEMP_PATH + "/ESGCS.s3db");
                            GcsCommon.copy(fDBPhoto, fDBTemp);
                            fDBPhoto.delete();
                        }
                    }
                }

                if(file_db.exists()){
                    GcsSqliteConnection temp_conn = new GcsSqliteConnection(GcsDongBoFragment.this.getActivity().getApplicationContext(), "ESGCS.s3db", Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_TEMP_PATH);

                    try{
                        connection.BeginTransaction();

                        //-> Lấy dữ liệu trong db vừa tải từ server cho vào ArrayList
                        ArrayList<LinkedHashMap<String, String>> list = temp_conn.ConvertDataFromSqliteToArrayList("SELECT * FROM GCS_CHISO_HHU"); // TODO ĐỔI LẠI CONNECNT TỚI db tạm

                        //-> chèn sổ mới
                        connection.insert_arraylist_to_db(array_selected,list, GcsCommon.getMaDql());

                        connection.SetTransactionSuccessful();
                    }
                    catch(Exception e){
                        throw new Exception();
                    }
                    finally {
                        connection.EndTransaction();
                    }
                }
                else{
                    // nếu trên máy chưa có db thì copy db từ folder temp luôn
                    File file_src = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_TEMP_PATH).append("/ESGCS.s3db").toString());
                    GcsCommon.copy(file_src, file_db);
                }
                Common.showAlertDialogGreen(getActivity(), "Nhận sổ", Color.WHITE, result, Color.WHITE, "OK", Color.WHITE);
            }
            GcsCommon.LoadFolder(getActivity());
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Nhận sổ", Color.RED, "Nhận sổ thất bại", Color.WHITE, "OK", Color.RED);
        }
    }

    private void sendData(){
        try {
            String[] array_selected = spnChonSo_GetSelectedItem(lvSo);
            ArrayList<String> arr_selected = new ArrayList<String>();
            for (String s : array_selected) {
                arr_selected.add(s.contains("_")?s.split("_")[0].toString():s.replace(".",",").split(",")[0].toString());
            }
            GcsCommon.scanZipPhotoFiles(arr_selected);

            String result = (new GcsAsyncCallWS()).WS_UPLOAD_SO_GCS_CALL(GcsCommon.getMaDviqly(), GcsCommon.getMaNvgcs(), array_selected);
            if(result.contains("ERROR")){
                Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ", Color.WHITE, "OK", Color.RED);
            }
            else{
                // Xóa file
                File file = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/ESGCS/ESGCS.zip").toString());
                if(file.exists()){
                    file.delete();
                }
                File file2 = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/ESGCS/Photo/").toString());
                String [] listF = file2.list();
                for (int i = 0; i < listF.length; i++) {
                    if(listF[i].contains(".zip")){
                        File file3 = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/ESGCS/Photo/").append(listF[i]).toString());
                        if(file3.exists()){
                            file3.delete();
                        }
                    }
                }
                Common.showAlertDialogGreen(getActivity(), "Gửi sổ", Color.WHITE, result, Color.WHITE, "OK", Color.WHITE);
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Gửi sổ", Color.RED, "Gửi sổ thất bại", Color.WHITE, "OK", Color.RED);
        }
    }

    private void backupDB(){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            String dateBackup = sdf.format(new Date());
            String sSRC = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("ESGCS.s3db").toString();
            String sDST = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_BACKUP_PATH).append("ESGCS_").append(dateBackup).append(".s3db").toString();
            File fSRC = new File(sSRC);
            File fDST = new File(sDST);

            if(connection.countBanGhi() > 0) {
                Common.copy(fSRC, fDST);
                new SingleMediaScanner(GcsDongBoFragment.this.getActivity(), fDST);

                File fBackup = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_BACKUP_PATH).toString());
                String[] arrBackup = fBackup.list();
                if(arrBackup.length > 20) {
                    File fileMin = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(
                            GcsConstantVariables.PROGRAM_BACKUP_PATH).append(arrBackup[0]).toString());
                    long min = new Date(fileMin.lastModified()).getTime();
                    for (String sDate : arrBackup) {
                        File fDate = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                                .append(GcsConstantVariables.PROGRAM_BACKUP_PATH).append(sDate).toString());
                        Date lastModDate = new Date(fDate.lastModified());
                        if(min > lastModDate.getTime()){
                            min = lastModDate.getTime();
                            fileMin = fDate;
                        }
                    }
                    if(fileMin != null && fileMin.exists()) {
                        fileMin.delete();
                    }
                }
            }
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi sao lưu dữ liệu", Toast.LENGTH_LONG).show();
        }
    }

    private boolean unpackZip(String zipname, String path) {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String[] spnChonSo_GetSelectedItem(ListView lvChonSo){
        List<String> list_selected = new ArrayList<String>();
        ArrayAdapter<String> dataAdapter = (ArrayAdapter<String>) lvChonSo.getAdapter();
        SparseBooleanArray ck = lvChonSo.getCheckedItemPositions();
        for (int i = 0; i < lvChonSo.getCount(); i++) {
            if(ck.get(i)){
                list_selected.add(dataAdapter.getItem(i));
            }
        }
        return list_selected.toArray(new String[0]);
    }

    private void SyncAct(String Action){
        RecreateConnSqlite();

        if(Action.contains("SEND_DATA")){
            spnChonSo_GetData(lvSo, "SEND_DATA");
        }
        else {
            spnChonSo_GetData(lvSo, "GET_DATA");
        }

        File file_db = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("/ESGCS.s3db").toString());
        new SingleMediaScanner(this.getActivity(), file_db);
    }

    private void spnChonSo_GetData(ListView lvChonSo, String Action) {
        arr_so_server = (new GcsAsyncCallWS()).WS_GET_TEN_FILE_OF_NV_CALL(GcsCommon.getMaDviqly(), GcsCommon.getMaNvgcs());
        File file_db = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_DATA_PATH).append("/ESGCS.s3db").toString());
        List<String> lvdata = null;
        if(file_db.exists()){
            list_so = connection.GetAllSo();
            same_items = Common.GetSameItems(arr_so_server, (String[])list_so.toArray(new String[0]));
        }else{
            list_so = new ArrayList<String>();
            same_items = null;
        }

        if(Action.contains("GET_DATA")){
            lvdata = Common.ConvertArrayString2List(arr_so_server);
        } else {
            lvdata = same_items;
        }

        java.util.Collections.sort(lvdata);
        try {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GcsDongBoFragment.this.getActivity(), R.layout.gcs_row_qlso, lvdata);
            lvChonSo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvChonSo.setAdapter(dataAdapter);

        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Chưa có dữ liệu trong thư mục", Color.WHITE, "OK", Color.RED);
        }
    }

    private void RecreateConnSqlite(){
        if(connection != null){
            connection.close();
        }
        connection = null;
        connection = new GcsSqliteConnection(GcsDongBoFragment.this.getActivity());
    }

}
