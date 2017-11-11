package com.es.tungnv.views;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.es.tungnv.fragments.InvoiceCauHinhFragment;
import com.es.tungnv.fragments.InvoiceChamNoFragment;
import com.es.tungnv.fragments.InvoiceTongHopFragment;
import com.es.tungnv.navigator.NavDrawerItem;
import com.es.tungnv.navigator.NavDrawerListAdapter;
import com.es.tungnv.utils.InvoiceCommon;

import java.util.ArrayList;

/**
 * Created by TUNGNV on 2/24/2016.
 */
@SuppressWarnings("ResourceType")
public class InvoiceMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public static int width;
    public static int height;

    private Toolbar toolbar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.invoice_activity_main);
            mTitle = mDrawerTitle = "Chấm nợ";
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            navMenuTitles = getResources().getStringArray(R.array.invoice_nav_drawer_items);
            navMenuIcons = getResources().obtainTypedArray(R.array.invoice_nav_drawer_icons);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.invoice_activity_main_dl_main);
            mDrawerList = (ListView) findViewById(R.id.invoice_activity_main_lv_slidermenu);
            mDrawerList.getLayoutParams().width = 4*width/5;
            navDrawerItems = new ArrayList<NavDrawerItem>();

            for (int i = 0; i < navMenuIcons.length(); i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }

            navMenuIcons.recycle();

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            mDrawerList.setAdapter(adapter);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.invoice_actionbar_unselected));

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

                @SuppressLint("NewApi") public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(mTitle);
                    invalidateOptionsMenu();
                }

                @SuppressLint("NewApi") public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            displayView(0, null);
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error onCreate: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position, view);
        }
    }

    private void displayView(int position, View view) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new InvoiceChamNoFragment();
                InvoiceCommon.pos_activity = 0;
                break;
            case 1:
                fragment = new InvoiceTongHopFragment();
                InvoiceCommon.pos_activity = 1;
                break;
            case 2:
                fragment = new InvoiceCauHinhFragment();
                InvoiceCommon.pos_activity = 1;
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.invoice_activity_main_fl_container, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Toast.makeText(getApplicationContext(), "Error change fragment", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        switch (InvoiceCommon.pos_activity) {
            case 0:
                this.finish();
                break;
            case 1:
                Fragment fragment = null;
                fragment = new InvoiceChamNoFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    setTitle(navMenuTitles[0]);
                    fragmentManager.beginTransaction().replace(R.id.invoice_activity_main_fl_container, fragment).commit();
                }
                InvoiceCommon.pos_activity = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main, menu);
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error create menu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_about).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

}
