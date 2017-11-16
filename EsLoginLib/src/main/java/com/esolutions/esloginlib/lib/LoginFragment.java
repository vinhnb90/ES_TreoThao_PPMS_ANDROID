package com.esolutions.esloginlib.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.esolutions.esloginlib.R;
import com.esolutions.esloginlib.common.Common;

import java.util.List;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {
    private Bundle bundle;
    private FragmentTransaction mTransaction;
    private LoginInteface mLoginInteface;
    private ICryptPass mICryptPass;
    private ILoginOffline mILoginOffline;
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
            try {
                showSnackBar("Lỗi hiển thị ", e.getMessage(), null);
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
            try {
                showSnackBar("Lỗi hiển thị", e.getMessage(), null);
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return viewRoot;
    }

    private void setAction(final Bundle savedInstanceState) throws Exception {
        if (mDepartModule != null && mDepartModule.isShowModule()) {
            mDepartModule.getViewEntity().getIbtnDownloadDvi().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //check url
                                mURL = mEtURL.getText().toString();
                                if (TextUtils.isEmpty(mURL))
                                    throw new RuntimeException("Không để trống đường dẫn máy chủ");


                                //call server
                                final List<?> dataMTB = mLoginInteface.callServerDepart();


                                //set data and save data
                                //show depart spin
                                mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mDepartModule.setmListDepart(dataMTB);

                                            //and save data
                                            if (!mDepartModule.getmListDepart().isEmpty())
                                                mLoginInteface.saveDBDepart(mDepartModule.getmListDepart());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            //hide progressbar
                                            try {
                                                mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            mDepartModule.getViewEntity().getIbtnDownloadDvi().setVisibility(View.VISIBLE);
                                                            mDepartModule.getViewEntity().getPbarDownloadDvi().setVisibility(View.GONE);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            try {
                                                                showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                                            } catch (Exception e1) {
                                                                e1.printStackTrace();
                                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                try {
                                                    showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).start();
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
                    mURL = mEtURL.getText().toString().trim();
                    mUser = mEtUser.getText().toString().trim();
                    mPass = mEtPass.getText().toString().trim();
                    final LoginSharePrefData data = new LoginSharePrefData(mURL, mPosDvi, mUser, mPass, mIsSaveInfo);


                    //disable all view in login
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        mDepartModule.getViewEntity().getSpDvi().setEnabled(false);
                        mDepartModule.getViewEntity().getIbtnDownloadDvi().setEnabled(false);
                    }
                    mEtUser.setEnabled(false);
                    mEtPass.setEnabled(false);
                    mCbSaveInfo.setEnabled(false);


                    //check connect internet
                    if (mILoginOffline != null) {
                        if (!Common.isNetworkConnected(getContext()))
                            throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //check validate
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        if (mDepartModule.getmListDepart().size() != 0)
                            depart = mDepartModule.getmListDepart().get(mDepartModule.getViewEntity().getSpDvi().getSelectedItemPosition()).toString();


                        if (TextUtils.isEmpty(depart))
                            throw new Exception("Vui lòng chọn đơn vị");
                    }

                    if (TextUtils.isEmpty(mURL)) {
                        mEtUser.setEnabled(true);
                        mEtUser.requestFocus();
                        throw new Exception("Không để trống đường dẫn máy chủ");
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
                    final boolean resultCheckServerLogin = mLoginInteface.checkServerLogin(data);


                    //check login offline,
                    // nếu có mode login thì lấy kết quả seesion, ngược lại bỏ qua phần này resultCheckSessionLogin = true;
                    boolean resultCheckSessionLogin = true;
                    if (mILoginOffline != null) {
                        resultCheckSessionLogin = mILoginOffline.checkSessionLogin(data);
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


                    //login offline
                    //Hiển thị message thông báo nếu đang chế độ ofline trong 3s sau đó sẽ gọi main
                    if (resultCheckServerLogin == false) {
                        final boolean finalResultCheckSessionLogin = resultCheckSessionLogin;

                        if (resultCheckSessionLogin == true) {

                            ISnackbarIteractions snackbarIteractions = new ISnackbarIteractions() {
                                @Override
                                public void doIfPressOK() {
                                    try {
                                        //save session
                                        mILoginOffline.saveSessionLogin(data);


                                        //open main
                                        if (resultCheckServerLogin || finalResultCheckSessionLogin)
                                            mLoginInteface.openMainView();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            showSnackBar("Lỗi đăng nhập", e.getMessage(), null);
                                        } catch (Exception e1) {
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            };
                            showSnackBar("Thông báo", "Đăng nhập chế độ offline!", snackbarIteractions);
                        } else {
                            showSnackBar("Thông báo", "Đăng nhập thất bại. Yêu cầu có kết nối mạng!", null);
                        }
                    } else {
                        //open main
                        //login online
                        mLoginInteface.openMainView();
                    }
                } catch (Exception e) {
                    try {
                        showSnackBar("Lỗi đăng nhập", e.getMessage(), null);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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

    public LoginFragment setmILoginOffline(ILoginOffline mILoginOffline) {
        this.mILoginOffline = mILoginOffline;
        return this;
    }

    public ILoginOffline getmILoginOffline() {
        return mILoginOffline;
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

    public int getmColorBackground() {
        return mColorBackground;
    }

    public LoginFragment setmColorBackground(int mColorBackground) {
        this.mColorBackground = mColorBackground;
        return this;
    }

    protected void showSnackBar(String message, @Nullable String content, @Nullable final ISnackbarIteractions actionOK) throws Exception {
        //check
        if (mCoordinatorLayout == null)
            throw new RuntimeException("Be must set view CoordinatorLayout!");


        snackbar = Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });


        // Hide the text and set width full screen
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        ((TextView) layout.findViewById(android.support.design.R.id.snackbar_text)).setVisibility(View.INVISIBLE);
        ((Button) layout.findViewById(android.support.design.R.id.snackbar_action)).setVisibility(View.INVISIBLE);


        // Inflate our custom view
        View snackView = this.getLayoutInflater().inflate(R.layout.snackbar_custom, null);
        TextView tvMessage = (TextView) snackView.findViewById(R.id.tv_snackbar_message);
        Button btnOk = (Button) snackView.findViewById(R.id.btn_snackbar_ok);
        Button btnContent = (Button) snackView.findViewById(R.id.btn_snackbar_content);
        final EditText etContent = (EditText) snackView.findViewById(R.id.et_snackbar_content);


        //set value
        etContent.setVisibility(View.GONE);
        tvMessage.setText(message);
        if (content == null) content = "";
        etContent.setText(content);


        //catch action
        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etContent.getVisibility() == View.GONE)
                    etContent.setVisibility(View.VISIBLE);
                else
                    etContent.setVisibility(View.GONE);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionOK != null)
                    actionOK.doIfPressOK();
                else
                    snackbar.dismiss();
            }
        });


        // Add the view to the Snackbar's layout and show
        layout.addView(snackView, 0);
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

    private interface ISnackbarIteractions {
        void doIfPressOK();
    }


    public abstract static interface ILoginOffline {
        public abstract boolean checkSessionLogin(LoginSharePrefData loginData) throws Exception;

        public abstract void saveSessionLogin(LoginSharePrefData dataLoginSession) throws Exception;
    }

}
