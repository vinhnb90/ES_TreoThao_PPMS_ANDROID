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

public class ChungLoaiAdapter extends RecyclerView.Adapter<ChungLoaiAdapter.ViewHolder> {

    private Context context;
    private List<DataChungLoaiAdapter> listData = new ArrayList<>();

    public ChungLoaiAdapter(Context context, List<DataChungLoaiAdapter> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_chungloai, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataChungLoaiAdapter data = listData.get(position);
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.tvtenNuoc.setText(data.tenNuoc);
        holder.tvdienAp.setText(data.dienAp);
        holder.tvmota.setText(data.mota);
        holder.tvtenLoaiCto.setText(data.tenLoaiCto);
        holder.tvVHcong.setText(data.vhCong);
        holder.tvmaHang.setText(data.maHang);
        holder.tvphuongthucdoxa.setText(data.phuongthucdoxa);
        holder.tvadd.setText(data.tenNuoc);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataChungLoaiAdapter> listData ){
        listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvstt;
        public TextView tvmota;
        public TextView tvtenLoaiCto;
        public TextView tvVHcong;
        public TextView tvmaHang;
        public TextView tvtenNuoc;
        public TextView tvdienAp;
        public TextView tvphuongthucdoxa;
        TextView tvadd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvadd = itemView.findViewById(R.id.tv_cloai_tennuoc1);
            tvstt = itemView.findViewById(R.id.tv_cloai_stt);
            tvtenNuoc = itemView.findViewById(R.id.tv_cloai_tennuoc1);
            tvdienAp = itemView.findViewById(R.id.tv_cloai_dienap);
            tvmota = itemView.findViewById(R.id.tv_cloai_mota);
            tvtenLoaiCto = itemView.findViewById(R.id.tv_cloai_tenCLoai);
            tvVHcong = itemView.findViewById(R.id.tv_cloai_vh_cong);
            tvmaHang = itemView.findViewById(R.id.tv_cloai_ma_hang);
            tvphuongthucdoxa = itemView.findViewById(R.id.tv_cloai_phuongthucdoxa);
        }
    }

    public static class DataChungLoaiAdapter {
        private String mota;
        private String tenLoaiCto;
        private String vhCong;
        private String maHang;
        private String tenNuoc;
        private String dienAp;
        private String phuongthucdoxa;

        public String getVhCong() {
            return vhCong;
        }

        public void setVhCong(String vhCong) {
            this.vhCong = vhCong;
        }

        public String getMaHang() {
            return maHang;
        }

        public void setMaHang(String maHang) {
            this.maHang = maHang;
        }

        public String getMota() {
            return mota;
        }

        public void setMota(String mota) {
            this.mota = mota;
        }

        public String getTenLoaiCto() {
            return tenLoaiCto;
        }

        public void setTenLoaiCto(String tenLoaiCto) {
            this.tenLoaiCto = tenLoaiCto;
        }

        public String getTenNuoc() {
            return tenNuoc;
        }

        public void setTenNuoc(String tenNuoc) {
            this.tenNuoc = tenNuoc;
        }

        public String getDienAp() {
            return dienAp;
        }

        public void setDienAp(String dienAp) {
            this.dienAp = dienAp;
        }

        public String getPhuongthucdoxa() {
            return phuongthucdoxa;
        }

        public void setPhuongthucdoxa(String phuongthucdoxa) {
            this.phuongthucdoxa = phuongthucdoxa;
        }
    }
}
