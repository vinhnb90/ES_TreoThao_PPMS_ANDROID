package com.es.tungnv.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.PopupMenu.MenuItem;
import com.es.tungnv.PopupMenu.PopupMenu;
import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.db.GphtSqliteConnection;
import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.GphtConfigInfo;
import com.es.tungnv.entity.GphtEntityLogQuyen;
import com.es.tungnv.sharepref.SharePrefManager;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.utils.GphtCommon;
import com.es.tungnv.utils.GphtConstantVariables;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;
import com.es.tungnv.webservice.GphtAsyncCallWSJson;
import com.es.tungnv.webservice.TthtAsyncCallWSApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 7/4/2016.
 */
public class GphtLoginActivity extends Activity implements View.OnClickListener {

    private LinearLayout llLogin;
    private RelativeLayout rlBackground;
    private TextView tvVersion, tvCountVersion, tvIP, tvDonVi;
    private EditText etIP, etUser, etPass;
    private Spinner spDonVi;
    private Button btTaiUser, btTaiDonVi, btDangNhap;
    private ImageView ivLogo;
    private ImageButton ibSyn, ibSetting;
    private Animation anim;

    private GphtSqliteConnection connection;
    private EsspSqliteConnection connectionEssp;
    private TthtSQLiteConnection connectionTtht;
    private GphtAsyncCallWSJson asyncCallWSJson;
    private ProgressDialog progressDialog;

    private ArrayList<JSONObject> joDviQly;
    private ArrayList<JSONObject> joDMHeThong;
    private ArrayList<JSONObject> joUserCha;
    private ArrayList<JSONObject> joUserCon;
    private ArrayList<JSONObject> joQuyen;
    private ArrayList<JSONObject> joLogQuyen;

    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private int countDongBo = 0;
    private int countDongBoQuyen = 0;
    private int sumDongBoQuyen = 0;
    private int countDongBoDonVi = 0;
    private int sumDongBoDonVi = 0;
    private int countDongBoHeThong = 0;
    private int sumDongBoHeThong = 0;
    private int countDongBoUserCha = 0;
    private int sumDongBoUserCha = 0;
    private int countDongBoUserCon = 0;
    private int sumDongBoUserCon = 0;

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    private LinkedHashMap<String, String> hmConfig;

    private String prefname = "config";
    private int countLogin = 0;
    private ProgressDialog progressDialogLogin;
    private EsspAsyncCallWSJson acJsonCD;
    private boolean checkLogin = false;
    private TthtAsyncCallWSApi asyncCallWSApi;
    private static SharePrefManager sSharePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.gpht_activity_login);
            CheckFileConfigExist();
            getDataConfig();
            connection = GphtSqliteConnection.getInstance(this);
            connectionEssp = EsspSqliteConnection.getInstance(this);
            connectionTtht = TthtSQLiteConnection.getInstance(this);
            asyncCallWSJson = GphtAsyncCallWSJson.getInstance();
            initCoponents();
//            loadDataSpinner();
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GphtLoginActivity.this, "Lỗi", Color.RED,
                    "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            countLogin = pre.getInt("COUNT_LOGIN", 0);
            String oldIP = GphtCommon.cfgInfo.getIP_SV_1();
//            if(oldIP.trim().isEmpty()){
//                etIP.requestFocus();
//            } else {
                tvIP.setText(oldIP);
                Cursor c = connection.getMaxDataLogLogin();
                if(c.moveToFirst()) {
                    etUser.setText(c.getString(c.getColumnIndex("Ten_TCap")));
//                    int pos = connection.getPosSelectDVIQLY(c.getInt(c.getColumnIndex("ID_DonVi")));
//                    spDonVi.setSelection(pos);
                    etPass.setText(Common.decryptPassword(c.getString(c.getColumnIndex("Mat_Khau"))));
                    tvDonVi.setText(connection.getDVIQLYByID(c.getInt(c.getColumnIndex("ID_DonVi"))));
                }
                if(etUser.getText().toString().trim().isEmpty()){
                    etUser.requestFocus();
                } else {
                    etPass.requestFocus();
                }
//            }
            tvCountVersion.setText(Html.fromHtml("<p>Bạn còn <font color=\"green\"><b>"
                    + (GphtConstantVariables.MAX_LOGIN - countLogin) + "</b></font> lần đăng nhập offline</p>"));
            String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                    .getString("BUNDLE_MA_NVIEN", "");
            TthtCommon.setMaNvien(MA_NVIEN);
            GphtCommon.LoadFolder(GphtLoginActivity.this);
        } catch(Exception ex) {
            Toast.makeText(GphtLoginActivity.this.getApplicationContext(), "Error onResume", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            String version = GphtCommon.cfgInfo.getVERSION();
            String IP = tvIP.getText().toString().trim();
            if(!IP.isEmpty() && !IP.equals(GphtCommon.cfgInfo.getIP_SV_1())){
                saveConfig(IP, !version.isEmpty()?version:"HN");
                GphtCommon.setVERSION(!version.isEmpty()?version:"HN");
                GphtCommon.cfgInfo.setIP_SV_1(IP);
            }

//            if (Common.isNetworkOnline(GphtLoginActivity.this)) {
                // Check server change
                // if(change){
                //      countLogin = 0;
                //      savingPreferencesCountLogin(countLogin);
                // }
//            }
        } catch(Exception ex) {
            Toast.makeText(GphtLoginActivity.this.getApplicationContext(), "Error onPause", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.close();
        connection = null;
    }

    private void loadDataSpinner(){
        ArrayAdapter<String> adapterDvi = new ArrayAdapter<String>(GphtLoginActivity.this, R.layout.gpht_row_dvi, connection.getDataDVIQLY());
        spDonVi.setAdapter(adapterDvi);
    }

    private void loadDataSpinner(Spinner spDonVi){
        ArrayAdapter<String> adapterDvi = new ArrayAdapter<String>(GphtLoginActivity.this, R.layout.gpht_row_dvi, connection.getDataDVIQLY());
        spDonVi.setAdapter(adapterDvi);
    }

    private void initCoponents() {
        llLogin = (LinearLayout) findViewById(R.id.gpht_activity_login_ll_login);
        ivLogo = (ImageView) findViewById(R.id.gpht_activity_login_ivLogo);
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
        tvVersion = (TextView) findViewById(R.id.gpht_activity_login_tv_Version);
        tvCountVersion = (TextView) findViewById(R.id.gpht_activity_login_tv_CountLogin);
        tvIP = (TextView) findViewById(R.id.gpht_activity_login_tvIP);
        tvDonVi = (TextView) findViewById(R.id.gpht_activity_login_tvDonVi);
        etIP = (EditText) findViewById(R.id.gpht_activity_login_et_ip);
        etUser = (EditText) findViewById(R.id.gpht_activity_login_et_username);
        etPass = (EditText) findViewById(R.id.gpht_activity_login_et_password);
        spDonVi = (Spinner) findViewById(R.id.gpht_activity_login_sp_dvi);
        btTaiUser = (Button) findViewById(R.id.gpht_activity_login_bt_loadQuyen);
        btTaiDonVi = (Button) findViewById(R.id.gpht_activity_login_bt_loadDMuc);
        btDangNhap = (Button) findViewById(R.id.gpht_activity_login_bt_login);
        ibSyn = (ImageButton) findViewById(R.id.gpht_activity_login_ibSyn);
        ibSetting = (ImageButton) findViewById(R.id.gpht_activity_login_ibSetting);

        tvVersion.setText("version: " + Common.getVersion() + " - Imei: " + Common.GetIMEI(GphtLoginActivity.this) );
        setAnimationLayout(llLogin, R.anim.zoom_in);
        setAnimationLayout(ivLogo, R.anim.zoom_top);
        setAnimationLayout(tvVersion, R.anim.zoom_bottom);

        btTaiUser.setOnClickListener(this);
        btTaiDonVi.setOnClickListener(this);
        btDangNhap.setOnClickListener(this);
        ibSyn.setOnClickListener(this);
        ibSetting.setOnClickListener(this);

        btTaiDonVi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                connection.deleteAll();
                loadDataSpinner();
                etUser.setText("");
                etPass.setText("");
                return false;
            }
        });

        sSharePrefManager = SharePrefManager.getInstance(this);
        sSharePrefManager.addSharePref("shareRefMaNhanVien", MODE_PRIVATE);
    }

    public void setAnimationLayout(View v, int resource){
        anim = AnimationUtils.loadAnimation(GphtLoginActivity.this, resource);
        v.startAnimation(anim);
    }

    private void loadQuyen(View v){
        String ip = tvIP.getText().toString().trim();
        if(ip.length() != 0){
            EsspCommon.setIP_SERVER_1(ip);
            asyncCallWSJson = GphtAsyncCallWSJson.getInstance();
        }
        countDongBo = 0;

        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Đang đồng bộ quyền người dùng ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        Thread threadUser = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDongBoQuyen = 0;
                    sumDongBoQuyen = 0;
                    joLogQuyen = new ArrayList<>();
                    joQuyen = new ArrayList<>();
                    joLogQuyen = asyncCallWSJson.WS_GET_LOG_QUYEN_CALL();
                    joQuyen = asyncCallWSJson.WS_GET_QUYEN_CALL();

                    int maxSize = joLogQuyen.size() + joQuyen.size();
                    float snap = (float) maxSize / 100f;

                    if (joLogQuyen != null) {
                        if (connection.deleteAllLogQuyen() != -1) {
                            for (int i = 0; i < joLogQuyen.size(); i++) {
                                countDongBo++;
                                sumDongBoQuyen++;
                                try {
                                    JSONObject joDvi = joLogQuyen.get(i);
                                    if (connection.insertDataLogQuyen(joDvi.getInt("id"), joDvi.getString("ThoiGian")) != -1) {
                                        countDongBoQuyen++;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressBarStatus = (int) (countDongBo / snap);
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.setProgress(progressBarStatus);
                                    }
                                });
                            }
                        }
                    }

                    if (joQuyen != null) {
                        if (connection.deleteAllQuyen() != -1) {
                            for (int i = 0; i < joQuyen.size(); i++) {
                                countDongBo++;
                                sumDongBoQuyen++;
                                try {
                                    JSONObject joDvi = joQuyen.get(i);
                                    if (connection.insertDataQuyen(joDvi.getInt("ID"), joDvi.getInt("NV_ID"),
                                            joDvi.getInt("ID_HeThong"), joDvi.getInt("ID_User")) != -1) {
                                        countDongBoQuyen++;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressBarStatus = (int) (countDongBo / snap);
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.setProgress(progressBarStatus);
                                    }
                                });
                            }
                        }
                    }

                    handlerQuyen.sendEmptyMessage(0);
                } catch(Exception ex) {
                    progressDialog.dismiss();
                    Common.showAlertDialogGreen(GphtLoginActivity.this, "Lỗi", Color.WHITE,
                            "Lỗi đồng bộ quyền", Color.WHITE, "OK", Color.WHITE);
                }
            }
        });
        threadUser.start();
    }

    private void loadDMuc(View v) {
        try{
            String ip = tvIP.getText().toString().trim();
            if(ip.length() != 0){
                EsspCommon.setIP_SERVER_1(ip);
                asyncCallWSJson = GphtAsyncCallWSJson.getInstance();
            }
            countDongBo = 0;
            countDongBoDonVi = 0;
            countDongBoHeThong = 0;
            countDongBoUserCha = 0;
            countDongBoUserCon = 0;
            sumDongBoDonVi = 0;
            sumDongBoHeThong = 0;
            sumDongBoUserCha = 0;
            sumDongBoUserCon = 0;

            progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Đang đồng bộ danh mục ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            Thread threadDanhMuc = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        joDviQly = new ArrayList<>();
                        joDMHeThong = new ArrayList<>();
                        joUserCha = new ArrayList<>();
                        joUserCon = new ArrayList<>();

                        joDviQly = asyncCallWSJson.WS_GET_DVI_QLY_CALL();
                        joDMHeThong = asyncCallWSJson.WS_GET_HE_THONG_CALL();
                        joUserCha = asyncCallWSJson.WS_GET_USER_CHA_CALL();
                        joUserCon = asyncCallWSJson.WS_GET_USER_CON_CALL();

                        int maxSize = joDviQly.size() + joDMHeThong.size() + joUserCha.size() + joUserCon.size();
                        float snap = (float) maxSize / 100f;

                        if (joDviQly != null) {
                            if (connection.deleteAllDataDVIQLY() != -1) {
                                for (int i = 0; i < joDviQly.size(); i++) {
                                    countDongBo++;
                                    sumDongBoDonVi++;
                                    try {
                                        JSONObject joDvi = joDviQly.get(i);
                                        if (connection.insertDataDVIQLY(joDvi.getInt("ID_DonVi"), joDvi.getString("Ten_DonVi").trim(), joDvi.getString("Ma_DonVi").trim()) != -1) {
                                            countDongBoDonVi++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressBarStatus = (int) (countDongBo / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                }
                            }
                        }

                        if (joDMHeThong != null) {
                            if (connection.deleteAllDataDMHeThong() != -1) {
                                for (int i = 0; i < joDMHeThong.size(); i++) {
                                    countDongBo++;
                                    sumDongBoHeThong++;
                                    try {
                                        JSONObject joDM = joDMHeThong.get(i);
                                        if (connection.insertDataDMHeThong(joDM.getInt("ID_HeThong"), joDM.getString("Ten_HeThong").trim(),
                                                joDM.getString("Address").trim(), joDM.getInt("ID_DonVi"), joDM.getString("Key_HThong").trim()) != -1) {
                                            countDongBoHeThong++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressBarStatus = (int) (countDongBo / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                }
                            }
                        }

                        if (joUserCha != null) {
                            if (connection.deleteAllDataUserCha() != -1) {
                                for (int i = 0; i < joUserCha.size(); i++) {
                                    countDongBo++;
                                    sumDongBoUserCha++;
                                    try {
                                        JSONObject joDM = joUserCha.get(i);
                                        if (connection.insertDataUserCha(joDM.getInt("NV_ID"), joDM.getString("Ten_TCap").trim(),
                                                joDM.getString("Mat_Khau").trim(), joDM.getString("Ten_DD").trim(), joDM.getString("Dien_Thoai").trim(),
                                                joDM.getString("Email").trim(), joDM.getInt("STT"), joDM.getInt("ID_DonVi"),
                                                joDM.getString("DuongDan_URL_Default").trim(), joDM.getBoolean("Admin")) != -1) {
                                            countDongBoUserCha++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressBarStatus = (int) (countDongBo / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                }
                            }
                        }

                        if (joUserCon != null) {
                            if (connection.deleteAllDataUserCon() != -1) {
                                for (int i = 0; i < joUserCon.size(); i++) {
                                    countDongBo++;
                                    sumDongBoUserCon++;
                                    try {
                                        JSONObject joDM = joUserCon.get(i);
                                        if (connection.insertDataUserCon(joDM.getInt("ID_User"), joDM.getInt("ID_HeThong"),
                                                joDM.getString("GhiChu").trim(), joDM.getString("Ten_DangNhap").trim(),
                                                joDM.getString("Mat_Khau").trim()) != -1) {
                                            countDongBoUserCon++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progressBarStatus = (int) (countDongBo / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                }
                            }
                        }

                        handlerDongBo.sendEmptyMessage(0);
                    } catch(Exception ex) {
                        progressDialog.dismiss();
                        Common.showAlertDialogGreen(GphtLoginActivity.this, "Lỗi", Color.WHITE,
                                "Lỗi đồng bộ danh mục", Color.WHITE, "OK", Color.WHITE);
                    }
                }
            });
            threadDanhMuc.start();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        String ip = tvIP.getText().toString().trim();
        switch (v.getId()){
            case R.id.gpht_activity_login_bt_loadQuyen:
                loadQuyen(v);
                break;
            case R.id.gpht_activity_login_bt_loadDMuc:
                loadDMuc(v);
                break;
            case R.id.gpht_activity_login_bt_login:
                try {
//                    GphtLoginActivity.this.finish();
//                    startActivity(new Intent(GphtLoginActivity.this, GphtMainActivity.class));
                    String ipServer = tvIP.getText().toString().trim();
//                    int id_dviqly = connection.getListIDMaDviQly().get(spDonVi.getSelectedItemPosition());
                    int id_dviqly = connection.getIDSelectDVIQLY(tvDonVi.getText().toString().split("-")[0].trim());
                    String user = etUser.getText().toString().trim();
                    String pass = etPass.getText().toString().trim();
//                    etIP.setError(null);
                    etUser.setError(null);
                    etPass.setError(null);

                    if(ipServer.isEmpty()){
                        Toast.makeText(GphtLoginActivity.this, "Bạn chưa nhập IP", Toast.LENGTH_LONG).show();
                    } else if (user.isEmpty()) {
                        etUser.setError("Bạn chưa nhập tài khoản");
                        etUser.requestFocus();
                    } else if (pass.isEmpty()) {
                        etPass.setError("Bạn chưa nhập mật khẩu");
                        etPass.requestFocus();
                    } else {
                        if (Common.isNetworkOnline(GphtLoginActivity.this)) {
//                            if(countLogin == 0) {
//                                login(user, pass, id_dviqly, true);
//                            } else {
                                GphtEntityLogQuyen entityLogQuyen = connection.getDataLogQuyen();
                                if(!asyncCallWSJson.WS_CHECK_CHANGE_CALL(entityLogQuyen.getID(), entityLogQuyen.getTHOI_GIAN())) {
                                    loadQuyen(v);
                                    Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                                            new StringBuilder("Dữ liệu trên máy chủ có thay đổi\nĐã cập nhật thành công vui lòng đăng nhập lại").toString(),
                                            Color.WHITE, "OK", Color.WHITE);
                                } else {
                                    login(user, pass, id_dviqly, true);
                                }
                                countLogin = 0;
                                savingPreferencesCountLogin(countLogin);
//                            }
                        } else {
                            if (countLogin > GphtConstantVariables.MAX_LOGIN) {
                                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                                        new StringBuilder("Bạn chỉ có tối đa ").append(GphtConstantVariables.MAX_LOGIN)
                                                .append(" lần đăng nhập offline\nVui lòng kết nối mạng để tiếp tục đăng nhập").toString(),
                                        Color.WHITE, "OK", Color.WHITE);
                            } else {
                                login(user, pass, id_dviqly, false);
                            }
                        }
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                            new StringBuilder("Lỗi đăng nhập: ").append(ex.getMessage()).toString(),
                            Color.WHITE, "OK", Color.WHITE);
                }
                break;
            case R.id.gpht_activity_login_ibSyn:
                showPopupMenu(v);
                break;
            case R.id.gpht_activity_login_ibSetting:
                showDialogUpdateIP();
                break;
        }
    }

    private void login(String user, String pass, int id_dviqly, boolean online){
        String password = Common.encryptPassword(pass).trim();
        Cursor c = connection.getLogin(user, password, id_dviqly);
        if(c.moveToFirst()) {
            if(!online) {
                countLogin++;
                savingPreferencesCountLogin(countLogin);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            int NV_ID = c.getInt(c.getColumnIndex("NV_ID"));
            String TGian_TCap = sdf.format(new Date());
            connection.insertDataLogLogin(NV_ID, TGian_TCap);
//            GphtLoginActivity.this.finish();

            Intent it = null;
            Cursor cHT = connection.getDataHeThong(NV_ID);
            if (cHT.moveToFirst()) {
                StringBuilder sbKeyHT = new StringBuilder();
                if(cHT.getCount() > 1) {
                    do {
                        sbKeyHT.append(cHT.getString(cHT.getColumnIndex("Key_HThong"))).append(";");
                    } while (cHT.moveToNext());
                    it = new Intent(GphtLoginActivity.this, GphtMainActivity.class);
                    sbKeyHT.substring(0, sbKeyHT.length() - 1);
                    it.putExtra("KEY_HTHONG", sbKeyHT.toString());
                    it.putExtra("NV_ID", NV_ID);
                    startActivity(it);
                } else {
                    String key = cHT.getString(cHT.getColumnIndex("Key_HThong"));
                    String MA_DVIQLY = connection.getMaSelectDVIQLY(id_dviqly);
                    if(key.equals("GCS")) {
                        startActivity(new Intent(GphtLoginActivity.this, GCSMainActivity.class));
                    } else if(key.equals("CD")) {
                        EsspCommon.setIP_SERVER_1(cHT.getString(cHT.getColumnIndex("Address")));
                        acJsonCD = EsspAsyncCallWSJson.getInstance();
                        Cursor cUser = connection.getUser(cHT.getInt(cHT.getColumnIndex("ID_User")),
                                cHT.getInt(cHT.getColumnIndex("ID_HeThong")));
                        if(cUser.moveToFirst()) {
                            String usernameCD = cUser.getString(cUser.getColumnIndex("Ten_DangNhap"));
                            String passwordCD = Common.decryptPassword(cUser.getString(cUser.getColumnIndex("Mat_Khau")));
                            loginCD(usernameCD, passwordCD, MA_DVIQLY, online);
                        }
                    } else if(key.equals("CN")) {
                    } else if(key.equals("TTHT")) {
                        TthtCommon.setIP_SERVER_1(cHT.getString(cHT.getColumnIndex("Address")));
                        asyncCallWSApi = TthtAsyncCallWSApi.getInstance();
                        Cursor cUser = connection.getUser(cHT.getInt(cHT.getColumnIndex("ID_User")),
                                cHT.getInt(cHT.getColumnIndex("ID_HeThong")));
                        if(cUser.moveToFirst()) {
                            String usernameTT = cUser.getString(cUser.getColumnIndex("Ten_DangNhap"));
                            String passwordTT = Common.decryptPassword(cUser.getString(cUser.getColumnIndex("Mat_Khau")));
                            loginTTHT(usernameTT, passwordTT, MA_DVIQLY, online);
                        }
                    }
                }
            }
        } else {
            Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                    "Sai thông tin đăng nhập hoặc bạn chưa đồng bộ danh mục", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void loginCD(final String user, final String pass, final String MA_DVIQLY, boolean online) {
        if(online){
            if(spDonVi.getCount() > 0){
                progressDialogLogin = ProgressDialog.show(GphtLoginActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true,false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            String result = acJsonCD.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(GphtLoginActivity.this));
                            checkLogin = Boolean.parseBoolean(result);
//
                            Message msgObj = handlerLogin.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("user", user);
                            b.putString("pass", pass);
                            b.putString("MA_NVIEN", MA_DVIQLY);
                            msgObj.setData(b);

                            handlerLogin.sendMessage(msgObj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } else {
                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        } else {
            if(spDonVi.getCount() > 0){
                EsspCommon.setMaDviqly(MA_DVIQLY);
                EsspCommon.setUSERNAME(user);
                startActivity(new Intent(GphtLoginActivity.this, EsspMainActivity.class));
            } else {
                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        }
    }

    private void loginTTHT(final String user, final String pass, final String MA_DVIQLY, boolean online) {
        if(online){
            if(spDonVi.getCount() > 0){
                progressDialogLogin = ProgressDialog.show(GphtLoginActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true,false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(50);
                            JSONObject result = asyncCallWSApi.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(GphtLoginActivity.this));
                            if(result != null){
                                GphtLoginActivity.this.runOnUiThread(new Runnable() {
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
                                if(!MA_NVIEN.equals("error")) {
                                    checkLogin = true;
                                    //VinhNB
                                    sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                                            .edit()
                                            .putString("BUNDLE_MA_NVIEN", MA_NVIEN)
                                            .commit();
                                    //end VinhNB
                                    TthtCommon.setMaNvien(MA_NVIEN);

                                    Message msgObj = handlerLoginTtht.obtainMessage();
                                    Bundle b = new Bundle();
                                    b.putString("user", user);
                                    b.putString("pass", pass);
                                    b.putString("MA_NVIEN", MA_DVIQLY);
                                    msgObj.setData(b);

                                    handlerLoginTtht.sendMessage(msgObj);
//                                    handlerLoginTtht.sendEmptyMessage(0);
                                } else {
                                    checkLogin = false;
                                    GphtLoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
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
                                GphtLoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                                                    "Đăng nhập thất bại. Kiểm tra lại thông tin cấu hình, hoặc liên hệ bộ phận hỗ trợ!", Color.WHITE, "OK", Color.WHITE);
                                            progressDialogLogin.dismiss();
                                        } catch (final Exception ex) {
                                            Log.i("---", "Exception in thread");
                                        }
                                    }
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } else {
                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        } else {
            if(spDonVi.getCount() > 0){
                //VinhNB
                String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE).getString("BUNDLE_MA_NVIEN", "");

                if (MA_NVIEN.equals("")) {
                    Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Đăng nhập thất bại.\n" +
                            "Vui lòng kiểm tra mạng và đặng nhập lại!", Color.WHITE, "OK", Color.WHITE);
                    return;
                }
                boolean checkInputOffline = connectionTtht.checkInfoInputCompairWithRememberTable(MA_DVIQLY, user, pass);
                if(!checkInputOffline){
                    Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Đăng nhập thất bại.\nVui lòng kiểm tra wifi và đặng nhập lại!", Color.WHITE, "OK", Color.WHITE);
                    return;
                }
                //end VinhNB
                TthtCommon.setMaDviqly(MA_DVIQLY);
                TthtCommon.setUSERNAME(user);

                startActivity(new Intent(GphtLoginActivity.this, TthtMainActivity.class));
            } else {
                Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        }
    }

    private Handler handlerLogin = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                progressDialogLogin.dismiss();
                if (!checkLogin) {
                    Common.showAlertDialogGreen(GphtLoginActivity.this, "Lỗi", Color.RED, "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);
                } else {
                    String MA_DVIQLY = msg.getData().getString("MA_NVIEN");
                    String user = msg.getData().getString("user");
                    EsspCommon.setMaDviqly(MA_DVIQLY);
                    EsspCommon.setUSERNAME(user);
                    startActivity(new Intent(GphtLoginActivity.this, EsspMainActivity.class));
                    if (connectionEssp.deleteAllDataRemember() != -1)
                        connectionEssp.insertDataRememger(msg.getData().getString("MA_NVIEN"),
                                msg.getData().getString("user"), msg.getData().getString("pass"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private Handler handlerLoginTtht = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GphtLoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressDialogLogin.dismiss();
                    } catch (final Exception ex) {
                        Log.i("---", "Exception in thread");
                    }
                }
            });
            if(!checkLogin){
                GphtLoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Common.showAlertDialogGreen(GphtLoginActivity.this, "Lỗi", Color.RED,
                                    "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);

                        } catch (final Exception ex) {
                            Log.i("---", "Exception in thread");
                        }
                    }
                });

            } else {
                String user = msg.getData().getString("user");
                String MA_DVIQLY = msg.getData().getString("MA_NVIEN");
                TthtCommon.setMaDviqly(MA_DVIQLY);
                TthtCommon.setUSERNAME(user);
                startActivity(new Intent(GphtLoginActivity.this, TthtMainActivity.class));
                if(connectionTtht.deleteAllDataRemember() != -1)
                    connectionTtht.insertDataRememger(MA_DVIQLY, user, msg.getData().getString("pass"));
            }
        }
    };

    private Handler handlerDongBo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            StringBuilder sbMSG = new StringBuilder();
            sbMSG.append("Đồng bộ thành công:\n");
            sbMSG.append(countDongBoDonVi).append("/").append(sumDongBoDonVi).append(" đơn vị").append("\n");
            sbMSG.append(countDongBoHeThong).append("/").append(sumDongBoHeThong).append(" hệ thống").append("\n");
            sbMSG.append(countDongBoUserCha).append("/").append(sumDongBoUserCha).append(" user đăng nhập").append("\n");
            sbMSG.append(countDongBoUserCon).append("/").append(sumDongBoUserCon).append(" user hệ thống").append("\n");
            Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                    sbMSG.toString(), Color.WHITE, "OK", Color.WHITE);
//            loadDataSpinner();
            progressDialog.dismiss();
        }
    };

    private Handler handlerQuyen = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Common.showAlertDialogGreen(GphtLoginActivity.this, "Thông báo", Color.WHITE,
                    new StringBuilder("Đồng bộ thành công ").append(countDongBoQuyen).append("/")
                            .append(sumDongBoQuyen).append(" danh mục").toString(), Color.WHITE, "OK", Color.WHITE);
            progressDialog.dismiss();
        }
    };

    public void savingPreferencesCountLogin(int countLogin) {
        SharedPreferences pre=getSharedPreferences(prefname, MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.putInt("COUNT_LOGIN", countLogin);
        editor.commit();
    }

    private void CheckFileConfigExist(){
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH + GphtConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                GphtCommon.cfgInfo = GphtCommon.GetFileConfig();
                if(GphtCommon.cfgInfo == null){
                    GphtCommon.cfgInfo = new GphtConfigInfo();
                }
            } else {
                GphtCommon.cfgInfo = new GphtConfigInfo();
                GphtCommon.CreateFileConfig(GphtCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(GphtLoginActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình", Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + GphtConstantVariables.PROGRAM_PATH;
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
                        for (int j = 0; j < GphtConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(GphtConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(GphtConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            GphtCommon.setIpServer(hmConfig.get("IP_SV_1"));
                            GphtCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(GphtLoginActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean saveConfig(String IP_SERVER, String VERSION){
        GphtConfigInfo config = new GphtConfigInfo(IP_SERVER, VERSION);
        String filePath = Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH + GphtConstantVariables.CFG_FILENAME;
        File cfgFile = new File(filePath);
        if(GphtCommon.CreateFileConfig(config, cfgFile, null)){
            GphtCommon.cfgInfo.setIP_SV_1(IP_SERVER);
            GphtCommon.cfgInfo.setVERSION(VERSION);
            return true;
        }
        return false;
    }

    //region Xử lý dialog
    public void showPopupMenu(final View v) {
        try {
            PopupMenu menu = new PopupMenu(GphtLoginActivity.this);
            menu.setHeaderTitle("Đồng bộ");
            menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case EsspConstantVariables.MENU_CHI_TIET:
                            loadDMuc(v);
                            break;
                        case EsspConstantVariables.MENU_KHAO_SAT:
                            loadQuyen(v);
                            break;

                    }
                }
            });
            // Add Menu (Android menu like style)
            menu.add(GphtConstantVariables.MENU_SYN_DMUC, "Đồng bộ danh mục").setIcon(null);
            menu.add(GphtConstantVariables.MENU_SYN_QUYEN, "Đồng bộ quyền").setIcon(null);
            menu.show(v);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GphtLoginActivity.this,
                    "Lỗi", Color.RED, new StringBuilder("Lỗi hiển thị menu đồng bộ:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogUpdateIP() {
        try {
            final Dialog dialog = new Dialog(GphtLoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gpht_dialog_setting);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            final EditText etIP = (EditText) dialog.findViewById(R.id.gpht_dialog_setting_et_soluong);
            Button btOK = (Button) dialog.findViewById(R.id.gpht_dialog_setting_bt_luu_soluong);
            final Spinner spDvi = (Spinner) dialog.findViewById(R.id.gpht_dialog_setting_spDvi);
            LinearLayout llBackground = (LinearLayout) dialog.findViewById(R.id.gpht_dialog_setting_ll_Background);

            llBackground.setBackgroundColor(Color.WHITE);
            String oldIP = GphtCommon.cfgInfo.getIP_SV_1();
            etIP.setText(oldIP);

            loadDataSpinner(spDvi);

            btOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String version = GphtCommon.cfgInfo.getVERSION();
                        String IP = etIP.getText().toString().trim();
                        if(!IP.isEmpty() && !IP.equals(GphtCommon.cfgInfo.getIP_SV_1())){
                            saveConfig(IP, !version.isEmpty()?version:"HN");
                            GphtCommon.setVERSION(!version.isEmpty()?version:"HN");
                            GphtCommon.cfgInfo.setIP_SV_1(IP);
                            tvIP.setText(IP);
                        }
                        if(spDvi.getCount() > 0){
                            tvDonVi.setText(spDvi.getSelectedItem().toString());
                        }
                        dialog.dismiss();
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(GphtLoginActivity.this,
                                "Lỗi", Color.RED, "Lỗi cập nhật ghi chú", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GphtLoginActivity.this,
                    "Lỗi", Color.RED, "Lỗi cập nhật ghi chú", Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion
}
