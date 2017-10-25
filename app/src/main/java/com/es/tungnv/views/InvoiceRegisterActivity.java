package com.es.tungnv.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.es.tungnv.db.InvoiceSQLiteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.InvoiceCommon;
import com.es.tungnv.utils.InvoiceConstantVariables;
import com.es.tungnv.utils.Security;
import com.es.tungnv.webservice.InvoiceAsyncCallWS;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TUNGNV on 2/23/2016.
 */
public class InvoiceRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spnMaQVIQLY, spnMayChu;
    private EditText etTenDVIQLY, etMaTN, etTenTN, etMatKhau, etDienThoai, etIMEI;
    private Button btDangKy;

    private InvoiceAsyncCallWS asyncCallWS;
    private InvoiceSQLiteConnection connection;
    private Security security;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.invoice_activity_register);
            getSupportActionBar().hide();
            connection = new InvoiceSQLiteConnection(this);
            asyncCallWS = new InvoiceAsyncCallWS();
            security = new Security();
            initComponent();
            initData();
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error onCreate: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void initComponent(){
        try{
            spnMaQVIQLY = (Spinner) findViewById(R.id.invoice_activity_register_spn_ma_dvql);
            spnMayChu = (Spinner) findViewById(R.id.invoice_activity_register_spn_maychu);
            etTenDVIQLY = (EditText) findViewById(R.id.invoice_activity_register_et_ten_dvql);
            etMaTN = (EditText) findViewById(R.id.invoice_activity_register_et_ma_thungan);
            etTenTN = (EditText) findViewById(R.id.invoice_activity_register_et_ten_thungan);
            etMatKhau = (EditText) findViewById(R.id.invoice_activity_register_et_matkhau);
            etDienThoai = (EditText) findViewById(R.id.invoice_activity_register_et_dienthoai);
            etIMEI = (EditText) findViewById(R.id.invoice_activity_register_et_imei);
            btDangKy = (Button) findViewById(R.id.invoice_activity_register_bt_dangky);

            btDangKy.setOnClickListener(this);
        } catch (Exception ex) {
            Toast.makeText(this.getApplicationContext(), "Lỗi khởi tạo đối tượng: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void initData(){
        try{
            if(InvoiceCommon.VERSION.equals("YB")) {
                ArrayAdapter<String> adapterMaDViQly = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, InvoiceConstantVariables.MA_DVIQLY_YB);
                spnMaQVIQLY.setAdapter(adapterMaDViQly);
                spnMaQVIQLY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        etTenDVIQLY.setText(InvoiceConstantVariables.TEN_DVIQLY_YB[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                 });
            }
            String IMEI = Common.GetIMEI(InvoiceRegisterActivity.this);
            String[] sArrIP = {InvoiceCommon.cfgInfo.getIP_SV_1()};
            ArrayAdapter<String> adapterMayChu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sArrIP);
            spnMayChu.setAdapter(adapterMayChu);
            etIMEI.setText(IMEI);

            etMaTN.requestFocus();

            Cursor c = connection.getAllDataTHUNGAN();
            if(c.moveToFirst()){
                do {
                    String MA_TNGAN = c.getString(c.getColumnIndex("MA_TNGAN"));
                    String TEN_TNGAN = c.getString(c.getColumnIndex("TEN_TNGAN"));
//                    String MKHAU_TNGAN = security.decrypt_Base64(c.getString(c.getColumnIndex("MKHAU_TNGAN")));
                    String DTHOAI_TNGAN = c.getString(c.getColumnIndex("DTHOAI_TNGAN"));
                    etMaTN.setText(MA_TNGAN);
                    etTenTN.setText(TEN_TNGAN);
//                    etMatKhau.setText(MKHAU_TNGAN);
                    etDienThoai.setText(DTHOAI_TNGAN);
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Toast.makeText(this.getApplicationContext(), "Lỗi khởi tạo dữ liệu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_activity_register_bt_dangky:
                try {
                    connection.deleteAllDataTHUNGAN();
                    String MA_DVIQLY = spnMaQVIQLY.getSelectedItem().toString();
                    String MA_MAY = etIMEI.getText().toString().trim();
                    String MA_TNGAN = etMaTN.getText().toString().trim().toUpperCase();
                    String TEN_TNGAN = etTenTN.getText().toString().trim();
                    String MAT_KHAU = etMatKhau.getText().toString().trim();
                    String DTHOAI_TNGAN = etDienThoai.getText().toString().trim();
                    if (MA_TNGAN.equals("")) {
                        Toast.makeText(this.getApplicationContext(), "Bạn chưa nhập mã thu ngân", Toast.LENGTH_LONG).show();
                    } else if (TEN_TNGAN.equals("")) {
                        Toast.makeText(this.getApplicationContext(), "Bạn chưa nhập tên thu ngân", Toast.LENGTH_LONG).show();
                    } else if (MAT_KHAU.equals("")) {
                        Toast.makeText(this.getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_LONG).show();
                    } else if (DTHOAI_TNGAN.equals("")) {
                        Toast.makeText(this.getApplicationContext(), "Bạn chưa nhập điện thoại", Toast.LENGTH_LONG).show();
                    } else {
                        JSONObject JSONRegister = asyncCallWS.WS_REGISTER_CALL(MA_DVIQLY, MA_MAY, MA_TNGAN, TEN_TNGAN, security.encrypt_Base64(MAT_KHAU), DTHOAI_TNGAN);
                        if (JSONRegister != null) {
                            String sMA_DVIQLY = JSONRegister.getString("MA_DVIQLY");
                            String sTEN_DVIQLY = InvoiceConstantVariables.TEN_DVIQLY_YB[spnMaQVIQLY.getSelectedItemPosition()];
                            String sDCHI_DVIQLY = JSONRegister.getString("DCHI_DVIQLY");
                            String sMA_STHUE = JSONRegister.getString("MA_STHUE");
                            String sSO_TKHOAN = JSONRegister.getString("SO_TKHOAN");
                            String sMA_TNGAN = JSONRegister.getString("MA_TNGAN").toUpperCase();
                            String sTEN_TNGAN = JSONRegister.getString("TEN_TNGAN");
                            String sMKHAU_TNGAN = JSONRegister.getString("MKHAU_TNGAN");
                            String sDTHOAI_TNGAN = JSONRegister.getString("DTHOAI_TNGAN");
                            String sMA_MAY = JSONRegister.getString("MA_MAY");
                            String sDU_PHONG_1 = "";
                            String sDU_PHONG_2 = "";
                            if(connection.insertDataTHUNGAN(sMA_DVIQLY, sTEN_DVIQLY, sDCHI_DVIQLY, sMA_STHUE, sSO_TKHOAN,
                                    sMA_TNGAN, sTEN_TNGAN, sMKHAU_TNGAN, sDTHOAI_TNGAN, sMA_MAY, sDU_PHONG_1, sDU_PHONG_2) != -1){
                                Toast.makeText(this.getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                InvoiceRegisterActivity.this.finish();
                            } else {
                                Toast.makeText(this.getApplicationContext(), "Đăng ký client thất bại", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this.getApplicationContext(), "Đăng ký server thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch(Exception ex) {
                    Toast.makeText(this.getApplicationContext(), "Lỗi đăng ký: " + ex.toString(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
