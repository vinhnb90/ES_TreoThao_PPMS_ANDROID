package com.es.tungnv.views;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.entity.EsspConfigInfo;
import com.es.tungnv.utils.CmdCode;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.utils.Security;
import com.es.tungnv.webservice.EsspAsyncCallWS;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;

import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspLoginActivity extends AppCompatActivity implements View.OnClickListener{

    //region Khai báo biến
    private EditText etIP, etUserName, etPassWord;
    private TextView tvVersion;
    private Spinner spDvi;
    private Button btLogin;
    private ImageButton ibLoadDvi, ibCMD;

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    private LinkedHashMap<String, String> hmConfig;

    private EsspSqliteConnection connection;
    private Security security;
    private EsspAsyncCallWS ac;
    private EsspAsyncCallWSJson acJson;
    private ProgressDialog progressDialogGetDvi;
    private ProgressDialog progressDialogLogin;
    private ArrayList<JSONObject> lstJsonDviQly;

    private int countDvi = 0;
    private String MA_DVIQLY = "";
    private boolean checkLogin = false;
    //endregion

    //region Khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.essp_activity_login);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            InitComponent();

            CheckFileConfigExist();
            getDataConfig();
            connection = EsspSqliteConnection.getInstance(EsspLoginActivity.this);
            security = new Security();
            ac = new EsspAsyncCallWS();
            acJson = EsspAsyncCallWSJson.getInstance();

            connection.updateDB();

            loadDataSpinner();
            initDataLogin();

            getSupportActionBar().hide();

//            String mk = "ANAtaT0Y7SJlJ6jISHA9mpsu+kzSBTuD3QVF+K62eQ1AMJvfsW+8oHR/BnQZzSzrVw==";
//            Security sec = new Security();
//            String mk1 = sec.decrypt_Base64(mk);
//            String mk3 = sec.decodeString(mk);
        } catch(Exception ex) {
            Common.showAlertDialogGreen(EsspLoginActivity.this, "Lỗi", Color.RED, "Lỗi khởi tạo: " + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            String oldIP = EsspCommon.cfgInfo.getIP_SV_1();
            if(oldIP.trim().equals("")){
                etIP.requestFocus();
            } else {
                etIP.setText(oldIP);
                if(etUserName.getText().toString().trim().equals("")){
                    etUserName.requestFocus();
                } else {
                    etPassWord.requestFocus();
                }
            }
            EsspCommon.LoadFolder(EsspLoginActivity.this);
        } catch(Exception ex) {
            Toast.makeText(EsspLoginActivity.this.getApplicationContext(), "Error onResume", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            String version = EsspCommon.cfgInfo.getVERSION();
            String IP = etIP.getText().toString().trim();
            if(!IP.equals("") && !IP.equals(EsspCommon.cfgInfo.getIP_SV_1())){
                saveConfig(IP, !version.equals("")?version:"HN");
                EsspCommon.setVERSION(!version.equals("")?version:"HN");
                EsspCommon.cfgInfo.setIP_SV_1(IP);
            }
        } catch(Exception ex) {
            Toast.makeText(EsspLoginActivity.this.getApplicationContext(), "Error onPause", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        connection.closeDataBase();
        connection.close();
        connection = null;
    }

    private void InitComponent(){
        etIP = (EditText)findViewById(R.id.essp_activity_login_et_ip);
        etUserName = (EditText)findViewById(R.id.essp_activity_login_et_username);
        etPassWord = (EditText)findViewById(R.id.essp_activity_login_et_password);
        spDvi = (Spinner)findViewById(R.id.essp_activity_login_sp_dvi);
        btLogin = (Button)findViewById(R.id.essp_activity_login_bt_login);
        ibLoadDvi = (ImageButton)findViewById(R.id.essp_activity_login_ib_loaddvi);
        ibCMD = (ImageButton)findViewById(R.id.essp_activity_login_ib_cmd);
        tvVersion = (TextView)findViewById(R.id.essp_activity_login_tv_version);

        btLogin.setOnClickListener(this);
        ibLoadDvi.setOnClickListener(this);
        ibCMD.setOnClickListener(this);

        try {
            tvVersion.setText(new StringBuilder("Version: ").append(BuildConfig.VERSION_NAME).append(" - Emei: ").append(Common.GetIMEI(this)));
        } catch(Exception ex) {
            ex.toString();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.essp_activity_login_bt_login:
                etUserName.setError(null);
                etPassWord.setError(null);
                if(etIP.getText().toString().length() != 0){
                    EsspCommon.setIP_SERVER_1(etIP.getText().toString().trim());
                }
                if(EsspCommon.CheckDbExist()){
                    Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Không có file dữ liệu", Color.WHITE, "OK", Color.WHITE);
                } else if (etUserName.getText().toString().length() == 0){
                    etUserName.setError("Bạn chưa nhập tài khoản");
                    etUserName.requestFocus();
                } else if (etPassWord.getText().toString().length() == 0) {
                    etPassWord.setError("Bạn chưa nhập mật khẩu");
                    etPassWord.requestFocus();
                } else {
                    if(Common.isNetworkOnline(EsspLoginActivity.this)){
                        if(spDvi.getCount() > 0){
                            progressDialogLogin = ProgressDialog.show(EsspLoginActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true,false);
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        MA_DVIQLY = connection.getListIDMaDviQly().get(spDvi.getSelectedItemPosition());
                                        String user = etUserName.getText().toString();
                                        String pass = etPassWord.getText().toString();
                                        String result = acJson.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(EsspLoginActivity.this));
                                        checkLogin = Boolean.parseBoolean(result);

                                        handlerLogin.sendEmptyMessage(0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                        } else {
                            Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                        }
                    } else {
                        if(spDvi.getCount() > 0){
//                            IdDviQly = Integer.parseInt(connection.getListIDMaDviQly().get(spDvi.getSelectedItemPosition()));
                            MA_DVIQLY = connection.getListIDMaDviQly().get(spDvi.getSelectedItemPosition());
                            EsspCommon.setMaDviqly(MA_DVIQLY);
                            EsspCommon.setUSERNAME(etUserName.getText().toString());
                            startActivity(new Intent(EsspLoginActivity.this, EsspMainActivity.class));
                        } else {
                            Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                        }
                    }
                }
                break;
            case R.id.essp_activity_login_ib_loaddvi:
                if(etIP.getText().toString().length() != 0){
                    EsspCommon.setIP_SERVER_1(etIP.getText().toString().trim());
                    acJson = EsspAsyncCallWSJson.getInstance();
                }
                if(!Common.isNetworkOnline(EsspLoginActivity.this)){
                    Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else if(etIP.length() == 0){
                    Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Bạn chưa nhập địa chỉ IP", Color.WHITE, "OK", Color.WHITE);
                } else {
                    countDvi = 0;
                    lstJsonDviQly = new ArrayList<>();
                    progressDialogGetDvi = ProgressDialog.show(EsspLoginActivity.this, "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                lstJsonDviQly = acJson.WS_GET_DON_VI_CALL();

                                if(lstJsonDviQly != null){
                                    if(connection.deleteAllDataDVIQLY() != -1){
                                        for (JSONObject m : lstJsonDviQly) {
                                            if(connection.insertDataDVIQLY(m.getString("MA_NVIEN"), m.getString("TEN_DVIQLY")) != -1){
                                                countDvi++;
                                            }
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Common.showAlertDialogGreen(EsspLoginActivity.this, "Lỗi", Color.RED, "Không xóa được dữ liệu cũ", Color.WHITE, "OK", Color.RED);
                                            }
                                        });
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Common.showAlertDialogGreen(EsspLoginActivity.this, "Thông báo", Color.WHITE, "Đã lấy " + countDvi + "/" + lstJsonDviQly.size() + " đơn vị", Color.WHITE, "OK", Color.WHITE);
                                        }
                                    });
                                    countDvi = 0;

                                    handlerDvi.sendEmptyMessage(0);
                                } else {
                                    progressDialogGetDvi.dismiss();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Common.showAlertDialogGreen(EsspLoginActivity.this, "Lỗi", Color.RED, "Không lấy được dữ liệu", Color.WHITE, "OK", Color.RED);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
                break;
            case R.id.essp_activity_login_ib_cmd:
                showDialogCmd();
                break;
        }
    }
    //endregion

    //region Tạo và kiểm tra file cấu hình
    private void CheckFileConfigExist(){
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            File programSampleDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_SAMPLE_PATH);
            if (!programSampleDirectory.exists()) {
                programSampleDirectory.mkdirs();
            }
            File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_PATH);
            if (!programPhotoDirectory.exists()) {
                programPhotoDirectory.mkdirs();
            }
            File programPhotoAnhDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH);
            if (!programPhotoAnhDirectory.exists()) {
                programPhotoAnhDirectory.mkdirs();
            }
            File programPhotoBVDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH);
            if (!programPhotoBVDirectory.exists()) {
                programPhotoBVDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + EsspConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                EsspCommon.cfgInfo = EsspCommon.GetFileConfig();
                if(EsspCommon.cfgInfo == null){
                    EsspCommon.cfgInfo = new EsspConfigInfo();
                }
            } else {
                EsspCommon.cfgInfo = new EsspConfigInfo();
                EsspCommon.CreateFileConfig(EsspCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(EsspLoginActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình", Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + EsspConstantVariables.PROGRAM_PATH;
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
                        for (int j = 0; j < EsspConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(EsspConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(EsspConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            EsspCommon.setIP_SERVER_1(hmConfig.get("IP_SV_1"));
                            EsspCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(EsspLoginActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean saveConfig(String IP_SERVER, String VERSION){
        EsspConfigInfo config = new EsspConfigInfo(IP_SERVER, VERSION);
        String filePath = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + EsspConstantVariables.CFG_FILENAME;
        File cfgFile = new File(filePath);
        if(EsspCommon.CreateFileConfig(config, cfgFile, null)){
            EsspCommon.cfgInfo.setIP_SV_1(IP_SERVER);
            EsspCommon.cfgInfo.setVERSION(VERSION);
            return true;
        }
        return false;
    }
    //endregion

    //region Xử lý handler
    private Handler handlerDvi = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadDataSpinner();
            progressDialogGetDvi.dismiss();
        }
    };

    private Handler handlerLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialogLogin.dismiss();
            if(!checkLogin){
                Common.showAlertDialogGreen(EsspLoginActivity.this, "Lỗi", Color.RED, "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);
            } else {
//                EsspCommon.setIdDviqly(IdDviQly);
                EsspCommon.setMaDviqly(MA_DVIQLY);
                EsspCommon.setUSERNAME(etUserName.getText().toString());
                startActivity(new Intent(EsspLoginActivity.this, EsspMainActivity.class));
                // lưu user, pass
                if(connection.deleteAllDataRemember() != -1)
                    connection.insertDataRememger(MA_DVIQLY, etUserName.getText().toString(), etPassWord.getText().toString());
            }
        }
    };
    //endregion

    //region Khởi tạo dữ liệu
    private void loadDataSpinner(){
        ArrayAdapter<String> adapterDvi = new ArrayAdapter<String>(EsspLoginActivity.this, android.R.layout.simple_list_item_1, connection.getDataDVIQLY());
        spDvi.setAdapter(adapterDvi);
    }

    private void initDataLogin(){
        if(connection.checkRemember()){
            Cursor c = connection.getDataRemember();
            if (c.moveToFirst()) {
                int pos = connection.getPosDviQly(c.getString(c.getColumnIndex("MA_NVIEN")));
                spDvi.setSelection(pos);
                etUserName.setText(c.getString(c.getColumnIndex("USERNAME")));
                etPassWord.setText(c.getString(c.getColumnIndex("PASSWORD")));
            }
        }
    }
    //endregion

    //region Xử lý CMD
    private void CMDReCreateDatabase(){
        StringBuilder pathDB = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(EsspConstantVariables.PROGRAM_DATA_PATH).append(EsspConstantVariables.DATABASE_NAME);
        File fDB = new File(pathDB.toString());
        if(fDB.exists()){
            if(fDB.delete()){
                EsspLoginActivity.this.finish();
//                startActivity(new Intent(EsspLoginActivity.this, EsspLoginActivity.class));
            } else {
                Toast.makeText(EsspLoginActivity.this, "Tạo lại DB thất bại", Toast.LENGTH_LONG).show();
            }
        } else {
            EsspLoginActivity.this.finish();
//            startActivity(new Intent(EsspLoginActivity.this, EsspLoginActivity.class));
        }
    }

    private void CMDUpdateDatabase(){
        connection.updateDB();
    }

    private void popupMenuTraCuu(String content) {
        try {
            final Dialog dialog = new Dialog(EsspLoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_lookup);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            TextView tvLookup = (TextView) dialog.findViewById(R.id.dialog_lookup_tvLookup);
            tvLookup.setText(content);

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspLoginActivity.this,
                    "Lỗi", Color.RED, "Lỗi tra cứu", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogCmd() {
        try{
            final Dialog dialog = new Dialog(EsspLoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_cmd);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            dialog.setCanceledOnTouchOutside(false);

            final EditText etCMD = (EditText) dialog.findViewById(R.id.dialog_cmd_etCMD);
            Button btOK = (Button) dialog.findViewById(R.id.dialog_cmd_btOK);
            Button btClear = (Button) dialog.findViewById(R.id.dialog_cmd_btClear);

            btOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        String cmdCode = etCMD.getText().toString();
                        etCMD.setError(null);
                        if(cmdCode.isEmpty()){
                            etCMD.setError("Bạn chưa nhập mã");
                            etCMD.requestFocus();
                        } else {
                            if(cmdCode.toLowerCase().trim().equals(CmdCode.RECREATE_DATABASE)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(EsspLoginActivity.this);
                                builder.setMessage("Bạn có chắc chắn muốn tạo lại DB?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CMDReCreateDatabase();
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.show();
                            } else if(cmdCode.toLowerCase().trim().equals(CmdCode.LOOKUP)){
                                StringBuilder content = new StringBuilder();
                                content.append(CmdCode.LOOKUP).append(": Tra cứu mã code\n");
                                content.append(CmdCode.RECREATE_DATABASE).append(": Tạo lại cơ sở dữ liệu\n");
                                popupMenuTraCuu(content.toString());
                            } else if(cmdCode.toLowerCase().trim().equals(CmdCode.UPDATE_DATABASE)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(EsspLoginActivity.this);
                                builder.setMessage("Bạn có chắc chắn muốn cập nhật DB?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CMDUpdateDatabase();
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.show();
                            } else {
                                etCMD.setText("Mã lệnh \"" + cmdCode + "\" không tồn tại");
                            }
                        }
                        etCMD.setText("");
                    } catch(Exception ex) {
                        Toast.makeText(EsspLoginActivity.this.getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                        etCMD.setText("");
                    }
                }
            });

            btClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etCMD.setText("");
                }
            });

            dialog.show();
        } catch(Exception ex) {
            Toast.makeText(EsspLoginActivity.this.getApplicationContext(), "CMD error", Toast.LENGTH_LONG).show();
        }
    }
    //endregion

}
