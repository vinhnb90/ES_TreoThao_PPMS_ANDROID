package com.es.tungnv.fragments;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.entity.EsspEntityDuToanThiCong;
import com.es.tungnv.entity.EsspEntityHoSo;
import com.es.tungnv.entity.EsspEntityHoSoThiCong;
import com.es.tungnv.entity.EsspEntityPhuongAn;
import com.es.tungnv.entity.EsspEntityVatTuDuToan;
import com.es.tungnv.entity.EsspEntityVatTuNHT;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUNGNV on 9/27/2016.
 */
public class EsspNghiemThuFragment extends Fragment implements View.OnClickListener {

    //region Khai báo biến
    private View rootView;

    private EditText etSearch;
    private ImageButton ibClear;
    private RecyclerView rvKH, rvDuToan;
    private Button btLayHS, btGuiHS, btXoaHS;

    public static ArrayList<String> lstHoSoId = new ArrayList<>();
    public static ArrayList<String> lstPos = new ArrayList<>();

    private static EsspSqliteConnection connection;
    private EsspAsyncCallWSJson ac;
    private ArrayList<JSONObject> lstHoSo;
    private ProgressDialog progressDialog;
    private List<EsspEntityHoSoThiCong> lstKhangHang;
    private List<EsspEntityDuToanThiCong> lstDuToan;
    private EsspHoSoAdapter adapterHoSo;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private EsspDuToanAdapter esspDuToanAdapter;

    private int countHS = 0;

    private boolean checkGetData = false;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private int maxSize = 0;
    private int countFileSent = 0;
    private float snapProgresBar = 1f;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //endregion

    //region Khởi tạo
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.essp_fragment_main, null);
            connection = EsspSqliteConnection.getInstance(this.getActivity());
            ac = EsspAsyncCallWSJson.getInstance();
            lstHoSoId = new ArrayList<>();
            initComponent(rootView);
            LoadListViewData();

            verifyStoragePermissions(this.getActivity());
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED,
                    new StringBuilder("Lỗi khởi tạo fragment: ").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lstHoSoId.clear();
        lstPos.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        lstHoSoId.clear();
        lstPos.clear();
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    private void initComponent(View rootView) {
        etSearch = (EditText) rootView.findViewById(R.id.essp_fragment_main_et_search);
        ibClear = (ImageButton) rootView.findViewById(R.id.essp_fragment_main_ib_clear);
        rvKH = (RecyclerView) rootView.findViewById(R.id.essp_fragment_main_rv_kh);
        btLayHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_layhs);
        btGuiHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_guihs);
        btXoaHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_xoahs);

        layoutManager = new LinearLayoutManager(this.getActivity());
        rvKH.setHasFixedSize(true);
        rvKH.setLayoutManager(layoutManager);

        ibClear.setOnClickListener(this);
        btLayHS.setOnClickListener(this);
        btGuiHS.setOnClickListener(this);
        btXoaHS.setOnClickListener(this);

        etSearch.post(new Runnable() {
            @Override
            public void run() {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            List<EsspEntityHoSoThiCong> data = new ArrayList<EsspEntityHoSoThiCong>();
                            String query = Common.removeAccent(s.toString().trim().toLowerCase());
                            for (EsspEntityHoSoThiCong entity : lstKhangHang) {
                                if (Common.removeAccent(entity.getCMIS_MA_YCAU_KNAI().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getMA_HDONG().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getMA_KHANG().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getMA_GCS().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getMA_CTO().toLowerCase()).contains(query)) {
                                    data.add(entity);
                                }
                            }
                            adapterHoSo.updateList(data);
                        } catch (Exception ex) {
                            Toast.makeText(EsspNghiemThuFragment.this.getActivity(),
                                    new StringBuilder("Lỗi tìm kiếm:\n").append(ex.toString()).toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.essp_fragment_main_ib_clear:
                etSearch.setText("");
                break;
            case R.id.essp_fragment_main_bt_layhs:
                if (!Common.isNetworkOnline(EsspNghiemThuFragment.this.getActivity())) {
                    Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstHoSo = new ArrayList<>();
                    progressDialog = ProgressDialog.show(EsspNghiemThuFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true, false);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                lstHoSo = ac.WS_GET_HO_SO_THI_CONG_CALL(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME());
                                if (lstHoSo != null) {
                                    checkGetData = false;
                                } else {
                                    checkGetData = true;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handlerGetHoSo.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
                break;
            case R.id.essp_fragment_main_bt_guihs:
                guiHoSo(v);
                break;
            case R.id.essp_fragment_main_bt_xoahs:
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                    builder.setMessage("Bạn có chắc chắn muốn xóa?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = lstHoSoId.size() - 1; i >= 0; i--) {
                                String sHOSO_ID = lstHoSoId.get(i);
                                connection.deleteDuToanThiCong(sHOSO_ID);
                                if (connection.deleteHoSoThiCong(sHOSO_ID) != -1) {
                                    adapterHoSo.removeItem(Integer.parseInt(lstPos.get(i)));
                                }
                            }
                            lstHoSoId.clear();
                            lstPos.clear();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                            "Thông báo", Color.RED, new StringBuilder("Lỗi xóa hồ sơ:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
                }
                break;
        }
    }

    private void guiHoSo(View v) {
        try {
            progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Đang gửi hồ sơ ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();

            progressBarStatus = 0;
            maxSize = lstHoSoId.size();
            snapProgresBar = (float) maxSize / 100f;

            Thread threadHS = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    try {
                        Thread.sleep(50);
                        JSONArray jsonArrHS = new JSONArray();
                        JSONArray jsonArrDT = new JSONArray();
                        for (int i = 0; i < lstHoSoId.size(); i++) {
                            EsspEntityHoSoThiCong entityHoSoThiCong = connection.getHoSoNThuByID(Integer.parseInt(lstHoSoId.get(i)));
                            int TINH_TRANG = entityHoSoThiCong.getTINH_TRANG();
                            if (TINH_TRANG == 1 && !entityHoSoThiCong.getCHI_SO_TREO().isEmpty()) {
                                Cursor c = connection.getDataDutoanNThuByID(entityHoSoThiCong.getHoSoId());
                                if (c.moveToFirst()) {
                                    do {
                                        jsonArrDT.put(DATAtoJSONDuToan(c.getInt(c.getColumnIndex("DUTOAN_ID")),
                                                c.getFloat(c.getColumnIndex("THUC_TE"))));
                                    } while (c.moveToNext());
                                }
                                jsonArrHS.put(DATAtoJSONHoSoThiCong(entityHoSoThiCong.getHoSoId(),
                                        entityHoSoThiCong.getMA_DVIQLY(), jsonArrDT,
                                        entityHoSoThiCong.getCHI_SO_TREO(), entityHoSoThiCong.getKIMNIEMCHISO()));
                            }
                        }
                        ArrayList<JSONObject> result = ac.WS_UPDATE_HO_SO_THI_CONG_CALL(jsonArrHS);
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++){
                                countFileSent++;
                                JSONObject jObject = result.get(i);
                                if(jObject.getString("RESULT").equals("OK")){
                                    countHS++;
                                    connection.updateTinhTrangHSThiCong(jObject.getInt("ID"), 2);
                                }
                                progressBarStatus = (int)(countFileSent/snapProgresBar);
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.setProgress(progressBarStatus);
                                    }
                                });
                            }
                        } else {
                            countHS = -1;
                        }
                    } catch (final Exception e) {
                        EsspNghiemThuFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, new StringBuilder("Gửi thông tin khảo sát thất bại\n")
                                                .append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                            }
                        });
                    }
                    handlerSentHoSo.sendEmptyMessage(0);
                }
            });
            threadHS.start();
        } catch (Exception ex) {

        }
    }

    public JSONObject DATAtoJSONDuToan(int DUTOAN_ID, float THUC_TE) {
        String sDUTOAN_ID = "DUTOAN_ID";
        String sTHUC_TE = "THUC_TE";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sDUTOAN_ID, DUTOAN_ID);
            jsonObject.accumulate(sTHUC_TE, THUC_TE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONHoSoThiCong(int HOSO_ID, String MA_DVIQLY, JSONArray List_DuToanVatTU, String CHI_SO_TREO,
                                            String KIM_NIEM_CHI_SO) {
        String sHOSO_ID = "HOSO_ID";
        String sMA_DVIQLY = "MA_DVIQLY";
        String sList_DuToanVatTU = "List_DuToanVatTU";
        String sCHI_SO_TREO = "CHI_SO_TREO";
        String sKIM_NIEM_CHI_SO = "KIM_NIEM_CHI_SO";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sMA_DVIQLY, MA_DVIQLY);
            jsonObject.accumulate(sList_DuToanVatTU, List_DuToanVatTU);
            jsonObject.accumulate(sCHI_SO_TREO, CHI_SO_TREO);
            jsonObject.accumulate(sKIM_NIEM_CHI_SO, KIM_NIEM_CHI_SO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    //endregion

    //region Xử lý handler
    private Handler handlerGetHoSo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (checkGetData) {
                Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không có hồ sơ mới", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    int count = 0;
                    for (JSONObject map : lstHoSo) {
                        int HoSoId = map.getString("HoSoId").equals("null") ? 0 : map.getInt("HoSoId");
                        String MA_DVIQLY = map.getString("MA_DVIQLY").equals("null") ? "" : map.getString("MA_DVIQLY");
                        String CMIS_MA_YCAU_KNAI = map.getString("CMIS_MA_YCAU_KNAI").equals("null") ? "" : map.getString("CMIS_MA_YCAU_KNAI");
                        int isNHIEUBIEUGIA = map.getString("isNHIEUBIEUGIA").equals("true") ? 1 : 0;
                        String KIMNIEMCHISO = map.getString("KIMNIEMCHISO").equals("null") ? "" : map.getString("KIMNIEMCHISO");
                        String MA_HDONG = map.getString("MA_HDONG").equals("null") ? "" : map.getString("MA_HDONG");
                        String MA_KHANG = map.getString("MA_KHANG").equals("null") ? "" : map.getString("MA_KHANG");
                        String TEN_KHANG = map.getString("TEN_KHANG").equals("null") ? "" : map.getString("TEN_KHANG");
                        String DIA_CHI = map.getString("DIA_CHI").equals("null") ? "" : map.getString("DIA_CHI");
                        String SO_CMT = map.getString("SO_CMT").equals("null") ? "" : map.getString("SO_CMT");
                        String DIEN_THOAI = map.getString("DIEN_THOAI").equals("null") ? "" : map.getString("DIEN_THOAI");
                        int TINH_TRANG = 1;
                        String MA_GCS = map.getString("MA_GCS").equals("null") ? "" : map.getString("MA_GCS");
                        String LYDO_TREOTHAO = map.getString("LYDO_TREOTHAO").equals("null") ? "" : map.getString("LYDO_TREOTHAO");
                        String LOAI_CTO = map.getString("LOAI_CTO").equals("null") ? "" : map.getString("LOAI_CTO");
                        String MA_CTO = map.getString("MA_CTO").equals("null") ? "" : map.getString("MA_CTO");
                        String MA_NUOC = map.getString("MA_NUOC").equals("null") ? "" : map.getString("MA_NUOC");
                        String TEN_NUOC = map.getString("TEN_NUOC").equals("null") ? "" : map.getString("TEN_NUOC");
                        int NAM_SX = map.getString("NAM_SX").equals("null") ? 2000 : map.getInt("NAM_SX");
                        String DONG_DIEN = map.getString("DONG_DIEN").equals("null") ? "" : map.getString("DONG_DIEN");
                        String DIEN_AP = map.getString("DIEN_AP").equals("null") ? "" : map.getString("DIEN_AP");
                        String HANGSO_K = map.getString("HANGSO_K").equals("null") ? "" : map.getString("HANGSO_K");
                        String CCX = map.getString("CCX").equals("null") ? "" : map.getString("CCX");
                        String TEM_CQUANG = map.getString("TEM_CQUANG").equals("null") ? "" : map.getString("TEM_CQUANG");
                        int SOLAN_CBAO = map.getString("SOLAN_CBAO").equals("null") ? 0 : map.getInt("SOLAN_CBAO");
                        String HS_NHAN = map.getString("HS_NHAN").equals("null") ? "" : map.getString("HS_NHAN");
                        String SO_CTO = map.getString("SO_CTO").equals("null") ? "" : map.getString("SO_CTO");
                        String LAPQUA_TI = map.getString("LAPQUA_TI").equals("null") ? "" : map.getString("LAPQUA_TI");
                        String LAPQUA_TU = map.getString("LAPQUA_TU").equals("null") ? "" : map.getString("LAPQUA_TU");
                        String HS_NHAN_LUOI = map.getString("HS_NHAN_LUOI").equals("null") ? "" : map.getString("HS_NHAN_LUOI");
                        String P_MAX = map.getString("P_MAX").equals("null") ? "" : map.getString("P_MAX");
                        String TD_P_MAX = map.getString("TD_P_MAX").equals("null") ? "" : map.getString("TD_P_MAX");
                        String H1_P_MAX = map.getString("H1_P_MAX").equals("null") ? "" : map.getString("H1_P_MAX");
                        String TD_H1_P_MAX = map.getString("TD_H1_P_MAX").equals("null") ? "" : map.getString("TD_H1_P_MAX");
                        String TTRANG_CHI_NIEMPHONG = map.getString("TTRANG_CHI_NIEMPHONG").equals("null") ? "" : map.getString("TTRANG_CHI_NIEMPHONG");
                        String MASO_CHI_KDINH = map.getString("MASO_CHI_KDINH").equals("null") ? "" : map.getString("MASO_CHI_KDINH");
                        String SOVIEN_CHIKDINH = map.getString("SOVIEN_CHIKDINH").equals("null") ? "" : map.getString("SOVIEN_CHIKDINH");
                        String MASO_CHICONGQUANG = map.getString("MASO_CHICONGQUANG").equals("null") ? "" : map.getString("MASO_CHICONGQUANG");
                        String SOVIEN_CHICONGQUANG = map.getString("SOVIEN_CHICONGQUANG").equals("null") ? "" : map.getString("SOVIEN_CHICONGQUANG");
                        String MASO_CHIHOM = map.getString("MASO_CHIHOM").equals("null") ? "" : map.getString("MASO_CHIHOM");
                        String SOVIEN_CHIHOM = map.getString("SOVIEN_CHIHOM").equals("null") ? "" : map.getString("SOVIEN_CHIHOM");
                        String MASO_TEMKDINH = map.getString("MASO_TEMKDINH").equals("null") ? "" : map.getString("MASO_TEMKDINH");
                        String NGAY_LAP = map.getString("NGAY_LAP").equals("null") ? "" : map.getString("NGAY_LAP");
                        String NGUOI_LAP = map.getString("NGUOI_LAP").equals("null") ? "" : map.getString("NGUOI_LAP");
                        String NGAY_KDINH_FORMAT = map.getString("NGAY_KDINH_FORMAT").equals("null") ? "" : map.getString("NGAY_KDINH_FORMAT");
                        String TEN_NHAN_VIEN = map.getString("TEN_NHAN_VIEN").equals("null") ? "" : map.getString("TEN_NHAN_VIEN");
                        String SO_BIEN_BAN = map.getString("SO_BIEN_BAN").equals("null") ? "" : map.getString("SO_BIEN_BAN");
                        String USER_NAME = map.getString("USER_NAME").equals("null") ? "" : map.getString("USER_NAME");
                        String lstVTu = map.getString("List_DuToanVatTU");
                        String CHI_SO_TREO = "";
                        JSONArray jaDuToan = null;
                        if (!lstVTu.equals("null") && !lstVTu.isEmpty())
                            jaDuToan = new JSONArray(lstVTu);

                        if (connection.insertDataHosoNThu(HoSoId, isNHIEUBIEUGIA, KIMNIEMCHISO, MA_HDONG, MA_KHANG, TEN_KHANG,
                                DIA_CHI, SO_CMT, DIEN_THOAI, MA_GCS, LYDO_TREOTHAO, LOAI_CTO, MA_CTO, MA_NUOC, TEN_NUOC,
                                NAM_SX, DONG_DIEN, DIEN_AP, HANGSO_K, CCX, TEM_CQUANG,
                                SOLAN_CBAO, HS_NHAN, SO_CTO, LAPQUA_TI, LAPQUA_TU, HS_NHAN_LUOI, P_MAX, TD_P_MAX, H1_P_MAX,
                                TD_H1_P_MAX, TTRANG_CHI_NIEMPHONG, MASO_CHI_KDINH, SOVIEN_CHIKDINH,
                                MASO_CHICONGQUANG, SOVIEN_CHICONGQUANG, MASO_CHIHOM, SOVIEN_CHIHOM,
                                MASO_TEMKDINH, NGAY_LAP, NGUOI_LAP, MA_DVIQLY, CMIS_MA_YCAU_KNAI,
                                NGAY_KDINH_FORMAT, TEN_NHAN_VIEN, SO_BIEN_BAN, USER_NAME, CHI_SO_TREO, TINH_TRANG) != -1) {
                            if (jaDuToan != null && jaDuToan.length() > 0) {
                                for (int i = 0; i < jaDuToan.length(); i++) {
                                    JSONObject joDuToan = jaDuToan.getJSONObject(i);
                                    if (connection.insertDataDutoanNThu(joDuToan.getInt("DUTOAN_ID"), joDuToan.getInt("HOSO_ID"),
                                            joDuToan.getString("MA_DVIQLY"), joDuToan.getString("MA_VTU"),
                                            Float.parseFloat(joDuToan.getString("SO_LUONG").equals("null") ? "0" : joDuToan.getString("SO_LUONG")),
                                            joDuToan.getInt("SO_HUU"), joDuToan.getInt("STT"), joDuToan.getString("GHI_CHU"),
                                            joDuToan.getInt("TT_TUTUC"), joDuToan.getInt("TT_THUHOI"),
                                            Float.parseFloat(joDuToan.getString("SO_LUONG").equals("null") ? "0" : joDuToan.getString("SO_LUONG")),
                                            Float.parseFloat(joDuToan.getString("CHENH_LECH").equals("null") ? "0" : joDuToan.getString("CHENH_LECH")),
                                            joDuToan.getString("TEN_VTU"), joDuToan.getString("DON_VI_TINH"), joDuToan.getString("KHTT")) != -1) {

                                    }
                                }
                            }
                            ac.WS_UPDATE_TINH_TRANG_THI_CONG_CALL(HoSoId, 1);
                            count++;
                        }
                    }
                    LoadListViewData();
                    if (lstHoSo.size() > 0)
                        Toast.makeText(EsspNghiemThuFragment.this.getActivity(),
                                new StringBuilder("Lấy thành công ").append(count).append("/")
                                        .append(lstHoSo.size()).append(" hồ sơ").toString(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                            "Thông báo", Color.RED, new StringBuilder("Lỗi tải hồ sơ:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
                }
            }
            progressDialog.dismiss();
        }
    };

    private Handler handlerSentHoSo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private void thongBaoGuiHS() {
        StringBuilder message = new StringBuilder();
        if(countHS == -1)
            message.append("Biên bản đã gửi không thể gửi lại");
        else
            message.append("Bạn đã gửi thành công:\n")
                .append(new StringBuilder(String.valueOf(countHS)).append("/").
                                append(lstHoSoId.size()).append(" biên bản\n").toString());
        Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                "Thông báo", Color.WHITE, message.toString(), Color.WHITE, "OK", Color.WHITE);
        countHS = 0;
        progressDialog.dismiss();
    }
    //endregion

    //region Khởi tạo dữ liệu
    private void LoadListViewData() {
        try {
            lstHoSo = new ArrayList<>();
            lstKhangHang = new ArrayList<>();
            Cursor c = connection.getDataHosoNThu(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME());
            if (c.moveToFirst()) {
                int stt = 0;
                do {
                    stt++;
                    EsspEntityHoSoThiCong entity = new EsspEntityHoSoThiCong();
                    try {
                        entity.setHoSoId(Integer.parseInt(c.getString(c.getColumnIndex("HoSoId"))));
                    } catch (Exception ex) {
                    }
                    entity.setSTT(stt);
                    entity.setIsNHIEUBIEUGIA(c.getInt(c.getColumnIndex("isNHIEUBIEUGIA")));
                    entity.setKIMNIEMCHISO(c.getString(c.getColumnIndex("KIMNIEMCHISO")) == null ? "" : c.getString(c.getColumnIndex("KIMNIEMCHISO")));
                    entity.setMA_HDONG(c.getString(c.getColumnIndex("MA_HDONG")) == null ? "" : c.getString(c.getColumnIndex("MA_HDONG")));
                    entity.setMA_KHANG(c.getString(c.getColumnIndex("MA_KHANG")) == null ? "" : c.getString(c.getColumnIndex("MA_KHANG")));
                    entity.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")) == null ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
                    entity.setDIA_CHI(c.getString(c.getColumnIndex("DIA_CHI")) == null ? "" : c.getString(c.getColumnIndex("DIA_CHI")));
                    entity.setSO_CMT(c.getString(c.getColumnIndex("SO_CMT")) == null ? "" : c.getString(c.getColumnIndex("SO_CMT")));
                    entity.setDIEN_THOAI(c.getString(c.getColumnIndex("DIEN_THOAI")) == null ? "" : c.getString(c.getColumnIndex("DIEN_THOAI")));
                    entity.setMA_GCS(c.getString(c.getColumnIndex("MA_GCS")) == null ? "" : c.getString(c.getColumnIndex("MA_GCS")));
                    entity.setLYDO_TREOTHAO(c.getString(c.getColumnIndex("LYDO_TREOTHAO")) == null ? "" : c.getString(c.getColumnIndex("LYDO_TREOTHAO")));
                    entity.setLOAI_CTO(c.getString(c.getColumnIndex("LOAI_CTO")) == null ? "" : c.getString(c.getColumnIndex("LOAI_CTO")));
                    entity.setMA_CTO(c.getString(c.getColumnIndex("MA_CTO")) == null ? "" : c.getString(c.getColumnIndex("MA_CTO")));
                    entity.setMA_NUOC(c.getString(c.getColumnIndex("MA_NUOC")) == null ? "" : c.getString(c.getColumnIndex("MA_NUOC")));
                    entity.setTEN_NUOC(c.getString(c.getColumnIndex("TEN_NUOC")) == null ? "" : c.getString(c.getColumnIndex("TEN_NUOC")));
                    entity.setNAM_SX(c.getString(c.getColumnIndex("NAM_SX")) == null ? 0 : c.getInt(c.getColumnIndex("NAM_SX")));
                    entity.setDONG_DIEN(c.getString(c.getColumnIndex("DONG_DIEN")) == null ? "" : c.getString(c.getColumnIndex("DONG_DIEN")));
                    entity.setDIEN_AP(c.getString(c.getColumnIndex("DIEN_AP")) == null ? "" : c.getString(c.getColumnIndex("DIEN_AP")));
                    entity.setHANGSO_K(c.getString(c.getColumnIndex("HANGSO_K")) == null ? "" : c.getString(c.getColumnIndex("HANGSO_K")));
                    entity.setCCX(c.getString(c.getColumnIndex("CCX")) == null ? "" : c.getString(c.getColumnIndex("CCX")));
                    entity.setTEM_CQUANG(c.getString(c.getColumnIndex("TEM_CQUANG")) == null ? "" : c.getString(c.getColumnIndex("TEM_CQUANG")));
                    entity.setSOLAN_CBAO(c.getString(c.getColumnIndex("SOLAN_CBAO")) == null ? 1 : c.getInt(c.getColumnIndex("SOLAN_CBAO")));
                    entity.setHS_NHAN(c.getString(c.getColumnIndex("HS_NHAN")) == null ? "" : c.getString(c.getColumnIndex("HS_NHAN")));
                    entity.setSO_CTO(c.getString(c.getColumnIndex("SO_CTO")) == null ? "" : c.getString(c.getColumnIndex("SO_CTO")));
                    entity.setLAPQUA_TI(c.getString(c.getColumnIndex("LAPQUA_TI")) == null ? "" : c.getString(c.getColumnIndex("LAPQUA_TI")));
                    entity.setLAPQUA_TU(c.getString(c.getColumnIndex("LAPQUA_TU")) == null ? "" : c.getString(c.getColumnIndex("LAPQUA_TU")));
                    entity.setHS_NHAN_LUOI(c.getString(c.getColumnIndex("HS_NHAN_LUOI")) == null ? "" : c.getString(c.getColumnIndex("HS_NHAN_LUOI")));
                    entity.setP_MAX(c.getString(c.getColumnIndex("P_MAX")) == null ? "" : c.getString(c.getColumnIndex("P_MAX")));
                    entity.setTD_P_MAX(c.getString(c.getColumnIndex("TD_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("TD_P_MAX")));
                    entity.setH1_P_MAX(c.getString(c.getColumnIndex("H1_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("H1_P_MAX")));
                    entity.setTD_H1_P_MAX(c.getString(c.getColumnIndex("TD_H1_P_MAX")) == null ? "" : c.getString(c.getColumnIndex("TD_H1_P_MAX")));
                    entity.setTTRANG_CHI_NIEMPHONG(c.getString(c.getColumnIndex("TTRANG_CHI_NIEMPHONG")) == null ? "" : c.getString(c.getColumnIndex("TTRANG_CHI_NIEMPHONG")));
                    entity.setMASO_CHI_KDINH(c.getString(c.getColumnIndex("MASO_CHI_KDINH")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHI_KDINH")));
                    entity.setSOVIEN_CHIKDINH(c.getString(c.getColumnIndex("SOVIEN_CHIKDINH")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHIKDINH")));
                    entity.setMASO_CHICONGQUANG(c.getString(c.getColumnIndex("MASO_CHICONGQUANG")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHICONGQUANG")));
                    entity.setSOVIEN_CHICONGQUANG(c.getString(c.getColumnIndex("SOVIEN_CHICONGQUANG")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHICONGQUANG")));
                    entity.setMASO_CHIHOM(c.getString(c.getColumnIndex("MASO_CHIHOM")) == null ? "" : c.getString(c.getColumnIndex("MASO_CHIHOM")));
                    entity.setSOVIEN_CHIHOM(c.getString(c.getColumnIndex("SOVIEN_CHIHOM")) == null ? "" : c.getString(c.getColumnIndex("SOVIEN_CHIHOM")));
                    entity.setMASO_TEMKDINH(c.getString(c.getColumnIndex("MASO_TEMKDINH")) == null ? "" : c.getString(c.getColumnIndex("MASO_TEMKDINH")));
                    entity.setNGAY_LAP(c.getString(c.getColumnIndex("NGAY_LAP")) == null ? "" : c.getString(c.getColumnIndex("NGAY_LAP")));
                    entity.setNGUOI_LAP(c.getString(c.getColumnIndex("NGUOI_LAP")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_LAP")));
                    entity.setMA_DVIQLY(c.getString(c.getColumnIndex("MA_DVIQLY")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIQLY")));
                    entity.setCMIS_MA_YCAU_KNAI(c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")));
                    entity.setNGAY_KDINH_FORMAT(c.getString(c.getColumnIndex("NGAY_KDINH_FORMAT")) == null ? "" : c.getString(c.getColumnIndex("NGAY_KDINH_FORMAT")));
                    entity.setTEN_NHAN_VIEN(c.getString(c.getColumnIndex("TEN_NHAN_VIEN")) == null ? "" : c.getString(c.getColumnIndex("TEN_NHAN_VIEN")));
                    entity.setSO_BIEN_BAN(c.getString(c.getColumnIndex("SO_BIEN_BAN")) == null ? "" : c.getString(c.getColumnIndex("SO_BIEN_BAN")));
                    entity.setUSER_NAME(c.getString(c.getColumnIndex("USER_NAME")) == null ? "" : c.getString(c.getColumnIndex("USER_NAME")));
                    entity.setCHI_SO_TREO(c.getString(c.getColumnIndex("CHI_SO_TREO")) == null ? "" : c.getString(c.getColumnIndex("CHI_SO_TREO")));

                    lstKhangHang.add(entity);
                } while (c.moveToNext());
                adapterHoSo = new EsspHoSoAdapter(lstKhangHang);
                rvKH.setAdapter(adapterHoSo);
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi hiển thị dữ liệu:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void setDataDuToan(RecyclerView rvDuToan, int HOSO_ID) {
        lstDuToan = new ArrayList<>();
        Cursor c = connection.getDataDutoanNThuByID(HOSO_ID);
        if (c.moveToFirst()) {
            do {
                EsspEntityDuToanThiCong entity = new EsspEntityDuToanThiCong();
                entity.setDUTOAN_ID(c.getInt(c.getColumnIndex("DUTOAN_ID")));
                entity.setHOSO_ID(c.getInt(c.getColumnIndex("HOSO_ID")));
                entity.setMA_DVIQLY(c.getString(c.getColumnIndex("MA_DVIQLY")));
                entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                entity.setSTT(c.getInt(c.getColumnIndex("STT")));
                entity.setGHI_CHU(c.getString(c.getColumnIndex("GHI_CHU")));
                entity.setTT_TUTUC(c.getInt(c.getColumnIndex("TT_TUTUC")));
                entity.setTT_THUHOI(c.getInt(c.getColumnIndex("TT_THUHOI")));
                entity.setTHUC_TE(c.getFloat(c.getColumnIndex("THUC_TE")));
                entity.setCHENH_LECH(c.getFloat(c.getColumnIndex("CHENH_LECH")));
                entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                entity.setDON_VI_TINH(c.getString(c.getColumnIndex("DON_VI_TINH")));
                entity.setKHTT(c.getString(c.getColumnIndex("KHTT")));
                lstDuToan.add(entity);
            } while (c.moveToNext());
        }
        esspDuToanAdapter = new EsspDuToanAdapter(lstDuToan);
        rvDuToan.setAdapter(esspDuToanAdapter);
    }

    //endregion

    //region Xử lý dialog
    private void popupMenuNghiemThu(final EsspEntityHoSoThiCong entity, final int pos) {
        try {
            final Dialog dialog = new Dialog(EsspNghiemThuFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_nghiem_thu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            final EditText etSoKimNiemChi = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etSoKimNiemChi);
            final EditText etCS1 = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etCS1);
            final EditText etCS2 = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etCS2);
            final EditText etCS3 = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etCS3);
            final EditText etCS4 = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etCS4);
            final EditText etCS5 = (EditText) dialog.findViewById(R.id.dialog_nghiem_thu_etCS5);
            rvDuToan = (RecyclerView) dialog.findViewById(R.id.dialog_nghiem_thu_rvVatTu);
            Button btLuu = (Button) dialog.findViewById(R.id.dialog_nghiem_thu_btLuu);

            layoutManager2 = new LinearLayoutManager(this.getActivity());
            rvDuToan.setHasFixedSize(true);
            rvDuToan.setLayoutManager(layoutManager2);

            String CHI_SO_TREO = entity.getCHI_SO_TREO();
            etSoKimNiemChi.setText(entity.getKIMNIEMCHISO());
            if (entity.getIsNHIEUBIEUGIA() == 1) {
                etCS2.setVisibility(View.VISIBLE);
                etCS3.setVisibility(View.VISIBLE);
                etCS4.setVisibility(View.VISIBLE);
                etCS5.setVisibility(View.VISIBLE);
                etCS1.setHint("BT");
                if (CHI_SO_TREO != null && !CHI_SO_TREO.isEmpty()) {
                    etCS1.setText(CHI_SO_TREO.split(";")[0].split(":")[1]);
                    etCS2.setText(CHI_SO_TREO.split(";")[1].split(":")[1]);
                    etCS3.setText(CHI_SO_TREO.split(";")[2].split(":")[1]);
                    etCS4.setText(CHI_SO_TREO.split(";")[3].split(":")[1]);
                    etCS5.setText(CHI_SO_TREO.split(";")[4].split(":")[1]);
                }
            } else {
                etCS2.setVisibility(View.GONE);
                etCS3.setVisibility(View.GONE);
                etCS4.setVisibility(View.GONE);
                etCS5.setVisibility(View.GONE);
                etCS1.setHint("KT");
                if (CHI_SO_TREO != null && !CHI_SO_TREO.isEmpty()) {
                    etCS1.setText(CHI_SO_TREO.split(":")[1]);
                }
            }

            setDataDuToan(rvDuToan, entity.getHoSoId());

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etCS1.setError(null);
                        etCS2.setError(null);
                        etCS3.setError(null);
                        etCS4.setError(null);
                        etCS5.setError(null);
                        String soKimNiemChi = etSoKimNiemChi.getText().toString();
                        String CS1 = etCS1.getText().toString();
                        String CS2 = etCS2.getText().toString();
                        String CS3 = etCS3.getText().toString();
                        String CS4 = etCS4.getText().toString();
                        String CS5 = etCS5.getText().toString();
                        StringBuilder CHI_SO_TREO = new StringBuilder();
                        if (entity.getIsNHIEUBIEUGIA() == 1) {
                            if (CS1.isEmpty()) {
                                etCS1.setError("Bạn chưa nhập chỉ số");
                                etCS1.requestFocus();
                            } else if (CS2.isEmpty()) {
                                etCS2.setError("Bạn chưa nhập chỉ số");
                                etCS2.requestFocus();
                            } else if (CS3.isEmpty()) {
                                etCS3.setError("Bạn chưa nhập chỉ số");
                                etCS3.requestFocus();
                            } else if (CS4.isEmpty()) {
                                etCS4.setError("Bạn chưa nhập chỉ số");
                                etCS4.requestFocus();
                            } else if (CS5.isEmpty()) {
                                etCS5.setError("Bạn chưa nhập chỉ số");
                                etCS5.requestFocus();
                            } else {
                                CHI_SO_TREO.append("BT:").append(CS1).append(";CD:").append(CS2).append(";TD:").append(CS3)
                                        .append(";SG:").append(CS4).append(";VC:").append(CS5);
                            }
                        } else {
                            if (CS1.isEmpty()) {
                                etCS1.setError("Bạn chưa nhập chỉ số");
                                etCS1.requestFocus();
                            } else {
                                CHI_SO_TREO.append("KT:").append(CS1);
                            }
                        }
                        if (!CHI_SO_TREO.toString().isEmpty()) {
                            if (connection.updateDataNghiemThu(entity.getHoSoId(), soKimNiemChi, CHI_SO_TREO.toString()) != -1) {
                                for (int i = 0; i < esspDuToanAdapter.listData.size(); i++) {
                                    int DUTOAN_ID = esspDuToanAdapter.listData.get(i).getDUTOAN_ID();
                                    float SL_THUCTE = esspDuToanAdapter.listData.get(i).getTHUC_TE();
                                    connection.updateDataDuToanTTe(DUTOAN_ID, String.valueOf(SL_THUCTE));
                                }
                                adapterHoSo.listData.get(pos).setKIMNIEMCHISO(soKimNiemChi);
                                adapterHoSo.listData.get(pos).setCHI_SO_TREO(CHI_SO_TREO.toString());
                                Toast.makeText(EsspNghiemThuFragment.this.getActivity(), "Lưu thành công", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                adapterHoSo.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                                "Lỗi", Color.RED, new StringBuilder("Lỗi lưu thông tin biên bản nghiệm thu:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspNghiemThuFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi cập nhật biên bản nghiệm thu:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion

    //region Các hàm xử lý chung
    private void showKeyboard(View etSLuongTTe) {
        InputMethodManager keyboard = (InputMethodManager)
                EsspNghiemThuFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(etSLuongTTe, 0);
    }
    //endregion

    //region class adapter
    class EsspHoSoAdapter extends RecyclerView.Adapter<EsspHoSoAdapter.RecyclerViewHolder> {

        List<EsspEntityHoSoThiCong> listData = new ArrayList<>();

        public EsspHoSoAdapter(List<EsspEntityHoSoThiCong> listData) {
            this.listData = listData;
        }

        public void updateList(List<EsspEntityHoSoThiCong> data) {
            listData = data;
            notifyDataSetChanged();
        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_hoso, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvSTT.setText(String.valueOf(listData.get(position).getSTT()));
            holder.tvTenKH.setText(listData.get(position).getTEN_KHANG());
            holder.tvDiaChi.setText(listData.get(position).getDIA_CHI());

            if (listData.get(position).getTINH_TRANG() == 2) {
                holder.itemView.setBackgroundResource(R.drawable.bg_item_blue);
            } else {
                if (listData.get(position).getCHI_SO_TREO() == null || listData.get(position).getCHI_SO_TREO().isEmpty()) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_white);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_gray);
                }
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvSTT, tvTenKH, tvDiaChi;
            public CheckBox ckChon;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvSTT = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_stt);
                tvTenKH = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_ten_kh);
                tvDiaChi = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_diachi);
                ckChon = (CheckBox) itemView.findViewById(R.id.essp_row_hoso_ck_chon);
                itemView.setOnClickListener(this);
                ckChon.post(new Runnable() {
                    @Override
                    public void run() {
                        ckChon.setOnCheckedChangeListener(RecyclerViewHolder.this);
                    }
                });
            }

            @Override
            public void onClick(View v) {
                popupMenuNghiemThu(listData.get(getPosition()), getPosition());
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!lstHoSoId.contains(String.valueOf(listData.get(getPosition()).getHoSoId()))) {
                        lstHoSoId.add(String.valueOf(listData.get(getPosition()).getHoSoId()));
                        lstPos.add(String.valueOf(getPosition()));
                    }
                } else {
                    if (lstHoSoId.contains(String.valueOf(listData.get(getPosition()).getHoSoId()))) {
                        lstHoSoId.remove(String.valueOf(listData.get(getPosition()).getHoSoId()));
                        lstPos.remove(String.valueOf(getPosition()));
                    }
                }
            }
        }

    }

    class EsspDuToanAdapter extends RecyclerView.Adapter<EsspDuToanAdapter.RecyclerViewHolder> {

        List<EsspEntityDuToanThiCong> listData = new ArrayList<>();

        public EsspDuToanAdapter(List<EsspEntityDuToanThiCong> listData) {
            this.listData = listData;
        }

        public void updateList(List<EsspEntityDuToanThiCong> data) {
            listData = data;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_dutoan_thicong, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvTenVTu.setText(listData.get(position).getTEN_VTU());
            holder.tvSLuongDToan.setText(String.valueOf(listData.get(position).getSO_LUONG()));
            holder.etSLuongTTe.setText(String.valueOf(listData.get(position).getTHUC_TE()));
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

            public TextView tvTenVTu, tvSLuongDToan;
            public EditText etSLuongTTe;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvTenVTu = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tvTenVTu);
                tvSLuongDToan = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tvSLuongDToan);
                etSLuongTTe = (EditText) itemView.findViewById(R.id.essp_row_dutoan_etSLuongTTe);
                etSLuongTTe.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        listData.get(getPosition()).setTHUC_TE(Float.parseFloat(s.toString()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                etSLuongTTe.requestFocus();
                etSLuongTTe.selectAll();
                showKeyboard(etSLuongTTe);
            }
        }

    }
    //endregion

}
