package com.esolutions.esloginlib.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.esolutions.esloginlib.R;
import com.esolutions.esloginlib.common.Common;

import java.util.List;

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
    private LoginData mLoginData;
    private String mDepart;


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
            return;
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
            //fill data mDepart spin
            if (mDepartModule != null) {
                mDepartModule.setmListDepart(mLoginInteface.selectDBDepart());
            }

            if (mLoginSharedPref != null) {
                mLoginData = mLoginInteface.getDataLoginSharedPref();

                mURL = mLoginData.getmURL();
                mPosDvi = mLoginData.getmPosDvi();
                mUser = mLoginData.getmUser();
                mIsSaveInfo = mLoginData.ismIsSaveInfo();

                //decrypt pass if has mode encrypt pass
                mPass = mLoginData.getmPass();
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
            showSnackBar("Lỗi hiển thị ", e.getMessage(), null);
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
            showSnackBar("Lỗi hiển thị", e.getMessage(), null);
        }
        return viewRoot;
    }

    private void setAction(final Bundle savedInstanceState) throws Exception {
        if (mDepartModule != null && mDepartModule.isShowModule()) {
            mDepartModule.getViewEntity().getIbtnDownloadDvi().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show progressbar
                    mDepartModule.getViewEntity().getIbtnDownloadDvi().setVisibility(View.GONE);
                    mDepartModule.getViewEntity().getPbarDownloadDvi().setVisibility(View.VISIBLE);


                    mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //check url
                                mURL = mEtURL.getText().toString();
                                if (TextUtils.isEmpty(mURL))
                                    throw new RuntimeException("Không để trống đường dẫn máy chủ");


                                //call server
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            final List<?> dataMTB = mLoginInteface.callServerDepart();


                                            mLoginViewEntity.getViewLayout().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //set data and save data
                                                    //show mDepart spin
                                                    try {
                                                        mDepartModule.setmListDepart(dataMTB);


                                                        //and save data
                                                        if (!mDepartModule.getmListDepart().isEmpty())
                                                            mLoginInteface.saveDBDepart(mDepartModule.getmListDepart());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                                    } finally {
                                                        //hide progressbar
                                                        mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    mDepartModule.getViewEntity().getIbtnDownloadDvi().setVisibility(View.VISIBLE);
                                                                    mDepartModule.getViewEntity().getPbarDownloadDvi().setVisibility(View.GONE);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        } catch (final Exception e) {
                                            e.printStackTrace();


                                            //hide progressbar
                                            mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        showSnackBar(e.getMessage(), null, null);
                                                        mDepartModule.getViewEntity().getIbtnDownloadDvi().setVisibility(View.VISIBLE);
                                                        mDepartModule.getViewEntity().getPbarDownloadDvi().setVisibility(View.GONE);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                                    }
                                                }
                                            });


                                        }
                                    }
                                }).start();
                            } catch (final Exception e) {
                                e.printStackTrace();
                                mDepartModule.getViewEntity().getViewLayout().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showSnackBar(e.getMessage(), null, null);
                                            mDepartModule.getViewEntity().getIbtnDownloadDvi().setVisibility(View.VISIBLE);
                                            mDepartModule.getViewEntity().getPbarDownloadDvi().setVisibility(View.GONE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            showSnackBar("Lỗi hiển thị", e.getMessage(), null);
                                        }
                                    }
                                });
                            }
                        }
                    });
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
                    //set data
                    mURL = mEtURL.getText().toString();
                    if (mDepartModule != null)
                        mPosDvi = mDepartModule.getViewEntity().getSpDvi().getSelectedItemPosition();
                    mUser = mEtUser.getText().toString();
                    mPass = mEtPass.getText().toString();
                    mIsSaveInfo = mCbSaveInfo.isChecked();


                    //disable all view in login
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        mDepartModule.getViewEntity().getSpDvi().setEnabled(false);
                        mDepartModule.getViewEntity().getIbtnDownloadDvi().setEnabled(false);
                    }
                    mEtUser.setEnabled(false);
                    mEtPass.setEnabled(false);
                    mCbSaveInfo.setEnabled(false);


                    //check connect internet
                    if (mILoginOffline == null) {
                        if (!Common.isNetworkConnected(getContext()))
                            throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //check validate
                    if (mDepartModule != null && mDepartModule.isShowModule()) {
                        if (mDepartModule.getmListDepart().size() != 0)
                            mDepart = mILoginOffline.getCodeDepart(mPosDvi);


                        if (TextUtils.isEmpty(mDepart))
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


                    //set Data
                    LoginData dataNeedCheckLogin = new LoginData.Builder(mURL, mUser, mPass).setmIsSaveInfo(mIsSaveInfo).setmDvi(mPosDvi, mDepart).build();
                    mLoginData = dataNeedCheckLogin;

                    final boolean resultCheckOfflineLogin = mILoginOffline.checkSessionLogin(mLoginData);

                    if (!Common.isNetworkConnected(getContext())) {
                        //check kết quả
                        if (resultCheckOfflineLogin) {
                            //nếu ofline ok
                            Common.IDialog iDialog = new Common.IDialog() {
                                @Override
                                public void clickOK() {
                                    try {
                                        processLogin();
                                    } catch (Exception e) {
                                        showSnackBar("Đăng nhập chế độ offline thất bại!", e.getMessage(), null);
                                    }
                                }

                                @Override
                                public void clickCancel() {

                                }
                            }.setTextBtnOK("Đăng nhập OFFLINE");

                            Common.showDialog(getContext(), "Hiện không có kết nối internet.\nTài khoản có thể đăng nhập chế độ offline.", iDialog);
                        } else
                            showSnackBar("Cần kết nối mạng internet!", null, null);
                        return;
                    } else {
                        //show progress bar
                        mLoginViewEntity.getBtnLogin().setVisibility(View.GONE);
                        mLoginViewEntity.getPbarLogin().setVisibility(View.VISIBLE);


                        //call server
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    final ResultLogin resultCheckServerLogin = mLoginInteface.checkServerLogin(mLoginData);
                                    //nếu server trả về
                                    if (resultCheckServerLogin.isLoginSuccess) {
                                        processLogin();
                                    } else {
                                        //ko login đc server thì xóa row dữ liệu seession đó
                                        mILoginOffline.deleteSessionDatabaseLogin(mLoginData);
                                        showSnackBar(resultCheckServerLogin.message, null, null);
                                    }
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                    getView().post(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (TextUtils.isEmpty(e.getMessage()))
                                                showSnackBar("Đăng nhập thất bại. Xem chi tiết", "Chờ máy chủ quá lâu!", null);
                                            else
                                                showSnackBar("Không kết nối được máy chủ. Kiểm tra lại thông tin kết nối.", e.getMessage(), null);
                                        }
                                    });

                                } finally {
                                    finallyClickLogin();
                                }
                            }
                        }).start();

                    }
                } catch (Exception e) {
                    //show messasge
                    showSnackBar(e.getMessage(), null, null);
                    finallyClickLogin();
                }

            }
        });
    }

    private void finallyClickLogin() {
        getView().post(new Runnable() {
            @Override
            public void run() {
                mEtUser.setEnabled(true);
                mEtPass.setEnabled(true);
                mCbSaveInfo.setEnabled(true);
                if (mDepartModule != null) {
                    mDepartModule.getViewEntity().getSpDvi().setEnabled(true);
                    mDepartModule.getViewEntity().getIbtnDownloadDvi().setEnabled(true);
                }
                //hide progressbar
                mLoginViewEntity.getBtnLogin().setVisibility(View.VISIBLE);
                mLoginViewEntity.getPbarLogin().setVisibility(View.GONE);
            }
        });
    }

    private void processLogin() throws Exception {
        //encrypt pass if return empty when not has requirement encrypt pass
        if (mICryptPass != null)
            mPass = mICryptPass.encryptPass(mPass);
        LoginData loginDataAfterEncryptPass = new LoginData
                .Builder(mURL, mUser, mPass)
                .setmDvi(mPosDvi, mDepart)
                .setmIsSaveInfo(mIsSaveInfo)
                .build();


        //set data
        mLoginData = loginDataAfterEncryptPass;


        //save  data
        if (mCbSaveInfo.isChecked()) {
            mLoginInteface.saveDataSharePref(mLoginData);
        }
        mILoginOffline.saveSessionDatabaseLogin(mLoginData);


        //open main
        mLoginInteface.openMainView(mLoginData);
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

        try {
            viewRoot.setBackgroundColor(getResources().getColor(mColorBackground));
        } catch (Exception e1) {

        }
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

        mTvImei.setText("Imei - " + Common.getImei(getContext()));
        mTvVersion.setText("Phiên bản - " + Common.getVersion(getContext()));
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

    protected void showSnackBar(String message, @Nullable String content, @Nullable final ISnackbarIteractions actionOK) {
        try {
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
            if (content == null)
                etContent.setVisibility(View.GONE);

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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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


    public LoginData getmLoginData() {
        return mLoginData;
    }

    public interface ICryptPass {
        String encryptPass(String mPass);

        String decyptPass(String mPass);
    }

    public static class LoginData implements Parcelable {
        private final String mURL;
        private int mPosDvi;
        private String mMaDvi;
        private final String mUser;
        private final String mPass;
        private boolean mIsSaveInfo;

        private LoginData(Builder builder) {
            this.mURL = builder.mURL;
            this.mPosDvi = builder.mPosDvi;
            this.mMaDvi = builder.mMaDvi;
            this.mUser = builder.mUser;
            this.mPass = builder.mPass;
            this.mIsSaveInfo = builder.mIsSaveInfo;
        }

        protected LoginData(Parcel in) {
            mURL = in.readString();
            mPosDvi = in.readInt();
            mMaDvi = in.readString();
            mUser = in.readString();
            mPass = in.readString();
            mIsSaveInfo = in.readByte() != 0;
        }

        public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
            @Override
            public LoginData createFromParcel(Parcel in) {
                return new LoginData(in);
            }

            @Override
            public LoginData[] newArray(int size) {
                return new LoginData[size];
            }
        };

        public String getmURL() {
            return mURL;
        }

        public String getmMaDvi() {
            return mMaDvi;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mURL);
            parcel.writeInt(mPosDvi);
            parcel.writeString(mMaDvi);
            parcel.writeString(mUser);
            parcel.writeString(mPass);
            parcel.writeByte((byte) (mIsSaveInfo ? 1 : 0));
        }

        public static class Builder {
            private final String mURL;
            private int mPosDvi;
            private String mMaDvi;
            private final String mUser;
            private final String mPass;
            private boolean mIsSaveInfo;

            public Builder(String mURL, String mUser, String mPass) {
                this.mURL = mURL;
                this.mUser = mUser;
                this.mPass = mPass;
            }

            public Builder setmDvi(int mPosDvi, String mMaDvi) {
                this.mPosDvi = mPosDvi;
                this.mMaDvi = mMaDvi;
                return this;
            }

            public Builder setmIsSaveInfo(boolean mIsSaveInfo) {
                this.mIsSaveInfo = mIsSaveInfo;
                return this;
            }

            public LoginData build() {
                return new LoginData(this);
            }
        }

    }

    private interface ISnackbarIteractions {
        void doIfPressOK();
    }

    public static class ResultLogin {
        boolean isLoginSuccess;
        String message;

        public ResultLogin(boolean isLoginSuccess, String message) {
            this.isLoginSuccess = isLoginSuccess;
            this.message = message;
        }
    }


    public interface ILoginOffline {
        boolean checkSessionLogin(LoginData loginData) throws Exception;

        void saveSessionDatabaseLogin(LoginData dataLoginSession) throws Exception;

        void deleteSessionDatabaseLogin(LoginFragment.LoginData dataLoginSession) throws Exception;

        String getCodeDepart(int pos);
    }

}
