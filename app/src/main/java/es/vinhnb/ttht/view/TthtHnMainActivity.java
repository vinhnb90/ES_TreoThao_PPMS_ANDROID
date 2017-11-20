package es.vinhnb.ttht.view;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import es.vinhnb.ttht.common.Common;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;

public class TthtHnMainActivity extends TthtHnBaseActivity {

    private LoginFragment.LoginData mLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initDataAndView();


            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }

    }

    @Override
    void initDataAndView() throws Exception {
        setContentView(activity_ttht_hn_main);
        super.setupFullScreen();
        super.setCoordinatorLayout((CoordinatorLayout)findViewById(R.id.cl_ac_main));


        //init View

    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //getBundle
        mLoginData = (LoginFragment.LoginData) getIntent().getParcelableExtra(TthtHnLoginActivity.BUNDLE_LOGIN);




    }
}
