package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private static Context context;
    private static List<DataBBanAdapter> listData = new ArrayList<>();

    private static Drawable xml_tththn_rectangle3_error, xml_tththn_rectangle3_success, xml_tththn_rectangle3_normal, xml_tththn_rectangle3_da_ghi;

    public BBanAdapter(Context context, List<DataBBanAdapter> listData) {
        this.context = context;
        //clone
        this.listData = Common.cloneList(listData);

        if (context instanceof IOnBBanAdapter) {
            iOnBBanAdapter = (IOnBBanAdapter) context;
        } else
            throw new ClassCastException("must be implement IOnBBanAdapter!");

        if (xml_tththn_rectangle3_error == null)
            xml_tththn_rectangle3_error = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_error);
        if (xml_tththn_rectangle3_success == null)
            xml_tththn_rectangle3_success = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_success);
        if (xml_tththn_rectangle3_normal == null)
            xml_tththn_rectangle3_normal = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_normal);
        if (xml_tththn_rectangle3_da_ghi == null)
            xml_tththn_rectangle3_da_ghi = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_da_ghi);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tththn_bban, null);
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


        holder.tvStatus.setText(data.getTRANG_THAI_DU_LIEU().content);

        holder.ibtnError.setVisibility(View.GONE);
        switch (data.getTRANG_THAI_DU_LIEU()) {
            case CHUA_TON_TAI:
            case CHUA_GHI:
                holder.rlRow.setBackground(xml_tththn_rectangle3_normal);
                break;

            case DA_GHI:
            case GUI_THAT_BAI:
                holder.rlRow.setBackground(xml_tththn_rectangle3_da_ghi);
                break;

            case DA_TON_TAI_GUI_TRUOC_DO:
            case HET_HIEU_LUC:
                holder.rlRow.setBackground(xml_tththn_rectangle3_error);
                break;

            case DANG_CHO_XAC_NHAN_CMIS:
            case DA_XAC_NHAN_TREN_CMIS:
                holder.rlRow.setBackground(xml_tththn_rectangle3_success);
                break;
            case LOI_BAT_NGO:
                holder.rlRow.setBackground(xml_tththn_rectangle3_error);
                holder.ibtnError.setVisibility(View.VISIBLE);
                break;
            default:
                holder.rlRow.setBackground(xml_tththn_rectangle3_normal);

        }
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
        public ImageButton ibtnError;
        public TextView tvStatus;
        public RelativeLayout rlRow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_bb_stt);
            tvtenKH = itemView.findViewById(R.id.tv_bb_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.tv_bb_diachi);
            tvmaGCS = itemView.findViewById(R.id.tv_bb_magcs);
            tvmaTramCapDien = itemView.findViewById(R.id.tv_bb_tramcapdien);
            ngayTreothao = itemView.findViewById(R.id.tv_bb_ngaytreothao);
            tvStatus = itemView.findViewById(R.id.tv_status_bb);
            ibtnError = itemView.findViewById(R.id.ibtn_error_bban);

            rlRow = itemView.findViewById(R.id.rl_row_row);
            btnMore = itemView.findViewById(R.id.btn_bban_more);

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iOnBBanAdapter.clickBtnBBanMore(pos, listData.get(pos));
                }
            });

            ibtnError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iOnBBanAdapter.clickBtnBBanEror(pos, listData.get(pos));
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
        private Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU;
        private String NOI_DUNG_LOI_DONG_BO;

        public String getSobban() {
            return sobban;
        }

        public void setSobban(String sobban) {
            this.sobban = sobban;
        }

        public Common.TRANG_THAI_DU_LIEU getTRANG_THAI_DU_LIEU() {
            return TRANG_THAI_DU_LIEU;
        }

        public void setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU) {
            this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
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

        public String getNOI_DUNG_LOI_DONG_BO() {
            return NOI_DUNG_LOI_DONG_BO;
        }

        public void setNOI_DUNG_LOI_DONG_BO(String NOI_DUNG_LOI_DONG_BO) {
            this.NOI_DUNG_LOI_DONG_BO = NOI_DUNG_LOI_DONG_BO;
        }
    }

    public interface IOnBBanAdapter {
        void clickBtnBBanMore(int pos, DataBBanAdapter dataBBanAdapter);

        void clickBtnBBanEror(int pos, DataBBanAdapter dataBBanAdapter);
    }
}
