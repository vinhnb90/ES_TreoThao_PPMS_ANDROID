package com.esolutions.esloginlib.lib;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by VinhNB on 10/26/2017.
 */

public class LoginViewEntity {
    private final Context context;
    private final View viewLayout;
    private final EditText etURL;
    private final EditText etUser;
    private final EditText etPass;
    private final AppCompatCheckBox cbSave;
    private final Button btnLogin;
    private final ProgressBar pbarLogin;
    private final TextView tvTitleAppName;
    private final LinearLayout llModuleLogin;
    private final ImageView ivIconLogin;
    private final CoordinatorLayout coordinatorLayout;
    private final TextView tvVersion;
    private final TextView tvImei;
    private final ImageButton ibtnVisiblePass;

    private LoginViewEntity(Builder builder
    ) throws Exception {
        this.context = builder.context;
        this.viewLayout = LayoutInflater.from(context).inflate(builder.viewLayoutID, null);
        this.etURL = (EditText) viewLayout.findViewById(builder.etURLID);
        this.etUser = (EditText) viewLayout.findViewById(builder.etUserID);
        this.etPass = (EditText) viewLayout.findViewById(builder.etPassID);
        this.cbSave = (AppCompatCheckBox) viewLayout.findViewById(builder.cbSaveID);
        this.btnLogin = (Button) viewLayout.findViewById(builder.btnLoginID);
        this.pbarLogin = (ProgressBar) viewLayout.findViewById(builder.pbarLoginID);
        tvTitleAppName = (TextView) viewLayout.findViewById(builder.tvTittleID);
        llModuleLogin = (LinearLayout) viewLayout.findViewById(builder.llModuleLoginID);
        ivIconLogin = (ImageView) viewLayout.findViewById(builder.ivIconLoginID);
        coordinatorLayout = (CoordinatorLayout) viewLayout.findViewById(builder.coordinatorLayoutID);

        tvVersion = (TextView) viewLayout.findViewById(builder.tvVersion);
        tvImei = (TextView) viewLayout.findViewById(builder.tvImei);
        ibtnVisiblePass = (ImageButton) viewLayout.findViewById(builder.ibtnVisiblePass);
    }

    public static class Builder {
        private Context context;
        private int viewLayoutID;
        private int etURLID;
        private int etUserID;
        private int etPassID;
        private int cbSaveID;
        private int btnLoginID;
        private int pbarLoginID;
        private int tvTittleID;
        private int llModuleLoginID;
        private int ivIconLoginID;
        private int coordinatorLayoutID;
        private int tvVersion;
        private int tvImei;
        private int ibtnVisiblePass;

        public Builder(Context context, int viewLayoutID, int etURLID, int etUserID, int etPassID, int cbSaveID, int btnLoginID, int pbarLoginID, int tvTittleID, int coordinatorLayoutID, int ibtnVisiblePass) {
            this.context = context;
            this.viewLayoutID = viewLayoutID;
            this.etURLID = etURLID;
            this.etUserID = etUserID;
            this.etPassID = etPassID;
            this.cbSaveID = cbSaveID;
            this.btnLoginID = btnLoginID;
            this.pbarLoginID = pbarLoginID;
            this.tvTittleID = tvTittleID;
            this.coordinatorLayoutID = coordinatorLayoutID;
            this.ibtnVisiblePass = ibtnVisiblePass;
        }

        public Builder setLlModuleLoginID(int llModuleLoginID) {
            this.llModuleLoginID = llModuleLoginID;
            return this;
        }

        public Builder setIvIconLoginID(int ivIconLoginID) {
            this.ivIconLoginID = ivIconLoginID;
            return this;
        }

        public Builder setTvVersion(int tvVersion) {
            this.tvVersion = tvVersion;
            return this;
        }

        public Builder setTvImei(int tvImei) {
            this.tvImei = tvImei;
            return this;
        }

        public LoginViewEntity build() throws Exception {
            return new LoginViewEntity(this);
        }
    }

    public Context getContext() {
        return context;
    }

    public View getViewLayout() {
        return viewLayout;
    }

    public EditText getEtURL() {
        return etURL;
    }

    public EditText getEtUser() {
        return etUser;
    }

    public EditText getEtPass() {
        return etPass;
    }

    public AppCompatCheckBox getCbSave() {
        return cbSave;
    }

    public Button getBtnLogin() {
        return btnLogin;
    }

    public ProgressBar getPbarLogin() {
        return pbarLogin;
    }

    public TextView getTvTitleAppName() {
        return tvTitleAppName;
    }

    public LinearLayout getLlModuleLogin() {
        return llModuleLogin;
    }

    public ImageView getIvIconLogin() {
        return ivIconLogin;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public TextView getTvVersion() {
        return tvVersion;
    }

    public TextView getTvImei() {
        return tvImei;
    }

    public ImageButton getIbtnVisiblePass() {
        return ibtnVisiblePass;
    }
}
