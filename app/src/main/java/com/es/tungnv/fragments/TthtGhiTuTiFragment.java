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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.entity.TthtBBanEntity;
import com.es.tungnv.entity.TthtCtoEntity;
import com.es.tungnv.entity.TthtTuTiEntity;
import com.es.tungnv.interfaces.ICallBackDetailActivity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.views.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TthtGhiTuTiFragment extends Fragment implements View.OnClickListener {
    private ICallBackDetailActivity ICallBack;
    private String MA_BDONG;
    //    private TthtKHangEntity objectDataTreo;
//    private TthtKHangEntity objectDataThao;
    private boolean isDoiSoat = false;
    private TthtSQLiteConnection connection;
    //    private TthtKHangEntity objectData;
    private TthtTuTiEntity tuTreo, tuThao, tiTreo, tiThao;
    private TthtBBanEntity tthtBBanEntity;
    private TthtCtoEntity tthtCtoEntity;
    private List<Integer> lstID_TUTI = new ArrayList<>();
    private int ID_CHITIET_CTO;
    private int ID_BBAN_TUTI;
    private int ID_BBAN_TRTH;

    private Button
            btExpand6, btExpand7, btExpand8, btExpand9, btExpand10, btExpand11, btExpand12,
            btChupTuTreo, btChupTiTreo, btChupNhiThuTuTreo, btChupNhiThuTiTreo, btChupNiemPhongTuTreo, btChupNiemPhongTiTreo;
    private TextView
            tvTenKH, tvDiaChiKH, tvLyDoTreoThao, tvMaGCS, tvSoNo,
            tvCapDienApTuThao, tvTySoBienTuThao, tvTemKDinhTuThao, tvNgayKDinhTuThao, tvSoTuThao, tvCCXTuThao, tvNuocSXTuThao, tvSoVongTuThao, tvChiKDinhTuThao, tvChiHopTuThao,
            tvCapDienApTuTreo, tvTySoBienTuTreo, tvTemKDinhTuTreo, tvNgayKDinhTuTreo, tvSoTuTreo, tvCCXTuTreo, tvNuocSXTuTreo, tvSoVongTuTreo, tvChiKDinhTuTreo, tvChiHopTuTreo,
            tvCapDienApTiThao, tvTySoBienTiThao, tvTemKDinhTiThao, tvNgayKDinhTiThao, tvSoTiThao, tvCCXTiThao, tvNuocSXTiThao, tvSoVongTiThao, tvChiKDinhTiThao, tvChiHopTiThao,
            tvCapDienApTiTreo, tvTySoBienTiTreo, tvTemKDinhTiTreo, tvNgayKDinhTiTreo, tvSoTiTreo, tvCCXTiTreo, tvNuocSXTiTreo, tvSoVongTiTreo, tvChiKDinhTiTreo, tvChiHopTiTreo;

    private EditText
            etDongDienSauLap, etDienApSauLap, etKSauLap, etCCXSauLap,
            etPSauLap, etQSauLap, etBTSauLap, etCDSauLap, etTDSauLap, etLapQuaTuSauLap, etLapQuaTiSauLap, etHSNhanSauLap;

    private LinearLayout
            llExpand6,
            llExpand7,
            llExpand8,
            llExpand9,
            llExpand10,
            llExpand11,
            llExpand12;

    private ImageView
            ivTuTreo,
            ivTiTreo,
            ivNhiThuTuTreo,
            ivNhiThuTiTreo,
            ivNiemPhongTuTreo,
            ivNiemPhongTiTreo;

    //TODO biến được lưu để sử dụng
    private String
            DATE_TIME_FOR_CREATE_FILE,
            DATE_TIME_TO_INPUT_SQL,
            TEN_ANH_TU = "",
            TEN_ANH_TI = "",
            TEN_ANH_NHI_THU_TU = "",
            TEN_ANH_NHI_THU_TI = "",
            TEN_ANH_NIEM_PHONG_TU = "",
            TEN_ANH_NIEM_PHONG_TI = "";

    private boolean isRefreshImageTU = false;
    private boolean isRefreshImageTI = false;
    private boolean isRefreshImageNhiThuTu = false;
    private boolean isRefreshImageNhiThuTi = false;
    private boolean isRefreshImageNiemPhongTu = false;
    private boolean isRefreshImageNiemPhongTi = false;

    public TthtGhiTuTiFragment() {
        // Required empty public constructor
    }

    public String getMA_BDONG() {
        return MA_BDONG;
    }

    public void setMA_BDONG(String MA_BDONG) {
        this.MA_BDONG = MA_BDONG;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode != 0) {
                // TODO thông tin thêm để hiển thị ảnh
                String MA_DVIQLY = "";
                String MA_TRAM = "";
                String MA_CTO = "";
                Cursor c = connection.getBBanByIDBBanTRTH(ID_BBAN_TRTH);
                if (c != null) {
                    c.moveToFirst();
                    MA_DVIQLY = c.getString(c.getColumnIndex("MA_DVIQLY"));
                    MA_TRAM = c.getString(c.getColumnIndex("MA_TRAM"));
                }

                Cursor c1 = connection.getCToByID_CHITIET_CTO(ID_CHITIET_CTO);
                if(c1 != null) {
                    c1.moveToFirst();
                    MA_CTO = c1.getString(c1.getColumnIndex("MA_CTO"));
                }
                this.DATE_TIME_FOR_CREATE_FILE = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy.toString());
                this.DATE_TIME_TO_INPUT_SQL = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.yyyyMMddTHHmmss_Slash_Colon.toString());
                String PATH_ANH = "";
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if (requestCode == TthtCommon.CAMERA_REQUEST_TU) {
                    //TODO fill data
                    this.TEN_ANH_TU = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_TU.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                        isRefreshImageTU = true;
                    }
                }
                if (requestCode == TthtCommon.CAMERA_REQUEST_TI) {
                    //TODO fill data
                    this.TEN_ANH_TI = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_TI.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                        isRefreshImageTI = true;
                    }
                }
                if (requestCode == TthtCommon.CAMERA_REQUEST_NHI_THU_TU) {
                    //TODO fill data
                    this.TEN_ANH_NHI_THU_TU = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_NHI_THU_TU;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                        isRefreshImageNhiThuTu = true;
                    }
                }

                if (requestCode == TthtCommon.CAMERA_REQUEST_NHI_THU_TI) {
                    //TODO fill data
                    this.TEN_ANH_NHI_THU_TI = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_NHI_THU_TI;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
                        isRefreshImageNhiThuTi = true;
                    }
                }

                if (requestCode == TthtCommon.CAMERA_REQUEST_NIEM_PHONG_TU) {
                    //TODO fill data
                    this.TEN_ANH_NIEM_PHONG_TU = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_NIEM_PHONG_TU;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                        isRefreshImageNiemPhongTu = true;
                    }
                }

                if (requestCode == TthtCommon.CAMERA_REQUEST_NIEM_PHONG_TI) {
                    //TODO fill data
                    this.TEN_ANH_NIEM_PHONG_TI = TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI.name(), DATE_TIME_FOR_CREATE_FILE, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
                    PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_NIEM_PHONG_TI;
                    TthtCommon.scaleImage(PATH_ANH, getActivity());
                    Bitmap bitmap = BitmapFactory.decodeFile(PATH_ANH, options);
                    if (bitmap != null) {
                        setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                        isRefreshImageNiemPhongTi = true;
                    }
                }

            }
        } catch (Exception e) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi (393)", Color.RED, e.getMessage(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void setImageToView(Bitmap bitmap, TthtCommon.TYPE_IMAGE typeImage) {
        if (bitmap == null)
            return;
        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TU) {
            ivTuTreo.setImageBitmap(bitmap);
        }
        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TI) {
            ivTiTreo.setImageBitmap(bitmap);
        }

        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) {
            ivNhiThuTuTreo.setImageBitmap(bitmap);
        }

        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI) {
            ivNhiThuTiTreo.setImageBitmap(bitmap);
        }

        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) {
            ivNiemPhongTuTreo.setImageBitmap(bitmap);
        }

        if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI) {
            ivNiemPhongTiTreo.setImageBitmap(bitmap);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ttht_ghi_tu_ti, container, false);

        //TODO get bundle
        getBundle();
        initView(view);
        setAction(view);
        fillData();

        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        MA_BDONG = bundle.getString("MA_BDONG", "");
        ID_BBAN_TRTH = bundle.getInt("ID_BBAN_TRTH", 0);
        ID_CHITIET_CTO = bundle.getInt("ID_CHITIET_CTO", 0);
        isDoiSoat = bundle.getBoolean("IS_DOI_SOAT");
    }

    private void setAction(View view) {
        //TODO set onclick
        btExpand6.setOnClickListener(this);
        btExpand7.setOnClickListener(this);
        btExpand8.setOnClickListener(this);
        btExpand9.setOnClickListener(this);
        btExpand10.setOnClickListener(this);
        btExpand11.setOnClickListener(this);
        btExpand12.setOnClickListener(this);

        btChupTuTreo.setOnClickListener(this);
        btChupTiTreo.setOnClickListener(this);
        btChupNhiThuTuTreo.setOnClickListener(this);
        btChupNhiThuTiTreo.setOnClickListener(this);
        btChupNiemPhongTuTreo.setOnClickListener(this);
        btChupNiemPhongTiTreo.setOnClickListener(this);
        ivTuTreo.setOnClickListener(this);
        ivTiTreo.setOnClickListener(this);
        ivNhiThuTuTreo.setOnClickListener(this);
        ivNhiThuTiTreo.setOnClickListener(this);
        ivNiemPhongTuTreo.setOnClickListener(this);
        ivNiemPhongTiTreo.setOnClickListener(this);

        //TODO set visible view at first time
        llExpand6.setVisibility(View.GONE);
        btExpand6.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand6.setCompoundDrawablePadding(10);
        btExpand6.setPadding(10, 0, 0, 0);
        llExpand7.setVisibility(View.GONE);
        btExpand7.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand7.setCompoundDrawablePadding(10);
        btExpand7.setPadding(10, 0, 0, 0);
        llExpand8.setVisibility(View.GONE);
        btExpand8.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand8.setCompoundDrawablePadding(10);
        btExpand8.setPadding(10, 0, 0, 0);
        llExpand9.setVisibility(View.GONE);
        btExpand9.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand9.setCompoundDrawablePadding(10);
        btExpand9.setPadding(10, 0, 0, 0);
        llExpand10.setVisibility(View.GONE);
        btExpand10.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand10.setCompoundDrawablePadding(10);
        btExpand10.setPadding(10, 0, 0, 0);
        llExpand11.setVisibility(View.GONE);
        btExpand11.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand11.setCompoundDrawablePadding(10);
        btExpand11.setPadding(10, 0, 0, 0);
        llExpand12.setVisibility(View.GONE);
        btExpand12.setBackgroundResource(R.drawable.title_elasticity_full);
        btExpand12.setCompoundDrawablePadding(10);
        btExpand12.setPadding(10, 0, 0, 0);
    }

    private void fillData() {
        //TODO Xóa hết biến tạm lưu dữ liệu

        DATE_TIME_FOR_CREATE_FILE = DATE_TIME_TO_INPUT_SQL = "";
        isRefreshImageTU = false;
        isRefreshImageTI = false;
        isRefreshImageNhiThuTu = false;
        isRefreshImageNhiThuTi = false;
        isRefreshImageNiemPhongTu = false;
        isRefreshImageNiemPhongTi = false;

        connection = TthtSQLiteConnection.getInstance(getActivity());

        Cursor cursorGetDataTUTI = connection.getDataTUTI(ID_BBAN_TUTI);
        if (cursorGetDataTUTI != null && cursorGetDataTUTI.getCount() != 0) {
            do {
                boolean IS_TU = Boolean.parseBoolean(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("IS_TU")));
                String MA_BDONG_TUTI = cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_BDONG"));

                if (IS_TU) {
                    if (MA_BDONG_TUTI.equalsIgnoreCase(TthtCommon.arrMaBDong[0])) {
                        tuTreo = new TthtTuTiEntity();
                        setDataTUTI(tuTreo, cursorGetDataTUTI, IS_TU, MA_BDONG_TUTI);
                    } else {
                        tuThao = new TthtTuTiEntity();
                        setDataTUTI(tuThao, cursorGetDataTUTI, IS_TU, MA_BDONG_TUTI);
                    }
                } else {
                    if (MA_BDONG_TUTI.equalsIgnoreCase(TthtCommon.arrMaBDong[1])) {
                        tiTreo = new TthtTuTiEntity();
                        setDataTUTI(tiTreo, cursorGetDataTUTI, IS_TU, MA_BDONG_TUTI);
                    } else {
                        tiThao = new TthtTuTiEntity();
                        setDataTUTI(tiThao, cursorGetDataTUTI, IS_TU, MA_BDONG_TUTI);
                    }
                }
            } while (cursorGetDataTUTI.moveToNext());
        }

        //lấy TRANG_THAI_DU_LIEU
        Cursor c = connection.getTRANG_THAI_DU_LIEU(ID_CHITIET_CTO);

        int TRANG_THAI_DU_LIEU = c.getInt(c.getColumnIndex("TRANG_THAI_DU_LIEU"));
        if (TRANG_THAI_DU_LIEU == 2) {
            btChupTuTreo.setEnabled(false);
            btChupNhiThuTuTreo.setEnabled(false);
            btChupNiemPhongTuTreo.setEnabled(false);
            btChupTiTreo.setEnabled(false);
            btChupNhiThuTiTreo.setEnabled(false);
            btChupNiemPhongTiTreo.setEnabled(false);
        } else {
            btChupTuTreo.setEnabled(true);
            btChupNhiThuTuTreo.setEnabled(true);
            btChupNiemPhongTuTreo.setEnabled(true);
            btChupTiTreo.setEnabled(true);
            btChupNhiThuTiTreo.setEnabled(true);
            btChupNiemPhongTiTreo.setEnabled(true);
        }

        if (isDoiSoat) {
            btChupTuTreo.setEnabled(false);
            btChupNhiThuTuTreo.setEnabled(false);
            btChupNiemPhongTuTreo.setEnabled(false);
            btChupTiTreo.setEnabled(false);
            btChupNhiThuTiTreo.setEnabled(false);
            btChupNiemPhongTiTreo.setEnabled(false);
        }


        //TODO check data to visible view
        if (tuThao == null) {
            btExpand6.setVisibility(View.GONE);
            llExpand6.setVisibility(View.GONE);
        }
        if (tuTreo == null) {
            btExpand7.setVisibility(View.GONE);
            llExpand7.setVisibility(View.GONE);
        }
        if (tiTreo == null) {
            btExpand8.setVisibility(View.GONE);
            llExpand8.setVisibility(View.GONE);
        }
        if (tiThao == null) {
            btExpand9.setVisibility(View.GONE);
            llExpand9.setVisibility(View.GONE);
        }

        Cursor cBban = connection.getBBanByIDBBanTRTH(ID_BBAN_TRTH);
        if (cBban != null) {
            tthtBBanEntity = new TthtBBanEntity();
            setDataBBan(tthtBBanEntity, cBban);

        }

        Cursor cCto = connection.getCToByID_CHITIET_CTO(ID_CHITIET_CTO);
        if (cCto != null) {
            tthtCtoEntity = new TthtCtoEntity();
            setDataCto(tthtCtoEntity, cCto);
        }

        //TODO set textview
        tvTenKH.setText(tthtBBanEntity.getTEN_KHANG());
        tvDiaChiKH.setText(tthtBBanEntity.getDCHI_HDON());
        tvLyDoTreoThao.setText(tthtBBanEntity.getLY_DO_TREO_THAO());
        tvMaGCS.setText(tthtBBanEntity.getMA_GCS_CTO());
        tvSoNo.setText(tthtCtoEntity.getSO_CTO());
        String NGAY_KDINH = "";
        if (tuThao != null) {
            //TODO get data Tu Thao and set textview
            tvCapDienApTuThao.setText(String.valueOf(tuThao.getCAP_DAP()));
            tvTySoBienTuThao.setText(tuThao.getTYSO_BIEN());
            tvTemKDinhTuThao.setText(tuThao.getSO_TEM_KDINH());
            NGAY_KDINH = tuThao.getNGAY_KDINH();
            NGAY_KDINH = TthtCommon.convertDateTime(NGAY_KDINH, 1);
            tvNgayKDinhTuThao.setText(NGAY_KDINH);
            tvSoTuThao.setText(tuThao.getSO_TU_TI());
            tvCCXTuThao.setText(String.valueOf(tuThao.getCAP_CXAC()));
            tvNuocSXTuThao.setText(tuThao.getNUOC_SX());
            tvSoVongTuThao.setText(String.valueOf(tuThao.getSO_VONG_THANH_CAI()));
            tvNuocSXTuThao.setText(tuThao.getNUOC_SX());
            tvChiKDinhTuThao.setText(tuThao.getMA_CHI_KDINH());
            tvChiHopTuThao.setText(tuThao.getMA_CHI_HOP_DDAY());
        }
        //TODO get data Tu Treo and set textview
        if (tuTreo != null) {
            tvCapDienApTuTreo.setText(String.valueOf(tuTreo.getCAP_DAP()));
            tvTySoBienTuTreo.setText(tuTreo.getTYSO_BIEN());
            tvTemKDinhTuTreo.setText(tuTreo.getSO_TEM_KDINH());
            NGAY_KDINH = tuTreo.getNGAY_KDINH();
            NGAY_KDINH = TthtCommon.convertDateTime(NGAY_KDINH, 1);
            tvNgayKDinhTuTreo.setText(NGAY_KDINH);
            tvSoTuTreo.setText(tuTreo.getSO_TU_TI());
            tvCCXTuTreo.setText(String.valueOf(String.valueOf(tuTreo.getCAP_CXAC())));
            tvNuocSXTuTreo.setText(tuTreo.getNUOC_SX());
            tvSoVongTuTreo.setText(tuTreo.getSO_VONG_THANH_CAI() + "");
            tvNuocSXTuTreo.setText(tuTreo.getNUOC_SX());
            tvChiKDinhTuTreo.setText(tuTreo.getMA_CHI_KDINH());
            tvChiHopTuTreo.setText(tuTreo.getMA_CHI_HOP_DDAY());
        }
        //TODO get data Ti Thao and set textview.
        if (tiThao != null) {
            tvCapDienApTiThao.setText(tiThao.getCAP_DAP() + "");
            tvTySoBienTiThao.setText(tiThao.getTYSO_BIEN());
            tvTemKDinhTiThao.setText(tiThao.getSO_TEM_KDINH());
            NGAY_KDINH = tiThao.getNGAY_KDINH();
            NGAY_KDINH = TthtCommon.convertDateTime(NGAY_KDINH, 1);
            tvNgayKDinhTiThao.setText(NGAY_KDINH);
            tvSoTiThao.setText(tiThao.getSO_TU_TI());
            tvCCXTiThao.setText(tiThao.getCAP_CXAC() + "");
            tvNuocSXTiThao.setText(tiThao.getNUOC_SX());
            tvSoVongTiThao.setText(tiThao.getSO_VONG_THANH_CAI() + "");
            tvNuocSXTiThao.setText(tiThao.getNUOC_SX());
            tvChiKDinhTiThao.setText(tiThao.getMA_CHI_KDINH());
            tvChiHopTiThao.setText(tiThao.getMA_CHI_HOP_DDAY());
        }
        //TODO get data Ti Treo and set textview
        if (tiTreo != null) {
            tvCapDienApTiTreo.setText(tiTreo.getCAP_DAP() + "");
            tvTySoBienTiTreo.setText(tiTreo.getTYSO_BIEN());
            tvTemKDinhTiTreo.setText(tiTreo.getSO_TEM_KDINH());
            NGAY_KDINH = tiTreo.getNGAY_KDINH();
            NGAY_KDINH = TthtCommon.convertDateTime(NGAY_KDINH, 1);
            tvNgayKDinhTiTreo.setText(NGAY_KDINH);
            tvSoTiTreo.setText(tiTreo.getSO_TU_TI());
            tvCCXTiTreo.setText(tiTreo.getCAP_CXAC() + "");
            tvNuocSXTiTreo.setText(tiTreo.getNUOC_SX());
            tvSoVongTiTreo.setText(tiTreo.getSO_VONG_THANH_CAI() + "");
            tvNuocSXTiTreo.setText(tiTreo.getNUOC_SX());
            tvChiKDinhTiTreo.setText(tiTreo.getMA_CHI_KDINH());
            tvChiHopTiTreo.setText(tiTreo.getMA_CHI_HOP_DDAY());
        }

        //TODO set EditText
        etDongDienSauLap.setText("");
        etDienApSauLap.setText("");
        etKSauLap.setText("");
        etCCXSauLap.setText("");
        //P sau lap = SG
        //Q sau lap = VC
        etPSauLap.setText("");
        etQSauLap.setText("");

        etBTSauLap.setText("");
        etCDSauLap.setText("");
        etTDSauLap.setText("");
        etPSauLap.setText("");
        etQSauLap.setText("");
        etLapQuaTuSauLap.setText("");
        etLapQuaTiSauLap.setText("");
        etHSNhanSauLap.setText("");

        //TODO hiển thị mặc định dữ liệu bên phía công tơ, sau đó ghi đè bên TU TI nếu có dữ liệu
        String DongDien = tthtCtoEntity.getDONG_DIEN_SAULAP_TUTI();
        String DienAp = tthtCtoEntity.getDIEN_AP_SAULAP_TUTI();
        String K = tthtCtoEntity.getHANGSO_K_SAULAP_TUTI();
        String CCX = String.valueOf(tthtCtoEntity.getCAP_CX_SAULAP_TUTI());
        String CHI_SO = tthtCtoEntity.getCHI_SO_SAULAP_TUTI();
        String LapQuaTu = tthtCtoEntity.getSO_TU_SAULAP_TUTI();
        String LapQuaTi = tthtCtoEntity.getSO_TI_SAULAP_TUTI();
        String HSNhan = String.valueOf(tthtCtoEntity.getHS_NHAN_SAULAP_TUTI());
        String LOAI_CTO = tthtCtoEntity.getLOAI_CTO();

        etDongDienSauLap.setHint(DongDien == null ? "" : DongDien);
        etDienApSauLap.setHint(DienAp == null ? "" : DienAp);
        etKSauLap.setHint(K == null ? "" : K);
        etCCXSauLap.setHint(CCX == null ? "" : CCX);

        if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.D1.name())) {
            if (CHI_SO != null && !CHI_SO.equals("") && CHI_SO.contains(";")) {
                etBTSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                etCDSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
                etTDSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[2].split(":")[1] : CHI_SO.split(";")[2]);
                etPSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[3].split(":")[1] : CHI_SO.split(";")[3]);
                etQSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[4].split(":")[1] : CHI_SO.split(";")[4]);
            } else {
                etBTSauLap.setHint("");
                etCDSauLap.setHint("");
                etTDSauLap.setHint("");
                etPSauLap.setHint("");
                etQSauLap.setHint("");
            }
        }

        if (LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.HC.name()) || LOAI_CTO.equals(TthtCommon.LOAI_CONGTO.VC.name())) {
            if (CHI_SO != null && !CHI_SO.equals("") && CHI_SO.contains(";")) {
                etPSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[0].split(":")[1] : CHI_SO.split(";")[0]);
                etQSauLap.setHint(CHI_SO.contains(":") ? CHI_SO.split(";")[1].split(":")[1] : CHI_SO.split(";")[1]);
            } else {
                etPSauLap.setHint("");
                etQSauLap.setHint("");
            }
        }

        etLapQuaTuSauLap.setHint(LapQuaTu == null ? "" : LapQuaTu);
        etLapQuaTiSauLap.setHint(LapQuaTi == null ? "" : LapQuaTi);
        etHSNhanSauLap.setHint(HSNhan == null ? "" : HSNhan);

        ivTuTreo.setImageBitmap(null);
        ivTiTreo.setImageBitmap(null);
        ivNhiThuTuTreo.setImageBitmap(null);
        ivNhiThuTiTreo.setImageBitmap(null);
        ivNiemPhongTuTreo.setImageBitmap(null);
        ivNiemPhongTiTreo.setImageBitmap(null);

        Cursor cursorImageTU = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
        if (cursorImageTU.moveToFirst()) {
            TEN_ANH_TU = cursorImageTU.getString(cursorImageTU.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_TU;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivTuTreo.setImageBitmap(bitmap);
            }
            cursorImageTU.close();
        } else ivTuTreo.setImageBitmap(null);

        Cursor cursorImageTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
        if (cursorImageTi.moveToFirst()) {
            TEN_ANH_TI = cursorImageTi.getString(cursorImageTi.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_TI;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivTiTreo.setImageBitmap(bitmap);
            }
            cursorImageTi.close();
        } else ivTiTreo.setImageBitmap(null);

        Cursor cursorImageNhiThuTu = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
        if (cursorImageNhiThuTu.moveToFirst()) {
            TEN_ANH_NHI_THU_TU = cursorImageNhiThuTu.getString(cursorImageNhiThuTu.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_NHI_THU_TU;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivNhiThuTuTreo.setImageBitmap(bitmap);
            }
            cursorImageNhiThuTu.close();
        }

        Cursor cursorImageNhiThuTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
        if (cursorImageNhiThuTi.moveToFirst()) {
            TEN_ANH_NHI_THU_TI = cursorImageNhiThuTi.getString(cursorImageNhiThuTi.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_NHI_THU_TI;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivNhiThuTiTreo.setImageBitmap(bitmap);
            }
            cursorImageNhiThuTi.close();
        } else ivNhiThuTiTreo.setImageBitmap(null);

        Cursor cursorImageNiemPhongTu = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
        if (cursorImageNiemPhongTu.moveToFirst()) {
            TEN_ANH_NIEM_PHONG_TU = cursorImageNiemPhongTu.getString(cursorImageNiemPhongTu.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH_NIEM_PHONG_TU;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivNiemPhongTuTreo.setImageBitmap(bitmap);
            }
            cursorImageNiemPhongTu.close();
        } else ivNiemPhongTuTreo.setImageBitmap(null);

        Cursor cursorImageNiemPhongTi = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
        if (cursorImageNiemPhongTi.moveToFirst()) {
            TEN_ANH_NIEM_PHONG_TI = cursorImageNiemPhongTi.getString(cursorImageNiemPhongTi.getColumnIndex("TEN_ANH"));
            String uriImage = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH_NIEM_PHONG_TI;
            Bitmap bitmap = TthtCommon.getBitmapFromUri(uriImage);
            if (bitmap != null) {
                ivNiemPhongTiTreo.setImageBitmap(bitmap);
            }
            cursorImageNiemPhongTi.close();
        } else ivNiemPhongTiTreo.setImageBitmap(null);
    }

    private void setDataCto(final TthtCtoEntity tthtCtoEntity, final Cursor cCto) {
        if (tthtCtoEntity == null || cCto == null)
            return;

        tthtCtoEntity.setMA_DVIQLY(cCto.getString(cCto.getColumnIndex("MA_DVIQLY")));
        tthtCtoEntity.setID_BBAN_TRTH(cCto.getString(cCto.getColumnIndex("ID_BBAN_TRTH")) != null
                && !cCto.getString(cCto.getColumnIndex("ID_BBAN_TRTH")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("ID_BBAN_TRTH")) : 0);
        tthtCtoEntity.setMA_CTO(cCto.getString(cCto.getColumnIndex("MA_CTO")));
        tthtCtoEntity.setSO_CTO(cCto.getString(cCto.getColumnIndex("SO_CTO")));
        tthtCtoEntity.setLAN(cCto.getString(cCto.getColumnIndex("LAN")) != null
                && !cCto.getString(cCto.getColumnIndex("LAN")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("LAN")) : 0);
        String MA_BDONG_CTO = cCto.getString(cCto.getColumnIndex("MA_BDONG"));
        tthtCtoEntity.setMA_BDONG(MA_BDONG_CTO);
        tthtCtoEntity.setNGAY_BDONG(cCto.getString(cCto.getColumnIndex("NGAY_BDONG")));
        tthtCtoEntity.setMA_CLOAI(cCto.getString(cCto.getColumnIndex("MA_CLOAI")));
        tthtCtoEntity.setLOAI_CTO(cCto.getString(cCto.getColumnIndex("LOAI_CTO")));
        tthtCtoEntity.setVTRI_TREO(cCto.getString(cCto.getColumnIndex("VTRI_TREO")) != null
                && !cCto.getString(cCto.getColumnIndex("VTRI_TREO")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("VTRI_TREO")) : 0);
        tthtCtoEntity.setMA_SOCBOOC(cCto.getString(cCto.getColumnIndex("MA_SOCBOOC")));
        tthtCtoEntity.setSOVIEN_CBOOC(cCto.getInt(cCto.getColumnIndex("SO_VIENCBOOC")));
        tthtCtoEntity.setLOAI_HOM(cCto.getInt(cCto.getColumnIndex("LOAI_HOM")));
        tthtCtoEntity.setMA_SOCHOM(cCto.getString(cCto.getColumnIndex("MA_SOCHOM")));
        tthtCtoEntity.setSO_VIENCHOM(cCto.getString(cCto.getColumnIndex("SO_VIENCHOM")) != null
                && !cCto.getString(cCto.getColumnIndex("SO_VIENCHOM")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("SO_VIENCHOM")) : 0);
        tthtCtoEntity.setHS_NHAN(cCto.getString(cCto.getColumnIndex("HS_NHAN")) != null
                && !cCto.getString(cCto.getColumnIndex("HS_NHAN")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("HS_NHAN")) : 1);
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
        tthtCtoEntity.setSOVIEN_CHIKDINH(cCto.getString(cCto.getColumnIndex("SOVIEN_CHIKDINH")) != null
                && !cCto.getString(cCto.getColumnIndex("SOVIEN_CHIKDINH")).isEmpty()
                ? cCto.getInt(cCto.getColumnIndex("SOVIEN_CHIKDINH")) : 0);
        tthtCtoEntity.setDIEN_AP(cCto.getString(cCto.getColumnIndex("DIEN_AP")));
        tthtCtoEntity.setDONG_DIEN(cCto.getString(cCto.getColumnIndex("DONG_DIEN")));
        tthtCtoEntity.setHANGSO_K(cCto.getString(cCto.getColumnIndex("HANGSO_K")));
        tthtCtoEntity.setMA_NUOC(cCto.getString(cCto.getColumnIndex("MA_NUOC")));
        tthtCtoEntity.setTEN_NUOC(cCto.getString(cCto.getColumnIndex("TEN_NUOC")));
        tthtCtoEntity.setSO_KIM_NIEM_CHI(cCto.getString(cCto.getColumnIndex("SO_KIM_NIEM_CHI")));
        tthtCtoEntity.setTTRANG_NPHONG(cCto.getString(cCto.getColumnIndex("TTRANG_NPHONG")));

        int ID_CHITIET_CTO = cCto.getInt(cCto.getColumnIndex("ID_CHITIET_CTO"));
        //TODO lấy tên ảnh
        Cursor cusorGetAnhCongTo = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_CONG_TO);
        if (cusorGetAnhCongTo.moveToFirst()) {
            String TEN_ANH = cusorGetAnhCongTo.getString(cusorGetAnhCongTo.getColumnIndex("TEN_ANH"));
            tthtCtoEntity.setTEN_ANH_CONG_TO(TEN_ANH);
            cusorGetAnhCongTo.close();
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
    }

    private void setDataBBan(final TthtBBanEntity bban, final Cursor cBban) {
        if (bban == null || cBban == null)
            return;

        bban.setGHI_CHU(cBban.getString(cBban.getColumnIndex("GHI_CHU")));
        bban.setID_BBAN_TRTH(cBban.getInt(cBban.getColumnIndex("ID_BBAN_TRTH")));
        bban.setMA_CNANG(cBban.getString(cBban.getColumnIndex("MA_CNANG")));
        bban.setMA_DDO(cBban.getString(cBban.getColumnIndex("MA_DDO")));
        bban.setMA_DVIQLY(cBban.getString(cBban.getColumnIndex("MA_DVIQLY")));
        bban.setMA_LDO(cBban.getString(cBban.getColumnIndex("MA_LDO")));
        bban.setMA_NVIEN(cBban.getString(cBban.getColumnIndex("BUNDLE_MA_NVIEN")));
        bban.setMA_YCAU_KNAI(cBban.getString(cBban.getColumnIndex("MA_YCAU_KNAI")));
        bban.setNGAY_SUA(cBban.getString(cBban.getColumnIndex("NGAY_SUA")));
        bban.setNGAY_TAO(cBban.getString(cBban.getColumnIndex("NGAY_TAO")));
        bban.setNGAY_TRTH(cBban.getString(cBban.getColumnIndex("NGAY_TRTH")));
        bban.setNGUOI_SUA(cBban.getString(cBban.getColumnIndex("NGUOI_SUA")));
        bban.setNGUOI_TAO(cBban.getString(cBban.getColumnIndex("NGUOI_TAO")));
        bban.setSO_BBAN(cBban.getString(cBban.getColumnIndex("SO_BBAN")));
        bban.setTRANG_THAI(cBban.getInt(cBban.getColumnIndex("TRANG_THAI")));
        bban.setGHI_CHU(cBban.getString(cBban.getColumnIndex("GHI_CHU")));
        bban.setId_BBAN_CONGTO(cBban.getInt(cBban.getColumnIndex("ID_BBAN_CONGTO")));
        bban.setLOAI_BBAN(cBban.getString(cBban.getColumnIndex("LOAI_BBAN")));
        bban.setTEN_KHANG(cBban.getString(cBban.getColumnIndex("TEN_KHANG")));
        bban.setDCHI_HDON(cBban.getString(cBban.getColumnIndex("DCHI_HDON")));
        bban.setDTHOAI(cBban.getString(cBban.getColumnIndex("DTHOAI")));
        bban.setMA_GCS_CTO(cBban.getString(cBban.getColumnIndex("MA_GCS_CTO")));
        bban.setMA_TRAM(cBban.getString(cBban.getColumnIndex("MA_TRAM")));
        bban.setMA_HDONG(cBban.getString(cBban.getColumnIndex("MA_HDONG")));

    }

    private void setDataTUTI(final TthtTuTiEntity tuti, final Cursor cursorGetDataTUTI, boolean IS_TU, String MA_BDONG_TUTI) {
        if (tuti == null || cursorGetDataTUTI == null || MA_BDONG_TUTI == null || MA_BDONG_TUTI.isEmpty())
            return;

        //TODO lấy tên ảnh
        Cursor cusorGetAnhTuTi = (IS_TU ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI));
        if (cusorGetAnhTuTi.moveToFirst()) {
            String TEN_ANH = cusorGetAnhTuTi.getString(cusorGetAnhTuTi.getColumnIndex("TEN_ANH"));
            tuti.setTEN_ANH_TU_TI(TEN_ANH);
            cusorGetAnhTuTi.close();
        }

        Cursor cusorGetAnhMachNhiThu = (IS_TU ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
        if (cusorGetAnhMachNhiThu.moveToFirst()) {
            String TEN_ANH = cusorGetAnhMachNhiThu.getString(cusorGetAnhMachNhiThu.getColumnIndex("TEN_ANH"));
            tuti.setTEN_ANH_MACH_NHI_THU(TEN_ANH);
            cusorGetAnhMachNhiThu.close();
        }

        Cursor cusorGetAnhMachCongTo = (IS_TU ? connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) : connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
        if (cusorGetAnhMachCongTo.moveToFirst()) {
            String TEN_ANH = cusorGetAnhMachCongTo.getString(cusorGetAnhMachCongTo.getColumnIndex("TEN_ANH"));
            tuti.setTEN_ANH_MACH_CONG_TO(TEN_ANH);
            cusorGetAnhMachCongTo.close();
        }

        tuti.setMA_CLOAI(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_CLOAI")));
        tuti.setLOAI_TU_TI(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("LOAI_TU_TI")));
        tuti.setMO_TA(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MO_TA")));
        tuti.setSO_PHA(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("SO_PHA")));
        tuti.setTYSO_DAU(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("TYSO_DAU")));
        tuti.setCAP_CXAC(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("CAP_CXAC")));
        tuti.setCAP_DAP(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("CAP_DAP")));
        tuti.setMA_NUOC(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_NUOC")));
        tuti.setMA_HANG(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_HANG")));
        tuti.setTRANG_THAI(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("TRANG_THAI")));
        tuti.setIS_TU(String.valueOf(IS_TU));
        tuti.setID_BBAN_TUTI(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("ID_BBAN_TUTI")));
        tuti.setID_CHITIET_TUTI(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("ID_CHITIET_TUTI")));
        tuti.setSO_TU_TI(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("SO_TU_TI")));
        tuti.setNUOC_SX(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("NUOC_SX")));
        tuti.setSO_TEM_KDINH(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("SO_TEM_KDINH")));
        tuti.setNGAY_KDINH(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("NGAY_KDINH")));
        tuti.setMA_CHI_KDINH(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_CHI_KDINH")));
        tuti.setMA_CHI_HOP_DDAY(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_CHI_HOP_DDAY")));
        tuti.setSO_VONG_THANH_CAI(cursorGetDataTUTI.getInt(cursorGetDataTUTI.getColumnIndex("SO_VONG_THANH_CAI")));
        tuti.setTYSO_BIEN(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("TYSO_BIEN")));
        tuti.setMA_BDONG(MA_BDONG_TUTI);
        tuti.setMA_DVIQLY(cursorGetDataTUTI.getString(cursorGetDataTUTI.getColumnIndex("MA_DVIQLY")));
    }

    private void initView(View view) {
        //TODO init textview
        tvTenKH = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_tvTenKH);
        tvDiaChiKH = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_tvDiaChiKH);
        tvLyDoTreoThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_tvLyDoTreoThao);
        tvMaGCS = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_tvMaGCS);
        tvSoNo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_tvSoNo);

        //TODO TU Tháo
        tvCapDienApTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvCapDienAp);
        tvTySoBienTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvTySoBien);
        tvTemKDinhTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvTemKDinh);
        tvNgayKDinhTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvNgayKDinh);
        tvSoTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvSoTuTi);
        tvCCXTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvCCX);
        tvNuocSXTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvNuocSX);
        tvSoVongTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvSoVong);
        tvChiKDinhTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvChiKDinh);
        tvChiHopTuThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTu_tvChiHop);

        //TODO TU treo
        tvCapDienApTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvCapDienAp);
        tvTySoBienTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvTySoBien);
        tvTemKDinhTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvTemKDinh);
        tvNgayKDinhTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvNgayKDinh);
        tvSoTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvSoTuTi);
        tvCCXTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvCCX);
        tvNuocSXTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvNuocSX);
        tvSoVongTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvSoVong);
        tvChiKDinhTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvChiKDinh);
        tvChiHopTuTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTu_tvChiHop);

        //TODO Ti Thao
        tvCapDienApTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvCapDienAp);
        tvTySoBienTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvTySoBien);
        tvTemKDinhTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvTemKDinh);
        tvNgayKDinhTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvNgayKDinh);
        tvSoTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvSoTuTi);
        tvCCXTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvCCX);
        tvNuocSXTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvNuocSX);
        tvSoVongTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvSoVong);
        tvChiKDinhTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvChiKDinh);
        tvChiHopTiThao = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_thaoTi_tvChiHop);

        //TODO Ti Treo
        tvCapDienApTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvCapDienAp);
        tvTySoBienTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvTySoBien);
        tvTemKDinhTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvTemKDinh);
        tvNgayKDinhTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvNgayKDinh);
        tvSoTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvSoTuTi);
        tvCCXTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvCCX);
        tvNuocSXTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvNuocSX);
        tvSoVongTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvSoVong);
        tvChiKDinhTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvChiKDinh);
        tvChiHopTiTreo = (TextView) view.findViewById(R.id.ttht_frag_ghiTuTi_lapTi_tvChiHop);

        //TODO init EditText
        etDongDienSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etDongDien);
        etDienApSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etDienAp);
        etKSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etK);
        etCCXSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etCCX);
        etPSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etP);
        etQSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etQ);
        etBTSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etBinhThuong);
        etCDSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etCaoDiem);
        etTDSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etThapDiem);
        etLapQuaTuSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etLapQuaTu);
        etLapQuaTiSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etLapQuaTi);
        etHSNhanSauLap = (EditText) view.findViewById(R.id.ttht_frag_ghiTuTi_SauLap_etHeSoNhan);

        //TODO init button
        btExpand6 = (Button) view.findViewById(R.id.onClickExpand6);
        btExpand7 = (Button) view.findViewById(R.id.onClickExpand7);
        btExpand8 = (Button) view.findViewById(R.id.onClickExpand8);
        btExpand9 = (Button) view.findViewById(R.id.onClickExpand9);
        btExpand10 = (Button) view.findViewById(R.id.onClickExpand10);
        btExpand11 = (Button) view.findViewById(R.id.onClickExpand11);
        btExpand12 = (Button) view.findViewById(R.id.onClickExpand12);

        btChupTuTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupTuTreo);
        btChupTiTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupTiTreo);
        btChupNhiThuTuTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupNhiThuTuTreo);
        btChupNhiThuTiTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupNhiThuTiTreo);
        btChupNiemPhongTuTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupNiemPhongTuTreo);
        btChupNiemPhongTiTreo = (Button) view.findViewById(R.id.fragment_ttht_ghiTuTi_btchupNiemPhongTiTreo);

        //TODO init linear layout
        llExpand6 = (LinearLayout) view.findViewById(R.id.ll_expand6);
        llExpand7 = (LinearLayout) view.findViewById(R.id.ll_expand7);
        llExpand8 = (LinearLayout) view.findViewById(R.id.ll_expand8);
        llExpand9 = (LinearLayout) view.findViewById(R.id.ll_expand9);
        llExpand10 = (LinearLayout) view.findViewById(R.id.ll_expand10);
        llExpand11 = (LinearLayout) view.findViewById(R.id.ll_expand11);
        llExpand12 = (LinearLayout) view.findViewById(R.id.ll_expand12);

        //TODO init imageView
        ivTuTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupTuTreo);
        ivTiTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupTiTreo);
        ivNhiThuTuTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupMachNhiThuTuTreo);
        ivNhiThuTiTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupMachNhiThuTiTreo);
        ivNiemPhongTuTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupNiemPhongTuTreo);
        ivNiemPhongTiTreo = (ImageView) view.findViewById(R.id.ttht_frag_ghiTuTi_ivChupNiemPhongTiTreo);

    }

    @Override
    public void onClick(View view) {
        Bitmap bitmap = null;

        switch (view.getId()) {
            case R.id.onClickExpand6:
                if (llExpand6.getVisibility() == View.VISIBLE) {
                    llExpand6.setVisibility(View.GONE);
                    btExpand6.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand6.setVisibility(View.VISIBLE);
                    btExpand6.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand6.setPadding(10, 0, 0, 0);
                btExpand6.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand7:
                btExpand7.setCompoundDrawablePadding(10);
                if (llExpand7.getVisibility() == View.VISIBLE) {
                    llExpand7.setVisibility(View.GONE);
                    btExpand7.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand7.setVisibility(View.VISIBLE);
                    btExpand7.setBackgroundResource(R.drawable.title_elasticity);
                }
                break;

            case R.id.onClickExpand8:
                if (llExpand8.getVisibility() == View.VISIBLE) {
                    llExpand8.setVisibility(View.GONE);
                    btExpand8.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand8.setVisibility(View.VISIBLE);
                    btExpand8.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand8.setPadding(10, 0, 0, 0);
                btExpand8.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand9:
                if (llExpand9.getVisibility() == View.VISIBLE) {
                    llExpand9.setVisibility(View.GONE);
                    btExpand9.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand9.setVisibility(View.VISIBLE);
                    btExpand9.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand9.setPadding(10, 0, 0, 0);
                btExpand9.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand10:
                if (llExpand10.getVisibility() == View.VISIBLE) {
                    llExpand10.setVisibility(View.GONE);
                    btExpand10.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand10.setVisibility(View.VISIBLE);
                    btExpand10.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand10.setPadding(10, 0, 0, 0);
                btExpand10.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand11:
                if (llExpand11.getVisibility() == View.VISIBLE) {
                    llExpand11.setVisibility(View.GONE);
                    btExpand11.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand11.setVisibility(View.VISIBLE);
                    btExpand11.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand11.setPadding(10, 0, 0, 0);
                btExpand11.setCompoundDrawablePadding(10);
                break;

            case R.id.onClickExpand12:
                if (llExpand12.getVisibility() == View.VISIBLE) {
                    llExpand12.setVisibility(View.GONE);
                    btExpand12.setBackgroundResource(R.drawable.title_elasticity_full);
                } else {
                    llExpand12.setVisibility(View.VISIBLE);
                    btExpand12.setBackgroundResource(R.drawable.title_elasticity);
                }
                btExpand12.setPadding(10, 0, 0, 0);
                btExpand12.setCompoundDrawablePadding(10);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupTuTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_TU);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupTiTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_TI);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupNhiThuTuTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupNhiThuTiTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupNiemPhongTuTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                break;

            case R.id.fragment_ttht_ghiTuTi_btchupNiemPhongTiTreo:
                ChupAnh(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupTuTreo:
                bitmap = (ivTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivTuTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupTiTreo:
                bitmap = (ivTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivTiTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupMachNhiThuTuTreo:
                bitmap = (ivNhiThuTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNhiThuTuTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupMachNhiThuTiTreo:
                bitmap = (ivNhiThuTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNhiThuTiTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupNiemPhongTuTreo:
                bitmap = (ivNiemPhongTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNiemPhongTuTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

            case R.id.ttht_frag_ghiTuTi_ivChupNiemPhongTiTreo:
                bitmap = (ivNiemPhongTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNiemPhongTiTreo.getDrawable()).getBitmap();
                if (bitmap == null) {
                    return;
                }
                TthtCommon.zoomImage(getActivity(), bitmap);
                break;

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

    private void ChupAnh(TthtCommon.TYPE_IMAGE typeImage) {
        try {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            String DATETIME = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy.toString());

            String MA_DVIQLY = "";
            String MA_TRAM = "";
            String MA_CTO = "";

            Cursor c = connection.getBBanByIDBBanTRTH(ID_BBAN_TRTH);
            if (c != null) {
                MA_DVIQLY = c.getString(c.getColumnIndex("MA_DVIQLY"));
                MA_TRAM = c.getString(c.getColumnIndex("MA_TRAM"));
            }

            Cursor c1 = connection.getCToByID_CHITIET_CTO(ID_CHITIET_CTO);
            if(c1!=null)
            {
                c1.moveToFirst();
                MA_CTO = c1.getString(c1.getColumnIndex("MA_CTO"));
            }


            String fileName = "";
            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TU) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_TU.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }
            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TI) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_TI.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI) {
                fileName = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name())
                        + "/"
                        + TthtCommon.getImageName(TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI.name(), DATETIME, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, MA_CTO);
            }

            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TU) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_TU);
            }
            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_TI) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_TI);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_NHI_THU_TU);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_NHI_THU_TI);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_NIEM_PHONG_TU);
            }

            if (typeImage == TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI) {
                startActivityForResult(cameraIntent, TthtCommon.CAMERA_REQUEST_NIEM_PHONG_TI);
            }

        } catch (IOException ex) {
            Common.showAlertDialogGreen(getActivity(), "Lỗi (394)", Color.RED, "Lỗi lưu ảnh TU\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }


    public boolean checkWriteDataTuTiComplete(final TthtGhiTuTiFragment fragment) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        if (fragment != null && fragment == this) {
            Bitmap bitmapTUTreo = (ivTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivTuTreo.getDrawable()).getBitmap();
            Bitmap bitmapTITreo = (ivTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivTiTreo.getDrawable()).getBitmap();
            Bitmap bitmapNhiThuTUTreo = (ivNhiThuTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNhiThuTuTreo.getDrawable()).getBitmap();
            Bitmap bitmapNhiThuTITreo = (ivNhiThuTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNhiThuTiTreo.getDrawable()).getBitmap();
            Bitmap bitmapNiemPhongTUTreo = (ivNiemPhongTuTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNiemPhongTuTreo.getDrawable()).getBitmap();
            Bitmap bitmapNiemPhongTITreo = (ivNiemPhongTiTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivNiemPhongTiTreo.getDrawable()).getBitmap();

            if (bitmapTUTreo == null || bitmapTITreo == null || bitmapNhiThuTUTreo == null || bitmapNhiThuTITreo == null || bitmapNiemPhongTUTreo == null || bitmapNiemPhongTITreo == null) {
                llExpand11.setVisibility(View.VISIBLE);
                llExpand12.setVisibility(View.VISIBLE);
                errorMessage.append("Các Ảnh yêu cầu.");
            }

            if (etDongDienSauLap.getText().toString().isEmpty() && etDongDienSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etDongDienSauLap.setError("Vui lòng nhập");
                etDongDienSauLap.setFocusable(true);
                errorMessage.append("Dòng điện sau lắp. ");
            }

            if (etDienApSauLap.getText().toString().isEmpty() && etDienApSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etDienApSauLap.setError("Vui lòng nhập");
                etDienApSauLap.setFocusable(true);
                errorMessage.append("Điện áp sau lắp. ");
            }

            if (etKSauLap.getText().toString().isEmpty() && etKSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etKSauLap.setError("Vui lòng nhập");
                etKSauLap.setFocusable(true);
                errorMessage.append("Hệ số K sau lắp. ");
            }


            if (etCCXSauLap.getText().toString().isEmpty() && etCCXSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etCCXSauLap.setError("Vui lòng nhập");
                etCCXSauLap.setFocusable(true);
                errorMessage.append("Cấp chính xác sau lắp. ");
            }

            if (etPSauLap.getText().toString().isEmpty() && etPSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etPSauLap.setError("Vui lòng nhập");
                etPSauLap.setFocusable(true);
                errorMessage.append("Chỉ số P sau lắp. ");
            }

            if (etQSauLap.getText().toString().isEmpty() && etQSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etQSauLap.setError("Vui lòng nhập");
                etQSauLap.setFocusable(true);
                errorMessage.append("Chỉ số Q sau lắp. ");
            }

            if (etBTSauLap.getText().toString().isEmpty() && etBTSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etBTSauLap.setError("Vui lòng nhập");
                etBTSauLap.setFocusable(true);
                errorMessage.append("Bình thường sau lắp. ");
            }


            if (etCDSauLap.getText().toString().isEmpty() && etCDSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etCDSauLap.setError("Vui lòng nhập");
                etCDSauLap.setFocusable(true);
                errorMessage.append("Chỉ số Cao điểm sau lắp. ");
            }


            if (etTDSauLap.getText().toString().isEmpty() && etTDSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etTDSauLap.setError("Vui lòng nhập");
                etTDSauLap.setFocusable(true);
                errorMessage.append("Thấp điểm sau lắp. ");
            }

            if (etLapQuaTuSauLap.getText().toString().isEmpty() && etLapQuaTuSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etLapQuaTuSauLap.setError("Vui lòng nhập");
                etLapQuaTuSauLap.setFocusable(true);
                errorMessage.append("Lắp qua TU sau lắp. ");
            }

            if (etLapQuaTiSauLap.getText().toString().isEmpty() && etLapQuaTiSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etLapQuaTiSauLap.setError("Vui lòng nhập");
                etLapQuaTiSauLap.setFocusable(true);
                errorMessage.append("Lắp qua TI sau lắp. ");
            }

            if (etHSNhanSauLap.getText().toString().isEmpty() && etHSNhanSauLap.getHint().toString().isEmpty()) {
                llExpand10.setVisibility(View.VISIBLE);
                etHSNhanSauLap.setError("Vui lòng nhập");
                etHSNhanSauLap.setFocusable(true);
                errorMessage.append("Hệ số nhân sau lắp. ");
            }

            if (!errorMessage.toString().isEmpty()) {
                errorMessage = new StringBuilder("Bạn chưa nhập đủ các dữ liệu treo tháo TU TI: \n");
                throw new Exception(errorMessage.toString());
            }
            return true;
        } else {
            throw new Exception("Lỗi gọi dữ liệu casting fragment!");
        }
    }

    public void saveDataTuTi(final TthtGhiTuTiFragment fragment) throws Exception {
        //TODO  kiểm tra dữ liệu cần thiết để ghi
        if (fragment != null && fragment == this) {
            //TODO thông tin thêm để vẽ lên ảnh
            String MA_DVIQLY = "";
            String MA_TRAM = "";
            String MA_CTO = "";
            String TEN_KHANG = "";
            String MA_DDO = "";
            String LOAI_CTO = "";
            Cursor c = connection.getBBanByIDBBanTRTH(ID_BBAN_TRTH);
            if (c != null) {
                c.moveToFirst();
                MA_DVIQLY = c.getString(c.getColumnIndex("MA_DVIQLY"));
                MA_TRAM = c.getString(c.getColumnIndex("MA_TRAM"));
                TEN_KHANG = c.getString(c.getColumnIndex("TEN_KHANG"));
                MA_DDO = c.getString(c.getColumnIndex("MA_DDO"));
            }

            Cursor c1 = connection.getCToByID_CHITIET_CTO(ID_CHITIET_CTO);
            if (c1 != null) {
                c.moveToFirst();
                MA_CTO = c1.getString(c1.getColumnIndex("MA_CTO"));
                LOAI_CTO = c1.getString(c1.getColumnIndex("LOAI_CTO"));
            }

            if (ID_BBAN_TUTI == 0)
                throw new Exception("Không có id biên bản TU TI");
            int ID_CHITIET_TU_TREO = tuTreo.getID_CHITIET_TUTI();
            int ID_CHITIET_TI_TREO = tiTreo.getID_CHITIET_TUTI();
            DATE_TIME_TO_INPUT_SQL = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.yyyyMMddTHHmmss_Slash_Colon.toString());

            //TODO lưu tên ảnh trước khi xóa dữ liệu cho trường hợp ghi đè thông tin lên ảnh cũ
            if (TEN_ANH_TU.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_TU = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }

            if (TEN_ANH_TI.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_TI = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }

            if (TEN_ANH_NHI_THU_TU.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_NHI_THU_TU = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }

            if (TEN_ANH_NHI_THU_TI.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_NHI_THU_TI = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }


            if (TEN_ANH_NIEM_PHONG_TU.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_NIEM_PHONG_TU = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }


            if (TEN_ANH_NIEM_PHONG_TI.isEmpty()) {
                Cursor cursorAnh = connection.getDataAnhByIDCto(ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                if (cursorAnh.moveToFirst()) {
                    TEN_ANH_NIEM_PHONG_TI = cursorAnh.getString(cursorAnh.getColumnIndex("TEN_ANH"));
                    cursorAnh.close();
                }
            }

            //TODO nếu vẫn không có dữ liệu thì báo lỗi
            if (TEN_ANH_TU.isEmpty() ||
                    TEN_ANH_TI.isEmpty() ||
                    TEN_ANH_NHI_THU_TI.isEmpty() ||
                    TEN_ANH_NHI_THU_TU.isEmpty() ||
                    TEN_ANH_NIEM_PHONG_TI.isEmpty() ||
                    TEN_ANH_NIEM_PHONG_TU.isEmpty())
                throw new Exception("Không có ảnh trong dữ liệu TU TI cần thiết trong database!");

            //TODO kiểm tra ảnh trong dữ liệu và không chụp lại thì xóa dữ liệu ảnh cũ
            Cursor checkImageTu = connection.checkAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
            if (checkImageTu != null && checkImageTu.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU);
            }

            Cursor checkImageTi = connection.checkAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
            if (checkImageTi != null && checkImageTi.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI);
            }

            Cursor checkImageNhiThuTu = connection.checkAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
            if (checkImageNhiThuTu != null && checkImageNhiThuTu.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
            }

            Cursor checkImageNhiThuTi = connection.checkAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
            if (checkImageNhiThuTi != null && checkImageNhiThuTi.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
            }

            Cursor checkImageNiemPhongTu = connection.checkAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
            if (checkImageNiemPhongTu != null && checkImageNiemPhongTu.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TU_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
            }

            Cursor checkImageNiemPhongTi = connection.checkAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
            if (checkImageNiemPhongTi != null && checkImageNiemPhongTi.getCount() > 0) {
                connection.deleteAnh(ID_CHITIET_TI_TREO, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
            }

            //TODO ghi thông tin dữ liệu
            //TODO ghi Tu
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            long rowAffect = connection.insertDataAnh(ID_CHITIET_TU_TREO, TEN_ANH_TU, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TU, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name() + "/" + TEN_ANH_TU);

                String SO_TU_TREO = tuTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH TU TREO";
                String SO_TU_TREO_DRAW = "SỐ TU TREO: " + SO_TU_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "MÃ C.TƠ:" + MA_CTO;
                String DATE_DRAW = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy_Slash.toString());
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TU_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);

                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_TU);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp Tu Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh Tu Treo!");


            //TODO ghi Ti
            rowAffect = connection.insertDataAnh(ID_CHITIET_TI_TREO, TEN_ANH_TI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_TI, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name() + "/" + TEN_ANH_TI);
                String SO_TI_TREO = tiTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH TI TREO";
                String SO_TI_TREO_DRAW = "SỐ TI TREO: " + SO_TI_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy_Slash.toString());
                ;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TI_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);

                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_TI);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp Ti Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh Ti Treo!");

            //TODO ghi mạch nhị thứ Tu Treo
            rowAffect = connection.insertDataAnh(ID_CHITIET_TU_TREO, TEN_ANH_NHI_THU_TU, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name() + "/" + TEN_ANH_NHI_THU_TU);
                String SO_TU_TREO = tuTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH MẠCH NHỊ THỨ TU";
                String SO_TU_TREO_DRAW = "SỐ TU TREO: " + SO_TU_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyy_Slash.toString());
                ;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TU_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);

                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp mạch nhị thứ Tu Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh mạch nhị thứ Tu Treo!");


            //TODO ghi mạch nhị thứ Ti Treo
            rowAffect = connection.insertDataAnh(ID_CHITIET_TI_TREO, TEN_ANH_NHI_THU_TI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name() + "/" + TEN_ANH_NHI_THU_TI);
                String SO_TI_TREO = tiTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH MẠCH NHỊ THỨ TI";
                String SO_TI_TREO_DRAW = "SỐ TI TREO: " + SO_TI_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = DATE_TIME_TO_INPUT_SQL;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TI_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);
                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp mạch nhị thứ Ti Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh mạch nhị thứ Ti Treo!");

            //TODO ghi mạch niêm phong Tu Treo
            rowAffect = connection.insertDataAnh(ID_CHITIET_TU_TREO, TEN_ANH_NIEM_PHONG_TU, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TU.name() + "/" + TEN_ANH_NIEM_PHONG_TU);
                String SO_TU_TREO = tuTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH NIÊM PHONG TU";
                String SO_TU_TREO_DRAW = "SỐ TU TREO: " + SO_TU_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = DATE_TIME_TO_INPUT_SQL;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TU_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);
                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp niêm phong Tu Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh niêm phong Tu Treo!");


            //TODO ghi mạch niêm phong Ti Treo
            rowAffect = connection.insertDataAnh(ID_CHITIET_TU_TREO, TEN_ANH_NIEM_PHONG_TI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {
                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name() + "/" + TEN_ANH_NIEM_PHONG_TI);
                String SO_TU_TREO = tiTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH NIÊM PHONG TI";
                String SO_TU_TREO_DRAW = "SỐ TI TREO: " + SO_TU_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = DATE_TIME_TO_INPUT_SQL;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TU_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);
                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp niêm phong Ti Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh niêm phong Ti Treo!");

            //TODO ghi Ti
            rowAffect = connection.insertDataAnh(ID_CHITIET_TI_TREO, TEN_ANH_NIEM_PHONG_TI, MA_DVIQLY, ID_BBAN_TUTI, ID_CHITIET_CTO, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI, DATE_TIME_TO_INPUT_SQL);
            if (rowAffect > 0) {

                //TODO fill data
                String PATH_ANH = TthtCommon.getRecordDirectoryFolder(TthtCommon.FOLDER_NAME.FOLDER_ANH_TI.name() + "/" + TEN_ANH_NIEM_PHONG_TI);
                String SO_TI_TREO = tiTreo.getSO_TU_TI();
                String TYPE_IMAGE_DRAW = "ẢNH TI TREO";
                String SO_TI_TREO_DRAW = "SỐ TI TREO: " + SO_TI_TREO;
                String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
                String MA_CTO_DRAW = "SỐ C.TƠ:" + MA_CTO;
                String DATE_DRAW = DATE_TIME_TO_INPUT_SQL;
                Bitmap bitmap = TthtCommon.drawTextOnBitmapCongTo(getActivity(), PATH_ANH, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_DRAW, SO_TI_TREO_DRAW, MA_CTO_DRAW, MA_DDO_DRAW);
                if (bitmap != null) {
                    setImageToView(bitmap, TthtCommon.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
                } else {
                    throw new Exception("Không tìm thấy ảnh chụp niêm phong Ti Treo!");
                }

            } else
                throw new Exception("Lỗi ghi vào database dữ liệu ảnh niêm phong Ti Treo!");


            //TODO ghi dữ liệu vào database
            String etCSBTTextSauLap = (etBTSauLap.getText().toString().isEmpty()) ? etBTSauLap.getHint().toString() : etBTSauLap.getText().toString();
            String etCSCDTextSauLap = (etCDSauLap.getText().toString().isEmpty()) ? etCDSauLap.getHint().toString() : etCDSauLap.getText().toString();
            String etCSTDTextSauLap = (etTDSauLap.getText().toString().isEmpty()) ? etTDSauLap.getHint().toString() : etTDSauLap.getText().toString();
            String etCSPTextSauLap = (etPSauLap.getText().toString().isEmpty()) ? etPSauLap.getHint().toString() : etPSauLap.getText().toString();
            String etCSQTextSauLap = (etQSauLap.getText().toString().isEmpty()) ? etQSauLap.getHint().toString() : etQSauLap.getText().toString();

            String CHI_SO_SAULAP_TUTI = TthtCommon.getStringChiSo(etCSBTTextSauLap, etCSCDTextSauLap, etCSTDTextSauLap, etCSPTextSauLap, etCSQTextSauLap, LOAI_CTO);

            String DONG_DIEN_SAULAP_TUTI = (etDongDienSauLap.getText().toString().isEmpty()) ? etDongDienSauLap.getHint().toString() : etDongDienSauLap.getText().toString();
            String DIEN_AP_SAULAP_TUTI = (etDienApSauLap.getText().toString().isEmpty()) ? etDienApSauLap.getHint().toString() : etDienApSauLap.getText().toString();
            String HANGSO_K_SAULAP_TUTI = (etKSauLap.getText().toString().isEmpty()) ? etKSauLap.getHint().toString() : etKSauLap.getText().toString();
            String CAP_CX_SAULAP_TUTI = (etCCXSauLap.getText().toString().isEmpty()) ? etCCXSauLap.getHint().toString() : etCCXSauLap.getText().toString();
            String SO_TU_SAULAP_TUTI = (etLapQuaTuSauLap.getText().toString().isEmpty()) ? etLapQuaTuSauLap.getHint().toString() : etLapQuaTuSauLap.getText().toString();
            String SO_TI_SAULAP_TUTI = (etLapQuaTiSauLap.getText().toString().isEmpty()) ? etLapQuaTiSauLap.getHint().toString() : etLapQuaTiSauLap.getText().toString();
            String HS_NHAN_SAULAP_TUTI = (etHSNhanSauLap.getText().toString().isEmpty()) ? etHSNhanSauLap.getHint().toString() : etHSNhanSauLap.getText().toString();

            rowAffect = connection.updateCongToSauLap(MA_BDONG, String.valueOf(ID_BBAN_TRTH),
                    CHI_SO_SAULAP_TUTI, DONG_DIEN_SAULAP_TUTI, DIEN_AP_SAULAP_TUTI, HANGSO_K_SAULAP_TUTI, CAP_CX_SAULAP_TUTI, SO_TU_SAULAP_TUTI, SO_TI_SAULAP_TUTI,
                    HS_NHAN_SAULAP_TUTI, TthtCommon.TRANG_THAI_DU_LIEU.DA_GHI.toString());

            if (rowAffect < 0) {
                throw new Exception("Lỗi ghi vào database dữ liệu công tơ!");
            }

            etDongDienSauLap.setText(DONG_DIEN_SAULAP_TUTI);
            etDienApSauLap.setText(DIEN_AP_SAULAP_TUTI);
            etKSauLap.setText(HANGSO_K_SAULAP_TUTI);
            etCCXSauLap.setText(CAP_CX_SAULAP_TUTI);
            etPSauLap.setText(etCSPTextSauLap);
            etQSauLap.setText(etCSQTextSauLap);
            etBTSauLap.setText(etCSBTTextSauLap);
            etCDSauLap.setText(etCSCDTextSauLap);
            etTDSauLap.setText(etCSTDTextSauLap);
            etLapQuaTuSauLap.setText(SO_TU_SAULAP_TUTI);
            etLapQuaTiSauLap.setText(SO_TI_SAULAP_TUTI);
            etHSNhanSauLap.setText(HS_NHAN_SAULAP_TUTI);

            etDongDienSauLap.setHint(DONG_DIEN_SAULAP_TUTI);
            etDienApSauLap.setHint(DIEN_AP_SAULAP_TUTI);
            etKSauLap.setHint(HANGSO_K_SAULAP_TUTI);
            etCCXSauLap.setHint(CAP_CX_SAULAP_TUTI);
            etPSauLap.setHint(etCSPTextSauLap);
            etQSauLap.setHint(etCSQTextSauLap);
            etBTSauLap.setHint(etCSBTTextSauLap);
            etCDSauLap.setHint(etCSCDTextSauLap);
            etTDSauLap.setHint(etCSTDTextSauLap);
            etLapQuaTuSauLap.setHint(SO_TU_SAULAP_TUTI);
            etLapQuaTiSauLap.setHint(SO_TI_SAULAP_TUTI);
            etHSNhanSauLap.setHint(HS_NHAN_SAULAP_TUTI);

            isRefreshImageTU = false;
            isRefreshImageTI = false;
            isRefreshImageNhiThuTu = false;
            isRefreshImageNhiThuTi = false;
            isRefreshImageNiemPhongTu = false;
            isRefreshImageNiemPhongTi = false;

            TEN_ANH_TU = "";
            TEN_ANH_TI = "";
            TEN_ANH_NHI_THU_TU = "";
            TEN_ANH_NHI_THU_TI = "";
            TEN_ANH_NIEM_PHONG_TU = "";
            TEN_ANH_NIEM_PHONG_TI = "";
        } else {
            throw new Exception("Lỗi khi casting fragment Công tơ!");
        }
    }

    public void checkSaveDataTuTi(final TthtGhiTuTiFragment fragment) throws Exception {
        if (fragment != null && fragment == this) {
            //TODO nếu chụp lại ảnh mà chưa lưu thì thông báo phải lưu lại
            //TODO nếu chụp lại ảnh mà chưa lưu thì thông báo phải lưu lại
            if (isRefreshImageTU ||
                    isRefreshImageTI ||
                    isRefreshImageNhiThuTu ||
                    isRefreshImageNhiThuTi ||
                    isRefreshImageNiemPhongTu ||
                    isRefreshImageNiemPhongTi) {
                throw new Exception("Thông tin TU TI mới thay đổi vẫn chưa lưu. Bạn cần lưu thông tin Tu Ti trước!");
            }
        } else {
            throw new Exception("Lỗi gọi dữ liệu casting fragment!");
        }
    }

//    public void refreshDataTuTi(TthtKHangEntity objectTreo, TthtKHangEntity objectThao, String MA_BDONG, final TthtGhiTuTiFragment fragment) throws Exception {
//        if (fragment != null && fragment == this) {
//            this.objectDataTreo = objectTreo;
//            this.objectDataThao = objectThao;
//            this.MA_BDONG = MA_BDONG;
//            fillData();
//        } else {
//            throw new Exception("Lỗi gọi dữ liệu casting fragment!");
//        }
//    }

    public void refreshDataTuTi(int id_bban_trth, int id_chitiet_cto, int id_bban_tuti) {
        if (id_bban_trth <= 0 || id_chitiet_cto <= 0 || id_bban_tuti <= 0)
            return;
        ID_CHITIET_CTO = id_chitiet_cto;
        ID_BBAN_TUTI = id_bban_tuti;
        ID_BBAN_TRTH = id_bban_trth;
        fillData();
    }
}