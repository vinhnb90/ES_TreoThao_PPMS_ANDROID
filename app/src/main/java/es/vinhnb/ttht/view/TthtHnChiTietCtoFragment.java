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
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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
import es.vinhnb.ttht.database.table.TABLE_LOAI_CONG_TO;
import es.vinhnb.ttht.database.table.TABLE_LYDO_TREOTHAO;
import es.vinhnb.ttht.database.table.TABLE_TRAM;
import esolutions.com.esdatabaselib.baseSqlite.SqlHelper;

import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.sqlite2;
import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.type6;
import static es.vinhnb.ttht.view.TthtHnLoginActivity.BUNDLE_TAG_MENU;
import static es.vinhnb.ttht.view.TthtHnMainActivityI.*;


public class TthtHnChiTietCtoFragment extends TthtHnBaseFragment {
    private OnITthtHnChiTietCtoFragment mListener;
    private IInteractionDataCommon onIDataCommom;
    private TagMenuNaviLeft tagMenuNaviLeft;

    private TthtHnSQLDAO mSqlDAO;
    private Unbinder unbinder;


    //view
    @BindView(R.id.scrollv_chitiet)
    ScrollView scrollChitiet;


    //fab
    @BindView(R.id.menu_red)
    FloatingActionMenu fabMenu;


    @BindView(R.id.fab_tt_kh)
    FloatingActionButton fabKH;

    @BindView(R.id.fab_tt_cto)
    FloatingActionButton fabCto;


    @BindView(R.id.fab_tt_cannhap)
    FloatingActionButton fabNhap;

    @BindView(R.id.fab_tt_chupanh)
    FloatingActionButton fabChupAnh;


    //menu bottom
    @BindView(R.id.ibtn_cto_truoc)
    ImageButton btnCtoTruoc;

    @BindView(R.id.btn_ghi)
    ImageButton btnGhi;

    @BindView(R.id.ibtn_cto_tieptheo)
    ImageButton btnCtoSau;


    //tv
    @BindView(R.id.tv_2a_khachhang)
    TextView tvKH;

    @BindView(R.id.tv_3a_diachi)
    TextView tvDiaChi;

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


    //spin

    @BindView(R.id.sp_lydo)
    Spinner spLydo;
    @BindView(R.id.iv_sp_lydo)
    ImageButton ibtnLydo;

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

    //anchor
    @BindView(R.id.tv_anchor_KH)
    TextView vAnchorKH;

    @BindView(R.id.tv_anchor_cto)
    TextView vAnchorCto;


    @BindView(R.id.tv_anchor_nhap)
    TextView vAnchorNhap;

    @BindView(R.id.tv_anchor_camera)
    TextView vAnchorCamera;


    //thong tin can nhap
    @BindView(R.id.et_25b_kimniemchi)
    EditText etKimNiemChi;

    @BindView(R.id.et_26b_temcamquang)
    EditText etTemCamQuang;

    @BindView(R.id.et_27b_solancanhbao)
    EditText etSoLanCanhBao;

    @BindView(R.id.et_28b_machikiemdinh)
    EditText etMaChiKiemDinh;


    @BindView(R.id.et_29b_machibooc)
    EditText etMaCHiBooc;

    @BindView(R.id.et_28_1b_machihomhop)
    EditText etMaCHiHomHop;

    @BindView(R.id.et_36b_ghichu)
    EditText etGhiChu;


    private TABLE_TRAM tableTram;
    private TABLE_LOAI_CONG_TO tableLoaiCongTo;
    private TABLE_BBAN_CTO tableBbanCto;
    private TABLE_CHITIET_CTO tableChitietCto;
    private TABLE_LYDO_TREOTHAO tableLydoTreothao;
    private List<TABLE_LYDO_TREOTHAO> tableLydoTreothaos;
    private boolean isRefreshAnhNiemPhong;
    private boolean isRefreshChiSo;
    private TABLE_ANH_HIENTRUONG tableAnhChiso;
    private TABLE_ANH_HIENTRUONG tableAnhNiemPhong;
    private String timeFileCaptureAnhChiSo;
    private String timeFileCaptureAnhNiemPhong;
    private String cs1, cs2, cs3, cs4, cs5;
    private int pos = -1;
    ;


    public TthtHnChiTietCtoFragment() {
    }


    public static TthtHnChiTietCtoFragment newInstance(TagMenuNaviLeft tagMenuNaviLeft, int pos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TAG_MENU, tagMenuNaviLeft);
        bundle.putInt(BUNDLE_POS, pos);
        TthtHnChiTietCtoFragment fragment = new TthtHnChiTietCtoFragment();
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
            tagMenuNaviLeft = (TagMenuNaviLeft) getArguments().getSerializable(BUNDLE_TAG_MENU);
            pos = getArguments().getInt(BUNDLE_POS, -1);
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
            ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
        }


        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnITthtHnChiTietCtoFragment) {
            mListener = (OnITthtHnChiTietCtoFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IOnTthtHnMainFragment");
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
    public void onStart() {
        super.onStart();
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CTO:
                    if (isRefreshAnhNiemPhong || isRefreshChiSo) {
                        TthtHnBaseFragment.IDialog iDialog = new TthtHnBaseFragment.IDialog() {
                            @Override
                            void clickOK() {
                                isRefreshAnhNiemPhong = false;
                                isRefreshChiSo = false;
                                TthtHnChiTietCtoFragment.this.onDestroy();
                            }

                            @Override
                            void clickCancel() {
                                TthtHnChiTietCtoFragment.this.onResume();
                            }
                        }.setTextBtnOK("TIẾP TỤC").setTextBtnCancel("TRỞ LẠI").setTitle("Thông báo");


                        TthtHnBaseFragment.showDialog(getActivity(), "Dữ liệu chỉnh sửa phần ảnh chỉ số và thông tin chỉ số chưa lưu!\n Hủy việc sửa ?", iDialog);
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

    public TagMenuNaviLeft getTagMenuNaviLeft() {
        return tagMenuNaviLeft;
    }

    public TthtHnChiTietCtoFragment refresh(TagMenuNaviLeft tagMenuNaviLeft, int pos) {
        this.pos = pos;
        this.tagMenuNaviLeft = tagMenuNaviLeft;
        return this;
    }

    //region TthtHnBaseFragment
    @Override
    public void initDataAndView(View rootView) throws Exception {
        fillDataChiTietCto();
    }

    private void fillDataChiTietCto() throws Exception {

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


        //getInfo LyDo
        String MA_DVIQLY = onIDataCommom.getLoginData().getmMaDvi();
        String[] args = new String[]{MA_DVIQLY};
        tableLydoTreothaos = mSqlDAO.getLydoTreothao(args);


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


        //get TRANG_THAI_DU_LIEU
        String TRANG_THAI_DU_LIEU = tableChitietCto.getTRANG_THAI_DU_LIEU();
        Common.TRANG_THAI_DU_LIEU trangThaiDuLieu = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEU);


        //set dataAnh
        String[] argsAnh;
        List<TABLE_ANH_HIENTRUONG> tableAnhHientruongList;
        int ID_CHITIET_CTO = tableChitietCto.getID_CHITIET_CTO();


        //get info ẢNH chỉ số
        argsAnh = new String[]{onIDataCommom.getMaNVien(), String.valueOf(ID_CHITIET_CTO)};
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_CONG_TO);
        if (tableAnhHientruongList.size() != 0)
            tableAnhChiso = tableAnhHientruongList.get(0);


        //get Ảnh niêm phong
        argsAnh = new String[]{onIDataCommom.getMaNVien(), String.valueOf(ID_CHITIET_CTO)};
        tableAnhHientruongList = mSqlDAO.getAnhHienTruong(argsAnh, Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG);
        if (tableAnhHientruongList.size() != 0)
            tableAnhNiemPhong = tableAnhHientruongList.get(0);


        //fill data text view
        //CHISO
        ViewBO_CHISO viewBOChiso = new ViewBO_CHISO();
        viewBOChiso.tvCS1 = tvCS1;
        viewBOChiso.tvCS2 = tvCS2;
        viewBOChiso.tvCS3 = tvCS3;
        viewBOChiso.tvCS4 = tvCS4;
        viewBOChiso.tvCS5 = tvCS5;

        viewBOChiso.etCS1 = etCS1;
        viewBOChiso.etCS2 = etCS2;
        viewBOChiso.etCS3 = etCS3;
        viewBOChiso.etCS4 = etCS4;
        viewBOChiso.etCS5 = etCS5;

        String LOAI_CTO = tableChitietCto.getLOAI_CTO();
        String CHISO = tableChitietCto.getCHI_SO();
        showChiso(viewBOChiso, LOAI_CTO, CHISO, trangThaiDuLieu, true);


        //fill data tv
        showTextView();


        //fill data et
        showEditText(trangThaiDuLieu);


        //fill spin
        fillSpinLyDo(trangThaiDuLieu);

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
                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
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
                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS)
                    ivAnhNiemPhong.setEnabled(false);
                break;
        }

    }

    private void fillSpinLyDo(Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU) throws Exception {
        if (tableLydoTreothaos == null)
            return;

        String LY_DO_TREO_THAO = tableBbanCto.getLY_DO_TREO_THAO();
        int pos = -1;
        for (int i = 0; i < tableLydoTreothaos.size(); i++) {
            if (tableLydoTreothaos.get(i).getMA_LDO().equalsIgnoreCase(LY_DO_TREO_THAO)) {
                pos = i;
                break;
            }
        }


        if (pos < 0)
            return;


        //set adapter
        ArrayAdapter<TABLE_LYDO_TREOTHAO> adapterLydo = new ArrayAdapter<>(getActivity(),
                R.layout.row_tththn_spin, tableLydoTreothaos);
        adapterLydo.setDropDownViewResource(R.layout.row_tththn_spin_dropdown_select);
        spLoaihom.setAdapter(adapterLydo);

        spLydo.setSelection(pos);
        spLydo.invalidate();

        //nếu đã ghi
        spLoaihom.setEnabled(true);
        ibtnLoaihom.setEnabled(true);
        if (TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spLoaihom.setEnabled(false);
            ibtnLoaihom.setEnabled(false);
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
        ibtnLoaihom.setEnabled(true);
        if (TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || TRANG_THAI_DU_LIEU == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spLoaihom.setEnabled(false);
            ibtnLoaihom.setEnabled(false);
        }
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
        ibtnSochikiemdinh.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spSochikiemdinh.setEnabled(false);
            ibtnSochikiemdinh.setEnabled(false);
        }
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
        ibtnSochihomhop.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spSochihomhop.setEnabled(false);
            ibtnSochihomhop.setEnabled(false);
        }
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
        ibtnSochibooc.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spSochibooc.setEnabled(false);
            ibtnSochibooc.setEnabled(false);
        }
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
        ibtnPhuongthucdoxa.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            spPhuongthucdoxa.setEnabled(false);
            ibtnPhuongthucdoxa.setEnabled(false);
        }
    }

    private void showTextView() {
        tvLoaicto.setText(tableLoaiCongTo.getTEN_LOAI_CTO());
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
        tvngaykiemdinh.setText(Common.convertDateToDate(String.valueOf(tableChitietCto.getNGAY_KDINH()), sqlite2, type6));
        tvnuocsanxuat.setText(tableChitietCto.getTEN_NUOC());
        tvnvienTreothao.setText(".....");
        tvtramcapdien.setText(tableTram.getTEN_TRAM());
        tvvtrilapdat.setText(tableChitietCto.getMO_TA_VTRI_TREO());
    }

    private void showEditText(Common.TRANG_THAI_DU_LIEU trangThaiDuLieu) throws Exception {

        if (!TextUtils.isEmpty(tableChitietCto.getTTRANG_NPHONG()))
            etTinhTrangNiemPhong.setText(tableChitietCto.getTTRANG_NPHONG());
        etGhiChu.setText(tableChitietCto.getGHI_CHU());
        etKimNiemChi.setText(tableChitietCto.getSO_KIM_NIEM_CHI());
        etMaCHiBooc.setText(tableChitietCto.getMA_SOCBOOC());
        etMaChiKiemDinh.setText(tableChitietCto.getMA_CHIKDINH());
        etSoLanCanhBao.setText(String.valueOf(tableChitietCto.getLAN()));
        etTemCamQuang.setText(tableChitietCto.getTEM_CQUANG());

        etMaCHiHomHop.setText(tableChitietCto.getMA_SOCHOM());
        etTinhTrangNiemPhong.setHint(TextUtils.isEmpty(tableChitietCto.getTTRANG_NPHONG()) ? "Đầy đủ, dây chì nguyên vẹn, thể hiện rõ mã hiệu ở hai mặt viên chì." : tableChitietCto.getTTRANG_NPHONG());
        etGhiChu.setHint(tableChitietCto.getGHI_CHU());
        etKimNiemChi.setHint(tableChitietCto.getSO_KIM_NIEM_CHI());
        etMaCHiBooc.setHint(tableChitietCto.getMA_SOCBOOC());
        etMaChiKiemDinh.setHint(tableChitietCto.getMA_CHIKDINH());
        etSoLanCanhBao.setHint(String.valueOf(tableChitietCto.getLAN()));
        etTemCamQuang.setHint(tableChitietCto.getTEM_CQUANG());


        //set visible
        etTinhTrangNiemPhong.setEnabled(true);
        etGhiChu.setEnabled(true);
        etKimNiemChi.setEnabled(true);
        etMaCHiBooc.setEnabled(true);
        etMaChiKiemDinh.setEnabled(true);
        etMaCHiHomHop.setEnabled(true);
        etTemCamQuang.setEnabled(true);

        etCS1.setEnabled(true);
        etCS2.setEnabled(true);
        etCS3.setEnabled(true);
        etCS4.setEnabled(true);
        etCS5.setEnabled(true);

        btnGhi.setEnabled(true);
        ibtnAnhchiso.setEnabled(true);
        ibtnAnhNiemPhong.setEnabled(true);
        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS || trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_XAC_NHAN_TREN_CMIS) {
            etTinhTrangNiemPhong.setEnabled(false);
            etGhiChu.setEnabled(false);
            etKimNiemChi.setEnabled(false);
            etMaCHiBooc.setEnabled(false);
            etMaChiKiemDinh.setEnabled(false);
            etMaCHiHomHop.setEnabled(false);
            etTemCamQuang.setEnabled(false);

            etCS1.setEnabled(false);
            etCS2.setEnabled(false);
            etCS3.setEnabled(false);
            etCS4.setEnabled(false);
            etCS5.setEnabled(false);

            btnGhi.setEnabled(false);
            ibtnAnhchiso.setEnabled(false);
            ibtnAnhNiemPhong.setEnabled(false);
        }
    }

    public static void showChiso(ViewBO_CHISO viewBOChiso, String loai_cto, String chiso, Common.TRANG_THAI_DU_LIEU trangThaiDuLieu, boolean isGoneNotVisible) throws Exception {
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
        viewBOChiso.etCS1.setEnabled(true);
        viewBOChiso.etCS2.setEnabled(true);
        viewBOChiso.etCS3.setEnabled(true);
        viewBOChiso.etCS4.setEnabled(true);
        viewBOChiso.etCS5.setEnabled(true);

        if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DANG_CHO_XAC_NHAN_CMIS) {
            viewBOChiso.etCS1.setEnabled(false);
            viewBOChiso.etCS2.setEnabled(false);
            viewBOChiso.etCS3.setEnabled(false);
            viewBOChiso.etCS4.setEnabled(false);
            viewBOChiso.etCS5.setEnabled(false);
        }

        //setVisibility
        viewBOChiso.tvCS1.setVisibility(View.VISIBLE);
        viewBOChiso.tvCS2.setVisibility(View.VISIBLE);
        viewBOChiso.tvCS3.setVisibility(View.VISIBLE);
        viewBOChiso.tvCS4.setVisibility(View.VISIBLE);
        viewBOChiso.tvCS5.setVisibility(View.VISIBLE);

        viewBOChiso.etCS1.setVisibility(View.VISIBLE);
        viewBOChiso.etCS2.setVisibility(View.VISIBLE);
        viewBOChiso.etCS3.setVisibility(View.VISIBLE);
        viewBOChiso.etCS4.setVisibility(View.VISIBLE);
        viewBOChiso.etCS5.setVisibility(View.VISIBLE);


        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(loai_cto);
        TextView[] textViews = new TextView[]{viewBOChiso.tvCS1, viewBOChiso.tvCS2, viewBOChiso.tvCS3, viewBOChiso.tvCS4, viewBOChiso.tvCS5};
        EditText[] editTexts = new EditText[]{viewBOChiso.etCS1, viewBOChiso.etCS2, viewBOChiso.etCS3, viewBOChiso.etCS4, viewBOChiso.etCS5};

        for (int i = 0; i < loaiCto.bochiso.length; i++) {
            textViews[i].setText(loaiCto.bochiso[i].code);
            editTexts[i].setText(dataChiSo.get(loaiCto.bochiso[i].code));

            editTexts[i].setHint(dataChiSo.get(loaiCto.bochiso[i].code));
        }

        for (int i = (loaiCto.bochiso.length); i < textViews.length; i++) {

            textViews[i].setVisibility(isGoneNotVisible ? View.GONE : View.INVISIBLE);
            editTexts[i].setVisibility(isGoneNotVisible ? View.GONE : View.INVISIBLE);
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
                    String TEN_ANH_CONG_TO = Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.code, timeFileCaptureAnhChiSo, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                    String pathURICapturedAnhChiso = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO;
                    Common.scaleImage(pathURICapturedAnhChiso, getActivity());


                    //get bitmap tu URI
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(pathURICapturedAnhChiso, options);


                    //set image and gắn cờ đã chụp lại ảnh
                    ivAnhChiso.setImageBitmap(bitmap);
                    isRefreshChiSo = true;


                    // chỉ lưu tạm giá trị data ảnh chỉ số
                    tableAnhChiso = new TABLE_ANH_HIENTRUONG();
                    tableAnhChiso.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                    tableAnhChiso.setCREATE_DAY(timeSQLCapturedAnhChiso);
                    tableAnhChiso.setMA_NVIEN(onIDataCommom.getMaNVien());
                    tableAnhChiso.setTYPE(Common.TYPE_IMAGE.IMAGE_CONG_TO.code);
                    tableAnhChiso.setTEN_ANH(TEN_ANH_CONG_TO);
                }

                if (requestCode == CAMERA_REQUEST_CONGTO_NIEMPHONG) {
                    //get time
                    String timeSQLCapturedAnhNiemPhong = Common.getDateTimeNow(Common.DATE_TIME_TYPE.sqlite1);


                    //scale ảnh
                    String TEN_ANH_CONG_TO_NIEMPHONG = Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), timeFileCaptureAnhNiemPhong, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);
                    String pathURICapturedAnhNiemPhong = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + TEN_ANH_CONG_TO_NIEMPHONG;
                    Common.scaleImage(pathURICapturedAnhNiemPhong, getActivity());


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(pathURICapturedAnhNiemPhong, options);


                    //set image and gắn cờ đã chụp lại ảnh
                    ivAnhNiemPhong.setImageBitmap(bitmap);
                    isRefreshAnhNiemPhong = true;


                    //chỉ lưu tạm giá trị data ảnh niêm phong
                    tableAnhNiemPhong = new TABLE_ANH_HIENTRUONG();
                    tableAnhNiemPhong.setID_CHITIET_CTO(tableChitietCto.getID_CHITIET_CTO());
                    tableAnhNiemPhong.setCREATE_DAY(timeSQLCapturedAnhNiemPhong);
                    tableAnhNiemPhong.setMA_NVIEN(onIDataCommom.getMaNVien());
                    tableAnhNiemPhong.setTYPE(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.code);
                    tableAnhNiemPhong.setTEN_ANH(TEN_ANH_CONG_TO_NIEMPHONG);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
        }
    }

    @Override
    public void setAction(Bundle savedInstanceState) throws Exception {
        //check sự thay đổi nếu sửa chỉ số
        catchEditCHI_SO();


        //catch action bottom menu
        catchClickBottomBar();


        //spin click
        catchSelectSpinner();


        //anh niem phong
        catchClickAnh();


        //catch click fab
        catchClickFab();


        //call main hide progress bar load and update navigation menu , title
        onIDataCommom.setVisiblePbarLoad(false);
        onIDataCommom.setMenuNaviAndTitle(onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B ? TagMenuNaviLeft.CHITIET_CTO_TREO : TagMenuNaviLeft.CHITIET_CTO_THAO);

    }

    private void catchClickFab() {
        scrollChitiet.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                if(!fabMenu.isMenuButtonHidden())
//                    fabMenu.hideMenuButton(true);
            }
        });


        fabKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollChitiet.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollChitiet.setScrollY(vAnchorKH.getTop());
                        vAnchorKH.getParent().requestChildFocus(vAnchorKH, vAnchorKH);
                    }
                });
            }
        });

        fabCto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollChitiet.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollChitiet.setScrollY(vAnchorCto.getTop());
                        vAnchorCto.getParent().requestChildFocus(vAnchorCto, vAnchorCto);
                    }
                });
            }
        });


        fabNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollChitiet.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollChitiet.setScrollY(vAnchorNhap.getTop());
                        vAnchorNhap.getParent().requestChildFocus(vAnchorNhap, vAnchorNhap);
                    }
                });
            }
        });


        fabChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollChitiet.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollChitiet.setScrollY(vAnchorCamera.getTop());
                        vAnchorCamera.getParent().requestChildFocus(vAnchorCamera, vAnchorCamera);
                    }
                });
            }
        });

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
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
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
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
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
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
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
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex08.getContent(), e.getMessage(), null);
                }

            }
        });
    }

    private void catchSelectSpinner() {
        ibtnLydo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spLydo.performClick();
            }
        });

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
        btnCtoTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onIDataCommom.setPreCto(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
                }
            }
        });

        btnCtoSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onIDataCommom.setNextCto(pos);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
                }
            }
        });


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
                    ((TthtHnBaseActivity) getActivity()).showSnackBar(Common.MESSAGE.ex03.getContent(), e.getMessage(), null);
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
        String TEN_KHANG = tableBbanCto.getTEN_KHANG();
        String MA_DDO = tableBbanCto.getMA_DDO();
        String MA_TRAM = tableBbanCto.getMA_TRAM();
        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(tableChitietCto.getLOAI_CTO());


        //data anh
        String TYPE_IMAGE_DRAW_chiso = (onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B) ? "ẢNH CÔNG TƠ TREO" : "ẢNH CÔNG TƠ THÁO";
        String TYPE_IMAGE_DRAW_niemphong = (onIDataCommom.getMA_BDONG() == Common.MA_BDONG.B) ? "ẢNH NIÊM PHONG TREO" : "ẢNH NIÊM PHONG THÁO";

        //data CHI_SO
        String etCS1Text = TextUtils.isEmpty(etCS1.getText().toString()) ? "0" : etCS1.getText().toString();
        String etCS2Text = TextUtils.isEmpty(etCS2.getText().toString()) ? "0" : etCS2.getText().toString();
        String etCS3Text = TextUtils.isEmpty(etCS3.getText().toString()) ? "0" : etCS3.getText().toString();
        String etCS4Text = TextUtils.isEmpty(etCS4.getText().toString()) ? "0" : etCS4.getText().toString();
        String etCS5Text = TextUtils.isEmpty(etCS5.getText().toString()) ? "0" : etCS5.getText().toString();
        String CHI_SO = Common.getStringChiSo(etCS1Text, etCS2Text, etCS3Text, etCS4Text, etCS5Text, loaiCto);
        tableChitietCto.setCHI_SO(CHI_SO);

        tableChitietCto.setCHI_SO(CHI_SO);
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

        tableAnhChiso.setMA_NVIEN(onIDataCommom.getMaNVien());
        tableAnhChiso.setCREATE_DAY(timeSQLSaveAnh);
        tableAnhNiemPhong.setMA_NVIEN(onIDataCommom.getMaNVien());
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


        String sKimNiemChi = etKimNiemChi.getText().toString();
        tableChitietCto.setSO_KIM_NIEM_CHI(sKimNiemChi);
        String sTemCamQuang = etTemCamQuang.getText().toString();
        tableChitietCto.setTEM_CQUANG(sTemCamQuang);
        String sSoLanCanhBao = etSoLanCanhBao.getText().toString();
        tableChitietCto.setLAN(Integer.parseInt(sSoLanCanhBao));
        String sMaChiKiemDinh = etMaChiKiemDinh.getText().toString();
        tableChitietCto.setMA_CHIKDINH(sMaChiKiemDinh);
        String sMaCHiBooc = etMaCHiBooc.getText().toString();
        tableChitietCto.setMA_SOCBOOC(sMaCHiBooc);
        String sMaCHiHomHop = etMaCHiHomHop.getText().toString();
        tableChitietCto.setMA_SOCHOM(sMaCHiHomHop);
        String sGhiChu = etGhiChu.getText().toString();
        tableChitietCto.setGHI_CHU(sGhiChu);
        String sTinhTrangNiemPhong = etTinhTrangNiemPhong.getText().toString();
        tableChitietCto.setTTRANG_NPHONG(sTinhTrangNiemPhong);


        String loaiHom = spLoaihom.getSelectedItem().toString();
        String phuongThucDoXa = spPhuongthucdoxa.getSelectedItem().toString();
        String soChiBooc = spSochibooc.getSelectedItem().toString();
        String soChiHomHop = spSochihomhop.getSelectedItem().toString();
        String soChiKiemDinh = spSochikiemdinh.getSelectedItem().toString();

        tableChitietCto.setLOAI_HOM(Integer.parseInt(loaiHom));
        tableChitietCto.setPHUONG_THUC_DO_XA(phuongThucDoXa);
        tableChitietCto.setSO_VIENCBOOC(Integer.parseInt(soChiBooc));
        tableChitietCto.setSO_VIENCHOM(Integer.parseInt(soChiHomHop));
        tableChitietCto.setSOVIEN_CHIKDINH(Integer.parseInt(soChiKiemDinh));


        tableChitietCto.setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU.DA_GHI.content);
        tableChitietCto.setID_TABLE_CHITIET_CTO((int) mSqlDAO.updateRows(TABLE_CHITIET_CTO.class, tableChitietCtoOld, tableChitietCto));


        //show view
        showEditText(Common.TRANG_THAI_DU_LIEU.DA_GHI);


        //reset
        isRefreshChiSo = false;
        isRefreshAnhNiemPhong = false;

        ((TthtHnBaseActivity) getActivity()).showSnackBar("Lưu dữ liệu công tơ thành công!", null, null);
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
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO.name(), timeFileCaptureAnhChiSo, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


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
                + Common.getImageName(Common.TYPE_IMAGE.IMAGE_CONG_TO_NIEM_PHONG.name(), timeFileCaptureAnhNiemPhong, MA_DVIQLY, MA_TRAM, onIDataCommom.getID_BBAN_TRTH(), SO_CTO);


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
    public interface OnITthtHnChiTietCtoFragment {
    }

    public int getPos() {
        return pos;
    }

    public static class ViewBO_CHISO {
        public TextView tvCS1;
        public TextView tvCS2;
        public TextView tvCS3;
        public TextView tvCS4;
        public TextView tvCS5;


        public EditText etCS1;
        public EditText etCS2;
        public EditText etCS3;
        public EditText etCS4;
        public EditText etCS5;
    }
}
