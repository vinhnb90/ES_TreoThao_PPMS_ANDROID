package es.vinhnb.ttht.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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


    public TthtHnBBanTutiFragment() {
        // Required empty public constructor
    }

    public static TthtHnBBanTutiFragment newInstance() {
        TthtHnBBanTutiFragment fragment = new TthtHnBBanTutiFragment();
        Bundle bundle = new Bundle();
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
        else
            tableChitietCto = new TABLE_CHITIET_CTO();


        //get Data bien ban
        String[] agrsBB = new String[]{String.valueOf(onIDataCommom.getID_BBAN_TRTH()), onIDataCommom.getMaNVien()};
        List<TABLE_BBAN_CTO> tableBbanCtoList = mSqlDAO.getBBan(agrsBB);
        if (tableBbanCtoList.size() != 0)
            tableBbanCto = tableBbanCtoList.get(0);
        else
            tableBbanCto = new TABLE_BBAN_CTO();


        //getInfo Chung loai
        String MA_CLOAI = tableChitietCto.getMA_CLOAI();
        String[] argsCloai = new String[]{MA_CLOAI};
        List<TABLE_LOAI_CONG_TO> tableLoaiCongToList = mSqlDAO.getLoaiCongto(argsCloai);
        if (tableLoaiCongToList.size() != 0)
            tableLoaiCongTo = tableLoaiCongToList.get(0);
        else
            tableLoaiCongTo = new TABLE_LOAI_CONG_TO();


        //getInfo Tram
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String[] argsTram = new String[]{MA_TRAM};
        List<TABLE_TRAM> tableTramList = mSqlDAO.getTRAM(argsTram);
        if (tableTramList.size() != 0)
            tableTram = tableTramList.get(0);
        else
            tableTram = new TABLE_TRAM();


        //get Data Bban tuti
        List<TABLE_BBAN_TUTI> tableBbanTutiList = mSqlDAO.getBBanTuti(onIDataCommom.getID_BBAN_TUTI_CTO(), onIDataCommom.getMaNVien());
        if (tableBbanTutiList.size() != 0)
            tableBbanTuti = tableBbanTutiList.get(0);
        else
            tableBbanTuti = new TABLE_BBAN_TUTI();


        //get Data chi tiet tuti
        List<TABLE_CHITIET_TUTI> tableChitietTutiList = mSqlDAO.getChitietTuTi(onIDataCommom.getID_BBAN_TUTI_CTO(), onIDataCommom.getMaNVien());
        for (TABLE_CHITIET_TUTI tableChitietTuti :
                tableChitietTutiList) {

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

        //fill KH
        fillInfoKH();


        //fill Tu thao
        fillInfoTuThao();


        fillInfoTuTreo();


        fillInfoTiThao();


        fillInfoTiTreo();


        //get TRANG_THAI_DU_LIEU
        String TRANG_THAI_DU_LIEU = tableBbanTuti.getTRANG_THAI_DU_LIEU();
        Common.TRANG_THAI_DU_LIEU trangThaiDuLieu = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEU);


        //fill data ảnh tu treo
        ivAnhTu.setImageBitmap(getAnh(tuTreo, Common.TYPE_IMAGE.IMAGE_TU));
        btnChupAnhTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhTu.setEnabled(false);


        //fill data ảnh tu treo nhi thứ
        ivAnhNhiThuTu.setImageBitmap(getAnh(tuTreo, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TU));
        btnChupAnhNhiThuTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhNhiThuTu.setEnabled(false);


        //fill data ảnh tu treo niêm phong
        ivAnhNiemPhongTu.setImageBitmap(getAnh(tuTreo, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TU));
        btnChupAnhNiemPhongTu.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhNiemPhongTu.setEnabled(false);


        //fill data ảnh ti treo
        ivAnhTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_TI));
        btnChupAnhTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhTi.setEnabled(false);


        //fill data ảnh ti treo nhi thứ
        ivAnhNhiThuTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_MACH_NHI_THU_TI));
        btnChupAnhNhiThuTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhNhiThuTi.setEnabled(false);


        //fill data ảnh ti treo niêm phong
        ivAnhNiemPhongTi.setImageBitmap(getAnh(tiTreo, Common.TYPE_IMAGE.IMAGE_NIEM_PHONG_TI));
        btnChupAnhNiemPhongTi.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
            btnChupAnhNiemPhongTi.setEnabled(false);


    }

    private Bitmap getAnh(TABLE_CHITIET_TUTI dataTuTi, Common.TYPE_IMAGE typeImage) {
        if (tableBbanTuti == null)
            return null;
        if (dataTuTi == null)
            return null;

        //get info ẢNH Tu
        String[] argsAnhTuTreo;
        argsAnhTuTreo = new String[]{onIDataCommom.getMaNVien(), String.valueOf(onIDataCommom.getID_BBAN_TUTI_CTO()), String.valueOf(dataTuTi.getID_CHITIET_TUTI())};
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

    private void saveDataTuti() {

        //data common
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String SO_CTO = tableChitietCto.getSO_CTO();
        String TEN_KHANG = tableBbanCto.getTEN_KHANG();
        String MA_DDO = tableBbanCto.getMA_DDO();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());


        //data anh
        String TYPE_IMAGE_DRAW_chiso = (onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B) ? "ẢNH CÔNG TƠ TREO" : "ẢNH CÔNG TƠ THÁO";
        String TYPE_IMAGE_DRAW_niemphong = (onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B) ? "ẢNH NIÊM PHONG TREO" : "ẢNH NIÊM PHONG THÁO";
        String CHI_SO_DRAW = (onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B) ? "CS TREO: " + tableChitietCto.getCHI_SO() : "CS THÁO: " + tableChitietCto.getCHI_SO();
        String MA_DDO_DRAW = "MÃ Đ.ĐO:" + MA_DDO;
        String SO_CTO_DRAW = "SỐ C.TƠ:" + SO_CTO;
        String timeDrawCapturedAnh = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type9);
        String timeNameFileCapturedAnh = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String timeSQLSaveAnh = Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite1);
        String pathOldAnhChiSo = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhChiso.getTEN_ANH();
        String pathOldAnhNiemPhong = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhNiemPhong.getTEN_ANH();


        //TODO
        //xóa row sql ảnh vừa chụp
        //update row sql ảnh với time hiện tại

        //change data
        TABLE_CHITIET_CTO tableChitietCtoOld = (TABLE_CHITIET_CTO) tableChitietCto.clone();
        TABLE_BBAN_CTO tableBbanCtoOld = (TABLE_BBAN_CTO) tableBbanCto.clone();

        TABLE_ANH_HIENTRUONG tableAnhChisoOld = (TABLE_ANH_HIENTRUONG) tableAnhChiso.clone();
        TABLE_ANH_HIENTRUONG tableAnhNiemPhongOld = (TABLE_ANH_HIENTRUONG) tableAnhNiemPhong.clone();

        tableAnhChiso.setCREATE_DAY(timeSQLSaveAnh);
        tableAnhNiemPhong.setCREATE_DAY(timeSQLSaveAnh);
        tableAnhChiso.setTEN_ANH(Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
        tableAnhNiemPhong.setTEN_ANH(Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO));
        String pathNewAnhChiSo = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhChiso.getTEN_ANH();
        String pathNewAnhNiemPhong = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhNiemPhong.getTEN_ANH();


        Bitmap bitmapChiso = Common.drawTextOnBitmapCongTo(getActivity(), pathOldAnhChiSo, TEN_KHANG, TYPE_IMAGE_DRAW_chiso, timeDrawCapturedAnh, CHI_SO_DRAW, SO_CTO_DRAW, MA_DDO_DRAW);
        ivAnhChiso.setImageBitmap(bitmapChiso);
        Bitmap bitmapAnhNiemPhong = Common.drawTextOnBitmapCongTo(getActivity(), pathOldAnhNiemPhong, TEN_KHANG, TYPE_IMAGE_DRAW_niemphong, timeDrawCapturedAnh, "", SO_CTO_DRAW, MA_DDO_DRAW);
        ivAnhNiemPhong.setImageBitmap(bitmapAnhNiemPhong);


        //rename file old ảnh chi so
        Common.renameFile(pathOldAnhChiSo, pathNewAnhChiSo);
        Common.renameFile(pathOldAnhNiemPhong, pathNewAnhNiemPhong);


        //update rows data và lấy ID refresh data
        //refresh data tableAnh...
        tableAnhChiso.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhChisoOld, tableAnhChiso));
        tableAnhNiemPhong.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.updateORInsertRows(TABLE_ANH_HIENTRUONG.class, tableAnhNiemPhongOld, tableAnhNiemPhong));


        //data CHI_SO
        String etCS1Text = TextUtils.isEmpty(etCS1.getText().toString()) ? "0" : etCS1.getText().toString();
        String etCS2Text = TextUtils.isEmpty(etCS2.getText().toString()) ? "0" : etCS2.getText().toString();
        String etCS3Text = TextUtils.isEmpty(etCS3.getText().toString()) ? "0" : etCS3.getText().toString();
        String etCS4Text = TextUtils.isEmpty(etCS4.getText().toString()) ? "0" : etCS4.getText().toString();
        String etCS5Text = TextUtils.isEmpty(etCS5.getText().toString()) ? "0" : etCS5.getText().toString();

        String CHI_SO = Common.getStringChiSo(etCS1Text, etCS2Text, etCS3Text, etCS4Text, etCS5Text, loaiCto);
        tableChitietCto.setCHI_SO(CHI_SO);
        tableChitietCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
        tableChitietCto.setID_TABLE_CHITIET_CTO((int) mSqlDAO.updateRows(TABLE_CHITIET_CTO.class, tableChitietCtoOld, tableChitietCto));


        //show view
        showEditText(Common.TRANG_THAI_DU_LIEU.DA_GHI);


        //reset
        isRefreshChiSo = false;
        isRefreshAnh = false;
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

    //endregion
    public interface IOnTthtHnBBanTutiFragment {
    }
}
