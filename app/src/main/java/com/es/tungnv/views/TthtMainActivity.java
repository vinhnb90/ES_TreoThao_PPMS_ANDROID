package com.es.tungnv.views;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.es.tungnv.entity.TthtConfigInfo;
import com.es.tungnv.fragments.TthtCauHinhFragment;
import com.es.tungnv.fragments.TthtMainFragment;
import com.es.tungnv.navigator.NavDrawerItem;
import com.es.tungnv.navigator.NavDrawerListAdapter;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.utils.TthtConstantVariables;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 2/24/2016.
 */
@SuppressWarnings("ResourceType")
public class TthtMainActivity extends AppCompatActivity{

    //region Khai báo biến
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

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    @SuppressWarnings("ResourceType")
    private LinkedHashMap<String, String> hmConfig;
    //endregion
    public static final int REQUEST_CODE_TO_DETAIL_ACTIVITY = 100;
    //region Khởi tạo
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.ttht_activity_main);
            mTitle = mDrawerTitle = "GSHT";
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();

//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            CheckFileConfigExist();
            getDataConfig();
            
            navMenuTitles = getResources().getStringArray(R.array.gsht_nav_drawer_items);
            navMenuIcons = getResources().obtainTypedArray(R.array.gsht_nav_drawer_icons);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.gsht_activity_main_dl_main);
            mDrawerList = (ListView) findViewById(R.id.gsht_activity_main_lv_slidermenu);
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
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gsht_actionbar_unselected));
            getSupportActionBar().hide();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO listen response from detailActivity
        if(data!=null && requestCode == REQUEST_CODE_TO_DETAIL_ACTIVITY && resultCode == RESULT_OK){
            String MA_BDONG = data.getExtras().getString("MA_BDONG");
            //TODO get Fragment is visible
            Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.gsht_activity_main_fl_container);
            if(currentFragment!=null && currentFragment.isVisible() && currentFragment instanceof TthtMainFragment){
                ((TthtMainFragment)currentFragment).refreshMainFrag(MA_BDONG);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        TthtCommon.LoadFolder(TthtMainActivity.this);
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
                fragment = new TthtMainFragment();
                TthtCommon.pos_activity = 0;
                break;
            case 1:
                fragment = new TthtCauHinhFragment();
                TthtCommon.pos_activity = 1;
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.gsht_activity_main_fl_container, fragment).addToBackStack("TthtMainFragment").commit();
//            fragment.getView().setFocusableInTouchMode(true);
//            fragment.getView().requestFocus();
//            fragment.getView().setOnKeyListener( new View.OnKeyListener()
//            {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                    if( i == KeyEvent.KEYCODE_BACK )
//                    {
//                        return true;
//                    }
//                    return false;
//                }
//
//            } );
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(navMenuTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Toast.makeText(getApplicationContext(), "Error change fragment", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        switch (TthtCommon.pos_activity) {
            case 0:
                this.finish();
                break;
            case 1:
                Fragment fragment = null;
                fragment = new TthtMainFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    setTitle(navMenuTitles[0]);
                    fragmentManager.beginTransaction().replace(R.id.gsht_activity_main_fl_container, fragment).commit();
                }
                TthtCommon.pos_activity = 0;
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
    //endregion

    //region Tạo và kiểm tra file cấu hình
    private void CheckFileConfigExist(){
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            File programDbBackupDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_BACKUP_PATH);
            if (!programDbBackupDirectory.exists()) {
                programDbBackupDirectory.mkdirs();
            }
            File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PHOTOS_PATH);
            if (!programPhotoDirectory.exists()) {
                programPhotoDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                TthtCommon.cfgInfo = TthtCommon.GetFileConfig();
                if(TthtCommon.cfgInfo == null){
                    TthtCommon.cfgInfo = new TthtConfigInfo();
                }
            } else {
                TthtCommon.cfgInfo = new TthtConfigInfo();
                TthtCommon.CreateFileConfig(TthtCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(TthtMainActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + TthtConstantVariables.PROGRAM_PATH;
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.length() - 4, fileName.length());
                if (fileExtension.toLowerCase().equals(".cfg")) {
                    fileConfig = file.toString();
                }
            }
        }
        return fileConfig;
    }

    public void getDataConfig() throws XmlPullParserException, IOException, SAXException {
        try {
            ArrListConfig = new ArrayList<LinkedHashMap<String, String>>();
            XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
            XmlPullParser pp = pf.newPullParser();
            try {
                String file = getConfig();

                FileInputStream ip = new FileInputStream(file);
                pp.setInput(ip, "UTF-8");
            } catch (Exception e1) {
                return;
            }
            int event = -1;
            String NodeName;
            while (event != XmlPullParser.END_DOCUMENT) {
                event = pp.next();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            hmConfig = new LinkedHashMap<String, String>();
                            break;
                        }
                        for (int j = 0; j < TthtConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(TthtConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(TthtConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            TthtCommon.setIP_SERVER_1(hmConfig.get("IP_SV_1"));
                            TthtCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(TthtMainActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }
    //endregion
    
}
