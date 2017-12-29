package es.vinhnb.ttht.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.views.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.adapter.DoiSoatAdapter;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_HISTORY;
import es.vinhnb.ttht.database.table.TABLE_HISTORY_UPLOAD;
import es.vinhnb.ttht.entity.api.MTBModelNew;
import es.vinhnb.ttht.entity.api.MTB_ResultModel_NEW;
import es.vinhnb.ttht.server.TthtHnApi;
import es.vinhnb.ttht.server.TthtHnApiInterface;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;
import retrofit2.Call;
import retrofit2.Response;

import static es.vinhnb.ttht.common.Common.DELAY;
import static es.vinhnb.ttht.common.Common.DELAY_PROGESS_PBAR;
import static es.vinhnb.ttht.common.Common.TRANG_THAI_CHON_GUI.DA_CHON_GUI;
import static es.vinhnb.ttht.common.Common.TRANG_THAI_DU_LIEU.GUI_THAT_BAI;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_TI;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_TU;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.BUNDLE_DATA;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.ERROR_BODY;
import static es.vinhnb.ttht.server.TthtHnApiInterface.IAsync.STATUS_CODE;

public class TthtHnUploadFragment extends TthtHnBaseFragment {

    private IOnTthtHnUploadFragment mListener;
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
    @BindView(R.id.btn_gui_bban)
    Button btnGuiBBan;

    @BindView(R.id.rl_upload3)
    RelativeLayout rlGuiBBan;


    @BindView(R.id.rl_info_gui)
    RelativeLayout rlInfoGui;


    @BindView(R.id.tv_upload_bb_ok)
    TextView tvUploadBBOk;
    @BindView(R.id.tv_upload_bb_tv_so_bb_error)
    TextView tvUploadBBError;

    @BindView(R.id.tv_nodata2)
    TextView tvNodata;

    private int sobbUpload;
    private Unbinder unbinder;
    private DoiSoatAdapter doiSoatAdapters;
    private DoiSoatAdapter.OnIDataDoiSoatAdapter iIteractor;
    private HashMap<Integer, DoiSoatAdapter.DataDoiSoatAdapter> hashMapData;


    private List<Integer> listID_BBAN_TRTH = new ArrayList<>();
    private TABLE_HISTORY infoSessionUpload;
    private StringBuilder messageServer = new StringBuilder();
    private Boolean isHasErrorServer = false;
    private TABLE_BBAN_CTO tableBbanCto;
    private TABLE_CHITIET_CTO tableChitietCtoTreo;
    private TABLE_CHITIET_CTO tableChitietCtoThao;
    private TABLE_ANH_HIENTRUONG tableAnhCongtoTreo;
    private TABLE_ANH_HIENTRUONG tableAnhCongtoThao;
    private TABLE_ANH_HIENTRUONG tableAnhNiemPhongTreo;
    private TABLE_ANH_HIENTRUONG tableAnhNiemPhongThao;
    private TABLE_CHITIET_TUTI tuTreo;
    private TABLE_CHITIET_TUTI tiTreo;
    private TABLE_ANH_HIENTRUONG anhTU;
    private TABLE_ANH_HIENTRUONG anhNhiThuTU;
    private TABLE_ANH_HIENTRUONG anhNiemPhongTU;
    private TABLE_ANH_HIENTRUONG anhTi;
    private TABLE_ANH_HIENTRUONG anhNhiThuTI;
    private TABLE_ANH_HIENTRUONG anhNiemPhongTI;
    private int sobbUploadOK;
    private int sobbUploadError;
    private List<DoiSoatAdapter.DataDoiSoatAdapter> listDataDoiSoatAdapter;


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
        if (context instanceof IOnTthtHnUploadFragment) {
            mListener = (IOnTthtHnUploadFragment) context;
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

        catchButton();
    }

    private void catchButton() {
        rlGuiBBan.setVisibility(View.GONE);
        rlInfoGui.setVisibility(View.GONE);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (rlInfoGui.getVisibility() == View.GONE) {
                        rlInfoGui.setVisibility(View.VISIBLE);
                        sobbUploadOK = sobbUploadError = 0;
                        updateStatusUpload();
                    }

                    prepareDataUpload();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex09.getContent(), e.getMessage(), null);
                }
            }
        });

        btnGuiBBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (hashMapData.size() != 0) {
                        sobbUpload = 0;
                        for (Map.Entry<Integer, DoiSoatAdapter.DataDoiSoatAdapter> entry : hashMapData.entrySet()) {
                            Integer key = entry.getKey();
                            DoiSoatAdapter.DataDoiSoatAdapter element = entry.getValue();
                            if (element.TRANG_THAI_CHON_GUI == DA_CHON_GUI && (element.TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_GHI || element.TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.CHUA_GHI))
                                sobbUpload++;
                        }
                    }


                    if (rlGuiBBan.getVisibility() == View.GONE) {
                        rlGuiBBan.setVisibility(View.VISIBLE);
                        tvDateUpload.setText(Common.getDateTimeNow(Common.DATE_TIME_TYPE.type9));
                        tvSoBBUpload.setText(sobbUpload + "/" + listDataDoiSoatAdapter.size() + " biên bản");
                        tvPercentUpload.setText("0%");
                        pbarUpload.setProgress(0);

                        rlInfoGui.setVisibility(View.GONE);
                    } else {
                        rlGuiBBan.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex09.getContent(), e.getMessage(), null);
                }
            }
        });
    }


    private void prepareDataUpload() throws Exception {

        if (listID_BBAN_TRTH.size() == 0)
            throw new Exception("Không có biên bản nào được chọn gửi!");


        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<Integer, TABLE_HISTORY_UPLOAD> resultUploadHistory = new HashMap<>();
                try {
                    JSONArray jsonArr = new JSONArray();
                    //region init View
                    List<MTBModelNew> dataUpload = new ArrayList<>();
                    List<MTB_ResultModel_NEW> resultUpload = null;

                    //set default
                    infoSessionUpload = new TABLE_HISTORY();
                    infoSessionUpload.setMA_DVIQLY(onIDataCommon.getLoginData().getmMaDvi());
                    infoSessionUpload.setMA_NVIEN(onIDataCommon.getMaNVien());
                    infoSessionUpload.setTYPE_CALL_API(Common.TYPE_CALL_API.UPLOAD.content);
                    infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.SUCCESS.content);
                    infoSessionUpload.setMESSAGE_RESULT("");
                    sobbUploadOK = sobbUploadError = 0;
                    isHasErrorServer = false;
                    //show Pbar
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            tvUploadInfo.setVisibility(View.VISIBLE);
                            btnUpload.setVisibility(View.GONE);


                            infoSessionUpload.setDATE_CALL_API(Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite2));


                            //convert date sqlite to date UI
                            String dateUI = Common.convertDateToDate(infoSessionUpload.getDATE_CALL_API(), Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type9);
                            tvDateUpload.setText(dateUI);
                            tvDateUpload.setTextColor(getResources().getColor(R.color.tththn_button));
                            tvSoBBUpload.setText(sobbUpload + "/" + listDataDoiSoatAdapter.size() + " biên bản");
                        }
                    });
                    //endregion


                    //region upload


                    //region init data commom

                    String TEN_ANH_CTO_TREO = "";
                    String TEN_ANH_NIEMPHONG_CTO_TREO = "";
                    String TEN_ANH_TU_CTO_TREO = "";
                    String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = "";
                    String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = "";
                    String ANH_CTO_TREO = "";
                    String ANH_NIEMPHONG_CTO_TREO = "";

                    String CREATE_DAY_ANH_CTO_TREO = "";
                    String CREATE_DAY_ANH_NIEMPHONG_CTO_TREO = "";
                    String CREATE_DAY_ANH_TU_CTO_TREO = "";
                    String CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO = "";
                    String CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO = "";

                    String ANH_TU_CTO_TREO = "";
                    String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = "";
                    String ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = "";

                    //TI cong to
                    String TEN_ANH_TI_CTO_TREO = "";
                    String TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = "";
                    String TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = "";

                    String CREATE_DAY_ANH_TI_CTO_TREO = "";
                    String CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO = "";
                    String CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO = "";

                    String ANH_TI_CTO_TREO = "";
                    String ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = "";
                    String ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = "";

                    String TEN_ANH_CTO_THAO = "";
                    String TEN_ANH_NIEMPHONG_CTO_THAO = "";
                    String ANH_CTO_THAO = "";
                    String ANH_NIEMPHONG_CTO_THAO = "";
                    String CREATE_DAY_ANH_CTO_THAO = "";
                    String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO = "";
                    //endregion

                    //region collect data

                    int SO_BBAN_API = 0;
                    int SO_BBAN_TUTI_API = 0;
                    int SO_CTO_TREO_API = 0;
                    int SO_CTO_THAO_API = 0;
                    int SO_TU_API = 0;
                    int SO_TI_API = 0;
                    messageServer.append("\nĐang xử lý dữ liệu để gửi lên máy chủ");
                    for (int i = 0; i < listID_BBAN_TRTH.size(); i++) {
                        SO_BBAN_API++;
                        SO_CTO_TREO_API++;
                        SO_CTO_THAO_API++;

                        TABLE_HISTORY_UPLOAD historyUpload = new TABLE_HISTORY_UPLOAD();
                        try {
                            historyUpload.setID_BBAN_TRTH(listID_BBAN_TRTH.get(i));
                            String[] args = new String[]{String.valueOf(listID_BBAN_TRTH.get(i)), onIDataCommon.getMaNVien()};

                            int ID_BBAN_CONGTO = 0;
                            List<TABLE_BBAN_CTO> tableBbanCtos = mSqlDAO.getBBan(args);
                            if (tableBbanCtos.size() > 0) {
                                ID_BBAN_CONGTO = tableBbanCtos.get(0).getID_BBAN_CONGTO();
                            } else {
                                messageServer.append("\nKhông tìm thấy ID ");
                                throw new Exception("\nKhông tìm thấy dữ liệu ID_BBAN_CONGTO tương ứng với ID_BBAN_TRTH = " + listID_BBAN_TRTH.get(i));
                            }

                            historyUpload.setID_BBAN_CONGTO(ID_BBAN_CONGTO);
                            historyUpload.setMA_NVIEN(onIDataCommon.getMaNVien());
                            historyUpload.setTYPE_RESPONSE_UPLOAD(Common.TYPE_RESPONSE_UPLOAD.LOI_BAT_NGO.content);

                            final int finalI = i;
                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateInfoUpload("Đang xử lý dữ liệu để gửi ...", finalI * 100 / listID_BBAN_TRTH.size());
                                }
                            }, DELAY_PROGESS_PBAR);


                            //region Description
                            //get Data bien ban
                            String[] agrsBB = new String[]{String.valueOf(listID_BBAN_TRTH.get(i)), onIDataCommon.getMaNVien()};
                            List<TABLE_BBAN_CTO> tableBbanCtoList = mSqlDAO.getBBan(agrsBB);

                            if (tableBbanCtoList.size() != 0)
                                tableBbanCto = tableBbanCtoList.get(0);

                            MTBModelNew mtbModelNew = new MTBModelNew();

                            //TODO bien ban
                            mtbModelNew.setID_BBAN_CONGTO(tableBbanCto.getID_BBAN_CONGTO());
                            mtbModelNew.setMA_DVIQLY(tableBbanCto.getMA_DVIQLY());
                            mtbModelNew.setSO_BBAN(tableBbanCto.getSO_BBAN());
                            String sTRANG_THAI = tableBbanCto.getTRANG_THAI();
                            Common.TRANG_THAI TRANG_THAI = Common.TRANG_THAI.findTRANG_THAI(sTRANG_THAI);
                            mtbModelNew.setTRANG_THAI(TRANG_THAI.code);


                            //TODO cong to treo
                            //get Data Chi tiet cong to
                            String[] agrs = new String[]{String.valueOf(listID_BBAN_TRTH.get(i)), Common.MA_BDONG.B.code, onIDataCommon.getMaNVien()};
                            List<TABLE_CHITIET_CTO> tableChitietCtoList = mSqlDAO.getChiTietCongto(agrs);
                            if (tableChitietCtoList.size() != 0)
                                tableChitietCtoTreo = tableChitietCtoList.get(0);

                            mtbModelNew.setID_BBAN_CONGTO(tableBbanCto.getID_BBAN_CONGTO());

                            mtbModelNew.setLAN_CTO_TREO(tableChitietCtoTreo.getLAN());
                            mtbModelNew.setVTRI_TREO_THAO_CTO_TREO(tableChitietCtoTreo.getVTRI_TREO());
                            mtbModelNew.setSOVIEN_CBOOC_CTO_TREO(tableChitietCtoTreo.getSO_VIENCBOOC());
                            mtbModelNew.setLOAI_HOM_CTO_TREO(tableChitietCtoTreo.getLOAI_HOM());
                            mtbModelNew.setSOVIEN_CHOM_CTO_TREO(tableChitietCtoTreo.getSO_VIENCHOM());
                            mtbModelNew.setHS_NHAN_CTO_TREO(tableChitietCtoTreo.getHS_NHAN());
                            mtbModelNew.setSOVIEN_CHIKDINH_CTO_TREO(tableChitietCtoTreo.getSOVIEN_CHIKDINH());
                            mtbModelNew.setTEM_CQUANG_CTO_TREO(tableChitietCtoTreo.getTEM_CQUANG());
                            mtbModelNew.setSO_KIM_NIEM_CHI_CTO_TREO(tableChitietCtoTreo.getSO_KIM_NIEM_CHI());
                            mtbModelNew.setTTRANG_NPHONG_CTO_TREO(tableChitietCtoTreo.getTTRANG_NPHONG());
                            mtbModelNew.setTEN_LOAI_CTO_CTO_TREO(tableChitietCtoTreo.getTEN_LOAI_CTO());
                            mtbModelNew.setPHUONG_THUC_DO_XA_CTO_TREO(tableChitietCtoTreo.getPHUONG_THUC_DO_XA());
                            mtbModelNew.setGHI_CHU_CTO_TREO(tableChitietCtoTreo.getGHI_CHU());

                            mtbModelNew.setDIEN_AP_CTO_TREO(tableChitietCtoTreo.getDIEN_AP());
                            mtbModelNew.setDONG_DIEN_CTO_TREO(tableChitietCtoTreo.getDONG_DIEN());
                            mtbModelNew.setHANGSO_K_CTO_TREO(tableChitietCtoTreo.getHANGSO_K());
                            mtbModelNew.setSO_TU_CTO_TREO(tableChitietCtoTreo.getSO_TU());
                            mtbModelNew.setSO_TI_CTO_TREO(tableChitietCtoTreo.getSO_TI());
                            mtbModelNew.setCHI_SO_CTO_TREO(tableChitietCtoTreo.getCHI_SO());
                            mtbModelNew.setCCX_CTO_TREO(tableChitietCtoTreo.getCAP_CX_SAULAP_TUTI());


                            //TODO update bang DU_LIEU_HIEN_TRUONG
                            //get info ẢNH công tơ treo
                            agrs = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tableChitietCtoTreo.getID_CHITIET_CTO())};
                            List<TABLE_ANH_HIENTRUONG> tableAnhList = mSqlDAO.getAnhHienTruong(agrs, Common.TYPE_IMAGE.IMAGE_CONG_TO);
                            if (tableAnhList.size() != 0) {
                                tableAnhCongtoTreo = tableAnhList.get(0);


                                //anh cong to treo
                                TEN_ANH_CTO_TREO = tableAnhCongtoTreo.getTEN_ANH();
                                if (!TextUtils.isEmpty(TEN_ANH_CTO_TREO)) {
                                    String PATH_ANH = Common.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CTO_TREO;
                                    ANH_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                    CREATE_DAY_ANH_CTO_TREO = tableAnhCongtoTreo.getCREATE_DAY();
                                }

                                mtbModelNew.setTEN_ANH_CTO_TREO(TEN_ANH_CTO_TREO);
                                mtbModelNew.setANH_CTO_TREO(ANH_CTO_TREO);
                                mtbModelNew.setCREATE_DAY_ANH_CTO_TREO(CREATE_DAY_ANH_CTO_TREO);
                            }


                            //get info ẢNH niêm phong treo
                            agrs = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tableChitietCtoTreo.getID_CHITIET_CTO())};
                            tableAnhList = mSqlDAO.getAnhHienTruong(agrs, Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
                            if (tableAnhList.size() != 0) {
                                tableAnhNiemPhongTreo = tableAnhList.get(0);


                                //anh cong to treo
                                TEN_ANH_NIEMPHONG_CTO_TREO = tableAnhNiemPhongTreo.getTEN_ANH();
                                if (!TextUtils.isEmpty(TEN_ANH_NIEMPHONG_CTO_TREO)) {
                                    String PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_NIEMPHONG_CTO_TREO;
                                    ANH_NIEMPHONG_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                    CREATE_DAY_ANH_NIEMPHONG_CTO_TREO = tableAnhNiemPhongTreo.getCREATE_DAY();
                                }


                                mtbModelNew.setTEN_ANH_NIEMPHONG_CTO_TREO(TEN_ANH_NIEMPHONG_CTO_TREO);
                                mtbModelNew.setANH_NIEMPHONG_CTO_TREO(ANH_NIEMPHONG_CTO_TREO);
                                mtbModelNew.setCREATE_DAY_ANH_NIEMPHONG_CTO_TREO(CREATE_DAY_ANH_NIEMPHONG_CTO_TREO);
                            }


                            String PATH_ANH = "";
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                            //TODO anh TU TI Treo

                            mtbModelNew.setID_CHITIET_CTO_TREO(tableChitietCtoTreo.getID_CHITIET_CTO());
                            mtbModelNew.setID_BBAN_TUTI_CTO_TREO(tableChitietCtoTreo.getID_BBAN_TUTI());


                            if (tableChitietCtoTreo.getID_BBAN_TUTI() != 0) {
                                SO_BBAN_TUTI_API++;
                                //get Data chi tiet tuti
                                List<TABLE_CHITIET_TUTI> tableChitietTutiList = mSqlDAO.getChitietTuTi(tableChitietCtoTreo.getID_BBAN_TUTI(), onIDataCommon.getMaNVien());
                                for (int j = 0; j < tableChitietTutiList.size(); j++) {
                                    TABLE_CHITIET_TUTI tableChitietTuti = tableChitietTutiList.get(j);

                                    //TODO chỉ lấy dữ liệu TU và TI Treo để gửi lên máy chủ
                                    //nếu là TU
                                    if (tableChitietTuti.getIS_TU().equals(String.valueOf(Common.IS_TU.TU.code))) {
                                        //MA_BDONG cho biết là TU Treo hay tháo
                                        if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.B.code)) {
                                            tuTreo = tableChitietTuti;
                                            mtbModelNew.setID_CHITIET_TUTI_TU_CTO_TREO(tuTreo.getID_CHITIET_TUTI());
                                            SO_TU_API++;
                                        }
                                    }


                                    //nếu là TI
                                    if (tableChitietTuti.getIS_TU().equals(String.valueOf(Common.IS_TU.TI.code))) {
                                        //MA_BDONG cho biết là TU Treo hay tháo

                                        if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.B.code)) {
                                            tiTreo = tableChitietTuti;
                                            mtbModelNew.setID_CHITIET_TUTI_TI_CTO_TREO(tiTreo.getID_CHITIET_TUTI());
                                            SO_TI_API++;
                                        }
                                    }
                                }


                                //get ảnh tu treo
                                if (tuTreo != null) {
                                    String[] argsAnh = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tuTreo.getID_BBAN_TUTI()), String.valueOf(tuTreo.getID_CHITIET_TUTI())};
                                    List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, IMAGE_TU);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhTU = tableAnhHientruongList.get(0);


                                        TEN_ANH_TU_CTO_TREO = anhTU.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TU_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_CTO_TREO;
                                            ANH_TU_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_TU_CTO_TREO = anhTU.getCREATE_DAY();
                                        }

                                        mtbModelNew.setTEN_ANH_TU_CTO_TREO(TEN_ANH_TU_CTO_TREO);
                                        mtbModelNew.setANH_TU_CTO_TREO(ANH_TU_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_TU_CTO_TREO(CREATE_DAY_ANH_TU_CTO_TREO);

                                    }


                                    //get anh nhi thu tu treo
                                    tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhNhiThuTU = tableAnhHientruongList.get(0);


                                        //anh TU mach nhi thu
                                        TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = anhNhiThuTU.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
                                            ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO = anhNhiThuTU.getCREATE_DAY();
                                        }

                                        mtbModelNew.setTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO(TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO);
                                        mtbModelNew.setANH_TU_ANH_MACH_NHI_THU_CTO_TREO(ANH_TU_ANH_MACH_NHI_THU_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO(CREATE_DAY_ANH_MACH_NHI_THU_TU_CTO_TREO);
                                    }


                                    tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhNiemPhongTU = tableAnhHientruongList.get(0);


                                        //anh TU mach niem phong
                                        TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = anhNiemPhongTU.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
                                            ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO = anhNiemPhongTU.getCREATE_DAY();
                                        }


                                        mtbModelNew.setTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO(TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO);
                                        mtbModelNew.setANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO(ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO(CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO);
                                    }
                                }


                                //get ảnh ti treo
                                if (tiTreo != null) {
                                    String[] argsAnh = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tiTreo.getID_BBAN_TUTI()), String.valueOf(tiTreo.getID_CHITIET_TUTI())};
                                    List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, IMAGE_TI);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhTi = tableAnhHientruongList.get(0);


                                        TEN_ANH_TI_CTO_TREO = anhTi.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TI_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_CTO_TREO;
                                            ANH_TI_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_TI_CTO_TREO = anhTi.getCREATE_DAY();
                                        }

                                        mtbModelNew.setTEN_ANH_TI_CTO_TREO(TEN_ANH_TI_CTO_TREO);
                                        mtbModelNew.setANH_TI_CTO_TREO(ANH_TI_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_TI_CTO_TREO(CREATE_DAY_ANH_TI_CTO_TREO);
                                    }


                                    //get anh nhi thu TI treo
                                    tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhNhiThuTI = tableAnhHientruongList.get(0);

                                        //anh TI mach nhi thu
                                        TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = anhNhiThuTI.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
                                            ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO = anhNhiThuTI.getCREATE_DAY();
                                        }

                                        mtbModelNew.setTEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO(TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO);
                                        mtbModelNew.setANH_TI_ANH_MACH_NHI_THU_CTO_TREO(ANH_TI_ANH_MACH_NHI_THU_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO(CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO);

                                    }


                                    tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                                    if (tableAnhHientruongList.size() != 0) {
                                        anhNiemPhongTI = tableAnhHientruongList.get(0);


                                        //anh TI mach niem phong
                                        TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = anhNiemPhongTI.getTEN_ANH();
                                        if (!TextUtils.isEmpty(TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO)) {
                                            PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
                                            ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO = anhNiemPhongTI.getCREATE_DAY();
                                        }


                                        mtbModelNew.setTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
                                        mtbModelNew.setANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
                                        mtbModelNew.setCREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO(CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO);
                                    }
                                }
                            }


                            //TODO cong to thao
                            agrs = new String[]{String.valueOf(listID_BBAN_TRTH.get(i)), Common.MA_BDONG.E.code, onIDataCommon.getMaNVien()};
                            tableChitietCtoList = mSqlDAO.getChiTietCongto(agrs);
                            if (tableChitietCtoList.size() != 0)
                                tableChitietCtoThao = tableChitietCtoList.get(0);


                            mtbModelNew.setID_CHITIET_CTO_THAO(tableChitietCtoThao.getID_CHITIET_CTO());

                            mtbModelNew.setTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
                            mtbModelNew.setANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO(ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
                            mtbModelNew.setCREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO(CREATE_DAY_ANH_MACH_NIEM_PHONG_TU_CTO_TREO);


                            mtbModelNew.setLAN_CTO_THAO(tableChitietCtoThao.getLAN());
                            mtbModelNew.setVTRI_TREO_THAO_CTO_THAO(tableChitietCtoThao.getVTRI_TREO());
                            mtbModelNew.setSOVIEN_CBOOC_CTO_THAO(tableChitietCtoThao.getSO_VIENCBOOC());
                            mtbModelNew.setLOAI_HOM_CTO_THAO(tableChitietCtoThao.getLOAI_HOM());
                            mtbModelNew.setSOVIEN_CHOM_CTO_THAO(tableChitietCtoThao.getSO_VIENCHOM());
                            mtbModelNew.setTEM_CQUANG_CTO_THAO(tableChitietCtoThao.getTEM_CQUANG());
                            mtbModelNew.setSOVIEN_CHIKDINH_CTO_THAO(tableChitietCtoThao.getSOVIEN_CHIKDINH());
                            mtbModelNew.setSO_KIM_NIEM_CHI_CTO_THAO(tableChitietCtoThao.getSO_KIM_NIEM_CHI());
                            mtbModelNew.setTTRANG_NPHONG_CTO_THAO(tableChitietCtoThao.getTTRANG_NPHONG());
                            mtbModelNew.setTEN_LOAI_CTO_CTO_THAO(tableChitietCtoThao.getTEN_LOAI_CTO());
                            mtbModelNew.setPHUONG_THUC_DO_XA_CTO_THAO(tableChitietCtoThao.getPHUONG_THUC_DO_XA());
                            mtbModelNew.setGHI_CHU_CTO_THAO(tableChitietCtoThao.getGHI_CHU());


                            mtbModelNew.setHS_NHAN_CTO_THAO(tableChitietCtoThao.getHS_NHAN());
                            mtbModelNew.setDIEN_AP_CTO_THAO(tableChitietCtoThao.getDIEN_AP());
                            mtbModelNew.setDONG_DIEN_CTO_THAO(tableChitietCtoThao.getDONG_DIEN());
                            mtbModelNew.setHANGSO_K_CTO_THAO(tableChitietCtoThao.getHANGSO_K());
                            mtbModelNew.setSO_TU_CTO_THAO(tableChitietCtoThao.getSO_TU());
                            mtbModelNew.setSO_TI_CTO_THAO(tableChitietCtoThao.getSO_TI());
                            mtbModelNew.setCHI_SO_CTO_THAO(tableChitietCtoThao.getCHI_SO());
                            mtbModelNew.setCCX_CTO_THAO(tableChitietCtoThao.getCAP_CX_SAULAP_TUTI());


                            //TODO lấy ảnh bên công tơ tháo
                            agrs = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tableChitietCtoThao.getID_CHITIET_CTO())};
                            tableAnhList = mSqlDAO.getAnhHienTruong(agrs, Common.TYPE_IMAGE.IMAGE_CONG_TO);
                            if (tableAnhList.size() != 0) {
                                tableAnhCongtoThao = tableAnhList.get(0);


                                //anh cong to THAO
                                TEN_ANH_CTO_THAO = tableAnhCongtoThao.getTEN_ANH();
                                if (!TextUtils.isEmpty(TEN_ANH_CTO_THAO)) {
                                    PATH_ANH = Common.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CTO_THAO;
                                    ANH_CTO_THAO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                    CREATE_DAY_ANH_CTO_THAO = tableAnhCongtoThao.getCREATE_DAY();
                                }

                                mtbModelNew.setTEN_ANH_CTO_THAO(TEN_ANH_CTO_THAO);
                                mtbModelNew.setANH_CTO_THAO(ANH_CTO_THAO);
                                mtbModelNew.setCREATE_DAY_ANH_CTO_THAO(CREATE_DAY_ANH_CTO_THAO);
                            }

                            //get info ẢNH niêm phong THAO
                            agrs = new String[]{onIDataCommon.getMaNVien(), String.valueOf(tableChitietCtoThao.getID_CHITIET_CTO())};
                            tableAnhList = mSqlDAO.getAnhHienTruong(agrs, Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
                            if (tableAnhList.size() != 0) {
                                tableAnhNiemPhongThao = tableAnhList.get(0);


                                //anh cong to THAO
                                TEN_ANH_NIEMPHONG_CTO_THAO = tableAnhNiemPhongThao.getTEN_ANH();
                                if (!TextUtils.isEmpty(TEN_ANH_NIEMPHONG_CTO_THAO)) {
                                    PATH_ANH = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_NIEMPHONG_CTO_THAO;
                                    ANH_NIEMPHONG_CTO_THAO = (Common.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : Common.convertBitmapToByte64(PATH_ANH);
                                    CREATE_DAY_ANH_NIEMPHONG_CTO_THAO = tableAnhNiemPhongThao.getCREATE_DAY();
                                }


                                mtbModelNew.setTEN_ANH_NIEMPHONG_CTO_THAO(TEN_ANH_NIEMPHONG_CTO_THAO);
                                mtbModelNew.setANH_NIEMPHONG_CTO_THAO(ANH_NIEMPHONG_CTO_THAO);
                                mtbModelNew.setCREATE_DAY_ANH_NIEMPHONG_CTO_THAO(CREATE_DAY_ANH_NIEMPHONG_CTO_THAO);
                            }


                            //create object upload
                            //endregion

                            dataUpload.add(mtbModelNew);

                        } catch (Exception e) {
                            e.printStackTrace();
                            historyUpload.setTYPE_RESPONSE_UPLOAD(Common.TYPE_RESPONSE_UPLOAD.LOI_BAT_NGO.content);
                            messageServer.append("\nLỗi khi khởi tạo dữ liệu upload của biên bản có ID_BBAN_TRTH: " + listID_BBAN_TRTH.get(i) + ". Nội dung lỗi: " + e.getMessage());

                            isHasErrorServer = true;
                            sobbUploadError++;
                            getView().post(new Runnable() {
                                @Override
                                public void run() {
                                    updateStatusUpload();
                                }
                            });
                            continue;
                        }

                        resultUploadHistory.put(historyUpload.getID_BBAN_CONGTO(), historyUpload);
                    }


                    //endregion

                    //nếu không có dữ liệu return
                    if (dataUpload.size() == 0) {
                        messageServer.append("\nKhông có biên bản nào được xử lý thành công để gửi lên máy chủ.");
                        sobbUploadError = sobbUpload;
                        infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                        infoSessionUpload.setMESSAGE_RESULT(messageServer.toString());

                        getView().post(new Runnable() {
                            @Override
                            public void run() {
                                sobbUploadError = listID_BBAN_TRTH.size();
                                updateStatusUpload();
                                ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex09.getContent(), null, null);
                            }
                        });

                        return;
                    }

                    //có dữ liệu nhưng  có lỗi xảy ra thì vẫn cho phép tiếp tục
                    if (isHasErrorServer) {
                        infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                        infoSessionUpload.setMESSAGE_RESULT(messageServer.toString());
                    }

                    messageServer.append("\nBắt đầu gửi dữ liệu biên bản lên máy chủ.");

                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateInfoUpload("Đang gửi dữ liệu biên bản lên máy chủ ...", 100);
                        }
                    }, DELAY_PROGESS_PBAR);

                    //region upload
                    resultUpload = callPostMTBWithImage(dataUpload);
                    infoSessionUpload.setSO_BBAN_API(SO_BBAN_API);
                    infoSessionUpload.setSO_BBAN_TUTI_API(SO_BBAN_TUTI_API);
                    infoSessionUpload.setSO_CTO_TREO_API(SO_CTO_TREO_API);
                    infoSessionUpload.setSO_CTO_THAO_API(SO_CTO_THAO_API);
                    infoSessionUpload.setSO_TU_API(SO_TU_API);
                    infoSessionUpload.setSO_TI_API(SO_TI_API);

                    //nếu null = có lỗi khi gửi dữ liệu treo tháo
                    if (resultUpload == null) {
                        //gửi thất bại
                        resultUpload = new ArrayList<>();
                        messageServer.append("\nKhông nhận được phản hồi từ máy chủ.");
                        isHasErrorServer = true;
                        sobbUploadError = sobbUpload;

                        for (Integer ID_BBAN_CONGTO : resultUploadHistory.keySet()
                                ) {
                            if (TextUtils.isEmpty(resultUploadHistory.get(ID_BBAN_CONGTO).getMESSAGE_RESPONSE()))
                                resultUploadHistory.get(ID_BBAN_CONGTO).setMESSAGE_RESPONSE("Không kết nối được máy chủ khi gửi biên bản!");
                        }

                    } else if (resultUpload.size() == 0) {
                        //kết nối gửi thành công nhưng ko có dữ liệu trả về nên quy là thất bại
                        //nếu rỗng thì hiện tại có biên bản nhưng không có công tơ, vẫn cho tiếp tục thực hiện call các api khác
                        messageServer.append("\nKhông nhận được phản hồi từ máy chủ.");
                        for (Integer ID_BBAN_CONGTO : resultUploadHistory.keySet()
                                ) {
                            if (TextUtils.isEmpty(resultUploadHistory.get(ID_BBAN_CONGTO).getMESSAGE_RESPONSE()))
                                resultUploadHistory.get(ID_BBAN_CONGTO).setMESSAGE_RESPONSE("Không nhận được dữ liệu hồi đáp từ máy chủ khi gửi biên bản!");
                        }

                        isHasErrorServer = true;
                        sobbUploadError = sobbUpload;

                    }

                    final int finalResultUpload = resultUpload.size();
                    if (finalResultUpload > 0)
                        messageServer.append("\nBắt đầu xử lý dữ liệu nhận được từ phản hồi của máy chủ.");
                    for (int i = 0; i < resultUpload.size(); i++) {
                        MTB_ResultModel_NEW mtbResultModelNew = resultUpload.get(i);
                        final int finalI = i;


                        getView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateInfoUpload("Đang xử lý dữ liệu trả về từ máy chủ ...", finalI * 100 / finalResultUpload);
                            }
                        }, DELAY_PROGESS_PBAR);


                        //server đang viết nhầm ID_BBAN_TRTH nhưng lại trả về ID_BBAN_CONGTO
                        Common.TYPE_RESPONSE_UPLOAD typeResponseUpload = Common.TYPE_RESPONSE_UPLOAD.findByCode(TextUtils.isEmpty(mtbResultModelNew.TRANG_THAI) ? "" : mtbResultModelNew.TRANG_THAI);
                        int ID_BBAN_CONGTO = Integer.parseInt(mtbResultModelNew.ID_BBAN_TRTH);
                        resultUploadHistory.get(ID_BBAN_CONGTO).setTYPE_RESPONSE_UPLOAD(typeResponseUpload.content);

                        String[] agrsBB = new String[]{String.valueOf(resultUploadHistory.get(ID_BBAN_CONGTO).getID_BBAN_TRTH()), onIDataCommon.getMaNVien()};
                        List<TABLE_BBAN_CTO> tableBbanCtoList = mSqlDAO.getBBan(agrsBB);
                        tableBbanCto = null;
                        if (tableBbanCtoList.size() != 0)
                            tableBbanCto = tableBbanCtoList.get(0);

                        if (typeResponseUpload == Common.TYPE_RESPONSE_UPLOAD.LOI_BAT_NGO)
                            resultUploadHistory.get(ID_BBAN_CONGTO).setMESSAGE_RESPONSE(mtbResultModelNew.ERROR);

                        try {
                            updateDataAfterUpload(mtbResultModelNew);
                        } catch (Exception e) {
                            messageServer.append("\nGặp vấn đề khi xử khi dữ liệu của biên bản có ID_BBAN_TRTH " + mtbResultModelNew.ID_BBAN_TRTH + ". Nội dung lỗi: " + e.getMessage());
                            continue;
                        }


//                        if (typeResponseUpload == Common.TYPE_RESPONSE_UPLOAD.LOI_BAT_NGO)
//                        {
//                            int ID_BBAN_CONGTO = 0;
//                            resultUploadHistory.get(ID_BBAN_CONGTO).setTYPE_RESPONSE_UPLOAD(typeResponseUpload.content);
//                            resultUploadHistory.get(ID_BBAN_CONGTO).setMESSAGE_RESPONSE(mtbResultModelNew.ERROR);
//                        }
//                        else {
//                            int ID_BBAN_CONGTO = Integer.parseInt(mtbResultModelNew.ID_BBAN_TRTH);
//                            resultUploadHistory.get(ID_BBAN_CONGTO).setTYPE_RESPONSE_UPLOAD(typeResponseUpload.content);
//                        }

                    }
                    //endregion
                    //endregion

                } catch (final Exception e) {
                    e.printStackTrace();
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex10.getContent(), e.getMessage(), null);
                        }
                    });

                } finally {
                    //set UI
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //update title download
                            messageServer.append("\n Kết thúc quá trình gửi dữ liệu lên máy chủ!");
                            updateInfoUpload("Kết thúc quá trình gửi dữ liệu lên máy chủ!", 100);

                            if (isHasErrorServer) {
                                infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                                ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex10.getContent(), messageServer.toString(), null);

                                return;
                            }

                        }
                    }, DELAY);


                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //hide Pbar
                            tvUploadInfo.setVisibility(View.GONE);
                            btnUpload.setVisibility(View.VISIBLE);
                            //convert date sqlite to date UI
                            String dateUI = Common.convertDateToDate(infoSessionUpload.getDATE_CALL_API(), Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type9);
                            tvDateUpload.setText(dateUI);
                        }
                    }, Common.DELAY);


                    //ghi history phiên download
                    try {
                        infoSessionUpload.setMESSAGE_RESULT(messageServer.toString());
                        infoSessionUpload.setID_TABLE_HISTORY((int) mSqlDAO.insert(TABLE_HISTORY.class, infoSessionUpload));
                        for (Integer ID_BBAN_CONGTO : resultUploadHistory.keySet()
                                ) {
                            try {
                                resultUploadHistory.get(ID_BBAN_CONGTO).setID_TABLE_HISTORY(infoSessionUpload.getID_TABLE_HISTORY());
                                resultUploadHistory.get(ID_BBAN_CONGTO).setID_TABLE_HISTORY_DETAIL((int) mSqlDAO.insert(TABLE_HISTORY_UPLOAD.class, resultUploadHistory.get(ID_BBAN_CONGTO)));
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                continue;
                            }
                        }
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
                            //update list view
                            updateListViewAfterUpload();
                        }
                    });
                }
            }
        }).start();

    }

    private void updateListViewAfterUpload() {

        if (rvDoiSoat.getAdapter() instanceof DoiSoatAdapter && doiSoatAdapters != null) {
            doiSoatAdapters = (DoiSoatAdapter) rvDoiSoat.getAdapter();
            doiSoatAdapters.refresh(new ArrayList<>(hashMapData.values()));
            rvDoiSoat.invalidate();
        }
    }

    private void updateDataAfterUpload(MTB_ResultModel_NEW mtbResultModelNew) throws Exception {
        Common.TYPE_RESPONSE_UPLOAD typeResponseUpload = Common.TYPE_RESPONSE_UPLOAD.findByCode(mtbResultModelNew.TRANG_THAI);

        TABLE_BBAN_CTO tableBbanCtoOld = (TABLE_BBAN_CTO) tableBbanCto.clone();
        switch (typeResponseUpload) {
            case GUI_CMIS_THATBAI:
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.GUI_THAT_BAI.content);
                sobbUploadError++;
                break;
            case DANG_CHO_CMIS_XACNHAN:
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS.content);
                sobbUploadOK++;
                break;
            case DA_TON_TAI_GUI_TRUOC_DO:
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_TON_TAI_GUI_TRUOC_DO.content);
                sobbUploadError++;
                break;
            case CMIS_XACNHAN_OK:
                sobbUploadError++;
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS.content);
                break;
            case HET_HIEU_LUC:
                sobbUploadError++;
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.HET_HIEU_LUC.content);
                break;
            case LOI_BAT_NGO:
                sobbUploadError++;
                tableBbanCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.GUI_THAT_BAI.content);
                infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
                infoSessionUpload.setMESSAGE_RESULT(mtbResultModelNew.ERROR);
                break;
        }

        //update table BBAN
        tableBbanCto.setID_TABLE_BBAN_CTO((int) mSqlDAO.updateRows(TABLE_BBAN_CTO.class, tableBbanCtoOld, tableBbanCto));

        hashMapData.get(tableBbanCto.getID_BBAN_TRTH()).TRANG_THAI_DU_LIEU = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(tableBbanCto.getTRANG_THAI_DU_LIEU());

        //vẫn để đối soát để hiển thị khi vào lại


        getView().post(new Runnable() {
            @Override
            public void run() {
                updateStatusUpload();
            }
        });


    }

    private List<MTB_ResultModel_NEW> callPostMTBWithImage(final List<MTBModelNew> dataUpload) throws ExecutionException, InterruptedException {
        TthtHnApiInterface.IAsync iAsync = new TthtHnApiInterface.IAsync() {
            @Override
            public void onPreExecute() throws Exception {
                try {
                    //init protocol server
                    apiInterface = TthtHnApi.getClient().create(TthtHnApiInterface.class);


                    //update
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            updateInfoUpload("Đang gửi yêu cầu cho phép gửi dữ liệu lên máy chủ....", 0);
                        }
                    });


                    //check internet
                    if (!Common.isNetworkConnected(getContext())) {
                        messageServer.append("\nChưa có kết nối internet, vui lòng kiểm tra lại!");
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
                List<MTB_ResultModel_NEW> dataServer = null;

                try {
                    //call check CMIS connect
                    Call<List<MTB_ResultModel_NEW>> callPostMTBWithImage = apiInterface.PostMTBWithImage(dataUpload);
                    Response<List<MTB_ResultModel_NEW>> PostMTBWithImageResponse = callPostMTBWithImage.execute();


                    //nếu có response về thì check code 200 (OK) hoặc code khác 200 (FAIL)
                    int statusCode = PostMTBWithImageResponse.code();
                    String errorBody = "";
                    if (PostMTBWithImageResponse.errorBody() != null)
                        errorBody = PostMTBWithImageResponse.errorBody().string();

                    if (PostMTBWithImageResponse.isSuccessful() && statusCode == 200) {
                        dataServer = PostMTBWithImageResponse.body();
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


        List<MTB_ResultModel_NEW> dataServer = null;
        Bundle resultPostMTBWithImage = asyncApi.get();


        //Bundle luôn khác null
        int statusCode = resultPostMTBWithImage.getInt(STATUS_CODE, 0);
        String errorBody = resultPostMTBWithImage.getString(ERROR_BODY, "");


        //check case
        if (statusCode == 200) {
            dataServer = resultPostMTBWithImage.getParcelableArrayList(BUNDLE_DATA);
        } else {
            //không show thông báo để đồng bộ các danh mục tiếp theo, chỉ ghi lịch sử
            messageServer.append("\nKhông nhận được dữ liệu phản hổi khi upload dứ liệu biên bản " + statusCode + " " + errorBody + "-");
            infoSessionUpload.setTYPE_RESULT(Common.TYPE_RESULT.ERROR.content);
        }


        return dataServer;
    }

    private void updateInfoUpload(String titleDownload, int percent) {
        tvPercentUpload.setText(percent + "%");
        pbarUpload.setProgress(percent);
        tvUploadInfo.setText(titleDownload);
    }

    private void updateStatusUpload() {
        tvUploadBBOk.setText(sobbUploadOK + " biên bản");
        tvUploadBBError.setText(sobbUploadError + " biên bản");
    }

    private void fillDataDoiSoat() throws Exception {
        listID_BBAN_TRTH.clear();

        //get anh treo
        String[] agrs = new String[]{onIDataCommon.getMaNVien(), onIDataCommon.getMaNVien(), Common.TYPE_IMAGE.IMAGE_CONG_TO.code, onIDataCommon.getMaNVien()};
        List<DoiSoatAdapter.DataDoiSoat> data = mSqlDAO.getDoiSoatAdapter(agrs);
        sobbUpload = 0;
        hashMapData = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).MA_BDONG == Common.MA_BDONG.B) {
                DoiSoatAdapter.DataDoiSoatAdapter element = new DoiSoatAdapter.DataDoiSoatAdapter();
                element.CHI_SO_TREO = data.get(i).CHI_SO;
                element.DIA_CHI_HOADON = data.get(i).DIA_CHI_HOADON;
                element.TEN_KH = data.get(i).TEN_KH;
                element.LOAI_CTO_TREO = data.get(i).LOAI_CTO;
                element.TEN_ANH_TREO = data.get(i).TEN_ANH;
                element.TRANG_THAI_DU_LIEU = data.get(i).TRANG_THAI_DU_LIEU;
                element.TRANG_THAI_CHON_GUI = data.get(i).TRANG_THAI_CHON_GUI;
                element.TRANG_THAI_DOI_SOAT = data.get(i).TRANG_THAI_DOI_SOAT;
                element.ID_BBAN_TRTH = data.get(i).ID_BBAN_TRTH;
                element.SO_BBAN = data.get(i).SO_BBAN;
                if (element.TRANG_THAI_CHON_GUI == DA_CHON_GUI)
                    sobbUpload++;
                hashMapData.put(data.get(i).ID_BBAN_TRTH, element);
            }
        }

        //update  anh thao
        for (int i = 0; i < data.size(); i++) {
            if (hashMapData.containsKey(data.get(i).ID_BBAN_TRTH)) {
                if (data.get(i).MA_BDONG == Common.MA_BDONG.E) {
                    hashMapData.get(data.get(i).ID_BBAN_TRTH).LOAI_CTO_THAO = data.get(i).LOAI_CTO;
                    hashMapData.get(data.get(i).ID_BBAN_TRTH).CHI_SO_THAO = data.get(i).CHI_SO;
                    hashMapData.get(data.get(i).ID_BBAN_TRTH).TEN_ANH_THAO = data.get(i).TEN_ANH;
                    hashMapData.get(data.get(i).ID_BBAN_TRTH).TRANG_THAI_DU_LIEU = data.get(i).TRANG_THAI_DU_LIEU;
                    //tăng số biên bản có thể upload
                    if (data.get(i).TRANG_THAI_CHON_GUI == Common.TRANG_THAI_CHON_GUI.DA_CHON_GUI && (data.get(i).TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_GHI || data.get(i).TRANG_THAI_DU_LIEU == GUI_THAT_BAI)) {
                        listID_BBAN_TRTH.add(data.get(i).ID_BBAN_TRTH);
                    }
                }
            }
        }

        listDataDoiSoatAdapter = new ArrayList<>(hashMapData.values());

        //kết hợp dữ liệu 2 loại này
        //fill data doisoat
        if (doiSoatAdapters == null) {
            if (iIteractor == null)
                iIteractor = new DoiSoatAdapter.OnIDataDoiSoatAdapter() {
                    @Override
                    public void doClickChonGui(int pos, DoiSoatAdapter.DataDoiSoatAdapter dataDoiSoatAdapter) {
                        try {
                            if (listDataDoiSoatAdapter.size() != 0 && pos < listDataDoiSoatAdapter.size()) {

                                switch (listDataDoiSoatAdapter.get(pos).TRANG_THAI_CHON_GUI) {
                                    case CHUA_CHON_GUI:
                                        listDataDoiSoatAdapter.get(pos).TRANG_THAI_CHON_GUI = Common.TRANG_THAI_CHON_GUI.DA_CHON_GUI;
                                        break;
                                    case DA_CHON_GUI:
                                        listDataDoiSoatAdapter.get(pos).TRANG_THAI_CHON_GUI = Common.TRANG_THAI_CHON_GUI.CHUA_CHON_GUI;
                                        break;
                                }


                                //save data
                                String[] args = new String[]{String.valueOf(listDataDoiSoatAdapter.get(pos).ID_BBAN_TRTH), onIDataCommon.getMaNVien()};
                                List<TABLE_BBAN_CTO> tableBbanCtoList = (List<TABLE_BBAN_CTO>) mSqlDAO.getBBan(args);

                                if (tableBbanCtoList.size() != 0) {
                                    TABLE_BBAN_CTO tableBbanCto = tableBbanCtoList.get(0);
                                    TABLE_BBAN_CTO tableBbanCtoOld = (TABLE_BBAN_CTO) tableBbanCto.clone();

                                    tableBbanCto.setTRANG_THAI_CHON_GUI(listDataDoiSoatAdapter.get(pos).TRANG_THAI_CHON_GUI.content);

                                    mSqlDAO.updateRows(TABLE_BBAN_CTO.class, tableBbanCtoOld, tableBbanCto);

                                    doiSoatAdapters.refresh(listDataDoiSoatAdapter);
                                    rvDoiSoat.invalidate();
                                }


                                //update so bien ban
                                if (listDataDoiSoatAdapter.get(pos).TRANG_THAI_CHON_GUI == Common.TRANG_THAI_CHON_GUI.DA_CHON_GUI) {
                                    sobbUpload++;
                                    listID_BBAN_TRTH.add(listDataDoiSoatAdapter.get(pos).ID_BBAN_TRTH);
                                } else {
                                    sobbUpload--;
                                    for (Iterator<Integer> iter = listID_BBAN_TRTH.listIterator(); iter.hasNext(); ) {
                                        Integer a = iter.next();
                                        if (listDataDoiSoatAdapter.get(pos).ID_BBAN_TRTH == a.intValue()) {
                                            iter.remove();
                                        }
                                    }
                                }


                                tvSoBBUpload.setText(sobbUpload + "/" + listDataDoiSoatAdapter.size() + " biên bản");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex09.getContent(), e.getMessage(), null);
                        }

                    }
                };

            doiSoatAdapters = new DoiSoatAdapter(getContext(), listDataDoiSoatAdapter, iIteractor);
            rvDoiSoat.setAdapter(doiSoatAdapters);
        } else
            doiSoatAdapters.refresh(listDataDoiSoatAdapter);

        tvSoBBUpload.setText(sobbUpload + "/" + listDataDoiSoatAdapter.size() + " biên bản");

        rvDoiSoat.invalidate();

        if (isShowNoDataText(listDataDoiSoatAdapter.size()))
            return;

    }

    private boolean isShowNoDataText(int size) {
        rvDoiSoat.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        tvNodata.setVisibility(size == 0 ? View.VISIBLE : View.GONE);

        if (size == 0)
            return true;
        else
            return false;
    }

    public void clearUpload() {
        try {
            for (Map.Entry<Integer, DoiSoatAdapter.DataDoiSoatAdapter> entry : hashMapData.entrySet()) {
                Integer key = entry.getKey();
                DoiSoatAdapter.DataDoiSoatAdapter element = entry.getValue();
                if (element.TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_GHI || element.TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.GUI_THAT_BAI) {
                    continue;
                }

                TABLE_BBAN_CTO tableBbanCto = mSqlDAO.getBBan(new String[]{String.valueOf(element.ID_BBAN_TRTH), onIDataCommon.getMaNVien()}).get(0);
                TABLE_BBAN_CTO tableBbanCtoOld = (TABLE_BBAN_CTO) tableBbanCto.clone();
                tableBbanCto.setTRANG_THAI_DOI_SOAT(Common.TRANG_THAI_DOI_SOAT.KHONG_THE_DOI_SOAT.content);
                tableBbanCto.setID_TABLE_BBAN_CTO((int) mSqlDAO.updateRows(TABLE_BBAN_CTO.class, tableBbanCtoOld, tableBbanCto));
            }

            fillDataDoiSoat();
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
    }

    public void searchData(String typeSearchString, String messageSearch) {
        try {
            Common.TYPE_SEARCH_UPLOAD typeSearch = Common.TYPE_SEARCH_UPLOAD.findTYPE_SEARCH(typeSearchString);
            List<DoiSoatAdapter.DataDoiSoatAdapter> dataFilter = new ArrayList<>();
            String query = Common.removeAccent(messageSearch.toString().trim().toLowerCase());
            switch (typeSearch) {
                case CHON:
                    fillDataDoiSoat();
                    break;
                case TEN_KH:
                    if (!TextUtils.isEmpty(messageSearch)) {
                        for (int i = 0; i < listDataDoiSoatAdapter.size(); i++) {
                            boolean isHasData = true;
                            DoiSoatAdapter.DataDoiSoatAdapter data = listDataDoiSoatAdapter.get(i);

                            isHasData = Common.removeAccent(data.TEN_KH.toLowerCase()).contains(query);

                            if (isHasData) {
                                dataFilter.add(data);
                            }
                        }
                    } else
                        dataFilter = Common.cloneList(listDataDoiSoatAdapter);

                    //giữ nguyên dữ liệu, lọc cái cần dùng
                    fillDataDoiSoat(dataFilter);
                    break;
                case SO_BBAN:
                    if (!TextUtils.isEmpty(messageSearch)) {
                        for (int i = 0; i < listDataDoiSoatAdapter.size(); i++) {
                            boolean isHasData = true;
                            DoiSoatAdapter.DataDoiSoatAdapter data = listDataDoiSoatAdapter.get(i);

                            isHasData = Common.removeAccent(data.SO_BBAN.toLowerCase()).contains(query);

                            if (isHasData) {
                                dataFilter.add(data);
                            }
                        }
                    } else
                        dataFilter = Common.cloneList(listDataDoiSoatAdapter);

                    //giữ nguyên dữ liệu, lọc cái cần dùng
                    fillDataDoiSoat(dataFilter);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }
    }

    private void fillDataDoiSoat(List<DoiSoatAdapter.DataDoiSoatAdapter> dataFilter) {
        if (doiSoatAdapters == null) {
            doiSoatAdapters = new DoiSoatAdapter(getContext(), dataFilter, iIteractor);
            rvDoiSoat.setAdapter(doiSoatAdapters);
        } else
            doiSoatAdapters.refresh(dataFilter);


        rvDoiSoat.invalidate();
    }

    //endregion

    public interface IOnTthtHnUploadFragment {
    }


    public JSONObject DATAtoJSONTTBBanWithImage(
            //TODO bien ban
            int ID_BBAN_CONGTO,
            String MA_DVIQLY,
            String SO_BBAN,
            int TRANG_THAI,

            //TODO cong to treo
            int LAN_CTO_TREO,
            int VTRI_TREO_THAO_CTO_TREO,
            int SOVIEN_CBOOC_CTO_TREO,
            int LOAI_HOM_CTO_TREO,
            int SOVIEN_CHOM_CTO_TREO,
            float HS_NHAN_CTO_TREO,
            String SO_TU_CTO_TREO,
            String SO_TI_CTO_TREO,
            String CHI_SO_CTO_TREO,
            int CCX_CTO_TREO,
            String TEM_CQUANG_CTO_TREO,
            int SOVIEN_CHIKDINH_CTO_TREO,
            String DIEN_AP_CTO_TREO,
            String DONG_DIEN_CTO_TREO,
            String HANGSO_K_CTO_TREO,
            String SO_KIM_NIEM_CHI_CTO_TREO,
            String TTRANG_NPHONG_CTO_TREO,
            String TEN_LOAI_CTO_CTO_TREO,
            String PHUONG_THUC_DO_XA_CTO_TREO,
            String GHI_CHU_CTO_TREO,

            //TODO cong to thao
            int LAN_CTO_THAO,
            int VTRI_TREO_THAO_CTO_THAO,
            int SOVIEN_CBOOC_CTO_THAO,
            int LOAI_HOM_CTO_THAO,
            int SOVIEN_CHOM_CTO_THAO,
            float HS_NHAN_CTO_THAO,
            String SO_TU_CTO_THAO,
            String SO_TI_CTO_THAO,
            String CHI_SO_CTO_THAO,
            int CCX_CTO_THAO,
            String TEM_CQUANG_CTO_THAO,
            int SOVIEN_CHIKDINH_CTO_THAO,
            String DIEN_AP_CTO_THAO,
            String DONG_DIEN_CTO_THAO,
            String HANGSO_K_CTO_THAO,
            String SO_KIM_NIEM_CHI_CTO_THAO,
            String TTRANG_NPHONG_CTO_THAO,
            String TEN_LOAI_CTO_CTO_THAO,
            String PHUONG_THUC_DO_XA_CTO_THAO,
            String GHI_CHU_CTO_THAO,

            //TODO update bang DU_LIEU_HIEN_TRUONG
            //TODO anh Cong To Treo
            int ID_BBAN_TUTI_CTO_TREO,
            int ID_CHITIET_TUTI_TU_CTO_TREO,
            int ID_CHITIET_TUTI_TI_CTO_TREO,

            int ID_CHITIET_CTO_TREO,
            //anh cong to
            String TEN_ANH_CTO_TREO,
            String TEN_ANH_NIEMPHONG_CTO_TREO,
            String ANH_CTO_TREO,
            String ANH_NIEMPHONG_CTO_TREO,
            String CREATE_DAY_ANH_CTO_TREO,
            String CREATE_DAY_ANH_NIEMPHONG_CTO_TREO,
            //anh TU
            String TEN_ANH_TU_CTO_TREO,
            String ANH_TU_CTO_TREO,
            String CREATE_DAY_ANH_TU_CTO_TREO,
            //anh TU mach nhi thu
            String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO,
            String ANH_TU_ANH_MACH_NHI_THU_CTO_TREO,
            String CREATE_DAY_ANH_TU_MACH_NHI_THU_CTO_TREO,
            //anh TU mach niem phong
            String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO,
            String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO,
            String CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO,


            //anh TI
            String TEN_ANH_TI_CTO_TREO,
            String ANH_TI_CTO_TREO,
            String CREATE_DAY_ANH_TI_CTO_TREO,
            //anh TI mach nhi thu
            String TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO,
            String ANH_TI_ANH_MACH_NHI_THU_CTO_TREO,
            String CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO,
            //anh TI mach niem phong
            String TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO,
            String ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO,
            String CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO,

            //TODO anh Cong To Thao
            int ID_BBAN_TUTI_CTO_THAO,
            int ID_CHITIET_TUTI_TU_CTO_THAO,
            int ID_CHITIET_TUTI_TI_CTO_THAO,

            int ID_CHITIET_CTO_THAO,
            //anh cong to
            String TEN_ANH_CTO_THAO,
            String TEN_ANH_NIEMPHONG_CTO_THAO,
            String ANH_CTO_THAO,
            String ANH_NIEMPHONG_CTO_THAO,
            String CREATE_DAY_ANH_CTO_THAO,
            String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO
    ) {


        String sID_BBAN_CONGTO = "ID_BBAN_CONGTO";
        String sMA_DVIQLY = "MA_NVIEN";
        String sSO_BBAN = "SO_BBAN";
        String sTRANG_THAI = "TRANG_THAI";

        //TODO cong sto treo

        String sID_BBAN_TUTI_CTO_TREO = "ID_BBAN_TUTI_CTO_TREO";
        String sID_CHITIET_TUTI_TU_CTO_TREO = "ID_CHITIET_TUTI_TU_CTO_TREO";
        String sID_CHITIET_TUTI_TI_CTO_TREO = "ID_CHITIET_TUTI_TI_CTO_TREO";
        String sLAN_CTO_TREO = "LAN_CTO_TREO";
        String sVTRI_TREO_THAO_CTO_TREO = "VTRI_TREO_THAO_CTO_TREO";
        String sSOVIEN_CBOOC_CTO_TREO = "SOVIEN_CBOOC_CTO_TREO";
        String sLOAI_HOM_CTO_TREO = "LOAI_HOM_CTO_TREO";
        String sSOVIEN_CHOM_CTO_TREO = "SOVIEN_CHOM_CTO_TREO";
        String sHS_NHAN_CTO_TREO = "HS_NHAN_CTO_TREO";
        String sSO_TU_CTO_TREO = "SO_TU_CTO_TREO";
        String sSO_TI_CTO_TREO = "SO_TI_CTO_TREO";
        String sCHI_SO_CTO_TREO = "CHI_SO_CTO_TREO";
        String sCCX_CTO_TREO = "CCX_CTO_TREO";
        String sTEM_CQUANG_CTO_TREO = "TEM_CQUANG_CTO_TREO";
        String sSOVIEN_CHIKDINH_CTO_TREO = "SOVIEN_CHIKDINH_CTO_TREO";
        String sDIEN_AP_CTO_TREO = "DIEN_AP_CTO_TREO";
        String sDONG_DIEN_CTO_TREO = "DONG_DIEN_CTO_TREO";
        String sHANGSO_K_CTO_TREO = "HANGSO_K_CTO_TREO";
        String sSO_KIM_NIEM_CHI_CTO_TREO = "SO_KIM_NIEM_CHI_CTO_TREO";
        String sTTRANG_NPHONG_CTO_TREO = "TTRANG_NPHONG_CTO_TREO";
        String sTEN_LOAI_CTO_CTO_TREO = "TEN_LOAI_CTO_CTO_TREO";
        String sPHUONG_THUC_DO_XA_CTO_TREO = "PHUONG_THUC_DO_XA_CTO_TREO";
        String sGHI_CHU_CTO_TREO = "GHI_CHU_CTO_TREO";

        //TODO cong sto thao
        String sID_BBAN_TUTI_CTO_THAO = "ID_BBAN_TUTI_CTO_THAO";
        String sID_CHITIET_TUTI_TU_CTO_THAO = "ID_CHITIET_TUTI_TU_CTO_THAO";
        String sID_CHITIET_TUTI_TI_CTO_THAO = "ID_CHITIET_TUTI_TI_CTO_THAO";
        String sLAN_CTO_THAO = "LAN_CTO_THAO";
        String sVTRI_TREO_THAO_CTO_THAO = "VTRI_TREO_THAO_CTO_THAO";
        String sSOVIEN_CBOOC_CTO_THAO = "SOVIEN_CBOOC_CTO_THAO";
        String sLOAI_HOM_CTO_THAO = "LOAI_HOM_CTO_THAO";
        String sSOVIEN_CHOM_CTO_THAO = "SOVIEN_CHOM_CTO_THAO";
        String sHS_NHAN_CTO_THAO = "HS_NHAN_CTO_THAO";
        String sSO_TU_CTO_THAO = "SO_TU_CTO_THAO";
        String sSO_TI_CTO_THAO = "SO_TI_CTO_THAO";
        String sCHI_SO_CTO_THAO = "CHI_SO_CTO_THAO";
        String sCCX_CTO_THAO = "CCX_CTO_THAO";
        String sTEM_CQUANG_CTO_THAO = "TEM_CQUANG_CTO_THAO";
        String sSOVIEN_CHIKDINH_CTO_THAO = "SOVIEN_CHIKDINH_CTO_THAO";
        String sDIEN_AP_CTO_THAO = "DIEN_AP_CTO_THAO";
        String sDONG_DIEN_CTO_THAO = "DONG_DIEN_CTO_THAO";
        String sHANGSO_K_CTO_THAO = "HANGSO_K_CTO_THAO";
        String sSO_KIM_NIEM_CHI_CTO_THAO = "SO_KIM_NIEM_CHI_CTO_THAO";
        String sTTRANG_NPHONG_CTO_THAO = "TTRANG_NPHONG_CTO_THAO";
        String sTEN_LOAI_CTO_CTO_THAO = "TEN_LOAI_CTO_CTO_THAO";
        String sPHUONG_THUC_DO_XA_CTO_THAO = "PHUONG_THUC_DO_XA_CTO_THAO";
        String sGHI_CHU_CTO_THAO = "GHI_CHU_CTO_THAO";


        //TODO update bang DU_LIEU_HIEN_TRUONG
        //TODO anh Cong To Treo
        String sID_CHITIET_CTO_TREO = "ID_CHITIET_CTO_TREO";
        //anh cong to
        String sTEN_ANH_CTO_TREO = "TEN_ANH_CTO_TREO";
        String sTEN_ANH_NIEMPHONG_CTO_TREO = "TEN_ANH_NIEMPHONG_CTO_TREO";

        String sANH_CTO_TREO = "ANH_CTO_TREO";
        String sANH_NIEMPHONG_CTO_TREO = "ANH_NIEMPHONG_CTO_TREO";

        String sCREATE_DAY_ANH_CTO_TREO = "CREATE_DAY_ANH_CTO_TREO";
        String sCREATE_DAY_ANH_NIEMPHONG_CTO_TREO = "CREATE_DAY_ANH_NIEMPHONG_CTO_TREO";

        //anh TU
        String sTEN_ANH_TU_CTO_TREO = "TEN_ANH_TU_CTO_TREO";
        String sANH_TU_CTO_TREO = "ANH_TU_CTO_TREO";
        String sCREATE_DAY_ANH_TU_CTO_TREO = "CREATE_DAY_ANH_TU_CTO_TREO";
        //anh TU mach nhi thu
        String sTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = "TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO";
        String sANH_TU_ANH_MACH_NHI_THU_CTO_TREO = "ANH_TU_ANH_MACH_NHI_THU_CTO_TREO";
        String sCREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO = "CREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO";
        //anh TU mach niem phong
        String sTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = "TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO";
        String sANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = "ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO";
        String sCREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO = "CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO";

        //anh TI
        String sTEN_ANH_TI_CTO_TREO = "TEN_ANH_TI_CTO_TREO";
        String sANH_TI_CTO_TREO = "ANH_TI_CTO_TREO";
        String sCREATE_DAY_ANH_TI_CTO_TREO = "CREATE_DAY_ANH_TI_CTO_TREO";
        //anh TI mach nhi thu
        String sTEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = "TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO";
        String sANH_TI_ANH_MACH_NHI_THU_CTO_TREO = "ANH_TI_ANH_MACH_NHI_THU_CTO_TREO";
        String sCREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO = "CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO";
        //anh TI mach niem phong
        String sTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = "TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO";
        String sANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = "ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO";
        String sCREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO = "CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO";

        //TODO anh Cong To Thao
        String sID_CHITIET_CTO_THAO = "ID_CHITIET_CTO_THAO";
        //anh cong to
        String sTEN_ANH_CTO_THAO = "TEN_ANH_CTO_THAO";
        String sTEN_ANH_NIEMPHONG_CTO_THAO = "TEN_ANH_NIEMPHONG_CTO_THAO";

        String sANH_CTO_THAO = "ANH_CTO_THAO";
        String sANH_NIEMPHONG_CTO_THAO = "ANH_CTO_NIEMPHONG_THAO";
        String sCREATE_DAY_ANH_CTO_THAO = "CREATE_DAY_ANH_CTO_THAO";
        String sCREATE_DAY_ANH_NIEMPHONG_CTO_THAO = "CREATE_DAY_ANH_NIEMPHONG_CTO_THAO";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sID_BBAN_CONGTO, ID_BBAN_CONGTO);
            jsonObject.accumulate(sMA_DVIQLY, MA_DVIQLY == null ? "" : MA_DVIQLY);
            jsonObject.accumulate(sSO_BBAN, SO_BBAN == null ? "" : SO_BBAN);
            jsonObject.accumulate(sTRANG_THAI, TRANG_THAI);

            //TODO cong sto treo
            jsonObject.accumulate(sLAN_CTO_TREO, LAN_CTO_TREO);
            jsonObject.accumulate(sVTRI_TREO_THAO_CTO_TREO, VTRI_TREO_THAO_CTO_TREO);
            jsonObject.accumulate(sSOVIEN_CBOOC_CTO_TREO, SOVIEN_CBOOC_CTO_TREO);
            jsonObject.accumulate(sLOAI_HOM_CTO_TREO, LOAI_HOM_CTO_TREO);
            jsonObject.accumulate(sSOVIEN_CHOM_CTO_TREO, SOVIEN_CHOM_CTO_TREO);
            jsonObject.accumulate(sHS_NHAN_CTO_TREO, HS_NHAN_CTO_TREO);
            jsonObject.accumulate(sSO_TU_CTO_TREO, SO_TU_CTO_TREO == null ? "" : SO_TU_CTO_TREO);
            jsonObject.accumulate(sSO_TI_CTO_TREO, SO_TI_CTO_TREO == null ? "" : SO_TI_CTO_TREO);
            jsonObject.accumulate(sCHI_SO_CTO_TREO, CHI_SO_CTO_TREO == null ? "" : CHI_SO_CTO_TREO);
            jsonObject.accumulate(sCCX_CTO_TREO, CCX_CTO_TREO);
            jsonObject.accumulate(sTEM_CQUANG_CTO_TREO, TEM_CQUANG_CTO_TREO == null ? "" : TEM_CQUANG_CTO_TREO);
            jsonObject.accumulate(sSOVIEN_CHIKDINH_CTO_TREO, SOVIEN_CHIKDINH_CTO_TREO);
            jsonObject.accumulate(sDIEN_AP_CTO_TREO, DIEN_AP_CTO_TREO == null ? "" : DIEN_AP_CTO_TREO);
            jsonObject.accumulate(sDONG_DIEN_CTO_TREO, DONG_DIEN_CTO_TREO == null ? "" : DONG_DIEN_CTO_TREO);
            jsonObject.accumulate(sHANGSO_K_CTO_TREO, HANGSO_K_CTO_TREO == null ? "" : HANGSO_K_CTO_TREO);
            jsonObject.accumulate(sSO_KIM_NIEM_CHI_CTO_TREO, SO_KIM_NIEM_CHI_CTO_TREO == null ? "" : SO_KIM_NIEM_CHI_CTO_TREO);
            jsonObject.accumulate(sTTRANG_NPHONG_CTO_TREO, TTRANG_NPHONG_CTO_TREO == null ? "" : TTRANG_NPHONG_CTO_TREO);
            jsonObject.accumulate(sTEN_LOAI_CTO_CTO_TREO, TEN_LOAI_CTO_CTO_TREO == null ? "" : TEN_LOAI_CTO_CTO_TREO);
            jsonObject.accumulate(sPHUONG_THUC_DO_XA_CTO_TREO, PHUONG_THUC_DO_XA_CTO_TREO == null ? "" : PHUONG_THUC_DO_XA_CTO_TREO);
            jsonObject.accumulate(sGHI_CHU_CTO_TREO, GHI_CHU_CTO_TREO == null ? "" : GHI_CHU_CTO_TREO);

            //TODO cong sto thao
            jsonObject.accumulate(sLAN_CTO_THAO, LAN_CTO_THAO);
            jsonObject.accumulate(sVTRI_TREO_THAO_CTO_THAO, VTRI_TREO_THAO_CTO_THAO);
            jsonObject.accumulate(sSOVIEN_CBOOC_CTO_THAO, SOVIEN_CBOOC_CTO_THAO);
            jsonObject.accumulate(sLOAI_HOM_CTO_THAO, LOAI_HOM_CTO_THAO);
            jsonObject.accumulate(sSOVIEN_CHOM_CTO_THAO, SOVIEN_CHOM_CTO_THAO);
            jsonObject.accumulate(sHS_NHAN_CTO_THAO, HS_NHAN_CTO_THAO);
            jsonObject.accumulate(sSO_TU_CTO_THAO, SO_TU_CTO_THAO == null ? "" : SO_TU_CTO_THAO);
            jsonObject.accumulate(sSO_TI_CTO_THAO, SO_TI_CTO_THAO == null ? "" : SO_TI_CTO_THAO);
            jsonObject.accumulate(sCHI_SO_CTO_THAO, CHI_SO_CTO_THAO == null ? "" : CHI_SO_CTO_THAO);
            jsonObject.accumulate(sCCX_CTO_THAO, CCX_CTO_THAO);
            jsonObject.accumulate(sTEM_CQUANG_CTO_THAO, TEM_CQUANG_CTO_THAO == null ? "" : TEM_CQUANG_CTO_THAO);
            jsonObject.accumulate(sSOVIEN_CHIKDINH_CTO_THAO, SOVIEN_CHIKDINH_CTO_THAO);
            jsonObject.accumulate(sDIEN_AP_CTO_THAO, DIEN_AP_CTO_THAO == null ? "" : DIEN_AP_CTO_THAO);
            jsonObject.accumulate(sDONG_DIEN_CTO_THAO, DONG_DIEN_CTO_THAO == null ? "" : DONG_DIEN_CTO_THAO);
            jsonObject.accumulate(sHANGSO_K_CTO_THAO, HANGSO_K_CTO_THAO == null ? "" : HANGSO_K_CTO_THAO);
            jsonObject.accumulate(sSO_KIM_NIEM_CHI_CTO_THAO, SO_KIM_NIEM_CHI_CTO_THAO == null ? "" : SO_KIM_NIEM_CHI_CTO_THAO);
            jsonObject.accumulate(sTTRANG_NPHONG_CTO_THAO, TTRANG_NPHONG_CTO_THAO == null ? "" : TTRANG_NPHONG_CTO_THAO);
            jsonObject.accumulate(sTEN_LOAI_CTO_CTO_THAO, TEN_LOAI_CTO_CTO_THAO == null ? "" : TEN_LOAI_CTO_CTO_THAO);
            jsonObject.accumulate(sPHUONG_THUC_DO_XA_CTO_THAO, PHUONG_THUC_DO_XA_CTO_THAO == null ? "" : PHUONG_THUC_DO_XA_CTO_THAO);
            jsonObject.accumulate(sGHI_CHU_CTO_THAO, GHI_CHU_CTO_THAO == null ? "" : GHI_CHU_CTO_THAO);


            //TODO update bang DU_LIEU_HIEN_TRUONG
            //TODO anh Cong To Treo
            jsonObject.accumulate(sID_BBAN_TUTI_CTO_TREO, ID_BBAN_TUTI_CTO_TREO);
            jsonObject.accumulate(sID_CHITIET_TUTI_TU_CTO_TREO, ID_CHITIET_TUTI_TU_CTO_TREO);
            jsonObject.accumulate(sID_CHITIET_TUTI_TI_CTO_TREO, ID_CHITIET_TUTI_TI_CTO_TREO);

            jsonObject.accumulate(sID_CHITIET_CTO_TREO, ID_CHITIET_CTO_TREO);

            //anh cong to
            jsonObject.accumulate(sTEN_ANH_CTO_TREO, TEN_ANH_CTO_TREO == null ? "" : TEN_ANH_CTO_TREO);
            jsonObject.accumulate(sTEN_ANH_NIEMPHONG_CTO_TREO, TEN_ANH_NIEMPHONG_CTO_TREO == null ? "" : TEN_ANH_NIEMPHONG_CTO_TREO);

            jsonObject.accumulate(sANH_CTO_TREO, ANH_CTO_TREO == null ? "" : ANH_CTO_TREO);
            jsonObject.accumulate(sANH_NIEMPHONG_CTO_TREO, ANH_NIEMPHONG_CTO_TREO == null ? "" : TEN_ANH_NIEMPHONG_CTO_TREO);

            jsonObject.accumulate(sCREATE_DAY_ANH_CTO_TREO, CREATE_DAY_ANH_CTO_TREO == null ? "" : CREATE_DAY_ANH_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_NIEMPHONG_CTO_TREO, CREATE_DAY_ANH_NIEMPHONG_CTO_TREO == null ? "" : CREATE_DAY_ANH_NIEMPHONG_CTO_TREO);

            //anh TU
            jsonObject.accumulate(sTEN_ANH_TU_CTO_TREO, TEN_ANH_TU_CTO_TREO == null ? "" : TEN_ANH_TU_CTO_TREO);
            jsonObject.accumulate(sANH_TU_CTO_TREO, ANH_TU_CTO_TREO == null ? "" : ANH_TU_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_TU_CTO_TREO, CREATE_DAY_ANH_TU_CTO_TREO == null ? "" : CREATE_DAY_ANH_TU_CTO_TREO);
            //anh TU mach nhi thu
            jsonObject.accumulate(sTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO, TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO == null ? "" : TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO);
            jsonObject.accumulate(sANH_TU_ANH_MACH_NHI_THU_CTO_TREO, ANH_TU_ANH_MACH_NHI_THU_CTO_TREO == null ? "" : ANH_TU_ANH_MACH_NHI_THU_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO, CREATE_DAY_ANH_TU_MACH_NHI_THU_CTO_TREO == null ? "" : CREATE_DAY_ANH_TU_MACH_NHI_THU_CTO_TREO);
            //anh TU mach niem phong
            jsonObject.accumulate(sTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO, TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO == null ? "" : TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO);
            jsonObject.accumulate(sANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO, ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO == null ? "" : ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO, CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO == null ? "" : CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO);

            //anh TI
            jsonObject.accumulate(sTEN_ANH_TI_CTO_TREO, TEN_ANH_TI_CTO_TREO == null ? "" : TEN_ANH_TI_CTO_TREO);
            jsonObject.accumulate(sANH_TI_CTO_TREO, ANH_TI_CTO_TREO == null ? "" : ANH_TI_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_TI_CTO_TREO, CREATE_DAY_ANH_TI_CTO_TREO == null ? "" : CREATE_DAY_ANH_TI_CTO_TREO);
            //anh TI mach nhi thu
            jsonObject.accumulate(sTEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO, TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO == null ? "" : TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO);
            jsonObject.accumulate(sANH_TI_ANH_MACH_NHI_THU_CTO_TREO, ANH_TI_ANH_MACH_NHI_THU_CTO_TREO == null ? "" : ANH_TI_ANH_MACH_NHI_THU_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO, CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO == null ? "" : CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO);
            //anh TI mach niem phong
            jsonObject.accumulate(sTEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO, TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO == null ? "" : TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
            jsonObject.accumulate(sANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO, ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO == null ? "" : ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO, CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO == null ? "" : CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO);

            //TODO anh Cong To Thao
            jsonObject.accumulate(sID_BBAN_TUTI_CTO_THAO, ID_BBAN_TUTI_CTO_THAO);
            jsonObject.accumulate(sID_CHITIET_TUTI_TI_CTO_THAO, ID_CHITIET_TUTI_TI_CTO_THAO);
            jsonObject.accumulate(sID_CHITIET_TUTI_TU_CTO_THAO, ID_CHITIET_TUTI_TU_CTO_THAO);

            jsonObject.accumulate(sID_CHITIET_CTO_THAO, ID_CHITIET_CTO_THAO);
            //anh cong to
            jsonObject.accumulate(sTEN_ANH_CTO_THAO, TEN_ANH_CTO_THAO == null ? "" : TEN_ANH_CTO_THAO);
            jsonObject.accumulate(sTEN_ANH_NIEMPHONG_CTO_THAO, TEN_ANH_NIEMPHONG_CTO_THAO == null ? "" : TEN_ANH_NIEMPHONG_CTO_THAO);

            jsonObject.accumulate(sANH_CTO_THAO, ANH_CTO_THAO == null ? "" : ANH_CTO_THAO);
            jsonObject.accumulate(sANH_NIEMPHONG_CTO_THAO, ANH_NIEMPHONG_CTO_THAO == null ? "" : ANH_NIEMPHONG_CTO_THAO);

            jsonObject.accumulate(sCREATE_DAY_ANH_CTO_THAO, CREATE_DAY_ANH_CTO_THAO == null ? "" : CREATE_DAY_ANH_CTO_THAO);
            jsonObject.accumulate(sCREATE_DAY_ANH_NIEMPHONG_CTO_THAO, CREATE_DAY_ANH_NIEMPHONG_CTO_THAO == null ? "" : CREATE_DAY_ANH_NIEMPHONG_CTO_THAO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
