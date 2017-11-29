package com.es.tungnv.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityNhanVien;
import com.es.tungnv.entity.PpmsEntityTask;
import com.es.tungnv.interfaces.IPpmsTasks;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.views.ISearchVisible;
import com.es.tungnv.views.PpmsTaskActivity;
import com.es.tungnv.views.PpmsTaskDetailActivity;
import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PpmsTaskFragment extends Fragment {
    private ISearchVisible iSearchVisible;
    //TODO Khai báo đối tượng UI
    private RecyclerView rViewTask;
    private LinearLayout llNoData;

    //TODO Khai báo đối tượng
    private AdapterRviewTask adapterRviewTask;
    private PpmsSqliteConnection connection;
    private PpmsEntityNhanVien emp;
    private static List<PpmsEntityTask> sListTask = new ArrayList<>();
    private IPpmsTasks iPpmsTasks;

    //region region ovrride

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PpmsTaskActivity) {
            iPpmsTasks = (IPpmsTasks) context;
            iSearchVisible = (ISearchVisible) context;
        } else {
            throw new ClassCastException("Lỗi khi casting context!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = PpmsSqliteConnection.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_ppms_task, container, false);
        rViewTask = (RecyclerView) viewRoot.findViewById(R.id.ppms_rview_fragment_task);
        llNoData = (LinearLayout) viewRoot.findViewById(R.id.ppms_ll_image_fragment_task);

        rViewTask.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                iSearchVisible.setVisibleSearch();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //TODO get data from activity
        emp = getArguments().getParcelable("EMPLOYEE");
        if (emp == null) {
            return null;
        }
        String idFrag = getArguments().getString("WHICH_FRAG");

        //TODO get data task with TRANG_THAI
        sListTask.clear();
        String queryGetAllTask = "";
        if (idFrag.equals("HISTORY_FRAG")) {
            queryGetAllTask = connection.getsQueryGetAllRowsTaskDoiSoatWithTRANG_THAI(emp.getNhanVienId(), 2);
        }
        if (idFrag.equals("TASK_FRAG")) {
            queryGetAllTask = connection.getsQueryGetAllRowsTaskNotCommit(emp.getNhanVienId());
        }

        Cursor cursorTask = connection.runQueryReturnCursor(queryGetAllTask);
        if (cursorTask != null) {
            do {
                int MA_PHAN_CONG = cursorTask.getInt(cursorTask.getColumnIndex("MA_PHAN_CONG"));
                String MA_DVIQLY = cursorTask.getString(cursorTask.getColumnIndex("MA_NVIEN"));
                String MA_DDO = cursorTask.getString(cursorTask.getColumnIndex("MA_DDO"));
                String MA_SO_GCS = cursorTask.getString(cursorTask.getColumnIndex("MA_SO_GCS"));
                String MA_KH = cursorTask.getString(cursorTask.getColumnIndex("MA_KH"));
                int CHI_SO_MOI = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_MOI"));
                int CHI_SO_CU = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_CU"));
                int SAN_LUONG = cursorTask.getInt(cursorTask.getColumnIndex("SAN_LUONG"));
                String SO_CTO = cursorTask.getString(cursorTask.getColumnIndex("SO_CTO"));
                String MA_CTO = cursorTask.getString(cursorTask.getColumnIndex("MA_CTO"));
                String NGAY_PHAN_CONG = cursorTask.getString(cursorTask.getColumnIndex("NGAY_PHAN_CONG"));
                String THANG_KIEM_TRA = cursorTask.getString(cursorTask.getColumnIndex("THANG_KIEM_TRA"));
                int MA_NVIEN = cursorTask.getInt(cursorTask.getColumnIndex("BUNDLE_MA_NVIEN"));
                float TY_LE_CHENH_LECH = cursorTask.getFloat(cursorTask.getColumnIndex("TY_LE_CHENH_LECH"));
                float SAN_LUONG_TB = cursorTask.getFloat(cursorTask.getColumnIndex("SAN_LUONG_TB"));
                String TEN_TRAM = cursorTask.getString(cursorTask.getColumnIndex("TEN_TRAM"));
                int HS_NHAN = cursorTask.getInt(cursorTask.getColumnIndex("HS_NHAN"));
                String ANH_CTO = cursorTask.getString(cursorTask.getColumnIndex("ANH_CTO"));
                String SAI_CHI_SO = cursorTask.getString(cursorTask.getColumnIndex("SAI_CHI_SO"));
                String TEN_KH = cursorTask.getString(cursorTask.getColumnIndex("TEN_KH"));
                String DIA_CHI_DUNG_DIEN = cursorTask.getString(cursorTask.getColumnIndex("DIA_CHI_DUNG_DIEN"));
                int SO_HOP = cursorTask.getInt(cursorTask.getColumnIndex("SO_HOP"));
                int SO_O = cursorTask.getInt(cursorTask.getColumnIndex("SO_O"));
                int SO_COT = cursorTask.getInt(cursorTask.getColumnIndex("SO_COT"));
                String LOAI_HOP = cursorTask.getString(cursorTask.getColumnIndex("LOAI_HOP"));
                String LOAI_CTO = cursorTask.getString(cursorTask.getColumnIndex("LOAI_CTO"));
                String TINH_TRANG_CTO = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_CTO"));
                String NGAY_PHUC_TRA = cursorTask.getString(cursorTask.getColumnIndex("NGAY_PHUC_TRA"));
                String NGAY_GHI_DIEN = cursorTask.getString(cursorTask.getColumnIndex("NGAY_GHI_DIEN"));
                String MUC_DICH_SD_DIEN = cursorTask.getString(cursorTask.getColumnIndex("MUC_DICH_SD_DIEN"));
                String TINH_TRANG_SD_DIEN = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_SD_DIEN"));
                String NHAN_XET_PHUC_TRA = cursorTask.getString(cursorTask.getColumnIndex("NHAN_XET_PHUC_TRA"));
                int CHI_SO_PHUC_TRA = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_PHUC_TRA"));
                String TINH_TRANG_NIEM_PHONG = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_NIEM_PHONG"));
                int TRANG_THAI = cursorTask.getInt(cursorTask.getColumnIndex("TRANG_THAI"));

                PpmsEntityTask task = new PpmsEntityTask(
                        MA_PHAN_CONG, MA_DVIQLY, MA_DDO, MA_SO_GCS, MA_KH, CHI_SO_MOI, CHI_SO_CU, SAN_LUONG,
                        SO_CTO, MA_CTO, NGAY_PHAN_CONG, THANG_KIEM_TRA, MA_NVIEN, TY_LE_CHENH_LECH, SAN_LUONG_TB,
                        TEN_TRAM, HS_NHAN, ANH_CTO, SAI_CHI_SO, TEN_KH, DIA_CHI_DUNG_DIEN, SO_HOP, SO_O, SO_COT,
                        LOAI_HOP, LOAI_CTO, TINH_TRANG_CTO, NGAY_PHUC_TRA, NGAY_GHI_DIEN, MUC_DICH_SD_DIEN,
                        TINH_TRANG_SD_DIEN, NHAN_XET_PHUC_TRA, CHI_SO_PHUC_TRA, TINH_TRANG_NIEM_PHONG);

                sListTask.add(task);
            } while (cursorTask.moveToNext());
        };


        if (sListTask.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
            rViewTask.setVisibility(View.GONE);
        } else {
            if (idFrag.equals("HISTORY_FRAG")) {
                Collections.reverse(sListTask);
            }
            iPpmsTasks.sendITaskArray(sListTask);
            llNoData.setVisibility(View.GONE);
            rViewTask.setVisibility(View.VISIBLE);

            //TODO set adapter task
            if (adapterRviewTask == null) {
                adapterRviewTask = new AdapterRviewTask(sListTask);

                rViewTask.setHasFixedSize(true);
                rViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                rViewTask.setAdapter(adapterRviewTask);
            } else {
                adapterRviewTask.notifyDataSetChanged();
                rViewTask.setHasFixedSize(true);
                rViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                rViewTask.setAdapter(adapterRviewTask);
                rViewTask.invalidate();
            }
        }


        return viewRoot;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sListTask.clear();
    }

    //endregion

    //region region method common

    public void updateDataListTaskWithTaskDoneCommit() {

        //TODO set again listTask and refresh data
        sListTask.clear();
        String queryGetAllTask = connection.getsQueryGetAllRowsTaskDoiSoatWithTRANG_THAI(emp.getNhanVienId(), 2);
        Cursor cursorTask = connection.runQueryReturnCursor(queryGetAllTask);
        if (cursorTask != null) {
            do {
                int MA_PHAN_CONG = cursorTask.getInt(cursorTask.getColumnIndex("MA_PHAN_CONG"));
                String MA_DVIQLY = cursorTask.getString(cursorTask.getColumnIndex("MA_DVIQLY"));
                String MA_DDO = cursorTask.getString(cursorTask.getColumnIndex("MA_DDO"));
                String MA_SO_GCS = cursorTask.getString(cursorTask.getColumnIndex("MA_SO_GCS"));
                String MA_KH = cursorTask.getString(cursorTask.getColumnIndex("MA_KH"));
                int CHI_SO_MOI = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_MOI"));
                int CHI_SO_CU = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_CU"));
                int SAN_LUONG = cursorTask.getInt(cursorTask.getColumnIndex("SAN_LUONG"));
                String SO_CTO = cursorTask.getString(cursorTask.getColumnIndex("SO_CTO"));
                String MA_CTO = cursorTask.getString(cursorTask.getColumnIndex("MA_CTO"));
                String NGAY_PHAN_CONG = cursorTask.getString(cursorTask.getColumnIndex("NGAY_PHAN_CONG"));
                String THANG_KIEM_TRA = cursorTask.getString(cursorTask.getColumnIndex("THANG_KIEM_TRA"));
                int MA_NVIEN = cursorTask.getInt(cursorTask.getColumnIndex("BUNDLE_MA_NVIEN"));
                float TY_LE_CHENH_LECH = cursorTask.getFloat(cursorTask.getColumnIndex("TY_LE_CHENH_LECH"));
                float SAN_LUONG_TB = cursorTask.getFloat(cursorTask.getColumnIndex("SAN_LUONG_TB"));
                String TEN_TRAM = cursorTask.getString(cursorTask.getColumnIndex("TEN_TRAM"));
                int HS_NHAN = cursorTask.getInt(cursorTask.getColumnIndex("HS_NHAN"));
                String ANH_CTO = cursorTask.getString(cursorTask.getColumnIndex("ANH_CTO"));
                String SAI_CHI_SO = cursorTask.getString(cursorTask.getColumnIndex("SAI_CHI_SO"));
                String TEN_KH = cursorTask.getString(cursorTask.getColumnIndex("TEN_KH"));
                String DIA_CHI_DUNG_DIEN = cursorTask.getString(cursorTask.getColumnIndex("DIA_CHI_DUNG_DIEN"));
                int SO_HOP = cursorTask.getInt(cursorTask.getColumnIndex("SO_HOP"));
                int SO_O = cursorTask.getInt(cursorTask.getColumnIndex("SO_O"));
                int SO_COT = cursorTask.getInt(cursorTask.getColumnIndex("SO_COT"));
                String LOAI_HOP = cursorTask.getString(cursorTask.getColumnIndex("LOAI_HOP"));
                String LOAI_CTO = cursorTask.getString(cursorTask.getColumnIndex("LOAI_CTO"));
                String TINH_TRANG_CTO = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_CTO"));
                String NGAY_PHUC_TRA = cursorTask.getString(cursorTask.getColumnIndex("NGAY_PHUC_TRA"));
                String NGAY_GHI_DIEN = cursorTask.getString(cursorTask.getColumnIndex("NGAY_GHI_DIEN"));
                String MUC_DICH_SD_DIEN = cursorTask.getString(cursorTask.getColumnIndex("MUC_DICH_SD_DIEN"));
                String TINH_TRANG_SD_DIEN = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_SD_DIEN"));
                String NHAN_XET_PHUC_TRA = cursorTask.getString(cursorTask.getColumnIndex("NHAN_XET_PHUC_TRA"));
                int CHI_SO_PHUC_TRA = cursorTask.getInt(cursorTask.getColumnIndex("CHI_SO_PHUC_TRA"));
                String TINH_TRANG_NIEM_PHONG = cursorTask.getString(cursorTask.getColumnIndex("TINH_TRANG_NIEM_PHONG"));
                int TRANG_THAI = cursorTask.getInt(cursorTask.getColumnIndex("TRANG_THAI"));

                PpmsEntityTask task = new PpmsEntityTask(
                        MA_PHAN_CONG, MA_DVIQLY, MA_DDO, MA_SO_GCS, MA_KH, CHI_SO_MOI, CHI_SO_CU, SAN_LUONG,
                        SO_CTO, MA_CTO, NGAY_PHAN_CONG, THANG_KIEM_TRA, MA_NVIEN, TY_LE_CHENH_LECH, SAN_LUONG_TB,
                        TEN_TRAM, HS_NHAN, ANH_CTO, SAI_CHI_SO, TEN_KH, DIA_CHI_DUNG_DIEN, SO_HOP, SO_O, SO_COT,
                        LOAI_HOP, LOAI_CTO, TINH_TRANG_CTO, NGAY_PHUC_TRA, NGAY_GHI_DIEN, MUC_DICH_SD_DIEN,
                        TINH_TRANG_SD_DIEN, NHAN_XET_PHUC_TRA, CHI_SO_PHUC_TRA, TINH_TRANG_NIEM_PHONG);

                sListTask.add(task);
            } while (cursorTask.moveToNext());


            //TODO refresh adapter
            //TODO set adapter task
            if (adapterRviewTask == null) {
                adapterRviewTask = new AdapterRviewTask(sListTask);

                rViewTask.setHasFixedSize(true);
                rViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                rViewTask.setAdapter(adapterRviewTask);
                rViewTask.invalidate();
            } else {
                adapterRviewTask.notifyDataSetChanged();
                rViewTask.setHasFixedSize(true);
                rViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                rViewTask.setAdapter(adapterRviewTask);
                rViewTask.invalidate();
            }
        }

    }

    public void updateList(List<PpmsEntityTask> data) {
        if (adapterRviewTask == null) {
            adapterRviewTask = new AdapterRviewTask(data);
            adapterRviewTask.notifyDataSetChanged();
            rViewTask.setHasFixedSize(true);
            rViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
            rViewTask.setAdapter(adapterRviewTask);
            rViewTask.invalidate();
        } else {
            adapterRviewTask.updateData(data);
        }
    }
    //endregion

    class AdapterRviewTask extends RecyclerView.Adapter<AdapterRviewTask.ViewHolderTask> {
        private List<PpmsEntityTask> listTask = new ArrayList<>();

        public AdapterRviewTask(List<PpmsEntityTask> listTask) {
            this.listTask = listTask;
        }

        @Override
        public AdapterRviewTask.ViewHolderTask onCreateViewHolder(ViewGroup parent, int viewType) {

            //TODO return view holder
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewHolderTask taskRowView = new ViewHolderTask(inflater.inflate(R.layout.ppms_row_rview_task_fragment, parent, false));
            return taskRowView;
        }

        @Override
        public void onBindViewHolder(ViewHolderTask holder, final int position) {
            holder.getTvSTT().setText(String.valueOf(position + 1));
            holder.getTvName().setText(listTask.get(position).getTenKhachHang());
            holder.getTvAddress().setText(listTask.get(position).getDiaChiDungDien());
            holder.getTvMaSoGCS().setText(listTask.get(position).getMaSoGcs());
            holder.getTvChiSoCu().setText(String.valueOf(listTask.get(position).getChiSoCu()));
            //convert 2016-11-06T00:00:00.000 to 06/11/2016
            String ngayPhanCong = listTask.get(position).getNgayPhanCong();
            holder.getTvNgayPhanCong().setText(PpmsCommon.convertDateTimeType(ngayPhanCong, 1));
            holder.getLl_ngayPhucTra().setVisibility(View.GONE);
            //TODO set view visible
            String query = connection.getsQueryGetRowsTaskWith(listTask.get(position).getNhanVienId(), listTask.get(position).getPhanCongId());
            Cursor cursor = connection.runQueryReturnCursor(query);
            int TRANG_THAI = cursor.getInt(cursor.getColumnIndex("TRANG_THAI"));
            holder.getLlRow().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_item_white));
            if (TRANG_THAI == 0) {
                holder.getIvInfo().setVisibility(View.INVISIBLE);

            }
            if (TRANG_THAI == 1) {

                holder.getIvInfo().setVisibility(View.VISIBLE);
                holder.getIvInfo().setBackground((ContextCompat.getDrawable(getActivity(), R.drawable.ic_ppms_task_update_48)));
            }
            if (TRANG_THAI == 2) {
                holder.getLl_ngayPhucTra().setVisibility(View.VISIBLE);
                String ngayPhucTra = listTask.get(position).getNgayPhucTra();
                holder.getTvNgayPhucTra().setText(PpmsCommon.convertDateTimeType(ngayPhucTra, 1));
                holder.getIvInfo().setVisibility(View.VISIBLE);
                holder.getIvInfo().setBackground((ContextCompat.getDrawable(getActivity(), R.drawable.ic_ppms_upload_menu_48)));
            }

        }

        @Override
        public int getItemCount() {
            return listTask.size();
        }

        public class ViewHolderTask extends RecyclerView.ViewHolder {
            private TextView tvSTT, tvName, tvAddress, tvMaSoGCS, tvChiSoCu, tvNgayPhanCong, tvNgayPhucTra;
            private Button ivInfo;
            private LinearLayout llRow, ll_ngayPhucTra;

            public ViewHolderTask(View itemView) {
                super(itemView);
                llRow = (LinearLayout) itemView.findViewById(R.id.ll_ppms_row_rview_task);
                ll_ngayPhucTra = (LinearLayout) itemView.findViewById(R.id.ll_ppms_row_rview_task_fragment_ngayPhucTra);

                tvSTT = (TextView) itemView.findViewById(R.id.ppms_row_task_stt);
                tvName = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_name);
                tvAddress = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_address);
                tvMaSoGCS = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_soGCS);
                tvChiSoCu = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_chiSo_cu);
                tvNgayPhanCong = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_ngay_phanCong);
                tvNgayPhucTra = (TextView) itemView.findViewById(R.id.ppms_row_task_tv_ngay_phucTra);
                ivInfo = (Button) itemView.findViewById(R.id.ppms_row_task_btn_state);

                //TODO set action
                llRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PpmsTaskDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("TASK", listTask.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            public TextView getTvSTT() {
                return tvSTT;
            }

            public TextView getTvName() {
                return tvName;
            }

            public TextView getTvAddress() {
                return tvAddress;
            }

            public TextView getTvMaSoGCS() {
                return tvMaSoGCS;
            }

            public TextView getTvChiSoCu() {
                return tvChiSoCu;
            }

            public TextView getTvNgayPhanCong() {
                return tvNgayPhanCong;
            }

            public TextView getTvNgayPhucTra() {
                return tvNgayPhucTra;
            }

            public Button getIvInfo() {
                return ivInfo;
            }

            public LinearLayout getLlRow() {
                return llRow;
            }

            public LinearLayout getLl_ngayPhucTra() {
                return ll_ngayPhucTra;
            }
        }

        public void updateData(List<PpmsEntityTask> data) {
            listTask.clear();
            listTask.addAll(data);
            notifyDataSetChanged();
        }
    }
}
