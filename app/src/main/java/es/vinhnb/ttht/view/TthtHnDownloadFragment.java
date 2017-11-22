package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListenerTthtHnDownloadFragment} interface
 * to handle interaction events.
 * Use the {@link TthtHnDownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TthtHnDownloadFragment extends TthtHnBaseFragment {
    private LoginFragment.LoginData mLoginData;
    private String mMaNVien;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnListenerTthtHnDownloadFragment mListener;
    private TthtHnSQLDAO mSqlDAO;
    private TextView tvDateDownload;
    private TextView tvPercentDownload;
    private ProgressBar pbarDownload;
    private TextView tvDownload;
    private Button btnDownload;
    private TextView tvTitleDownload;

    public TthtHnDownloadFragment() {
        // Required empty public constructor
    }

    public static TthtHnDownloadFragment newInstance(LoginFragment.LoginData param1, String param2) {
        TthtHnDownloadFragment fragment = new TthtHnDownloadFragment();
        Bundle args = new Bundle();
        args.putParcelable(TthtHnLoginActivity.BUNDLE_LOGIN, param1);
        args.putString(TthtHnLoginActivity.MA_NVIEN, param2);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_dong_bo, container, false);
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
        if (context instanceof OnListenerTthtHnDownloadFragment) {
            mListener = (OnListenerTthtHnDownloadFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerTthtHnMainFragment");
        }

        //call Database access object
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

    //region TthtHnBaseFragment
    @Override
    void initDataAndView(View viewRoot) throws Exception {
        tvTitleDownload = (TextView) viewRoot.findViewById(R.id.tv_title_download);
        tvDateDownload = (TextView) viewRoot.findViewById(R.id.tv_date_download);
        tvPercentDownload = (TextView) viewRoot.findViewById(R.id.tv_percent);
        pbarDownload = (ProgressBar) viewRoot.findViewById(R.id.pbar_download);
        tvDownload = (TextView) viewRoot.findViewById(R.id.tv_download);
        btnDownload = (Button) viewRoot.findViewById(R.id.btn_download);

    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check internet
                try {
                    if (!Common.isNetworkConnected(getContext()))
                        throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");


                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                }

            }
        });

    }
    //endregion

    public interface OnListenerTthtHnDownloadFragment {
    }
}
