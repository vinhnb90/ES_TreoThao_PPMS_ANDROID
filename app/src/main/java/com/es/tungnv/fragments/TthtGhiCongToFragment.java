package com.es.tungnv.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.TthtBBanEntity;
import com.es.tungnv.entity.TthtCtoEntity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.views.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TthtGhiCongToFragment extends Fragment implements View.OnClickListener {
    private String MA_BDONG;
    TthtBBanEntity tthtBBanEntity;
    TthtCtoEntity tthtCtoEntity;

    //    private TthtKHangEntity objectData;
//    private TthtKHangEntity objectDataTreo;
//    private TthtKHangEntity objectDataThao;
    private boolean isDoiSoat = false;
    private int ID_BBAN_TRTH;
    private int ID_CHITIET_CTO;

    private TthtSQLiteConnection connection;


    private Button btExpand1, btExpand2, btExpand5, btChupCongTo;
    private TextView
            tvTenKH, tvDiaChiKH, tvLyDoTreoThao, tvMaGCS, tvSoNo,
            tvTenLoaiCto, tvNhanVienTrTh, tvmaKH, tvViTriLapDat, tvTramCapDien,
            tvCS1, tvCS2, tvCS3, tvCS4, tvCS5,
            tvLoaiCto, tvHeSoNhan, tvNuocSX, tvNamSX, tvMaTemKDinh, tvNgayKiemDinh, tvDongDien,
            tvDienAp, tvHeSoK, tvCCX, tvLapQuaTu, tvNhaCungCap, tvLapQuaTi;
    private EditText
            tvMaChiHop,
            tvMaChiBooc,
            tvMaChiKDinh,
            tvKimNiemChi,
            etTemCongQuang, etLanCanhBao, etTTrangNiemPhong, etGhiChu,
            etCS1, etCS2, etCS3, etCS4, etCS5;
    private Spinner
            spinSoVienChiKDinh, spinSoVienChiBooc, spinLoaiHom,
            spinSoVienChiHom, spinPhuongThucDoXa;
    private ImageView ivChupCongTo;
    private LinearLayout
            llExpand1,
            llExpand2,
            llExpand5, llExpand5_1;
    //TODO biến được lưu để sử dụng
    private StringBuilder CHI_SO = new StringBuilder();
    private String DATE_TIME_TO_CREATE_FILE, DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO, TEN_ANH_CONG_TO = "", TEN_ANH_CONG_TO_NIEMPHONG = "";
    private boolean isRefreshImageCongTo = false;
    private boolean isRefreshImageNiemPhongCongTo = false;
    private Button btExpand5_1;
    private ImageView ivChupCongToNiemPhong;
    private Button btChupCongToNiemPhong;

    public TthtGhiCongToFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ttht_ghi_cong_to, container, false);

        //TODO get bundle
        getBundle();
        initView(view);
        setAction(view);
        fillData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getBundle() {
        Bundle bundle = getArguments();
        ID_BBAN_TRTH = bundle.getInt("ID_BBAN_TRTH", 0);
        ID_CHITIET_CTO = bundle.getInt("ID_CHITIET_CTO", 0);
        MA_BDONG = bundle.getString("MA_BDONG", "");
        isDoiSoat = bundle.getBoolean("IS_DOI_SOAT");
    }

    public String getMA_BDONG() {
        return MA_BDONG;
    }

    public void setMA_BDONG(String MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
//        fillData();
    }

    private void setAction(final View rootView) {
        //TODO set onclick
        btExpand1.setOnClickListener(this);
        btExpand2.setOnClickListener(this);
        btExpand5.setOnClickListener(this);
        btChupCongTo.setOnClickListener(this);
        btChupCongToNiemPhong.setOnClickListener(this);
        ivChupCongToNiemPhong.setOnClickListener(this);
        ivChupCongTo.setOnClickListener(this);

        //TODO set visible view at first time
        llExpand1.setVisibility(View.VISIBLE);
        btExpand1.setBackgroundResource(R.drawable.title_elasticity);
        btExpand1.setCompoundDrawablePadding(10);
        btExpand1.setPadding(10, 0, 0, 0);
        llExpand2.setVisibility(View.VISIBLE);
        btExpand2.setBackgroundResource(R.drawable.title_elasticity);
        btExpand2.setCompoundDrawablePadding(10);
        btExpand2.setPadding(10, 0, 0, 0);
        llExpand5.setVisibility(View.VISIBLE);
        btExpand5.setBackgroundResource(R.drawable.title_elasticity);
        btExpand5.setCompoundDrawablePadding(10);
        btExpand5.setPadding(10, 0, 0, 0);

        llExpand5_1.setVisibility(View.VISIBLE);
        btExpand5_1.setBackgroundResource(R.drawable.title_elasticity);
        btExpand5_1.setCompoundDrawablePadding(10);
        btExpand5_1.setPadding(10, 0, 0, 0);

        //TODO bắt sự kiện click nút backPress
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //TODO nếu đã chụp ảnh mới nhưng chưa ghi thì thông báo
                    if (!TEN_ANH_CONG_TO.isEmpty()) {
                        PpmsCommon.showTitleByDialog(getActivity(), "Thông báo ", "Đã chụp ảnh công tơ mới nhưng chưa được lưu!", true, 0);
                    }

                    if (!TEN_ANH_CONG_TO_NIEMPHONG.isEmpty()) {
                        PpmsCommon.showTitleByDialog(getActivity(), "Thông báo ", "Đã chụp ảnh niêm phong mới nhưng chưa được lưu!", true, 0);
                    }


                }
                return false;
            }
        });
    }

    private void fillData() {
        //TODO clear data

        CHI_SO = new StringBuilder();
        DATE_TIME_TO_CREATE_FILE = DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO = TEN_ANH_CONG_TO = TEN_ANH_CONG_TO_NIEMPHONG = "";

        connection = TthtSQLiteConnection.getInstance(getActivity());

        Cursor cBBan = connection.getBBanByIDBBanTRTH(ID_BBAN_TRTH);
        if (cBBan != null) {
            tthtBBanEntity = new TthtBBanEntity();
            tthtBBanEntity.setGHI_CHU(cBBan.getString(cBBan.getColumnIndex("GHI_CHU")));
            tthtBBanEntity.setID_BBAN_TRTH(cBBan.getInt(cBBan.getColumnIndex("ID_BBAN_TRTH")));
            tthtBBanEntity.setMA_CNANG(cBBan.getString(cBBan.getColumnIndex("MA_CNANG")));
            tthtBBanEntity.setMA_DDO(cBBan.getString(cBBan.getColumnIndex("MA_DDO")));
            tthtBBanEntity.setMA_DVIQLY(cBBan.getString(cBBan.getColumnIndex("MA_DVIQLY")));
            tthtBBanEntity.setMA_LDO(cBBan.getString(cBBan.getColumnIndex("MA_LDO")));
            tthtBBanEntity.setMA_NVIEN(cBBan.getString(cBBan.getColumnIndex("BUNDLE_MA_NVIEN")));
            tthtBBanEntity.setMA_YCAU_KNAI(cBBan.getString(cBBan.getColumnIndex("MA_YCAU_KNAI")));
            tthtBBanEntity.setNGAY_SUA(cBBan.getString(cBBan.getColumnIndex("NGAY_SUA")));
            tthtBBanEntity.setNGAY_TAO(cBBan.getString(cBBan.getColumnIndex("NGAY_TAO")));
            tthtBBanEntity.setNGAY_TRTH(cBBan.getString(cBBan.getColumnIndex("NGAY_TRTH")));
            tthtBBanEntity.setNGUOI_SUA(cBBan.getString(cBBan.getColumnIndex("NGUOI_SUA")));
            tthtBBanEntity.setNGUOI_TAO(cBBan.getString(cBBan.getColumnIndex("NGUOI_TAO")));
            tthtBBanEntity.setSO_BBAN(cBBan.getString(cBBan.getColumnIndex("SO_BBAN")));
            tthtBBanEntity.setTRANG_THAI(cBBan.getInt(cBBan.getColumnIndex("TRANG_THAI")));
            tthtBBanEntity.setGHI_CHU(cBBan.getString(cBBan.getColumnIndex("GHI_CHU")));
            tthtBBanEntity.setId_BBAN_CONGTO(cBBan.getInt(cBBan.getColumnIndex("ID_BBAN_CONGTO")));
            tthtBBanEntity.setLOAI_BBAN(cBBan.getString(cBBan.getColumnIndex("LOAI_BBAN")));
            tthtBBanEntity.setTEN_KHANG(cBBan.getString(cBBan.getColumnIndex("TEN_KHANG")));
            tthtBBanEntity.setDCHI_HDON(cBBan.getString(cBBan.getColumnIndex("DCHI_HDON")));
            tthtBBanEntity.setDTHOAI(cBBan.getString(cBBan.getColumnIndex("DTHOAI")));
            tthtBBanEntity.setMA_GCS_CTO(cBBan.getString(cBBan.getColumnIndex("MA_GCS_CTO")));
            tthtBBanEntity.setMA_TRAM(cBBan.getString(cBBan.getColumnIndex("MA_TRAM")));
            tthtBBanEntity.setMA_KHANG(cBBan.getString(cBBan.getColumnIndex("MA_KHANG")));
            tthtBBanEntity.setLY_DO_TREO_THAO(cBBan.getString(cBBan.getColumnIndex("LY_DO_TREO_THAO")));
            tthtBBanEntity.setMA_HDONG(cBBan.getString(cBBan.getColumnIndex("MA_HDONG")));
            cBBan.close();
        }


        Cursor cCto = connection.getCToByID_CHITIET_CTO(ID_CHITIET_CTO);
        if (cCto != null) {
            tthtCtoEntity = new TthtCtoEntity();
            tthtCtoEntity.setMA_DVIQLY(cCto.getString(cCto.getColumnIndex("MA_DVIQLY")));
            tthtCtoEntity.setID_BBAN_TRTH(cCto.getInt(cCto.getColumnIndex("ID_BBAN_TRTH")));
            tthtCtoEntity.setMA_CTO(cCto.getString(cCto.getColumnIndex("MA_CTO")));
            tthtCtoEntity.setSO_CTO(cCto.getString(cCto.getColumnIndex("SO_CTO")));
            tthtCtoEntity.setLAN(cCto.getInt(cCto.getColumnIndex("LAN")));
            String MA_BDONG_CTO = cCto.getString(cCto.getColumnIndex("MA_BDONG"));
            tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
            tthtCtoEntity.setNGAY_BDONG(cCto.getString(cCto.getColumnIndex("NGAY_BDONG")));
            tthtCtoEntity.setMA_CLOAI(cCto.getString(cCto.getColumnIndex("MA_CLOAI")));
            tthtCtoEntity.setLOAI_CTO(cCto.getString(cCto.getColumnIndex("LOAI_CTO")));
            tthtCtoEntity.setVTRI_TREO(cCto.getInt(cCto.getColumnIndex("VTRI_TREO")));
            tthtCtoEntity.setMO_TA_VTRI_TREO(cCto.getString(cCto.getColumnIndex("MO_TA_VTRI_TREO")));

            tthtCtoEntity.setMA_SOCBOOC(cCto.getString(cCto.getColumnIndex("MA_SOCBOOC")));
            tthtCtoEntity.setSOVIEN_CBOOC(cCto.getInt(cCto.getColumnIndex("SO_VIENCBOOC")));
            tthtCtoEntity.setLOAI_HOM(cCto.getInt(cCto.getColumnIndex("LOAI_HOM")));
            tthtCtoEntity.setMA_SOCHOM(cCto.getString(cCto.getColumnIndex("MA_SOCHOM")));
            tthtCtoEntity.setSO_VIENCHOM(cCto.getInt(cCto.getColumnIndex("SO_VIENCHOM")));
            tthtCtoEntity.setHS_NHAN(cCto.getInt(cCto.getColumnIndex("HS_NHAN")));
            tthtCtoEntity.setNGAY_TAO(cCto.getString(cCto.getColumnIndex("NGAY_TAO")));
            tthtCtoEntity.setNGUOI_TAO(cCto.getString(cCto.getColumnIndex("NGUOI_TAO")));
            tthtCtoEntity.setNGAY_SUA(cCto.getString(cCto.getColumnIndex("NGAY_SUA")));
            tthtCtoEntity.setNGUOI_SUA(cCto.getString(cCto.getColumnIndex("NGUOI_SUA")));
            tthtCtoEntity.setMA_CNANG(cCto.getString(cCto.getColumnIndex("MA_CNANG")));
            tthtCtoEntity.setSO_TU(cCto.getString(cCto.getColumnIndex("SO_TU")));
            tthtCtoEntity.setSO_TI(cCto.getString(cCto.getColumnIndex("SO_TI")));
            tthtCtoEntity.setSO_COT(cCto.getString(cCto.getColumnIndex("SO_COT")));
            tthtCtoEntity.setSO_HOM(cCto.getString(cCto.getColumnIndex("SO_HOM")));
            tthtCtoEntity.setCHI_SO(cCto.getString(cCto.getColumnIndex("CHI_SO")));
            tthtCtoEntity.setNGAY_KDINH(cCto.getString(cCto.getColumnIndex("NGAY_KDINH")));
            tthtCtoEntity.setNAM_SX(cCto.getString(cCto.getColumnIndex("NAM_SX")));
            tthtCtoEntity.setTEM_CQUANG(cCto.getString(cCto.getColumnIndex("TEM_CQUANG")));
            tthtCtoEntity.setMA_CHIKDINH(cCto.getString(cCto.getColumnIndex("MA_CHIKDINH")));
            tthtCtoEntity.setMA_TEM(cCto.getString(cCto.getColumnIndex("MA_TEM")));
            tthtCtoEntity.setSOVIEN_CHIKDINH(cCto.getInt(cCto.getColumnIndex("SOVIEN_CHIKDINH")));
            tthtCtoEntity.setDIEN_AP(cCto.getString(cCto.getColumnIndex("DIEN_AP")));
            tthtCtoEntity.setDONG_DIEN(cCto.getString(cCto.getColumnIndex("DONG_DIEN")));
            tthtCtoEntity.setHANGSO_K(cCto.getString(cCto.getColumnIndex("HANGSO_K")));
            tthtCtoEntity.setMA_NUOC(cCto.getString(cCto.getColumnIndex("MA_NUOC")));
            tthtCtoEntity.setTEN_NUOC(cCto.getString(cCto.getColumnIndex("TEN_NUOC")));
            tthtCtoEntity.setSO_KIM_NIEM_CHI(cCto.getString(cCto.getColumnIndex("SO_KIM_NIEM_CHI")));
            tthtCtoEntity.setTTRANG_NPHONG(cCto.getString(cCto.getColumnIndex("TTRANG_NPHONG")));

            //TODO lấy tên ảnh
            Cursor cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
            if (cusorGetAnhCongTo.moveToFirst()) {
                String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
                tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
                cusorGetAnhCongTo.close();
            }

            Cursor cusorGetAnhNiemPhongCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
            if (cusorGetAnhNiemPhongCongTo.moveToFirst()) {
                String TEN_ANH = cusorGetAnhNiemPhongCongTo.getString(cusorGetAnhNiemPhongCongTo.getColumnIndex("TEN_ANH"));
                tthtCtoEntity.setTEN_ANH_NIEMPHONG_CONG_TO(TEN_ANH);
                cusorGetAnhNiemPhongCongTo.close();
            }

            tthtCtoEntity.setID_CHITIET_CTO(ID_CHITIET_CTO);
            tthtCtoEntity.setTEN_LOAI_CTO(cCto.getString(cCto.getColumnIndex("TEN_LOAI_CTO")).trim());

            tthtCtoEntity.setPHUONG_THUC_DO_XA(cCto.getString(cCto.getColumnIndex("PHUONG_THUC_DO_XA")).trim());
            tthtCtoEntity.setGHI_CHU(cCto.getString(cCto.getColumnIndex("GHI_CHU")).trim());
            tthtCtoEntity.setTRANG_THAI_DU_LIEU(cCto.getInt(cCto.getColumnIndex("TRANG_THAI_DU_LIEU")));
            int ID_BBAN_TUTI = cCto.getInt(cCto.getColumnIndex("ID_BBAN_TUTI"));

            tthtCtoEntity.setID_BBAN_TUTI(ID_BBAN_TUTI);
            tthtCtoEntity.setHS_NHAN_SAULAP_TUTI(cCto.getInt(cCto.getColumnIndex("HS_NHAN_SAULAP_TUTI")));
            tthtCtoEntity.setSO_TU_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("SO_TU_SAULAP_TUTI")));
            tthtCtoEntity.setSO_TI_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("SO_TI_SAULAP_TUTI")));
            tthtCtoEntity.setCHI_SO_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("CHI_SO_SAULAP_TUTI")));
            tthtCtoEntity.setDIEN_AP_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("DIEN_AP_SAULAP_TUTI")));
            tthtCtoEntity.setDONG_DIEN_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("DONG_DIEN_SAULAP_TUTI")));
            tthtCtoEntity.setHANGSO_K_SAULAP_TUTI(cCto.getString(cCto.getColumnIndex("HANGSO_K_SAULAP_TUTI")));
            tthtCtoEntity.setCAP_CX_SAULAP_TUTI(cCto.getInt(cCto.getColumnIndex("CAP_CX_SAULAP_TUTI")));
            cCto.close();
        }

        //TODO set textview
        String tvTenKHText = (tthtBBanEntity.getTEN_KHANG() == null) ? "" : tthtBBanEntity.getTEN_KHANG();
        String tvDiaChiKHText = (tthtBBanEntity.getDCHI_HDON() == null) ? "" : tthtBBanEntity.getDCHI_HDON();
        String tvLyDoTreoThaoText = (tthtBBanEntity.getLY_DO_TREO_THAO() == null) ? "" : tthtBBanEntity.getLY_DO_TREO_THAO();
        String tvMaGCSText = (tthtBBanEntity.getMA_GCS_CTO() == null) ? "" : tthtBBanEntity.getMA_GCS_CTO();
        String tvSoNoText = (tthtCtoEntity.getSO_CTO() == null) ? "" : tthtCtoEntity.getSO_CTO();
        String tvKimNiemChiText = (tthtCtoEntity.getSO_KIM_NIEM_CHI() == null) ? "" : tthtCtoEntity.getSO_KIM_NIEM_CHI();
        String tvTenLoaiCtoText = (tthtCtoEntity.getTEN_LOAI_CTO() == null) ? "" : tthtCtoEntity.getTEN_LOAI_CTO();
        String tvTramCapDienText = (tthtBBanEntity.getMA_TRAM() == null) ? "" : tthtBBanEntity.getMA_TRAM();
        String tvLoaiCtoText = (tthtCtoEntity.getLOAI_CTO() == null) ? "" : tthtCtoEntity.getLOAI_CTO();
        int tvHeSoNhanText = (tthtCtoEntity.getHS_NHAN() == 0) ? 0 : tthtCtoEntity.getHS_NHAN();
        String tvNuocSXText = (tthtCtoEntity.getMA_NUOC() == null) ? "" : tthtCtoEntity.getMA_NUOC();
        String tvNamSXText = (tthtCtoEntity.getNAM_SX() == null) ? "" : tthtCtoEntity.getNAM_SX();
        String tvMaTemKDinhText = (tthtCtoEntity.getMA_TEM() == null) ? "" : tthtCtoEntity.getMA_TEM();
        String tvNgayKiemDinhText = (tthtCtoEntity.getNGAY_KDINH() == null) ? "" : tthtCtoEntity.getNGAY_KDINH();
        String tvDongDienText = (tthtCtoEntity.getDONG_DIEN() == null) ? "" : tthtCtoEntity.getDONG_DIEN();
        String tvDienApText = (tthtCtoEntity.getDIEN_AP() == null) ? "" : tthtCtoEntity.getDIEN_AP();
        String tvHeSoKText = (tthtCtoEntity.getHANGSO_K() == null) ? "" : tthtCtoEntity.getHANGSO_K();

        String tvLapQuaTuText = (tthtCtoEntity.getSO_TU() == null) ? "" : tthtCtoEntity.getSO_TU();
        String tvLapQuaTiText = (tthtCtoEntity.getSO_TI() == null) ? "" : tthtCtoEntity.getSO_TI();
        String etTemCongQuangText = (tthtCtoEntity.getTEM_CQUANG() == null) ? "" : tthtCtoEntity.getTEM_CQUANG();
        String tvMaChiKDinhText = (tthtCtoEntity.getMA_CHIKDINH() == null) ? "" : tthtCtoEntity.getMA_CHIKDINH();
        int etLanCanhBaoText = (tthtCtoEntity.getLAN() == 0) ? 0 : tthtCtoEntity.getLAN();
        String tvMaChiHopText = (tthtCtoEntity.getMA_SOCHOM() == null) ? "" : tthtCtoEntity.getMA_SOCHOM();
        String tvMaChiBoocText = (tthtCtoEntity.getMA_SOCBOOC() == null) ? "" : tthtCtoEntity.getMA_SOCBOOC();
        String etTTrangNiemPhongText = (tthtCtoEntity.getTTRANG_NPHONG() == null) ? "" : tthtCtoEntity.getTTRANG_NPHONG();
        String etGhiChuText = (tthtCtoEntity.getGHI_CHU() == null) ? "" : tthtCtoEntity.getGHI_CHU();

        String MA_HANG = "";
        String ALL_PHUONG_THUC_DO_XA = "";
        String CCX = "";
        Cursor cursorGetLoaiCto = connection.getLoaiCongTo(tthtCtoEntity.getTEN_LOAI_CTO());
        if (cursorGetLoaiCto != null) {
            MA_HANG = cursorGetLoaiCto.getString(cursorGetLoaiCto.getColumnIndex("MA_HANG"));
            ALL_PHUONG_THUC_DO_XA = cursorGetLoaiCto.getString(cursorGetLoaiCto.getColumnIndex("PTHUC_DOXA"));
            CCX = cursorGetLoaiCto.getString(cursorGetLoaiCto.getColumnIndex("CAP_CXAC_P"));
            cursorGetLoaiCto.close();
        }

        String tvCCXText = (CCX == null) ? "" : CCX;
        String tvNhaCungCapText = (MA_HANG == null) ? "" : MA_HANG;

        int TRANG_THAI_DU_LIEU = tthtCtoEntity.getTRANG_THAI_DU_LIEU();
        if (TRANG_THAI_DU_LIEU == 2) {
            btChupCongTo.setEnabled(false);
        } else {
            btChupCongTo.setEnabled(true);
        }


        if (isDoiSoat) {
            btChupCongTo.setEnabled(false);
        }

        String MO_TA_VTRI_TREO = tthtCtoEntity.getMO_TA_VTRI_TREO();
        String MA_KHANG = tthtBBanEntity.getMA_KHANG();

        tvNhanVienTrTh.setText(TthtCommon.getTenNvien());
        tvmaKH.setText(MA_KHANG);
        tvViTriLapDat.setText(MO_TA_VTRI_TREO);

        tvTenKH.setText(tvTenKHText);
        tvDiaChiKH.setText(tvDiaChiKHText);
        tvLyDoTreoThao.setText(tvLyDoTreoThaoText);
        tvMaGCS.setText(tvMaGCSText);
        tvSoNo.setText(tvSoNoText);
        tvKimNiemChi.setText(tvKimNiemChiText);
        tvTenLoaiCto.setText(tvTenLoaiCtoText);
        tvTramCapDien.setText(tvTramCapDienText);
        tvLoaiCto.setText(tvLoaiCtoText);
        tvHeSoNhan.setText(tvHeSoNhanText + "");
        tvNuocSX.setText(tvNuocSXText);
        tvNamSX.setText(tvNamSXText);
        tvMaTemKDinh.setText(tvMaTemKDinhText);

        tvNgayKiemDinhText = TthtCommon.convertDateTime(tvNgayKiemDinhText, 1);
        tvNgayKiemDinh.setText(tvNgayKiemDinhText);

        tvDongDien.setText(tvDongDienText);
        tvDienAp.setText(tvDienApText);
        tvHeSoK.setText(tvHeSoKText);
        tvCCX.setText(tvCCXText);
        tvLapQuaTu.setText(tvLapQuaTuText);
        tvLapQuaTi.setText(tvLapQuaTiText);
        tvMaChiKDinh.setText(tvMaChiKDinhText);
        //TODO set EditText
        etTemCongQuang.setText(etTemCongQuangText);
        etLanCanhBao.setText(etLanCanhBaoText + "");
        tvMaChiHop.setText(tvMaChiHopText);
        tvMaChiBooc.setText(tvMaChiBoocText);
        etTTrangNiemPhong.setText(etTTrangNiemPhongText);
        etGhiChu.setText(etGhiChuText);
        tvNhaCungCap.setText(tvNhaCungCapText);

        String CHI_SO = tthtCtoEntity.getCHI_SO();
        etCS1.setText("");
        etCS2.setText("");
        etCS3.setText("");
        etCS4.setText("");
        etCS5.setText("");
        tvCS1.setVisibility(View.VISIBLE);
        tvCS2.setVisibility(View.VISIBLE);
        tvCS3.setVisibility(View.VISIBLE);
        tvCS4.setVisibility(View.VISIBLE);
        tvCS5.setVisibility(View.VISIBLE);
        etCS1.setVisibility(View.VISIBLE);
        etCS2.setVisibility(View.VISIBLE);
        etCS3.setVisibility(View.VISIBLE);
        etCS4.setVisibility(View.VISIBLE);
        etCS5.setVisibility(View.VISIBLE);
        if (tthtCtoEntity.getLOAI_CTO().equals(TthtCommon.LOAI_CONGTO.D1.name())) {

            tvCS1.setText("BT");
            tvCS2.setText("CD");
            tvCS3.setText("TD");
            tvCS4.setText("SG");
            tvCS5.setText("VC");
            if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                etCS1.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                etCS2.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                etCS3.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[2].split(":")[1] : CHI_SO.split(";")[2]);
                etCS4.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[3].split(":")[1] : CHI_SO.split(";")[3]);
                etCS5.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[4].split(":")[1] : CHI_SO.split(";")[4]);
            } else {
                etCS1.setHint("");
                etCS2.setHint("");
                etCS3.setHint("");
                etCS4.setHint("");
                etCS5.setHint("");
            }
        }

        if (tthtCtoEntity.getLOAI_CTO().equals(TthtCommon.LOAI_CONGTO.DT.name()) || tthtCtoEntity.getLOAI_CTO().equals(TthtCommon.LOAI_CONGTO.HC.name()) || tthtCtoEntity.getLOAI_CTO().equals(TthtCommon.LOAI_CONGTO.VC.name())) {

            etCS3.setVisibility(View.GONE);
            etCS4.setVisibility(View.GONE);
            etCS5.setVisibility(View.GONE);

            tvCS3.setVisibility(View.GONE);
            tvCS4.setVisibility(View.GONE);
            tvCS5.setVisibility(View.GONE);

            tvCS1.setText("KT");
            tvCS2.setText("VC");

            if (CHI_SO != null && !CHI_SO.isEmpty() && CHI_SO.contains(";")) {
                etCS1.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                etCS2.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
            } else {
                etCS1.setHint("");
                etCS2.setHint("");
            }

        }

        //TODO set spinner
        ArrayAdapter<String> adapterSoVien = new ArrayAdapter<>(getActivity(),
                R.layout.view_spinner_item, TthtCommon.arrSoVien);
        adapterSoVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSoVienChiKDinh.setAdapter(adapterSoVien);
        int posSoVienChiKDinh = Arrays.asList(TthtCommon.arrSoVien).indexOf(tthtCtoEntity.getSOVIEN_CHIKDINH() + "");
        spinSoVienChiKDinh.setSelection(posSoVienChiKDinh);

        spinSoVienChiHom.setAdapter(adapterSoVien);
        int posSoVienChiHom = Arrays.asList(TthtCommon.arrSoVien).indexOf(tthtCtoEntity.getSOVIEN_CHOM() + "");
        spinSoVienChiHom.setSelection(posSoVienChiHom);

        spinSoVienChiBooc.setAdapter(adapterSoVien);
        int posSoVienChiBooc = Arrays.asList(TthtCommon.arrSoVien).indexOf(tthtCtoEntity.getSOVIEN_CBOOC() + "");
        spinSoVienChiBooc.setSelection(posSoVienChiBooc);

        ArrayAdapter<String> adapterLoaiHom = new ArrayAdapter<>(getActivity(),
                R.layout.view_spinner_item, TthtCommon.arrLoaiHom);
        adapterLoaiHom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLoaiHom.setAdapter(adapterLoaiHom);
        int posLoaiHom = Arrays.asList(TthtCommon.arrLoaiHom).indexOf(tthtCtoEntity.getLOAI_HOM() + "");
        spinLoaiHom.setSelection(posLoaiHom);

        List<String> phuongThucDoXaList = new ArrayList<String>();
        if (!ALL_PHUONG_THUC_DO_XA.equals("")) {
            String[] itemPhuongThucDoXa = ALL_PHUONG_THUC_DO_XA.trim().split(",");
            phuongThucDoXaList.addAll(Arrays.asList(itemPhuongThucDoXa));
        }

        ArrayAdapter<String> adapterPhuongThucDoXa = new ArrayAdapter<String>(getActivity(), R.layout.view_spinner_item, phuongThucDoXaList);
        adapterPhuongThucDoXa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPhuongThucDoXa.setAdapter(adapterPhuongThucDoXa);

        //TODO set image
        int ID_CHITIET_CTO = tthtCtoEntity.getID_CHITIET_CTO();

        ivChupCongTo.setImageBitmap(null);
        ivChupCongToNiemPhong.setImageBitmap(null);

        Cursor cursorAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
        if (cursorAnhCongTo.moveToFirst()) {
            TEN_ANH_CONG_TO = cursorAnhCongTo.getString(cursorAnhCongTo.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivChupCongTo.setImageBitmap(bitmap);
            }
            cursorAnhCongTo.close();
        } else {
            ivChupCongTo.setImageBitmap(null);
        }


        Cursor cursorAnhNiemPhongCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
        if (cursorAnhNiemPhongCongTo.moveToFirst()) {
            TEN_ANH_CONG_TO_NIEMPHONG = cursorAnhNiemPhongCongTo.getString(cursorAnhCongTo.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO_NIEMPHONG;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivChupCongToNiemPhong.setImageBitmap(bitmap);
            }
            cursorAnhNiemPhongCongTo.close();
        } else {
            ivChupCongToNiemPhong.setImageBitmap(null);
        }
    }

    private void initView(final View rootView) {
        //TODO init textview
        tvTenKH = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvTenKH);
        tvDiaChiKH = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvDiaChiKH);
        tvLyDoTreoThao = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvLyDoTreoThao);
        tvMaGCS = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvMaGCS);
        tvSoNo = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvSoNo);
        tvTenLoaiCto = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvTenLoaiCTo);
        tvNhanVienTrTh = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvNVienTreoThao);
        tvmaKH = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvMaKH);
        tvViTriLapDat = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvViTriLapDat);
        tvTramCapDien = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvTramCapDien);
        tvLoaiCto = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvLoaiCTo);
        tvHeSoNhan = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvHeSoNhan);
        tvNuocSX = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvNuocSX);
        tvNamSX = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvNamSX);
        tvNgayKiemDinh = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvNgayKDinh);
        tvDongDien = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvDongDien);
        tvMaTemKDinh = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvMaTemKDinh);
        tvDienAp = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvDienAp);
        tvHeSoK = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvHeSoK);
        tvCCX = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvCCX);
        tvLapQuaTu = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvLApQuaTu);
        tvLapQuaTi = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvLApQuaTi);
        tvNhaCungCap = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_tvNhaCungCap);
        tvKimNiemChi = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etKimNiemChi);
        tvMaChiKDinh = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etMaChiKDinh);
        tvMaChiHop = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etMaChiHop);
        tvMaChiBooc = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etMaChiBooc);
        tvCS1 = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_titleCs1);
        tvCS2 = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_titleCs2);
        tvCS3 = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_titleCs3);
        tvCS4 = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_titleCs4);
        tvCS5 = (TextView) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_titleCs5);

        //TODO init editText
        etTemCongQuang = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etTemCongQuang);
        etLanCanhBao = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etLanCanhBao);
        etTTrangNiemPhong = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etTTNiemPhong);
        etGhiChu = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_etGhiChu);
        etCS1 = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_cs1);
        etCS2 = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_cs2);
        etCS3 = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_cs3);
        etCS4 = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_cs4);
        etCS5 = (EditText) rootView.findViewById(R.id.ttht_frag_ghiCongTo_et_cs5);

        //TODO init Spinner
        spinSoVienChiKDinh = (Spinner) rootView.findViewById(R.id.ttht_frag_ghiCongTo_spSoVienChiKDinh);
        spinSoVienChiHom = (Spinner) rootView.findViewById(R.id.ttht_frag_ghiCongTo_spSoVienChiHop);
        spinLoaiHom = (Spinner) rootView.findViewById(R.id.ttht_frag_ghiCongTo_spLoaiHom);
        spinSoVienChiBooc = (Spinner) rootView.findViewById(R.id.ttht_frag_ghiCongTo_spSoVienChiBooc);
        spinPhuongThucDoXa = (Spinner) rootView.findViewById(R.id.ttht_frag_ghiCongTo_spinPhuongThucDoXa);

        //TODO init image View
        ivChupCongTo = (ImageView) rootView.findViewById(R.id.fragment_ttht_ghi_cong_to_ivChupCongTo);
        ivChupCongToNiemPhong = (ImageView) rootView.findViewById(R.id.fragment_ttht_ghi_cong_to_ivChupCongTo_niemphong);


        //TODO init button
        btExpand1 = (Button) rootView.findViewById(R.id.onClickExpand1);
        btExpand2 = (Button) rootView.findViewById(R.id.onClickExpand2);
        btExpand5 = (Button) rootView.findViewById(R.id.onClickExpand5);
        btExpand5_1 = (Button) rootView.findViewById(R.id.onClickExpand5_1);

        btChupCongTo = (Button) rootView.findViewById(R.id.fragment_ttht_ghi_cong_to_chupCongTo);
        btChupCongToNiemPhong = (Button) rootView.findViewById(R.id.fragment_ttht_ghi_cong_to_chupCongTo_niemphong);

        //TODO init linear layout
        llExpand1 = (LinearLayout) rootView.findViewById(R.id.ll_expand1);
        llExpand2 = (LinearLayout) rootView.findViewById(R.id.ll_expand2);
        llExpand5 = (LinearLayout) rootView.findViewById(R.id.ll_expand5);
        llExpand5_1 = (LinearLayout) rootView.findViewById(R.id.ll_expand5_1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            String MA_DVIQLY = tthtBBanEntity.getMA_DVIQLY();
            String MA_TRAM = tthtBBanEntity.getMA_TRAM();
            int ID_BBAN_TRTH = tthtBBanEntity.getID_BBAN_TRTH();
            String SO_CTO = tthtCtoEntity.getSO_CTO();
            this.DATE_TIME_TO_CREATE_FILE = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy.toString());
            this.DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.yyyyMMddTHHmmss_Slash_Colon.toString());
            // TODO thông tin thêm để hiển thị ảnh
            if (requestCode == TthtCommon.CAMERA_REQUEST_CONGTO) {
                //TODO fill data
                this.TEN_ANH_CONG_TO = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO.name(), DATE_TIME_TO_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO;
                TthtCommon.scaleImage(PATH_ANH, getActivity());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                try {
                    setImageToView(bitmap);
                    isRefreshImageCongTo = true;
                } catch (Exception e) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi (401)", Color.WHITE,
                            e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                }
            }

            if (requestCode == TthtCommon.CAMERA_REQUEST_CONGTO_NIEMPHONG) {
                this.TEN_ANH_CONG_TO_NIEMPHONG = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), DATE_TIME_TO_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO_NIEMPHONG;
                TthtCommon.scaleImage(PATH_ANH, getActivity());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                try {
                    setImageAnhNiemPhongCToView(bitmap);
                    isRefreshImageNiemPhongCongTo= true;
                } catch (Exception e) {
                    Common.showAlertDialogGreen(getActivity(), "Lỗi (401)", Color.WHITE,
                            e.getMessage(), Color.WHITE, "OK", Color.WHITE);
                }
            }
        }
    }

    private void deleteImage(String PATH_ANH) {
        File file = new File(PATH_ANH);
        if (file.exists())
            file.delete();
    }

    private void setImageToView(Bitmap bitmap) throws Exception {
        ivChupCongTo.setImageBitmap(bitmap);
    }

    private void setImageAnhNiemPhongCToView(Bitmap bitmap) throws Exception {
        ivChupCongToNiemPhong.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        Bitmap bitmap = null;
        switch (view.getId()) {
            case R.id.onClickExpand1:
                if (llExpand1.getVisibility() == View.VISIBLE) {
                    llExpand1.setVisibility(View.GONE);
                    btExpand1.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand1.setVisibility(View.VISIBLE);
                    btExpand1.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand1.setCompoundDrawablePadding(10);
                btExpand1.setPadding(10, 0, 0, 0);
                break;

            case R.id.onClickExpand2:
                if (llExpand2.getVisibility() == View.VISIBLE) {
                    llExpand2.setVisibility(View.GONE);
                    btExpand2.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand2.setVisibility(View.VISIBLE);
                    btExpand2.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand2.setCompoundDrawablePadding(10);
                btExpand2.setPadding(10, 0, 0, 0);
                break;

            case R.id.onClickExpand5:
                if (llExpand5.getVisibility() == View.VISIBLE) {
                    llExpand5.setVisibility(View.GONE);
                    btExpand5.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand5.setVisibility(View.VISIBLE);
                    btExpand5.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand5.setPadding(10, 0, 0, 0);
                btExpand5.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand5_1:
                if (llExpand5_1.getVisibility() == View.VISIBLE) {
                    llExpand5_1.setVisibility(View.GONE);
                    btExpand5_1.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand5_1.setVisibility(View.VISIBLE);
                    btExpand5_1.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand5_1.setPadding(10, 0, 0, 0);
                btExpand5_1.setCompoundDrawablePadding(10);
                break;


            case R.id.fragment_ttht_ghi_cong_to_chupCongTo:
                chupAnhCongTo();
                break;

            case R.id.fragment_ttht_ghi_cong_to_chupCongTo_niemphong:
                chupAnhCongToNiemPhong();
                break;

            case R.id.fragment_ttht_ghi_cong_to_ivChupCongTo:
                bitmap = (ivChupCongTo.getDrawable() == null) ? null : ((BitmapDrawable) ivChupCongTo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.fragment_ttht_ghi_cong_to_ivChupCongTo_niemphong:
                bitmap = (ivChupCongToNiemPhong.getDrawable() == null) ? null : ((BitmapDrawable) ivChupCongToNiemPhong.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;
        }
    }

    private void chupAnhCongToNiemPhong() {
        try {

            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            String DATETIME = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy.toString());
            String MA_DVIQLY = tthtBBanEntity.getMA_DVIQLY();
            String MA_TRAM = tthtBBanEntity.getMA_TRAM();
            int ID_BBAN_TRTH = tthtBBanEntity.getID_BBAN_TRTH();
            String SO_CTO = tthtCtoEntity.getSO_CTO();
            final String fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name())
                    + "/"
                    + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_CONGTO_NIEMPHONG);
        } catch (IOException ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi (402)", Color.RED, "Lỗi lưu ảnh niêm phong Công tơ\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }


    private void chupAnhCongTo() {
        try {
            CHI_SO = new StringBuilder();
           /* if (etCS1.getText().toString().isEmpty() &&
                    etCS2.getText().toString().isEmpty() &&
                    etCS3.getText().toString().isEmpty() &&
                    etCS4.getText().toString().isEmpty() &&
                    etCS5.getText().toString().isEmpty() &&
                    etCS1.getHint().toString().isEmpty() &&
                    etCS2.getHint().toString().isEmpty() &&
                    etCS3.getHint().toString().isEmpty() &&
                    etCS4.getHint().toString().isEmpty() &&
                    etCS5.getHint().toString().isEmpty()
                    ) {
                Common.showAlertDialogGreen(getActivity(), "Thông báo", Color.WHITE,
                        "Bạn cần nhập chỉ số trước khi chụp!", Color.WHITE, "OK", Color.WHITE);
                return;
            }*/

            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            String DATETIME = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy.toString());
            String MA_DVIQLY = tthtBBanEntity.getMA_DVIQLY();
            String MA_TRAM = tthtBBanEntity.getMA_TRAM();
            int ID_BBAN_TRTH = tthtBBanEntity.getID_BBAN_TRTH();
            String SO_CTO = tthtCtoEntity.getSO_CTO();
            final String fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name())
                    + "/"
                    + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_CONGTO);
        } catch (IOException ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi (402)", Color.RED, "Lỗi lưu ảnh Công tơ\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public boolean checkWriteDataCongToComplete(final TthtGhiCongToFragment fragment) throws Exception {
        if (fragment != null && fragment == this) {
            Bitmap bitmapCongTo = (ivChupCongTo.getDrawable() == null) ? null : ((BitmapDrawable) ivChupCongTo.getDrawable()).getBitmap();
            Bitmap bitmapCongToNiemPhong = (ivChupCongToNiemPhong.getDrawable() == null) ? null : ((BitmapDrawable) ivChupCongToNiemPhong.getDrawable()).getBitmap();
            String etCS1Text = (etCS1.getHint() == null) ? "0" : etCS1.getHint().toString();
            String etCS1Hint = (etCS1.getHint() == null) ? "0" : etCS1.getHint().toString();
            String etCS2Text = (etCS2.getHint() == null) ? "0" : etCS2.getHint().toString();
            String etCS2Hint = (etCS2.getHint() == null) ? "0" : etCS2.getHint().toString();
            String etCS3Text = (etCS3.getHint() == null) ? "0" : etCS3.getHint().toString();
            String etCS3Hint = (etCS3.getHint() == null) ? "0" : etCS3.getHint().toString();

            String etCS4Text = (etCS4.getHint() == null) ? "0" : etCS4.getHint().toString();
            String etCS4Hint = (etCS4.getHint() == null) ? "0" : etCS4.getHint().toString();
            String etCS5Text = (etCS5.getHint() == null) ? "0" : etCS5.getHint().toString();
            String etCS5Hint = (etCS5.getHint() == null) ? "0" : etCS5.getHint().toString();

            if (bitmapCongToNiemPhong == null)
                throw new Exception("Vui lòng chụp ảnh niêm phong công tơ!");

            if (bitmapCongTo == null)
                throw new Exception("Vui lòng chụp ảnh công tơ!");

//            if (etCS1Text.isEmpty() && etCS2Text.isEmpty() && etCS3Text.isEmpty() && etCS4Text.isEmpty() && etCS5Text.isEmpty()) {
//                if (etCS1Hint.isEmpty() && etCS2Hint.isEmpty() && etCS3Hint.isEmpty() && etCS4Hint.isEmpty() && etCS5Hint.isEmpty()) {
//                    throw new Exception("Vui lòng nhập chỉ số!");
//                }
//            } else {
            etCS1.setHint(etCS1Text.isEmpty() ? etCS1Hint : etCS1Text);
            etCS2.setHint(etCS2Text.isEmpty() ? etCS2Hint : etCS2Text);
            etCS3.setHint(etCS3Text.isEmpty() ? etCS3Hint : etCS3Text);
            etCS4.setHint(etCS4Text.isEmpty() ? etCS4Hint : etCS4Text);
            etCS5.setHint(etCS5Text.isEmpty() ? etCS5Hint : etCS5Text);
//            }
        } else {
            throw new Exception("Lỗi gọi dữ liệu casting fragment!");
        }
        return true;
    }

    public void saveDataCongTo() throws Exception {
        //TODO  kiểm tra dữ liệu cần thiết để ghi
        //TODO thông tin thêm để vẽ lên ảnh
        String MA_DVIQLY = tthtBBanEntity.getMA_DVIQLY();
        String SO_CTO = tthtCtoEntity.getSO_CTO();
        int ID_BBAN_TRTH = tthtBBanEntity.getID_BBAN_TRTH();
        int ID_CHITIET_CTO = tthtCtoEntity.getID_CHITIET_CTO();
        int ID_BBAN_TUTI = 0;
        int ID_CHITIET_TUTI = 0;
        MA_BDONG = tthtCtoEntity.getMA_BDONG();


        if (TEN_ANH_CONG_TO.isEmpty()) {
            Cursor cursorAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
            if (cursorAnhCongTo.moveToFirst()) {
                TEN_ANH_CONG_TO = cursorAnhCongTo.getString(cursorAnhCongTo.getColumnIndex("TEN_ANH"));
                cursorAnhCongTo.close();
            }
        }
        if (TEN_ANH_CONG_TO.isEmpty())
            throw new Exception("Không có ảnh trong dữ liệu database!");
        String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO;


        String TEN_KHANG = tthtBBanEntity.getTEN_KHANG();
        String MA_DDO = tthtBBanEntity.getMA_DDO();
        DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.yyyyMMddTHHmmss_Slash_Colon.toString());

        //TODO nếu rỗng nhập thì lấy Hint set text = hint ngược lại lấy text set Hint = text
        if (etCS1.getText().toString().isEmpty()
                && etCS2.getText().toString().isEmpty()
                && etCS3.getText().toString().isEmpty()
                && etCS4.getText().toString().isEmpty()
                && etCS5.getText().toString().isEmpty()) {

            CHI_SO = new StringBuilder(
                    TthtCommon.getStringChiSo(
                            etCS1.getHint().toString(),
                            etCS2.getHint().toString(),
                            etCS3.getHint().toString(),
                            etCS4.getHint().toString(),
                            etCS5.getHint().toString(),
                            tthtCtoEntity.getLOAI_CTO()));
            etCS1.setText(etCS1.getHint().toString());
            etCS2.setText(etCS2.getHint().toString());
            etCS3.setText(etCS3.getHint().toString());
            etCS4.setText(etCS4.getHint().toString());
            etCS5.setText(etCS5.getHint().toString());
        } else {
            String hint = etCS1.getHint().toString();
            String hint1 = etCS2.getHint().toString();
            String hint2 = etCS3.getHint().toString();
            String hint3 = etCS4.getHint().toString();
            String hint4 = etCS5.getHint().toString();
            CHI_SO = new StringBuilder(
                    TthtCommon.getStringChiSo(
                            (etCS1.getText().toString().isEmpty()) ? etCS1.getHint().toString() : etCS1.getText().toString(),
                            (etCS2.getText().toString().isEmpty()) ? etCS2.getHint().toString() : etCS2.getText().toString(),
                            (etCS3.getText().toString().isEmpty()) ? etCS3.getHint().toString() : etCS3.getText().toString(),
                            (etCS4.getText().toString().isEmpty()) ? etCS4.getHint().toString() : etCS4.getText().toString(),
                            (etCS5.getText().toString().isEmpty()) ? etCS5.getHint().toString() : etCS5.getText().toString(),
                            tthtCtoEntity.getLOAI_CTO()));
            etCS1.setText((etCS1.getText().toString().isEmpty()) ? etCS1.getHint().toString() : etCS1.getText().toString());
            etCS2.setText((etCS2.getText().toString().isEmpty()) ? etCS2.getHint().toString() : etCS2.getText().toString());
            etCS3.setText((etCS3.getText().toString().isEmpty()) ? etCS3.getHint().toString() : etCS3.getText().toString());
            etCS4.setText((etCS4.getText().toString().isEmpty()) ? etCS4.getHint().toString() : etCS4.getText().toString());
            etCS5.setText((etCS5.getText().toString().isEmpty()) ? etCS5.getHint().toString() : etCS5.getText().toString());

        }

        String TYPE_IMAGE_DRAW = (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) ? "ẢNH CÔNG TƠ TREO" : "ẢNH CÔNG TƠ THÁO";
        String CHI_SO_DRAW = (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) ? "CS TREO: " + CHI_SO.toString() : "CS THÁO: " + CHI_SO.toString();
        String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
        String SO_CTO_DRAW = "SỐ C.TƠ:" + SO_CTO;
        String DATE_DRAW = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy_Slash.toString());
        long rowAffect = 0;
        //TODO kiểm tra ảnh trong dữ liệu và xóa dữ liệu ảnh cũ
        Cursor cusorCheckImage = connection.checkAnh(ID_CHITIET_TUTI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
        if (cusorCheckImage != null && cusorCheckImage.getCount() > 0) {
            connection.deleteAnh(ID_CHITIET_TUTI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
        }
        rowAffect = connection.insertDataAnh(ID_CHITIET_TUTI, TEN_ANH_CONG_TO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO, DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO);
        if (rowAffect > 0) {
            //TODO fill data
            Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, CHI_SO_DRAW, SO_CTO_DRAW, MA_DDO_DRAW);
            setImageToView(bitmap);

        } else
            throw new Exception("Lỗi ghi vào database dữ liệu ảnh công tơ!");

        //ghi ảnh niêm phong

        if (TEN_ANH_CONG_TO_NIEMPHONG.isEmpty()) {
            Cursor cursorAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
            if (cursorAnhCongTo.moveToFirst()) {
                TEN_ANH_CONG_TO_NIEMPHONG = cursorAnhCongTo.getString(cursorAnhCongTo.getColumnIndex("TEN_ANH"));
                cursorAnhCongTo.close();
            }
        }
        if (TEN_ANH_CONG_TO_NIEMPHONG.isEmpty())
            throw new Exception("Không có ảnh trong dữ liệu database!");
        PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO_NIEMPHONG;

        TYPE_IMAGE_DRAW = (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) ? "ẢNH NIÊM PHONG CTƠ TREO" : "ẢNH NIÊM PHONG CTƠ THÁO";
//         CHI_SO_DRAW = (MA_BDONG.equals(TthtCommon.arrMaBDong[0])) ? "CS TREO: " + CHI_SO.toString() : "CS THÁO: " + CHI_SO.toString();
        MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
        SO_CTO_DRAW = "SỐ C.TƠ:" + SO_CTO;
        DATE_DRAW = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy_Slash.toString());

        //TODO kiểm tra ảnh trong dữ liệu và xóa dữ liệu ảnh cũ
        cusorCheckImage = connection.checkAnh(ID_CHITIET_TUTI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
        if (cusorCheckImage != null && cusorCheckImage.getCount() > 0) {
            connection.deleteAnh(ID_CHITIET_TUTI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
        }
        rowAffect = connection.insertDataAnh(ID_CHITIET_TUTI, TEN_ANH_CONG_TO_NIEMPHONG, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG, DATE_TIME_TO_INPUT_SQL_ANH_CONG_TO);
        if (rowAffect > 0) {
            //TODO fill data
            Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, "", SO_CTO_DRAW, MA_DDO_DRAW);
            setImageAnhNiemPhongCToView(bitmap);

        } else
            throw new Exception("Lỗi ghi vào database dữ liệu ảnh niêm phong công tơ!");

        //TODO ghi dữ liệu vào database
        String etTemCongQuangText = etTemCongQuang.getText().toString();
        String etLanCanhBaoText = etLanCanhBao.getText().toString();
        String etSoKimNiemChi = tvKimNiemChi.getText().toString();
        String etMaChiKDinhText = tvMaChiKDinh.getText().toString();
        String etMaChiHopText = tvMaChiHop.getText().toString();
        String etMaChiBoocText = tvMaChiBooc.getText().toString();
        String soVienChiKDinh = spinSoVienChiKDinh.getSelectedItem().toString();
        String soVienChiHom = spinSoVienChiHom.getSelectedItem().toString();
        String soVienChiBooc = spinSoVienChiBooc.getSelectedItem().toString();
        String etTTrangNiemPhongText = etTTrangNiemPhong.getText().toString();
        String loaiHom = spinLoaiHom.getSelectedItem().toString();
        String phuongThucDoXa = spinPhuongThucDoXa.getSelectedItem().toString();
        String ghiChu = etGhiChu.getText().toString();

        rowAffect = connection.updateCongTo(MA_BDONG, String.valueOf(ID_BBAN_TRTH),
                CHI_SO.toString(), etTemCongQuangText, etLanCanhBaoText, etSoKimNiemChi, etMaChiKDinhText, etMaChiHopText, etMaChiBoocText,
                soVienChiKDinh, soVienChiHom, soVienChiBooc, etTTrangNiemPhongText, loaiHom, phuongThucDoXa, ghiChu, TthtCommon.TRANG_THAI_DU_LIEU.DA_GHI.toString());

        if (rowAffect < 0) {
            throw new Exception("Lỗi ghi vào database dữ liệu công tơ!");
        }

        //TODO set hint ngay tránh lỗi họ nhấn ghi lần nữa mà không chụp
        etTemCongQuang.setHint(etTemCongQuangText);
        etTemCongQuang.setText(etTemCongQuangText);

        etLanCanhBao.setHint(etLanCanhBaoText);
        etLanCanhBao.setText(etLanCanhBaoText);

        etTTrangNiemPhong.setHint(etTTrangNiemPhongText);
        etTTrangNiemPhong.setText(etTTrangNiemPhongText);

        etGhiChu.setHint(ghiChu);
        etGhiChu.setText(ghiChu);

        isRefreshImageCongTo = false;
        isRefreshImageNiemPhongCongTo = false;
        TEN_ANH_CONG_TO = "";
        TEN_ANH_CONG_TO_NIEMPHONG = "";
    }

    public void checkSaveDataCongTo(final TthtGhiCongToFragment fragment) throws Exception {
        if (fragment != null && fragment == this) {
            //TODO nếu chụp lại ảnh mà chưa lưu thì thông báo phải lưu lại
            if (isRefreshImageCongTo) {
                throw new Exception("Thông tin Công tơ mới thay đổi vẫn chưa lưu. Bạn cần lưu thông tin Công tơ trước!");
            }

            if (isRefreshImageNiemPhongCongTo) {
                throw new Exception("Thông tin ảnh niêm phong Công tơ thay đổi vẫn chưa lưu. Cần save!");
            }
        } else {
            throw new Exception("Lỗi gọi dữ liệu casting fragment!");
        }
    }

    /* public void refreshDataCongTo(TthtKHangEntity objectTreo, TthtKHangEntity objectThao, String MA_BDONG, final TthtGhiCongToFragment fragment) throws Exception {
         if (fragment != null && fragment == this) {
             this.objectDataTreo = objectTreo;
             this.objectDataThao = objectThao;
             this.MA_BDONG = MA_BDONG;
             fillData();
         } else {
             throw new Exception("Lỗi gọi dữ liệu casting fragment!");
         }
     }
 */
    public void refreshDataCongTo(int id_bban_trth, int id_chitiet_cto) {
        if (id_bban_trth <= 0 || id_chitiet_cto <= 0)
            return;

        ID_BBAN_TRTH = id_bban_trth;
        ID_CHITIET_CTO = id_chitiet_cto;
        fillData();
    }
}
