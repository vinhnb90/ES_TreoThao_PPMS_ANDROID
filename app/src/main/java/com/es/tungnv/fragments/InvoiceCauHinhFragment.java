package com.es.tungnv.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.es.tungnv.db.InvoiceSQLiteConnection;
import com.es.tungnv.entity.InvoiceConfigInfo;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.IntentIntegrator;
import com.es.tungnv.utils.IntentResult;
import com.es.tungnv.utils.InvoiceCommon;
import com.es.tungnv.utils.InvoiceConstantVariables;
import com.es.tungnv.utils.Security;
import com.es.tungnv.views.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by TUNGNV on 2/24/2016.
 */
public class InvoiceCauHinhFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Spinner spPhienBan;
    private EditText etMKCu, etMKMoi, etMKNhapLai;
    private ImageView ivLogo;
    private ImageButton ibChonAnh, ibLuuPhienBan, ibLuuMatKhau;

    private Security security;
    private InvoiceSQLiteConnection connection;

    public static String [] ARR_VERSION_NAME = {"Yên Bái", "Thái Bình"};
    public static String [] ARR_VERSION_SYMNOL = {"YB", "TB"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.invoice_fragment_cauhinh, null);
            connection = new InvoiceSQLiteConnection(InvoiceCauHinhFragment.this.getActivity());
            security = new Security();
            initComponent(rootView);
            setDataOnSpinner();

            String root = Environment.getExternalStorageDirectory().toString();
            File file = new File(root + "/" + InvoiceConstantVariables.PROGRAM_PATH + "logo.jpg");
            if(file.exists()){
                Bitmap bm = BitmapFactory.decodeFile(root + "/" + InvoiceConstantVariables.PROGRAM_PATH + "logo.jpg");
                Drawable d = new BitmapDrawable(getResources(), bm);
                ivLogo.setBackgroundDrawable(d);
            }
        } catch(Exception ex){
            Toast.makeText(getActivity().getApplicationContext(), "Error create view: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void initComponent(View rootView){
        spPhienBan = (Spinner) rootView.findViewById(R.id.invoice_fragment_cauhinh_sp_phienban);
        etMKCu = (EditText) rootView.findViewById(R.id.invoice_fragment_cauhinh_et_mk_cu);
        etMKMoi = (EditText) rootView.findViewById(R.id.invoice_fragment_cauhinh_et_mk_moi);
        etMKNhapLai = (EditText) rootView.findViewById(R.id.invoice_fragment_cauhinh_et_nhap_lai_mk);
        ivLogo = (ImageView) rootView.findViewById(R.id.invoice_fragment_cauhinh_iv_logo);
        ibChonAnh = (ImageButton) rootView.findViewById(R.id.invoice_fragment_cauhinh_ib_chon_anh);
        ibLuuPhienBan = (ImageButton) rootView.findViewById(R.id.invoice_fragment_cauhinh_ib_luu_phienban);
        ibLuuMatKhau = (ImageButton) rootView.findViewById(R.id.invoice_fragment_cauhinh_ib_luu_matkhau);

        ibChonAnh.setOnClickListener(this);
        ibLuuPhienBan.setOnClickListener(this);
        ibLuuMatKhau.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_fragment_cauhinh_ib_chon_anh:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                InvoiceCauHinhFragment.this.startActivityForResult(i, InvoiceConstantVariables.RESULT_LOAD_IMAGE);
                break;

            case R.id.invoice_fragment_cauhinh_ib_luu_phienban:
                String IP = InvoiceCommon.cfgInfo.getIP_SV_1();
                String VERSION = ARR_VERSION_SYMNOL[spPhienBan.getSelectedItemPosition()];
                if(!IP.equals("") && !VERSION.equals("")) {
                    if(saveConfig(IP, VERSION)){
                        InvoiceCommon.setVERSION(VERSION);
                        Toast.makeText(getActivity().getApplicationContext(), "Đã cấu hình phiên bản " + spPhienBan.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.invoice_fragment_cauhinh_ib_luu_matkhau:
                changePass();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        try {
            if(requestCode == InvoiceConstantVariables.RESULT_LOAD_IMAGE && resultCode == InvoiceCauHinhFragment.this.getActivity().RESULT_OK && null != data) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        Cursor cursor = InvoiceCauHinhFragment.this.getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bmPhoto = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath, options), 512, 265, false);

                        SaveImage(bmPhoto);
                        final Drawable d = new BitmapDrawable(getResources(), bmPhoto);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivLogo.setBackgroundDrawable(d);
                            }
                        });
                    }
                });
                t.start();
                t = null;
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(InvoiceCauHinhFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi chọn ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    public boolean saveConfig(String IP_SERVER, String VERSION){
        InvoiceConfigInfo config = new InvoiceConfigInfo(IP_SERVER, VERSION);
        String filePath = Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH + InvoiceConstantVariables.CFG_FILENAME;
        File cfgFile = new File(filePath);
        if(InvoiceCommon.CreateFileConfig(config, cfgFile, null)){
            InvoiceCommon.cfgInfo.setIP_SV_1(IP_SERVER);
            InvoiceCommon.cfgInfo.setVERSION(VERSION);
            return true;
        }
        return false;
    }

    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + InvoiceConstantVariables.PROGRAM_PATH);
        if(!myDir.exists())
            myDir.mkdirs();
        String fname = "logo.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataOnSpinner(){
        try{
            ArrayAdapter<String> adapterPhienBan = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ARR_VERSION_NAME);
            spPhienBan.setAdapter(adapterPhienBan);
            String PHIEN_BAN = InvoiceCommon.cfgInfo.getVERSION();
            for(int i = 0; i < ARR_VERSION_SYMNOL.length; i++){
                String pb = ARR_VERSION_SYMNOL[i];
                if(pb.equals(PHIEN_BAN)){
                    spPhienBan.setSelection(i);
                    return;
                }
            }
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi khởi tạo dữ liệu phiên bản: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void changePass(){
        try {
            String oldPass = etMKCu.getText().toString();
            String newPass = etMKMoi.getText().toString();
            String confPass = etMKNhapLai.getText().toString();
            if (oldPass.equals("")) {
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Bạn chưa nhập mật khẩu cũ", Color.WHITE, "OK", Color.WHITE);
            } else if (newPass.equals("")) {
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Bạn chưa nhập mật khẩu mới", Color.WHITE, "OK", Color.WHITE);
            } else if (confPass.equals("")) {
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Bạn chưa nhập lại mật khẩu mới", Color.WHITE, "OK", Color.WHITE);
            } else if(!security.encrypt_Base64(oldPass).equals(connection.getMkTHUNGAN(InvoiceCommon.getUSERNAME()))) {
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Bạn nhập sai mật khẩu cũ", Color.WHITE, "OK", Color.WHITE);
            } else if(!newPass.equals(confPass)){
                Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Mật khẩu nhập lại không khớp với mật khẩu mới", Color.WHITE, "OK", Color.WHITE);
            } else {
                if(connection.updateMkTHUNGAN(InvoiceCommon.getUSERNAME(), security.encrypt_Base64(newPass)) != -1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                } else {
                    Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Đổi mật khẩu thất bại", Color.WHITE, "OK", Color.WHITE);
                }
            }
        } catch(Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Lỗi đổi mật khẩu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
