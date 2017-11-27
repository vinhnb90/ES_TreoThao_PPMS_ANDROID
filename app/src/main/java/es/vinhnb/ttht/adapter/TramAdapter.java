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

public class TramAdapter extends RecyclerView.Adapter<TramAdapter.ViewHolder> {

    private Context context;
    private List<DataTramAdapter> listData = new ArrayList<>();

    public TramAdapter(Context context, List<DataTramAdapter> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_tram, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataTramAdapter data = listData.get(position);
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.congsuat.setText(data.maDviQly);
        holder.tenTram.setText(data.tenTram);
        holder.maTram.setText(data.maTram);
        holder.maDviQly.setText(data.maDviQly);
        holder.dinhDanh.setText(data.dinhDanh);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataTramAdapter> listData ){
        listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvstt;
        public TextView tenTram;
        public TextView maTram;
        public TextView maDviQly;
        public TextView dinhDanh;
        public TextView congsuat;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_tram_stt);
            congsuat = itemView.findViewById(R.id.tv_tram_csuat_tram);
            tenTram = itemView.findViewById(R.id.tv_tram_ten);
            maTram = itemView.findViewById(R.id.tv_tram_matram);
            maDviQly = itemView.findViewById(R.id.tv_tram_dviQly);
            dinhDanh = itemView.findViewById(R.id.tv_tram_dinh_danh);
        }
    }

    public static class DataTramAdapter {
        private String tenTram;
        private String maTram;
        private String maDviQly;
        private String dinhDanh;
        private String congsuat;

        public String getMaDviQly() {
            return maDviQly;
        }

        public void setMaDviQly(String maDviQly) {
            this.maDviQly = maDviQly;
        }

        public String getCongsuat() {
            return congsuat;
        }

        public void setCongsuat(String congsuat) {
            this.congsuat = congsuat;
        }

        public String getTenTram() {
            return tenTram;
        }

        public void setTenTram(String tenTram) {
            this.tenTram = tenTram;
        }

        public String getMaTram() {
            return maTram;
        }

        public void setMaTram(String maTram) {
            this.maTram = maTram;
        }

        public String getDinhDanh() {
            return dinhDanh;
        }

        public void setDinhDanh(String dinhDanh) {
            this.dinhDanh = dinhDanh;
        }
    }
}
