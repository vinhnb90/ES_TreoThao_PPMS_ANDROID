package com.es.tungnv.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.InfoPhieuTreoThao;
import com.es.tungnv.entity.TthtBBanEntity;
import com.es.tungnv.entity.TthtBBanTuTiEntity;
import com.es.tungnv.entity.TthtCtoEntity;
import com.es.tungnv.entity.TthtDoiSoatEntity;
import com.es.tungnv.entity.TthtEntityLogHistory;
import com.es.tungnv.entity.TthtEntityTTCongTo;
import com.es.tungnv.entity.TthtKHangEntity;
import com.es.tungnv.entity.TthtTuTiEntity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.utils.aiball.Util;
import com.es.tungnv.views.R;
import com.es.tungnv.views.TthtCapNhatBBanActivity;
import com.es.tungnv.views.TthtDetailTaskActivity;
import com.es.tungnv.webservice.TthtAsyncCallWSApi;
import com.es.tungnv.zoomImage.ImageViewTouch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TUNGNV on 3/16/2016.
 */
public class TthtMainFragment extends Fragment implements View.OnClickListener {

    //region Khai báo biến
    private static int sViewWidth = 0, sViewHeight = 0;

    private static TthtSQLiteConnection connection;


    public static EditText etSearch, etCS1, etCS2, etCS3, etCS4, etCS5, etKimNiemChi, etTinhTrangNiemPhong,
            etMaChiKDinh, etMaChiHop, etMaChiBooc, etGhiChu, etTenLoaiCTo, etNhaCungCap, etHeSoNhan;
    private TextView tvTenKH, tvMaGCS, tvSoNo, tvDiaChiKH, tvSaiLech, tvSaiLechTrenDuoi, titleEtCS1, titleEtCS2, titleEtCS3, titleEtCS4, titleEtCS5, tvTongCto, tvDaGhiCto, tvDaGuiCto, tvTenTram;
    private Spinner spFilter, spSoVienChiKDinh, spSoVienChiHop, spSoVienChiHom, mSpinPhuongThucDoXa;
    private AutoCompleteTextView auEtMaTram;

    private ImageButton ibClear;
    private RecyclerView rvKH;
    private static ImageView ivImage;
    private Button btCameraMTB, btGhi, btChucNang, btPreview, btNext, btNhanBBan, btGuiBBan, btDoiSanh, btSwitchThao, btSwitchTreo, btNgayCapNhat;
    private LinearLayout mLLNhaCungCap, mLLPhuongThucDoXa, mLLKimNiemChi, mLLThongTinCongTo, mLLDoSaiLech, mLLDialogGhiTotal;

    private RecyclerView.LayoutManager layoutManager;

    private List<TthtKHangEntity> lstKhangHangTreo = new ArrayList<>();
    private List<TthtKHangEntity> lstKhangHangThao = new ArrayList<>();
    private List<TthtKHangEntity> lstDSoatTreo = new ArrayList<>();
    private List<TthtKHangEntity> lstDSoatThao = new ArrayList<>();
    private List<TthtKHangEntity> lstChooseTreoToSubmit = new ArrayList<>();
    private List<TthtKHangEntity> lstChooseThaoToSubmit = new ArrayList<>();
    private List<InfoPhieuTreoThao> lstPhieuTreoThao = new ArrayList<>();
    private boolean[] isClickChooseArrayDoiSoat = null;


    private List<TthtDoiSoatEntity> lstDoiSanh;
    private TthtInfoPhieuTreoThaoAdapter adapterKH;
    private static TthtDoiSoatAdapter adapterDoiSoat;
    private ArrayAdapter<String> adapterFilter;
    private List<InfoTram> maTramList = new ArrayList<InfoTram>();
    private TthtAsyncCallWSApi asyncCallWSApi;
    private String mMaKimChiNiem = "";
    private int idBBTrTh;

    private static boolean isThreadRun = false;

    private ArrayList<JSONObject> lstDataCMIS;
    private ArrayList<JSONObject> lstBBanCTo;
    private ArrayList<JSONObject> lstChiTietCTo;
    private ArrayList<JSONObject> lstBBanTuTi;
    private ArrayList<JSONObject> lstChiTietTuTi;
    private ArrayList<JSONObject> lstTram;
    private ArrayList<JSONObject> lstLoaiCTo;

    private String mLoaiCto = "";
    private TthtEntityTTCongTo entityTTCongTo;
    private boolean isClickDoiSanh = false;

    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    int count = 0;
    int countBBan = 0;
    int countCTo = 0;
    int countBBanTuTi = 0;
    int countChiTietTuTi = 0;
    int countTram = 0;
    int countLoaiCTo = 0;
    //ghi trang thai dong bo va upload du lieu phuc vu viec Log history
    boolean logDownload = false;
    boolean logUpload = false;

    private int maxSize = 0;
    private float snapProgresBar = 1f;
    private int countFileSent = 0;

    private static final int CAMERA_REQUEST = 1888;

    private Dialog dialogImageZoom;
    private ProgressDialog progressDialog;
    private static int totalBTCDTD, cs1, cs2, cs3, cs4;
    //endregion

    //region Khởi tạo

    private List<InfoPhieuTreoThao> setDataBBanWithMA_BDONG(String MA_BDONG) {

        List<InfoPhieuTreoThao> result = new ArrayList<>();
        if (MA_BDONG == null || MA_BDONG.isEmpty())
            return result;
        if (TthtCommon.getTthtDateChon().equals(""))
            return result;

        String ngayConvert = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);

        Cursor cursorDataHistory = null;
        Cursor cursorDataToday = null;
        //lấy từ ngày trước ngày ngayConvert với trạng thái biên bản là 1
        if (MA_BDONG == TthtCommon.arrMaBDong[0]) {
            cursorDataHistory = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_HISTORY);
            cursorDataToday = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_TODAY);
        }
        if (MA_BDONG == TthtCommon.arrMaBDong[1]) {
            cursorDataHistory = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_HISTORY);
            cursorDataToday = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.PHIEU_TREO_THAO_TODAY);
        }

//        if (cursorDataHistory != null && cursorDataHistory.getCount() != 0) {
//            do {
//                InfoPhieuTreoThao phieuTreoThao = new InfoPhieuTreoThao();
//                phieuTreoThao.setID_BBAN_TRTH(cursorDataHistory.getInt(cursorDataHistory.getColumnIndex("ID_BBAN_TRTH")));
//                phieuTreoThao.setTEN_KHANG(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("TEN_KHANG")));
//                phieuTreoThao.setDCHI_HDON(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("DCHI_HDON")));
//                phieuTreoThao.setTYPE_CALL_API(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("SO_BBAN")));
//                phieuTreoThao.setCHI_SO(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("CHI_SO")));
//                phieuTreoThao.setCHI_SO_SAULAP_TUTI(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("CHI_SO_SAULAP_TUTI")));
//                phieuTreoThao.setMA_CTO(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("MA_CTO")));
//                phieuTreoThao.setSO_CTO(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("SO_CTO")));
//                phieuTreoThao.setMA_GCS_CTO(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("MA_GCS_CTO")));
//                phieuTreoThao.setMA_TRAM(cursorDataHistory.getString(cursorDataHistory.getColumnIndex("MA_TRAM")));
//                phieuTreoThao.setID_CHITIET_CTO(cursorDataHistory.getInt(cursorDataHistory.getColumnIndex("ID_CHITIET_CTO")));
//                phieuTreoThao.setTRANG_THAI_DU_LIEU(cursorDataHistory.getInt(cursorDataHistory.getColumnIndex("TRANG_THAI_DU_LIEU")));
//
//                result.add(phieuTreoThao);
//            } while (cursorDataHistory.moveToNext());
//        }

        if (cursorDataToday != null && cursorDataToday.getCount() != 0) {
            do {
                InfoPhieuTreoThao phieuTreoThao = new InfoPhieuTreoThao();
                phieuTreoThao.setID_BBAN_TRTH(cursorDataToday.getInt(cursorDataToday.getColumnIndex("ID_BBAN_TRTH")));
                phieuTreoThao.setTEN_KHANG(cursorDataToday.getString(cursorDataToday.getColumnIndex("TEN_KHANG")));
                phieuTreoThao.setDCHI_HDON(cursorDataToday.getString(cursorDataToday.getColumnIndex("DCHI_HDON")));
                phieuTreoThao.setSO_BBAN(cursorDataToday.getString(cursorDataToday.getColumnIndex("SO_BBAN")));
                phieuTreoThao.setCHI_SO(cursorDataToday.getString(cursorDataToday.getColumnIndex("CHI_SO")));
                phieuTreoThao.setCHI_SO_SAULAP_TUTI(cursorDataToday.getString(cursorDataToday.getColumnIndex("CHI_SO_SAULAP_TUTI")));
                phieuTreoThao.setMA_CTO(cursorDataToday.getString(cursorDataToday.getColumnIndex("MA_CTO")));
                phieuTreoThao.setSO_CTO(cursorDataToday.getString(cursorDataToday.getColumnIndex("SO_CTO")));
                phieuTreoThao.setMA_GCS_CTO(cursorDataToday.getString(cursorDataToday.getColumnIndex("MA_GCS_CTO")));
                phieuTreoThao.setMA_TRAM(cursorDataToday.getString(cursorDataToday.getColumnIndex("MA_TRAM")));
                phieuTreoThao.setID_CHITIET_CTO(cursorDataToday.getInt(cursorDataToday.getColumnIndex("ID_CHITIET_CTO")));
                phieuTreoThao.setTRANG_THAI_DU_LIEU(cursorDataToday.getInt(cursorDataToday.getColumnIndex("TRANG_THAI_DU_LIEU")));

                result.add(phieuTreoThao);
            } while (cursorDataToday.moveToNext());
        }

        return result;
    }

    private void invalidatReyclerView(String MA_BDONG) {

        if (adapterKH == null) {
            adapterKH = new TthtInfoPhieuTreoThaoAdapter(lstPhieuTreoThao, MA_BDONG);
            rvKH.setAdapter(adapterKH);
        } else
            adapterKH.refreshData(lstPhieuTreoThao, MA_BDONG);
        if (!(rvKH.getAdapter() instanceof TthtInfoPhieuTreoThaoAdapter)) {
            rvKH.removeAllViews();
            rvKH.invalidate();
            rvKH.setAdapter(adapterKH);
        }
        rvKH.invalidate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        try {
            //TODO init
            rootView = inflater.inflate(R.layout.ttht_fragment_main, null);
            initComponent(rootView);
            setAction(rootView);

//            TthtCommon.setTthtDateChon("01/11/2016");
            //TODO fill first data with B (Treo) and full KH
//            setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);
        } catch (Exception ex) {
            Log.d(TAG, "onCreateView: error " + ex.getMessage());
        }
        return rootView;
    }

    private void setAction(final View rootView) {
        //TODO get tvDate now and set tvDate
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        String dateNow = sdf.format(cal.getTime());

        TthtCommon.setTthtDateChon(dateNow);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isClickDoiSanh) {
//                        setTitleThongKe(3);
//                        setDataKHOnRecycleView(TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()], 3);
                        btDoiSanh.setText("ĐỐI SÁNH");
                        isClickDoiSanh = false;
                        return true;
                    } else {
                        getActivity().finish();
                        return false;
                    }
                }
                return false;
            }
        });

        spFilter.post(new Runnable() {
            @Override
            public void run() {
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isClickDoiSanh) {
//                            setTitleThongKe(2);
//                            setDataKHOnRecycleView(TthtCommon.arrMaBDong[position], 2);

                        } else {
                            if (!TthtCommon.getTthtDateChon().equals("")) {
                                setDataAutoTextMA_TRAM();
                            }
                            setTitleThongKe(TthtCommon.FILTER_DATA_FILL.ALL);
                            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
                            lstPhieuTreoThao.clear();
                            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
                            invalidatReyclerView(MA_BDONG);
//                            adapterKH.updateList(lstKhangHangTreo, lstKhangHangThao, MA_BDONG);
                            if (!TthtCommon.getTenTramSelected().equals("")) {
                                auEtMaTram.setText(TthtCommon.getMaTramSelected());
                                tvTenTram.setText(TthtCommon.getTenTramSelected());
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Cursor c = connection.getAllMaTramBBanTThao(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien());
            if (c != null) {
                maTramList.clear();
                //TODO fill data MA_TRAM - TEN_TRAM to auto TextView
                do {
                    String MA_TRAM = c.getString(c.getColumnIndex("MA_TRAM"));
                    Cursor c1 = connection.getInfoTRAMWithMA_TRAM(MA_TRAM);
                    if (c1 != null) {
                        InfoTram tram = new InfoTram(MA_TRAM, c1.getString(c1.getColumnIndex("TEN_TRAM")));
                        maTramList.add(tram);
                        c1.close();
                    } else {
                        Log.e(TAG, "onResume: Lỗi: Không thấy tên trạm trong cơ sở dữ liệu!");
//                        throw new Exception("Lỗi: Không thấy tên trạm trong cơ sở dữ liệu!");
                    }
                } while (c.moveToNext());
            }
               /* else {
                    throw new Exception("Lỗi: Không thấy trạm trong cơ sở dữ liệu!");
                }*/

            //TODO fill adapter MA_TRAM-TEN_TRAM
            TramArrayAdapter adapterUser = new TramArrayAdapter(getActivity(), R.layout.ttht_row_auto_complex_tv_tram, maTramList);
            auEtMaTram.setThreshold(2);
            auEtMaTram.setAdapter(adapterUser);

            //TODO set with when drop down TRAM autotext
            int widthFragment = getActivity().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
            auEtMaTram.setDropDownWidth(widthFragment);
            auEtMaTram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    Object item = parent.getItemAtPosition(position);
                    if (item instanceof InfoTram) {
                        InfoTram tram = (InfoTram) item;
                        auEtMaTram.setText(tram.getMA_TRAM());
                        tvTenTram.setText(tram.getTEN_TRAM());
                        TthtCommon.setMaTramSelected(tram.getMA_TRAM());
                        TthtCommon.setTenTramSelected(tram.getTEN_TRAM());
                    }
                }
            });
            auEtMaTram.invalidate();
            setDataAutoTextMA_TRAM();


            /*String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
            int countBBanWithDate = connection.countBBanWithDateSelectedFULLMA_TRAM(NGAY_TRTH, TthtCommon.getMaNvien(), TthtCommon.getMaDviqly());

            if (countBBanWithDate == 0) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TthtCapNhatBBanActivity.this);
                builder.setMessage("Không có dữ liệu của ngày:\n "
                        + TthtCommon.getTthtDateChon() + "\n\tBạn có muốn tải dữ liệu mới? ");
                builder.setPositiveButton("Không", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Có", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TthtCommon.isDownload = true;
                        finish();
                    }
                });
                builder.show();
            }else {
                TthtCommon.setMaTramSelected("");
                setDataBBanWithMA_BDONG(TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
            }*/

            btNgayCapNhat.setText(TthtCommon.getTthtDateChon());
            //TODO fill data recyclerview
            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
            lstPhieuTreoThao.clear();
            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
            invalidatReyclerView(MA_BDONG);
            int count = (rvKH.getAdapter() == null) ? 0 : rvKH.getAdapter().getItemCount();
            btNgayCapNhat.setText(TthtCommon.getTthtDateChon() + "-(" + count + ")");
            setTitleThongKeDate();
            /*if (!TthtCommon.isDownload) {
                setTitleThongKe(TthtCommon.FILTER_DATA_FILL.ALL);
                setDataBBanWithMA_BDONG(TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
//                setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);

                //TODO setText
                auEtMaTram.setText(TthtCommon.getMaTramSelected());
                tvTenTram.setText(TthtCommon.getTenTramSelected());
            } else {
                TthtCommon.isDownload = false;
                if (!isThreadRun) {
                    isThreadRun = true;
                    updateBBan();
                }
            }*/
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi hiển thị màn hình", Color.RED, "Nội dung lỗi:" + ex.getMessage(), Color.WHITE, "OK", Color.RED);
        }

    }

    private void updateBBan() {
        if (!Common.isNetworkOnline(getActivity())) {
            Common.showAlertDialogGreen(getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
        } else {
            lstDataCMIS = new ArrayList<>();
            lstDataCMIS = new ArrayList<>();
            lstBBanCTo = new ArrayList<>();
            lstChiTietCTo = new ArrayList<>();
            lstTram = new ArrayList<>();
            lstBBanCTo = new ArrayList<>();
            lstChiTietTuTi = new ArrayList<>();

            progressDialog = new ProgressDialog(this.getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Đang đồng bộ biên bản ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        count = 0;
                        countBBan = 0;
                        countCTo = 0;
                        countBBanTuTi = 0;
                        countChiTietTuTi = 0;
                        progressBarStatus = 0;
                        Thread.sleep(50);
                        String MA_DVIQLY = TthtCommon.getMaDviqly();
                        String MA_NVIEN = TthtCommon.getMaNvien();

                        //TODO get CMIS
                        lstDataCMIS = asyncCallWSApi.WS_GET_DATA_CMIS_CALL(MA_DVIQLY, MA_NVIEN);

                        if (lstDataCMIS.size() > 0) {
                            JSONObject mapCMIS = lstDataCMIS.get(0);
                            if (mapCMIS.getString("RESULT").equals("OK")) {
                                //TODO bật log ghi log đồng bộ CMIS, cứ mỗi bước thì bật hoặc tắt trạng thái
                                logDownload = true;

                                //TODO get biên bản
                                lstBBanCTo = asyncCallWSApi.WS_GET_BIEN_BAN_CALL(MA_DVIQLY, MA_NVIEN);
                                int maxSize = lstBBanCTo.size();
                                float snap = (float) maxSize / 100f;
                                if (lstBBanCTo != null) {
                                    logDownload = false;
                                    for (int i = 0; i < maxSize; i++) {
                                        JSONObject map = lstBBanCTo.get(i);

                                        //TODO kiểm tra TRANG_THAI trên table BBAN_CONGTO ID_BBAN_TRTH = 1742727
                                        if (map.getInt("ID_BBAN_TRTH") == 1742727)
                                            Log.d(TAG, "run: ");
                                        int checkUpload = connection.getTinhTrangBBanTThao(map.getInt("ID_BBAN_TRTH"));

                                        //TODO nếu ghi nhưng chưa submit thì update các trường có thể thay đổi
                                        if (checkUpload == 1) {
                                            long rowUpdate = connection.updateDataBBanByMaDviAndID(map.getString("MA_NVIEN"), map.getInt("ID_BBAN_TRTH"),
                                                    map.getString("MA_DDO"), map.getString("SO_BBAN"), map.getString("NGAY_TRTH"),
                                                    map.getString("BUNDLE_MA_NVIEN"), map.getString("MA_LDO"), map.getString("NGAY_TAO"),
                                                    map.getString("NGUOI_TAO"), map.getString("NGAY_SUA"), map.getString("NGUOI_SUA"),
                                                    map.getString("MA_CNANG"), map.getString("MA_YCAU_KNAI"), map.getInt("TRANG_THAI"),
                                                    map.getString("GHI_CHU"), map.getInt("ID_BBAN_CONGTO"), map.getString("LOAI_BBAN"),
                                                    map.getString("TEN_KHANG"), map.getString("DCHI_HDON"), map.getString("DTHOAI"),
                                                    map.getString("MA_GCS_CTO"), map.getString("MA_TRAM"), map.getString("MA_HDONG"),
                                                    map.getString("MA_KHANG"), map.getString("LY_DO_TREO_THAO"));
                                            if (rowUpdate > 0) {
                                                lstChiTietCTo = asyncCallWSApi.WS_GET_CONG_TO_CALL(TthtCommon.getMaDviqly(), map.getString("ID_BBAN_TRTH"));
                                                if (lstChiTietCTo != null) {
                                                    logDownload = true;
                                                    int lstChiTietCToSize = lstChiTietCTo.size();
                                                    if (lstChiTietCToSize != 2) {
                                                        connection.deleteDataBBanByMaDviAndID(TthtCommon.getMaDviqly(), map.getInt("ID_BBAN_TRTH"));
                                                        countBBan--;
                                                    } else {
                                                        for (int j = 0; j < lstChiTietCToSize; j++) {
                                                            JSONObject mapCTo = lstChiTietCTo.get(j);

                                                            //TODO get Data
                                                            String maChiKiemDinh = "";
                                                            String phuongThucDoXa = "";
                                                            String maBienDong = mapCTo.getString("MA_BDONG");
                                                            if (maBienDong.equals("B")) {
                                                                mMaKimChiNiem = mapCTo.getString("SO_KIM_NIEM_CHI");
                                                                phuongThucDoXa = mapCTo.getString("PHUONG_THUC_DO_XA");
                                                            }

                                                            long update = connection.updateChiSoTTinCtoWhenDownload(
                                                                    mapCTo.getInt("ID_BBAN_TRTH"),
                                                                    mapCTo.getString("MA_NVIEN"),
                                                                    mapCTo.getString("SO_KIM_NIEM_CHI"),
                                                                    mapCTo.getString("MA_SOCHOM"),
                                                                    mapCTo.getString("MA_CHIKDINH"),
                                                                    mapCTo.getString("MA_SOCBOOC"),
                                                                    mapCTo.getInt("HS_NHAN"),
                                                                    mapCTo.getInt("LOAI_HOM"),
                                                                    phuongThucDoXa, 0,
                                                                    maBienDong,
                                                                    mapCTo.getInt("ID_BBAN_TUTI"));
                                                            if (update != -1) {
                                                                countCTo++;
                                                            }
                                                        }
                                                    }

                                                } else {
                                                    logDownload = false;
                                                }
                                                countBBan++;
                                            }
                                        }

                                        //TODO nếu checkUpload trả về 0 tức chưa có bản ghi
                                        if (checkUpload == 0) {
                                            long rowInsert = connection.insertDataBBanCto(map.getString("MA_NVIEN"), map.getInt("ID_BBAN_TRTH"),
                                                    map.getString("MA_DDO"), map.getString("SO_BBAN"), map.getString("NGAY_TRTH"),
                                                    map.getString("BUNDLE_MA_NVIEN"), map.getString("MA_LDO"), map.getString("NGAY_TAO"),
                                                    map.getString("NGUOI_TAO"), map.getString("NGAY_SUA"), map.getString("NGUOI_SUA"),
                                                    map.getString("MA_CNANG"), map.getString("MA_YCAU_KNAI"), map.getInt("TRANG_THAI"),
                                                    map.getString("GHI_CHU"), map.getInt("ID_BBAN_CONGTO"), map.getString("LOAI_BBAN"),
                                                    map.getString("TEN_KHANG"), map.getString("DCHI_HDON"), map.getString("DTHOAI"),
                                                    map.getString("MA_GCS_CTO"), map.getString("MA_TRAM"), map.getString("MA_HDONG"),
                                                    map.getString("MA_KHANG"), map.getString("LY_DO_TREO_THAO"));
                                            if (rowInsert > 0) {
                                                lstChiTietCTo = asyncCallWSApi.WS_GET_CONG_TO_CALL(TthtCommon.getMaDviqly(), map.getString("ID_BBAN_TRTH"));
                                                if (lstChiTietCTo != null) {
                                                    logDownload = true;
                                                    int lstChiTietCToSize = lstChiTietCTo.size();

                                                    //nếu lstChiTietCToSize != 2 thì không cho ghi biên bản này, xóa biên bản của công tơ này
                                                    if (lstChiTietCToSize != 2) {
                                                        connection.deleteDataBBanByMaDviAndID(TthtCommon.getMaDviqly(), map.getInt("ID_BBAN_TRTH"));
                                                        countBBan--;
                                                    } else {
                                                        for (int j = 0; j < lstChiTietCToSize; j++) {
                                                            JSONObject mapCTo = lstChiTietCTo.get(j);
                                                            String maBienDong = mapCTo.getString("MA_BDONG");
                                                            String phuongThucDoXa = "";
                                                            String maChiKiemDinh = mapCTo.getString("MA_CHIKDINH");
                                                            if (maBienDong.equals("B")) {
                                                                mMaKimChiNiem = mapCTo.getString("SO_KIM_NIEM_CHI");
                                                                phuongThucDoXa = mapCTo.getString("PHUONG_THUC_DO_XA");
                                                            }

                                                            long insert = connection.insertDataTTinCto(mapCTo.getString("MA_NVIEN"), mapCTo.getInt("ID_BBAN_TRTH"),
                                                                    mapCTo.getString("MA_CTO"), mapCTo.getString("SO_CTO"), mapCTo.getInt("LAN"),
                                                                    mapCTo.getString("MA_BDONG"), mapCTo.getString("NGAY_BDONG"), mapCTo.getString("MA_CLOAI"),
                                                                    mapCTo.getString("LOAI_CTO"), mapCTo.getInt("VTRI_TREO"),
                                                                    mapCTo.getString("MO_TA_VTRI_TREO"),
                                                                    mapCTo.getString("MA_SOCBOOC"),
                                                                    mapCTo.getInt("SO_VIENCBOOC"), mapCTo.getInt("LOAI_HOM"), mapCTo.getString("MA_SOCHOM"), mapCTo.getInt("SO_VIENCHOM"),
                                                                    mapCTo.getInt("HS_NHAN"), mapCTo.getString("NGAY_TAO"), mapCTo.getString("NGUOI_TAO"),
                                                                    mapCTo.getString("NGAY_SUA"), mapCTo.getString("NGUOI_SUA"), mapCTo.getString("MA_CNANG"),
                                                                    mapCTo.getString("SO_TU"), mapCTo.getString("SO_TI"), mapCTo.getString("SO_COT"),
                                                                    mapCTo.getString("SO_HOM"), mapCTo.getString("CHI_SO"), mapCTo.getString("NGAY_KDINH"),
                                                                    mapCTo.getString("NAM_SX"), mapCTo.getString("TEM_CQUANG"), maChiKiemDinh,
                                                                    mapCTo.getString("MA_TEM"), mapCTo.getInt("SOVIEN_CHIKDINH"), mapCTo.getString("DIEN_AP"),
                                                                    mapCTo.getString("DONG_DIEN"), mapCTo.getString("HANGSO_K"), mapCTo.getString("MA_NUOC"),
                                                                    mapCTo.getString("TEN_NUOC"), mapCTo.getInt("ID_CHITIET_CTO"), mapCTo.getString("SO_KIM_NIEM_CHI"),
                                                                    mapCTo.getString("TTRANG_NPHONG"), mapCTo.getString("TEN_LOAI_CTO"), phuongThucDoXa, "", 0, mapCTo.getInt("ID_BBAN_TUTI"));
                                                            if (insert > 0) {
                                                                countCTo++;
                                                            }
                                                        }
                                                    }

                                                }
                                                countBBan++;
                                            }
                                        }
                                        progressBarStatus = (int) (count / snap);
                                        progressBarHandler.post(new Runnable() {
                                            public void run() {
                                                progressDialog.setProgress(progressBarStatus);
                                            }
                                        });
                                        count++;
                                    }
                                } else {
                                    logDownload = false;
                                }
                            } else {
                                logDownload = false;
                                TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                                "Lỗi đồng bộ dữ liệu từ CMIS", Color.WHITE, "OK", Color.WHITE);
                                    }
                                });
                            }
                        }
                    } catch (final Exception e) {
                        logDownload = false;
                        TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi nhận biên bản", Color.WHITE,
                                        e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }

                    try {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setMessage("Đang đồng bộ dữ liệu nhà cung cấp dịch vụ đo xa....");
                                progressDialog.setProgress(0);
                            }
                        });
                        lstLoaiCTo = asyncCallWSApi.WS_GET_ALL_DATA_LOAI_CONGTO();
                        int maxSize = lstLoaiCTo.size();
                        float snap = (float) maxSize / 100f;
                        if (lstLoaiCTo.size() > 0) {

                            //TODO ghi lại dữ liệu loại công tơ
                            connection.deleteAllLoaiCongTo();
                            for (int i = 0; i < lstLoaiCTo.size(); i++) {
                                JSONObject loaiCTo = lstLoaiCTo.get(i);
                                if (connection.insertLoaiCongTo(
                                        loaiCTo.getString("MA_CLOAI"),
                                        loaiCTo.getString("TEN_LOAI_CTO"),
                                        loaiCTo.getString("MO_TA"),
                                        loaiCTo.getString("SO_PHA"),
                                        loaiCTo.getString("SO_DAY"),
                                        loaiCTo.getString("SO_CS"),
                                        loaiCTo.getString("CAP_CXAC_P"),
                                        loaiCTo.getString("CAP_CXAC_Q"),
                                        loaiCTo.getString("DONG_DIEN"),
                                        loaiCTo.getString("DIEN_AP"),
                                        loaiCTo.getString("VH_CONG"),
                                        loaiCTo.getString("MA_NUOC"),
                                        loaiCTo.getString("MA_HANG"),
                                        loaiCTo.getString("HANGSO_K"),
                                        loaiCTo.getString("PTHUC_DOXA"),
                                        loaiCTo.getString("TEN_NUOC")
                                ) != -1) {
                                    progressBarStatus = (int) (countLoaiCTo / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                    countLoaiCTo++;
                                }
                            }
                        }
                    } catch (final Exception e) {
                        logDownload = false;
                        TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi nhận dữ liệu đo xa", Color.WHITE,
                                        e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }

                    try {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setMessage("Đang đồng bộ dữ liệu mã trạm....");
                                progressDialog.setProgress(0);
                            }
                        });
                        lstTram = asyncCallWSApi.WS_GET_TRAM_CALL();

                        int sizeTram = lstTram.size();
                        float snap = (float) sizeTram / 100f;
                        if (sizeTram > 0) {
                            countTram = 0;
                            //TODO ghi lại dữ liệu mã trạm
                            connection.deleteDataTRAMWithMA_DVIQLY(TthtCommon.getMaDviqly());
                            for (int i = 0; i < sizeTram; i++) {
                                JSONObject tram = lstTram.get(i);
                                if (connection.insertDataTRAM(
                                        tram.getString("MA_TRAM"),
                                        tram.getString("MA_NVIEN"),
                                        tram.getString("TEN_TRAM"),
                                        tram.getString("LOAI_TRAM"),
                                        tram.getString("CSUAT_TRAM"),
                                        tram.getString("MA_CAP_DA"),
                                        tram.getString("MA_CAP_DA_RA"),
                                        tram.getString("DINH_DANH")
                                ) != -1) {
                                    progressBarStatus = (int) (countTram / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                    countTram++;
                                }
                            }
                        }
                    } catch (final Exception e) {
                        logDownload = false;
                        TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi nhận dữ liệu", Color.WHITE,
                                        e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }

                    try {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setMessage("Đang đồng bộ dữ liệu Biên bản TU TI....");
                                progressDialog.setProgress(0);
                            }
                        });
                        lstBBanTuTi = asyncCallWSApi.WS_GET_BBan_TU_TI_CALL();
                        int sizeBBTuTi = lstBBanTuTi.size();
                        float snap = (float) sizeBBTuTi / 100f;
                        if (sizeBBTuTi > 0) {
                            countBBanTuTi = 0;
                            //TODO ghi lại dữ liệu biên bản TU TI
//                            connection.deleteBBanTuTiWithDviAndMaNVien(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien());
                            for (int i = 0; i < sizeBBTuTi; i++) {
                                JSONObject bbTuTi = lstBBanTuTi.get(i);
                                boolean checkBBanTuTi = connection.checkBBanTuTi(bbTuTi.getInt("ID_BBAN_TUTI"));
                                long rowAffect = -1;
                                if (checkBBanTuTi) {
                                    rowAffect = connection.updateBBanTuTi(
                                            bbTuTi.getString("MA_NVIEN"),
                                            bbTuTi.getInt("ID_BBAN_TUTI"),
                                            bbTuTi.getString("MA_DDO"),
                                            bbTuTi.getString("SO_BBAN"),
                                            bbTuTi.getString("NGAY_TRTH"),
                                            bbTuTi.getString("BUNDLE_MA_NVIEN"),
                                            bbTuTi.getInt("TRANG_THAI"),
                                            bbTuTi.getString("TEN_KHANG"),
                                            bbTuTi.getString("DCHI_HDON"),
                                            bbTuTi.getString("DTHOAI"),
                                            bbTuTi.getString("MA_GCS_CTO"),
                                            bbTuTi.getString("MA_TRAM"),
                                            bbTuTi.getString("LY_DO_TREO_THAO"),
                                            bbTuTi.getString("MA_KHANG"),
                                            bbTuTi.getInt("ID_BBAN_WEB_TUTI"),
                                            bbTuTi.getString("NVIEN_KCHI")
                                    );
                                } else {
                                    rowAffect = connection.insertBBanTuTi(
                                            bbTuTi.getString("MA_NVIEN"),
                                            bbTuTi.getInt("ID_BBAN_TUTI"),
                                            bbTuTi.getString("MA_DDO"),
                                            bbTuTi.getString("SO_BBAN"),
                                            bbTuTi.getString("NGAY_TRTH"),
                                            bbTuTi.getString("BUNDLE_MA_NVIEN"),
                                            bbTuTi.getInt("TRANG_THAI"),
                                            bbTuTi.getString("TEN_KHANG"),
                                            bbTuTi.getString("DCHI_HDON"),
                                            bbTuTi.getString("DTHOAI"),
                                            bbTuTi.getString("MA_GCS_CTO"),
                                            bbTuTi.getString("MA_TRAM"),
                                            bbTuTi.getString("LY_DO_TREO_THAO"),
                                            bbTuTi.getString("MA_KHANG"),
                                            bbTuTi.getInt("ID_BBAN_WEB_TUTI"),
                                            bbTuTi.getString("NVIEN_KCHI")
                                    );
                                }

                                if (rowAffect != -1) {
                                    lstChiTietTuTi = asyncCallWSApi.WS_GET_CHITIET_TU_TI_CALL(bbTuTi.getInt("ID_BBAN_TUTI"));
                                    if (lstChiTietTuTi != null) {
                                        logDownload = true;
                                        int sizeChiTietTuTi = lstChiTietTuTi.size();
                                        for (int j = 0; j < sizeChiTietTuTi; j++) {
                                            JSONObject mapTuTi = lstChiTietTuTi.get(j);
                                            boolean checkTuTi = connection.checkTuTi(mapTuTi.getInt("ID_CHITIET_TUTI"));
                                            long affectRow = 0;
                                            if (checkTuTi) {
                                                affectRow = connection.updateTuTi(
                                                        mapTuTi.getString("MA_CLOAI"),
                                                        mapTuTi.getString("LOAI_TU_TI"),
                                                        mapTuTi.getString("MO_TA"),
                                                        mapTuTi.getInt("SO_PHA"),
                                                        mapTuTi.getString("TYSO_DAU"),
                                                        mapTuTi.getInt("CAP_CXAC"),
                                                        mapTuTi.getInt("CAP_DAP"),
                                                        mapTuTi.getString("MA_NUOC"),
                                                        mapTuTi.getString("MA_HANG"),
                                                        mapTuTi.getInt("TRANG_THAI"),
                                                        mapTuTi.getBoolean("IS_TU"),
                                                        mapTuTi.getInt("ID_BBAN_TUTI"),
                                                        mapTuTi.getInt("ID_CHITIET_TUTI"),
                                                        mapTuTi.getString("SO_TU_TI"),
                                                        mapTuTi.getString("NUOC_SX"),
                                                        mapTuTi.getString("SO_TEM_KDINH"),
                                                        mapTuTi.getString("NGAY_KDINH"),
                                                        mapTuTi.getString("MA_CHI_KDINH"),
                                                        mapTuTi.getString("MA_CHI_HOP_DDAY"),
                                                        mapTuTi.getInt("SO_VONG_THANH_CAI"),
                                                        mapTuTi.getString("TYSO_BIEN"),
                                                        mapTuTi.getString("MA_BDONG"),
                                                        mapTuTi.getString("MA_NVIEN")
                                                );
                                            } else {
                                                affectRow = connection.insertTuTi(
                                                        mapTuTi.getString("MA_CLOAI"),
                                                        mapTuTi.getString("LOAI_TU_TI"),
                                                        mapTuTi.getString("MO_TA"),
                                                        mapTuTi.getInt("SO_PHA"),
                                                        mapTuTi.getString("TYSO_DAU"),
                                                        mapTuTi.getInt("CAP_CXAC"),
                                                        mapTuTi.getInt("CAP_DAP"),
                                                        mapTuTi.getString("MA_NUOC"),
                                                        mapTuTi.getString("MA_HANG"),
                                                        mapTuTi.getInt("TRANG_THAI"),
                                                        mapTuTi.getBoolean("IS_TU"),
                                                        mapTuTi.getInt("ID_BBAN_TUTI"),
                                                        mapTuTi.getInt("ID_CHITIET_TUTI"),
                                                        mapTuTi.getString("SO_TU_TI"),
                                                        mapTuTi.getString("NUOC_SX"),
                                                        mapTuTi.getString("SO_TEM_KDINH"),
                                                        mapTuTi.getString("NGAY_KDINH"),
                                                        mapTuTi.getString("MA_CHI_KDINH"),
                                                        mapTuTi.getString("MA_CHI_HOP_DDAY"),
                                                        mapTuTi.getInt("SO_VONG_THANH_CAI"),
                                                        mapTuTi.getString("TYSO_BIEN"),
                                                        mapTuTi.getString("MA_BDONG"),
                                                        mapTuTi.getString("MA_NVIEN"));
                                            }
                                            if (affectRow > 0) {
                                                countChiTietTuTi++;
                                            }
                                        }
                                    }
                                    countBBanTuTi++;
                                }
                            }
                        }
                    } catch (final Exception e) {
                        logDownload = false;
                        TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi nhận dữ liệu mã trạm", Color.WHITE,
                                        e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }

                    /*try {
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressDialog.setMessage("Đang đồng bộ dữ liệu Chi tiết TU TI....");
                                progressDialog.setProgress(0);
                            }
                        });
                        lstChiTietTuTi = asyncCallWSApi.WS_GET_CHITIET_TU_TI_CALL();

                        int sizeChiTietTuTi = lstChiTietTuTi.size();
                        float snap = (float) sizeChiTietTuTi / 100f;
                        if (sizeChiTietTuTi > 0) {
                            countChiTietTuTi = 0;
                            //TODO ghi lại dữ liệu biên bản TU TI
                            connection.deleteChiTietTuTiWithDviAndMaNVien(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien());
                            for (int i = 0; i < sizeChiTietTuTi; i++) {
                                JSONObject chiTietTuTi = lstChiTietTuTi.get(i);
                                if (connection.insertTuTi(
                                        chiTietTuTi.getString("MA_CLOAI"),
                                        chiTietTuTi.getString("LOAI_TU_TI"),
                                        chiTietTuTi.getString("MO_TA"),
                                        chiTietTuTi.getInt("SO_PHA"),
                                        chiTietTuTi.getString("TYSO_DAU"),
                                        chiTietTuTi.getInt("CAP_CXAC"),
                                        chiTietTuTi.getInt("CAP_DAP"),
                                        chiTietTuTi.getString("MA_NUOC"),
                                        chiTietTuTi.getString("MA_HANG"),
                                        chiTietTuTi.getInt("TRANG_THAI"),
                                        chiTietTuTi.getBoolean("IS_TU"),
                                        chiTietTuTi.getInt("ID_BBAN_TUTI"),
                                        chiTietTuTi.getInt("ID_CHITIET_TUTI"),
                                        chiTietTuTi.getString("SO_TU_TI"),
                                        chiTietTuTi.getString("NUOC_SX"),
                                        chiTietTuTi.getString("SO_TEM_KDINH"),
                                        chiTietTuTi.getString("NGAY_KDINH"),
                                        chiTietTuTi.getString("MA_CHI_KDINH"),
                                        chiTietTuTi.getString("MA_CHI_HOP_DDAY"),
                                        chiTietTuTi.getInt("SO_VONG_THANH_CAI"),
                                        chiTietTuTi.getString("TYSO_BIEN"),
                                        chiTietTuTi.getString("MA_BDONG"),
                                        chiTietTuTi.getString("MA_NVIEN")
                                        ) != -1) {
                                    progressBarStatus = (int) (countTram / snap);
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressDialog.setProgress(progressBarStatus);
                                        }
                                    });
                                    countChiTietTuTi++;
                                }
                            }
                        }
                    } catch (final Exception e) {
                        logDownload = false;
                        TthtMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi nhận dữ liệu mã trạm", Color.WHITE,
                                        e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }*/


                    if (logDownload) {
                        String maNVien = TthtCommon.getMaNvien();
                        TthtEntityLogHistory logHistory = new TthtEntityLogHistory(maNVien, "1");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ID_NHANVIEN", logHistory.getID_NHANVIEN());
                            jsonObject.put("ACTION", logHistory.getACTION());
                            JSONObject jsonObjectRsult = asyncCallWSApi.WS_REQUEST_LOG_CALL(jsonObject);

                            if (jsonObjectRsult.getString("RESULT").equals("OK")) {
                                Log.d("log", "Ghi log upload thành công!");
                            } else {
                                Log.d("log", "Lỗi ghi log upload!");
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                    handlerGet.sendEmptyMessage(0);
                }
            });
            thread.start();
        }
    }

    private void setTitleThongKeDate() {
        int tongCTo = 0, cToDaGhi = 0, cToDaGui = 0;
        String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
        String spFilterChoose = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];

        tongCTo = connection.getCountCToByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaTramSelected(), NGAY_TRTH);
        cToDaGhi = connection.getCountCToDaGhiByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), NGAY_TRTH);
        cToDaGui = connection.getCountCToDaGuiByDviWithMaTram(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), spFilterChoose, NGAY_TRTH);

        tvTongCto.setText(String.valueOf(tongCTo));
        tvDaGhiCto.setText(String.valueOf(cToDaGhi));
        tvDaGuiCto.setText(String.valueOf(cToDaGui));
    }

    private void setTitleThongKeDateDoiSoat(int tongCTo, int cToDaGhi, int cToDaGui) {
//        int tongCTo = 0, cToDaGhi = 0, cToDaGui = 0;
//        String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
//        String spFilterChoose = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
//
//        tongCTo = connection.getCountCToByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaTramSelected(), NGAY_TRTH);
//        cToDaGhi = connection.getCountCToDaGhiByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), NGAY_TRTH);
//        cToDaGui = connection.getCountCToDaGuiByDviWithMaTram(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), spFilterChoose, NGAY_TRTH);

        tvTongCto.setText(String.valueOf(tongCTo));
        tvDaGhiCto.setText(String.valueOf(cToDaGhi));
        tvDaGuiCto.setText(String.valueOf(cToDaGui));
    }

    private void setTitleThongKe(TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL) {
        int tongCTo = 0, cToDaGhi = 0, cToDaGui = 0;
        String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
        String spFilterChoose = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];

        if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.ALL) {
            tongCTo = connection.getCountCToByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaTramSelected(), NGAY_TRTH);
            cToDaGhi = connection.getCountCToDaGhiByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), NGAY_TRTH);
            cToDaGui = connection.getCountCToDaGuiByDviWithMaTram(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), spFilterChoose, NGAY_TRTH);
        }
       /* if (typeSetData == 2) {
            tongCTo = connection.getCountCToByDvi(TthtCommon.getMaDviqly(), spFilterChoose);
            cToDaGhi = connection.getCountCToDaGhiByDviFullMaTram(TthtCommon.getMaDviqly(), spFilterChoose, typeSetData, TthtCommon.getMaNvien(), NGAY_TRTH);
            cToDaGui = 0;
        }

        if (typeSetData == 3) {
            tongCTo = connection.getCountCToByDviFullMaTram(TthtCommon.getMaDviqly(), spFilterChoose, NGAY_TRTH);
            cToDaGhi = connection.getCountCToDaGhiByDviFullMaTram(TthtCommon.getMaDviqly(), spFilterChoose, typeSetData, TthtCommon.getMaNvien(), NGAY_TRTH);
            cToDaGui = connection.getCountCToDaGuiByDviFullMaTram(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien(), spFilterChoose, typeSetData, NGAY_TRTH);
        }

        if (typeSetData == 4) {
            tongCTo = connection.getCountCToByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaTramSelected(), NGAY_TRTH);
            cToDaGhi = connection.getCountCToDaGhiByDviWithMaTram(TthtCommon.getMaDviqly(), spFilterChoose, TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), NGAY_TRTH);
            cToDaGui = connection.getCountCToDaGuiByDviWithMaTram(TthtCommon.getMaDviqly(), TthtCommon.getMaNvien(), TthtCommon.getMaTramSelected(), spFilterChoose, NGAY_TRTH);
        }*/

        tvTongCto.setText(String.valueOf(tongCTo));
        tvDaGhiCto.setText(String.valueOf(cToDaGhi));
        tvDaGuiCto.setText(String.valueOf(cToDaGui));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        totalBTCDTD = 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        totalBTCDTD = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.close();
        totalBTCDTD = 0;
    }

    public void refreshMainFrag(String MA_BDONG) {
        spFilter.setSelection(Arrays.asList(TthtCommon.arrMaBDong).indexOf(MA_BDONG));
    }


    private void initComponent(View rootView) {
        //TODO init view
        etSearch = (EditText) rootView.findViewById(R.id.gsht_fragment_chamno_et_search);
        spFilter = (Spinner) rootView.findViewById(R.id.gsht_fragment_chamno_spFilter);
        ibClear = (ImageButton) rootView.findViewById(R.id.gsht_fragment_chamno_ib_clear);
        rvKH = (RecyclerView) rootView.findViewById(R.id.gsht_fragment_chamno_rv_kh);
        btNhanBBan = (Button) rootView.findViewById(R.id.ttht_fragment_main_btNhanBBan);
        btGuiBBan = (Button) rootView.findViewById(R.id.ttht_fragment_main_btGuiBBan);
        btDoiSanh = (Button) rootView.findViewById(R.id.ttht_fragment_main_btDoiSanh);
        tvTongCto = (TextView) rootView.findViewById(R.id.ttht_fragment_main_tongSo);
        tvDaGhiCto = (TextView) rootView.findViewById(R.id.ttht_fragment_main_daGhi);
        tvDaGuiCto = (TextView) rootView.findViewById(R.id.ttht_fragment_main_daGui);
        tvTenTram = (TextView) rootView.findViewById(R.id.ttht_fragment_main_tv_tenTram);
        auEtMaTram = (AutoCompleteTextView) rootView.findViewById(R.id.ttht_fragment_main_autotv_maTram);
        btNgayCapNhat = (Button) rootView.findViewById(R.id.ttht_fragment_main_bt_ngayChon);

        layoutManager = new LinearLayoutManager(this.getActivity());

        //TODO init action click
        ibClear.setOnClickListener(this);
        btNhanBBan.setOnClickListener(this);
        btGuiBBan.setOnClickListener(this);
        btDoiSanh.setOnClickListener(this);
        btNgayCapNhat.setOnClickListener(this);

        if (!isClickDoiSanh)
            btGuiBBan.setVisibility(View.GONE);
        else btGuiBBan.setVisibility(View.VISIBLE);

        //TODO set config recycler view
        rvKH.setHasFixedSize(true);
        rvKH.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO init action search and autoText MA_TRAM
        etSearch.post(new Runnable() {
                          @Override
                          public void run() {
                              etSearch.addTextChangedListener(new TextWatcher() {
                                                                  @Override
                                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                  }

                                                                  @Override
                                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                                      //TODO refresh data list
                                                                      try {

                                                                          List<InfoPhieuTreoThao> data = new ArrayList<InfoPhieuTreoThao>();
                                                                          String query = Common.removeAccent(s.toString().trim().toLowerCase());
                                                                          for (InfoPhieuTreoThao phieu : lstPhieuTreoThao) {
                                                                              if (Common.removeAccent(phieu.getMA_GCS_CTO().toLowerCase()).contains(query)
                                                                                      || Common.removeAccent(phieu.getSO_CTO().toLowerCase()).contains(query)) {
                                                                                  data.add(phieu);
                                                                              }
                                                                          }

                                                                          //TODO refresh
                                                                          String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
                                                                          adapterKH = (TthtInfoPhieuTreoThaoAdapter) rvKH.getAdapter();
                                                                          if (adapterKH != null)
                                                                              ((TthtInfoPhieuTreoThaoAdapter) adapterKH).updateList(data, MA_BDONG);

                                                                          /*
                                                                          List<TthtKHangEntity> dataTreo = new ArrayList<TthtKHangEntity>();
                                                                          List<TthtKHangEntity> dataThao = new ArrayList<TthtKHangEntity>();
                                                                          String query = Common.removeAccent(s.toString().trim().toLowerCase());
                                                                          for (TthtKHangEntity entityTreo : lstKhangHangTreo) {
                                                                              if (Common.removeAccent(entityTreo.getTthtBBanEntity().getMA_GCS_CTO().toLowerCase()).contains(query)
                                                                                      || Common.removeAccent(entityTreo.getTthtCtoEntity().getSO_CTO().toLowerCase()).contains(query)) {
                                                                                  dataTreo.add(entityTreo);
                                                                              }
                                                                          }
                                                                          for (TthtKHangEntity entityThao : lstKhangHangThao) {
                                                                              if (Common.removeAccent(entityThao.getTthtBBanEntity().getMA_GCS_CTO().toLowerCase()).contains(query)
                                                                                      || Common.removeAccent(entityThao.getTthtCtoEntity().getSO_CTO().toLowerCase()).contains(query)) {
                                                                                  dataThao.add(entityThao);
                                                                              }
                                                                          }

                                                                          //TODO refresh
                                                                          String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
                                                                          adapterKH.updateList(dataTreo, dataThao, MA_BDONG);*/
                                                                      } catch (Exception ex) {
                                                                          Toast.makeText(TthtMainFragment.this.getActivity(), "Lỗi tìm kiếm:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void afterTextChanged(Editable s) {
                                                                  }
                                                              }
                              );
                          }
                      }
        );

        if (!TthtCommon.getTthtDateChon().equals("")) {

            setDataAutoTextMA_TRAM();
        }

        //TODO init spin TreoThao
        adapterFilter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, TthtCommon.arrFilter);
        spFilter.setAdapter(adapterFilter);
        spFilter.setSelection(1);

        //TODO init variable common
        connection = TthtSQLiteConnection.getInstance(this.getActivity());
//        long result = connection.deleteALLAnh();
        asyncCallWSApi = TthtAsyncCallWSApi.getInstance();

    }


    private void setDataAutoTextMA_TRAM() {
        auEtMaTram.post(new Runnable() {
            @Override
            public void run() {
                auEtMaTram.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                        //TODO refresh data recyclerView
                        try {

                            List<InfoPhieuTreoThao> dataPhieu = new ArrayList<InfoPhieuTreoThao>();
//                            List<TthtKHangEntity> dataThao = new ArrayList<TthtKHangEntity>();
                            String query = Common.removeAccent(s.toString().trim().toLowerCase());
                            for (InfoPhieuTreoThao phieu : lstPhieuTreoThao) {
                                if (Common.removeAccent(phieu.getMA_TRAM().toLowerCase()).contains(query)) {
                                    dataPhieu.add(phieu);
                                }
                            }

                            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
                            lstPhieuTreoThao.clear();
                            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
                            invalidatReyclerView(MA_BDONG);
//                            setDataBBanWithMA_BDONG(MA_BDONG);
//                            adapterKH.updateList(dataTreo, dataThao, MA_BDONG);
                            setTitleThongKe(TthtCommon.FILTER_DATA_FILL.ALL);
                            if (s.length() == 0) tvTenTram.setText("");
                        } catch (Exception ex) {
                            Toast.makeText(TthtMainFragment.this.getActivity(), "Lỗi tìm kiếm:\n" + ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
    }

    private void fillDataAutoText(List<String> listUser, Context context, AutoCompleteTextView autoEtUser) {
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>
                (context, android.R.layout.select_dialog_item, listUser);
        autoEtUser.setThreshold(2);
        autoEtUser.setAdapter(adapterUser);
        autoEtUser.invalidate();
    }

//endregion

    //region Khởi tạo dữ liệu
  /*  class AsyncSetDataOnRecyclerView extends AsyncTask<Void, String, Void> {
        private TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL;

        public AsyncSetDataOnRecyclerView(TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL) {
            this.FILTER_DATA_FILL = FILTER_DATA_FILL;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            try {
            int stt = 0;
            Cursor cKHangTreoThao = null;
            Cursor cKHangThao = null;
            Cursor cusorGetAnhCongTo = null;
            Cursor cusorGetAnhTuTi = null;
            Cursor cusorGetAnhMachNhiThu = null;
            Cursor cusorGetAnhMachCongTo = null;
            Cursor cBBanTuTi = null;
            String ngayConvert = "";
            if (!TthtCommon.getTthtDateChon().equals("")) {
                ngayConvert = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
            }

            if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.TREO) {
                cKHangTreoThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
            } else {
                cKHangTreoThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
            }

//            if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.ALL) {
//                cKHangTreoThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
//                cKHangThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
//            }

            lstKhangHangTreo.clear();
            //lấy chi tiết

            try {
                if (cKHangTreoThao != null) {
                    do {
                        stt++;
                        //TODO get data TthtBBanEntity
                        TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
                        tthtBBanEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")));
                        tthtBBanEntity.setID_BBAN_TRTH(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")));
                        tthtBBanEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CNANG")));
                        tthtBBanEntity.setDATE_CALL_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_DDO")));
                        tthtBBanEntity.setMA_DVIQLY(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NVIEN")));
                        tthtBBanEntity.setSO_BBAN_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_LDO")));
                        tthtBBanEntity.setMA_NVIEN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("BUNDLE_MA_NVIEN")));
                        tthtBBanEntity.setSO_TRAM_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_YCAU_KNAI")));
                        tthtBBanEntity.setSO_BBAN_TUTI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_SUA")));
                        tthtBBanEntity.setSO_CTO_THAO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TAO")));
                        tthtBBanEntity.setTYPE_RESULT(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TRTH")));
                        tthtBBanEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_SUA")));
                        tthtBBanEntity.setSO_CTO_TREO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_TAO")));
                        tthtBBanEntity.setTYPE_CALL_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_BBAN")));
                        tthtBBanEntity.setSO_CHUNGLOAI_API(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("TRANG_THAI")));
                        tthtBBanEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")));
                        tthtBBanEntity.setId_BBAN_CONGTO(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_CONGTO")));
                        tthtBBanEntity.setLOAI_BBAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LOAI_BBAN")));
                        tthtBBanEntity.setTEN_KHANG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_KHANG")));
                        tthtBBanEntity.setDCHI_HDON(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DCHI_HDON")));
                        tthtBBanEntity.setDTHOAI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DTHOAI")));
                        tthtBBanEntity.setMA_GCS_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_GCS_CTO")));
                        tthtBBanEntity.setMA_TRAM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_TRAM")));
                        tthtBBanEntity.setMA_HDONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_HDONG")));

                        //TODO get data TthtCtoEntity
                        TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
                        tthtCtoEntity.setMA_DVIQLY(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NVIEN")));
                        tthtCtoEntity.setID_BBAN_TRTH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")) : 0);
                        tthtCtoEntity.setMA_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CTO")));
                        tthtCtoEntity.setSO_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_CTO")));
                        tthtCtoEntity.setLAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LAN")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LAN")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("LAN")) : 0);
                        String MA_BDONG_CTO = cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_BDONG"));
                        tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
                        tthtCtoEntity.setNGAY_BDONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_BDONG")));
                        tthtCtoEntity.setMA_CLOAI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CLOAI")));
                        tthtCtoEntity.setLOAI_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LOAI_CTO")));
                        tthtCtoEntity.setVTRI_TREO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("VTRI_TREO")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("VTRI_TREO")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("VTRI_TREO")) : 0);
                        tthtCtoEntity.setMA_SOCBOOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_SOCBOOC")));
                        tthtCtoEntity.setSOVIEN_CBOOC(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SO_VIENCBOOC")));
                        tthtCtoEntity.setLOAI_HOM(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("LOAI_HOM")));
                        tthtCtoEntity.setMA_SOCHOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_SOCHOM")));
                        tthtCtoEntity.setSO_VIENCHOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")) : 0);
                        tthtCtoEntity.setHS_NHAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HS_NHAN")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HS_NHAN")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("HS_NHAN")) : 1);
                        tthtCtoEntity.setSO_CTO_THAO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TAO")));
                        tthtCtoEntity.setSO_CTO_TREO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_TAO")));
                        tthtCtoEntity.setSO_BBAN_TUTI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_SUA")));
                        tthtCtoEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_SUA")));
                        tthtCtoEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CNANG")));
                        tthtCtoEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TU")));
                        tthtCtoEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TI")));
                        tthtCtoEntity.setSO_COT(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_COT")));
                        tthtCtoEntity.setSO_HOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_HOM")));
                        tthtCtoEntity.setCHI_SO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("CHI_SO")));
                        tthtCtoEntity.setNGAY_KDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_KDINH")));
                        tthtCtoEntity.setNAM_SX(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NAM_SX")));
                        tthtCtoEntity.setTEM_CQUANG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEM_CQUANG")));
                        tthtCtoEntity.setMA_CHIKDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CHIKDINH")));
                        tthtCtoEntity.setMA_TEM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_TEM")));
                        tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")) != null
                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
                        tthtCtoEntity.setDIEN_AP(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DIEN_AP")));
                        tthtCtoEntity.setDONG_DIEN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DONG_DIEN")));
                        tthtCtoEntity.setHANGSO_K(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HANGSO_K")));
                        tthtCtoEntity.setMA_NUOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NUOC")));
                        tthtCtoEntity.setTEN_NUOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_NUOC")));
                        tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_KIM_NIEM_CHI")));
                        tthtCtoEntity.setTTRANG_NPHONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TTRANG_NPHONG")));

                        int ID_CHITIET_CTO = cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_CHITIET_CTO"));
                        //TODO lấy tên ảnh
                        cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                        if (cusorGetAnhCongTo.moveToFirst()) {
                            String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
                            tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
                            cusorGetAnhCongTo.close();
                        }
                        tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
                        tthtCtoEntity.setTEN_LOAI_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_LOAI_CTO")).trim());

                        tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
                        tthtCtoEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")).trim());
                        tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("TRANG_THAI_DU_LIEU")));
                        int ID_BBAN_TUTI = cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TUTI"));

                        tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                        tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
                        tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TU_SAULAP_TUTI")));
                        tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TI_SAULAP_TUTI")));
                        tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("CHI_SO_SAULAP_TUTI")));
                        tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
                        tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
                        tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
                        tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("CAP_CX_SAULAP_TUTI")));


                        //TODO get data TU TI
                        if (tthtBBanEntity.getID_BBAN_TRTH() == 1742727)
                            Log.d(TAG, "doInBackground: ");
                        TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
                        ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;

                        cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                        try {
                            if (cBBanTuTi.moveToFirst()) {
                                tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
                                tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
                                ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
                                tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                                tthtBBanTuTiEntity.setDATE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
                                tthtBBanTuTiEntity.setTYPE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
                                tthtBBanTuTiEntity.setTYPE_RESULT(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
                                tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
                                tthtBBanTuTiEntity.setSO_CHUNGLOAI_API(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
                                tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
                                tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
                                tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
                                tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
                                tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
                                tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
                                tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
                                tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
                                tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));

                                Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                                try {
                                    if (cTuTi != null) {
                                        tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
                                        do {
                                            TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();
                                            //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
                                            if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                                                String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
                                                //TODO MA_BDONG trong bảng CHI_TIET_TUTI
                                                String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));

                                                //TODO lấy tên ảnh
                                                cusorGetAnhTuTi = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI));
                                                if (cusorGetAnhTuTi.moveToFirst()) {
                                                    String TEN_ANH = cusorGetAnhTuTi.getString(cusorGetAnhTuTi.getColumnIndex("TEN_ANH"));
                                                    tthtTuTiEntity.setTEN_ANH_TU_TI(TEN_ANH);
                                                    cusorGetAnhTuTi.close();
                                                }

                                                cusorGetAnhMachNhiThu = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
                                                if (cusorGetAnhMachNhiThu.moveToFirst()) {
                                                    String TEN_ANH = cusorGetAnhMachNhiThu.getString(cusorGetAnhMachNhiThu.getColumnIndex("TEN_ANH"));
                                                    tthtTuTiEntity.setTEN_ANH_MACH_NHI_THU(TEN_ANH);
                                                    cusorGetAnhMachNhiThu.close();
                                                }

                                                cusorGetAnhMachCongTo = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
                                                if (cusorGetAnhMachCongTo.moveToFirst()) {
                                                    String TEN_ANH = cusorGetAnhMachCongTo.getString(cusorGetAnhMachCongTo.getColumnIndex("TEN_ANH"));
                                                    tthtTuTiEntity.setTEN_ANH_MACH_CONG_TO(TEN_ANH);
                                                    cusorGetAnhMachCongTo.close();
                                                }

                                                tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
                                                tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
                                                tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
                                                tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
                                                tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
                                                tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
                                                tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
                                                tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
                                                tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
                                                tthtTuTiEntity.setSO_CHUNGLOAI_API(cTuTi.getInt(cTuTi.getColumnIndex("gNG_THAI")));
                                                tthtTuTiEntity.setIS_TU(IS_TU);
                                                tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
                                                tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
                                                tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
                                                tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
                                                tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
                                                tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
                                                tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
                                                tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
                                                tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
                                                tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
                                                tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
                                                tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
                                                tthtTuTiEntityList.add(tthtTuTiEntity);
                                            }
                                        }
                                        while (cTuTi.moveToNext());
                                    }
                                } finally {
                                    if (cTuTi != null)
                                        cTuTi.close();
                                }
                            }
                        } finally {
                            if (cBBanTuTi != null)
                                cBBanTuTi.close();
                        }

                        TthtKHangEntity tthtKHangEntityTreo = new TthtKHangEntity();
                        tthtKHangEntityTreo.setStt(stt);
                        tthtKHangEntityTreo.setTthtBBanEntity(tthtBBanEntity);
                        tthtKHangEntityTreo.setTthtCtoEntity(tthtCtoEntity);
                        if (tthtBBanTuTiEntity != null) {
                            tthtKHangEntityTreo.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
                        }
                        if (tthtTuTiEntityList != null) {
                            tthtKHangEntityTreo.setTthtTuTiEntity(tthtTuTiEntityList);
                        }

                        lstKhangHangTreo.add(tthtKHangEntityTreo);
                    } while (cKHangTreoThao.moveToNext());
                }
            } finally {
                if (cKHangTreoThao != null) {
                    cKHangTreoThao.close();
                }
            }
            //TODO sort by MaGCS
            Collections.sort(lstKhangHangTreo, new KHCompairByMaGCS());
            for (int i = 0; i < lstKhangHangTreo.size(); i++) {
                lstKhangHangTreo.get(i).setStt(i + 1);
            }

            lstKhangHangThao.clear();
            try {
                if (cKHangThao != null) {
                    if (cKHangThao.moveToFirst()) {
                        do {
                            stt++;
                            //TODO get data TthtBBanEntity
                            TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setID_BBAN_TRTH(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")));
                            tthtBBanEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
                            tthtBBanEntity.setDATE_CALL_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_DDO")));
                            tthtBBanEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
                            tthtBBanEntity.setSO_BBAN_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_LDO")));
                            tthtBBanEntity.setMA_NVIEN(cKHangThao.getString(cKHangThao.getColumnIndex("BUNDLE_MA_NVIEN")));
                            tthtBBanEntity.setSO_TRAM_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_YCAU_KNAI")));
                            tthtBBanEntity.setSO_BBAN_TUTI_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
                            tthtBBanEntity.setSO_CTO_THAO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
                            tthtBBanEntity.setTYPE_RESULT(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TRTH")));
                            tthtBBanEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
                            tthtBBanEntity.setSO_CTO_TREO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
                            tthtBBanEntity.setTYPE_CALL_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_BBAN")));
                            tthtBBanEntity.setSO_CHUNGLOAI_API(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI")));
                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setId_BBAN_CONGTO(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_CONGTO")));
                            tthtBBanEntity.setLOAI_BBAN(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_BBAN")));
                            tthtBBanEntity.setTEN_KHANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_KHANG")));
                            tthtBBanEntity.setDCHI_HDON(cKHangThao.getString(cKHangThao.getColumnIndex("DCHI_HDON")));
                            tthtBBanEntity.setDTHOAI(cKHangThao.getString(cKHangThao.getColumnIndex("DTHOAI")));
                            tthtBBanEntity.setMA_GCS_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_GCS_CTO")));
                            tthtBBanEntity.setMA_TRAM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TRAM")));
                            tthtBBanEntity.setMA_HDONG(cKHangThao.getString(cKHangThao.getColumnIndex("MA_HDONG")));

                            //TODO get data TthtCtoEntity
                            TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
                            tthtCtoEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
                            tthtCtoEntity.setID_BBAN_TRTH(cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) : 0);
                            tthtCtoEntity.setMA_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CTO")));
                            tthtCtoEntity.setSO_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("SO_CTO")));
                            tthtCtoEntity.setLAN(cKHangThao.getString(cKHangThao.getColumnIndex("LAN")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("LAN")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("LAN")) : 0);
                            String MA_BDONG_CTO = cKHangThao.getString(cKHangThao.getColumnIndex("MA_BDONG"));
                            tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
                            tthtCtoEntity.setNGAY_BDONG(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_BDONG")));
                            tthtCtoEntity.setMA_CLOAI(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CLOAI")));
                            tthtCtoEntity.setLOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_CTO")));
                            tthtCtoEntity.setVTRI_TREO(cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("VTRI_TREO")) : 0);
                            tthtCtoEntity.setMA_SOCBOOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCBOOC")));
                            tthtCtoEntity.setSOVIEN_CBOOC(cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCBOOC")));
                            tthtCtoEntity.setLOAI_HOM(cKHangThao.getInt(cKHangThao.getColumnIndex("LOAI_HOM")));
                            tthtCtoEntity.setMA_SOCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCHOM")));
                            tthtCtoEntity.setSO_VIENCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCHOM")) : 0);
                            tthtCtoEntity.setHS_NHAN(cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN")) : 1);
                            tthtCtoEntity.setSO_CTO_THAO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
                            tthtCtoEntity.setSO_CTO_TREO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
                            tthtCtoEntity.setSO_BBAN_TUTI_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
                            tthtCtoEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
                            tthtCtoEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
                            tthtCtoEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU")));
                            tthtCtoEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI")));
                            tthtCtoEntity.setSO_COT(cKHangThao.getString(cKHangThao.getColumnIndex("SO_COT")));
                            tthtCtoEntity.setSO_HOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_HOM")));
                            tthtCtoEntity.setCHI_SO(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO")));
                            tthtCtoEntity.setNGAY_KDINH(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_KDINH")));
                            tthtCtoEntity.setNAM_SX(cKHangThao.getString(cKHangThao.getColumnIndex("NAM_SX")));
                            tthtCtoEntity.setTEM_CQUANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEM_CQUANG")));
                            tthtCtoEntity.setMA_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CHIKDINH")));
                            tthtCtoEntity.setMA_TEM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TEM")));
                            tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
                            tthtCtoEntity.setDIEN_AP(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP")));
                            tthtCtoEntity.setDONG_DIEN(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN")));
                            tthtCtoEntity.setHANGSO_K(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K")));
                            tthtCtoEntity.setMA_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NUOC")));
                            tthtCtoEntity.setTEN_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_NUOC")));
                            tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_KIM_NIEM_CHI")));
                            tthtCtoEntity.setTTRANG_NPHONG(cKHangThao.getString(cKHangThao.getColumnIndex("TTRANG_NPHONG")));

                            int ID_CHITIET_CTO = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_CHITIET_CTO"));
                            cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                            try {
                                if (cusorGetAnhCongTo.moveToFirst()) {
                                    String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
                                    tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
                                }
                            } finally {
                                if (cusorGetAnhCongTo != null) cusorGetAnhCongTo.close();
                            }

                            tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
                            tthtCtoEntity.setTEN_LOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_LOAI_CTO")).trim());

                            tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangThao.getString(cKHangThao.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
                            tthtCtoEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")).trim());
                            int ID_BBAN_TUTI = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TUTI"));
                            tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                            tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI_DU_LIEU")));

                            tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI_SAULAP_TUTI")));
                            tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO_SAULAP_TUTI")));
                            tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
                            tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
                            tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
                            tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("CAP_CX_SAULAP_TUTI")));
                            //TODO get data TU TI
                            TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
                            ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;
                            cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                            try {
                                if (cBBanTuTi.moveToFirst()) {
                                    tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
                                    tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
                                    ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
                                    tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                                    tthtBBanTuTiEntity.setDATE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
                                    tthtBBanTuTiEntity.setTYPE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
                                    tthtBBanTuTiEntity.setTYPE_RESULT(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
                                    tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
                                    tthtBBanTuTiEntity.setSO_CHUNGLOAI_API(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
                                    tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
                                    tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
                                    tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
                                    tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
                                    tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
                                    tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
                                    tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
                                    tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
                                    tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));
                                    cBBanTuTi.close();
                                    Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                                    try {
                                        if (cTuTi != null) {
                                            tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
                                            do {
                                                TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();

                                                //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
                                                if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
                                                    String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
                                                    //TODO MA_BDONG trong bảng CHI_TIET_TUTI
                                                    String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));
                                                    tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
                                                    tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
                                                    tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
                                                    tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
                                                    tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
                                                    tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
                                                    tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
                                                    tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
                                                    tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
                                                    tthtTuTiEntity.setSO_CHUNGLOAI_API(cTuTi.getInt(cTuTi.getColumnIndex("TRANG_THAI")));
                                                    tthtTuTiEntity.setIS_TU(IS_TU);
                                                    tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
                                                    tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
                                                    tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
                                                    tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
                                                    tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
                                                    tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
                                                    tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
                                                    tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
                                                    tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
                                                    tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
                                                    tthtTuTiEntityList.add(tthtTuTiEntity);
                                                }
                                            }
                                            while (cTuTi.moveToNext());
                                        }
                                    } finally {
                                        if (cTuTi != null) cTuTi.close();
                                    }
                                }
                            } finally {
                                if (cBBanTuTi != null) cBBanTuTi.close();
                            }


                            TthtKHangEntity tthtKHangEntityThao = new TthtKHangEntity();
                            tthtKHangEntityThao.setStt(stt);
                            tthtKHangEntityThao.setTthtBBanEntity(tthtBBanEntity);
                            tthtKHangEntityThao.setTthtCtoEntity(tthtCtoEntity);
                            if (tthtBBanTuTiEntity != null) {
                                tthtKHangEntityThao.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
                            }
                            if (tthtTuTiEntityList != null) {
                                tthtKHangEntityThao.setTthtTuTiEntity(tthtTuTiEntityList);
                            }

                            lstKhangHangThao.add(tthtKHangEntityThao);
                        } while (cKHangThao.moveToNext());

                    }
                }

            } finally {
                if (cKHangThao != null) {
                    cKHangThao.close();
                }
            }

            //TODO sort by MaGCS
            Collections.sort(lstKhangHangThao, new KHCompairByMaGCS());

            for (int i = 0; i < lstKhangHangThao.size(); i++) {
                lstKhangHangThao.get(i).setStt(i + 1);
            }
            Log.d(TAG, "doInBackground: ");

//            } catch (Exception ex) {
//                publishProgress(ex.getMessage());
//            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu\n" + values[0], Color.WHITE, "OK", Color.RED);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                //TODO set Adapter
                adapterKH = new TthtKHAdapter(lstKhangHangTreo, lstKhangHangThao, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                rvKH.setAdapter(adapterKH);
                rvKH.setHasFixedSize(true);
                rvKH.invalidate();

                //TODO fill text
                if (!TthtCommon.getTenTramSelected().equals("")) {
                    auEtMaTram.setText(TthtCommon.getMaTramSelected());
                    tvTenTram.setText(TthtCommon.getTenTramSelected());
                }

                if (idBBTrTh != 0) {
                    //TODO set position recyclerview and refresh data when idbBBTrTh change
                    int posSelect = adapterKH.getPosIDBBanTrThao(idBBTrTh);
                    adapterKH.setPosSelect(posSelect);
                    adapterKH.notifyDataSetChanged();
                    rvKH.invalidate();

                   *//* //TODO get loaiCto
                    String LOAI_CTO = "";
                    String CHI_SO = "";
                    Cursor c4 = .getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                    if (c4 != null) {
                        LOAI_CTO = c4.getString(c4.getColumnIndex("LOAI_CTO"));
                        CHI_SO = c4.getString(c4.getColumnIndex("CHI_SO"));
                        c4.close();
                    } else {
                        throw new Exception("Lỗi không tìm thấy có LOAI_CTO gán với idBBTrTh");

                    }*//*

                    //TODO set Data and set Image
//                    setDataOnEditText(LOAI_CTO, CHI_SO);
//                    setImage();

//                    mLLKimNiemChi.setVisibility(View.VISIBLE);
                    //TODO set background button
                    *//*if (adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_BDONG().equals("B")) {
                        mLLKimNiemChi.setVisibility(View.GONE);
                        btSwitchTreo.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.button_press_light));
                        btSwitchThao.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tabTitleUnselect));
                    } else {
                        mLLKimNiemChi.setVisibility(View.VISIBLE);
                        btSwitchTreo.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.tabTitleUnselect));
                        btSwitchThao.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.button_press_light));
                    }*//*
                }
            } catch (Exception ex) {
                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED,
                        "Lỗi fill giá trị lên recyclerView\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
            }
        }

    }
*/
//    private void setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL typeSetData) {
//        new AsyncSetDataOnRecyclerView(typeSetData).execute();
//    }

//    private void setDataDSOnRecyclerView() {
//        new AsyncSetDataDSOnRecyclerView().execute();
//    }
    //endregion

    //region Khởi tạo đối sánh
  /*  class AsyncSetDataOnRecyclerViewDoiSanh extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                lstDoiSanh = new ArrayList<>();
                int stt = 0;
                //Get Treo
                String ngayConvert = "";
                if (!TthtCommon.getTthtDateChon().equals("")) {
                    ngayConvert = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
                }
                Cursor cDoiSanhTreo = .getDataKhangFullMA_TRAM("B", TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, 2);
                Cursor cursorAnh = null;
                if (cDoiSanhTreo != null) {
                    do {
                        TthtDoiSoatEntity doiSoatEntity = new TthtDoiSoatEntity();

                        doiSoatEntity.setStt(stt);
                        doiSoatEntity.setID_BBAN_TRTH(cDoiSanhTreo.getInt(cDoiSanhTreo.getColumnIndex("ID_BBAN_TRTH")));
                        doiSoatEntity.setMA_GCS_CTO(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("MA_GCS_CTO")));
                        doiSoatEntity.setTEN_KHANG(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("TEN_KHANG")));
                        doiSoatEntity.setDCHI_HDON(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("DCHI_HDON")));
                        doiSoatEntity.setID_BBAN_CONGTO(cDoiSanhTreo.getInt(cDoiSanhTreo.getColumnIndex("ID_BBAN_CONGTO")));

                        //cto treo
                        int ID_CHITIET_CTO_TREO = cDoiSanhTreo.getInt(cDoiSanhTreo.getColumnIndex("ID_CHITIET_CTO"));
                        doiSoatEntity.setID_CHITIET_CTO_TREO(ID_CHITIET_CTO_TREO);
                        doiSoatEntity.setMA_CTO_TREO(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("MA_CTO")));
                        doiSoatEntity.setSO_CTO_TREO_API(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("SO_CTO")));
                        doiSoatEntity.setCHI_SO_TREO(cDoiSanhTreo.getString(cDoiSanhTreo.getColumnIndex("CHI_SO")));

                        cursorAnh = .getDataAnhByIDCto(ID_CHITIET_CTO_TREO);
                        String pathAnhTreo = "";
                        if (cursorAnh != null) {
                            pathAnhTreo = Util.getSnapshotFileName(cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH")));
                        }
                        doiSoatEntity.setANH_PATH_TREO(pathAnhTreo);
                        lstDoiSanh.add(doiSoatEntity);
                        stt++;
                    } while (cDoiSanhTreo.moveToNext());
                }

                //Get Thao
                Cursor cDoiSanhThao = .getDataKhangFullMA_TRAM("E", TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, 2);
                stt = 0;
                if (cDoiSanhThao != null) {
                    do {
                        if (lstDoiSanh.get(stt).getID_BBAN_TRTH() == cDoiSanhThao.getInt(cDoiSanhThao.getColumnIndex("ID_BBAN_TRTH"))) {
                            lstDoiSanh.get(stt).setStt(stt);
                            lstDoiSanh.get(stt).setID_BBAN_TRTH(cDoiSanhThao.getInt(cDoiSanhThao.getColumnIndex("ID_BBAN_TRTH")));
                            lstDoiSanh.get(stt).setMA_GCS_CTO(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("MA_GCS_CTO")));
                            lstDoiSanh.get(stt).setTEN_KHANG(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("TEN_KHANG")));
                            lstDoiSanh.get(stt).setDCHI_HDON(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("DCHI_HDON")));

                            //cto THAO
                            int ID_CHITIET_CTO_THAO = cDoiSanhThao.getInt(cDoiSanhThao.getColumnIndex("ID_CHITIET_CTO"));
                            lstDoiSanh.get(stt).setID_CHITIET_CTO_THAO(ID_CHITIET_CTO_THAO);
                            lstDoiSanh.get(stt).setMA_CTO_THAO(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("MA_CTO")));
                            lstDoiSanh.get(stt).setSO_CTO_THAO_API(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("SO_CTO")));
                            lstDoiSanh.get(stt).setCHI_SO_THAO(cDoiSanhThao.getString(cDoiSanhThao.getColumnIndex("CHI_SO")));

                            cursorAnh = .getDataAnhByIDCto(ID_CHITIET_CTO_THAO);
                            String pathAnhThao = "";
                            if (cursorAnh != null) {
                                pathAnhThao = Util.getSnapshotFileName(cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH")));
                            }
                            lstDoiSanh.get(stt).setANH_PATH_THAO(pathAnhThao);
                        }
                        stt++;
                    } while (cDoiSanhThao.moveToNext());
                }

                //sort by MaGCS
                Collections.sort(lstDoiSanh, new KHCompairByMaGCS());
                for (int i = 0; i < lstDoiSanh.size(); i++) {
                    lstDoiSanh.get(i).setStt(i + 1);
                }
                adapterDoiSoat = new TthtDoiSoatAdapter(lstDoiSanh);

                isClickChooseArrayDoiSoat = new boolean[lstDoiSanh.size()];
            } catch (Exception ex) {
                publishProgress(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu\n" + values[0], Color.WHITE, "OK", Color.RED);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rvKH.setAdapter(adapterDoiSoat);
            layoutManager = new LinearLayoutManager(getActivity());
            rvKH.setHasFixedSize(true);
            rvKH.setLayoutManager(layoutManager);
            rvKH.invalidate();

        }
    }
*/
    /*private void setDataOnRecycleViewDoiSanh() {
        new AsyncSetDataOnRecyclerViewDoiSanh().execute();
    }*/
    //endregion

    //region Xử lý sự kiện
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gsht_fragment_chamno_ib_clear:
                etSearch.setText("");
                break;
            case R.id.ttht_fragment_main_btNhanBBan:
                NhanBBan();
                break;
            case R.id.ttht_fragment_main_btGuiBBan:
                GuiBBan();
                break;
            case R.id.ttht_fragment_main_btDoiSanh:
                DoiSoat();
                break;
            case R.id.ttht_fragment_main_bt_ngayChon:
                ChonNgay();
                break;
        }
    }

    private void ChonNgay() {
        try {
            showActivityCapNhatBBan();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    "Lỗi đồng bộ biên bản", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void DoiSoat() {
        if (!isClickDoiSanh) {
            btNhanBBan.setVisibility(View.GONE);
            btGuiBBan.setVisibility(View.VISIBLE);
            btDoiSanh.setText(TthtCommon.ARRAY_DOI_SOAT[1]);
            //set mac dinh adapterKH.listdata la cua ben treo de khi doi sanh se quy het ve treo
            setDataDSOnRecyclerView();
            isClickDoiSanh = true;

        } else {
            btNhanBBan.setVisibility(View.VISIBLE);
            btGuiBBan.setVisibility(View.GONE);
            btDoiSanh.setText(TthtCommon.ARRAY_DOI_SOAT[0]);
            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
            lstPhieuTreoThao.clear();
            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
            invalidatReyclerView(MA_BDONG);
            //set mac dinh lai nhu ban dau
            setTitleThongKeDate();
            isClickDoiSanh = false;
        }
    }

//    public void setDataOnEditText(String LOAI_CTO, String CS) {
//        try {
//            int posSelect = adapterKH.getPosSelect();
//            String tinhTrangNiemPhong = adapterKH.listData.get(posSelect).getTthtCtoEntity().getTTRANG_NPHONG();
//            mMaKimChiNiem = String.valueOf(adapterKH.listData.get(posSelect).getTthtCtoEntity().getSO_KIM_NIEM_CHI());
//            String maChiBooc = adapterKH.listData.get(posSelect).getTthtCtoEntity().getMA_SOCHOM();
//            String maChiHop = adapterKH.listData.get(posSelect).getTthtCtoEntity().getMA_SOCBOOC();
//            String tenLoaiCto = adapterKH.listData.get(posSelect).getTthtCtoEntity().getTEN_LOAI_CTO();
//            String ghichu = adapterKH.listData.get(posSelect).getTthtBBanEntity().getGHI_CHU();
//            String maBienDong = adapterKH.listData.get(posSelect).getTthtCtoEntity().getMA_BDONG();
//            String maGCS = adapterKH.listData.get(posSelect).getTthtBBanEntity().getMA_GCS_CTO();
//            String soNo = adapterKH.listData.get(posSelect).getTthtCtoEntity().getSO_CTO();
//            String maCongTo = adapterKH.listData.get(posSelect).getTthtCtoEntity().getMA_CTO();
//            String tenKH = adapterKH.listData.get(posSelect).getTthtBBanEntity().getTEN_KHANG();
//            TEN_KH.setText(tenKH);
//            DIA_CHI_HOADON.setText(adapterKH.listData.get(posSelect).getTthtBBanEntity().getDCHI_HDON());
//            tvMaGCS.setText(maGCS);
//            tvSoNo.setText(soNo);
//            int soVienChiKDinh = 0;
//            int soVienChiHom = 0;
//            int soVienChiHop = 0;
//
//
//            Cursor cursor = .getCToByMaCto(maCongTo);
//            String maChiKDinh = cursor.getString(cursor.getColumnIndex("MA_CHIKDINH"));
//            String phuongThucDoXa = cursor.getString(cursor.getColumnIndex("PHUONG_THUC_DO_XA"));
//            int heSoNhan = cursor.getInt(cursor.getColumnIndex("HS_NHAN"));
//            etHeSoNhan.setText(String.valueOf(heSoNhan));
//            try {
//                soVienChiKDinh = cursor.getInt(cursor.getColumnIndex("SOVIEN_CHIKDINH"));
//                soVienChiHom = cursor.getInt(cursor.getColumnIndex("SO_VIENCHOM"));
//                soVienChiHop = cursor.getInt(cursor.getColumnIndex("SO_VIENCBOOC"));
//            } catch (Exception e) {
//                soVienChiKDinh = 0;
//                soVienChiHom = 0;
//                soVienChiHop = 0;
//            }
//
//            cursor = .getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
//            CS = cursor.getString(cursor.getColumnIndex("CHI_SO"));
//            etKimNiemChi.setText("");
//            if (!mMaKimChiNiem.equals("null")) {
//                etKimNiemChi.setText(mMaKimChiNiem);
//            }
//            etTinhTrangNiemPhong.setText("");
//            if (!tinhTrangNiemPhong.equals("null")) {
//                etTinhTrangNiemPhong.setText(tinhTrangNiemPhong);
//            }
//            etMaChiKDinh.setText(maChiKDinh);
//            etMaChiBooc.setText(maChiBooc);
//            etMaChiHop.setText(maChiHop);
//            etGhiChu.setText(ghichu);
//
//
//            if (tenLoaiCto.equals("")) {
//                etTenLoaiCTo.setText("");
//                etNhaCungCap.setText("");
//            } else {
//                etTenLoaiCTo.setText(tenLoaiCto);
//                if (maBienDong.equals("B")) {
//                    etKimNiemChi.setText(mMaKimChiNiem);
//                    String nhaCungCap = .getLoaiCongTo(tenLoaiCto).getMA_HANG();
//                    etNhaCungCap.setText(nhaCungCap);
//                    List<String> phuongThucDoXaList = new ArrayList<String>();
//
//                    if (!phuongThucDoXa.equals("")) {
//                        String[] itemPhuongThucDoXa = phuongThucDoXa.trim().split(",");
//                        phuongThucDoXaList.addAll(Arrays.asList(itemPhuongThucDoXa));
//                    }
//                    if (!phuongThucDoXa.contains("Không có")) {
//                        phuongThucDoXaList.add("Không có");
//                    }
//                    ArrayAdapter<String> adapterPhuongThucDoXa = new ArrayAdapter<String>(TthtMainFragment.this.getActivity(), R.layout.view_spinner_item, phuongThucDoXaList);
//                    adapterPhuongThucDoXa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    mSpinPhuongThucDoXa.setAdapter(adapterPhuongThucDoXa);
//                    mSpinPhuongThucDoXa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            sPhuongThucDoXa = mSpinPhuongThucDoXa.getSelectedItem().toString();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
//                } else {
//                    mLLThongTinCongTo.setVisibility(View.GONE);
//                }
//            }
//
//
//            try {
//                spSoVienChiKDinh.setSelection(soVienChiKDinh);
//                spSoVienChiHom.setSelection(soVienChiHom);
//                spSoVienChiHop.setSelection(soVienChiHop);
//            } catch (Exception ex) {
//                spSoVienChiKDinh.setSelection(0);
//                spSoVienChiHom.setSelection(0);
//                spSoVienChiHop.setSelection(0);
//            }
//
//            etCS1.setText("");
//            etCS2.setText("");
//            etCS3.setText("");
//            etCS4.setText("");
//            etCS5.setText("");
//
//            if (LOAI_CTO.equals("HC")) {
//                etCS1.setEnabled(true);
//                etCS2.setEnabled(false);
//                etCS3.setEnabled(false);
//                etCS4.setEnabled(false);
//                etCS5.setEnabled(false);
//                etCS1.setVisibility(View.VISIBLE);
//                etCS2.setVisibility(View.INVISIBLE);
//                etCS3.setVisibility(View.INVISIBLE);
//                etCS4.setVisibility(View.INVISIBLE);
//                etCS5.setVisibility(View.INVISIBLE);
//                mLLDoSaiLech.setVisibility(View.GONE);
//                titleEtCS1.setVisibility(View.VISIBLE);
//                titleEtCS2.setVisibility(View.INVISIBLE);
//                titleEtCS3.setVisibility(View.INVISIBLE);
//                titleEtCS4.setVisibility(View.INVISIBLE);
//                titleEtCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setText("KT");
//                titleEtCS2.setText("");
//                titleEtCS3.setText("");
//                titleEtCS4.setText("");
//                titleEtCS5.setText("");
//
//                etCS1.setHint("KT");
//                etCS2.setHint("");
//                etCS3.setHint("");
//                etCS4.setHint("");
//                etCS5.setHint("");
//                etCS1.setText(CS.contains(":") ? CS.split(":")[1] : CS);
//                btCameraMTB.setEnabled(true);
//            } else if (LOAI_CTO.equals("VC")) {
//                etCS1.setEnabled(true);
//                etCS2.setEnabled(false);
//                etCS3.setEnabled(false);
//                etCS4.setEnabled(false);
//                etCS5.setEnabled(false);
//
//                etCS1.setVisibility(View.VISIBLE);
//                etCS2.setVisibility(View.INVISIBLE);
//                etCS3.setVisibility(View.INVISIBLE);
//                etCS4.setVisibility(View.INVISIBLE);
//                etCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setVisibility(View.VISIBLE);
//                titleEtCS2.setVisibility(View.INVISIBLE);
//                titleEtCS3.setVisibility(View.INVISIBLE);
//                titleEtCS4.setVisibility(View.INVISIBLE);
//                titleEtCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setText("VC");
//                titleEtCS2.setText("");
//                titleEtCS3.setText("");
//                titleEtCS4.setText("");
//                titleEtCS5.setText("");
//
//                mLLDoSaiLech.setVisibility(View.GONE);
//                etCS1.setHint("VC");
//                etCS2.setHint("");
//                etCS3.setHint("");
//                etCS4.setHint("");
//                etCS5.setHint("");
//                etCS1.setText(CS.contains(":") ? CS.split(":")[1] : CS);
//                btCameraMTB.setEnabled(true);
//            } else if (LOAI_CTO.equals("D1")) {
//                etCS1.setEnabled(true);
//                etCS2.setEnabled(false);
//                etCS3.setEnabled(false);
//                etCS4.setEnabled(false);
//                etCS5.setEnabled(false);
//
//                etCS1.setVisibility(View.VISIBLE);
//                etCS2.setVisibility(View.INVISIBLE);
//                etCS3.setVisibility(View.INVISIBLE);
//                etCS4.setVisibility(View.INVISIBLE);
//                etCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setVisibility(View.VISIBLE);
//                titleEtCS2.setVisibility(View.INVISIBLE);
//                titleEtCS3.setVisibility(View.INVISIBLE);
//                titleEtCS4.setVisibility(View.INVISIBLE);
//                titleEtCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setText("KT");
//                titleEtCS2.setText("");
//                titleEtCS3.setText("");
//                titleEtCS4.setText("");
//                titleEtCS5.setText("");
//
//                mLLDoSaiLech.setVisibility(View.GONE);
//                etCS1.setHint("KT");
//                etCS2.setHint("");
//                etCS3.setHint("");
//                etCS4.setHint("");
//                etCS5.setHint("");
//                try {
//                    etCS1.setText(CS.contains(":") ? CS.split(";")[0].split(":")[1] : CS.split(";")[0]);
//
//                } catch (Exception e) {
//                    etCS1.setText(null);
//                }
//
//                btCameraMTB.setEnabled(true);
//            } else if (LOAI_CTO.equals("DT")) {
//                etCS1.setEnabled(true);
//                etCS2.setEnabled(true);
//                etCS3.setEnabled(true);
//                etCS4.setEnabled(true);
//                etCS5.setEnabled(true);
////                mLLDoSaiLech.setVisibility(View.VISIBLE);
//                mLLDoSaiLech.setVisibility(View.GONE);
//                etCS1.setVisibility(View.VISIBLE);
//                etCS2.setVisibility(View.VISIBLE);
//                etCS3.setVisibility(View.VISIBLE);
//                etCS4.setVisibility(View.VISIBLE);
//                etCS5.setVisibility(View.VISIBLE);
//
//                titleEtCS1.setVisibility(View.VISIBLE);
//                titleEtCS2.setVisibility(View.VISIBLE);
//                titleEtCS3.setVisibility(View.VISIBLE);
//                titleEtCS4.setVisibility(View.VISIBLE);
//                titleEtCS5.setVisibility(View.VISIBLE);
//
//                titleEtCS1.setText("BT");
//                titleEtCS2.setText("CD");
//                titleEtCS3.setText("TD");
//                titleEtCS4.setText("SG");
//                titleEtCS5.setText("VC");
//
//
//                etCS1.setHint("BT");
//                etCS2.setHint("CD");
//                etCS3.setHint("TD");
//                etCS4.setHint("SG");
//                etCS5.setHint("VC");
////
//                if (!CS.isEmpty() && CS.contains(";")) {
//                    etCS1.setText(CS.contains(":") ? CS.split(";")[0].split(":")[1] : CS.split(";")[0]);
//                    etCS2.setText(CS.contains(":") ? CS.split(";")[1].split(":")[1] : CS.split(";")[1]);
//                    etCS3.setText(CS.contains(":") ? CS.split(";")[2].split(":")[1] : CS.split(";")[2]);
//                    etCS4.setText(CS.contains(":") ? CS.split(";")[3].split(":")[1] : CS.split(";")[3]);
//                    etCS5.setText(CS.contains(":") ? CS.split(";")[4].split(":")[1] : CS.split(";")[4]);
//                } else {
//                    etCS1.setText("");
//                    etCS2.setText("");
//                    etCS3.setText("");
//                    etCS4.setText("");
//                    etCS5.setText("");
//                }
//                btCameraMTB.setEnabled(false);
//            } else {
//                etCS1.setEnabled(true);
//                etCS2.setEnabled(false);
//                etCS3.setEnabled(false);
//                etCS4.setEnabled(false);
//                etCS5.setEnabled(false);
//
//                etCS1.setVisibility(View.VISIBLE);
//                etCS2.setVisibility(View.INVISIBLE);
//                etCS3.setVisibility(View.INVISIBLE);
//                etCS4.setVisibility(View.INVISIBLE);
//                etCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setVisibility(View.VISIBLE);
//                titleEtCS2.setVisibility(View.INVISIBLE);
//                titleEtCS3.setVisibility(View.INVISIBLE);
//                titleEtCS4.setVisibility(View.INVISIBLE);
//                titleEtCS5.setVisibility(View.INVISIBLE);
//
//                titleEtCS1.setText("Chỉ số");
//                titleEtCS2.setText("");
//                titleEtCS3.setText("");
//                titleEtCS4.setText("");
//                titleEtCS5.setText("");
//
//
//                etCS1.setHint("Chỉ số");
//                etCS2.setHint("");
//                etCS3.setHint("");
//                etCS4.setHint("");
//                etCS5.setHint("");
//                etCS1.setText(CS.contains(":") ? CS.split(":")[1] : CS);
//                btCameraMTB.setEnabled(true);
//            }
//
//            String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
//            boolean kiemTraGui = .checkCtoDaGui(idBBTrTh, TthtCommon.getMaDviqly(), TthtCommon.getMaTramSelected(), TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()], NGAY_TRTH);
//
//            if (kiemTraGui) {
//                enableDisableViewGroup(mLLDialogGhiTotal, false);
//            } else {
//                enableDisableViewGroup(mLLDialogGhiTotal, true);
//            }
//            btSwitchThao.setEnabled(true);
//            ivImage.setEnabled(true);
//            btNext.setEnabled(true);
//            btPreview.setEnabled(true);
//        } catch (Exception ex) {
//            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED,
//                    "Lỗi khởi tạo giá trị\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
//        }
//    }
    //endregion

    //region Xử lý ảnh
    public String getImageName() {
        //Image name: {MA_NVIEN}_{ID_BBAN_TRTH}_{MA_CTO}
        if (adapterKH.listData.size() > 0) {
//            int idBBTrTh = adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getID_BBAN_TRTH();
            String dviQly = TthtCommon.getMaDviqly();
            Cursor c3 = connection.getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
            String MA_CTO = "";
            if (c3 != null) {
                MA_CTO = c3.getString(c3.getColumnIndex("MA_CTO"));
            }
            c3.close();
            StringBuilder name = new StringBuilder().append(dviQly)
                    .append("_").append(idBBTrTh)
                    .append("_").append(MA_CTO);
            return name.toString();
        } else {
            return "";
        }
    }

    public void setImage() {
        if (adapterKH != null && adapterKH.listData.size() > 0) {
            String fileName = Util.getSnapshotFileName(getImageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
            if (bitmap != null) {
                bitmap = Common.scaleDown(bitmap, 300, true);
                ivImage.setImageBitmap(bitmap);
            } else {
                ivImage.setImageBitmap(null);
            }
        }
    }

    private void saveImage(String name) {


        final String fileName = Util.getSnapshotFileName(name);
        BufferedOutputStream bos = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

            if (bitmap != null) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight();
                if (w < h) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Common.scaleDown(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), 600, true);
                } else {
                    bitmap = Common.scaleDown(bitmap, 600, true);
                }

                bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bos.write(Common.encodeTobase64Byte(bitmap));
                bos.close();
                TthtCommon.scanFile(TthtMainFragment.this.getActivity(), new String[]{fileName});
            }
        } catch (IOException ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi lưu ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void saveImage(Bitmap bmp) {
        if (adapterKH.listData.size() > 0) {
            final String fileName = Util.getSnapshotFileName(getImageName());
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bos.write(Common.encodeTobase64Byte(bmp));
                bos.close();
                TthtCommon.scanFile(TthtMainFragment.this.getActivity(), new String[]{fileName});
            } catch (IOException ex) {
                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi lưu ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
            }
        }
    }


    private void showDialogViewImage(Bitmap bmImage) {
        try {
            final Dialog dialog = new Dialog(TthtMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_viewimage);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final ImageViewTouch ivtImage = (ImageViewTouch) dialog.findViewById(R.id.essp_dialog_viewimage_ivt_image);

            ivtImage.setImageBitmapReset(bmImage, 0, true);

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    public void createDialogShowImage(Bitmap bm) {
        try {
            dialogImageZoom = new Dialog(TthtMainFragment.this.getActivity());
            dialogImageZoom.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogImageZoom.setContentView(R.layout.ttht_dialog_view_image);
//            int height = TthtMainFragment.this.getActivity().getWindowManager().getDefaultDisplay().getHeight() / 2;
            dialogImageZoom.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialogImageZoom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogImageZoom.setCanceledOnTouchOutside(false);

            final Button btXoa = (Button) dialogImageZoom.findViewById(R.id.gsht_dialog_view_image_bt_xoa);
            final ImageViewTouch ivImage = (ImageViewTouch) dialogImageZoom.findViewById(R.id.gsht_dialog_view_image_iv_image);


            ivImage.setImageBitmapReset(bm, 0, true);
//            ivImage.setScaleType(ImageView.ScaleType.FIT_START);
//            ivImage.setImageBitmap(bm);

            btXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
                    boolean kiemTraGui = connection.checkCtoDaGui(idBBTrTh, TthtCommon.getMaDviqly(), TthtCommon.getMaTramSelected(), TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()], NGAY_TRTH);

                    if (kiemTraGui) {
                        Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                "Biên bản đã được gửi. Không thể xóa được ảnh", Color.WHITE, "OK", Color.WHITE);
                        return;
                    }
//                    DeleteImage();
                }
            });
            dialogImageZoom.show();
        } catch (Exception ex) {
            Toast.makeText(TthtMainFragment.this.getActivity(), "Lỗi:" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*private void drawTextOnBitmap() {
        try {
            if (adapterKH.listData.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                Cursor cursorBBan = connection.getBBanByIDBBanTRTH(idBBTrTh);

                String MA_DDO = cursorBBan.getString(cursorBBan.getColumnIndex("MA_DDO"));
                String TEN_KHANG = cursorBBan.getString(cursorBBan.getColumnIndex("TEN_KHANG"));
                cursorBBan.close();
                String NGAY_CHUP = sdf.format(cal.getTime());
                Cursor cursorCTo = connection.getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                String SO_CTO = "";
                SO_CTO = cursorCTo.getString(cursorCTo.getColumnIndex("SO_CTO"));
                cursorCTo.close();
//                String CHI_SO = adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getCHI_SO();
                StringBuilder CHI_SO = new StringBuilder();
                String CS1 = etCS1.getText().toString();
                if (!CS1.equals("")) {
                    CHI_SO.append(etCS1.getHint().toString() + ":");
                    CHI_SO.append(etCS1.getText().toString());

                }
                String CS2 = etCS2.getText().toString();
                if (!CS2.equals("")) {
                    CHI_SO.append(";");
                    CHI_SO.append(etCS2.getHint().toString() + ":");
                    CHI_SO.append(etCS2.getText().toString());
                }
                String CS3 = etCS3.getText().toString();
                if (!CS3.equals("")) {
                    CHI_SO.append(";");
                    CHI_SO.append(etCS3.getHint().toString() + ":");
                    CHI_SO.append(etCS3.getText().toString());
                }
                String CS4 = etCS4.getText().toString();
                if (!CS4.equals("")) {
                    CHI_SO.append(";");
                    CHI_SO.append(etCS4.getHint().toString() + ":");
                    CHI_SO.append(etCS4.getText().toString());
                }
                String CS5 = etCS5.getText().toString();
                if (!CS5.equals("")) {
                    CHI_SO.append(";");
                    CHI_SO.append(etCS5.getHint().toString() + ":");
                    CHI_SO.append(etCS5.getText().toString());
                }

                String fileName = Util.getSnapshotFileName(getImageName());
                File fBitmap = new File(fileName);
                if (fBitmap.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bmRoot = BitmapFactory.decodeFile(fileName, options);
                    if (bmRoot != null) {
                        Bitmap.Config bmConfig = bmRoot.getConfig();
                        if (bmConfig == null) {
                            bmConfig = android.graphics.Bitmap.Config.ARGB_8888;
                        }
                        bmRoot = bmRoot.copy(bmConfig, true);

                        Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint_text.setColor(Color.WHITE);
                        paint_text.setTextSize(bmRoot.getHeight() / 20);

                        Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint_rec.setColor(Color.BLACK);

                        String[] arrName = TEN_KHANG.trim().split(" ");
                        String name1 = "";
                        String name2 = "";

                        for (String sName : arrName) {
                            Rect bounds_cut = new Rect();
                            paint_text.getTextBounds(name1, 0, name1.length(), bounds_cut);
                            if (bounds_cut.width() < bmRoot.getWidth()) {
                                name1 += sName + " ";
                            } else {
                                name2 += sName + " ";
                            }
                        }

                        TEN_KHANG = name1.trim();

                        Rect bounds_name = new Rect();
                        paint_text.getTextBounds(TEN_KHANG, 0, TEN_KHANG.length(), bounds_name);
                        int x_name = 10;
                        int y_name_1 = bounds_name.height();
                        int y_name_2 = 2 * bounds_name.height();

                        // ---------------Tạo khung ảnh mới, chèn thêm vùng đen trên và dưới ảnh để điền chữ, trên 3 dòng, dưới 1 dòng
                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//                        Bitmap bmp = Bitmap.createBitmap(bmRoot.getWidth(), bmRoot.getHeight() + 4 * bounds_name.height(), conf);
                        Bitmap bmp = Bitmap.createBitmap(bmRoot.getWidth(), bmRoot.getHeight() + 5 * bounds_name.height(), conf);
                        Canvas cBitmap = new Canvas(bmp);


                        // ---------------Điền tên lên ảnh
                        cBitmap.drawRect(0, 0, bmp.getWidth(), 2 * bounds_name.height() + 15, paint_rec);
                        cBitmap.drawRect(0, bmp.getHeight() - 2 * bounds_name.height() - 15, bmp.getWidth(), bmp.getHeight(), paint_rec);
                        cBitmap.drawText(TEN_KHANG, x_name, y_name_1, paint_text);
                        if (!name2.isEmpty())
                            cBitmap.drawText(name2, x_name, y_name_2, paint_text);
                        // Vẽ ảnh lên khung mới có viền đen
//                        cBitmap.drawBitmap(bmRoot, 0, 3 * bounds_name.height() + 15, paint_rec);
                        cBitmap.drawBitmap(bmRoot, 0, 5 * bounds_name.height() + 15, paint_rec);

                        // ---------------Điền giờ lên ảnh
                        Rect bounds_date = new Rect();
                        paint_text.getTextBounds(NGAY_CHUP, 0, NGAY_CHUP.length(), bounds_date);
                        cBitmap.drawRect(0, 2 * bounds_name.height() + 15, bounds_date.width() + 15, 3 * bounds_name.height() + 15, paint_rec);

                        int x_date = 10;
                        int y_date = 2 * bounds_name.height() + 30;
                        cBitmap.drawText(NGAY_CHUP, x_date, y_date, paint_text);

                        // ---------------Điền mã điểm đo lên ảnh
                        Rect bounds_maddo = new Rect();
                        paint_text.getTextBounds("MÃ ĐIỂM ĐO: " + MA_DDO, 0, ("MÃ ĐIỂM ĐO: " + MA_DDO).length(), bounds_maddo);
                        int x_maddo = 10;
                        int y_maddo = bmp.getHeight() - 10;
                        cBitmap.drawText("MÃ ĐIỂM ĐO: " + MA_DDO, x_maddo, y_maddo, paint_text);

                        // ---------------Điền chỉ số lên ảnh
                        if (CHI_SO != null && !CHI_SO.equals("")) {
                            Rect bounds_cso = new Rect();
                            String tien_to = "";
                            if (adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_BDONG().equals("E")) {
                                tien_to = "CS tháo: ";
                            } else {
                                tien_to = "CS treo: ";
                            }
                            paint_text.getTextBounds(tien_to + CHI_SO.toString(), 0, (tien_to + CHI_SO.toString()).length(), bounds_cso);
//                            cBitmap.drawRect(bmRoot.getWidth() / 2, 2 * bounds_cso.height() + 15, bmRoot.getWidth(), 3 * bounds_cso.height() + 15, paint_rec);
                            cBitmap.drawRect(0, 3 * bounds_cso.height() + 30, bmRoot.getWidth(), 3 * bounds_cso.height() + 30, paint_rec);
                            int x_cso = bmRoot.getWidth() - bounds_cso.width() - 10;
//                            int y_cso = 2 * bounds_cso.height() + 30;
                            int y_cso = 3 * bounds_cso.height() + 45;
                            cBitmap.drawText(tien_to + CHI_SO.toString(), x_cso, y_cso, paint_text);
                        }

                        // ---------------Điền số công tơ lên ảnh
                        if (SO_CTO != null && !SO_CTO.isEmpty()) {
                            Rect bounds_socto = new Rect();
                            paint_text.getTextBounds("SỐ CTO: " + SO_CTO, 0, ("SỐ CTO: " + SO_CTO).length(), bounds_socto);
                            int x_socto = bmp.getWidth() - 10 - bounds_socto.width();
                            int y_socto = bmp.getHeight() - 10;
                            cBitmap.drawText("SỐ CTO: " + SO_CTO, x_socto, y_socto, paint_text);
                        }
                        saveImage(bmp);
                        setImage();
                    }
                }
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi ghi lên ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }*/
/*
    private void drawCSOnBitmap(String LOAI_CTO, String CHI_SO1) {
        try {
            StringBuilder CHI_SO = new StringBuilder();
            String CS1 = etCS1.getText().toString();
            if (!CS1.equals("")) {
                CHI_SO.append(etCS1.getHint().toString() + ":");
                CHI_SO.append(etCS1.getText().toString());

            }
            String CS2 = etCS2.getText().toString();
            if (!CS2.equals("")) {
                CHI_SO.append(";");
                CHI_SO.append(etCS2.getHint().toString() + ":");
                CHI_SO.append(etCS2.getText().toString());
            }
            String CS3 = etCS3.getText().toString();
            if (!CS3.equals("")) {
                CHI_SO.append(";");
                CHI_SO.append(etCS3.getHint().toString() + ":");
                CHI_SO.append(etCS3.getText().toString());
            }
            String CS4 = etCS4.getText().toString();
            if (!CS4.equals("")) {
                CHI_SO.append(";");
                CHI_SO.append(etCS4.getHint().toString() + ":");
                CHI_SO.append(etCS4.getText().toString());
            }
            String CS5 = etCS5.getText().toString();
            if (!CS5.equals("")) {
                CHI_SO.append(";");
                CHI_SO.append(etCS5.getHint().toString() + ":");
                CHI_SO.append(etCS5.getText().toString());
            }


            if (adapterKH.listData.size() > 0) {
                String fileName = Util.getSnapshotFileName(getImageName());
                File fBitmap = new File(fileName);
                if (!fBitmap.exists() && !LOAI_CTO.equals("DT")) {
                    Toast.makeText(TthtMainFragment.this.getActivity(), "Khách hàng chưa chụp ảnh", Toast.LENGTH_LONG).show();
                    return;
                }
                lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setCHI_SO(CHI_SO.toString());
                adapterKH.updateList(lstKhangHangTreo);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bmRoot = BitmapFactory.decodeFile(fileName, options);
                if (bmRoot != null) {
                    Bitmap.Config bmConfig = bmRoot.getConfig();
                    if (bmConfig == null) {
                        bmConfig = android.graphics.Bitmap.Config.ARGB_8888;
                    }
                    bmRoot = bmRoot.copy(bmConfig, true);

                    Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint_text.setColor(Color.WHITE);
                    paint_text.setTextSize(bmRoot.getHeight() / 24);

                    Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint_rec.setColor(Color.BLACK);

                    // ---------------Tạo khung ảnh mới, chèn thêm vùng đen trên và dưới ảnh để điền chữ, trên 3 dòng, dưới 1 dòng
                    Canvas cBitmap = new Canvas(bmRoot);

                    String LOAICS = "";
                    if (adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_BDONG().equals("E")) {
                        LOAICS = "CS tháo: ";
                    } else {
                        LOAICS = "CS treo: ";
                    }
                    // ---------------Điền chỉ số lên ảnh
                    Rect bounds_cso = new Rect();
                    paint_text.getTextBounds(LOAICS + CHI_SO.toString(), 0, (LOAICS + CHI_SO.toString()).length(), bounds_cso);
                    cBitmap.drawRect(bmRoot.getWidth() / 2, 2 * bounds_cso.height() + 12, bmRoot.getWidth(), 3 * bounds_cso.height() + 23, paint_rec);

                    int x_cso = bmRoot.getWidth() - bounds_cso.width() - 10;
                    int y_cso = 2 * bounds_cso.height() + 30;
                    cBitmap.drawText(LOAICS + CHI_SO.toString(), x_cso, y_cso, paint_text);

                    // ---------------Điền số công tơ lên ảnh
//                    if (SO_CTO != null && !SO_CTO.isEmpty() && TREO_THAO.equals("TREO")) {
//                        Rect bounds_socto = new Rect();
//                        paint_text.getTextBounds(SO_CTO, 0, SO_CTO.length(), bounds_socto);
//                        cBitmap.drawRect(bmRoot.getWidth() / 2, bmRoot.getHeight() - 30, bmRoot.getWidth(), bmRoot.getHeight(), paint_rec);
//                        int x_socto = bmRoot.getWidth() - 10 - bounds_socto.width();
//                        int y_socto = bmRoot.getHeight() - 10;
//                        cBitmap.drawText(SO_CTO, x_socto, y_socto, paint_text);
//                    }
//                    if (TREO_THAO.equals("TREO"))
//                        fBitmap.delete();

                    saveImage(bmRoot);
                    setImage();
                    int posSelect = adapterKH.getPosIDBBanTrThao(idBBTrTh);
//                    int posSelect = adapterKH.getPosSelect();
                    if (posSelect < adapterKH.listData.size() - 1) {
//                        adapterKH.setPosSelect(adapterKH.getPosSelect() + 1);
                        adapterKH.setPosSelect(posSelect + 1);
                        adapterKH.notifyDataSetChanged();
                        setImage();
                        setDataOnEditText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getLOAI_CTO(),
                                adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getCHI_SO());
                        etCS1.requestFocus();
                    }
                } else {
                    int posSelect = adapterKH.getPosIDBBanTrThao(idBBTrTh);
//                    int posSelect = adapterKH.getPosSelect();
                    if (posSelect < adapterKH.listData.size() - 1) {
//                        adapterKH.setPosSelect(adapterKH.getPosSelect() + 1);
                        adapterKH.setPosSelect(posSelect + 1);
                        adapterKH.notifyDataSetChanged();
                        setImage();
                        setDataOnEditText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getLOAI_CTO(),
                                adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getCHI_SO());
                        etCS1.requestFocus();
                    }
                }
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi ghi lên ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }*/
/*

    public void DeleteImage(final int TYPE) {
        Dialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(TthtMainFragment.this.getActivity());
        builder.setTitle("Xóa ảnh");
        builder.setMessage("Bạn có chắc chắn muốn xóa ảnh?");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fileName = Util.getSnapshotFileName(getImageName());
                        File fBitmap = new File(fileName);
                        if (fBitmap.exists()) {
                            fBitmap.delete();
                            if (connection.deleteDataAnhByID_CHITIET_CTOAndTYPE(
                                    adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getID_CHITIET_CTO(),
                                    TYPE) != -1) {
                                if (connection.updateCongTo(
                                        adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getMA_NVIEN(),
                                        adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getID_BBAN_TRTH(),
                                        adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_CTO(),
                                        "", "", "", "", 0, "", 0, "", 0 , "", "", 1, 0, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]) != -1) {
                                    if (connection.updateGhiChuBBan(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getId_BBAN_CONGTO(), "") != -1) {
                                    }
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtBBanEntity().setGHI_CHU("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setCHI_SO("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_KIM_NIEM_CHI("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setTTRANG_NPHONG("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCBOOC("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CBOOC(0);
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_CHIKDINH("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CHIKDINH(0);
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCHOM("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_VIENCHOM(0);

                                    adapterKH.updateList(lstKhangHangTreo);
                                    ivImage.setImageBitmap(null);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialogImageZoom.dismiss();
                                        }
                                    });

                                } else {
                                    Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                            "Không xóa được chỉ số", Color.WHITE, "OK", Color.WHITE);
                                }
                            } else {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                        "Không xóa được ảnh", Color.WHITE, "OK", Color.WHITE);
                            }
                        }
                    }
                });
        builder.setNegativeButton("Cancel", null);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_REQUEST && resultCode != 0) {
                if (adapterKH.listData.size() > 0) {
                    Cursor c = connection.getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                    int ID_CHITIET_CTO = c.getInt(c.getColumnIndex("ID_CHITIET_CTO"));
                    c.close();
                    /*if (
                            connection.insertDataAnh(
                                    TthtCommon.getMaDviqly(), ID_CHITIET_CTO,
                                    getImageName()) != -1) {
                        saveImage(getImageName());
                        drawTextOnBitmap();
                    }*/
                }
            }

        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi chụp ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion

    //region Xử lý popup
    /*public void showPopupMenu(View v) {
        try {
            PopupMenu menu = new PopupMenu(TthtMainFragment.this.getActivity());
            menu.setHeaderTitle("Chức năng");
            menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case TthtConstantVariables.MENU_CHI_TIET:
                            showDialogDetail();
                            break;
//                        case TthtConstantVariables.MENU_NHAN_BBAN:
//                            NhanBBan();
//                            break;
//                        case TthtConstantVariables.MENU_GUI_BBAN:
//                            GuiBBan();
//                            break;
//                        case TthtConstantVariables.MENU_GHI_CHU:
//
//                            //showDialogNote();
//                            break;
                        case TthtConstantVariables.MENU_XOA_CS:

                            String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
                            boolean kiemTraGui = connection.checkCtoDaGui(idBBTrTh, TthtCommon.getMaDviqly(), TthtCommon.getMaTramSelected(), TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()], NGAY_TRTH);

                            if (kiemTraGui) {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                        "Biên bản đã được gửi. Không thể xóa được chỉ số", Color.WHITE, "OK", Color.WHITE);
                                return;
                            }

                            Cursor cursorCongTo = connection.getCToByIDBBanWithMaBDong(idBBTrTh, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                            int ID_CHITIET_CTO = cursorCongTo.getInt(cursorCongTo.getColumnIndex("ID_CHITIET_CTO"));
                            String MA_CTO = cursorCongTo.getString(cursorCongTo.getColumnIndex("MA_CTO"));
                            cursorCongTo.close();
                            Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO);
                            String fileName = "";
                            if (cursorAnh != null) {
                                fileName = Util.getSnapshotFileName(cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH")));
                            }
                            File fBitmap = new File(fileName);
                            if (fBitmap.exists()) {
                                fBitmap.delete();
                                if (connection.deleteDataAnhByID_CHITIET_CTOAndTYPE(TthtCommon.getMaDviqly(), idBBTrTh) != -1) {
                                    if (connection.updateCongTo(TthtCommon.getMaDviqly(), idBBTrTh, MA_CTO,
                                            "", "", "", "", 0, "", 0, "", 0,"", "", 1, 0, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]) != -1) {
                                        if (connection.updateGhiChuBBan(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getId_BBAN_CONGTO(),
                                                "") != -1) {
                                        }
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtBBanEntity().setGHI_CHU("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setCHI_SO("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_KIM_NIEM_CHI("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setTTRANG_NPHONG("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCBOOC("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CBOOC(0);
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_CHIKDINH("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CHIKDINH(0);
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCHOM("");
                                        lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_VIENCHOM(0);
                                        adapterKH.updateList(lstKhangHangTreo);
                                        ivImage.setImageBitmap(null);
                                    } else {
                                        Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                                "Không xóa được chỉ số", Color.WHITE, "OK", Color.WHITE);
                                    }
                                } else {
                                    Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                            "Không xóa được ảnh", Color.WHITE, "OK", Color.WHITE);
                                }
                            } else {
                                if (connection.updateCongTo(TthtCommon.getMaDviqly(), idBBTrTh, MA_CTO,
                                        "", "", "", "", 0, "", 0, "", 0, "", "", 1, 0, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]) != -1) {
                                    if (connection.updateGhiChuBBan(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getId_BBAN_CONGTO(),
                                            "") != -1) {
                                    }
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtBBanEntity().setGHI_CHU("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setCHI_SO("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_KIM_NIEM_CHI("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setTTRANG_NPHONG("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCBOOC("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CBOOC(0);
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_CHIKDINH("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSOVIEN_CHIKDINH(0);
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setMA_SOCHOM("");
                                    lstKhangHangTreo.get(adapterKH.getPosSelect()).getTthtCtoEntity().setSO_VIENCHOM(0);
                                    adapterKH.updateList(lstKhangHangTreo);
                                } else {
                                    Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                            "Không xóa được chỉ số", Color.WHITE, "OK", Color.WHITE);
                                }
                            }
                            break;
                    }
                }
            });

            menu.add(TthtConstantVariables.MENU_CHI_TIET, "Chi tiết").setIcon(getResources().getDrawable(R.mipmap.icon_detail));
//            menu.add(TthtConstantVariables.MENU_GHI_CHU, "Ghi chú").setIcon(getResources().getDrawable(R.mipmap.gcs_ic_note));
            menu.add(TthtConstantVariables.MENU_XOA_CS, "Xóa chỉ số").setIcon(getResources().getDrawable(R.mipmap.gcs_ic_delete));

            menu.show(getView());
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                    "Lỗi chức năng", Color.WHITE, "OK", Color.WHITE);
        }
    }
*/

    public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
            if (view instanceof ImageView) {
                view.setEnabled(true);
            }
            if (enabled) {
                if (view instanceof EditText) {
                    ((EditText) view).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_edittext));
                    ((EditText) view).setTextColor(Color.parseColor("#000000"));
                }
                if (view instanceof Spinner) {
                    view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_spinner));
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }

        }
    }

    public void showActivityCapNhatBBan() {
        Intent intent = new Intent(getActivity(), TthtCapNhatBBanActivity.class);
        startActivity(intent);
    }


  /*  public void showDialogNote() {
        try {
            final Dialog dialog = new Dialog(TthtMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_ghichu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final EditText etGhiChu = (EditText) dialog.findViewById(R.id.gcs_dialog_ghichu_etGhichu);
            Button etLuu = (Button) dialog.findViewById(R.id.gcs_dialog_ghichu_btLuu);

            etLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String ghiChu = etGhiChu.getText().toString();
                        etGhiChu.setError(null);
                        if (ghiChu.isEmpty()) {
                            etGhiChu.setError("Bạn chưa nhập ghi chú");
                        } else {
                            if (connection.updateGhiChuBBan(
                                    adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getId_BBAN_CONGTO(), ghiChu) != -1) {
                                adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().setGHI_CHU(ghiChu);
                                adapterKH.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                                        "Lỗi lưu ghi chú", Color.WHITE, "OK", Color.WHITE);
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                                "Lỗi lưu ghi chú", Color.WHITE, "OK", Color.WHITE);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
                    "Lỗi ghi chú", Color.WHITE, "OK", Color.WHITE);
        }
    }
*/
//    public void showDialogDetail() {
//        try {
//            final Dialog dialog = new Dialog(TthtMainFragment.this.getActivity());
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.ttht_dialog_detail);
//            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
//            dialog.setCanceledOnTouchOutside(false);
//
//            final TabHost tabHost = (TabHost) dialog.findViewById(R.id.tabHost);
//            tabHost.setup();
//
//            TabHost.TabSpec spec1 = tabHost.newTabSpec("BBAN");
//            spec1.setContent(R.id.ttht_dialog_detail_tabBBan);
//            spec1.setIndicator("Biên bản");
//
//            TabHost.TabSpec spec2 = tabHost.newTabSpec("CTO");
//            spec2.setIndicator("Công tơ");
//            spec2.setContent(R.id.ttht_dialog_detail_tabCto);
//
//            tabHost.addTab(spec1);
//            tabHost.addTab(spec2);
//
//            setTabColor(tabHost);
//
//            TextView TEN_KH = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvTenKH);
//            TextView tvDChi = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvDChi);
//            TextView tvSDT = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSDT);
//            TextView tvMaTram = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaTram);
//            TextView tvMaHDong = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaHDong);
//            TextView tvMaDDo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaDDo);
//            TextView tvSoBBan = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoBBan);
//            TextView tvNgayTreoThao = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNgayTreoThao);
//            TextView tvMaNVien = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaNVien);
//            TextView tvMaLDo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaLDo);
//            TextView tvNgayTao = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNgayTao);
//            TextView tvNguoiTao = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNguoiTao);
//            TextView tvNgaySua = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNgaySua);
//            TextView tvNguoiSua = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNguoiSua);
//            TextView tvMaYCau = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaYCau);
//            TextView tvGhiChu = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvGhiChu);
//            TextView tvLoaiBBan = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvLoaiBBan);
//            TextView tvMaGCSCto = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaGCSCto);
//
//            TEN_KH.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getTEN_KHANG());
//            tvDChi.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getDCHI_HDON());
//            tvSDT.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getDTHOAI());
//            tvMaTram.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getMA_TRAM());
//            tvMaHDong.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getMA_HDONG());
//            tvMaDDo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getDATE_CALL_API());
//            tvSoBBan.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getTYPE_CALL_API());
//            tvNgayTreoThao.setText(getDate(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getTYPE_RESULT(), "yyyy-MM-dd'T'HH:mm:ss"));
//            tvMaNVien.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getMA_NVIEN());
//            tvMaLDo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_BBAN_API());
//            tvNgayTao.setText(getDate(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_CTO_THAO_API(), "yyyy-MM-dd'T'HH:mm:ss"));
//            tvNguoiTao.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_CTO_TREO_API());
//            tvNgaySua.setText(getDate(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_BBAN_TUTI_API(), "yyyy-MM-dd'T'HH:mm:ss"));
//            tvNguoiSua.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_TU_API());
//            tvMaYCau.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getSO_TRAM_API());
//            tvGhiChu.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getGHI_CHU());
//            tvLoaiBBan.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getLOAI_BBAN());
//            tvMaGCSCto.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtBBanEntity().getMA_GCS_CTO());
//
//            TextView tvMaCTo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaCTo);
//            TextView tvSoCTo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoCto);
//            TextView tvLan = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvLan);
//            TextView tvMaBDong = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaBDong);
//            TextView tvNgayBDong = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNgayBDong);
//            TextView tvMaCLoai = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaCLoai);
//            TextView tvLoaiCTo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvLoaiCTo);
//            TextView tvVTriTreo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvVTriTreo);
//            TextView tvMaSoChiBooc = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaSoChiBooc);
//            TextView tvSoVienChiBooc = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoVienChiBooc);
//            TextView tvMaChiHom = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaChiHom);
//            TextView tvSoVienChiHom = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoVienChiHom);
//            TextView tvHeSoNhan = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvHeSoNhan);
//            TextView tvMaCNang = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaCNang);
//            TextView tvSoTU = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoTU);
//            TextView tvSoTI = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoTI);
//            TextView tvSoCot = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoCot);
//            TextView tvSoHom = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoHom);
//            TextView tvChiSo = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvChiSo);
//            TextView tvNgayKDinh = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNgayKDinh);
//            TextView tvNamSX = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvNamSX);
//            TextView tvTemCQuang = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvTemCQuang);
//            TextView tvMaChiKDinh = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaChiKDinh);
//            TextView tvMaTem = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaTem);
//            TextView tvSoVienChiKDinh = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoVienChiKDinh);
//            TextView tvDienAp = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvDienAp);
//            TextView tvDongDien = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvDongDien);
//            TextView tvHangSoK = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvHangSoK);
//            TextView tvMaNuoc = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvMaNuoc);
//            TextView tvTenNuoc = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvTenNuoc);
//            TextView tvSoKimNiemChi = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvSoKimNiemChi);
//            TextView tvTTNiemPhong = (TextView) dialog.findViewById(R.id.ttht_dialog_detail_tvTTNiemPhong);
//
//            tvMaCTo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_CTO());
//            tvSoCTo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_CTO());
//            tvLan.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getLAN()));
//            tvMaBDong.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_BDONG());
//            tvNgayBDong.setText(getDate(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getNGAY_BDONG(), "yyyy-MM-dd'T'HH:mm:ss"));
//            tvMaCLoai.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_CLOAI());
//            tvLoaiCTo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getLOAI_CTO());
//            tvVTriTreo.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getVTRI_TREO()));
//            tvMaSoChiBooc.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_SOCBOOC());
//            tvSoVienChiBooc.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSOVIEN_CBOOC()));
//            tvMaChiHom.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_SOCHOM());
//            tvSoVienChiHom.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSOVIEN_CHOM()));
//            tvHeSoNhan.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getHS_NHAN()));
//            tvMaCNang.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_TI_API());
//            tvSoTU.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_TU_API());
//            tvSoTI.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_TI_API());
//            tvSoCot.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_COT());
//            tvSoHom.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_HOM());
//            tvChiSo.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getCHI_SO());
//            tvNgayKDinh.setText(getDate(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getNGAY_KDINH(), "yyyy-MM-dd'T'HH:mm:ss"));
//            tvNamSX.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getNAM_SX());
//            tvTemCQuang.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getTEM_CQUANG());
//            tvMaChiKDinh.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_CHIKDINH());
//            tvMaTem.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_TEM());
//            tvSoVienChiKDinh.setText(String.valueOf(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSOVIEN_CHIKDINH()));
//            tvDienAp.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getDIEN_AP());
//            tvDongDien.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getDONG_DIEN());
//            tvHangSoK.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getHANGSO_K());
//            tvMaNuoc.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getMA_NUOC());
//            tvTenNuoc.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getTEN_NUOC());
//            tvSoKimNiemChi.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getSO_KIM_NIEM_CHI());
//            tvTTNiemPhong.setText(adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getTTRANG_NPHONG());
//
//            dialog.show();
//        } catch (Exception ex) {
//            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.WHITE,
//                    "Lỗi chi tiết", Color.WHITE, "OK", Color.WHITE);
//        }
//    }


    private String getDate(String strCurrentDate, String sFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(sFormat);
            Date newDate = null;
            newDate = format.parse(strCurrentDate);

            format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = format.format(newDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
//            tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_indicator); //unselected
            tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_indicator); //unselected
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
//            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setGravity(Gravity.CENTER);
        }
    }
    //endregion

    public void DongBoBBan() {

    }


    //region Gửi nhận dữ liệu
    public void NhanBBan() {
        TthtCommon.isDownload = false;
        if (!isThreadRun) {
            isThreadRun = true;
            updateBBan();
        }
    }


    public void GuiBBan() {
        try {
            if (!Common.isNetworkOnline(TthtMainFragment.this.getActivity())) {
                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
            } else {
                //TODO lọc những danh sách gửi để chọn
                lstChooseTreoToSubmit.clear();
                lstChooseThaoToSubmit.clear();
                if (lstDSoatTreo.size() == 0 || lstDSoatTreo.size() == 0)
                    throw new Exception("Không có biên bản để gửi!");

                if (isClickChooseArrayDoiSoat.length != lstDSoatTreo.size() || isClickChooseArrayDoiSoat.length != lstDSoatTreo.size())
                    throw new Exception("Lỗi khi chọn biên bản để gửi!");


                for (int i = 0; i < isClickChooseArrayDoiSoat.length; i++) {
                    if (isClickChooseArrayDoiSoat[i]) {
                        lstChooseTreoToSubmit.add(lstDSoatTreo.get(i));
                        lstChooseThaoToSubmit.add(lstDSoatThao.get(i));
                    }
                }

                if (lstChooseTreoToSubmit.size() == 0) {
                    lstChooseTreoToSubmit.addAll(lstDSoatTreo);
                }

                if (lstChooseThaoToSubmit.size() == 0) {
                    lstChooseThaoToSubmit.addAll(lstDSoatThao);
                }

                if (lstChooseTreoToSubmit.size() > 0 && lstChooseThaoToSubmit.size() > 0) {
                    count = 0;
                    countBBan = 0;
                    countCTo = 0;
                    countFileSent = 0;
                    progressDialog = new ProgressDialog(this.getActivity());
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Đang gửi biên bản ...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    progressBarStatus = 0;
                    maxSize = lstChooseTreoToSubmit.size() + lstChooseThaoToSubmit.size();
                    snapProgresBar = (float) maxSize / 100f;

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public synchronized void run() {
                            try {
                                Thread.sleep(50);
                                JSONArray jsonArr = new JSONArray();
                                int ID_CHITIET_CTO_TREO = 0;
                                int ID_BBAN_TUTI_CTO_TREO = 0;
                                int ID_CHITIET_TUTI_TU_CTO_TREO = 0;
                                int ID_CHITIET_TUTI_TI_CTO_TREO = 0;
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
                                String CREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO = "";
                                String CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO = "";

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

                                int ID_CHITIET_CTO_THAO = 0;
                                int ID_BBAN_TUTI_CTO_THAO = 0;
                                int ID_CHITIET_TUTI_TU_CTO_THAO = 0;
                                int ID_CHITIET_TUTI_TI_CTO_THAO = 0;
                                String TEN_ANH_CTO_THAO = "";
                                String TEN_ANH_NIEMPHONG_CTO_THAO = "";
                                String TEN_ANH_TU_CTO_THAO = "";
                                String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO = "";
                                String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = "";
                                String ANH_CTO_THAO = "";
                                String ANH_NIEMPHONG_CTO_THAO = "";
                                String CREATE_DAY_ANH_CTO_THAO = "";
                                String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO = "";
                                String CREATE_DAY_ANH_TU_CTO_THAO = "";
                                String CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO = "";
                                String CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO = "";
                                String ANH_TU_CTO_THAO = "";
                                String ANH_TU_ANH_MACH_NHI_THU_CTO_THAO = "";
                                String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = "";


                                for (int i = 0; i < lstChooseTreoToSubmit.size(); i++) {
                                    entityTTCongTo = new TthtEntityTTCongTo();
                                    //TODO bien ban
                                    int ID_BBAN_CONGTO = lstChooseTreoToSubmit.get(i).getTthtBBanEntity().getId_BBAN_CONGTO();
                                    String MA_DVIQLY = lstChooseTreoToSubmit.get(i).getTthtBBanEntity().getMA_DVIQLY();
                                    String SO_BBAN = lstChooseTreoToSubmit.get(i).getTthtBBanEntity().getSO_BBAN();
                                    int TRANG_THAI = lstChooseTreoToSubmit.get(i).getTthtBBanEntity().getTRANG_THAI();
                                    //TODO cong to treo
                                    int LAN_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getLAN();
                                    int VTRI_TREO_THAO_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getVTRI_TREO();
                                    int SOVIEN_CBOOC_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CBOOC();
                                    int LOAI_HOM_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getLOAI_HOM();
                                    int SOVIEN_CHOM_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CHOM();
                                    float HS_NHAN_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getHS_NHAN_SAULAP_TUTI();
                                    int SOVIEN_CHIKDINH_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CHIKDINH();
                                    String TEM_CQUANG_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getTEM_CQUANG();
                                    String SO_KIM_NIEM_CHI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSO_KIM_NIEM_CHI();
                                    String TTRANG_NPHONG_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getTTRANG_NPHONG();
                                    String TEN_LOAI_CTO_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getTEN_LOAI_CTO();
                                    String PHUONG_THUC_DO_XA_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getPHUONG_THUC_DO_XA();
                                    String GHI_CHU_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getGHI_CHU();

                                    String DIEN_AP_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getDIEN_AP_SAULAP_TUTI();
                                    String DONG_DIEN_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getDONG_DIEN_SAULAP_TUTI();
                                    String HANGSO_K_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getHANGSO_K_SAULAP_TUTI();
                                    String SO_TU_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSO_TU_SAULAP_TUTI();
                                    String SO_TI_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getSO_TI_SAULAP_TUTI();
                                    String CHI_SO_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getCHI_SO_SAULAP_TUTI();
                                    int CAP_CX_SAULAP_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getCAP_CX_SAULAP_TUTI();

                                    //TODO cong to thao
                                    int LAN_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getLAN();
                                    int VTRI_TREO_THAO_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getVTRI_TREO();
                                    int SOVIEN_CBOOC_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CBOOC();
                                    int LOAI_HOM_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getLOAI_HOM();
                                    int SOVIEN_CHOM_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CHOM();
                                    String TEM_CQUANG_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getTEM_CQUANG();
                                    int SOVIEN_CHIKDINH_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSOVIEN_CHIKDINH();
                                    String SO_KIM_NIEM_CHI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSO_KIM_NIEM_CHI();
                                    String TTRANG_NPHONG_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getTTRANG_NPHONG();
                                    String TEN_LOAI_CTO_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getTEN_LOAI_CTO();
                                    String PHUONG_THUC_DO_XA_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getPHUONG_THUC_DO_XA();
                                    String GHI_CHU_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getGHI_CHU();

                                    float HS_NHAN_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getHS_NHAN_SAULAP_TUTI();
                                    String DIEN_AP_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getDIEN_AP_SAULAP_TUTI();
                                    String DONG_DIEN_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getDONG_DIEN_SAULAP_TUTI();
                                    String HANGSO_K_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getHANGSO_K_SAULAP_TUTI();
                                    String SO_TU_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSO_TU_SAULAP_TUTI();
                                    String SO_TI_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getSO_TI_SAULAP_TUTI();
                                    String CHI_SO_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getCHI_SO_SAULAP_TUTI();
                                    int CAP_CX_SAULAP_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getCAP_CX_SAULAP_TUTI();


                                    //TODO update bang DU_LIEU_HIEN_TRUONG
                                    String PATH_ANH = "";
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                                    //TODO anh Cong To Treo
                                    ID_CHITIET_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getID_CHITIET_CTO();
                                    ID_BBAN_TUTI_CTO_TREO = lstChooseTreoToSubmit.get(i).getTthtCtoEntity().getID_BBAN_TUTI();
                                    if (ID_BBAN_TUTI_CTO_TREO != 0) {
                                        ArrayList<TthtTuTiEntity> tuTiEntityArrayList = lstChooseTreoToSubmit.get(i).getTthtTuTiEntity();
//                                        ID_CHITIET_TUTI_CTO_TREO
                                        for (TthtTuTiEntity object : tuTiEntityArrayList) {
                                            if (object.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[0]) && object.getIS_TU().equalsIgnoreCase("true")) {
                                                //TODO nếu là TU của công tơ treo
                                                ID_CHITIET_TUTI_TU_CTO_TREO = object.getID_CHITIET_TUTI();
                                            }
                                            if (object.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[0]) && object.getIS_TU().equalsIgnoreCase("false")) {
                                                //TODO nếu là TU của công tơ treo
                                                ID_CHITIET_TUTI_TI_CTO_TREO = object.getID_CHITIET_TUTI();
                                            }
                                        }
                                    }


                                    //anh cong to treo
                                    Cursor cursorGetDataAnhCTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                                    if (cursorGetDataAnhCTo.moveToFirst()) {
                                        TEN_ANH_CTO_TREO = cursorGetDataAnhCTo.getString(cursorGetDataAnhCTo.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CTO_TREO;
                                        ANH_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_CTO_TREO = cursorGetDataAnhCTo.getString(cursorGetDataAnhCTo.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhCTo.close();
                                    }

                                    //anh niemPhong cong to treo
                                    Cursor cursorGetDataAnhNiemPhongCTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
                                    if (cursorGetDataAnhNiemPhongCTo.moveToFirst()) {
                                        TEN_ANH_NIEMPHONG_CTO_TREO = cursorGetDataAnhNiemPhongCTo.getString(cursorGetDataAnhNiemPhongCTo.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_NIEMPHONG_CTO_TREO;
                                        ANH_NIEMPHONG_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_NIEMPHONG_CTO_TREO = cursorGetDataAnhNiemPhongCTo.getString(cursorGetDataAnhNiemPhongCTo.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhNiemPhongCTo.close();
                                    }

                                    if (lstChooseTreoToSubmit.get(i).getTthtBBanTuTiEntity() != null) {
                                        //anh TU
                                        Cursor cursorGetDataAnhTu = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                                        if (cursorGetDataAnhTu.moveToFirst()) {
                                            TEN_ANH_TU_CTO_TREO = cursorGetDataAnhTu.getString(cursorGetDataAnhTu.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_CTO_TREO;
                                            ANH_TU_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_TU_CTO_TREO = cursorGetDataAnhTu.getString(cursorGetDataAnhTu.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhTu.close();
                                        }
                                        //anh TU mach nhi thu
                                        Cursor cursorGetDataAnhNhiThu = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                                        if (cursorGetDataAnhNhiThu.moveToFirst()) {
                                            TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = cursorGetDataAnhNhiThu.getString(cursorGetDataAnhNhiThu.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO;
                                            ANH_TU_ANH_MACH_NHI_THU_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO = cursorGetDataAnhNhiThu.getString(cursorGetDataAnhNhiThu.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhNhiThu.close();
                                        }
                                        //anh TU mach niem phong
                                        Cursor cursorGetDataAnhNiemPhong = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                                        if (cursorGetDataAnhNiemPhong.moveToFirst()) {
                                            TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = cursorGetDataAnhNiemPhong.getString(cursorGetDataAnhNiemPhong.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO;
                                            ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO = cursorGetDataAnhNiemPhong.getString(cursorGetDataAnhNiemPhong.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhNiemPhong.close();
                                        }


                                        //anh TI
                                        Cursor cursorGetDataAnhTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                                        if (cursorGetDataAnhTi.moveToFirst()) {
                                            TEN_ANH_TI_CTO_TREO = cursorGetDataAnhTi.getString(cursorGetDataAnhTi.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_CTO_TREO;
                                            ANH_TI_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_TI_CTO_TREO = cursorGetDataAnhTi.getString(cursorGetDataAnhTi.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhTi.close();
                                        }
                                        //anh TI mach nhi thu
                                        Cursor cursorGetDataAnhNhiThuTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
                                        if (cursorGetDataAnhNhiThuTi.moveToFirst()) {
                                            TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = cursorGetDataAnhNhiThuTi.getString(cursorGetDataAnhNhiThuTi.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO;
                                            ANH_TI_ANH_MACH_NHI_THU_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO = cursorGetDataAnhNhiThuTi.getString(cursorGetDataAnhNhiThuTi.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhNhiThuTi.close();
                                        }
                                        //anh TI mach niem phong
                                        Cursor cursorGetDataAnhNiemPhongTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO_TREO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                                        if (cursorGetDataAnhNiemPhongTi.moveToFirst()) {
                                            TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = cursorGetDataAnhNiemPhongTi.getString(cursorGetDataAnhNiemPhongTi.getColumnIndex("TEN_ANH"));
                                            PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO;
                                            ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                            CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO = cursorGetDataAnhNiemPhongTi.getString(cursorGetDataAnhNiemPhongTi.getColumnIndex("CREATE_DAY"));
                                            cursorGetDataAnhNiemPhongTi.close();
                                        }

                                    }

                                    //TODO anh Cong To Thao

                                    ID_CHITIET_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getID_CHITIET_CTO();
                                    ID_BBAN_TUTI_CTO_THAO = lstChooseThaoToSubmit.get(i).getTthtCtoEntity().getID_BBAN_TUTI();
                                    if (ID_BBAN_TUTI_CTO_THAO != 0) {
                                        ArrayList<TthtTuTiEntity> tuTiEntityArrayList = lstChooseThaoToSubmit.get(i).getTthtTuTiEntity();
                                        for (TthtTuTiEntity object : tuTiEntityArrayList) {
                                            if (object.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[1]) && object.getIS_TU().equalsIgnoreCase("true")) {
                                                //TODO nếu là TU của công tơ treo
                                                ID_CHITIET_TUTI_TU_CTO_THAO = object.getID_CHITIET_TUTI();
                                            }
                                            if (object.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[1]) && object.getIS_TU().equalsIgnoreCase("false")) {
                                                //TODO nếu là TU của công tơ treo
                                                ID_CHITIET_TUTI_TI_CTO_THAO = object.getID_CHITIET_TUTI();
                                            }
                                        }
                                    }

                                    //anh cong to
                                    cursorGetDataAnhCTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO_THAO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                                    if (cursorGetDataAnhCTo.moveToFirst()) {
                                        TEN_ANH_CTO_THAO = cursorGetDataAnhCTo.getString(cursorGetDataAnhCTo.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CTO_THAO;
                                        ANH_CTO_THAO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_CTO_THAO = cursorGetDataAnhCTo.getString(cursorGetDataAnhCTo.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhCTo.close();
                                    }

                                    //anh niemPhong cong to treo
                                    cursorGetDataAnhNiemPhongCTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO_THAO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
                                    if (cursorGetDataAnhNiemPhongCTo.moveToFirst()) {
                                        TEN_ANH_NIEMPHONG_CTO_THAO = cursorGetDataAnhNiemPhongCTo.getString(cursorGetDataAnhNiemPhongCTo.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_NIEMPHONG_CTO_THAO;
                                        ANH_NIEMPHONG_CTO_THAO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_NIEMPHONG_CTO_THAO = cursorGetDataAnhNiemPhongCTo.getString(cursorGetDataAnhNiemPhongCTo.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhNiemPhongCTo.close();
                                    }


                                    //anh TU
                                    Cursor cursorGetDataAnhTu = connection.getDataAnhByIDCto(ID_CHITIET_CTO_THAO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                                    if (cursorGetDataAnhTu.moveToFirst()) {
                                        TEN_ANH_TU_CTO_THAO = cursorGetDataAnhTu.getString(cursorGetDataAnhTu.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_CTO_THAO;
                                        ANH_TU_CTO_THAO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_TU_CTO_THAO = cursorGetDataAnhTu.getString(cursorGetDataAnhTu.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhTu.close();
                                    }
                                    //anh TU mach nhi thu
                                    Cursor cursorGetDataAnhNhiThu = connection.getDataAnhByIDCto(ID_CHITIET_CTO_THAO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                                    if (cursorGetDataAnhNhiThu.moveToFirst()) {
                                        TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO = cursorGetDataAnhNhiThu.getString(cursorGetDataAnhNhiThu.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO;
                                        ANH_TU_ANH_MACH_NHI_THU_CTO_THAO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO = cursorGetDataAnhNhiThu.getString(cursorGetDataAnhNhiThu.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhNhiThu.close();
                                    }

                                    //anh TU mach niem phong
                                    Cursor cursorGetDataAnhNiemPhong = connection.getDataAnhByIDCto(ID_CHITIET_CTO_THAO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                                    if (cursorGetDataAnhNiemPhong.moveToFirst()) {
                                        TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = cursorGetDataAnhNiemPhong.getString(cursorGetDataAnhNiemPhong.getColumnIndex("TEN_ANH"));
                                        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO;
                                        ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = (TthtCommon.convertBitmapToByte64(PATH_ANH).isEmpty()) ? "" : TthtCommon.convertBitmapToByte64(PATH_ANH);
                                        CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO = cursorGetDataAnhNiemPhong.getString(cursorGetDataAnhNiemPhong.getColumnIndex("CREATE_DAY"));
                                        cursorGetDataAnhNiemPhong.close();
                                    }


                                    jsonArr.put(
                                            DATAtoJSONTTBBanWithImage(
                                                    //TODO bien ban
                                                    ID_BBAN_CONGTO,
                                                    MA_DVIQLY,
                                                    SO_BBAN,
                                                    TRANG_THAI,

                                                    //TODO cong to treo
                                                    LAN_CTO_TREO,
                                                    VTRI_TREO_THAO_CTO_TREO,
                                                    SOVIEN_CBOOC_CTO_TREO,
                                                    LOAI_HOM_CTO_TREO,
                                                    SOVIEN_CHOM_CTO_TREO,
                                                    HS_NHAN_SAULAP_TUTI_CTO_TREO,
                                                    SO_TU_SAULAP_TUTI_CTO_TREO,
                                                    SO_TI_SAULAP_TUTI_CTO_TREO,
                                                    CHI_SO_SAULAP_TUTI_CTO_TREO,
                                                    CAP_CX_SAULAP_TUTI_CTO_TREO,
                                                    TEM_CQUANG_CTO_TREO,
                                                    SOVIEN_CHIKDINH_CTO_TREO,
                                                    DIEN_AP_SAULAP_TUTI_CTO_TREO,
                                                    DONG_DIEN_SAULAP_TUTI_CTO_TREO,
                                                    HANGSO_K_SAULAP_TUTI_CTO_TREO,
                                                    SO_KIM_NIEM_CHI_CTO_TREO,
                                                    TTRANG_NPHONG_CTO_TREO,
                                                    TEN_LOAI_CTO_CTO_TREO,
                                                    PHUONG_THUC_DO_XA_CTO_TREO,
                                                    GHI_CHU_CTO_TREO,

                                                    //TODO cong to thao
                                                    LAN_CTO_THAO,
                                                    VTRI_TREO_THAO_CTO_THAO,
                                                    SOVIEN_CBOOC_CTO_THAO,
                                                    LOAI_HOM_CTO_THAO,
                                                    SOVIEN_CHOM_CTO_THAO,
                                                    HS_NHAN_SAULAP_TUTI_CTO_THAO,
                                                    SO_TU_SAULAP_TUTI_CTO_THAO,
                                                    SO_TI_SAULAP_TUTI_CTO_THAO,
                                                    CHI_SO_SAULAP_TUTI_CTO_THAO,
                                                    CAP_CX_SAULAP_TUTI_CTO_THAO,
                                                    TEM_CQUANG_CTO_THAO,
                                                    SOVIEN_CHIKDINH_CTO_THAO,
                                                    DIEN_AP_SAULAP_TUTI_CTO_THAO,
                                                    DONG_DIEN_SAULAP_TUTI_CTO_THAO,
                                                    HANGSO_K_SAULAP_TUTI_CTO_THAO,
                                                    SO_KIM_NIEM_CHI_CTO_THAO,
                                                    TTRANG_NPHONG_CTO_THAO,
                                                    TEN_LOAI_CTO_CTO_THAO,
                                                    PHUONG_THUC_DO_XA_CTO_THAO,
                                                    GHI_CHU_CTO_THAO,

                                                    //TODO update bang DU_LIEU_HIEN_TRUONG
                                                    //TODO anh Cong To Treo
                                                    ID_BBAN_TUTI_CTO_TREO,
                                                    ID_CHITIET_TUTI_TU_CTO_TREO,
                                                    ID_CHITIET_TUTI_TI_CTO_TREO,

                                                    ID_CHITIET_CTO_TREO,
                                                    //anh cong to
                                                    TEN_ANH_CTO_TREO,

                                                    //anh
                                                    TEN_ANH_NIEMPHONG_CTO_TREO,

                                                    ANH_CTO_TREO,

                                                    ANH_NIEMPHONG_CTO_TREO,

                                                    CREATE_DAY_ANH_CTO_TREO,

                                                    CREATE_DAY_ANH_NIEMPHONG_CTO_TREO,
                                                    //anh TU
                                                    TEN_ANH_TU_CTO_TREO,
                                                    ANH_TU_CTO_TREO,
                                                    CREATE_DAY_ANH_TU_CTO_TREO,
                                                    //anh TU mach nhi thu
                                                    TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_TREO,
                                                    ANH_TU_ANH_MACH_NHI_THU_CTO_TREO,
                                                    CREATE_DAY_ANH_MACH_NHI_THU_CTO_TREO,
                                                    //anh TU mach niem phong
                                                    TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO,
                                                    ANH_TU_ANH_MACH_NIEM_PHONG_CTO_TREO,
                                                    CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_TREO,

                                                    //anh TI
                                                    TEN_ANH_TI_CTO_TREO,
                                                    ANH_TI_CTO_TREO,
                                                    CREATE_DAY_ANH_TI_CTO_TREO,
                                                    //anh TI mach nhi thu
                                                    TEN_ANH_TI_ANH_MACH_NHI_THU_CTO_TREO,
                                                    ANH_TI_ANH_MACH_NHI_THU_CTO_TREO,
                                                    CREATE_DAY_ANH_MACH_NHI_THU_TI_CTO_TREO,
                                                    //anh TI mach niem phong
                                                    TEN_ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO,
                                                    ANH_TI_ANH_MACH_NIEM_PHONG_CTO_TREO,
                                                    CREATE_DAY_ANH_MACH_NIEM_PHONG_TI_CTO_TREO,

                                                    //TODO anh Cong To Thao
                                                    ID_BBAN_TUTI_CTO_THAO,
                                                    ID_CHITIET_TUTI_TU_CTO_THAO,
                                                    ID_CHITIET_TUTI_TI_CTO_THAO,

                                                    ID_CHITIET_CTO_THAO,
                                                    //anh cong to
                                                    TEN_ANH_CTO_THAO,
                                                    //anh
                                                    TEN_ANH_NIEMPHONG_CTO_THAO,

                                                    ANH_CTO_THAO,

                                                    ANH_NIEMPHONG_CTO_THAO,

                                                    CREATE_DAY_ANH_CTO_THAO,

                                                    CREATE_DAY_ANH_NIEMPHONG_CTO_THAO,

                                                    //anh TU
                                                    TEN_ANH_TU_CTO_THAO,
                                                    ANH_TU_CTO_THAO,
                                                    CREATE_DAY_ANH_TU_CTO_THAO,
                                                    //anh TU mach nhi thu
                                                    TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO,
                                                    ANH_TU_ANH_MACH_NHI_THU_CTO_THAO,
                                                    CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO,
                                                    //anh TU mach niem phong
                                                    TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO,
                                                    ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO,
                                                    CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO
                                            )
                                    );
                                }

                                String maNVien = TthtCommon.getMaNvien();

                                if (jsonArr.length() > 0) {
                                    ArrayList<JSONObject> resultCTo = asyncCallWSApi.WS_UPDATE_ALL_CONG_TO_CALL(jsonArr);
                                    if (resultCTo != null && resultCTo.size() > 0) {
                                        for (int i = 0; i < resultCTo.size(); i++) {
                                            countFileSent++;
                                            JSONObject jObject = resultCTo.get(i);
                                            if (jObject.getString("RESULT").equals("OK")) {
                                                logUpload = true;
                                                countBBan++;

                                                //TODO update to 2 table BBAN_CONGTO(TRANG_THAI) and DETAIL_CONGTO(TRANG_THAI_DU_LIEU)
                                                //object json return jObject.getInt("ID") là  ID_BBAN_CTO
//                                                connection.updateTinhTrangBBan(jObject.getInt("ID"), 2);
                                                Cursor cursorBBanCto = connection.getAllBBanTThao(jObject.getInt("ID"));
                                                int ID_BBAN_TRTH = (cursorBBanCto == null) ? 0 : cursorBBanCto.getInt(cursorBBanCto.getColumnIndex("ID_BBAN_TRTH"));
                                                if (ID_BBAN_TRTH != 0) {
                                                    long rowUpdateTreo = connection.updateCongToWhenSubmit(ID_BBAN_TRTH, TthtCommon.arrMaBDong[0], TthtCommon.TRANG_THAI_DU_LIEU.DA_GUI.toString());
                                                    long rowUpdateThao = connection.updateCongToWhenSubmit(ID_BBAN_TRTH, TthtCommon.arrMaBDong[1], TthtCommon.TRANG_THAI_DU_LIEU.DA_GUI.toString());
                                                }
                                            }

                                            progressBarStatus = (int) (countFileSent / snapProgresBar);
                                            progressBarHandler.post(new Runnable() {
                                                public void run() {
                                                    progressDialog.setProgress(progressBarStatus);
                                                }
                                            });
                                        }

                                    } else {
                                        logUpload = false;
                                        countBBan = -1;
                                    }
                                } else {
                                    countBBan = -1;
                                }

                                if (logUpload) {
                                    TthtEntityLogHistory logHistory = new TthtEntityLogHistory(maNVien, "2");
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("ID_NHANVIEN", logHistory.getID_NHANVIEN());
                                        jsonObject.put("ACTION", logHistory.getACTION());
                                        JSONObject jsonObjectRsult = asyncCallWSApi.WS_REQUEST_LOG_CALL(jsonObject);

                                        if (jsonObjectRsult.getString("RESULT").equals("OK")) {
                                            Log.d("log", "Ghi log upload thành công!");
                                        } else {
                                            Log.d("log", "Lỗi ghi log upload!");
                                        }
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            } catch (Exception e) {
                                Log.e(TAG, "run: " + e.getMessage());
                            }
                            handlerSend.sendEmptyMessage(0);
                        }
                    }

                    );
                    thread.start();
                }
            }
        } catch (
                Exception e
                )

        {
            Log.e(TAG, "run: " + e.getMessage());
        }
    }


    public JSONObject DATAtoJSONTTBBan(int ID_BBAN_CONGTO, int TRANG_THAI, String
            GHI_CHU, String ID_CHITIET_CTO, String CHI_SO,
                                       int SO_VIENCHOM, String MA_SOCHOM, String ID_CONGTO_THAO, String TTRANG_NPHONG,
                                       String CHI_SO_THAO, int SOVIEN_CHIHOP_THAO, int SO_KIM_NIEM_CHI, String
                                               MA_CHIHOP_THAO,
                                       String MA_CHIKDINH, int SOVIEN_CHIKDINH, String MA_SOCHOM_THAO, int SO_VIENCHOM_THAO) {
        String sID_BBAN_CONGTO = "ID_BBAN_CONGTO";
        String sTRANG_THAI = "TRANG_THAI";
        String sGHI_CHU = "GHI_CHU";

        String sID_CONGTO_TREO = "ID_CONGTO_TREO";
        String sCHI_SO_TREO = "CHI_SO_TREO";
        String sSOVIEN_CHIHOP_TREO = "SOVIEN_CHIHOP_TREO";
        String sMA_CHIHOP_TREO = "MA_CHIHOP_TREO";

        String sID_CONGTO_THAO = "ID_CONGTO_THAO";
        String sTTRANG_NPHONG = "TTRANG_NPHONG";
        String sCHI_SO_THAO = "CHI_SO_THAO";
        String sSOVIEN_CHIHOP_THAO = "SOVIEN_CHIHOP_THAO";
        String sSO_KIM_NIEM_CHI = "SO_KIM_NIEM_CHI";
        String sMA_CHIHOP_THAO = "MA_CHIHOP_THAO";
        String sMA_CHIKDINH = "MA_CHIKDINH";
        String sSOVIEN_CHIKDINH = "SOVIEN_CHIKDINH";
        String sMA_SOCHOM_THAO = "MA_SOCHOM_THAO";
        String sSO_VIENCHOM_THAO = "SO_VIENCHOM_THAO";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sID_BBAN_CONGTO, ID_BBAN_CONGTO);
            jsonObject.accumulate(sTRANG_THAI, TRANG_THAI);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);

            jsonObject.accumulate(sID_CONGTO_TREO, ID_CHITIET_CTO);
            jsonObject.accumulate(sCHI_SO_TREO, CHI_SO);
            jsonObject.accumulate(sSOVIEN_CHIHOP_TREO, SO_VIENCHOM);
            jsonObject.accumulate(sMA_CHIHOP_TREO, MA_SOCHOM);

            jsonObject.accumulate(sID_CONGTO_THAO, ID_CONGTO_THAO);
            jsonObject.accumulate(sTTRANG_NPHONG, TTRANG_NPHONG);
            jsonObject.accumulate(sCHI_SO_THAO, CHI_SO_THAO);
            jsonObject.accumulate(sSOVIEN_CHIHOP_THAO, SOVIEN_CHIHOP_THAO);
            jsonObject.accumulate(sSO_KIM_NIEM_CHI, SO_KIM_NIEM_CHI);
            jsonObject.accumulate(sMA_CHIHOP_THAO, MA_CHIHOP_THAO);
            jsonObject.accumulate(sMA_CHIKDINH, MA_CHIKDINH);
            jsonObject.accumulate(sSOVIEN_CHIKDINH, SOVIEN_CHIKDINH);
            jsonObject.accumulate(sMA_SOCHOM_THAO, MA_SOCHOM_THAO);
            jsonObject.accumulate(sSO_VIENCHOM_THAO, SO_VIENCHOM_THAO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

//region vinhNB


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
            String CREATE_DAY_ANH_NIEMPHONG_CTO_THAO,
            //anh TU
            String TEN_ANH_TU_CTO_THAO,
            String ANH_TU_CTO_THAO,
            String CREATE_DAY_ANH_TU_CTO_THAO,
            //anh TU mach nhi thu
            String TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO,
            String ANH_TU_ANH_MACH_NHI_THU_CTO_THAO,
            String CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO,
            //anh TU mach niem phong
            String TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO,
            String ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO,
            String CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO
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

        //anh TU
        String sTEN_ANH_TU_CTO_THAO = "TEN_ANH_TU_CTO_THAO";
        String sANH_TU_CTO_THAO = "ANH_TU_CTO_THAO";
        String sCREATE_DAY_ANH_TU_CTO_THAO = "CREATE_DAY_ANH_TU_CTO_THAO";
        //anh TU mach nhi thu
        String sTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO = "TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO";
        String sANH_TU_ANH_MACH_NHI_THU_CTO_THAO = "ANH_TU_ANH_MACH_NHI_THU_CTO_THAO";
        String sCREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO = "CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO";
        //anh TU mach niem phong
        String sTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = "TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO";
        String sANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO = "ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO";
        String sCREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO = "CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO";


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
            jsonObject.accumulate(sID_CHITIET_TUTI_TU_CTO_THAO, ID_CHITIET_TUTI_TU_CTO_THAO);
            jsonObject.accumulate(sID_CHITIET_TUTI_TI_CTO_THAO, ID_CHITIET_TUTI_TI_CTO_THAO);

            jsonObject.accumulate(sID_CHITIET_CTO_THAO, ID_CHITIET_CTO_THAO);
            //anh cong to
            jsonObject.accumulate(sTEN_ANH_CTO_THAO, TEN_ANH_CTO_THAO == null ? "" : TEN_ANH_CTO_THAO);
            jsonObject.accumulate(sTEN_ANH_NIEMPHONG_CTO_THAO, TEN_ANH_NIEMPHONG_CTO_THAO == null ? "" : TEN_ANH_NIEMPHONG_CTO_THAO);

            jsonObject.accumulate(sANH_CTO_THAO, ANH_CTO_THAO == null ? "" : ANH_CTO_THAO);
            jsonObject.accumulate(sANH_NIEMPHONG_CTO_THAO, ANH_NIEMPHONG_CTO_THAO == null ? "" : ANH_NIEMPHONG_CTO_THAO);

            jsonObject.accumulate(sCREATE_DAY_ANH_CTO_THAO, CREATE_DAY_ANH_CTO_THAO == null ? "" : CREATE_DAY_ANH_CTO_THAO);
            jsonObject.accumulate(sCREATE_DAY_ANH_NIEMPHONG_CTO_THAO, CREATE_DAY_ANH_NIEMPHONG_CTO_THAO == null ? "" : CREATE_DAY_ANH_NIEMPHONG_CTO_THAO);
            //anh TU
            jsonObject.accumulate(sTEN_ANH_TU_CTO_THAO, TEN_ANH_TU_CTO_THAO == null ? "" : TEN_ANH_TU_CTO_THAO);
            jsonObject.accumulate(sANH_TU_CTO_THAO, ANH_TU_CTO_THAO == null ? "" : ANH_TU_CTO_THAO);
            jsonObject.accumulate(sCREATE_DAY_ANH_TU_CTO_THAO, CREATE_DAY_ANH_TU_CTO_THAO == null ? "" : CREATE_DAY_ANH_TU_CTO_THAO);
            //anh TU mach nhi thu
            jsonObject.accumulate(sTEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO, TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO == null ? "" : TEN_ANH_TU_ANH_MACH_NHI_THU_CTO_THAO);
            jsonObject.accumulate(sANH_TU_ANH_MACH_NHI_THU_CTO_THAO, ANH_TU_ANH_MACH_NHI_THU_CTO_THAO == null ? "" : ANH_TU_ANH_MACH_NHI_THU_CTO_THAO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO, CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO == null ? "" : CREATE_DAY_ANH_MACH_NHI_THU_CTO_THAO);
            //anh TU mach niem phong
            jsonObject.accumulate(sTEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO, TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO == null ? "" : TEN_ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO);
            jsonObject.accumulate(sANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO, ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO == null ? "" : ANH_TU_ANH_MACH_NIEM_PHONG_CTO_THAO);
            jsonObject.accumulate(sCREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO, CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO == null ? "" : CREATE_DAY_ANH_MACH_NIEM_PHONG_CTO_THAO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    //endRegion
    public JSONObject DATAtoJSONBBan(int ID_BBAN_CONGTO, int TRANG_THAI, String GHI_CHU) {
        String sID_BBAN_CONGTO = "ID_BBAN_CONGTO";
        String sTRANG_THAI = "TRANG_THAI";
        String sGHI_CHU = "GHI_CHU";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sID_BBAN_CONGTO, ID_BBAN_CONGTO);
            jsonObject.accumulate(sTRANG_THAI, TRANG_THAI);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONCTo(int ID_CHITIET_CTO, String CHI_SO, int SOVIEN_CHIHOP,
                                    int SO_KIM_NIEM_CHI,
                                    String MA_CHIHOP, String TTRANG_NPHONG) {
        String sID_CHITIET_CTO = "ID_CHITIET_CTO";
        String sCHI_SO = "CHI_SO";
        String sSOVIEN_CHIHOP = "SOVIEN_CHIHOP";
        String sSO_KIM_NIEM_CHI = "SO_KIM_NIEM_CHI";
        String sMA_CHIHOP = "MA_CHIHOP";
        String sTTRANG_NPHONG = "TTRANG_NPHONG";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sID_CHITIET_CTO, ID_CHITIET_CTO);
            jsonObject.accumulate(sCHI_SO, CHI_SO);
            jsonObject.accumulate(sSOVIEN_CHIHOP, SOVIEN_CHIHOP);
            jsonObject.accumulate(sSO_KIM_NIEM_CHI, SO_KIM_NIEM_CHI);
            jsonObject.accumulate(sMA_CHIHOP, MA_CHIHOP);
            jsonObject.accumulate(sTTRANG_NPHONG, TTRANG_NPHONG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

/*
public String UploadFileHA(String fFileName,int CONGTO_ID,String GHI_CHU,String MA_NVIEN){
        try{
        FileInputStream fstrm=new FileInputStream(Environment.getExternalStorageDirectory().toString()+EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH+fFileName);
        String URL_SERVICE=new StringBuilder("http://").append(TthtCommon.getIP_SERVER_1()).append(TthtCommon.getServerName()).toString();
        TthtSentImage hfu=new TthtSentImage(new StringBuilder(URL_SERVICE).append("Post_mtb_image_htruong").toString());
        return hfu.Send_Now(fstrm,fFileName,CONGTO_ID,GHI_CHU,MA_NVIEN);
        }catch(FileNotFoundException e){
        return e.toString();
        }
        }
*/

    private Handler handlerGet = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isThreadRun = false;
            StringBuilder sbMsg = new StringBuilder();
            if (countBBan > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countBBan).append(" biên bản\n");
                countBBan = 0;
            } else {
                sbMsg.append("Không có biên bản\n");
            }
            if (countCTo > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countCTo).append(" công tơ\n");
                countCTo = 0;
            } else {
                sbMsg.append("Không có công tơ\n");
            }

            if (countTram > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countTram).append(" Trạm\n");
                countTram = 0;
            } else {
                sbMsg.append("Không có Trạm\n");
            }

            if (countLoaiCTo > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countLoaiCTo).append(" nhà cung cấp dịch vụ đo xa.\n");
                countLoaiCTo = 0;
            } else {
                sbMsg.append("Không có danh sách loại công tơ.\n");
            }

            if (countBBanTuTi > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countBBanTuTi).append(" biên bản TU TI.\n");
                countBBanTuTi = 0;
            } else {
                sbMsg.append("Không có danh sách biên bản TU TI.\n");
            }

            if (countChiTietTuTi > 0) {
                sbMsg.append("Đồng bộ thành công ").append(countChiTietTuTi).append(" chi tiết TU TI.\n");
                countChiTietTuTi = 0;
            } else {
                sbMsg.append("Không có danh sách chi tiết TU TI.\n");
            }
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    sbMsg.toString(), Color.WHITE, "OK", Color.WHITE);
            setTitleThongKe(TthtCommon.FILTER_DATA_FILL.ALL);
            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
            lstPhieuTreoThao.clear();
            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
            invalidatReyclerView(MA_BDONG);
//            setDataBBanWithMA_BDONG(TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
//            setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);
            progressDialog.dismiss();
        }
    };

    private Handler handlerSend = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            StringBuilder sbMsg = new StringBuilder();
            if (countBBan != -1) {
                sbMsg.append("Gửi thành công ").append(countBBan).append("/").append(lstChooseThaoToSubmit.size()).append(" biên bản\n");
            } else {
                sbMsg.append("Biên bản đã gửi");
            }
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    sbMsg.toString(), Color.WHITE, "OK", Color.WHITE);

            btDoiSanh.setText(TthtCommon.ARRAY_DOI_SOAT[0]);
            isClickDoiSanh = false;
            setTitleThongKe(TthtCommon.FILTER_DATA_FILL.ALL);
            String MA_BDONG = TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()];
            lstPhieuTreoThao.clear();
            lstPhieuTreoThao.addAll(setDataBBanWithMA_BDONG(MA_BDONG));
            invalidatReyclerView(MA_BDONG);
//            setDataBBanWithMA_BDONG(TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
//            setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);
            progressDialog.dismiss();
        }
    };
//endregion

    //region xử lý class adapterFilter
   /* class TthtKHAdapter extends RecyclerView.Adapter<TthtKHAdapter.RecyclerViewHolder> {

        List<TthtKHangEntity> listData = new ArrayList<>();
        List<TthtKHangEntity> listDataTreo = new ArrayList<>();
        List<TthtKHangEntity> listDataThao = new ArrayList<>();
        private int posSelect = 0;


        public void setPosSelect(int posSelect) {
            this.posSelect = posSelect;
        }

        public int getPosSelect() {
            return posSelect;
        }

        public TthtKHAdapter(List<TthtKHangEntity> dataTreo, List<TthtKHangEntity> dataThao, String MA_BDONG) {
            listDataTreo = dataTreo;
            listDataThao = dataThao;
            if (MA_BDONG.equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                this.listData = dataTreo;
            } else {
                this.listData = dataThao;
            }


        }

        public void animateTo(List<TthtKHangEntity> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<TthtKHangEntity> newModels) {
            for (int i = listData.size() - 1; i >= 0; i--) {
                final TthtKHangEntity model = listData.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<TthtKHangEntity> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final TthtKHangEntity model = newModels.get(i);
                if (!listData.contains(model)) {
                    addItem(i, model);
                }
            }
        }

        private void applyAndAnimateMovedItems(List<TthtKHangEntity> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final TthtKHangEntity model = newModels.get(toPosition);
                final int fromPosition = listData.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public void moveItem(int fromPosition, int toPosition) {
            final TthtKHangEntity model = listData.remove(fromPosition);
            listData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        public int getPosIDBBanTrThao(int idBBTrTh) {
            if (listData.size() == 0) {
                return -1;
            }
            for (int i = 0; i < listData.size(); i++) {
                if (idBBTrTh == listData.get(i).getTthtBBanEntity().getID_BBAN_TRTH()) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.ttht_row_cto, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvSTT.setText(String.valueOf(listData.get(position).getStt()));
            holder.TEN_KH.setText(listData.get(position).getTthtBBanEntity().getTEN_KHANG());
            holder.tvDiaChi.setText(listData.get(position).getTthtBBanEntity().getDCHI_HDON());
            holder.tvSoNo.setText(listData.get(position).getTthtCtoEntity().getSO_CTO());
            holder.tvMaGCS.setText(listData.get(position).getTthtBBanEntity().getMA_GCS_CTO());
            holder.tvChiSo.setText(listData.get(position).getTthtCtoEntity().getCHI_SO());
            holder.tvMaTram.setText(listData.get(position).getTthtBBanEntity().getMA_TRAM());

            if (position == getPosSelect()) {
                holder.itemView.setBackgroundResource(R.drawable.bg_item_blue);
//                holder.itemView.setBackgroundResource(R.color.button_dark_orange);
            } else {
                if (listData.get(position).getTthtCtoEntity().getTRANG_THAI_DU_LIEU() == 2) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_gray);
//                    holder.itemView.setBackgroundResource(R.color.button_disable_dark_orange);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_white);
                }
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public void addItem(int position, TthtKHangEntity data) {
            listData.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvSTT, TEN_KH, tvDiaChi, tvSoNo, tvMaGCS, tvChiSo, tvMaTram;
            public ImageButton ibChiTiet, cbCheck;
            public RelativeLayout rlItem;
            public LinearLayout lnCheck;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvMaTram = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvMaTram);
                tvSTT = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvSTT);
                TEN_KH = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvTenKH);
                tvDiaChi = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvDiaChi);
                tvSoNo = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvSoNo);
                tvMaGCS = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvMaGCS);
                tvChiSo = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvChiSo);
                ibChiTiet = (ImageButton) itemView.findViewById(R.id.ttht_row_cto_ibChiTiet);
                rlItem = (RelativeLayout) itemView.findViewById(R.id.ttht_row_cto_rlItem);
                lnCheck = (LinearLayout) itemView.findViewById(R.id.ttht_row_cto_lnCheck);
                rlItem.setOnClickListener(this);
                ibChiTiet.setOnClickListener(this);
//trừ margin bottom in xml fixed @dimen/margin_recycle_2
//                int heightRvKH = rvKH.getMeasuredHeight() / 3 - 2 - 2 - 2 - 2 - 2 - 2;
//                int heightReal = TEN_KH.getMeasuredHeight() + tvDiaChi.getMeasuredHeight() + tvMaGCS.getMeasuredHeight()+tvSoNo.getMeasuredHeight()+tvChiSo.getMeasuredHeight();
//                if(heightReal<heightRvKH){
//                    rlItem.getLayoutParams().height = heightRvKH;
//                }else {
//                    rlItem.getLayoutParams().height = heightReal + 10;
//                }
            }

            @Override
            public void onClick(View v) {
                try {
                    setPosSelect(getPosition());
                    idBBTrTh = listData.get(getAdapterPosition()).getTthtBBanEntity().getID_BBAN_TRTH();
                    notifyDataSetChanged();


                    if (v.getId() == R.id.ttht_row_cto_ibChiTiet) {
//                        showPopupMenu(v);
                    } else if (v.getId() == R.id.ttht_row_cto_rlItem) {
                        //TODO start new activity
                        idBBTrTh = adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getID_BBAN_TRTH();
                        TthtKHangEntity tthtKHangEntityTreo = listDataTreo.get(getPosSelect());
                        TthtKHangEntity tthtKHangEntityThao = listDataThao.get(getPosSelect());

                        Intent intent = new Intent(getActivity(), TthtDetailTaskActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList("LIST_DATA_TREO", (ArrayList<? extends Parcelable>) lstKhangHangTreo);
                        bundle.putParcelableArrayList("LIST_DATA_THAO", (ArrayList<? extends Parcelable>) lstKhangHangThao);
                        bundle.putParcelable("OBJECT_DATA_TREO", tthtKHangEntityTreo);
                        bundle.putParcelable("OBJECT_DATA_THAO", tthtKHangEntityThao);
                        bundle.putInt("ID_BBAN_TRTH", idBBTrTh);
                        bundle.putBoolean("IS_DOI_SOAT", false);
                        bundle.putString("MA_BDONG", TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent, TthtMainActivity.REQUEST_CODE_TO_DETAIL_ACTIVITY);
//                        showDialogWrite();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           *//* TthtSelectedBBanEntity entityBBan = new TthtSelectedBBanEntity();
            TthtSelectedCToEntity entityCto = new TthtSelectedCToEntity();
            entityCto.setMA_CTO(listData.get(getPosition()).getTthtCtoEntity().getMA_CTO());
            entityCto.setPOS(getPosition());
            entityBBan.setID_BBAN_CONGTO(listData.get(getPosition()).getTthtBBanEntity().getId_BBAN_CONGTO());
            entityBBan.setPOS(getPosition());
            if (!lstSelected.contains(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO())))
                lstSelected.add(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO()));
            else
                lstSelected.remove(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO()));
            if (isChecked) {
                if (!lstSelectedBBan.contains(entityBBan)) {
                    lstSelectedBBan.add(entityBBan);
                }
                if (!lstSelectedCTo.contains(entityCto)) {
                    lstSelectedCTo.add(entityCto);
                }
            } else {
                if (lstSelectedBBan.contains(entityBBan)) {
                    lstSelectedBBan.remove(entityBBan);
                }
                if (lstSelectedCTo.contains(entityCto)) {
                    lstSelectedCTo.remove(entityCto);
                }
            }*//*
                notifyDataSetChanged();
            }
        }


    }
*/
    class TthtInfoPhieuTreoThaoAdapter extends RecyclerView.Adapter<TthtInfoPhieuTreoThaoAdapter.RecyclerViewHolder> {

        private String MA_BDONG;
        private List<InfoPhieuTreoThao> listData = new ArrayList<>();
        private int posSelect = 0;


        public void setPosSelect(int posSelect) {
            this.posSelect = posSelect;
        }

        public int getPosSelect() {
            return posSelect;
        }

        public TthtInfoPhieuTreoThaoAdapter(List<InfoPhieuTreoThao> dataTreoThao, String MA_BDONG) {
            if (dataTreoThao == null)
                return;
            listData = new ArrayList<>();
            listData.addAll(dataTreoThao);
        }

        //
        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.ttht_row_cto, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvSTT.setText(position + 1 + "");
            holder.tvTenKH.setText(listData.get(position).getTEN_KHANG());
            holder.tvDiaChi.setText(listData.get(position).getDCHI_HDON());
            holder.tvSoNo.setText(listData.get(position).getMA_CTO());
            holder.tvMaGCS.setText(listData.get(position).getMA_GCS_CTO());
            holder.tvChiSo.setText(listData.get(position).getCHI_SO());
            holder.tvMaTram.setText(listData.get(position).getMA_TRAM());

            if (listData.get(position).getTRANG_THAI_DU_LIEU() == 0)
                holder.itemView.setBackgroundResource(R.drawable.bg_item_white);
            if (listData.get(position).getTRANG_THAI_DU_LIEU() == 1)
                holder.itemView.setBackgroundResource(R.drawable.bg_item_gray);
            if (listData.get(position).getTRANG_THAI_DU_LIEU() == 2)
                holder.itemView.setBackgroundResource(R.drawable.bg_item_blue);

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public void updateList(List<InfoPhieuTreoThao> data, String ma_bdong) {

            listData = data;
            MA_BDONG = ma_bdong;
//            //TODO reset stt
//            for (int i = 0; i < listData.size(); i++) {
//                listData.get(i).setStt(i + 1);
//
//            }
            notifyDataSetChanged();
        }

        public void refreshData(List<InfoPhieuTreoThao> lstPhieuTreoThao, String ma_bdong) {
            listData.clear();
            listData.addAll(lstPhieuTreoThao);
            MA_BDONG = ma_bdong;
            notifyDataSetChanged();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvSTT, tvTenKH, tvDiaChi, tvSoNo, tvMaGCS, tvChiSo, tvMaTram;
            public ImageButton ibChiTiet, cbCheck;
            public RelativeLayout rlItem;
            public LinearLayout lnCheck;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvMaTram = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvMaTram);
                tvSTT = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvSTT);
                tvTenKH = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvTenKH);
                tvDiaChi = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvDiaChi);
                tvSoNo = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvSoNo);
                tvMaGCS = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvMaGCS);
                tvChiSo = (TextView) itemView.findViewById(R.id.ttht_row_cto_tvChiSo);
                ibChiTiet = (ImageButton) itemView.findViewById(R.id.ttht_row_cto_ibChiTiet);
                rlItem = (RelativeLayout) itemView.findViewById(R.id.ttht_row_cto_rlItem);
                lnCheck = (LinearLayout) itemView.findViewById(R.id.ttht_row_cto_lnCheck);
                rlItem.setOnClickListener(this);
                ibChiTiet.setOnClickListener(this);
//trừ margin bottom in xml fixed @dimen/margin_recycle_2
//                int heightRvKH = rvKH.getMeasuredHeight() / 3 - 2 - 2 - 2 - 2 - 2 - 2;
//                int heightReal = TEN_KH.getMeasuredHeight() + tvDiaChi.getMeasuredHeight() + tvMaGCS.getMeasuredHeight()+tvSoNo.getMeasuredHeight()+tvChiSo.getMeasuredHeight();
//                if(heightReal<heightRvKH){
//                    rlItem.getLayoutParams().height = heightRvKH;
//                }else {
//                    rlItem.getLayoutParams().height = heightReal + 10;
//                }
            }

            @Override
            public void onClick(View v) {
                try {
                    if (v.getId() == R.id.ttht_row_cto_ibChiTiet) {
//                        showPopupMenu(v);
                    } else if (v.getId() == R.id.ttht_row_cto_rlItem) {
                        int pos = getAdapterPosition();
                        final Intent intent = new Intent(getActivity(), TthtDetailTaskActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID_BBAN_TRTH", listData.get(pos).getID_BBAN_TRTH());
                        bundle.putInt("ID_CHITIET_CTO", listData.get(pos).getID_CHITIET_CTO());
                        bundle.putString("MA_BDONG", TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                        bundle.putInt("POSITION", pos);
                        TthtCommon.setListPhieu(listData);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                       /* //TODO start new activity
                        idBBTrTh = adapterKH.listData.get(adapterKH.getPosSelect()).getTthtCtoEntity().getID_BBAN_TRTH();
                        TthtKHangEntity tthtKHangEntityTreo = listDataTreo.get(getPosSelect());
                        TthtKHangEntity tthtKHangEntityThao = listDataThao.get(getPosSelect());

                        Intent intent = new Intent(getActivity(), TthtDetailTaskActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList("LIST_DATA_TREO", (ArrayList<? extends Parcelable>) lstKhangHangTreo);
                        bundle.putParcelableArrayList("LIST_DATA_THAO", (ArrayList<? extends Parcelable>) lstKhangHangThao);
                        bundle.putParcelable("OBJECT_DATA_TREO", tthtKHangEntityTreo);
                        bundle.putParcelable("OBJECT_DATA_THAO", tthtKHangEntityThao);
                        bundle.putInt("ID_BBAN_TRTH", idBBTrTh);
                        bundle.putBoolean("IS_DOI_SOAT", false);
                        bundle.putString("MA_BDONG", TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent, TthtMainActivity.REQUEST_CODE_TO_DETAIL_ACTIVITY);*/
//                        showDialogWrite();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           /* TthtSelectedBBanEntity entityBBan = new TthtSelectedBBanEntity();
            TthtSelectedCToEntity entityCto = new TthtSelectedCToEntity();
            entityCto.setMA_CTO(listData.get(getPosition()).getTthtCtoEntity().getMA_CTO());
            entityCto.setPOS(getPosition());
            entityBBan.setID_BBAN_CONGTO(listData.get(getPosition()).getTthtBBanEntity().getId_BBAN_CONGTO());
            entityBBan.setPOS(getPosition());
            if (!lstSelected.contains(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO())))
                lstSelected.add(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO()));
            else
                lstSelected.remove(String.valueOf(listData.get(getPosition()).getTthtCtoEntity().getID_CHITIET_CTO()));
            if (isChecked) {
                if (!lstSelectedBBan.contains(entityBBan)) {
                    lstSelectedBBan.add(entityBBan);
                }
                if (!lstSelectedCTo.contains(entityCto)) {
                    lstSelectedCTo.add(entityCto);
                }
            } else {
                if (lstSelectedBBan.contains(entityBBan)) {
                    lstSelectedBBan.remove(entityBBan);
                }
                if (lstSelectedCTo.contains(entityCto)) {
                    lstSelectedCTo.remove(entityCto);
                }
            }*/
                notifyDataSetChanged();
            }
        }


    }

//endregion

    //region xử lý class Doi soat
    class TthtDoiSoatAdapter extends RecyclerView.Adapter<TthtDoiSoatAdapter.RecyclerViewHolder> {

        private List<TthtKHangEntity> lstDSoatTreo = new ArrayList<>();
        private List<TthtKHangEntity> lstDSoatThao = new ArrayList<>();
        ;
        private TthtTuTiEntity tuTreoCongToTreo, tiTreoCongToTreo, tuThaoCongToThao, tiThaoCongToThao;
        private int posSelect = 0;

        public TthtDoiSoatAdapter(List<TthtKHangEntity> lstDSoatTreo, List<TthtKHangEntity> lstDSoatThao) {
            this.lstDSoatTreo = lstDSoatTreo;
            this.lstDSoatThao = lstDSoatThao;

        }

        public void setPosSelect(int posSelect) {
            this.posSelect = posSelect;
        }


        public int getPosSelect() {
            return posSelect;
        }

        public void updateList(List<TthtKHangEntity> lstDSoatTreo, List<TthtKHangEntity> lstDSoatThao) {
            this.lstDSoatTreo.clear();
            this.lstDSoatThao.clear();

            this.lstDSoatTreo = lstDSoatTreo;
            this.lstDSoatThao = lstDSoatThao;

            notifyDataSetChanged();
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.ttht_row_doi_soat, parent, false);
            return new RecyclerViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvStt.setText(position + 1 + "");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = null;
            Bitmap defaultBitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.image_not_found);
            String pathAnh = "";

            //TODO setVisible linearlayout
            //TODO get data
            for (TthtTuTiEntity objectTreo : lstDSoatTreo.get(position).getTthtTuTiEntity()) {
                if (objectTreo.getIS_TU().equalsIgnoreCase("true")) {
//                    if (objectTreo.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                    tuTreoCongToTreo = objectTreo;
//                    } else tuThao = objectTreo;
                } else {
//                    if (objectTreo.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
                    tiTreoCongToTreo = objectTreo;
//                    } else tiThao = objectTreo;
                }
            }

            for (TthtTuTiEntity objectThao : lstDSoatThao.get(position).getTthtTuTiEntity()) {
                if (objectThao.getIS_TU().equalsIgnoreCase("true")) {
//                    if (objectTreo.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                    tuThaoCongToThao = objectThao;
//                    } else tuThao = objectTreo;
                } else {
//                    if (objectTreo.getMA_BDONG().equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
                    tiThaoCongToThao = objectThao;
//                    } else tiThao = objectTreo;
                }
            }

            if (tuTreoCongToTreo != null) {
                holder.llTuTreoCongToTreo.setVisibility(View.VISIBLE);
                //TODO tv Tu treo
                holder.tvSoTu_CToTreo.setText(tuTreoCongToTreo.getSO_TU_TI());
                //TODO iv Tu treo
                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuTreoCongToTreo.getTEN_ANH_TU_TI();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivTu_CToTreo_Tu.setImageBitmap(bitmap);
                } else {
                    holder.ivTu_CToTreo_Tu.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuTreoCongToTreo.getTEN_ANH_MACH_NHI_THU();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNhiThu_Tu_CToTreo.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNhiThu_Tu_CToTreo.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuTreoCongToTreo.getTEN_ANH_MACH_CONG_TO();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNiemPhong_Tu_CToTreo.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNiemPhong_Tu_CToTreo.setImageBitmap(defaultBitmap);
                }
            } else {
                holder.llTuTreoCongToTreo.setVisibility(View.GONE);
            }

            if (tiTreoCongToTreo != null) {
                holder.llTiTreoCongToTreo.setVisibility(View.VISIBLE);
                //TODO tv Ti Treo
                holder.tvSoTu_CToTreo.setText(tiTreoCongToTreo.getSO_TU_TI());
                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiTreoCongToTreo.getTEN_ANH_TU_TI();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivTi_CToTreo_Ti.setImageBitmap(bitmap);
                } else {
                    holder.ivTi_CToTreo_Ti.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiTreoCongToTreo.getTEN_ANH_MACH_NHI_THU();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNhiThu_Ti_CToTreo.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNhiThu_Ti_CToTreo.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiTreoCongToTreo.getTEN_ANH_MACH_CONG_TO();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNiemPhong_Ti_CToTreo.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNiemPhong_Ti_CToTreo.setImageBitmap(defaultBitmap);
                }
            } else {
                holder.llTiTreoCongToTreo.setVisibility(View.GONE);
            }

            if (tuThaoCongToThao != null) {
                holder.llTuThaoCongToThao.setVisibility(View.VISIBLE);
                //TODO tv Ti Treo
                holder.tvSoTu_CToThao.setText(tuThaoCongToThao.getSO_TU_TI());

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuThaoCongToThao.getTEN_ANH_TU_TI();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivTu_CToThao_Tu.setImageBitmap(bitmap);
                } else {
                    holder.ivTu_CToThao_Tu.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuThaoCongToThao.getTEN_ANH_MACH_NHI_THU();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNhiThu_Tu_CToThao.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNhiThu_Tu_CToThao.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tuThaoCongToThao.getTEN_ANH_MACH_CONG_TO();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNiemPhong_Tu_CToThao.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNiemPhong_Tu_CToThao.setImageBitmap(defaultBitmap);
                }
            } else {
                holder.llTuThaoCongToThao.setVisibility(View.GONE);
            }

            if (tiThaoCongToThao != null) {
                holder.llTiThaoCongToThao.setVisibility(View.VISIBLE);
                //TODO tv Ti Treo
                holder.tvSoTu_CToThao.setText(tiThaoCongToThao.getSO_TU_TI());

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiThaoCongToThao.getTEN_ANH_TU_TI();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivTi_CToThao_Ti.setImageBitmap(bitmap);
                } else {
                    holder.ivTi_CToThao_Ti.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiThaoCongToThao.getTEN_ANH_MACH_NHI_THU();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNhiThu_Ti_CToThao.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNhiThu_Ti_CToThao.setImageBitmap(defaultBitmap);
                }

                pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tiThaoCongToThao.getTEN_ANH_MACH_CONG_TO();
                bitmap = BitmapFactory.decodeFile(pathAnh, options);
                if (bitmap != null) {
                    holder.ivMachNiemPhong_Ti_CToThao.setImageBitmap(bitmap);
                } else {
                    holder.ivMachNiemPhong_Ti_CToThao.setImageBitmap(defaultBitmap);
                }
            } else {
                holder.llTiThaoCongToThao.setVisibility(View.GONE);
            }

            //TODO set tv chung
            holder.ibDSChoose.setBackgroundResource(R.mipmap.essp_uncheck);

            holder.tvStt.setText(String.valueOf(lstDSoatTreo.get(position).getStt()));
            holder.tvKH.setText(lstDSoatTreo.get(position).getTthtBBanEntity().getTEN_KHANG());
            holder.tvDiaChi.setText(lstDSoatTreo.get(position).getTthtBBanEntity().getDCHI_HDON());

            //TODO tv Cong Tơ treo
            holder.tvSoCto_CToTreo.setText(lstDSoatTreo.get(position).getTthtCtoEntity().getSO_CTO());
            String CHI_SO = lstDSoatTreo.get(position).getTthtCtoEntity().getCHI_SO();
            String LOAI_CTO = lstDSoatTreo.get(position).getTthtCtoEntity().getLOAI_CTO();
            if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.D1.name())) {
                holder.tvTitleCS1_CToTreo.setVisibility(View.VISIBLE);
                holder.tvTitleCS2_CToTreo.setVisibility(View.VISIBLE);
                holder.tvTitleCS3_CToTreo.setVisibility(View.VISIBLE);
                holder.tvTitleCS4_CToTreo.setVisibility(View.VISIBLE);
                holder.tvTitleCS5_CToTreo.setVisibility(View.VISIBLE);

                holder.tvCS1_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS2_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS3_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS4_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS5_CToTreo.setVisibility(View.VISIBLE);
                holder.tvTitleCS1_CToTreo.setText("BT:");
                holder.tvTitleCS2_CToTreo.setText("CD:");
                holder.tvTitleCS3_CToTreo.setText("TD:");
                holder.tvTitleCS4_CToTreo.setText("SG:");
                holder.tvTitleCS5_CToTreo.setText("VC:");
                if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                    holder.tvCS1_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                    holder.tvCS2_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                    holder.tvCS3_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[2].split(":")[1] : CHI_SO.split(";")[2]);
                    holder.tvCS4_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[3].split(":")[1] : CHI_SO.split(";")[3]);
                    holder.tvCS5_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[4].split(":")[1] : CHI_SO.split(";")[4]);
                }
            }

            if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.HC.name()) || LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.VC.name())) {
                holder.tvTitleCS1_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS2_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS3_CToThao.setVisibility(View.GONE);
                holder.tvTitleCS4_CToThao.setVisibility(View.GONE);
                holder.tvTitleCS5_CToThao.setVisibility(View.GONE);

                holder.tvCS1_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS2_CToTreo.setVisibility(View.VISIBLE);
                holder.tvCS3_CToTreo.setVisibility(View.GONE);
                holder.tvCS4_CToTreo.setVisibility(View.GONE);
                holder.tvCS5_CToTreo.setVisibility(View.GONE);

                holder.tvTitleCS1_CToThao.setText("KT:");
                holder.tvTitleCS2_CToThao.setText("VC:");
                if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                    holder.tvCS1_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                    holder.tvCS2_CToTreo.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                }

            }

            //TODO tv Cong Tơ tháo
            holder.tvSoCto_CToThao.setText(lstDSoatThao.get(position).getTthtCtoEntity().getSO_CTO());
            CHI_SO = lstDSoatThao.get(position).getTthtCtoEntity().getCHI_SO();
            LOAI_CTO = lstDSoatThao.get(position).getTthtCtoEntity().getLOAI_CTO();
            if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.D1.name())) {
                holder.tvTitleCS1_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS2_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS3_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS4_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS5_CToThao.setVisibility(View.VISIBLE);

                holder.tvCS1_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS2_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS3_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS4_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS5_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS1_CToThao.setText("BT:");
                holder.tvTitleCS2_CToThao.setText("CD:");
                holder.tvTitleCS3_CToThao.setText("TD:");
                holder.tvTitleCS4_CToThao.setText("SG:");
                holder.tvTitleCS5_CToThao.setText("VC:");
                if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                    holder.tvCS1_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                    holder.tvCS2_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                    holder.tvCS3_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[2].split(":")[1] : CHI_SO.split(";")[2]);
                    holder.tvCS4_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[3].split(":")[1] : CHI_SO.split(";")[3]);
                    holder.tvCS5_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[4].split(":")[1] : CHI_SO.split(";")[4]);
                }
            }

            if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.HC.name()) || LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.VC.name())) {
                holder.tvTitleCS1_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS2_CToThao.setVisibility(View.VISIBLE);
                holder.tvTitleCS3_CToThao.setVisibility(View.GONE);
                holder.tvTitleCS4_CToThao.setVisibility(View.GONE);
                holder.tvTitleCS5_CToThao.setVisibility(View.GONE);

                holder.tvCS1_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS2_CToThao.setVisibility(View.VISIBLE);
                holder.tvCS3_CToThao.setVisibility(View.GONE);
                holder.tvCS4_CToThao.setVisibility(View.GONE);
                holder.tvCS5_CToThao.setVisibility(View.GONE);

                holder.tvTitleCS1_CToThao.setText("KT");
                holder.tvTitleCS2_CToThao.setText("VC");
                if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                    holder.tvCS1_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                    holder.tvCS2_CToThao.setText(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                }
            }

            //TODO set iv Cong To Treo
            pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + lstDSoatTreo.get(position).getTthtCtoEntity().getTEN_ANH_CONG_TO();
            bitmap = BitmapFactory.decodeFile(pathAnh, options);
            if (bitmap != null) {
                holder.ivCongTo_CToTreo.setImageBitmap(bitmap);
            } else {
                holder.ivCongTo_CToTreo.setImageBitmap(defaultBitmap);
            }

            //TODO set iv Cong To Thao
            pathAnh = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + lstDSoatThao.get(position).getTthtCtoEntity().getTEN_ANH_CONG_TO();
            bitmap = BitmapFactory.decodeFile(pathAnh, options);
            if (bitmap != null) {
                holder.ivCongTo_CToThao.setImageBitmap(bitmap);
            } else {
                holder.ivCongTo_CToThao.setImageBitmap(defaultBitmap);
            }

//            if (position == getPosSelect()) {
//                holder.itemView.setBackgroundResource(R.drawable.bg_item_blue);
////                holder.itemView.setBackgroundResource(R.color.button_dark_orange);
//            } else {
//                if (mListDoiSoat.get(position).getTthtBBanEntity().getSO_CHUNGLOAI_API() == 2) {
//                    holder.itemView.setBackgroundResource(R.drawable.bg_item_gray);
////                    holder.itemView.setBackgroundResource(R.color.button_disable_dark_orange);
//                } else {
//                    holder.itemView.setBackgroundResource(R.drawable.bg_item_white);
//                }
//            }
        }

        @Override
        public int getItemCount() {
            return lstDSoatTreo.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder
                implements AdapterView.OnClickListener {
            public LinearLayout llTuTreoCongToTreo, llTiTreoCongToTreo, llTuThaoCongToThao, llTiThaoCongToThao;

            public TextView tvStt, tvKH, tvDiaChi,
            //TODO tv Cong Tơ treo
            tvSoCto_CToTreo, tvCS1_CToTreo, tvCS2_CToTreo, tvCS3_CToTreo, tvCS4_CToTreo, tvCS5_CToTreo,
                    tvTitleCS1_CToTreo, tvTitleCS2_CToTreo, tvTitleCS3_CToTreo, tvTitleCS4_CToTreo, tvTitleCS5_CToTreo,
            //TODO tv Tu treo
            tvSoTu_CToTreo,
            //TODO tv Ti Treo
            tvSoTi_CToTreo,

            //TODO tv Cong Tơ tháo
            tvSoCto_CToThao, tvCS1_CToThao, tvCS2_CToThao, tvCS3_CToThao, tvCS4_CToThao, tvCS5_CToThao,
                    tvTitleCS1_CToThao, tvTitleCS2_CToThao, tvTitleCS3_CToThao, tvTitleCS4_CToThao, tvTitleCS5_CToThao,
            //TODO tv Tu treo
            tvSoTu_CToThao,
            //TODO tv Ti Treo
            tvSoTi_CToThao;

            public ImageView
                    //TODO iv Cong Tơ treo
                    ivCongTo_CToTreo,
            //TODO iv Tu treo
            ivTu_CToTreo_Tu, ivMachNhiThu_Tu_CToTreo, ivMachNiemPhong_Tu_CToTreo,
            //TODO iv Ti Treo
            ivTi_CToTreo_Ti, ivMachNhiThu_Ti_CToTreo, ivMachNiemPhong_Ti_CToTreo,

            //TODO iv Cong Tơ tháo
            ivCongTo_CToThao,
            //TODO iv Tu treo
            ivTu_CToThao_Tu, ivMachNhiThu_Tu_CToThao, ivMachNiemPhong_Tu_CToThao,
            //TODO iv Ti Treo
            ivTi_CToThao_Ti, ivMachNhiThu_Ti_CToThao, ivMachNiemPhong_Ti_CToThao;

            public ImageButton ibDSChoose;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                //TODO linearlayout
                llTuTreoCongToTreo = (LinearLayout) itemView.findViewById(R.id.ttht_row_doisoat_congToTreo_ll_tuTreo);
                llTiTreoCongToTreo = (LinearLayout) itemView.findViewById(R.id.ttht_row_doisoat_congToTreo_ll_tiTreo);
                llTuThaoCongToThao = (LinearLayout) itemView.findViewById(R.id.ttht_row_doisoat_congToThao_ll_tuThao);
                llTiThaoCongToThao = (LinearLayout) itemView.findViewById(R.id.ttht_row_doisoat_congToThao_ll_tiThao);

                //TODO Text view
                //TODO tv chung
                ibDSChoose = (ImageButton) itemView.findViewById(R.id.ib_ttht_row_doisoat_chon);
                ibDSChoose.setOnClickListener(this);
                tvStt = (TextView) itemView.findViewById(R.id.ttht_row_doisoat_tvSTT);
                tvKH = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_tenKH);
                tvDiaChi = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_diachiKH);

                //TODO tv Cong Tơ treo
                tvSoCto_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_soNo);
                tvCS1_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_cs1);
                tvCS2_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_cs2);
                tvCS3_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_cs3);
                tvCS4_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_cs4);
                tvCS5_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_cs5);

                tvTitleCS1_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_title_cs1);
                tvTitleCS2_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_title_cs2);
                tvTitleCS3_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_title_cs3);
                tvTitleCS4_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_title_cs4);
                tvTitleCS5_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_title_cs5);
                //TODO tv Tu treo
                tvSoTu_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_soTu);
                //TODO tv Ti Treo
                tvSoTi_CToTreo = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_soTi);


                //TODO tv Cong Tơ thao
                tvSoCto_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_soNo);
                tvCS1_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_cs1);
                tvCS2_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_cs2);
                tvCS3_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_cs3);
                tvCS4_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_cs4);
                tvCS5_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_cs5);
                tvTitleCS1_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_title_cs1);
                tvTitleCS2_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_title_cs2);
                tvTitleCS3_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_title_cs3);
                tvTitleCS4_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_title_cs4);
                tvTitleCS5_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_title_cs5);
                //TODO tv Tu treo
                tvSoTu_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_soTu);
                //TODO tv Ti Treo
                tvSoTi_CToThao = (TextView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_soTi);


                //TODO iv Cong Tơ treo
                ivCongTo_CToTreo = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_anhCongTo);
                ivCongTo_CToTreo.setOnClickListener(this);
                //TODO iv Tu treo
                ivTu_CToTreo_Tu = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhTu);
                ivMachNhiThu_Tu_CToTreo = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhNhiThu);
                ivMachNiemPhong_Tu_CToTreo = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhNiemPhong);
                ivTu_CToTreo_Tu.setOnClickListener(this);
                ivMachNhiThu_Tu_CToTreo.setOnClickListener(this);
                ivMachNiemPhong_Tu_CToTreo.setOnClickListener(this);
                //TODO iv Ti Treo
                ivTi_CToTreo_Ti = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhTi);
                ivMachNhiThu_Ti_CToTreo = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhNhiThu);
                ivMachNiemPhong_Ti_CToTreo = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhNiemPhong);
                ivTi_CToTreo_Ti.setOnClickListener(this);
                ivMachNhiThu_Ti_CToTreo.setOnClickListener(this);
                ivMachNiemPhong_Ti_CToTreo.setOnClickListener(this);


                //TODO iv Cong Tơ tháo
                ivCongTo_CToThao = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_anhCongTo);
                ivCongTo_CToThao.setOnClickListener(this);
                //TODO iv Tu treo
                ivTu_CToThao_Tu = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhTu);
                ivMachNhiThu_Tu_CToThao = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhNhiThu);
                ivMachNiemPhong_Tu_CToThao = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhNiemPhong);
                ivTu_CToThao_Tu.setOnClickListener(this);
                ivMachNhiThu_Tu_CToThao.setOnClickListener(this);
                ivMachNiemPhong_Tu_CToThao.setOnClickListener(this);
                //TODO iv Ti Treo
                ivTi_CToThao_Ti = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhTi);
                ivMachNhiThu_Ti_CToThao = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhNhiThu);
                ivMachNiemPhong_Ti_CToThao = (ImageView) itemView.findViewById(R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhNiemPhong);
                ivMachNiemPhong_Ti_CToThao.setOnClickListener(this);
                ivMachNhiThu_Ti_CToThao.setOnClickListener(this);
                ivTi_CToThao_Ti.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                String filePathImage = "";
                Bitmap bitmap = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                switch (view.getId()) {
                    case R.id.ll_ttht_row_doisoat_chon:
                     /*   idBBTrTh = adapterDoiSoat.lstDSoatTreo.get(getAdapterPosition()).getTthtCtoEntity().getID_BBAN_TRTH();
                        TthtKHangEntity tthtKHangEntityTreo = adapterDoiSoat.lstDSoatTreo.get(getAdapterPosition());
                        TthtKHangEntity tthtKHangEntityThao = adapterDoiSoat.lstDSoatTreo.get(getAdapterPosition());

                        Intent intent = new Intent(getActivity(), TthtDetailTaskActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putParcelableArrayList("LIST_DATA_TREO", (ArrayList<? extends Parcelable>) adapterDoiSoat.lstDSoatTreo);
                        bundle.putParcelableArrayList("LIST_DATA_THAO", (ArrayList<? extends Parcelable>) adapterDoiSoat.lstDSoatThao);
                        bundle.putParcelable("OBJECT_DATA_TREO", tthtKHangEntityTreo);
                        bundle.putParcelable("OBJECT_DATA_THAO", tthtKHangEntityThao);
                        bundle.putInt("ID_BBAN_TRTH", idBBTrTh);
                        bundle.putBoolean("IS_DOI_SOAT", true);
                        bundle.putString("MA_BDONG", TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);

                        intent.putExtras(bundle);
                        getActivity().startActivityForResult(intent, TthtMainActivity.REQUEST_CODE_TO_DETAIL_ACTIVITY);*/
                        break;

                    case R.id.ib_ttht_row_doisoat_chon:
                        //set image button
                        int positionDoiSanh = this.getAdapterPosition();
                        if (!isClickChooseArrayDoiSoat[positionDoiSanh]) {
                            isClickChooseArrayDoiSoat[positionDoiSanh] = true;
                            ibDSChoose.setBackgroundResource(R.mipmap.essp_check);
                        } else {
                            isClickChooseArrayDoiSoat[positionDoiSanh] = false;
                            ibDSChoose.setBackgroundResource(R.mipmap.essp_uncheck);
                        }
                        break;

                    //TODO click iv cong to treo
                    case R.id.tv_ttht_row_doisoat_congtoTreo_anhCongTo:
                        bitmap = (ivCongTo_CToTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivCongTo_CToTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhTu:
                        bitmap = (ivTu_CToTreo_Tu.getDrawable() == null) ? null : ((BitmapDrawable) ivTu_CToTreo_Tu.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhNhiThu:
                        bitmap = (ivMachNhiThu_Tu_CToTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNhiThu_Tu_CToTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tuTreo_anhNiemPhong:
                        bitmap = (ivMachNiemPhong_Tu_CToTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNiemPhong_Tu_CToTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhTi:
                        bitmap = (ivTi_CToTreo_Ti.getDrawable() == null) ? null : ((BitmapDrawable) ivTi_CToTreo_Ti.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhNhiThu:
                        bitmap = (ivMachNhiThu_Ti_CToTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNhiThu_Ti_CToTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoTreo_tiTreo_anhNiemPhong:
                        bitmap = (ivMachNiemPhong_Ti_CToTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNiemPhong_Ti_CToTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;

                    //TODO click iv cong to Thao
                    case R.id.tv_ttht_row_doisoat_congtoThao_anhCongTo:
                        bitmap = (ivCongTo_CToThao.getDrawable() == null) ? null : ((BitmapDrawable) ivCongTo_CToThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhTu:
                        bitmap = (ivTu_CToThao_Tu.getDrawable() == null) ? null : ((BitmapDrawable) ivTu_CToThao_Tu.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhNhiThu:
                        bitmap = (ivMachNhiThu_Tu_CToThao.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNhiThu_Tu_CToThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tuTreo_anhNiemPhong:
                        bitmap = (ivMachNiemPhong_Tu_CToThao.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNiemPhong_Tu_CToThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhTi:
                        bitmap = (ivTi_CToThao_Ti.getDrawable() == null) ? null : ((BitmapDrawable) ivTi_CToThao_Ti.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhNhiThu:
                        bitmap = (ivMachNhiThu_Ti_CToThao.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNhiThu_Ti_CToThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                    case R.id.tv_ttht_row_doisoat_congtoThao_tiTreo_anhNiemPhong:
                        bitmap = (ivMachNiemPhong_Ti_CToThao.getDrawable() == null) ? null : ((BitmapDrawable) ivMachNiemPhong_Ti_CToThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }
                        TthtCommon.zoomImage(getActivity(), bitmap);
                        break;
                }

            }

//endregion

        }
    }

    public class KHCompairByMaGCS implements Comparator<Object> {

        // Ghi đè method compare.
        // Nêu rõ quy tắc so sánh giữa 2 đối tượng Person.
        @Override
        public int compare(Object t0, Object t1) {
            // Hai đối tượng null coi như bằng nhau.
            if (t0 == null && t1 == null) {
                return 0;
            }
            // Nếu tthtKHangEntity null, coi như t1 lớn hơn
            if (t0 == null) {
                return -1;
            }
            // Nếu t1 null, coi như tthtKHangEntity lớn hơn.
            if (t1 == null) {
                return 1;
            }
            // Nguyên tắc:
            // Sắp xếp tăng dần theo MaGCS.
            String maGSC1 = "";
            String maGSC2 = "";
            if (t0 instanceof TthtKHangEntity && t1 instanceof TthtKHangEntity) {
                maGSC1 = ((TthtKHangEntity) t0).getTthtBBanEntity().getMA_GCS_CTO();
                maGSC2 = ((TthtKHangEntity) t1).getTthtBBanEntity().getMA_GCS_CTO();

            } else if (t0 instanceof TthtDoiSoatEntity && t1 instanceof TthtDoiSoatEntity) {
                maGSC1 = ((TthtDoiSoatEntity) t0).getMA_GCS_CTO();
                maGSC2 = ((TthtDoiSoatEntity) t0).getMA_GCS_CTO();
            } else {
                return 0;
            }

            int value = 0;
            String[] maGCS1Split = null, maGCS2Split = null;
            if (maGSC1.contains("-")) {
                maGCS1Split = maGSC1.split("-");
            }
            if (maGSC2.contains("-")) {
                maGCS2Split = maGSC2.split("-");
            }
            //"S"compareTo"C" =  -16;
            value = maGCS1Split[0].compareTo(maGCS2Split[0]);
            if (value != 0) {
                return value;
            }
            // truong hop maGCS truoc dau "-" giogn nhau so sanh tiep so thu tu
            value = maGCS1Split[1].compareTo(maGCS2Split[1]);
            if (value != 0) {
                return value;
            }
            return value;
        }
    }

    public class TramArrayAdapter extends ArrayAdapter<InfoTram> implements Filterable {

        List<InfoTram> allCodes = new ArrayList<>();
        List<InfoTram> originalCodes = new ArrayList<>();
        StringFilter filter;
        int resourceLayout;

        public TramArrayAdapter(Context context, int resource, List<InfoTram> keys) {
            super(context, resource, keys);
            allCodes = keys;
            originalCodes = keys;
            resourceLayout = resource;
        }

        public int getCount() {
            return allCodes.size();
        }

        public InfoTram getItem(int position) {
            return allCodes.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(resourceLayout, null);
            }
            InfoTram tram = allCodes.get(position);
            if (tram != null) {
                TextView maTram = (TextView) v.findViewById(R.id.tv_ttht_row_autotv_MA_TRAM);
                TextView tenTram = (TextView) v.findViewById(R.id.tv_ttht_row_autotv_TEN_TRAM);
                if (maTram != null) {
                    maTram.setText(tram.getMA_TRAM());
                }

                if (tenTram != null) {
                    tenTram.setText(tram.getTEN_TRAM());
                }
            }
            return v;
        }

        private class StringFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = "";
                if (constraint != null) {
                    filterString = constraint.toString().toLowerCase();
                }
                FilterResults results = new FilterResults();
                final List<InfoTram> list = originalCodes;

                int count = list.size();
                final ArrayList<InfoTram> nlist = new ArrayList<InfoTram>(count);
                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getMA_TRAM();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(list.get(i));
                    }
                }

                results.values = nlist;
                results.count = nlist.size();
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                allCodes = (ArrayList<InfoTram>) results.values;
                notifyDataSetChanged();
            }

        }


        @Override
        public Filter getFilter() {
            return new StringFilter();
        }
    }

    public class InfoTram {
        private String MA_TRAM;
        private String TEN_TRAM;

        public InfoTram() {
        }

        public InfoTram(String MA_TRAM, String TEN_TRAM) {
            this.MA_TRAM = MA_TRAM;
            this.TEN_TRAM = TEN_TRAM;
        }

        public String getMA_TRAM() {
            return MA_TRAM;
        }

        public void setMA_TRAM(String MA_TRAM) {
            this.MA_TRAM = MA_TRAM;
        }

        public String getTEN_TRAM() {
            return TEN_TRAM;
        }

        public void setTEN_TRAM(String TEN_TRAM) {
            this.TEN_TRAM = TEN_TRAM;
        }
    }


    public class AsyncSetDataDSOnRecyclerView extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Log.d(TAG, "doInBackground: " + lstPhieuTreoThao);
                lstDSoatTreo.clear();
                lstDSoatThao.clear();
                setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);
//                getDataDoiSoat();


            } catch (Exception ex) {
                publishProgress(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi (392)", Color.RED, "Lỗi khởi tạo dữ liệu\n" + values[0], Color.WHITE, "OK", Color.RED);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                //TODO set Adapter
                adapterDoiSoat = new TthtDoiSoatAdapter(lstDSoatTreo, lstDSoatThao);
                rvKH.setAdapter(adapterDoiSoat);
                rvKH.setHasFixedSize(true);
                rvKH.invalidate();

                //TODO fill text
                if (!TthtCommon.getTenTramSelected().equals("")) {
                    auEtMaTram.setText(TthtCommon.getMaTramSelected());
                    tvTenTram.setText(TthtCommon.getTenTramSelected());
                }
            } catch (Exception ex) {
                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi (391)", Color.RED,
                        "Lỗi fill giá trị lên recyclerView\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
            }
        }
    }

//    private void getDataDoiSoat() {
//        try {
//            int stt = 0;
//            Cursor cKHangTreoThao = null;
//            Cursor cKHangThao = null;
//            Cursor cusorGetAnhCongTo = null;
//            Cursor cusorGetAnhTuTi = null;
//            Cursor cusorGetAnhMachNhiThu = null;
//            Cursor cusorGetAnhMachCongTo = null;
//            Cursor cBBanTuTi = null;
//            String ngayConvert = "";
//            if (!TthtCommon.getTthtDateChon().equals("")) {
//                ngayConvert = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
//            }
//
//            cKHangTreoThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.THAO);
//            cKHangThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, TthtCommon.FILTER_DATA_FILL.THAO);
//
//
//            lstKhangHangTreo.clear();
//            //lấy chi tiết
//
//            try {
//                if (cKHangTreoThao != null) {
//                    do {
//                        stt++;
//                        //TODO get data TthtBBanEntity
//                        TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
//                        tthtBBanEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")));
//                        tthtBBanEntity.setID_BBAN_TRTH(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")));
//                        tthtBBanEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CNANG")));
////                        tthtBBanEntity.setDATE_CALL_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_DDO")));
//                        tthtBBanEntity.setDATE_CALL_API("");
//                        tthtBBanEntity.setMA_DVIQLY(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NVIEN")));
////                        tthtBBanEntity.setSO_BBAN_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_LDO")));
//                        tthtBBanEntity.setSO_BBAN_API("");
//                        tthtBBanEntity.setMA_NVIEN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("BUNDLE_MA_NVIEN")));
////                        tthtBBanEntity.setSO_TRAM_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_YCAU_KNAI")));
//                        tthtBBanEntity.setSO_TRAM_API("");
//                        tthtBBanEntity.setSO_BBAN_TUTI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_SUA")));
//                        tthtBBanEntity.setSO_CTO_THAO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TAO")));
////                        tthtBBanEntity.setTYPE_RESULT(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TRTH")));
//                        tthtBBanEntity.setTYPE_RESULT("");
//                        tthtBBanEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_SUA")));
//                        tthtBBanEntity.setSO_CTO_TREO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_TAO")));
////                        tthtBBanEntity.setTYPE_CALL_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_BBAN")));
//                        tthtBBanEntity.setTYPE_CALL_API("");
////                        tthtBBanEntity.setSO_CHUNGLOAI_API(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("TRANG_THAI")));
//                        tthtBBanEntity.setSO_CHUNGLOAI_API(0);
//                        tthtBBanEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")));
//                        tthtBBanEntity.setId_BBAN_CONGTO(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_CONGTO")));
//                        tthtBBanEntity.setLOAI_BBAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LOAI_BBAN")));
//                        tthtBBanEntity.setTEN_KHANG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_KHANG")));
//                        tthtBBanEntity.setDCHI_HDON(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DCHI_HDON")));
//                        tthtBBanEntity.setDTHOAI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DTHOAI")));
//                        tthtBBanEntity.setMA_GCS_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_GCS_CTO")));
//                        tthtBBanEntity.setMA_TRAM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_TRAM")));
//                        tthtBBanEntity.setMA_HDONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_HDONG")));
//
//                        //TODO get data TthtCtoEntity
//                        TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
//                        tthtCtoEntity.setMA_DVIQLY(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NVIEN")));
//                        tthtCtoEntity.setID_BBAN_TRTH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TRTH")) : 0);
//                        tthtCtoEntity.setMA_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CTO")));
//                        tthtCtoEntity.setSO_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_CTO")));
//                        tthtCtoEntity.setLAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LAN")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LAN")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("LAN")) : 0);
//                        String MA_BDONG_CTO = cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_BDONG"));
//                        tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
//                        tthtCtoEntity.setNGAY_BDONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_BDONG")));
//                        tthtCtoEntity.setMA_CLOAI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CLOAI")));
//                        tthtCtoEntity.setLOAI_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("LOAI_CTO")));
//                        tthtCtoEntity.setVTRI_TREO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("VTRI_TREO")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("VTRI_TREO")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("VTRI_TREO")) : 0);
//                        tthtCtoEntity.setMA_SOCBOOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_SOCBOOC")));
//                        tthtCtoEntity.setSOVIEN_CBOOC(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SO_VIENCBOOC")));
//                        tthtCtoEntity.setLOAI_HOM(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("LOAI_HOM")));
//                        tthtCtoEntity.setMA_SOCHOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_SOCHOM")));
//                        tthtCtoEntity.setSO_VIENCHOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SO_VIENCHOM")) : 0);
//                        tthtCtoEntity.setHS_NHAN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HS_NHAN")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HS_NHAN")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("HS_NHAN")) : 1);
//                        tthtCtoEntity.setSO_CTO_THAO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_TAO")));
//                        tthtCtoEntity.setSO_CTO_TREO_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_TAO")));
//                        tthtCtoEntity.setSO_BBAN_TUTI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_SUA")));
//                        tthtCtoEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGUOI_SUA")));
//                        tthtCtoEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CNANG")));
//                        tthtCtoEntity.setSO_TU_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TU")));
//                        tthtCtoEntity.setSO_TI_API(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TI")));
//                        tthtCtoEntity.setSO_COT(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_COT")));
//                        tthtCtoEntity.setSO_HOM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_HOM")));
//                        tthtCtoEntity.setCHI_SO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("CHI_SO")));
//                        tthtCtoEntity.setNGAY_KDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NGAY_KDINH")));
//                        tthtCtoEntity.setNAM_SX(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("NAM_SX")));
//                        tthtCtoEntity.setTEM_CQUANG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEM_CQUANG")));
//                        tthtCtoEntity.setMA_CHIKDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_CHIKDINH")));
//                        tthtCtoEntity.setMA_TEM(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_TEM")));
//                        tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")) != null
//                                && !cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
//                                ? cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
//                        tthtCtoEntity.setDIEN_AP(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DIEN_AP")));
//                        tthtCtoEntity.setDONG_DIEN(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DONG_DIEN")));
//                        tthtCtoEntity.setHANGSO_K(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HANGSO_K")));
//                        tthtCtoEntity.setMA_NUOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("MA_NUOC")));
//                        tthtCtoEntity.setTEN_NUOC(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_NUOC")));
//                        tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_KIM_NIEM_CHI")));
//                        tthtCtoEntity.setTTRANG_NPHONG(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TTRANG_NPHONG")));
//
//                        int ID_CHITIET_CTO = cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_CHITIET_CTO"));
//                        //TODO lấy tên ảnh
//                        cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
//                        if (cusorGetAnhCongTo.moveToFirst()) {
//                            String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
//                            tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
//                            cusorGetAnhCongTo.close();
//                        }
//                        tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
//                        tthtCtoEntity.setTEN_LOAI_CTO(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("TEN_LOAI_CTO")).trim());
//
//                        tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
//                        tthtCtoEntity.setGHI_CHU(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("GHI_CHU")).trim());
//                        tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("TRANG_THAI_DU_LIEU")));
//                        int ID_BBAN_TUTI = cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("ID_BBAN_TUTI"));
//
//                        tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
//                        tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
//                        tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TU_SAULAP_TUTI")));
//                        tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("SO_TI_SAULAP_TUTI")));
//                        tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("CHI_SO_SAULAP_TUTI")));
//                        tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
//                        tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
//                        tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangTreoThao.getString(cKHangTreoThao.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
//                        tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangTreoThao.getInt(cKHangTreoThao.getColumnIndex("CAP_CX_SAULAP_TUTI")));
//
//
//                        //TODO get data TU TI
//                        if (tthtBBanEntity.getID_BBAN_TRTH() == 1742727)
//                            Log.d(TAG, "doInBackground: ");
//                        TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
//                        ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;
//
//                        cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
//                        try {
//                            if (cBBanTuTi.moveToFirst()) {
//                                tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
//                                tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
//                                ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
//                                tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
//                                tthtBBanTuTiEntity.setDATE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
//                                tthtBBanTuTiEntity.setTYPE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
//                                tthtBBanTuTiEntity.setTYPE_RESULT(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
//                                tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
//                                tthtBBanTuTiEntity.setSO_CHUNGLOAI_API(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
//                                tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
//                                tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
//                                tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
//                                tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
//                                tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
//                                tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
//                                tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
//                                tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
//                                tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));
//
//                                Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
//                                try {
//                                    if (cTuTi != null) {
//                                        tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
//                                        do {
//                                            TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();
//                                            //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
//                                            if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
//                                                String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
//                                                //TODO MA_BDONG trong bảng CHI_TIET_TUTI
//                                                String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));
//
//                                                //TODO lấy tên ảnh
//                                                cusorGetAnhTuTi = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI));
//                                                if (cusorGetAnhTuTi.moveToFirst()) {
//                                                    String TEN_ANH = cusorGetAnhTuTi.getString(cusorGetAnhTuTi.getColumnIndex("TEN_ANH"));
//                                                    tthtTuTiEntity.setTEN_ANH_TU_TI(TEN_ANH);
//                                                    cusorGetAnhTuTi.close();
//                                                }
//
//                                                cusorGetAnhMachNhiThu = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
//                                                if (cusorGetAnhMachNhiThu.moveToFirst()) {
//                                                    String TEN_ANH = cusorGetAnhMachNhiThu.getString(cusorGetAnhMachNhiThu.getColumnIndex("TEN_ANH"));
//                                                    tthtTuTiEntity.setTEN_ANH_MACH_NHI_THU(TEN_ANH);
//                                                    cusorGetAnhMachNhiThu.close();
//                                                }
//
//                                                cusorGetAnhMachCongTo = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
//                                                if (cusorGetAnhMachCongTo.moveToFirst()) {
//                                                    String TEN_ANH = cusorGetAnhMachCongTo.getString(cusorGetAnhMachCongTo.getColumnIndex("TEN_ANH"));
//                                                    tthtTuTiEntity.setTEN_ANH_MACH_CONG_TO(TEN_ANH);
//                                                    cusorGetAnhMachCongTo.close();
//                                                }
//
//                                                tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
//                                                tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
//                                                tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
//                                                tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
//                                                tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
//                                                tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
//                                                tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
//                                                tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
//                                                tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
//                                                tthtTuTiEntity.setSO_CHUNGLOAI_API(cTuTi.getInt(cTuTi.getColumnIndex("TRANG_THAI")));
//                                                tthtTuTiEntity.setIS_TU(IS_TU);
//                                                tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
//                                                tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
//                                                tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
//                                                tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
//                                                tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
//                                                tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
//                                                tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
//                                                tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
//                                                tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
//                                                tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
//                                                tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
//                                                tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
//                                                tthtTuTiEntityList.add(tthtTuTiEntity);
//                                            }
//                                        }
//                                        while (cTuTi.moveToNext());
//                                    }
//                                } finally {
//                                    if (cTuTi != null)
//                                        cTuTi.close();
//                                }
//                            }
//                        } finally {
//                            if (cBBanTuTi != null)
//                                cBBanTuTi.close();
//                        }
//
//                        TthtKHangEntity tthtKHangEntityTreo = new TthtKHangEntity();
//                        tthtKHangEntityTreo.setStt(stt);
//                        tthtKHangEntityTreo.setTthtBBanEntity(tthtBBanEntity);
//                        tthtKHangEntityTreo.setTthtCtoEntity(tthtCtoEntity);
//                        if (tthtBBanTuTiEntity != null) {
//                            tthtKHangEntityTreo.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
//                        }
//                        if (tthtTuTiEntityList != null) {
//                            tthtKHangEntityTreo.setTthtTuTiEntity(tthtTuTiEntityList);
//                        }
//
//                        lstKhangHangTreo.add(tthtKHangEntityTreo);
//                    } while (cKHangTreoThao.moveToNext());
//                }
//            } finally {
//                if (cKHangTreoThao != null) {
//                    cKHangTreoThao.close();
//                }
//            }
//            //TODO sort by MaGCS
//            Collections.sort(lstKhangHangTreo, new KHCompairByMaGCS());
//            for (int i = 0; i < lstKhangHangTreo.size(); i++) {
//                lstKhangHangTreo.get(i).setStt(i + 1);
//            }
//
//            lstKhangHangThao.clear();
//            try {
//                if (cKHangThao != null) {
//                    if (cKHangThao.moveToFirst()) {
//                        do {
//                            stt++;
//                            //TODO get data TthtBBanEntity
//                            TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
//                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
//                            tthtBBanEntity.setID_BBAN_TRTH(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")));
//                            tthtBBanEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
//                            tthtBBanEntity.setDATE_CALL_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_DDO")));
//                            tthtBBanEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
//                            tthtBBanEntity.setSO_BBAN_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_LDO")));
//                            tthtBBanEntity.setMA_NVIEN(cKHangThao.getString(cKHangThao.getColumnIndex("BUNDLE_MA_NVIEN")));
//                            tthtBBanEntity.setSO_TRAM_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_YCAU_KNAI")));
//                            tthtBBanEntity.setSO_BBAN_TUTI_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
//                            tthtBBanEntity.setSO_CTO_THAO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
//                            tthtBBanEntity.setTYPE_RESULT(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TRTH")));
//                            tthtBBanEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
//                            tthtBBanEntity.setSO_CTO_TREO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
//                            tthtBBanEntity.setTYPE_CALL_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_BBAN")));
//                            tthtBBanEntity.setSO_CHUNGLOAI_API(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI")));
//                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
//                            tthtBBanEntity.setId_BBAN_CONGTO(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_CONGTO")));
//                            tthtBBanEntity.setLOAI_BBAN(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_BBAN")));
//                            tthtBBanEntity.setTEN_KHANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_KHANG")));
//                            tthtBBanEntity.setDCHI_HDON(cKHangThao.getString(cKHangThao.getColumnIndex("DCHI_HDON")));
//                            tthtBBanEntity.setDTHOAI(cKHangThao.getString(cKHangThao.getColumnIndex("DTHOAI")));
//                            tthtBBanEntity.setMA_GCS_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_GCS_CTO")));
//                            tthtBBanEntity.setMA_TRAM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TRAM")));
//                            tthtBBanEntity.setMA_HDONG(cKHangThao.getString(cKHangThao.getColumnIndex("MA_HDONG")));
//
//                            //TODO get data TthtCtoEntity
//                            TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
//                            tthtCtoEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
//                            tthtCtoEntity.setID_BBAN_TRTH(cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) : 0);
//                            tthtCtoEntity.setMA_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CTO")));
//                            tthtCtoEntity.setSO_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("SO_CTO")));
//                            tthtCtoEntity.setLAN(cKHangThao.getString(cKHangThao.getColumnIndex("LAN")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("LAN")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("LAN")) : 0);
//                            String MA_BDONG_CTO = cKHangThao.getString(cKHangThao.getColumnIndex("MA_BDONG"));
//                            tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
//                            tthtCtoEntity.setNGAY_BDONG(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_BDONG")));
//                            tthtCtoEntity.setMA_CLOAI(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CLOAI")));
//                            tthtCtoEntity.setLOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_CTO")));
//                            tthtCtoEntity.setVTRI_TREO(cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("VTRI_TREO")) : 0);
//                            tthtCtoEntity.setMA_SOCBOOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCBOOC")));
//                            tthtCtoEntity.setSOVIEN_CBOOC(cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCBOOC")));
//                            tthtCtoEntity.setLOAI_HOM(cKHangThao.getInt(cKHangThao.getColumnIndex("LOAI_HOM")));
//                            tthtCtoEntity.setMA_SOCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCHOM")));
//                            tthtCtoEntity.setSO_VIENCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCHOM")) : 0);
//                            tthtCtoEntity.setHS_NHAN(cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN")) : 1);
//                            tthtCtoEntity.setSO_CTO_THAO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
//                            tthtCtoEntity.setSO_CTO_TREO_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
//                            tthtCtoEntity.setSO_BBAN_TUTI_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
//                            tthtCtoEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
//                            tthtCtoEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
//                            tthtCtoEntity.setSO_TU_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU")));
//                            tthtCtoEntity.setSO_TI_API(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI")));
//                            tthtCtoEntity.setSO_COT(cKHangThao.getString(cKHangThao.getColumnIndex("SO_COT")));
//                            tthtCtoEntity.setSO_HOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_HOM")));
//                            tthtCtoEntity.setCHI_SO(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO")));
//                            tthtCtoEntity.setNGAY_KDINH(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_KDINH")));
//                            tthtCtoEntity.setNAM_SX(cKHangThao.getString(cKHangThao.getColumnIndex("NAM_SX")));
//                            tthtCtoEntity.setTEM_CQUANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEM_CQUANG")));
//                            tthtCtoEntity.setMA_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CHIKDINH")));
//                            tthtCtoEntity.setMA_TEM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TEM")));
//                            tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
//                            tthtCtoEntity.setDIEN_AP(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP")));
//                            tthtCtoEntity.setDONG_DIEN(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN")));
//                            tthtCtoEntity.setHANGSO_K(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K")));
//                            tthtCtoEntity.setMA_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NUOC")));
//                            tthtCtoEntity.setTEN_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_NUOC")));
//                            tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_KIM_NIEM_CHI")));
//                            tthtCtoEntity.setTTRANG_NPHONG(cKHangThao.getString(cKHangThao.getColumnIndex("TTRANG_NPHONG")));
//
//                            int ID_CHITIET_CTO = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_CHITIET_CTO"));
//                            cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
//                            try {
//                                if (cusorGetAnhCongTo.moveToFirst()) {
//                                    String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
//                                    tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
//                                }
//                            } finally {
//                                if (cusorGetAnhCongTo != null) cusorGetAnhCongTo.close();
//                            }
//
//                            tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
//                            tthtCtoEntity.setTEN_LOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_LOAI_CTO")).trim());
//
//                            tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangThao.getString(cKHangThao.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
//                            tthtCtoEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")).trim());
//                            int ID_BBAN_TUTI = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TUTI"));
//                            tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
//                            tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI_DU_LIEU")));
//
//                            tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
//                            tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU_SAULAP_TUTI")));
//                            tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI_SAULAP_TUTI")));
//                            tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO_SAULAP_TUTI")));
//                            tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
//                            tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
//                            tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
//                            tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("CAP_CX_SAULAP_TUTI")));
//                            //TODO get data TU TI
//                            TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
//                            ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;
//                            cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
//                            try {
//                                if (cBBanTuTi.moveToFirst()) {
//                                    tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
//                                    tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
//                                    ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
//                                    tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
//                                    tthtBBanTuTiEntity.setDATE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
//                                    tthtBBanTuTiEntity.setTYPE_CALL_API(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
//                                    tthtBBanTuTiEntity.setTYPE_RESULT(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
//                                    tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
//                                    tthtBBanTuTiEntity.setSO_CHUNGLOAI_API(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
//                                    tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
//                                    tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
//                                    tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
//                                    tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
//                                    tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
//                                    tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
//                                    tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
//                                    tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
//                                    tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));
//                                    cBBanTuTi.close();
//                                    Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
//                                    try {
//                                        if (cTuTi != null) {
//                                            tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
//                                            do {
//                                                TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();
//
//                                                //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
//                                                if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
//                                                    String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
//                                                    //TODO MA_BDONG trong bảng CHI_TIET_TUTI
//                                                    String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));
//                                                    tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
//                                                    tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
//                                                    tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
//                                                    tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
//                                                    tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
//                                                    tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
//                                                    tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
//                                                    tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
//                                                    tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
//                                                    tthtTuTiEntity.setSO_CHUNGLOAI_API(cTuTi.getInt(cTuTi.getColumnIndex("TRANG_THAI")));
//                                                    tthtTuTiEntity.setIS_TU(IS_TU);
//                                                    tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
//                                                    tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
//                                                    tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
//                                                    tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
//                                                    tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
//                                                    tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
//                                                    tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
//                                                    tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
//                                                    tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
//                                                    tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
//                                                    tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
//                                                    tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
//                                                    tthtTuTiEntityList.add(tthtTuTiEntity);
//                                                }
//                                            }
//                                            while (cTuTi.moveToNext());
//                                        }
//                                    } finally {
//                                        if (cTuTi != null) cTuTi.close();
//                                    }
//                                }
//                            } finally {
//                                if (cBBanTuTi != null) cBBanTuTi.close();
//                            }
//
//
//                            TthtKHangEntity tthtKHangEntityThao = new TthtKHangEntity();
//                            tthtKHangEntityThao.setStt(stt);
//                            tthtKHangEntityThao.setTthtBBanEntity(tthtBBanEntity);
//                            tthtKHangEntityThao.setTthtCtoEntity(tthtCtoEntity);
//                            if (tthtBBanTuTiEntity != null) {
//                                tthtKHangEntityThao.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
//                            }
//                            if (tthtTuTiEntityList != null) {
//                                tthtKHangEntityThao.setTthtTuTiEntity(tthtTuTiEntityList);
//                            }
//
//                            lstKhangHangThao.add(tthtKHangEntityThao);
//                        } while (cKHangThao.moveToNext());
//
//                    }
//                }
//
//            } finally {
//                if (cKHangThao != null) {
//                    cKHangThao.close();
//                }
//            }
//
//
//            //TODO sort by MaGCS
//            Collections.sort(lstKhangHangThao, new KHCompairByMaGCS());
//
//            for (int i = 0; i < lstKhangHangThao.size(); i++) {
//                lstKhangHangThao.get(i).setStt(i + 1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "getDataDoiSoat: " + e.getMessage());
//        }
//    }


    //region Khởi tạo dữ liệu
    class AsyncSetDataOnRecyclerView extends AsyncTask<Void, String, Void> {
        private TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL;

        public AsyncSetDataOnRecyclerView(TthtCommon.FILTER_DATA_FILL FILTER_DATA_FILL) {
            this.FILTER_DATA_FILL = FILTER_DATA_FILL;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            try {
            int stt = 0;
            Cursor cKHangTreo = null;
            Cursor cKHangThao = null;
            Cursor cusorGetAnhCongTo = null;
            Cursor cusorGetAnhTuTi = null;
            Cursor cusorGetAnhMachNhiThu = null;
            Cursor cusorGetAnhMachCongTo = null;
            Cursor cBBanTuTi = null;
            String ngayConvert = "";
            if (!TthtCommon.getTthtDateChon().equals("")) {
                ngayConvert = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
            }
            if (FILTER_DATA_FILL == TthtCommon.FILTER_DATA_FILL.ALL) {
                cKHangTreo = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[0], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
                cKHangThao = connection.getDataKhangFullMA_TRAM(TthtCommon.arrMaBDong[1], TthtCommon.getMaNvien(), TthtCommon.getMaDviqly(), ngayConvert, FILTER_DATA_FILL);
            }

            lstKhangHangTreo.clear();
            try {
                if (cKHangTreo != null) {
                    if (cKHangTreo.moveToFirst()) {
                        do {
                            stt++;
                            //TODO get data TthtBBanEntity
                            TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
                            tthtBBanEntity.setGHI_CHU(cKHangTreo.getString(cKHangTreo.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setID_BBAN_TRTH(cKHangTreo.getInt(cKHangTreo.getColumnIndex("ID_BBAN_TRTH")));
                            tthtBBanEntity.setMA_CNANG(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_CNANG")));
                            tthtBBanEntity.setMA_DDO(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_DDO")));
                            tthtBBanEntity.setMA_DVIQLY(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_NVIEN")));
                            tthtBBanEntity.setMA_LDO(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_LDO")));
                            tthtBBanEntity.setMA_NVIEN(cKHangTreo.getString(cKHangTreo.getColumnIndex("BUNDLE_MA_NVIEN")));
                            tthtBBanEntity.setMA_YCAU_KNAI(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_YCAU_KNAI")));
                            tthtBBanEntity.setNGAY_SUA(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_SUA")));
                            tthtBBanEntity.setNGAY_TAO(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_TAO")));
                            tthtBBanEntity.setNGAY_TRTH(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_TRTH")));
                            tthtBBanEntity.setNGUOI_SUA(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGUOI_SUA")));
                            tthtBBanEntity.setNGUOI_TAO(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGUOI_TAO")));
                            tthtBBanEntity.setSO_BBAN(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_BBAN")));
                            tthtBBanEntity.setTRANG_THAI(cKHangTreo.getInt(cKHangTreo.getColumnIndex("TRANG_THAI")));
                            tthtBBanEntity.setGHI_CHU(cKHangTreo.getString(cKHangTreo.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setId_BBAN_CONGTO(cKHangTreo.getInt(cKHangTreo.getColumnIndex("ID_BBAN_CONGTO")));
                            tthtBBanEntity.setLOAI_BBAN(cKHangTreo.getString(cKHangTreo.getColumnIndex("LOAI_BBAN")));
                            tthtBBanEntity.setTEN_KHANG(cKHangTreo.getString(cKHangTreo.getColumnIndex("TEN_KHANG")));
                            tthtBBanEntity.setDCHI_HDON(cKHangTreo.getString(cKHangTreo.getColumnIndex("DCHI_HDON")));
                            tthtBBanEntity.setDTHOAI(cKHangTreo.getString(cKHangTreo.getColumnIndex("DTHOAI")));
                            tthtBBanEntity.setMA_GCS_CTO(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_GCS_CTO")));
                            tthtBBanEntity.setMA_TRAM(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_TRAM")));
                            tthtBBanEntity.setMA_HDONG(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_HDONG")));

                            //TODO get data TthtCtoEntity
                            TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
                            tthtCtoEntity.setMA_DVIQLY(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_NVIEN")));
                            tthtCtoEntity.setID_BBAN_TRTH(cKHangTreo.getString(cKHangTreo.getColumnIndex("ID_BBAN_TRTH")) != null
                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("ID_BBAN_TRTH")) : 0);
                            tthtCtoEntity.setMA_CTO(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_CTO")));
                            tthtCtoEntity.setSO_CTO(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_CTO")));
                            tthtCtoEntity.setLAN(cKHangTreo.getString(cKHangTreo.getColumnIndex("LAN")) != null
                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("LAN")).isEmpty()
                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("LAN")) : 0);
                            String MA_BDONG_CTO = cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_BDONG"));
                            tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
                            tthtCtoEntity.setNGAY_BDONG(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_BDONG")));
                            tthtCtoEntity.setMA_CLOAI(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_CLOAI")));
                            tthtCtoEntity.setLOAI_CTO(cKHangTreo.getString(cKHangTreo.getColumnIndex("LOAI_CTO")));
//                            tthtCtoEntity.setVITRI_TREO_THAO(cKHangTreo.getString(cKHangTreo.getColumnIndex("VTRI_TREO")) != null
//                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("VTRI_TREO")).isEmpty()
//                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("VTRI_TREO")) : 0);
                            tthtCtoEntity.setMA_SOCBOOC(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_SOCBOOC")));
                            tthtCtoEntity.setSOVIEN_CBOOC(cKHangTreo.getInt(cKHangTreo.getColumnIndex("SO_VIENCBOOC")));
                            tthtCtoEntity.setLOAI_HOM(cKHangTreo.getInt(cKHangTreo.getColumnIndex("LOAI_HOM")));
                            tthtCtoEntity.setMA_SOCHOM(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_SOCHOM")));
                            tthtCtoEntity.setSO_VIENCHOM(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_VIENCHOM")) != null
                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_VIENCHOM")).isEmpty()
                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("SO_VIENCHOM")) : 0);
                            tthtCtoEntity.setHS_NHAN(cKHangTreo.getString(cKHangTreo.getColumnIndex("HS_NHAN")) != null
                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("HS_NHAN")).isEmpty()
                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("HS_NHAN")) : 1);
                            tthtCtoEntity.setNGAY_TAO(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_TAO")));
                            tthtCtoEntity.setNGUOI_TAO(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGUOI_TAO")));
                            tthtCtoEntity.setNGAY_SUA(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_SUA")));
                            tthtCtoEntity.setNGUOI_SUA(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGUOI_SUA")));
                            tthtCtoEntity.setMA_CNANG(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_CNANG")));
                            tthtCtoEntity.setSO_TU(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_TU")));
                            tthtCtoEntity.setSO_TI(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_TI")));
                            tthtCtoEntity.setSO_COT(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_COT")));
                            tthtCtoEntity.setSO_HOM(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_HOM")));
                            tthtCtoEntity.setCHI_SO(cKHangTreo.getString(cKHangTreo.getColumnIndex("CHI_SO")));
                            tthtCtoEntity.setNGAY_KDINH(cKHangTreo.getString(cKHangTreo.getColumnIndex("NGAY_KDINH")));
                            tthtCtoEntity.setNAM_SX(cKHangTreo.getString(cKHangTreo.getColumnIndex("NAM_SX")));
                            tthtCtoEntity.setTEM_CQUANG(cKHangTreo.getString(cKHangTreo.getColumnIndex("TEM_CQUANG")));
                            tthtCtoEntity.setMA_CHIKDINH(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_CHIKDINH")));
                            tthtCtoEntity.setMA_TEM(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_TEM")));
                            tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangTreo.getString(cKHangTreo.getColumnIndex("SOVIEN_CHIKDINH")) != null
                                    && !cKHangTreo.getString(cKHangTreo.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
                                    ? cKHangTreo.getInt(cKHangTreo.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
                            tthtCtoEntity.setDIEN_AP(cKHangTreo.getString(cKHangTreo.getColumnIndex("DIEN_AP")));
                            tthtCtoEntity.setDONG_DIEN(cKHangTreo.getString(cKHangTreo.getColumnIndex("DONG_DIEN")));
                            tthtCtoEntity.setHANGSO_K(cKHangTreo.getString(cKHangTreo.getColumnIndex("HANGSO_K")));
                            tthtCtoEntity.setMA_NUOC(cKHangTreo.getString(cKHangTreo.getColumnIndex("MA_NUOC")));
                            tthtCtoEntity.setTEN_NUOC(cKHangTreo.getString(cKHangTreo.getColumnIndex("TEN_NUOC")));
                            tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_KIM_NIEM_CHI")));
                            tthtCtoEntity.setTTRANG_NPHONG(cKHangTreo.getString(cKHangTreo.getColumnIndex("TTRANG_NPHONG")));

                            int ID_CHITIET_CTO = cKHangTreo.getInt(cKHangTreo.getColumnIndex("ID_CHITIET_CTO"));
                            //TODO lấy tên ảnh
                            cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                            if (cusorGetAnhCongTo.moveToFirst()) {
                                String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
                                tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
                                cusorGetAnhCongTo.close();
                            }
                            tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
                            tthtCtoEntity.setTEN_LOAI_CTO(cKHangTreo.getString(cKHangTreo.getColumnIndex("TEN_LOAI_CTO")).trim());

                            tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangTreo.getString(cKHangTreo.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
                            tthtCtoEntity.setGHI_CHU(cKHangTreo.getString(cKHangTreo.getColumnIndex("GHI_CHU")).trim());
                            tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangTreo.getInt(cKHangTreo.getColumnIndex("TRANG_THAI_DU_LIEU")));
                            int ID_BBAN_TUTI = cKHangTreo.getInt(cKHangTreo.getColumnIndex("ID_BBAN_TUTI"));

                            tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                            tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangTreo.getInt(cKHangTreo.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_TU_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("SO_TI_SAULAP_TUTI")));
                            tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("CHI_SO_SAULAP_TUTI")));
                            tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
                            tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
                            tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangTreo.getString(cKHangTreo.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
                            tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangTreo.getInt(cKHangTreo.getColumnIndex("CAP_CX_SAULAP_TUTI")));


                            //TODO get data TU TI
                            if (tthtBBanEntity.getID_BBAN_TRTH() == 1742727)
                                Log.d(TAG, "doInBackground: ");
                            TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
                            ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;

                            cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                            try {
                                if (cBBanTuTi.moveToFirst()) {
                                    tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
                                    tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
                                    ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
                                    tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                                    tthtBBanTuTiEntity.setMA_DDO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
                                    tthtBBanTuTiEntity.setSO_BBAN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
                                    tthtBBanTuTiEntity.setNGAY_TRTH(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
                                    tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
                                    tthtBBanTuTiEntity.setTRANG_THAI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
                                    tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
                                    tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
                                    tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
                                    tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
                                    tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
                                    tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
                                    tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
                                    tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
                                    tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));

                                    Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                                    try {
                                        if (cTuTi != null) {
                                            tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
                                            do {
                                                TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();
                                                //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
                                                if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                                                    String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
                                                    //TODO MA_BDONG trong bảng CHI_TIET_TUTI
                                                    String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));

                                                    //TODO lấy tên ảnh
                                                    cusorGetAnhTuTi = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI));
                                                    if (cusorGetAnhTuTi.moveToFirst()) {
                                                        String TEN_ANH = cusorGetAnhTuTi.getString(cusorGetAnhTuTi.getColumnIndex("TEN_ANH"));
                                                        tthtTuTiEntity.setTEN_ANH_TU_TI(TEN_ANH);
                                                        cusorGetAnhTuTi.close();
                                                    }

                                                    cusorGetAnhMachNhiThu = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
                                                    if (cusorGetAnhMachNhiThu.moveToFirst()) {
                                                        String TEN_ANH = cusorGetAnhMachNhiThu.getString(cusorGetAnhMachNhiThu.getColumnIndex("TEN_ANH"));
                                                        tthtTuTiEntity.setTEN_ANH_MACH_NHI_THU(TEN_ANH);
                                                        cusorGetAnhMachNhiThu.close();
                                                    }

                                                    cusorGetAnhMachCongTo = (IS_TU.equals("true") ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
                                                    if (cusorGetAnhMachCongTo.moveToFirst()) {
                                                        String TEN_ANH = cusorGetAnhMachCongTo.getString(cusorGetAnhMachCongTo.getColumnIndex("TEN_ANH"));
                                                        tthtTuTiEntity.setTEN_ANH_MACH_CONG_TO(TEN_ANH);
                                                        cusorGetAnhMachCongTo.close();
                                                    }

                                                    tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
                                                    tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
                                                    tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
                                                    tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
                                                    tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
                                                    tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
                                                    tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
                                                    tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
                                                    tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
                                                    tthtTuTiEntity.setTRANG_THAI(cTuTi.getInt(cTuTi.getColumnIndex("TRANG_THAI")));
                                                    tthtTuTiEntity.setIS_TU(IS_TU);
                                                    tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
                                                    tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
                                                    tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
                                                    tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
                                                    tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
                                                    tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
                                                    tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
                                                    tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
                                                    tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
                                                    tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
                                                    tthtTuTiEntityList.add(tthtTuTiEntity);
                                                }
                                            }
                                            while (cTuTi.moveToNext());
                                        }
                                    } finally {
                                        if (cTuTi != null)
                                            cTuTi.close();
                                    }
                                }
                            } finally {
                                if (cBBanTuTi != null)
                                    cBBanTuTi.close();
                            }

                            TthtKHangEntity tthtKHangEntityTreo = new TthtKHangEntity();
                            tthtKHangEntityTreo.setStt(stt);
                            tthtKHangEntityTreo.setTthtBBanEntity(tthtBBanEntity);
                            tthtKHangEntityTreo.setTthtCtoEntity(tthtCtoEntity);
                            if (tthtBBanTuTiEntity != null) {
                                tthtKHangEntityTreo.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
                            }
                            if (tthtTuTiEntityList != null) {
                                tthtKHangEntityTreo.setTthtTuTiEntity(tthtTuTiEntityList);
                            }

                            lstKhangHangTreo.add(tthtKHangEntityTreo);
                        } while (cKHangTreo.moveToNext());
                    }
                }
            } finally {
                if (cKHangTreo != null) {
                    cKHangTreo.close();
                }
            }
            //TODO sort by MaGCS
            Collections.sort(lstKhangHangTreo, new KHCompairByMaGCS());
            for (int i = 0; i < lstKhangHangTreo.size(); i++) {
                lstKhangHangTreo.get(i).setStt(i + 1);
            }

            lstKhangHangThao.clear();
            try {
                if (cKHangThao != null) {
                    if (cKHangThao.moveToFirst()) {
                        do {
                            stt++;
                            //TODO get data TthtBBanEntity
                            TthtBBanEntity tthtBBanEntity = new TthtBBanEntity();
                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setID_BBAN_TRTH(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")));
                            tthtBBanEntity.setMA_CNANG(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
                            tthtBBanEntity.setMA_DDO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_DDO")));
                            tthtBBanEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
                            tthtBBanEntity.setMA_LDO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_LDO")));
                            tthtBBanEntity.setMA_NVIEN(cKHangThao.getString(cKHangThao.getColumnIndex("BUNDLE_MA_NVIEN")));
                            tthtBBanEntity.setMA_YCAU_KNAI(cKHangThao.getString(cKHangThao.getColumnIndex("MA_YCAU_KNAI")));
                            tthtBBanEntity.setNGAY_SUA(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
                            tthtBBanEntity.setNGAY_TAO(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
                            tthtBBanEntity.setNGAY_TRTH(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TRTH")));
                            tthtBBanEntity.setNGUOI_SUA(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
                            tthtBBanEntity.setNGUOI_TAO(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
                            tthtBBanEntity.setSO_BBAN(cKHangThao.getString(cKHangThao.getColumnIndex("SO_BBAN")));
                            tthtBBanEntity.setTRANG_THAI(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI")));
                            tthtBBanEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")));
                            tthtBBanEntity.setId_BBAN_CONGTO(cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_CONGTO")));
                            tthtBBanEntity.setLOAI_BBAN(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_BBAN")));
                            tthtBBanEntity.setTEN_KHANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_KHANG")));
                            tthtBBanEntity.setDCHI_HDON(cKHangThao.getString(cKHangThao.getColumnIndex("DCHI_HDON")));
                            tthtBBanEntity.setDTHOAI(cKHangThao.getString(cKHangThao.getColumnIndex("DTHOAI")));
                            tthtBBanEntity.setMA_GCS_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_GCS_CTO")));
                            tthtBBanEntity.setMA_TRAM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TRAM")));
                            tthtBBanEntity.setMA_HDONG(cKHangThao.getString(cKHangThao.getColumnIndex("MA_HDONG")));

                            //TODO get data TthtCtoEntity
                            TthtCtoEntity tthtCtoEntity = new TthtCtoEntity();
                            tthtCtoEntity.setMA_DVIQLY(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NVIEN")));
                            tthtCtoEntity.setID_BBAN_TRTH(cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TRTH")) : 0);
                            tthtCtoEntity.setMA_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CTO")));
                            tthtCtoEntity.setSO_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("SO_CTO")));
                            tthtCtoEntity.setLAN(cKHangThao.getString(cKHangThao.getColumnIndex("LAN")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("LAN")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("LAN")) : 0);
                            String MA_BDONG_CTO = cKHangThao.getString(cKHangThao.getColumnIndex("MA_BDONG"));
                            tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
                            tthtCtoEntity.setNGAY_BDONG(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_BDONG")));
                            tthtCtoEntity.setMA_CLOAI(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CLOAI")));
                            tthtCtoEntity.setLOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("LOAI_CTO")));
//                            tthtCtoEntity.setVITRI_TREO_THAO(cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")) != null
//                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("VTRI_TREO")).isEmpty()
//                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("VTRI_TREO")) : 0);
                            tthtCtoEntity.setMA_SOCBOOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCBOOC")));
                            tthtCtoEntity.setSOVIEN_CBOOC(cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCBOOC")));
                            tthtCtoEntity.setLOAI_HOM(cKHangThao.getInt(cKHangThao.getColumnIndex("LOAI_HOM")));
                            tthtCtoEntity.setMA_SOCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_SOCHOM")));
                            tthtCtoEntity.setSO_VIENCHOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SO_VIENCHOM")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SO_VIENCHOM")) : 0);
                            tthtCtoEntity.setHS_NHAN(cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("HS_NHAN")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN")) : 1);
                            tthtCtoEntity.setNGAY_TAO(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_TAO")));
                            tthtCtoEntity.setNGUOI_TAO(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_TAO")));
                            tthtCtoEntity.setNGAY_SUA(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_SUA")));
                            tthtCtoEntity.setNGUOI_SUA(cKHangThao.getString(cKHangThao.getColumnIndex("NGUOI_SUA")));
                            tthtCtoEntity.setMA_CNANG(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CNANG")));
                            tthtCtoEntity.setSO_TU(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU")));
                            tthtCtoEntity.setSO_TI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI")));
                            tthtCtoEntity.setSO_COT(cKHangThao.getString(cKHangThao.getColumnIndex("SO_COT")));
                            tthtCtoEntity.setSO_HOM(cKHangThao.getString(cKHangThao.getColumnIndex("SO_HOM")));
                            tthtCtoEntity.setCHI_SO(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO")));
                            tthtCtoEntity.setNGAY_KDINH(cKHangThao.getString(cKHangThao.getColumnIndex("NGAY_KDINH")));
                            tthtCtoEntity.setNAM_SX(cKHangThao.getString(cKHangThao.getColumnIndex("NAM_SX")));
                            tthtCtoEntity.setTEM_CQUANG(cKHangThao.getString(cKHangThao.getColumnIndex("TEM_CQUANG")));
                            tthtCtoEntity.setMA_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("MA_CHIKDINH")));
                            tthtCtoEntity.setMA_TEM(cKHangThao.getString(cKHangThao.getColumnIndex("MA_TEM")));
                            tthtCtoEntity.setSOVIEN_CHIKDINH(cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) != null
                                    && !cKHangThao.getString(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
                                    ? cKHangThao.getInt(cKHangThao.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
                            tthtCtoEntity.setDIEN_AP(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP")));
                            tthtCtoEntity.setDONG_DIEN(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN")));
                            tthtCtoEntity.setHANGSO_K(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K")));
                            tthtCtoEntity.setMA_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("MA_NUOC")));
                            tthtCtoEntity.setTEN_NUOC(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_NUOC")));
                            tthtCtoEntity.setSO_KIM_NIEM_CHI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_KIM_NIEM_CHI")));
                            tthtCtoEntity.setTTRANG_NPHONG(cKHangThao.getString(cKHangThao.getColumnIndex("TTRANG_NPHONG")));

                            int ID_CHITIET_CTO = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_CHITIET_CTO"));
                            cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
                            try {
                                if (cusorGetAnhCongTo.moveToFirst()) {
                                    String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
                                    tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
                                }
                            } finally {
                                if (cusorGetAnhCongTo != null) cusorGetAnhCongTo.close();
                            }

                            tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
                            tthtCtoEntity.setTEN_LOAI_CTO(cKHangThao.getString(cKHangThao.getColumnIndex("TEN_LOAI_CTO")).trim());

                            tthtCtoEntity.setPHUONG_THUC_DO_XA(cKHangThao.getString(cKHangThao.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
                            tthtCtoEntity.setGHI_CHU(cKHangThao.getString(cKHangThao.getColumnIndex("GHI_CHU")).trim());
                            int ID_BBAN_TUTI = cKHangThao.getInt(cKHangThao.getColumnIndex("ID_BBAN_TUTI"));
                            tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                            tthtCtoEntity.setTRANG_THAI_DU_LIEU(cKHangThao.getInt(cKHangThao.getColumnIndex("TRANG_THAI_DU_LIEU")));

                            tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TU_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TU_SAULAP_TUTI")));
                            tthtCtoEntity.setSO_TI_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("SO_TI_SAULAP_TUTI")));
                            tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("CHI_SO_SAULAP_TUTI")));
                            tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
                            tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
                            tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cKHangThao.getString(cKHangThao.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
                            tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cKHangThao.getInt(cKHangThao.getColumnIndex("CAP_CX_SAULAP_TUTI")));
                            //TODO get data TU TI
                            TthtBBanTuTiEntity tthtBBanTuTiEntity = null;
                            ArrayList<TthtTuTiEntity> tthtTuTiEntityList = null;
                            cBBanTuTi = connection.getBBanTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                            try {
                                if (cBBanTuTi.moveToFirst()) {
                                    tthtBBanTuTiEntity = new TthtBBanTuTiEntity();
                                    tthtBBanTuTiEntity.setMA_DVIQLY(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_NVIEN")));
                                    ID_BBAN_TUTI = cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_TUTI"));
                                    tthtBBanTuTiEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
                                    tthtBBanTuTiEntity.setMA_DDO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_DDO")));
                                    tthtBBanTuTiEntity.setSO_BBAN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("SO_BBAN")));
                                    tthtBBanTuTiEntity.setNGAY_TRTH(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NGAY_TRTH")));
                                    tthtBBanTuTiEntity.setMA_NVIEN(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("BUNDLE_MA_NVIEN")));
                                    tthtBBanTuTiEntity.setTRANG_THAI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("TRANG_THAI")));
                                    tthtBBanTuTiEntity.setTEN_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("TEN_KHANG")));
                                    tthtBBanTuTiEntity.setDCHI_HDON(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DCHI_HDON")));
                                    tthtBBanTuTiEntity.setDTHOAI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("DTHOAI")));
                                    tthtBBanTuTiEntity.setMA_GCS_CTO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_GCS_CTO")));
                                    tthtBBanTuTiEntity.setMA_TRAM(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_TRAM")));
                                    tthtBBanTuTiEntity.setLY_DO_TREO_THAO(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("LY_DO_TREO_THAO")));
                                    tthtBBanTuTiEntity.setMA_KHANG(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("MA_KHANG")));
                                    tthtBBanTuTiEntity.setID_BBAN_WEB_TUTI(cBBanTuTi.getInt(cBBanTuTi.getColumnIndex("ID_BBAN_WEB_TUTI")));
                                    tthtBBanTuTiEntity.setNVIEN_KCHI(cBBanTuTi.getString(cBBanTuTi.getColumnIndex("NVIEN_KCHI")));
                                    cBBanTuTi.close();
                                    Cursor cTuTi = connection.getTuTiWithID_BBAN_TUTI(ID_BBAN_TUTI);
                                    try {
                                        if (cTuTi != null) {
                                            tthtTuTiEntityList = new ArrayList<TthtTuTiEntity>();
                                            do {
                                                TthtTuTiEntity tthtTuTiEntity = new TthtTuTiEntity();

                                                //TODO từ ID_BBAN_TUTI ta lấy được MA_BDONG trong bảng DETAIL_CONGTO
                                                if (MA_BDONG_CTO.equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
                                                    String IS_TU = cTuTi.getString(cTuTi.getColumnIndex("IS_TU"));
                                                    //TODO MA_BDONG trong bảng CHI_TIET_TUTI
                                                    String MA_BDONG_TUTI = cTuTi.getString(cTuTi.getColumnIndex("MA_BDONG"));
                                                    tthtTuTiEntity.setMA_CLOAI(cTuTi.getString(cTuTi.getColumnIndex("MA_CLOAI")));
                                                    tthtTuTiEntity.setLOAI_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("LOAI_TU_TI")));
                                                    tthtTuTiEntity.setMO_TA(cTuTi.getString(cTuTi.getColumnIndex("MO_TA")));
                                                    tthtTuTiEntity.setSO_PHA(cTuTi.getInt(cTuTi.getColumnIndex("SO_PHA")));
                                                    tthtTuTiEntity.setTYSO_DAU(cTuTi.getString(cTuTi.getColumnIndex("TYSO_DAU")));
                                                    tthtTuTiEntity.setCAP_CXAC(cTuTi.getInt(cTuTi.getColumnIndex("CAP_CXAC")));
                                                    tthtTuTiEntity.setCAP_DAP(cTuTi.getInt(cTuTi.getColumnIndex("CAP_DAP")));
                                                    tthtTuTiEntity.setMA_NUOC(cTuTi.getString(cTuTi.getColumnIndex("MA_NUOC")));
                                                    tthtTuTiEntity.setMA_HANG(cTuTi.getString(cTuTi.getColumnIndex("MA_HANG")));
                                                    tthtTuTiEntity.setTRANG_THAI(cTuTi.getInt(cTuTi.getColumnIndex("TRANG_THAI")));
                                                    tthtTuTiEntity.setIS_TU(IS_TU);
                                                    tthtTuTiEntity.setID_BBAN_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_BBAN_TUTI")));
                                                    tthtTuTiEntity.setID_CHITIET_TUTI(cTuTi.getInt(cTuTi.getColumnIndex("ID_CHITIET_TUTI")));
                                                    tthtTuTiEntity.setSO_TU_TI(cTuTi.getString(cTuTi.getColumnIndex("SO_TU_TI")));
                                                    tthtTuTiEntity.setNUOC_SX(cTuTi.getString(cTuTi.getColumnIndex("NUOC_SX")));
                                                    tthtTuTiEntity.setSO_TEM_KDINH(cTuTi.getString(cTuTi.getColumnIndex("SO_TEM_KDINH")));
                                                    tthtTuTiEntity.setNGAY_KDINH(cTuTi.getString(cTuTi.getColumnIndex("NGAY_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_KDINH(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_KDINH")));
                                                    tthtTuTiEntity.setMA_CHI_HOP_DDAY(cTuTi.getString(cTuTi.getColumnIndex("MA_CHI_HOP_DDAY")));
                                                    tthtTuTiEntity.setSO_VONG_THANH_CAI(cTuTi.getInt(cTuTi.getColumnIndex("SO_VONG_THANH_CAI")));
                                                    tthtTuTiEntity.setTYSO_BIEN(cTuTi.getString(cTuTi.getColumnIndex("TYSO_BIEN")));
                                                    tthtTuTiEntity.setMA_BDONG(MA_BDONG_TUTI);
                                                    tthtTuTiEntity.setMA_DVIQLY(cTuTi.getString(cTuTi.getColumnIndex("MA_NVIEN")));
                                                    tthtTuTiEntityList.add(tthtTuTiEntity);
                                                }
                                            }
                                            while (cTuTi.moveToNext());
                                        }
                                    } finally {
                                        if (cTuTi != null) cTuTi.close();
                                    }
                                }
                            } finally {
                                if (cBBanTuTi != null) cBBanTuTi.close();
                            }


                            TthtKHangEntity tthtKHangEntityThao = new TthtKHangEntity();
                            tthtKHangEntityThao.setStt(stt);
                            tthtKHangEntityThao.setTthtBBanEntity(tthtBBanEntity);
                            tthtKHangEntityThao.setTthtCtoEntity(tthtCtoEntity);
                            if (tthtBBanTuTiEntity != null) {
                                tthtKHangEntityThao.setTthtBBanTuTiEntity(tthtBBanTuTiEntity);
                            }
                            if (tthtTuTiEntityList != null) {
                                tthtKHangEntityThao.setTthtTuTiEntity(tthtTuTiEntityList);
                            }

                            lstKhangHangThao.add(tthtKHangEntityThao);
                        } while (cKHangThao.moveToNext());

                    }
                }

            } finally {
                if (cKHangThao != null) {
                    cKHangThao.close();
                }
            }

            //TODO sort by MaGCS
            Collections.sort(lstKhangHangThao, new KHCompairByMaGCS());

            for (int i = 0; i < lstKhangHangThao.size(); i++) {
                lstKhangHangThao.get(i).setStt(i + 1);
            }
            Log.d(TAG, "doInBackground: ");

//            } catch (Exception ex) {
//                publishProgress(ex.getMessage());
//            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu\n" + values[0], Color.WHITE, "OK", Color.RED);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                int sizeListKHTreo = lstKhangHangTreo.size();
                int sizeListKHThao = lstKhangHangThao.size();

                if (sizeListKHTreo == 0 || sizeListKHThao == 0 || sizeListKHThao != sizeListKHTreo) {
                    throw new Exception("Không có dữ liệu");
                }

                for (int i = 0; i < sizeListKHTreo; i++) {
                    if (lstKhangHangTreo.get(i).getTthtBBanEntity().getID_BBAN_TRTH() != lstKhangHangThao.get(i).getTthtBBanEntity().getID_BBAN_TRTH()) {
                        throw new Exception("Lỗi dữ liệu, Số công tơ treo không tương xứng số công tơ tháo!");
                    }

                    if (lstKhangHangTreo.get(i).getTthtCtoEntity().getTRANG_THAI_DU_LIEU() == Integer.parseInt(TthtCommon.TRANG_THAI_DU_LIEU.DA_GHI.toString())
                            && lstKhangHangThao.get(i).getTthtCtoEntity().getTRANG_THAI_DU_LIEU() == Integer.parseInt(TthtCommon.TRANG_THAI_DU_LIEU.DA_GHI.toString())) {
                        lstDSoatTreo.add(lstKhangHangTreo.get(i));
                        lstDSoatThao.add(lstKhangHangThao.get(i));
                    }
                }


                //TODO sort by MaGCS
                Collections.sort(lstDSoatTreo, new KHCompairByMaGCS());
                Collections.sort(lstDSoatThao, new KHCompairByMaGCS());
                for (int i = 0; i < lstDSoatTreo.size(); i++) {
                    lstDSoatTreo.get(i).setStt(i + 1);
                    lstDSoatThao.get(i).setStt(i + 1);
                }

                isClickChooseArrayDoiSoat = new boolean[lstDSoatThao.size()];

//                //TODO set Adapter
//                adapterKH = new TthtKHAdapter(lstKhangHangTreo, lstKhangHangThao, TthtCommon.arrMaBDong[spFilter.getSelectedItemPosition()]);
//                rvKH.setAdapter(adapterKH);
//                rvKH.setHasFixedSize(true);
//                rvKH.invalidate();
//
//                //TODO fill text
//                if (!TthtCommon.getTenTramSelected().equals("")) {
//                    auEtMaTram.setText(TthtCommon.getMaTramSelected());
//                    tvTenTram.setText(TthtCommon.getTenTramSelected());
//                }
//
//                if (idBBTrTh != 0) {
//                    //TODO set position recyclerview and refresh data when idbBBTrTh change
//                    int posSelect = adapterKH.getPosIDBBanTrThao(idBBTrTh);
//                    adapterKH.setPosSelect(posSelect);
//                    adapterKH.notifyDataSetChanged();
//                    rvKH.invalidate();


                //TODO set Adapter
                adapterDoiSoat = new TthtDoiSoatAdapter(lstDSoatTreo, lstDSoatThao);
                rvKH.setAdapter(adapterDoiSoat);
                rvKH.setHasFixedSize(true);
                rvKH.invalidate();

                //TODO fill text
                if (!TthtCommon.getTenTramSelected().equals("")) {
                    auEtMaTram.setText(TthtCommon.getMaTramSelected());
                    tvTenTram.setText(TthtCommon.getTenTramSelected());
                }
                setTitleThongKeDateDoiSoat(adapterDoiSoat.getItemCount() * 2, adapterDoiSoat.getItemCount() * 2, 0);

            } catch (Exception ex) {
                Common.showAlertDialogGreen(TthtMainFragment.this.getActivity(), "Lỗi", Color.RED,
                        "Lỗi fill giá trị lên recyclerView\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
            }
        }

    }

    /**
     * @param typeSetData :
     *                    1: full
     *                    2: has data and not submit
     */
    private void setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL typeSetData) {
        new AsyncSetDataOnRecyclerView(typeSetData).execute();
    }

    private void setDataDSOnRecyclerView() {
        try {

            Log.d(TAG, "doInBackground: " + lstPhieuTreoThao);
            lstDSoatTreo.clear();
            lstDSoatThao.clear();
            setDataKHOnRecycleView(TthtCommon.FILTER_DATA_FILL.ALL);
//                getDataDoiSoat();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "setDataDSOnRecyclerView: " + ex.getMessage());
        }
//        new AsyncSetDataDSOnRecyclerView().execute();
    }
    //endregion

}


