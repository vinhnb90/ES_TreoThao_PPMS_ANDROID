package es.vinhnb.ttht.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.tungnv.views.R;
import com.esolutions.esloginlib.lib.LoginFragment;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.database.dao.TthtHnSQLDAO;
import es.vinhnb.ttht.database.table.TABLE_BBAN_CTO;
import es.vinhnb.ttht.database.table.TABLE_CHITIET_CTO;
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

    @BindView(R.id.et_28b_machikiemdinh)
    EditText etMachikiemdinh;

    @BindView(R.id.et_29b_machibooc)
    EditText etMachibooc;


    //spin
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
    Button btnSaveAnhNiemPhong;

    @BindView(R.id.et_39b_tinhtrangniemphong)
    EditText etTinhTrangNiemPhong;


    //chup anh chi so
    @BindView(R.id.iv_40_anh_chiso)
    ImageView ivAnhChiso;

    @BindView(R.id.ibtn_40_anh_chiso)
    ImageView ibtnAnhchiso;

    @BindView(R.id.btn_40_save_anh_chiso)
    Button btnSaveAnhChiso;

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            fillDataChiTietTreo();

        if (tagMenu == TthtHnMainActivity.TagMenu.CHITIET_CTO_THAO)
            maBdong = Common.MA_BDONG.E;
    }

    private void fillDataChiTietTreo() throws Exception {
        maBdong = Common.MA_BDONG.B;


        //get Data Chi tiet cong to
        String[] agrs = new String[]{String.valueOf(ID_BBAN_TRTH), maBdong.code, mMaNVien};
        List<TABLE_CHITIET_CTO> tableChitietCtoList = mSqlDAO.getChiTietCongto(agrs);
        if (tableChitietCtoList.size() != 1)
            return;
        TABLE_CHITIET_CTO tableChitietCto = tableChitietCtoList.get(0);


        //get Data bien ban
        String[] agrsBB = new String[]{String.valueOf(ID_BBAN_TRTH), mMaNVien};
        List<TABLE_BBAN_CTO> tableBbanCtoList = mSqlDAO.getBBan(agrsBB);
        if (tableBbanCtoList.size() != 1)
            return;
        TABLE_BBAN_CTO tableBbanCto = tableBbanCtoList.get(0);


        //getInfo Chung loai
        String MA_CLOAI = tableChitietCto.getMA_CLOAI();
        String[] argsCloai = new String[]{MA_CLOAI};
        List<TABLE_LOAI_CONG_TO> tableLoaiCongToList = mSqlDAO.getLoaiCongto(argsCloai);
        if (tableLoaiCongToList.size() != 1)
            return;
        TABLE_LOAI_CONG_TO tableLoaiCongTo = tableLoaiCongToList.get(0);


        //getInfo Tram

        String MA_TRAM = tableBbanCto.getMA_TRAM();
        String[] argsTram = new String[]{MA_TRAM};
        List<TABLE_TRAM> tableTramList = mSqlDAO.getTRAM(argsTram);
        if (tableTramList.size() != 1)
            return;
        TABLE_TRAM tableTram = tableTramList.get(0);


        //get
        String TRANG_THAI_DU_LIEU = tableChitietCto.getTRANG_THAI_DU_LIEU();
        Common.TRANG_THAI_DU_LIEU trangThaiDuLieu = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(TRANG_THAI_DU_LIEU);


        //fill data text view


        //CHISO
        String LOAI_CTO = tableChitietCto.getLOAI_CTO();
        String CHISO = tableChitietCto.getCHI_SO();
        showChiso(LOAI_CTO, CHISO, trangThaiDuLieu);


        //fill data tv
        showTextView(tableBbanCto, tableChitietCto, tableLoaiCongTo, tableTram);


        //fill data et
        showEditText(tableBbanCto, tableChitietCto, tableLoaiCongTo, TRANG_THAI_DU_LIEU);

    }

    private void showTextView(TABLE_BBAN_CTO tableBbanCto, TABLE_CHITIET_CTO tableChitietCto, TABLE_LOAI_CONG_TO tableLoaiCongTo, TABLE_TRAM tableTram) {
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

    private void showEditText(TABLE_BBAN_CTO tableBbanCto, TABLE_CHITIET_CTO tableChitietCto, TABLE_LOAI_CONG_TO tableLoaiCongTo, String TRANG_THAI_DU_LIEU) throws Exception {

        etTinhTrangNiemPhong.setText(tableChitietCto.getTTRANG_NPHONG());
        etGhichu.setText(tableChitietCto.getGHI_CHU());
        etKimNiemChi.setText(tableChitietCto.getSO_KIM_NIEM_CHI());
        etMachibooc.setText(tableChitietCto.getMA_SOCBOOC());
        etMachikiemdinh.setText(tableChitietCto.getMA_CHIKDINH());
        etSolancanhbao.setText(String.valueOf(tableChitietCto.getLAN()));
        etTemcamquang.setText(tableChitietCto.getTEM_CQUANG());

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

        //restore all view
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

        etCS1.setEnabled(true);
        etCS2.setEnabled(true);
        etCS3.setEnabled(true);
        etCS4.setEnabled(true);
        etCS5.setEnabled(true);


        Common.LOAI_CTO loaiCto = Common.LOAI_CTO.findLOAI_CTO(loai_cto);
        switch (loaiCto) {
            case D1:
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

                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI) {
                    etCS1.setEnabled(false);
                    etCS2.setEnabled(false);
                    etCS3.setEnabled(false);
                    etCS4.setEnabled(false);
                    etCS5.setEnabled(false);
                }
                break;


            case DT:
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


                if (trangThaiDuLieu == Common.TRANG_THAI_DU_LIEU.DA_GUI) {
                    etCS1.setEnabled(false);
                    etCS2.setEnabled(false);
                }
                break;
        }

    }

    @Override
    void setAction(Bundle savedInstanceState) throws Exception {
        //spin click
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


        //anh niem phong
        ibtnAnhNiemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivAnhNiemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSaveAnhNiemPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //anh chi so
        ibtnAnhchiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSaveAnhChiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivAnhChiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
