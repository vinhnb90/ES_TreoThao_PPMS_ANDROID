package es.vinhnb.ttht.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;
import com.github.clans.fab.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_ANH_HIENTRUONG;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
import es.vinhnb.ttht.database.table.TABLE_DVIQLY;
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.common.Common.LOAI_CTO.D1;
import static es.vinhnb.ttht.common.Common.LOAI_CTO.DT;
import static es.vinhnb.ttht.view.TthtHnLoginActivity.BUNDLE_LOGIN;
import static es.vinhnb.ttht.view.TthtHnLoginActivity.BUNDLE_MA_NVIEN;
import static es.vinhnb.ttht.view.TthtHnLoginActivity.BUNDLE_TAG_MENU;


public class TthtHnChiTietCtoFragment extends TthtHnBaseFragment {
    private OnListenerTthtHnChiTietCtoFragment mListener;
    private TthtHnMainActivity.TagMenu tagMenu;
    private LoginFragment.LoginData mLoginData;
    private String mMaNVien;
    private Common.MA_BDONG maBdong;
    private int ID_BBAN_TRTH;

    private TthtHnSQLDAO mSqlDAO;
    private Unbinder unbinder;

    //menu bottom
    @BindView(R.id.btn_ghi)
    Button btnGhi;


    //tv
    @BindView(R.id.tv_2a_khachhang)
    TextView tvKH;

    @BindView(R.id.tv_3a_diachi)
    TextView tvDiaChi;

    @BindView(R.id.tv_4a_lydo)
    TextView tvLydo;

    @BindView(R.id.tv_5b_magcs)
    TextView tvMaGCS;

    @BindView(R.id.tv_6b_sono)
    TextView tvSoNo;

    @BindView(R.id.tv_8b_nvientreothao)
    TextView tvnvienTreothao;

    @BindView(R.id.tv_9b_vitrilapdat)
    TextView tvvtrilapdat;

    @BindView(R.id.tv_10b_tramcapdien)
    TextView tvtramcapdien;

    @BindView(R.id.tv_11b_loaicongto)
    TextView tvloaicongto;

    @BindView(R.id.tv_12b_hesonhan)
    TextView tvhesonhan;

    @BindView(R.id.tv_13a_nuocsanxuat)
    TextView tvnuocsanxuat;

    @BindView(R.id.tv_14b_namsanxuat)
    TextView tvnamsanxuat;

    @BindView(R.id.tv_15b_matemkiemdinh)
    TextView tvmatemkiemdinh;

    @BindView(R.id.tv_16b_ngaykiemdinh)
    TextView tvngaykiemdinh;

    @BindView(R.id.tv_17b_dongdien)
    TextView tvdongdien;

    @BindView(R.id.tv_18b_dienap)
    TextView tvdienap;

    @BindView(R.id.tv_19b_hesok)
    TextView tvhesoK;

    @BindView(R.id.tv_21b_lapquaTU)
    TextView tvlapquaTu;

    @BindView(R.id.tv_22b_lapquaTi)
    TextView tvlapquaTi;

    @BindView(R.id.et_25b_kimniemchi)
    EditText etKimNiemChi;

    @BindView(R.id.et_26b_temcamquang)
    EditText etTemcamquang;

    @BindView(R.id.et_27b_solancanhbao)
    EditText etSolancanhbao;

    @BindView(R.id.et_28_1b_machihomhop)
    EditText etMachihomhop;

    @BindView(R.id.et_28b_machikiemdinh)
    EditText etMachikiemdinh;

    @BindView(R.id.et_29b_machibooc)
    EditText etMachibooc;


    //spin
    @BindView(R.id.sp_30_1b_loaihom)
    Spinner spLoaihom;
    @BindView(R.id.iv_30a_sp_loaihom)
    ImageButton ibtnLoaihom;

    @BindView(R.id.sp_30b_sochi_kdinh)
    Spinner spSochikiemdinh;
    @BindView(R.id.iv_30a_sp_sochikiemdinh)
    ImageButton ibtnSochikiemdinh;

    @BindView(R.id.sp_31b_sochi_homhop)
    Spinner spSochihomhop;
    @BindView(R.id.iv_31a_sp_sochi_homhop)
    ImageButton ibtnSochihomhop;

    @BindView(R.id.sp_32b_sochi_booc)
    Spinner spSochibooc;
    @BindView(R.id.iv_32a_sp_sochi_booc)
    ImageButton ibtnSochibooc;

    @BindView(R.id.tv_33b_loaicongto)
    TextView tvLoaicto;

    @BindView(R.id.tv_34b_nhacungcap)
    TextView tvNhacungcap;


    @BindView(R.id.sp_35b_sp_phuongthucdoxa)
    Spinner spPhuongthucdoxa;
    @BindView(R.id.iv_35_sp_phuongthucdoxa)
    ImageButton ibtnPhuongthucdoxa;


    @BindView(R.id.et_36b_ghichu)
    EditText etGhichu;

    //chup anh cto

    @BindView(R.id.iv_37_anh_niemphong)
    ImageView ivAnhNiemPhong;

    @BindView(R.id.ibtn_37_anh_niemphong)
    ImageButton ibtnAnhNiemPhong;

    @BindView(R.id.btn_37_save_anh_niemphong)
    Button btnChupAnhNiemPhong;

    @BindView(R.id.et_39b_tinhtrangniemphong)
    EditText etTinhTrangNiemPhong;


    //chup anh chi so
    @BindView(R.id.iv_40_anh_chiso)
    ImageView ivAnhChiso;

    @BindView(R.id.ibtn_40_anh_chiso)
    ImageView ibtnAnhchiso;

    @BindView(R.id.btn_40_save_anh_chiso)
    Button btnChupAnhChiso;

    @BindView(R.id.tv_41a_CS1)
    TextView tvCS1;
    @BindView(R.id.et_41b_CS1)
    EditText etCS1;

    @BindView(R.id.tv_42a_CS2)
    TextView tvCS2;
    @BindView(R.id.et_42b_CS2)
    EditText etCS2;

    @BindView(R.id.tv_43a_CS3)
    TextView tvCS3;
    @BindView(R.id.et_43b_CS3)
    EditText etCS3;

    @BindView(R.id.tv_44a_CS4)
    TextView tvCS4;
    @BindView(R.id.et_44b_CS4)
    EditText etCS4;

    @BindView(R.id.tv_45a_CS5)
    TextView tvCS5;
    @BindView(R.id.et_45b_CS5)
    EditText etCS5;


    private TABLE_TRAM tableTram;
    private TABLE_LOAI_CONG_TO tableLoaiCongTo;
    private TABLE_BBAN_CTO tableBbanCto;
    private TABLE_CHITIET_CTO tableChitietCto;
    private boolean isRefreshAnh;
    private boolean isRefreshChiSo;
    private TABLE_ANH_HIENTRUONG tableAnhChiso;
    private TABLE_ANH_HIENTRUONG tableAnhNiemPhong;
    private String timeFileCaptureAnhChiSo;
    private String timeFileCaptureAnhNiemPhong;
    private String cs1, cs2, cs3, cs4, cs5;


    public TthtHnChiTietCtoFragment() {
        // Required empty public constructor
    }


    public static TthtHnChiTietCtoFragment newInstance(Bundle bundle) {
        TthtHnChiTietCtoFragment fragment = new TthtHnChiTietCtoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //getBundle
            mLoginData = (LoginFragment.LoginData) getArguments().getParcelable(BUNDLE_LOGIN);
            mMaNVien = getArguments().getString(BUNDLE_MA_NVIEN);
            tagMenu = (TthtHnMainActivity.TagMenu) getArguments().getSerializable(BUNDLE_TAG_MENU);
            ID_BBAN_TRTH = getArguments().getInt(TthtHnMainActivity.ID_BBAN_TRTH, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ttht_chitiet_treo, container, false);
        unbinder = ButterKnife.bind(TthtHnChiTietCtoFragment.this, viewRoot);


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
        if (context instanceof OnListenerTthtHnChiTietCtoFragment) {
            mListener = (OnListenerTthtHnChiTietCtoFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerTthtHnMainFragment");
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

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TthtHnBaseFragment.MESSAGE_CTO:
                    if (isRefreshAnh || isRefreshChiSo) {
                        IDialog iDialog = new IDialog() {
                            @Override
                            void clickOK() {
                                isRefreshAnh = false;
                                isRefreshChiSo = false;
                                TthtHnChiTietCtoFragment.this.onDestroy();
                            }

                            @Override
                            void clickCancel() {
                                TthtHnChiTietCtoFragment.this.onResume();
                            }
                        }.setTextBtnOK("TIẾP TỤC").setTextBtnCancel("TRỞ LẠI").setTitle("Thông báo");


                        showDialog("Dữ liệu chỉnh sửa phần ảnh chỉ số và thông tin chỉ số chưa lưu!\n Hủy việc sửa ?", iDialog);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Message message = new Message();
        message.obj = MESSAGE_CTO;
        mHandler.sendMessage(message);

        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public TthtHnChiTietCtoFragment switchMA_BDONG(Bundle bundle) {
        this.tagMenu = (TthtHnMainActivity.TagMenu) bundle.getSerializable(TthtHnMainActivity.BUNDLE_TAG_MENU);
        this.ID_BBAN_TRTH = bundle.getInt(TABLE_BBAN_CTO.table.ID_BBAN_TRTH.name());
        return this;
    }


    //region TthtHnBaseFragment
    @Override
    void initDataAndView(View viewRoot) throws Exception {
        if (tagMenu == TthtHnMainActivity.TagMenu.CHITIET_CTO_TREO)
            fillDataChiTietCto(Common.MA_BDONG.B);

        if (tagMenu == TthtHnMainActivity.TagMenu.CHITIET_CTO_THAO)
            fillDataChiTietCto(Common.MA_BDONG.E);
    }

    private void fillDataChiTietCto(Common.MA_BDONG maBdong) throws Exception {
        this.maBdong = maBdong;

        //get Data Chi tiet cong to
        String[] agrs = new String[]{String.valueOf(ID_BBAN_TRTH), maBdong.code, mMaNVien};
        List<TABLE_CHITIET_CTO> tableChitietCtoList = mSqlDAO.getChiTietCongto(agrs);
        if (tableChitietCtoList.size() != 0)
            tableChitietCto = tableChitietCtoList.get(0);
        else
            tableChitietCto = new TABLE_CHITIET_CTO();


        //get Data bien ban
        String[] agrsBB = new String[]{String.valueOf(ID_BBAN_TRTH), mMaNVien};
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


        //get TRANG_THAI_DU_LIEU
        String TRANG_THAI_DU_LIEU = tableChitietCto.getTRANG_THAI_DU_LIEU();
        Common.TRANG_THAI_DU_LIEU trangThaiDuLieu = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEU);


        //set dataAnh
        String[] argsAnh;
        List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList;
        int ID_CHITIET_CTO = tableChitietCto.getID_CHITIET_CTO();


        //get info ẢNH chỉ số
        argsAnh = new String[]{mMaNVien, String.valueOf(ID_CHITIET_CTO)};
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_CONG_TO);
        if (tableAnhHientruongList.size() != 0)
            tableAnhChiso = tableAnhHientruongList.get(0);
        else
            tableAnhChiso = new TABLE_ANH_HIENTRUONG();


        //get Ảnh niêm phong
        argsAnh = new String[]{mMaNVien, String.valueOf(ID_CHITIET_CTO)};
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
        if (tableAnhHientruongList.size() != 0)
            tableAnhNiemPhong = tableAnhHientruongList.get(0);
        else
            tableAnhNiemPhong = new TABLE_ANH_HIENTRUONG();


        //fill data text view
        //CHISO
        String LOAI_CTO = tableChitietCto.getLOAI_CTO();
        String CHISO = tableChitietCto.getCHI_SO();
        showChiso(LOAI_CTO, CHISO, trangThaiDuLieu);


        //fill data tv
        showTextView();


        //fill data et
        showEditText(trangThaiDuLieu);


        //fill spin
        fillSpinLoaiHom(trangThaiDuLieu);

        fillSpinPhuongthucdoxa(trangThaiDuLieu);

        fillSpinSoChibooc(trangThaiDuLieu);

        fillSpinSochiHom(trangThaiDuLieu);

        fillSpinSoChiKiemdinh(trangThaiDuLieu);


        //fill iv
        fillImageView(Common.TYPE_IMAGE.IMAGE_CONG_TO, trangThaiDuLieu);

        fillImageView(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG, trangThaiDuLieu);

    }

    private void fillImageView(Common.TYPE_IMAGE typeImage, Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) {
        //get ten anh
        String TEN_ANH = "";
        String pathAnh = "";
        Bitmap bitmap = null;


        switch (typeImage) {
            case IMAGE_CONG_TO:
                TEN_ANH = tableAnhChiso.getTEN_ANH();
                if (TextUtils.isEmpty(TEN_ANH))
                    return;


                pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH;
                bitmap = Common.getBitmapFromUri(pathAnh);
                if (bitmap == null)
                    return;


                ivAnhChiso.setImageBitmap(bitmap);
                ivAnhChiso.setEnabled(true);
                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
                    ivAnhChiso.setEnabled(false);

                break;
            case IMAGE_CONG_TO_NIEM_PHONG:
                TEN_ANH = tableAnhNiemPhong.getTEN_ANH();
                if (TextUtils.isEmpty(TEN_ANH))
                    return;


                pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH;
                bitmap = Common.getBitmapFromUri(pathAnh);
                if (bitmap == null)
                    return;


                ivAnhNiemPhong.setImageBitmap(bitmap);
                ivAnhNiemPhong.setEnabled(true);
                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI)
                    ivAnhNiemPhong.setEnabled(false);
                break;
        }

    }

    private void fillSpinLoaiHom(Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU) throws Exception {
        //set adapter
        ArrayAdapter<String> adapterSoVien = new ArrayAdapter<>(getActivity(),
                R.layout.row_tththn_spin, Common.arrLoaiHom);
        adapterSoVien.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spLoaihom.setAdapter(adapterSoVien);


        //set pos
        int posLoaiHom = Arrays.asList(Common.arrLoaiHom).indexOf(tableChitietCto.getLOAI_HOM() + "");
        spLoaihom.setSelection(posLoaiHom);


        //nếu đã ghi
        spLoaihom.setEnabled(true);
        if (TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_GHI)
            spLoaihom.setEnabled(false);
    }

    private void fillSpinSoChiKiemdinh(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {
        //set adapter
        ArrayAdapter<String> adapterSoVien = new ArrayAdapter<>(getActivity(),
                R.layout.row_tththn_spin, Common.arrSoVien);
        adapterSoVien.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spSochikiemdinh.setAdapter(adapterSoVien);


        //set pos
        int posSoVienChiKDinh = Arrays.asList(Common.arrSoVien).indexOf(tableChitietCto.getSOVIEN_CHIKDINH() + "");
        spSochikiemdinh.setSelection(posSoVienChiKDinh);


        //set enable
        spSochikiemdinh.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GHI)
            spSochikiemdinh.setEnabled(false);
    }

    private void fillSpinSochiHom(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {
        //set adapter
        ArrayAdapter<String> adapterSoVien = new ArrayAdapter<>(getActivity(),
                R.layout.row_tththn_spin, Common.arrSoVien);
        adapterSoVien.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spSochihomhop.setAdapter(adapterSoVien);


        //set pos
        int posSoVienchihom = Arrays.asList(Common.arrSoVien).indexOf(tableChitietCto.getSO_VIENCHOM() + "");
        spSochihomhop.setSelection(posSoVienchihom);


        //set enable
        spSochihomhop.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GHI)
            spSochihomhop.setEnabled(false);
    }

    private void fillSpinSoChibooc(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {
        //set adapter
        ArrayAdapter<String> adapterSoVien = new ArrayAdapter<>(getActivity(),
                R.layout.row_tththn_spin, Common.arrSoVien);
        adapterSoVien.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spSochibooc.setAdapter(adapterSoVien);


        //set pos
        int posSoVienchibooc = Arrays.asList(Common.arrSoVien).indexOf(tableChitietCto.getSO_VIENCBOOC() + "");
        spSochibooc.setSelection(posSoVienchibooc);


        //set enable
        spSochibooc.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GHI)
            spSochibooc.setEnabled(false);
    }


    private void fillSpinPhuongthucdoxa(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {
        //get data
        List<String> phuongThucDoXaList = new ArrayList<String>();
        String ALL_PHUONG_THUC_DO_XA = tableLoaiCongTo.getPTHUC_DOXA();


        //split
        if (!TextUtils.isEmpty(ALL_PHUONG_THUC_DO_XA)) {
            String[] itemPhuongThucDoXa = ALL_PHUONG_THUC_DO_XA.trim().split(",");
            phuongThucDoXaList.addAll(Arrays.asList(itemPhuongThucDoXa));
        }


        //set adapter
        ArrayAdapter<String> adapterPhuongThucDoXa = new ArrayAdapter<String>(getActivity(), R.layout.row_tththn_spin, phuongThucDoXaList);
        adapterPhuongThucDoXa.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spPhuongthucdoxa.setAdapter(adapterPhuongThucDoXa);


        //set enable
        spPhuongthucdoxa.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GHI)
            spPhuongthucdoxa.setEnabled(false);
    }

    private void showTextView() {
        tvLoaicto.setText(tableLoaiCongTo.getTEN_LOAI_CTO());
        tvLydo.setText(tableBbanCto.getLY_DO_TREO_THAO());
        tvKH.setText(tableBbanCto.getTEN_KHANG());
        tvNhacungcap.setText(tableLoaiCongTo.getMA_HANG());
        tvMaGCS.setText(tableBbanCto.getMA_GCS_CTO());
        tvSoNo.setText(tableChitietCto.getSO_CTO());
        tvDiaChi.setText(tableBbanCto.getDCHI_HDON());
        tvdienap.setText(tableChitietCto.getDIEN_AP());
        tvdongdien.setText(tableChitietCto.getDONG_DIEN());
        tvhesoK.setText(tableChitietCto.getHANGSO_K());
        tvhesonhan.setText(String.valueOf(tableChitietCto.getHS_NHAN()));
        tvlapquaTi.setText(tableChitietCto.getSO_TI_SAULAP_TUTI());
        tvlapquaTu.setText(tableChitietCto.getSO_TU_SAULAP_TUTI());

        tvloaicongto.setText(tableLoaiCongTo.getTEN_LOAI_CTO());
        tvmatemkiemdinh.setText(tableChitietCto.getMA_TEM());
        tvnamsanxuat.setText(tableChitietCto.getNAM_SX());
        tvngaykiemdinh.setText(tableChitietCto.getNGAY_KDINH());
        tvnuocsanxuat.setText(tableChitietCto.getTEN_NUOC());
        tvnvienTreothao.setText(".....");
        tvtramcapdien.setText(tableTram.getTEN_TRAM());
        tvvtrilapdat.setText(tableChitietCto.getMO_TA_VTRI_TREO());
    }

    private void showEditText(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {

        if (!TextUtils.isEmpty(tableChitietCto.getTTRANG_NPHONG()))
            etTinhTrangNiemPhong.setText(tableChitietCto.getTTRANG_NPHONG());
        etGhichu.setText(tableChitietCto.getGHI_CHU());
        etKimNiemChi.setText(tableChitietCto.getSO_KIM_NIEM_CHI());
        etMachibooc.setText(tableChitietCto.getMA_SOCBOOC());
        etMachikiemdinh.setText(tableChitietCto.getMA_CHIKDINH());
        etSolancanhbao.setText(String.valueOf(tableChitietCto.getLAN()));
        etTemcamquang.setText(tableChitietCto.getTEM_CQUANG());

        etTinhTrangNiemPhong.setHint(TextUtils.isEmpty(tableChitietCto.getTTRANG_NPHONG()) ? "Đầy đủ, dây chì nguyên vẹn, thể hiện rõ mã hiệu ở hai mặt viên chì." : tableChitietCto.getTTRANG_NPHONG());
        etGhichu.setHint(tableChitietCto.getGHI_CHU());
        etKimNiemChi.setHint(tableChitietCto.getSO_KIM_NIEM_CHI());
        etMachibooc.setHint(tableChitietCto.getMA_SOCBOOC());
        etMachikiemdinh.setHint(tableChitietCto.getMA_CHIKDINH());
        etSolancanhbao.setHint(String.valueOf(tableChitietCto.getLAN()));
        etTemcamquang.setHint(tableChitietCto.getTEM_CQUANG());


        //set visible
        etTinhTrangNiemPhong.setEnabled(true);
        etGhichu.setEnabled(true);
        etKimNiemChi.setEnabled(true);
        etMachibooc.setEnabled(true);
        etMachikiemdinh.setEnabled(true);
        etSolancanhbao.setEnabled(true);
        etTemcamquang.setEnabled(true);

        etCS1.setEnabled(true);
        etCS2.setEnabled(true);
        etCS3.setEnabled(true);
        etCS4.setEnabled(true);
        etCS5.setEnabled(true);

        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI) {
            etTinhTrangNiemPhong.setEnabled(false);
            etGhichu.setEnabled(false);
            etKimNiemChi.setEnabled(false);
            etMachibooc.setEnabled(false);
            etMachikiemdinh.setEnabled(false);
            etSolancanhbao.setEnabled(false);
            etTemcamquang.setEnabled(false);

            etCS1.setEnabled(false);
            etCS2.setEnabled(false);
            etCS3.setEnabled(false);
            etCS4.setEnabled(false);
            etCS5.setEnabled(false);
        }
    }

    private void showChiso(String loai_cto, String chiso, Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {
        //split chiso
        HashMap<String, String> dataChiSo = new HashMap<>();

        if (!TextUtils.isEmpty(chiso)) {
            String[] csoSplit = chiso.split(";");
            for (int i = 0; i < csoSplit.length; i++) {
                String[] cso = csoSplit[i].split(":");
                dataChiSo.put(cso[0], cso[1]);
            }
        }


        //set setEnabled
        etCS1.setEnabled(true);
        etCS2.setEnabled(true);
        etCS3.setEnabled(true);
        etCS4.setEnabled(true);
        etCS5.setEnabled(true);

        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI) {
            etCS1.setEnabled(false);
            etCS2.setEnabled(false);
            etCS3.setEnabled(false);
            etCS4.setEnabled(false);
            etCS5.setEnabled(false);
        }

        //setVisibility
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


        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(loai_cto);
        switch (loaiCto) {
            case D1:
            case DT:
                tvCS1.setText(D1.bochiso[0].code);
                tvCS2.setText(D1.bochiso[1].code);
                tvCS3.setText(D1.bochiso[2].code);
                tvCS4.setText(D1.bochiso[3].code);
                tvCS5.setText(D1.bochiso[4].code);

                etCS1.setText(dataChiSo.get(D1.bochiso[0].code));
                etCS2.setText(dataChiSo.get(D1.bochiso[1].code));
                etCS3.setText(dataChiSo.get(D1.bochiso[2].code));
                etCS4.setText(dataChiSo.get(D1.bochiso[3].code));
                etCS5.setText(dataChiSo.get(D1.bochiso[4].code));

                etCS1.setHint(dataChiSo.get(D1.bochiso[0].code));
                etCS2.setHint(dataChiSo.get(D1.bochiso[1].code));
                etCS3.setHint(dataChiSo.get(D1.bochiso[2].code));
                etCS4.setHint(dataChiSo.get(D1.bochiso[3].code));
                etCS5.setHint(dataChiSo.get(D1.bochiso[4].code));

                break;


            case VC:
            case HC:
                tvCS1.setText(DT.bochiso[0].code);
                tvCS2.setText(DT.bochiso[1].code);

                etCS1.setText(dataChiSo.get(DT.bochiso[0].code));
                etCS2.setText(dataChiSo.get(DT.bochiso[1].code));

                etCS1.setHint(dataChiSo.get(DT.bochiso[0].code));
                etCS2.setHint(dataChiSo.get(DT.bochiso[1].code));

                tvCS3.setVisibility(View.GONE);
                tvCS4.setVisibility(View.GONE);
                tvCS5.setVisibility(View.GONE);
                etCS3.setVisibility(View.GONE);
                etCS4.setVisibility(View.GONE);
                etCS5.setVisibility(View.GONE);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode != 0) {

                String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
                String MA_TRAM = tableBbanCto.getMA_TRAM();
                String SO_CTO = tableChitietCto.getSO_CTO();


                if (requestCode == CAMERA_REQUEST_CONGTO) {
                    //get time
                    String timeSQLCapturedAnhChiso = Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite1);


                    //scale ảnh
                    String TEN_ANH_CONG_TO = Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.code, timeFileCaptureAnhChiSo, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
                    String pathURICapturedAnhChiso = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO;
                    Common.scaleImage(pathURICapturedAnhChiso, getActivity());


                    //get bitmap tu URI
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(pathURICapturedAnhChiso, options);


                    //set image and gắn cờ đã chụp lại ảnh
                    ivAnhChiso.setImageBitmap(bitmap);
                    isRefreshAnh = true;


//                    //nếu tồn tại ảnh cũ thì xóa ảnh cũ và xóa row cũ
//                    if (!TextUtils.isEmpty(tableAnhChiso.getTEN_ANH())) {
//                        String pathURICapturedAnhChisoOld = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhChiso.getTEN_ANH();
//                        File fileOld = new File(pathURICapturedAnhChisoOld);
//                        if (fileOld.exists())
//                            fileOld.delete();
//
//
//                        //delete
//                        String[] nameCollumnDelete = new String[]{
//                                TABLE_ANH_HIENTRUONG.table.ID_TABLE_ANH_HIENTRUONG.name(),
//                        };
//                        String[] valuesDelete = new String[]{
//                                String.valueOf(tableAnhChiso.getID_TABLE_ANH_HIENTRUONG())
//                        };
//                        mSqlDAO.deleteRows(TABLE_ANH_HIENTRUONG.class, nameCollumnDelete, valuesDelete);
//                    }


                    // chỉ lưu tạm giá trị data ảnh chỉ số
                    tableAnhChiso = new TABLE_ANH_HIENTRUONG();
                    tableAnhChiso.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                    tableAnhChiso.setCREATE_DAY(timeSQLCapturedAnhChiso);
                    tableAnhChiso.setMA_NVIEN(mMaNVien);
                    tableAnhChiso.setTYPE(Common.TYPE_IMAGE.IMAGE_CONG_TO.code);
                    tableAnhChiso.setTEN_ANH(TEN_ANH_CONG_TO);
//                    tableAnhChiso.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.insert(TABLE_ANH_HIENTRUONG.class, tableAnhChiso));
                }

                if (requestCode == CAMERA_REQUEST_CONGTO_NIEMPHONG) {
                    //get time
                    String timeSQLCapturedAnhNiemPhong = Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite1);


                    //scale ảnh
                    String TEN_ANH_CONG_TO_NIEMPHONG = Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), timeFileCaptureAnhNiemPhong, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);
                    String pathURICapturedAnhNiemPhong = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO_NIEMPHONG;
                    Common.scaleImage(pathURICapturedAnhNiemPhong, getActivity());


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(pathURICapturedAnhNiemPhong, options);


                    //set image and gắn cờ đã chụp lại ảnh
                    ivAnhNiemPhong.setImageBitmap(bitmap);
                    isRefreshAnh = true;


                    //nếu tồn tại ảnh cũ thì xóa ảnh cũ
//                    if (!TextUtils.isEmpty(tableAnhNiemPhong.getTEN_ANH())) {
//                        String pathURICapturedAnhChisoOld = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + tableAnhNiemPhong.getTEN_ANH();
//                        File fileOld = new File(pathURICapturedAnhChisoOld);
//                        if (fileOld.exists())
//                            fileOld.delete();
//
//
//                        //delete
//                        String[] nameCollumnDelete = new String[]{
//                                TABLE_ANH_HIENTRUONG.table.ID_TABLE_ANH_HIENTRUONG.name(),
//                        };
//                        String[] valuesDelete = new String[]{
//                                String.valueOf(tableAnhNiemPhong.getID_TABLE_ANH_HIENTRUONG())
//                        };
//                        mSqlDAO.deleteRows(TABLE_ANH_HIENTRUONG.class, nameCollumnDelete, valuesDelete);
//                    }

                    //chỉ lưu tạm giá trị data ảnh niêm phong
                    tableAnhNiemPhong = new TABLE_ANH_HIENTRUONG();
                    tableAnhNiemPhong.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                    tableAnhNiemPhong.setCREATE_DAY(timeSQLCapturedAnhNiemPhong);
                    tableAnhNiemPhong.setMA_NVIEN(mMaNVien);
                    tableAnhNiemPhong.setTYPE(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.code);
                    tableAnhNiemPhong.setTEN_ANH(TEN_ANH_CONG_TO_NIEMPHONG);
//                    tableAnhNiemPhong.setID_TABLE_ANH_HIENTRUONG((int) mSqlDAO.insert(TABLE_ANH_HIENTRUONG.class, tableAnhNiemPhong));

                }
            }
        } catch (Exception e) {
            ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
        }
    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //check sự thay đổi nếu sửa chỉ số
        catchEditCHI_SO();


        //catch action bottom menu
        catchClickBottomBar();


        //spin click
        catchSelectSpinner();


        //anh niem phong
        catchClickAnh();
    }

    private void catchClickAnh() {
        ivAnhNiemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhNiemPhong.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhong.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        btnChupAnhNiemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    captureAnhNiemPhong();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });


        //anh chi so
        btnChupAnhChiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //check chỉ số
                    boolean writedChiso = false;
                    if (etCS1.getVisibility() == View.VISIBLE)
                        writedChiso = !TextUtils.isEmpty(etCS1.getText().toString());

                    if (etCS2.getVisibility() == View.VISIBLE)
                        writedChiso = !TextUtils.isEmpty(etCS2.getText().toString());

                    if (etCS3.getVisibility() == View.VISIBLE)
                        writedChiso = !TextUtils.isEmpty(etCS3.getText().toString());

                    if (etCS4.getVisibility() == View.VISIBLE)
                        writedChiso = !TextUtils.isEmpty(etCS4.getText().toString());

                    if (etCS5.getVisibility() == View.VISIBLE)
                        writedChiso = !TextUtils.isEmpty(etCS5.getText().toString());


                    if (writedChiso) {
                        captureAnhChiSo();
                    } else
                        throw new Exception("Phải ghi từng chỉ số trước khi chụp ảnh!");
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }
            }
        });

        ivAnhChiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //get bitmap
                    Bitmap bitmap = (ivAnhChiso.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhChiso.getDrawable()).getBitmap();
                    if (bitmap == null) {
                        return;
                    }


                    //zoom
                    Common.zoomImage(getActivity(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }

            }
        });
    }

    private void catchSelectSpinner() {
        ibtnLoaihom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLoaihom.performClick();
            }
        });

        ibtnPhuongthucdoxa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spPhuongthucdoxa.performClick();
            }
        });

        ibtnSochibooc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spSochibooc.performClick();
            }
        });

        ibtnSochihomhop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spSochihomhop.performClick();
            }
        });

        ibtnSochikiemdinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spSochikiemdinh.performClick();
            }
        });
    }

    private void catchClickBottomBar() {
        btnGhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //check Data
                    if (isFullRequireDataCto()) {
                        saveDataCto();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getContext()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
                }
            }
        });
    }

    private void catchEditCHI_SO() {
        cs1 = etCS1.getText().toString();
        cs2 = etCS2.getText().toString();
        cs3 = etCS3.getText().toString();
        cs4 = etCS4.getText().toString();
        cs5 = etCS5.getText().toString();

        if (etCS1.getVisibility() == View.VISIBLE)
            etCS1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().equals(cs1))
                        isRefreshChiSo = true;
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        etCS2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(cs2))
                    isRefreshChiSo = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCS3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(cs3))
                    isRefreshChiSo = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etCS4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(cs4))
                    isRefreshChiSo = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCS5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(cs5))
                    isRefreshChiSo = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void saveDataCto() throws Exception {
        //data common
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String SO_CTO = tableChitietCto.getSO_CTO();
        String MA_BDONG = maBdong.code;
        int ID_CHITIET_CTO = tableChitietCto.getID_CHITIET_CTO();
        String TEN_KHANG = tableBbanCto.getTEN_KHANG();
        String MA_DDO = tableBbanCto.getMA_DDO();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());


        //data anh
        String TYPE_IMAGE_DRAW_chiso = (maBdong == Common.MA_BDONG.B) ? "ẢNH CÔNG TƠ TREO" : "ẢNH CÔNG TƠ THÁO";
        String TYPE_IMAGE_DRAW_niemphong = (maBdong == Common.MA_BDONG.B) ? "ẢNH NIÊM PHONG TREO" : "ẢNH NIÊM PHONG THÁO";
        String CHI_SO_DRAW = (maBdong == Common.MA_BDONG.B) ? "CS TREO: " + tableChitietCto.getCHI_SO() : "CS THÁO: " + tableChitietCto.getCHI_SO();
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
        tableAnhChiso.setTEN_ANH(Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO));
        tableAnhNiemPhong.setTEN_ANH(Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.code, timeNameFileCapturedAnh, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO));
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

    private boolean isFullRequireDataCto() throws Exception {
        //check bitmap
        Bitmap bitmapAnhChiso = (ivAnhChiso.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhChiso.getDrawable()).getBitmap();
        if (bitmapAnhChiso == null)
            throw new Exception("Vui lòng chụp ảnh chỉ số công tơ!");


        Bitmap bitmapAnhNiemPhong = (ivAnhNiemPhong.getDrawable() == null) ? null : ((BitmapDrawable) ivAnhNiemPhong.getDrawable()).getBitmap();
        if (bitmapAnhNiemPhong == null)
            throw new Exception("Vui lòng chụp ảnh niêm phong công tơ!");


        return true;
    }

    private void captureAnhChiSo() throws Exception {
        timeFileCaptureAnhChiSo = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.name(), timeFileCaptureAnhChiSo, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CONGTO);
    }

    private void captureAnhNiemPhong() throws Exception {
        timeFileCaptureAnhNiemPhong = Common.getDateTimeNow(Common.DATE_TIME_TYPE.type12);
        String MA_DVIQLY = tableBbanCto.getMA_DVIQLY();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String SO_CTO = tableChitietCto.getSO_CTO();


        final String fileName = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name())
                + "/"
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), timeFileCaptureAnhNiemPhong, MA_DVIQLY, MA_TRAM, ID_BBAN_TRTH, SO_CTO);


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();


        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CONGTO_NIEMPHONG);
    }
    //endregion

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListenerTthtHnChiTietCtoFragment {
    }
}
