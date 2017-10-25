package com.es.tungnv.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityNhanVien;
import com.es.tungnv.entity.PpmsEntityTask;
import com.es.tungnv.interfaces.ITasksChoose;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.views.ISearchVisible;
import com.es.tungnv.views.PpmsTaskActivity;
import com.es.tungnv.views.PpmsTaskDetailActivity;
import com.es.tungnv.views.R;
import com.es.tungnv.zoomImage.ImageViewTouch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PpmsDoiSoatFragment extends Fragment {

    //TODO Khai báo đối tượng UI

    private RecyclerView rViewDoiSoat;
    private LinearLayout llNoData;

    //TODO Khai báo đối tượng
    private AdapterRviewDoiSoat adapterRviewDoiSoat;
    private PpmsSqliteConnection connection;
    private static PpmsEntityNhanVien sEmp;
    private static List<PpmsEntityTask> sListTaskPrepareCommit = new ArrayList<>();
    private static boolean[] isClickChooseTaskArray = null;
    private ITasksChoose iTasksChoose;

    //region region ovrride


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PpmsTaskActivity) {
            iTasksChoose = (ITasksChoose) context;
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
        View viewRoot = inflater.inflate(R.layout.fragment_ppms_doi_soat, container, false);
        rViewDoiSoat = (RecyclerView) viewRoot.findViewById(R.id.ppms_rview_fragment_doiSoat);
        llNoData = (LinearLayout) viewRoot.findViewById(R.id.ppms_ll_image_fragment_doiSoat);

        //TODO get data from activity
        sEmp = getArguments().getParcelable("EMPLOYEE");
        if (sEmp == null) {
            return null;
        }


       /* //TODO get data task with TRANG_THAI = 1

        setDataRecyclerViewDoiSoat();
*/
        return viewRoot;
    }

    public void setDataRecyclerViewDoiSoat() {
        sListTaskPrepareCommit.clear();
        String queryGetAllTaskPrepareCommit = connection.getsQueryGetAllRowsTaskDoiSoatWithTRANG_THAI(sEmp.getNhanVienId(), 1);
        Cursor cursorTask = connection.runQueryReturnCursor(queryGetAllTaskPrepareCommit);
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
                int MA_NVIEN = cursorTask.getInt(cursorTask.getColumnIndex("MA_NVIEN"));
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
                sListTaskPrepareCommit.add(task);
            } while (cursorTask.moveToNext());
        }

        if (sListTaskPrepareCommit.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
            rViewDoiSoat.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            rViewDoiSoat.setVisibility(View.VISIBLE);

            //TODO set adapter task
            adapterRviewDoiSoat = new AdapterRviewDoiSoat(sListTaskPrepareCommit);
            rViewDoiSoat.setHasFixedSize(true);
            rViewDoiSoat.setLayoutManager(new LinearLayoutManager(getActivity()));
            rViewDoiSoat.setAdapter(adapterRviewDoiSoat);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sListTaskPrepareCommit.clear();
        isClickChooseTaskArray = null;
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PpmsCommon.REQUEST_CODE_DOISOAT_TO_DETAIL
                && resultCode == PpmsCommon.RESPONSE_CODE_DETAIL_TO_DOISOAT
                && data != null) {
            //TODO update List
            Bundle bundle = data.getExtras();
            int pos = bundle.getInt("POSITION", -1);
            PpmsEntityTask task = bundle.getParcelable("TASK");
            if (pos != -1 && task != null) {
                adapterRviewDoiSoat.updateTask(pos, task);
            }
        }
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        setDataRecyclerViewDoiSoat();
    }

    //endregion

    //region region method common
    public void setVisiblechooseOrClearAll(boolean isChoose) {
        if (isClickChooseTaskArray == null) {
            return;
        }
        if (isClickChooseTaskArray.length == 0) {
            return;
        }

        //TODO set all element boolean[]
        for (int i = 0; i < isClickChooseTaskArray.length; i++) {
            isClickChooseTaskArray[i] = isChoose;
        }

        //TODO send out to activity
        iTasksChoose.sendIClickChooseArray(isClickChooseTaskArray, sListTaskPrepareCommit);

        //TODO refresh adapter
        adapterRviewDoiSoat.notifyDataSetChanged();
        rViewDoiSoat.setAdapter(adapterRviewDoiSoat);
        rViewDoiSoat.invalidate();
    }
    //endregion

    class AdapterRviewDoiSoat extends RecyclerView.Adapter<PpmsDoiSoatFragment.AdapterRviewDoiSoat.ViewHolderDoiSoat> {
        private List<PpmsEntityTask> listTask = new ArrayList<>();
        private int widthIvAnhCto, heighIvAnhCto;

        public AdapterRviewDoiSoat(List<PpmsEntityTask> listTask) {
            if (listTask.size() != 0) {
                this.listTask = listTask;
                isClickChooseTaskArray = new boolean[listTask.size()];
            }
        }

        public void updateTask(int position, PpmsEntityTask task) {
            if (position < 0 || task == null || position > listTask.size() - 1)
                return;
            listTask.set(position, task);
            notifyDataSetChanged();
        }

        @Override
        public AdapterRviewDoiSoat.ViewHolderDoiSoat onCreateViewHolder(ViewGroup parent, int viewType) {

            //TODO return view holder
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewHolderDoiSoat taskRowView = new ViewHolderDoiSoat(inflater.inflate(R.layout.ppms_row_rview_fragment_doisoat, parent, false));
            return taskRowView;
        }

        @Override
        public void onBindViewHolder(final ViewHolderDoiSoat holder, final int position) {
            holder.getTvSTT().setText(String.valueOf(position + 1));
            holder.getTvName().setText(listTask.get(position).getTenKhachHang());
            holder.getTvAddress().setText(listTask.get(position).getDiaChiDungDien());
            holder.getTvMaSoGCS().setText(listTask.get(position).getMaSoGcs());
            holder.getTvChiSoCu().setText(String.valueOf(listTask.get(position).getChiSoCu()));
            //convert 2016-11-06T00:00:00.000 to 06/11/2016
            String ngayPhanCong = listTask.get(position).getNgayPhanCong();
            holder.getTvNgayPhanCong().setText(PpmsCommon.convertDateTimeType(ngayPhanCong, 1));
            holder.getTvSoNo().setText(listTask.get(position).getSoCongTo());
            holder.getTvChiSoMoi().setText(String.valueOf(listTask.get(position).getChiSoMoi()));
            holder.getTvChiSoPhucTra().setText(String.valueOf(listTask.get(position).getChiSoPhucTra()));

            //TODO set image
            Bitmap bitmap = null;
            File file = new File(listTask.get(position).getAnhCongTo());
            if (!file.exists()) {
                bitmap = ((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.image_not_found)).getBitmap();
            } else {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            }
            holder.getIvAnhCto().setImageBitmap(bitmap);

            //TODO set view visible
            String query = connection.getsQueryGetRowsTaskWith(listTask.get(position).getNhanVienId(), listTask.get(position).getPhanCongId());
            Cursor cursor = connection.runQueryReturnCursor(query);
            int TRANG_THAI = cursor.getInt(cursor.getColumnIndex("TRANG_THAI"));
            /*if (TRANG_THAI == 1) {
                holder.getLlRow().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_item_white));
            }
            if (TRANG_THAI == 2) {
                holder.getLlRow().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_item_gray));
            }*/

            //TODO set checked or none checked
            if (isClickChooseTaskArray[position]) {
                holder.getIvChoose().setBackgroundResource(R.drawable.ic_check);
            } else {
                holder.getIvChoose().setBackgroundResource(R.drawable.ic_uncheck);
            }
        }

        @Override
        public int getItemCount() {
            return listTask.size();
        }

        public class ViewHolderDoiSoat extends RecyclerView.ViewHolder {
            private TextView tvSTT, tvName, tvAddress, tvMaSoGCS, tvChiSoCu, tvNgayPhanCong, tvSoNo, tvChiSoMoi, tvChiSoPhucTra;
            private ImageView ivAnhCto;
            private ImageButton ivChoose;
            private LinearLayout llRow;

            public ViewHolderDoiSoat(View itemView) {
                super(itemView);
                llRow = (LinearLayout) itemView.findViewById(R.id.ppms_row_doiSoat_ll_row);
                tvSTT = (TextView) itemView.findViewById(R.id.ppms_row_doisoat_tvSTT);
                tvName = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_name);
                tvAddress = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_address);
                tvMaSoGCS = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_soGCS);
                tvChiSoCu = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_chiSo_cu);
                tvNgayPhanCong = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_ngay_phanCong);
                tvSoNo = (TextView) itemView.findViewById(R.id.ppms_row_doisoat_tv_soNo);
                tvChiSoMoi = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_chiSo_moi);
                tvChiSoPhucTra = (TextView) itemView.findViewById(R.id.ppms_row_doiSoat_tv_chiSo_phuc_tra);
                ivAnhCto = (ImageView) itemView.findViewById(R.id.ppms_row_doisoat_iv_anhCto);
                ivChoose = (ImageButton) itemView.findViewById(R.id.ppms_row_doisoat_ibtn_chon);

                //TODO set action
                llRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), PpmsTaskDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("TASK", listTask.get(getAdapterPosition()));
                        bundle.putInt("TYPE_CALL_TASK_DETAIL", 2);
                        bundle.putInt("POSITION", getAdapterPosition());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, PpmsCommon.REQUEST_CODE_DOISOAT_TO_DETAIL);
                    }
                });

                ivChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int positionDoiSanh = getAdapterPosition();
                        PpmsEntityTask taskChoose = new PpmsEntityTask();

                        //TODO set image button choose
                        if (!isClickChooseTaskArray[positionDoiSanh]) {
                            isClickChooseTaskArray[positionDoiSanh] = true;
                            ivChoose.setBackgroundResource(R.drawable.ic_check);

                        } else {
                            isClickChooseTaskArray[positionDoiSanh] = false;
                            ivChoose.setBackgroundResource(R.drawable.ic_uncheck);
                        }
                        notifyDataSetChanged();

                        //TODO send out to activity
                        iTasksChoose.sendIClickChooseArray(isClickChooseTaskArray, sListTaskPrepareCommit);
                    }
                });

                ivAnhCto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createDialogShowImage();
                    }


                });
            }

            private void createDialogShowImage() {
                try {

                    //TODO create dialog
                    final Dialog dialogImageZoom = new Dialog(getContext());
                    dialogImageZoom.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogImageZoom.setContentView(R.layout.ppms_diaglog_view_image);
                    dialogImageZoom.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialogImageZoom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogImageZoom.setCanceledOnTouchOutside(false);

                    //TODO get image
                    Bitmap bitmap = null;
                    File file = new File(listTask.get(getAdapterPosition()).getAnhCongTo());
                    if (!file.exists()) {
                        bitmap = ((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.image_not_found)).getBitmap();
                    } else {
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                    }

                    //TODO set image
                    final ImageViewTouch ivImage = (ImageViewTouch) dialogImageZoom.findViewById(R.id.ppms_dialog_ivTouch_anhCto);
                    ivImage.setImageBitmapReset(bitmap, 0, true);
                    dialogImageZoom.show();
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Lỗi:" + ex.toString(), Toast.LENGTH_LONG).show();
                }
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

            public TextView getTvSoNo() {
                return tvSoNo;
            }

            public TextView getTvChiSoMoi() {
                return tvChiSoMoi;
            }

            public TextView getTvChiSoPhucTra() {
                return tvChiSoPhucTra;
            }

            public ImageView getIvAnhCto() {
                return ivAnhCto;
            }

            public ImageButton getIvChoose() {
                return ivChoose;
            }

            public LinearLayout getLlRow() {
                return llRow;
            }
        }
    }

}

