package es.vinhnb.ttht.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.es.tungnv.views.R;

/**
 * Created by VinhNB on 11/22/2017.
 */

public abstract class TthtHnBaseFragment extends Fragment implements IBaseView{
    public static final int CAMERA_REQUEST_CONGTO = 1111;
    public static final int CAMERA_REQUEST_CONGTO_NIEMPHONG = 1112;

    public static final int CAMERA_REQUEST_ANH_TU = 1113;
    public static final int CAMERA_REQUEST_ANH_NHITHU_TU = 1114;
    public static final int CAMERA_REQUEST_ANH_NIEMPHONG_TU = 1115;
    public static final int CAMERA_REQUEST_ANH_TI = 1116;
    public static final int CAMERA_REQUEST_ANH_NHITHU_TI = 1117;
    public static final int CAMERA_REQUEST_ANH_NIEMPHONG_TI = 1118;



    public static final int MESSAGE_CTO = 1113;


    protected static void showDialog(Context context, String message, final IDialog iDialog) {
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
        tvMessage.setMovementMethod(new ScrollingMovementMethod());
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

    abstract static class IDialog {
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

        abstract void clickOK();

        abstract void clickCancel();
    }
}

