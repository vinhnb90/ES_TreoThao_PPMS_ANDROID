package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;

import es.vinhnb.ttht.adapter.NaviMenuAdapter;
import es.vinhnb.ttht.common.Common;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;

public class TthtHnMainActivity extends TthtHnBaseActivity implements NaviMenuAdapter.INaviMenuAdapter {

    private LoginFragment.LoginData mLoginData;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private String mMaNVien;
    private GridView mGridView;
    private ImageButton mIbtnLogout;

    private ArrayList<NaviMenuAdapter.NaviMenu> naviMenuList = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;

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


        //init nav menu
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(R.drawable.ic_tththn_bien_ban, "Biên bản treo tháo").setClicked(true));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(R.drawable.ic_tththn_tram, "Trạm"));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(R.drawable.ic_tththn_cto_treo, "Công tơ treo"));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(R.drawable.ic_tththn_cto_thao, "Công tơ tháo"));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(R.drawable.ic_tththn_chungloai, "Chủng loại công tơ"));


        //init View
        initNavigationDrawer();
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //getBundle
        mLoginData = (LoginFragment.LoginData) getIntent().getParcelableExtra(TthtHnLoginActivity.BUNDLE_LOGIN);
        mMaNVien = getIntent().getStringExtra(TthtHnLoginActivity.MA_NVIEN);


        //menu left
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NaviMenuAdapter adapterNavMenu = new NaviMenuAdapter(this, naviMenuList);
        mGridView.setAdapter(adapterNavMenu);
        mGridView.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tththn_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.menu_1:
                    Toast.makeText(this, "menu1", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.menu_2:
                    Toast.makeText(this, "menu1", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
        return super.onOptionsItemSelected(item);
    }


    public void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mIbtnLogout = (ImageButton) findViewById(R.id.ibtn_logout);
        mGridView = (GridView) findViewById(R.id.gv_nav_menu);

        findViewById(R.id.app_bar_tththn).bringToFront();
        setSupportActionBar(mToolbar);
        this.setActionBarTittle(naviMenuList.get(0).text);
    }

    //region NaviMenuAdapter.INaviMenuAdapter
    @Override
    public void doClickNaviMenu(int pos) {
        //set text action bar
        setActionBarTittle(naviMenuList.get(pos).text);


        //refresh again
        naviMenuList.get(pos).setClicked(true);
        ((NaviMenuAdapter) mGridView.getAdapter()).refresh(naviMenuList, pos);
    }

    private void setActionBarTittle(String message) {
        SpannableString s = new SpannableString(message);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setElevation(0);
    }
    //endregion
}
