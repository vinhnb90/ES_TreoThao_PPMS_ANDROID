package es.vinhnb.ttht.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_TUTI;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_TUTI;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static android.content.ContentValues.TAG;
import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.sqlite2;
import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.type6;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_TI;
import static es.vinhnb.ttht.common.Common.TYPE_IMAGE.IMAGE_TU;
import static es.vinhnb.ttht.view.TthtHnBaseActivity.BUNDLE_POS;

public class TthtHnBBanTutiFragment extends TthtHnBaseFragment {

    private IOnTthtHnBBanTutiFragment mListener;
    private Unbinder unbinder;
    private TthtHnSQLDAO mSqlDAO;


    //scroll
    @BindView(R.id.scrollv_chitiet_tuti)
    ScrollView scrollViewTuti;


    //anchor
    @BindView(R.id.fab_tt_kh_bbantuti)
    FloatingActionButton fabTtKh;
    @BindView(R.id.tv_anchor_KH)
    TextView tvfabTtKh;


    @BindView(R.id.fab_tt_tu_bbantuti)
    FloatingActionButton fabTtTu;
    @BindView(R.id.tv_anchor_tuthao)
    TextView tvfabTtTu;


    @BindView(R.id.fab_tt_ti_bbantuti)
    FloatingActionButton fabTtTi;
    @BindView(R.id.tv_anchor_tithao)
    TextView tvfabTtTi;


    @BindView(R.id.fab_tt_camera_bbantuti)
    FloatingActionButton fabTtCamera;
    @BindView(R.id.tv_anchor_camera_tu)
    TextView tvfabTtCamera;


    @BindView(R.id.fab_tt_cannhap_bbanbtuti)
    FloatingActionButton fabTtCannhapsaulap;
    @BindView(R.id.tv_anchor_saulap)
    TextView tvfabTtCannhapsaulap;


    //khach hang
    @BindView(R.id.tv_2a_khachhang_tuti)
    TextView tvKHTuti;

    @BindView(R.id.tv_3a_diachi_tuti)
    TextView tvDiachiTuti;

    @BindView(R.id.tv_4a_lydo_tuti)
    TextView tvLydoTuti;

    @BindView(R.id.tv_5b_magcs_tuti)
    TextView tvMaGcsTuti;

    @BindView(R.id.tv_6b_sono_tuti)
    TextView tvSoNoTuti;


    //Thông tin TU Tháo
    @BindView(R.id.tv_9b_capdienap_tuthao)
    TextView tvCapdienapTuthao;

    @BindView(R.id.tv_10b_tysobien_tuthao)
    TextView tvTysobienTuthao;

    @BindView(R.id.tv_12b_ngaykiemdinh_tuthao)
    TextView tvNgayKiemdinhTuthao;

    @BindView(R.id.tv_13a_sotu_tuthao)
    TextView tvSoTuthao;

    @BindView(R.id.tv_14b_ccx_tuthao)
    TextView tvCCXTuthao;

    @BindView(R.id.tv_14b_nuocsx_tuthao)
    TextView tvNuocsxTuthao;

    @BindView(R.id.tv_13a_sovong_tuthao)
    TextView tvSovongTuthao;

    @BindView(R.id.tv_14b_chikiemdinh_tuthao)
    TextView tvChiKiemdinhTuthao;

    @BindView(R.id.tv_14b_chihopdauday_tuthao)
    TextView tvChihopdaudayTuthao;


    //Thông tin TU Treo
    @BindView(R.id.tv_9b_capdienap_tutreo)
    TextView tvCapdienapTutreo;

    @BindView(R.id.tv_10b_tysobien_tutreo)
    TextView tvTysobienTutreo;

    @BindView(R.id.tv_12b_ngaykiemdinh_tutreo)
    TextView tvNgayKiemdinhTutreo;

    @BindView(R.id.tv_13a_sotu_tutreo)
    TextView tvSoTutreo;

    @BindView(R.id.tv_14b_ccx_tutreo)
    TextView tvCCXTutreo;

    @BindView(R.id.tv_14b_nuocsx_tutreo)
    TextView tvNuocsxTutreo;

    @BindView(R.id.tv_13a_sovong_tutreo)
    TextView tvSovongTutreo;

    @BindView(R.id.tv_14b_chikiemdinh_tutreo)
    TextView tvChiKiemdinhTutreo;

    @BindView(R.id.tv_14b_chihopdauday_tutreo)
    TextView tvChihopdaudayTutreo;


    //Thông tin Ti thao
    @BindView(R.id.tv_9b_capdienap_tithao)
    TextView tvCapdienapTithao;

    @BindView(R.id.tv_10b_tysobien_tithao)
    TextView tvTysobienTithao;

    @BindView(R.id.tv_12b_ngaykiemdinh_tithao)
    TextView tvNgayKiemdinhTithao;

    @BindView(R.id.tv_13a_sotu_tithao)
    TextView tvSoTithao;

    @BindView(R.id.tv_14b_ccx_tithao)
    TextView tvCCXTithao;

    @BindView(R.id.tv_14b_nuocsx_tithao)
    TextView tvNuocsxTithao;

    @BindView(R.id.tv_13a_sovong_tithao)
    TextView tvSovongTithao;

    @BindView(R.id.tv_14b_chikiemdinh_tithao)
    TextView tvChiKiemdinhTithao;

    @BindView(R.id.tv_14b_chihopdauday_tithao)
    TextView tvChihopdaudayTithao;


    //Thông tin Ti treo
    @BindView(R.id.tv_9b_capdienap_titreo)
    TextView tvCapdienapTitreo;

    @BindView(R.id.tv_10b_tysobien_titreo)
    TextView tvTysobienTitreo;

    @BindView(R.id.tv_12b_ngaykiemdinh_titreo)
    TextView tvNgayKiemdinhTitreo;

    @BindView(R.id.tv_13a_sotu_titreo)
    TextView tvSoTitreo;

    @BindView(R.id.tv_14b_ccx_titreo)
    TextView tvCCXTitreo;

    @BindView(R.id.tv_14b_nuocsx_titreo)
    TextView tvNuocsxTitreo;

    @BindView(R.id.tv_13a_sovong_titreo)
    TextView tvSovongTitreo;

    @BindView(R.id.tv_14b_chikiemdinh_titreo)
    TextView tvChiKiemdinhTitreo;

    @BindView(R.id.tv_14b_chihopdauday_titreo)
    TextView tvChihopdaudayTitreo;


    //chup anh tu
    @BindView(R.id.iv_37_anh_tu)
    ImageView ivAnhTu;

    @BindView(R.id.ibtn_37_anh_tu)
    ImageView ibtnAnhTu;

    @BindView(R.id.btn_37_save_anh_tu)
    Button btnChupAnhTu;


    @BindView(R.id.iv_37_anhnhithu_tu)
    ImageView ivAnhNhiThuTu;

    @BindView(R.id.ibtn_37_anhnhithu_tu)
    ImageView ibtnAnhNhiThuTu;

    @BindView(R.id.btn_37_save_anhnhithu_tu)
    Button btnChupAnhNhiThuTu;


    @BindView(R.id.iv_37_anh_niemphong_tu)
    ImageView ivAnhNiemPhongTu;

    @BindView(R.id.ibtn_37_anh_niemphong_tu)
    ImageView ibtnAnhNiemPhongTu;

    @BindView(R.id.btn_37_save_anh_niemphong_tu)
    Button btnChupAnhNiemPhongTu;


    //chup anh ti
    @BindView(R.id.iv_37_anh_ti)
    ImageView ivAnhTi;

    @BindView(R.id.ibtn_37_anh_ti)
    ImageView ibtnAnhTi;

    @BindView(R.id.btn_37_save_anh_ti)
    Button btnChupAnhTi;


    @BindView(R.id.iv_37_anhnhithu_ti)
    ImageView ivAnhNhiThuTi;

    @BindView(R.id.ibtn_37_anhnhithu_ti)
    ImageView ibtnAnhNhiThuTi;

    @BindView(R.id.btn_37_save_anhnhithu_ti)
    Button btnChupAnhNhiThuTi;


    @BindView(R.id.iv_37_anh_niemphong_ti)
    ImageView ivAnhNiemPhongTi;

    @BindView(R.id.ibtn_37_anh_niemphong_ti)
    ImageView ibtnAnhNiemPhongTi;

    @BindView(R.id.btn_37_save_anh_niemphong_ti)
    Button btnChupAnhNiemPhongTi;


    //sau lap
    @BindView(R.id.et_9b_dongdien_saulap)
    EditText etDongdienSaulap;

    @BindView(R.id.et_9b_dienap_saulap)
    EditText etDienapSaulap;

    @BindView(R.id.et_9b_hesok_saulap)
    EditText etHesoKSaulap;

    @BindView(R.id.et_9b_ccx_saulap)
    EditText etCCXSaulap;

    @BindView(R.id.et_9b_chisotongP_saulap)
    EditText etTongPSaulap;

    @BindView(R.id.et_9b_chisotongQ_saulap)
    EditText etTongQSaulap;

    @BindView(R.id.et_9b_bt_saulap)
    EditText etBTSaulap;

    @BindView(R.id.et_9b_cd_saulap)
    EditText etCDSaulap;

    @BindView(R.id.et_9b_td_saulap)
    EditText etTDSaulap;

    @BindView(R.id.et_9b_lapquatu_saulap)
    EditText etLapquaTuSaulap;

    @BindView(R.id.et_9b_lapquati_saulap)
    EditText etLapquaTiSaulap;

    @BindView(R.id.et_9b_hesonhan_saulap)
    EditText etHesonhanSaulap;


    //bottom menu
    @BindView(R.id.ibtn_ghi_bbantuti)
    ImageButton ibtnGhiTuti;


    private IInteractionDataCommon onIDataCommom;
    private TABLE_CHITIET_CTO tableChitietCto;
    private TABLE_BBAN_CTO tableBbanCto;
    private TABLE_LOAI_CONG_TO tableLoaiCongTo;
    private TABLE_TRAM tableTram;
    private TABLE_BBAN_TUTI tableBbanTuti;
    private TABLE_CHITIET_TUTI tuTreo;
    private TABLE_CHITIET_TUTI tuThao;
    private TABLE_CHITIET_TUTI tiTreo;
    private TABLE_CHITIET_TUTI tiThao;
    private TABLE_ANH_HIENTRUONG anhTU;
    private TABLE_ANH_HIENTRUONG anhNhiThuTU;
    private TABLE_ANH_HIENTRUONG anhNiemPhongTU;
    private TABLE_ANH_HIENTRUONG anhNhiThuTI;
    private TABLE_ANH_HIENTRUONG anhTI;
    private TABLE_ANH_HIENTRUONG anhNiemPhongTI;
    private int pos = -1;
    private String timeFileCaptureAnhTu;
    private String timeFileCaptureAnhNhiThuTu;
    private String timeFileCaptureAnhNiemPhongTu;
    private String timeFileCaptureAnhTi;
    private String timeFileCaptureAnhNhiThuTi;
    private String timeFileCaptureAnhNiemPhongTi;
    private boolean isRefreshAnhTu;
    private boolean isRefreshAnhTi;
    private boolean isRefreshAnhNhiThuTu;
    private boolean isRefreshAnhNhiThuTi;
    private boolean isRefreshAnhNiemPhongTu;
    private boolean isRefreshAnhNiemPhongTi;


    public TthtHnBBanTutiFragment() {
        // Required empty public constructor
    }

    public static TthtHnBBanTutiFragment newInstance(int pos) {

        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_POS, pos);
        TthtHnBBanTutiFragment fragment = new TthtHnBBanTutiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (this.getActivity() instanceof IInteractionDataCommon)
            this.onIDataCommom = (IInteractionDataCommon) getActivity();
        else
            throw new ClassCastException("context must be implemnet IInteractionDataCommon!");

        if (getArguments() != null) {
            pos = getArguments().getInt(BUNDLE_POS, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_hn_bban_tuti, container, false);
        unbinder = ButterKnife.bind(TthtHnBBanTutiFragment.this, viewRoot);


        try {
            initDataAndView(viewRoot);
            setAction(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }


        return viewRoot;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_REQUEST_ANH_TU) {
                onActivityResultCapture(IMAGE_TU);
            }

            if (requestCode == CAMERA_REQUEST_ANH_TI) {
                onActivityResultCapture(IMAGE_TI);
            }

            if (requestCode == CAMERA_REQUEST_ANH_NHITHU_TU) {
                onActivityResultCapture(IMAGE_MACH_NHI_THU_TU);
            }

            if (requestCode == CAMERA_REQUEST_ANH_NHITHU_TI) {
                onActivityResultCapture(IMAGE_MACH_NHI_THU_TI);
            }

            if (requestCode == CAMERA_REQUEST_ANH_NIEMPHONG_TU) {
                onActivityResultCapture(IMAGE_NIEM_PHONG_TU);
            }

            if (requestCode == CAMERA_REQUEST_ANH_NIEMPHONG_TI) {
                onActivityResultCapture(IMAGE_NIEM_PHONG_TI);
            }
        } catch (Exception e) {
            ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
        }
    }

    private void onActivityResultCapture(Common.TYPE_IMAGE typeImage) throws Exception {
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();
        String TEN_ANH = "";
        String pathURICapturedAnh = null;


        //scale ảnh
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return;


            case IMAGE_TU:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH;
                break;
            case IMAGE_MACH_NHI_THU_TU:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhNhiThuTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH;
                break;
            case IMAGE_NIEM_PHONG_TU:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhNiemPhongTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + TEN_ANH;
                break;

            case IMAGE_TI:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH;
                break;
            case IMAGE_MACH_NHI_THU_TI:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhNhiThuTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH;
                break;
            case IMAGE_NIEM_PHONG_TI:
                TEN_ANH = Common.getImageName(typeImage.code, timeFileCaptureAnhNiemPhongTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                pathURICapturedAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + TEN_ANH;
                break;
        }


        if (TextUtils.isEmpty(pathURICapturedAnh))
            return;
        Common.scaleImage(pathURICapturedAnh, getActivity());


        //get bitmap tu URI
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pathURICapturedAnh, options);


        //set image and gắn cờ đã chụp lại ảnh
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return;

            case IMAGE_TU:
                ivAnhTu.setImageBitmap(bitmap);
                isRefreshAnhTu = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhTU = new TABLE_ANH_HIENTRUONG();
                anhTU.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhTU.setCREATE_DAY(timeFileCaptureAnhTu);
                anhTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhTU.setTYPE(IMAGE_TU.code);
                anhTU.setTEN_ANH(TEN_ANH);
                break;
            case IMAGE_TI:
                ivAnhTi.setImageBitmap(bitmap);
                isRefreshAnhTi = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhTI = new TABLE_ANH_HIENTRUONG();
                anhTI.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhTI.setCREATE_DAY(timeFileCaptureAnhTi);
                anhTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhTI.setTYPE(IMAGE_TI.code);
                anhTI.setTEN_ANH(TEN_ANH);
                break;
            case IMAGE_MACH_NHI_THU_TU:
                ivAnhNhiThuTu.setImageBitmap(bitmap);
                isRefreshAnhNhiThuTu = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhNhiThuTU = new TABLE_ANH_HIENTRUONG();
                anhNhiThuTU.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhNhiThuTU.setCREATE_DAY(timeFileCaptureAnhNhiThuTu);
                anhNhiThuTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNhiThuTU.setTYPE(IMAGE_MACH_NHI_THU_TU.code);
                anhNhiThuTU.setTEN_ANH(TEN_ANH);
                break;
            case IMAGE_MACH_NHI_THU_TI:
                ivAnhNhiThuTi.setImageBitmap(bitmap);
                isRefreshAnhNhiThuTi = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhNhiThuTI = new TABLE_ANH_HIENTRUONG();
                anhNhiThuTI.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhNhiThuTI.setCREATE_DAY(timeFileCaptureAnhNhiThuTi);
                anhNhiThuTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNhiThuTI.setTYPE(IMAGE_MACH_NHI_THU_TI.code);
                anhNhiThuTI.setTEN_ANH(TEN_ANH);
                break;
            case IMAGE_NIEM_PHONG_TU:
                ivAnhNiemPhongTu.setImageBitmap(bitmap);
                isRefreshAnhNiemPhongTu = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhNiemPhongTU = new TABLE_ANH_HIENTRUONG();
                anhNiemPhongTU.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhNiemPhongTU.setCREATE_DAY(timeFileCaptureAnhNiemPhongTu);
                anhNiemPhongTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNiemPhongTU.setTYPE(IMAGE_NIEM_PHONG_TU.code);
                anhNiemPhongTU.setTEN_ANH(TEN_ANH);
                break;
            case IMAGE_NIEM_PHONG_TI:
                ivAnhNiemPhongTi.setImageBitmap(bitmap);
                isRefreshAnhNiemPhongTi = true;

                // chỉ lưu tạm giá trị data ảnh chỉ số
                anhNiemPhongTI = new TABLE_ANH_HIENTRUONG();
                anhNiemPhongTI.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                anhNiemPhongTI.setCREATE_DAY(timeFileCaptureAnhNiemPhongTi);
                anhNiemPhongTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNiemPhongTI.setTYPE(IMAGE_NIEM_PHONG_TI.code);
                anhNiemPhongTI.setTEN_ANH(TEN_ANH);
                break;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnTthtHnBBanTutiFragment) {
            mListener = (IOnTthtHnBBanTutiFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnBBanTutiFragment");
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
    public void onDestroyView() {
        super.onDestroyView();
        onIDataCommom = null;
        unbinder.unbind();
    }

    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View viewRoot) throws Exception {
        fillDataBBanTuti();
    }

    private void fillDataBBanTuti() throws Exception {
        //get Data Chi tiet cong to
        String[] agrs = new String[]{String.valueOf(onIDataCommom.getID_BBAN_TRTH()), onIDataCommom.getMA_BDONG().code, onIDataCommom.getMaNVien()};
        List<TABLE_CHITIET_CTO> tableChitietCtoList = mSqlDAO.getChiTietCongto(agrs);
        if (tableChitietCtoList.size() != 0)
            tableChitietCto = tableChitietCtoList.get(0);


        //get Data bien ban
        String[] agrsBB = new String[]{String.valueOf(onIDataCommom.getID_BBAN_TRTH()), onIDataCommom.getMaNVien()};
        List<TABLE_BBAN_CTO> tableBbanCtoList = mSqlDAO.getBBan(agrsBB);
        if (tableBbanCtoList.size() != 0)
            tableBbanCto = tableBbanCtoList.get(0);


        //getInfo Chung loai
        String MA_CLOAI = tableChitietCto.getMA_CLOAI();
        String[] argsCloai = new String[]{MA_CLOAI};
        List<TABLE_LOAI_CONG_TO> tableLoaiCongToList = mSqlDAO.getLoaiCongto(argsCloai);
        if (tableLoaiCongToList.size() != 0)
            tableLoaiCongTo = tableLoaiCongToList.get(0);


        //getInfo Tram
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String[] argsTram = new String[]{MA_TRAM};
        List<TABLE_TRAM> tableTramList = mSqlDAO.getTRAM(argsTram);
        if (tableTramList.size() != 0)
            tableTram = tableTramList.get(0);


        //get Data Bban tuti
        List<TABLE_BBAN_TUTI> tableBbanTutiList = mSqlDAO.getBBanTuti(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO(), onIDataCommom.getMaNVien());
        if (tableBbanTutiList.size() != 0)
            tableBbanTuti = tableBbanTutiList.get(0);


        //get Data chi tiet tuti
        List<TABLE_CHITIET_TUTI> tableChitietTutiList = mSqlDAO.getChitietTuTi(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO(), onIDataCommom.getMaNVien());
        for (int i = 0; i < tableChitietTutiList.size(); i++) {
            TABLE_CHITIET_TUTI tableChitietTuti = tableChitietTutiList.get(i);

            //nếu là TU
            if (tableChitietTuti.getIS_TU().equals(String.valueOf(Common.IS_TU.TU.code))) {
                //MA_BDONG cho biết là TU Treo hay tháo
                if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.B.code))
                    tuTreo = tableChitietTuti;

                if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.E.code))
                    tuThao = tableChitietTuti;
            }


            //nếu là TI
            if (tableChitietTuti.getIS_TU().equals(String.valueOf(Common.IS_TU.TI.code))) {
                //MA_BDONG cho biết là TU Treo hay tháo
                if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.B.code))
                    tiTreo = tableChitietTuti;

                if (tableChitietTuti.getMA_BDONG().equals(Common.MA_BDONG.E.code))
                    tiThao = tableChitietTuti;
            }
        }


        //get ảnh tu treo
        String[] argsAnh = new String[]{onIDataCommom.getMaNVien(), String.valueOf(tuTreo.getID_BBAN_TUTI()), String.valueOf(tuTreo.getID_CHITIET_TUTI())};
        List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, IMAGE_TU);
        if (tableAnhHientruongList.size() != 0)
            anhTU = tableAnhHientruongList.get(0);


        //get anh nhi thu tu treo
        tableAnhHientruongList.clear();
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU);
        if (tableAnhHientruongList.size() != 0)
            anhNhiThuTU = tableAnhHientruongList.get(0);


        //get anh niem phong tu treo
        tableAnhHientruongList.clear();
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU);
        if (tableAnhHientruongList.size() != 0)
            anhNiemPhongTU = tableAnhHientruongList.get(0);


        //get ảnh ti treo
        tableAnhHientruongList.clear();
        argsAnh = new String[]{onIDataCommom.getMaNVien(), String.valueOf(tiTreo.getID_BBAN_TUTI()), String.valueOf(tiTreo.getID_CHITIET_TUTI())};
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_TI);
        if (tableAnhHientruongList.size() != 0)
            anhTI = tableAnhHientruongList.get(0);


        //get anh nhi thu ti treo
        tableAnhHientruongList.clear();
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI);
        if (tableAnhHientruongList.size() != 0)
            anhNhiThuTI = tableAnhHientruongList.get(0);


        //get anh niem phong ti treo
        tableAnhHientruongList.clear();
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI);
        if (tableAnhHientruongList.size() != 0)
            anhNiemPhongTI = tableAnhHientruongList.get(0);


        //fill KH
        fillInfoKH();


        //fill Tu thao
        fillInfoTuThao();


        fillInfoTuTreo();


        fillInfoTiThao();


        fillInfoTiTreo();


        //fill sau lap
        //get TRANG_THAI_DU_LIEU
        String TRANG_THAI_DU_LIEU = tableBbanTuti.getTRANG_THAI_DU_LIEU();
        Common.TRANG_THAI_DU_LIEU trangThaiDuLieu = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEU);
        fillInfoSauLap(trangThaiDuLieu);


        //fill data ảnh tu treo
        ivAnhTu.setImageBitmap(getAnh(tuTreo, IMAGE_TU));
        btnChupAnhTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhTu.setEnabled(false);


        //fill data ảnh tu treo nhi thứ
        ivAnhNhiThuTu.setImageBitmap(getAnh(tuTreo, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU));
        btnChupAnhNhiThuTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhNhiThuTu.setEnabled(false);


        //fill data ảnh tu treo niêm phong
        ivAnhNiemPhongTu.setImageBitmap(getAnh(tuTreo, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU));
        btnChupAnhNiemPhongTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhNiemPhongTu.setEnabled(false);


        //fill data ảnh ti treo
        ivAnhTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_TI));
        btnChupAnhTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhTi.setEnabled(false);


        //fill data ảnh ti treo nhi thứ
        ivAnhNhiThuTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
        btnChupAnhNhiThuTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhNhiThuTi.setEnabled(false);


        //fill data ảnh ti treo niêm phong
        ivAnhNiemPhongTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
        btnChupAnhNiemPhongTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
            btnChupAnhNiemPhongTi.setEnabled(false);


    }


    private void fillInfoSauLap(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) {
        if (tableChitietCto != null) {
            etDongdienSaulap.setText(tableChitietCto.getDONG_DIEN_SAULAP_TUTI());
            etDienapSaulap.setText(tableChitietCto.getDIEN_AP_SAULAP_TUTI());
            etHesoKSaulap.setText(tableChitietCto.getHANGSO_K_SAULAP_TUTI());
            etCCXSaulap.setText(String.valueOf(tableChitietCto.getCAP_CX_SAULAP_TUTI()));


            etLapquaTuSaulap.setText(tableChitietCto.getSO_TU_SAULAP_TUTI());
            etLapquaTiSaulap.setText(tableChitietCto.getSO_TI_SAULAP_TUTI());
            etHesonhanSaulap.setText(String.valueOf(tableChitietCto.getHS_NHAN_SAULAP_TUTI()));


            //chi so
            Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());
            String CHI_SO = tableChitietCto.getCHI_SO_SAULAP_TUTI();
            HashMap<String, String> dataCHI_SO = Common.spilitCHI_SO(loaiCto, CHI_SO);

            etBTSaulap.setText(TextUtils.isEmpty(dataCHI_SO.get(Common.BO_CHISO.BT.code)) ? "0" : dataCHI_SO.get(Common.BO_CHISO.BT.code));
            etCDSaulap.setText(TextUtils.isEmpty(dataCHI_SO.get(Common.BO_CHISO.CD.code)) ? "0" : dataCHI_SO.get(Common.BO_CHISO.CD.code));
            etTDSaulap.setText(TextUtils.isEmpty(dataCHI_SO.get(Common.BO_CHISO.TD.code)) ? "0" : dataCHI_SO.get(Common.BO_CHISO.TD.code));
            etTongPSaulap.setText(TextUtils.isEmpty(dataCHI_SO.get(Common.BO_CHISO.SG.code)) ? "0" : dataCHI_SO.get(Common.BO_CHISO.SG.code));
            etTongQSaulap.setText(TextUtils.isEmpty(dataCHI_SO.get(Common.BO_CHISO.VC.code)) ? "0" : dataCHI_SO.get(Common.BO_CHISO.VC.code));

            //set enable

            etDongdienSaulap.setEnabled(true);
            etDienapSaulap.setEnabled(true);
            etHesoKSaulap.setEnabled(true);
            etCCXSaulap.setEnabled(true);
            etLapquaTuSaulap.setEnabled(true);
            etLapquaTiSaulap.setEnabled(true);
            etHesonhanSaulap.setEnabled(true);

            etBTSaulap.setEnabled(true);
            etCDSaulap.setEnabled(true);
            etTDSaulap.setEnabled(true);
            etTongPSaulap.setEnabled(true);
            etTongQSaulap.setEnabled(true);

            if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS) {
                etDongdienSaulap.setEnabled(false);
                etDienapSaulap.setEnabled(false);
                etHesoKSaulap.setEnabled(false);
                etCCXSaulap.setEnabled(false);
                etLapquaTuSaulap.setEnabled(false);
                etLapquaTiSaulap.setEnabled(false);
                etHesonhanSaulap.setEnabled(false);

                etBTSaulap.setEnabled(false);
                etCDSaulap.setEnabled(false);
                etTDSaulap.setEnabled(false);
                etTongPSaulap.setEnabled(false);
                etTongQSaulap.setEnabled(false);
            }


            //chiso

        }
    }

    private Bitmap getAnh(TABLE_CHITIET_TUTI dataTuTi, Common.TYPE_IMAGE typeImage) {
        if (tableBbanTuti == null)
            return null;
        if (dataTuTi == null)
            return null;

        //get info ẢNH Tu
        String[] argsAnhTuTreo;
        argsAnhTuTreo = new String[]{onIDataCommom.getMaNVien(), String.valueOf(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO()), String.valueOf(dataTuTi.getID_CHITIET_TUTI())};
        List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnhTuTreo, typeImage);
        TABLE_ANH_HIENTRUONG tableAnh = null;
        if (tableAnhHientruongList.size() != 0)
            tableAnh = tableAnhHientruongList.get(0);

        if (tableAnh == null) {
            Log.i(TAG, "getAnh: không có ảnh tu treo");
            return null;
        }


        String TEN_ANH = tableAnh.getTEN_ANH();
        if (TextUtils.isEmpty(TEN_ANH))
            return null;

        String folderAnh = "";
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return null;

            case IMAGE_TU:
            case IMAGE_MACH_NHI_THU_TU:
            case IMAGE_NIEM_PHONG_TU:
                folderAnh = Common.FOLDER_NAME.FOLDER_ANH_TU.name();

                break;
            case IMAGE_TI:
            case IMAGE_MACH_NHI_THU_TI:
            case IMAGE_NIEM_PHONG_TI:
                folderAnh = Common.FOLDER_NAME.FOLDER_ANH_TI.name();

                break;
        }

        String pathAnh = Common.getRecordDirectoryFolder(folderAnh + "/" + TEN_ANH);
        Bitmap bitmap = Common.getBitmapFromUri(pathAnh);
        if (bitmap == null)
            return null;

        return bitmap;
    }

    private void fillInfoTiTreo() {
        if (tiTreo != null) {
            tvCapdienapTitreo.setText(String.valueOf(tiTreo.getCAP_DAP()));
            tvTysobienTitreo.setText(String.valueOf(tiTreo.getTYSO_BIEN()));
            //2016-11-02T00:00:00
            tvNgayKiemdinhTitreo.setText(Common.convertDateToDate(String.valueOf(tiTreo.getNGAY_KDINH()), sqlite2, type6));
            tvSoTitreo.setText(String.valueOf(tiTreo.getSO_TU_TI()));
            tvNuocsxTitreo.setText(String.valueOf(tiTreo.getNUOC_SX()));
            tvSovongTitreo.setText(String.valueOf(tiTreo.getSO_VONG_THANH_CAI()));
            tvChiKiemdinhTitreo.setText(String.valueOf(tiTreo.getMA_CHI_KDINH()));
            tvChihopdaudayTitreo.setText(String.valueOf(tiTreo.getMA_CHI_HOP_DDAY()));
        }
    }

    private void fillInfoTiThao() {
        if (tiThao != null) {
            tvCapdienapTithao.setText(String.valueOf(tiThao.getCAP_DAP()));
            tvTysobienTithao.setText(String.valueOf(tiThao.getTYSO_BIEN()));
            //2016-11-02T00:00:00
            tvNgayKiemdinhTithao.setText(Common.convertDateToDate(String.valueOf(tiThao.getNGAY_KDINH()), sqlite2, type6));
            tvSoTithao.setText(String.valueOf(tiThao.getSO_TU_TI()));
            tvNuocsxTithao.setText(String.valueOf(tiThao.getNUOC_SX()));
            tvSovongTithao.setText(String.valueOf(tiThao.getSO_VONG_THANH_CAI()));
            tvChiKiemdinhTithao.setText(String.valueOf(tiThao.getMA_CHI_KDINH()));
            tvChihopdaudayTithao.setText(String.valueOf(tiThao.getMA_CHI_HOP_DDAY()));
        }
    }

    private void fillInfoTuTreo() {
        if (tuTreo != null) {
            tvCapdienapTutreo.setText(String.valueOf(tuTreo.getCAP_DAP()));
            tvTysobienTutreo.setText(String.valueOf(tuTreo.getTYSO_BIEN()));
            //2016-11-02T00:00:00
            tvNgayKiemdinhTutreo.setText(Common.convertDateToDate(String.valueOf(tuTreo.getNGAY_KDINH()), sqlite2, type6));
            tvSoTutreo.setText(String.valueOf(tuTreo.getSO_TU_TI()));
            tvNuocsxTutreo.setText(String.valueOf(tuTreo.getNUOC_SX()));
            tvSovongTutreo.setText(String.valueOf(tuTreo.getSO_VONG_THANH_CAI()));
            tvChiKiemdinhTutreo.setText(String.valueOf(tuTreo.getMA_CHI_KDINH()));
            tvChihopdaudayTutreo.setText(String.valueOf(tuTreo.getMA_CHI_HOP_DDAY()));
        }
    }

    private void fillInfoTuThao() {
        if (tuThao != null) {
            tvCapdienapTuthao.setText(String.valueOf(tuThao.getCAP_DAP()));
            tvTysobienTuthao.setText(String.valueOf(tuThao.getTYSO_BIEN()));
            //2016-11-02T00:00:00
            tvNgayKiemdinhTuthao.setText(Common.convertDateToDate(String.valueOf(tuThao.getNGAY_KDINH()), sqlite2, type6));
            tvSoTuthao.setText(String.valueOf(tuThao.getSO_TU_TI()));
            tvNuocsxTuthao.setText(String.valueOf(tuThao.getNUOC_SX()));
            tvSovongTuthao.setText(String.valueOf(tuThao.getSO_VONG_THANH_CAI()));
            tvChiKiemdinhTuthao.setText(String.valueOf(tuThao.getMA_CHI_KDINH()));
            tvChihopdaudayTuthao.setText(String.valueOf(tuThao.getMA_CHI_HOP_DDAY()));
        }
    }

    private void fillInfoKH() {
        if (tableBbanTuti != null) {
            tvKHTuti.setText(tableBbanTuti.getTEN_KHANG());
            tvDiachiTuti.setText(tableBbanTuti.getDCHI_HDON());
            tvLydoTuti.setText(tableBbanTuti.getLY_DO_TREO_THAO());
            tvMaGcsTuti.setText(tableBbanTuti.getMA_GCS_CTO());
        }

        if (tableChitietCto != null) {
            tvSoNoTuti.setText(tableChitietCto.getSO_CTO());
        }
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //click fab
        clickFab();

        clickMenuBottom();

        clickCapture();

    }

    private void clickCapture() {
        btnChupAnhTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhTu();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhNhiThuTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhNhiThuTu();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhNiemPhongTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhNiemPhongTu();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhTi();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhNhiThuTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhNhiThuTi();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhNiemPhongTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhNiemPhongTi();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });


        //click image
        ivAnhTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhTu.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhNhiThuTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhNhiThuTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNhiThuTu.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhNiemPhongTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhNiemPhongTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhongTu.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhTi.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhNhiThuTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhNhiThuTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNhiThuTi.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhNiemPhongTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhNiemPhongTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhongTi.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });
    }


    private void captureAnhNiemPhongTu() throws IOException {
        timeFileCaptureAnhNiemPhongTu = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU.name(), timeFileCaptureAnhNiemPhongTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_NIEMPHONG_TU);
    }

    private void captureAnhNhiThuTu() throws IOException {
        timeFileCaptureAnhNhiThuTu = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU.name(), timeFileCaptureAnhNhiThuTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_NHITHU_TU);
    }

    private void captureAnhTu() throws IOException {
        timeFileCaptureAnhTu = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name())
                + "/"
                + Common.getImageName(IMAGE_TU.name(), timeFileCaptureAnhTu, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_TU);
    }


    private void captureAnhNiemPhongTi() throws IOException {
        timeFileCaptureAnhNiemPhongTi = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI.name(), timeFileCaptureAnhNiemPhongTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_NIEMPHONG_TI);
    }

    private void captureAnhNhiThuTi() throws IOException {
        timeFileCaptureAnhNhiThuTi = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI.name(), timeFileCaptureAnhNhiThuTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_NHITHU_TI);
    }

    private void captureAnhTi() throws IOException {
        timeFileCaptureAnhTi = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_TI.name(), timeFileCaptureAnhTi, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_ANH_TI);
    }

    private void clickMenuBottom() {
        ibtnGhiTuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //check Data
                    if (isFullRequireDataTuti()) {
                        saveDataTuti();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
                }
            }
        });
    }

    private void saveDataTuti() throws Exception {

        //data common
        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());

        //ghi ảnh tu treo
        ivAnhTu.setImageBitmap(saveAndGetBitmap(IMAGE_TU));


        //ghi ảnh nhi thu tu treo
        ivAnhNhiThuTu.setImageBitmap(saveAndGetBitmap(Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU));


        //ghi ảnh niem phong tu treo
        ivAnhNiemPhongTu.setImageBitmap(saveAndGetBitmap(Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU));


        //ghi ảnh ti treo
        ivAnhTi.setImageBitmap(saveAndGetBitmap(Common.TYPE_IMAGE.IMAGE_TI));


        //ghi ảnh nhi thu ti treo
        ivAnhNhiThuTi.setImageBitmap(saveAndGetBitmap(Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));


        //ghi ảnh tu treo
        ivAnhNiemPhongTi.setImageBitmap(saveAndGetBitmap(Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));


        String sDongdienSaulap = etDongdienSaulap.getText().toString();

        String sDienapSaulap = etDienapSaulap.getText().toString();

        String sHesoKSaulap = etHesoKSaulap.getText().toString();

        String sCCXSaulap = etCCXSaulap.getText().toString();


        String sTongPSaulap = etTongPSaulap.getText().toString();

        String sTongQSaulap = etTongQSaulap.getText().toString();

        String sBTSaulap = etBTSaulap.getText().toString();

        String sCDSaulap = etCDSaulap.getText().toString();

        String sTDSaulap = etTDSaulap.getText().toString();

        String CHI_SO_SAULAP_TUTI = Common.getStringChiSo(sBTSaulap, sCDSaulap, sTDSaulap, sTongPSaulap, sTongQSaulap, loaiCto);

        String sLapquaTuSaulap = etLapquaTuSaulap.getText().toString();

        String sLapquaTiSaulap = etLapquaTiSaulap.getText().toString();

        String sHesonhanSaulap = etHesonhanSaulap.getText().toString();

        //update TABLE_CHITIET_CTO
        TABLE_CHITIET_CTO tableChitietCtoOld = (TABLE_CHITIET_CTO) tableChitietCto.clone();
        tableChitietCto.setDONG_DIEN_SAULAP_TUTI(sDongdienSaulap);
        tableChitietCto.setDIEN_AP_SAULAP_TUTI(sDienapSaulap);
        tableChitietCto.setHANGSO_K_SAULAP_TUTI(sHesoKSaulap);
        tableChitietCto.setCAP_CX_SAULAP_TUTI(Integer.parseInt(sCCXSaulap));
        tableChitietCto.setCHI_SO_SAULAP_TUTI(CHI_SO_SAULAP_TUTI);
        tableChitietCto.setSO_TU_SAULAP_TUTI(sLapquaTuSaulap);
        tableChitietCto.setSO_TI_SAULAP_TUTI(sLapquaTiSaulap);
        tableChitietCto.setHS_NHAN_SAULAP_TUTI(Integer.parseInt(sHesonhanSaulap));
        tableChitietCto.setID_CHITIET_CTO((int) mSqlDAO.updateORInsertRows(TABLE_CHITIET_CTO.class, tableChitietCtoOld, tableChitietCto));


//        //update TABLE_CHITIET_TUTI
//        if(tuThao!=null)
//        {
//            TABLE_CHITIET_TUTI tableChitietTutiOld = (TABLE_CHITIET_TUTI)tuThao;
//            tuThao.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.code);
//            tuThao.setID_TABLE_CHITIET_TUTI((int) mSqlDAO.updateORInsertRows(TABLE_CHITIET_TUTI.class, tableChitietTutiOld, tuThao));
//        }
//
//        if(tuTreo!=null)
//        {
//            TABLE_CHITIET_TUTI tableChitietTutiOld = (TABLE_CHITIET_TUTI)tuTreo;
//            tuTreo.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.code);
//            tuTreo.setID_TABLE_CHITIET_TUTI((int) mSqlDAO.updateORInsertRows(TABLE_CHITIET_TUTI.class, tableChitietTutiOld, tuTreo));
//        }
//
//        if(tiThao!=null)
//        {
//            TABLE_CHITIET_TUTI tableChitietTutiOld = (TABLE_CHITIET_TUTI)tiThao;
//            tiThao.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.code);
//            tiThao.setID_TABLE_CHITIET_TUTI((int) mSqlDAO.updateORInsertRows(TABLE_CHITIET_TUTI.class, tableChitietTutiOld, tiThao));
//        }
//
//        if(tiTreo!=null)
//        {
//            TABLE_CHITIET_TUTI tableChitietTutiOld = (TABLE_CHITIET_TUTI)tiTreo;
//            tiTreo.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.code);
//            tiTreo.setID_TABLE_CHITIET_TUTI((int) mSqlDAO.updateORInsertRows(TABLE_CHITIET_TUTI.class, tableChitietTutiOld, tiTreo));
//        }


        //update TABLE_BBAN_TUTI
        TABLE_BBAN_TUTI tableBbanTutiOld = (TABLE_BBAN_TUTI) tableBbanTuti.clone();
        tableBbanTuti.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
        tableBbanTuti.setID_TABLE_BBAN_TUTI((int) mSqlDAO.updateORInsertRows(TABLE_BBAN_TUTI.class, tableBbanTutiOld, tableBbanTuti));


        //reset
        isRefreshAnhTu = false;
        isRefreshAnhTi = false;
        isRefreshAnhNhiThuTu = false;
        isRefreshAnhNhiThuTi = false;
        isRefreshAnhNiemPhongTu = false;
        isRefreshAnhNiemPhongTi = false;


        ((TthtHnBaseActivity) getContext()).showSnackBar("Lưu thông tin dữ liệu data biên bản TU TI thành công!", null, null);
    }

    private Bitmap saveAndGetBitmap(Common.TYPE_IMAGE typeImage) throws Exception {
        //TODO save
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String SO_CTO = tableChitietCto.getSO_CTO();
        String TEN_KHANG = tableBbanCto.getTEN_KHANG();
        String MA_DDO = tableBbanCto.getMA_DDO();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());
        String TYPE_IMAGE_DRAW;
        String SO_TUTI_DRAW = "";
        String MA_DDO_DRAW;
        String SO_CTO_DRAW;
        String DATE_TIME_DRAW;
        String timeNameFileCapturedAnh;
        String timeSQLSaveAnh;
        String pathOldAnh = "";

        TABLE_ANH_HIENTRUONG tableAnhHientruongOld = null;

        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return null;

            case IMAGE_TU:
            case IMAGE_MACH_NHI_THU_TU:
            case IMAGE_NIEM_PHONG_TU:
                SO_TUTI_DRAW = "SỐ TU TREO: " + tuTreo.getSO_TU_TI();
                break;

            case IMAGE_TI:
            case IMAGE_MACH_NHI_THU_TI:
            case IMAGE_NIEM_PHONG_TI:
                SO_TUTI_DRAW = "SỐ TI TREO: " + tiTreo.getSO_TU_TI();
                break;
        }


        TYPE_IMAGE_DRAW = typeImage.nameImage;
        MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
        SO_CTO_DRAW = "SỐ C.TƠ:" + SO_CTO;
        DATE_TIME_DRAW = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type9);
        timeNameFileCapturedAnh = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        timeSQLSaveAnh = Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite1);
        String pathNewAnh = "";

        //clone data old
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return null;

            case IMAGE_TU:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhTU.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;
            case IMAGE_MACH_NHI_THU_TU:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhNhiThuTU.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;
            case IMAGE_NIEM_PHONG_TU:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhNiemPhongTU.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;

            case IMAGE_TI:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhTI.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;
            case IMAGE_MACH_NHI_THU_TI:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhNhiThuTI.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;
            case IMAGE_NIEM_PHONG_TI:
                tableAnhHientruongOld = (TABLE_ANH_HIENTRUONG) anhNiemPhongTI.clone();
                pathOldAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + tableAnhHientruongOld.getTEN_ANH();
                break;
        }

        //refresh new Data and get path anh newest
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:

                return null;

            case IMAGE_TU:
                anhTU.setCREATE_DAY(timeSQLSaveAnh);
                anhTU.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + anhTU.getTEN_ANH();
                break;
            case IMAGE_MACH_NHI_THU_TU:
                anhNhiThuTU.setCREATE_DAY(timeSQLSaveAnh);
                anhNhiThuTU.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + anhNhiThuTU.getTEN_ANH();
                break;
            case IMAGE_NIEM_PHONG_TU:
                anhNiemPhongTU.setCREATE_DAY(timeSQLSaveAnh);
                anhNiemPhongTU.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TU.name()) + "/" + anhNiemPhongTU.getTEN_ANH();
                break;

            case IMAGE_TI:
                anhTI.setCREATE_DAY(timeSQLSaveAnh);
                anhTI.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + anhTI.getTEN_ANH();
                break;
            case IMAGE_MACH_NHI_THU_TI:
                anhNhiThuTI.setCREATE_DAY(timeSQLSaveAnh);
                anhNhiThuTI.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + anhNhiThuTI.getTEN_ANH();
                break;
            case IMAGE_NIEM_PHONG_TI:
                anhNiemPhongTI.setCREATE_DAY(timeSQLSaveAnh);
                anhNiemPhongTI.setTEN_ANH(Common.getImageName(typeImage.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
                pathNewAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_TI.name()) + "/" + anhNiemPhongTI.getTEN_ANH();
                break;
        }

        Bitmap bitmap = Common.drawTextOnBitmapCongTo(getActivity(), pathOldAnh, TEN_KHANG, TYPE_IMAGE_DRAW, DATE_TIME_DRAW, SO_TUTI_DRAW, SO_CTO_DRAW, MA_DDO_DRAW);

        //rename file ảnh cũ nếu có
        if (!TextUtils.isEmpty(pathOldAnh))
            Common.renameFile(pathOldAnh, pathNewAnh);


        //update rows data và lấy ID refresh data
        //refresh data tableAnh...
        switch (typeImage) {
            case IMAGE_CONG_TO:
            case IMAGE_CONG_TO_NIEM_PHONG:
                return null;

            case IMAGE_TU:
                anhTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhTU.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhTU.setID_CHITIET_TUTI(tuTreo.getID_CHITIET_TUTI());
                anhTU.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhTU));
                break;
            case IMAGE_MACH_NHI_THU_TU:
                anhNhiThuTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNhiThuTU.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhNhiThuTU.setID_CHITIET_TUTI(tuTreo.getID_CHITIET_TUTI());
                anhNhiThuTU.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhNhiThuTU));
                break;
            case IMAGE_NIEM_PHONG_TU:
                anhNiemPhongTU.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNiemPhongTU.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhNiemPhongTU.setID_CHITIET_TUTI(tuTreo.getID_CHITIET_TUTI());
                anhNiemPhongTU.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhNiemPhongTU));
                break;

            case IMAGE_TI:
                anhTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhTI.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhTI.setID_CHITIET_TUTI(tiTreo.getID_CHITIET_TUTI());
                anhTI.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhTI));
                break;
            case IMAGE_MACH_NHI_THU_TI:
                anhNhiThuTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNhiThuTI.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhNhiThuTI.setID_CHITIET_TUTI(tiTreo.getID_CHITIET_TUTI());
                anhNhiThuTI.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhNhiThuTI));
                break;
            case IMAGE_NIEM_PHONG_TI:
                anhNiemPhongTI.setMA_NVIEN(onIDataCommom.getMaNVien());
                anhNiemPhongTI.setID_BBAN_TUTI(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? onIDataCommom.getID_BBAN_TUTI_CTO_TREO() : onIDataCommom.getID_BBAN_TUTI_CTO_THAO());
                anhNiemPhongTI.setID_CHITIET_TUTI(tiTreo.getID_CHITIET_TUTI());
                anhNiemPhongTI.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhHientruongOld, anhNiemPhongTI));
                break;
        }

        return bitmap;
    }

    private boolean isFullRequireDataTuti() throws Exception {
        //check bitmap
        Bitmap bitmap = (ivAnhTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhTu.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh TU treo!");

        bitmap = (ivAnhTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhTi.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh TI treo!");

        bitmap = (ivAnhNhiThuTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNhiThuTu.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh nhị thứ của TU treo!");

        bitmap = (ivAnhNhiThuTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNhiThuTi.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh nhị thứ của TI treo!");

        bitmap = (ivAnhNiemPhongTu.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhongTu.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh niêm phong của TU treo!");

        bitmap = (ivAnhNiemPhongTi.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhongTi.getDrawable()).getBitmap();
        if (bitmap == null)
            throw new Exception("Vui lòng chụp ảnh niêm phong của TI treo!");

        return true;
    }

    private void clickFab() {
        fabTtKh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewTuti.post(new Runnable() {
                    @Override
                    public void run() {
                        tvfabTtKh.getParent().requestChildFocus(tvfabTtKh, tvfabTtKh);
                        scrollViewTuti.scrollTo(0, tvfabTtKh.getTop());
                    }
                });
            }
        });


        fabTtTu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewTuti.post(new Runnable() {
                    @Override
                    public void run() {
                        tvfabTtTu.getParent().requestChildFocus(tvfabTtTu, tvfabTtTu);
                        scrollViewTuti.scrollTo(0, tvfabTtTu.getTop());
                    }
                });
            }
        });

        fabTtTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewTuti.post(new Runnable() {
                    @Override
                    public void run() {
                        tvfabTtTi.getParent().requestChildFocus(tvfabTtTi, tvfabTtTi);
                        scrollViewTuti.scrollTo(0, tvfabTtTi.getTop());
                    }
                });
            }
        });

        fabTtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewTuti.post(new Runnable() {
                    @Override
                    public void run() {
                        tvfabTtCamera.getParent().requestChildFocus(tvfabTtCamera, tvfabTtCamera);
                        scrollViewTuti.scrollTo(0, tvfabTtCamera.getTop());
                    }
                });
            }
        });

        fabTtCannhapsaulap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollViewTuti.post(new Runnable() {
                    @Override
                    public void run() {
                        tvfabTtCannhapsaulap.getParent().requestChildFocus(tvfabTtCannhapsaulap, tvfabTtCannhapsaulap);
                        scrollViewTuti.scrollTo(0, tvfabTtCannhapsaulap.getTop());
                    }
                });
            }
        });
    }

    public int getPos() {
        return pos;
    }

    //endregion
    public interface IOnTthtHnBBanTutiFragment {
    }
}
