package com.es.tungnv.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.InvoiceSQLiteConnection;
import com.es.tungnv.entity.InvoiceConfigInfo;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.InvoiceCommon;
import com.es.tungnv.utils.InvoiceConstantVariables;
import com.es.tungnv.utils.Security;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class InvoiceLoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvRegister;
    private Button btLogin;
    private EditText etIP, etUserName, etPassWord;
    private ImageView ivLogo;

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    private LinkedHashMap<String, String> hmConfig;

    private InvoiceSQLiteConnection connection;
    private Security security;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.invoice_activity_login);
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            initComponent();
            CheckFileConfigExist();
            getDataConfig();
            connection = new InvoiceSQLiteConnection(this);
            security = new Security();
        } catch(Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Error onCreate: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            String oldIP = InvoiceCommon.cfgInfo.getIP_SV_1();
            if(oldIP.trim().equals("")){
                etIP.requestFocus();
            } else {
                etIP.setText(oldIP);
                Cursor c = connection.getAllDataTHUNGAN();
                if(c.moveToFirst()){
                    do {
                        String MA_TNGAN = c.getString(c.getColumnIndex("MA_TNGAN"));
                        etUserName.setText(MA_TNGAN);
                    } while (c.moveToNext());
                }
                if(etUserName.getText().toString().trim().equals("")){
                    etUserName.requestFocus();
                } else {
                    etPassWord.requestFocus();
                }
            }
            InvoiceCommon.LoadFolder(InvoiceLoginActivity.this);
//            etPassWord.setText("");

            String root = Environment.getExternalStorageDirectory().toString();
            File file = new File(root + "/" + InvoiceConstantVariables.PROGRAM_PATH + "logo.jpg");
            if(file.exists()){
                Bitmap bm = BitmapFactory.decodeFile(root + "/" + InvoiceConstantVariables.PROGRAM_PATH + "logo.jpg");
                Drawable d = new BitmapDrawable(getResources(), bm);
                ivLogo.setBackgroundDrawable(d);
            }
        } catch(Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Error onResume: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            String version = InvoiceCommon.cfgInfo.getVERSION();
            String IP = etIP.getText().toString().trim();
            if(!IP.equals("") && !IP.equals(InvoiceCommon.cfgInfo.getIP_SV_1())){
                saveConfig(IP, !version.equals("")?version:"YB");
                InvoiceCommon.setVERSION(!version.equals("")?version:"YB");
                InvoiceCommon.cfgInfo.setIP_SV_1(IP);
            }
        } catch(Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Error onResume: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponent(){
        try {
            tvRegister = (TextView) findViewById(R.id.invoice_activity_login_tv_register);
            btLogin = (Button) findViewById(R.id.invoice_activity_login_bt_login);
            etIP = (EditText) findViewById(R.id.invoice_activity_login_et_ip);
            etUserName = (EditText) findViewById(R.id.invoice_activity_login_et_username);
            etPassWord = (EditText) findViewById(R.id.invoice_activity_login_et_password);
            ivLogo = (ImageView) findViewById(R.id.invoice_activity_login_iv_logo);

            tvRegister.setOnClickListener(this);
            btLogin.setOnClickListener(this);

        } catch(Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Error initComponent: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void CheckFileConfigExist(){
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_DB_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH + InvoiceConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                InvoiceCommon.cfgInfo = InvoiceCommon.GetFileConfig();
                if(InvoiceCommon.cfgInfo == null){
                    InvoiceCommon.cfgInfo = new InvoiceConfigInfo();
                }
            } else {
                InvoiceCommon.cfgInfo = new InvoiceConfigInfo();
                InvoiceCommon.CreateFileConfig(InvoiceCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + InvoiceConstantVariables.PROGRAM_PATH;
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.length() - 4, fileName.length());
                if (fileExtension.toLowerCase().equals(".cfg")) {
                    fileConfig = file.toString();
                }
            }
        }
        return fileConfig;
    }

    public void getDataConfig() throws XmlPullParserException, IOException, SAXException {
        try {
            ArrListConfig = new ArrayList<LinkedHashMap<String, String>>();
            XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
            XmlPullParser pp = pf.newPullParser();
            try {
                String file = getConfig();

                FileInputStream ip = new FileInputStream(file);
                pp.setInput(ip, "UTF-8");
            } catch (Exception e1) {
                return;
            }
            int event = -1;
            String NodeName;
            while (event != XmlPullParser.END_DOCUMENT) {
                event = pp.next();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            hmConfig = new LinkedHashMap<String, String>();
                            break;
                        }
                        for (int j = 0; j < InvoiceConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(InvoiceConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(InvoiceConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            InvoiceCommon.setIP_SERVER_1(hmConfig.get("IP_SV_1"));
                            InvoiceCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invoice_activity_login_tv_register:
                startActivity(new Intent(InvoiceLoginActivity.this, InvoiceRegisterActivity.class));
                break;

            case R.id.invoice_activity_login_bt_login:
                String IP = etIP.getText().toString().trim();
                String USER = etUserName.getText().toString().trim();
                String PASS = etPassWord.getText().toString().trim();
                if(IP.equals("")){
                    Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Bạn chưa nhập IP máy chủ", Toast.LENGTH_LONG).show();
                    etIP.requestFocus();
                } else {
                    if(USER.equals("")){
                        Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Bạn chưa nhập tài khoản", Toast.LENGTH_LONG).show();
                        etUserName.requestFocus();
                    } else {
                        if(PASS.equals("")) {
                            Toast.makeText(InvoiceLoginActivity.this.getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_LONG).show();
                            etPassWord.requestFocus();
                        } else {
                            if(connection.checkLogin(USER, security.encrypt_Base64(PASS)) > 0) {
                                InvoiceCommon.setUSERNAME(USER);
                                startActivity(new Intent(InvoiceLoginActivity.this, InvoiceMainActivity.class));
                            } else {
                                Common.showAlertDialogGreen(InvoiceLoginActivity.this, "Thông báo", Color.WHITE, "Sai thông tin tài khoản, mật khẩu hoặc tài khoản không tồn tại", Color.WHITE, "OK", Color.WHITE);
                            }
                        }
                    }
                }
                break;
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

}

