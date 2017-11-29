package com.es.tungnv.tabs;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.entity.GcsEntityKhachHang;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUNGNV on 6/24/2016.
 */
public class GcsSlbtActivity extends Fragment{

    private View mRoot;
    private Spinner spMaQuyen;
    private RecyclerView rvSLBT;
    private TextView tvTongSo;
    private GcsSqliteConnection connection;
    private ArrayList<String> arrMaQuyen;
    private ArrayAdapter<String> adapterMaQuyen;
    private ArrayList<GcsEntityKhachHang> arrKH;
    private GcsKhachHangAdapter adapterKH;

    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            mRoot = inflater.inflate(R.layout.gcs_activity_slbt, null);
            initComponents(mRoot);
            initRecycleView();
            connection = new GcsSqliteConnection(this.getActivity());
            initMaQuyen();
            return mRoot;
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo:\n"
                    + ex.toString(), Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(this.getActivity());
        rvSLBT.setHasFixedSize(true);
        rvSLBT.setLayoutManager(layoutManager);
    }

    private void initMaQuyen() {
        try{
            arrMaQuyen = new ArrayList<>();
            Cursor c = connection.getMaQuyen();
            if(c.moveToFirst()){
                do {
                    arrMaQuyen.add(c.getString(0));
                } while (c.moveToNext());
            }
            adapterMaQuyen = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, arrMaQuyen);
            spMaQuyen.setAdapter(adapterMaQuyen);

            spMaQuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        initData(parent.getItemAtPosition(position).toString());
                    } catch (Exception ex) {
                        Common.showAlertDialogGreen(GcsSlbtActivity.this.getActivity(), "Lỗi", Color.RED, "Lỗi thay đổi mã quyển:\n"
                                + ex.toString(), Color.WHITE, "OK", Color.RED);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo mã quyển:\n"
                    + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void initComponents(View mRoot) {
        spMaQuyen = (Spinner) mRoot.findViewById(R.id.gcs_activity_slbt_spMaQuyen);
        rvSLBT = (RecyclerView) mRoot.findViewById(R.id.gcs_activity_slbt_rvSLBT);
        tvTongSo = (TextView) mRoot.findViewById(R.id.gcs_activity_slbt_tvTongSo);
    }

    private void initData(String MA_QUYEN) {
        try{
            arrKH = new ArrayList<>();
            int stt = 0;
            Cursor cKhachHang = connection.getDataByMaQuyen(MA_QUYEN);
            if(cKhachHang.moveToFirst()){
                do {
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

                    float SLChenhLech = 0;
                    float SL_Cu = entityKH.getSL_CU();
                    float SL_Moi = entityKH.getSL_MOI();
                    float SL_Thao = entityKH.getSL_THAO();

                    if (SL_Cu > 0) {
                        if (SL_Moi + SL_Thao >= SL_Cu) {
                            SLChenhLech = (float) (SL_Moi - SL_Cu + SL_Thao) / (float) SL_Cu;
                        } else {
                            SLChenhLech = (float) (SL_Cu - SL_Moi - SL_Thao) / (float) SL_Cu;
                        }
                    }

                    if(GcsCommon.isSave(entityKH.getCS_MOI(), entityKH.getTTR_MOI())){
                        if (GcsCommon.cfgInfo.isWarningEnable()) {
                            if ((SL_Moi > SL_Cu && SLChenhLech >= ((float) GcsCommon.cfgInfo.getVuotDinhMuc() / 100))
                                    || (SL_Moi < SL_Cu && SLChenhLech >= ((float) GcsCommon.cfgInfo.getDuoiDinhMuc() / 100))) {
                                entityKH.setSTT(stt + 1);
                                stt++;
                                arrKH.add(entityKH);
                            }
                        } else if (GcsCommon.cfgInfo.isWarningEnable2()) {
                            SLChenhLech *= (float) SL_Cu;
                            if ((SL_Moi > SL_Cu && SLChenhLech >= (float) GcsCommon.cfgInfo.getVuotDinhMuc2())
                                    || (SL_Moi < SL_Cu && SLChenhLech >= (float) GcsCommon.cfgInfo.getDuoiDinhMuc2())) {
                                entityKH.setSTT(stt + 1);
                                stt++;
                                arrKH.add(entityKH);
                            }
                        } else {
                            if ((SL_Moi > SL_Cu && SLChenhLech >= ((float) 200 / 100))
                                    || (SL_Moi < SL_Cu && SLChenhLech >= ((float) 200 / 100))) {
                                entityKH.setSTT(stt + 1);
                                stt++;
                                arrKH.add(entityKH);
                            }
                        }
                    }

                } while (cKhachHang.moveToNext());
                adapterKH = new GcsKhachHangAdapter(arrKH);
                rvSLBT.setAdapter(adapterKH);
                tvTongSo.setText("Tổng số: " + adapterKH.getItemCount());
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu:\n"
                    + ex.toString(), Color.WHITE, "OK", Color.RED);
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

        public void animateTo(List<GcsEntityKhachHang> models) {
            applyAndAnimateRemovals(models);
            applyAndAnimateMovedItems(models);
        }

        private void applyAndAnimateRemovals(List<GcsEntityKhachHang> newModels) {
            for (int i = listData.size() - 1; i >= 0; i--) {
                final GcsEntityKhachHang model = listData.get(i);
                if (!newModels.contains(model)) {
                    removeItem(i);
                }
            }
        }

        private void applyAndAnimateAdditions(List<GcsEntityKhachHang> newModels) {
            for (int i = 0, count = newModels.size(); i < count; i++) {
                final GcsEntityKhachHang model = newModels.get(i);
                if (!listData.contains(model)) {
                    addItem(i, model);
                }
            }
        }

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
                holder.tvSTT.setText(listData.get(position).getSTT() + ".");
                holder.tvTenKH.setText(listData.get(position).getTEN_KHANG());
                holder.tvCSMoi.setText("" + listData.get(position).getCS_MOI());
                holder.tvCSCu.setText("" + listData.get(position).getCS_CU());
                holder.tvSLMoi.setText("" + listData.get(position).getSL_MOI());
                holder.tvSLCu.setText("" + listData.get(position).getSL_CU());
                holder.tvDtt.setText("" + (listData.get(position).getSL_MOI() + listData.get(position).getSL_THAO()));
                holder.tvMaCot.setText(listData.get(position).getMA_COT());
                holder.tvLoaiBCS.setText(listData.get(position).getLOAI_BCS());
                holder.tvTTRMoi.setText(listData.get(position).getTTR_MOI());
                holder.tvSoCto.setText(listData.get(position).getSERY_CTO());
            } catch(Exception ex) {
                Log.e("Error bind view", ex.toString());
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public void addItem(int position, GcsEntityKhachHang data) {
            listData.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder{

            public TextView tvSTT, tvTenKH, tvCSMoi, tvSLMoi, tvDtt, tvCSCu, tvSLCu, tvMaCot, tvLoaiBCS, tvTTRMoi, tvSoCto;
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
                ibShowMenu.setVisibility(View.GONE);
            }

        }

    }

}
