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

import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_ID_BBAN_TRTH;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_ID_BBAN_TUTI;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_MA_BDONG;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TYPE_TOPMENU;

public class TthtHnTopMenuChiTietCtoFragment extends TthtHnBaseFragment {

    private IOnTthtHnTopMenuChiTietCtoFragment mListener;

    private Drawable drawable10, drawable11;

    private Unbinder unbinder;


    //menu top
    @BindView(R.id.btn_cto_menu)
    Button btnCtoMenu;

    @BindView(R.id.btn_bban_tuti_menu)
    Button btnBBTuTiMenu;

    @BindView(R.id.btn_chuyen_loaicto_menu)
    Button btnChuyenLoaiCtoMenu;


    private int ID_BBAN_TUTI;
    private int ID_BBAN_TRTH;
    private Common.MA_BDONG maBdong;


    public TthtHnTopMenuChiTietCtoFragment() {
        // Required empty public constructor
    }

    public static TthtHnTopMenuChiTietCtoFragment newInstance(Bundle bundle) {
        TthtHnTopMenuChiTietCtoFragment fragment = new TthtHnTopMenuChiTietCtoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            ID_BBAN_TRTH = bundle.getInt(BUNDLE_ID_BBAN_TRTH, 0);
            ID_BBAN_TUTI = bundle.getInt(BUNDLE_ID_BBAN_TUTI, 0);
            maBdong = (Common.MA_BDONG) bundle.getSerializable(BUNDLE_MA_BDONG);
        }
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
        if (context instanceof IOnTthtHnTopMenuChiTietCtoFragment) {
            mListener = (IOnTthtHnTopMenuChiTietCtoFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnTopMenuChiTietCtoFragment");
        }

        drawable10 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle10);
        drawable11 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle11);

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
    void initDataAndView(View viewRoot) throws Exception {

        //show visible Topmenu
        showTopMenu(btnCtoMenu);
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
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
        if (ID_BBAN_TUTI == 0)
        {
            btnBBTuTiMenu.setVisibility(View.GONE);
        }
    }

    private void catchClickTopMenu() {
        btnCtoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setBackgroundTopMenu(view);


                    //clickTopMenuButton
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_TYPE_TOPMENU, TthtHnMainActivity.TagMenuTop.CHITIET_CTO);
                    bundle.putSerializable(BUNDLE_MA_BDONG, maBdong);
                    bundle.putInt(BUNDLE_ID_BBAN_TRTH, ID_BBAN_TRTH);
                    mListener.clickTopMenuButton(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChuyenLoaiCtoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundTopMenu(view);


                //clickTopMenuButton
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_TYPE_TOPMENU, TthtHnMainActivity.TagMenuTop.CHUYEN_LOAI_CTO);
                bundle.putSerializable(BUNDLE_MA_BDONG, maBdong);
                bundle.putInt(BUNDLE_ID_BBAN_TRTH, ID_BBAN_TRTH);
                mListener.clickTopMenuButton(bundle);
            }
        });

        btnBBTuTiMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setBackgroundTopMenu(view);


                    //clickTopMenuButton
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_TYPE_TOPMENU, TthtHnMainActivity.TagMenuTop.BBAN_TUTI);
                    bundle.putSerializable(BUNDLE_MA_BDONG, maBdong);
                    bundle.putInt(BUNDLE_ID_BBAN_TRTH, ID_BBAN_TRTH);
                    bundle.putInt(BUNDLE_ID_BBAN_TUTI, ID_BBAN_TUTI);
                    mListener.clickTopMenuButton(bundle);
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

    public interface IOnTthtHnTopMenuChiTietCtoFragment {
        void clickTopMenuButton(Bundle bundle);
    }
}
