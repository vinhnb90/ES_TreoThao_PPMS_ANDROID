package es.vinhnb.ttht.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.es.tungnv.views.R;
import java.util.ArrayList;
import java.util.List;

import esolutions.com.esloginlib.common.SharePrefManager;
import esolutions.com.esloginlib.example.DepartEntity;
import esolutions.com.esloginlib.lib.DepartUpdateFragment;
import esolutions.com.esloginlib.lib.DepartViewEntity;
import esolutions.com.esloginlib.lib.LoginFragment;
import esolutions.com.esloginlib.lib.LoginInteface;
import esolutions.com.esloginlib.lib.LoginViewEntity;


public class TthtHnLoginActivity extends AppCompatActivity implements LoginInteface<DepartEntity> {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private List<DepartEntity> data;
    private SharePrefManager mPrefManager;

    public static final String PREF_CONFIG = "PREF_CONFIG";

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
                    .Builder(TthtHnLoginActivity.this, R.layout.fragment_login, R.id.et_url, R.id.et_user, R.id.et_pass, R.id.cb_save_info, R.id.btn_login, R.id.pbar_login, R.id.tv_title_appname, R.id.cl_frag_login)
                    .setLlModuleLoginID(R.id.ll_include_login)
                    .setIvIconLoginID(R.id.iv_image_app)
                    .build();

            DepartViewEntity DepartViewEntity = new DepartViewEntity
                    .Builder(TthtHnLoginActivity.this, R.layout.layout_department, R.id.spin_dvi, R.id.ibtn_download_dvi, R.id.pbar_download_dvi)
                    .build();

            //setup module
            DepartUpdateFragment<DepartEntity> departmentModule = new DepartUpdateFragment()
                    .setViewEntity(DepartViewEntity)
                    .setShowModule(true);

            //setup View
            Fragment loginFragment = LoginFragment.newInstance(null)
                    .setmLoginViewEntity(loginViewEntity)
                    .setmDepartModule(departmentModule)
                    .setmTitleAppName("Treo tháo hiện trường")
                    .setmIconLogin(R.mipmap.ic_home, (int) getResources().getDimension(R.dimen._50sdp), (int) getResources().getDimension(R.dimen._50sdp))
                    .setmColorBackground(R.color.colorLoginDemo)
                    .setmLoginSharedPref(mPrefManager.getSharePref(PREF_CONFIG, MODE_PRIVATE));

            //call View
            transaction.replace(R.id.rl_ac_login, loginFragment).commit();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void openMainView() {
        startActivity(new Intent(TthtHnLoginActivity.this, TthtHnMainActivity.class));
    }

    @Override
    public List<DepartEntity> callServerDepart() {
        return data;
    }

    @Override
    public boolean checkServerLogin(String depart, String user, String pass) {
        return true;
    }

    @Override
    public boolean checkSessionLogin(boolean hasModeLoginOffline, String depart, String mUser, String mPass) {
        return false;
    }

    @Override
    public void saveDBDepart(List<DepartEntity> list) throws Exception {

    }

    @Override
    public List<DepartEntity> selectDBDepart() {
        return data;
    }

    public static void checkSharePreference(SharePrefManager mPrefManager) {
        if (!mPrefManager.checkExistSharePref(PREF_CONFIG)) {
            mPrefManager.addSharePref(PREF_CONFIG, MODE_PRIVATE);
        }
    }
}
