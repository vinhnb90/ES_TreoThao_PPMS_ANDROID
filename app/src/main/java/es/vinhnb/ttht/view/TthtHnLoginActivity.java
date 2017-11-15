package es.vinhnb.ttht.view;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.example.DepartEntity;
import com.esolutions.esloginlib.lib.DepartUpdateFragment;
import com.esolutions.esloginlib.lib.DepartViewEntity;
import com.esolutions.esloginlib.lib.LoginFragment;
import com.esolutions.esloginlib.lib.LoginInteface;
import com.esolutions.esloginlib.lib.LoginViewEntity;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.entity.sharedpref.LoginSharePref;
import es.vinhnb.ttht.entity.sqlite.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.entity.sqlite.TABLE_BBAN_CTO;
import es.vinhnb.ttht.entity.sqlite.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.entity.sqlite.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.entity.sqlite.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.entity.sqlite.TABLE_CONGTO_TI;
import es.vinhnb.ttht.entity.sqlite.TABLE_CONGTO_TU;
import es.vinhnb.ttht.entity.sqlite.TABLE_DVIQLY;
import es.vinhnb.ttht.entity.sqlite.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.entity.sqlite.TABLE_SESSION;
import es.vinhnb.ttht.entity.sqlite.TABLE_TRAM;
import es.vinhnb.ttht.entity.sqlite.TthtHnDbConfig;
import esolutions.com.esdatabaselib.baseSharedPref.SharePrefManager;
import esolutions.com.esdatabaselib.baseSqlite.SqlDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;


public class TthtHnLoginActivity extends TthtHnBaseActivity implements LoginInteface<TABLE_DVIQLY> {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private List<TABLE_DVIQLY> listDepart;
    private SharePrefManager mPrefManager;
    private LoginFragment loginFragment;
    private LoginSharePref dataLoginSharePref;
    private SqlDAO mSqlDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //save savedInstanceState
            super.savedInstanceState = savedInstanceState;


            //check permission
            if (Common.checkPermission(this))
                return;


            //init view
            initDataAndView();


            //set action
            setAction(savedInstanceState);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //fill listDepart shared pref
            fillDataSharePref();
        } catch (Exception e) {
            e.printStackTrace();


            try {
                super.showSnackBar(e.getMessage());
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(this, e1.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case Common.REQUEST_CODE_PERMISSION: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED
                        || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TthtHnLoginActivity.this, "Unable to show permission required", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        //init view
                        initDataAndView();


                        setAction(super.savedInstanceState);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

    private void fillDataSharePref() throws Exception {
        try {
            dataLoginSharePref = (LoginSharePref) mPrefManager.getSharePrefObject(LoginSharePref.class);


            if (dataLoginSharePref != null) {
                //show listDepart
                loginFragment.getmLoginViewEntity().getEtURL().setText(dataLoginSharePref.ip);
                loginFragment.getmLoginViewEntity().getEtUser().setText(dataLoginSharePref.user);
                loginFragment.getmLoginViewEntity().getEtPass().setText(dataLoginSharePref.pass);


                //check and set pos for dvi spin
                if (loginFragment.getmDepartModule() == null)
                    return;
                AppCompatSpinner spDvi = ((DepartUpdateFragment<DepartEntity>) loginFragment.getmDepartModule()).getViewEntity().getSpDvi();
                spDvi.setSelection(dataLoginSharePref.posSpinDvi);
                spDvi.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(Common.MESSAGE.ex01.getContent());
        }
    }

    //region LoginInteface
    @Override
    public void openMainView() {
        startActivity(new Intent(TthtHnLoginActivity.this, TthtHnMainActivity.class));
    }

    @Override
    public List<TABLE_DVIQLY> callServerDepart() {
        return listDepart;
    }

    @Override
    public boolean checkServerLogin(String depart, String user, String pass) {
        return true;
    }

    @Override
    public boolean checkSessionLogin(boolean hasModeLoginOffline, String depart, String mUser, String mPass) throws Exception {
        if (hasModeLoginOffline) {
            //create data check
            TABLE_SESSION dataCheck = new TABLE_SESSION();
            dataCheck.setMA_DVIQLY(depart);
            dataCheck.setUSERNAME(mUser);
            dataCheck.setPASSWORD(mPass);


            //check row in TABLE_SESSION
            return mSqlDAO.checkRows(TABLE_SESSION.class, dataCheck);
        } else
            return true;
    }

    @Override
    public void saveDBDepart(List<TABLE_DVIQLY> list) throws Exception {
        mSqlDAO.insert(list);
    }

    @Override
    public List<TABLE_DVIQLY> selectDBDepart() {
        return mSqlDAO.selectAllLazy(TABLE_DVIQLY.class, null);
    }

    @Override
    public void saveDataSharePref(LoginFragment.LoginSharePrefData loginSharePrefData) throws Exception {
        //convert data save of login fragment to data save of Login activity
        dataLoginSharePref = new LoginSharePref(loginSharePrefData.getmURL(), loginSharePrefData.getmPosDvi(), loginSharePrefData.getmUser(), loginSharePrefData.getmPass(), loginSharePrefData.ismIsSaveInfo());
        mPrefManager.writeDataSharePref(LoginSharePref.class, dataLoginSharePref);
    }

    @Override
    public LoginFragment.LoginSharePrefData getDataLoginSharedPref() throws Exception {
        //get data shared pref now
        dataLoginSharePref = (LoginSharePref) mPrefManager.getSharePrefObject(LoginSharePref.class);


        //convert data save of login activity to data save of Login fragment
        LoginFragment.LoginSharePrefData loginSharePrefData = new LoginFragment.LoginSharePrefData
                .Builder(dataLoginSharePref.ip, dataLoginSharePref.user, dataLoginSharePref.pass)
                .setmPosDvi(dataLoginSharePref.posSpinDvi)
                .setmIsSaveInfo(dataLoginSharePref.isCheckSave)
                .build();


        return loginSharePrefData;
    }
    //endregion

    //region TthtHnBaseActivity
    @Override
    void initDataAndView() throws Exception {
        //set view full screen
        setContentView(R.layout.activity_login);
        super.setupFullScreen();


        //create database
        SqlHelper.setupDB(
                TthtHnLoginActivity.this,
                TthtHnDbConfig.class,
                new Class[]{
                        TABLE_ANH_HIENTRUONG.class,
                        TABLE_BBAN_CTO.class,
                        TABLE_BBAN_TUTI.class,
                        TABLE_CHITIET_CTO.class,
                        TABLE_CHITIET_TUTI.class,
                        TABLE_CONGTO_TI.class,
                        TABLE_CONGTO_TU.class,
                        TABLE_DVIQLY.class,
                        TABLE_LOAI_CONG_TO.class,
                        TABLE_SESSION.class,
                        TABLE_TRAM.class,
                });


        //call Database access object
        mSqlDAO = new SqlDAO(SqlHelper.getIntance().openDB(), this);


        //try reload because lib reload is not working
        Toast.makeText(this, SqlHelper.getDatabasePath(), Toast.LENGTH_SHORT).show();


        //create shared pref
        ArrayList<Class<?>> setClassSharedPrefConfig = new ArrayList<Class<?>>();
        setClassSharedPrefConfig.add(LoginSharePref.class);
        mPrefManager = SharePrefManager.getInstance(this, setClassSharedPrefConfig);


        //dump data test
        listDepart = new ArrayList<>();
        listDepart.add(new TABLE_DVIQLY(1, "PD", "Tổng công ty Điện Lực Hà Nội"));
        listDepart.add(new TABLE_DVIQLY(2, "PD0100", "Điện lực Hoàn Kiếm"));


        //setup dataView login
        LoginViewEntity loginViewEntity = new LoginViewEntity
                .Builder(TthtHnLoginActivity.this, R.layout.fragment_login, R.id.et_url, R.id.et_user, R.id.et_pass, R.id.cb_save_info, R.id.btn_login, R.id.pbar_login, R.id.tv_title_appname, R.id.cl_frag_login, R.id.ibtn_visible_pass)
                .setLlModuleLoginID(R.id.ll_include_login)
                .setIvIconLoginID(R.id.iv_image_app)
                .setTvVersion(R.id.tv_version)
                .setTvImei(R.id.tv_imei)
                .build();


        //setup dataView đơn vị
        DepartViewEntity DepartViewEntity = new DepartViewEntity
                .Builder(TthtHnLoginActivity.this, R.layout.layout_department, R.id.spin_dvi, R.id.ibtn_download_dvi, R.id.pbar_download_dvi)
                .build();


        //setup module đơn vị
        DepartUpdateFragment<TABLE_DVIQLY> departmentModule = new DepartUpdateFragment()
                .setViewEntity(DepartViewEntity)
                .setShowModule(true);


        //setup module login
        loginFragment = LoginFragment.newInstance(null)
                .setmLoginViewEntity(loginViewEntity)
                .setmDepartModule(departmentModule)
                .setmTitleAppName("TREO THÁO HIỆN TRƯỜNG \nHÀ NỘI")
//                    .setmIconLogin(R.mipmap.ic_home, (int) getResources().getDimension(R.dimen._50sdp), (int) getResources().getDimension(R.dimen._50sdp))
                .setmColorBackground(R.color.colorPrimary)
                .setmLoginSharedPref(mPrefManager.getSharePref(LoginSharePref.class));
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //show view login
        transaction.replace(R.id.rl_ac_login, loginFragment).commit();
    }
    //endregion
}
