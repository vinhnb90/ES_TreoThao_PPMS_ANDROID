package com.esolutions.esloginlib.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.esolutions.esloginlib.R;
import com.esolutions.esloginlib.common.SharePrefManager;
import com.esolutions.esloginlib.lib.DepartUpdateFragment;
import com.esolutions.esloginlib.lib.DepartViewEntity;
import com.esolutions.esloginlib.lib.LoginFragment;
import com.esolutions.esloginlib.lib.LoginInteface;
import com.esolutions.esloginlib.lib.LoginViewEntity;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements LoginInteface<DepartEntity> {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private List<DepartEntity> data;
    private SharePrefManager mPrefManager;

    private static final String PREF_CONFIG = "PREF_CONFIG";
    private static final String KEY_PREF_SERVER_URL = "KEY_PREF_SERVER_URL";
    private static final String KEY_PREF_POS_DVI = "KEY_PREF_POS_DVI";
    private static final String KEY_PREF_USER = "KEY_PREF_USER";
    private static final String KEY_PREF_PASS = "KEY_PREF_PASS";
    private static final String KEY_PREF_CB_SAVE = "KEY_PREF_CB_SAVE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();

            data = new ArrayList<>();
            data.add(new DepartEntity(1, "PD", "Tổng công ty Điện Lực Hà Nội"));
            data.add(new DepartEntity(2, "PD0100", "Điện lực Hoàn Kiếm"));
            mPrefManager = SharePrefManager.getInstance(this);
            checkSharePreference(mPrefManager);

            //setup dataView

            LoginViewEntity loginViewEntity = new LoginViewEntity
                    .Builder(LoginActivity.this, R.layout.fragment_login, R.id.et_url, R.id.et_user, R.id.et_pass, R.id.cb_save_info, R.id.btn_login, R.id.pbar_login, R.id.tv_title_appname, R.id.cl_frag_login, R.id.ibtn_visible_pass)
                    .setLlModuleLoginID(R.id.ll_include_login)
                    .setIvIconLoginID(R.id.iv_image_app)
                    .setTvImei(R.id.tv_imei)
                    .setTvVersion(R.id.tv_version)
                    .build();

            DepartViewEntity DepartViewEntity = new DepartViewEntity
                    .Builder(LoginActivity.this, R.layout.layout_department, R.id.spin_dvi, R.id.ibtn_download_dvi, R.id.pbar_download_dvi)
                    .build();

            //setup module
            DepartUpdateFragment<DepartEntity> departmentModule = new DepartUpdateFragment()
//                .setmListDepart(data)
                    .setViewEntity(DepartViewEntity)
                    .setShowModule(true);


            //set up mode login offline
            LoginFragment.ILoginOffline loginOfflineModeConfig = new LoginFragment.ILoginOffline() {
                @Override
                public boolean checkSessionLogin(LoginFragment.LoginData loginData) throws Exception {
                    return false;
                }

                @Override
                public void saveSessionDatabaseLogin(LoginFragment.LoginData dataLoginSession) {

                }

                @Override
                public String getCodeDepart(int pos) {
                    return null;
                }
            };

            //setup View
            Fragment loginFragment = LoginFragment.newInstance(null)
                    .setmLoginViewEntity(loginViewEntity)
                    .setmDepartModule(departmentModule)
                    .setmILoginOffline(loginOfflineModeConfig)
                    .setmTitleAppName("Treo tháo hiện trường")
                    .setmIconLogin(R.mipmap.ic_home, (int) getResources().getDimension(R.dimen._50sdp), (int) getResources().getDimension(R.dimen._50sdp))
                    .setmColorBackground(R.color.colorPrimary)
                    .setmLoginSharedPref(mPrefManager.getSharePref(PREF_CONFIG, MODE_PRIVATE));

            //call View
            transaction.replace(R.id.cl_ac_login, loginFragment).commit();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void openMainView(LoginFragment.LoginData mLoginData) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public List<DepartEntity> callServerDepart() {
        return data;
    }

    @Override
    public LoginFragment.ResultLogin checkServerLogin(LoginFragment.LoginData loginData) {
        return null;
    }

    @Override
    public void saveDBDepart(List<DepartEntity> list) throws Exception {

    }

    @Override
    public List<DepartEntity> selectDBDepart() {
        return data;
    }

    @Override
    public void saveDataSharePref(LoginFragment.LoginData loginData) throws Exception {
        mPrefManager.getSharePref(PREF_CONFIG, MODE_PRIVATE)
                .edit()
                .putString(KEY_PREF_SERVER_URL, loginData.getmURL())
                .putInt(KEY_PREF_POS_DVI, loginData.getmPosDvi())
                .putString(KEY_PREF_USER, loginData.getmUser())
                .putString(KEY_PREF_PASS, loginData.getmPass())
                .putBoolean(KEY_PREF_CB_SAVE, loginData.ismIsSaveInfo())
                .commit();
    }


    @Override
    public LoginFragment.LoginData getDataLoginSharedPref() {
        try {
            SharedPreferences sharedPreferences = mPrefManager.getSharePref(PREF_CONFIG, MODE_PRIVATE);
            String ip = sharedPreferences.getString(KEY_PREF_SERVER_URL, "");
            int posSpinDvi = sharedPreferences.getInt(KEY_PREF_POS_DVI, 0);
            String user = sharedPreferences.getString(KEY_PREF_USER, "");
            String pass = sharedPreferences.getString(KEY_PREF_PASS, "");
            boolean isCheckSave = sharedPreferences.getBoolean(KEY_PREF_CB_SAVE, false);


            //convert data save of login activity to data save of Login fragment
            LoginFragment.LoginData loginData = new LoginFragment.LoginData
                    .Builder(ip, user, pass)
                    .setmDvi(posSpinDvi, "")
                    .setmIsSaveInfo(isCheckSave)
                    .build();

            return loginData;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkSharePreference(SharePrefManager mPrefManager) {
        if (!mPrefManager.checkExistSharePref(PREF_CONFIG)) {
            mPrefManager.addSharePref(PREF_CONFIG, MODE_PRIVATE);
        }
        mPrefManager.getSharePref(PREF_CONFIG, MODE_PRIVATE)
                .edit()
                .putString(KEY_PREF_SERVER_URL, "")
                .putInt(KEY_PREF_POS_DVI, 0)
                .putString(KEY_PREF_USER, "")
                .putString(KEY_PREF_PASS, "")
                .putBoolean(KEY_PREF_CB_SAVE, false)
                .commit();
    }
}
