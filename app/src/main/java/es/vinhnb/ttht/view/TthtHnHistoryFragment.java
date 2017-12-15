package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.adapter.HistoryAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;


public class TthtHnHistoryFragment extends TthtHnBaseFragment {

    // TODO: Rename and change types of parameters

    private IOnTthtHnHistoryFragment mListener;
    private RecyclerView rvHistory;
    private TthtHnSQLDAO mSqlDAO;
    private IInteractionDataCommon onIDataCommon;
    private TextView tvNodata;
    private HistoryAdapter.OnIDataHistoryAdapter iDataHistoryAdapter;
    private HistoryAdapter historyAdapter;
    private List<HistoryAdapter.DataHistoryAdapter> tableHistories7Day;

    public TthtHnHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TthtHnHistoryFragment newInstance() {
        TthtHnHistoryFragment fragment = new TthtHnHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //call Database access object
        try {
            mSqlDAO = new TthtHnSQLDAO(SqlHelper.getIntance().openDB(), getContext());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
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
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_history, container, false);
        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
        }
        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnTthtHnHistoryFragment) {
            mListener = (IOnTthtHnHistoryFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnHistoryFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void initDataAndView(View rootView) throws Exception {
        tvNodata = (TextView) rootView.findViewById(R.id.tv_nodata3);
        rvHistory = (RecyclerView) rootView.findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {

        //listDataHistory
        tableHistories7Day = mSqlDAO.getTABLE_HISTORYinNDay(onIDataCommon.getMaNVien(), 7);
        fillDataHistory(tableHistories7Day);
    }

    private void fillDataHistory(List<HistoryAdapter.DataHistoryAdapter> dataHistoryAdapters) {
        if (isShowNoDataText(dataHistoryAdapters.size()))
            return;

        //fill data history
        //fill data history 2Day
        if (iDataHistoryAdapter == null)
            iDataHistoryAdapter = new HistoryAdapter.OnIDataHistoryAdapter() {
                @Override
                public void doClickBtnMessageHistory(Common.TYPE_CALL_API typeCallApi, int pos, final HistoryAdapter.DataHistoryAdapter historyAdapter) {
                    IDialog iDialog = new IDialog() {
                        @Override
                        void clickOK() {
                            //copy text
                            Common.copyTextClipBoard(getContext(), historyAdapter.message);
                            Toast.makeText(getContext(), "Đã sao chép nội dung.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        void clickCancel() {

                        }
                    }.setTextBtnOK("CHÉP NỘI DUNG");
                    TthtHnHistoryFragment.super.showDialog(getContext(), historyAdapter.message, iDialog);
                }

            };

        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(getContext(), dataHistoryAdapters, iDataHistoryAdapter);
            rvHistory.setAdapter(historyAdapter);
        } else
            historyAdapter.refresh(dataHistoryAdapters);


        rvHistory.invalidate();
    }

    private boolean isShowNoDataText(int size) {
        if(size == 0)
        {
            rvHistory.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
            return true;
        }else {
            rvHistory.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
            return false;
        }
    }

    public void searchData(String typeSearchString, String messageSearch) {
        Common.TYPE_SEARCH_HISTORY typeSearch = Common.TYPE_SEARCH_HISTORY.findTYPE_SEARCH(typeSearchString);
        List<HistoryAdapter.DataHistoryAdapter> dataFilter = new ArrayList<>();
        String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
        switch (typeSearch) {
            case CHON:
                dataFilter = Common.cloneList(tableHistories7Day);
                break;
            case DOWNLOAD:
            case UPLOAD:
                if (!TextUtils.isEmpty(messageSearch)) {
                    for (int i = 0; i < tableHistories7Day.size(); i++) {
                        boolean isHasData = true;
                        HistoryAdapter.DataHistoryAdapter data = tableHistories7Day.get(i);

                        isHasData = Common.removeAccent(data.typeCallApi.toLowerCase()).contains(query);

                        if (isHasData) {
                            dataFilter.add(data);
                        }
                    }
                } else
                    dataFilter = Common.cloneList(tableHistories7Day);

                break;
            case DATE_CALL_API:

                break;
        }

        //giữ nguyên dữ liệu, lọc cái cần dùng
        fillDataHistory(dataFilter);
    }


    public interface IOnTthtHnHistoryFragment {
    }
}
