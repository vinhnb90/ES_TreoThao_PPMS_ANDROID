package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;

import static es.vinhnb.ttht.common.Common.DATE_TIME_TYPE.type6;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class HistoryBBanUploadAdapter extends RecyclerView.Adapter<HistoryBBanUploadAdapter.ViewHolder> {

    private static IOnBBanAdapter iOnBBanAdapter;
    private Context context;
    private static List<DataBBanUploadHistoryAdapter> listData = new ArrayList<>();

    private static Drawable xml_tththn_rectangle3_error, xml_tththn_rectangle3_success;

    public HistoryBBanUploadAdapter(Context context, List<DataBBanUploadHistoryAdapter> listData) {
        this.context = context;
        //clone
        this.listData = Common.cloneList(listData);
        if (xml_tththn_rectangle3_error == null)
            xml_tththn_rectangle3_error = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_error);
        if (xml_tththn_rectangle3_success == null)
            xml_tththn_rectangle3_success = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle3_success);

        if (context instanceof IOnBBanAdapter) {
            iOnBBanAdapter = (IOnBBanAdapter) context;
        } else
            throw new ClassCastException("must be implement IOnBBanAdapter!");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_bban_history_upload, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataBBanUploadHistoryAdapter data = listData.get(position);
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.tvtenKH.setText(data.tenKH);
        holder.tvdiachiKH.setText(data.diachiKH);
        holder.tvmaGCS.setText(data.maGCS);
        holder.tvmaTramCapDien.setText(data.maTramcapdien);
        holder.ngayTreothao.setText(Common.convertDateToDate(data.ngayTrth, Common.DATE_TIME_TYPE.sqlite2, type6));
        holder.tvStatus.setText(Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(data.getTRANG_THAI_DU_LIEU()).content);

        holder.btnMessageResponse.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(data.getMESSAGE_RESPONSE()))
            holder.btnMessageResponse.setVisibility(View.GONE);

        switch (Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(data.getTRANG_THAI_DU_LIEU())) {
            case CHUA_TON_TAI:
            case CHUA_GHI:
            case DA_GHI:
            case GUI_THAT_BAI:
            case DA_TON_TAI_GUI_TRUOC_DO:
            case DA_XAC_NHAN_TREN_CMIS:
            case HET_HIEU_LUC:
                holder.rlRow.setBackground(xml_tththn_rectangle3_error);
                break;

            case DANG_CHO_XAC_NHAN_CMIS:
                holder.rlRow.setBackground(xml_tththn_rectangle3_success);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataBBanUploadHistoryAdapter> listData) {
        this.listData.clear();
        //clone
        this.listData = Common.cloneList(listData);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlRow;
        public TextView tvstt;
        public TextView tvtenKH;
        public TextView tvdiachiKH;
        public TextView tvmaGCS;
        public TextView tvmaTramCapDien;
        public TextView ngayTreothao;

        public Button btnMore;
        public ImageButton btnMessageResponse;
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRow = itemView.findViewById(R.id.rl_row);
            tvstt = itemView.findViewById(R.id.history_upload_tv_bb_stt);
            tvtenKH = itemView.findViewById(R.id.history_upload_tv_bb_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.history_upload_tv_bb_diachi);
            tvmaGCS = itemView.findViewById(R.id.history_upload_tv_bb_magcs);
            tvmaTramCapDien = itemView.findViewById(R.id.history_upload_tv_bb_tramcapdien);
            ngayTreothao = itemView.findViewById(R.id.history_upload_tv_bb_ngaytreothao);
            tvStatus = itemView.findViewById(R.id.history_upload_tv_status_history_upload);


            btnMore = itemView.findViewById(R.id.history_upload_btn_bban_more);
            btnMessageResponse = itemView.findViewById(R.id.history_upload_ibtn_notify);

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iOnBBanAdapter.clickBtnUploadHistoryBBanMore(pos, listData.get(pos));
                }
            });

            btnMessageResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iOnBBanAdapter.clickBtnUploadHistoryBBanMessageResponse(pos, listData.get(pos));
                }
            });
        }
    }

    public static class DataBBanUploadHistoryAdapter {
        private String tenKH;
        private String diachiKH;
        private String maKH;
        private String maHopDong;
        private String maGCS;
        private String maTramcapdien;
        private String lydo;
        private String ngayTrth;
        private String sobban;
        private int ID_BBAN_TRTH;
        private String TYPE_RESPONSE_UPLOAD;
        private String TRANG_THAI_DU_LIEU;
        private String MESSAGE_RESPONSE;

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

        public int getID_BBAN_TRTH() {
            return ID_BBAN_TRTH;
        }

        public void setID_BBAN_TRTH(int ID_BBAN_TRTH) {
            this.ID_BBAN_TRTH = ID_BBAN_TRTH;
        }

        public String getTYPE_RESPONSE_UPLOAD() {
            return TYPE_RESPONSE_UPLOAD;
        }

        public void setTYPE_RESPONSE_UPLOAD(String TYPE_RESPONSE_UPLOAD) {
            this.TYPE_RESPONSE_UPLOAD = TYPE_RESPONSE_UPLOAD;
        }

        public String getMESSAGE_RESPONSE() {
            return MESSAGE_RESPONSE;
        }

        public void setMESSAGE_RESPONSE(String MESSAGE_RESPONSE) {
            this.MESSAGE_RESPONSE = MESSAGE_RESPONSE;
        }

        public String getTRANG_THAI_DU_LIEU() {
            return TRANG_THAI_DU_LIEU;
        }

        public void setTRANG_THAI_DU_LIEU(String TRANG_THAI_DU_LIEU) {
            this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
        }
    }

    public interface IOnBBanAdapter {
        void clickBtnUploadHistoryBBanMore(int pos, DataBBanUploadHistoryAdapter DataBBanUploadHistoryAdapter);

        void clickBtnUploadHistoryBBanMessageResponse(int pos, DataBBanUploadHistoryAdapter DataBBanUploadHistoryAdapter);
    }
}
