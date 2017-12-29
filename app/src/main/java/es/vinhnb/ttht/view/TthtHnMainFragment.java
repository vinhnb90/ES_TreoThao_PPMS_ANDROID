package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import es.vinhnb.ttht.entity.sharedpref.MainSharePref;
import es.vinhnb.ttht.entity.sharedpref.MenuTopSearchSharePref;
import es.vinhnb.ttht.view.TthtHnMainActivity.TagMenuNaviLeft;
import esolutions.com.esdatabaselib.baseSharedPref.SharePrefManager;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.adapter.ChiTietCtoAdapter.*;
import static es.vinhnb.ttht.common.Common.TYPE_SEARCH_BBAN.NGAY_TRTH;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_TAG_MENU;

public class TthtHnMainFragment extends TthtHnBaseFragment {

    private TagMenuNaviLeft tagMenuNaviLeft;
    private IInteractionDataCommon onIDataCommon;

    private IOnTthtHnMainFragment mListener;
    private TthtHnSQLDAO mSqlDAO;

    private RecyclerView mRvMain;
    private TextView tvNodata;
    private ChiTietCtoAdapter treoCtoAdapter;
    private List<DataBBanAdapter> dataBBanAdaptersList = new ArrayList<>();
    private List<DataChiTietCtoAdapter> dataChiTietCtoAdaptersList = new ArrayList<>();
    private List<DataChungLoaiAdapter> dataCloaiAdapterList = new ArrayList<>();
    private List<DataTramAdapter> dataTramAdapterList = new ArrayList<>();

    //save data cho fragment bien ban
    private int posRecylerClick = -1;
    private int sizeList = 0;

    private SharePrefManager sharePrefManager;

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
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        //interface
        if (context instanceof IOnTthtHnMainFragment) {
            mListener = (IOnTthtHnMainFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnMainFragment");
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
        try {
            if (tagMenuNaviLeft == TagMenuNaviLeft.CTO_TREO || tagMenuNaviLeft == TagMenuNaviLeft.CTO_THAO) {
                MainSharePref mainSharePref = (MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class);
                posRecylerClick = mainSharePref.posClicked;
                sizeList = mainSharePref.sizeList;
            } else {
                posRecylerClick = -1;
                sizeList = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public TthtHnMainFragment setTagMenuNaviLeft(TagMenuNaviLeft tagMenuNaviLeft) {
        this.tagMenuNaviLeft = tagMenuNaviLeft;
        return this;
    }

    public TagMenuNaviLeft getTagMenuNaviLeft() {
        return tagMenuNaviLeft;
    }


    private void fillDataChungLoai(List<DataChungLoaiAdapter> dataChungLoai) {
        if (isShowNoDataText(dataChungLoai.size()))
            return;

        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            ((ChungLoaiAdapter) mRvMain.getAdapter()).refresh(dataChungLoai);
        } else {
            ChungLoaiAdapter chungLoaiAdapter = new ChungLoaiAdapter(getContext(), dataChungLoai);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(chungLoaiAdapter, true);
        }

        mRvMain.invalidate();

        mListener.refreshTopThongKeMainFragment(dataCloaiAdapterList.size(), dataChungLoai.size());
    }


    private void fillDataCto(List<DataChiTietCtoAdapter> dataTreoChiTietCto) {
        if (isShowNoDataText(dataTreoChiTietCto.size()))
            return;

        if (mRvMain.getAdapter() instanceof ChiTietCtoAdapter) {
            ((ChiTietCtoAdapter) mRvMain.getAdapter()).refresh(dataTreoChiTietCto);
        } else {
            ChiTietCtoAdapter treoCtoAdapter = new ChiTietCtoAdapter(getContext(), dataTreoChiTietCto);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(treoCtoAdapter, true);
        }

        mRvMain.invalidate();

        mListener.refreshTopThongKeMainFragment(dataChiTietCtoAdaptersList.size(), dataTreoChiTietCto.size());
    }

    private void fillDataTram(List<DataTramAdapter> dataTram) {
        if (isShowNoDataText(dataTram.size()))
            return;

        if (mRvMain.getAdapter() instanceof TramAdapter) {
            ((TramAdapter) mRvMain.getAdapter()).refresh(dataTram);
        } else {
            TramAdapter tramAdapter = new TramAdapter(getContext(), dataTram);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(tramAdapter, true);
        }

        mRvMain.invalidate();

        mListener.refreshTopThongKeMainFragment(dataTramAdapterList.size(), dataTram.size());
    }

    private void fillDataBBanCto(List<DataBBanAdapter> dataBBanAdapters) {
        if (isShowNoDataText(dataBBanAdapters.size()))
            return;


        if (mRvMain.getAdapter() instanceof BBanAdapter) {
            ((BBanAdapter) mRvMain.getAdapter()).refresh(dataBBanAdapters);
        } else {
            BBanAdapter bBanAdapter = new BBanAdapter(getContext(), dataBBanAdapters);
            mRvMain.removeAllViews();
            mRvMain.invalidate();
            mRvMain.swapAdapter(bBanAdapter, true);
        }
        mRvMain.invalidate();

        mListener.refreshTopThongKeMainFragment(dataBBanAdaptersList.size(), dataBBanAdapters.size());
    }

    private boolean isShowNoDataText(int size) {
        mRvMain.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        tvNodata.setVisibility(size == 0 ? View.VISIBLE : View.GONE);

        if (size == 0)
            return true;
        else
            return false;
    }


    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View viewRoot) throws Exception {
        //share pref
        sharePrefManager = SharePrefManager.getInstance();

        //set Layout mRvMain
        mRvMain = (RecyclerView) viewRoot.findViewById(R.id.rv_tththn_main);
        tvNodata = (TextView) viewRoot.findViewById(R.id.tv_nodata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvMain.setLayoutManager(layoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), R.drawable.ttht_hn_divider);
//        dividerItemDecoration.setOrientation(DividerItemDecoration.VERTICAL);
//        mRvMain.addItemDecoration(dividerItemDecoration);
//        mRvMain.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //fill data recycler
        switch (tagMenuNaviLeft) {
            case BBAN_CTO:
                //get Data and apdater
                String[] agrs = new String[]{onIDataCommon.getMaNVien()};
                dataBBanAdaptersList = null;
                dataBBanAdaptersList = mSqlDAO.getBBanAdapter2Day(agrs);


                fillDataBBanCto(dataBBanAdaptersList);
                break;

            case CTO_TREO:
            case CTO_THAO:
                //get share pref
                //nếu ở lần đầu thì set MA_BDONG
                //nếu ở lần sau chuyển cto thì lấy dữ liệu liệu ở sharepref

                MainSharePref mainSharePref = (MainSharePref) sharePrefManager.getSharePrefObject(MainSharePref.class);
                if (!TextUtils.isEmpty(mainSharePref.tagMenuNaviLeft)) {
                    this.tagMenuNaviLeft = TagMenuNaviLeft.findTagMenu(mainSharePref.tagMenuNaviLeft);
                }
                onIDataCommon.setMA_BDONG(tagMenuNaviLeft == TagMenuNaviLeft.CTO_TREO ? Common.MA_BDONG.B : Common.MA_BDONG.E);


                MenuTopSearchSharePref menuTopSearchSharePref = (MenuTopSearchSharePref) sharePrefManager.getSharePrefObject(MenuTopSearchSharePref.class);
                String typeSearchString = menuTopSearchSharePref.typeSearchString;
                String messageSearch = menuTopSearchSharePref.messageSearch;

                //get Data and apdater
                tagMenuNaviLeft = onIDataCommon.getMA_BDONG() == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO : TagMenuNaviLeft.CTO_THAO;

                String[] agrsCto = new String[]{onIDataCommon.getMaNVien(), onIDataCommon.getMA_BDONG().code};
                dataChiTietCtoAdaptersList = null;
                dataChiTietCtoAdaptersList = mSqlDAO.getTreoDataChiTietCto2DayAdapter(agrsCto);

                if (!TextUtils.isEmpty(typeSearchString)) {
                    //nếu có store data fragment thì restore
                    searchCto(typeSearchString, messageSearch);

                    if (posRecylerClick != -1) {
                        mRvMain.scrollToPosition(posRecylerClick);
                        mRvMain.postInvalidate();
                    }
                } else {
                    mListener.refreshTopThongKeMainFragment(dataChiTietCtoAdaptersList.size(), 0);
                    fillDataCto(dataChiTietCtoAdaptersList);
                }

                break;

            case CHUNG_LOAI:
                //get Data and apdater
                agrs = new String[]{};
                dataCloaiAdapterList = null;
                dataCloaiAdapterList = mSqlDAO.getCloaiAdapter(agrs);
                fillDataChungLoai(dataCloaiAdapterList);
                break;

            case TRAM:
                agrs = new String[]{};
                dataTramAdapterList = null;
                dataTramAdapterList = mSqlDAO.getTramAdapter(agrs);
                fillDataTram(dataTramAdapterList);
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


    @Deprecated
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
                break;
            case CTO_TREO:
            case CTO_THAO:
                searchCto(typeSearchString, messageSearch);
                break;
            case CHUNG_LOAI:
                searchChungLoai(typeSearchString, messageSearch);
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


    private void searchChungLoai(String typeSearchString, String messageSearch) {
        Common.TYPE_SEARCH_CLOAI typeSearch = Common.TYPE_SEARCH_CLOAI.findTYPE_SEARCH(typeSearchString);
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        List<DataChungLoaiAdapter> dataFilter = new ArrayList<>();


        if (!TextUtils.isEmpty(messageSearch)) {
            for (int i = 0; i < dataCloaiAdapterList.size(); i++) {
                boolean isHasData = true;
                DataChungLoaiAdapter data = dataCloaiAdapterList.get(i);

                switch (typeSearch) {
                    case TEN_LOAI_CTO:
                        isHasData = Common.removeAccent(data.getTenLoaiCto().toLowerCase()).contains(query);
                        break;
                    case VH_CONG:
                        isHasData = Common.removeAccent(data.getVhCong().toLowerCase()).contains(query);
                        break;
                    case MA_HANG:
                        isHasData = Common.removeAccent(data.getMaHang().toLowerCase()).contains(query);
                        break;
                    case TEN_NUOC:
                        isHasData = Common.removeAccent(data.getTenNuoc().toLowerCase()).contains(query);
                        break;
                }
                if (isHasData) {
                    dataFilter.add(data);
                }
            }
        } else
            dataFilter = Common.cloneList(dataCloaiAdapterList);


        //giữ nguyên dữ liệu, lọc cái cần dùng
        fillDataChungLoai(dataFilter);
    }

    private void searchCto(String typeSearchString, String messageSearch) {


        Common.TYPE_SEARCH_CTO typeSearch = Common.TYPE_SEARCH_CTO.findTYPE_SEARCH(typeSearchString);
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        List<DataChiTietCtoAdapter> dataFilter = new ArrayList<>();


        if (!TextUtils.isEmpty(messageSearch)) {

            if (typeSearch == Common.TYPE_SEARCH_CTO.NGAY_TRTH) {
                String convertSqlDate = Common.convertDateToDate(query, Common.DATE_TIME_TYPE.type6, Common.DATE_TIME_TYPE.sqlite2);

                dataChiTietCtoAdaptersList.clear();
                String dateNow = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type6);
                if (messageSearch.equals(dateNow)) {
                    String[] args = new String[]{onIDataCommon.getMaNVien(), onIDataCommon.getMA_BDONG().code};
                    dataChiTietCtoAdaptersList = mSqlDAO.getTreoDataChiTietCto2DayAdapter(args);
                } else {
                    String[] args = new String[]{onIDataCommon.getMaNVien(), convertSqlDate, onIDataCommon.getMA_BDONG().code};
                    dataChiTietCtoAdaptersList = mSqlDAO.getTreoDataChiTietCtoAdapter(args);
                }
                dataFilter = dataChiTietCtoAdaptersList;
            } else {
                for (int i = 0; i < dataChiTietCtoAdaptersList.size(); i++) {
                    boolean isHasData = true;
                    DataChiTietCtoAdapter data = dataChiTietCtoAdaptersList.get(i);

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
            }
        } else
            dataFilter = Common.cloneList(dataChiTietCtoAdaptersList);


        //giữ nguyên dữ liệu, lọc cái cần dùng
        mListener.refreshTopThongKeMainFragment(dataFilter.size(), 0);
        fillDataCto(dataFilter);
    }

    private void searchBBan(String typeSearchString, String messageSearch) {
        Common.TYPE_SEARCH_BBAN typeSearch = Common.TYPE_SEARCH_BBAN.findTYPE_SEARCH(typeSearchString);
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        List<DataBBanAdapter> dataFilter = new ArrayList<>();


        if (!TextUtils.isEmpty(messageSearch)) {
            if (typeSearch == NGAY_TRTH) {
                String convertSqlDate = Common.convertDateToDate(query, Common.DATE_TIME_TYPE.type6, Common.DATE_TIME_TYPE.sqlite2);

                dataBBanAdaptersList.clear();
                String dateNow = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type6);
                if (messageSearch.equals(dateNow)) {
                    String[] args = new String[]{onIDataCommon.getMaNVien()};
                    dataBBanAdaptersList = mSqlDAO.getBBanAdapter2Day(args);
                } else {
                    String[] args = new String[]{onIDataCommon.getMaNVien(), convertSqlDate};
                    dataBBanAdaptersList = mSqlDAO.getBBanAdapterInDay(args);
                }
                dataFilter = dataBBanAdaptersList;
                mListener.refreshTopThongKeMainFragment(dataBBanAdaptersList.size(), 0);
            } else {
                for (int i = 0; i < dataBBanAdaptersList.size(); i++) {
                    boolean isHasData = true;
                    DataBBanAdapter data = dataBBanAdaptersList.get(i);
                    switch (typeSearch) {
                        case MA_TRAM:
                            isHasData = Common.removeAccent(data.getMaTramcapdien().toLowerCase()).contains(query);
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
                        case BBAN_ERROR:
                            isHasData = Common.removeAccent(data.getTRANG_THAI_DU_LIEU().content.toLowerCase()).contains(query);
                            break;
                        case NGAY_TRTH:
                            break;
                    }
                    if (isHasData) {
                        dataFilter.add(data);
                    }
                }
            }
        } else
            dataFilter = Common.cloneList(dataBBanAdaptersList);


        //giữ nguyên dữ liệu, lọc cái cần dùng
        fillDataBBanCto(dataFilter);
    }


    public void savePosClick(int pos, int sizeFitle) throws Exception {
        if (pos < 0 || pos >= sizeFitle)
            throw new Exception("Không còn công tơ!");

        this.posRecylerClick = pos;
        this.sizeList = sizeFitle;
        MainSharePref mainSharePref = new MainSharePref(pos, sizeFitle, onIDataCommon.getMA_BDONG() == Common.MA_BDONG.B ? TagMenuNaviLeft.CTO_TREO.tagFrag : TagMenuNaviLeft.CTO_THAO.tagFrag);
        sharePrefManager.writeDataSharePref(MainSharePref.class, mainSharePref);
    }

    public void searchDate(String date) {
        switch (tagMenuNaviLeft) {
            case BBAN_CTO:
                searchBBan(NGAY_TRTH.content, date);
                break;
            case TRAM:
                break;
            case CTO_TREO:
            case CTO_THAO:
                searchCto(NGAY_TRTH.content, date);
                break;
            case CHUNG_LOAI:
                searchChungLoai(NGAY_TRTH.content, date);
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


    public interface IOnTthtHnMainFragment {

        void refreshTopThongKeMainFragment(int countRow, int thongKe);

    }
}

//endregion

