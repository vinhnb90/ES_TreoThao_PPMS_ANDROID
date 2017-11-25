package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.HistoryAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.view.TthtHnMainActivity.*;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenu.*;
import static es.vinhnb.ttht.view.TthtHnMainActivity.TagMenu.CTO_TREO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListenerTthtHnMainFragment} interface
 * to handle interaction events.
 * Use the {@link TthtHnMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TthtHnMainFragment extends TthtHnBaseFragment {

    private LoginFragment.LoginData mLoginData;
    private String mMaNVien;
    private TagMenu tagMenu;

    private List<TABLE_BBAN_CTO> listTABLE_BBAN_CTO = new ArrayList<>();
    private List<TABLE_TRAM> listTABLE_TRAM = new ArrayList<>();
    private List<TABLE_CHITIET_CTO> listTreoTABLE_CHITIET_CTO = new ArrayList<>();
    private List<TABLE_CHITIET_CTO> listThaoTABLE_CHITIET_CTO = new ArrayList<>();
    private List<TABLE_LOAI_CONG_TO> listTABLE_LOAI_CONG_TO = new ArrayList<>();

    private OnListenerTthtHnMainFragment mListener;
    private TthtHnSQLDAO mSqlDAO;

    private RecyclerView mRvMain;
    private ChiTietCtoAdapter treoCtoAdapter;

    public TthtHnMainFragment() {
        // Required empty public constructor
    }

    public static TthtHnMainFragment newInstance(LoginFragment.LoginData param1, String param2, TagMenu tagNew) {
        TthtHnMainFragment fragment = new TthtHnMainFragment();
        Bundle args = new Bundle();
        args.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, param1);
        args.putString(TthtHnLoginActivity.MA_NVIEN, param2);
        args.putSerializable(TthtHnLoginActivity.TAG_MENU, tagNew);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //getBundle
            mLoginData = (LoginFragment.LoginData) getArguments().getParcelable(TthtHnLoginActivity.BUNDLE_LOGIN);
            mMaNVien = getArguments().getString(TthtHnLoginActivity.MA_NVIEN);
            tagMenu = (TagMenu) getArguments().getSerializable(TthtHnLoginActivity.TAG_MENU);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_main, container, false);
        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        //interface
        if (context instanceof OnListenerTthtHnMainFragment) {
            mListener = (OnListenerTthtHnMainFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerTthtHnMainFragment");
        }


        //call Database access objectS
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

    public TthtHnMainFragment switchMenu(TagMenu tagMenu) {
        this.tagMenu = tagMenu;
        return this;
    }

    private void fillDataChungLoai() {
    }

    private void fillDataCtoThao() {
        //        listTreoTABLE_CHITIET_CTO = mSqlDAO.selectAllLazy(TABLE_CHITIET_CTO.class);
        //get Data and apdater
        String[] agrs = new String[]{};
        List<ChiTietCtoAdapter.DataChiTietCtoAdapter> dataThaoChiTietCtoAdapters = mSqlDAO.getThaoDataChiTietCtoAdapter(agrs);


        //check apdater and refresh or swap
        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            ((ChiTietCtoAdapter) mRvMain.getAdapter()).refresh(dataThaoChiTietCtoAdapters);
        } else {
            ChiTietCtoAdapter thaoCtoAdapter = new ChiTietCtoAdapter(getContext(), dataThaoChiTietCtoAdapters);


            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(thaoCtoAdapter, true);
        }


        mRvMain.invalidate();
    }


    private void fillDataCtoTreo() {
        //get Data and apdater
        String[] agrs = new String[]{Common.MA_BDONG.B.code};
        List<ChiTietCtoAdapter.DataChiTietCtoAdapter> dataTreoChiTietCtoAdapters = mSqlDAO.getTreoDataChiTietCtoAdapter(agrs);


        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            ((ChiTietCtoAdapter) mRvMain.getAdapter()).refresh(dataTreoChiTietCtoAdapters);
        } else {
            ChiTietCtoAdapter treoCtoAdapter = new ChiTietCtoAdapter(getContext(), dataTreoChiTietCtoAdapters);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(treoCtoAdapter, true);
        }


        mRvMain.invalidate();
    }

    private void fillDataTram() {
    }

    private void fillDataBBanCto() {

    }

    //region TthtHnBaseFragment
    @Override
    void initDataAndView(View viewRoot) throws Exception {
        mRvMain = (RecyclerView) viewRoot.findViewById(R.id.rv_tththn_main);


        //set Layout mRvMain
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvMain.setLayoutManager(layoutManager);
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //fill data recycler
        switch (tagMenu) {
            case BBAN_CTO:

                break;

            case CTO_TREO:

                fillDataCtoTreo();
                break;

            case CTO_THAO:

                break;

            case CHUNG_LOAI:

                break;

            case TRAM:

                break;

        }
    }
    //endregion

    public interface OnListenerTthtHnMainFragment {
    }
}
