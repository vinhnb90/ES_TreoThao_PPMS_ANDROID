package es.vinhnb.ttht.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import es.vinhnb.ttht.common.Common;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;

public class TthtHnMainActivity extends TthtHnBaseActivity {

    private LoginFragment.LoginData mLoginData;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

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
        super.setCoordinatorLayout((CoordinatorLayout) findViewById(R.id.cl_ac_main));


        //init View
        initNavigationDrawer();

    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //getBundle
        mLoginData = (LoginFragment.LoginData) getIntent().getParcelableExtra(TthtHnLoginActivity.BUNDLE_LOGIN);


    }


    public void initNavigationDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home1:
                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trash:
                        Toast.makeText(getApplicationContext(),"Trash",Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("raj.amalw@learn2crack.com");
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
