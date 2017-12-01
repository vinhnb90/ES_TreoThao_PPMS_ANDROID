package es.vinhnb.ttht.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.NaviMenuAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.view.TthtHnBBanTutiFragment.IOnTthtHnBBanTutiFragment;
import es.vinhnb.ttht.view.TthtHnDownloadFragment.OnListenerTthtHnDownloadFragment;

import static com.es.tungnv.views.R.layout.activity_ttht_hn_main;
import static es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.OnITthtHnChiTietCtoFragment;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuTop.CHITIET_CTO;
import static es.vinhnb.ttht.view.TthtHnMainFragment.OnListenerTthtHnMainFragment;
import static es.vinhnb.ttht.view.TthtHnTopMenuChiTietCtoFragment.IOnTthtHnTopMenuChiTietCtoFragment;

public class TthtHnMainActivity extends TthtHnBaseActivity implements
        NaviMenuAdapter.INaviMenuAdapter,
        OnListenerTthtHnMainFragment,
        OnListenerTthtHnDownloadFragment,
        OnITthtHnChiTietCtoFragment,
        ChiTietCtoAdapter.OnIChiTietCtoAdapter,
        IOnTthtHnTopMenuChiTietCtoFragment,
        IOnTthtHnBBanTutiFragment {

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
    private TagMenuNaviLeft tagOld;


    public enum TypeFragment {
        TthtHnMainFragment(TthtHnMainFragment.class),
        TthtHnDownloadFragment(TthtHnDownloadFragment.class),
        TthtHnUploadFragment(TthtHnUploadFragment.class),
        TthtHnHistoryFragment(TthtHnHistoryFragment.class),
        TthtHnChiTietCtoFragment(TthtHnChiTietCtoFragment.class),
        TthtHnBBanTutiFragment(TthtHnBBanTutiFragment.class),

        TthtHnTopMenuChiTietCtoFragment(TthtHnTopMenuChiTietCtoFragment.class);


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

        CHITIET_CTO_TREO("CHITIET_CTO_TREO", TypeFragment.TthtHnChiTietCtoFragment, "Chi tiết công tơ treo", R.drawable.ic_tththn_cto_treo, TypeViewMenu.VIEW),
        CHITIET_CTO_THAO("CHITIET_CTO_THAO", TypeFragment.TthtHnChiTietCtoFragment, "Chi tiết công tơ tháo", R.drawable.ic_tththn_cto_thao, TypeViewMenu.VIEW),
        CHITIET_BBAN_TUTI("CHITIET_BBAN_TUTI", TypeFragment.TthtHnBBanTutiFragment, "Chi tiết biên bản TU Ti của công tơ", R.drawable.ic_tththn_cto_treo, TypeViewMenu.VIEW),

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
        CHUYEN_LOAI_CTO("CHUYEN_LOAI_CTO", TypeFragment.TthtHnTopMenuChiTietCtoFragment);

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
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.BBAN_CTO).setClicked(true));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.TRAM));
        naviMenuList.add(new NaviMenuAdapter.NaviMenu(TagMenuNaviLeft.CTO_TREO));
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
    void setAction(Bundle savedInstanceState) throws Exception {
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, mLoginData);
        bundle.putString(TthtHnLoginActivity.BUNDLE_MA_NVIEN, mMaNVien);
        bundle.putSerializable(BUNDLE_TAG_MENU, TagMenuNaviLeft.BBAN_CTO);


        fragmentMain = new TthtHnMainFragment().newInstance(bundle);
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(mRlMain.getId(), fragmentMain);
        mTransaction.addToBackStack(TagMenuNaviLeft.BBAN_CTO.tagFrag);
        tagOld = TagMenuNaviLeft.BBAN_CTO;
        mTransaction.commit();
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


        //set action bar
        findViewById(R.id.app_bar_tththn).bringToFront();
        setSupportActionBar(mToolbar);
        this.setActionBarTittle(TagMenuNaviLeft.BBAN_CTO.title);
    }

    //region NaviMenuAdapter.INaviMenuAdapter
    @Override
    public void doClickNaviMenu(int pos, TagMenuNaviLeft tagNew) {
        try {
            //set text action bar
            setActionBarTittle(tagNew.title);


            //refresh againrơ
            naviMenuList.get(pos).setClicked(true);
            ((NaviMenuAdapter) mGridView.getAdapter()).refresh(naviMenuList, pos);


            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_TAG_MENU, tagNew);
            callFragment(tagNew, bundle);


//
//            //check fragment
//            //trong trường hợp mRlMain.getId() được replace bởi nhiều loại fragment khác nhau trong enum TypeFragment
//            //kiểm tra click menu mới có phải là loại khác fragment của menu cũ không
//            boolean isSameTypeFragment = false;
//            if (tagOld.typeFrag == tagNew.typeFrag)
//                isSameTypeFragment = true;


//            //nếu cùng kiểu thì chỉ cần nhận biết và gọi thân fragment update
//            //ngược lại thì replace và add to backstack fragment mới
//            mTransaction = getSupportFragmentManager().beginTransaction();
//            android.support.v4.app.Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
//            if (isSameTypeFragment) {
//                if (fragmentVisible instanceof TthtHnMainFragment) {
//                    fragmentMain.switchMenu(tagNew);
//                    mTransaction.detach(fragmentMain);
////                    fragmentMain = (TthtHnMainFragment) fragmentVisible;
//                    mTransaction.attach(fragmentMain);
//                    mTransaction.commit();
//                }
//
//                if (fragmentVisible instanceof TthtHnDownloadFragment) {
//                    fragmentDownload = (TthtHnDownloadFragment) fragmentVisible;
//                    mTransaction.detach(fragmentDownload);
//                    mTransaction.attach(fragmentDownload);
//                    mTransaction.commit();
//                }
//            } else {
//                if (tagNew.typeFrag == TypeFragment.TthtHnMainFragment) {
//                    fragmentMain = new TthtHnMainFragment().newInstance(mLoginData, mMaNVien, tagNew);
//                    mTransaction.replace(mRlMain.getId(), fragmentMain);
//                    mTransaction.addToBackStack(tagNew.tagFrag);
//                    mTransaction.commit();
//                }
//
//                if (tagNew.typeFrag == TypeFragment.TthtHnDownloadFragment) {
//                    fragmentDownload = new TthtHnDownloadFragment().newInstance(mLoginData, mMaNVien);
//                    mTransaction.replace(mRlMain.getId(), fragmentDownload);
//                    mTransaction.addToBackStack(tagNew.tagFrag);
//                    mTransaction.commit();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }


    }

    private void setActionBarTittle(String message) {
        SpannableString s = new SpannableString(message);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setElevation(0);
    }
    //endregion


    //region ChiTietCtoAdapter.OnIChiTietCtoAdapter
    @Override
    public void clickRowChiTietCtoAdapter(Common.MA_BDONG maBdong, ChiTietCtoAdapter.DataChiTietCtoAdapter ctoAdapter) {
        showChiTietCtoFragment(maBdong, ctoAdapter.getIdbbantrth());
    }

    private void showChiTietCtoFragment(Common.MA_BDONG maBdong, int idbbantrth) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, mLoginData);
        bundle.putString(TthtHnLoginActivity.BUNDLE_MA_NVIEN, mMaNVien);
        bundle.putInt(TthtHnMainActivity.BUNDLE_ID_BBAN_TRTH, idbbantrth);


        if (maBdong == Common.MA_BDONG.B) {
            bundle.putSerializable(BUNDLE_TAG_MENU, TagMenuNaviLeft.CHITIET_CTO_TREO);
            callFragment(TagMenuNaviLeft.CHITIET_CTO_TREO, bundle);
        } else {
            bundle.putSerializable(BUNDLE_TAG_MENU, TagMenuNaviLeft.CHITIET_CTO_THAO);
            callFragment(TagMenuNaviLeft.CHITIET_CTO_THAO, bundle);
        }
    }

    private void showBBanTuTiFragment(Common.MA_BDONG maBdong, int idbbantrth, int ID_BBAN_TUTI) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, mLoginData);
        bundle.putSerializable(TthtHnLoginActivity.BUNDLE_MA_BDONG, maBdong);
        bundle.putString(TthtHnLoginActivity.BUNDLE_MA_NVIEN, mMaNVien);
        bundle.putInt(TthtHnMainActivity.BUNDLE_ID_BBAN_TRTH, idbbantrth);
        bundle.putInt(TthtHnMainActivity.BUNDLE_ID_BBAN_TUTI, ID_BBAN_TUTI);


        bundle.putSerializable(BUNDLE_TAG_MENU, TagMenuNaviLeft.CHITIET_BBAN_TUTI);
        callFragment(TagMenuNaviLeft.CHITIET_BBAN_TUTI, bundle);
    }

    //endregion
    private void callFragment(TagMenuNaviLeft tagNew, Bundle bundle) {
        try {
            //check fragment
            //trong trường hợp mRlMain.getId() được replace bởi nhiều loại fragment khác nhau trong enum TypeFragment
            //kiểm tra click menu mới có phải là loại khác fragment của menu cũ không
            boolean isSameTypeFragment = false;
            if (tagOld.typeFrag == tagNew.typeFrag)
                isSameTypeFragment = true;
            tagOld = tagNew;


            //nếu cùng kiểu thì chỉ cần nhận biết và gọi thân fragment update
            //ngược lại thì replace và add to backstack fragment mới
            mTransaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlMain.getId());
            if (isSameTypeFragment) {
                if (fragmentVisible instanceof TthtHnMainFragment) {
                    fragmentMain.switchMenu(tagNew);
                    mTransaction.detach(fragmentMain);
                    mTransaction.attach(fragmentMain);
                    mTransaction.commit();
                }

                if (fragmentVisible instanceof TthtHnDownloadFragment) {
                    mTransaction.detach(fragmentDownload);
                    mTransaction.attach(fragmentDownload);
                    mTransaction.commit();
                }

                if (fragmentVisible instanceof TthtHnChiTietCtoFragment) {
                    fragmentChitietCto.switchMA_BDONG(bundle);
                    mTransaction.detach(fragmentChitietCto);
                    mTransaction.attach(fragmentChitietCto);
                    mTransaction.commit();
                }

                if (fragmentVisible instanceof TthtHnBBanTutiFragment) {
                    mTransaction.detach(fragmentBBanTuTi);
                    mTransaction.attach(fragmentBBanTuTi);
                    mTransaction.commit();
                }

            } else {
                if (tagNew.typeFrag == TypeFragment.TthtHnMainFragment) {
                    bundle.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, mLoginData);
                    bundle.putString(TthtHnLoginActivity.BUNDLE_MA_NVIEN, mMaNVien);
                    bundle.putSerializable(BUNDLE_TAG_MENU, tagNew);


                    fragmentMain = new TthtHnMainFragment().newInstance(bundle);
                    mTransaction.replace(mRlMain.getId(), fragmentMain);
                    mTransaction.commit();
                }

                if (tagNew.typeFrag == TypeFragment.TthtHnDownloadFragment) {
                    bundle.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, mLoginData);
                    bundle.putString(TthtHnLoginActivity.BUNDLE_MA_NVIEN, mMaNVien);


                    fragmentDownload = new TthtHnDownloadFragment().newInstance(bundle);
                    mTransaction.replace(mRlMain.getId(), fragmentDownload);
                    mTransaction.commit();
                }

                if (tagNew.typeFrag == TypeFragment.TthtHnChiTietCtoFragment) {
                    //replace main relative
                    fragmentChitietCto = new TthtHnChiTietCtoFragment().newInstance(bundle);
                    mTransaction.replace(mRlMain.getId(), fragmentChitietCto);
                    mTransaction.addToBackStack(tagNew.tagFrag);
                    mTransaction.commit();
                }

                if (tagNew.typeFrag == TypeFragment.TthtHnBBanTutiFragment) {
                    //replace main relative
                    fragmentBBanTuTi = new TthtHnBBanTutiFragment().newInstance(bundle);
                    mTransaction.replace(mRlMain.getId(), fragmentBBanTuTi);
                    mTransaction.addToBackStack(tagNew.tagFrag);
                    mTransaction.commit();
                }

            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }


    //region OnITthtHnChiTietCtoFragment
    @Override
    public void showTopMenuChiTietCtoFragment(Bundle bundle) {
        try {
            //check fragment mRlTopMenu
            mTransaction = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.Fragment fragmentVisible = getSupportFragmentManager().findFragmentById(mRlTopMenu.getId());
            if (fragmentVisible instanceof TthtHnTopMenuChiTietCtoFragment) {
                mTransaction.detach(fragmentTopMenuChiTietCto);
                mTransaction.attach(fragmentTopMenuChiTietCto);
                mTransaction.commit();
            } else {
                bundle.putSerializable(BUNDLE_TYPE_TOPMENU, CHITIET_CTO);
                fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment().newInstance(bundle);
                mTransaction.replace(mRlTopMenu.getId(), fragmentTopMenuChiTietCto);
                mTransaction.commit();
            }
        } catch (Exception e) {
            super.showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }
    }
    //endregion


    //region IOnTthtHnTopMenuChiTietCtoFragment
    @Override
    public void clickTopMenuButton(Bundle bundle) {
        int ID_BBAN_TRTH = bundle.getInt(BUNDLE_ID_BBAN_TRTH, 0);
        int ID_BBAN_TUTI = bundle.getInt(BUNDLE_ID_BBAN_TUTI, 0);
        Common.MA_BDONG maBdong = (Common.MA_BDONG) bundle.getSerializable(BUNDLE_MA_BDONG);
        TagMenuTop tagMenuTop = (TagMenuTop) bundle.getSerializable(BUNDLE_TYPE_TOPMENU);

        switch (tagMenuTop) {
            case CHITIET_CTO:
            case CHUYEN_LOAI_CTO:
                showChiTietCtoFragment(maBdong, ID_BBAN_TRTH);
                break;

            case BBAN_TUTI:
                showBBanTuTiFragment(maBdong, ID_BBAN_TRTH, ID_BBAN_TUTI);
                break;
        }
    }
    //endregion

//    //replace top menu relative
//                    bundle.putString();
//    fragmentTopMenuChiTietCto = new TthtHnTopMenuChiTietCtoFragment.newInstance(bundle)
}
