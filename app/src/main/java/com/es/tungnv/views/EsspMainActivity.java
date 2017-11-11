package com.es.tungnv.views;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.es.tungnv.entity.EsspConfigInfo;
import com.es.tungnv.fragments.EsspCauHinhFragment;
import com.es.tungnv.fragments.EsspDanhMucFragment;
import com.es.tungnv.fragments.EsspMainFragment;
import com.es.tungnv.fragments.EsspNghiemThuFragment;
import com.es.tungnv.navigator.NavDrawerItem;
import com.es.tungnv.navigator.NavDrawerListAdapter;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;

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
public class EsspMainActivity extends AppCompatActivity {
    @SuppressWarnings("ResourceType")
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
    private LinkedHashMap<String, String> hmConfig;
    //endregion

    //region Khởi tạo
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.essp_activity_main);
            mTitle = mDrawerTitle = "Khảo sát cấp điện";
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            CheckFileConfigExist();
            getDataConfig();
            
            navMenuTitles = getResources().getStringArray(R.array.essp_nav_drawer_items);
            navMenuIcons = getResources().obtainTypedArray(R.array.essp_nav_drawer_icons);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.essp_activity_main_dl_main);
            mDrawerList = (ListView) findViewById(R.id.essp_activity_main_lv_slidermenu);
            mDrawerList.getLayoutParams().width = 4*width/5;
            navDrawerItems = new ArrayList<NavDrawerItem>();

            for (int i = 0; i < navMenuIcons.length(); i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }
//            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
//            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
//            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
//            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

            navMenuIcons.recycle();

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            mDrawerList.setAdapter(adapter);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.essp_actionbar_unselected));

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
    protected void onResume() {
        super.onResume();
        EsspCommon.LoadFolder(EsspMainActivity.this);
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
                fragment = new EsspMainFragment();
                EsspCommon.pos_activity = 0;
                break;
            case 1:
                fragment = new EsspNghiemThuFragment();
                EsspCommon.pos_activity = 1;
                break;
            case 2:
                fragment = new EsspDanhMucFragment();
                EsspCommon.pos_activity = 1;
                break;
            case 3:
                fragment = new EsspCauHinhFragment();
                EsspCommon.pos_activity = 1;
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.essp_activity_main_fl_container, fragment).commit();
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
        switch (EsspCommon.pos_activity) {
            case 0:
                this.finish();
                break;
            case 1:
                Fragment fragment = null;
                fragment = new EsspMainFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    setTitle(navMenuTitles[0]);
                    fragmentManager.beginTransaction().replace(R.id.essp_activity_main_fl_container, fragment).commit();
                }
                EsspCommon.pos_activity = 0;
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
            File programDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            File programDbBackupDirectory = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_SAMPLE_PATH);
            if (!programDbBackupDirectory.exists()) {
                programDbBackupDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + EsspConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                EsspCommon.cfgInfo = EsspCommon.GetFileConfig();
                if(EsspCommon.cfgInfo == null){
                    EsspCommon.cfgInfo = new EsspConfigInfo();
                }
            } else {
                EsspCommon.cfgInfo = new EsspConfigInfo();
                EsspCommon.CreateFileConfig(EsspCommon.cfgInfo, cfgFile, "");
            }
        } catch (Exception ex) {
            Toast.makeText(EsspMainActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + EsspConstantVariables.PROGRAM_PATH;
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
                        for (int j = 0; j < EsspConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(EsspConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(EsspConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Table1")) {
                            ArrListConfig.add(hmConfig);
                            EsspCommon.setIP_SERVER_1(hmConfig.get("IP_SV_1"));
                            EsspCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(EsspMainActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }
    //endregion
    
}
