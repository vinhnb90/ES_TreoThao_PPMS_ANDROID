package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.vinhnb.ttht.adapter.DownloadAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_HISTORY;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_LYDO_TREOTHAO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import es.vinhnb.ttht.entity.api.CHUNG_LOAI_CONGTO;
import es.vinhnb.ttht.entity.api.D_LY_DO_MODEL;
import es.vinhnb.ttht.entity.api.MTB_TuTiModel;
import es.vinhnb.ttht.entity.api.MtbBbanModel;
import es.vinhnb.ttht.entity.api.MtbBbanTutiModel;
import es.vinhnb.ttht.entity.api.MtbCtoModel;
import es.vinhnb.ttht.entity.api.ResponseGetSuccessPostRequest;
import es.vinhnb.ttht.entity.api.TRAMVIEW;
import es.vinhnb.ttht.entity.api.UpdateStatus;
import es.vinhnb.ttht.server.TthtHnApi;
import es.vinhnb.ttht.server.TthtHnApiInterface;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;
import esolutions.com.esdatabaselib.baseSqlite.anonation.TYPE;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static es.vinhnb.ttht.common.Common.DELAY;
import static es.vinhnb.ttht.common.Common.DELAY_PROGESS_PBAR;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.BUNDLE_DATA;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.ERROR_BODY;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.STATUS_CODE;

public class TthtHnDownloadFragment extends TthtHnBaseFragment {

    private TthtHnApiInterface apiInterface;
    private IInteractionDataCommon onIDataCommon;

    private OnListenerTthtHnDownloadFragment mListener;
    private TthtHnSQLDAO mSqlDAO;
    private TextView tvDateDownload;
    private TextView tvPercentDownload;
    private ProgressBar pbarDownload;
    private TextView tvDownload;
    private Button btnDownload;
    private RecyclerView rvDownload;
    private TextView tvNodata;

    private TABLE_HISTORY infoSessionDownload = new TABLE_HISTORY();
    //tạo 1 string messageServer để lưu trữ thông báo của từng thời điểm phục vụ history infoSessionDownload
    private StringBuilder messageServer = new StringBuilder();
    private DownloadAdapter downloadAdapter;
    private DownloadAdapter.OnIDataHistoryAdapter iDataHistoryAdapter;


    public TthtHnDownloadFragment() {
        // Required empty public constructor
    }

    public static TthtHnDownloadFragment newInstance() {
        TthtHnDownloadFragment fragment = new TthtHnDownloadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_dong_bo, container, false);
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
        if (context instanceof OnListenerTthtHnDownloadFragment) {
            mListener = (OnListenerTthtHnDownloadFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnMainFragment");
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
    public void initDataAndView(View viewRoot) throws Exception {
        tvDateDownload = (TextView) viewRoot.findViewById(R.id.tv_date_download);
        tvPercentDownload = (TextView) viewRoot.findViewById(R.id.tv_percent);
        pbarDownload = (ProgressBar) viewRoot.findViewById(R.id.pbar_download);
        tvDownload = (TextView) viewRoot.findViewById(R.id.tv_download);
        btnDownload = (Button) viewRoot.findViewById(R.id.btn_download);
        tvNodata = (TextView) viewRoot.findViewById(R.id.tv_nodata1);

        //set layout ngược
        rvDownload = (RecyclerView) viewRoot.findViewById(R.id.rv_download_history);
        rvDownload.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //setDate time now
        tvDateDownload.setTextColor(getResources().getColor(R.color.text_black));
        tvDateDownload.setText(Common.getDateTimeNow(Common.DATE_TIME_TYPE.type6));


        //fill data history 2Day
        iDataHistoryAdapter = new DownloadAdapter.OnIDataHistoryAdapter() {
            @Override
            public void doClickBtnMessageHistory(int pos, final DownloadAdapter.DataDownloadAdapter historyAdapter) {

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
                TthtHnDownloadFragment.super.showDialog(getContext(), historyAdapter.message, iDialog);
            }
        };


        fillDataHistory2Day();


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //region init
                            //init data
                            List<UpdateStatus> resultLayDuLieuCmis = null;
                            List<MtbBbanModel> resultGeT_BBAN = null;
                            List<MtbCtoModel> resultGet_cto = null;
                            List<MtbBbanTutiModel> resultGet_bban_TUTI = null;
                            List<MTB_TuTiModel> resultGet_TUTI = null;
                            List<TRAMVIEW> resultGetTram = null;
                            List<CHUNG_LOAI_CONGTO> resultLayDuLieuLoaiCongTo = null;
                            List<D_LY_DO_MODEL> resultLayDuLieuLyDoTreothao = null;

                            //set default
                            infoSessionDownload.setMA_DVIQLY(onIDataCommon.getLoginData().getmMaDvi());
                            infoSessionDownload.setMA_NVIEN(onIDataCommon.getMaNVien());
                            infoSessionDownload.setTYPE_CALL_API(Common.TYPE_CALL_API.DOWNLOAD.content);
                            infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.SUCCESS.content);
                            infoSessionDownload.setMESSAGE_RESULT("");


                            //show Pbar
                            getView().post(new Runnable() {
                                @Override
                                public void run() {
                                    tvDownload.setVisibility(View.VISIBLE);
                                    btnDownload.setVisibility(View.GONE);


                                    infoSessionDownload.setDATE_CALL_API(Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite2));


                                    //convert date sqlite to date UI
                                    String dateUI = Common.convertDateToDate(infoSessionDownload.getDATE_CALL_API(), Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type9);
                                    tvDateDownload.setText(dateUI);
                                    tvDateDownload.setTextColor(getResources().getColor(R.color.tththn_button));
                                }
                            });
                            //endregion


                            //region LayDuLieuCmis
                            resultLayDuLieuCmis = callLayDuLieuCmis();


                            //nếu null = mất kết nối, đã thông báo snackbar trong callLayDuLieuCmis()
                            if (resultLayDuLieuCmis == null) {
                                return;
                            }
                            //nếu rỗng hoặc !OK (từ server trả về) thì lh quản trị
                            if (!resultLayDuLieuCmis.get(0).RESULT.equals("OK")) {
                                infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                                messageServer.append("-Máy chủ kết nối CMIS thất bại!-");
                                throw new Exception("Kết nối CMIS thất bại. Liên hệ với quản trị viên");
                            }
                            //endregion


                            //region get bban và cto
                            //TODO nếu OK thì đồng bộ biên bản
                            //TODO với mỗi biên bản thì đồng bộ chi tiết công tơ tương ứng biên bản đó
                            resultGeT_BBAN = callGeT_BBAN();


                            //nếu null = có lỗi khi đồng bộ biên bản
                            //không thông báo để tiếp tục đồng bộ các danh mục khác
                            if (resultGeT_BBAN == null) {
                                infoSessionDownload.setSO_BBAN_API(0);
                            } else if (resultGeT_BBAN.size() == 0) {
                                //nếu rỗng thì đã hết biên bản, vẫn cho tiếp tục thực hiện call các api khác
                                messageServer.append("-Hết biên bản trên máy chủ!-");
                                infoSessionDownload.setSO_BBAN_API(0);
                            } else {
                                //nếu có dữ liệu thì set
                                messageServer.append("-Gửi yêu cầu đồng bộ công tơ-..");
                            }


                            //luôn thực hiện tiếp
                            //đếm số công tơ trong tất cả biên bản gửi yêu cầu lên máy chủ lấy về
                            if (resultGeT_BBAN != null) {
                                //dữ liệu biên bản nhận về là dữ liệu trong 3 ngày trước gồm trạng thái 2, 3, 4
                                //set tất cả dữ liệu thành hết hiệu lực
                                //update tình trạng mới nhất của từng biên bản

                                //TODO
                                //get all biên bản không phải hết hiệu lực
                                String[] args = new String[]{onIDataCommon.getMaNVien()};
                                List<TABLE_BBAN_CTO> tableBbanCtoHetHieuLucList = mSqlDAO.getBBanHetHieuLuc(args);
                                final int sizetableBbanCtoHetHieuLucList = tableBbanCtoHetHieuLucList.size();
                                for (int i = 0; i < tableBbanCtoHetHieuLucList.size(); i++) {
                                    final int finalIi = i;
                                    getView().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateInfoDownload("Đang xử lý dữ liệu biên bản trên thiết bị ...", finalIi * 100 / sizetableBbanCtoHetHieuLucList);
                                        }
                                    }, DELAY_PROGESS_PBAR);

                                    //update het hieu luc
                                    TABLE_BBAN_CTO tableBbanCtoNew = (TABLE_BBAN_CTO) tableBbanCtoHetHieuLucList.get(i).clone();
                                    tableBbanCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.HET_HIEU_LUC.content);
                                    tableBbanCtoNew.setTRANG_THAI(Common.TRANG_THAI.HET_HIEU_LUC.content);
                                    mSqlDAO.updateRows(TABLE_BBAN_CTO.class, tableBbanCtoHetHieuLucList.get(i), tableBbanCtoNew);
                                }


                                if (sizetableBbanCtoHetHieuLucList != 0) {
                                    Thread.sleep(DELAY);
                                    getView().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateInfoDownload("Kết thúc xử lý dữ liệu biên bản trên thiết bị...", 100);
                                        }
                                    }, Common.DELAY);

                                }


                                final int resultGeT_BBANsize = resultGeT_BBAN.size();
                                int countCtoTreo = 0;
                                int countCtoThao = 0;

                                for (int i = 0; i < resultGeT_BBANsize; i++) {
                                    MtbBbanModel bbanModel = resultGeT_BBAN.get(i);
                                    if (bbanModel.ID_BBAN_TRTH == 2704811) {
                                        Log.e(TAG, "run error: " + 2704811);
                                    }
                                    final int finalI = i;

                                    if (i == 10 * (i / 10)) {
                                        Log.e(TAG, "run: ");
                                    }

                                    getView().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateInfoDownload("Đang đồng bộ công tơ ...", finalI * 100 / resultGeT_BBANsize);
                                        }
                                    }, DELAY_PROGESS_PBAR);


                                    //có phải update dữ liệu công tơ không
                                    saveDataBBan(bbanModel);

                                    //call Get_cto
                                    resultGet_cto = callGet_cto(bbanModel.ID_BBAN_TRTH);


                                    //nếu null = có lỗi khi đồng bộ các công tơ của biên bản
                                    //vẫn cho tiếp tục với các biên bản khác
                                    if (resultGet_cto == null) {
                                        resultGet_cto = new ArrayList<>();
                                    } else if (resultGet_cto.size() == 0) {
                                        //nếu rỗng thì hiện tại có biên bản nhưng không có công tơ, vẫn cho tiếp tục thực hiện call các api khác
                                        messageServer.append("-ID_BBAN_TRTH [" + bbanModel.ID_BBAN_TRTH + "] hiện không có công tơ!-");
                                    }


                                    //nếu có dữ liệu
                                    //update công tơ
                                    for (int j = 0; j < resultGet_cto.size(); j++) {
                                        MtbCtoModel mtbCtoModel = resultGet_cto.get(j);


                                        //nếu là treo
                                        if (mtbCtoModel.MA_BDONG.equalsIgnoreCase(Common.MA_BDONG.B.code))
                                            countCtoTreo++;

                                        //nếu là tháo
                                        if (mtbCtoModel.MA_BDONG.equalsIgnoreCase(Common.MA_BDONG.E.code))
                                            countCtoThao++;


                                        //cập nhật dữ liệu vào TABLE_CHITIET_CTO dựa vào trạng thái dữ liệu
                                        //kiểm tra trạng thái và cập nhật công tơ vào database
                                        saveDataCto(mtbCtoModel);
                                    }
                                }
                                //kết thúc đồng bộ công tơ
                                infoSessionDownload.setSO_BBAN_API(resultGeT_BBANsize);
                                infoSessionDownload.setSO_CTO_TREO_API(countCtoTreo);
                                infoSessionDownload.setSO_CTO_THAO_API(countCtoThao);
                            } else {
                                //kết thúc đồng bộ công tơ
                                infoSessionDownload.setSO_BBAN_API(0);
                                infoSessionDownload.setSO_CTO_TREO_API(0);
                                infoSessionDownload.setSO_CTO_THAO_API(0);
                            }


                            Thread.sleep(DELAY);

                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoDownload("Kết thúc đồng bộ biên bản công tơ...", 100);
                                }
                            }, Common.DELAY);
                            //endregion
//

                            //region get bban tu ti và tu ti
                            //TODO Get Tu ti
                            resultGet_bban_TUTI = callGet_bban_TUTI();


                            //nếu null = có lỗi khi đồng bộ biên bản tuti
                            //không thông báo để tiếp tục đồng bộ các danh mục khác
                            if (resultGet_bban_TUTI == null) {
                                infoSessionDownload.setSO_BBAN_TUTI_API(0);
                                resultGet_bban_TUTI = new ArrayList<>();
                            } else if (resultGet_bban_TUTI.size() == 0) {
                                //nếu rỗng thì đã hết biên bản tu ti, vẫn cho tiếp tục thực hiện call các api khác
                                messageServer.append("-Hết biên bản TU TI trên máy chủ!-");
                                infoSessionDownload.setSO_BBAN_TUTI_API(0);
                            } else {
                                //nếu có dữ liệu thì set
                                messageServer.append("-Gửi yêu cầu đồng bộ biên bản TU TI...-");
                            }


                            //luôn thực hiện tiếp
                            final int resultGet_bban_TUTIsize = resultGet_bban_TUTI.size();
                            int countTI = 0;
                            int countTU = 0;
                            for (int i = 0; i < resultGet_bban_TUTIsize; i++) {
                                MtbBbanTutiModel bbanTutiModel = resultGet_bban_TUTI.get(i);

                                final int finalI = i;
                                getView().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateInfoDownload("Đang đồng bộ dữ liệu TU TI...", finalI * 100 / resultGet_bban_TUTIsize);
                                    }
                                }, DELAY_PROGESS_PBAR);


                                saveDataBBanTuti(bbanTutiModel);

                                //call get
                                resultGet_TUTI = callGet_TUTI(bbanTutiModel.ID_BBAN_TUTI);


                                //nếu null = có lỗi khi đồng bộ các TU TI của biên bản TU TI
                                //vẫn cho tiếp tục với các biên bản khác
                                if (resultGet_TUTI == null) {
                                    resultGet_TUTI = new ArrayList<>();
                                } else if (resultGet_TUTI.size() == 0) {
                                    //nếu rỗng thì hiện tại có biên bản TU TI nhưng không có TU TI, vẫn cho tiếp tục thực hiện call các api khác
                                    messageServer.append("-ID_BBAN_TUTI [" + bbanTutiModel.ID_BBAN_TUTI + "] hiện không có TU TI nào!-");
                                }


                                //nếu có dữ liệu
                                //update tu ti
                                for (int j = 0; j < resultGet_TUTI.size(); j++) {
                                    MTB_TuTiModel tuTiModel = resultGet_TUTI.get(j);


                                    //nếu là TU
                                    if (tuTiModel.IS_TU == Common.IS_TU.TU.code)
                                        countTU++;

                                    //nếu là TI
                                    if (tuTiModel.IS_TU == Common.IS_TU.TI.code)
                                        countTI++;


                                    //cập nhật dữ liệu vào TABLE_CHITIET_TUTI dựa vào trạng thái dữ liệu
                                    //kiểm tra trạng thái công tơ trong database
                                    saveDataTuti(tuTiModel);
                                }
                            }


                            //Kết thúc đồng bộ TU TI
                            infoSessionDownload.setSO_BBAN_TUTI_API(resultGet_bban_TUTIsize);
                            infoSessionDownload.setSO_TU_API(countTU);
                            infoSessionDownload.setSO_TI_API(countTI);

                            Thread.sleep(DELAY);
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoDownload("Kết thúc đồng bộ dữ liệu biên bản TU TI...", 100);
                                }
                            }, Common.DELAY);
                            //endregion


                            //region xác nhận đồng bộ ok các biên bản trên
                            if (infoSessionDownload.getTYPE_RESULT() != Common.TYPE_RESULT.ERROR.content) {
                                ResponseGetSuccessPostRequest dataRequest = new ResponseGetSuccessPostRequest(onIDataCommon.getLoginData().getmMaDvi(), onIDataCommon.getMaNVien(), "OK");

                                //nếu call ok
                                if (callResponseGetSuccessPostRequest(dataRequest)) {
                                    Thread.sleep(DELAY);
                                    getView().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateInfoDownload("Xác nhận đồng bộ nguyên vẹn biên bản...", 100);
                                        }
                                    }, Common.DELAY);
                                } else {
                                    Thread.sleep(DELAY);
                                    getView().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateInfoDownload("Gặp vấn dề khi xác nhận đồng bộ nguyên vẹn biên bản...", 100);
                                        }
                                    }, Common.DELAY);

                                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                                }
                            }

                            //endregion


                            //region GetTram
                            resultGetTram = callGetTram();
                            //nếu null = có lỗi khi đồng bộ các dữ liệu trạm
                            //vẫn cho tiếp tục với các biên bản khác
                            if (resultGetTram == null) {
                                resultGetTram = new ArrayList<>();
                            } else if (resultGetTram.size() == 0) {
                                //nếu rỗng thì hiện tại có biên bản nhưng không có công tơ, vẫn cho tiếp tục thực hiện call các api khác
                                messageServer.append("-Danh mục Trạm hiện không có dữ liệu trên máy chủ!-");
                            }


                            final int resultGetTramsize = resultGetTram.size();
                            //mỗi dữ liệu trạm sẽ được cập nhật toàn bộ

                            for (int i = 0; i < resultGetTramsize; i++) {
                                TRAMVIEW tramview = resultGetTram.get(i);
                                final int finalI = i;
                                getView().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateInfoDownload("Đang đồng bộ Trạm ...", finalI * 100 / resultGetTramsize);
                                    }
                                }, DELAY_PROGESS_PBAR);


                                String[] arg = new String[]{tramview.MA_TRAM};
                                List<TABLE_TRAM> tableTrams = mSqlDAO.getTRAM(arg);
                                TABLE_TRAM tableTram = null;
                                TABLE_TRAM tableTramNew = null;
                                if (tableTrams.size() != 0) {
                                    tableTram = tableTrams.get(0);
                                }

                                if (tableTram != null) {
                                    //update
                                    tableTramNew = (TABLE_TRAM) tableTram.clone();
                                    tableTramNew.setCSUAT_TRAM(tableTram.getCSUAT_TRAM());
                                    tableTramNew.setDINH_DANH(tableTram.getDINH_DANH());
                                    tableTramNew.setLOAI_TRAM(tableTram.getLOAI_TRAM());
                                    tableTramNew.setMA_CAP_DA(tableTram.getMA_CAP_DA());
                                    tableTramNew.setMA_CAP_DA_RA(tableTram.getMA_CAP_DA_RA());
                                    tableTramNew.setMA_DVIQLY(tableTram.getMA_DVIQLY());
                                    tableTramNew.setMA_TRAM(tableTram.getMA_TRAM());
                                    tableTramNew.setTEN_TRAM(tableTram.getTEN_TRAM());
                                    mSqlDAO.updateORInsertRows(TABLE_TRAM.class, tableTram, tableTramNew);
                                } else {
                                    //insert
                                    tableTramNew = new TABLE_TRAM(
                                            0,
                                            tramview.MA_TRAM,
                                            tramview.MA_DVIQLY,
                                            tramview.TEN_TRAM,
                                            tramview.LOAI_TRAM,
                                            tramview.CSUAT_TRAM,
                                            tramview.MA_CAP_DA,
                                            tramview.MA_CAP_DA_RA,
                                            tramview.DINH_DANH);

                                    mSqlDAO.insert(TABLE_TRAM.class, tableTramNew);
                                }
                            }


                            //Kết thúc đồng bộ trạm
                            infoSessionDownload.setSO_TRAM_API(resultGetTramsize);
                            Thread.sleep(DELAY);
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoDownload("Kết thúc đồng bộ Trạm...", 100);
                                }
                            }, DELAY);
                            //endregion


                            //region Chung loai cto
                            resultLayDuLieuLoaiCongTo = callLayDuLieuLoaiCongTo();
                            //nếu null = có lỗi khi đồng bộ các dữ liệu Chủng loại
                            if (resultLayDuLieuLoaiCongTo == null) {
                                resultLayDuLieuLoaiCongTo = new ArrayList<>();
                            } else if (resultLayDuLieuLoaiCongTo.size() == 0) {
                                //nếu rỗng thì hiện tại có biên bản nhưng không có công tơ, vẫn cho tiếp tục thực hiện call các api khác
                                messageServer.append("-Danh mục chủng loại hiện không có loại nào!-");
                            }


                            //mỗi dữ liệu Chủng loại công tơ sẽ được cập nhật toàn bộ
                            final int resultLayDuLieuLoaiCongTosize = resultLayDuLieuLoaiCongTo.size();
                            for (int i = 0; i < resultLayDuLieuLoaiCongTo.size(); i++) {
                                CHUNG_LOAI_CONGTO object = resultLayDuLieuLoaiCongTo.get(i);
                                final int finalI = i;
                                getView().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateInfoDownload("Đang đồng bộ nhà cung cấp, chủng loại công tơ ...", finalI * 100 / resultLayDuLieuLoaiCongTosize);
                                    }
                                }, DELAY_PROGESS_PBAR);


                                //delete row
                                String[] nameCollumnCheck = new String[]{TABLE_LOAI_CONG_TO.table.MA_CLOAI.name()};
                                String[] valuesCheck = new String[]{object.MA_CLOAI};
                                mSqlDAO.deleteRows(TABLE_LOAI_CONG_TO.class, nameCollumnCheck, valuesCheck);


                                //insert row
                                //Lọc dữ liệu từ service to dữ liệu insert

                                TABLE_LOAI_CONG_TO tableLoaiCongTo = new TABLE_LOAI_CONG_TO(
                                        0,
                                        object.MA_CLOAI,
                                        object.TEN_LOAI_CTO,
                                        object.MO_TA,
                                        object.SO_PHA,
                                        object.SO_DAY,
                                        object.HS_NHAN,
                                        object.SO_CS,
                                        object.CAP_CXAC_P,
                                        object.CAP_CXAC_Q,
                                        object.DONG_DIEN,
                                        object.DIEN_AP,
                                        object.VH_CONG,
                                        object.MA_NUOC,
                                        object.MA_HANG,
                                        object.HANGSO_K,
                                        object.PTHUC_DOXA,
                                        object.TEN_NUOC);

                                mSqlDAO.insert(TABLE_LOAI_CONG_TO.class, tableLoaiCongTo);
                            }


                            //Kết thúc đồng bộ trạm
                            Thread.sleep(DELAY);
                            infoSessionDownload.setSO_CHUNGLOAI_API(resultLayDuLieuLoaiCongTosize);
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoDownload("Kết thúc đồng bộ Chủng loại...", 100);
                                }
                            }, DELAY);
                            //endregion


                            //region get ly do treo thao
                            resultLayDuLieuLyDoTreothao = callLayDuLieuLyDoTreothao();

                            //nếu null = có lỗi khi đồng bộ các dữ liệu Chủng loại
                            if (resultLayDuLieuLoaiCongTo == null) {
                                resultLayDuLieuLoaiCongTo = new ArrayList<>();
                            } else if (resultLayDuLieuLoaiCongTo.size() == 0) {
                                //nếu rỗng thì hiện tại có biên bản nhưng không có công tơ, vẫn cho tiếp tục thực hiện call các api khác
                                messageServer.append("-Danh mục lý do treo tháo  hiện không có lý do nào!-");
                            }


                            //mỗi dữ liệu lý do treo tháo sẽ được cập nhật toàn bộ
                            final int resultLayDuLieuLyDoTreothaosize = resultLayDuLieuLyDoTreothao.size();

                            for (int i = 0; i < resultLayDuLieuLyDoTreothao.size(); i++) {
                                D_LY_DO_MODEL object = resultLayDuLieuLyDoTreothao.get(i);
                                final int finalI = i;
                                getView().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateInfoDownload("Đang đồng bộ lý do treo tháo...", finalI * 100 / resultLayDuLieuLyDoTreothaosize);
                                    }
                                }, DELAY_PROGESS_PBAR);


                                //delete row
                                String[] nameCollumnCheck = new String[]{TABLE_LYDO_TREOTHAO.table.MA_LDO.name()};
                                String[] valuesCheck = new String[]{object.MA_LDO};
                                mSqlDAO.deleteRows(TABLE_LYDO_TREOTHAO.class, nameCollumnCheck, valuesCheck);


                                //insert row
                                //Lọc dữ liệu từ service to dữ liệu insert

                                TABLE_LYDO_TREOTHAO tableLydo = new TABLE_LYDO_TREOTHAO(
                                        0,
                                        object.MA_DVIQLY,
                                        object.MA_LDO,
                                        object.TEN_LDO,
                                        object.NHOM);

                                mSqlDAO.insert(TABLE_LYDO_TREOTHAO.class, tableLydo);
                            }

                            //Kết thúc đồng bộ lý do
                            Thread.sleep(DELAY);
                            infoSessionDownload.setSO_LYDO_TREOTHAO(resultLayDuLieuLyDoTreothaosize);
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoDownload("Kết thúc đồng bộ lý do treo tháo!", 100);
                                }
                            }, DELAY);
                            //endregion

                        } catch (final Exception e) {
                            e.printStackTrace();
                            getView().post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                                }
                            });

                        } finally {
                            //set UI
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //update title download
                                    updateInfoDownload("Kết thúc quá trình đồng bộ dữ liệu!", 100);
                                }
                            }, DELAY);
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //hide Pbar
                                    tvDownload.setVisibility(View.GONE);
                                    btnDownload.setVisibility(View.VISIBLE);
                                    //convert date sqlite to date UI
                                    String dateUI = Common.convertDateToDate(infoSessionDownload.getDATE_CALL_API(), Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type9);
                                    tvDateDownload.setText(dateUI);
                                }
                            }, Common.DELAY);


                            //ghi history phiên download
                            try {
                                infoSessionDownload.setMESSAGE_RESULT(messageServer.toString());
                                mSqlDAO.insert(TABLE_HISTORY.class, infoSessionDownload);
                            } catch (final Exception e) {
                                getView().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex07.getContent(), e.getMessage(), null);
                                    }
                                });
                            }

                            getView().post(new Runnable() {
                                @Override
                                public void run() {
                                    fillDataHistory2Day();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private boolean callResponseGetSuccessPostRequest(final ResponseGetSuccessPostRequest dataRequest) throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //update
//                    getView().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            messageServer.append("-Đang gửi yêu cầu tới máy chủ xác nhận đồng bộ biên bản thành công...-");
//                            updateInfoDownload("Đang gửi yêu cầu tới máy chủ xác nhận đồng bộ biên bản thành công...", 0);
//                        }
//                    });


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        messageServer.append("-Chưa có kết nối internet, vui lòng kiểm tra lại!-");
                        throw new Exception(messageServer.toString());
                    }

                } catch (final Exception e) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex10.getContent(), e.getMessage(), null);
                        }
                    });


                    //ném ex ra bên ngoài thân async để cancel async
                    e.printStackTrace();
                    throw e;
                }

            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                Boolean dataServer = null;

                try {
                    //call check CMIS connect
                    Call<Boolean> callResponseGetSuccess = apiInterface.ResponseGetSuccess(dataRequest);
                    Response<Boolean> ResponseGetSuccessResponse = callResponseGetSuccess.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = ResponseGetSuccessResponse.code();
                    String errorBody = "";
                    if (ResponseGetSuccessResponse.errorBody() != null)
                        errorBody = ResponseGetSuccessResponse.errorBody().string();

                    if (ResponseGetSuccessResponse.isSuccessful() && statusCode == 200) {
                        dataServer = ResponseGetSuccessResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putBoolean(BUNDLE_DATA, dataServer);
                    result.putString(ERROR_BODY, errorBody);

                    return result;

                } catch (Exception e) {
                    //history lỗi bất ngờ, nén vào bundle ném ra ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putBoolean(BUNDLE_DATA, false);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();


        Boolean dataServer = null;
        Bundle resultPostMTBWithImage = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultPostMTBWithImage.getInt(STATUS_CODE, 0);
        String errorBody = resultPostMTBWithImage.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            dataServer = resultPostMTBWithImage.getBoolean(BUNDLE_DATA);
        } else {
            //không show thông báo để đồng bộ các danh mục tiếp theo, chỉ ghi lịch sử
            messageServer.append("-Gặp lỗi khi upload biên bản " + statusCode + " " + errorBody + "-");
            infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
        }


        return dataServer;
    }


    private void fillDataHistory2Day() {


        //listDataHistory
        List<DownloadAdapter.DataDownloadAdapter> tableHistories2Day = mSqlDAO.getTABLE_HISTORYDownloadinNDay(onIDataCommon.getMaNVien(), 7);

        if (isShowNoDataText(tableHistories2Day.size()))
            return;

        //fill data history
        if (downloadAdapter == null) {
            downloadAdapter = new DownloadAdapter(getContext(), tableHistories2Day, iDataHistoryAdapter);
            rvDownload.setAdapter(downloadAdapter);
        } else
            downloadAdapter.refresh(tableHistories2Day);


        rvDownload.invalidate();
    }

    private boolean saveDataCto(MtbCtoModel mtbCtoModel) throws Exception {
        boolean availableCanUpdateInfoCto = false;


        //Lấy dữ liệu TRANG_THAI_DU_LIEU của biên bản gồm mã công tơ, ID bban treo tháo, mã biến động
        String[] valueCheck = new String[]{String.valueOf(mtbCtoModel.MA_CTO), String.valueOf(mtbCtoModel.ID_BBAN_TRTH), mtbCtoModel.MA_BDONG, onIDataCommon.getMaNVien()};
        List<String> TRANG_THAI_DU_LIEUList = mSqlDAO.getTRANG_THAI_DU_LIEUofTABLE_CHITIET_CTO(valueCheck);


        //casting dữ liệu server MtbCtoModel sang dữ liệu sqlite TABLE_CHITIET_CTO

        TABLE_CHITIET_CTO tableChitietCtoNew = new TABLE_CHITIET_CTO(
                0,
                mtbCtoModel.MA_DVIQLY,
                onIDataCommon.getMaNVien(),
                mtbCtoModel.ID_BBAN_TRTH,
                mtbCtoModel.MA_CTO,
                mtbCtoModel.SO_CTO,
                mtbCtoModel.LAN,
                mtbCtoModel.MA_BDONG,
                mtbCtoModel.NGAY_BDONG,
                mtbCtoModel.MA_CLOAI,
                mtbCtoModel.LOAI_CTO,
                mtbCtoModel.VTRI_TREO,
                mtbCtoModel.MO_TA_VTRI_TREO,
                mtbCtoModel.MA_SOCBOOC,
                mtbCtoModel.SO_VIENCBOOC,
                mtbCtoModel.LOAI_HOM,
                mtbCtoModel.MA_SOCHOM,
                mtbCtoModel.SO_VIENCHOM,
                mtbCtoModel.HS_NHAN,
                mtbCtoModel.NGAY_TAO,
                mtbCtoModel.NGUOI_TAO,
                mtbCtoModel.NGAY_SUA,
                mtbCtoModel.NGUOI_SUA,
                mtbCtoModel.MA_CNANG,
                mtbCtoModel.SO_TU,
                mtbCtoModel.SO_TI,
                mtbCtoModel.SO_COT,
                mtbCtoModel.SO_HOM,
                mtbCtoModel.CHI_SO,
                mtbCtoModel.NGAY_KDINH,
                mtbCtoModel.NAM_SX,
                mtbCtoModel.TEM_CQUANG,
                mtbCtoModel.MA_CHIKDINH,
                mtbCtoModel.MA_TEM,
                mtbCtoModel.SOVIEN_CHIKDINH,
                mtbCtoModel.DIEN_AP,
                mtbCtoModel.DONG_DIEN,
                mtbCtoModel.HANGSO_K,
                mtbCtoModel.MA_NUOC,
                mtbCtoModel.TEN_NUOC,
                mtbCtoModel.ID_CHITIET_CTO,
                mtbCtoModel.SO_KIM_NIEM_CHI,
                mtbCtoModel.TTRANG_NPHONG,
                mtbCtoModel.TEN_LOAI_CTO,
                mtbCtoModel.PHUONG_THUC_DO_XA,
                "",//GHI_CHU
                mtbCtoModel.ID_BBAN_TUTI,
                0,//HS_NHAN_SAULAP_TUTI,
                "",//SO_TU_SAULAP_TUTI,
                "",//SO_TI_SAULAP_TUTI,
                "",//CHI_SO_SAULAP_TUTI,
                "",//DIEN_AP_SAULAP_TUTI,
                "",//DONG_DIEN_SAULAP_TUTI,
                "",//HANGSO_K_SAULAP_TUTI,
                0,//CAP_CX_SAULAP_TUTI,
                Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content);


        if (TRANG_THAI_DU_LIEUList.size() == 0) {
            //insert
            mSqlDAO.insert(TABLE_CHITIET_CTO.class, tableChitietCtoNew);
            availableCanUpdateInfoCto = true;
        } else {
            String[] args = new String[]{String.valueOf(mtbCtoModel.ID_BBAN_TRTH), mtbCtoModel.MA_BDONG, onIDataCommon.getMaNVien()};
            TABLE_CHITIET_CTO tableChitietCtoHienTai = mSqlDAO.getChiTietCongto(args).get(0);

            //check trạng thái biên bản và trạng thái dữ liệu biên bản
            args = new String[]{String.valueOf(mtbCtoModel.ID_BBAN_TRTH), onIDataCommon.getMaNVien()};
            TABLE_BBAN_CTO tableBbanCto = mSqlDAO.getBBan(args).get(0);


            //không quan tâm tới trạng thái của công tơ trên server, chỉ quan tâm trạng thái của biên bản trên server, đã đc cập nhật mới
            boolean isMustUpdateData = false;
            switch (Common.TRANG_THAI.findTRANG_THAI(tableBbanCto.getTRANG_THAI())) {
                case CHUA_TON_TAI:
                case DA_XUAT_RA_WEB:

                    //ko co
                    break;
                case DA_XUAT_RA_MTB:
                    //nếu dữ liệu đã ghi, đang chờ xác nhân, gửi thất bại, dã tồn tại, đã xác nhận chuyển thành đã ghi
                    //còn lại thì thành chưa ghi
                    switch (Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(tableChitietCtoHienTai.getTRANG_THAI_DU_LIEU())) {
                        case CHUA_TON_TAI:
                        case CHUA_GHI:
                        case HET_HIEU_LUC:
                            tableChitietCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content);
                            break;
                        case DA_GHI:
                        case GUI_THAT_BAI:
                        case DANG_CHO_XAC_NHAN_CMIS:
                        case DA_TON_TAI_GUI_TRUOC_DO:
                        case DA_XAC_NHAN_TREN_CMIS:
                            tableChitietCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
                            break;
                    }


                    isMustUpdateData = true;
                    break;
                case DANG_CHO_XAC_NHAN_CMIS:
                    tableChitietCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS.content);
                    break;
                case XAC_NHAN_TREN_CMIS:
                    tableChitietCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS.content);
                    break;
                case HET_HIEU_LUC:
                    tableChitietCtoNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.HET_HIEU_LUC.content);
                    break;
            }


            //nếu phải update thì lưu các thông tin đã được ghi ngoài hiện trường
            if (isMustUpdateData) {
                tableChitietCtoNew.setGHI_CHU(tableChitietCtoHienTai.getGHI_CHU());
                tableChitietCtoNew.setTTRANG_NPHONG(tableChitietCtoHienTai.getTTRANG_NPHONG());
                tableChitietCtoNew.setCHI_SO(tableChitietCtoHienTai.getCHI_SO());
                tableChitietCtoNew.setCHI_SO_SAULAP_TUTI(tableChitietCtoHienTai.getCHI_SO_SAULAP_TUTI());
                tableChitietCtoNew.setDIEN_AP_SAULAP_TUTI(tableChitietCtoHienTai.getDIEN_AP_SAULAP_TUTI());
                tableChitietCtoNew.setCHI_SO_SAULAP_TUTI(tableChitietCtoHienTai.getCHI_SO_SAULAP_TUTI());
                tableChitietCtoNew.setCAP_CX_SAULAP_TUTI(tableChitietCtoHienTai.getCAP_CX_SAULAP_TUTI());
                tableChitietCtoNew.setDONG_DIEN_SAULAP_TUTI(tableChitietCtoHienTai.getDONG_DIEN_SAULAP_TUTI());
                tableChitietCtoNew.setHANGSO_K_SAULAP_TUTI(tableChitietCtoHienTai.getHANGSO_K_SAULAP_TUTI());
                tableChitietCtoNew.setHS_NHAN_SAULAP_TUTI(tableChitietCtoHienTai.getHS_NHAN_SAULAP_TUTI());
                tableChitietCtoNew.setSO_TI_SAULAP_TUTI(tableChitietCtoHienTai.getSO_TI_SAULAP_TUTI());
                tableChitietCtoNew.setSO_TU_SAULAP_TUTI(tableChitietCtoHienTai.getSO_TU_SAULAP_TUTI());

                tableChitietCtoNew.setSO_KIM_NIEM_CHI(tableChitietCtoHienTai.getSO_KIM_NIEM_CHI());
                tableChitietCtoNew.setTEM_CQUANG(tableChitietCtoHienTai.getTEM_CQUANG());
                tableChitietCtoNew.setLAN(tableChitietCtoHienTai.getLAN());

                tableChitietCtoNew.setMA_CHIKDINH(tableChitietCtoHienTai.getMA_CHIKDINH());
                tableChitietCtoNew.setMA_CHIKDINH(tableChitietCtoHienTai.getMA_CHIKDINH());
                tableChitietCtoNew.setMA_SOCBOOC(tableChitietCtoHienTai.getMA_SOCBOOC());
                tableChitietCtoNew.setMA_SOCHOM(tableChitietCtoHienTai.getMA_SOCHOM());
                tableChitietCtoNew.setLOAI_HOM(tableChitietCtoHienTai.getLOAI_HOM());
                tableChitietCtoNew.setSOVIEN_CHIKDINH(tableChitietCtoHienTai.getSOVIEN_CHIKDINH());
                tableChitietCtoNew.setSO_VIENCBOOC(tableChitietCtoHienTai.getSO_VIENCBOOC());
                tableChitietCtoNew.setSO_VIENCHOM(tableChitietCtoHienTai.getSO_VIENCHOM());
                tableChitietCtoNew.setPHUONG_THUC_DO_XA(tableChitietCtoHienTai.getPHUONG_THUC_DO_XA());
            }

            mSqlDAO.updateRows(TABLE_CHITIET_CTO.class, tableChitietCtoHienTai, tableChitietCtoNew);


        }
        return availableCanUpdateInfoCto;
    }

    private void saveDataBBan(MtbBbanModel bbanModel) throws Exception {

        //Lấy dữ liệu TRANG_THAI_DU_LIEU của biên bản
        String[] valueCheck = new String[]{String.valueOf(bbanModel.ID_BBAN_TRTH), onIDataCommon.getMaNVien()};
        List<String> TRANG_THAI_DU_LIEUList = mSqlDAO.getTRANG_THAI_DU_LIEUofTABLE_BBAN_CTO(valueCheck);

        //server ko trả về dữ liệu
        String MA_NVIEN = TextUtils.isEmpty(bbanModel.MA_NVIEN) ? onIDataCommon.getMaNVien() : bbanModel.MA_NVIEN;

        //check trang thai web
        //khi check về chỉ có những biên bản có giá trị TRANG_THAI là DA_XUAT_RA_WEB - 1 và HET_HIEU_LUC - 21
        Common.TRANG_THAI trangThaiWebNew = Common.TRANG_THAI.findTRANG_THAI(bbanModel.TRANG_THAI);

        TABLE_BBAN_CTO tableBbanCtoNewData = new TABLE_BBAN_CTO(
                0,
                onIDataCommon.getLoginData().getmMaDvi(),
                bbanModel.ID_BBAN_TRTH,
                bbanModel.MA_DDO,
                bbanModel.SO_BBAN,
                bbanModel.NGAY_TRTH,
                MA_NVIEN,
                bbanModel.MA_LDO,
                bbanModel.NGAY_TAO,
                bbanModel.NGUOI_TAO,
                bbanModel.NGAY_SUA,
                bbanModel.NGUOI_SUA,
                bbanModel.MA_CNANG,
                bbanModel.MA_YCAU_KNAI,
                trangThaiWebNew.content,
                bbanModel.GHI_CHU,
                bbanModel.ID_BBAN_CONGTO,
                bbanModel.LOAI_BBAN,
                bbanModel.TEN_KHANG,
                bbanModel.DCHI_HDON,
                bbanModel.DTHOAI,
                bbanModel.MA_GCS_CTO,
                bbanModel.MA_TRAM,
                bbanModel.MA_HDONG,
                bbanModel.MA_KHANG,
                bbanModel.LY_DO_TREO_THAO,
                Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content,
                Common.TRANG_THAI_DOI_SOAT.CHUA_DOISOAT.content);

        Common.TRANG_THAI_DU_LIEU trangThaiDuLieuMTBNew = null;

        if (TRANG_THAI_DU_LIEUList.size() == 0) {
            //CHUA_TON_TAI
            //insert
            mSqlDAO.insert(TABLE_BBAN_CTO.class, tableBbanCtoNewData);
        } else {
            trangThaiDuLieuMTBNew = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEUList.get(0));

            boolean isMustUpdate = false;

            switch (trangThaiWebNew) {
                case CHUA_TON_TAI:
                    //ko nhận
                    break;
                case DA_XUAT_RA_WEB:
                case DA_XUAT_RA_MTB:
                    //nếu dữ liệu đã ghi, đang chờ xác nhân, gửi thất bại, dã tồn tại, đã xác nhận chuyển thành đã ghi
                    //còn lại thì thành chưa ghi
                    switch (trangThaiDuLieuMTBNew) {
                        case CHUA_TON_TAI:
                        case CHUA_GHI:
                        case HET_HIEU_LUC:
                            tableBbanCtoNewData.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content);
                            break;
                        case DA_GHI:
                        case GUI_THAT_BAI:
                        case DANG_CHO_XAC_NHAN_CMIS:
                        case DA_TON_TAI_GUI_TRUOC_DO:
                        case DA_XAC_NHAN_TREN_CMIS:
                            tableBbanCtoNewData.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
                            break;
                    }
                    isMustUpdate = true;
                    break;
                case DANG_CHO_XAC_NHAN_CMIS:
                    tableBbanCtoNewData.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS.content);
                    break;
                case XAC_NHAN_TREN_CMIS:
                    tableBbanCtoNewData.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS.content);
                    break;
                case HET_HIEU_LUC:
                    tableBbanCtoNewData.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.HET_HIEU_LUC.content);
                    break;
            }

            //update tất cả các trường
            String[] args = new String[]{String.valueOf(bbanModel.ID_BBAN_TRTH), onIDataCommon.getMaNVien()};
            TABLE_BBAN_CTO tableBbanCtoHienTai = mSqlDAO.getBBan(args).get(0);
            if (isMustUpdate) {
                tableBbanCtoNewData.setMA_LDO(tableBbanCtoHienTai.getMA_LDO());
                tableBbanCtoNewData.setGHI_CHU(tableBbanCtoHienTai.getGHI_CHU());
                tableBbanCtoNewData.setMA_LDO(tableBbanCtoHienTai.getMA_LDO());
                tableBbanCtoNewData.setLY_DO_TREO_THAO(tableBbanCtoHienTai.getLY_DO_TREO_THAO());
                tableBbanCtoNewData.setMA_LDO(tableBbanCtoHienTai.getMA_LDO());
                tableBbanCtoNewData.setMA_LDO(tableBbanCtoHienTai.getMA_LDO());
                tableBbanCtoNewData.setMA_LDO(tableBbanCtoHienTai.getMA_LDO());
            }

            mSqlDAO.updateRows(TABLE_BBAN_CTO.class, tableBbanCtoHienTai, tableBbanCtoNewData);
        }
    }

    private void saveDataBBanTuti(MtbBbanTutiModel bbanTutiModel) throws Exception {

        //Lấy dữ liệu TRANG_THAI_DU_LIEU của biên bản tu ti
        String[] valueCheck = new String[]{String.valueOf(bbanTutiModel.ID_BBAN_TUTI), onIDataCommon.getMaNVien()};
        List<String> TRANG_THAI_DU_LIEUList = mSqlDAO.getTRANG_THAI_DU_LIEUofTABLE_BBAN_TUTI(valueCheck);


        //casting dữ liệu server MtbBbanTutiModel sang dữ liệu sqlite TABLE_BBAN_TUTI
        //catch case server ko trả về dữ liệu
        String MA_NVIEN = TextUtils.isEmpty(bbanTutiModel.MA_NVIEN) ? onIDataCommon.getMaNVien() : bbanTutiModel.MA_NVIEN;


        TABLE_BBAN_TUTI tableBbanTutiNew = new TABLE_BBAN_TUTI(
                0,
                bbanTutiModel.MA_DVIQLY,
                bbanTutiModel.ID_BBAN_TUTI,
                bbanTutiModel.MA_DDO,
                bbanTutiModel.SO_BBAN,
                bbanTutiModel.NGAY_TRTH,
                MA_NVIEN,
                Common.TRANG_THAI.findTRANG_THAI(bbanTutiModel.TRANG_THAI).content,
                bbanTutiModel.TEN_KHANG,
                bbanTutiModel.DCHI_HDON,
                bbanTutiModel.DTHOAI,
                bbanTutiModel.MA_GCS_CTO,
                bbanTutiModel.MA_TRAM,
                bbanTutiModel.LY_DO_TREO_THAO,
                bbanTutiModel.MA_KHANG,
                bbanTutiModel.ID_BBAN_WEB_TUTI,
                bbanTutiModel.NVIEN_KCHI,
                Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content);


        if (TRANG_THAI_DU_LIEUList.size() == 0) {
            //insert
            mSqlDAO.insert(TABLE_BBAN_TUTI.class, tableBbanTutiNew);
        } else {
            TABLE_BBAN_TUTI tableBbanTutiHienTai = mSqlDAO.getBBanTuti(tableBbanTutiNew.getID_BBAN_TUTI(), onIDataCommon.getMaNVien()).get(0);


            boolean isMustUpdateData = false;
            switch (Common.TRANG_THAI.findTRANG_THAI(bbanTutiModel.TRANG_THAI)) {
                case CHUA_TON_TAI:
                case DA_XUAT_RA_WEB:
                case DA_XUAT_RA_MTB:
                    //nếu dữ liệu đã ghi, đang chờ xác nhân, gửi thất bại, dã tồn tại, đã xác nhận chuyển thành đã ghi
                    //còn lại thì thành chưa ghi
                    switch (Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(tableBbanTutiHienTai.getTRANG_THAI_DU_LIEU())) {
                        case CHUA_TON_TAI:
                        case CHUA_GHI:
                        case HET_HIEU_LUC:
                            tableBbanTutiNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.CHUA_GHI.content);
                            break;
                        case DA_GHI:
                        case GUI_THAT_BAI:
                        case DANG_CHO_XAC_NHAN_CMIS:
                        case DA_TON_TAI_GUI_TRUOC_DO:
                        case DA_XAC_NHAN_TREN_CMIS:
                            tableBbanTutiNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
                            break;
                    }


                    isMustUpdateData = true;
                    break;
                case DANG_CHO_XAC_NHAN_CMIS:
                    tableBbanTutiNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS.content);
                    break;
                case XAC_NHAN_TREN_CMIS:
                    tableBbanTutiNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS.content);
                    break;
                case HET_HIEU_LUC:
                    tableBbanTutiNew.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.HET_HIEU_LUC.content);
                    break;
            }


            //nếu phải update thì lưu các thông tin đã được ghi ngoài hiện trường
            if (isMustUpdateData) {
                tableBbanTutiNew.setLY_DO_TREO_THAO(tableBbanTutiHienTai.getLY_DO_TREO_THAO());
            }

            mSqlDAO.updateRows(TABLE_BBAN_TUTI.class, tableBbanTutiHienTai, tableBbanTutiNew);
        }

    }

    private void saveDataTuti(MTB_TuTiModel tuTiModel) throws Exception {


        //Lấy dữ liệu TRANG_THAI_DU_LIEU của biên bản
        String[] collumnCheck = new String[]{TABLE_CHITIET_TUTI.table.ID_CHITIET_TUTI.name()};
        String[] valueCheck = new String[]{String.valueOf(tuTiModel.ID_CHITIET_TUTI)};
        boolean existRows = mSqlDAO.isExistRows(TABLE_CHITIET_TUTI.class, collumnCheck, valueCheck);


        //casting dữ liệu server MTB_TuTiModel sang dữ liệu sqlite TABLE_CHITIET_TUTI

        TABLE_CHITIET_TUTI tableChitietTutiNew = new TABLE_CHITIET_TUTI(
                0,
                tuTiModel.MA_CLOAI,
                tuTiModel.LOAI_TU_TI,
                tuTiModel.MO_TA,
                tuTiModel.SO_PHA,
                tuTiModel.TYSO_DAU,
                tuTiModel.CAP_CXAC,
                tuTiModel.CAP_DAP,
                tuTiModel.MA_NUOC,
                tuTiModel.MA_HANG,
                tuTiModel.TRANG_THAI,
                String.valueOf(tuTiModel.IS_TU),
                tuTiModel.ID_BBAN_TUTI,
                tuTiModel.ID_CHITIET_TUTI,
                tuTiModel.SO_TU_TI,
                tuTiModel.NUOC_SX,
                tuTiModel.SO_TEM_KDINH,
                tuTiModel.NGAY_KDINH,
                tuTiModel.MA_CHI_KDINH,
                tuTiModel.MA_CHI_HOP_DDAY,
                tuTiModel.SO_VONG_THANH_CAI,
                tuTiModel.TYSO_BIEN,
                tuTiModel.MA_BDONG,
                tuTiModel.MA_DVIQLY,
                onIDataCommon.getMaNVien());


        if (!existRows) {
            //insert
            mSqlDAO.insert(TABLE_CHITIET_TUTI.class, tableChitietTutiNew);
        } else {
            //update toan bo
            TABLE_CHITIET_TUTI tableChitietTutiHienTai = mSqlDAO.getChitietTuTi(tuTiModel.ID_BBAN_TUTI, onIDataCommon.getMaNVien()).get(0);

            mSqlDAO.updateRows(TABLE_CHITIET_TUTI.class, tableChitietTutiHienTai, tableChitietTutiNew);

        }
    }

    private List<D_LY_DO_MODEL> callLayDuLieuLyDoTreothao() throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Mất kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //update
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            updateInfoDownload("Đang đồng bộ dữ liệu Lý do treo tháo...", 0);
                        }
                    });
                } catch (final Exception e) {
                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                    e.printStackTrace();
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<D_LY_DO_MODEL> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<D_LY_DO_MODEL>> LayDuLieuLyDoTreoThaoCall = apiInterface.GET_LY_DO_TREO_THAO(onIDataCommon.getLoginData().getmMaDvi());
                    Response<List<D_LY_DO_MODEL>> LayDuLieuLoaiCongToResponse = LayDuLieuLyDoTreoThaoCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = LayDuLieuLoaiCongToResponse.code();
                    String errorBody = "";
                    if (LayDuLieuLoaiCongToResponse.errorBody() != null) {
                        errorBody = LayDuLieuLoaiCongToResponse.errorBody().string();
                    }
                    if (LayDuLieuLoaiCongToResponse.isSuccessful() && statusCode == 200) {
                        dataServer = LayDuLieuLoaiCongToResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, "Mất kết nối tới máy chủ!");
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();

        Bundle resultGetLyDoTreothao = asyncApi.get();

        int statusCode = resultGetLyDoTreothao.getInt(STATUS_CODE, 0);
        String errorBody = resultGetLyDoTreothao.getString(ERROR_BODY, "");
        List<D_LY_DO_MODEL> dataServer = null;


        //check case
        if (statusCode == 0) {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), null, null);
        } else if (statusCode == 200) {
            //process to next async
            dataServer = resultGetLyDoTreothao.getParcelableArrayList(BUNDLE_DATA);
        } else {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + errorBody, null);
        }


        return dataServer;
    }

    private List<CHUNG_LOAI_CONGTO> callLayDuLieuLoaiCongTo() throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Mất kết nối internet, vui lòng kiểm tra lại!");
                    }


                    //update
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            updateInfoDownload("Đang đồng bộ dữ liệu Trạm...", 0);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<CHUNG_LOAI_CONGTO> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<CHUNG_LOAI_CONGTO>> LayDuLieuLoaiCongToCall = apiInterface.LayDuLieuLoaiCongTo();
                    Response<List<CHUNG_LOAI_CONGTO>> LayDuLieuLoaiCongToResponse = LayDuLieuLoaiCongToCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = LayDuLieuLoaiCongToResponse.code();
                    String errorBody = "";
                    if (LayDuLieuLoaiCongToResponse.errorBody() != null) {
                        errorBody = LayDuLieuLoaiCongToResponse.errorBody().string();
                    }
                    if (LayDuLieuLoaiCongToResponse.isSuccessful() && statusCode == 200) {
                        dataServer = LayDuLieuLoaiCongToResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, "Mất kết nối tới máy chủ!");
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();

        Bundle resultGetTram = asyncApi.get();

        int statusCode = resultGetTram.getInt(STATUS_CODE, 0);
        String errorBody = resultGetTram.getString(ERROR_BODY, "");
        List<CHUNG_LOAI_CONGTO> dataServer = null;


        //check case
        if (statusCode == 0) {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), null, null);
        } else if (statusCode == 200) {
            //process to next async
            dataServer = resultGetTram.getParcelableArrayList(BUNDLE_DATA);
        } else {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex02.getContent(), "Mã lỗi: " + statusCode + "\nNội dung:" + errorBody, null);
        }


        return dataServer;
    }


    private List<TRAMVIEW> callGetTram() throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);

                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Mất kết nối internet, vui lòng kiểm tra lại!");
                    }


                } catch (final Exception e) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });
                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                    //ném ex ra ngoài để cancel
                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<TRAMVIEW> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<TRAMVIEW>> GetTramCall = apiInterface.GetTram(onIDataCommon.getLoginData().getmMaDvi());
                    Response<List<TRAMVIEW>> GetTramResponse = GetTramCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = GetTramResponse.code();
                    String errorBody = "";
                    if (GetTramResponse.errorBody() != null)
                        errorBody = GetTramResponse.errorBody().string();
                    if (GetTramResponse.isSuccessful() && statusCode == 200) {
                        dataServer = GetTramResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    //nén ex vào bundle và ném ra ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();
        List<TRAMVIEW> dataServer = null;
        Bundle resultGetTram = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultGetTram.getInt(STATUS_CODE, 0);
        String errorBody = resultGetTram.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            //process to next async
            dataServer = resultGetTram.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //vẫn cho thực hiện tiếp tục với các ID_BBAN_TUTI nên chỉ lưu history lỗi khi đồng bộ các công tơ trong ID_BBAN_TUTI này.
            messageServer.append("- Lỗi đồng bộ dữ liệu trạm " + statusCode + " " + errorBody + "-");
        }


        return dataServer;
    }

    private List<MTB_TuTiModel> callGet_TUTI(final int ID_BBAN_TUTI) throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Mất kết nối internet, vui lòng kiểm tra lại!");
                    }
                } catch (final Exception e) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });


                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<MTB_TuTiModel> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<MTB_TuTiModel>> Get_TUTICall = apiInterface.Get_TUTI(onIDataCommon.getLoginData().getmMaDvi(), String.valueOf(ID_BBAN_TUTI));
                    Response<List<MTB_TuTiModel>> Get_TUTIResponse = Get_TUTICall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = Get_TUTIResponse.code();
                    String errorBody = "";
                    if (Get_TUTIResponse.errorBody() != null)
                        errorBody = Get_TUTIResponse.errorBody().string();
                    if (Get_TUTIResponse.isSuccessful() && statusCode == 200) {
                        dataServer = Get_TUTIResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    //nén ex vào bundle và ném ra ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();
        List<MTB_TuTiModel> dataServer = null;
        Bundle resultGet_TUTI = asyncApi.get();


        //bundle luôn khác null
        int statusCode = resultGet_TUTI.getInt(STATUS_CODE, 0);
        String errorBody = resultGet_TUTI.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            //process to next async
            dataServer = resultGet_TUTI.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //vẫn cho thực hiện tiếp tục với các ID_BBAN_TUTI nên chỉ lưu history lỗi khi đồng bộ các công tơ trong ID_BBAN_TUTI này.
            messageServer.append("-ID_BBAN_TUTI[" + ID_BBAN_TUTI + "] lỗi đồng bộ các công tơ " + statusCode + " " + errorBody + "-");
        }


        return dataServer;
    }

    private List<MtbBbanTutiModel> callGet_bban_TUTI() throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);

                    //update
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageServer.append("-Đang gửi yêu cầu đồng bộ biên bản TU TI...-");
                            updateInfoDownload("Đang gửi yêu cầu đồng bộ biên bản TU TI...", 0);
                        }
                    }, Common.DELAY);

                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        throw new Exception("Mất kết nối internet, vui lòng kiểm tra lại!");
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });
                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                    //ném ex ra bên ngoài thân async để cancel async
                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<MtbBbanTutiModel> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<MtbBbanTutiModel>> Get_bban_TUTICall = apiInterface.Get_bban_TUTI(onIDataCommon.getLoginData().getmMaDvi(), onIDataCommon.getMaNVien());
                    Response<List<MtbBbanTutiModel>> Get_bban_TUTIResponse = Get_bban_TUTICall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = Get_bban_TUTIResponse.code();
                    String errorBody = "";
                    if (Get_bban_TUTIResponse.errorBody() != null)
                        errorBody = Get_bban_TUTIResponse.errorBody().string();
                    if (Get_bban_TUTIResponse.isSuccessful() && statusCode == 200) {
                        dataServer = Get_bban_TUTIResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    //nén ex vào bundle
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();
        List<MtbBbanTutiModel> dataServer = null;
        Bundle resultGet_bban_TUTI = asyncApi.get();


        //Bundle luông khác null
        int statusCode = resultGet_bban_TUTI.getInt(STATUS_CODE, 0);
        String errorBody = resultGet_bban_TUTI.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            dataServer = resultGet_bban_TUTI.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //không show thông báo để đồng bộ các danh mục tiếp theo, chỉ ghi lịch sử
            messageServer.append("-Gặp lỗi khi đồng bộ biên bản TU TI" + statusCode + " " + errorBody + "-");
            infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
        }


        return dataServer;
    }

    private List<MtbCtoModel> callGet_cto(final int ID_BBAN_TRTH) throws Exception {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);

                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        messageServer.append("-Chưa có kết nối internet, vui lòng kiểm tra lại!-");
                        throw new Exception(messageServer.toString());
                    }


                } catch (final Exception e) {

                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });


                    //ném ex ra ngoài để cancel async
                    e.printStackTrace();
                    throw e;
                }

            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<MtbCtoModel> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<MtbCtoModel>> Get_ctoCall = apiInterface.Get_cto(onIDataCommon.getLoginData().getmMaDvi(), String.valueOf(ID_BBAN_TRTH));
                    Response<List<MtbCtoModel>> Get_ctoResponse = Get_ctoCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = Get_ctoResponse.code();
                    String errorBody = "";
                    if (Get_ctoResponse.errorBody() != null)
                        errorBody = Get_ctoResponse.errorBody().string();
                    if (Get_ctoResponse.isSuccessful() && statusCode == 200) {
                        dataServer = Get_ctoResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);
                    return result;

                } catch (Exception e) {
                    //history lỗi bất ngờ thì nén vào Bundle và gửi ra phía ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();

        List<MtbCtoModel> dataServer = null;
        Bundle resultGet_cto = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultGet_cto.getInt(STATUS_CODE, 0);
        String errorBody = resultGet_cto.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            //process to next async
            dataServer = resultGet_cto.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //vẫn cho thực hiện tiếp tục với các ID_BBAN_TRTH nên chỉ lưu history lỗi khi đồng bộ các công tơ trong ID_BBAN_TRTH này.
            messageServer.append("-ID_BBAN_TRTH[" + ID_BBAN_TRTH + "] lỗi đồng bộ " + statusCode + " " + errorBody + "-");
        }


        return dataServer;
    }

    private List<UpdateStatus> callLayDuLieuCmis() throws Exception {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server

                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);

                    //update
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageServer.append("-Đang kiểm tra kết nối tới CMIS...-");
                            updateInfoDownload("Đang kiểm tra kết nối tới CMIS...", 0);
                        }
                    }, Common.DELAY);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        messageServer.append("-Chưa có kết nối internet, vui lòng kiểm tra lại!-");
                        throw new Exception("Chưa có kết nối internet, vui lòng kiểm tra lại!");
                    }
                } catch (final Exception e) {
                    //show snackBar
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });
                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);

                    //ném ex ra bên ngoài thân async để cancel async
                    e.printStackTrace();
                    throw e;
                }
            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<UpdateStatus> dataServer = null;


                try {
                    //call check CMIS connect
                    Call<List<UpdateStatus>> LayDuLieuCmisCall = apiInterface.LayDuLieuCmis(onIDataCommon.getLoginData().getmMaDvi(), onIDataCommon.getMaNVien());
                    Response<List<UpdateStatus>> LayDuLieuCmisCallResponse = LayDuLieuCmisCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = LayDuLieuCmisCallResponse.code();
                    String errorBody = "";
                    if (LayDuLieuCmisCallResponse.errorBody() != null)
                        errorBody = LayDuLieuCmisCallResponse.errorBody().string();
                    if (LayDuLieuCmisCallResponse.isSuccessful() && statusCode == 200) {
                        dataServer = LayDuLieuCmisCallResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);

                    return result;

                } catch (Exception e) {
                    //history lỗi bất ngờ, nén vào bundle và ném ra ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();


        List<UpdateStatus> dataServer = null;
        Bundle resultLayDuLieuCmis = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultLayDuLieuCmis.getInt(STATUS_CODE, 0);
        final String errorBody = resultLayDuLieuCmis.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            dataServer = resultLayDuLieuCmis.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //kết nối thất bại thì hủy toàn bộ các việc đồng bộ các danh mục tiếp theo
            //show thông báo
            //show snackBar
            getView().post(new Runnable() {
                @Override
                public void run() {
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex06.getContent(), errorBody, null);
                }
            });


            // ghi lịch sử
            messageServer.append("-Gặp lỗi khi kết nối CMIS [e" + statusCode + "] " + errorBody + "-");
            infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
        }


        return dataServer;
    }

    private List<MtbBbanModel> callGeT_BBAN() throws Exception {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //update
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageServer.append("-Đang gửi yêu cầu đồng bộ biên bản treo tháo...-");
                            updateInfoDownload("Đang gửi yêu cầu đồng bộ biên bản treo tháo...", 0);
                        }
                    }, Common.DELAY);


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        messageServer.append("-Chưa có kết nối internet, vui lòng kiểm tra lại!-");
                        throw new Exception(messageServer.toString());
                    }

                } catch (final Exception e) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex05.getContent(), e.getMessage(), null);
                        }
                    });

                    infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                    //ném ex ra bên ngoài thân async để cancel async
                    e.printStackTrace();
                    throw e;
                }

            }

            @Override
            public Bundle doInBackground() {
                //ghi vào buldle
                Bundle result = new Bundle();
                List<MtbBbanModel> dataServer = null;

                try {
                    //call check CMIS connect
                    Call<List<MtbBbanModel>> GeT_BBANCall = apiInterface.GeT_BBAN(onIDataCommon.getLoginData().getmMaDvi(), onIDataCommon.getMaNVien());
                    Response<List<MtbBbanModel>> GeT_BBANResponse = GeT_BBANCall.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = GeT_BBANResponse.code();
                    String errorBody = "";
                    if (GeT_BBANResponse.errorBody() != null)
                        errorBody = GeT_BBANResponse.errorBody().string();

                    if (GeT_BBANResponse.isSuccessful() && statusCode == 200) {
                        dataServer = GeT_BBANResponse.body();
                    }


                    result.putInt(STATUS_CODE, statusCode);
                    result.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) dataServer);
                    result.putString(ERROR_BODY, errorBody);

                    return result;

                } catch (Exception e) {
                    //history lỗi bất ngờ, nén vào bundle ném ra ngoài
                    e.printStackTrace();


                    //return result
                    result.putInt(STATUS_CODE, 0);
                    result.putParcelableArrayList(BUNDLE_DATA, null);
                    result.putString(ERROR_BODY, e.getMessage());
                    return result;
                }
            }
        };


        //call
        TthtHnApiInterface.AsyncApi asyncApi = new TthtHnApiInterface.AsyncApi(iAsync);
        asyncApi.execute();


        List<MtbBbanModel> dataServer = null;
        Bundle resultGeT_BBAN = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultGeT_BBAN.getInt(STATUS_CODE, 0);
        String errorBody = resultGeT_BBAN.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            dataServer = resultGeT_BBAN.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //không show thông báo để đồng bộ các danh mục tiếp theo, chỉ ghi lịch sử
            messageServer.append("-Gặp lỗi khi đồng bộ biên bản " + statusCode + " " + errorBody + "-");
            infoSessionDownload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
        }


        return dataServer;
    }

    private boolean isShowNoDataText(int size) {
        rvDownload.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        tvNodata.setVisibility(size == 0 ? View.VISIBLE : View.GONE);

        if (size == 0)
            return true;
        else
            return false;
    }

    //endregion
    private void updateInfoDownload(String titleDownload, int percent) {
        tvPercentDownload.setText(percent + "%");
        pbarDownload.setProgress(percent);
        tvDownload.setText(titleDownload);
    }

    public interface OnListenerTthtHnDownloadFragment {
    }


}
