package com.esolutions.esloginlib.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esolutions.esloginlib.R;
import com.esolutions.esloginlib.common.Common;

public class LoginFragment extends Fragment {
    private Bundle bundle;
    private FragmentTransaction mTransaction;
    private LoginInteface mLoginInteface;
    private ICryptPass mICryptPass;
    private DepartUpdateFragment mDepartModule;
    private LoginViewEntity mLoginViewEntity;
    private String mTitleAppName;
    private Drawable mBackgroundLogin;
    private int mColorBackground;
    private int mIconLogin, mSizeWithIconLogin, mSizeHeightIconLogin;
    private TextView mTvTitleAppName;
    private LinearLayout mLLModuleLogin;
    private ImageView mIvIconLogin;
    private EditText mEtURL;
    private EditText mEtUser;
    private EditText mEtPass;
    private Button mBtnLogin;
    private CoordinatorLayout mCoordinatorLayout;
    private AppCompatCheckBox mCbSaveInfo;
    private boolean hasModeLoginOffline;
    private Snackbar snackbar;
    private SharedPreferences mLoginSharedPref;
    private String mURL;
    private String mUser;
    private String mPass;
    private int mPosDvi;
    private boolean mIsSaveInfo;
    private TextView mTvVersion;
    private TextView mTvImei;
    private ImageButton mIbtnVisibePass;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment fragment = new LoginFragment();
        if (bundle == null)
            bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteface) {
            mLoginInteface = (LoginInteface) context;
        } else
            throw new ClassCastException("Class must be implement LoginInteface!");

        if (mLoginSharedPref == null) {
            throw new NullPointerException("Shared Preference Login must be not null!");
        }

        if (mLoginSharedPref.getAll().isEmpty()) {
            throw new NullPointerException("Not has key value in file sharepref initial!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //fill data depart spin
            if (mDepartModule != null) {
                mDepartModule.setmListDepart(mLoginInteface.selectDBDepart());
            }

            if (mLoginSharedPref != null) {
                LoginSharePrefData loginSharePrefData = mLoginInteface.getDataLoginSharedPref();

                mURL = loginSharePrefData.getmURL();
                mPosDvi = loginSharePrefData.getmPosDvi();
                mUser = loginSharePrefData.getmUser();
                mIsSaveInfo = loginSharePrefData.ismIsSaveInfo();

                //decrypt pass if has mode encrypt pass
                mPass = loginSharePrefData.getmPass();
                if (mICryptPass != null) {
                    mPass = mICryptPass.decyptPass(mPass);
                }

                mEtURL.setText(mURL);
                if (mDepartModule != null)
                    mDepartModule.getViewEntity().getSpDvi().setSelection(mPosDvi);

                mEtUser.setText(mUser);
                mEtPass.setText(mPass);
                mCbSaveInfo.setChecked(mIsSaveInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(e.getMessage(), null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = null;
        try {
            if (mLoginViewEntity != null)
                viewRoot = mLoginViewEntity.getViewLayout();
            else
                viewRoot = inflater.inflate(R.layout.fragment_login, container, false);

            initView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(e.getMessage(), null);
        }
        return viewRoot;
    }

    private void setAction(final Bundle savedInstanceState) throws Exception {
        if (mDepartModule != null && mDepartModule.isShowModule()) {
            mDepartModule.getViewEntity().getIbtnDownloadDvi().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        mDepartModule.setmListDepart(mLoginInteface.callServerDepart());
                        mLoginInteface.saveDBDepart(mDepartModule.getmListDepart());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackbar(e.getMessage(), null);
                    }
                }
            });
        }
        mIbtnVisibePass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mEtPass.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        mEtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //declare var
                    String depart = "";
                    mUser = mEtUser.getText().toString().trim();
                    mPass = mEtPass.getText().toString().trim();

                    //disable all view in login
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        mDepartModule.getViewEntity().getSpDvi().setEnabled(false);
                        mDepartModule.getViewEntity().getIbtnDownloadDvi().setEnabled(false);
                    }
                    mEtUser.setEnabled(false);
                    mEtPass.setEnabled(false);
                    mCbSaveInfo.setEnabled(false);

                    //check connect internet
                    if (!hasModeLoginOffline) {
                        if (!Common.isNetworkConnected(getContext()))
                            throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }
                    //check validate
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        depart = mDepartModule.getmListDepart().get(mDepartModule.getViewEntity().getSpDvi().getSelectedItemPosition()).toString();
                        if (TextUtils.isEmpty(depart))
                            throw new Exception("Vui lòng chọn đơn vị");
                    }

                    if (TextUtils.isEmpty(mUser)) {
                        mEtUser.setEnabled(true);
                        mEtUser.requestFocus();
                        throw new Exception("Không để trống tài khoản");
                    }

                    if (TextUtils.isEmpty(mPass)) {
                        mEtPass.setEnabled(true);
                        mEtPass.requestFocus();
                        throw new Exception("Không để trống mật khẩu");
                    }

                    //call server
                    final boolean resultCheckServerLogin = mLoginInteface.checkServerLogin(depart, mUser, mPass);

                    //check login offline
                    boolean resultCheckSessionLogin = false;
                    if (hasModeLoginOffline) {
                        resultCheckSessionLogin = mLoginInteface.checkSessionLogin(hasModeLoginOffline, depart, mUser, mPass);
                    }


                    //save share pref
                    if (mCbSaveInfo.isChecked()) {
                        mURL = (mCbSaveInfo.isChecked()) ? mEtURL.getText().toString() : "";
                        if (mDepartModule != null)
                            mPosDvi = (mCbSaveInfo.isChecked()) ? mDepartModule.getViewEntity().getSpDvi().getSelectedItemPosition() : 0;
                        mUser = (mCbSaveInfo.isChecked()) ? mEtUser.getText().toString() : "";
                        mPass = (mCbSaveInfo.isChecked()) ? mEtPass.getText().toString() : "";
                        mIsSaveInfo = mCbSaveInfo.isChecked();

                        //encrypt pass if return empty when not has requirement encrypt pass
                        if (mICryptPass != null)
                            mPass = mICryptPass.encryptPass(mPass);

                        LoginSharePrefData loginSharePrefData = new LoginSharePrefData
                                .Builder(mURL, mUser, mPass)
                                .setmPosDvi(mPosDvi)
                                .setmIsSaveInfo(mIsSaveInfo)
                                .build();

                        mLoginInteface.saveDataSharePref(loginSharePrefData);
                    }


                    //Hiển thị message thông báo nếu đang chế độ ofline trong 3s sau đó sẽ gọi main
                    if (resultCheckServerLogin == false && resultCheckSessionLogin == true) {
                        final boolean finalResultCheckSessionLogin = resultCheckSessionLogin;
                        SnackbarIteractions snackbarIteractions = new SnackbarIteractions() {
                            @Override
                            public void doIfPressOK() {
                                //open main
                                if (resultCheckServerLogin || finalResultCheckSessionLogin)
                                    mLoginInteface.openMainView();
                            }
                        };


                        showSnackbar("Đăng nhập chế độ offline!", snackbarIteractions);
                        return;
                    }


                    //open main
                    if (resultCheckServerLogin || resultCheckSessionLogin) {
                        mLoginInteface.openMainView();
                        return;
                    }
                } catch (Exception e) {
                    showSnackbar(e.getMessage(), null);
                } finally {
                    //enable again all view in login
                    if (mDepartModule != null) {
                        mEtUser.setEnabled(true);
                        mEtPass.setEnabled(true);
                        mCbSaveInfo.setEnabled(true);

                        try {
                            mDepartModule.getViewEntity().getSpDvi().setEnabled(true);
                            mDepartModule.getViewEntity().getIbtnDownloadDvi().setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void initView(final View viewRoot) throws Exception {
        mTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();


        if (mLoginViewEntity == null) {
            mTvTitleAppName = (TextView) viewRoot.findViewById(R.id.tv_title_appname);
            mLLModuleLogin = (LinearLayout) viewRoot.findViewById(R.id.ll_include_login);
            mIvIconLogin = (ImageView) viewRoot.findViewById(R.id.iv_image_app);
            mCoordinatorLayout = (CoordinatorLayout) viewRoot.findViewById(R.id.cl_frag_login);

            mEtUser = (EditText) viewRoot.findViewById(R.id.et_user);
            mEtPass = (EditText) viewRoot.findViewById(R.id.et_pass);
            mEtURL = (EditText) viewRoot.findViewById(R.id.et_url);
            mBtnLogin = (Button) viewRoot.findViewById(R.id.btn_login);
            mCbSaveInfo = (AppCompatCheckBox) viewRoot.findViewById(R.id.cb_save_info);

            mTvVersion = (TextView) viewRoot.findViewById(R.id.tv_version);
            mTvImei = (TextView) viewRoot.findViewById(R.id.tv_imei);
            mIbtnVisibePass = (ImageButton) viewRoot.findViewById(R.id.ibtn_visible_pass);

        } else {
            mTvTitleAppName = mLoginViewEntity.getTvTitleAppName();
            mLLModuleLogin = mLoginViewEntity.getLlModuleLogin();
            mIvIconLogin = mLoginViewEntity.getIvIconLogin();
            mCoordinatorLayout = mLoginViewEntity.getCoordinatorLayout();
            mEtUser = mLoginViewEntity.getEtUser();
            mEtPass = mLoginViewEntity.getEtPass();
            mEtURL = mLoginViewEntity.getEtURL();
            mBtnLogin = mLoginViewEntity.getBtnLogin();
            mCbSaveInfo = mLoginViewEntity.getCbSave();

            mTvVersion = mLoginViewEntity.getTvVersion();
            mTvImei = mLoginViewEntity.getTvImei();
            mIbtnVisibePass = mLoginViewEntity.getIbtnVisiblePass();
        }

        if (mBackgroundLogin != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewRoot.setBackground(mBackgroundLogin);
            } else {
                viewRoot.setBackgroundDrawable(mBackgroundLogin);
            }
        }

        viewRoot.setBackgroundColor(getResources().getColor(mColorBackground));

        mTvTitleAppName.setText(mTitleAppName);
        if (mIvIconLogin != null) {
            mIvIconLogin.setImageResource(mIconLogin);
            mIvIconLogin.getLayoutParams().width = mSizeWithIconLogin;
            mIvIconLogin.getLayoutParams().height = mSizeHeightIconLogin;
        }

        if (mDepartModule != null && mDepartModule.isShowModule()) {
            if (mLLModuleLogin != null)
                mTransaction.replace(mLLModuleLogin.getId(), mDepartModule).commit();
        }

        mTvImei.setText("Imei - " + Common.getImei(mLoginViewEntity.getContext()));
        mTvVersion.setText("Phiên bản - " + Common.getVersion(mLoginViewEntity.getContext()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLoginInteface = null;
    }

    public ModuleFragment getmDepartModule() {
        return mDepartModule;
    }

    public LoginFragment setmDepartModule(DepartUpdateFragment mDepartModule) {
        this.mDepartModule = mDepartModule;
        return this;
    }

    public String getmTitleAppName() {
        return mTitleAppName;
    }

    public LoginFragment setmTitleAppName(String mTitleAppName) {
        this.mTitleAppName = mTitleAppName;
        return this;
    }

    public Drawable getmBackgroundLogin() {
        return mBackgroundLogin;
    }

    public LoginFragment setmBackgroundLogin(Drawable mBackgroundLogin) {
        this.mBackgroundLogin = mBackgroundLogin;
        return this;
    }

    public int getmIconLogin() {
        return mIconLogin;
    }

    public LoginFragment setmIconLogin(int mIconLogin, int sizeWith, int sizeHeight) {
        this.mIconLogin = mIconLogin;
        this.mSizeWithIconLogin = sizeWith;
        this.mSizeHeightIconLogin = sizeHeight;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public LoginFragment setHasModeLoginOffline(boolean hasModeLoginOffline) {
        this.hasModeLoginOffline = hasModeLoginOffline;
        return this;
    }

    public boolean isHasModeLoginOffline() {
        return hasModeLoginOffline;
    }

    public int getmColorBackground() {
        return mColorBackground;
    }

    public LoginFragment setmColorBackground(int mColorBackground) {
        this.mColorBackground = mColorBackground;
        return this;
    }

    private void showSnackbar(String message, @Nullable SnackbarIteractions snackbarIteractions) {
        snackbar = Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(10);
        snackbar.show();
    }

    public SharedPreferences getmLoginSharedPref() {
        return mLoginSharedPref;
    }

    public LoginFragment setmLoginSharedPref(SharedPreferences mLoginSharedPref) {
        this.mLoginSharedPref = mLoginSharedPref;
        return this;
    }

    public LoginViewEntity getmLoginViewEntity() {
        return mLoginViewEntity;
    }

    public LoginFragment setmLoginViewEntity(LoginViewEntity mLoginViewEntity) {
        this.mLoginViewEntity = mLoginViewEntity;
        return this;
    }

    public LoginFragment setmICryptPass(ICryptPass mICryptPass) {
        this.mICryptPass = mICryptPass;
        return this;
    }


    public interface ICryptPass {
        String encryptPass(String mPass);

        String decyptPass(String mPass);
    }

    public static class LoginSharePrefData {
        private final String mURL;
        private int mPosDvi;
        private final String mUser;
        private final String mPass;
        private boolean mIsSaveInfo;

        private LoginSharePrefData(String mURL, int mPosDvi, String mUser, String mPass, boolean mIsSaveInfo) {
            this.mURL = mURL;
            this.mPosDvi = mPosDvi;
            this.mUser = mUser;
            this.mPass = mPass;
            this.mIsSaveInfo = mIsSaveInfo;
        }

        public String getmURL() {
            return mURL;
        }

        public int getmPosDvi() {
            return mPosDvi;
        }

        public String getmUser() {
            return mUser;
        }

        public String getmPass() {
            return mPass;
        }

        public boolean ismIsSaveInfo() {
            return mIsSaveInfo;
        }

        public static class Builder {
            private final String mURL;
            private int mPosDvi;
            private final String mUser;
            private final String mPass;
            private boolean mIsSaveInfo;

            public Builder(String mURL, String mUser, String mPass) {
                this.mURL = mURL;
                this.mUser = mUser;
                this.mPass = mPass;
            }

            public Builder setmPosDvi(int mPosDvi) {
                this.mPosDvi = mPosDvi;
                return this;
            }

            public Builder setmIsSaveInfo(boolean mIsSaveInfo) {
                this.mIsSaveInfo = mIsSaveInfo;
                return this;
            }

            public LoginSharePrefData build() {
                return new LoginSharePrefData(mURL, mPosDvi, mUser, mPass, mIsSaveInfo);
            }
        }

    }

    public interface SnackbarIteractions {
        void doIfPressOK();
    }
}
