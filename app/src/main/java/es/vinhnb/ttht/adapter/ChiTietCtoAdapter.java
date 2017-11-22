package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class ChiTietCtoAdapter extends RecyclerView.Adapter<ChiTietCtoAdapter.ViewHolder> {

    private Context context;
    private List<DataChiTietCtoAdapter> listData = new ArrayList<>();

    public ChiTietCtoAdapter(Context context, List<DataChiTietCtoAdapter> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_chitiet_cto_adapter, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataChiTietCtoAdapter> listData ){
        listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DataChiTietCtoAdapter {
        private String maCto;
        private String soCto;
        private String tenKH;
        private String diachiKH;
        private String maGCS;
        private String maTram;
        private String chiso;

        public String getMaCto() {
            return maCto;
        }

        public void setMaCto(String maCto) {
            this.maCto = maCto;
        }

        public String getSoCto() {
            return soCto;
        }

        public void setSoCto(String soCto) {
            this.soCto = soCto;
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

        public String getMaTram() {
            return maTram;
        }

        public void setMaTram(String maTram) {
            this.maTram = maTram;
        }

        public String getChiso() {
            return chiso;
        }

        public void setChiso(String chiso) {
            this.chiso = chiso;
        }
    }
}
