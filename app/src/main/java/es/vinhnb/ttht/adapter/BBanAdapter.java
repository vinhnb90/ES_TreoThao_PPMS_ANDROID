package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class BBanAdapter extends RecyclerView.Adapter<BBanAdapter.ViewHolder> {

    private Context context;
    private List<DataBBanAdapter> listData = new ArrayList<>();

    public BBanAdapter(Context context, List<DataBBanAdapter> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_bban, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataBBanAdapter data = listData.get(position);
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.tvmaKH.setText(data.maKH);
        holder.tvmaHopDong.setText(data.maHopDong);
        holder.tvtenKH.setText(data.tenKH);
        holder.tvdiachiKH.setText(data.diachiKH);
        holder.tvmaGCS.setText(data.maGCS);
        holder.tvmaTramCapDien.setText(data.maTramcapdien);
        holder.tvlydo.setText(data.lydo);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataBBanAdapter> listData ){
        listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvstt;
        public TextView tvtenKH;
        public TextView tvdiachiKH;
        public TextView tvmaGCS;
        public TextView tvmaTramCapDien;
        public TextView tvmaKH;
        public TextView tvmaHopDong;
        public TextView tvlydo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_bb_stt);
            tvmaKH = itemView.findViewById(R.id.tv_bb_maKH);
            tvmaHopDong = itemView.findViewById(R.id.tv_bb_ma_hopdong);
            tvtenKH = itemView.findViewById(R.id.tv_bb_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.tv_bb_diachi);
            tvmaGCS = itemView.findViewById(R.id.tv_bb_magcs);
            tvmaTramCapDien = itemView.findViewById(R.id.tv_bb_tramcapdien);
            tvlydo = itemView.findViewById(R.id.tv_bb_lydo);
        }
    }

    public static class DataBBanAdapter {
        private String tenKH;
        private String diachiKH;
        private String maKH;
        private String maHopDong;
        private String maGCS;
        private String maTramcapdien;
        private String lydo;

        public String getMaKH() {
            return maKH;
        }

        public void setMaKH(String maKH) {
            this.maKH = maKH;
        }

        public String getMaHopDong() {
            return maHopDong;
        }

        public void setMaHopDong(String maHopDong) {
            this.maHopDong = maHopDong;
        }

        public String getTenKH() {
            return tenKH;
        }

        public void setTenKH(String tenKH) {
            this.tenKH = tenKH;
        }

        public String getDiachiKH() {
            return diachiKH;
        }

        public void setDiachiKH(String diachiKH) {
            this.diachiKH = diachiKH;
        }

        public String getMaGCS() {
            return maGCS;
        }

        public void setMaGCS(String maGCS) {
            this.maGCS = maGCS;
        }

        public String getMaTramcapdien() {
            return maTramcapdien;
        }

        public void setMaTramcapdien(String maTramcapdien) {
            this.maTramcapdien = maTramcapdien;
        }

        public String getLydo() {
            return lydo;
        }

        public void setLydo(String lydo) {
            this.lydo = lydo;
        }
    }
}
