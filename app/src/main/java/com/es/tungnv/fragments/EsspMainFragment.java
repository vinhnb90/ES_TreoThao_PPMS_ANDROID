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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.PopupMenu.MenuItem;
import com.es.tungnv.PopupMenu.PopupMenu;
import com.es.tungnv.adapters.EsspThietBiAdapter;
import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.entity.EsspEntityHinhAnh;
import com.es.tungnv.entity.EsspEntityHoSo;
import com.es.tungnv.entity.EsspEntityPhuongAn;
import com.es.tungnv.entity.EsspEntityVatTu;
import com.es.tungnv.entity.EsspEntityVatTuDuToan;
import com.es.tungnv.entity.EsspEntityVatTuNHT;
import com.es.tungnv.measuredistance.MtdMainActivity;
import com.es.tungnv.utils.CmdCode;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.views.EsspDrawImageActivity;
import com.es.tungnv.views.EsspSoDoCDActivity;
import com.es.tungnv.views.EsspViewPTNActivity;
import com.es.tungnv.views.R;
import com.es.tungnv.webservice.EsspAsyncCallWSJson;
import com.es.tungnv.webservice.EsspSentBV;
import com.es.tungnv.webservice.EsspSentImage;
import com.es.tungnv.zoomImage.ImageViewTouch;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.es.tungnv.utils.Data.TAG;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspMainFragment extends Fragment implements View.OnClickListener {

    //region Khai báo biến
    private View rootView;

    private EditText etSearch;
    private ImageButton ibClear;
    private RecyclerView rvKH;
    private Button btLayHS, btGuiHS, btXoaHS;
    private static EditText etCongSuat, etSoLuong;
    private static AutoCompleteTextView etLoaiTB, etMucDich, etDienAp;
    private ListView lvGuiHS;

    public static ArrayList<String> lstHoSoId = new ArrayList<>();
    public static ArrayList<String> lstPos = new ArrayList<>();

    private static EsspSqliteConnection connection;
    private EsspAsyncCallWSJson ac;
    private ArrayList<JSONObject> lstHoSo;
    private ProgressDialog progressDialog;
    private List<EsspEntityHoSo> lstKhangHang;
    private EsspHoSoAdapter adapterHoSo;
    private RecyclerView.LayoutManager layoutManager;
    private EsspEntityPhuongAn esspEntityPhuongAn;
    private EsspVatTuDuToanAdapter adapterDuToan;
    private EsspVatTuDuToanMoiAdapter adapterDuToanMoi;
    private EsspVatTuHeThongAdapter adapterVatTu;
    private EsspVatTuHeThongMoiAdapter adapterVatTuMoi;
    private EsspHinhAnhAdapter adapterHinhAnh;

    private ArrayList<String> arr_vatTu = null;
    private ArrayList<String> arr_tram = null;
    private ArrayList<String> arr_thiet_bi = null;
    private ArrayList<String> arr_muc_dich = null;
    private ArrayList<String> arr_dien_ap = null;
    private ArrayList<String> arr_quyen = null;
    private ArrayList<EsspEntityVatTuDuToan> arrVatTu = null;
    private ArrayList<EsspEntityVatTuNHT> arrVatTuNht = null;
    private ArrayList<String> arrMaVatTu = null;
    private ArrayList<Integer> arrIDVT_NHT = null;
    private ArrayList<EsspEntityHinhAnh> arrHinhAnh = null;
    private String TEN_ANH;
    private static int ID_TBI = 0;
    public static int POS_SELECT = 0;
    private static EsspThietBiAdapter adapterThietBi;
    private int countHS = 0, countPA = 0, countDT = 0, countHA = 0, countBV = 0, countTB = 0, countCS = 0;
    private int sumDT = 0, sumHA = 0, sumTB = 0, sumCS = 0;
    private int countTieuChi = 0;

    private boolean checkGetData = false;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private int maxSize = 0;
    private int countFileSent = 0;
    private float snapProgresBar = 1f;

    private static final int CAMERA_REQUEST = 1888;

    private ArrayList<LinkedHashMap<String, String>> arrCongSuat;
    private AdapterCongSuat adapterCongSuat;
    private int POS_CONGSUAT = -1;
    private EditText etKhoangGio, etCongSuatCS;

    private CustomAdapter adapterTBi, adapterMucDich, adapterDienAp;

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
            connection = EsspSqliteConnection.getInstance(EsspMainFragment.this.getActivity());
            ac = EsspAsyncCallWSJson.getInstance();
            lstHoSoId = new ArrayList<>();
            initComponent(rootView);
            LoadListViewData();

            verifyStoragePermissions(this.getActivity());
            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(), "Lỗi", Color.RED,
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
//        else {
//            createPDF();
//        }
    }

    private void initComponent(View rootView) {
        etSearch = (EditText) rootView.findViewById(R.id.essp_fragment_main_et_search);
        ibClear = (ImageButton) rootView.findViewById(R.id.essp_fragment_main_ib_clear);
        rvKH = (RecyclerView) rootView.findViewById(R.id.essp_fragment_main_rv_kh);
        btLayHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_layhs);
        btGuiHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_guihs);
        btXoaHS = (Button) rootView.findViewById(R.id.essp_fragment_main_bt_xoahs);

        lstKhangHang = new ArrayList<>();
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
                            List<EsspEntityHoSo> data = new ArrayList<EsspEntityHoSo>();
                            String query = Common.removeAccent(s.toString().trim().toLowerCase());
                            for (EsspEntityHoSo entity : lstKhangHang) {
                                if (Common.removeAccent(entity.getCMIS_MA_YCAU_KNAI().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getTEN_KHANG().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getDUONG_PHO().toLowerCase()).contains(query)
                                        || Common.removeAccent(entity.getDUONG_PHO_DDO().toLowerCase()).contains(query)) {
                                    data.add(entity);
                                }
                            }
                            adapterHoSo.updateList(data);
                        } catch (Exception ex) {
                            Toast.makeText(EsspMainFragment.this.getActivity(),
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
                if (!Common.isNetworkOnline(EsspMainFragment.this.getActivity())) {
                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                } else {
                    lstHoSo = new ArrayList<>();
                    progressDialog = ProgressDialog.show(EsspMainFragment.this.getActivity(), "Please Wait ...", "Đang lấy dữ liệu ...", true, false);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                lstHoSo = ac.WS_GET_HO_SO_CALL(EsspCommon.getMaDviqly(), EsspCommon.getUSERNAME());
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
                popupMenuGuiHS();
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
                                connection.deletePhanCong(sHOSO_ID);
                                connection.deleteDuToan(sHOSO_ID);
                                connection.deleteBanVe(sHOSO_ID);
                                connection.deletePhuongAn(sHOSO_ID);
                                connection.deleteAnh(sHOSO_ID);
                                connection.deleteThietBi(sHOSO_ID);
                                connection.deleteCongSuat(sHOSO_ID);
                                connection.deleteKQKsat(sHOSO_ID);

                                if (connection.deleteHoSo(sHOSO_ID) != -1) {
                                    adapterHoSo.removeItem(Integer.parseInt(lstPos.get(i)));
                                }

                                String fileNameAnh = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH;
                                File fileAnh = new File(fileNameAnh);
                                String[] sFileAnh = fileAnh.list();
                                for (int j = 0; j < sFileAnh.length; j++) {
                                    if (sFileAnh[j].split("_")[1].equals(sHOSO_ID)) {
                                        File fDeleteAnh = new File(fileNameAnh + sFileAnh[j]);
                                        if (fDeleteAnh.exists()) {
                                            fDeleteAnh.delete();
                                        }
                                    }
                                }

                                String fileNameBV = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH + EsspCommon.getMaDviqly() + "_" + sHOSO_ID;
                                File fileBV = new File(fileNameBV);
                                if (fileBV.exists()) {
                                    fileBV.delete();
                                }
                            }
                            lstHoSoId.clear();
                            lstPos.clear();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                            "Thông báo", Color.RED, new StringBuilder("Lỗi xóa hồ sơ:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
                }
                break;
        }
    }
    //endregion

    //region Xử lý handler
    private Handler handlerGetHoSo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (checkGetData) {
                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(), "Thông báo", Color.WHITE, "Không có hồ sơ mới", Color.WHITE, "OK", Color.WHITE);
            } else {
                try {
                    int count = 0;
                    for (JSONObject map : lstHoSo) {
                        int HOSO_ID = Integer.parseInt(map.getString("HOSO_ID").equals("null") ? "0" : map.getString("HOSO_ID"));
                        String MA_DVIQLY = map.getString("MA_DVIQLY").equals("null") ? "" : map.getString("MA_DVIQLY");
                        String MA_YCAU_KNAI = map.getString("CMIS_MA_YCAU_KNAI").equals("null") ? "" : map.getString("CMIS_MA_YCAU_KNAI");
                        if (!connection.checkMaHS(HOSO_ID)) {
                            connection.updateTinhTrang(HOSO_ID, EsspCommon.TINH_TRANG);
                            connection.updateTinhTrangPHUONGAN(HOSO_ID, 0);
                            connection.updateTinhTrangDT(HOSO_ID, 0);
                            connection.updateTinhTrangHA(HOSO_ID, 0);
                            connection.updateTinhTrangBV(HOSO_ID, 0);
                            connection.updateTinhTrangTB(HOSO_ID, 0);
                            connection.updateTinhTrangCS(HOSO_ID, 0);
                            ac.WS_UPDATE_TINH_TRANG_CALL(HOSO_ID, EsspCommon.TINH_TRANG);
                        } else {
                            String TEN_KHHANG = map.getString("TEN_KHANG").equals("null") ? "" : map.getString("TEN_KHANG");
                            String SO_NHA = map.getString("SO_NHA").equals("null") ? "" : map.getString("SO_NHA");
                            String DUONG_PHO = map.getString("DUONG_PHO").equals("null") ? "" : map.getString("DUONG_PHO");
                            String MA_LOAI_YCAU = map.getString("CMIS_MA_LOAI_YCAU").equals("null") ? "" : map.getString("CMIS_MA_LOAI_YCAU");
                            String TINH_TRANG = "" + EsspCommon.TINH_TRANG;
                            String MA_LOAIHD = map.getString("MA_LOAIHD").equals("null") ? "" : map.getString("MA_LOAIHD");
                            String MASO_THUE = map.getString("MA_SOTHUE").equals("null") ? "" : map.getString("MA_SOTHUE");
                            String LOAI_DDO = map.getString("LOAI_DDO").equals("null") ? "" : map.getString("LOAI_DDO");
                            String TAI_KHOAN = map.getString("TKHOAN_NGANHANG").equals("null") ? "" : map.getString("TKHOAN_NGANHANG");
                            String MA_NHANG = map.getString("MA_NHANG").equals("null") ? "" : map.getString("MA_NHANG");
                            String MA_TRAM = map.getString("MA_TRAM").equals("null") ? "" : map.getString("MA_TRAM");
                            String KIMUA_CSPK = map.getString("KYMUA_CSPK").equals("null") ? "" : map.getString("KYMUA_CSPK");
                            String SO_PHA = map.getString("SO_PHA").equals("null") ? "" : map.getString("SO_PHA");
                            String SO_CMT = map.getString("SO_CMT").equals("null") ? "" : map.getString("SO_CMT");
                            String NGAY_CAP = map.getString("NGAY_CAP").equals("null") ? "" : map.getString("NGAY_CAP");
                            String DTHOAI_DD = map.getString("DTHOAI_DD").equals("null") ? "" : map.getString("DTHOAI_DD");
                            String DTHOAI_CD = map.getString("DTHOAI_CD").equals("null") ? "" : map.getString("DTHOAI_CD");
                            String FAX = map.getString("FAX").equals("null") ? "" : map.getString("FAX");
                            String EMAIL = map.getString("EMAIL").equals("null") ? "" : map.getString("EMAIL");
                            String WEBSITE = map.getString("WEBSITE").equals("null") ? "" : map.getString("WEBSITE");
                            String NGAY_TAO = map.getString("NGAY_TAO").equals("null") ? "" : map.getString("NGAY_TAO");
                            String NGUOI_SUA = map.getString("NGUOI_SUA").equals("null") ? "" : map.getString("NGUOI_SUA");
                            String NGAY_SUA = map.getString("NGAY_SUA").equals("null") ? "" : map.getString("NGAY_SUA");
                            String SO_NHA_DDO = map.getString("SO_NHA_DDO").equals("null") ? "" : map.getString("SO_NHA_DDO");
                            String DUONG_PHO_DDO = map.getString("DUONG_PHO_DDO").equals("null") ? "" : map.getString("DUONG_PHO_DDO");
                            String NGAY_HENKHAOSAT = map.getString("NGAY_HENKHAOSAT").equals("null") ? "" : map.getString("NGAY_HENKHAOSAT");
                            String NOI_CAP = map.getString("NOI_CAP").equals("null") ? "" : map.getString("NOI_CAP");
                            String NGUON_TIEPNHAN = map.getString("NGUON_TIEPNHAN").equals("null") ? "" : map.getString("NGUON_TIEPNHAN");
                            String CHUC_VU = map.getString("CHUC_VU").equals("null") ? "" : map.getString("CHUC_VU");
                            String NGUOI_DAI_DIEN = map.getString("NGUOI_DAI_DIEN").equals("null") ? "" : map.getString("NGUOI_DAI_DIEN");
                            String Gender_NGUOI_DAI_DIEN = map.getString("Gender_NGUOI_DAI_DIEN").equals("null") ? "" : map.getString("Gender_NGUOI_DAI_DIEN");
                            String TEN_NGUOIYCAU = map.getString("TEN_NGUOIYCAU").equals("null") ? "" : map.getString("TEN_NGUOIYCAU");
                            String GenderId = map.getString("GenderId").equals("null") ? "" : map.getString("GenderId");
                            String MA_TRACUU = map.getString("MA_TRACUU").equals("null") ? "" : map.getString("MA_TRACUU");
                            String NGUOI_TIEPNHAN = map.getString("NGUOI_TIEPNHAN").equals("null") ? "" : map.getString("NGUOI_TIEPNHAN");
                            String NGAY_TIEPNHAN = map.getString("NGAY_TIEPNHAN").equals("null") ? "" : map.getString("NGAY_TIEPNHAN");
                            String NGUOI_PHANCONG = map.getString("NGUOI_PHANCONG").equals("null") ? "" : map.getString("NGUOI_PHANCONG");
                            int HINHTHUC_LAPDAT = map.getString("HINHTHUC_LAPDAT").equals("null") ? 0 : map.getInt("HINHTHUC_LAPDAT");
                            int LOAI_CAP = map.getString("LOAI_CAP").equals("null") ? 0 : map.getInt("LOAI_CAP");
                            boolean isTachVatTu = map.getString("isTachVatTu").equals("null") ? true : map.getBoolean("isTachVatTu");
//                            boolean isKhaiGia = false;
                            boolean isKhaiGia = map.getString("isKhaiGia").equals("null") ? true : map.getBoolean("isKhaiGia");
                            String MA_QUYEN = map.getString("MA_QUYEN").equals("null") ? "" : map.getString("MA_QUYEN");
                            String HS_DONG_THOI = "";//map.getString("HS_DONG_THOI").equals("null") ? "" : map.getString("HS_DONG_THOI");
                            String MA_DVIDCHINHC2 = "";
                            String MA_DVIDCHINHC3 = "";
                            try {
                                MA_DVIDCHINHC2 = map.getString("MA_DVIDCHINHC2").equals("null") ? "" : map.getString("MA_DVIDCHINHC2");
                                MA_DVIDCHINHC3 = map.getString("MA_DVIDCHINHC3").equals("null") ? "" : map.getString("MA_DVIDCHINHC3");
                            } catch (Exception ex) {
                                MA_DVIDCHINHC2 = "";
                                MA_DVIDCHINHC3 = "";
                            }
                            int KQ_KHSAT_ID = map.getString("KQ_KHSAT_ID").equals("null") ? 0 : map.getInt("KQ_KHSAT_ID");
                            if (connection.insertDataKS(HOSO_ID, MA_DVIQLY, TEN_KHHANG,
                                    SO_NHA, DUONG_PHO, SO_CMT,
                                    NGAY_CAP, NOI_CAP, DTHOAI_DD,
                                    DTHOAI_CD, FAX, EMAIL,
                                    WEBSITE, SO_NHA_DDO, DUONG_PHO_DDO,
                                    TAI_KHOAN, MA_NHANG, MASO_THUE,
                                    CHUC_VU, NGUOI_DAI_DIEN, Gender_NGUOI_DAI_DIEN, TEN_NGUOIYCAU, GenderId,
                                    MA_TRACUU, MA_YCAU_KNAI, MA_LOAI_YCAU, TINH_TRANG,
                                    MA_LOAIHD, NGUON_TIEPNHAN, NGUOI_TIEPNHAN, NGAY_TIEPNHAN,
                                    NGUOI_SUA, NGAY_SUA, NGAY_TAO, NGAY_HENKHAOSAT, NGUOI_PHANCONG,
                                    LOAI_DDO, MA_TRAM, KIMUA_CSPK, SO_PHA, MA_DVIDCHINHC2, MA_DVIDCHINHC3,
                                    EsspCommon.getUSERNAME(), MA_QUYEN, HS_DONG_THOI, isTachVatTu,
                                    HINHTHUC_LAPDAT, isKhaiGia, LOAI_CAP) != -1) {
                                connection.insertDataKQ_KSAT(HOSO_ID, "", KQ_KHSAT_ID);
                                connection.insertDataPC(HOSO_ID, EsspCommon.getUSERNAME());
                                ac.WS_UPDATE_TINH_TRANG_CALL(HOSO_ID, EsspCommon.TINH_TRANG);
                                count++;
                            }
                        }
                    }
                    LoadListViewData();
                    if (lstHoSo.size() > 0)
                        Toast.makeText(EsspMainFragment.this.getActivity(),
                                new StringBuilder("Lấy thành công ").append(count).append("/")
                                        .append(lstHoSo.size()).append(" hồ sơ").toString(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
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

    private Handler handlerSentPhuongAn = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private Handler handlerSentDuToan = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private Handler handlerSentHinhAnh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private Handler handlerSentBanVe = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private Handler handlerSentThietBi = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private Handler handlerSentCongSuat = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            thongBaoGuiHS();
        }
    };

    private void thongBaoGuiHS() {
        if (countTieuChi == lvGuiHS.getCheckedItemCount()) {
            SparseBooleanArray checked = lvGuiHS.getCheckedItemPositions();
            StringBuilder message = new StringBuilder();
            message.append("Bạn đã gửi thành công:\n")
                    .append(checked.get(0) ? countHS == -1 ? "Hồ sơ đã gửi không thể gửi lại\n" : new StringBuilder(String.valueOf(countHS)).append("/").append(lstHoSoId.size()).append(" hồ sơ\n").toString() : "")
                    .append(checked.get(1) ? countPA == -1 ? "Phương án đã gửi không thể gửi lại\n" : new StringBuilder(String.valueOf(countPA)).append("/").append(String.valueOf(lstHoSoId.size())).append(" phương án\n").toString() : "")
                    .append(checked.get(2) ? countDT == -1 ? "Dự toán đã gửi không thể gửi lại\n" : new StringBuilder(String.valueOf(countDT)).append("/").append(String.valueOf(sumDT)).append(" dự toán\n").toString() : "")
                    .append(checked.get(3) ? countHA == -1 ? "Không có hình ảnh\n" : new StringBuilder(String.valueOf(countHA)).append("/").append(String.valueOf(sumHA)).append(" hình ảnh\n").toString() : "")
                    .append(checked.get(4) ? countBV == -1 ? "Không có bản vẽ\n" : new StringBuilder(String.valueOf(countBV)).append("/").append(String.valueOf(lstHoSoId.size())).append(" bản vẽ\n").toString() : "")
                    .append(checked.get(5) ? countTB == -1 ? "Thiết bị đã gửi không thể gửi lại\n" : new StringBuilder(String.valueOf(countTB)).append("/").append(String.valueOf(sumTB)).append(" thiết bị\n").toString() : "")
                    .append(checked.get(6) ? countCS == -1 ? "Công suất đã gửi không thể gửi lại\n" : new StringBuilder(String.valueOf(countCS)).append("/").append(String.valueOf(sumCS)).append(" công suất\n").toString() : "");
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Thông báo", Color.WHITE, message.toString(), Color.WHITE, "OK", Color.WHITE);
            countHS = 0;
            countPA = 0;
            countDT = 0;
            countHA = 0;
            countBV = 0;
            countTB = 0;
            countCS = 0;
            sumDT = 0;
            sumHA = 0;
            sumTB = 0;
            sumCS = 0;
            progressDialog.dismiss();
        }
    }
    //endregion

    //region Khởi tạo dữ liệu
    private void LoadListViewData() {
        try {
            lstKhangHang.clear();
            lstHoSo = new ArrayList<>();
            Cursor c = connection.getDataHoSoByUser(EsspCommon.getUSERNAME());
            if (c.moveToFirst()) {
                int stt = 0;
                do {
                    stt++;
                    EsspEntityHoSo entity = new EsspEntityHoSo();
                    try {
                        entity.setHOSO_ID(Integer.parseInt(c.getString(c.getColumnIndex("HOSO_ID"))));
                    } catch (Exception ex) {
                    }
                    entity.setSTT("" + stt);
                    entity.setMA_DVIQLY(c.getString(c.getColumnIndex("MA_DVIQLY")) == null ? "" : c.getString(c.getColumnIndex("MA_DVIQLY")));
                    entity.setTEN_KHANG(c.getString(c.getColumnIndex("TEN_KHANG")) == null ? "" : c.getString(c.getColumnIndex("TEN_KHANG")));
                    entity.setSO_NHA(c.getString(c.getColumnIndex("SO_NHA")) == null ? "" : c.getString(c.getColumnIndex("SO_NHA")));
                    entity.setDUONG_PHO(c.getString(c.getColumnIndex("DUONG_PHO")) == null ? "" : c.getString(c.getColumnIndex("DUONG_PHO")));
                    entity.setSO_CMT(c.getString(c.getColumnIndex("SO_CMT")) == null ? "" : c.getString(c.getColumnIndex("SO_CMT")));
                    entity.setNGAY_CAP(c.getString(c.getColumnIndex("NGAY_CAP")) == null ? "" : c.getString(c.getColumnIndex("NGAY_CAP")));
                    entity.setNOI_CAP(c.getString(c.getColumnIndex("NOI_CAP")) == null ? "" : c.getString(c.getColumnIndex("NOI_CAP")));
                    entity.setDTHOAI_DD(c.getString(c.getColumnIndex("DTHOAI_DD")) == null ? "" : c.getString(c.getColumnIndex("DTHOAI_DD")));
                    entity.setDTHOAI_CD(c.getString(c.getColumnIndex("DTHOAI_CD")) == null ? "" : c.getString(c.getColumnIndex("DTHOAI_CD")));
                    entity.setFAX(c.getString(c.getColumnIndex("FAX")) == null ? "" : c.getString(c.getColumnIndex("FAX")));
                    entity.setEMAIL(c.getString(c.getColumnIndex("EMAIL")) == null ? "" : c.getString(c.getColumnIndex("EMAIL")));
                    entity.setWEBSITE(c.getString(c.getColumnIndex("WEBSITE")) == null ? "" : c.getString(c.getColumnIndex("WEBSITE")));
                    entity.setSO_NHA_DDO(c.getString(c.getColumnIndex("SO_NHA_DDO")) == null ? "" : c.getString(c.getColumnIndex("SO_NHA_DDO")));
                    entity.setDUONG_PHO_DDO(c.getString(c.getColumnIndex("DUONG_PHO_DDO")) == null ? "" : c.getString(c.getColumnIndex("DUONG_PHO_DDO")));
                    entity.setTKHOAN_NGANHANG(c.getString(c.getColumnIndex("TKHOAN_NGANHANG")) == null ? "" : c.getString(c.getColumnIndex("TKHOAN_NGANHANG")));
                    entity.setMA_NGANHANG(c.getString(c.getColumnIndex("MA_NHANG")) == null ? "" : c.getString(c.getColumnIndex("MA_NHANG")));
                    entity.setMA_SOTHUE(c.getString(c.getColumnIndex("MA_SOTHUE")) == null ? "" : c.getString(c.getColumnIndex("MA_SOTHUE")));
                    entity.setCHUC_VU(c.getString(c.getColumnIndex("CHUC_VU")) == null ? "" : c.getString(c.getColumnIndex("CHUC_VU")));
                    entity.setGender_NGUOI_DAI_DIEN(c.getString(c.getColumnIndex("Gender_NGUOI_DAI_DIEN")) == null ? 0 : c.getInt(c.getColumnIndex("Gender_NGUOI_DAI_DIEN")));
                    entity.setNGUOI_DAI_DIEN(c.getString(c.getColumnIndex("NGUOI_DAI_DIEN")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_DAI_DIEN")));
                    entity.setTEN_NGUOIYCAU(c.getString(c.getColumnIndex("TEN_NGUOIYCAU")) == null ? "" : c.getString(c.getColumnIndex("TEN_NGUOIYCAU")));
                    entity.setGenderId(c.getString(c.getColumnIndex("GenderId")) == null ? 0 : c.getInt(c.getColumnIndex("GenderId")));
                    entity.setMA_TRACUU(c.getString(c.getColumnIndex("MA_TRACUU")) == null ? "" : c.getString(c.getColumnIndex("MA_TRACUU")));
                    entity.setCMIS_MA_YCAU_KNAI(c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_YCAU_KNAI")));
                    entity.setCMIS_MA_LOAI_YCAU(c.getString(c.getColumnIndex("CMIS_MA_LOAI_YCAU")) == null ? "" : c.getString(c.getColumnIndex("CMIS_MA_LOAI_YCAU")));
                    entity.setTINH_TRANG(c.getString(c.getColumnIndex("TINH_TRANG")) == null ? 4 : c.getInt(c.getColumnIndex("TINH_TRANG")));
                    entity.setMA_LOAIHD(c.getString(c.getColumnIndex("MA_LOAIHD")) == null ? 1 : c.getInt(c.getColumnIndex("MA_LOAIHD")));
                    entity.setNGUON_TIEPNHAN(c.getString(c.getColumnIndex("NGUON_TIEPNHAN")) == null ? 0 : c.getInt(c.getColumnIndex("NGUON_TIEPNHAN")));
                    entity.setNGUOI_TIEPNHAN(c.getString(c.getColumnIndex("NGUOI_TIEPNHAN")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_TIEPNHAN")));
                    entity.setNGAY_TIEPNHAN(c.getString(c.getColumnIndex("NGAY_TIEPNHAN")) == null ? "" : c.getString(c.getColumnIndex("NGAY_TIEPNHAN")));
                    entity.setNGUOI_SUA(c.getString(c.getColumnIndex("NGUOI_SUA")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_SUA")));
                    entity.setNGAY_SUA(c.getString(c.getColumnIndex("NGAY_SUA")) == null ? "" : c.getString(c.getColumnIndex("NGAY_SUA")));
                    entity.setNGAY_TAO(c.getString(c.getColumnIndex("NGAY_TAO")) == null ? "" : c.getString(c.getColumnIndex("NGAY_TAO")));
                    entity.setNGAY_HENKHAOSAT(c.getString(c.getColumnIndex("NGAY_HENKHAOSAT")) == null ? "" : c.getString(c.getColumnIndex("NGAY_HENKHAOSAT")));
                    entity.setNGUOI_PHANCONG(c.getString(c.getColumnIndex("NGUOI_PHANCONG")) == null ? "" : c.getString(c.getColumnIndex("NGUOI_PHANCONG")));
                    entity.setLOAI_DDO(c.getString(c.getColumnIndex("LOAI_DDO")) == null ? 1 : c.getInt(c.getColumnIndex("LOAI_DDO")));
                    entity.setMA_TRAM(c.getString(c.getColumnIndex("MA_TRAM")) == null ? "" : c.getString(c.getColumnIndex("MA_TRAM")));
                    entity.setKIMUA_CSPK(c.getString(c.getColumnIndex("KIMUA_CSPK")) == null ? 0 : c.getInt(c.getColumnIndex("KIMUA_CSPK")));
                    entity.setSO_PHA(c.getString(c.getColumnIndex("SO_PHA")) == null ? 1 : c.getInt(c.getColumnIndex("SO_PHA")));
                    entity.setUSERNAME(c.getString(c.getColumnIndex("USERNAME")) == null ? "" : c.getString(c.getColumnIndex("USERNAME")));
                    entity.setTachVatTu(c.getString(c.getColumnIndex("isTachVatTu")) == null ? true : c.getInt(c.getColumnIndex("isTachVatTu")) == 1 ? true : false);
                    entity.setLOAI_CAP(c.getInt(c.getColumnIndex("LOAI_CAP")));

                    lstKhangHang.add(entity);
                } while (c.moveToNext());
                adapterHoSo = new EsspHoSoAdapter(lstKhangHang);
                rvKH.setAdapter(adapterHoSo);
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi hiển thị dữ liệu:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion

    //region Gửi hồ sơ
    private void popupMenuGuiHS() {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_guihs);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            countTieuChi = 0;
            final CheckBox cbChonTat = (CheckBox) dialog.findViewById(R.id.essp_dialog_guihs_cb_chontat);
            lvGuiHS = (ListView) dialog.findViewById(R.id.essp_dialog_guihs_lv_tieuchi);
            Button btGui = (Button) dialog.findViewById(R.id.essp_dialog_guihs_bt_gui);

            String[] tieuchi = new String[]{
                    "Gửi hồ sơ khảo sát",
                    "Gửi phương án cấp điện",
                    "Gửi dự trù vật tư",
                    "Gửi hình ảnh hiện trường",
                    "Gửi bản vẽ kỹ thuật",
                    "Gửi bảng kê thiết bị",
                    "Gửi biểu đồ phụ tải"
            };

            ArrayAdapter<String> adapterTieuChi = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(),
                    R.layout.essp_row_guihs, tieuchi);
            lvGuiHS.setAdapter(adapterTieuChi);

            cbChonTat.setChecked(true);
            int itemCount = lvGuiHS.getCount();
            for (int i = 0; i < itemCount; i++) {
                lvGuiHS.setItemChecked(i, cbChonTat.isChecked());
            }

            btGui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Common.isNetworkOnline(EsspMainFragment.this.getActivity())) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(), "Thông báo", Color.WHITE, "Bạn chưa có kết nối mạng", Color.WHITE, "OK", Color.WHITE);
                    } else {
                        countFileSent = 0;
                        SparseBooleanArray checked = lvGuiHS.getCheckedItemPositions();
                        if (lvGuiHS.getCheckedItemCount() > 0) {
//                            progressDialog = ProgressDialog.show(EsspMainFragment.this.getActivity(), "Please Wait ...", "Đang gửi dữ liệu ...", true, false);
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Đang gửi hồ sơ ...");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setProgress(0);
                            progressDialog.setMax(100);
                            progressDialog.show();

                            progressBarStatus = 0;
                            maxSize = countItemSend(lstHoSoId, lvGuiHS);
                            snapProgresBar = (float) maxSize / 100f;

                            if (checked.get(0)) {// Gửi hồ sơ được check
                                Thread threadHS = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            Thread.sleep(50);
                                            JSONArray jsonArr = new JSONArray();
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
                                                EsspEntityHoSo entityHS = connection.getDataHoSoById(Integer.parseInt(lstHoSoId.get(i)), EsspCommon.getUSERNAME());
                                                int TINH_TRANG = entityHS.getTINH_TRANG();
                                                if (TINH_TRANG == EsspCommon.TINH_TRANG) {// && !entityHS.getMA_TRAM().isEmpty()
                                                    int HOSO_ID = entityHS.getHOSO_ID();
                                                    String MA_DVIQLY = entityHS.getMA_DVIQLY();
                                                    String MA_TRACUU = entityHS.getMA_TRACUU();
                                                    String CMIS_MA_YCAU_KNAI = entityHS.getCMIS_MA_YCAU_KNAI();
                                                    String MKHAU_TRACUU = "";
                                                    String CMIS_MA_LOAI_YCAU = entityHS.getCMIS_MA_LOAI_YCAU();
                                                    int MA_LOAIHD = entityHS.getMA_LOAIHD();
                                                    int NGUON_TIEPNHAN = entityHS.getNGUON_TIEPNHAN();
                                                    String NGUOI_TIEPNHAN = entityHS.getNGUOI_TIEPNHAN();
                                                    String NGAY_TIEPNHAN = entityHS.getNGAY_TIEPNHAN();
                                                    String NGUOI_SUA = entityHS.getNGUOI_SUA();
                                                    String NGAY_SUA = entityHS.getNGAY_SUA();
                                                    String NGAY_TAO = entityHS.getNGAY_TAO();
                                                    String NGAY_HENKHAOSAT = entityHS.getNGAY_HENKHAOSAT();
                                                    String GIO_BDAU_TCONG = "";
                                                    String GIO_KTHUC_TCONG = "";
                                                    String SODO_CAPDIEN = "";
                                                    String NGUOI_PHANCONG = entityHS.getNGUOI_PHANCONG();
                                                    String DONGBO_CMIS = "";
                                                    String GHI_CHU_KHI_THICONG = "";
                                                    int LOAI_DDO = entityHS.getLOAI_DDO();
                                                    String MA_TRAM = entityHS.getMA_TRAM();
                                                    int KYMUA_CSPK = entityHS.getKIMUA_CSPK();
                                                    int SO_PHA = entityHS.getSO_PHA();
                                                    String TEN_KHANG = entityHS.getTEN_KHANG();
                                                    String SO_NHA = entityHS.getSO_NHA();
                                                    String DUONG_PHO = entityHS.getDUONG_PHO();
                                                    String SO_CMT = entityHS.getSO_CMT();
                                                    String NGAY_CAP = entityHS.getNGAY_CAP();
                                                    String NOI_CAP = entityHS.getNOI_CAP();
                                                    String DTHOAI_DD = entityHS.getDTHOAI_DD();
                                                    String DTHOAI_CD = entityHS.getDTHOAI_CD();
                                                    String FAX = entityHS.getFAX();
                                                    String EMAIL = entityHS.getEMAIL();
                                                    String WEBSITE = entityHS.getWEBSITE();
                                                    String SO_NHA_DDO = entityHS.getSO_NHA_DDO();
                                                    String DUONG_PHO_DDO = entityHS.getDUONG_PHO_DDO();
                                                    String TKHOAN_NGANHANG = entityHS.getTKHOAN_NGANHANG();
                                                    String MA_NHANG = entityHS.getMA_NGANHANG();
                                                    String MA_SOTHUE = entityHS.getMA_SOTHUE();
                                                    String CHUC_VU = entityHS.getCHUC_VU();
                                                    String NGUOI_DAI_DIEN = entityHS.getNGUOI_DAI_DIEN();
                                                    String TEN_NGUOIYCAU = entityHS.getTEN_NGUOIYCAU();
                                                    int GenderId = entityHS.getGenderId();
                                                    int Gender_NGUOI_DAI_DIEN = entityHS.getGender_NGUOI_DAI_DIEN();
                                                    String MA_DVIDCHINHC2 = entityHS.getMA_DVIDCHINHC2();
                                                    String MA_DVIDCHINHC3 = entityHS.getMA_DVIDCHINHC3();
                                                    String MA_QUYEN = entityHS.getMA_QUYEN();
                                                    int HINHTHUC_LAPDAT = entityHS.getLOAIHINH_THANHTOAN();
                                                    int LOAI_CAP = entityHS.getLOAI_CAP();
                                                    int NKY_KSAT = entityHS.getNKY_KSAT();
                                                    Cursor c = connection.getKQKS(HOSO_ID);
                                                    int KQ_KHSAT_ID = 0;
                                                    int MA_TNGAI = 0;
                                                    if (c.moveToFirst()) {
                                                        KQ_KHSAT_ID = c.getInt(c.getColumnIndex("KQ_KHSAT_ID"));
                                                        MA_TNGAI = c.getInt(c.getColumnIndex("MA_TNGAI"));
                                                        if (MA_TNGAI == 0)
                                                            TINH_TRANG = 5;
                                                        else
                                                            TINH_TRANG = 4;
                                                    }
                                                    jsonArr.put(DATAtoJSONHoSo(HOSO_ID, MA_DVIQLY, MA_TRACUU, CMIS_MA_YCAU_KNAI, MKHAU_TRACUU,
                                                            CMIS_MA_LOAI_YCAU, TINH_TRANG, MA_LOAIHD, NGUON_TIEPNHAN, NGUOI_TIEPNHAN,
                                                            NGAY_TIEPNHAN, NGUOI_SUA, NGAY_SUA, NGAY_TAO, NGAY_HENKHAOSAT,
                                                            GIO_BDAU_TCONG, GIO_KTHUC_TCONG, SODO_CAPDIEN, NGUOI_PHANCONG,
                                                            DONGBO_CMIS, GHI_CHU_KHI_THICONG, String.valueOf(LOAI_DDO), MA_TRAM, KYMUA_CSPK,
                                                            SO_PHA, TEN_KHANG, NGUOI_DAI_DIEN, Gender_NGUOI_DAI_DIEN, SO_NHA, DUONG_PHO, SO_CMT, NGAY_CAP,
                                                            NOI_CAP, TEN_NGUOIYCAU, DTHOAI_DD, DTHOAI_CD, FAX, EMAIL, WEBSITE,
                                                            SO_NHA_DDO, DUONG_PHO_DDO, TKHOAN_NGANHANG, MA_NHANG,
                                                            MA_SOTHUE, CHUC_VU, GenderId, MA_DVIDCHINHC2, MA_DVIDCHINHC3,
                                                            KQ_KHSAT_ID, String.valueOf(MA_TNGAI), MA_QUYEN, HINHTHUC_LAPDAT, LOAI_CAP, NKY_KSAT));
                                                }
                                            }
                                            ArrayList<JSONObject> result = ac.WS_UPDATE_HO_SO_CALL(jsonArr);
                                            if (result != null && result.size() > 0) {
                                                for (int i = 0; i < result.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countHS++;
                                                        connection.updateTinhTrang(jObject.getInt("ID"), connection.getMaTNGAI(jObject.getInt("ID")) == 0 ? 5 : 4);
                                                    }
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
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
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi thông tin khảo sát thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentHoSo.sendEmptyMessage(0);
                                    }
                                });
                                threadHS.start();
                            }
                            if (checked.get(1)) {// Gửi phương án được check
                                Thread threadPA = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            Thread.sleep(50);
                                            JSONArray jsonArr = new JSONArray();
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
//                                                if(connection.getTinhTrangID(Integer.parseInt(lstHoSoId.get(i))) > 3) {
                                                if (connection.getTinhTrangPAByHoSoID(Integer.parseInt(lstHoSoId.get(i))) == 0) {
                                                    Cursor c = connection.getPhuongAnhByHoSoID(Integer.parseInt(lstHoSoId.get(i)));
                                                    if (c.moveToFirst()) {
                                                        jsonArr.put(DATAtoJSONPhuongAn(c.getInt(c.getColumnIndex("HOSO_ID")), 0,
                                                                0, 0, 0, 0, 0, 0, 0, c.getString(c.getColumnIndex("DODEM_CTO_A")),
                                                                c.getString(c.getColumnIndex("DODEM_CTO_V")), c.getString(c.getColumnIndex("DODEM_CTO_TI")),
                                                                c.getString(c.getColumnIndex("DIEM_DAUDIEN")), "",
                                                                c.getInt(c.getColumnIndex("VTRI_CTO")), c.getString(c.getColumnIndex("COT_SO")),
                                                                c.getString(c.getColumnIndex("VI_TRI_KHAC")), c.getInt(c.getColumnIndex("TTRANG_SDUNG_DIEN")),
                                                                c.getInt(c.getColumnIndex("HTHUC_GCS")), c.getInt(c.getColumnIndex("HTHUC_TTOAN")),
                                                                c.getString(c.getColumnIndex("HTK"))));
                                                    }
                                                }
//                                                }
                                            }
                                            ArrayList<JSONObject> result = ac.WS_UPDATE_PHUONG_AN_CALL(jsonArr);
                                            if (result != null && result.size() > 0) {
                                                for (int i = 0; i < result.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countPA++;
                                                        connection.updateTinhTrangPHUONGAN(jObject.getInt("ID"), 1);
                                                    }
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                    progressBarHandler.post(new Runnable() {
                                                        public void run() {
                                                            progressDialog.setProgress(progressBarStatus);
                                                        }
                                                    });
                                                }
                                            } else {
                                                countPA = -1;
                                            }
                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi phương án thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentPhuongAn.sendEmptyMessage(0);
                                    }
                                });
                                threadPA.start();
                            }
                            if (checked.get(2)) {// Gửi dự toán được check
                                Thread threadDT = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            JSONArray jsonArr = new JSONArray();
                                            JSONArray jsonArr2 = new JSONArray();
                                            Thread.sleep(50);
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
//                                                if(connection.countTinhTrangDTByHoSoID(Integer.parseInt(lstHoSoId.get(i))) > 0) {
                                                Cursor c = connection.getDuToanByIdHS(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c.moveToFirst()) {
                                                    do {
                                                        if (c.getInt(c.getColumnIndex("TINH_TRANG")) == 0) {
//                                                            if(EsspCommon.getMaDviqly().contains("PD")) {
                                                            jsonArr.put(DATAtoJSONDuToan(c.getInt(c.getColumnIndex("ID")),
                                                                    c.getInt(c.getColumnIndex("HOSO_ID")), EsspCommon.getMaDviqly(),
                                                                    c.getString(c.getColumnIndex("MA_VTU")), c.getFloat(c.getColumnIndex("SO_LUONG")),
                                                                    c.getInt(c.getColumnIndex("SO_HUU")), c.getInt(c.getColumnIndex("STT")),
                                                                    "", c.getInt(c.getColumnIndex("TT_TU_TUC")),
                                                                    c.getInt(c.getColumnIndex("TT_THU_HOI")),
                                                                    c.getFloat(c.getColumnIndex("HSDC_K1NC")),
                                                                    c.getFloat(c.getColumnIndex("HSDC_K2NC")),
                                                                    c.getFloat(c.getColumnIndex("HSDC_MTC"))));
//                                                            } else {
//                                                                jsonArr.put(DATAtoJSONDuToan(c.getInt(c.getColumnIndex("ID")),
//                                                                        c.getInt(c.getColumnIndex("HOSO_ID")), EsspCommon.getMaDviqly(),
//                                                                        c.getString(c.getColumnIndex("MA_VTU")), c.getFloat(c.getColumnIndex("SO_LUONG")),
//                                                                        c.getInt(c.getColumnIndex("SO_HUU")), c.getInt(c.getColumnIndex("STT")),
//                                                                        "", c.getInt(c.getColumnIndex("TT_TU_TUC")),
//                                                                        c.getInt(c.getColumnIndex("TT_THU_HOI"))));
//                                                            }
                                                        }
                                                    } while (c.moveToNext());
                                                }
//                                                }
//                                                if(connection.countTinhTrangDTNHTByHoSoID(Integer.parseInt(lstHoSoId.get(i))) > 0) {
                                                Cursor c2 = connection.getDuToanNHTByIdHS(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c2.moveToFirst()) {
                                                    do {
                                                        if (c2.getInt(c2.getColumnIndex("TINH_TRANG")) == 0) {
//                                                            if(EsspCommon.getMaDviqly().contains("PD")) {
                                                            jsonArr2.put(DATAtoJSONDuToanNHT(c2.getInt(c2.getColumnIndex("VTKHTT_ID")),
                                                                    c2.getInt(c2.getColumnIndex("HOSO_ID")), c2.getString(c2.getColumnIndex("TENVTU_MTB")),
                                                                    c2.getFloat(c2.getColumnIndex("SO_LUONG")), c2.getString(c2.getColumnIndex("DVI_TINH")),
                                                                    c2.getDouble(c2.getColumnIndex("DON_GIA_KH")), c2.getString(c2.getColumnIndex("GHI_CHU")),
                                                                    c2.getInt(c2.getColumnIndex("CHUNG_LOAI")),
                                                                    //TODO VinhNB sửa. Lý do, sai c2....(c....
//                                                                    c2.getDouble(c.getColumnIndex("DG_NCONG")), c2.getDouble(c.getColumnIndex("DG_VTU")),
//                                                                    c2.getDouble(c.getColumnIndex("DG_MTCONG")), c2.getFloat(c.getColumnIndex("HSDC_K1NC")),
//                                                                    c2.getFloat(c.getColumnIndex("HSDC_K2NC")), c2.getFloat(c.getColumnIndex("HSDC_MTC"))));
                                                                    c2.getDouble(c2.getColumnIndex("DG_NCONG")), c2.getDouble(c2.getColumnIndex("DG_VTU")),
                                                                    c2.getDouble(c2.getColumnIndex("DG_MTCONG")), c2.getFloat(c2.getColumnIndex("HSDC_K1NC")),
                                                                    c2.getFloat(c2.getColumnIndex("HSDC_K2NC")), c2.getFloat(c2.getColumnIndex("HSDC_MTC"))));
//                                                            } else {
//                                                                jsonArr2.put(DATAtoJSONDuToanNHT(c2.getInt(c2.getColumnIndex("VTKHTT_ID")),
//                                                                        c2.getInt(c2.getColumnIndex("HOSO_ID")), c2.getString(c2.getColumnIndex("TENVTU_MTB")),
//                                                                        c2.getFloat(c2.getColumnIndex("SO_LUONG")), c2.getString(c2.getColumnIndex("DVI_TINH")),
//                                                                        c2.getDouble(c2.getColumnIndex("DON_GIA_KH")), c2.getString(c2.getColumnIndex("GHI_CHU")),
//                                                                        c2.getInt(c2.getColumnIndex("CHUNG_LOAI"))));
//                                                            }
                                                        }
                                                    } while (c2.moveToNext());
                                                }
//                                                }
                                            }
                                            ArrayList<JSONObject> result = null;
                                            ArrayList<JSONObject> result2 = null;
//                                            if(EsspCommon.getMaDviqly().contains("PD")) {
//                                                result = ac.WS_UPDATE_DU_TOAN_CALL("Post_mtb_dutoan_moi", jsonArr);
//                                                result2 = ac.WS_UPDATE_VT_NHT_CALL("Post_VTU_KHANG_TTUC_moi", jsonArr2);
//                                            } else {
                                            result = ac.WS_UPDATE_DU_TOAN_CALL("Post_mtb_dutoan", jsonArr);
                                            result2 = ac.WS_UPDATE_VT_NHT_CALL("Post_VTU_KHANG_TTUC", jsonArr2);
//                                            }
                                            if (result != null && result.size() > 0) {
                                                for (int i = 0; i < result.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countDT++;
                                                        connection.updateTinhTrangDT(jObject.getInt("ID"), 1);
                                                    }
                                                    sumDT++;
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                    progressBarHandler.post(new Runnable() {
                                                        public void run() {
                                                            progressDialog.setProgress(progressBarStatus);
                                                        }
                                                    });
                                                }
                                            }
                                            if (result2 != null && result2.size() > 0) {
                                                for (int i = 0; i < result2.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result2.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countDT++;
                                                        connection.updateTinhTrangDTNHT(jObject.getInt("ID"), 1);
                                                    }
                                                    sumDT++;
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                    progressBarHandler.post(new Runnable() {
                                                        public void run() {
                                                            progressDialog.setProgress(progressBarStatus);
                                                        }
                                                    });
                                                }
                                            } else {
                                                if (countDT == 0)
                                                    countDT = -1;
                                            }
                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi dự toán thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentDuToan.sendEmptyMessage(0);
                                    }
                                });
                                threadDT.start();
                            }
                            if (checked.get(3)) {// Gửi hình ảnh được check
                                Thread threadHA = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            String result = "";
                                            Thread.sleep(50);
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
//                                                if(connection.countTinhTrangHAByHoSoID(Integer.parseInt(lstHoSoId.get(i))) > 0) {
                                                Cursor c = connection.getAllDataHinhAnhByMaHS(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c.moveToFirst()) {
                                                    do {
                                                        countFileSent++;
                                                        result = UploadFileHA(c.getInt(c.getColumnIndex("HOSO_ID")),
                                                                new StringBuilder(c.getString(c.getColumnIndex("TEN_ANH"))).append(".jpg").toString(),
                                                                c.getString(c.getColumnIndex("GHI_CHU")));
                                                        if (result == null || result.isEmpty()) {
                                                            countHA++;
                                                            connection.updateTinhTrangHA(c.getInt(c.getColumnIndex("HOSO_ID")), 1);
                                                        }
                                                        sumHA++;
                                                        progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                        progressBarHandler.post(new Runnable() {
                                                            public void run() {
                                                                progressDialog.setProgress(progressBarStatus);
                                                            }
                                                        });
                                                    } while (c.moveToNext());
                                                } else {
                                                    countHA = -1;
                                                }
//                                                }
                                            }

                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi Hình ảnh thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentHinhAnh.sendEmptyMessage(0);
                                    }
                                });
                                threadHA.start();
                            }
                            if (checked.get(4)) {// Gửi bản vẽ được check
                                Thread threadBV = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            String result = "";
                                            Thread.sleep(50);
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
//                                                if(connection.getTinhTrangBVByHoSoID(Integer.parseInt(lstHoSoId.get(i))) == 0) {
                                                Cursor c = connection.getBanVe(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c.moveToFirst()) {
                                                    do {
                                                        countFileSent++;
                                                        result = UploadFileBV(c.getInt(c.getColumnIndex("HOSO_ID")),
                                                                new StringBuilder(c.getString(c.getColumnIndex("TEN_ANH"))).append(".jpg").toString());
                                                        if (result == null || result.isEmpty()) {
                                                            countBV++;
                                                            connection.updateTinhTrangBV(c.getInt(c.getColumnIndex("HOSO_ID")), 1);
                                                        }
                                                        progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                        progressBarHandler.post(new Runnable() {
                                                            public void run() {
                                                                progressDialog.setProgress(progressBarStatus);
                                                            }
                                                        });
                                                    } while (c.moveToNext());
                                                } else {
                                                    countBV = -1;
                                                }
//                                                }
                                            }

                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi bản vẽ thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentBanVe.sendEmptyMessage(0);
                                    }
                                });
                                threadBV.start();
                            }
                            if (checked.get(5)) {// Gửi thiết bị được check
                                Thread threadTB = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            JSONArray jsonArr = new JSONArray();
                                            Thread.sleep(50);
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
//                                                if(connection.countTinhTrangTBByHoSoID(Integer.parseInt(lstHoSoId.get(i))) > 0) {
                                                Cursor c = connection.getAllDataThietBiByMaHS(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c.moveToFirst()) {
                                                    do {
                                                        if (c.getInt(c.getColumnIndex("TINH_TRANG")) == 0) {
                                                            jsonArr.put(DATAtoJSONThietBi(c.getInt(c.getColumnIndex("HOSO_ID")),
                                                                    c.getString(c.getColumnIndex("MUC_DICH")),
                                                                    c.getString(c.getColumnIndex("LOAI_TBI")), c.getString(c.getColumnIndex("CONG_SUAT")),
                                                                    c.getInt(c.getColumnIndex("SO_LUONG")), c.getString(c.getColumnIndex("DIENAP_SUDUNG"))));
                                                        }
                                                    } while (c.moveToNext());
                                                }
//                                                }
                                            }
                                            ArrayList<JSONObject> result = ac.WS_UPDATE_THIET_BI_CALL(jsonArr);
                                            if (result != null && result.size() > 0) {
                                                for (int i = 0; i < result.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countTB++;
                                                        connection.updateTinhTrangTB(jObject.getInt("ID"), 1);
                                                    }
                                                    sumTB++;
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                    progressBarHandler.post(new Runnable() {
                                                        public void run() {
                                                            progressDialog.setProgress(progressBarStatus);
                                                        }
                                                    });
                                                }
                                            } else {
                                                countTB = -1;
                                            }
                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi thiết bị thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentThietBi.sendEmptyMessage(0);
                                    }
                                });
                                threadTB.start();
                            }
                            if (checked.get(6)) {// Gửi công suất được check
                                Thread threadCS = new Thread(new Runnable() {
                                    @Override
                                    public synchronized void run() {
                                        try {
                                            JSONArray jsonArr = new JSONArray();
                                            Thread.sleep(50);
                                            for (int i = 0; i < lstHoSoId.size(); i++) {
                                                Cursor c = connection.getCSUATByID(Integer.parseInt(lstHoSoId.get(i)));
                                                if (c.moveToFirst()) {
                                                    ArrayList<LinkedHashMap<String, String>> arrKhoangGio = sortKhoangGio(c);
                                                    int STT = 0;
                                                    if (arrKhoangGio != null) {
                                                        for (int j = 0; j < arrKhoangGio.size(); j++) {
                                                            STT++;
                                                            if (Integer.parseInt(arrKhoangGio.get(j).get("TINH_TRANG")) == 0) {
                                                                jsonArr.put(DATAtoJSONCongSuat(Integer.parseInt(arrKhoangGio.get(j).get("ID")),
                                                                        Integer.parseInt(arrKhoangGio.get(j).get("HOSO_ID")),
                                                                        String.valueOf(STT),
                                                                        Integer.parseInt(arrKhoangGio.get(j).get("CONG_SUAT")),
                                                                        arrKhoangGio.get(j).get("KHOANG_TGIAN")));
                                                            }
                                                        }
                                                    }
//                                                    do {
//                                                        STT++;
//                                                        if(c.getInt(c.getColumnIndex("TINH_TRANG")) == 0) {
//                                                            jsonArr.put(DATAtoJSONCongSuat(c.getInt(c.getColumnIndex("ID")),
//                                                                    c.getInt(c.getColumnIndex("HOSO_ID")), String.valueOf(STT),
//                                                                    c.getInt(c.getColumnIndex("CONG_SUAT")),
//                                                                    c.getString(c.getColumnIndex("KHOANG_TGIAN"))));
//                                                        }
//                                                    } while(c.moveToNext());
                                                }
                                            }
                                            ArrayList<JSONObject> result = ac.WS_UPDATE_CONG_SUAT_CALL(jsonArr);
                                            if (result != null && result.size() > 0) {
                                                for (int i = 0; i < result.size(); i++) {
                                                    countFileSent++;
                                                    JSONObject jObject = result.get(i);
                                                    if (jObject.getString("RESULT").equals("OK")) {
                                                        countCS++;
                                                        connection.updateTinhTrangCS(jObject.getInt("ID"), 1);
                                                    }
                                                    sumCS++;
                                                    progressBarStatus = (int) (countFileSent / snapProgresBar);
                                                    progressBarHandler.post(new Runnable() {
                                                        public void run() {
                                                            progressDialog.setProgress(progressBarStatus);
                                                        }
                                                    });
                                                }
                                            } else {
                                                countCS = -1;
                                            }
                                        } catch (final Exception e) {
//                                            progressDialog.dismiss();
                                            EsspMainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                            "Thông báo", Color.WHITE, new StringBuilder("Gửi công suất thất bại\n").append(e.toString()).toString(), Color.WHITE, "OK", Color.WHITE);
                                                }
                                            });
                                        }
                                        countTieuChi++;
                                        handlerSentCongSuat.sendEmptyMessage(0);
                                    }
                                });
                                threadCS.start();
                            }
                        }
                    }
                    dialog.dismiss();
                }
            });

            cbChonTat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox ck = (CheckBox) v;
                    int itemCount = lvGuiHS.getCount();
                    for (int i = 0; i < itemCount; i++) {
                        lvGuiHS.setItemChecked(i, ck.isChecked());
                    }
                }
            });

            lvGuiHS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int checkedItemCount = lvGuiHS.getCheckedItemCount();

                    if (lvGuiHS.getCount() == checkedItemCount)
                        cbChonTat.setChecked(true);
                    else
                        cbChonTat.setChecked(false);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi đồng bộ hồ sơ:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private int countItemSend(ArrayList<String> lstHoSoId, ListView lvGuiHS) {
        int maxSize = 0;
        SparseBooleanArray checked = lvGuiHS.getCheckedItemPositions();
        if (lvGuiHS.getCheckedItemCount() > 0) {
            if (checked.get(0)) {// Gửi thông tin khảo sát
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countHSByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
            if (checked.get(1)) {// Gửi phương án
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countPhuongAnByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
            if (checked.get(2)) {// Gửi dự toán
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countDTByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                        maxSize += connection.countDTNHTByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (checked.get(3)) {// Gửi hình ảnh
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countHAByHoSoID(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
            if (checked.get(4)) {// Gửi bản vẽ
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countBanVeByHoSoID(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
            if (checked.get(5)) {// Gửi thiết bị
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countTBByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
            if (checked.get(6)) {// Gửi công suất
                for (int i = 0; i < lstHoSoId.size(); i++) {
                    try {
                        maxSize += connection.countCSByHoSoID2(Integer.parseInt(lstHoSoId.get(i)));
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        }
        return maxSize;
    }

    public String UploadFileHA(int HOSO_ID, String fFileName, String ghichu) {
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH + fFileName);
            // Set your server page url (and the file title/description)
            String URL_SERVICE = new StringBuilder("http://").append(EsspCommon.getIP_SERVER_1()).append(EsspCommon.getServerName()).toString();
            EsspSentImage hfu = new EsspSentImage(new StringBuilder(URL_SERVICE).append("Post_mtb_image_htruong").toString(), ghichu);
            return hfu.Send_Now(fstrm, fFileName, HOSO_ID);
        } catch (FileNotFoundException e) {
            return e.toString();
        }
    }

    public String UploadFileBV(int HOSO_ID, String fFileName) {
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH + fFileName);
            // Set your server page url (and the file title/description)
            String URL_SERVICE = new StringBuilder("http://").append(EsspCommon.getIP_SERVER_1()).append(EsspCommon.getServerName()).toString();
            EsspSentBV hfu = new EsspSentBV(new StringBuilder(URL_SERVICE).append("Post_mtb_image_sodo").toString());
            return hfu.Send_Now(fstrm, fFileName, HOSO_ID);
        } catch (FileNotFoundException e) {
            return e.toString();
        }
    }
    //endregion

    //region Khảo sát thông tin
    public void showPopupMenu(View v, final EsspEntityHoSo entity, final int pos) {
        try {
            PopupMenu menu = new PopupMenu(EsspMainFragment.this.getActivity());
            menu.setHeaderTitle("Khảo sát thiết kế");
            menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case EsspConstantVariables.MENU_CHI_TIET:
                            popupMenuChiTiet(entity);
                            break;

                        case EsspConstantVariables.MENU_KHAO_SAT:
                            popupMenuKhaoSat(entity.getHOSO_ID(), pos);
                            break;

                        case EsspConstantVariables.MENU_TRA_HOSO:
                            AlertDialog.Builder builder = new AlertDialog.Builder(EsspMainFragment.this.getActivity());
                            builder.setMessage("Bạn có chắc chắn muốn trả lại hồ sơ?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        String result = ac.WS_UPDATE_TINH_TRANG_CALL(entity.getHOSO_ID(), 2);
                                        if (Integer.parseInt(result) == 200) {
                                            if (connection.deleteHoSo(String.valueOf(entity.getHOSO_ID())) != -1) {

                                            }
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton("No", null);
                            builder.show();
                            break;

                        case EsspConstantVariables.MENU_PHUONG_AN:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuPhuongAn(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_DU_TOAN:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuDuToan(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_DU_TOAN_MOI:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuDuToanMoi(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_HINH_ANH:
                            if (!entity.getMA_TRAM().isEmpty()) {
//                                if(connection.getTinhTrangHAByHoSoID(entity.getHOSO_ID()) == 0) {
                                popupMenuHinhAnh(entity.getHOSO_ID());
//                                } else {
//                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
//                                            "Thông báo", Color.WHITE, "Hồ sơ đã gửi không thể thu thập hình ảnh", Color.WHITE, "OK", Color.WHITE);
//                                }
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_BAN_VE:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuBanVe(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_KHOANG_CACH:
                            Bundle b = new Bundle();
                            b.putString("MA_YCAU_KNAI", entity.getCMIS_MA_YCAU_KNAI());
                            Intent intent = new Intent(EsspMainFragment.this.getActivity(), MtdMainActivity.class);
                            intent.putExtra("MA", b);
                            startActivity(intent);
                            break;

                        case EsspConstantVariables.MENU_NHAT_KY:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuNhatKy(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_THIET_BI:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuThietBi(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;

                        case EsspConstantVariables.MENU_PHU_TAI:
                            if (!entity.getMA_TRAM().isEmpty()) {
                                popupMenuCongSuat(entity.getHOSO_ID());
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Bạn cần cập nhật thông tin khảo sát trước khi sử dụng tính năng này", Color.WHITE, "OK", Color.WHITE);
                            }
                            break;
                        case EsspConstantVariables.MENU_IN:
                            Intent it = new Intent(EsspMainFragment.this.getActivity(), EsspViewPTNActivity.class);
                            it.putExtra("HOSO_ID", entity.getHOSO_ID());
                            startActivity(it);
                            break;
                    }
                }
            });
            // Add Menu (Android menu like style)
            menu.add(EsspConstantVariables.MENU_CHI_TIET, "Chi tiết hồ sơ").setIcon(null);
            menu.add(EsspConstantVariables.MENU_KHOANG_CACH, "Đo khoảng cách").setIcon(null);
            menu.add(EsspConstantVariables.MENU_TRA_HOSO, "Trả lại hồ sơ").setIcon(null);
            menu.add(EsspConstantVariables.MENU_KHAO_SAT, "Khảo sát thông tin")
                    .setIcon(connection.getTinhTrangID(entity.getHOSO_ID()) == EsspCommon.TINH_TRANG
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            menu.add(EsspConstantVariables.MENU_PHUONG_AN, "Phương án cấp điện")
                    .setIcon(connection.getTinhTrangPAByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            if (!entity.isTachVatTu()) {
                menu.add(EsspConstantVariables.MENU_DU_TOAN, "Dự toán vật tư")
                        .setIcon(connection.countDTByHoSoID(entity.getHOSO_ID()) == 0
                                ? getResources().getDrawable(R.mipmap.essp_uncheck)
                                : connection.countTinhTrangDTByHoSoID(entity.getHOSO_ID()) > 0
                                ? getResources().getDrawable(R.mipmap.essp_uncheck)
                                : getResources().getDrawable(R.mipmap.essp_check));
            } else {
                menu.add(EsspConstantVariables.MENU_DU_TOAN_MOI, "Dự toán vật tư mới")
                        .setIcon(connection.countDTByHoSoID(entity.getHOSO_ID()) == 0
                                ? getResources().getDrawable(R.mipmap.essp_uncheck)
                                : connection.countTinhTrangDTByHoSoID(entity.getHOSO_ID()) > 0
                                ? getResources().getDrawable(R.mipmap.essp_uncheck)
                                : getResources().getDrawable(R.mipmap.essp_check));
            }
            menu.add(EsspConstantVariables.MENU_HINH_ANH, "Hình ảnh hiện trường")
                    .setIcon(connection.countHAByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck) :
                            connection.countTinhTrangHAByHoSoID(entity.getHOSO_ID()) > 0
                                    ? getResources().getDrawable(R.mipmap.essp_uncheck)
                                    : getResources().getDrawable(R.mipmap.essp_check));
            menu.add(EsspConstantVariables.MENU_BAN_VE, "Bản vẽ kỹ thuật")
                    .setIcon(connection.getTinhTrangBVByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            menu.add(EsspConstantVariables.MENU_THIET_BI, "Bảng kê thiết bị")
                    .setIcon(connection.countTBByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : connection.countTinhTrangTBByHoSoID(entity.getHOSO_ID()) > 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            menu.add(EsspConstantVariables.MENU_PHU_TAI, "Biểu đồ phụ tải")
                    .setIcon(connection.getTinhTrangCSByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            menu.add(EsspConstantVariables.MENU_IN, "In phiếu tiếp nhận")
                    .setIcon(connection.getTinhTrangCSByHoSoID(entity.getHOSO_ID()) == 0
                            ? getResources().getDrawable(R.mipmap.essp_uncheck)
                            : getResources().getDrawable(R.mipmap.essp_check));
            menu.show(EsspMainFragment.this.getView());
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi hiển thị menu:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuChiTiet(EsspEntityHoSo entity) {
        try {
            Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_chitiet);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tvMaHS = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_mahs);
            TextView tvTenKH = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_tenkh);
            TextView tvDiaChi = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_diachi);
            TextView tvDiaChiCD = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_diachi_cd);
            TextView tvTinhTrang = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_tinhtrang);
            TextView tvMaLoaiHD = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_ma_loai_hdong);
            TextView tvLoaiDDo = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_loai_diemdo);
            TextView tvMaTram = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_ma_tram);
            TextView tvKyMuaCSPK = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_ky_mua_cspk);
            TextView tvSoPha = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_so_pha);
            TextView tvDThoaiDD = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_dienthoai_dd);
            TextView tvDThoaiCD = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_tv_dienthoai_cd);

            tvMaHS.setText(entity.getCMIS_MA_YCAU_KNAI());
            tvTenKH.setText(entity.getTEN_KHANG());
            tvDiaChi.setText(new StringBuilder(entity.getSO_NHA()).append(" ").append(entity.getDUONG_PHO()).toString());
            tvDiaChiCD.setText(new StringBuilder(entity.getSO_NHA_DDO()).append(" ").append(entity.getDUONG_PHO_DDO()).toString());
            tvTinhTrang.setText(String.valueOf(entity.getTINH_TRANG()).isEmpty() ? "" : entity.getTINH_TRANG() != 3 ? "Đã gửi" : "Chưa gửi");
            tvMaLoaiHD.setText((String.valueOf(entity.getMA_LOAIHD()).isEmpty() || String.valueOf(entity.getMA_LOAIHD()).equals("null")) ? "" : entity.getMA_LOAIHD() == 0 ? "Sinh hoạt" : "Ngoài mục đích sinh hoạt");
            if (!String.valueOf(entity.getLOAI_DDO()).isEmpty() && !String.valueOf(entity.getLOAI_DDO()).equals("null")) {
                if (entity.getLOAI_DDO() == 1) {
                    tvLoaiDDo.setText("Công tơ cơ HC hoặc công tơ điện tử 1 giá(KT)");
                } else if (entity.getLOAI_DDO() == 2) {
                    tvLoaiDDo.setText("Công tơ cơ/điện tử 1 giá HC+VC");
                } else if (entity.getLOAI_DDO() == 3) {
                    tvLoaiDDo.setText("Công tơ điện tử ban 3 giá");
                } else if (entity.getLOAI_DDO() == 4) {
                    tvLoaiDDo.setText("Công tơ điện tử ban 2 giá");
                } else if (entity.getLOAI_DDO() == 5) {
                    tvLoaiDDo.setText("Công tơ điện tử 3 giá ban 1 giá");
                } else if (entity.getLOAI_DDO() == 6) {
                    tvLoaiDDo.setText("Công tơ điện tử 3 giá, có giá KT");
                }
            } else {
                tvLoaiDDo.setText("");
            }
            tvMaTram.setText(entity.getMA_TRAM());
            try {
                tvKyMuaCSPK.setText(entity.getKIMUA_CSPK() == 0 ? "Không" : "Có");
            } catch (Exception ex) {
                tvKyMuaCSPK.setText("");
            }
            tvSoPha.setText(String.valueOf(entity.getSO_PHA()));
            tvDThoaiDD.setText(entity.getDTHOAI_DD());
            tvDThoaiCD.setText(entity.getDTHOAI_CD());

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi hiển thị chi tiết:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuKhaoSat(final int HOSO_ID, final int pos) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_khaosat);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final AutoCompleteTextView etMaTram = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_khaosat_et_ma_tram);
            final AutoCompleteTextView etMaQuyen = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_khaosat_et_ma_quyen);
            final EditText etTaiKhoan = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_taikhoan);
            final AutoCompleteTextView etNganHang = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_khaosat_et_nganhang);
            final EditText etChucVu = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_chucvu);
            final EditText etNguoiDaiDien = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_nguoidaidien);
            final EditText etTenNguoiYcau = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_donvidaidien);
            final EditText etHsDongThoi = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_hsdongthoi);
            final EditText etGhiChu = (EditText) dialog.findViewById(R.id.essp_dialog_khaosat_et_ghichu);

            final TextView tvMaTram = (TextView) dialog.findViewById(R.id.essp_dialog_khaosat_tv_ma_tram);

            final Spinner spMaLoaiYC = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_maloai_yc);
            final Spinner spMaLoaiHD = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_maloai_hd);
            final Spinner spLoaiDDo = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_loai_ddo);
            final Spinner spKyMuaCSPK = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_kymua_cspk);
            final Spinner spSoPha = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_so_pha);
            final Spinner spHinhThuc = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_hinhthuc);
            final Spinner spLoaiCap = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_loaicap);
            final Spinner spNKyKSat = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_nkyksat);
            final Spinner spTroNgai = (Spinner) dialog.findViewById(R.id.essp_dialog_khaosat_sp_tro_ngai);

            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_khaosat_bt_luu);

            etTenNguoiYcau.setVisibility(View.GONE);

            initSpinner(0, EsspConstantVariables.ARR_MA_LOAI_YC, null, spMaLoaiYC);
            initSpinner(0, EsspConstantVariables.ARR_MA_LOAI_HD, null, spMaLoaiHD);
            initSpinner(0, EsspCommon.getMaDviqly().contains("PD") ? EsspConstantVariables.ARR_LOAI_DD : EsspConstantVariables.ARR_LOAI_DD_CPC, null, spLoaiDDo);
            initSpinner(0, EsspConstantVariables.ARR_KY_MUA_CSPK, null, spKyMuaCSPK);
            initSpinner(0, EsspConstantVariables.ARR_SO_PHA, null, spSoPha);
            initSpinner(0, EsspConstantVariables.ARR_HINH_THUC, null, spHinhThuc);
            initSpinner(0, EsspConstantVariables.ARR_LOAI_CAP, null, spLoaiCap);
            initSpinner(0, EsspConstantVariables.ARR_NKY_KSAT, null, spNKyKSat);
            initSpinner(1, null, connection.getDataTNgai(), spTroNgai);

            tvMaTram.append(Html.fromHtml("<font color=\"red\">(*) </font>"));

            setDataSelectTram(etMaTram);
            setDataSelectQuyen(etMaQuyen);
            setDataSelectNganHang(etNganHang);

            spTroNgai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        etGhiChu.setEnabled(false);
                    } else {
                        etGhiChu.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            try {
                EsspEntityHoSo entityHS = connection.getDataHoSoById(HOSO_ID, EsspCommon.getUSERNAME());
                if (entityHS != null) {
                    if (!String.valueOf(entityHS.getLOAIHINH_THANHTOAN()).isEmpty()
                            && !String.valueOf(entityHS.getLOAIHINH_THANHTOAN()).equals("null")) {
                        spHinhThuc.setSelection(entityHS.getLOAIHINH_THANHTOAN());
                    }
                    if (!String.valueOf(entityHS.getLOAI_CAP()).isEmpty()
                            && !String.valueOf(entityHS.getLOAI_CAP()).equals("null")) {
                        spLoaiCap.setSelection(entityHS.getLOAI_CAP());
                    }
                    if (!String.valueOf(entityHS.getNKY_KSAT()).isEmpty()
                            && !String.valueOf(entityHS.getNKY_KSAT()).equals("null")
                            && entityHS.getNKY_KSAT() != -1) {
                        spNKyKSat.setSelection(entityHS.getNKY_KSAT() + 1);
                    }
                    if (!String.valueOf(entityHS.getMA_LOAIHD()).isEmpty() && !String.valueOf(entityHS.getMA_LOAIHD()).equals("null"))
                        spMaLoaiHD.setSelection(entityHS.getMA_LOAIHD());
                    if (!String.valueOf(entityHS.getLOAI_DDO()).isEmpty() && !String.valueOf(entityHS.getLOAI_DDO()).equals("null"))
                        spLoaiDDo.setSelection(entityHS.getLOAI_DDO() - 1);
                    etMaTram.setText(entityHS.getMA_TRAM().isEmpty() ? "" : new StringBuilder(entityHS.getMA_TRAM()).append(" - ").append(connection.getTenTram(entityHS.getMA_TRAM())).toString());
                    etMaQuyen.setText(entityHS.getMA_QUYEN().isEmpty() ? "" : new StringBuilder(entityHS.getMA_QUYEN()).append(" - ").append(connection.getTenQuyen(entityHS.getMA_QUYEN())).toString());
                    if (!String.valueOf(entityHS.getKIMUA_CSPK()).isEmpty() && !String.valueOf(entityHS.getKIMUA_CSPK()).equals("null"))
                        spKyMuaCSPK.setSelection(entityHS.getKIMUA_CSPK() == 1 ? 0 : 1);
                    if (!String.valueOf(entityHS.getSO_PHA()).isEmpty() && !String.valueOf(entityHS.getSO_PHA()).equals("null")) {
                        if (entityHS.getSO_PHA() == 1) {
                            spSoPha.setSelection(0);
                        } else {
                            spSoPha.setSelection(1);
                        }
                    }
                    etTaiKhoan.setText(entityHS.getTKHOAN_NGANHANG());
                    etNganHang.setText(entityHS.getMA_NGANHANG().equals("") ? "" : new StringBuilder(entityHS.getMA_NGANHANG()).append(" - ").append(connection.getTenNHByMaNH(entityHS.getMA_NGANHANG())).toString());
                    etChucVu.setText(entityHS.getCHUC_VU());
                    etNguoiDaiDien.setText(entityHS.getNGUOI_DAI_DIEN());
                    Cursor c = connection.getKQKS(entityHS.getHOSO_ID());
                    if (c.moveToFirst()) {
                        etGhiChu.setText(c.getString(c.getColumnIndex("GHI_CHU")));
                        for (int i = 0; i < spTroNgai.getCount(); i++) {
                            int tn = Integer.parseInt(spTroNgai.getItemAtPosition(i).toString().split("-")[0].toString().trim());
                            if (c.getInt(c.getColumnIndex("MA_TNGAI")) == tn) {
                                spTroNgai.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(EsspMainFragment.this.getActivity(), new StringBuilder("Lỗi khởi tạo dữ liệu khảo sát:\n").append(ex.toString()).toString(), Toast.LENGTH_LONG).show();
            }

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etMaTram.setError(null);
                        if (etMaTram.getText().toString().trim().isEmpty() && spTroNgai.getSelectedItemPosition() == 0) {
                            etMaTram.setError("Bạn chưa nhập mã trạm");
                        } else if (spNKyKSat.getSelectedItemPosition() == 0) {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa chọn nhật ký khảo sát", Toast.LENGTH_LONG).show();
                        } else {
                            String CMIS_MA_LOAI_YCAU = spMaLoaiYC.getSelectedItem().toString();
                            int MA_LOAIHD = spMaLoaiHD.getSelectedItemPosition();
                            int LOAI_DDO = (spLoaiDDo.getSelectedItemPosition() + 1);
                            String MA_TRAM = etMaTram.getText().toString().trim().split("-")[0].toString().trim();
                            String MA_QUYEN = etMaQuyen.getText().toString().trim().split("-")[0].toString().trim();
                            String HS_DONG_THOI = etHsDongThoi.getText().toString().trim().split("-")[0].toString().trim();
                            int KIMUA_CSPK = spKyMuaCSPK.getSelectedItemPosition() == 0 ? 1 : 0;
                            int SO_PHA = Integer.parseInt(spSoPha.getSelectedItem().toString());
                            int HINH_THUC = spHinhThuc.getSelectedItemPosition();
                            int LOAI_CAP = spLoaiCap.getSelectedItemPosition();
                            String TAI_KHOAN = etTaiKhoan.getText().toString().trim();
                            String MA_NGANHANG = etNganHang.getText().toString().trim().split("-")[0].toString().trim();
                            String CHUC_VU = etChucVu.getText().toString().trim();
                            String NGUOI_DAI_DIEN = etNguoiDaiDien.getText().toString().trim();
                            String TRO_NGAI = spTroNgai.getSelectedItem().toString().split("-")[0].toString().trim();
                            String GHI_CHU = spTroNgai.getSelectedItemPosition() == 0 ? "" : etGhiChu.getText().toString();
                            String X = "";
                            String Y = "";
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            String NGAY_SUA = sdf.format(new Date());
                            int NKY_KSAT = spNKyKSat.getSelectedItemPosition() - 1;

                            if (!connection.checkMaTram(MA_TRAM) && spTroNgai.getSelectedItemPosition() == 0) {
                                etMaTram.setError("Mã trạm bạn nhập không có trong hệ thống");
                                etMaTram.setText("");
                                etMaTram.requestFocus();
                            } else if (!etNganHang.getText().toString().isEmpty() && !connection.checkMaNganHang(MA_NGANHANG)) {
                                etNganHang.setError("Ngân hàng bạn nhập không có trong hệ thống");
                                etNganHang.setText("");
                                etNganHang.requestFocus();
                            } else {
                                if (connection.updateKS(HOSO_ID, CMIS_MA_LOAI_YCAU, MA_LOAIHD, LOAI_DDO, MA_TRAM, TAI_KHOAN, MA_NGANHANG,
                                        KIMUA_CSPK, SO_PHA, EsspCommon.getUSERNAME(), NGAY_SUA, CHUC_VU, NGUOI_DAI_DIEN, X, Y, MA_QUYEN,
                                        HS_DONG_THOI, HINH_THUC, LOAI_CAP, NKY_KSAT) != -1) {
//                                    connection.updateKQ_KS(HOSO_ID, TRO_NGAI);
                                    connection.updateKQ_KS(HOSO_ID, TRO_NGAI, GHI_CHU);

                                    Cursor cDT = connection.getDuToanByIdHS(HOSO_ID);
                                    if (cDT.moveToFirst()) {
                                        do {
                                            String MA_VTU = cDT.getString(cDT.getColumnIndex("MA_VTU"));
                                            double DON_GIA = 0d;
                                            int SO_LUONG = cDT.getInt(cDT.getColumnIndex("SO_LUONG"));
                                            switch (HINH_THUC) {
                                                case EsspConstantVariables.DT_TRONGOI:
                                                    if (connection.getChungLoai(MA_VTU) == 1) {
                                                        DON_GIA = connection.getDGTronGoi(MA_VTU);
                                                    } else {
                                                        DON_GIA = 0d;
                                                    }
                                                    break;
                                                case EsspConstantVariables.DT_TUTUC_CAP:
                                                    if (connection.getChungLoai(MA_VTU) == 1) {//dây cáp
                                                        DON_GIA = connection.getDGNCong(MA_VTU);
                                                    } else {
                                                        DON_GIA = connection.getDGVTu(MA_VTU);
                                                    }
                                                    break;
                                                case EsspConstantVariables.DT_TUTUC_VTU:
                                                    DON_GIA = connection.getDGNCong(MA_VTU);
                                                    break;
                                                case EsspConstantVariables.DT_TUTUC_HTOAN:
                                                    DON_GIA = 0d;
                                                    break;
                                            }
                                            double THANH_TIEN = DON_GIA * SO_LUONG;
                                            if (connection.updateDonGiaThanhTien(HOSO_ID, "1", MA_VTU,
                                                    String.valueOf(DON_GIA), String.valueOf(THANH_TIEN)) <= 0) {
                                                Log.i("update", "success");
                                            }
                                        } while (cDT.moveToNext());
                                    }

                                    Toast.makeText(EsspMainFragment.this.getActivity(), "Cập nhật hồ sơ thành công", Toast.LENGTH_LONG).show();
                                    lstKhangHang.get(pos).setCMIS_MA_LOAI_YCAU(CMIS_MA_LOAI_YCAU);
                                    lstKhangHang.get(pos).setMA_LOAIHD(MA_LOAIHD);
                                    lstKhangHang.get(pos).setLOAI_DDO(LOAI_DDO);
                                    lstKhangHang.get(pos).setMA_TRAM(MA_TRAM);
                                    lstKhangHang.get(pos).setKIMUA_CSPK(KIMUA_CSPK);
                                    lstKhangHang.get(pos).setSO_PHA(SO_PHA);
                                    lstKhangHang.get(pos).setTKHOAN_NGANHANG(TAI_KHOAN);
                                    lstKhangHang.get(pos).setMA_NGANHANG(MA_NGANHANG);
                                    lstKhangHang.get(pos).setMA_QUYEN(MA_QUYEN);
                                    lstKhangHang.get(pos).setLOAIHINH_THANHTOAN(HINH_THUC);
                                    lstKhangHang.get(pos).setLOAI_CAP(LOAI_CAP);
                                    lstKhangHang.get(pos).setNKY_KSAT(NKY_KSAT);
                                    adapterHoSo.updateList(lstKhangHang);
                                    dialog.dismiss();
                                } else {
                                    Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                            "Lỗi", Color.RED, "Cập nhật hồ sơ thất bại", Color.WHITE, "OK", Color.RED);
                                    dialog.dismiss();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi lưu dữ liệu", Color.WHITE, "OK", Color.RED);
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, new StringBuilder("Lỗi cập nhật khảo sát:\n").append(ex.toString()).toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuPhuongAn(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_phuongan);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final EditText etDiemDauDien = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_diemdaudien);
            final EditText etA = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_a);
            final EditText etV = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_v);
            final EditText etTi = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_ti);
            final EditText etSoCot = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_socot);
            final EditText etViTriKhac = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_vitrikhac);
            final EditText etHTK = (EditText) dialog.findViewById(R.id.essp_dialog_phuongan_et_htk);

            final RadioButton rbTrongNha = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_trongnha);
            final RadioButton rbTrenTuong = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_trentuong);
            final RadioButton rbViTriKhac = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_vitrikhac);
            final RadioButton rbTaiCot = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_taicot);
            final RadioButton rbTienMat = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_tienmat);
            final RadioButton rbNganHang = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_nganhang);
            final RadioButton rbCongThanhToan = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_congthanhtoan);
            final RadioButton rbHTKhac = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_htkhac);
            final RadioButton rbChuaCo = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_chuaco);
            final RadioButton rbDungChung = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_dungchung);
            final RadioButton rbTrucTiep = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_tructiep);
            final RadioButton rbHHU = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_hhu);
            final RadioButton rbTuXa = (RadioButton) dialog.findViewById(R.id.essp_dialog_phuongan_rb_tuxa);

            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_phuongan_bt_luu);

            if (!connection.checkPAexist(HOSO_ID)) {
                esspEntityPhuongAn = connection.getDataPAByMaHS(HOSO_ID);
                etDiemDauDien.setText(esspEntityPhuongAn.getDIEM_DAUDIEN());
                etA.setText(String.valueOf(esspEntityPhuongAn.getDODEM_CTO_A()));
                etV.setText(String.valueOf(esspEntityPhuongAn.getDODEM_CTO_V()));
                etTi.setText(String.valueOf(esspEntityPhuongAn.getDODEM_CTO_TI()));
                etHTK.setText(esspEntityPhuongAn.getHTK());
                if (esspEntityPhuongAn.getVTRI_CTO() == 0) {
                    rbTrongNha.setChecked(true);
                } else if (esspEntityPhuongAn.getVTRI_CTO() == 1) {
                    rbTrenTuong.setChecked(true);
                } else if (esspEntityPhuongAn.getVTRI_CTO() == 2) {
                    rbTaiCot.setChecked(true);
                    etSoCot.setVisibility(View.VISIBLE);
                    etSoCot.setText(esspEntityPhuongAn.getCOT_SO());
                } else {
                    rbViTriKhac.setChecked(true);
                    etViTriKhac.setText(esspEntityPhuongAn.getVI_TRI_KHAC());
                    etViTriKhac.setVisibility(View.VISIBLE);
                }
                if (esspEntityPhuongAn.getHTHUC_TTOAN() == 1) {
                    rbTienMat.setChecked(true);
                    etHTK.setVisibility(View.GONE);
                } else if (esspEntityPhuongAn.getHTHUC_TTOAN() == 2) {
                    rbNganHang.setChecked(true);
                    etHTK.setVisibility(View.GONE);
                } else if (esspEntityPhuongAn.getHTHUC_TTOAN() == 3) {
                    rbCongThanhToan.setChecked(true);
                    etHTK.setVisibility(View.GONE);
                } else {
                    rbHTKhac.setChecked(true);
                    etHTK.setVisibility(View.VISIBLE);
                }
                if (esspEntityPhuongAn.getTTRANG_SDUNG_DIEN() == 1) {
                    rbChuaCo.setChecked(true);
                } else {
                    rbDungChung.setChecked(true);
                }

                if (esspEntityPhuongAn.getHTHUC_GCS() == 1) {
                    rbTrucTiep.setChecked(true);
                } else if (esspEntityPhuongAn.getHTHUC_GCS() == 2) {
                    rbHHU.setChecked(true);
                } else {
                    rbTuXa.setChecked(true);
                }
            }

            rbHTKhac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        etHTK.setVisibility(View.VISIBLE);
                    } else {
                        etHTK.setText("");
                        etHTK.setVisibility(View.GONE);
                    }
                }
            });

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String DODEM_CTO_A = etA.getText().toString().trim();
                    String DODEM_CTO_V = etV.getText().toString().trim();
                    String DODEM_CTO_TI = etTi.getText().toString().trim();
                    String COT_SO = "";
                    String VI_TRI_KHAC = "";
                    int VTRI_CTO = 0;
                    if (rbTrongNha.isChecked()) {
                        VTRI_CTO = 0;
                    } else if (rbTrenTuong.isChecked()) {
                        VTRI_CTO = 1;
                    } else if (rbTaiCot.isChecked()) {
                        VTRI_CTO = 2;
                        COT_SO = etSoCot.getText().toString().trim();
                    } else {
                        VTRI_CTO = 3;
                        VI_TRI_KHAC = etViTriKhac.getText().toString().trim();
                    }
                    String HTK = "";
                    int HTHUC_TTOAN = 1;
                    if (rbTienMat.isChecked()) {
                        HTHUC_TTOAN = 1;
                    } else if (rbNganHang.isChecked()) {
                        HTHUC_TTOAN = 2;
                    } else if (rbCongThanhToan.isChecked()) {
                        HTHUC_TTOAN = 3;
                    } else {
                        HTHUC_TTOAN = 4;
                        HTK = etHTK.getText().toString().trim();
                    }
                    String DIEM_DAUDIEN = etDiemDauDien.getText().toString().trim();
                    int TTRANG_SDUNG_DIEN = 1;
                    if (rbChuaCo.isChecked()) {
                        TTRANG_SDUNG_DIEN = 1;
                    } else {
                        TTRANG_SDUNG_DIEN = 2;
                    }

                    int HTHUC_GCS = 1;
                    if (rbTrucTiep.isChecked()) {
                        HTHUC_GCS = 1;
                    } else if (rbHHU.isChecked()) {
                        HTHUC_GCS = 2;
                    } else {
                        HTHUC_GCS = 3;
                    }

                    int TINH_TRANG = 0;

                    if (connection.checkPAexist(HOSO_ID)) {
                        if (connection.insertDataPHUONGAN(HOSO_ID, DODEM_CTO_A, DODEM_CTO_V,
                                DODEM_CTO_TI, VTRI_CTO, COT_SO, VI_TRI_KHAC, HTHUC_TTOAN, DIEM_DAUDIEN, TTRANG_SDUNG_DIEN, HTHUC_GCS, HTK, TINH_TRANG) != -1) {
//                            if (connection.updateSoCotKS(MA_YCAU_KNAI, COT_SO) != -1) {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Cập nhật phương án thành công", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(EsspMainFragment.this.getActivity(), "Không cập nhật được số cột", Toast.LENGTH_LONG).show();
//                            }
                        } else {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Không cập nhật được phương án", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (connection.updateDataPHUONGAN(esspEntityPhuongAn.getID(), HOSO_ID, DODEM_CTO_A, DODEM_CTO_V,
                                DODEM_CTO_TI, VTRI_CTO, COT_SO, VI_TRI_KHAC, HTHUC_TTOAN, DIEM_DAUDIEN, TTRANG_SDUNG_DIEN, HTHUC_GCS, HTK) != -1) {
//                            if (connection.updateSoCotKS(MA_YCAU_KNAI, COT_SO) != -1) {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Cập nhật phương án thành công", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(EsspMainFragment.this.getActivity(), "Không cập nhật được số cột", Toast.LENGTH_LONG).show();
//                            }
                        } else {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Không cập nhật được phương án", Toast.LENGTH_LONG).show();
                        }
                    }
                    dialog.dismiss();
                }
            });

            rbTaiCot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            etSoCot.setVisibility(View.VISIBLE);
                            etSoCot.setText(esspEntityPhuongAn == null ? "" : esspEntityPhuongAn.getCOT_SO());
                        } else {
                            etSoCot.setText("");
                            etSoCot.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi chọn cột", Toast.LENGTH_LONG).show();
                    }
                }
            });

            rbViTriKhac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            etViTriKhac.setVisibility(View.VISIBLE);
                            etViTriKhac.setText(esspEntityPhuongAn == null ? "" : esspEntityPhuongAn.getVI_TRI_KHAC());
                        } else {
                            etViTriKhac.setText("");
                            etViTriKhac.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi chọn vị trí khác", Toast.LENGTH_LONG).show();
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi cập nhật khảo sát:", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuDuToan(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_dutoan);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final TextView tvThanhTien = (TextView) dialog.findViewById(R.id.essp_dialog_dutoan_tv_thanhtien);
            final Spinner spLoaiCP = (Spinner) dialog.findViewById(R.id.essp_dialog_dutoan_sp_loai_cp);
            final Spinner spKhaiGia = (Spinner) dialog.findViewById(R.id.essp_dialog_dutoan_sp_khaigia);
            final RecyclerView rvDuToan = (RecyclerView) dialog.findViewById(R.id.essp_dialog_dutoan_rv_dutoan);
            Button ibAdd = (Button) dialog.findViewById(R.id.essp_dialog_dutoan_ib_add);
            final Button ibAddNht = (Button) dialog.findViewById(R.id.essp_dialog_dutoan_ib_add_nht);

//            ibAddNht.setVisibility(View.GONE);
            spKhaiGia.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            rvDuToan.setHasFixedSize(true);
            rvDuToan.setLayoutManager(layoutManager);

            String[] strCPs = {"Khách hàng", "Công ty"};
            ArrayAdapter<String> adapterCP = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, strCPs);
            spLoaiCP.setAdapter(adapterCP);

            arrMaVatTu = new ArrayList<>();
            arrVatTu = new ArrayList<>();
            arrVatTuNht = new ArrayList<>();
            arrIDVT_NHT = new ArrayList<>();
            Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
            if (c.moveToFirst()) {
                do {
                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                    entity.setID(c.getInt(c.getColumnIndex("ID")));
                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                    entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                    entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                    entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                    entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                    entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                    entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                    arrVatTu.add(entity);
                    arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                } while (c.moveToNext());
            }

            if (spLoaiCP.getSelectedItemPosition() == 0) {
                Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                if (c3.moveToFirst()) {
                    do {
                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                        entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                        entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                        entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                        entity.setMA_LOAI_CPHI("");
                        entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                        entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                        entity.setDON_GIA_KH("0");
                        entity.setTT_DON_GIA("");
                        entity.setLOAI(0);
                        entity.setCHUNG_LOAI(1);
                        entity.setSO_HUU(3);
                        entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                        entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                        entity.setTT_TU_TUC(0);
                        entity.setTT_THU_HOI(0);
                        arrVatTu.add(entity);
                        arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                    } while (c3.moveToNext());
                }
            }

            adapterDuToan = new EsspVatTuDuToanAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
            rvDuToan.setAdapter(adapterDuToan);

//            rvDuToanNht.setVisibility(View.GONE);

//            Cursor c2 = connection.getDuToanNhtByIdHS(HOSO_ID);
//            if(c2.moveToFirst()){
//                do {
//                    EsspEntityVatTuNHT entityVatTuNHT = new EsspEntityVatTuNHT();
//                    entityVatTuNHT.setVTKHTT_ID(c2.getInt(c2.getColumnIndex("VTKHTT_ID")));
//                    entityVatTuNHT.setHOSO_ID(c2.getInt(c2.getColumnIndex("HOSO_ID")));
//                    entityVatTuNHT.setTENVTU_MTB(c2.getString(c2.getColumnIndex("TENVTU_MTB")));
//                    entityVatTuNHT.setSO_LUONG(c2.getString(c2.getColumnIndex("SO_LUONG")));
//                    entityVatTuNHT.setDVI_TINH(c2.getString(c2.getColumnIndex("DVI_TINH")));
//                    entityVatTuNHT.setDON_GIA_KH(c2.getString(c2.getColumnIndex("DON_GIA_KH")));
//                    entityVatTuNHT.setBBAN_GHI_CHU(c2.getString(c2.getColumnIndex("GHI_CHU")));
//                    entityVatTuNHT.setTINH_TRANG(c2.getInt(c2.getColumnIndex("TINH_TRANG")));
//                    arrVatTuNht.add(entityVatTuNHT);
//                    arrIDVT_NHT.add(c2.getInt(c2.getColumnIndex("VTKHTT_ID")));
//                }while (c2.moveToNext());
//            }
//            adapterDuToanNHT = new EsspVatTuDuToanNHTAdapter(arrVatTuNht, HOSO_ID);
//            rvDuToanNht.setAdapter(adapterDuToanNHT);

//            try {
//                tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                        .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1))
//                                + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//            } catch (Exception ex) {
//                tvThanhTien.setText("Thành tiền: 0");
//            }
            tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), false);

            ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenuVatTu(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
                }
            });

            ibAddNht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenuVatTuNgoaiHeThong(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
                }
            });

            spLoaiCP.post(new Runnable() {
                @Override
                public void run() {
                    spLoaiCP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                if (spLoaiCP.getSelectedItemPosition() == 0) {
                                    ibAddNht.setVisibility(View.VISIBLE);//VISIBLE
                                } else {
                                    ibAddNht.setVisibility(View.GONE);//GONE
                                }
                                arrMaVatTu = new ArrayList<>();
                                arrVatTu = new ArrayList<>();
                                Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
                                if (c.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c.getInt(c.getColumnIndex("ID")));
                                        entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                        entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                        entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                        entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                        entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                        entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                        entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                        entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                        entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                        entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                        entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                        entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                    } while (c.moveToNext());
                                }
                                if (spLoaiCP.getSelectedItemPosition() == 0) {
                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(0);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                } else {
                                    ibAddNht.setVisibility(View.GONE);
                                }
                                adapterDuToan = new EsspVatTuDuToanAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
                                rvDuToan.setAdapter(adapterDuToan);

//                                try {
//                                    tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                            .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1))
//                                                    + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                                } catch (Exception ex) {
//                                    tvThanhTien.setText("Thành tiền: 0");
//                                }
                                tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), false);
                            } catch (Exception ex) {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Lỗi", Color.RED, "Lỗi thay đổi sở hữu", Color.WHITE, "OK", Color.RED);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi cập nhật khảo sát", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuDuToanMoi(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_dutoan);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final TextView tvThanhTien = (TextView) dialog.findViewById(R.id.essp_dialog_dutoan_tv_thanhtien);
            final Spinner spLoaiCP = (Spinner) dialog.findViewById(R.id.essp_dialog_dutoan_sp_loai_cp);
            final Spinner spKhaiGia = (Spinner) dialog.findViewById(R.id.essp_dialog_dutoan_sp_khaigia);
            final RecyclerView rvDuToan = (RecyclerView) dialog.findViewById(R.id.essp_dialog_dutoan_rv_dutoan);
            Button ibAdd = (Button) dialog.findViewById(R.id.essp_dialog_dutoan_ib_add);
            final Button ibAddNht = (Button) dialog.findViewById(R.id.essp_dialog_dutoan_ib_add_nht);
            final Button btViewKhaiGia = (Button) dialog.findViewById(R.id.essp_dialog_dutoan_ib_viewKhaiGia);


            //TODO VinhNB
            /*dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    Log.d(TAG, "onKey: at");
                    return false;
                }
            });
*/
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                        Log.d(TAG, "onKey: at");
                        dialog.dismiss();
                    }
                    return true;
                }
            });


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            rvDuToan.setHasFixedSize(true);
            rvDuToan.setLayoutManager(layoutManager);

            String[] strCPs = {"Khách hàng", "Công ty"};
            ArrayAdapter<String> adapterCP = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, strCPs);
            spLoaiCP.setAdapter(adapterCP);

            String[] strKhaiGias = {"Dự toán vật tư", "Khai giá"};
            ArrayAdapter<String> adapterKG = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, strKhaiGias);
            spKhaiGia.setAdapter(adapterKG);

            int kg = connection.getKhaiGia(HOSO_ID);
            spKhaiGia.setSelection(kg);

            if (kg == 0) {
                ibAddNht.setVisibility(View.GONE);
                btViewKhaiGia.setVisibility(View.GONE);
            } else {
                ibAddNht.setVisibility(View.VISIBLE);
                btViewKhaiGia.setVisibility(View.VISIBLE);
            }

            arrMaVatTu = new ArrayList<>();
            arrVatTu = new ArrayList<>();
            arrVatTuNht = new ArrayList<>();
            arrIDVT_NHT = new ArrayList<>();
            Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
            if (c.moveToFirst()) {
                do {
                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                    entity.setID(c.getInt(c.getColumnIndex("ID")));
                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                    entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                    entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                    entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                    entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                    entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                    entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                    entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                    entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                    entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                    arrVatTu.add(entity);
                    arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                } while (c.moveToNext());
            }

            if (spLoaiCP.getSelectedItemPosition() == 0 && spKhaiGia.getSelectedItemPosition() == 1) {
                Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                if (c3.moveToFirst()) {
                    do {
                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                        entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                        entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                        entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                        entity.setMA_LOAI_CPHI("");
                        entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                        entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                        entity.setDON_GIA_KH("0");
                        entity.setTT_DON_GIA("");
                        entity.setLOAI(0);
                        entity.setCHUNG_LOAI(1);
                        entity.setSO_HUU(3);
                        entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                        entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                        entity.setTT_TU_TUC(0);
                        entity.setTT_THU_HOI(0);
                        entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                        entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                        entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                        arrVatTu.add(entity);
                        arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                    } while (c3.moveToNext());
                }
            }

            adapterDuToanMoi = new EsspVatTuDuToanMoiAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, spKhaiGia.getSelectedItemPosition());
            rvDuToan.setAdapter(adapterDuToanMoi);

            ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (connection.getKhaiGia(HOSO_ID) == 0 || spLoaiCP.getSelectedItemPosition() == 1 || !EsspCommon.getMaDviqly().contains("PD")) {
                        popupMenuVatTuMoi(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
                    } else {
                        popupMenuVatTuKhaiGiaMoi(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, "", true, false, spKhaiGia.getSelectedItemPosition() == 0 ? false : true, 0);
                    }
                }
            });

            ibAddNht.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EsspCommon.getMaDviqly().contains("PD")) {
                        popupMenuVatTuKhaiGiaMoi(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, "", true, true, spKhaiGia.getSelectedItemPosition() == 0 ? false : true, 0);
                    } else {
                        popupMenuVatTuNgoaiHeThong(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien);
                    }
                }
            });

            btViewKhaiGia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupChiTietKhaiGia(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
                }
            });

            spKhaiGia.post(new Runnable() {
                @Override
                public void run() {
                    spKhaiGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                connection.updateKhaiGia(HOSO_ID, position);
                                if (position == 0) {
                                    ibAddNht.setVisibility(View.GONE);
                                    btViewKhaiGia.setVisibility(View.GONE);
                                } else {
                                    ibAddNht.setVisibility(View.VISIBLE);
                                    btViewKhaiGia.setVisibility(View.VISIBLE);
                                }

                                arrMaVatTu = new ArrayList<>();
                                arrVatTu = new ArrayList<>();
                                Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
                                if (c.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c.getInt(c.getColumnIndex("ID")));
                                        entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                        entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                        entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                        entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                        entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                        entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                        entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                        entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                        entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                        entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                        entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                        entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                        entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                        entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                        entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                    } while (c.moveToNext());
                                }

                                if (spLoaiCP.getSelectedItemPosition() == 0 && position == 1) {

                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(0);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                            entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                            entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                }

                                adapterDuToanMoi = new EsspVatTuDuToanMoiAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, spKhaiGia.getSelectedItemPosition());
                                rvDuToan.setAdapter(adapterDuToanMoi);

                                //TODO VinhNB sửa thêm. Lý do: nếu vật tư công ty thì không cần phải tính thành tiền
                                /*tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1),
                                        spKhaiGia.getSelectedItemPosition() == 0 ? false : true);*/
                                int SO_HUU = spLoaiCP.getSelectedItemPosition() + 1;
                                if (SO_HUU != 2)
                                    tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(SO_HUU),
                                            spKhaiGia.getSelectedItemPosition() == 0 ? false : true);
                                else tvThanhTien.setText("0");
                            } catch (Exception ex) {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Lỗi", Color.RED, "Lỗi thay đổi kiểu dự toán", Color.WHITE, "OK", Color.RED);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            spLoaiCP.post(new Runnable() {
                @Override
                public void run() {
                    spLoaiCP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                if (spLoaiCP.getSelectedItemPosition() == 0) {
                                    ibAddNht.setVisibility(View.VISIBLE);//VISIBLE
                                    spKhaiGia.setVisibility(View.VISIBLE);//VISIBLE
                                    btViewKhaiGia.setVisibility(View.VISIBLE);//VISIBLE
                                } else {
                                    ibAddNht.setVisibility(View.GONE);//GONE
                                    spKhaiGia.setVisibility(View.GONE);//GONE
                                    btViewKhaiGia.setVisibility(View.GONE);//GONE
                                }
                                arrMaVatTu = new ArrayList<>();
                                arrVatTu = new ArrayList<>();
                                Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
                                if (c.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c.getInt(c.getColumnIndex("ID")));
                                        entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                        entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                        entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                        entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                        entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                        entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                        entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                        entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                        entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                        entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                        entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                        entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                    } while (c.moveToNext());
                                }
                                if (spLoaiCP.getSelectedItemPosition() == 0 && spKhaiGia.getSelectedItemPosition() == 1) {
                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(0);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                } else {
                                    ibAddNht.setVisibility(View.GONE);
                                }
                                adapterDuToanMoi = new EsspVatTuDuToanMoiAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, spKhaiGia.getSelectedItemPosition());
                                rvDuToan.setAdapter(adapterDuToanMoi);

                                tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1),
                                        spKhaiGia.getSelectedItemPosition() == 0 ? false : true);
                            } catch (Exception ex) {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Lỗi", Color.RED, "Lỗi thay đổi sở hữu", Color.WHITE, "OK", Color.RED);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), spKhaiGia.getSelectedItemPosition() == 0 ? false : true);
            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi cập nhật khảo sát", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuVatTuNgoaiHeThong(final int HOSO_ID, final String SO_HUU, final TextView tvThanhTien) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_vattu_ngoaiht);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final EditText etTenVT = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_tenvtu);
            final EditText etSoLuong = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_soluong);
            final EditText etDonVi = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_dvi_tinh);
            final EditText etDonGia = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_dongia);
            Button btAdd = (Button) dialog.findViewById(R.id.essp_dialog_vattu_bt_add);
            final CheckBox cbVatTuChinh = (CheckBox) dialog.findViewById(R.id.essp_dialog_vattu_ckVatTuChinh);

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String tenVatTu = etTenVT.getText().toString();
                        String soLuong = etSoLuong.getText().toString();
                        String donVi = etDonVi.getText().toString();
                        String donGia = etDonGia.getText().toString();
                        int chungLoai = cbVatTuChinh.isChecked() ? 1 : 0;
                        etTenVT.setError(null);
                        etSoLuong.setError(null);
                        etDonVi.setError(null);
                        etDonGia.setError(null);
                        if (tenVatTu.isEmpty()) {
                            etTenVT.setError("Bạn chưa nhập tên vật tư");
                            etTenVT.requestFocus();
                        } else if (soLuong.isEmpty()) {
                            etSoLuong.setError("Bạn chưa nhập số lượng");
                            etSoLuong.requestFocus();
                        } else if (donVi.isEmpty()) {
                            etDonVi.setError("Bạn chưa nhập đơn vị tính");
                            etDonVi.requestFocus();
                        } else if (donGia.isEmpty()) {
                            etDonGia.setError("Bạn chưa nhập đơn giá");
                            etDonGia.requestFocus();
                        } else {
                            if (connection.insertDataVTNgoaiHT(HOSO_ID, tenVatTu, soLuong, donVi, donGia, "", 0, chungLoai) != -1) {
                                arrVatTu.clear();
                                arrMaVatTu.clear();
                                Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                                if (c.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c.getInt(c.getColumnIndex("ID")));
                                        entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                        entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                        entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                        entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                        entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                        entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                        entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                        entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                        entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                        entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                        entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                        entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                    } while (c.moveToNext());
                                }
                                if (Integer.parseInt(SO_HUU) == 1) {
                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(chungLoai);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                }
                                adapterDuToanMoi.updateList(arrVatTu);

                                etTenVT.setText("");
                                etSoLuong.setText("");
                                etDonVi.setText("");
                                etDonGia.setText("");
                                etTenVT.requestFocus();
                                Toast.makeText(EsspMainFragment.this.getActivity(), "Thêm vật tư thành công", Toast.LENGTH_LONG).show();
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Lỗi", Color.RED, "Không thêm được vật tư", Color.WHITE, "OK", Color.RED);
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi thêm vật tư", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi thêm vật tư ngoài hệ thống", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuVatTu(final int HOSO_ID, final String SO_HUU, final TextView tvThanhTien) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_vattu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            RecyclerView rvVatTu = (RecyclerView) dialog.findViewById(R.id.essp_dialog_vattu_rv_dutoan);
            final EditText etSearch = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_search);
            ImageButton btClear = (ImageButton) dialog.findViewById(R.id.essp_dialog_vattu_ib_clear);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            rvVatTu.setHasFixedSize(true);
            rvVatTu.setLayoutManager(layoutManager);

            final ArrayList<EsspEntityVatTu> arrVatTu = new ArrayList<>();
            Cursor c = connection.getVatTu();
            if (c.moveToFirst()) {
                do {
                    EsspEntityVatTu entity = new EsspEntityVatTu();
                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                    if (!arrMaVatTu.contains(c.getString(c.getColumnIndex("MA_VTU"))))
                        arrVatTu.add(entity);
                } while (c.moveToNext());
            }
            adapterVatTu = new EsspVatTuHeThongAdapter(arrVatTu, SO_HUU, HOSO_ID);
            rvVatTu.setAdapter(adapterVatTu);

            btClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSearch.setText("");
                }
            });

            etSearch.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        etSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                List<EsspEntityVatTu> data = new ArrayList<>();
                                String query = Common.removeAccent(s.toString().trim().toLowerCase());
                                for (EsspEntityVatTu entity : arrVatTu) {
                                    if (Common.removeAccent(entity.getTEN_VTU().toLowerCase()).contains(query)) {
                                        data.add(entity);
                                    }
                                }
                                adapterVatTu.updateList(data);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi lọc vật tư", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    try {
//                        if (tvThanhTien != null)
//                            tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                    .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, SO_HUU)
//                                            + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                    } catch (Exception ex) {
//                        tvThanhTien.setText("Thành tiền: 0");
//                    }
                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi chọn vật tư dự toán", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuVatTuMoi(final int HOSO_ID, final String SO_HUU, final TextView tvThanhTien) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_vattu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            RecyclerView rvVatTu = (RecyclerView) dialog.findViewById(R.id.essp_dialog_vattu_rv_dutoan);
            final EditText etSearch = (EditText) dialog.findViewById(R.id.essp_dialog_vattu_et_search);
            ImageButton btClear = (ImageButton) dialog.findViewById(R.id.essp_dialog_vattu_ib_clear);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            rvVatTu.setHasFixedSize(true);
            rvVatTu.setLayoutManager(layoutManager);

            final ArrayList<EsspEntityVatTu> arrVatTu = new ArrayList<>();
            Cursor c = connection.getVatTu();
            if (c.moveToFirst()) {
                do {
                    EsspEntityVatTu entity = new EsspEntityVatTu();
                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                    entity.setDG_TRONGOI(c.getString(c.getColumnIndex("DG_TRONGOI")));
                    entity.setDG_NCONG(c.getString(c.getColumnIndex("DG_NCONG")));
                    entity.setDG_VTU(c.getString(c.getColumnIndex("DG_VTU")));
                    entity.setDG_MTCONG(c.getString(c.getColumnIndex("DG_MTCONG")));
                    if (!arrMaVatTu.contains(c.getString(c.getColumnIndex("MA_VTU"))))
                        arrVatTu.add(entity);
                } while (c.moveToNext());
            }
            adapterVatTuMoi = new EsspVatTuHeThongMoiAdapter(arrVatTu, SO_HUU, HOSO_ID, connection.getLoaiHinhTToan(HOSO_ID));
            rvVatTu.setAdapter(adapterVatTuMoi);

            btClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSearch.setText("");
                }
            });

            etSearch.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        etSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                List<EsspEntityVatTu> data = new ArrayList<>();
                                String query = Common.removeAccent(s.toString().trim().toLowerCase());
                                for (EsspEntityVatTu entity : arrVatTu) {
                                    if (Common.removeAccent(entity.getTEN_VTU().toLowerCase()).contains(query)) {
                                        data.add(entity);
                                    }
                                }
                                adapterVatTuMoi.updateList(data);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi lọc vật tư", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi chọn vật tư dự toán", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuVatTuKhaiGiaMoi(final int HOSO_ID, final String SO_HUU, final TextView tvThanhTien,
                                          final String MA_VTU, final boolean isAddDT, final boolean isNHT, final boolean isKhaiGia, final int ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_khaigia_vattu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final AutoCompleteTextView etVatTu = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_VatTu);
            final EditText etDonVi = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_donvi);
            final EditText etSoLuong = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_SoLuong);
            final EditText et1nc = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_1nc);
            final EditText et2nc = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_2nc);
            final EditText etMTC = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_mtc);

            final EditText etDgVatTuGoc = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgVatTuGoc);
            final EditText etDgNhanCongGoc = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgNhanCongGoc);
            final EditText etDgMTCGoc = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgMTCGoc);

            final EditText etDgVatTuDChinh = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgVatTu);
            final EditText etDgNhanCongDChinh = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgNhanCong);
            final EditText etDgMTCDChinh = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgMTC);
            final EditText etDgTruocThue = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_dgTruocThue);
            final EditText etGtTruocThue = (EditText) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_et_gtTruocThue);

            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_bt_luu);
            final CheckBox cbVTuChinh = (CheckBox) dialog.findViewById(R.id.essp_dialog_khaigia_vattu_cbVTuChinh);

            //TODO VinhNB thêm, kiểm tra tính trạng sau khi gửi không cho hiển thị nút lưu khi hiển thị form này
            Cursor cursorCheck = connection.getAllDataDTByMaHSvsSH2(HOSO_ID, SO_HUU, MA_VTU);

            //nếu không phải thêm mới
            if (!isAddDT) {
                etDgVatTuDChinh.setEnabled(false);
                etDgNhanCongDChinh.setEnabled(false);
                etDgMTCDChinh.setEnabled(false);
                etDgTruocThue.setEnabled(false);
                etGtTruocThue.setEnabled(false);
                if (!isNHT) {
                    etVatTu.setEnabled(false);
                    etDgVatTuGoc.setEnabled(false);
                    etDgNhanCongGoc.setEnabled(false);
                    etDgMTCGoc.setEnabled(false);
                } else {
                    etVatTu.setEnabled(true);
                    etDgVatTuGoc.setEnabled(true);
                    etDgNhanCongGoc.setEnabled(true);
                    etDgMTCGoc.setEnabled(true);
                }
            } else {
                etVatTu.setEnabled(true);
                etDgVatTuGoc.setEnabled(true);
                etDgNhanCongGoc.setEnabled(true);
                etDgMTCGoc.setEnabled(true);

                etDgVatTuDChinh.setEnabled(true);
                etDgNhanCongDChinh.setEnabled(true);
                etDgMTCDChinh.setEnabled(true);
                etDgTruocThue.setEnabled(true);
                etGtTruocThue.setEnabled(true);

            }
            int LOAIHINH_TTOAN = connection.getLoaiHinhTToan(HOSO_ID);
            if (!isNHT) {
                cbVTuChinh.setVisibility(View.GONE);
                setDataSelectVatTu(etVatTu);

                etVatTu.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (etVatTu.getText().toString().contains("-")) {
                                String MA_VTU = etVatTu.getText().toString().split("-")[0].trim();
                                Cursor c = connection.getDG(MA_VTU);
                                if (c.moveToFirst()) {
                                    etDonVi.setText(c.getString(c.getColumnIndex("DVI_TINH")));
                                    etDgVatTuGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_VTU"))).toString()));
                                    etDgNhanCongGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_NCONG"))).toString()));
                                    etDgMTCGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_MTCONG"))).toString()));
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            } else {
                cbVTuChinh.setVisibility(View.VISIBLE);
            }

            if (!isAddDT) {
                if (!isNHT) {
                    etVatTu.setEnabled(false);
                    Cursor c = connection.getAllDataDTByMaHSvsSH2(HOSO_ID, SO_HUU, MA_VTU);
                    if (c.moveToFirst()) {
                        StringBuilder sMA_VTU = new StringBuilder(c.getString(c.getColumnIndex("MA_VTU")));
                        etVatTu.setText(sMA_VTU.append(" - ").append(connection.getTenVTu(c.getString(c.getColumnIndex("MA_VTU")))).toString());
                        etDonVi.setText(c.getString(c.getColumnIndex("DVI_TINH")));
                        etSoLuong.setText(c.getString(c.getColumnIndex("SO_LUONG")));
                        et1nc.setText(c.getString(c.getColumnIndex("HSDC_K1NC")));
                        et2nc.setText(c.getString(c.getColumnIndex("HSDC_K2NC")));
                        etMTC.setText(c.getString(c.getColumnIndex("HSDC_MTC")));
                        etDgVatTuGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_VTU"))).toString()));
                        etDgNhanCongGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_NCONG"))).toString()));
                        etDgMTCGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_MTCONG"))).toString()));
                        double DG_VTU_DCHING = 0d;
                        double DG_NCONG_DCHING = 0d;
                        double DG_MTC_DCHING = 0d;
//                        if (!isNHT) {
                        switch (LOAIHINH_TTOAN) {
                            case EsspConstantVariables.DT_TRONGOI:
                                DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
                                break;
                            case EsspConstantVariables.DT_TUTUC_CAP:
                                if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 1) {//dây cáp
                                    DG_VTU_DCHING = 0d;
                                    DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                    DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
                                } else {
                                    DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                    DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                    DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
                                }
                                break;
                            case EsspConstantVariables.DT_TUTUC_VTU:
                                DG_VTU_DCHING = 0d;
                                DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                                DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
                                break;
                            case EsspConstantVariables.DT_TUTUC_HTOAN:
                                DG_VTU_DCHING = 0d;
                                DG_NCONG_DCHING = 0d;
                                DG_MTC_DCHING = 0d;
                                break;
                        }
//                        } else {
//                            DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                            DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                            DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                        }
                        etDgVatTuDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_VTU_DCHING).toString()));
                        etDgNhanCongDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_NCONG_DCHING).toString()));
                        etDgMTCDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_MTC_DCHING).toString()));
                        etDgTruocThue.setText(Common.formatFloatNumber(new BigDecimal(DG_VTU_DCHING + DG_NCONG_DCHING + DG_MTC_DCHING).toString()));
                        etGtTruocThue.setText(Common.formatFloatNumber(new BigDecimal((DG_VTU_DCHING + DG_NCONG_DCHING + DG_MTC_DCHING) * c.getFloat(c.getColumnIndex("SO_LUONG"))).toString()));
                    }
                } else {
                    Cursor c = connection.getDuToanNHTById(ID);
                    if (c.moveToFirst()) {
                        etVatTu.setText(c.getString(c.getColumnIndex("TENVTU_MTB")));
                        etDonVi.setText(c.getString(c.getColumnIndex("DVI_TINH")));
                        etSoLuong.setText(c.getString(c.getColumnIndex("SO_LUONG")));
                        et1nc.setText(c.getString(c.getColumnIndex("HSDC_K1NC")));
                        et2nc.setText(c.getString(c.getColumnIndex("HSDC_K2NC")));
                        etMTC.setText(c.getString(c.getColumnIndex("HSDC_MTC")));
                        etDgVatTuGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_VTU"))).toString()));
                        etDgNhanCongGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_NCONG"))).toString()));
                        etDgMTCGoc.setText(Common.formatFloatNumber(new BigDecimal(c.getString(c.getColumnIndex("DG_MTCONG"))).toString()));
                        double DG_VTU_DCHING = 0d;
                        double DG_NCONG_DCHING = 0d;
                        double DG_MTC_DCHING = 0d;
//                        if (!isNHT) {
//                            switch (LOAIHINH_TTOAN) {
//                                case EsspConstantVariables.DT_TRONGOI:
//                                    DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                    DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                    DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                                    break;
//                                case EsspConstantVariables.DT_TUTUC_CAP:
//                                    if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 1) {//dây cáp
//                                        DG_VTU_DCHING = 0d;
//                                        DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                        DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                                    } else {
//                                        DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                        DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                        DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                                    }
//                                    break;
//                                case EsspConstantVariables.DT_TUTUC_VTU:
//                                    DG_VTU_DCHING = 0d;
//                                    DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                    DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                                    break;
//                                case EsspConstantVariables.DT_TUTUC_HTOAN:
//                                    DG_VTU_DCHING = 0d;
//                                    DG_NCONG_DCHING = 0d;
//                                    DG_MTC_DCHING = 0d;
//                                    break;
//                            }
//                        } else {
                        //TODO VinhNB sửa
                        //DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                        DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU"));
                        DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
                        DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                        }
                        etDgVatTuDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_VTU_DCHING).toString()));
                        etDgNhanCongDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_NCONG_DCHING).toString()));
                        etDgMTCDChinh.setText(Common.formatFloatNumber(new BigDecimal(DG_MTC_DCHING).toString()));
                        etDgTruocThue.setText(Common.formatFloatNumber(new BigDecimal(DG_VTU_DCHING + DG_NCONG_DCHING + DG_MTC_DCHING).toString()));
                        etGtTruocThue.setText(Common.formatFloatNumber(new BigDecimal((DG_VTU_DCHING + DG_NCONG_DCHING + DG_MTC_DCHING) * c.getFloat(c.getColumnIndex("SO_LUONG"))).toString()));
                        if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 0)
                            cbVTuChinh.setChecked(false);
                        else
                            cbVTuChinh.setChecked(true);
                    }
                }
            } else {
                etVatTu.setEnabled(true);
            }

            if (isNHT) {
                etDgVatTuGoc.setEnabled(true);
                etDgNhanCongGoc.setEnabled(true);
                etDgMTCGoc.setEnabled(true);
                etDonVi.setEnabled(true);
            } else {
                etDgVatTuGoc.setEnabled(false);
                etDgNhanCongGoc.setEnabled(false);
                etDgMTCGoc.setEnabled(false);
                etDonVi.setEnabled(false);
            }

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etVatTu.setError(null);
                        etDgVatTuGoc.setError(null);
                        etDgNhanCongGoc.setError(null);
                        etDgMTCGoc.setError(null);
                        etDonVi.setError(null);

                        if (etVatTu.getText().toString().isEmpty()) {
                            etVatTu.setError("Bạn chưa nhập tên vật tư");
                            etVatTu.requestFocus();
                        } else if (etDgVatTuGoc.getText().toString().isEmpty()) {
                            if (isNHT) {
                                etDgVatTuGoc.setError("Bạn chưa nhập tên vật tư");
                                etDgVatTuGoc.requestFocus();
                            } else {
                                etDgVatTuGoc.setError("Danh mục vật tư của bạn chưa có đơn giá gốc");
                                etDgVatTuGoc.requestFocus();
                            }
                        } else if (etDgNhanCongGoc.getText().toString().isEmpty()) {
                            if (isNHT) {
                                etDgNhanCongGoc.setError("Bạn chưa nhập tên vật tư");
                                etDgNhanCongGoc.requestFocus();
                            } else {
                                etDgNhanCongGoc.setError("Danh mục vật tư của bạn chưa có đơn giá nhân công gốc");
                                etDgNhanCongGoc.requestFocus();
                            }
                        } else if (etDgMTCGoc.getText().toString().isEmpty()) {
                            if (isNHT) {
                                etDgMTCGoc.setError("Bạn chưa nhập tên vật tư");
                                etDgMTCGoc.requestFocus();
                            } else {
                                etDgMTCGoc.setError("Danh mục vật tư của bạn chưa có đơn giá MTC gốc");
                                etDgMTCGoc.requestFocus();
                            }
                        } else if (etDonVi.getText().toString().isEmpty() && isNHT) {
                            etDonVi.setError("Bạn chưa nhập đơn vị tính");
                            etDonVi.requestFocus();
                        } else {
                            String MA_VTU = etVatTu.getText().toString().split("-")[0].trim();
                            float SO_LUONG = etSoLuong.getText().toString().isEmpty() ? 1.0f : Float.parseFloat(etSoLuong.getText().toString());
                            float _1NC = et1nc.getText().toString().isEmpty() ? 1.0f : Float.parseFloat(et1nc.getText().toString());
                            float _2NC = et2nc.getText().toString().isEmpty() ? 1.0f : Float.parseFloat(et2nc.getText().toString());
                            float _MTC = etMTC.getText().toString().isEmpty() ? 1.0f : Float.parseFloat(etMTC.getText().toString());
                            double DG_VTU_GOC = Double.parseDouble(etDgVatTuGoc.getText().toString());
                            double DG_NCONG_GOC = Double.parseDouble(etDgNhanCongGoc.getText().toString());
                            double DG_MTC_GOC = Double.parseDouble(etDgMTCGoc.getText().toString());
                            //TODO VinhNB sua
//                            double DG_VTU_DCHING = DG_VTU_GOC * _1NC * _2NC;
                            double DG_VTU_DCHING = DG_VTU_GOC;
                            double DG_NCONG_DCHING = DG_NCONG_GOC * _1NC * _2NC;
                            double DG_MTC_DCHING = DG_MTC_GOC * _MTC;
                            double DG_TRUOC_THUE = DG_VTU_DCHING + DG_NCONG_DCHING + DG_MTC_DCHING;
                            double GT_TRUOC_THUE = DG_TRUOC_THUE * SO_LUONG;

                            String THANH_TIEN = "0";
                            String DON_GIA = "0";
                            Cursor cVT = connection.getVatTu(MA_VTU);
                            if (cVT.moveToFirst()) {
                                switch (connection.getLoaiHinhTToan(HOSO_ID)) {
                                    case EsspConstantVariables.DT_TRONGOI:
                                        if (cVT.getInt(cVT.getColumnIndex("CHUNG_LOAI")) == 1) {
                                            DON_GIA = cVT.getString(cVT.getColumnIndex("DG_TRONGOI"));
                                            THANH_TIEN = String.valueOf(cVT.getDouble(cVT.getColumnIndex("DG_TRONGOI")) * SO_LUONG);
                                        } else {
                                            if (EsspCommon.getMaDviqly().contains("PC")
                                                    || EsspCommon.getMaDviqly().contains("PP")) {
                                                DON_GIA = cVT.getString(cVT.getColumnIndex("DG_TRONGOI"));
                                                THANH_TIEN = String.valueOf(cVT.getDouble(cVT.getColumnIndex("DG_TRONGOI")) * SO_LUONG);
                                            } else {
                                                THANH_TIEN = "0";
                                                DON_GIA = "0";
                                            }
                                        }
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_CAP:
                                        if (cVT.getInt(cVT.getColumnIndex("CHUNG_LOAI")) == 1) {
                                            DON_GIA = cVT.getString(cVT.getColumnIndex("DON_GIA_KH"));
                                            THANH_TIEN = String.valueOf(cVT.getDouble(cVT.getColumnIndex("DON_GIA_KH")) * SO_LUONG);
                                        }
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_VTU:
                                        DON_GIA = String.valueOf(cVT.getDouble(cVT.getColumnIndex("DG_NCONG")) * 10 / 100);
                                        THANH_TIEN = String.valueOf(cVT.getDouble(cVT.getColumnIndex("DG_NCONG")) * SO_LUONG * 10 / 100);
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_HTOAN:
                                        THANH_TIEN = "0";
                                        DON_GIA = "0";
                                        break;
                                }
                            }

                            if (isAddDT) {
                                if (!isNHT) {
                                    if (connection.insertDataDuToan(HOSO_ID, EsspCommon.getMaDviqly(), MA_VTU, "1", String.valueOf(SO_LUONG),
                                            DON_GIA, SO_HUU, "", THANH_TIEN, "0", "0", 0, 0,
                                            String.valueOf(_1NC), String.valueOf(_2NC), String.valueOf(_MTC)) != -1) {
                                        etVatTu.setText("");
                                        etDonVi.setText("");
                                        etSoLuong.setText("");
                                        et1nc.setText("");
                                        et2nc.setText("");
                                        etMTC.setText("");
                                        etDgVatTuGoc.setText("");
                                        etDgNhanCongGoc.setText("");
                                        etDgMTCGoc.setText("");
                                        etDgVatTuDChinh.setText("");
                                        etDgNhanCongDChinh.setText("");
                                        etDgMTCDChinh.setText("");
                                        etDgTruocThue.setText("");
                                        etGtTruocThue.setText("");

                                        arrVatTu = new ArrayList<EsspEntityVatTuDuToan>();
                                        arrMaVatTu = new ArrayList<String>();
                                        Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                                        if (c.moveToFirst()) {
                                            do {
                                                EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                entity.setID(c.getInt(c.getColumnIndex("ID")));
                                                entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                                entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                                entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                                entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                                entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                                entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                                entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                                entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                                entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                                entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                                entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                                entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                                entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                                entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                                entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                                entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                                entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                                arrVatTu.add(entity);
                                                arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                            } while (c.moveToNext());
                                        }
                                        if (Integer.parseInt(SO_HUU) == 1) {
                                            Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                            if (c3.moveToFirst()) {
                                                do {
                                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                    entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                                    entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                    entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                                    entity.setMA_LOAI_CPHI("");
                                                    entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                                    entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                                    entity.setDON_GIA_KH("0");
                                                    entity.setTT_DON_GIA("");
                                                    entity.setLOAI(0);
                                                    entity.setCHUNG_LOAI(1);
                                                    entity.setSO_HUU(3);
                                                    entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                                    entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                                    entity.setTT_TU_TUC(0);
                                                    entity.setTT_THU_HOI(0);
                                                    entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                                    entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                                    entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                                    arrVatTu.add(entity);
                                                    arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                } while (c3.moveToNext());
                                            }
                                        }
                                        adapterDuToanMoi.updateList(arrVatTu);
                                    } else {
                                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Thêm vật tư thất bại", Color.WHITE, "OK", Color.RED);
                                    }
                                } else {
                                    if (connection.insertDataVTNgoaiHT(HOSO_ID, etVatTu.getText().toString(), String.valueOf(SO_LUONG),
                                            etDonVi.getText().toString(), String.valueOf(DG_TRUOC_THUE), "", 0, cbVTuChinh.isChecked() ? 1 : 0, "0",
                                            String.valueOf(DG_NCONG_GOC), String.valueOf(DG_VTU_GOC),
                                            String.valueOf(DG_MTC_GOC), String.valueOf(_1NC), String.valueOf(_2NC), String.valueOf(_MTC)) != -1) {
                                        etVatTu.setText("");
                                        etDonVi.setText("");
                                        etSoLuong.setText("");
                                        et1nc.setText("");
                                        et2nc.setText("");
                                        etMTC.setText("");
                                        etDgVatTuGoc.setText("");
                                        etDgNhanCongGoc.setText("");
                                        etDgMTCGoc.setText("");
                                        etDgVatTuDChinh.setText("");
                                        etDgNhanCongDChinh.setText("");
                                        etDgMTCDChinh.setText("");
                                        etDgTruocThue.setText("");
                                        etGtTruocThue.setText("");

                                        arrVatTu = new ArrayList<EsspEntityVatTuDuToan>();
                                        arrMaVatTu = new ArrayList<String>();
                                        Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                                        if (c.moveToFirst()) {
                                            do {
                                                EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                entity.setID(c.getInt(c.getColumnIndex("ID")));
                                                entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                                entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                                entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                                entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                                entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                                entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                                entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                                entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                                entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                                entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                                entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                                entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                                entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                                entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                                entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                                entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                                entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                                arrVatTu.add(entity);
                                                arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                            } while (c.moveToNext());
                                        }
                                        if (Integer.parseInt(SO_HUU) == 1) {
                                            Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                            if (c3.moveToFirst()) {
                                                do {
                                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                    entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                                    entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                    entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                                    entity.setMA_LOAI_CPHI("");
                                                    entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                                    entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                                    entity.setDON_GIA_KH("0");
                                                    entity.setTT_DON_GIA("");
                                                    entity.setLOAI(0);
                                                    entity.setCHUNG_LOAI(1);
                                                    entity.setSO_HUU(3);
                                                    entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                                    entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                                    entity.setTT_TU_TUC(0);
                                                    entity.setTT_THU_HOI(0);
                                                    entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                                    entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                                    entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                                    arrVatTu.add(entity);
                                                    arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                } while (c3.moveToNext());
                                            }
                                        }
                                        adapterDuToanMoi.updateList(arrVatTu);
                                    } else {
                                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Thêm vật tư thất bại", Color.WHITE, "OK", Color.RED);
                                    }
                                }
                            } else {
                                if (!isNHT) {
                                    if (connection.updateDataDuToan(HOSO_ID, EsspCommon.getMaDviqly(), MA_VTU, "1", String.valueOf(SO_LUONG),
                                            DON_GIA, SO_HUU, "", THANH_TIEN, "0", "0", 0, 0,
                                            String.valueOf(_1NC), String.valueOf(_2NC), String.valueOf(_MTC)) != -1) {
                                        etVatTu.setText("");
                                        etDonVi.setText("");
                                        etSoLuong.setText("");
                                        et1nc.setText("");
                                        et2nc.setText("");
                                        etMTC.setText("");
                                        etDgVatTuGoc.setText("");
                                        etDgNhanCongGoc.setText("");
                                        etDgMTCGoc.setText("");
                                        etDgVatTuDChinh.setText("");
                                        etDgNhanCongDChinh.setText("");
                                        etDgMTCDChinh.setText("");
                                        etDgTruocThue.setText("");
                                        etGtTruocThue.setText("");

                                        arrVatTu = new ArrayList<EsspEntityVatTuDuToan>();
                                        arrMaVatTu = new ArrayList<String>();
                                        Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                                        if (c.moveToFirst()) {
                                            do {
                                                EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                entity.setID(c.getInt(c.getColumnIndex("ID")));
                                                entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                                entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                                entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                                entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                                entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                                entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                                entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                                entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                                entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                                entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                                entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                                entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                                entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                                entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                                entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                                entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                                entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                                arrVatTu.add(entity);
                                                arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                            } while (c.moveToNext());
                                        }
                                        if (Integer.parseInt(SO_HUU) == 1) {
                                            Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                            if (c3.moveToFirst()) {
                                                do {
                                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                    entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                                    entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                    entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                                    entity.setMA_LOAI_CPHI("");
                                                    entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                                    entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                                    entity.setDON_GIA_KH("0");
                                                    entity.setTT_DON_GIA("");
                                                    entity.setLOAI(0);
                                                    entity.setCHUNG_LOAI(1);
                                                    entity.setSO_HUU(3);
                                                    entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                                    entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                                    entity.setTT_TU_TUC(0);
                                                    entity.setTT_THU_HOI(0);
                                                    entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                                    entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                                    entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                                    arrVatTu.add(entity);
                                                    arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                } while (c3.moveToNext());
                                            }
                                        }
                                        adapterDuToanMoi.updateList(arrVatTu);
                                    } else {
                                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Thêm vật tư thất bại", Color.WHITE, "OK", Color.RED);
                                    }
                                } else {
                                    if (connection.updateDataVTNgoaiHT(ID, etVatTu.getText().toString(), String.valueOf(SO_LUONG),
                                            etDonVi.getText().toString(), String.valueOf(DG_TRUOC_THUE), "", 0, cbVTuChinh.isChecked() ? 1 : 0, "0",
                                            String.valueOf(DG_NCONG_GOC), String.valueOf(DG_VTU_GOC),
                                            String.valueOf(DG_MTC_GOC), String.valueOf(_1NC), String.valueOf(_2NC), String.valueOf(_MTC)) != -1) {
                                        etVatTu.setText("");
                                        etDonVi.setText("");
                                        etSoLuong.setText("");
                                        et1nc.setText("");
                                        et2nc.setText("");
                                        etMTC.setText("");
                                        etDgVatTuGoc.setText("");
                                        etDgNhanCongGoc.setText("");
                                        etDgMTCGoc.setText("");
                                        etDgVatTuDChinh.setText("");
                                        etDgNhanCongDChinh.setText("");
                                        etDgMTCDChinh.setText("");
                                        etDgTruocThue.setText("");
                                        etGtTruocThue.setText("");

                                        arrVatTu = new ArrayList<EsspEntityVatTuDuToan>();
                                        arrMaVatTu = new ArrayList<String>();
                                        Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                                        if (c.moveToFirst()) {
                                            do {
                                                EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                entity.setID(c.getInt(c.getColumnIndex("ID")));
                                                entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                                entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                                entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                                entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                                entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                                entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                                entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                                entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                                entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                                entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                                entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                                entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                                entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                                entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                                entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                                entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                                entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                                arrVatTu.add(entity);
                                                arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                            } while (c.moveToNext());
                                        }
                                        if (Integer.parseInt(SO_HUU) == 1) {
                                            Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                            if (c3.moveToFirst()) {
                                                do {
                                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                                    entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                                    entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                    entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                                    entity.setMA_LOAI_CPHI("");
                                                    entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                                    entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                                    entity.setDON_GIA_KH("0");
                                                    entity.setTT_DON_GIA("");
                                                    entity.setLOAI(0);
                                                    entity.setCHUNG_LOAI(1);
                                                    entity.setSO_HUU(3);
                                                    entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                                    entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                                    entity.setTT_TU_TUC(0);
                                                    entity.setTT_THU_HOI(0);
                                                    entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                                    entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                                    entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                                    arrVatTu.add(entity);
                                                    arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                                } while (c3.moveToNext());
                                            }
                                        }
                                        adapterDuToanMoi.updateList(arrVatTu);
                                    } else {
                                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Thêm vật tư thất bại", Color.WHITE, "OK", Color.RED);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi thêm vật tư", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, isKhaiGia);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi chọn vật tư dự toán", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupChiTietKhaiGia(final int HOSO_ID, final String SO_HUU) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_chitiet_khaigia);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            TextView tvCpVatLieu = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpVatLieu);
            TextView tvCpNhanCong = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpNhanCong);
            TextView tvCpMayThiCong = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpMayThiCong);
            TextView tvCpTrucTiepKhac = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpTrucTiepKhac);
            TextView tvCpTrucTiep = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpTrucTiep);
            TextView tvCpChung = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_cpChung);
            TextView tvChiuThueTruoc = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_chiuThueTruoc);
            TextView tvGtXayLapTruocThue = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_gtXayLapTruocThue);
            TextView tvKhaoSat = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_khaoSat);
            TextView tvTongDoanhThuTruocThue = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_tongDoanhThuTruocThue);
            TextView tvVat = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_vat);
            TextView tvTongCong = (TextView) dialog.findViewById(R.id.essp_dialog_chitiet_khaigia_tv_tongCong);

            //TODO VinhNB thêm do ở biểu tổng hợp khai giá mới sẽ bỏ các giá trị này
            tvCpTrucTiepKhac.setVisibility(View.GONE);
            tvCpTrucTiep.setVisibility(View.GONE);
            tvCpChung.setVisibility(View.GONE);
            tvChiuThueTruoc.setVisibility(View.GONE);

            double cpVatLieu = 0d;
            double cpNhanCong = 0d;
            double cpMayThiCong = 0d;
            double cpTrucTiepKhac = 0d;
            double cpTrucTiep = 0d;
            double cpChung = 0d;
            double chiuThueTruoc = 0d;
            double gtXayLapTruocThue = 0d;
            double khaoSat = 0d;
            double tongDoanhThuTruocThue = 0d;
            double vat = 0d;
            double tongCong = 0d;

            int LOAIHINH_TTOAN = connection.getLoaiHinhTToan(HOSO_ID);
            double sumVatLieu = connection.sumVatLieu(LOAIHINH_TTOAN, HOSO_ID, SO_HUU);
            double sumVatTu = connection.sumVatTu(HOSO_ID);

            cpVatLieu = sumVatLieu + sumVatTu;
            cpNhanCong = connection.sumNhanCong(LOAIHINH_TTOAN, HOSO_ID, SO_HUU) + connection.sumNhanCong(HOSO_ID);
            cpMayThiCong = connection.sumMTC(LOAIHINH_TTOAN, HOSO_ID, SO_HUU) + connection.sumMTC(HOSO_ID);
            //TODO VINHNB sửa
//                    cpTrucTiepKhac = (cpVatLieu + cpNhanCong + cpMayThiCong) * 2 / 100;
//                    cpTrucTiep = cpVatLieu + cpNhanCong + cpMayThiCong + cpTrucTiepKhac;
//                    cpChung = connection.getLoaiCap(HOSO_ID) == 0 ? (cpNhanCong * 60 / 100) : (cpTrucTiep * 5.5 / 100);
//                    chiuThueTruoc = (cpTrucTiep + cpChung) * 6 / 100;
//                    gtXayLapTruocThue = cpTrucTiep + cpChung + chiuThueTruoc;

            cpTrucTiepKhac = 0;
            cpTrucTiep = 0;
            cpChung = 0;
            chiuThueTruoc = 0;
            gtXayLapTruocThue = cpVatLieu + cpNhanCong + cpMayThiCong;
//            cpTrucTiepKhac = (cpVatLieu + cpNhanCong + cpMayThiCong) * 2 / 100;
//            cpTrucTiep = cpVatLieu + cpNhanCong + cpMayThiCong + cpTrucTiepKhac;
//            cpChung = connection.getLoaiCap(HOSO_ID) == 0 ? (cpNhanCong * 60 / 100) : (cpTrucTiep * 5.5 / 100);
//            chiuThueTruoc = (cpTrucTiep + cpChung) * 6 / 100;
//            gtXayLapTruocThue = cpTrucTiep + cpChung + chiuThueTruoc;
            khaoSat = 0d;
            tongDoanhThuTruocThue = gtXayLapTruocThue + khaoSat;
            vat = gtXayLapTruocThue * 10 / 100;
            tongCong = gtXayLapTruocThue + vat;

            tvCpVatLieu.setText(Common.formatMoney(new BigDecimal(cpVatLieu).toString()) + " đ");
            tvCpNhanCong.setText(Common.formatMoney(new BigDecimal(cpNhanCong).toString()) + " đ");
            tvCpMayThiCong.setText(Common.formatMoney(new BigDecimal(cpMayThiCong).toString()) + " đ");
            tvCpTrucTiepKhac.setText(Common.formatMoney(new BigDecimal(cpTrucTiepKhac).toString()) + " đ");
            tvCpTrucTiep.setText(Common.formatMoney(new BigDecimal(cpTrucTiep).toString()) + " đ");
            tvCpChung.setText(Common.formatMoney(new BigDecimal(cpChung).toString()) + " đ");
            tvChiuThueTruoc.setText(Common.formatMoney(new BigDecimal(chiuThueTruoc).toString()) + " đ");
            tvGtXayLapTruocThue.setText(Common.formatMoney(new BigDecimal(gtXayLapTruocThue).toString()) + " đ");
            tvKhaoSat.setText(Common.formatMoney(new BigDecimal(khaoSat).toString()) + " đ");
            tvTongDoanhThuTruocThue.setText(Common.formatMoney(new BigDecimal(tongDoanhThuTruocThue).toString()) + " đ");
            tvVat.setText(Common.formatMoney(new BigDecimal(vat).toString()) + " đ");
            tvTongCong.setText(Common.formatMoney(new BigDecimal(tongCong).toString()) + " đ");

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi chọn vật tư dự toán", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuHinhAnh(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_hinhanh);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            RecyclerView rvHinhAnh = (RecyclerView) dialog.findViewById(R.id.essp_dialog_hinhanh_rv_hinhanh);
            TextView tvKH = (TextView) dialog.findViewById(R.id.essp_dialog_hinhanh_tv_tenkh);
            ImageButton ibTakePhoto = (ImageButton) dialog.findViewById(R.id.essp_dialog_hinhanh_ib_chupanh);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            rvHinhAnh.setHasFixedSize(true);
            rvHinhAnh.setLayoutManager(layoutManager);

            tvKH.setText(new StringBuilder("Tên KH: ").append(connection.getTenKH(HOSO_ID)).toString());

            arrHinhAnh = new ArrayList<>();
            Cursor c = connection.getHinhAnhByMaKH(HOSO_ID);
            if (c.moveToFirst()) {
                do {
                    EsspEntityHinhAnh entity = new EsspEntityHinhAnh();
                    entity.setHOSO_ID(c.getInt(c.getColumnIndex("HOSO_ID")));
                    entity.setTEN_ANH(c.getString(c.getColumnIndex("TEN_ANH")));
                    entity.setGHI_CHU(c.getString(c.getColumnIndex("GHI_CHU")));
                    entity.setTINH_TRANG(c.getString(c.getColumnIndex("TINH_TRANG")));
                    arrHinhAnh.add(entity);
                } while (c.moveToNext());
            }
            adapterHinhAnh = new EsspHinhAnhAdapter(arrHinhAnh, HOSO_ID);
            rvHinhAnh.setAdapter(adapterHinhAnh);

            ibTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                        TEN_ANH = new StringBuilder(EsspCommon.getMaDviqly()).append("_").append(HOSO_ID).append("_").append(sdf.format(new Date())).toString();
                        String fileName = new StringBuilder(Environment.getExternalStorageDirectory().toString())
                                .append(EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH).append(TEN_ANH).append(".jpg").toString();
                        File file = new File(fileName);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi hình ảnh:", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hình ảnh:", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuBanVe(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_banve);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ImageView ivBanVe = (ImageView) dialog.findViewById(R.id.essp_dialog_banve_iv_banve);
            ListView lvBanVe = (ListView) dialog.findViewById(R.id.essp_dialog_banve_lv_banve);
            Button btXoaBanVe = (Button) dialog.findViewById(R.id.essp_dialog_banve_bt_xoa_banve);
            Button btDraw = (Button) dialog.findViewById(R.id.essp_dialog_banve_bt_draw);
            LinearLayout llBanVe = (LinearLayout) dialog.findViewById(R.id.essp_dialog_banve_ll_banve);

            if (connection.checkDuToanExist(HOSO_ID)) {
                Cursor c = connection.getBanVe(HOSO_ID);
                if (c.moveToFirst()) {
                    btXoaBanVe.setVisibility(View.VISIBLE);
                    lvBanVe.setVisibility(View.GONE);
                    btDraw.setVisibility(View.GONE);
                    String TEN_ANH = c.getString(c.getColumnIndex("TEN_ANH"));
                    String PATH = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_BV_PATH)
                            .append(TEN_ANH).append(".jpg").toString();
                    final File fImage = new File(PATH);
                    if (fImage.exists()) {
                        Bitmap bm = Common.scaleDown(BitmapFactory.decodeFile(PATH), 800, false);
                        ivBanVe.setImageBitmap(bm);
                    }

                    btXoaBanVe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (connection.deleteBanVe(String.valueOf(HOSO_ID)) != -1) {
                                fImage.delete();
                                Toast.makeText(EsspMainFragment.this.getActivity(), "Đã xóa ảnh", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                popupMenuBanVe(HOSO_ID);
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Không xóa được bản vẽ", Color.WHITE, "OK", Color.WHITE);
                            }
                        }
                    });
                } else {
                    btXoaBanVe.setVisibility(View.GONE);
                    lvBanVe.setVisibility(View.VISIBLE);
                    btDraw.setVisibility(View.VISIBLE);

                    EsspBanVeAdapter adapterBanVe = new EsspBanVeAdapter(EsspMainFragment.this.getActivity(), R.layout.essp_row_banve,
                            EsspConstantVariables.ARR_TEN_BAN_VE);
                    lvBanVe.setAdapter(adapterBanVe);

                    lvBanVe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            popupChonCap(dialog, HOSO_ID, position);
                        }
                    });

                    btDraw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(EsspMainFragment.this.getActivity(), EsspSoDoCDActivity.class);
                            intent.putExtra("HOSO_ID", HOSO_ID);
                            startActivity(intent);
                        }
                    });
                }
                dialog.show();
            } else {
                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                        "Thông báo", Color.WHITE, "Bạn chưa dự toán", Color.WHITE, "OK", Color.WHITE);
            }

        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi bản vẽ:", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupChonCap(final Dialog d, final int HOSO_ID, final int position) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_select_cap);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final Spinner spCapTruoc = (Spinner) dialog.findViewById(R.id.essp_dialog_select_cap_et_cap_truoc);
            final Spinner spCapSau = (Spinner) dialog.findViewById(R.id.essp_dialog_select_cap_et_cap_sau);
            Button btVe = (Button) dialog.findViewById(R.id.essp_dialog_select_cap_bt_ve);

            ArrayList<String> arrCapTruoc = new ArrayList<>();
            final ArrayList<Integer> arrIDCapTruoc = new ArrayList<>();
            arrCapTruoc.add("Có sẵn");
            Cursor cCapTruoc = connection.getDayCap(HOSO_ID, "2");
            if (cCapTruoc.moveToFirst()) {
                do {
                    arrCapTruoc.add(cCapTruoc.getString(cCapTruoc.getColumnIndex("TEN_VTU")));
                    arrIDCapTruoc.add(cCapTruoc.getInt(cCapTruoc.getColumnIndex("ID")));
                } while (cCapTruoc.moveToNext());
            }
            ArrayAdapter<String> adapterCapTruoc = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(),
                    android.R.layout.simple_list_item_1, arrCapTruoc);
            spCapTruoc.setAdapter(adapterCapTruoc);

            ArrayList<String> arrCapSau = new ArrayList<>();
            final ArrayList<Integer> arrIDCapSau = new ArrayList<>();
            arrCapSau.add("Có sẵn");
            Cursor cCapSau = connection.getDayCap(HOSO_ID, "1");
            if (cCapSau.moveToFirst()) {
                do {
                    arrCapSau.add(cCapSau.getString(cCapSau.getColumnIndex("TEN_VTU")));
                    arrIDCapSau.add(cCapSau.getInt(cCapSau.getColumnIndex("ID")));
                } while (cCapSau.moveToNext());
            }
            ArrayAdapter<String> adapterCapSau = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(),
                    android.R.layout.simple_list_item_1, arrCapSau);
            spCapSau.setAdapter(adapterCapSau);

            btVe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (connection.insertDataBanVe(HOSO_ID, new StringBuilder(EsspCommon.getMaDviqly()).append("_").append(HOSO_ID).toString(), "", "0") != -1) {
                            Bitmap bmBanVe = Common.scaleDown(BitmapFactory.decodeResource(getResources(),
                                    EsspConstantVariables.ARR_BAN_VE[position]), 1700, false);
                            String TEN_ANH = new StringBuilder(EsspCommon.getMaDviqly()).append("_").append(HOSO_ID).toString();
                            String PATH = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_BV_PATH).toString();
                            Common.SaveImage(PATH, new StringBuilder(TEN_ANH).append(".jpg").toString(), bmBanVe);

                            Intent it = new Intent(EsspMainFragment.this.getActivity(), EsspDrawImageActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putInt("HOSO_ID", HOSO_ID);
                            mBundle.putString("TEN_ANH", TEN_ANH);
                            EsspCommon.IMAGE_NAME = TEN_ANH;
                            mBundle.putString("TYPE", "BAN_VE");
                            if (spCapTruoc.getSelectedItemPosition() > 0) {
                                EsspCommon.CAP_TRUOC = new StringBuilder("(").append(spCapTruoc.getSelectedItem().toString()).append(") = ")
                                        .append(connection.getSoLuong(arrIDCapTruoc.get(spCapTruoc.getSelectedItemPosition() - 1)))
                                        .append(" m").toString();
                            } else {
                                EsspCommon.CAP_TRUOC = "(Có sẵn)";
                            }
                            if (spCapSau.getSelectedItemPosition() > 0) {
                                EsspCommon.CAP_SAU = new StringBuilder("(").append(spCapSau.getSelectedItem().toString()).append(") = ")
                                        .append(connection.getSoLuong(arrIDCapSau.get(spCapSau.getSelectedItemPosition() - 1)))
                                        .append(" m").toString();
                            } else {
                                EsspCommon.CAP_SAU = "(Có sẵn)";
                            }
                            EsspCommon.LOAI_BV = position;
                            Cursor c = connection.getTenAndDchi(HOSO_ID);
                            if (c.moveToFirst()) {
                                EsspCommon.TEN_KH = c.getString(c.getColumnIndex("TEN_KHANG"));
                                EsspCommon.DIA_CHI = new StringBuilder(c.getString(c.getColumnIndex("SO_NHA_DDO"))).append(" ")
                                        .append(c.getString(c.getColumnIndex("DUONG_PHO_DDO"))).toString();
                                EsspCommon.TRAM = new StringBuilder("TBA ").append(connection.getTenTram(c.getString(c.getColumnIndex("MA_TRAM")))).toString();
                            }
                            EsspCommon.TYPE = "BAN_VE";
                            it.putExtra("INFO", mBundle);
                            startActivity(it);
                        } else {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Không tạo được bản vẽ", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                        d.dismiss();
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi bản vẽ", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi chọn cáp", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuNhatKy(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_note);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final EditText etNoiDung = (EditText) dialog.findViewById(R.id.essp_dialog_note_et_ghichu);
            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_note_bt_luu);
            Button btXoa = (Button) dialog.findViewById(R.id.essp_dialog_note_bt_xoa);

            Cursor c = connection.getAllDataNhatKyByMaHS(HOSO_ID);
            if (c.moveToFirst()) {
                etNoiDung.setText(c.getString(c.getColumnIndex("TINH_TRANG_SD_DIEN")));
            }

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noiDung = etNoiDung.getText().toString().trim();
                    if (noiDung.equals("")) {
                        Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa nhập nội dung", Toast.LENGTH_LONG).show();
                    } else {
                        if (!connection.checkNhatKyExist(HOSO_ID)) {
                            if (connection.insertDataNhatKy(HOSO_ID, noiDung, "", 0) != -1) {
                                Toast.makeText(EsspMainFragment.this.getActivity(), "Cập nhật nhật ký thành công!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Không thêm được nhật ký", Color.WHITE, "OK", Color.WHITE);
                            }
                        } else {
                            if (connection.updateDataNhatKy(HOSO_ID, noiDung, "") != -1) {
                                Toast.makeText(EsspMainFragment.this.getActivity(), "Cập nhật nhật ký thành công!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Thông báo", Color.WHITE, "Không thêm được nhật ký", Color.WHITE, "OK", Color.WHITE);
                            }
                        }
                    }
                }
            });

            btXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (connection.deleteAllDataNhatKy(HOSO_ID) != -1) {
                        Toast.makeText(EsspMainFragment.this.getActivity(), "Xóa nhật ký thành công!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Thông báo", Color.WHITE, "Không xóa được nhật ký", Color.WHITE, "OK", Color.WHITE);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hình ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuThietBi(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_thietbi);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            etMucDich = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_thietbi_et_mucdich);
            etLoaiTB = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_thietbi_et_loaitb);
            etCongSuat = (EditText) dialog.findViewById(R.id.essp_dialog_thietbi_et_congsuat);
            etSoLuong = (EditText) dialog.findViewById(R.id.essp_dialog_thietbi_et_soluong);
            etDienAp = (AutoCompleteTextView) dialog.findViewById(R.id.essp_dialog_thietbi_et_dienap);
            ListView lvThietBi = (ListView) dialog.findViewById(R.id.essp_dialog_thietbi_lv_thietbi);
            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_thietbi_bt_luu);

            etMucDich.requestFocus();
            Cursor c = connection.getAllDataThietBiByMaHS(HOSO_ID);
            ArrayList<LinkedHashMap<String, String>> arrThietBi = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("ID", c.getString(c.getColumnIndex("ID")));
                    map.put("MUC_DICH", c.getString(c.getColumnIndex("MUC_DICH")));
                    map.put("LOAI_TBI", c.getString(c.getColumnIndex("LOAI_TBI")));
                    map.put("CONG_SUAT", c.getString(c.getColumnIndex("CONG_SUAT")));
                    map.put("SO_LUONG", c.getString(c.getColumnIndex("SO_LUONG")));
                    map.put("DIENAP_SUDUNG", c.getString(c.getColumnIndex("DIENAP_SUDUNG")));
                    arrThietBi.add(map);
                } while (c.moveToNext());
            }
            adapterThietBi = new EsspThietBiAdapter(EsspMainFragment.this.getActivity(), R.layout.essp_row_thietbi, arrThietBi);
            lvThietBi.setAdapter(adapterThietBi);

            setDataSelectThietBi(etLoaiTB);
            setDataSelectMucDich(etMucDich);
            setDataSelectDienAp(etDienAp);

            btLuu.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             try {
                                                 String muc_dich = "";
                                                 String loai_tb = "";
                                                 String cong_suat = "";
                                                 String dien_ap = "";
                                                 int so_luong = 0;

                                                 muc_dich = etMucDich.getText().toString().trim();
                                                 loai_tb = etLoaiTB.getText().toString().trim();
                                                 cong_suat = etCongSuat.getText().toString().trim();
                                                 dien_ap = etDienAp.getText().toString().trim();

                                                 if (loai_tb.equals("")) {
                                                     Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa nhập loại thiết bị", Toast.LENGTH_LONG).show();
                                                 } else if (cong_suat.equals("")) {
                                                     Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa nhập công suất", Toast.LENGTH_LONG).show();
                                                 } else if (etSoLuong.getText().toString().trim().equals("")) {
                                                     Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa nhập số lượng", Toast.LENGTH_LONG).show();
                                                 } else if (dien_ap.trim().equals("")) {
                                                     Toast.makeText(EsspMainFragment.this.getActivity(), "Bạn chưa nhập điện áp", Toast.LENGTH_LONG).show();
                                                 } else {
                                                     so_luong = Integer.parseInt(etSoLuong.getText().toString().trim());
                                                     if (ID_TBI == 0) {
                                                         if (connection.insertDataThietBi(HOSO_ID,
                                                                 muc_dich, loai_tb, cong_suat, so_luong, dien_ap, 0) != -1) {
                                                             Toast.makeText(EsspMainFragment.this.getActivity(), "Thêm dữ liệu thành công", Toast.LENGTH_LONG).show();
                                                             LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                                             map.put("ID", String.valueOf(connection.getMaxID()));
                                                             map.put("MUC_DICH", muc_dich);
                                                             map.put("LOAI_TBI", loai_tb);
                                                             map.put("CONG_SUAT", cong_suat);
                                                             map.put("SO_LUONG", String.valueOf(so_luong));
                                                             map.put("DIENAP_SUDUNG", dien_ap);
                                                             adapterThietBi.add(map);
                                                             adapterThietBi.notifyDataSetChanged();

                                                             if (!connection.checkExistLogSuggest(EsspConstantVariables.SUGGEST_TYPE_TBI, loai_tb.toLowerCase().trim())) {
                                                                 connection.insertDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_TBI, loai_tb.toLowerCase().trim());
                                                                 arr_thiet_bi.add(loai_tb.toLowerCase().trim());
                                                                 adapterTBi.notifyDataSetChanged();
                                                             }
                                                             if (!connection.checkExistLogSuggest(EsspConstantVariables.SUGGEST_TYPE_MUC_DICH, muc_dich.toLowerCase().trim())) {
                                                                 connection.insertDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_MUC_DICH, muc_dich.toLowerCase().trim());
                                                                 arr_muc_dich.add(muc_dich.toLowerCase().trim());
                                                                 adapterMucDich.notifyDataSetChanged();
                                                             }
                                                             if (!connection.checkExistLogSuggest(EsspConstantVariables.SUGGEST_TYPE_DIEN_AP, dien_ap.toLowerCase().trim())) {
                                                                 connection.insertDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_DIEN_AP, dien_ap.toLowerCase().trim());
                                                                 arr_dien_ap.add(dien_ap);
                                                                 adapterDienAp.notifyDataSetChanged();
                                                             }
                                                         } else {
                                                             Toast.makeText(EsspMainFragment.this.getActivity(), "Thêm dữ liệu thất bại", Toast.LENGTH_LONG).show();
                                                         }
                                                     } else {
                                                         if (connection.updateDataThietBi(ID_TBI, HOSO_ID,
                                                                 muc_dich, loai_tb, cong_suat, so_luong, dien_ap) != -1) {
                                                             Toast.makeText(EsspMainFragment.this.getActivity(), "Sửa dữ liệu thành công", Toast.LENGTH_LONG).show();
                                                             adapterThietBi.getItem(POS_SELECT).put("MUC_DICH", muc_dich);
                                                             adapterThietBi.getItem(POS_SELECT).put("LOAI_TBI", loai_tb);
                                                             adapterThietBi.getItem(POS_SELECT).put("CONG_SUAT", cong_suat);
                                                             adapterThietBi.getItem(POS_SELECT).put("SO_LUONG", String.valueOf(so_luong));
                                                             adapterThietBi.getItem(POS_SELECT).put("DIENAP_SUDUNG", dien_ap);
                                                             adapterThietBi.notifyDataSetChanged();
                                                         } else {
                                                             Toast.makeText(EsspMainFragment.this.getActivity(), "Sửa dữ liệu thất bại", Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                     ID_TBI = 0;
                                                     etMucDich.setText("");
                                                     etCongSuat.setText("");
                                                     etLoaiTB.setText("");
                                                     etSoLuong.setText("");
                                                     etDienAp.setText("");
                                                     etMucDich.requestFocus();
                                                 }
                                             } catch (Exception ex) {
                                                 Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi cập nhật thiết bị", Toast.LENGTH_LONG).show();
                                             }
                                         }
                                     }

            );

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ID_TBI = 0;
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi thiết bị", Color.WHITE, "OK", Color.RED);
        }
    }

    private void popupMenuCongSuat(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_congsuat);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final TextView tvMaHS = (TextView) dialog.findViewById(R.id.tvMaHS);
            Button btLuu = (Button) dialog.findViewById(R.id.btLuu);
            Button btXoa = (Button) dialog.findViewById(R.id.btXoa);
            ListView lvCongSuat = (ListView) dialog.findViewById(R.id.lvCongSuat);
            etKhoangGio = (EditText) dialog.findViewById(R.id.etKhoangGio);
            etCongSuatCS = (EditText) dialog.findViewById(R.id.etCongSuat);

            arrCongSuat = new ArrayList<>();
            tvMaHS.setText("");
            tvMaHS.setVisibility(View.GONE);
            Cursor c = connection.getCSUATByID(HOSO_ID);
            if (c.moveToFirst()) {
                do {
                    LinkedHashMap<String, String> map = new LinkedHashMap<>();
                    map.put("ID", c.getString(c.getColumnIndex("ID")));
                    map.put("HOSO_ID", c.getString(c.getColumnIndex("HOSO_ID")));
                    map.put("KHOANG_TGIAN", c.getString(c.getColumnIndex("KHOANG_TGIAN")));
                    map.put("CONG_SUAT", c.getString(c.getColumnIndex("CONG_SUAT")));
                    arrCongSuat.add(map);
                } while (c.moveToNext());
            }
            adapterCongSuat = new AdapterCongSuat(EsspMainFragment.this.getActivity(), R.layout.essp_row_congsuat, arrCongSuat);
            lvCongSuat.setAdapter(adapterCongSuat);

            lvCongSuat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        LinkedHashMap<String, String> map = adapterCongSuat.getItem(position);
                        POS_CONGSUAT = position;
                        etKhoangGio.setText(map.get("KHOANG_TGIAN"));
                        etCongSuatCS.setText(map.get("CONG_SUAT"));
                    } catch (Exception ex) {
                        ex.toString();
                        POS_CONGSUAT = -1;
                    }
                }
            });

            etKhoangGio.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                    if (countKey(charSequence.toString(), '-') > 1) {
                        etKhoangGio.setText(charSequence.toString().substring(0, charSequence.toString().length() - 1));
                        etKhoangGio.setSelection(charSequence.toString().length() - 1);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etKhoangGio.setError(null);
                        etCongSuatCS.setError(null);
                        String KHOANG_TGIAN = etKhoangGio.getText().toString().trim();
                        String CONG_SUAT = etCongSuatCS.getText().toString().trim();
                        if (KHOANG_TGIAN.length() == 0) {
                            etKhoangGio.setError("Bạn chưa nhập khoảng thời gian");
                            etKhoangGio.requestFocus();
                        } else if (CONG_SUAT.length() == 0) {
                            etCongSuatCS.setError("Bạn chưa nhập công suất");
                            etCongSuatCS.requestFocus();
                        } else {
                            while (KHOANG_TGIAN.substring(0, 1).equals("-")) {
                                KHOANG_TGIAN = KHOANG_TGIAN.substring(1);
                            }
                            while (KHOANG_TGIAN.substring(KHOANG_TGIAN.length() - 1, KHOANG_TGIAN.length()).equals("-")) {
                                KHOANG_TGIAN = KHOANG_TGIAN.substring(0, KHOANG_TGIAN.length() - 1);
                            }
                            if (!KHOANG_TGIAN.contains("-")) {
                                etKhoangGio.setError("Bạn nhập sai định dạng \"từ giờ-đến giờ\"");
                                etKhoangGio.requestFocus();
                            } else if (Integer.parseInt(KHOANG_TGIAN.split("-")[0]) > 24 || Integer.parseInt(KHOANG_TGIAN.split("-")[1]) > 24) {
                                etKhoangGio.setError("Giờ phải nhở hơn 24");
                                etKhoangGio.requestFocus();
                            } else {
                                if (Integer.parseInt(KHOANG_TGIAN.split("-")[0]) > Integer.parseInt(KHOANG_TGIAN.split("-")[1])) {
                                    KHOANG_TGIAN = new StringBuilder(KHOANG_TGIAN.split("-")[1]).append("-").append(KHOANG_TGIAN.split("-")[0]).toString();
                                }
                                if (POS_CONGSUAT == -1) {
                                    if (connection.insertDataCSUAT(HOSO_ID, KHOANG_TGIAN, CONG_SUAT, 0) != -1) {
                                        LinkedHashMap<String, String> mapAdd = new LinkedHashMap<String, String>();
                                        mapAdd.put("ID", String.valueOf(connection.getIDCSuat()));
                                        mapAdd.put("HOSO_ID", String.valueOf(HOSO_ID));
                                        mapAdd.put("KHOANG_TGIAN", KHOANG_TGIAN);
                                        mapAdd.put("CONG_SUAT", CONG_SUAT);
                                        arrCongSuat.add(mapAdd);
                                        adapterCongSuat.notifyDataSetChanged();
                                        etKhoangGio.setText("");
                                        etCongSuatCS.setText("");
                                        etKhoangGio.requestFocus();
                                    } else {
                                        Toast.makeText(EsspMainFragment.this.getActivity(), "Không thêm được công suất", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (connection.updateDataCSUAT(Integer.parseInt(adapterCongSuat.getItem(POS_CONGSUAT).get("ID")), KHOANG_TGIAN, CONG_SUAT, 0) != -1) {
                                        adapterCongSuat.getItem(POS_CONGSUAT).put("KHOANG_TGIAN", KHOANG_TGIAN);
                                        adapterCongSuat.getItem(POS_CONGSUAT).put("CONG_SUAT", CONG_SUAT);
                                        adapterCongSuat.notifyDataSetChanged();
                                        etKhoangGio.setText("");
                                        etCongSuatCS.setText("");
                                        etKhoangGio.requestFocus();
                                        POS_CONGSUAT = -1;
                                    } else {
                                        Toast.makeText(EsspMainFragment.this.getActivity(), "Không sửa được công suất", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi lưu công suất", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            btXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (POS_CONGSUAT != -1) {
                        if (connection.deleteCongSuat(adapterCongSuat.getItem(POS_CONGSUAT).get("ID")) != -1) {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Đã xóa công suất", Toast.LENGTH_LONG).show();
                            etKhoangGio.setText("");
                            etCongSuatCS.setText("");
                            arrCongSuat.remove(POS_CONGSUAT);
                            adapterCongSuat.notifyDataSetChanged();
                            POS_CONGSUAT = -1;
                        } else {
                            Toast.makeText(EsspMainFragment.this.getActivity(), "Không xóa được công suất", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi cập nhật công suất", Color.WHITE, "OK", Color.RED);
        }
    }

    public static void deleteTB(Context ctx, int id, int position) {
        try {
            if (connection.deleteDataTBIByID(id) != -1) {
                adapterThietBi.remove(adapterThietBi.getItem(position));
                adapterThietBi.notifyDataSetChanged();
            } else {
                Toast.makeText(ctx, "Lỗi xóa thiết bị", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED, "Lỗi xóa thiết bị", Color.WHITE, "OK", Color.RED);
        }
    }

    public static void setDataOnEditText(Context ctx, LinkedHashMap<String, String> map, int pos) {
        try {
            POS_SELECT = pos;
            ID_TBI = Integer.parseInt(map.get("ID"));
            String muc_dich = map.get("MUC_DICH");
            String loai_tb = map.get("LOAI_TBI");
            String cong_suat = map.get("CONG_SUAT");
            String so_luong = map.get("SO_LUONG");
            String dien_ap = map.get("DIENAP_SUDUNG");

            etMucDich.setText(muc_dich);

            etLoaiTB.setText(loai_tb);
            etCongSuat.setText(cong_suat);
            etSoLuong.setText(so_luong);
            etDienAp.setText(dien_ap);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED, "Lỗi lấy thông tin thiết bị", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogUpdateSoLuong(final int ID, final int HOSO_ID, final String MA_VTU, final String SO_HUU, final int POS, final boolean isChecked, final TextView tvThanhTien) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_soluong);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);

            final EditText etSoLuong = (EditText) dialog.findViewById(R.id.essp_dialog_soluong_et_soluong);
            Button btLuuSoLuong = (Button) dialog.findViewById(R.id.essp_dialog_soluong_bt_luu_soluong);

            if (Integer.parseInt(SO_HUU) < 3) {
                etSoLuong.setText(connection.getSoLuong(ID));
            } else {
                etSoLuong.setText(connection.getSoLuongNHT(ID));
            }
            etSoLuong.selectAll();

            btLuuSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!etSoLuong.getText().toString().trim().isEmpty()) {
                            float soluong = Float.parseFloat(etSoLuong.getText().toString().trim());
                            if (Integer.parseInt(SO_HUU) < 3) {
                                String dongia = "0";
                                Cursor c = connection.getDonGiaVatDu(MA_VTU);
                                if (c.moveToFirst()) {
                                    if (isChecked) {
                                        dongia = c.getString(c.getColumnIndex("DON_GIA_KH"));
                                    } else {
                                        dongia = c.getString(c.getColumnIndex("DON_GIA"));
                                    }
                                    double thanhtien = Double.parseDouble(dongia) * soluong;
                                    if (connection.updateSoluong(ID, String.valueOf(soluong), String.valueOf(thanhtien)) != -1) {
                                        dialog.dismiss();
                                        adapterDuToan.updateSoLuongItem(POS, soluong);
//                                        try {
//                                            tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                                    .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, SO_HUU)
//                                                            + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                                        } catch (Exception ex) {
//                                            tvThanhTien.setText("Thành tiền: 0");
//                                        }
                                        tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                                    }
                                }
                            } else {
                                if (connection.updateSoLuongDTNHT(ID, soluong) != -1) {
                                    dialog.dismiss();
                                    adapterDuToan.updateSoLuongItem(POS, soluong);
//                                    try {
//                                        tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                                .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, SO_HUU)
//                                                        + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                                    } catch (Exception ex) {
//                                        tvThanhTien.setText("Thành tiền: 0");
//                                    }
                                    tinhThanhTien(tvThanhTien, HOSO_ID, "1", false);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi cập nhật số lượng", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi thay đổi số lượng", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogUpdateSoLuongMoi(final int ID, final int HOSO_ID, final String MA_VTU, final String SO_HUU, final int POS, final TextView tvThanhTien, final int LOAIHINH_TTOAN) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_soluong);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);

            final EditText etSoLuong = (EditText) dialog.findViewById(R.id.essp_dialog_soluong_et_soluong);
            Button btLuuSoLuong = (Button) dialog.findViewById(R.id.essp_dialog_soluong_bt_luu_soluong);

            if (Integer.parseInt(SO_HUU) < 3) {
                etSoLuong.setText(connection.getSoLuong(ID));
            } else {
                etSoLuong.setText(connection.getSoLuongNHT(ID));
            }
            etSoLuong.selectAll();

            btLuuSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!etSoLuong.getText().toString().trim().isEmpty()) {
                            float soluong = Float.parseFloat(etSoLuong.getText().toString().trim());
                            if (Integer.parseInt(SO_HUU) < 3) {
                                String dongia = "0";
                                Cursor c = connection.getDonGiaVatDuMoi(MA_VTU);
                                if (c.moveToFirst()) {
                                    switch (LOAIHINH_TTOAN) {
                                        case EsspConstantVariables.DT_TRONGOI:
                                            if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 1) {
                                                dongia = c.getString(c.getColumnIndex("DG_TRONGOI"));
                                            } else {
                                                dongia = "0";
                                            }
                                            break;
                                        case EsspConstantVariables.DT_TUTUC_CAP:
                                            if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 1) {//dây cáp
                                                dongia = c.getString(c.getColumnIndex("DON_GIA_KH"));
                                            }
                                            break;
                                        case EsspConstantVariables.DT_TUTUC_VTU:
                                            dongia = String.valueOf(c.getDouble(c.getColumnIndex("DG_NCONG")) * 10 / 100);
                                            break;
                                        case EsspConstantVariables.DT_TUTUC_HTOAN:
                                            dongia = "0";
                                            break;
                                    }
                                    double thanhtien = Double.parseDouble(dongia) * soluong;
                                    if (connection.updateSoluong(ID, String.valueOf(soluong), String.valueOf(thanhtien)) != -1) {
                                        dialog.dismiss();
                                        adapterDuToanMoi.updateSoLuongItem(POS, soluong);
                                        tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                                    }
                                }
                            } else {
                                if (connection.updateSoLuongDTNHT(ID, soluong) != -1) {
                                    dialog.dismiss();
                                    adapterDuToanMoi.updateSoLuongItem(POS, soluong);
                                    tinhThanhTien(tvThanhTien, HOSO_ID, "1", false);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi cập nhật số lượng", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi thay đổi số lượng", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogCmdCode(final int HOSO_ID) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_soluong);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);

            final EditText etCode = (EditText) dialog.findViewById(R.id.essp_dialog_soluong_et_soluong);
            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_soluong_bt_luu_soluong);

            etCode.setHint("Enter code");
            etCode.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String code = etCode.getText().toString().trim();
                        etCode.setError(null);
                        if (code.isEmpty()) {
                            etCode.setError("Enter code");
                            etCode.requestFocus();
                        } else {
                            if (code.equals(CmdCode.UPDATE_STATE_ALL)) {
                                connection.updateTinhTrang(HOSO_ID, 3);
                                connection.updateTinhTrangPHUONGAN(HOSO_ID, 0);
                                connection.updateTinhTrangDT(HOSO_ID, 0);
                                connection.updateTinhTrangDTNHT(HOSO_ID, 0);
                                connection.updateTinhTrangBV(HOSO_ID, 0);
                                connection.updateTinhTrangHA(HOSO_ID, 0);
                                connection.updateTinhTrangTB(HOSO_ID, 0);
                                connection.updateTinhTrangCS(HOSO_ID, 0);
                                dialog.dismiss();
                            }
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi CMD", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi code cmd", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogUpdateGhiChu(final String TEN_ANH, final int POS) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_note);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final EditText etGhiChu = (EditText) dialog.findViewById(R.id.essp_dialog_note_et_ghichu);
            Button btLuu = (Button) dialog.findViewById(R.id.essp_dialog_note_bt_luu);
            Button btXoa = (Button) dialog.findViewById(R.id.essp_dialog_note_bt_xoa);

            btXoa.setVisibility(View.GONE);
            etGhiChu.setText(connection.getGhiChuByTenAnh(TEN_ANH));

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String GHI_CHU = etGhiChu.getText().toString().trim();
                        if (!GHI_CHU.isEmpty()) {
                            if (connection.updateDataHinhAnh(TEN_ANH, GHI_CHU) != -1) {
                                adapterHinhAnh.updateGhiChu(POS, GHI_CHU);
                            }
                            dialog.dismiss();
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi cập nhật ghi chú", Color.WHITE, "OK", Color.RED);
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi cập nhật ghi chú", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogViewImage(final String TEN_ANH) {
        try {
            final Dialog dialog = new Dialog(EsspMainFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_viewimage);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final ImageViewTouch ivtImage = (ImageViewTouch) dialog.findViewById(R.id.essp_dialog_viewimage_ivt_image);

            String pathImage = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH).append(TEN_ANH).append(".jpg").toString();
            File fImage = new File(pathImage);
            if (fImage.exists()) {
                Bitmap bmImage = BitmapFactory.decodeFile(pathImage);
                ivtImage.setImageBitmapReset(bmImage, 0, true);
            }

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion

    //region Xử lý dữ liệu
    private void initSpinner(int state, String[] strDatas, List<String> arrData, Spinner spData) {
        try {
            ArrayAdapter<String> adapterData = null;
            if (state == 0) { // khởi tạo dữ liệu với mảng string
                adapterData = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, strDatas);
            } else {
                adapterData = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, arrData);
            }
            spData.setAdapter(adapterData);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu spinner", Color.WHITE, "OK", Color.RED);
        }
    }

    private void setDataSelectVatTu(AutoCompleteTextView spVatTu) {
        arr_vatTu = new ArrayList<>();
        Cursor c = null;
        c = connection.getAllDataVTu(EsspCommon.getMaDviqly());
        if (c.moveToFirst()) {
            do {
                if (!arrMaVatTu.contains(c.getString(c.getColumnIndex("MA_VTU")))) {
                    arr_vatTu.add(new StringBuilder(c.getString(c.getColumnIndex("MA_VTU"))).append(" - ").append(c.getString(c.getColumnIndex("TEN_VTU"))).toString());
                }
            } while (c.moveToNext());
        }
        CustomAdapter adapterVTu = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_vatTu);
        spVatTu.setAdapter(adapterVTu);
    }

    private void setDataSelectTram(AutoCompleteTextView spTram) {
        arr_tram = new ArrayList<>();
        Cursor c = null;
        c = connection.getAllDataTram(EsspCommon.getMaDviqly());
        if (c.moveToFirst()) {
            do {
                arr_tram.add(new StringBuilder(c.getString(c.getColumnIndex("MA_TRAM"))).append(" - ").append(c.getString(c.getColumnIndex("TEN_TRAM"))).toString());
            } while (c.moveToNext());
        }
        CustomAdapter adapterTram = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_tram);
        spTram.setAdapter(adapterTram);
    }

    private void setDataSelectQuyen(AutoCompleteTextView spQuyen) {
        arr_quyen = new ArrayList<>();
        Cursor c = null;
        c = connection.getAllDataQuyen(EsspCommon.getMaDviqly());
        if (c.moveToFirst()) {
            do {
                arr_quyen.add(new StringBuilder(c.getString(c.getColumnIndex("MA_SO"))).append(" - ").append(c.getString(c.getColumnIndex("TEN_SO"))).toString());
            } while (c.moveToNext());
        }
        CustomAdapter adapterTram = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_quyen);
        spQuyen.setAdapter(adapterTram);
    }

    private void setDataSelectThietBi(AutoCompleteTextView spTBi) {
        arr_thiet_bi = new ArrayList<>();
        Cursor c = null;
        c = connection.getDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_TBI);
        if (c.moveToFirst()) {
            do {
                arr_thiet_bi.add(c.getString(c.getColumnIndex("LOG_CONTENT")));
            } while (c.moveToNext());
        }
        adapterTBi = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_thiet_bi);
        spTBi.setAdapter(adapterTBi);
    }

    private void setDataSelectMucDich(AutoCompleteTextView spMucDich) {
        arr_muc_dich = new ArrayList<>();
        Cursor c = null;
        c = connection.getDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_MUC_DICH);
        if (c.moveToFirst()) {
            do {
                arr_muc_dich.add(c.getString(c.getColumnIndex("LOG_CONTENT")));
            } while (c.moveToNext());
        }
        adapterMucDich = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_muc_dich);
        spMucDich.setAdapter(adapterMucDich);
    }

    private void setDataSelectDienAp(AutoCompleteTextView spDienAp) {
        arr_dien_ap = new ArrayList<>();
        Cursor c = null;
        c = connection.getDataLogSuggest(EsspConstantVariables.SUGGEST_TYPE_DIEN_AP);
        if (c.moveToFirst()) {
            do {
                arr_dien_ap.add(c.getString(c.getColumnIndex("LOG_CONTENT")));
            } while (c.moveToNext());
        }
        adapterDienAp = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_dien_ap);
        spDienAp.setAdapter(adapterDienAp);
    }

    private void setDataSelectNganHang(AutoCompleteTextView spNganHang) {
        arr_tram = new ArrayList<>();
        Cursor c = null;
        c = connection.getAllDataNganHang();
        if (c.moveToFirst()) {
            do {
                arr_tram.add(new StringBuilder(c.getString(c.getColumnIndex("MA_NHANG"))).append(" - ").append(c.getString(c.getColumnIndex("TEN_NHANG"))).toString());
            } while (c.moveToNext());
        }
//        ArrayAdapter<String> adapterNganHang = new ArrayAdapter<String>(EsspMainFragment.this.getActivity(), android.R.layout.simple_list_item_1, arr_tram);
        CustomAdapter adapterNganHang = new CustomAdapter(EsspMainFragment.this.getActivity(), arr_tram);
        spNganHang.setAdapter(adapterNganHang);
    }

    private void tinhThanhTien(TextView tvThanhTien, int HOSO_ID, String SO_HUU, boolean isKhaiGia) {
        try {
            if (tvThanhTien != null) {
                if (Integer.parseInt(SO_HUU) == 2) {
                    tvThanhTien.setText("Thành tiền: 0");
                } else {
                    if (!isKhaiGia) {
                        //TODO VinhNB sửa lý do: theo 4 loại LOAIHINH_THANHTOAN thì tính tiền khác nhau
                        //get LOAIHINH_THANHTOAN
                        int LOAIHINH_THANHTOAN = connection.getLoaiHinhTToan(HOSO_ID);
                        Cursor cDT = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                        if (cDT.moveToFirst()) {
                            do {
                                String MA_VTU = cDT.getString(cDT.getColumnIndex("MA_VTU"));
                                double DON_GIA = 0d;
                                int SO_LUONG = cDT.getInt(cDT.getColumnIndex("SO_LUONG"));
                                switch (LOAIHINH_THANHTOAN) {
                                    case EsspConstantVariables.DT_TRONGOI:
                                        if (connection.getChungLoai(MA_VTU) == 1) {
                                            DON_GIA = connection.getDGTronGoi(MA_VTU);
                                        } else {
                                            DON_GIA = 0d;
                                        }
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_CAP:
                                        if (connection.getChungLoai(MA_VTU) == 1) {//dây cáp
                                            DON_GIA = connection.getDON_GIA_KH(MA_VTU);
                                        } else {
                                            DON_GIA = 0d;
                                        }
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_VTU:
                                        DON_GIA = connection.getDGNCong(MA_VTU) + connection.getDG_MTCONG(MA_VTU);
                                        DON_GIA *= 1.1;
                                        break;
                                    case EsspConstantVariables.DT_TUTUC_HTOAN:
                                        DON_GIA = 0d;
                                        break;
                                    default:
                                        break;
                                }
                                double THANH_TIEN = DON_GIA * SO_LUONG;
                                if (connection.updateDonGiaThanhTien(HOSO_ID, "1", MA_VTU,
                                        String.valueOf(DON_GIA), String.valueOf(THANH_TIEN)) <= 0) {
                                    Log.i("update", "success");
                                }
                            } while (cDT.moveToNext());
                        }
                        double tt1 = connection.sumThanhTien(HOSO_ID, SO_HUU);
                        tvThanhTien.setText(new StringBuilder("Thành tiền: ")
                                .append(Common.formatMoney(new BigDecimal(tt1).toString())).append(" đ").toString());
                    } else {
                        double cpVatLieu = 0d;
                        double cpNhanCong = 0d;
                        double cpMayThiCong = 0d;
                        double cpTrucTiepKhac = 0d;
                        double cpTrucTiep = 0d;
                        double cpChung = 0d;
                        double chiuThueTruoc = 0d;
                        double gtXayLapTruocThue = 0d;
                        double khaoSat = 0d;
                        double tongDoanhThuTruocThue = 0d;
                        double vat = 0d;
                        double tongCong = 0d;

                        int LOAIHINH_TTOAN = connection.getLoaiHinhTToan(HOSO_ID);
//                    switch (LOAIHINH_TTOAN) {
//                        case EsspConstantVariables.DT_TRONGOI:
//                            DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                            DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                            DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                            break;
//                        case EsspConstantVariables.DT_TUTUC_CAP:
//                            if (c.getInt(c.getColumnIndex("CHUNG_LOAI")) == 1) {//dây cáp
//                                DG_VTU_DCHING = 0d;
//                                DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                            } else {
//                                DG_VTU_DCHING = c.getDouble(c.getColumnIndex("DG_VTU")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                                DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                            }
//                            break;
//                        case EsspConstantVariables.DT_TUTUC_VTU:
//                            DG_VTU_DCHING = 0d;
//                            DG_NCONG_DCHING = c.getDouble(c.getColumnIndex("DG_NCONG")) * c.getDouble(c.getColumnIndex("HSDC_K1NC")) * c.getDouble(c.getColumnIndex("HSDC_K2NC"));
//                            DG_MTC_DCHING = c.getDouble(c.getColumnIndex("DG_MTCONG")) * c.getDouble(c.getColumnIndex("HSDC_MTC"));
//                            break;
//                        case EsspConstantVariables.DT_TUTUC_HTOAN:
//                            DG_VTU_DCHING = 0d;
//                            DG_NCONG_DCHING = 0d;
//                            DG_MTC_DCHING = 0d;
//                            break;
//                    }

                        cpVatLieu = connection.sumVatLieu(LOAIHINH_TTOAN, HOSO_ID, SO_HUU) + connection.sumVatTu(HOSO_ID);
                        cpNhanCong = connection.sumNhanCong(LOAIHINH_TTOAN, HOSO_ID, SO_HUU) + connection.sumNhanCong(HOSO_ID);
                        cpMayThiCong = connection.sumMTC(LOAIHINH_TTOAN, HOSO_ID, SO_HUU) + connection.sumMTC(HOSO_ID);
                        //TODO VINHNB sửa
//                    cpTrucTiepKhac = (cpVatLieu + cpNhanCong + cpMayThiCong) * 2 / 100;
//                    cpTrucTiep = cpVatLieu + cpNhanCong + cpMayThiCong + cpTrucTiepKhac;
//                    cpChung = connection.getLoaiCap(HOSO_ID) == 0 ? (cpNhanCong * 60 / 100) : (cpTrucTiep * 5.5 / 100);
//                    chiuThueTruoc = (cpTrucTiep + cpChung) * 6 / 100;
//                    gtXayLapTruocThue = cpTrucTiep + cpChung + chiuThueTruoc;

                        cpTrucTiepKhac = 0;
                        cpTrucTiep = 0;
                        cpChung = 0;
                        chiuThueTruoc = 0;
                        gtXayLapTruocThue = cpVatLieu + cpNhanCong + cpMayThiCong;

                        khaoSat = 0d;
                        tongDoanhThuTruocThue = gtXayLapTruocThue + khaoSat;
                        vat = gtXayLapTruocThue * 10 / 100;
                        tongCong = gtXayLapTruocThue + vat;

                        tvThanhTien.setText(new StringBuilder("Thành tiền: ")
                                .append(Common.formatMoney(new BigDecimal(tongCong).toString())).append(" đ").toString());
                    }
                }
            }
        } catch (Exception ex) {
            tvThanhTien.setText("Thành tiền: " + ex.getMessage());
        }
    }

    private ArrayList<LinkedHashMap<String, String>> sortKhoangGio(Cursor c) {
        ArrayList<LinkedHashMap<String, String>> strKhoangGio = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("TINH_TRANG", c.getString(c.getColumnIndex("TINH_TRANG")));
                map.put("ID", c.getString(c.getColumnIndex("ID")));
                map.put("HOSO_ID", c.getString(c.getColumnIndex("HOSO_ID")));
                map.put("CONG_SUAT", c.getString(c.getColumnIndex("CONG_SUAT")));
                map.put("KHOANG_TGIAN", c.getString(c.getColumnIndex("KHOANG_TGIAN")));
                strKhoangGio.add(map);
            } while (c.moveToNext());
            if (strKhoangGio != null) {
                for (int j = 0; j < strKhoangGio.size() - 1; j++) {
                    for (int k = j; k < strKhoangGio.size(); k++) {
                        LinkedHashMap<String, String> tg = new LinkedHashMap<>();
                        if (Integer.parseInt(strKhoangGio.get(j).get("KHOANG_TGIAN").split("-")[0]) >
                                Integer.parseInt(strKhoangGio.get(k).get("KHOANG_TGIAN").split("-")[0])) {
                            tg = strKhoangGio.get(j);
                            strKhoangGio.add(j, strKhoangGio.get(k));
                            strKhoangGio.remove(j + 1);
                            strKhoangGio.add(k, tg);
                            strKhoangGio.remove(k + 1);
                        }
                    }
                }
            }
        }
        return strKhoangGio;
    }

    private int countKey(String value, char key) {
        if (value.isEmpty()) {
            return 0;
        } else {
            int count = 0;
            char[] cValue = value.toCharArray();
            for (int i = 0; i < cValue.length; i++) {
                if (key == cValue[i]) {
                    count++;
                }
            }
            return count;
        }
    }

    private String setKey(int numberKey) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < numberKey; i++) {
            key.append("-");
        }
        return key.toString();
    }

    //endregion

    //region Tạo chuỗi JSON
    public JSONObject DATAtoJSONHoSo(int HOSO_ID, String MA_DVIQLY, String MA_TRACUU, String CMIS_MA_YCAU_KNAI, String MKHAU_TRACUU,
                                     String CMIS_MA_LOAI_YCAU, int TINH_TRANG, int MA_LOAIHD, int NGUON_TIEPNHAN, String NGUOI_TIEPNHAN,
                                     String NGAY_TIEPNHAN, String NGUOI_SUA, String NGAY_SUA, String NGAY_TAO, String NGAY_HENKHAOSAT,
                                     String GIO_BDAU_TCONG, String GIO_KTHUC_TCONG, String SODO_CAPDIEN, String NGUOI_PHANCONG,
                                     String DONGBO_CMIS, String GHI_CHU_KHI_THICONG, String LOAI_DDO, String MA_TRAM, int KYMUA_CSPK,
                                     int SO_PHA, String TEN_KHANG, String NGUOI_DAI_DIEN, int Gender_NGUOI_DAI_DIEN, String SO_NHA,
                                     String DUONG_PHO, String SO_CMT, String NGAY_CAP, String NOI_CAP, String TEN_NGUOIYCAU,
                                     String DTHOAI_DD, String DTHOAI_CD, String FAX, String EMAIL, String WEBSITE,
                                     String SO_NHA_DDO, String DUONG_PHO_DDO, String TKHOAN_NGANHANG, String MA_NHANG,
                                     String MA_SOTHUE, String CHUC_VU, int GenderId, String MA_DVIDCHINHC2, String MA_DVIDCHINHC3,
                                     int KQ_KHSAT_ID, String MA_TNGAI, String MA_QUYEN, int HINHTHUC_LAPDAT, int LOAI_CAP, int NKY_KSAT) {
        String sHOSO_ID = "HOSO_ID";
        String sMA_DVIQLY = "MA_DVIQLY";
        String sMA_TRACUU = "MA_TRACUU";
        String sCMIS_MA_YCAU_KNAI = "CMIS_MA_YCAU_KNAI";
        String sMKHAU_TRACUU = "MKHAU_TRACUU";
        String sCMIS_MA_LOAI_YCAU = "CMIS_MA_LOAI_YCAU";
        String sTINH_TRANG = "TINH_TRANG";
        String sMA_LOAIHD = "MA_LOAIHD";
        String sNGUON_TIEPNHAN = "NGUON_TIEPNHAN";
        String sNGUOI_TIEPNHAN = "NGUOI_TIEPNHAN";
        String sNGAY_TIEPNHAN = "NGAY_TIEPNHAN";
        String sNGUOI_SUA = "NGUOI_SUA";
        String sNGAY_SUA = "NGAY_SUA";
        String sNGAY_TAO = "NGAY_TAO";
        String sNGAY_HENKHAOSAT = "NGAY_HENKHAOSAT";
        String sGIO_BDAU_TCONG = "GIO_BDAU_TCONG";
        String sGIO_KTHUC_TCONG = "GIO_KTHUC_TCONG";
        String sSODO_CAPDIEN = "SODO_CAPDIEN";
        String sNGUOI_PHANCONG = "NGUOI_PHANCONG";
        String sDONGBO_CMIS = "DONGBO_CMIS";
        String sGHI_CHU_KHI_THICONG = "GHI_CHU_KHI_THICONG";
        String sLOAI_DDO = "LOAI_DDO";
        String sMA_TRAM = "MA_TRAM";
        String sKYMUA_CSPK = "KYMUA_CSPK";
        String sSO_PHA = "SO_PHA";
        String sTEN_KHANG = "TEN_KHANG";
        String sNGUOI_DAI_DIEN = "NGUOI_DAI_DIEN";
        String sGender_NGUOI_DAI_DIEN = "Gender_NGUOI_DAI_DIEN";
        String sSO_NHA = "SO_NHA";
        String sDUONG_PHO = "DUONG_PHO";
        String sSO_CMT = "SO_CMT";
        String sNGAY_CAP = "NGAY_CAP";
        String sNOI_CAP = "NOI_CAP";
        String sTEN_NGUOIYCAU = "TEN_NGUOIYCAU";
        String sDTHOAI_DD = "DTHOAI_DD";
        String sDTHOAI_CD = "DTHOAI_CD";
        String sFAX = "FAX";
        String sEMAIL = "EMAIL";
        String sWEBSITE = "WEBSITE";
        String sSO_NHA_DDO = "SO_NHA_DDO";
        String sDUONG_PHO_DDO = "DUONG_PHO_DDO";
        String sTKHOAN_NGANHANG = "TKHOAN_NGANHANG";
        String sMA_NHANG = "MA_NHANG";
        String sMA_SOTHUE = "MA_SOTHUE";
        String sCHUC_VU = "CHUC_VU";
        String sGenderId = "GenderId";
        String sMA_DVIDCHINHC2 = "MA_DVIDCHINHC2";
        String sMA_DVIDCHINHC3 = "MA_DVIDCHINHC3";
        String sKQ_KHSAT_ID = "KQ_KHSAT_ID";
        String sMA_TNGAI = "MA_TNGAI";
        String sMA_QUYEN = "MA_QUYEN";
        String sHINHTHUC_LAPDAT = "HINHTHUC_LAPDAT";
        String sLOAI_CAP = "LOAI_CAP";
        String sNKY_KSAT = "NKY_KSAT";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sMA_DVIQLY, MA_DVIQLY);
            jsonObject.accumulate(sMA_TRACUU, MA_TRACUU);
            jsonObject.accumulate(sCMIS_MA_YCAU_KNAI, CMIS_MA_YCAU_KNAI);
            jsonObject.accumulate(sMKHAU_TRACUU, MKHAU_TRACUU);
            jsonObject.accumulate(sCMIS_MA_LOAI_YCAU, CMIS_MA_LOAI_YCAU);
            jsonObject.accumulate(sTINH_TRANG, TINH_TRANG);
            jsonObject.accumulate(sMA_LOAIHD, MA_LOAIHD);
            jsonObject.accumulate(sNGUON_TIEPNHAN, NGUON_TIEPNHAN);
            jsonObject.accumulate(sNGUOI_TIEPNHAN, NGUOI_TIEPNHAN);
            jsonObject.accumulate(sNGAY_TIEPNHAN, NGAY_TIEPNHAN);
            jsonObject.accumulate(sNGUOI_SUA, NGUOI_SUA);
            jsonObject.accumulate(sNGAY_SUA, NGAY_SUA);
            jsonObject.accumulate(sNGAY_TAO, NGAY_TAO);
            jsonObject.accumulate(sNGAY_HENKHAOSAT, NGAY_HENKHAOSAT);
            jsonObject.accumulate(sGIO_BDAU_TCONG, GIO_BDAU_TCONG);
            jsonObject.accumulate(sGIO_KTHUC_TCONG, GIO_KTHUC_TCONG);
            jsonObject.accumulate(sSODO_CAPDIEN, SODO_CAPDIEN);
            jsonObject.accumulate(sNGUOI_PHANCONG, NGUOI_PHANCONG);
            jsonObject.accumulate(sDONGBO_CMIS, DONGBO_CMIS);
            jsonObject.accumulate(sGHI_CHU_KHI_THICONG, GHI_CHU_KHI_THICONG);
            jsonObject.accumulate(sLOAI_DDO, LOAI_DDO);
            jsonObject.accumulate(sMA_TRAM, MA_TRAM);
            jsonObject.accumulate(sKYMUA_CSPK, KYMUA_CSPK);
            jsonObject.accumulate(sSO_PHA, SO_PHA);
            jsonObject.accumulate(sTEN_KHANG, TEN_KHANG);
            jsonObject.accumulate(sNGUOI_DAI_DIEN, NGUOI_DAI_DIEN);
            jsonObject.accumulate(sGender_NGUOI_DAI_DIEN, Gender_NGUOI_DAI_DIEN);
            jsonObject.accumulate(sSO_NHA, SO_NHA);
            jsonObject.accumulate(sDUONG_PHO, DUONG_PHO);
            jsonObject.accumulate(sSO_CMT, SO_CMT);
            jsonObject.accumulate(sNGAY_CAP, NGAY_CAP);
            jsonObject.accumulate(sNOI_CAP, NOI_CAP);
            jsonObject.accumulate(sTEN_NGUOIYCAU, TEN_NGUOIYCAU);
            jsonObject.accumulate(sDTHOAI_DD, DTHOAI_DD);
            jsonObject.accumulate(sDTHOAI_CD, DTHOAI_CD);
            jsonObject.accumulate(sFAX, FAX);
            jsonObject.accumulate(sEMAIL, EMAIL);
            jsonObject.accumulate(sWEBSITE, WEBSITE);
            jsonObject.accumulate(sSO_NHA_DDO, SO_NHA_DDO);
            jsonObject.accumulate(sDUONG_PHO_DDO, DUONG_PHO_DDO);
            jsonObject.accumulate(sTKHOAN_NGANHANG, TKHOAN_NGANHANG);
            jsonObject.accumulate(sMA_NHANG, MA_NHANG);
            jsonObject.accumulate(sMA_SOTHUE, MA_SOTHUE);
            jsonObject.accumulate(sCHUC_VU, CHUC_VU);
            jsonObject.accumulate(sGenderId, GenderId);
            jsonObject.accumulate(sMA_DVIDCHINHC2, MA_DVIDCHINHC2);
            jsonObject.accumulate(sMA_DVIDCHINHC3, MA_DVIDCHINHC3);
            jsonObject.accumulate(sKQ_KHSAT_ID, KQ_KHSAT_ID);
            jsonObject.accumulate(sMA_TNGAI, MA_TNGAI);
            jsonObject.accumulate(sMA_QUYEN, MA_QUYEN);
            jsonObject.accumulate(sHINHTHUC_LAPDAT, HINHTHUC_LAPDAT);
            jsonObject.accumulate(sLOAI_CAP, LOAI_CAP);
            jsonObject.accumulate(sNKY_KSAT, NKY_KSAT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONPhuongAn(int HOSO_ID, int CSUAT_TOIDA_THUCTE, int CSUAT_PHANKHANG_THUCTE, int MUC_CSUAT_2204,
                                         int MUC_CSUAT_0409, int MUC_CSUAT_0911, int MUC_CSUAT_1117, int MUC_CSUAT_1720,
                                         int MUC_CSUAT_2022, String DODEM_CTO_A, String DODEM_CTO_V, String DODEM_CTO_Ti,
                                         String DIEM_DAUDIEN, String TAITRAMBIENAP_NGAY, int VTRI_CTO, String COT_SO,
                                         String VTRI_CTO_KHAC, int TTRANG_SDUNG_DIEN, int HTHUC_GCS, int HTHUC_TRATIEN,
                                         String HTHUC_TRATIEN_KHAC) {
        String sHOSO_ID = "HOSO_ID";
        String sCSUAT_TOIDA_THUCTE = "CSUAT_TOIDA_THUCTE";
        String sCSUAT_PHANKHANG_THUCTE = "CSUAT_PHANKHANG_THUCTE";
        String sMUC_CSUAT_2204 = "MUC_CSUAT_2204";
        String sMUC_CSUAT_0409 = "MUC_CSUAT_0409";
        String sMUC_CSUAT_0911 = "MUC_CSUAT_0911";
        String sMUC_CSUAT_1117 = "MUC_CSUAT_1117";
        String sMUC_CSUAT_1720 = "MUC_CSUAT_1720";
        String sMUC_CSUAT_2022 = "MUC_CSUAT_2022";
        String sDODEM_CTO_A = "DODEM_CTO_A";
        String sDODEM_CTO_V = "DODEM_CTO_V";
        String sDODEM_CTO_Ti = "DODEM_CTO_Ti";
        String sDIEM_DAUDIEN = "DIEM_DAUDIEN";
        String sTAITRAMBIENAP_NGAY = "TAITRAMBIENAP_NGAY";
        String sVTRI_CTO = "VTRI_CTO";
        String sCOT_SO = "COT_SO";
        String sVTRI_CTO_KHAC = "VTRI_CTO_KHAC";
        String sTTRANG_SDUNG_DIEN = "TTRANG_SDUNG_DIEN";
        String sHTHUC_GCS = "HTHUC_GCS";
        String sHTHUC_TRATIEN = "HTHUC_TRATIEN";
        String sHTHUC_TRATIEN_KHAC = "HTHUC_TRATIEN_KHAC";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sCSUAT_TOIDA_THUCTE, CSUAT_TOIDA_THUCTE);
            jsonObject.accumulate(sCSUAT_PHANKHANG_THUCTE, CSUAT_PHANKHANG_THUCTE);
            jsonObject.accumulate(sMUC_CSUAT_2204, MUC_CSUAT_2204);
            jsonObject.accumulate(sMUC_CSUAT_0409, MUC_CSUAT_0409);
            jsonObject.accumulate(sMUC_CSUAT_0911, MUC_CSUAT_0911);
            jsonObject.accumulate(sMUC_CSUAT_1117, MUC_CSUAT_1117);
            jsonObject.accumulate(sMUC_CSUAT_1720, MUC_CSUAT_1720);
            jsonObject.accumulate(sMUC_CSUAT_2022, MUC_CSUAT_2022);
            jsonObject.accumulate(sDODEM_CTO_A, DODEM_CTO_A);
            jsonObject.accumulate(sDODEM_CTO_V, DODEM_CTO_V);
            jsonObject.accumulate(sDODEM_CTO_Ti, DODEM_CTO_Ti);
            jsonObject.accumulate(sDIEM_DAUDIEN, DIEM_DAUDIEN);
            jsonObject.accumulate(sTAITRAMBIENAP_NGAY, TAITRAMBIENAP_NGAY);
            jsonObject.accumulate(sVTRI_CTO, VTRI_CTO);
            jsonObject.accumulate(sCOT_SO, COT_SO);
            jsonObject.accumulate(sVTRI_CTO_KHAC, VTRI_CTO_KHAC);
            jsonObject.accumulate(sTTRANG_SDUNG_DIEN, TTRANG_SDUNG_DIEN);
            jsonObject.accumulate(sHTHUC_GCS, HTHUC_GCS);
            jsonObject.accumulate(sHTHUC_TRATIEN, HTHUC_TRATIEN);
            jsonObject.accumulate(sHTHUC_TRATIEN_KHAC, HTHUC_TRATIEN_KHAC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONDuToan(int DUTOAN_ID, int HOSO_ID, String MA_DVIQLY, String MA_VTU,
                                       float SO_LUONG, int SO_HUU, int STT, String GHI_CHU,
                                       int TT_TUTUC, int TT_THUHOI, float HSDC_K1NC, float HSDC_K2NC, float HSDC_MTC) {
        String sDUTOAN_ID = "DUTOAN_ID";
        String sHOSO_ID = "HOSO_ID";
        String sMA_DVIQLY = "MA_DVIQLY";
        String sMA_VTU = "MA_VTU";
        String sSO_LUONG = "SO_LUONG";
        String sSO_HUU = "SO_HUU";
        String sSTT = "STT";
        String sGHI_CHU = "GHI_CHU";
        String sTT_TUTUC = "TT_TUTUC";
        String sTT_THUHOI = "TT_THUHOI";
        String sHSDC_K1NC = "HSDC_K1NC";
        String sHSDC_K2NC = "HSDC_K2NC";
        String sHSDC_MTC = "HSDC_MTC";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sDUTOAN_ID, DUTOAN_ID);
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sMA_DVIQLY, MA_DVIQLY);
            jsonObject.accumulate(sMA_VTU, MA_VTU);
            jsonObject.accumulate(sSO_LUONG, SO_LUONG);
            jsonObject.accumulate(sSO_HUU, SO_HUU);
            jsonObject.accumulate(sSTT, STT);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);
            jsonObject.accumulate(sTT_TUTUC, TT_TUTUC);
            jsonObject.accumulate(sTT_THUHOI, TT_THUHOI);
            jsonObject.accumulate(sHSDC_K1NC, HSDC_K1NC);
            jsonObject.accumulate(sHSDC_K2NC, HSDC_K2NC);
            jsonObject.accumulate(sHSDC_MTC, HSDC_MTC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONDuToan(int DUTOAN_ID, int HOSO_ID, String MA_DVIQLY, String MA_VTU,
                                       float SO_LUONG, int SO_HUU, int STT, String GHI_CHU,
                                       int TT_TUTUC, int TT_THUHOI) {
        String sDUTOAN_ID = "DUTOAN_ID";
        String sHOSO_ID = "HOSO_ID";
        String sMA_DVIQLY = "MA_DVIQLY";
        String sMA_VTU = "MA_VTU";
        String sSO_LUONG = "SO_LUONG";
        String sSO_HUU = "SO_HUU";
        String sSTT = "STT";
        String sGHI_CHU = "GHI_CHU";
        String sTT_TUTUC = "TT_TUTUC";
        String sTT_THUHOI = "TT_THUHOI";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sDUTOAN_ID, DUTOAN_ID);
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sMA_DVIQLY, MA_DVIQLY);
            jsonObject.accumulate(sMA_VTU, MA_VTU);
            jsonObject.accumulate(sSO_LUONG, SO_LUONG);
            jsonObject.accumulate(sSO_HUU, SO_HUU);
            jsonObject.accumulate(sSTT, STT);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);
            jsonObject.accumulate(sTT_TUTUC, TT_TUTUC);
            jsonObject.accumulate(sTT_THUHOI, TT_THUHOI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONDuToanNHT(int VTKHTT_ID, int HOSO_ID, String TENVTU_MTB,
                                          float SO_LUONG, String DVI_TINH, double DON_GIA_KH,
                                          String GHI_CHU, int CHUNG_LOAI, double DG_NCONG,
                                          double DG_VTU, double DG_MTCONG, float HSDC_K1NC, float HSDC_K2NC, float HSDC_MTC) {
        String sVTKHTT_ID = "VTKHTT_ID";
        String sHOSO_ID = "HOSO_ID";
        String sTENVTU_MTB = "TENVTU_MTB";
        String sSO_LUONG = "SO_LUONG";
        String sDVI_TINH = "DVI_TINH";
        String sDON_GIA_KH = "DON_GIA_KH";
        String sGHI_CHU = "GHI_CHU";
        String sCHUNG_LOAI = "CHUNG_LOAI";
        String sDG_NCONG = "DG_NCONG";
        String sDG_VTU = "DG_VTU";
        String sDG_MTCONG = "DG_MTCONG";
        String sHSDC_K1NC = "HSDC_K1NC";
        String sHSDC_K2NC = "HSDC_K2NC";
        String sHSDC_MTC = "HSDC_MTC";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sVTKHTT_ID, VTKHTT_ID);
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sTENVTU_MTB, TENVTU_MTB);
            jsonObject.accumulate(sSO_LUONG, SO_LUONG);
            jsonObject.accumulate(sDVI_TINH, DVI_TINH);
            jsonObject.accumulate(sDON_GIA_KH, DON_GIA_KH);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);
            jsonObject.accumulate(sCHUNG_LOAI, CHUNG_LOAI);
            jsonObject.accumulate(sDG_NCONG, DG_NCONG);
            jsonObject.accumulate(sDG_VTU, DG_VTU);
            jsonObject.accumulate(sDG_MTCONG, DG_MTCONG);
            jsonObject.accumulate(sHSDC_K1NC, HSDC_K1NC);
            jsonObject.accumulate(sHSDC_K2NC, HSDC_K2NC);
            jsonObject.accumulate(sHSDC_MTC, HSDC_MTC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONDuToanNHT(int VTKHTT_ID, int HOSO_ID, String TENVTU_MTB,
                                          float SO_LUONG, String DVI_TINH, double DON_GIA_KH,
                                          String GHI_CHU, int CHUNG_LOAI) {
        String sVTKHTT_ID = "VTKHTT_ID";
        String sHOSO_ID = "HOSO_ID";
        String sTENVTU_MTB = "TENVTU_MTB";
        String sSO_LUONG = "SO_LUONG";
        String sDVI_TINH = "DVI_TINH";
        String sDON_GIA_KH = "DON_GIA_KH";
        String sGHI_CHU = "GHI_CHU";
        String sCHUNG_LOAI = "CHUNG_LOAI";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sVTKHTT_ID, VTKHTT_ID);
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sTENVTU_MTB, TENVTU_MTB);
            jsonObject.accumulate(sSO_LUONG, SO_LUONG);
            jsonObject.accumulate(sDVI_TINH, DVI_TINH);
            jsonObject.accumulate(sDON_GIA_KH, DON_GIA_KH);
            jsonObject.accumulate(sGHI_CHU, GHI_CHU);
            jsonObject.accumulate(sCHUNG_LOAI, CHUNG_LOAI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONCongSuat(int CSUAT_ID, int HOSO_ID, String GIO_TRONG_NGAY, int CSUAT, String KHOANG_GIO) {
        String sCSUAT_ID = "CSUAT_ID";
        String sHOSO_ID = "HOSO_ID";
        String sGIO_TRONG_NGAY = "GIO_TRONG_NGAY";
        String sCSUAT = "CSUAT";
        String sKHOANG_GIO = "KHOANG_GIO";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sCSUAT_ID, CSUAT_ID);
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sGIO_TRONG_NGAY, GIO_TRONG_NGAY);
            jsonObject.accumulate(sCSUAT, CSUAT);
            jsonObject.accumulate(sKHOANG_GIO, KHOANG_GIO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONThietBi(int HOSO_ID, String MUC_DICH, String LOAI_TBI,
                                        String CONG_SUAT, int SO_LUONG, String DIENAP_SUDUNG) {
        String sHOSO_ID = "HOSO_ID";
        String sMUC_DICH = "MUC_DICH";
        String sLOAI_TBI = "LOAI_TBI";
        String sCONG_SUAT = "CONG_SUAT";
        String sSO_LUONG = "SO_LUONG";
        String sDIENAP_SUDUNG = "DIENAP_SUDUNG";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(sHOSO_ID, HOSO_ID);
            jsonObject.accumulate(sMUC_DICH, MUC_DICH);
            jsonObject.accumulate(sLOAI_TBI, LOAI_TBI);
            jsonObject.accumulate(sCONG_SUAT, CONG_SUAT);
            jsonObject.accumulate(sSO_LUONG, SO_LUONG);
            jsonObject.accumulate(sDIENAP_SUDUNG, DIENAP_SUDUNG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    //endregion

    //region Tạo file pdf
    private static Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(EsspCommon.color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(EsspCommon.width);
        return paint;
    }

    private static Paint getPaintText() {
        Paint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(10);
        return paintText;
    }

    public static void createPDF(Context ctx, EsspEntityHoSo entity) {
        Document document = new Document(PageSize.A4);
        String outpath = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + "/PTN.pdf";
        File fOut = new File(outpath);
        if (fOut.exists()) {
            fOut.delete();
        }
        Rectangle rect = document.getPageSize();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        float width = rect.getWidth();
        float height = rect.getHeight();
        float leftMargin = width / 15;
        float rightMargin = width / 20;
        float topMargin = width / 20;
        float bottomMargin = width / 20;
        document.setMargins(leftMargin, rightMargin, topMargin, bottomMargin);
        Bitmap bmp = Bitmap.createBitmap((int) width, (int) height, conf);
        Canvas canvas = new Canvas(bmp);
//        canvas.drawRect(0, 0, width - 2*rightMargin, height - 2*bottomMargin, getPaint());
        canvas.drawText("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", 1, 1, getPaintText());

        try {
            BaseFont urName = BaseFont.createFont("assets/times.ttf", "UTF-8", BaseFont.EMBEDDED);
//            BaseFont urName = BaseFont.createFont("assets/times.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
            Font urFontName = new Font(urName);
            PdfWriter.getInstance(document, new FileOutputStream(outpath));
            document.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());

            document.add(image);
            // header
//            Paragraph paragraph = new Paragraph(entity.getCMIS_MA_YCAU_KNAI());
//            paragraph.setAlignment(Element.ALIGN_LEFT);
//            document.add(paragraph);
//            // Centered
////            paragraph = new Paragraph("TỔNG CÔNG TY", FontFactory.getFont(FontFactory.TIMES_BOLD, "UTF-8", 13, Typeface.NORMAL, BaseColor.BLACK));
////            byte[] b = "TỔNG CÔNG TY".getBytes("UTF-8");
//            paragraph = new Paragraph("TỔNG CÔNG TY điện lực thành phố Hà Nội", urFontName);
//            paragraph.setAlignment(Element.ALIGN_CENTER);
//            document.add(paragraph);
//            // Left
//            paragraph = new Paragraph("This is left aligned text");
//            paragraph.setAlignment(Element.ALIGN_LEFT);
//            document.add(paragraph);
//            // Left with indentation
//            paragraph = new Paragraph("This is left aligned text with indentation This is left aligned text with indentation " +
//                    "This is left aligned text with indentation This is left aligned text with indentation " +
//                    "This is left aligned text with indentation This is left aligned text with indentation " +
//                    "This is left aligned text with indentation");
//            paragraph.setAlignment(Element.ALIGN_LEFT);
//            paragraph.setIndentationLeft(50);
//            document.add(paragraph);

            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Xử lý class adapter
    public class CustomAdapter extends BaseAdapter implements Filterable {

        private ArrayList<String> _Search;
        private Activity context;
        private LayoutInflater inflater;
        private ValueFilter valueFilter;
        private ArrayList<String> mStringFilterList;

        public CustomAdapter(Activity context, ArrayList<String> _Search) {
            super();
            this.context = context;
            this._Search = _Search;
            mStringFilterList = _Search;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }

        @Override
        public int getCount() {
            return _Search.size();
        }

        @Override
        public Object getItem(int position) {
            return _Search.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder {
            TextView tvMenu;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.essp_row_auto_text, null);
                holder.tvMenu = (TextView) convertView.findViewById(R.id.gcs_row_menu_tvMenu);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvMenu.setText(_Search.get(position));
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new ValueFilter();
            }

            return valueFilter;
        }

        private class ValueFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<String> filterList = new ArrayList<>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            filterList.add(mStringFilterList.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }


            //Invoked in the UI thread to publish the filtering results in the user interface.
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                _Search = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        }

    }

    class AdapterCongSuat extends ArrayAdapter<LinkedHashMap<String, String>> {

        private Context context;

        public AdapterCongSuat(Context context, int resource, ArrayList<LinkedHashMap<String, String>> objects) {
            super(context, resource, objects);
            this.context = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.essp_row_congsuat, null);

            TextView tvKhoangGio = (TextView) rootView.findViewById(R.id.essp_row_congsuat_tvKhoangGio);
            TextView tvCongSuat = (TextView) rootView.findViewById(R.id.essp_row_congsuat_tvCongSuat);

            LinkedHashMap<String, String> map = getItem(position);
            tvKhoangGio.setText(map.get("KHOANG_TGIAN"));
            tvCongSuat.setText(map.get("CONG_SUAT"));

            return rootView;
        }
    }

    class EsspHoSoAdapter extends RecyclerView.Adapter<EsspHoSoAdapter.RecyclerViewHolder> {

        List<EsspEntityHoSo> listData = new ArrayList<>();

        public EsspHoSoAdapter(List<EsspEntityHoSo> listData) {
            this.listData = listData;
        }

        public void updateList(List<EsspEntityHoSo> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<EsspEntityHoSo> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

        private void applyAndAnimateRemovals(List<EsspEntityHoSo> newModels) {
            for (int i = listData.size() - 1; i >= 0; i--) {
                final EsspEntityHoSo model = listData.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

//        private void applyAndAnimateAdditions(List<EsspEntityHoSo> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final EsspEntityHoSo model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

        private void applyAndAnimateMovedItems(List<EsspEntityHoSo> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final EsspEntityHoSo model = newModels.get(toPosition);
                final int fromPosition = listData.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public void moveItem(int fromPosition, int toPosition) {
            final EsspEntityHoSo model = listData.remove(fromPosition);
            listData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_hoso, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvSTT.setText(listData.get(position).getSTT());
//            holder.tvMaHS.setText(listData.get(position).getCMIS_MA_YCAU_KNAI());
            holder.tvTenKH.setText(listData.get(position).getTEN_KHANG());
            holder.tvDiaChi.setText(listData.get(position).getSO_NHA_DDO() + " " + listData.get(position).getDUONG_PHO_DDO());

            int isSentDuToan = EsspMainFragment.this.connection.getTTDT(listData.get(position).getHOSO_ID());
            int isSentBanVe = EsspMainFragment.this.connection.getTTBV(listData.get(position).getHOSO_ID());
            int isSentPhuongAn = EsspMainFragment.this.connection.getTTPA(listData.get(position).getHOSO_ID());

            if (listData.get(position).getTINH_TRANG() == EsspCommon.TINH_TRANG && isSentDuToan == 1 && isSentBanVe == 1 && isSentPhuongAn == 1) {
                holder.itemView.setBackgroundResource(R.drawable.bg_item_blue);
//                holder.itemView.setBackgroundResource(R.drawable.card_background_blue);
//                holder.itemView.setBackgroundResource(R.drawable.bg_listitem_cyan);
            } else {
                if (listData.get(position).getMA_TRAM().isEmpty()) {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_white);
//                    holder.itemView.setBackgroundResource(R.drawable.card_background_white);
//                    holder.itemView.setBackgroundResource(R.drawable.bg_listitem_white);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bg_item_gray);
//                    holder.itemView.setBackgroundResource(R.drawable.card_background_gray);
//                    holder.itemView.setBackgroundResource(R.drawable.bg_listitem_green);
                }
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, EsspEntityHoSo data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

            public TextView tvSTT, tvTenKH, tvDiaChi;
            public CheckBox ckChon;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvSTT = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_stt);
//                tvMaHS = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_ma_hoso);
                tvTenKH = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_ten_kh);
                tvDiaChi = (TextView) itemView.findViewById(R.id.essp_row_hoso_tv_diachi);
                ckChon = (CheckBox) itemView.findViewById(R.id.essp_row_hoso_ck_chon);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                ckChon.post(new Runnable() {
                    @Override
                    public void run() {
                        ckChon.setOnCheckedChangeListener(RecyclerViewHolder.this);
                    }
                });
            }

            @Override
            public void onClick(View v) {
                showPopupMenu(v, listData.get(getPosition()), getPosition());
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!lstHoSoId.contains(String.valueOf(listData.get(getPosition()).getHOSO_ID()))) {
                        lstHoSoId.add(String.valueOf(listData.get(getPosition()).getHOSO_ID()));
                        lstPos.add(String.valueOf(getPosition()));
                    }
                } else {
                    if (lstHoSoId.contains(String.valueOf(listData.get(getPosition()).getHOSO_ID()))) {
                        lstHoSoId.remove(String.valueOf(listData.get(getPosition()).getHOSO_ID()));
                        lstPos.remove(String.valueOf(getPosition()));
                    }
                }
            }

            @Override
            public boolean onLongClick(View v) {
                showDialogCmdCode(listData.get(getPosition()).getHOSO_ID());
                return false;
            }
        }

    }

    class EsspVatTuDuToanAdapter extends RecyclerView.Adapter<EsspVatTuDuToanAdapter.RecyclerViewHolder> {

        List<EsspEntityVatTuDuToan> listData = new ArrayList<>();
        int HOSO_ID;
        String SO_HUU;
        TextView tvThanhTien;

        public EsspVatTuDuToanAdapter(List<EsspEntityVatTuDuToan> listData, int HOSO_ID, String SO_HUU, TextView tvThanhTien) {
            this.listData = listData;
            this.HOSO_ID = HOSO_ID;
            this.SO_HUU = SO_HUU;
            this.tvThanhTien = tvThanhTien;
        }

        public void updateList(List<EsspEntityVatTuDuToan> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<EsspEntityVatTuDuToan> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

//        private void applyAndAnimateRemovals(List<EsspEntityVatTuDuToan> newModels) {
//            for (int i = listData.size() - 1; i >= 0; i--) {
//                final EsspEntityVatTuDuToan model = listData.get(i);
//                if (!newModels.contains(model)) {
//                    removeItem(i);
//                }
//            }
//        }

//        private void applyAndAnimateAdditions(List<EsspEntityVatTuDuToan> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final EsspEntityVatTuDuToan model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

//        private void applyAndAnimateMovedItems(List<EsspEntityVatTuDuToan> newModels) {
//            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//                final EsspEntityVatTuDuToan model = newModels.get(toPosition);
//                final int fromPosition = listData.indexOf(model);
//                if (fromPosition >= 0 && fromPosition != toPosition) {
//                    moveItem(fromPosition, toPosition);
//                }
//            }
//        }

//        public void moveItem(int fromPosition, int toPosition) {
//            final EsspEntityVatTuDuToan model = listData.remove(fromPosition);
//            listData.add(toPosition, model);
//            notifyItemMoved(fromPosition, toPosition);
//        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_dutoan, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvVatTu.setText(listData.get(position).getTEN_VTU());
            holder.tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(listData.get(position).getDON_GIA()).toString()))
                    .append(" đ/").append(listData.get(position).getDVI_TINH()).toString());
            holder.tvSoLuong.setText(Common.formatFloatNumber(String.valueOf(listData.get(position).getSO_LUONG())));

            if (listData.get(position).getSO_HUU() == 1) {
                holder.ckThuHoi.setVisibility(View.GONE);
                holder.tvThuHoi.setVisibility(View.GONE);
                if (listData.get(position).getCHUNG_LOAI() == 1) {// Dây cáp
                    holder.ckTuTuc.setVisibility(View.VISIBLE);
                    holder.tvTuTuc.setVisibility(View.VISIBLE);
                    holder.ckTuTucHt.setVisibility(View.VISIBLE);
                    holder.tvTuTucHt.setVisibility(View.VISIBLE);
                    if (listData.get(position).getTT_TU_TUC() == 1) {
                        holder.ckTuTuc.setChecked(true);
                        holder.ckTuTucHt.setChecked(false);
                    } else if (listData.get(position).getTT_TU_TUC() == 2) {
                        holder.ckTuTuc.setChecked(false);
                        holder.ckTuTucHt.setChecked(true);
                    } else {
                        holder.ckTuTuc.setChecked(false);
                        holder.ckTuTucHt.setChecked(false);
                    }
                } else {
                    holder.ckTuTuc.setVisibility(View.GONE);
                    holder.tvTuTuc.setVisibility(View.GONE);
                    holder.ckTuTucHt.setVisibility(View.GONE);
                    holder.tvTuTucHt.setVisibility(View.GONE);
                }
            } else if (listData.get(position).getSO_HUU() == 2) {
                holder.ckTuTuc.setVisibility(View.GONE);
                holder.tvTuTuc.setVisibility(View.GONE);
                holder.ckTuTucHt.setVisibility(View.GONE);
                holder.tvTuTucHt.setVisibility(View.GONE);
                holder.ckThuHoi.setVisibility(View.VISIBLE);
                holder.tvThuHoi.setVisibility(View.VISIBLE);
                if (listData.get(position).getTT_THU_HOI() == 1) {
                    holder.ckThuHoi.setChecked(true);
                } else {
                    holder.ckThuHoi.setChecked(false);
                }
            } else {
                holder.ckTuTuc.setVisibility(View.GONE);
                holder.tvTuTuc.setVisibility(View.GONE);
                holder.ckTuTucHt.setVisibility(View.GONE);
                holder.tvTuTucHt.setVisibility(View.GONE);
                holder.ckThuHoi.setVisibility(View.GONE);
                holder.tvThuHoi.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, EsspEntityVatTuDuToan data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public void updateSoLuongItem(int position, float SO_LUONG) {
            listData.get(position).setSO_LUONG(SO_LUONG);
            notifyItemChanged(position);
        }

//        public void updateItem(int position, float SO_LUONG, String DON_GIA, String THANH_TIEN) {
//            listData.get(position).setSO_LUONG(SO_LUONG);
//            listData.get(position).setDON_GIA(DON_GIA);
//            listData.get(position).setTHANH_TIEN(THANH_TIEN);
//            notifyItemChanged(position);
//        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvVatTu, tvDonGia, tvSoLuong, tvTuTuc, tvTuTucHt, tvThuHoi;
            public ImageButton ibDelete, ibAddNumber;
            public CheckBox ckTuTuc, ckTuTucHt, ckThuHoi;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvVatTu = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_ten_vattu);
                tvDonGia = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_dongia);
                tvSoLuong = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_soluong);
                ibDelete = (ImageButton) itemView.findViewById(R.id.essp_row_dutoan_ib_delete);
                ibAddNumber = (ImageButton) itemView.findViewById(R.id.essp_row_dutoan_ib_add_sl);
                ckTuTuc = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_tutuc);
                ckTuTucHt = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_tutuc_ht);
                ckThuHoi = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_thuhoi);
                tvTuTuc = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_tutuc);
                tvTuTucHt = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_tutuc_ht);
                tvThuHoi = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_thuhoi);

                ibDelete.setOnClickListener(this);
                ibAddNumber.setOnClickListener(this);
                ckTuTuc.setOnCheckedChangeListener(this);
                ckTuTucHt.setOnCheckedChangeListener(this);
                ckThuHoi.setOnCheckedChangeListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.essp_row_dutoan_ib_delete:
                            if (listData.get(getPosition()).getSO_HUU() < 3) {
                                if (connection.deleteDuToan(HOSO_ID, listData.get(getPosition()).getMA_VTU(), SO_HUU) != -1) {
                                    String ma_vtu = listData.get(getPosition()).getMA_VTU();
                                    if (arrMaVatTu.contains(ma_vtu))
                                        arrMaVatTu.remove(ma_vtu);
                                    removeItem(getPosition());
                                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                                }
                            } else {
                                if (connection.deleteDuToanNht(listData.get(getPosition()).getID()) != -1) {
                                    removeItem(getPosition());
                                    tinhThanhTien(tvThanhTien, HOSO_ID, "1", false);
                                }
                            }
                            break;
                        case R.id.essp_row_dutoan_ib_add_sl:
                            showDialogUpdateSoLuong(listData.get(getPosition()).getID(), HOSO_ID, listData.get(getPosition()).getMA_VTU(), String.valueOf(listData.get(getPosition()).getSO_HUU()), getPosition(), ckTuTuc.isChecked(), tvThanhTien);
                            break;
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi sự kiện", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (buttonView.getId() == R.id.essp_row_dutoan_ck_tutuc) {
                        Cursor c = connection.getDonGiaVatDu(listData.get(getPosition()).getMA_VTU());
                        if (c.moveToFirst()) {
                            double DON_GIA = 0d;
                            int TU_TUC = 0;
                            if (isChecked) {
                                ckTuTucHt.setChecked(false);
                                DON_GIA = (Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA_KH")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA_KH")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA_KH"))));
                                TU_TUC = 1;
                            } else {
                                DON_GIA = Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA")));
                                TU_TUC = 0;
                            }
                            float SO_LUONG = Float.parseFloat(connection.getSoLuong(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU()));
                            double THANH_TIEN = DON_GIA * SO_LUONG;
                            if (connection.updateDonGia(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(),
                                    String.valueOf(DON_GIA), String.valueOf(THANH_TIEN), TU_TUC) != -1) {
//                                tvDonGia.setText(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()));
                                tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()))
                                        .append(" đ/").append(c.getString(c.getColumnIndex("DVI_TINH"))).toString());
//                                try {
//                                    tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                            .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, SO_HUU)
//                                                    + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                                } catch (Exception ex) {
//                                    tvThanhTien.setText("Thành tiền: 0");
//                                }
                                tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                            }
                        }
                    } else if (buttonView.getId() == R.id.essp_row_dutoan_ck_thuhoi) {
                        int THU_HOI = 0;
                        if (ckThuHoi.isChecked()) {
                            THU_HOI = 1;
                        } else {
                            THU_HOI = 0;
                        }
                        if (connection.updateThuHoi(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(), THU_HOI) != -1) {

                        }
                    } else if (buttonView.getId() == R.id.essp_row_dutoan_ck_tutuc_ht) {
                        Cursor c = connection.getDonGiaVatDu(listData.get(getPosition()).getMA_VTU());
                        if (c.moveToFirst()) {
                            double DON_GIA = 0d;
                            int TU_TUC = 0;
                            if (isChecked) {
                                ckTuTuc.setChecked(false);
                                DON_GIA = 0;
                                TU_TUC = 2;
                            } else {
                                DON_GIA = Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA")));
                                TU_TUC = 0;
                            }
                            float SO_LUONG = Float.parseFloat(connection.getSoLuong(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU()));
                            double THANH_TIEN = DON_GIA * SO_LUONG;
                            if (connection.updateDonGia(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(),
                                    String.valueOf(DON_GIA), String.valueOf(THANH_TIEN), TU_TUC) != -1) {
//                                tvDonGia.setText(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()));
                                tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()))
                                        .append(" đ/").append(c.getString(c.getColumnIndex("DVI_TINH"))).toString());
//                                try {
//                                    tvThanhTien.setText(new StringBuilder("Thành tiền: ")
//                                            .append(Common.formatMoney(String.valueOf(connection.sumThanhTien(HOSO_ID, SO_HUU)
//                                                    + connection.sumThanhTienNHT(HOSO_ID)))).toString());
//                                } catch (Exception ex) {
//                                    tvThanhTien.setText("Thành tiền: 0");
//                                }
                                tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, false);
                            }
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi khách hàng cấp", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    class EsspVatTuDuToanMoiAdapter extends RecyclerView.Adapter<EsspVatTuDuToanMoiAdapter.RecyclerViewHolder> {

        List<EsspEntityVatTuDuToan> listData = new ArrayList<>();
        int HOSO_ID;
        String SO_HUU;
        TextView tvThanhTien;
        int khaiGia;

        public EsspVatTuDuToanMoiAdapter(List<EsspEntityVatTuDuToan> listData, int HOSO_ID, String SO_HUU, TextView tvThanhTien, int khaiGia) {
            this.listData = listData;
            this.HOSO_ID = HOSO_ID;
            this.SO_HUU = SO_HUU;
            this.tvThanhTien = tvThanhTien;
            this.khaiGia = khaiGia;
        }

        public void updateList(List<EsspEntityVatTuDuToan> data) {
            listData = data;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_dutoan, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            try {
                holder.tvVatTu.setText(listData.get(position).getTEN_VTU());
                holder.tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(listData.get(position).getDON_GIA()).toString()))
                        .append(" đ/").append(listData.get(position).getDVI_TINH()).toString());
                holder.tvSoLuong.setText(Common.formatFloatNumber(String.valueOf(listData.get(position).getSO_LUONG())));

//                holder.ckTuTuc.setVisibility(View.GONE);
//                holder.tvTuTuc.setVisibility(View.GONE);
//                holder.ckTuTucHt.setVisibility(View.GONE);
//                holder.tvTuTucHt.setVisibility(View.GONE);
                if (listData.get(position).getSO_HUU() == 1) {
                    holder.ckThuHoi.setVisibility(View.GONE);
                    holder.tvThuHoi.setVisibility(View.GONE);
                } else if (listData.get(position).getSO_HUU() == 2) {
                    holder.ckThuHoi.setVisibility(View.VISIBLE);
                    holder.tvThuHoi.setVisibility(View.VISIBLE);
                    if (listData.get(position).getTT_THU_HOI() == 1) {
                        holder.ckThuHoi.setChecked(true);
                    } else {
                        holder.ckThuHoi.setChecked(false);
                    }
                } else {
                    holder.ckThuHoi.setVisibility(View.GONE);
                    holder.tvThuHoi.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public void updateSoLuongItem(int position, float SO_LUONG) {
            listData.get(position).setSO_LUONG(SO_LUONG);
            notifyItemChanged(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvVatTu, tvDonGia, tvSoLuong, tvTuTuc, tvTuTucHt, tvThuHoi;
            public ImageButton ibDelete, ibAddNumber;
            public CheckBox ckTuTuc, ckTuTucHt, ckThuHoi;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvVatTu = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_ten_vattu);
                tvDonGia = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_dongia);
                tvSoLuong = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_soluong);
                ibDelete = (ImageButton) itemView.findViewById(R.id.essp_row_dutoan_ib_delete);
                ibAddNumber = (ImageButton) itemView.findViewById(R.id.essp_row_dutoan_ib_add_sl);
                ckTuTuc = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_tutuc);
                ckTuTucHt = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_tutuc_ht);
                ckThuHoi = (CheckBox) itemView.findViewById(R.id.essp_row_dutoan_ck_thuhoi);
                tvTuTuc = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_tutuc);
                tvTuTucHt = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_tutuc_ht);
                tvThuHoi = (TextView) itemView.findViewById(R.id.essp_row_dutoan_tv_tittle_thuhoi);

                ckTuTuc.setVisibility(View.GONE);
                tvTuTuc.setVisibility(View.GONE);
                ckTuTucHt.setVisibility(View.GONE);
                tvTuTucHt.setVisibility(View.GONE);

                if (Integer.parseInt(SO_HUU) == 1) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ibDelete.getLayoutParams();
                    if (khaiGia == 0) {
                        ibAddNumber.setVisibility(View.VISIBLE);
                        params.addRule(RelativeLayout.LEFT_OF, R.id.essp_row_dutoan_ib_add_sl);
                    } else {
                        ibAddNumber.setVisibility(View.GONE);
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                    ibDelete.setLayoutParams(params);
                } else {
                    ibAddNumber.setVisibility(View.VISIBLE);
                }

                ibDelete.setOnClickListener(this);
                ibAddNumber.setOnClickListener(this);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO VinhNB sửa. lý do: không click được vào row item dự trù vật tư công ty
                        if (Integer.parseInt(SO_HUU) == 1) {
                            popupMenuVatTuKhaiGiaMoi(HOSO_ID, SO_HUU, tvThanhTien, listData.get(getPosition()).getMA_VTU(),
                                    false, listData.get(getPosition()).getMA_LOAI_CPHI().isEmpty() ? true : false,
                                    khaiGia == 0 ? false : true, listData.get(getPosition()).getID());
                        }
//                        popupMenuVatTuKhaiGiaMoi(HOSO_ID, SO_HUU, tvThanhTien, listData.get(getPosition()).getMA_VTU(),
//                                false, listData.get(getPosition()).getMA_LOAI_CPHI().isEmpty() ? true : false,
//                                khaiGia == 0 ? false : true, listData.get(getPosition()).getID());
                    }
                });
            }

            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.essp_row_dutoan_ib_delete:
                            if (listData.get(getPosition()).getSO_HUU() < 3) {
                                if (connection.deleteDuToan(HOSO_ID, listData.get(getPosition()).getMA_VTU(), SO_HUU) != -1) {
                                    String ma_vtu = listData.get(getPosition()).getMA_VTU();
                                    if (arrMaVatTu.contains(ma_vtu))
                                        arrMaVatTu.remove(ma_vtu);
                                    removeItem(getPosition());
                                    tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, khaiGia == 0 ? false : true);
                                }
                            } else {
                                if (connection.deleteDuToanNht(listData.get(getPosition()).getID()) != -1) {
                                    removeItem(getPosition());
                                    tinhThanhTien(tvThanhTien, HOSO_ID, "1", khaiGia == 0 ? false : true);
                                }
                            }
                            break;
                        case R.id.essp_row_dutoan_ib_add_sl:
                            showDialogUpdateSoLuongMoi(listData.get(getPosition()).getID(), HOSO_ID, listData.get(getPosition()).getMA_VTU(),
                                    String.valueOf(listData.get(getPosition()).getSO_HUU()), getPosition(), tvThanhTien,
                                    connection.getLoaiHinhTToan(HOSO_ID));
                            break;
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi sự kiện", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (buttonView.getId() == R.id.essp_row_dutoan_ck_tutuc) {
                        Cursor c = connection.getDonGiaVatDu(listData.get(getPosition()).getMA_VTU());
                        if (c.moveToFirst()) {
                            double DON_GIA = 0d;
                            int TU_TUC = 0;
                            if (isChecked) {
                                ckTuTucHt.setChecked(false);
                                DON_GIA = (Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA_KH")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA_KH")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA_KH"))));
                                TU_TUC = 1;
                            } else {
                                DON_GIA = Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA")));
                                TU_TUC = 0;
                            }
                            float SO_LUONG = Float.parseFloat(connection.getSoLuong(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU()));
                            double THANH_TIEN = DON_GIA * SO_LUONG;
                            if (connection.updateDonGia(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(),
                                    String.valueOf(DON_GIA), String.valueOf(THANH_TIEN), TU_TUC) != -1) {
                                tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()))
                                        .append(" đ/").append(c.getString(c.getColumnIndex("DVI_TINH"))).toString());
                                tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, khaiGia == 0 ? false : true);
                            }
                        }
                    } else if (buttonView.getId() == R.id.essp_row_dutoan_ck_thuhoi) {
                        int THU_HOI = 0;
                        if (ckThuHoi.isChecked()) {
                            THU_HOI = 1;
                        } else {
                            THU_HOI = 0;
                        }
                        if (connection.updateThuHoi(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(), THU_HOI) != -1) {

                        }
                    } else if (buttonView.getId() == R.id.essp_row_dutoan_ck_tutuc_ht) {
                        Cursor c = connection.getDonGiaVatDu(listData.get(getPosition()).getMA_VTU());
                        if (c.moveToFirst()) {
                            double DON_GIA = 0d;
                            int TU_TUC = 0;
                            if (isChecked) {
                                ckTuTuc.setChecked(false);
                                DON_GIA = 0;
                                TU_TUC = 2;
                            } else {
                                DON_GIA = Double.parseDouble((c.getString(c.getColumnIndex("DON_GIA")).isEmpty() || c.getString(c.getColumnIndex("DON_GIA")).equals("null"))
                                        ? "0" : c.getString(c.getColumnIndex("DON_GIA")));
                                TU_TUC = 0;
                            }
                            float SO_LUONG = Float.parseFloat(connection.getSoLuong(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU()));
                            double THANH_TIEN = DON_GIA * SO_LUONG;
                            if (connection.updateDonGia(HOSO_ID, SO_HUU, listData.get(getPosition()).getMA_VTU(),
                                    String.valueOf(DON_GIA), String.valueOf(THANH_TIEN), TU_TUC) != -1) {
                                tvDonGia.setText(new StringBuilder(Common.formatMoney(new BigDecimal(String.valueOf(DON_GIA)).toString()))
                                        .append(" đ/").append(c.getString(c.getColumnIndex("DVI_TINH"))).toString());
                                tinhThanhTien(tvThanhTien, HOSO_ID, SO_HUU, khaiGia == 0 ? false : true);
                            }
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi khách hàng cấp", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    class EsspVatTuHeThongAdapter extends RecyclerView.Adapter<EsspVatTuHeThongAdapter.RecyclerViewHolder> {

        List<EsspEntityVatTu> listData = new ArrayList<>();
        String SO_HUU;
        int HOSO_ID;

        public EsspVatTuHeThongAdapter(List<EsspEntityVatTu> listData, String SO_HUU, int HOSO_ID) {
            this.listData = listData;
            this.SO_HUU = SO_HUU;
            this.HOSO_ID = HOSO_ID;
        }

        public void updateList(List<EsspEntityVatTu> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<EsspEntityVatTu> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

//        private void applyAndAnimateRemovals(List<EsspEntityVatTu> newModels) {
//            for (int i = listData.size() - 1; i >= 0; i--) {
//                final EsspEntityVatTu model = listData.get(i);
//                if (!newModels.contains(model)) {
//                    removeItem(i);
//                }
//            }
//        }

//        private void applyAndAnimateAdditions(List<EsspEntityVatTu> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final EsspEntityVatTu model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

//        private void applyAndAnimateMovedItems(List<EsspEntityVatTu> newModels) {
//            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//                final EsspEntityVatTu model = newModels.get(toPosition);
//                final int fromPosition = listData.indexOf(model);
//                if (fromPosition >= 0 && fromPosition != toPosition) {
//                    moveItem(fromPosition, toPosition);
//                }
//            }
//        }

        public void moveItem(int fromPosition, int toPosition) {
            final EsspEntityVatTu model = listData.remove(fromPosition);
            listData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_vattu, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvVatTu.setText(listData.get(position).getTEN_VTU());
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, EsspEntityVatTu data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

            public TextView tvVatTu;
            public ImageButton ibAdd;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvVatTu = (TextView) itemView.findViewById(R.id.essp_row_vattu_tv_vattu);
                ibAdd = (ImageButton) itemView.findViewById(R.id.essp_row_vattu_ib_add);
                ibAdd.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    if (!connection.checkDuToanExist(HOSO_ID, listData.get(getPosition()).getMA_VTU(), SO_HUU)) {
                        if (connection.insertDataDuToan(HOSO_ID, connection.getMaDviByID(EsspCommon.getIdDviqly()),
                                listData.get(getPosition()).getMA_VTU(), listData.get(getPosition()).getMA_LOAI_CPHI(), "1",
                                listData.get(getPosition()).getDON_GIA(), SO_HUU, "", listData.get(getPosition()).getDON_GIA(),
                                "0", "0", 0, 0, "1", "1", "1") != -1) {
                            removeItem(getPosition());

                            arrVatTu.clear();
                            arrMaVatTu.clear();
//                            Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, "1");
                            Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                            if (c.moveToFirst()) {
                                do {
                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                    entity.setID(c.getInt(c.getColumnIndex("ID")));
                                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                    entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                    entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                    entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                    entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                    entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                    entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                    arrVatTu.add(entity);
                                    arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                } while (c.moveToNext());
                            }

                            if (Integer.parseInt(SO_HUU) == 1) {
                                Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                if (c3.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                        entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                        entity.setMA_LOAI_CPHI("");
                                        entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                        entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH("0");
                                        entity.setTT_DON_GIA("");
                                        entity.setLOAI(0);
                                        entity.setCHUNG_LOAI(1);
                                        entity.setSO_HUU(3);
                                        entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                        entity.setTT_TU_TUC(0);
                                        entity.setTT_THU_HOI(0);
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                    } while (c3.moveToNext());
                                }
                                adapterDuToan.updateList(arrVatTu);
                            }
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi dự toán", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    class EsspVatTuHeThongMoiAdapter extends RecyclerView.Adapter<EsspVatTuHeThongMoiAdapter.RecyclerViewHolder> {

        List<EsspEntityVatTu> listData = new ArrayList<>();
        String SO_HUU;
        int HOSO_ID;
        int LOAIHINH_TTOAN;

        public EsspVatTuHeThongMoiAdapter(List<EsspEntityVatTu> listData, String SO_HUU, int HOSO_ID, int LOAIHINH_TTOAN) {
            this.listData = listData;
            this.SO_HUU = SO_HUU;
            this.HOSO_ID = HOSO_ID;
            this.LOAIHINH_TTOAN = LOAIHINH_TTOAN;
        }

        public void updateList(List<EsspEntityVatTu> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<EsspEntityVatTu> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

//        private void applyAndAnimateRemovals(List<EsspEntityVatTu> newModels) {
//            for (int i = listData.size() - 1; i >= 0; i--) {
//                final EsspEntityVatTu model = listData.get(i);
//                if (!newModels.contains(model)) {
//                    removeItem(i);
//                }
//            }
//        }

//        private void applyAndAnimateAdditions(List<EsspEntityVatTu> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final EsspEntityVatTu model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

//        private void applyAndAnimateMovedItems(List<EsspEntityVatTu> newModels) {
//            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//                final EsspEntityVatTu model = newModels.get(toPosition);
//                final int fromPosition = listData.indexOf(model);
//                if (fromPosition >= 0 && fromPosition != toPosition) {
//                    moveItem(fromPosition, toPosition);
//                }
//            }
//        }

        public void moveItem(int fromPosition, int toPosition) {
            final EsspEntityVatTu model = listData.remove(fromPosition);
            listData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_vattu, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvVatTu.setText(listData.get(position).getTEN_VTU());
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, EsspEntityVatTu data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

            public TextView tvVatTu;
            public ImageButton ibAdd;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvVatTu = (TextView) itemView.findViewById(R.id.essp_row_vattu_tv_vattu);
                ibAdd = (ImageButton) itemView.findViewById(R.id.essp_row_vattu_ib_add);
                ibAdd.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    String THANH_TIEN = "0";
                    String DON_GIA = "0";
                    switch (LOAIHINH_TTOAN) {
                        case EsspConstantVariables.DT_TRONGOI:
                            if (listData.get(getPosition()).getCHUNG_LOAI() == 1) {
                                THANH_TIEN = listData.get(getPosition()).getDG_TRONGOI();
                                DON_GIA = listData.get(getPosition()).getDG_TRONGOI();
                            } else {
                                if (EsspCommon.getMaDviqly().contains("PC")
                                        || EsspCommon.getMaDviqly().contains("PP")) {
                                    THANH_TIEN = listData.get(getPosition()).getDG_TRONGOI();
                                    DON_GIA = listData.get(getPosition()).getDG_TRONGOI();
                                } else {
                                    THANH_TIEN = "0";
                                    DON_GIA = "0";
                                }
                            }
                            break;
                        case EsspConstantVariables.DT_TUTUC_CAP:
                            if (listData.get(getPosition()).getCHUNG_LOAI() == 1) {//dây cáp
                                THANH_TIEN = listData.get(getPosition()).getDON_GIA_KH();
                                DON_GIA = listData.get(getPosition()).getDON_GIA_KH();
                            }
//                            else {
//                                THANH_TIEN = listData.get(getPosition()).getDG_VTU();
//                                DON_GIA = listData.get(getPosition()).getDG_VTU();
//                            }
                            break;
                        case EsspConstantVariables.DT_TUTUC_VTU:
                            THANH_TIEN = String.valueOf(Double.parseDouble(listData.get(getPosition()).getDG_NCONG()) * 10 / 100);
                            DON_GIA = String.valueOf(Double.parseDouble(listData.get(getPosition()).getDG_NCONG()) * 10 / 100);
                            break;
                        case EsspConstantVariables.DT_TUTUC_HTOAN:
                            THANH_TIEN = "0";
                            DON_GIA = "0";
                            break;
                    }
                    if (!connection.checkDuToanExist(HOSO_ID, listData.get(getPosition()).getMA_VTU(), SO_HUU)) {
                        if (connection.insertDataDuToan(HOSO_ID, connection.getMaDviByID(EsspCommon.getIdDviqly()),
                                listData.get(getPosition()).getMA_VTU(), listData.get(getPosition()).getMA_LOAI_CPHI(), "1",
                                DON_GIA, SO_HUU, "", THANH_TIEN, "0", "0", 0, 0) != -1) {
                            removeItem(getPosition());
                            arrVatTu.clear();
                            arrMaVatTu.clear();
                            Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, SO_HUU);
                            if (c.moveToFirst()) {
                                do {
                                    EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                    entity.setID(c.getInt(c.getColumnIndex("ID")));
                                    entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                    entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                    entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                    entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                    entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                    entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                    entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                    entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                    entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                    entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                    entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                    entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                    entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                    entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                    arrVatTu.add(entity);
                                    arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                } while (c.moveToNext());
                            }

                           /* try {
                                connection.updateKhaiGia(HOSO_ID, position);
                                if (position == 0) {
                                    ibAddNht.setVisibility(View.GONE);
                                    btViewKhaiGia.setVisibility(View.GONE);
                                } else {
                                    ibAddNht.setVisibility(View.VISIBLE);
                                    btViewKhaiGia.setVisibility(View.VISIBLE);
                                }

                                arrMaVatTu = new ArrayList<>();
                                arrVatTu = new ArrayList<>();
                                Cursor c = connection.getAllDataDTByMaHSvsSH(HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1));
                                if (c.moveToFirst()) {
                                    do {
                                        EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                        entity.setID(c.getInt(c.getColumnIndex("ID")));
                                        entity.setMA_VTU(c.getString(c.getColumnIndex("MA_VTU")));
                                        entity.setTEN_VTU(c.getString(c.getColumnIndex("TEN_VTU")));
                                        entity.setMA_LOAI_CPHI(c.getString(c.getColumnIndex("MA_LOAI_CPHI")));
                                        entity.setDON_GIA(c.getString(c.getColumnIndex("DON_GIA")));
                                        entity.setDVI_TINH(c.getString(c.getColumnIndex("DVI_TINH")));
                                        entity.setDON_GIA_KH(c.getString(c.getColumnIndex("DON_GIA_KH")));
                                        entity.setTT_DON_GIA(c.getString(c.getColumnIndex("TT_DON_GIA")));
                                        entity.setLOAI(c.getInt(c.getColumnIndex("LOAI")));
                                        entity.setCHUNG_LOAI(c.getInt(c.getColumnIndex("CHUNG_LOAI")));
                                        entity.setSO_HUU(c.getInt(c.getColumnIndex("SO_HUU")));
                                        entity.setSO_LUONG(c.getFloat(c.getColumnIndex("SO_LUONG")));
                                        entity.setTHANH_TIEN(c.getString(c.getColumnIndex("THANH_TIEN")));
                                        entity.setTT_TU_TUC(c.getInt(c.getColumnIndex("TT_TU_TUC")));
                                        entity.setTT_THU_HOI(c.getInt(c.getColumnIndex("TT_THU_HOI")));
                                        entity.setHSDC_K1NC(c.getFloat(c.getColumnIndex("HSDC_K1NC")));
                                        entity.setHSDC_K2NC(c.getFloat(c.getColumnIndex("HSDC_K2NC")));
                                        entity.setHSDC_MTC(c.getFloat(c.getColumnIndex("HSDC_MTC")));
                                        arrVatTu.add(entity);
                                        arrMaVatTu.add(c.getString(c.getColumnIndex("MA_VTU")));
                                    } while (c.moveToNext());
                                }

                                if (spLoaiCP.getSelectedItemPosition() == 0 && position == 1) {

                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(0);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            entity.setHSDC_K1NC(c3.getFloat(c3.getColumnIndex("HSDC_K1NC")));
                                            entity.setHSDC_K2NC(c3.getFloat(c3.getColumnIndex("HSDC_K2NC")));
                                            entity.setHSDC_MTC(c3.getFloat(c3.getColumnIndex("HSDC_MTC")));
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                }

                                adapterDuToanMoi = new EsspVatTuDuToanMoiAdapter(arrVatTu, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1), tvThanhTien, spKhaiGia.getSelectedItemPosition());
                                rvDuToan.setAdapter(adapterDuToanMoi);

                                tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1),
                                        spKhaiGia.getSelectedItemPosition() == 0 ? false : true);
                            } catch (Exception ex) {
                                Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(),
                                        "Lỗi", Color.RED, "Lỗi thay đổi kiểu dự toán", Color.WHITE, "OK", Color.RED);
                            }

                            */
                            if (Integer.parseInt(SO_HUU) == 1) {
                                //TODO VINHNB sửa
                                int kg = connection.getKhaiGia(HOSO_ID);
                                if (kg == 1) {
                                    //TODO end VINHNB sửa

                                    Cursor c3 = connection.getDuToanNhtByIdHS(HOSO_ID);
                                    if (c3.moveToFirst()) {
                                        do {
                                            EsspEntityVatTuDuToan entity = new EsspEntityVatTuDuToan();
                                            entity.setID(c3.getInt(c3.getColumnIndex("VTKHTT_ID")));
                                            entity.setMA_VTU(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                            entity.setTEN_VTU(c3.getString(c3.getColumnIndex("TENVTU_MTB")));
                                            entity.setMA_LOAI_CPHI("");
                                            entity.setDON_GIA(c3.getString(c3.getColumnIndex("DON_GIA_KH")));
                                            entity.setDVI_TINH(c3.getString(c3.getColumnIndex("DVI_TINH")));
                                            entity.setDON_GIA_KH("0");
                                            entity.setTT_DON_GIA("");
                                            entity.setLOAI(0);
                                            entity.setCHUNG_LOAI(1);
                                            entity.setSO_HUU(3);
                                            entity.setSO_LUONG(c3.getFloat(c3.getColumnIndex("SO_LUONG")));
                                            entity.setTHANH_TIEN(String.valueOf(c3.getFloat(c3.getColumnIndex("DON_GIA_KH")) * c3.getFloat(c3.getColumnIndex("SO_LUONG"))));
                                            entity.setTT_TU_TUC(0);
                                            entity.setTT_THU_HOI(0);
                                            arrVatTu.add(entity);
                                            arrMaVatTu.add(new StringBuilder("VTNHT").append(c3.getInt(c3.getColumnIndex("VTKHTT_ID"))).toString());
                                        } while (c3.moveToNext());
                                    }
                                }
                            }
                            adapterDuToanMoi.updateList(arrVatTu);
                           /* if (!isKhaiGia) {
                                double tt1 = connection.sumThanhTien(HOSO_ID, SO_HUU);
                                tvThanhTien.setText(new StringBuilder("Thành tiền: ")
                                        .append(Common.formatMoney(new BigDecimal(tt1).toString())).append(" đ").toString());

                            }else {

                            }
                            tinhThanhTien(tvThanhTien, HOSO_ID, String.valueOf(spLoaiCP.getSelectedItemPosition() + 1),
                                    spKhaiGia.getSelectedItemPosition() == 0 ? false : true);*/
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi dự toán", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    class EsspHinhAnhAdapter extends RecyclerView.Adapter<EsspHinhAnhAdapter.RecyclerViewHolder> {

        List<EsspEntityHinhAnh> listData = new ArrayList<>();
        int HOSO_ID;

        public EsspHinhAnhAdapter(List<EsspEntityHinhAnh> listData, int HOSO_ID) {
            this.listData = listData;
            this.HOSO_ID = HOSO_ID;
        }

        public void updateList(List<EsspEntityHinhAnh> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<EsspEntityHinhAnh> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

//        private void applyAndAnimateRemovals(List<EsspEntityHinhAnh> newModels) {
//            for (int i = listData.size() - 1; i >= 0; i--) {
//                final EsspEntityHinhAnh model = listData.get(i);
//                if (!newModels.contains(model)) {
//                    removeItem(i);
//                }
//            }
//        }

//        private void applyAndAnimateAdditions(List<EsspEntityHinhAnh> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final EsspEntityHinhAnh model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

//        private void applyAndAnimateMovedItems(List<EsspEntityHinhAnh> newModels) {
//            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//                final EsspEntityHinhAnh model = newModels.get(toPosition);
//                final int fromPosition = listData.indexOf(model);
//                if (fromPosition >= 0 && fromPosition != toPosition) {
//                    moveItem(fromPosition, toPosition);
//                }
//            }
//        }

//        public void moveItem(int fromPosition, int toPosition) {
//            final EsspEntityHinhAnh model = listData.remove(fromPosition);
//            listData.add(toPosition, model);
//            notifyItemMoved(fromPosition, toPosition);
//        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_hinhanh, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            try {
                holder.tvGhiChu.setText(listData.get(position).getGHI_CHU());
                String imageName = listData.get(position).getTEN_ANH();
                String path = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH).append(imageName).append(".jpg").toString();
                File fImage = new File(path);
                if (fImage.exists()) {
                    Bitmap bmImage = Common.scaleDown(BitmapFactory.decodeFile(path), 200, false);
                    holder.ivHinhAnh.setImageBitmap(bmImage);
                }
            } catch (Exception ex) {
                Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi hiển thị ảnh", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, EsspEntityHinhAnh data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public void updateGhiChu(int position, String GHI_CHU) {
            listData.get(position).setGHI_CHU(GHI_CHU);
            notifyDataSetChanged();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

            public TextView tvGhiChu;
            public ImageButton ibDraw, ibGhiChu, ibXoa;
            public ImageView ivHinhAnh;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvGhiChu = (TextView) itemView.findViewById(R.id.essp_row_hinhanh_tv_ghichu);
                ivHinhAnh = (ImageView) itemView.findViewById(R.id.essp_row_hinhanh_iv_hinhanh);
                ibDraw = (ImageButton) itemView.findViewById(R.id.essp_row_hinhanh_ib_draw);
                ibGhiChu = (ImageButton) itemView.findViewById(R.id.essp_row_hinhanh_ib_ghichu);
                ibXoa = (ImageButton) itemView.findViewById(R.id.essp_row_hinhanh_ib_xoa);

                ibDraw.setOnClickListener(this);
                ibGhiChu.setOnClickListener(this);
                ivHinhAnh.setOnClickListener(this);
                ibXoa.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    String TEN_ANH = listData.get(getPosition()).getTEN_ANH();
                    switch (v.getId()) {
                        case R.id.essp_row_hinhanh_ib_draw:
                            Intent it = new Intent(EsspMainFragment.this.getActivity(), EsspDrawImageActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putInt("HOSO_ID", HOSO_ID);
                            mBundle.putString("TEN_ANH", TEN_ANH);
                            EsspCommon.IMAGE_NAME = TEN_ANH;
                            mBundle.putString("TYPE", "ANH");
                            EsspCommon.TYPE = "ANH";
                            it.putExtra("INFO", mBundle);
                            startActivity(it);
                            break;
                        case R.id.essp_row_hinhanh_ib_ghichu:
                            showDialogUpdateGhiChu(listData.get(getPosition()).getTEN_ANH(), getPosition());
                            break;
                        case R.id.essp_row_hinhanh_iv_hinhanh:
                            showDialogViewImage(listData.get(getPosition()).getTEN_ANH());
                            break;
                        case R.id.essp_row_hinhanh_ib_xoa:
                            if (!TEN_ANH.equals("")) {
                                if (connection.deleteHinhAnh(TEN_ANH) != -1) {
                                    removeItem(getPosition());
                                    String pathImage = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH)
                                            .append(TEN_ANH).append(".jpg").toString();
                                    File fImage = new File(pathImage);
                                    if (fImage.exists()) {
                                        fImage.delete();
                                    }
                                }
                            }
                            break;
                    }
                } catch (Exception ex) {
                    Toast.makeText(EsspMainFragment.this.getActivity(), "Lỗi sự kiện", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    class EsspBanVeAdapter extends ArrayAdapter<String> {

        Context context;

        public EsspBanVeAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.essp_row_banve, null);

                TextView tvTenBanVe = (TextView) convertView.findViewById(R.id.essp_row_banve_tv_tenbv);

                tvTenBanVe.setText(getItem(position));
            } catch (Exception ex) {
                ex.toString();
            }
            return convertView;
        }

    }
    //endregion

    //region Xử lý activity result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_REQUEST && resultCode != 0) {
                if (TEN_ANH != null && !TEN_ANH.isEmpty()) {
                    if (connection.insertDataHinhAnh(Integer.parseInt(TEN_ANH.split("_")[1].toString()), TEN_ANH, "0") != -1) {
                        String fileName = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH + TEN_ANH + ".jpg";
                        File imgFile = new File(fileName);
                        Bitmap myBitmap = Common.scaleDown(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 1000, false);
                        OutputStream out = new FileOutputStream(fileName);
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        out.close();
                        EsspCommon.scanFile(EsspMainFragment.this.getActivity(), new String[]{fileName});

                        arrHinhAnh = new ArrayList<>();
                        Cursor c = connection.getHinhAnhByMaKH(Integer.parseInt(TEN_ANH.split("_")[1].toString()));
                        if (c.moveToFirst()) {
                            do {
                                EsspEntityHinhAnh entity = new EsspEntityHinhAnh();
                                entity.setHOSO_ID(c.getInt(c.getColumnIndex("HOSO_ID")));
                                entity.setTEN_ANH(c.getString(c.getColumnIndex("TEN_ANH")));
                                entity.setGHI_CHU(c.getString(c.getColumnIndex("GHI_CHU")));
                                entity.setTINH_TRANG(c.getString(c.getColumnIndex("TINH_TRANG")));
                                arrHinhAnh.add(entity);
                            } while (c.moveToNext());
                        }
                        adapterHinhAnh.updateList(arrHinhAnh);
                    }
                }
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(EsspMainFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi chụp ảnh", Color.WHITE, "OK", Color.RED);
        }
    }
    //endregion

}
