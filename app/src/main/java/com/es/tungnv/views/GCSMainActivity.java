package com.es.tungnv.views;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.entity.GcsConfigInfo;
import com.es.tungnv.fragments.GcsBaoCaoFragment;
import com.es.tungnv.fragments.GcsCauHinhFragment;
import com.es.tungnv.fragments.GcsDongBoFragment;
import com.es.tungnv.fragments.GcsGhiChiSoFragment;
import com.es.tungnv.fragments.GcsQuanLySoFragment;
import com.es.tungnv.navigator.NavDrawerItem;
import com.es.tungnv.navigator.NavDrawerListAdapter;
import com.es.tungnv.navigator.NavDrawerNoIconListAdapter;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;
import com.es.tungnv.utils.SingleMediaScanner;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 2/24/2016.
 */
@SuppressWarnings("ResourceType")
public class GCSMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList_main, mDrawerList_so;
    private LinearLayout lnSlidermenu;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private ArrayList<String> navDrawerItemsNoIcon;
    private NavDrawerListAdapter adapter;
    private NavDrawerNoIconListAdapter adapterNoIcon;

    public static int width;
    public static int height;

    private ArrayList<LinkedHashMap<String, String>> ArrListConfig;
    private static String fileConfig = "";
    private LinkedHashMap<String, String> hmConfig;

    public static GcsSqliteConnection connection;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.gcs_activity_main);
            mTitle = mDrawerTitle = "Ghi chỉ số";
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();

//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            CheckFileConfigExist();
            getDataConfig();
            connection = new GcsSqliteConnection(getApplicationContext());
            
            navMenuTitles = getResources().getStringArray(R.array.gcs_nav_drawer_items);
            navMenuIcons = getResources().obtainTypedArray(R.array.gcs_nav_drawer_icons);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.gcs_activity_main_dl_main);
            mDrawerList_main = (ListView) findViewById(R.id.gcs_activity_main_lv_slidermenu_main);
            mDrawerList_so = (ListView) findViewById(R.id.gcs_activity_main_lv_slidermenu_so);
            lnSlidermenu = (LinearLayout) findViewById(R.id.gcs_activity_main_ln_slidermenu);
            lnSlidermenu.getLayoutParams().width = 4*width/5;
            navDrawerItems = new ArrayList<>();
            navDrawerItemsNoIcon = new ArrayList<>();

            for (int i = 0; i < navMenuIcons.length(); i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }

            Cursor cMaQuyen = connection.getMaQuyen();
            if(cMaQuyen.moveToFirst()){
                do {
                    navDrawerItemsNoIcon.add(cMaQuyen.getString(0));
                } while(cMaQuyen.moveToNext());
            }
            cMaQuyen.close();

            navMenuIcons.recycle();

            mDrawerList_main.setOnItemClickListener(new SlideMenuClickListener());
            mDrawerList_so.setOnItemClickListener(new SlideMenuSoClickListener());

            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            mDrawerList_main.setAdapter(adapter);

            adapterNoIcon = new NavDrawerNoIconListAdapter(getApplicationContext(), navDrawerItemsNoIcon);
            mDrawerList_so.setAdapter(adapterNoIcon);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.essp_actionbar_unselected));
//            getSupportActionBar().hide();

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

            mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    try {
                        View view = GCSMainActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(GCSMainActivity.this, "Lỗi ẩn bàn phím:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

            GcsCommon.setIMEI(Common.GetIMEI(this));

            String MA_QUYEN = adapterNoIcon.getCount() > 0 ? adapterNoIcon.getItem(0).toString() : "";
            displayViewSo(0, MA_QUYEN);
//            handleIntent(getIntent());
            GcsCommon.LoadFolder(GCSMainActivity.this);
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error onCreate: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        GcsCommon.LoadFolder(GCSMainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        backupDB();
    }

    private void backupDB(){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            String dateBackup = sdf.format(new Date());
            String sSRC = Environment.getExternalStorageDirectory().getAbsolutePath() + GcsConstantVariables.PROGRAM_DATA_PATH + "ESGCS.s3db";
            String sDST = Environment.getExternalStorageDirectory().getAbsolutePath() + GcsConstantVariables.PROGRAM_BACKUP_PATH + "ESGCS_" + dateBackup + ".s3db";
            File fSRC = new File(sSRC);
            File fDST = new File(sDST);

            if(connection.countBanGhi() > 0) {
                Common.copy(fSRC, fDST);
                new SingleMediaScanner(GCSMainActivity.this, fDST);

                File fBackup = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + GcsConstantVariables.PROGRAM_BACKUP_PATH);
                String[] arrBackup = fBackup.list();
                if(arrBackup.length > 20) {
                    File fileMin = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            GcsConstantVariables.PROGRAM_BACKUP_PATH + arrBackup[0]);
                    long min = new Date(fileMin.lastModified()).getTime();
                    for (String sDate : arrBackup) {
                        File fDate = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                GcsConstantVariables.PROGRAM_BACKUP_PATH + sDate);
                        Date lastModDate = new Date(fDate.lastModified());
                        if(min > lastModDate.getTime()){
                            min = lastModDate.getTime();
                            fileMin = fDate;
                        }
                    }
                    if(fileMin != null && fileMin.exists()) {
                        fileMin.delete();
                    }
                }
            }
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Lỗi sao lưu dữ liệu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private class SlideMenuSoClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayViewSo(position, parent.getAdapter().getItem(position).toString());
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
                fragment = new GcsDongBoFragment();
                GcsCommon.pos_activity = 0;
                break;
            case 1:
                fragment = new GcsCauHinhFragment();
                GcsCommon.pos_activity = 1;
                break;
            case 2:
                fragment = new GcsBaoCaoFragment();
                GcsCommon.pos_activity = 1;
                break;
            case 3:
                fragment = new GcsQuanLySoFragment();
                GcsCommon.pos_activity = 1;
                break;
            default:
                break;
        }

        mDrawerList_so.clearChoices();
        adapterNoIcon.notifyDataSetChanged();
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.gcs_activity_main_fl_container, fragment, fragment.getTag()).commit();
            mDrawerList_main.setItemChecked(position, true);
            mDrawerList_main.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(lnSlidermenu);
        } else {
            Toast.makeText(getApplicationContext(), "Error change fragment", Toast.LENGTH_LONG).show();
        }
    }

    private void displayViewSo(int position, String maQuyen) {
        try {
            if(!TextUtils.isEmpty(maQuyen)) {
                Fragment fragment = new GcsGhiChiSoFragment();

                mDrawerList_main.clearChoices();
                adapter.notifyDataSetChanged();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.gcs_activity_main_fl_container, fragment, "GCS").commit();
                    mDrawerList_so.setItemChecked(position, true);
                    mDrawerList_so.setSelection(position);
                    int daghi = GcsCommon.getDaGhi(connection.getCSoAndTTR(maQuyen));
                    int chuaghi = connection.getSoLuongBanGhi(maQuyen);
                    setTitle(maQuyen + " (" + daghi + "/" + chuaghi + ")");
                    mDrawerLayout.closeDrawer(lnSlidermenu);
                    Bundle bundle = new Bundle();
                    bundle.putString("MA_QUYEN", maQuyen);
                    fragment.setArguments(bundle);
                    backupDB();
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi chọn sổ", Toast.LENGTH_LONG).show();
                }
            }
        } catch(Exception ex) {
            ex.toString();
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searchView.clearFocus();
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag("GCS");
                            if (fragment != null && fragment.isVisible()) {
                                GcsGhiChiSoFragment fGCS = (GcsGhiChiSoFragment)getFragmentManager().findFragmentByTag("GCS");
                                fGCS.searchKH(newText);
//                                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });
                }
            });

        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), "Error create menu: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(lnSlidermenu);
        menu.findItem(R.id.search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void CheckFileConfigExist(){
        try {
            File programDirectory = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PATH);
            if (!programDirectory.exists()) {
                programDirectory.mkdirs();
            }
            File programDbDirectory = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH);
            if (!programDbDirectory.exists()) {
                programDbDirectory.mkdirs();
            }
            File programBackupDirectory = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_BACKUP_PATH);
            if (!programBackupDirectory.exists()) {
                programBackupDirectory.mkdirs();
            }
            File programTempDirectory = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_TEMP_PATH);
            if (!programTempDirectory.exists()) {
                programTempDirectory.mkdirs();
            }
            File programPhotoDirectory = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH);
            if (!programPhotoDirectory.exists()) {
                programPhotoDirectory.mkdirs();
            }
            String filePath = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PATH + GcsConstantVariables.CFG_FILENAME;
            File cfgFile = new File(filePath);
            if(cfgFile.exists()){
                GcsCommon.cfgInfo = GcsCommon.GetFileConfig();
                if(GcsCommon.cfgInfo == null){
                    GcsCommon.cfgInfo = new GcsConfigInfo();
                }
            } else {
                GcsCommon.cfgInfo = new GcsConfigInfo();
                GcsCommon.CreateFileConfig(GcsCommon.cfgInfo, cfgFile);
            }
        } catch (Exception ex) {
            Toast.makeText(GCSMainActivity.this.getApplicationContext(), "Lỗi kiểm tra file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getConfig() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String directoryName = sdcard + GcsConstantVariables.PROGRAM_PATH;
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
                        if (NodeName.equalsIgnoreCase("Config")) {
                            hmConfig = new LinkedHashMap<String, String>();
                            break;
                        }
                        for (int j = 0; j < GcsConstantVariables.CFG_COLUMN.length; j++) {
                            if (NodeName.equalsIgnoreCase(GcsConstantVariables.CFG_COLUMN[j])) {
                                hmConfig.put(GcsConstantVariables.CFG_COLUMN[j], pp.nextText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        NodeName = pp.getName();
                        if (NodeName.equalsIgnoreCase("Config")) {
                            ArrListConfig.add(hmConfig);
                            GcsCommon.setIpServer(hmConfig.get("IP"));
                            GcsCommon.setVERSION(hmConfig.get("VERSION"));
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    default:

                        break;
                }
            }

        } catch (Exception ex) {
            Toast.makeText(GCSMainActivity.this.getApplicationContext(), "Lỗi đọc file cấu hình: " + ex.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }
    
}
