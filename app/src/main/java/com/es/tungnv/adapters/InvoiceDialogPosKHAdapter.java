package com.es.tungnv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es.tungnv.entity.InvoicePosKHEntity;
import com.es.tungnv.utils.Common;
import com.es.tungnv.views.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUNGNV on 3/7/2016.
 */
public class InvoiceDialogPosKHAdapter extends RecyclerView.Adapter<InvoiceDialogPosKHAdapter.RecyclerViewHolder> {

    List<InvoicePosKHEntity> listData = new ArrayList<>();

    public InvoiceDialogPosKHAdapter(List<InvoicePosKHEntity> listData) {
        this.listData = listData;
    }

//    public void updateList(List<InvoicePosKHEntity> data) {
//        listData = data;
//        notifyDataSetChanged();
//    }

//    public void animateTo(List<InvoicePosKHEntity> models) {
//        applyAndAnimateRemovals(models);
//        applyAndAnimateAdditions(models);
//        applyAndAnimateMovedItems(models);
//    }

//    private void applyAndAnimateRemovals(List<InvoicePosKHEntity> newModels) {
//        for (int i = listData.size() - 1; i >= 0; i--) {
//            final InvoicePosKHEntity model = listData.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
//    }

//    private void applyAndAnimateAdditions(List<InvoicePosKHEntity> newModels) {
//        for (int i = 0, count = newModels.size(); i < count; i++) {
//            final InvoicePosKHEntity model = newModels.get(i);
//            if (!listData.contains(model)) {
//                addItem(i, model);
//            }
//        }
//    }

//    private void applyAndAnimateMovedItems(List<InvoicePosKHEntity> newModels) {
//        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//            final InvoicePosKHEntity model = newModels.get(toPosition);
//            final int fromPosition = listData.indexOf(model);
//            if (fromPosition >= 0 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }

//    public void moveItem(int fromPosition, int toPosition) {
//        final InvoicePosKHEntity model = listData.remove(fromPosition);
//        listData.add(toPosition, model);
//        notifyItemMoved(fromPosition, toPosition);
//    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.invoice_dialog_row_pos_kh, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvSTT.setText(listData.get(position).getSTT());
        holder.tvSo.setText(listData.get(position).getSO());
        holder.tvTenKH.setText(listData.get(position).getTEN_KH());
        holder.tvSDT.setText(listData.get(position).getDIEN_THOAI());
        holder.tvDiaChi.setText(listData.get(position).getDIA_CHI());
        holder.tvSoTien.setText(Common.formatMoney(new BigDecimal(listData.get(position).getSO_TIEN()).toString()));
        holder.tvNgayNop.setText(listData.get(position).getNGAY_NOP());
        holder.tvMaKH.setText(listData.get(position).getMA());
        holder.tvSoCto.setText(listData.get(position).getSO_CTO());
        holder.tvky.setText(listData.get(position).getKY());
        holder.tvThang.setText(listData.get(position).getTHANG());
        holder.tvNam.setText(listData.get(position).getNAM());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

//    public void addItem(int position, InvoicePosKHEntity data) {
//        listData.add(position, data);
//        notifyItemInserted(position);
//    }

//    public void removeItem(int position) {
//        listData.remove(position);
//        notifyItemRemoved(position);
//    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tvSTT, tvSo, tvTenKH, tvSDT, tvDiaChi, tvSoTien, tvNgayNop, tvMaKH, tvSoCto, tvky, tvThang, tvNam;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvSTT = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_stt);
            tvSo = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_so);
            tvTenKH = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_ten_kh);
            tvSDT = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_sdt);
            tvDiaChi = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_diachi);
            tvSoTien = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_sotien);
            tvNgayNop = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_ngaynop);
            tvMaKH = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_ma_kh);
            tvSoCto = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_so_cto);
            tvky = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_ky);
            tvThang = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_thang);
            tvNam = (TextView) itemView.findViewById(R.id.invoice_row_pos_kh_tv_nam);
        }
    }
}
