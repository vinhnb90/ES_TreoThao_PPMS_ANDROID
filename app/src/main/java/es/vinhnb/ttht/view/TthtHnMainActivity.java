package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.NaviMenuAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.view.TthtHnMainFragment.OnListenerTthtHnMainFragment;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;
import static es.vinhnb.ttht.view.TthtHnBBanTutiFragment.IOnTthtHnBBanTutiFragment;
import static es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.OnITthtHnChiTietCtoFragment;
import static es.vinhnb.ttht.view.TthtHnDownloadFragment.OnListenerTthtHnDownloadFragment;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft.CTO_THAO;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft.CTO_TREO;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft.TRAM;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuTop.CHITIET_CTO;
import static es.vinhnb.ttht.view.TthtHnTopMenuChiTietCtoFragment.IOnTthtHnTopMenuChiTietCtoFragment;
import static es.vinhnb.ttht.view.TthtHnTopSearchFragment.IOnTthtHnTopSearchFragment;

public class TthtHnMainActivity extends TthtHnBaseActivity
        implements
        NaviMenuAdapter.INaviMenuAdapter,
        OnListenerTthtHnMainFragment,
        OnListenerTthtHnDownloadFragment,
        OnITthtHnChiTietCtoFragment,
        ChiTietCtoAdapter.OnIChiTietCtoAdapter,
        IOnTthtHnTopMenuChiTietCtoFragment,
        IOnTthtHnBBanTutiFragment,
        IOnTthtHnTopSearchFragment,
        IInteractionDataCommon {

    private LoginFragment.LoginData mLoginData;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private String mMaNVien;
    private GridView mGridView;
    private ImageButton mIbtnLogout;
    private TthtHnMainFragment fragmentMain;
    private TthtHnDownloadFragment fragmentDownload;
    private TthtHnChiTietCtoFragment fragmentChitietCto;
    private TthtHnBBanTutiFragment fragmentBBanTuTi;
    private TthtHnTopMenuChiTietCtoFragment fragmentTopMenuChiTietCto;
    private TthtHnTopSearchFragment fragmentTopSearchFragment;
    private TagMenuNaviLeft tagMenuNaviLeftList;


    private int ID_BBAN_TRTH;
    private int ID_BBAN_TUTI_CTO;
    private Common.MA_BDONG MA_BDONG;
    private ProgressBar mPbarload;
    private int indexSessionFragment;
    private boolean isAddTop;
    private boolean isAddMain;


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
        UPLOAD("UPLOAD", TypeFragment.TthtHnUploadFragment, "Gửi dữ liệu", R.drawable.ic_tththn_upload, TypeViewMenu.VIEW),
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

        public TagMenuNaviLeft findTagMenu(String tag) {
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
        CHI_TIET_CTO("CHI_TIET_CTO", TypeFragment.TthtHnTopMenuChiTietCtoFragment),
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
    public void initDataAndView(View viewRoot) throws Exception {
        super.setupFullScreen();
        super.setCoordinatorLayout((CoordinatorLayout) findViewById(R.id.cl_ac_main));


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
        fragmentTopSearchFragment = new TthtHnTopSearchFragment().newInstance(TagMenuNaviLeft.BBAN_CTO, TagMenuTop.SEARCH);
        fragmentMain = new TthtHnMainFragment().newInstance(TagMenuNaviLeft.BBAN_CTO);
        isAddMain = true;
        isAddTop = true;
        updateSessionBackstackFragment(fragmentTopSearchFragment, fragmentMain);
//        mTransaction = getSupportFragmentManager().beginTransaction();
//        mTransaction.replace(mRlMain.getId(), fragmentMain);
//
//        tagMenuNaviLeftList = TagMenuNaviLeft.BBAN_CTO;
//
//
//        //replace top menu

//        mTransaction.replace(mRlTopMenu.getId(), fragmentTopSearchFragment);
//        mTransaction.commit();
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
//                    if (fragmentVisible instanceof TthtHnMainFragment) {
//                        fragmentMain.setTagMenuNaviLeft(tagNew);
//                        mTransaction.detach(fragmentMain);
//                        mTransaction.attach(fragmentMain);
//                        mTransaction.commit();
//                    }
//
//                    else {
//                        fragmentMain = new TthtHnMainFragment().newInstance(tagNew);
//                        mTransaction.replace(mRlMain.getId(), fragmentMain);
//                        mTransaction.addToBackStack(tagNew.tagFrag);
//                        mTransaction.commit();
//                    }
//
//
//                    //show top menu
//                    if (tagNew == CHUNG_LOAI || tagNew == TRAM) {
//                        Fragment fragment = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
//                        if (fragment != null)
//                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    } else
//                        showTopMenuFragment(tagNew, SEARCH);
                    //luu gia tri de su dung khi onBackPress
                    tagMenuNaviLeftList = tagNew;

                    if (fragmentVisible instanceof TthtHnMainFragment) {
                        fragmentMain.setTagMenuNaviLeft(tagNew);
                        mTransaction.detach(fragmentMain);
                        mTransaction.attach(fragmentMain);
                        mTransaction.commit();
                        isAddMain = false;
                    } else {
                        fragmentMain = new TthtHnMainFragment().newInstance(tagNew);
                        isAddMain = true;
                    }

                    Fragment fragmentTop = showTopMenuFragmentTest(tagNew, TagMenuTop.SEARCH);
                    updateSessionBackstackFragment(fragmentTop, fragmentMain);
                    break;


//                if (fragmentVisible instanceof TthtHnDownloadFragment) {
//                    mTransaction.detach(fragmentDownload);
//                    mTransaction.attach(fragmentDownload);
//                    mTransaction.commit();
//                }
//
//
//                if (fragmentVisible instanceof TthtHnBBanTutiFragment) {
//                    mTransaction.detach(fragmentBBanTuTi);
//                    mTransaction.attach(fragmentBBanTuTi);
//                    mTransaction.commit();
//                }
//
//            } else{
//                if (tagNew.typeFrag == TypeFragment.TthtHnMainFragment) {
//                    fragmentMain = new TthtHnMainFragment().newInstance(tagNew);
//                    mTransaction.replace(mRlMain.getId(), fragmentMain);
//                    if (tagNew == CTO_TREO || tagNew == CTO_THAO)
//                        mTransaction.addToBackStack(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO.tagFrag : TagMenuNaviLeft.CTO_THAO.tagFrag);
//                    mTransaction.commit();
//                }
//
//                if (tagNew.typeFrag == TypeFragment.TthtHnDownloadFragment) {
//                    fragmentDownload = new TthtHnDownloadFragment().newInstance();
//                    mTransaction.replace(mRlMain.getId(), fragmentDownload);
//                    mTransaction.commit();
//                }
//
//
//                if (tagNew.typeFrag == TypeFragment.TthtHnBBanTutiFragment) {
//                    //replace main relative
//                    fragmentBBanTuTi = new TthtHnBBanTutiFragment().newInstance();
//                    mTransaction.replace(mRlMain.getId(), fragmentBBanTuTi);
////                    mTransaction.addToBackStack(tagNew.tagFrag);
//                    mTransaction.commit();
//                }
//
//            }


                case DOWNLOAD:
                    //luu gia tri de su dung khi onBackPress
                    tagMenuNaviLeftList = tagNew;

                    if (fragmentVisible instanceof TthtHnDownloadFragment) {
                        mTransaction.detach(fragmentDownload);
                        mTransaction.attach(fragmentDownload);
                        mTransaction.commit();
                        isAddMain = false;
                    } else {
                        fragmentDownload = new TthtHnDownloadFragment().newInstance();
                        isAddMain = true;
                    }
                    fragmentTop = showTopMenuFragmentTest(tagNew, TagMenuTop.EMPTY);
                    updateSessionBackstackFragment(fragmentTop, fragmentDownload);

                    break;
                case UPLOAD:
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
                    break;
            }

//            callFragment(tagNew);
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
        //refresh data common
        Fragment fragmentVisible = null;
        int pos = -1;


        switch (tagMenuTop) {
            case CHITIET_CTO:

                if (MA_BDONG == Common.MA_BDONG.B)
                    setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_TREO);
                else
                    setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_THAO);


                //lấy pos của fragment chitiet hien tại
                fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
                if (fragmentVisible instanceof TthtHnChiTietCtoFragment)
                    pos = ((TthtHnChiTietCtoFragment) fragmentVisible).getPos();

                if (pos != -1)
                    showChiTietCtoFragment(pos);
                break;

            case CHUYEN_LOAI_CTO:
                setMenuNaviAndTitle(MA_BDONG == Common.MA_BDONG.B ? CTO_TREO : CTO_THAO);


                callFragment(MA_BDONG == Common.MA_BDONG.B ? CTO_TREO : CTO_THAO);


                //lấy pos của fragment chitiet hien tại
                fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
                if (fragmentVisible instanceof TthtHnChiTietCtoFragment)
                    pos = ((TthtHnChiTietCtoFragment) fragmentVisible).getPos();

                if (pos != -1)
                    showChiTietCtoFragment(pos);


                break;

            case BBAN_TUTI:
                showBBanTuTiFragment();


                if (MA_BDONG == Common.MA_BDONG.B)
                    setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_BBAN_TUTI_TREO);
                else
                    setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_BBAN_TUTI_THAO);
                break;
        }
    }
    //endregion

    //region ChiTietCtoAdapter.OnIChiTietCtoAdapter
    @Override
    public void clickRowChiTietCtoAdapter(int pos) {
        //title
        if (MA_BDONG == Common.MA_BDONG.B)
            setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_TREO);
        else
            setMenuNaviAndTitle(TagMenuNaviLeft.CHITIET_CTO_THAO);


        //save data posClick
        Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
        if (fragmentVisible instanceof TthtHnMainFragment) {
            ((TthtHnMainFragment) fragmentVisible).savePosClick(pos, MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO);
        }


        //show body
        showChiTietCtoFragment(pos);


        //show top menu
        showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);
    }

    public void showChiTietCtoFragment(int pos) {
        try {
            //check fragment
            //nếu cùng kiểu thì chỉ cần nhận biết và gọi thân fragment update
            //ngược lại thì replace và add to backstack fragment mới
            mTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (fragmentVisible instanceof TthtHnChiTietCtoFragment) {
                fragmentChitietCto.refresh(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CHITIET_CTO_TREO : TagMenuNaviLeft.CHITIET_CTO_THAO, pos);
                mTransaction.detach(fragmentChitietCto);
                mTransaction.attach(fragmentChitietCto);

                mTransaction.commit();
            } else {
                fragmentChitietCto = new TthtHnChiTietCtoFragment().newInstance(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CHITIET_CTO_TREO : TagMenuNaviLeft.CHITIET_CTO_THAO, pos);
                mTransaction.replace(mRlMain.getId(), fragmentChitietCto);
                mTransaction.commit();
            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }

    private void showBBanTuTiFragment() {
        if (MA_BDONG == Common.MA_BDONG.B)
            callFragment(TagMenuNaviLeft.CHITIET_BBAN_TUTI_TREO);
        else
            callFragment(TagMenuNaviLeft.CHITIET_BBAN_TUTI_THAO);
    }

    //endregion

    //region IInteractionDataCommon
    @Override
    public int getID_BBAN_TRTH() {
        return ID_BBAN_TRTH;
    }

    @Override
    public int getID_BBAN_TUTI_CTO() {
        return ID_BBAN_TUTI_CTO;
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


    @Override
    public void setID_BBAN_TUTI_CTO(int ID_BBAN_TUTI_CTO) {
        this.ID_BBAN_TUTI_CTO = ID_BBAN_TUTI_CTO;
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
            int posNew = ((TthtHnMainFragment) fragmentVisible).refreshPreCto(posOld);
            ((TthtHnMainFragment) fragmentVisible).savePosClick(posNew, MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO);


            //show body
            showChiTietCtoFragment(posNew);


            //show top
            showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);

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
            int posNew = ((TthtHnMainFragment) fragmentVisible).refreshPreCto(posOld);
            ((TthtHnMainFragment) fragmentVisible).savePosClick(posNew, MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO);


            //show body
            showChiTietCtoFragment(posNew);


            //show top
            showTopMenuFragment(MA_BDONG == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO, CHITIET_CTO);
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
        if (!(fragmentVisible instanceof TthtHnMainFragment)) {
            return;
        }

        ((TthtHnMainFragment) fragmentVisible).searchData(typeSearchString, messageSearch);
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
                    fragmentBBanTuTi = new TthtHnBBanTutiFragment().newInstance();
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

    private void showTopMenuFragment(TagMenuNaviLeft tagMenuNaviLeft, TagMenuTop tagMenuTop) {
        try {
            //check fragment mRlTopMenu
            mTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());

            switch (tagMenuTop) {
                case CHITIET_CTO:
                case BBAN_TUTI:
                case CHUYEN_LOAI_CTO:
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        ((TthtHnTopMenuChiTietCtoFragment) fragmentVisible).refreshTagTopMenu(tagMenuTop);
                    } else {
                        fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment().newInstance(tagMenuTop);
                        mTransaction.replace(mRlTopMenu.getId(), fragmentTopMenuChiTietCto);
                        mTransaction.commit();
                    }
                    break;
                case SEARCH:
                    if (fragmentVisible instanceof TthtHnTopSearchFragment) {
                        ((TthtHnTopSearchFragment) fragmentVisible).refreshTagTopMenu(tagMenuNaviLeft, tagMenuTop);
                    } else {
                        fragmentTopSearchFragment = new TthtHnTopSearchFragment().newInstance(tagMenuNaviLeft, tagMenuTop);
                        mTransaction.replace(mRlTopMenu.getId(), fragmentTopSearchFragment);
                        mTransaction.commit();
                    }
                    break;
            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }


    private Fragment showTopMenuFragmentTest(TagMenuNaviLeft tagMenuNaviLeft, TagMenuTop tagMenuTop) {
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
                        mTransaction.detach(fragmentTopMenuChiTietCto);
                        mTransaction.attach(fragmentTopMenuChiTietCto);
                        mTransaction.commit();
                        isAddTop = false;
                    } else {
                        fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment().newInstance(tagMenuTop);
                        isAddTop = true;
                    }
                    return fragmentTopMenuChiTietCto;
                case SEARCH:
                    if (fragmentVisible instanceof TthtHnTopSearchFragment) {
                        fragmentTopSearchFragment.refreshTagTopMenu(tagMenuNaviLeft, tagMenuTop);
                        mTransaction.detach(fragmentTopSearchFragment);
                        mTransaction.attach(fragmentTopSearchFragment);
                        mTransaction.commit();
                        isAddTop = false;
                    } else {
                        fragmentTopSearchFragment = new TthtHnTopSearchFragment().newInstance(tagMenuNaviLeft, tagMenuTop);
                        isAddTop = true;
                    }
                    return fragmentTopSearchFragment;


                case CHI_TIET_CTO:
                    if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                        fragmentTopMenuChiTietCto.refreshTagTopMenu(tagMenuTop);
                        mTransaction.detach(fragmentTopMenuChiTietCto);
                        mTransaction.attach(fragmentTopMenuChiTietCto);
                        mTransaction.commit();
                        isAddTop = false;
                    } else {
                        fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment().newInstance(tagMenuTop);
                        isAddTop = true;
                    }
                    return fragmentTopMenuChiTietCto;
                case EMPTY:
                    Fragment fragment = new Fragment();
                    isAddTop = true;
                    return fragment;
            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0)
            super.onBackPressed();


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
        if (f != null){
            updateMenuNaviAndTitle(f);
        }
    }


    private void updateMenuNaviAndTitle(Fragment f) {
        if(f instanceof TthtHnMainFragment)
        {

            setMenuNaviAndTitle(((TthtHnMainFragment)f).getTagMenuNaviLeft());
        }

        if(f instanceof TthtHnDownloadFragment)
        {
            setMenuNaviAndTitle(TagMenuNaviLeft.DOWNLOAD);
        }

        if(f instanceof TthtHnChiTietCtoFragment || f instanceof TthtHnBBanTutiFragment)
        {
            setMenuNaviAndTitle(((TthtHnChiTietCtoFragment)f).getTagMenuNaviLeft());
        }

        if(f instanceof TthtHnHistoryFragment)
        {
            setMenuNaviAndTitle(TagMenuNaviLeft.HISTORY);
        }

        if(f instanceof TthtHnUploadFragment)
        {
            setMenuNaviAndTitle(TagMenuNaviLeft.UPLOAD);
        }
    }

    private void updateSessionBackstackFragment(@Nullable android.support.v4.app.Fragment fragmentTopMenu, @Nullable android.support.v4.app.Fragment fragmentMain) {
        //create name
        indexSessionFragment++;
        String tagSession1 = String.valueOf(indexSessionFragment);


        if (isAddTop && fragmentTopMenu!=null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mRlTopMenu.getId(), fragmentTopMenu, fragmentTopMenu.getClass().getSimpleName())
                    .addToBackStack(tagSession1)
                    .commit();

        if (isAddMain && fragmentMain!=null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mRlMain.getId(), fragmentMain, fragmentMain.getClass().getSimpleName())
                    .addToBackStack(tagSession1)
                    .commit();
    }
}

