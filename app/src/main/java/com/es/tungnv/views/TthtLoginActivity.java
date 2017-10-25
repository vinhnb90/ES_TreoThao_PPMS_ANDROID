package com.es.tungnv.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.TthtConfigInfo;
import com.es.tungnv.sharepref.SharePrefManager;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.utils.TthtConstantVariables;
import com.es.tungnv.webservice.TthtAsyncCallWSApi;

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
 * Created by TUNGNV on 8/23/2016.
 */
public class TthtLoginActivity extends ActionBarActivity implements View.OnClickListener {

    //region Khai báo biến
    private EditText etIP, etUser, etPass;
    private TextView tvVersion;
    private Spinner spDviQly;
    private ImageButton ibDownload, ibCMD;
    private Button btLogin;
    private ImageButton ibtDonVi;

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    private LinkedHashMap<String, String> hmConfig;

    private TthtSQLiteConnection connection;
    private TthtAsyncCallWSApi asyncCallWSApi;

    private int countDvi = 0;
    private String MA_DVIQLY = "";
    private boolean checkLogin = false;

    private ProgressDialog progressDialogGetDvi;
    private ProgressDialog progressDialogLogin;
    private ArrayList<JSONObject> lstJsonDviQly;
    private boolean isHasDonVi;
    //endregion

    //region VinhNB share preference
    private static SharePrefManager sSharePrefManager;

    //endregion
    //region Khởi tạo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.ttht_activity_login);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            initComponents();
            CheckFileConfigExist();
            getDataConfig();

            connection = TthtSQLiteConnection.getInstance(this);
            asyncCallWSApi = TthtAsyncCallWSApi.getInstance();

            loadDataSpinner();
            initDataLogin();

        } catch (Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.WHITE, "Lỗi khởi tạo " + ex.getMessage(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String oldIP = TthtCommon.cfgInfo.getIP_SV_1();
            if (oldIP.trim().isEmpty()) {
                etIP.requestFocus();
            } else {
                etIP.setText(oldIP);
                if (etUser.getText().toString().trim().isEmpty()) {
                    etUser.requestFocus();
                } else {
                    etPass.requestFocus();
                }
            }
            //VinhNB
            String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                    .getString("MA_NVIEN", "");
            TthtCommon.setMaNvien(MA_NVIEN);
            //end VinhNB
            TthtCommon.LoadFolder(TthtLoginActivity.this);
        } catch (Exception ex) {
            Toast.makeText(TthtLoginActivity.this.getApplicationContext(), "Error onResume", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            String version = TthtCommon.cfgInfo.getVERSION();
            String IP = etIP.getText().toString().trim();
            if (!IP.isEmpty() && !IP.equals(TthtCommon.cfgInfo.getIP_SV_1())) {
                saveConfig(IP, !version.isEmpty() ? version : "HN");
                TthtCommon.setVERSION(!version.isEmpty() ? version : "HN");
                TthtCommon.cfgInfo.setIP_SV_1(IP);
            }
        } catch (Exception ex) {
            Toast.makeText(TthtLoginActivity.this.getApplicationContext(), "Lỗi onPause", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.close();
        connection = null;
    }

    private void initComponents() {
        etIP = (EditText) findViewById(R.id.ttht_activity_login_et_ip);
        etUser = (EditText) findViewById(R.id.ttht_activity_login_et_username);
        etPass = (EditText) findViewById(R.id.ttht_activity_login_et_password);
        spDviQly = (Spinner) findViewById(R.id.ttht_activity_login_sp_dvi);
        ibDownload = (ImageButton) findViewById(R.id.ttht_activity_login_ib_loaddvi);
        ibCMD = (ImageButton) findViewById(R.id.ttht_activity_login_ib_cmd);
        btLogin = (Button) findViewById(R.id.ttht_activity_login_bt_login);
        ibtDonVi = (ImageButton) findViewById(R.id.ttht_activity_login_ib_loaddvi);
        tvVersion = (TextView) findViewById(R.id.ttht_activity_login_tv_version);

        btLogin.setOnClickListener(this);
        ibDownload.setOnClickListener(this);
        ibCMD.setOnClickListener(this);

        try {
            tvVersion.setText(new StringBuilder("Version: ").append(BuildConfig.VERSION_NAME).append(" - Emei: ").append(Common.GetIMEI(this)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sSharePrefManager = SharePrefManager.getInstance(this);
        sSharePrefManager.addSharePref("shareRefMaNhanVien", MODE_PRIVATE);

    }
    //endregion

    //region Xử lý sự kiện
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ttht_activity_login_ib_loaddvi:
                isHasDonVi = false;
                ibtDonVi.setEnabled(false);
                if (etIP.getText().toString().length() != 0) {
                    TthtCommon.setIP_SERVER_1(etIP.getText().toString().trim());
                    asyncCallWSApi = TthtAsyncCallWSApi.getInstance();
                }
                if (!Common.isNetworkOnline(TthtLoginActivity.this)) {
                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                    ibtDonVi.setEnabled(true);
                } else if (etIP.length() == 0) {
                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Bạn chưa nhập địa chỉ IP", Color.WHITE, "OK", Color.WHITE);
                    ibtDonVi.setEnabled(true);
                } else {
                    countDvi = 0;
                    lstJsonDviQly = new ArrayList<>();
                    progressDialogGetDvi = ProgressDialog.show(TthtLoginActivity.this, "Please Wait ...", "Đang lấy dữ liệu ...", true, false);


                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(50);
                                lstJsonDviQly = asyncCallWSApi.WS_GET_DON_VI_CALL();

                                if (lstJsonDviQly != null) {
                                    if (connection.deleteAllDataDVIQLY() != -1) {
                                        for (JSONObject m : lstJsonDviQly) {
                                            if (connection.insertDataDVIQLY(m.getString("MA_DVIQLY"), m.getString("TEN_DVIQLY")) != -1) {
                                                countDvi++;

                                            }
                                        }
                                        TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    ibtDonVi.setEnabled(true);
                                                } catch (final Exception ex) {
                                                    Log.i("---", "Exception in thread");
                                                }
                                            }
                                        });

                                    } else {
                                        TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    progressDialogGetDvi.dismiss();
                                                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Lỗi", Color.WHITE, "Không xóa được dữ liệu cũ", Color.WHITE, "OK", Color.WHITE);
                                                    ibtDonVi.setEnabled(true);
                                                } catch (final Exception ex) {
                                                    Log.i("---", "Exception in thread");
                                                }
                                            }
                                        });

                                    }

//                                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Đã lấy " + countDvi + "/" + lstJsonDviQly.size() + " đơn vị", Color.WHITE, "OK", Color.WHITE);
                                    countDvi = 0;
                                    isHasDonVi = true;
                                    handlerDvi.sendEmptyMessage(0);
                                } else {
                                    TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                progressDialogGetDvi.dismiss();
                                                Common.showAlertDialogGreen(TthtLoginActivity.this, "Lỗi", Color.RED, "Sai thông tin cấu hình.\nVui lòng kiểm tra lại hoặc liên hệ nhân viên hỗ trợ!", Color.WHITE, "OK", Color.RED);
                                                ibtDonVi.setEnabled(true);
                                            } catch (final Exception ex) {
                                                Log.i("---", "Exception in thread");
                                            }
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialogGetDvi.dismiss();
                            }
                        }
                    });
                    thread.start();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialogGetDvi.isShowing()) {
                                progressDialogGetDvi.dismiss();
                                thread.interrupt();
//                                Common.showAlertDialogGreen(TthtLoginActivity.this, "Lỗi", Color.RED, "Sai thông tin cấu hình.\nVui lòng kiểm tra wifi hoặc liên hệ nhân viên hỗ trợ!", Color.WHITE, "OK", Color.RED);
                                ibtDonVi.setEnabled(true);
                            }
                        }
                    }, 10000);
                }
                break;
            case R.id.ttht_activity_login_ib_cmd:
//                TthtCommon.setMaDviqly("PD0100");
//                TthtCommon.setMaNvien("27");
//                startActivity(new Intent(TthtLoginActivity.this, TthtMainActivity.class));
                break;
            case R.id.ttht_activity_login_bt_login:
//                TthtCommon.setMaDviqly("PD0100");
//                TthtCommon.setMaNvien("27");
//                startActivity(new Intent(TthtLoginActivity.this, TthtMainActivity.class));
//                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
//                Calendar c = Calendar.getInstance();
//                c.set(2010, 1, 1, 12, 00, 00);
//                AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//                am.setTime(c.getTimeInMillis());

                etUser.setError(null);
                etPass.setError(null);
                if (etIP.getText().toString().length() != 0) {
                    TthtCommon.setIP_SERVER_1(etIP.getText().toString().trim());
                }
                if (TthtCommon.CheckDbExist()) {
                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Không có file dữ liệu", Color.WHITE, "OK", Color.WHITE);
                } else if (etUser.getText().toString().length() == 0) {
                    etUser.setError("Bạn chưa nhập tài khoản");
                    etUser.requestFocus();
                } else if (etPass.getText().toString().length() == 0) {
                    etPass.setError("Bạn chưa nhập mật khẩu");
                    etPass.requestFocus();
                } else {
                    if (Common.isNetworkOnline(TthtLoginActivity.this)) {
                        if (spDviQly.getCount() > 0) {
                            progressDialogLogin = ProgressDialog.show(TthtLoginActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true, false);
                            final Thread threadLogin = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(50);
                                        MA_DVIQLY = connection.getListIDMaDviQly().get(spDviQly.getSelectedItemPosition());
                                        String user = etUser.getText().toString();
                                        String pass = etPass.getText().toString();
                                        JSONObject result = asyncCallWSApi.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(TthtLoginActivity.this));
                                        if (result != null) {
                                            TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        progressDialogLogin.dismiss();
                                                    } catch (final Exception ex) {
                                                        Log.i("---", "Exception in thread");
                                                    }
                                                }
                                            });
                                            String MA_NVIEN = result.getString("MA_NHAN_VIEN");
                                            if (!MA_NVIEN.equals("error")) {
                                                checkLogin = true;
                                                //VinhNB
                                                sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                                                        .edit()
                                                        .putString("MA_NVIEN", MA_NVIEN)
                                                        .commit();
                                                //end VinhNB
                                                TthtCommon.setMaNvien(MA_NVIEN);
//                                                String TEN_NVIEN = result.getString("TEN_NVIEN");
//                                                TthtCommon.setTenNvien(TEN_NVIEN);

                                                handlerLogin.sendEmptyMessage(0);
                                            } else {
                                                checkLogin = false;
                                                TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE,
                                                                    "Đăng nhập thất bại. Kiểm tra lại thông tin đăng nhập hoặc thiết bị này chưa được đăng ký!", Color.WHITE, "OK", Color.WHITE);

                                                            progressDialogLogin.dismiss();
                                                        } catch (final Exception ex) {
                                                            Log.i("---", "Exception in thread");
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            checkLogin = false;
                                            TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (progressDialogLogin.isShowing()) {
                                                                    progressDialogLogin.dismiss();

                                                                    btLogin.setEnabled(true);
                                                                    try {
                                                                        MA_DVIQLY = connection.getListIDMaDviQly().get(spDviQly.getSelectedItemPosition());
                                                                        String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                                                                                .getString("MA_NVIEN", "");

                                                                        boolean checkInputOffline = connection.checkInfoInputCompairWithRememberTable(MA_DVIQLY, etUser.getText().toString(), etPass.getText().toString());
                                                                        if (!checkInputOffline) {
                                                                            TthtLoginActivity.this.runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE,
                                                                                            "Đăng nhập thất bại.!", Color.WHITE, "OK", Color.WHITE);
                                                                                }
                                                                            });
                                                                        } else {
                                                                            TthtCommon.setMaDviqly(MA_DVIQLY);
                                                                            TthtCommon.setUSERNAME(etUser.getText().toString());
                                                                            startActivity(new Intent(TthtLoginActivity.this, TthtMainActivity.class));
                                                                        }
                                                                    } catch (Exception e) {
                                                                        Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Vui lòng kiểm tra lại mạng wifi!", Color.WHITE, "OK", Color.WHITE);
                                                                    }
                                                                }
                                                            }
                                                        }, 5000);
                                                    } catch (final Exception ex) {
                                                        Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE,
                                                                "Đăng nhập thất bại. Không nhận được dữ liệu từ máy chủ.!", Color.WHITE, "OK", Color.WHITE);
                                                    }
                                                }
                                            });

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            threadLogin.start();


                        } else {
                            Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                        }
                    } else {
                        if (spDviQly.getCount() > 0) {
                            MA_DVIQLY = connection.getListIDMaDviQly().get(spDviQly.getSelectedItemPosition());

                            String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                                    .getString("MA_NVIEN", "");

                            if (MA_NVIEN.equals("")) {
                                return;
                            }
                            boolean checkInputOffline = connection.checkInfoInputCompairWithRememberTable(MA_DVIQLY, etUser.getText().toString(), etPass.getText().toString());
                            if (!checkInputOffline) {
                                Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Đăng nhập thất bại.\nVui lòng kiểm tra wifi và đặng nhập lại!", Color.WHITE, "OK", Color.WHITE);
                                return;
                            }
                            //end VinhNB
                            TthtCommon.setMaDviqly(MA_DVIQLY);
                            TthtCommon.setUSERNAME(etUser.getText().toString());

                            startActivity(new Intent(TthtLoginActivity.this, TthtMainActivity.class));
                        } else {
                            Common.showAlertDialogGreen(TthtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                        }
                    }
                }
                break;
        }
    }
    //endregion

    //region Tạo và đọc file cấu hình
    private void CheckFileConfigExist() {
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            File programSampleDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_BACKUP_PATH);
            if (!programSampleDirectory.exists()) {
                programSampleDirectory.mkdirs();
            }
            File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PHOTOS_PATH);
            if (!programPhotoDirectory.exists()) {
                programPhotoDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if (cfgFile.exists()) {
                TthtCommon.cfgInfo = TthtCommon.GetFileConfig();
                if (TthtCommon.cfgInfo == null) {
                    TthtCommon.cfgInfo = new TthtConfigInfo();
                }
            } else {
                TthtCommon.cfgInfo = new TthtConfigInfo();
                TthtCommon.CreateFileConfig(TthtCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(TthtLoginActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình", Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + TthtConstantVariables.PROGRAM_PATH;
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
                        for (int j = 0; j < TthtConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(TthtConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(TthtConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            TthtCommon.setIP_SERVER_1(hmConfig.get("IP_SV_1"));
                            TthtCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(TthtLoginActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean saveConfig(String IP_SERVER, String VERSION) {
        TthtConfigInfo config = new TthtConfigInfo(IP_SERVER, VERSION);
        String filePath = Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME;
        File cfgFile = new File(filePath);
        if (TthtCommon.CreateFileConfig(config, cfgFile, null)) {
            TthtCommon.cfgInfo.setIP_SV_1(IP_SERVER);
            TthtCommon.cfgInfo.setVERSION(VERSION);
            return true;
        }
        return false;
    }
    //endregion

    //region Khởi tạo dữ liệu
    private void loadDataSpinner() {
        ArrayAdapter<String> adapterDvi = new ArrayAdapter<String>(TthtLoginActivity.this, android.R.layout.simple_list_item_1, connection.getDataDVIQLY());
        spDviQly.setAdapter(adapterDvi);
    }

    private void initDataLogin() {
        if (connection.checkRemember()) {
            Cursor c = connection.getDataRemember();
            if (c.moveToFirst()) {
                int pos = connection.getPosDviQly(c.getString(c.getColumnIndex("MA_DVIQLY")));
                spDviQly.setSelection(pos);
                etUser.setText(c.getString(c.getColumnIndex("USERNAME")));
                etPass.requestFocus();
                etPass.setText(c.getString(c.getColumnIndex("PASSWORD")));
            }
        }
    }
    //endregion

    //region xử lý handler
    private Handler handlerDvi = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ibtDonVi.setEnabled(true);
            loadDataSpinner();
            progressDialogGetDvi.dismiss();
        }
    };

    private Handler handlerLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TthtLoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressDialogLogin.dismiss();
                    } catch (final Exception ex) {
                        Log.i("---", "Exception in thread");
                    }
                }
            });
            if (!checkLogin) {
                TthtLoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Common.showAlertDialogGreen(TthtLoginActivity.this, "Lỗi", Color.RED, "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);

                        } catch (final Exception ex) {
                            Log.i("---", "Exception in thread");
                        }
                    }
                });

            } else {
                TthtCommon.setMaDviqly(MA_DVIQLY);
                TthtCommon.setUSERNAME(etUser.getText().toString());
                startActivity(new Intent(TthtLoginActivity.this, TthtMainActivity.class));
                if (connection.deleteAllDataRemember() != -1)
                    connection.insertDataRememger(MA_DVIQLY, etUser.getText().toString(), etPass.getText().toString());
            }
        }
    };
    //endregion

   /* public void onClickForm1(View view) {
        Intent intent = new Intent(TthtLoginActivity.this, TestScreenCongToActivity.class);
        startActivity(intent);
    }*/
}
