package es.vinhnb.ttht.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.NaviMenuAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_HISTORY;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_LYDO_TREOTHAO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import es.vinhnb.ttht.entity.sharedpref.MainSharePref;
import es.vinhnb.ttht.view.TthtHnHistoryFragment.IOnTthtHnHistoryFragment;
import es.vinhnb.ttht.view.TthtHnMainFragment.IOnTthtHnMainFragment;
import es.vinhnb.ttht.view.TthtHnUploadFragment.IOnTthtHnUploadFragment;
import esolutions.com.esdatabaselib.baseSharedPref.SharePrefManager;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;
import static es.vinhnb.ttht.adapter.BBanAdapter.*;
import static es.vinhnb.ttht.view.TthtHnBBanTutiFragment.IOnTthtHnBBanTutiFragment;
import static es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.OnITthtHnChiTietCtoFragment;
import static es.vinhnb.ttht.view.TthtHnDownloadFragment.OnListenerTthtHnDownloadFragment;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CHITIET_BBAN_TUTI_THAO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CHITIET_BBAN_TUTI_TREO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CHITIET_CTO_THAO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CHITIET_CTO_TREO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CTO_THAO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.CTO_TREO;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuNaviLeft.TRAM;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuTop.BBAN_TUTI;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.TagMenuTop.CHITIET_CTO;
import static es.vinhnb.ttht.view.TthtHnTopMenuChiTietCtoFragment.IOnTthtHnTopMenuChiTietCtoFragment;
import static es.vinhnb.ttht.view.TthtHnTopSearchFragment.IOnTthtHnTopSearchFragment;

public class TthtHnMainActivityI extends TthtHnBaseActivity
        implements
        NaviMenuAdapter.INaviMenuAdapter,
        IOnTthtHnMainFragment,
        OnListenerTthtHnDownloadFragment,
        OnITthtHnChiTietCtoFragment,
        ChiTietCtoAdapter.OnIChiTietCtoAdapter,
        IOnTthtHnTopMenuChiTietCtoFragment,
        IOnTthtHnBBanTutiFragment,
        IOnTthtHnTopSearchFragment,
        IInteractionDataCommon,
        IOnTthtHnUploadFragment,
        IOnTthtHnHistoryFragment,
        IOnBBanAdapter {

    private LoginFragment.LoginData mLoginData;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private String mMaNVien;
    private GridView mGridView;
    private LinearLayout mllLogout;
    private TthtHnMainFragment fragmentMain;
    private TthtHnDownloadFragment fragmentDownload;
    private TthtHnUploadFragment fragmentUpload;
    private TthtHnHistoryFragment fragmentHistory;
    private TthtHnChiTietCtoFragment fragmentChitietCto;
    private TthtHnBBanTutiFragment fragmentBBanTuTi;
    private TthtHnTopMenuChiTietCtoFragment fragmentTopMenuChiTietCto;
    private TthtHnTopSearchFragment fragmentTopSearchFragment;
    private TagMenuNaviLeft tagMenuNaviLeftList;


    private int ID_BBAN_TRTH;
    private Common.MA_BDONG MA_BDONG;
    private ProgressBar mPbarload;
    private int indexSessionFragment;
    private boolean isAddTop;
    private boolean isAddMain;
    private SharePrefManager sharePrefManager;
    private int ID_BBAN_TUTI_CTO_TREO;
    private int ID_BBAN_TUTI_CTO_THAO;
    private TthtHnSQLDAO mSqlDao;


    public enum TypeFragment {
        TthtHnMainFragment(TthtHnMainFragment.class),
        TthtHnDownloadFragment(TthtHnDownloadFragment.class),
        TthtHnUploadFragment(TthtHnUploadFragment.class),
        TthtHnHistoryFragment(TthtHnHistoryFragment.class),
        TthtHnChiTietCtoFragment(es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.class),
        TthtHnBBanTutiFragment(TthtHnBBanTutiFragment.class),

        TthtHnTopMenuChiTietCtoFragment(TthtHnTopMenuChiTietCtoFragment.class),
        TthtHnTopSearchFragment(TthtHnTopSearchFragment.class);


        public Class<?> typeFrag;

        TypeFragment(Class<?> typeFrag) {
            this.typeFrag = typeFrag;
        }
    }

    public enum TypeViewMenu {
        VIEW,
        LINE,
        EMPTY
    }

    public enum TagMenuNaviLeft {
        BBAN_CTO("BBAN_CTO", TypeFragment.TthtHnMainFragment, "Biên bản công tơ", R.drawable.ic_tththn_bien_ban, TypeViewMenu.VIEW),
        TRAM("TRAM", TypeFragment.TthtHnMainFragment, "Trạm", R.drawable.ic_tththn_tram, TypeViewMenu.VIEW),
        CTO_TREO("CTO_TREO", TypeFragment.TthtHnMainFragment, "Công tơ treo", R.drawable.ic_tththn_cto_treo, TypeViewMenu.VIEW),
        CTO_THAO("CTO_THAO", TypeFragment.TthtHnMainFragment, "Công tơ tháo", R.drawable.ic_tththn_cto_thao, TypeViewMenu.VIEW),
        CHUNG_LOAI("CHUNG_LOAI", TypeFragment.TthtHnMainFragment, "Chủng loại", R.drawable.ic_tththn_chungloai, TypeViewMenu.VIEW),

        CHITIET_CTO_TREO("CHITIET_CTO_TREO", TypeFragment.TthtHnChiTietCtoFragment, "Chi tiết công tơ treo", R.drawable.ic_tththn_cto_treo, TypeViewMenu.EMPTY),
        CHITIET_CTO_THAO("CHITIET_CTO_THAO", TypeFragment.TthtHnChiTietCtoFragment, "Chi tiết công tơ tháo", R.drawable.ic_tththn_cto_thao, TypeViewMenu.EMPTY),
        CHITIET_BBAN_TUTI_TREO("CHITIET_BBAN_TUTI_TREO", TypeFragment.TthtHnBBanTutiFragment, "Biên bản TU Ti Treo", R.drawable.ic_tththn_cto_treo, TypeViewMenu.EMPTY),
        CHITIET_BBAN_TUTI_THAO("CHITIET_BBAN_TUTI_THAO", TypeFragment.TthtHnBBanTutiFragment, "Biên bản TU Ti Tháo", R.drawable.ic_tththn_cto_thao, TypeViewMenu.EMPTY),

        EMPTY1("", null, "", 0, TypeViewMenu.EMPTY),
        LINE1("", null, "", 0, TypeViewMenu.LINE),
        LINE2("", null, "", 0, TypeViewMenu.LINE),

        DOWNLOAD("DOWNLOAD", TypeFragment.TthtHnDownloadFragment, "Đồng bộ dữ liệu", R.drawable.ic_tththn_download_white, TypeViewMenu.VIEW),
        UPLOAD("UPLOAD", TypeFragment.TthtHnUploadFragment, "Gửi dữ liệu", R.drawable.ic_tththn_upload_2, TypeViewMenu.VIEW),
        HISTORY("HISTORY", TypeFragment.TthtHnHistoryFragment, "Lịch sử đồng bộ/Gửi", R.drawable.ic_tththn_history, TypeViewMenu.VIEW);


        public String tagFrag;
        public TypeFragment typeFrag;
        public String title;
        public int drawableIconID;
        public TypeViewMenu typeViewMenu;

        TagMenuNaviLeft(String tag, TypeFragment typeFrag, String title, int drawableIconID, TypeViewMenu typeViewMenu) {
            this.tagFrag = tag;
            this.typeFrag = typeFrag;
            this.title = title;
            this.drawableIconID = drawableIconID;
            this.typeViewMenu = typeViewMenu;
        }

        public static TagMenuNaviLeft findTagMenu(String tag) {
            for (TagMenuNaviLeft tagMenuNaviLeft : TagMenuNaviLeft.values()) {
                if (tagMenuNaviLeft.tagFrag.equals(tag))
                    return tagMenuNaviLeft;
            }
            return null;
        }
    }

    public enum TagMenuTop {
        CHITIET_CTO("CHITIET_CTO", TypeFragment.TthtHnTopMenuChiTietCtoFragment),
        BBAN_TUTI("BBAN_TUTI", TypeFragment.TthtHnTopMenuChiTietCtoFragment),
        CHUYEN_LOAI_CTO("CHUYEN_LOAI_CTO", TypeFragment.TthtHnTopMenuChiTietCtoFragment),
        SEARCH("SEARCH", TypeFragment.TthtHnTopSearchFragment),
        EMPTY("", null);


        public String tagFrag;
        public TypeFragment typeFrag;

        TagMenuTop(String tagFrag, TypeFragment typeFrag) {
            this.tagFrag = tagFrag;
            this.typeFrag = typeFrag;
        }

        public TagMenuTop findTagMenuTop(String tag) {
            for (TagMenuTop tagMenuTop : TagMenuTop.values()) {
                if (tagMenuTop.tagFrag.equals(tag))
                    return tagMenuTop;
            }
            return null;
        }
    }

    private ArrayList<NaviMenuAdapter.NaviMenu> naviMenuList = new ArrayList<>();
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mRlMain;
    private RelativeLayout mRlTopMenu;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(activity_ttht_hn_main);

            initDataAndView(getWindow().getDecorView().getRootView());

            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clear
        try {
            MainSharePref mainSharePref = (MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class);
            mainSharePref.tagMenuNaviLeft = "";
            sharePrefManager.writeDataSharePref(MainSharePref.class, mainSharePref);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initDataAndView(View viewRoot) throws Exception {
        super.setupFullScreen();
        super.setCoordinatorLayout((CoordinatorLayout) findViewById(R.id.cl_ac_main));

        //sqldao
        mSqlDao = new TthtHnSQLDAO(SqlHelper.getIntance().openDB(), this);

        //get Data sharepref
        sharePrefManager = SharePrefManager.getInstance();


        //init nav menu
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.BBAN_CTO));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TRAM));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(CTO_TREO));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.CTO_THAO));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.CHUNG_LOAI));


        //add line
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.EMPTY1));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.LINE1));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.LINE2));


        //addLine
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.DOWNLOAD));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.UPLOAD));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.HISTORY));


        //init View
        initNavigationDrawer();
    }


    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //getBundle
        mLoginData = (LoginFragment.LoginData) getIntent().getParcelableExtra(TthtHnLoginActivity.BUNDLE_LOGIN);
        mMaNVien = getIntent().getStringExtra(TthtHnLoginActivity.BUNDLE_MA_NVIEN);


        //click
        mllLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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


        //replace fragment
        //set fragment
        fragmentMain = new TthtHnMainFragment().newInstance(TagMenuNaviLeft.BBAN_CTO);
        fragmentTopSearchFragment = new TthtHnTopSearchFragment().newInstance(TagMenuNaviLeft.BBAN_CTO, TagMenuTop.SEARCH);
        isAddMain = true;
        isAddTop = true;
        updateSessionBackstackFragment(fragmentTopSearchFragment, fragmentMain, true);
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
                    TthtHnBaseFragment.IDialog iDialog = new TthtHnBaseFragment.IDialog() {
                        @Override
                        void clickOK() {
                            //delete
                            try {
                                mSqlDao.deleteAll(TABLE_BBAN_CTO.class);
                                mSqlDao.deleteAll(TABLE_ANH_HIENTRUONG.class);
                                mSqlDao.deleteAll(TABLE_BBAN_TUTI.class);
                                mSqlDao.deleteAll(TABLE_CHITIET_TUTI.class);
                                mSqlDao.deleteAll(TABLE_CHITIET_CTO.class);
                                mSqlDao.deleteAll(TABLE_LOAI_CONG_TO.class);
                                mSqlDao.deleteAll(TABLE_TRAM.class);
                                mSqlDao.deleteAll(TABLE_HISTORY.class);
                                mSqlDao.deleteAll(TABLE_LYDO_TREOTHAO.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showSnackBar(Common.MESSAGE.ex0x.getContent(), e.getMessage(), null);
                            }finally {
                                finish();
                            }


                        }

                        @Override
                        void clickCancel() {

                        }
                    }.setTextBtnOK("XÓA DỮ LIỆU").setTextBtnCancel("HỦY THAO TÁC");
                    TthtHnBaseFragment.showDialog(this, "Bạn có chắc muốn xóa tất cả dữ liệu!. Màn hình sẽ quay về đăng nhập", iDialog);
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
        mllLogout = (LinearLayout) findViewById(R.id.ll_ibtn_logout);
        mGridView = (GridView) findViewById(R.id.gv_nav_menu);
        mRlMain = (RelativeLayout) findViewById(R.id.rl_content_main);
        mRlTopMenu = (RelativeLayout) findViewById(R.id.rl_topmenu);
        mPbarload = (ProgressBar) findViewById(R.id.pbar_load);


        //set action bar
        findViewById(R.id.app_bar_tththn).bringToFront();
        setSupportActionBar(mToolbar);
        this.setActionBarTittle(TagMenuNaviLeft.BBAN_CTO.title);
    }

    //region NaviMenuAdapter.INaviMenuAdapter
    @Override
    public void doClickNaviMenu(int posNew, TagMenuNaviLeft tagNew) {
        try {
            setMenuNaviAndTitle(tagNew);
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            mTransaction = getSupportFragmentManager().beginTransaction();
            switch (tagNew) {
                case BBAN_CTO:
                case TRAM:
                case CHUNG_LOAI:
                case CTO_TREO:
                case CTO_THAO:
                    tagMenuNaviLeftList = tagNew;

                    if (fragmentVisible instanceof TthtHnMainFragment) {
                        fragmentMain = (TthtHnMainFragment) fragmentVisible;
                        fragmentMain.setTagMenuNaviLeft(tagNew);
                        isAddMain = false;
                    } else {
                        fragmentMain = new TthtHnMainFragment().newInstance(tagNew);
                        isAddMain = true;
                    }


                    fragmentTopSearchFragment = (TthtHnTopSearchFragment) showTopMenuFragment(tagNew, TagMenuTop.SEARCH);


                    updateSessionBackstackFragment(fragmentTopSearchFragment, fragmentMain, true);

                    break;

                case DOWNLOAD:
                    //luu gia tri de su dung khi onBackPress
                    tagMenuNaviLeftList = tagNew;


                    if (fragmentVisible instanceof TthtHnDownloadFragment) {
                        isAddMain = false;
                    } else {
                        fragmentDownload = new TthtHnDownloadFragment().newInstance();
                        isAddMain = true;
                    }


                    Fragment emptyFragment = showTopMenuFragment(tagNew, TagMenuTop.EMPTY);


                    updateSessionBackstackFragment(emptyFragment, fragmentDownload, false);

                    break;
                case UPLOAD:
                    //luu gia tri de su dung khi onBackPress
                    tagMenuNaviLeftList = tagNew;


                    if (fragmentVisible instanceof TthtHnUploadFragment) {
                        isAddMain = false;
                    } else {
                        fragmentUpload = new TthtHnUploadFragment().newInstance();
                        isAddMain = true;
                    }


                    emptyFragment = showTopMenuFragment(tagNew, TagMenuTop.EMPTY);


                    updateSessionBackstackFragment(emptyFragment, fragmentUpload, false);
                    break;


                case CHITIET_CTO_TREO:
                    break;
                case CHITIET_CTO_THAO:
                    break;
                case CHITIET_BBAN_TUTI_TREO:
                    break;
                case CHITIET_BBAN_TUTI_THAO:
                    break;


                case EMPTY1:
                    break;
                case LINE1:
                    break;
                case LINE2:
                    break;

                case HISTORY:
                    //luu gia tri de su dung khi onBackPress
                    tagMenuNaviLeftList = tagNew;


                    if (fragmentVisible instanceof TthtHnHistoryFragment) {
                        isAddMain = false;
                    } else {
                        fragmentHistory = new TthtHnHistoryFragment().newInstance();
                        isAddMain = true;
                    }


                    fragmentTopSearchFragment = (TthtHnTopSearchFragment) showTopMenuFragment(tagNew, TagMenuTop.SEARCH);

                    updateSessionBackstackFragment(fragmentTopSearchFragment, fragmentHistory, false);

                    break;
            }


            //nếu không phải cto treo, hoặc cto tháo thì xóa shared pref
            if (tagMenuNaviLeftList != CTO_TREO && tagMenuNaviLeftList != CTO_THAO) {
                MainSharePref mainSharePref = (MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class);
                mainSharePref.tagMenuNaviLeft = "";
                sharePrefManager.writeDataSharePref(MainSharePref.class, mainSharePref);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }


    }

    private void setMenuNaviAndTitle(String title, int posNew) {
        //set text action bar
        setActionBarTittle(title);


        //refresh againrơ
        if (posNew != 0)
            ((NaviMenuAdapter) mGridView.getAdapter()).refresh(naviMenuList, posNew);
    }


    //endregion

    //region IOnTthtHnTopSearchFragment
    @Override
    public void clickTopMenuChitietCto(TagMenuTop tagMenuTop) {
        try {

            //refresh data common
            Fragment fragmentVisible = null;
            int pos = -1;


            switch (tagMenuTop) {
                case CHITIET_CTO:

                    if (MA_BDONG == Common.MA_BDONG.B) {
                        setMenuNaviAndTitle(TagMenuNaviLeft.CTO_TREO);
                        setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_TREO);
                    } else {
                        setMenuNaviAndTitle(TagMenuNaviLeft.CTO_THAO);
                        setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_THAO);
                    }


                    //lấy pos của fragment chitiet hien tại
                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
                    if (fragmentVisible instanceof TthtHnChiTietCtoFragment)
                        pos = ((TthtHnChiTietCtoFragment) fragmentVisible).getPos();

                    if (fragmentVisible instanceof TthtHnBBanTutiFragment)
                        pos = ((TthtHnBBanTutiFragment) fragmentVisible).getPos();


                    if (pos == -1)
                        return;

                    fragmentChitietCto = (TthtHnChiTietCtoFragment) showChiTietCtoFragment(pos);

                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        fragmentTopMenuChiTietCto = ((TthtHnTopMenuChiTietCtoFragment) fragmentVisible);
                        fragmentTopMenuChiTietCto.refreshTagTopMenu(CHITIET_CTO);
                        isAddTop = false;
                    }

                    updateSessionBackstackFragment(fragmentTopMenuChiTietCto, fragmentChitietCto, false);
                    break;

                case CHUYEN_LOAI_CTO:
                    //set title
                    setMenuNaviAndTitle(MA_BDONG == Common.MA_BDONG.B ? CTO_TREO : CTO_THAO);
                    setMenuNaviAndTitle(MA_BDONG == Common.MA_BDONG.B ? CHITIET_CTO_TREO : CHITIET_CTO_THAO);


                    //refresh data chi tiet cto
                    //lấy pos của fragment chitiet hien tại
                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
                    if (fragmentVisible instanceof TthtHnChiTietCtoFragment)
                        pos = ((TthtHnChiTietCtoFragment) fragmentVisible).getPos();

                    if (fragmentVisible instanceof TthtHnBBanTutiFragment)
                        pos = ((TthtHnBBanTutiFragment) fragmentVisible).getPos();

                    if (pos == -1)
                        return;

                    fragmentChitietCto = (TthtHnChiTietCtoFragment) showChiTietCtoFragment(pos);


                    mTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        fragmentTopMenuChiTietCto = ((TthtHnTopMenuChiTietCtoFragment) fragmentVisible);
                        fragmentTopMenuChiTietCto.refreshTagTopMenu(CHITIET_CTO);
                        isAddTop = false;
                    }

                    updateSessionBackstackFragment(fragmentTopMenuChiTietCto, fragmentChitietCto, false);

                    //ghi thong tin sharepref
                    MainSharePref mainSharePref = (MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class);
                    mainSharePref.tagMenuNaviLeft = (MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO.tagFrag : TagMenuNaviLeft.CTO_THAO.tagFrag);
                    sharePrefManager.writeDataSharePref(MainSharePref.class, mainSharePref);
                    break;

                case BBAN_TUTI:
                    setMenuNaviAndTitle(MA_BDONG == Common.MA_BDONG.B ? CTO_TREO : CTO_THAO);
                    setMenuNaviAndTitle(MA_BDONG == Common.MA_BDONG.B ? CHITIET_BBAN_TUTI_TREO : CHITIET_BBAN_TUTI_THAO);


                    mTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
                    if (fragmentVisible instanceof TthtHnBBanTutiFragment) {
                        mTransaction.detach(fragmentBBanTuTi);
                        mTransaction.attach(fragmentBBanTuTi);
                        mTransaction.commit();
                    } else {
                        pos = -1;
                        if (fragmentVisible instanceof TthtHnChiTietCtoFragment)
                            pos = ((TthtHnChiTietCtoFragment) fragmentVisible).getPos();
                        //replace main relative
                        fragmentBBanTuTi = new TthtHnBBanTutiFragment().newInstance(pos);
                        mTransaction.replace(mRlMain.getId(), fragmentBBanTuTi);
                        mTransaction.commit();
                    }

                    mTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        fragmentTopMenuChiTietCto = ((TthtHnTopMenuChiTietCtoFragment) fragmentVisible);
                        fragmentTopMenuChiTietCto.refreshTagTopMenu(BBAN_TUTI);
                        mTransaction.detach(fragmentTopMenuChiTietCto);
                        mTransaction.attach(fragmentTopMenuChiTietCto);
                        mTransaction.commit();
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }
    //endregion

    //region ChiTietCtoAdapter.OnIChiTietCtoAdapter
    @Override
    public void clickRowChiTietCtoAdapter(int pos, int sizeFitle) {
        try {
            //title
            if (MA_BDONG == Common.MA_BDONG.B)
                setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_TREO);
            else
                setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_THAO);


            //save data posClick
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (fragmentVisible instanceof TthtHnMainFragment) {
                ((TthtHnMainFragment) fragmentVisible).savePosClick(pos, sizeFitle);
            }


            //save data search top menu
            fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
            if (fragmentVisible instanceof TthtHnTopSearchFragment) {
                ((TthtHnTopSearchFragment) fragmentVisible).saveInfoSearch();
            }


            //show body
            fragmentChitietCto = (TthtHnChiTietCtoFragment) showChiTietCtoFragment(pos);


            //show top menu
            fragmentTopMenuChiTietCto = (TthtHnTopMenuChiTietCtoFragment) showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);


            updateSessionBackstackFragment(fragmentTopMenuChiTietCto, fragmentChitietCto, true);

        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }

    public Fragment showChiTietCtoFragment(int pos) {
        try {
            //check fragment
            //nếu cùng kiểu thì chỉ cần nhận biết và gọi thân fragment update
            //ngược lại thì replace và add to backstack fragment mới
            mTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (fragmentVisible instanceof TthtHnChiTietCtoFragment) {
                fragmentChitietCto.refresh(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CHITIET_CTO_TREO : TagMenuNaviLeft.CHITIET_CTO_THAO, pos);
                isAddMain = false;
            } else {
                isAddMain = true;
                fragmentChitietCto = new TthtHnChiTietCtoFragment().newInstance(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CHITIET_CTO_TREO : TagMenuNaviLeft.CHITIET_CTO_THAO, pos);
            }

            return fragmentChitietCto;
        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }

        return new Fragment();
    }

    private void showBBanTuTiFragment() {
        if (MA_BDONG == Common.MA_BDONG.B)
            callFragment(CHITIET_BBAN_TUTI_TREO);
        else
            callFragment(CHITIET_BBAN_TUTI_THAO);
    }

    //endregion

    //region IInteractionDataCommon
    @Override
    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }


    @Override
    public int getID_BBAN_TUTI_CTO_TREO() {
        return ID_BBAN_TUTI_CTO_TREO;
    }

    @Override
    public int getID_BBAN_TUTI_CTO_THAO() {
        return ID_BBAN_TUTI_CTO_THAO;
    }

    @Override
    public Common.MA_BDONG getMA_BDONG() {
        return MA_BDONG;
    }

    @Override
    public LoginFragment.LoginData getLoginData() {
        return mLoginData;
    }

    @Override
    public String getMaNVien() {
        return mMaNVien;
    }

    @Override
    public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
        this.ID_BBAN_TRTH = ID_BBAN_TRTH;
    }


    public void setID_BBAN_TUTI_CTO_THAO(int ID_BBAN_TUTI_CTO_THAO) {
        this.ID_BBAN_TUTI_CTO_THAO = ID_BBAN_TUTI_CTO_THAO;
    }

    public void setID_BBAN_TUTI_CTO_TREO(int ID_BBAN_TUTI_CTO_TREO) {
        this.ID_BBAN_TUTI_CTO_TREO = ID_BBAN_TUTI_CTO_TREO;
    }

    @Override
    public void setMA_BDONG(Common.MA_BDONG MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
    }

    @Override
    public void setLoginData(LoginFragment.LoginData mLoginData) {
        this.mLoginData = mLoginData;
    }

    @Override
    public void setMaNVien(String mMaNVien) {
        this.mMaNVien = mMaNVien;
    }

    @Override
    public void setVisiblePbarLoad(boolean isShow) {
        if (isShow)
            mPbarload.setVisibility(View.VISIBLE);
        else
            mPbarload.setVisibility(View.GONE);
    }

    @Override
    public void setMenuNaviAndTitle(TagMenuNaviLeft tagMenuNaviLeft) {
        //set text action bar
        setActionBarTittle(tagMenuNaviLeft.title);


        //refresh againrơ
        ((NaviMenuAdapter) mGridView.getAdapter()).refresh(tagMenuNaviLeft);
    }

    @Override
    public void setNextCto(int posOld) {
        try {
            //check frag
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (!(fragmentVisible instanceof TthtHnMainFragment))
                return;


            //save pos new
            //kiem tra sharePref
            int sizeFitle = ((MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class)).sizeList;
            int posNew = posOld++;
            ((TthtHnMainFragment) fragmentVisible).savePosClick(posNew, sizeFitle);


            //show body
            fragmentChitietCto = (TthtHnChiTietCtoFragment) showChiTietCtoFragment(posNew);


            //show top
            fragmentTopMenuChiTietCto = (TthtHnTopMenuChiTietCtoFragment) showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);


            updateSessionBackstackFragment(fragmentTopMenuChiTietCto, fragmentChitietCto, false);

        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
    }

    @Override
    public void setPreCto(int posOld) {
        try {
            //check frag
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (!(fragmentVisible instanceof TthtHnMainFragment))
                return;


            //save pos new
            //kiem tra sharePref
            int sizeFitle = ((MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class)).sizeList;
            int posNew = posOld--;
            ((TthtHnMainFragment) fragmentVisible).savePosClick(posNew, sizeFitle);


            //show body
            fragmentChitietCto = (TthtHnChiTietCtoFragment) showChiTietCtoFragment(posNew);


            //show top
            fragmentTopMenuChiTietCto = (TthtHnTopMenuChiTietCtoFragment) showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);


            updateSessionBackstackFragment(fragmentTopMenuChiTietCto, fragmentChitietCto, false);
        } catch (Exception e) {
            e.printStackTrace();
            super.showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
    }

    //endregion


    //region IOnTthtHnTopSearchFragment
    @Override
    public void clickSearch(String typeSearchString, String messageSearch) {

        Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
        if (fragmentVisible instanceof TthtHnMainFragment) {
            ((TthtHnMainFragment) fragmentVisible).searchData(typeSearchString, messageSearch);
            return;
        }

        if (fragmentVisible instanceof TthtHnHistoryFragment) {
            ((TthtHnHistoryFragment) fragmentVisible).searchData(typeSearchString, messageSearch);
            return;
        }


    }
    //endregion

    private void callFragment(TagMenuNaviLeft tagNew) {
        try {
            //check fragment
            //trong trường hợp mRlMain.getId() được replace bởi nhiều loại fragment khác nhau trong enum TypeFragment
            //kiểm tra click menu mới có phải là loại khác fragment của menu cũ không
            boolean isSameTypeFragment = false;
            if (tagMenuNaviLeftList.typeFrag == tagNew.typeFrag)
                isSameTypeFragment = true;
            tagMenuNaviLeftList = tagNew;


            //nếu cùng kiểu thì chỉ cần nhận biết và gọi thân fragment update
            //ngược lại thì replace và add to backstack fragment mới
            mTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (isSameTypeFragment) {
                if (fragmentVisible instanceof TthtHnMainFragment) {
                    fragmentMain.setTagMenuNaviLeft(tagNew);
                    mTransaction.detach(fragmentMain);
                    mTransaction.attach(fragmentMain);
                    if (tagNew == CTO_TREO || tagNew == CTO_THAO)
                        mTransaction.addToBackStack(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO.tagFrag : TagMenuNaviLeft.CTO_THAO.tagFrag);
                    mTransaction.commit();
                }

                if (fragmentVisible instanceof TthtHnDownloadFragment) {
                    mTransaction.detach(fragmentDownload);
                    mTransaction.attach(fragmentDownload);
                    mTransaction.commit();
                }


                if (fragmentVisible instanceof TthtHnBBanTutiFragment) {
                    mTransaction.detach(fragmentBBanTuTi);
                    mTransaction.attach(fragmentBBanTuTi);
                    mTransaction.commit();
                }

            } else {
                if (tagNew.typeFrag == TypeFragment.TthtHnMainFragment) {
                    fragmentMain = new TthtHnMainFragment().newInstance(tagNew);
                    mTransaction.replace(mRlMain.getId(), fragmentMain);
                    if (tagNew == CTO_TREO || tagNew == CTO_THAO)
                        mTransaction.addToBackStack(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO.tagFrag : TagMenuNaviLeft.CTO_THAO.tagFrag);
                    mTransaction.commit();
                }

                if (tagNew.typeFrag == TypeFragment.TthtHnDownloadFragment) {
                    fragmentDownload = new TthtHnDownloadFragment().newInstance();
                    mTransaction.replace(mRlMain.getId(), fragmentDownload);
                    mTransaction.commit();
                }


                if (tagNew.typeFrag == TypeFragment.TthtHnBBanTutiFragment) {
                    //replace main relative
                    fragmentBBanTuTi = new TthtHnBBanTutiFragment().newInstance(0);
                    mTransaction.replace(mRlMain.getId(), fragmentBBanTuTi);
//                    mTransaction.addToBackStack(tagNew.tagFrag);
                    mTransaction.commit();
                }

            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }

    private void setActionBarTittle(String message) {
        SpannableString s = new SpannableString(message);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setElevation(0);
    }


    private Fragment showTopMenuFragment(TagMenuNaviLeft tagMenuNaviLeft, TagMenuTop tagMenuTop) {
        try {
            //check fragment mRlTopMenu
            mTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
            switch (tagMenuTop) {
                case CHITIET_CTO:
                case BBAN_TUTI:
                case CHUYEN_LOAI_CTO:
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        fragmentTopMenuChiTietCto.refreshTagTopMenu(tagMenuTop);
                        isAddTop = false;
                    } else {
                        fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment().newInstance(tagMenuTop);
                        isAddTop = true;
                    }
                    return fragmentTopMenuChiTietCto;
                case SEARCH:
                    if (fragmentVisible instanceof TthtHnTopSearchFragment) {
                        fragmentTopSearchFragment.refreshTagTopMenu(tagMenuNaviLeft, tagMenuTop);
                        isAddTop = false;
                    } else {
                        fragmentTopSearchFragment = new TthtHnTopSearchFragment().newInstance(tagMenuNaviLeft, tagMenuTop);
                        isAddTop = true;
                    }
                    return fragmentTopSearchFragment;

                case EMPTY:
                    Fragment fragment = new Fragment();
                    isAddTop = true;
                    return fragment;
            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
        return new Fragment();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0)
            super.onBackPressed();

        try {
            int index = this.getSupportFragmentManager().getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tagSessionNewest = backEntry.getName();


            String nameTag = "";
            do {
                nameTag = fm.getBackStackEntryAt(this.getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                if (tagSessionNewest.equalsIgnoreCase(nameTag)) {
                    fm.popBackStackImmediate();
                }
            }
            while (nameTag.equalsIgnoreCase(tagSessionNewest));


            //update menuNavigation
            //get fragment last
            Fragment f = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (f != null) {
                updateMenuNaviAndTitleOnBackPress(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.finish();
        }
    }


    private void updateMenuNaviAndTitleOnBackPress(Fragment f) {
        if (f instanceof TthtHnMainFragment) {

            setMenuNaviAndTitle(((TthtHnMainFragment) f).getTagMenuNaviLeft());
        }

        if (f instanceof TthtHnDownloadFragment) {
            setMenuNaviAndTitle(TagMenuNaviLeft.DOWNLOAD);
        }

        if (f instanceof TthtHnChiTietCtoFragment || f instanceof TthtHnBBanTutiFragment) {
            setMenuNaviAndTitle(((TthtHnChiTietCtoFragment) f).getTagMenuNaviLeft());
        }

        if (f instanceof TthtHnHistoryFragment) {
            setMenuNaviAndTitle(TagMenuNaviLeft.HISTORY);
        }

        if (f instanceof TthtHnUploadFragment) {
            setMenuNaviAndTitle(TagMenuNaviLeft.UPLOAD);
        }
    }

    private void updateSessionBackstackFragment(@Nullable android.support.v4.app.Fragment fragmentTopMenu, @Nullable android.support.v4.app.Fragment fragmentMain, boolean isAddToBackStack) {
        //create name
        if (isAddToBackStack)
            indexSessionFragment++;
        String tagSession1 = String.valueOf(indexSessionFragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (fragmentMain != null) {
            if (isAddMain) {
                ft.replace(mRlMain.getId(), fragmentMain, fragmentMain.getClass().getSimpleName());
                if (isAddToBackStack)
                    ft.addToBackStack(tagSession1);
            } else {
                ft.detach(fragmentMain);
                ft.attach(fragmentMain);
            }
            ft.commit();
        }

        ft = getSupportFragmentManager().beginTransaction();
        if (fragmentTopMenu != null) {
            if (isAddTop) {
                ft.replace(mRlTopMenu.getId(), fragmentTopMenu, fragmentTopMenu.getClass().getSimpleName());
                if (isAddToBackStack)
                    ft.addToBackStack(tagSession1);
            } else {
                ft.detach(fragmentTopMenu);
                ft.attach(fragmentTopMenu);
            }
            ft.commit();
        }
    }


    //region IOnBBanAdapter
    @Override
    public void clickBtnBBanMore(int pos, DataBBanAdapter dataBBanAdapter) {
        //show dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.row_tththn_bban_more);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        final TextView maKH = (TextView) dialog.findViewById(R.id.tv_bb_maKH);
        final TextView maHopDong = (TextView) dialog.findViewById(R.id.tv_bb_ma_hopdong);
        final TextView sobban = (TextView) dialog.findViewById(R.id.tv_bb_sobb);
        final TextView lydo = (TextView) dialog.findViewById(R.id.tv_bb_lydo);


        //fill data
        maKH.setText(dataBBanAdapter.getMaKH());
        maHopDong.setText(dataBBanAdapter.getMaHopDong());
        sobban.setText(dataBBanAdapter.getSobban());
        lydo.setText(dataBBanAdapter.getLydo());

        dialog.show();
    }
    //endregion

    //region IOnTthtHnMainFragment
    @Override
    public void refreshTopThongKeMainFragment(int countRow, int thongKe) {

        Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
        if(fragmentVisible instanceof TthtHnTopSearchFragment)
        {
            ((TthtHnTopSearchFragment)fragmentVisible).refreshTopThongKeMainFragment(countRow, thongKe);
        }
    }
    //endregion
}

