package com.esolutions.esloginlib.common;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.esolutions.esloginlib.R;

/**
 * Created by VinhNB on 10/30/2017.
 */

public class Common {
    public static boolean isNetworkConnected(Context context) throws Exception {
        if (context == null)
            return false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }


    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    public static String getVersion(Context context) throws Exception {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    public static void showDialog(Context context, String message, final IDialog iDialog) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tththn_message);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        final TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title_dialog);
        final Button btHuy = (Button) dialog.findViewById(R.id.btn_huy);
        final Button btContinued = (Button) dialog.findViewById(R.id.btn_tieptuc);
        final View divider = (View) dialog.findViewById(R.id.v_mid_dialog);

        if(!TextUtils.isEmpty(iDialog.textBtnOK))
        {
            btContinued.setText(iDialog.textBtnOK);
        }

        if(!TextUtils.isEmpty(iDialog.textBtnCancel))
        {
            btHuy.setText(iDialog.textBtnCancel);
        }else {
            btHuy.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(iDialog.title))
        {
            tvTitle.setText(iDialog.title);
        }

        tvMessage.setText(message);
        btContinued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDialog.clickOK();
                dialog.dismiss();
            }
        });


        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDialog.clickCancel();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

     public abstract static class IDialog {
        String title;
        String textBtnCancel;
        String textBtnOK;

        public IDialog setTitle(String title) {
            this.title = title;
            return this;
        }

        public IDialog setTextBtnCancel(String textBtnCancel) {
            this.textBtnCancel = textBtnCancel;
            return this;
        }

        public IDialog setTextBtnOK(String textBtnOK) {
            this.textBtnOK = textBtnOK;
            return this;
        }

        public abstract void clickOK();

        public abstract void clickCancel();
    }
}
