package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.adapter.BBanAdapter;
import es.vinhnb.ttht.adapter.BBanAdapter.DataBBanAdapter;
import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.ChungLoaiAdapter;
import es.vinhnb.ttht.adapter.TramAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.view.TthtHnMainActivity.TagMenu;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;


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

    public static TthtHnMainFragment newInstance(Bundle bundle) {
        TthtHnMainFragment fragment = new TthtHnMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //getBundle
            Bundle bundle = getArguments();
            mLoginData = (LoginFragment.LoginData) bundle.getParcelable(TthtHnLoginActivity.BUNDLE_LOGIN);
            mMaNVien = bundle.getString(TthtHnLoginActivity.BUNDLE_MA_NVIEN);
            tagMenu = (TagMenu) bundle.getSerializable(TthtHnLoginActivity.BUNDLE_TAG_MENU);
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
        //get Data and apdater
        String[] agrs = new String[]{};
        List<ChungLoaiAdapter.DataChungLoaiAdapter> dataCloaiAdapterList = mSqlDAO.getCloaiAdapter(agrs);


        if (mRvMain.getAdapter() instanceof ChungLoaiAdapter) {
            ((ChungLoaiAdapter) mRvMain.getAdapter()).refresh(dataCloaiAdapterList);
        } else {
            ChungLoaiAdapter chungLoaiAdapter = new ChungLoaiAdapter(getContext(), dataCloaiAdapterList);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(chungLoaiAdapter, true);
        }


        mRvMain.invalidate();
    }


    private void fillDataCto(Common.MA_BDONG maBdong) {
        //get Data and apdater
        String[] agrs = new String[]{maBdong.code};
        List<ChiTietCtoAdapter.DataChiTietCtoAdapter> dataTreoChiTietCtoAdapters = mSqlDAO.getTreoDataChiTietCtoAdapter(agrs);


        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            ((ChiTietCtoAdapter) mRvMain.getAdapter()).refresh(dataTreoChiTietCtoAdapters);
        } else {
            ChiTietCtoAdapter treoCtoAdapter = new ChiTietCtoAdapter(getContext(), maBdong, dataTreoChiTietCtoAdapters);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(treoCtoAdapter, true);
        }


        mRvMain.invalidate();
    }

    private void fillDataTram() {
        //get Data and apdater
        String[] agrs = new String[]{};
        List<TramAdapter.DataTramAdapter> dataTramAdapterList = mSqlDAO.getTramAdapter(agrs);


        if (mRvMain.getAdapter() instanceof TramAdapter) {
            ((TramAdapter) mRvMain.getAdapter()).refresh(dataTramAdapterList);
        } else {
            TramAdapter tramAdapter = new TramAdapter(getContext(), dataTramAdapterList);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(tramAdapter, true);
        }


        mRvMain.invalidate();
    }

    private void fillDataBBanCto() {
        //get Data and apdater
        String[] agrs = new String[]{};
        List<DataBBanAdapter> dataBBanAdapters = mSqlDAO.getBBanAdapter(agrs);


        if (mRvMain.getAdapter() instanceof BBanAdapter) {
            ((BBanAdapter) mRvMain.getAdapter()).refresh(dataBBanAdapters);
        } else {
            BBanAdapter bBanAdapter = new BBanAdapter(getContext(), dataBBanAdapters);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(bBanAdapter, true);
        }


        mRvMain.invalidate();
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
                fillDataBBanCto();
                break;

            case CTO_TREO:

                fillDataCto(Common.MA_BDONG.B);
                break;

            case CTO_THAO:
                fillDataCto(Common.MA_BDONG.E);
                break;

            case CHUNG_LOAI:
                fillDataChungLoai();
                break;

            case TRAM:
                fillDataTram();
                break;

        }
    }
    //endregion

    public interface OnListenerTthtHnMainFragment {
    }
}
