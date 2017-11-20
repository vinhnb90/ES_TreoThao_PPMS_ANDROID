package es.vinhnb.ttht.view;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
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
import es.vinhnb.ttht.entity.api.D_DVIQLYModel;
import es.vinhnb.ttht.entity.api.UserMtb;
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
import es.vinhnb.ttht.entity.sqlite.TTHT_HN_DB_CONFIG;
import es.vinhnb.ttht.server.TthtHnApi;
import es.vinhnb.ttht.server.TthtHnApiInterface;
import esolutions.com.esdatabaselib.baseSharedPref.SharePrefManager;
import esolutions.com.esdatabaselib.baseSqlite.SqlDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;
import retrofit2.Call;
import retrofit2.Response;


public class TthtHnLoginActivity extends TthtHnBaseActivity implements LoginInteface<TABLE_DVIQLY> {
    public static final String BUNDLE_LOGIN = "BUNDLE_LOGIN";
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private List<TABLE_DVIQLY> listDepart = new ArrayList<>();
    private SharePrefManager mPrefManager;
    private LoginFragment loginFragment;
    private LoginSharePref dataLoginSharePref;
    private SqlDAO mSqlDAO;
    private TthtHnApiInterface apiInterface;

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
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //lấy dữ liệu và fill Data dvi
            listDepart = mSqlDAO.selectAllLazy(TABLE_DVIQLY.class, null);
            ((DepartUpdateFragment) loginFragment.getmDepartModule()).setmListDepart(listDepart);


            //fill listDepart shared pref
            fillDataSharePref();
        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
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
            throw new Exception(Common.MESSAGE.ex01.getContent() + " - " + e.getMessage());
        }
    }

    //region LoginInteface
    @Override
    public void openMainView(LoginFragment.LoginData mLoginData) {
        Bundle bundle = new Bundle();
        LoginFragment.LoginData loginData = loginFragment.getmLoginData();
        bundle.putParcelable(BUNDLE_LOGIN, loginData);
        startActivity(new Intent(TthtHnLoginActivity.this, TthtHnMainActivity.class).putExtras(bundle));
    }

    @Override
    public List<TABLE_DVIQLY> callServerDepart() throws Exception {
        //cài đặt đường dẫn máy chủ
        //create api server
        Common.setURLServer(loginFragment.getmLoginViewEntity().getEtURL().getText().toString());
        apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);
        List<TABLE_DVIQLY> dataMTB = new ArrayList<>();
        List<D_DVIQLYModel> dataServer = new ArrayList<>();
        ;

        //call server
        Call<List<D_DVIQLYModel>> dviqlyModelCall = apiInterface.Get_d_dviqly();
        Response<List<D_DVIQLYModel>> dviResponse = dviqlyModelCall.execute();


        //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
        int statusCode = dviResponse.code();
        if (dviResponse.isSuccessful()) {
            if (statusCode == 200) {
                dataServer = dviResponse.body();


                //convert list data server to data mtb
                for (D_DVIQLYModel dviSever : dataServer) {
                    TABLE_DVIQLY dviMTB = new TABLE_DVIQLY(0, dviSever.MA_DVIQLY, dviSever.TEN_DVIQLY);
                    dataMTB.add(dviMTB);
                }
            } else {
                showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + dviResponse.errorBody().string(), null);
            }
        }


        return dataMTB;
    }

    @Override
    public boolean checkServerLogin(LoginFragment.LoginData data) throws Exception {
        //cài đặt đường dẫn máy chủ
        //create api server
        //get list depart
        Common.setURLServer(loginFragment.getmLoginViewEntity().getEtURL().getText().toString());
        apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);
        listDepart = ((DepartUpdateFragment) loginFragment.getmDepartModule()).getmListDepart();

        //gọi server với imei
        String imei = Common.getImei(this);
        Call<UserMtb> Get_LoginMTB = apiInterface
                .Get_LoginMTB(
                        listDepart.get(data.getmPosDvi()).getMA_DVIQLY(),
                        data.getmUser(),
                        data.getmPass(),
                        imei
                );


        //gọi bất đồng bộ
        //lấy dữ liệu
        UserMtb userMtb = null;
        Response<UserMtb> userMtbResponse = Get_LoginMTB.execute();


        //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
        int statusCode = userMtbResponse.code();
        if (userMtbResponse.isSuccessful()) {
            if (statusCode == 200) {
                userMtb = userMtbResponse.body();
                return true;
            } else {
                showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + userMtbResponse.errorBody().string(), null);
                return false;
            }
        } else
            return false;
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
    public void saveDataSharePref(LoginFragment.LoginData loginData) throws Exception {
        //convert data save of login fragment to data save of Login activity
        dataLoginSharePref = new LoginSharePref(loginData.getmURL(), loginData.getmPosDvi(), loginData.getmMaDvi(), loginData.getmUser(), loginData.getmPass(), loginData.ismIsSaveInfo());
        mPrefManager.writeDataSharePref(LoginSharePref.class, dataLoginSharePref);
    }

    @Override
    public LoginFragment.LoginData getDataLoginSharedPref() throws Exception {
        //get data shared pref now
        dataLoginSharePref = (LoginSharePref) mPrefManager.getSharePrefObject(LoginSharePref.class);


        //convert data save of login activity to data save of Login fragment
        LoginFragment.LoginData loginData = new LoginFragment.LoginData
                .Builder(dataLoginSharePref.ip, dataLoginSharePref.user, dataLoginSharePref.pass)
                .setmDvi(dataLoginSharePref.posSpinDvi, dataLoginSharePref.maDvi)
                .setmIsSaveInfo(dataLoginSharePref.isCheckSave)
                .build();


        return loginData;
    }
    //endregion

    //region TthtHnBaseActivity
    @Override
    void initDataAndView() throws Exception {
        //set view
        setContentView(R.layout.activity_login);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_ac_login);


        //set view full screen
        super.setupFullScreen();
        super.setCoordinatorLayout(coordinatorLayout);

        //create database
        SqlHelper.setupDB(
                TthtHnLoginActivity.this,
                TTHT_HN_DB_CONFIG.class,
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


        //create shared pref
        ArrayList<Class<?>> setClassSharedPrefConfig = new ArrayList<Class<?>>();
        setClassSharedPrefConfig.add(LoginSharePref.class);
        mPrefManager = SharePrefManager.getInstance(this, setClassSharedPrefConfig);


        //dump data test
//        listDepart = new ArrayList<>();
//        listDepart.add(new TABLE_DVIQLY(1, "PD", "Tổng công ty Điện Lực Hà Nội"));
//        listDepart.add(new TABLE_DVIQLY(2, "PD0100", "Điện lực Hoàn Kiếm"));


        LoginFragment.ILoginOffline loginOfflineModeConfig = new LoginFragment.ILoginOffline() {

            @Override
            public boolean checkSessionLogin(LoginFragment.LoginData loginData) throws Exception {
                //create data check
                TABLE_SESSION dataCheck = new TABLE_SESSION();
                dataCheck.setMA_DVIQLY(listDepart.get(loginData.getmPosDvi()).getMA_DVIQLY());
                dataCheck.setUSERNAME(loginData.getmUser());
                dataCheck.setPASSWORD(loginData.getmPass());


                //check row in TABLE_SESSION
                return mSqlDAO.checkRows(TABLE_SESSION.class, dataCheck);
            }

            @Override
            public void saveSessionDatabaseLogin(LoginFragment.LoginData dataLoginSession) throws Exception {
                TABLE_SESSION dataCheck = new TABLE_SESSION();
                dataCheck.setMA_DVIQLY(listDepart.get(dataLoginSession.getmPosDvi()).getMA_DVIQLY());
                dataCheck.setUSERNAME(dataLoginSession.getmUser());
                dataCheck.setPASSWORD(dataLoginSession.getmPass());
                dataCheck.setDATE_LOGIN(Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyyHHmmss));


                //save data
                mSqlDAO.insert(dataCheck);
            }
        };

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
                .setmILoginOffline(loginOfflineModeConfig)
                .setmTitleAppName("TREO THÁO HIỆN TRƯỜNG \nHÀ NỘI")
//                    .setmIconLogin(R.mipmap.ic_home, (int) getResources().getDimension(R.dimen._50sdp), (int) getResources().getDimension(R.dimen._50sdp))
                .setmColorBackground(R.color.colorPrimary)
                .setmLoginSharedPref(mPrefManager.getSharePref(LoginSharePref.class));
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //show view login
        transaction.replace(R.id.cl_ac_login, loginFragment).commit();
    }
    //endregion
}
