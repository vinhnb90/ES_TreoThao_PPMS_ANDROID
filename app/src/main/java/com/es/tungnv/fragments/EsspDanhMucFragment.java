package com.es.tungnv.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspDanhMucFragment extends Fragment implements View.OnClickListener{

    //region Khai báo biến
    private View rootView;

    private ImageButton ibGetTram, ibGetVatTu, ibGetSoGCS, ibGetTroNgai, ibGetNganHang;
    private TextView tvTongTram, tvTongVatTu, tvTongSoGCS, tvTongSoTroNgai, tvTongSoNganHang, tvTimeDongBoTram,
            tvTimeDongBoSo, tvTimeDongBoVattu, tvTimeDongBoTroNgai, tvTimeDongBoNganHang;
    private ProgressDialog progressDialog;

    private EsspSqliteConnection connection;
    private EsspAsyncCallWSJson acJson;
    private ArrayList<JSONObject> lstJsonTram, lstJsonVatTu, lstJsonTroNgai, lstJsonSoGCS, lstJsonNganHang;

    private boolean checkGetData = false;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    int countTram = 0;
    int countVatTu = 0;
    int countSo = 0;
    int countTroNgai = 0;
    int countNganHang = 0;
    //endregion

    //region Khởi tạo
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.essp_fragment_danhmuc, null);
            initComponent(rootView);
            connection = EsspSqliteConnection.getInstance(EsspDanhMucFragment.this.getActivity());
            acJson = EsspAsyncCallWSJson.getInstance();
            initData();
            return rootView;
        } catch(Exception ex) {
            Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponent(View rootView){
        ibGetTram = (ImageButton) rootView.findViewById(R.id.essp_fragment_danhmuc_ib_get_tram);
        ibGetVatTu = (ImageButton) rootView.findViewById(R.id.essp_fragment_danhmuc_ib_get_vattu);
        ibGetSoGCS = (ImageButton) rootView.findViewById(R.id.essp_fragment_danhmuc_ib_get_so);
        ibGetTroNgai = (ImageButton) rootView.findViewById(R.id.essp_fragment_danhmuc_ib_get_tro_ngai);
        ibGetNganHang = (ImageButton) rootView.findViewById(R.id.essp_fragment_danhmuc_ib_get_ngan_hang);
        tvTongTram = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_tongso_tram);
        tvTongVatTu = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_tongso_vattu);
        tvTongSoGCS = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_tongso_so);
        tvTongSoTroNgai = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_tongso_tro_ngai);
        tvTongSoNganHang = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_tongso_ngan_hang);
        tvTimeDongBoTram = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_time_dongbo_tram);
        tvTimeDongBoVattu = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_time_dongbo_vattu);
        tvTimeDongBoSo = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_time_dongbo_so);
        tvTimeDongBoTroNgai = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_time_dongbo_tro_ngai);
        tvTimeDongBoNganHang = (TextView) rootView.findViewById(R.id.essp_fragment_danhmuc_tv_time_dongbo_ngan_hang);

        ibGetTram.setOnClickListener(this);
        ibGetVatTu.setOnClickListener(this);
        ibGetSoGCS.setOnClickListener(this);
        ibGetTroNgai.setOnClickListener(this);
        ibGetNganHang.setOnClickListener(this);
    }

    private void initData(){
        tvTongTram.setText(new StringBuilder("Tổng số trạm: ").append(connection.countTram(EsspCommon.getMaDviqly())).toString());
        tvTimeDongBoTram.setText(new StringBuilder("Thời gian đồng bộ: ").append(connection.getTGianDBo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "TRAM")).toString());
        tvTongVatTu.setText(new StringBuilder("Tổng số vật tư: ").append(connection.countVT(EsspCommon.getMaDviqly())).toString());
        tvTimeDongBoVattu.setText(new StringBuilder("Thời gian đồng bộ: ").append(connection.getTGianDBo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "VAT_TU")).toString());
        tvTongSoTroNgai.setText(new StringBuilder("Tổng số trở ngại: ").append(connection.countTroNgai(EsspCommon.getMaDviqly())).toString());
        tvTimeDongBoTroNgai.setText(new StringBuilder("Thời gian đồng bộ: ").append(connection.getTGianDBo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "TRO_NGAI")).toString());
        tvTongSoGCS.setText(new StringBuilder("Tổng số trở ngại: ").append(connection.countSoGCS(EsspCommon.getMaDviqly())).toString());
        tvTimeDongBoSo.setText(new StringBuilder("Thời gian đồng bộ: ").append(connection.getTGianDBo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "SO_GCS")).toString());
        tvTongSoNganHang.setText(new StringBuilder("Tổng số ngân hàng: ").append(connection.countNganHang()).toString());
        tvTimeDongBoNganHang.setText(new StringBuilder("Thời gian đồng bộ: ").append(connection.getTGianDBo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "NGAN_HANG")).toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.essp_fragment_danhmuc_ib_get_tram:
                try {
                    if (!Common.isNetworkOnline(EsspDanhMucFragment.this.getActivity())) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                    } else {
                        lstJsonTram = new ArrayList<>();
//                    progressDialog = ProgressDialog.show(EsspDanhMucFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setCancelable(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage("Đang tải danh mục trạm ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    countTram = 0;
                                    progressBarStatus = 0;
                                    Thread.sleep(50);
                                    lstJsonTram = acJson.WS_GET_TRAM_CALL(EsspCommon.getMaDviqly());

                                    if (lstJsonTram != null) {
                                        checkGetData = false;

                                        if (lstJsonTram.size() == 0) {
                                            Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                    "Không có trạm trên server", Color.WHITE, "OK", Color.WHITE);
                                            progressDialog.dismiss();
                                            return;
                                        }

                                        int maxSize = lstJsonTram.size();
                                        float snap = (float)maxSize / 100f;
                                        for (int i = 0; i < maxSize; i++) {
                                            JSONObject map = lstJsonTram.get(i);
                                            if (connection.checkTramExist(map.getString("MA_TRAM")) == 0) {
                                                if (connection.insertDataTram(EsspCommon.getMaDviqly(), map.getString("MA_TRAM"),
                                                        map.getString("TEN_TRAM"), map.getString("LOAI_TRAM"), map.getString("CSUAT_TRAM"), map.getString("MA_CAP_DA"),
                                                        map.getString("MA_CAP_DA_RA"), map.getString("DINH_DANH")) != -1) {
                                                    countTram++;
                                                }
                                            }
                                            progressBarStatus = (int)(i/snap);
                                            progressBarHandler.post(new Runnable() {
                                                public void run() {
                                                    progressDialog.setProgress(progressBarStatus);
                                                }
                                            });
                                        }
                                    } else {
                                        checkGetData = true;
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                handlerTram.sendEmptyMessage(0);
                            }
                        });
                        thread.start();
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo",
                            Color.WHITE, "Lỗi đồng bộ trạm", Color.WHITE, "OK", Color.WHITE);
                }
                break;
            case R.id.essp_fragment_danhmuc_ib_get_vattu:
                if(!Common.isNetworkOnline(EsspDanhMucFragment.this.getActivity())){
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstJsonVatTu = new ArrayList<>();
//                    progressDialog = ProgressDialog.show(EsspDanhMucFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Đang tải danh mục vật tư ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countVatTu = 0;
                                progressBarStatus = 0;
                                Thread.sleep(50);
                                lstJsonVatTu = acJson.WS_GET_VTU_CALL(EsspCommon.getMaDviqly());
                                if(lstJsonVatTu != null){
                                    checkGetData = false;
                                    if(lstJsonVatTu.size() == 0){
                                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                "Không có vật tư trên server", Color.WHITE, "OK", Color.WHITE);
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    int maxSize = lstJsonVatTu.size();
                                    float snap = (float)maxSize / 100f;
                                    for (int i = 0; i < maxSize; i++) {
                                        JSONObject map = lstJsonVatTu.get(i);

                                        String MA_VTU = map.getString("MA_VTU");
                                        String TEN_VTU = map.getString("TEN_VTU");
                                        String MA_LOAI_CPHI = map.getString("MA_LOAI_CPHI");
                                        String DON_GIA = map.getString("DON_GIA");
                                        String DVI_TINH = map.getString("DVI_TINH");
                                        String DON_GIA_KH = map.getString("DON_GIA_KH");
                                        String LOAI = map.getString("LOAI");
                                        String CHUNG_LOAI = map.getString("CHUNG_LOAI");
                                        String DG_TRONGOI = map.getString("DG_TRONGOI");
                                        String DG_NCONG = map.getString("DG_NCONG");
                                        String DG_VTU = map.getString("DG_VTU");
                                        String DG_MTCONG = map.getString("DG_MTCONG");
                                        if (connection.checkVT(MA_VTU)) {
                                            if (connection.insertDataVT(MA_VTU, EsspCommon.getMaDviqly(), TEN_VTU, MA_LOAI_CPHI,
                                                    DON_GIA, DVI_TINH, DON_GIA_KH, LOAI, CHUNG_LOAI, DG_TRONGOI, DG_NCONG,
                                                    DG_VTU, DG_MTCONG) != -1) {
                                                countVatTu++;
                                            }
                                        } else {
                                            if (connection.updateDataVT(MA_VTU, TEN_VTU, MA_LOAI_CPHI, DON_GIA, DVI_TINH, DON_GIA_KH,
                                                    LOAI, CHUNG_LOAI, DG_TRONGOI, DG_NCONG, DG_VTU, DG_MTCONG) != -1) {
                                                countVatTu++;
                                            }
                                        }
                                        progressBarStatus = (int)(i/snap);
                                        progressBarHandler.post(new Runnable() {
                                            public void run() {
                                                progressDialog.setProgress(progressBarStatus);
                                            }
                                        });
                                    }
                                } else {
                                    checkGetData = true;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handlerVatTu.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
                break;
            case R.id.essp_fragment_danhmuc_ib_get_so:
                if(!Common.isNetworkOnline(EsspDanhMucFragment.this.getActivity())){
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstJsonSoGCS = new ArrayList<>();
//                    progressDialog = ProgressDialog.show(EsspDanhMucFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Đang tải danh mục sổ ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countSo = 0;
                                progressBarStatus = 0;
                                Thread.sleep(50);
                                lstJsonSoGCS = acJson.WS_GET_SO_GCS_CALL(EsspCommon.getMaDviqly());
                                if(lstJsonSoGCS != null){
                                    checkGetData = false;

                                    if(lstJsonSoGCS.size() == 0){
                                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                "Không có sổ trên server", Color.WHITE, "OK", Color.WHITE);
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    int maxSize = lstJsonSoGCS.size();
                                    float snap = (float)maxSize / 100f;
                                    if (connection.deleteAllDataSO() != -1) {
                                        for (int i = 0; i < maxSize; i++) {
                                            JSONObject map = lstJsonSoGCS.get(i);

                                            String MA_DVIQLY = EsspCommon.getMaDviqly();
                                            String TEN_SO = map.getString("TEN_SO");
                                            String MA_SO = map.getString("MA_SO");
                                            if (connection.insertDataSo(MA_DVIQLY, TEN_SO, MA_SO) != -1) {
                                                countSo++;
                                            }
                                            progressBarStatus = (int)(i/snap);
                                            progressBarHandler.post(new Runnable() {
                                                public void run() {
                                                    progressDialog.setProgress(progressBarStatus);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    checkGetData = true;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handlerSo.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
                break;
            case R.id.essp_fragment_danhmuc_ib_get_tro_ngai:
                if(!Common.isNetworkOnline(EsspDanhMucFragment.this.getActivity())){
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstJsonTroNgai = new ArrayList<>();
//                    progressDialog = ProgressDialog.show(EsspDanhMucFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Đang tải danh mục trở ngại ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countTroNgai = 0;
                                progressBarStatus = 0;
                                Thread.sleep(50);
                                lstJsonTroNgai = acJson.WS_GET_TRO_NGAI_CALL();
                                if(lstJsonTroNgai != null){
                                    checkGetData = false;
                                    if(lstJsonTroNgai.size() == 0){
                                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                "Không có trở ngại trên server", Color.WHITE, "OK", Color.WHITE);
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    int maxSize = lstJsonTroNgai.size();
                                    float snap = (float)maxSize / 100f;
                                    if(connection.deleteAllDataTN() != -1) {
                                        for (int i = 0; i < maxSize; i++) {
                                            JSONObject map = lstJsonTroNgai.get(i);

                                            String ma_tngai = map.getString("MA_TNGAI");
                                            String ten_tngai = map.getString("TEN_TNGAI");
                                            String tchat_kphuc = map.getString("TCHAT_KPHUC");
                                            String nguyen_nhan = map.getString("NGUYEN_NHAN");
                                            String ma_cviec = map.getString("MA_CVIEC");
                                            String ngay_hluc = map.getString("NGAY_HLUC");

                                            if (connection.insertDataTNgai(EsspCommon.getMaDviqly(), ma_tngai, ten_tngai,
                                                    tchat_kphuc, nguyen_nhan, ma_cviec, ngay_hluc) != -1) {
                                                countTroNgai++;
                                            }
                                            progressBarStatus = (int)(i/snap);
                                            progressBarHandler.post(new Runnable() {
                                                public void run() {
                                                    progressDialog.setProgress(progressBarStatus);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    checkGetData = true;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handlerTroNgai.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
                break;
            case R.id.essp_fragment_danhmuc_ib_get_ngan_hang:
                if(!Common.isNetworkOnline(EsspDanhMucFragment.this.getActivity())){
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstJsonTroNgai = new ArrayList<>();
//                    progressDialog = ProgressDialog.show(EsspDanhMucFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true,false);
                    progressDialog = new ProgressDialog(v.getContext());
//                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Đang tải danh mục ngân hàng ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                countTroNgai = 0;
                                progressBarStatus = 0;
                                Thread.sleep(50);
                                lstJsonNganHang = acJson.WS_GET_NGAN_HANG_CALL();
                                if(lstJsonNganHang != null){
                                    checkGetData = false;
                                    if(lstJsonNganHang.size() == 0){
                                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                "Không có ngân hàng trên server", Color.WHITE, "OK", Color.WHITE);
                                        progressDialog.dismiss();
                                        return;
                                    }
                                    int maxSize = lstJsonNganHang.size();
                                    float snap = (float)maxSize / 100f;
                                    if(connection.deleteAllDataNH() != -1) {
                                        for (int i = 0; i < maxSize; i++) {
                                            JSONObject map = lstJsonNganHang.get(i);

                                            String MA_NHANG = map.getString("MA_NHANG");
                                            String TEN_NHANG = map.getString("TEN_NHANG");
                                            String DIA_CHI = map.getString("DIA_CHI");
                                            String DIEN_THOAI = map.getString("DIEN_THOAI");
                                            String SO_TKHOAN = map.getString("SO_TKHOAN");
                                            String MA_NHANG_CHA = map.getString("MA_NHANG_CHA");

                                            long result = connection.insertDataNganHang(MA_NHANG, TEN_NHANG, DIA_CHI, DIEN_THOAI, SO_TKHOAN, MA_NHANG_CHA);
                                            if (result != -1) {
                                                countNganHang++;
                                            }
                                            progressBarStatus = (int)(i/snap);
                                            progressBarHandler.post(new Runnable() {
                                                public void run() {
                                                    progressDialog.setProgress(progressBarStatus);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    checkGetData = true;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            handlerNganHang.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
                break;
        }
    }
    //endregion

    //region Xử lý handler
    private Handler handlerTram = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (checkGetData) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không lấy được dữ liệu hoặc có thể dữ liệu của bạn đã đủ", Color.WHITE, "OK", Color.WHITE);
                } else {
                    if (countTram > 0) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                new StringBuilder("Bạn đã đồng bộ thành công ").append(countTram).append("/").append(lstJsonTram.size()).append(" trạm").toString(), Color.WHITE, "OK", Color.WHITE);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cTime = sdf.format(new Date());

                        if (connection.isTGianDboExist(EsspCommon.getUSERNAME(), "TRAM")) {
                            if (connection.updateDataTgianDbo(EsspCommon.getUSERNAME(), "TRAM", cTime) != -1) {
                                tvTimeDongBoTram.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        } else {
                            if (connection.insertDataTgianDbo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "TRAM", cTime) != -1) {
                                tvTimeDongBoTram.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime));
                            }
                        }
                        tvTongTram.setText(new StringBuilder("Tổng số trạm: ").append(connection.countTram(EsspCommon.getMaDviqly())).toString());
                    } else {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Bạn đã đồng bộ đủ trạm", Color.WHITE, "OK", Color.WHITE);
                    }
                }
            } catch(Exception ex) {
                Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ trạm", Color.WHITE, "OK", Color.RED);
            }
            progressDialog.dismiss();
        }
    };

    private Handler handlerVatTu = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(checkGetData){
                Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không lấy được dữ liệu hoặc có thể dữ liệu của bạn đã đủ", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    if (countVatTu > 0) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                new StringBuilder("Bạn đã đồng bộ thành công ").append(countVatTu).append("/").append(lstJsonVatTu.size()).append(" vật tư").toString(), Color.WHITE, "OK", Color.WHITE);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cTime = sdf.format(new Date());

                        if (connection.isTGianDboExist(EsspCommon.getUSERNAME(), "VAT_TU")) {
                            if (connection.updateDataTgianDbo(EsspCommon.getUSERNAME(), "VAT_TU", cTime) != -1) {
                                tvTimeDongBoVattu.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        } else {
                            if (connection.insertDataTgianDbo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "VAT_TU", cTime) != -1) {
                                tvTimeDongBoVattu.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        }
                        tvTongVatTu.setText(new StringBuilder("Tổng số vật tư: ").append(connection.countVT(EsspCommon.getMaDviqly())).toString());
                    } else {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Bạn đã đồng bộ đủ vật tư", Color.WHITE, "OK", Color.WHITE);
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ vật tư", Color.WHITE, "OK", Color.RED);
                }
            }
            progressDialog.dismiss();
        }
    };

    private Handler handlerSo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(checkGetData){
                Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không lấy được dữ liệu hoặc có thể dữ liệu của bạn đã đủ", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    if (countSo > 0) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                new StringBuilder("Bạn đã đồng bộ thành công ").append(countSo).append("/").append(lstJsonSoGCS.size()).append(" sổ GCS").toString(), Color.WHITE, "OK", Color.WHITE);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cTime = sdf.format(new Date());

                        if (connection.isTGianDboExist(EsspCommon.getUSERNAME(), "SO_GCS")) {
                            if (connection.updateDataTgianDbo(EsspCommon.getUSERNAME(), "SO_GCS", cTime) != -1) {
                                tvTimeDongBoSo.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        } else {
                            if (connection.insertDataTgianDbo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "SO_GCS", cTime) != -1) {
                                tvTimeDongBoSo.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        }
                        tvTongSoGCS.setText(new StringBuilder("Tổng số trở ngại: ").append(connection.countSoGCS(EsspCommon.getMaDviqly())).toString());
                    } else {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Bạn đã đồng bộ đủ sổ gcs", Color.WHITE, "OK", Color.WHITE);
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ sổ gcs", Color.WHITE, "OK", Color.RED);
                }
            }
            progressDialog.dismiss();
        }
    };

    private Handler handlerTroNgai = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(checkGetData){
                Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không lấy được dữ liệu hoặc có thể dữ liệu của bạn đã đủ", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    if (countTroNgai > 0) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                new StringBuilder("Bạn đã đồng bộ thành công ").append(countTroNgai).append("/").append(lstJsonTroNgai.size()).append(" trở ngại").toString(), Color.WHITE, "OK", Color.WHITE);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cTime = sdf.format(new Date());

                        if (connection.isTGianDboExist(EsspCommon.getUSERNAME(), "TRO_NGAI")) {
                            if (connection.updateDataTgianDbo(EsspCommon.getUSERNAME(), "TRO_NGAI", cTime) != -1) {
                                tvTimeDongBoTroNgai.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        } else {
                            if (connection.insertDataTgianDbo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "TRO_NGAI", cTime) != -1) {
                                tvTimeDongBoTroNgai.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        }
                        tvTongSoTroNgai.setText(new StringBuilder("Tổng số trở ngại: ").append(connection.countTroNgai(EsspCommon.getMaDviqly())).toString());
                    } else {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Bạn đã đồng bộ đủ trở ngại", Color.WHITE, "OK", Color.WHITE);
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ trở ngại", Color.WHITE, "OK", Color.RED);
                }
            }
            progressDialog.dismiss();
        }
    };

    private Handler handlerNganHang = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(checkGetData){
                Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không lấy được dữ liệu hoặc có thể dữ liệu của bạn đã đủ", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    if (countNganHang > 0) {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                new StringBuilder("Bạn đã đồng bộ thành công ").append(countNganHang).append("/").append(lstJsonNganHang.size()).append(" ngân hàng").toString(), Color.WHITE, "OK", Color.WHITE);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String cTime = sdf.format(new Date());

                        if (connection.isTGianDboExist(EsspCommon.getUSERNAME(), "NGAN_HANG")) {
                            if (connection.updateDataTgianDbo(EsspCommon.getUSERNAME(), "NGAN_HANG", cTime) != -1) {
                                tvTimeDongBoNganHang.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        } else {
                            if (connection.insertDataTgianDbo(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME(), "NGAN_HANG", cTime) != -1) {
                                tvTimeDongBoNganHang.setText(new StringBuilder("Thời gian đồng bộ: ").append(cTime).toString());
                            }
                        }
                        tvTongSoNganHang.setText(new StringBuilder("Tổng số ngân hàng: ").append(connection.countNganHang()).toString());
                    } else {
                        Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Bạn đã đồng bộ đủ ngân hàng", Color.WHITE, "OK", Color.WHITE);
                    }
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspDanhMucFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi đồng bộ ngân hàng", Color.WHITE, "OK", Color.RED);
                }
            }
            progressDialog.dismiss();
        }
    };
    //endregion

}
