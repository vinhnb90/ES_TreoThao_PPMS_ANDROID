package es.vinhnb.ttht.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.es.tungnv.views.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.common.Common.DELAY_ANIM;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TYPE_TOPMENU;

public class TthtHnTopMenuChiTietCtoFragment extends TthtHnBaseFragment {

    private IOnTthtHnTopMenuChiTietCtoFragment mListener;

    private Drawable drawable10, drawable11;

    private Unbinder unbinder;
    private IInteractionDataCommon onIDataCommon;

    //menu top
    @BindView(R.id.btn_cto_menu)
    Button btnCtoMenu;

    @BindView(R.id.btn_bban_tuti_menu)
    Button btnBBTuTiMenu;

    @BindView(R.id.btn_chuyen_loaicto_menu)
    Button btnChuyenLoaiCtoMenu;

    TthtHnSQLDAO mSqlDAO;
    private TthtHnMainActivity.TagMenuTop tagMenuTop;

    public TthtHnTopMenuChiTietCtoFragment() {
        // Required empty public constructor
    }

    public static TthtHnTopMenuChiTietCtoFragment newInstance(TthtHnMainActivity.TagMenuTop tagMenuTop) {
        TthtHnTopMenuChiTietCtoFragment fragment = new TthtHnTopMenuChiTietCtoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TYPE_TOPMENU, tagMenuTop);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagMenuTop = (TthtHnMainActivity.TagMenuTop) bundle.getSerializable(BUNDLE_TYPE_TOPMENU);
        }


        if (getContext() instanceof IInteractionDataCommon)
            this.onIDataCommon = (IInteractionDataCommon) getContext();
        else
            throw new ClassCastException("context must be implemnet IInteractionDataCommon!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_tththn_top_menu_chitietcto, container, false);
        unbinder = ButterKnife.bind(TthtHnTopMenuChiTietCtoFragment.this, viewRoot);


        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }


        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        drawable10 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle10);
        drawable11 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle11);


        if (context instanceof IOnTthtHnTopMenuChiTietCtoFragment) {
            mListener = (IOnTthtHnTopMenuChiTietCtoFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnTopSearchFragment");
        }


        try {
            mSqlDAO = new TthtHnSQLDAO(SqlHelper.getIntance().openDB(), context);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View viewRoot) throws Exception {

        //show visible Topmenu
        showTopMenu(viewRoot);
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //menu top
        catchClickTopMenu();
    }

    private void showTopMenu(View view) {
        //set background menu
        setBackgroundTopMenu(view);


        //set visible
        btnCtoMenu.setVisibility(View.VISIBLE);
        btnBBTuTiMenu.setVisibility(View.VISIBLE);
        btnChuyenLoaiCtoMenu.setVisibility(View.VISIBLE);


        //get info bban tu ti
        if (onIDataCommon.getID_BBAN_TUTI_CTO() == 0) {
            btnBBTuTiMenu.setVisibility(View.GONE);
        }
    }

    private void catchClickTopMenu() {
        btnCtoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    //clickTopMenuChitietCto
                    Common.runAnimationClickView(view, R.anim.tththn_scale_view_pull, DELAY_ANIM);


                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setBackgroundTopMenu(view);
                            mListener.clickTopMenuChitietCto(TthtHnMainActivity.TagMenuTop.CHITIET_CTO);
                        }
                    }, DELAY_ANIM + DELAY_ANIM);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChuyenLoaiCtoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //clickTopMenuChitietCto
                    Common.runAnimationClickView(view, R.anim.tththn_scale_view_pull, DELAY_ANIM);

                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                onIDataCommon.setVisiblePbarLoad(true);

                                //refresh data MA_BDONG, ID_BBAN_TUTI
                                onIDataCommon.setMA_BDONG(onIDataCommon.getMA_BDONG() == Common.MA_BDONG.B ? Common.MA_BDONG.E : Common.MA_BDONG.B);

//                                int ID_BBAN_TUTI = 0;
//                                String[] args = new String[]{String.valueOf(onIDataCommon.getID_BBAN_TRTH()), onIDataCommon.getMA_BDONG().code, onIDataCommon.getMaNVien()};
//                                List<TABLE_CHITIET_CTO> tableChitietCtoList = mSqlDAO.getChiTietCongto(args);
//                                if (tableChitietCtoList.size() != 0)
//                                    ID_BBAN_TUTI = tableChitietCtoList.get(0).getID_BBAN_TUTI();
//                                onIDataCommon.setID_BBAN_TUTI_CTO(ID_BBAN_TUTI);

                                mListener.clickTopMenuChitietCto(TthtHnMainActivity.TagMenuTop.CHUYEN_LOAI_CTO);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                            }
                        }
                    }, DELAY_ANIM +DELAY_ANIM);

                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnBBTuTiMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //clickTopMenuChitietCto
                    Common.runAnimationClickView(view, R.anim.tththn_scale_view_pull, DELAY_ANIM);
                    setBackgroundTopMenu(view);


                    //clickTopMenuChitietCto
                    mListener.clickTopMenuChitietCto(TthtHnMainActivity.TagMenuTop.BBAN_TUTI);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });
    }

    private void setBackgroundTopMenu(View view) {
        btnCtoMenu.setBackground(drawable10);
        btnBBTuTiMenu.setBackground(drawable10);
        btnChuyenLoaiCtoMenu.setBackground(drawable10);

        if (view.getId() == btnBBTuTiMenu.getId()) {
            btnBBTuTiMenu.setBackground(drawable11);
        }
        if (view.getId() == btnCtoMenu.getId()) {
            btnCtoMenu.setBackground(drawable11);
        }
        if (view.getId() == btnChuyenLoaiCtoMenu.getId()) {
            btnChuyenLoaiCtoMenu.setBackground(drawable11);
        }
    }

    public void refreshTagTopMenu(TthtHnMainActivity.TagMenuTop tagMenuTop) {
        this.tagMenuTop = tagMenuTop;
    }

    public interface IOnTthtHnTopMenuChiTietCtoFragment {
        void clickTopMenuChitietCto(TthtHnMainActivity.TagMenuTop tagMenuTop);
    }
}
