package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;

import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.type6;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class BBanAdapter extends RecyclerView.Adapter<BBanAdapter.ViewHolder> {

    private static IOnBBanAdapter iOnBBanAdapter;
    private Context context;
    private static List<DataBBanAdapter> listData = new ArrayList<>();

    public BBanAdapter(Context context, List<DataBBanAdapter> listData) {
        this.context = context;
        //clone
        this.listData = Common.cloneList(listData);

        if (context instanceof IOnBBanAdapter) {
            iOnBBanAdapter = (IOnBBanAdapter) context;
        } else
            throw new ClassCastException("must be implement IOnBBanAdapter!");
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
        holder.tvtenKH.setText(data.tenKH);
        holder.tvdiachiKH.setText(data.diachiKH);
        holder.tvmaGCS.setText(data.maGCS);
        holder.tvmaTramCapDien.setText(data.maTramcapdien);
        holder.ngayTreothao.setText(Common.convertDateToDate(data.ngayTrth, Common.DATE_TIME_TYPE.sqlite2, type6));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataBBanAdapter> listData) {
        this.listData.clear();
        //clone
        this.listData = Common.cloneList(listData);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvstt;
        public TextView tvtenKH;
        public TextView tvdiachiKH;
        public TextView tvmaGCS;
        public TextView tvmaTramCapDien;
        public TextView ngayTreothao;
        public Button btnMore;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_bb_stt);
            tvtenKH = itemView.findViewById(R.id.tv_bb_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.tv_bb_diachi);
            tvmaGCS = itemView.findViewById(R.id.tv_bb_magcs);
            tvmaTramCapDien = itemView.findViewById(R.id.tv_bb_tramcapdien);
            ngayTreothao = itemView.findViewById(R.id.tv_bb_ngaytreothao);

            btnMore = itemView.findViewById(R.id.btn_bban_more);

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iOnBBanAdapter.clickBtnBBanMore(pos, listData.get(pos));
                }
            });
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
        private String ngayTrth;
        private String sobban;

        public String getSobban() {
            return sobban;
        }

        public void setSobban(String sobban) {
            this.sobban = sobban;
        }

        public String getNgayTrth() {
            return ngayTrth;
        }

        public void setNgayTrth(String ngayTrth) {
            this.ngayTrth = ngayTrth;
        }

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

    public interface IOnBBanAdapter {
        void clickBtnBBanMore(int pos, DataBBanAdapter dataBBanAdapter);
    }
}
