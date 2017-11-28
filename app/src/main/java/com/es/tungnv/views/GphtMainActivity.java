package com.es.tungnv.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.db.GphtSqliteConnection;
import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.enums.GcsEnumAnim;
import com.es.tungnv.sharepref.SharePrefManager;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;
import com.es.tungnv.webservice.TthtAsyncCallWSApi;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by TUNGNV on 3/23/2016.
 */
public class GphtMainActivity extends Activity implements View.OnClickListener{

    private ImageView ivBanner;
    private Button btDangXuat, btGCS, btCapDien, btChamNo, btTreoThao;
    private LinearLayout llButton;
    private RelativeLayout rlBanner;
    private Animation animBounce;

    private String prefname = "config";

    private Dialog alertDialog;

    private GphtSqliteConnection connection;
    private EsspSqliteConnection connectionEssp;
    private TthtSQLiteConnection connectionTtht;
    private int NV_ID;

    private ProgressDialog progressDialogLogin;
    private EsspAsyncCallWSJson acJsonCD;
    private boolean checkLogin = false;
    private TthtAsyncCallWSApi asyncCallWSApi;
    private static SharePrefManager sSharePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.gpht_activity_main_2);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            connection = GphtSqliteConnection.getInstance(this);
            connectionEssp = EsspSqliteConnection.getInstance(this);
            connectionTtht = TthtSQLiteConnection.getInstance(this);
//            NV_ID = getIntent().getExtras().getInt("NV_ID");
            initComponent();
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi:", Color.RED, "Lỗi khởi tạo: " + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE)
                    .getString("BUNDLE_MA_NVIEN", "");
            TthtCommon.setMaNvien(MA_NVIEN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponent(){
        ivBanner = (ImageView) findViewById(R.id.gpht_activity_main_2_ivBanner);
        btDangXuat = (Button) findViewById(R.id.gpht_activity_main_2_btDangXuat);
        btGCS = (Button) findViewById(R.id.gpht_activity_main_2_btGCS);
        btCapDien = (Button) findViewById(R.id.gpht_activity_main_2_btCapDien);
        btChamNo = (Button) findViewById(R.id.gpht_activity_main_2_btChanNo);
        btTreoThao = (Button) findViewById(R.id.gpht_activity_main_2_btTreoThao);
        llButton = (LinearLayout) findViewById(R.id.gpht_activity_main_2_llButton);
        rlBanner = (RelativeLayout) findViewById(R.id.gpht_activity_main_2_rlBanner);

        //region Cấu hình tạm
        btGCS.setVisibility(View.GONE);
        btChamNo.setVisibility(View.GONE);
        //endregion

        //region Cầu hình theo file config
//        String key = getIntent().getExtras().getString("KEY_HTHONG");
//        if(key.contains("GCS")) {
//            btGCS.setVisibility(View.VISIBLE);
//        } else {
//            btGCS.setVisibility(View.GONE);
//        }
//        if(key.contains("CD")) {
//            btCapDien.setVisibility(View.VISIBLE);
//        } else {
//            btCapDien.setVisibility(View.GONE);
//        }
//        if(key.contains("CN")) {
//            btChamNo.setVisibility(View.VISIBLE);
//        } else {
//            btChamNo.setVisibility(View.GONE);
//        }
//        if(key.contains("TTHT")) {
//            btTreoThao.setVisibility(View.VISIBLE);
//        } else {
//            btTreoThao.setVisibility(View.GONE);
//        }
        //endregion

        int width = getWindowManager().getDefaultDisplay().getWidth();
        Bitmap bmpBanner = Common.drawableToBitmap(ivBanner.getDrawable());
        int wBmp = bmpBanner.getWidth();
        int hBmp = bmpBanner.getHeight();
        rlBanner.getLayoutParams().height = width * hBmp / wBmp;
        btGCS.getLayoutParams().width = (int)(width/1.6f);
        btCapDien.getLayoutParams().width = (int)(width/1.6f);
        btChamNo.getLayoutParams().width = (int)(width/1.6f);
        btTreoThao.getLayoutParams().width = (int)(width/1.6f);

        setAnimationLayout(btDangXuat, R.anim.left_to_right);
        setAnimationLayout(btGCS, R.anim.left_to_right);
        setAnimationLayout(btCapDien, R.anim.left_to_right_2);
        setAnimationLayout(btChamNo, R.anim.left_to_right_3);
        setAnimationLayout(btTreoThao, R.anim.left_to_right_4);

        btDangXuat.setOnClickListener(this);
        btGCS.setOnClickListener(this);
        btCapDien.setOnClickListener(this);
        btChamNo.setOnClickListener(this);
        btTreoThao.setOnClickListener(this);

        sSharePrefManager = SharePrefManager.getInstance(this);
        sSharePrefManager.addSharePref("shareRefMaNhanVien", MODE_PRIVATE);
    }

    public void savingPreferences(String MA_DVIQLY, int TRANG_THAI, String KEY) {
        try {
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("MA_DVIQLY", MA_DVIQLY);
            editor.putInt("TRANG_THAI", TRANG_THAI);
            editor.putString("KEY", KEY);
            editor.commit();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi:", Color.RED, "Lỗi lưu cấu hình: " + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    public void savingPreferencesIP(String IP) {
        try {
            SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("IP", IP);
            editor.commit();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi:", Color.RED, "Lỗi lưu cấu hình: " + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    public void restoringPreferences() {
        SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
        String MA_DVIQLY = pre.getString("MA_DVIQLY", "");
        String KEY = pre.getString("KEY", "");
        int TRANG_THAI = pre.getInt("TRANG_THAI", 0);
    }

    public String getIP() {
        SharedPreferences pre = getSharedPreferences(prefname, MODE_PRIVATE);
        return pre.getString("IP", "");
    }

    private void moveViewToScreenCenter(View view, GcsEnumAnim eCheck) {
//        AnimationSet set = new AnimationSet(true);
//
//        RelativeLayout root = (RelativeLayout) findViewById( R.id.rootLayout );
//        DisplayMetrics dm = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
//
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        ibClose.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//
//        int originalPos[] = new int[2];
//        view.getLocationOnScreen(originalPos);
//
//        int xDest = dm.widthPixels/2;
//        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;
//        TranslateAnimation anim = null;
//        if(eCheck == GcsEnumAnim.ESSP) {
//            anim = new TranslateAnimation(xDest - view.getMeasuredWidth() / 2 - Common.dipToPixels(this, 5), 0, 0, 0);
//            anim.setDuration(500);
//        } else if(eCheck == GcsEnumAnim.GCS) {
//            anim = new TranslateAnimation(view.getMeasuredWidth() / 2 + ibClose.getMeasuredWidth()/2, 0, view.getMeasuredHeight(), 0);
//            anim.setDuration(600);
//        } else if(eCheck == GcsEnumAnim.INVOICE) {
//            anim = new TranslateAnimation(-view.getMeasuredWidth() / 2 - ibClose.getMeasuredWidth()/2, 0, view.getMeasuredHeight(), 0);
//            anim.setDuration(700);
//        } else {
//            anim = new TranslateAnimation(-xDest + view.getMeasuredWidth() / 2 + Common.dipToPixels(this, 5), 0, 0, 0);
//            anim.setDuration(800);
//        }
//        anim.setFillAfter(true);
//
//        set.addAnimation(anim);
//        set.addAnimation(createAlphaAnimation());
//
//        view.startAnimation(set);
    }

    private static Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 1440 : 0, expanded ? 0 : 1440, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(700);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }

    private static Animation createscaleAnimation() {
        Animation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f);
        animation.setStartOffset(0);
        animation.setDuration(700);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }

    private static Animation createAlphaAnimation() {
        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setStartOffset(0);
        animation.setDuration(700);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gpht_activity_main_2_btCapDien:
                //region Code tạm
                startActivity(new Intent(GphtMainActivity.this, EsspLoginActivity.class));
                //endregion

                //region Code gốc
//                Cursor cHT = connection.getDataHeThong(NV_ID, "CD");
//                if (cHT.moveToFirst()) {
//                    EsspCommon.setIP_SERVER_1(cHT.getString(cHT.getColumnIndex("Address")));
//                    acJsonCD = EsspAsyncCallWSJson.getInstance();
//                    String MA_DVIQLY = connection.getMaSelectDVIQLY(cHT.getInt(cHT.getColumnIndex("ID_DonVi")));
//                    Cursor cUser = connection.getUser(cHT.getInt(cHT.getColumnIndex("ID_User")),
//                            cHT.getInt(cHT.getColumnIndex("ID_HeThong")));
//                    if(cUser.moveToFirst()) {
//                        String usernameCD = cUser.getString(cUser.getColumnIndex("Ten_DangNhap"));
//                        String passwordCD = Common.decryptPassword(cUser.getString(cUser.getColumnIndex("Mat_Khau")));
//                        loginCD(usernameCD, passwordCD, MA_DVIQLY, Common.isNetworkOnline(GphtMainActivity.this));
//                    }
//                }
                //endregion
                break;
            case R.id.gpht_activity_main_2_btGCS:
                startActivity(new Intent(GphtMainActivity.this, GCSMainActivity.class));
                break;
            case R.id.gpht_activity_main_2_btChanNo:
                startActivity(new Intent(GphtMainActivity.this, InvoiceLoginActivity.class));
                break;
            case R.id.gpht_activity_main_2_btTreoThao:
                //region Code tạm
                startActivity(new Intent(GphtMainActivity.this, GphtLoginActivity.class));
                //endregion

                //region Code gốc
//                Cursor cHT2 = connection.getDataHeThong(NV_ID, "TTHT");
//                if (cHT2.moveToFirst()) {
//                    TthtCommon.setIP_SERVER_1(cHT2.getString(cHT2.getColumnIndex("Address")));
//                    asyncCallWSApi = TthtAsyncCallWSApi.getInstance();
//                    String MA_DVIQLY = connection.getMaSelectDVIQLY(cHT2.getInt(cHT2.getColumnIndex("ID_DonVi")));
//                    Cursor cUser = connection.getUser(cHT2.getInt(cHT2.getColumnIndex("ID_User")),
//                            cHT2.getInt(cHT2.getColumnIndex("ID_HeThong")));
//                    if(cUser.moveToFirst()) {
//                        String usernameCD = cUser.getString(cUser.getColumnIndex("Ten_DangNhap"));
//                        String passwordCD = Common.decryptPassword(cUser.getString(cUser.getColumnIndex("Mat_Khau")));
//                        loginTTHT(usernameCD, passwordCD, MA_DVIQLY, Common.isNetworkOnline(GphtMainActivity.this));
//                    }
//                }
                //endregion
                break;
            case R.id.gpht_activity_main_2_btDangXuat:
                this.finish();
                break;
        }
    }

    private void loginCD(final String user, final String pass, final String MA_DVIQLY, boolean online) {
        if(online){
            if(!MA_DVIQLY.isEmpty()){
                progressDialogLogin = ProgressDialog.show(GphtMainActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true,false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            String result = acJsonCD.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(GphtMainActivity.this));
                            checkLogin = Boolean.parseBoolean(result);
//
                            Message msgObj = handlerLoginCD.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("user", user);
                            b.putString("pass", pass);
                            b.putString("MA_DVIQLY", MA_DVIQLY);
                            msgObj.setData(b);

                            handlerLoginCD.sendMessage(msgObj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } else {
                Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        } else {
            if(!MA_DVIQLY.isEmpty()){
                EsspCommon.setMaDviqly(MA_DVIQLY);
                EsspCommon.setUSERNAME(user);
                startActivity(new Intent(GphtMainActivity.this, EsspMainActivity.class));
            } else {
                Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
            }
        }
    }

    private Handler handlerLoginCD = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                progressDialogLogin.dismiss();
                if (!checkLogin) {
                    Common.showAlertDialogGreen(GphtMainActivity.this, "Lỗi", Color.RED, "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);
                } else {
                    String MA_DVIQLY = msg.getData().getString("MA_DVIQLY");
                    String user = msg.getData().getString("user");
                    EsspCommon.setMaDviqly(MA_DVIQLY);
                    EsspCommon.setUSERNAME(user);
                    startActivity(new Intent(GphtMainActivity.this, EsspMainActivity.class));
                    if (connectionEssp.deleteAllDataRemember() != -1)
                        connectionEssp.insertDataRememger(msg.getData().getString("MA_DVIQLY"),
                                msg.getData().getString("user"), msg.getData().getString("pass"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private void loginTTHT(final String user, final String pass, final String MA_DVIQLY, boolean online) {
        try {
            if (online) {
                if (!MA_DVIQLY.isEmpty()) {
                    progressDialogLogin = ProgressDialog.show(GphtMainActivity.this, "Please Wait ...", "Đang kiểm tra thông tin đăng nhập ...", true, false);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(50);
                                JSONObject result = asyncCallWSApi.WS_LOGIN_CALL(MA_DVIQLY, user, pass, Common.GetIMEI(GphtMainActivity.this));
                                if (result != null) {
                                    GphtMainActivity.this.runOnUiThread(new Runnable() {
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
                                                .putString("BUNDLE_MA_NVIEN", MA_NVIEN)
                                                .commit();
                                        //end VinhNB
                                        TthtCommon.setMaNvien(MA_NVIEN);

                                        Message msgObj = handlerLoginTtht.obtainMessage();
                                        Bundle b = new Bundle();
                                        b.putString("user", user);
                                        b.putString("pass", pass);
                                        b.putString("MA_DVIQLY", MA_DVIQLY);
                                        msgObj.setData(b);

                                        handlerLoginTtht.sendMessage(msgObj);
//                                    handlerLoginTtht.sendEmptyMessage(0);
                                    } else {
                                        checkLogin = false;
                                        GphtMainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE,
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
                                    GphtMainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE,
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
                    Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                }
            } else {
                if (!MA_DVIQLY.isEmpty()) {
                    //VinhNB
                    String MA_NVIEN = sSharePrefManager.getSharePref("shareRefMaNhanVien", MODE_PRIVATE).getString("BUNDLE_MA_NVIEN", "");

                    if (MA_NVIEN.equals("")) {
                        Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Đăng nhập thất bại.\n" +
                                "Vui lòng kiểm tra mạng và đặng nhập lại!", Color.WHITE, "OK", Color.WHITE);
                        return;
                    }
                    boolean checkInputOffline = connectionTtht.checkInfoInputCompairWithRememberTable(MA_DVIQLY, user, pass);
                    if (!checkInputOffline) {
                        Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Đăng nhập thất bại.\nVui lòng kiểm tra wifi và đặng nhập lại!", Color.WHITE, "OK", Color.WHITE);
                        return;
                    }
                    //end VinhNB
                    TthtCommon.setMaDviqly(MA_DVIQLY);
                    TthtCommon.setUSERNAME(user);

                    startActivity(new Intent(GphtMainActivity.this, TthtMainActivity.class));
                } else {
                    Common.showAlertDialogGreen(GphtMainActivity.this, "Thông báo", Color.WHITE, "Chưa có danh mục mã đơn vị quản lý", Color.WHITE, "OK", Color.WHITE);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private Handler handlerLoginTtht = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            GphtMainActivity.this.runOnUiThread(new Runnable() {
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
                GphtMainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Common.showAlertDialogGreen(GphtMainActivity.this, "Lỗi", Color.RED,
                                    "Sai thông tin đăng nhập hoặc máy chưa được đăng ký sử dụng", Color.WHITE, "OK", Color.RED);

                        } catch (final Exception ex) {
                            Log.i("---", "Exception in thread");
                        }
                    }
                });

            } else {
                String user = msg.getData().getString("user");
                String MA_DVIQLY = msg.getData().getString("MA_DVIQLY");
                TthtCommon.setMaDviqly(MA_DVIQLY);
                TthtCommon.setUSERNAME(user);
                startActivity(new Intent(GphtMainActivity.this, TthtMainActivity.class));
                if(connectionTtht.deleteAllDataRemember() != -1)
                    connectionTtht.insertDataRememger(MA_DVIQLY, user, msg.getData().getString("pass"));
            }
        }
    };
    
    public void setAnimationLayout(View v, int resource){
        animBounce = AnimationUtils.loadAnimation(GphtMainActivity.this, resource);
        v.startAnimation(animBounce);
    }

    private void turnGPSOn(){
        try{
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Kích hoạt GPS");
                builder.setMessage("Bạn phải kích hoạt GPS để tiếp tục sử dụng chương trình\nKích hoạt ở chế độ chính xác cao để thu thập tọa độ chính xác nhất");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                Geocoder gcd = new Geocoder(GphtMainActivity.this, Locale.getDefault());
                double lat = 0d;
                double lng = 0d;
                List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
            }
        } catch(Exception ex) {
            Log.e("error", ex.toString());
        }
    }

}
