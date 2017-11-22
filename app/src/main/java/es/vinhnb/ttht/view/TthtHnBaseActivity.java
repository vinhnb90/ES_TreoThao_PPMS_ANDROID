package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;

/**
 * Created by VinhNB on 11/11/2017.
 */

public abstract class TthtHnBaseActivity extends AppCompatActivity {
    protected Snackbar snackbar;
    protected CoordinatorLayout coordinatorLayout;
    protected Bundle savedInstanceState;

    protected void setupFullScreen() {
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TthtHnBaseActivity", "setupFullScreen: " + e.getMessage());
        }
    }

    protected void showSnackBar(String message, @Nullable String content, @Nullable final ISnackbarIteractions actionOK) {
        try {

            //check
            if (coordinatorLayout == null)
                throw new RuntimeException("Be must set view CoordinatorLayout!");


            snackbar = Snackbar
                    .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
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
            View snackView = this.getLayoutInflater().inflate(R.layout.tththn_snackbar, null);
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void showSnackBar(String message, @Nullable final ISnackbarIteractions actionOK) {
        try {

            //check
            if (coordinatorLayout == null)
                throw new RuntimeException("Be must set view CoordinatorLayout!");


            snackbar = Snackbar
                    .make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
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
            View snackView = this.getLayoutInflater().inflate(R.layout.tththn_snackbar, null);
            TextView tvMessage = (TextView) snackView.findViewById(R.id.tv_snackbar_message);
            Button btnOk = (Button) snackView.findViewById(R.id.btn_snackbar_ok);
            Button btnContent = (Button) snackView.findViewById(R.id.btn_snackbar_content);
            btnContent.setVisibility(View.GONE);
            final EditText etContent = (EditText) snackView.findViewById(R.id.et_snackbar_content);
            etContent.setVisibility(View.GONE);


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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    abstract void initDataAndView() throws Exception;

    abstract void setAction(Bundle savedInstanceState) throws Exception;

    private interface ISnackbarIteractions {
        void doIfPressOK();
    }


}
