package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.entity.api.MtbBbanModel;
import es.vinhnb.ttht.entity.api.UpdateStatus;
import es.vinhnb.ttht.server.TthtHnApi;
import es.vinhnb.ttht.server.TthtHnApiInterface;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;
import retrofit2.Call;
import retrofit2.Response;

import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.BUNDLE_DATA;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.STATUS_CODE;

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

    private TthtHnApiInterface apiInterface;

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

                try {
                    //init data
                    List<UpdateStatus> resultLayDuLieuCmis = null;
                    List<MtbBbanModel> resultGeT_BBAN = null;

                    //show Pbar
                    pbarDownload.setVisibility(View.VISIBLE);
                    btnDownload.setVisibility(View.GONE);


                    //LayDuLieuCmis
                    resultLayDuLieuCmis = callLayDuLieuCmis();
                    if (resultLayDuLieuCmis.size() == 0) {
                        ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), "Web kết nối CMIS lỗi!", null);
                        return;
                    }


                    //nếu OK thì đồng bộ biên bản
                    if (resultLayDuLieuCmis.get(0).RESULT.equals("OK")) {
                        //call GeT_BBAN
                        resultGeT_BBAN = callGeT_BBAN();
                    }


                    //GeT_BBAN
                    if (resultGeT_BBAN.size() == 0) {
                        ((TthtHnBaseActivity) getContext()).showSnackBar("Hiện tại không còn biên bản trên máy chủ!", null, null);
                        return;
                    }

                    //với mỗi biên bản thì đồng bộ chi tiết công tơ tương ứng biên bản đó
                    for (int i = 0; i < resultGeT_BBAN.size(); i++) {
                        MtbBbanModel bbanModel = resultGeT_BBAN.get(i);


                        //kiểm tra trạng thái biên bản trong database, ngừng thực hiện update biên bản nếu nó đã gửi
                        if (availableCanUpdateInfoBBan(bbanModel)) {

                        }
                    }


                } catch (Exception e) {
                    //hide Pbar
                    pbarDownload.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                } finally {
                    //hide Pbar
                    pbarDownload.setVisibility(View.GONE);
                    btnDownload.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean availableCanUpdateInfoBBan(MtbBbanModel bbanModel) throws Exception {
        boolean availableCanUpdateInfoBBan = false;


        //Lấy dữ liệu TRANG_THAI_DU_LIEU của biên bản
        String[] valueCheck = new String[]{String.valueOf(bbanModel.ID_BBAN_TRTH)};
        List<String> TRANG_THAI_DU_LIEUList = mSqlDAO.getTRANG_THAI_DU_LIEUofTABLE_BBAN_CTO(valueCheck);


        //check
        switch (TABLE_BBAN_CTO.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEUList.get(0)))
        {
            case CHUA_TON_TAI:
                //insert

                availableCanUpdateInfoBBan = true;
                break;

            case CHUA_GHI:
                //update full

                availableCanUpdateInfoBBan = true;
                break;

            case DA_GHI:
                //update những giá trường không không ghi ngoài hiện trường

                availableCanUpdateInfoBBan = true;
                break;

            case DA_GUI:
                //not update or insert


                break;

        }

//                mSqlDAO.isExistRows(TABLE_BBAN_CTO.class, collumnCheck, valueCheck);
    }

    private List<MtbBbanModel> callGeT_BBAN() throws Exception {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //update
                    updateInfoDownload("Đang đồng bộ biên bản treo tháo...", 0);

                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                }

            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<MtbBbanModel> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<MtbBbanModel>> GeT_BBANCall = apiInterface.GeT_BBAN(mLoginData.getmMaDvi(), mMaNVien);
                    Response<List<MtbBbanModel>> GeT_BBANResponse = GeT_BBANCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = GeT_BBANResponse.code();
                    if (GeT_BBANResponse.isSuccessful() && statusCode == 200) {
                        dataServer = GeT_BBANResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);


                    return result;

                } catch (Exception e) {
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();

        Bundle resultGeT_BBAN = asyncApi.get();

        int statusCode = resultGeT_BBAN.getInt(STATUS_CODE, 0);
        List<MtbBbanModel> dataServer = null;


        //check case
        if (statusCode == 0) {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), null, null);
        } else if (statusCode == 200) {
            //process to next async
            dataServer = resultGeT_BBAN.getParcelableArrayList(BUNDLE_DATA);
        } else {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + LayDuLieuCmisCallResponse.errorBody().string(), null);
        }


        return dataServer;
    }

    private List<UpdateStatus> callLayDuLieuCmis() throws Exception {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //update
                    updateInfoDownload("Đang kiểm tra kết nối tới CMIS...", 0);

                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                }

            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<UpdateStatus> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<UpdateStatus>> LayDuLieuCmisCall = apiInterface.LayDuLieuCmis(mLoginData.getmMaDvi(), mMaNVien);
                    Response<List<UpdateStatus>> LayDuLieuCmisCallResponse = LayDuLieuCmisCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = LayDuLieuCmisCallResponse.code();
                    if (LayDuLieuCmisCallResponse.isSuccessful() && statusCode == 200) {
                        dataServer = LayDuLieuCmisCallResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);


                    return result;

                } catch (Exception e) {
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();

        Bundle resultLayDuLieuCmis = asyncApi.get();

        int statusCode = resultLayDuLieuCmis.getInt(STATUS_CODE, 0);
        List<UpdateStatus> dataServer = null;


        //check case
        if (statusCode == 0) {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), null, null);
        } else if (statusCode == 200) {
            //process to next async
            dataServer = resultLayDuLieuCmis.getParcelableArrayList(BUNDLE_DATA);
        } else {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + LayDuLieuCmisCallResponse.errorBody().string(), null);
        }


        return dataServer;
    }

    private void updateInfoDownload(String titleDownload, int pbarPercent) {
        tvPercentDownload.setText(pbarPercent + "%");
        tvDownload.setText(titleDownload);
    }
    //endregion

    public interface OnListenerTthtHnDownloadFragment {
    }
}
