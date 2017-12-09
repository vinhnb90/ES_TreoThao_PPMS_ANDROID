package es.vinhnb.ttht.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.adapter.DoiSoatAdapter;
import es.vinhnb.ttht.adapter.HistoryAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.server.TthtHnApiInterface;

public class TthtHnUploadFragment extends TthtHnBaseFragment {

    private OnFragmentInteractionListener mListener;
    private TthtHnApiInterface apiInterface;
    private IInteractionDataCommon onIDataCommon;
    private TthtHnSQLDAO mSqlDAO;


    @BindView(R.id.tv_upload_date_upload)
    TextView tvDateUpload;
    @BindView(R.id.tv_upload_so_bb_upload)
    TextView tvSoBBUpload;
    @BindView(R.id.tv_upload_percent)
    TextView tvPercentUpload;
    @BindView(R.id.pbar_upload)
    ProgressBar pbarUpload;
    @BindView(R.id.tv_upload_info)
    TextView tvUploadInfo;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.rv_tththn_upload)
    RecyclerView rvDoiSoat;


    private Unbinder unbinder;
    private DoiSoatAdapter doiSoatAdapters;

    public TthtHnUploadFragment() {
        // Required empty public constructor
    }

    public static TthtHnUploadFragment newInstance() {
        TthtHnUploadFragment fragment = new TthtHnUploadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_upload, container, false);
        unbinder = ButterKnife.bind(TthtHnUploadFragment.this, viewRoot);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerTthtHnMainFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View rootView) throws Exception {
        rvDoiSoat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //setDate time now
        tvDateUpload.setTextColor(getResources().getColor(R.color.text_black));
        tvDateUpload.setText(Common.getDateTimeNow(Common.DATE_TIME_TYPE.type6));

        fillDataDoiSoat();
    }

    private void fillDataDoiSoat() {
        List<DoiSoatAdapter.DataDoiSoatAdapter> dataDoiSoatAdapters = mSqlDAO.getDoiSoatAdapterinNDay(2);

        //fill data doisoat
        if (doiSoatAdapters == null) {
            doiSoatAdapters = new DoiSoatAdapter(getContext(), dataDoiSoatAdapters
//                    , iDataHistoryAdapter
            );
            rvDoiSoat.setAdapter(doiSoatAdapters);
        } else
            doiSoatAdapters.refresh(dataDoiSoatAdapters);


        rvDoiSoat.invalidate();
    }
    //endregion

    public interface OnFragmentInteractionListener {
    }
}
