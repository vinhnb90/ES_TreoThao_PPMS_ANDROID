package com.es.tungnv.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.es.tungnv.Aiball.GcsRecordActivity;
import com.es.tungnv.Gps.AppLocationService;
import com.es.tungnv.PopupMenu.MenuItem;
import com.es.tungnv.PopupMenu.PopupMenu;
import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.entity.GcsEntityKhachHang;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;
import com.es.tungnv.utils.SingleMediaScanner;
import com.es.tungnv.views.GCSMainActivity;
import com.es.tungnv.views.R;
import com.es.tungnv.zoomImage.ImageViewTouch;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsGhiChiSoFragment extends Fragment implements View.OnClickListener{

    private View rootView;

    private RecyclerView rvKhachHang;
    private EditText etNgayPmax, etPmax, etChiSo;
    private Button btCameraMTB, btGayCamera, btChucNang, btGhi;
    private Spinner spTinhTrang;
    private ImageViewTouch ivViewImage;

    private GcsSqliteConnection connection;

    private RecyclerView.LayoutManager layoutManager;
    private GcsKhachHangAdapter adapterKH = null;
    private ArrayList<GcsEntityKhachHang> arrKH;

    private int _pos_selected = 0;
    private static final int CAMERA_REQUEST = 1888;
    private String TEN_FILE;
//    private String name_cut = "";
    private ThumbnailCache mCache;
    private int _TrangThaiHienThiSo = 0;
    StringBuilder selectDate = new StringBuilder();

    private AppLocationService appLocationService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.gcs_fragment_ghichiso, null);
            initComponent(rootView);
            connection = new GcsSqliteConnection(getActivity().getApplicationContext());
            appLocationService = new AppLocationService(GcsGhiChiSoFragment.this.getActivity().getApplicationContext());
            _pos_selected = connection.getIndex(getArguments().getString("MA_QUYEN"));
            initRecycleView();
            initDataTinhTrang();

            TEN_FILE = connection.getTenFileByMaQuyen(getArguments().getString("MA_QUYEN"));
            TEN_FILE = TEN_FILE.toLowerCase().contains(".xml")?TEN_FILE.substring(0, TEN_FILE.length() - 4):TEN_FILE;
            File fFolderPhoto = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH + TEN_FILE);
            if(!fFolderPhoto.exists()){
                fFolderPhoto.mkdir();
            }

            initDataKhachHang();

            return rootView;
        } catch (Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo", Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setImage(getImageFirst());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        File fFolderPhoto = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH)
               .append(TEN_FILE).toString());
        if(fFolderPhoto.exists()){
            if(fFolderPhoto.list().length == 0){
                fFolderPhoto.delete();
            }
        }
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(this.getActivity());
        rvKhachHang.setHasFixedSize(true);
        rvKhachHang.setLayoutManager(layoutManager);
    }

    private void initDataKhachHang() {
        try{
            arrKH = new ArrayList<>();
            int stt = 0;
            Cursor cKhachHang = connection.getDataByMaQuyen(getArguments().getString("MA_QUYEN"));
            if(cKhachHang.moveToFirst()){
                do {
                    stt++;
                    GcsEntityKhachHang entityKH = new GcsEntityKhachHang();
                    entityKH.setID(cKhachHang.getInt(cKhachHang.getColumnIndex("ID")));
                    entityKH.setMA_NVGCS(cKhachHang.getString(cKhachHang.getColumnIndex("MA_NVGCS")));
                    entityKH.setMA_KHANG(cKhachHang.getString(cKhachHang.getColumnIndex("MA_KHANG")));
                    entityKH.setMA_DDO(cKhachHang.getString(cKhachHang.getColumnIndex("MA_DDO")));
                    entityKH.setMA_DVIQLY(cKhachHang.getString(cKhachHang.getColumnIndex("MA_NVIEN")));
                    entityKH.setMA_GC(cKhachHang.getString(cKhachHang.getColumnIndex("MA_GC")));
                    entityKH.setMA_QUYEN(cKhachHang.getString(cKhachHang.getColumnIndex("MA_QUYEN")));
                    entityKH.setMA_TRAM(cKhachHang.getString(cKhachHang.getColumnIndex("MA_TRAM")));
                    entityKH.setBOCSO_ID(cKhachHang.getString(cKhachHang.getColumnIndex("BOCSO_ID")));
                    entityKH.setLOAI_BCS(cKhachHang.getString(cKhachHang.getColumnIndex("LOAI_BCS")));
                    entityKH.setLOAI_CS(cKhachHang.getString(cKhachHang.getColumnIndex("LOAI_CS")));
                    entityKH.setTEN_KHANG(cKhachHang.getString(cKhachHang.getColumnIndex("TEN_KHANG")));
                    entityKH.setDIA_CHI(cKhachHang.getString(cKhachHang.getColumnIndex("DIA_CHI")));
                    entityKH.setMA_NN(cKhachHang.getString(cKhachHang.getColumnIndex("MA_NN")));
                    entityKH.setSO_HO(cKhachHang.getInt(cKhachHang.getColumnIndex("SO_HO")));
                    entityKH.setMA_CTO(cKhachHang.getString(cKhachHang.getColumnIndex("MA_CTO")));
                    entityKH.setSERY_CTO(cKhachHang.getString(cKhachHang.getColumnIndex("SERY_CTO")));
                    entityKH.setHSN(cKhachHang.getInt(cKhachHang.getColumnIndex("HSN")));
                    entityKH.setCS_CU(cKhachHang.getFloat(cKhachHang.getColumnIndex("CS_CU")));
                    entityKH.setTTR_CU(cKhachHang.getString(cKhachHang.getColumnIndex("TTR_CU")));
                    entityKH.setSL_CU(cKhachHang.getFloat(cKhachHang.getColumnIndex("SL_CU")));
                    entityKH.setSL_TTIEP(cKhachHang.getInt(cKhachHang.getColumnIndex("SL_TTIEP")));
                    entityKH.setNGAY_CU(cKhachHang.getString(cKhachHang.getColumnIndex("NGAY_CU")));
                    entityKH.setCS_MOI(cKhachHang.getFloat(cKhachHang.getColumnIndex("CS_MOI")));
                    entityKH.setTTR_MOI(cKhachHang.getString(cKhachHang.getColumnIndex("TTR_MOI")));
                    entityKH.setSL_MOI(cKhachHang.getFloat(cKhachHang.getColumnIndex("SL_MOI")));
                    entityKH.setCHUOI_GIA(cKhachHang.getString(cKhachHang.getColumnIndex("CHUOI_GIA")));
                    entityKH.setKY(cKhachHang.getInt(cKhachHang.getColumnIndex("KY")));
                    entityKH.setTHANG(cKhachHang.getInt(cKhachHang.getColumnIndex("THANG")));
                    entityKH.setNAM(cKhachHang.getInt(cKhachHang.getColumnIndex("NAM")));
                    entityKH.setNGAY_MOI(cKhachHang.getString(cKhachHang.getColumnIndex("NGAY_MOI")));
                    entityKH.setNGUOI_GCS(cKhachHang.getString(cKhachHang.getColumnIndex("NGUOI_GCS")));
                    entityKH.setSL_THAO(cKhachHang.getFloat(cKhachHang.getColumnIndex("SL_THAO")));
                    entityKH.setKIMUA_CSPK(cKhachHang.getInt(cKhachHang.getColumnIndex("KIMUA_CSPK")));
                    entityKH.setMA_COT(cKhachHang.getString(cKhachHang.getColumnIndex("MA_COT")));
                    entityKH.setCGPVTHD(cKhachHang.getString(cKhachHang.getColumnIndex("CGPVTHD")));
                    entityKH.setHTHUC_TBAO_DK(cKhachHang.getString(cKhachHang.getColumnIndex("HTHUC_TBAO_DK")));
                    entityKH.setDTHOAI_SMS(cKhachHang.getString(cKhachHang.getColumnIndex("DTHOAI_SMS")));
                    entityKH.setEMAIL(cKhachHang.getString(cKhachHang.getColumnIndex("EMAIL")));
                    entityKH.setTHOI_GIAN(cKhachHang.getString(cKhachHang.getColumnIndex("THOI_GIAN")));
                    entityKH.setX(cKhachHang.getString(cKhachHang.getColumnIndex("X")));
                    entityKH.setY(cKhachHang.getString(cKhachHang.getColumnIndex("Y")));
                    entityKH.setSO_TIEN(cKhachHang.getFloat(cKhachHang.getColumnIndex("SO_TIEN")));
                    entityKH.setHTHUC_TBAO_TH(cKhachHang.getString(cKhachHang.getColumnIndex("HTHUC_TBAO_TH")));
                    entityKH.setTENKHANG_RUTGON(cKhachHang.getString(cKhachHang.getColumnIndex("TENKHANG_RUTGON")));
                    entityKH.setTTHAI_DBO(cKhachHang.getInt(cKhachHang.getColumnIndex("TTHAI_DBO")));
                    entityKH.setDU_PHONG(cKhachHang.getString(cKhachHang.getColumnIndex("DU_PHONG")));
                    entityKH.setTEN_FILE(cKhachHang.getString(cKhachHang.getColumnIndex("TEN_FILE")));
                    entityKH.setGHICHU(cKhachHang.getString(cKhachHang.getColumnIndex("GHICHU")));
                    entityKH.setTT_KHAC(cKhachHang.getString(cKhachHang.getColumnIndex("TT_KHAC")));
                    entityKH.setID_SQLITE(cKhachHang.getInt(cKhachHang.getColumnIndex("ID_SQLITE")));
                    entityKH.setSLUONG_1(cKhachHang.getString(cKhachHang.getColumnIndex("SLUONG_1")));
                    entityKH.setSLUONG_2(cKhachHang.getString(cKhachHang.getColumnIndex("SLUONG_2")));
                    entityKH.setSLUONG_3(cKhachHang.getString(cKhachHang.getColumnIndex("SLUONG_3")));
                    entityKH.setSO_HOM(cKhachHang.getString(cKhachHang.getColumnIndex("SO_HOM")));
                    entityKH.setPMAX(cKhachHang.getFloat(cKhachHang.getColumnIndex("PMAX")));
                    entityKH.setNGAY_PMAX(cKhachHang.getString(cKhachHang.getColumnIndex("NGAY_PMAX")));
                    entityKH.setSTR_CHECK_DSOAT(cKhachHang.getString(cKhachHang.getColumnIndex("STR_CHECK_DSOAT")));
                    if(connection.countBanGhi(entityKH.getMA_CTO()) == 5){
                        if(entityKH.getLOAI_BCS().equals("SG") || entityKH.getLOAI_BCS().equals("VC")) {
                            entityKH.setSTT(stt - 3);
                        } else {
                            entityKH.setSTT(stt + 2);
                        }
                    } else {
                        entityKH.setSTT(stt);
                    }
                    if(connection.countBanGhi(entityKH.getMA_CTO()) == 5
                            && (entityKH.getLOAI_BCS().equals("SG") || entityKH.getLOAI_BCS().equals("VC"))) {
                        if(_TrangThaiHienThiSo == 0) {// Hiển thị toàn bộ
                            arrKH.add(stt - 4, entityKH);
                        } else { // Hiển thị công tơ chưa đạt
                            if(!entityKH.getSTR_CHECK_DSOAT().equals("CTO_DTU") && !entityKH.getSTR_CHECK_DSOAT().equals("CHECK")){
                                arrKH.add(entityKH);
                            }
                        }
                    } else {
                        if(_TrangThaiHienThiSo == 0) {// Hiển thị toàn bộ
                            arrKH.add(entityKH);
                        } else {// Hiển thị công tơ chưa đạt
                            if(!entityKH.getSTR_CHECK_DSOAT().equals("CTO_DTU") && !entityKH.getSTR_CHECK_DSOAT().equals("CHECK")){
                                arrKH.add(entityKH);
                            }
                        }
                    }
                } while (cKhachHang.moveToNext());
                if(_TrangThaiHienThiSo == 0) {// Hiển thị toàn bộ
                    _pos_selected = connection.getIndex(getArguments().getString("MA_QUYEN"));
                } else {
                    _pos_selected = 0;
                }
                adapterKH = new GcsKhachHangAdapter(arrKH);
                rvKhachHang.setAdapter(adapterKH);
                initChiSo(arrKH);

                rvKhachHang.scrollToPosition(_pos_selected);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi",
                    Color.RED, "Lỗi khởi tạo dữ liệu khách hàng", Color.WHITE, "OK", Color.RED);
        }
    }

    private void initDataTinhTrang() {
        ArrayAdapter<String> adapterTinhTrang = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, GcsConstantVariables.ARR_TINH_TRANG);
        spTinhTrang.setAdapter(adapterTinhTrang);
    }

    private void initComponent(View rootView) {
        rvKhachHang = (RecyclerView) rootView.findViewById(R.id.gcs_fragment_ghichiso_rvKH);
        etNgayPmax = (EditText) rootView.findViewById(R.id.gcs_fragment_ghichiso_etNgayPmax);
        etPmax = (EditText) rootView.findViewById(R.id.gcs_fragment_ghichiso_etPmax);
        etChiSo = (EditText) rootView.findViewById(R.id.gcs_fragment_ghichiso_etChiSo);
        spTinhTrang = (Spinner) rootView.findViewById(R.id.gcs_fragment_ghichiso_spTinhTrang);
        btCameraMTB = (Button) rootView.findViewById(R.id.gcs_fragment_ghichiso_btCameraMTB);
        btGayCamera = (Button) rootView.findViewById(R.id.gcs_fragment_ghichiso_btGayCamera);
        btChucNang = (Button) rootView.findViewById(R.id.gcs_fragment_ghichiso_btChucNang);
        btGhi = (Button) rootView.findViewById(R.id.gcs_fragment_ghichiso_btGhi);
        ivViewImage = (ImageViewTouch) rootView.findViewById(R.id.gcs_fragment_ghichiso_ivViewImage);

        showKeyboard(etChiSo);

        btCameraMTB.setOnClickListener(this);
        btGayCamera.setOnClickListener(this);
        btChucNang.setOnClickListener(this);
        btGhi.setOnClickListener(this);

        ivViewImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                        showDialogViewImage();
                } catch(Exception ex) {

                }
                return false;
            }
        });

        etNgayPmax.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    showDialogSelectDatetime();
                }
                return false;
            }
        });

    }

    private void showKeyboard(EditText etChiSo){
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etChiSo, InputMethodManager.SHOW_IMPLICIT);
    }

    public void searchKH(String newText){
        try {
            List<GcsEntityKhachHang> data = new ArrayList<>();
            String query = Common.removeAccent(newText.toLowerCase());
            for (GcsEntityKhachHang entity : arrKH) {
                if (Common.removeAccent(entity.getMA_COT().toLowerCase()).contains(query)
                        || Common.removeAccent(entity.getSERY_CTO().toLowerCase()).contains(query)
                        || Common.removeAccent(String.valueOf(entity.getSTT()).toLowerCase()).contains(query)
                        || Common.removeAccent(entity.getTEN_KHANG().toLowerCase()).contains(query)
                        || Common.removeAccent(entity.getMA_GC().toLowerCase()).contains(query)
                        || Common.removeAccent(entity.getMA_KHANG().toLowerCase()).contains(query)
                        || Common.removeAccent(String.valueOf(entity.getCS_CU()).toLowerCase()).contains(query)
                        || Common.removeAccent(String.valueOf(entity.getSL_CU()).toLowerCase()).contains(query)
                        || Common.removeAccent(String.valueOf(entity.getSL_MOI()).toLowerCase()).contains(query)
                        || Common.removeAccent(entity.getDIA_CHI().toLowerCase()).contains(query)) {
                    data.add(entity);
                }
            }
            adapterKH.updateList(data);
        } catch (Exception ex) {
            Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Lỗi tìm kiếm", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == CAMERA_REQUEST && resultCode != 0) {
                String fileName = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH)
                        .append(TEN_FILE).append("/").append(GcsCommon.getPhotoName(adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                        adapterKH.listData.get(_pos_selected).getMA_CTO(),
                        adapterKH.listData.get(_pos_selected).getNAM(),
                        adapterKH.listData.get(_pos_selected).getTHANG(),
                        adapterKH.listData.get(_pos_selected).getKY(),
                        adapterKH.listData.get(_pos_selected).getMA_DDO(),
                        adapterKH.listData.get(_pos_selected).getLOAI_BCS())).toString();

                saveImage(GcsGhiChiSoFragment.this.getActivity(), fileName);
                setImage(fileName);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi chụp ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.gcs_fragment_ghichiso_btCameraMTB:
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    final String fileName = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH)
                            .append(TEN_FILE).append("/").append(GcsCommon.getPhotoName(adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                            adapterKH.listData.get(_pos_selected).getMA_CTO(),
                            adapterKH.listData.get(_pos_selected).getNAM(),
                            adapterKH.listData.get(_pos_selected).getTHANG(),
                            adapterKH.listData.get(_pos_selected).getKY(),
                            adapterKH.listData.get(_pos_selected).getMA_DDO(),
                            adapterKH.listData.get(_pos_selected).getLOAI_BCS())).toString();
                    File file = new File(fileName);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    break;
                case R.id.gcs_fragment_ghichiso_btGayCamera:
                    Intent it = new Intent(GcsGhiChiSoFragment.this.getActivity(), GcsRecordActivity.class);
                    it.putExtra("MA_QUYEN", adapterKH.listData.get(_pos_selected).getMA_QUYEN());
                    it.putExtra("LOAI_BCS", adapterKH.listData.get(_pos_selected).getLOAI_BCS());
                    it.putExtra("MA_CTO", adapterKH.listData.get(_pos_selected).getMA_CTO());
                    startActivity(it);
                    break;
                case R.id.gcs_fragment_ghichiso_btChucNang:
                    showPopupMenu(v);
                    break;
                case R.id.gcs_fragment_ghichiso_btGhi:
                    ghiChiSo();
                    break;
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi",
                    Color.RED, "Lỗi sự kiện click", Color.WHITE, "OK", Color.RED);
        }
    }

    private void ghiChiSo() {
        try{
            // Get values
            String PMAX = etPmax.getText().toString().trim();
            String NGAY_PMAX = etNgayPmax.getText().toString().trim();
            String TTR_MOI = GcsConstantVariables.ARR_TINH_TRANG_SYMBOL[spTinhTrang.getSelectedItemPosition()];

            // Clear error
            etChiSo.setError(null);
            etPmax.setError(null);
            etNgayPmax.setError(null);

            // Check values
            if(TextUtils.isEmpty(etChiSo.getText().toString().trim()) && (spTinhTrang.getSelectedItemPosition() == 0 ||
                    ("Q,G,T,C,K,H").contains(TTR_MOI))){
                etChiSo.setError("Bạn chưa nhập chỉ số");
                etChiSo.requestFocus();
                return;
            }

            if(("BT,CD").contains(adapterKH.listData.get(_pos_selected).getLOAI_BCS())){
                if(TextUtils.isEmpty(PMAX)) {
//                    etPmax.setError("Bạn chưa nhập chỉ số Pmax");
//                    etPmax.requestFocus();
//                    return;
                    Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Bạn chưa nhập Pmax", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(NGAY_PMAX)) {
//                    etNgayPmax.setError("Bạn chưa nhập chỉ số Ngày Pmax");
//                    etNgayPmax.requestFocus();
//                    return;
                    Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Bạn chưa nhập ngày Pmax", Toast.LENGTH_LONG).show();
                }
            }

            // get data
            GcsEntityKhachHang entity = adapterKH.listData.get(_pos_selected);
            int ID_SQLITE = entity.getID_SQLITE();
            float CS_CU = entity.getCS_CU();
            float CS_MOI = Float.parseFloat(etChiSo.getText().toString().trim());
            float SL_CU = entity.getSL_CU();
            float SL_MOI = 0f;
            float SL_THAO = entity.getSL_THAO();
            float HSN = entity.getHSN();
            String LOAI_BCS = entity.getLOAI_BCS();
            String SO_CTO = entity.getSERY_CTO();

            if(!TextUtils.isEmpty(TTR_MOI) && ("U,V,M,L,X,D").contains(TTR_MOI)){
                CS_MOI = CS_CU;
                SL_MOI = 0f;
                luuChiSo("0", String.valueOf(ID_SQLITE), CS_MOI, SL_MOI, TTR_MOI, LOAI_BCS);
            } else if(TTR_MOI.equals("Q")) {
                final int ID_SQLITE1 = ID_SQLITE;
                final float SL_CU1 = SL_CU;
                final float SL_THAO1 = SL_THAO;
                final Float SL_MOI1 = TinhSanLuong(CS_CU, CS_MOI, HSN, TTR_MOI);
                final float CS_MOI1 = CS_MOI;
                final String TTR_MOI1 = TTR_MOI;
                final String LOAI_BCS1 = LOAI_BCS;
                final String SO_CTO1 = SO_CTO;

                new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Thông báo").setMessage("Công tơ qua vòng. \nBạn có muốn lưu?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CanhBaoChenhLechSL(String.valueOf(ID_SQLITE1), SL_CU1, SL_THAO1, SL_MOI1, CS_MOI1, TTR_MOI1, LOAI_BCS1, SO_CTO1);
                            }
                        }).setNegativeButton("No", null)
                        .show().getWindow().setGravity(Gravity.BOTTOM);
            } else if(("C,K,T,H,G").contains(TTR_MOI) || TextUtils.isEmpty(TTR_MOI)) {
                if(CS_MOI >= CS_CU){
                    SL_MOI = TinhSanLuong(CS_CU, CS_MOI, HSN, TTR_MOI);
                    CanhBaoChenhLechSL(String.valueOf(ID_SQLITE), SL_CU, SL_THAO, SL_MOI, CS_MOI, TTR_MOI, LOAI_BCS, SO_CTO);
                } else {
                    Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Thông báo", Color.WHITE,
                            "Chỉ số mới nhỏ hơn chỉ số cũ\\nBạn phải chọn trạng thái mới có thể ghi", Color.WHITE, "OK", Color.WHITE);
                    return;
                }
            }

        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi",
                    Color.RED, "Lỗi ghi chỉ số", Color.WHITE, "OK", Color.RED);
        }
    }

    private void CanhBaoChenhLechSL(String ID_SQLITE, Float SL_CU, Float SL_THAO, Float SL_MOI, Float CS_MOI, String tinh_trang_moi, String LOAI_BCS, String SO_CTO) {
        try{
            float SLChenhLech = 0;
            if (SL_CU > 0) {
                if(SL_MOI + SL_THAO > SL_CU){
                    SLChenhLech = (float) (SL_MOI - SL_CU + SL_THAO) / (float) SL_CU;
                } else {
                    SLChenhLech = (float) (SL_CU - SL_MOI - SL_THAO) / (float) SL_CU;
                }
            }

            if (GcsCommon.cfgInfo.isWarningEnable3()) {
                if(SL_CU >= 400){
                    if (SL_MOI + SL_THAO > SL_CU && SLChenhLech*100 >= 200) {
                        CreateDialogCanhBaoXacNhan(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng vượt quá ").append(SLChenhLech*100)
                                .append("%.\nYêu cầu ký xác nhận với khách hàng").append(" \nBạn có muốn lưu?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else {
                        CanhBaoChenhLechTongSLCto3Pha("0", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else {
                    CanhBaoChenhLechTongSLCto3Pha("0", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                }
            } else if (GcsCommon.cfgInfo.isWarningEnable()) {// Cảnh báo theo %
                if (SL_MOI + SL_THAO > SL_CU
                        && SLChenhLech*100 >= ((float) GcsCommon.cfgInfo.getVuotDinhMuc())) {
                    if(SLChenhLech*100 < 50){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới vượt quá ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getVuotDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_MOI - SL_CU + SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 50 && SLChenhLech*100 < 100){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới vượt quá ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getVuotDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_MOI - SL_CU + SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 100){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới vượt quá ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getVuotDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_MOI - SL_CU + SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else if (SL_MOI + SL_THAO < SL_CU
                        && SLChenhLech*100 >= ((float) GcsCommon.cfgInfo.getDuoiDinhMuc())) {
                    if(SLChenhLech*100 < 50){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới dưới ngưỡng định mức ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getDuoiDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 50 && SLChenhLech*100 < 100){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới dưới ngưỡng định mức ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getDuoiDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 100){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới dưới ngưỡng định mức ")
                                .append(GcsCommon.round(SLChenhLech*100, 2)).append("% so với mức ").append(GcsCommon.cfgInfo.getDuoiDinhMuc())
                                .append("% đã đặt trong cấu hình tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?")
                                .toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else {
                    CanhBaoChenhLechTongSLCto3Pha("0", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                }
            } else if (GcsCommon.cfgInfo.isWarningEnable2()) {
                if (SL_MOI + SL_THAO >= SL_CU) {
                    SLChenhLech = SL_MOI - SL_CU + SL_THAO;
                } else {
                    SLChenhLech = SL_CU - SL_MOI - SL_THAO;
                }
                String chenhLechTren = "" + Math.abs((SLChenhLech - (float) GcsCommon.cfgInfo.getVuotDinhMuc2()));
                String chenhLechDuoi = "" + Math.abs((SLChenhLech - (float) GcsCommon.cfgInfo.getDuoiDinhMuc2()));

                if (SL_MOI + SL_THAO > SL_CU
                        && SLChenhLech > (float) GcsCommon.cfgInfo.getVuotDinhMuc2()) {
                    if(Float.parseFloat(chenhLechTren) < 100){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới vượt quá định mức cho phép. ")
                                .append(chenhLechTren).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(Float.parseFloat(chenhLechTren) >= 100 && Float.parseFloat(chenhLechTren) < 150){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới vượt quá định mức cho phép. ")
                                .append(chenhLechTren).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(Float.parseFloat(chenhLechTren) >= 150){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới vượt quá định mức cho phép. ")
                                .append(chenhLechTren).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else if (SL_MOI + SL_THAO < SL_CU && SLChenhLech > (float) GcsCommon.cfgInfo.getDuoiDinhMuc2()) {
                    if(Float.parseFloat(chenhLechDuoi) < 100){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới dưới định mức cho phép. ")
                                .append(chenhLechDuoi).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(Float.parseFloat(chenhLechDuoi) >= 100 && Float.parseFloat(chenhLechDuoi) < 150){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới dưới định mức cho phép. ")
                                .append(chenhLechDuoi).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(Float.parseFloat(chenhLechDuoi) >= 150){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới dưới định mức cho phép. ")
                                .append(chenhLechDuoi).append(" kw\nBạn có muốn lưu ?").toString(), ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else {
                    CanhBaoChenhLechTongSLCto3Pha("0", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                }
            } else {
                if (SL_MOI + SL_THAO > SL_CU
                        && SLChenhLech*100 >= 30) {
                    if(SLChenhLech*100 < 50){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới vượt quá ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(Math.abs(SL_MOI - SL_CU) + SL_THAO).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 50 && SLChenhLech*100 < 100){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới vượt quá ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(Math.abs(SL_MOI - SL_CU) + SL_THAO).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 100){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới vượt quá ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(Math.abs(SL_MOI - SL_CU) + SL_THAO).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else if (SL_MOI + SL_THAO < SL_CU
                        && SLChenhLech*100 >= ((float) GcsCommon.cfgInfo.getDuoiDinhMuc())) {
                    if(SLChenhLech*100 < 50){
                        CreateDialogCanhBao(Color.parseColor("#005789"), new StringBuilder("Sản lượng mới dưới ngưỡng ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 50 && SLChenhLech*100 < 100){
                        CreateDialogCanhBao(Color.parseColor("#FFCC00"), new StringBuilder("Sản lượng mới dưới ngưỡng ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    } else if(SLChenhLech*100 >= 100){
                        CreateDialogCanhBao(Color.parseColor("#FF0000"), new StringBuilder("Sản lượng mới dưới ngưỡng ").append(SLChenhLech*100).append("% so với mức ")
                                .append("30% mặc định tương ứng với ").append(String.valueOf(SL_CU - SL_MOI - SL_THAO)).append("kw/h\nBạn có muốn lưu ?").toString(),
                                ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    }
                } else {
                    CanhBaoChenhLechTongSLCto3Pha("0", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    "Lỗi cảnh báo", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void CreateDialogCanhBao(int color, String canhbao,
                                     final String ID_SQLITE, final Float CS_MOI, final Float SL_MOI,
                                     final String tinh_trang_moi, final String LOAI_BCS,
                                     final String SO_CTO) {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_canhbao);
            dialog.getWindow().setLayout(
                    android.app.ActionBar.LayoutParams.MATCH_PARENT,
                    android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
            LinearLayout lnTittle = (LinearLayout) dialog.findViewById(R.id.lnTitle);
            TextView tvCanhBao = (TextView) dialog.findViewById(R.id.tvCanhBao);

            tvCanhBao.setText(canhbao);
            lnTittle.setBackgroundColor(color);

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CanhBaoChenhLechTongSLCto3Pha("1", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    "Lỗi cảnh báo", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void CreateDialogCanhBaoXacNhan(int color, String canhbao,
                                            final String ID_SQLITE, final Float CS_MOI, final Float SL_MOI,
                                            final String tinh_trang_moi, final String LOAI_BCS,
                                            final String SO_CTO) {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_canhbao_xacnhan);
            dialog.getWindow().setLayout(
                    android.app.ActionBar.LayoutParams.MATCH_PARENT,
                    android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setCanceledOnTouchOutside(false);

            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
            LinearLayout lnTittle = (LinearLayout) dialog.findViewById(R.id.lnTitle);
            TextView tvCanhBao = (TextView) dialog.findViewById(R.id.tvCanhBao);

            tvCanhBao.setText(canhbao);
            lnTittle.setBackgroundColor(color);

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CanhBaoChenhLechTongSLCto3Pha("1", ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS, SO_CTO);
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    "Lỗi cảnh báo", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void CanhBaoChenhLechTongSLCto3Pha(final String tinh_trang_qua_sl, final String ID_SQLITE, final float CS_MOI, final float SL_MOI, final String tinh_trang_moi, final String LOAI_BCS, String SO_CTO) {
        try{
            int sai_so_cto_tong = 2;
            int dem = 0;
            if(LOAI_BCS.equals("KT")){
                luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
            } else {
                float sl_sg = 0f;
                float sl_tong = 0f;
                Cursor c = connection.getDataSameSoCto(SO_CTO);
                if(c.moveToFirst()){
                    do{
                        if(c.getString(9).equals("SG")){
                            if(LOAI_BCS.equals("SG")){
                                sl_sg = SL_MOI;
                            } else {
                                sl_sg = Float.parseFloat(c.getString(25));
                            }
                            if(Float.parseFloat(c.getString(25)) != 0){
                                dem++;
                            }
                        } else if(c.getString(9).equals("BT")){
                            if(LOAI_BCS.equals("BT")){
                                sl_tong += SL_MOI;
                            } else {
                                sl_tong += Float.parseFloat(c.getString(25));
                            }
                            if(Float.parseFloat(c.getString(25)) != 0){
                                dem++;
                            }
                        } else if(c.getString(9).equals("CD")){
                            if(LOAI_BCS.equals("CD")){
                                sl_tong += SL_MOI;
                            } else {
                                sl_tong += Float.parseFloat(c.getString(25));
                            }
                            if(Float.parseFloat(c.getString(25)) != 0){
                                dem++;
                            }
                        } else if(c.getString(9).equals("TD")){
                            if(LOAI_BCS.equals("TD")){
                                sl_tong += SL_MOI;
                            } else {
                                sl_tong += Float.parseFloat(c.getString(25));
                            }
                            if(Float.parseFloat(c.getString(25)) != 0){
                                dem++;
                            }
                        }
                    } while(c.moveToNext());
                    if(dem < 3){
                        luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                    } else {
                        if(dem == 4){
                            if(Math.abs(sl_sg - sl_tong) > sai_so_cto_tong){
                                new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity())
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Thông báo")
                                        .setMessage("Tổng sản lượng công tơ 3 pha chênh lệch nhiều. \nBạn có muốn lưu ?")
                                        .setPositiveButton("Đồng ý",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                                                    }

                                                }).setNegativeButton("Không", null).show()
                                        .getWindow().setGravity(Gravity.BOTTOM);
                            } else {
                                luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                            }
                        } else {
                            if(connection.getSlmoiById(ID_SQLITE) == 0){
                                if(Math.abs(sl_sg - sl_tong) > sai_so_cto_tong){
                                    new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Thông báo")
                                            .setMessage("Tổng sản lượng công tơ 3 pha chênh lệch nhiều. \nBạn có muốn lưu ?")
                                            .setPositiveButton("Đồng ý",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which) {
                                                            luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                                                        }

                                                    }).setNegativeButton("Không", null).show()
                                            .getWindow().setGravity(Gravity.BOTTOM);
                                } else {
                                    luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                                }
                            } else {
                                luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                            }
                        }
                    }
                } else {
                    if(LOAI_BCS.equals("VC"))
                        luuChiSo(tinh_trang_qua_sl, ID_SQLITE, CS_MOI, SL_MOI, tinh_trang_moi, LOAI_BCS);
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Thông báo", Color.WHITE,
                    "Lỗi cảnh báo chênh lệch công tơ 3 pha", Color.WHITE, "OK", Color.WHITE);
        }
    }

    private Float TinhSanLuong(Float CS_Cu, Float CS_Moi, Float HSN, String TT_Moi) {
        Float SL = 0F;
        if (CS_Moi < CS_Cu || (TT_Moi != null && TT_Moi.equals("Q"))) {
            SL = Float.parseFloat(((Math.pow(10, (CS_Cu.intValue() + "").length()) - CS_Cu + CS_Moi) * HSN) + "");
            SL = Float.parseFloat(String.format(Locale.ENGLISH, "%.3f", SL));
        } else if (CS_Moi > CS_Cu) {
            float hs = Float.parseFloat(String.format(Locale.ENGLISH, "%.3f", CS_Moi - CS_Cu));
            SL = hs * HSN;
            SL = Float.parseFloat(String.format(Locale.ENGLISH, "%.3f", SL));
        } else {
            SL = 0F;
        }

        return SL;
    }

    private void luuChiSo(final String TT_KHAC, final String ID_SQLITE, float CS_MOI, float SL_MOI, String TTR_MOI, final String LOAI_BCS) {
        try {
            // Lấy tọa độ
            double latitude = 0;
            double longitude = 0;

            Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

            if (gpsLocation != null) {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
            } else {
                Toast.makeText(getActivity(), "Không lấy được tọa độ", Toast.LENGTH_LONG).show();
            }

            // Lấy thời gian ghi
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            final String TTHAI_DBO = "1";
            String PMAX = etPmax.getText().toString();
            String NGAY_PMAX = etNgayPmax.getText().toString();
            String STR_CHECK_DSOAT = "CHUA_DOI_SOAT";
            Bitmap bm;
            if (connection.getDSoat(ID_SQLITE).equals("CTO_DTU")) {
                STR_CHECK_DSOAT = "CTO_DTU";
            }

            if (LOAI_BCS.equals("CD")) {
                if(!TextUtils.isEmpty(PMAX) && !TextUtils.isEmpty(NGAY_PMAX)) {
                    changePmax(PMAX, NGAY_PMAX, "" + (Float.parseFloat(ID_SQLITE) - 1));
                } else {
                    PMAX = "0";
                    NGAY_PMAX = "";
                }
            }
            long result = connection.updateChiSo(ID_SQLITE, CS_MOI, TTR_MOI, SL_MOI, currentDateandTime,
                    String.valueOf(latitude), String.valueOf(longitude), TTHAI_DBO, TT_KHAC, PMAX, NGAY_PMAX, STR_CHECK_DSOAT);
            if (result != -1) {
                // Sua anh
                final String fileName = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH
                        + TEN_FILE + "/" + GcsCommon.getPhotoName(
                        adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                        adapterKH.listData.get(_pos_selected).getMA_CTO(),
                        adapterKH.listData.get(_pos_selected).getNAM(),
                        adapterKH.listData.get(_pos_selected).getTHANG(),
                        adapterKH.listData.get(_pos_selected).getKY(),
                        adapterKH.listData.get(_pos_selected).getMA_DDO(),
                        adapterKH.listData.get(_pos_selected).getLOAI_BCS());
                bm = createBitMap(fileName);
                if (bm != null) {
                    bm = drawCSMoiToBitmap(GcsGhiChiSoFragment.this.getActivity(), bm, String.valueOf(CS_MOI));
                    if (saveImageToFile(bm, fileName)) {
                    }
                }

                reLoadData(CS_MOI, TTR_MOI, SL_MOI, Float.parseFloat(TextUtils.isEmpty(PMAX)?"0":PMAX), NGAY_PMAX, currentDateandTime);

                if (_pos_selected < adapterKH.getItemCount() - 1) {
                    _pos_selected++;
                }
                adapterKH.notifyDataSetChanged();
                try {
//                if (.getText().toString().equals("")) {
                    connection.deleteDataIndex(adapterKH.listData.get(_pos_selected).getMA_QUYEN());
                    connection.insertDataIndex(adapterKH.listData.get(_pos_selected).getMA_QUYEN(), _pos_selected);
//                }
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi",
                            Color.RED, "Không lưu được vị trí", Color.WHITE, "OK", Color.RED);
                }

                initChiSo(adapterKH.listData);
                String fileName2 = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH
                        + TEN_FILE + "/" + GcsCommon.getPhotoName(
                        adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                        adapterKH.listData.get(_pos_selected).getMA_CTO(),
                        adapterKH.listData.get(_pos_selected).getNAM(),
                        adapterKH.listData.get(_pos_selected).getTHANG(),
                        adapterKH.listData.get(_pos_selected).getKY(),
                        adapterKH.listData.get(_pos_selected).getMA_DDO(),
                        adapterKH.listData.get(_pos_selected).getLOAI_BCS());
                setImage(fileName2);

                setTitle();

                rvKhachHang.smoothScrollToPosition(_pos_selected);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi",
                    Color.RED, "Lỗi ghi chỉ số", Color.WHITE, "OK", Color.RED);
        }
    }

    private void setTitle(){
        try {
            int daghi = GcsCommon.getDaGhi(connection.getCSoAndTTR(getArguments().getString("MA_QUYEN")));
            int chuaghi = connection.getSoLuongBanGhi(getArguments().getString("MA_QUYEN"));
            ((GCSMainActivity) getActivity()).getSupportActionBar().setTitle(new StringBuilder(getArguments().getString("MA_QUYEN"))
                    .append(" (").append(daghi).append("/").append(chuaghi).append(")").toString());
        } catch(Exception ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi",
                    Color.RED, "Lỗi cập nhật tiêu đề", Color.WHITE, "OK", Color.RED);
        }
    }

    private void changePmax(String H1_PMAX, String NGAY_H1_PMAX, String ID_SQLITE){
        Cursor c = connection.getPmax(ID_SQLITE);
        if(c.moveToFirst()) {
            String PMAX = c.getString(0);
            if (Float.parseFloat(PMAX) <= 40) {
                if (Float.parseFloat(H1_PMAX) > 40) {
                    connection.updatePmax(ID_SQLITE, H1_PMAX, NGAY_H1_PMAX);
                } else {
                    if (Float.parseFloat(H1_PMAX) > Float.parseFloat(PMAX)) {
                        connection.updatePmax(ID_SQLITE, H1_PMAX, NGAY_H1_PMAX);
                    }
                }
            }
        }
    }

    public Bitmap createBitMap(String path){
        File file = new File(path);
        Bitmap bitmap = null;
        if(file.exists())
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }

    public void saveImage(Context ctx, String fileName){
        BufferedOutputStream bos = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

            if(bitmap != null) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight();
                if(w < h){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Common.scaleDown(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), 640, true);
                } else {
                    bitmap = Common.scaleDown(bitmap, 640, true);
                }

                bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bitmap = drawTextToBitmap(bitmap, adapterKH.listData.get(_pos_selected).getTEN_KHANG(),
                        adapterKH.listData.get(_pos_selected).getMA_DDO(), adapterKH.listData.get(_pos_selected).getSERY_CTO(),
                        "" + adapterKH.listData.get(_pos_selected).getCS_MOI(), adapterKH.listData.get(_pos_selected).getCHUOI_GIA());
                bos.write(Common.encodeTobase64Byte(bitmap));
                bos.close();
                new SingleMediaScanner(ctx, new File(fileName));
            }
        } catch (IOException ex) {
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED, "Lỗi lưu ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    private boolean saveImageToFile(Bitmap bmp_result, String fileName){
        try{
            if(bmp_result != null){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp_result.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File f = new File(fileName);
                if(f.exists()){
                    f.delete();
                }
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                new SingleMediaScanner(GcsGhiChiSoFragment.this.getActivity(), new File(fileName));
                fo.close();
                return true;
            } else {
                return false;
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi lưu ảnh", Color.WHITE, "OK", Color.RED);
            return false;
        }
    }

    private String getImageFirst(){
        String fileName = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH)
                .append(TEN_FILE).append("/").append(GcsCommon.getPhotoName(adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                adapterKH.listData.get(_pos_selected).getMA_CTO(),
                adapterKH.listData.get(_pos_selected).getNAM(),
                adapterKH.listData.get(_pos_selected).getTHANG(),
                adapterKH.listData.get(_pos_selected).getKY(),
                adapterKH.listData.get(_pos_selected).getMA_DDO(),
                adapterKH.listData.get(_pos_selected).getLOAI_BCS())).toString();
        return fileName;
    }

    private void setImage(String fileName) {
        try{
            if(new File(fileName).exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
                bitmap = Common.scaleDown(bitmap, 300, true);
                ivViewImage.setImageBitmapReset(bitmap, 0, true);
                ivViewImage.setEnabled(true);
            } else {
                ivViewImage.setImageBitmapReset(null, 0, true);
                ivViewImage.setEnabled(false);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    public Bitmap drawTextToBitmap(Bitmap bitmap, String name, String ma_dd, String so_cto, String cs_moi, String chuoi_gia) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

//		Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(bitmap.getHeight()/20);

        Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_rec.setColor(Color.BLACK);

        // Xử lý cắt chuỗi tên xuống dòng
        String[] arrName = name.trim().split(" ");
        String name1 = "";
        String name2 = "";
        for (String sName : arrName) {
            Rect bounds_cut = new Rect();
            paint.getTextBounds(name1, 0, name1.length(), bounds_cut);
            if(bounds_cut.width() < 3*bitmap.getWidth()/4){
                name1 += sName + " ";
            } else {
                name2 += sName + " ";
            }
        }
//		name = name1.trim() + "\n" + name2.trim();
        name = name1.trim();
//        name_cut = name1.trim() + "\n" + name2.trim();

        Rect bounds1 = new Rect();
        paint.getTextBounds(name, 0, name.length(), bounds1);
        int x1 = 10;
        int y1 = bounds1.height();
        int y1_2 = 2*bounds1.height();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + 5 * bounds1.height() + 30, conf); // this creates a MUTABLE bitmap
        Canvas c = new Canvas(bmp);
        // Vẽ tên
        c.drawRect(0, 0, bmp.getWidth(), 2 * bounds1.height() + 15, paint_rec);
        c.drawRect(0, bmp.getHeight() - 2 * bounds1.height() - 15, bmp.getWidth(), bmp.getHeight(), paint_rec);
        c.drawText(name, x1, y1, paint);
        if(!name2.equals(""))
            c.drawText(name2, x1, y1_2, paint);
        // Vẽ ảnh lên khung mới có viền đen
        c.drawBitmap(bitmap, 0, 3 * bounds1.height() + 15, paint_rec);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        Rect bounds0 = new Rect();
        paint.getTextBounds(currentDateandTime, 0, currentDateandTime.length(), bounds0);
        c.drawRect(0, 2 * bounds1.height() + 15, bounds0.width() + 15, 3*bounds1.height() + 15, paint_rec);

        int x5 = 10;
        int y5 = 2 * bounds1.height() + 30;
        c.drawText(currentDateandTime, x5, y5, paint);

        Rect bounds6 = new Rect();
        paint.getTextBounds(chuoi_gia, 0, chuoi_gia.length(), bounds6);
        int x6 = 5;//bmp.getWidth() - 10 - bounds6.width()
        int y6 = bmp.getHeight() - 20 - bounds6.height();
        c.drawText(chuoi_gia, x6, y6, paint);

        Rect bounds2 = new Rect();
        paint.getTextBounds(ma_dd, 0, ma_dd.length(), bounds2);
        int x2 = 10;
        int y2 = bmp.getHeight() - 10;
        c.drawText(ma_dd, x2, y2, paint);

        Rect bounds3 = new Rect();
        paint.getTextBounds(so_cto, 0, so_cto.length(), bounds3);
        int x3 = bmp.getWidth() - 10 - bounds3.width();
        int y3 = bmp.getHeight() - 10;
        c.drawText(so_cto, x3, y3, paint);

        // Vẽ CS_MOI lên góc trên bên phải
        Rect bounds4 = new Rect();
        paint.getTextBounds(cs_moi, 0, cs_moi.length(), bounds4);
        int x4 = bmp.getWidth() - 10 - bounds4.width();
        int y4 = bounds4.height() + 10;
        c.drawText(cs_moi, x4, y4, paint);

        return bmp;
    }

    public Bitmap drawCSMoiToBitmap(Context gContext, Bitmap bitmap, String cs_moi) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(bitmap.getHeight()/25);

        Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_rec.setColor(Color.BLACK);

        Rect bounds_width = new Rect();
        paint.getTextBounds("000000", 0, "000000".length(), bounds_width);

        Rect bounds = new Rect();
        paint.getTextBounds(cs_moi, 0, cs_moi.length(), bounds);
        int x = bitmap.getWidth() - 10 - bounds.width();
        int y = bounds.height() + 10;
        canvas.drawRect(bitmap.getWidth() - bounds_width.width(), 0, bitmap.getWidth(), bounds.height() + 15, paint_rec);
        canvas.drawText(cs_moi, x, y, paint);

        return bitmap;
    }

    private void initChiSo(List<GcsEntityKhachHang> listData) {
        try {
            if(listData.size() > 0) {
                etPmax.setError(null);
                etNgayPmax.setError(null);
                etChiSo.setError(null);

                if (listData.get(_pos_selected).getLOAI_BCS().equals("BT")) {
                    etNgayPmax.setEnabled(true);
                    etPmax.setEnabled(true);
                    etPmax.setHint("Pmax");
                    etPmax.setText(String.valueOf(listData.get(_pos_selected).getPMAX()));
                    etNgayPmax.setText(listData.get(_pos_selected).getNGAY_PMAX());
                } else if (listData.get(_pos_selected).getLOAI_BCS().equals("CD")) {
                    etNgayPmax.setEnabled(true);
                    etPmax.setEnabled(true);
                    etPmax.setHint("H1Pmax");
                    etPmax.setText("");
                    etNgayPmax.setText("");
                } else {
                    etNgayPmax.setEnabled(false);
                    etPmax.setEnabled(false);
                    etPmax.setText("");
                    etNgayPmax.setText("");
                }
                etChiSo.setText(String.valueOf(listData.get(_pos_selected).getCS_MOI()));
                etChiSo.requestFocus();
                etChiSo.selectAll();

                int pos_tinhtrang = Arrays.asList(GcsConstantVariables.ARR_TINH_TRANG_SYMBOL).
                        indexOf(listData.get(_pos_selected).getTTR_MOI());
                spTinhTrang.setSelection(pos_tinhtrang);
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Lỗi", Color.RED,
                    "Lỗi lấy chỉ số", Color.WHITE, "OK", Color.RED);
        }
    }

    private void reLoadData(float CS_MOI, String TTR_MOI, float SL_MOI, float PMAX, String NGAY_PMAX, String THOI_GIAN) {
        adapterKH.listData.get(_pos_selected).setCS_MOI(CS_MOI);
        adapterKH.listData.get(_pos_selected).setTTR_MOI(TTR_MOI);
        adapterKH.listData.get(_pos_selected).setSL_MOI(SL_MOI);
        adapterKH.listData.get(_pos_selected).setPMAX(PMAX);
        adapterKH.listData.get(_pos_selected).setNGAY_PMAX(NGAY_PMAX);
        adapterKH.listData.get(_pos_selected).setTHOI_GIAN(THOI_GIAN);
    }

    private void showDialogSelectDatetime() {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_select_dateimte);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);

            final CalendarView cvDate = (CalendarView) dialog.findViewById(R.id.gcs_dialog_select_datetime_cvDate);
            final TimePicker tpTime = (TimePicker) dialog.findViewById(R.id.gcs_dialog_select_datetime_tpTime);
            Button btOK = (Button) dialog.findViewById(R.id.gcs_dialog_select_datetime_btOK);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            selectDate.setLength(0);
            selectDate.append(sdf.format(new Date(cvDate.getDate())));

            cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    selectDate.setLength(0);
                    selectDate.append(dayOfMonth).append("/").append(month).append("/").append(year);
                }
            });

            btOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        String selectedTime = new StringBuilder(tpTime.getCurrentHour()).append(":").append(tpTime.getCurrentMinute()).toString();
                        etNgayPmax.setText(selectDate.append(" ").append(selectedTime).toString());
                    } catch(Exception ex) {
                        Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi chọn ngày", Color.WHITE, "OK", Color.RED);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogMenu(final GcsEntityKhachHang entity) {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_menu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);

            TextView tvTongSLHC = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_tongSLHC);
            TextView tvTongSLVC = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_tongSLVC);
            TextView tvTongTTBT = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_tongTTBT);
            TextView tvTenKH = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_tenKH);
            TextView tvDiaChi = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_diaChi);
            TextView tvMaKH = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_maKH);
            TextView tvMaDDo = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_maDDo);
            TextView tvMaGC = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_maGC);
            TextView tvMaTram = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_maTram);
            TextView tvSoHo = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_soHo);
            TextView tvHSN = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_HSN);
            TextView tvSLThao = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_SLThao);
            TextView tvPmax = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_Pmax);
            TextView tvNgayPmax = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_NgayPmax);
            TextView tvChuoiGia = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_chuoiGia);
            final TextView tvGhiChu = (TextView) dialog.findViewById(R.id.gcs_dialog_menu_ghichu);
            CheckBox cbCtoDtu = (CheckBox) dialog.findViewById(R.id.gcs_dialog_menu_cbCtoDtu);
            ListView lvMenu = (ListView) dialog.findViewById(R.id.gcs_dialog_menu_lvMenu);

            // init data
            tvTongSLHC.setText(String.valueOf(connection.sumSLHC(entity.getMA_QUYEN())));
            tvTongSLVC.setText(String.valueOf(connection.sumSLVC(entity.getMA_QUYEN())));
            tvTongTTBT.setText(String.valueOf(connection.sumTTBT(entity.getMA_QUYEN())));

            tvTenKH.setText(entity.getTEN_KHANG());
            tvDiaChi.setText(entity.getDIA_CHI());
            tvMaKH.setText(entity.getMA_KHANG());
            tvMaDDo.setText(entity.getMA_DDO());
            tvMaGC.setText(entity.getMA_GC());
            tvMaTram.setText(entity.getMA_TRAM());
            tvSoHo.setText(String.valueOf(entity.getSO_HO()));
            tvHSN.setText(String.valueOf(entity.getHSN()));
            tvSLThao.setText(String.valueOf(entity.getSL_THAO()));
            tvPmax.setText(String.valueOf(entity.getPMAX()));
            tvNgayPmax.setText(entity.getNGAY_PMAX());
            tvChuoiGia.setText(entity.getCHUOI_GIA());
            tvGhiChu.setText(entity.getGHICHU());
            if(entity.getSTR_CHECK_DSOAT().equals("CTO_DTU")){
                cbCtoDtu.setChecked(true);
            } else {
                cbCtoDtu.setChecked(false);
            }

            cbCtoDtu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        connection.updateDSOAT(String.valueOf(entity.getSERY_CTO()), "CTO_DTU");
                        if(connection.countBanGhi(entity.getMA_CTO()) == 1){
                            adapterKH.listData.get(_pos_selected).setSTR_CHECK_DSOAT("CTO_DTU");
                        } else {
                            for (int i = 0; i <adapterKH.getItemCount(); i++){
                                GcsEntityKhachHang entityKH = adapterKH.listData.get(i);
                                if(entity.getSERY_CTO().equals(entityKH.getSERY_CTO())){
                                    adapterKH.listData.get(i).setSTR_CHECK_DSOAT("CTO_DTU");
                                }
                            }
                        }
                    }
                }
            });

            GcsmenuAdapter adapterMenu = new GcsmenuAdapter(GcsGhiChiSoFragment.this.getActivity(),
                    R.layout.gcs_row_menu, GcsConstantVariables.ARR_MENU);
            lvMenu.setAdapter(adapterMenu);

            lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:// Ghi chú
                            showDialogGhiChu(entity, tvGhiChu);
                            break;
                        case 1:// Xóa chỉ số
                            AlertDialog.Builder builder = new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity());
                            builder.setMessage("Bạn có chắc chắn muốn xóa?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int which) {
                                    if (connection.updateChiSo(String.valueOf(entity.getID_SQLITE()),
                                            0F, "", 0F, "", "", "", "", "") != -1) {

                                        reLoadData(0F, "", 0F, 0F, "", "");
                                        adapterKH.notifyDataSetChanged();
                                        dialog.dismiss();
                                        setTitle();
                                        Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Đã xóa chỉ số", Toast.LENGTH_LONG).show();
                                    } else {
                                        Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Không xóa được chỉ số", Color.WHITE, "OK", Color.RED);
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", null);
                            builder.show();
                            break;
                        case 2:// Xóa chỉ số, xóa ảnh
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity());
                            builder2.setMessage("Bạn có chắc chắn muốn xóa?");
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int which) {
                                    if(connection.updateChiSo(String.valueOf(entity.getID_SQLITE()),
                                            0F, "", 0F, "", "", "", "", "") != -1){
                                        final String fileName = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH
                                                + TEN_FILE + "/" + GcsCommon.getPhotoName(
                                                entity.getMA_QUYEN(),
                                                entity.getMA_CTO(),
                                                entity.getNAM(),
                                                entity.getTHANG(),
                                                entity.getKY(),
                                                entity.getMA_DDO(),
                                                entity.getLOAI_BCS());
                                        File file = new File(fileName);
                                        if(file.exists()){
                                            file.delete();
                                            Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Đã xóa ảnh và chỉ số", Toast.LENGTH_LONG).show();
                                        }
                                        reLoadData(0F, "", 0F, 0F, "", "");
                                        ivViewImage.setImageBitmapReset(null, 0, true);
                                        adapterKH.notifyDataSetChanged();
                                        dialog.dismiss();
                                        setTitle();
                                    } else {
                                        Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                                                "Lỗi", Color.RED, "Không xóa được ảnh chỉ số", Color.WHITE, "OK", Color.RED);
                                    }
                                }
                            });
                            builder2.setNegativeButton("Cancel", null);
                            builder2.show();

                            break;
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    public void showPopupMenu(View v) {
        try {
            PopupMenu menu = new PopupMenu(GcsGhiChiSoFragment.this.getActivity());
            menu.setHeaderTitle("Chức năng");
            menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case GcsConstantVariables.MENU_AN_HIEN:
                            if (((AppCompatActivity) getActivity()).getSupportActionBar().isShowing()) {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                            } else {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                            }
                            break;

                        case GcsConstantVariables.MENU_HINH_THUC_HIEN_THI:
                            if (_TrangThaiHienThiSo == 0) {//_TrangThaiHienThiSo = 0: Hiện thị hoàn toàn
                                _TrangThaiHienThiSo = 1;
                                initDataKhachHang();
                            } else {//_TrangThaiHienThiSo = 1: Hiển thị công tơ đối soát ko đạt
                                _TrangThaiHienThiSo = 0;
                                initDataKhachHang();
                            }
                            break;
                    }
                }
            });
            // Add Menu (Android menu like style)
            menu.add(GcsConstantVariables.MENU_AN_HIEN, "Ẩn hiện thanh tìm kiếm").setIcon(null);
            menu.add(GcsConstantVariables.MENU_HINH_THUC_HIEN_THI, _TrangThaiHienThiSo==0?"Công tơ đối soát không đạt":"Hiển thị tất cả công tơ").setIcon(null);
            menu.show(v);
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị menu", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogGhiChu(final GcsEntityKhachHang entity, final TextView tvGhiChu) {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_ghichu);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);

            final EditText etGhiChu = (EditText) dialog.findViewById(R.id.gcs_dialog_ghichu_etGhichu);
            Button btLuu = (Button) dialog.findViewById(R.id.gcs_dialog_ghichu_btLuu);

            String gc = connection.getGhiChu(String.valueOf(entity.getID_SQLITE()));
            etGhiChu.setText(gc);

            btLuu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String ghiChu = etGhiChu.getText().toString().trim();
                        if (connection.updateGhiChu(String.valueOf(entity.getID_SQLITE()), ghiChu) != -1) {
                            adapterKH.listData.get(_pos_selected).setGHICHU(ghiChu);
                            tvGhiChu.setText(ghiChu);
                            Toast.makeText(GcsGhiChiSoFragment.this.getActivity(), "Đã thêm ghi chú", Toast.LENGTH_LONG).show();
                        } else {
                            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                                    "Lỗi", Color.RED, "Ghi chú lỗi", Color.WHITE, "OK", Color.RED);
                        }
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                                "Lỗi", Color.RED, "Lỗi lưu ghi chú", Color.WHITE, "OK", Color.RED);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi ghi chú", Color.WHITE, "OK", Color.RED);
        }
    }

    private void showDialogViewImage() {
        try {
            final Dialog dialog = new Dialog(GcsGhiChiSoFragment.this.getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.gcs_dialog_view_image);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.CENTER);

            final ImageViewTouch ivtImage = (ImageViewTouch) dialog.findViewById(R.id.gcs_dialog_view_image_ivtImage);
            ImageButton ibDelete = (ImageButton) dialog.findViewById(R.id.gcs_dialog_view_image_ibDelete);

            String fileName = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH
                    + TEN_FILE + "/" + GcsCommon.getPhotoName(
                    adapterKH.listData.get(_pos_selected).getMA_QUYEN(),
                    adapterKH.listData.get(_pos_selected).getMA_CTO(),
                    adapterKH.listData.get(_pos_selected).getNAM(),
                    adapterKH.listData.get(_pos_selected).getTHANG(),
                    adapterKH.listData.get(_pos_selected).getKY(),
                    adapterKH.listData.get(_pos_selected).getMA_DDO(),
                    adapterKH.listData.get(_pos_selected).getLOAI_BCS());
            final File fImage = new File(fileName);
            if (fImage.exists()) {
                Bitmap bmImage = BitmapFactory.decodeFile(fileName);
                ivtImage.setImageBitmapReset(bmImage, 0, true);
                dialog.show();
            }

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fImage.exists()) {
                        AlertDialog.Builder build = new AlertDialog.Builder(GcsGhiChiSoFragment.this.getActivity());
                        build.setMessage("Bạn có chắc chắn muốn xóa?");
                        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                fImage.delete();
                                dialog.dismiss();
                                ivViewImage.setImageBitmapReset(null, 0, true);
                                ivViewImage.setEnabled(false);
                            }
                        });
                        build.setNegativeButton("No", null);
                        build.show();
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ivtImage.setImageBitmapReset(null, 0, true);
                }
            });
        } catch (Exception ex) {
            Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(),
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_MODERATE) { // 60
            mCache.evictAll();
        } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
            mCache.trimToSize(mCache.size() / 2);
        }
    }

    public static class ThumbnailCache extends LruCache<Long, Bitmap> {
        public ThumbnailCache(int maxSizeBytes) {
            super(maxSizeBytes);
        }

        @Override
        protected int sizeOf(Long key, Bitmap value) {
            return value.getByteCount();
        }
    }

    class GcsmenuAdapter extends ArrayAdapter<String> {

        Context context;
        int resource;
        String[] objects;

        public GcsmenuAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.gcs_row_menu_ivIcon);
            TextView tvMenu = (TextView) convertView.findViewById(R.id.gcs_row_menu_tvMenu);

            tvMenu.setText(objects[position]);
            ivIcon.setImageResource(GcsConstantVariables.ARR_ICON_MENU[position]);

            return convertView;
        }
    }

    class GcsKhachHangAdapter extends RecyclerView.Adapter<GcsKhachHangAdapter.RecyclerViewHolder> {

        List<GcsEntityKhachHang> listData = new ArrayList<>();

        public GcsKhachHangAdapter(List<GcsEntityKhachHang> listData) {
            this.listData = listData;
        }

        public void updateList(List<GcsEntityKhachHang> data) {
            listData = data;
            notifyDataSetChanged();
        }

//        public void animateTo(List<GcsEntityKhachHang> models) {
//            applyAndAnimateRemovals(models);
//            applyAndAnimateMovedItems(models);
//        }

        private void applyAndAnimateRemovals(List<GcsEntityKhachHang> newModels) {
            for (int i = listData.size() - 1; i >= 0; i--) {
                final GcsEntityKhachHang model = listData.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

//        private void applyAndAnimateAdditions(List<GcsEntityKhachHang> newModels) {
//            for (int i = 0, count = newModels.size(); i < count; i++) {
//                final GcsEntityKhachHang model = newModels.get(i);
//                if (!listData.contains(model)) {
//                    addItem(i, model);
//                }
//            }
//        }

        private void applyAndAnimateMovedItems(List<GcsEntityKhachHang> newModels) {
            for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
                final GcsEntityKhachHang model = newModels.get(toPosition);
                final int fromPosition = listData.indexOf(model);
                if (fromPosition >= 0 && fromPosition != toPosition) {
                    moveItem(fromPosition, toPosition);
                }
            }
        }

        public void moveItem(int fromPosition, int toPosition) {
            final GcsEntityKhachHang model = listData.remove(fromPosition);
            listData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);

        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.gcs_row_khachhang, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            try {
                holder.tvSTT.setText(new StringBuilder(String.valueOf(listData.get(position).getSTT())).append(".").toString());
                holder.tvTenKH.setText(listData.get(position).getTEN_KHANG());
                holder.tvCSMoi.setText(String.valueOf(listData.get(position).getCS_MOI()));
                holder.tvCSCu.setText(String.valueOf(listData.get(position).getCS_CU()));
                holder.tvSLMoi.setText(String.valueOf(listData.get(position).getSL_MOI()));
                holder.tvSLCu.setText(String.valueOf(listData.get(position).getSL_CU()));
                holder.tvDtt.setText(String.valueOf((listData.get(position).getSL_MOI() + listData.get(position).getSL_THAO())));
                holder.tvMaCot.setText(listData.get(position).getMA_COT());
                holder.tvLoaiBCS.setText(listData.get(position).getLOAI_BCS());
                holder.tvTTRMoi.setText(listData.get(position).getTTR_MOI());
                holder.tvSoCto.setText(listData.get(position).getSERY_CTO());
                if(_pos_selected == position){
                    holder.itemView.setBackgroundResource(R.drawable.gcs_bg_listitem_green);
                } else {
                    if (GcsCommon.isSave(listData.get(position).getCS_MOI(), listData.get(position).getTTR_MOI())) {
                        holder.itemView.setBackgroundResource(R.drawable.gcs_bg_listitem_blue);
                    } else {
                        holder.itemView.setBackgroundResource(R.drawable.gcs_bg_listitem_white);
                    }
                }
            } catch(Exception ex) {
//                Log.e("Error bind view",ex.toString());
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

//        public void addItem(int position, GcsEntityKhachHang data) {
//            listData.add(position, data);
//            notifyItemInserted(position);
//        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener, CompoundButton.OnCheckedChangeListener {

            public TextView tvSTT, tvTenKH, tvCSMoi, tvSLMoi, tvDtt, tvCSCu, tvSLCu, tvMaCot, tvLoaiBCS, tvTTRMoi, tvSoCto;
            public CheckBox ckCtoDtu;
            public ImageButton ibShowMenu;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvSTT = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_stt);
                tvTenKH = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_tenkh);
                tvCSMoi = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_csmoi);
                tvCSCu = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_cscu);
                tvSLMoi = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_slmoi);
                tvSLCu = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_slcu);
                tvDtt = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_dtt);
                tvMaCot = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_macot);
                tvLoaiBCS = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_loaibcs);
                tvTTRMoi = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_ttrmoi);
                tvSoCto = (TextView) itemView.findViewById(R.id.gcs_row_khachhang_socto);
                ibShowMenu = (ImageButton) itemView.findViewById(R.id.gcs_row_khachhang_showMenu);

                itemView.setOnClickListener(this);
                ibShowMenu.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    _pos_selected = getPosition();
                    initChiSo(listData);
                    String fileName = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(GcsConstantVariables.PROGRAM_PHOTO_PATH)
                            .append(TEN_FILE).append("/").append(GcsCommon.getPhotoName(
                            listData.get(_pos_selected).getMA_QUYEN(),
                            listData.get(_pos_selected).getMA_CTO(),
                            listData.get(_pos_selected).getNAM(),
                            listData.get(_pos_selected).getTHANG(),
                            listData.get(_pos_selected).getKY(),
                            listData.get(_pos_selected).getMA_DDO(),
                            listData.get(_pos_selected).getLOAI_BCS())).toString();
                    setImage(fileName);
                    notifyDataSetChanged();
                    if(v.getId() == R.id.gcs_row_khachhang_showMenu){
                        showDialogMenu(listData.get(_pos_selected));
                    } else {
                        showKeyboard(etChiSo);
                    }
                    rvKhachHang.smoothScrollToPosition(_pos_selected);
                } catch(Exception ex) {
                    Common.showAlertDialogGreen(GcsGhiChiSoFragment.this.getActivity(), "Lỗi", Color.RED, "Lỗi lưu ảnh", Color.WHITE, "OK", Color.RED);
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        }

    }

}
