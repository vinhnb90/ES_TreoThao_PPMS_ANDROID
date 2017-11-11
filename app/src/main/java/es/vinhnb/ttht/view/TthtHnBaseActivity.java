package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

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
        getSupportActionBar().hide();
    }

    protected void showSnackBar(String message) throws Exception{
        //check
        if(coordinatorLayout==null)
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


        //full with, max line = 10, show
        (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(10);
        snackbar.show();
    }

    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
    }

    abstract void initDataAndView() throws Exception;

    abstract void setAction(Bundle savedInstanceState) throws Exception;
}
