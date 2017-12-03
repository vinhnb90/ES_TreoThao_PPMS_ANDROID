package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.adapter.BBanAdapter;
import es.vinhnb.ttht.adapter.BBanAdapter.DataBBanAdapter;
import es.vinhnb.ttht.adapter.ChiTietCtoAdapter;
import es.vinhnb.ttht.adapter.ChungLoaiAdapter;
import es.vinhnb.ttht.adapter.TramAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TAG_MENU;

public class TthtHnMainFragment extends TthtHnBaseFragment {

    private TagMenuNaviLeft tagMenuNaviLeft;
    private IInteractionDataCommon onIDataCommon;

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

    public static TthtHnMainFragment newInstance(TagMenuNaviLeft tagMenuNaviLeft) {
        TthtHnMainFragment fragment = new TthtHnMainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TAG_MENU, tagMenuNaviLeft);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getBundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagMenuNaviLeft = (TagMenuNaviLeft) bundle.getSerializable(BUNDLE_TAG_MENU);
        }


        //get onIDataCommon
        if (getContext() instanceof IInteractionDataCommon)
            this.onIDataCommon = (IInteractionDataCommon) getContext();
        else
            throw new ClassCastException("context must be implemnet IInteractionDataCommon!");
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

    public TthtHnMainFragment switchMenu(TagMenuNaviLeft tagMenuNaviLeft) {
        this.tagMenuNaviLeft = tagMenuNaviLeft;
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


    private void fillDataCto() {

        //get Data and apdater
        String[] agrs = new String[]{onIDataCommon.getMA_BDONG().code};
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
    public void initDataAndView(View viewRoot) throws Exception {
        mRvMain = (RecyclerView) viewRoot.findViewById(R.id.rv_tththn_main);


        //set Layout mRvMain
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvMain.setLayoutManager(layoutManager);
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //fill data recycler
        switch (tagMenuNaviLeft) {
            case BBAN_CTO:
                fillDataBBanCto();
                break;

            case CTO_TREO:
            case CTO_THAO:
                fillDataCto();

            case CHUNG_LOAI:
                fillDataChungLoai();
                break;

            case TRAM:
                fillDataTram();
                break;

        }
    }

    public void refreshNextCto(int posOld) throws Exception {
        if (posOld == mRvMain.getAdapter().getItemCount())
            throw new Exception("Không còn công tơ!");


        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            int posNew = ((ChiTietCtoAdapter) mRvMain.getAdapter()).refreshNextPos(posOld);
            if (posNew != posOld) ;
            {
                mRvMain.getLayoutManager().scrollToPosition(posNew);
            }
        }
    }

    public void refreshPreCto(int posOld)  throws Exception {
        if (posOld == mRvMain.getAdapter().getItemCount())
            throw new Exception("Không còn công tơ!");


        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            int posNew = ((ChiTietCtoAdapter) mRvMain.getAdapter()).refreshPrePos(posOld);
            if (posNew != posOld) ;
            {
                mRvMain.getLayoutManager().scrollToPosition(posNew);
            }
        }
    }
    //endregion

    public interface OnListenerTthtHnMainFragment {
    }
}
