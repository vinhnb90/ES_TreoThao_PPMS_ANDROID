package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import es.vinhnb.ttht.adapter.ChungLoaiAdapter.DataChungLoaiAdapter;
import es.vinhnb.ttht.adapter.TramAdapter;
import es.vinhnb.ttht.adapter.TramAdapter.DataTramAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.adapter.ChiTietCtoAdapter.*;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_ID_BBAN_TRTH;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_MESSAGE_SEARCH;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_POS;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TAG_MENU;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TYPE_SEARCH_STORE;

public class TthtHnMainFragment extends TthtHnBaseFragment {

    private TagMenuNaviLeft tagMenuNaviLeft;
    private IInteractionDataCommon onIDataCommon;

    private OnListenerTthtHnMainFragment mListener;
    private TthtHnSQLDAO mSqlDAO;

    private RecyclerView mRvMain;
    private ChiTietCtoAdapter treoCtoAdapter;
    private List<DataBBanAdapter> dataBBanAdapters = new ArrayList<>();
    private List<DataChiTietCtoAdapter> dataChiTietCtoAdapters = new ArrayList<>();
    private List<DataChungLoaiAdapter> dataCloaiAdapterList = new ArrayList<>();
    private List<DataTramAdapter> dataTramAdapterList = new ArrayList<>();

    //save data cho fragment bien ban
    private String typeSearchStringCto;
    private String messageSearchCto;
    private int posRecylerClick = -1;

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
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            tagMenuNaviLeft = (TagMenuNaviLeft) savedInstanceState.getSerializable(BUNDLE_MESSAGE_SEARCH);
            typeSearchStringCto = savedInstanceState.getString(BUNDLE_TYPE_SEARCH_STORE);
            messageSearchCto = savedInstanceState.getString(BUNDLE_MESSAGE_SEARCH);
            posRecylerClick = savedInstanceState.getInt(BUNDLE_POS);
        } else {
            tagMenuNaviLeft = TagMenuNaviLeft.BBAN_CTO;
            typeSearchStringCto = null;
            messageSearchCto = null;
            posRecylerClick = -1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        dataCloaiAdapterList = mSqlDAO.getCloaiAdapter(agrs);


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


    private void fillDataCto(List<DataChiTietCtoAdapter> dataTreoChiTietCtoAdapters) {

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
        dataTramAdapterList = mSqlDAO.getTramAdapter(agrs);


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

    private void fillDataBBanCto(List<DataBBanAdapter> dataBBanAdapters) {

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
                //get Data and apdater
                String[] agrs = new String[]{};
                dataBBanAdapters = mSqlDAO.getBBanAdapter(agrs);


                fillDataBBanCto(dataBBanAdapters);
                break;

            case CTO_TREO:
            case CTO_THAO:
                //set MA_BDONG
                onIDataCommon.setMA_BDONG(tagMenuNaviLeft == TagMenuNaviLeft.CTO_TREO ? Common.MA_BDONG.B : Common.MA_BDONG.E);


                //get Data and apdater
                String[] agrsCto = new String[]{onIDataCommon.getMA_BDONG().code};
                dataChiTietCtoAdapters = mSqlDAO.getTreoDataChiTietCtoAdapter(agrsCto);


                //nếu có store data fragment thì restore
                if (isStateSaved()) {
                    searchCto(typeSearchStringCto, messageSearchCto);
                    mRvMain.scrollToPosition(posRecylerClick);
                    mRvMain.postInvalidate();
                } else
                    fillDataCto(dataChiTietCtoAdapters);


                break;

            case CHUNG_LOAI:
                fillDataChungLoai();
                break;

            case TRAM:
                fillDataTram();
                break;

        }
    }

    public int refreshNextCto(int posOld) throws Exception {
        if (posOld == mRvMain.getAdapter().getItemCount())
            throw new Exception("Không còn công tơ!");

        int posNew = 0;

        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            posNew = ((ChiTietCtoAdapter) mRvMain.getAdapter()).refreshNextPos(posOld);
            if (posNew != posOld) ;
            {
                mRvMain.getLayoutManager().scrollToPosition(posNew);
            }
        }

        return posNew;
    }

    public int refreshPreCto(int posOld) throws Exception {
        if (posOld == mRvMain.getAdapter().getItemCount())
            throw new Exception("Không còn công tơ!");


        int posNew = 0;
        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            posNew = ((ChiTietCtoAdapter) mRvMain.getAdapter()).refreshPrePos(posOld);
            if (posNew != posOld) ;
            {
                mRvMain.getLayoutManager().scrollToPosition(posNew);
            }
        }

        return posNew;
    }

    public void searchData(String typeSearchString, String messageSearch) {
        switch (tagMenuNaviLeft) {
            case BBAN_CTO:
                searchBBan(typeSearchString, messageSearch);
                break;
            case TRAM:
                searchCto(typeSearchString, messageSearch);
                break;
            case CTO_TREO:
                break;
            case CTO_THAO:
                break;
            case CHUNG_LOAI:
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
            case DOWNLOAD:
                break;
            case UPLOAD:
                break;
            case HISTORY:
                break;
        }
    }

    private void searchCto(String typeSearchString, String messageSearch) {
        this.typeSearchStringCto = typeSearchString;
        this.messageSearchCto = messageSearch;


        Common.TYPE_SEARCH_CTO typeSearch = Common.TYPE_SEARCH_CTO.findTYPE_SEARCH(typeSearchString);
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        List<DataChiTietCtoAdapter> dataFilter = new ArrayList<>();


        if (!TextUtils.isEmpty(messageSearch)) {
            for (int i = 0; i < dataChiTietCtoAdapters.size(); i++) {
                boolean isHasData = true;
                DataChiTietCtoAdapter data = dataChiTietCtoAdapters.get(i);

                switch (typeSearch) {
                    case MA_TRAM:
                        isHasData = Common.removeAccent(data.getMaTram().toLowerCase()).contains(query);
                        break;
                    case NGAY_TRTH:
                        isHasData = Common.removeAccent(data.getNgaytrth().toLowerCase()).contains(query);
                        break;
                    case TEN_KH:
                        isHasData = Common.removeAccent(data.getTenKH().toLowerCase()).contains(query);
                        break;
                    case MA_GCS:
                        isHasData = Common.removeAccent(data.getMaGCS().toLowerCase()).contains(query);
                        break;
                    case SO_BBAN:
                        isHasData = Common.removeAccent(data.getSobban().toLowerCase()).contains(query);
                        break;
                    case MA_CTO:
                        isHasData = Common.removeAccent(data.getMaCto().toLowerCase()).contains(query);
                        break;
                    case SO_CTO:
                        isHasData = Common.removeAccent(data.getSoCto().toLowerCase()).contains(query);
                        break;
                }
                if (isHasData) {
                    dataFilter.add(data);
                }
            }
        } else
            dataFilter = Common.cloneList(dataChiTietCtoAdapters);


        //giữ nguyên dữ liệu, lọc cái cần dùng
        fillDataCto(dataFilter);
    }

    private void searchBBan(String typeSearchString, String messageSearch) {
        Common.TYPE_SEARCH_BBAN typeSearch = Common.TYPE_SEARCH_BBAN.findTYPE_SEARCH(typeSearchString);
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        List<DataBBanAdapter> dataFilter = new ArrayList<>();


        if (!TextUtils.isEmpty(messageSearch)) {
            for (int i = 0; i < dataBBanAdapters.size(); i++) {
                boolean isHasData = true;
                DataBBanAdapter data = dataBBanAdapters.get(i);
                switch (typeSearch) {
                    case MA_TRAM:
                        isHasData = Common.removeAccent(data.getMaTramcapdien().toLowerCase()).contains(query);
                        break;
                    case NGAY_TRTH:
                        isHasData = Common.removeAccent(data.getNgayTrth().toLowerCase()).contains(query);
                        break;
                    case TEN_KH:
                        isHasData = Common.removeAccent(data.getTenKH().toLowerCase()).contains(query);
                        break;
                    case MA_GCS:
                        isHasData = Common.removeAccent(data.getMaGCS().toLowerCase()).contains(query);
                        break;
                    case SO_BBAN:
                        isHasData = Common.removeAccent(data.getSobban().toLowerCase()).contains(query);
                        break;
                }
                if (isHasData) {
                    dataFilter.add(data);
                }
            }
        } else
            dataFilter = Common.cloneList(dataBBanAdapters);


        //giữ nguyên dữ liệu, lọc cái cần dùng
        fillDataBBanCto(dataFilter);
    }

    public void savePosClick(int pos) {
        this.posRecylerClick = pos;
        //save
        Bundle outState = new Bundle();
        if (tagMenuNaviLeft == TagMenuNaviLeft.CTO_TREO || tagMenuNaviLeft == TagMenuNaviLeft.CTO_THAO) {
            outState.putString(BUNDLE_TYPE_SEARCH_STORE, typeSearchStringCto);
            outState.putString(BUNDLE_MESSAGE_SEARCH, messageSearchCto);
            outState.putSerializable(BUNDLE_TAG_MENU, tagMenuNaviLeft);
            outState.putInt(BUNDLE_POS, posRecylerClick);
        }
        onSaveInstanceState(outState);
    }


    public interface OnListenerTthtHnMainFragment {
    }
}

//endregion

