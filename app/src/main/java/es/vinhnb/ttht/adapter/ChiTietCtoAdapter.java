package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.common.Common.TRANG_THAI_DU_LIEU;
import es.vinhnb.ttht.view.IInteractionDataCommon;

import static es.vinhnb.ttht.common.Common.MA_BDONG.B;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class ChiTietCtoAdapter extends RecyclerView.Adapter<ChiTietCtoAdapter.ViewHolder> {

    private static OnIChiTietCtoAdapter onIChiTietCtoAdapter;
    private static IInteractionDataCommon OnIDataCommom;
    private Context context;
    private static List<DataChiTietCtoAdapter> listData = new ArrayList<>();
    private static int posClick;

    private static Drawable xml_tththn_rectangle3_error, xml_tththn_rectangle3_success, xml_tththn_rectangle3_da_ghi, xml_tththn_rectangle3_normal;

    public ChiTietCtoAdapter(Context context, List<DataChiTietCtoAdapter> listData) {
        this.context = context;
        //clone
        this.listData.clear();
        this.listData = Common.cloneList(listData);


        if (context instanceof OnIChiTietCtoAdapter)
            this.onIChiTietCtoAdapter = (OnIChiTietCtoAdapter) context;
        else
            throw new ClassCastException("context must be implemnet OnIChiTietCtoAdapter!");


        if (context instanceof IInteractionDataCommon)
            this.OnIDataCommom = (IInteractionDataCommon) context;
        else
            throw new ClassCastException("context must be implemnet IInteractionDataCommon!");

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
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tththn_chitiet_cto_adapter, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataChiTietCtoAdapter data = listData.get(position);
        holder.tvstt.setText(String.valueOf(position + 1));
        holder.tvmaCto.setText(data.maCto);
        holder.tvsoCto.setText(data.soCto);
        holder.tvtenKH.setText(data.tenKH);
        holder.tvdiachiKH.setText(data.diachiKH);
        holder.tvmaGCS.setText(data.maGCS);
        holder.tvmaTram.setText(data.maTram);
        holder.tvchiso.setText(data.chiso);
        holder.tvNgayTrth.setText(Common.convertDateToDate(data.ngaytrth, Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type6));


        holder.tvStatus.setText(data.getTRANG_THAI_DU_LIEU().content);

        holder.rlRow.setBackground(xml_tththn_rectangle3_normal);

        switch (data.TRANG_THAI_DU_LIEU) {
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
            default:
                holder.rlRow.setBackground(xml_tththn_rectangle3_normal);

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataChiTietCtoAdapter> listData) {
        this.listData.clear();
        this.listData = Common.cloneList(listData);
        notifyDataSetChanged();
    }

    public int refreshNextPos(int posOld) {
        if (posOld < 0 || posOld >= listData.size())
            return posOld;

        //set data
        this.posClick = posOld + 1;
        OnIDataCommom.setID_BBAN_TRTH(listData.get(this.posClick).getIdbbantrth());


        notifyDataSetChanged();
        return this.posClick;
    }

    public int refreshPrePos(int posOld) {
        if (posOld <= 0 || posOld > listData.size())
            return posOld;

        //set data
        this.posClick = posOld - 1;
        OnIDataCommom.setID_BBAN_TRTH(listData.get(this.posClick).getIdbbantrth());


        notifyDataSetChanged();
        return this.posClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNgayTrth;
        public TextView tvstt;
        public TextView tvmaCto;
        public TextView tvsoCto;
        public TextView tvtenKH;
        public TextView tvdiachiKH;
        public TextView tvmaGCS;
        public TextView tvmaTram;
        public TextView tvchiso;

        public TextView tvStatus;
        public RelativeLayout rlRow;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_stt);
            tvmaCto = itemView.findViewById(R.id.tv_ma_cto);
            tvsoCto = itemView.findViewById(R.id.tv_so_no_cto);
            tvtenKH = itemView.findViewById(R.id.tv_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.tv_diachi);
            tvmaGCS = itemView.findViewById(R.id.tv_magcs);
            tvmaTram = itemView.findViewById(R.id.tv_matram);
            tvNgayTrth = itemView.findViewById(R.id.tv_ngaytreothao);
            tvchiso = itemView.findViewById(R.id.tv_chiso);
            rlRow = itemView.findViewById(R.id.rl_rl_rl);
            tvStatus = itemView.findViewById(R.id.tv_status_cto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    DataChiTietCtoAdapter dataChiTietCtoAdapter = listData.get(pos);
                    OnIDataCommom.setID_BBAN_TRTH(dataChiTietCtoAdapter.getIdbbantrth());
                    if (OnIDataCommom.getMA_BDONG() == B)
                        OnIDataCommom.setID_BBAN_TUTI_CTO_TREO(dataChiTietCtoAdapter.getIdbbantuti());
                    else
                        OnIDataCommom.setID_BBAN_TUTI_CTO_THAO(dataChiTietCtoAdapter.getIdbbantuti());


                    //get next and pre
//                    int nextID_BBAN_TRTH = 0;
//                    int preID_BBAN_TRTH = 0;
//                    if (pos < listData.size() - 1)
//                        nextID_BBAN_TRTH = listData.get(pos + 1).getIdbbantrth();
//                    if (pos > 0)
//                        preID_BBAN_TRTH = listData.get(pos - 1).getIdbbantrth();


                    posClick = pos;
                    onIChiTietCtoAdapter.clickRowChiTietCtoAdapter(pos, listData.size());
                }
            });
        }
    }

    public interface OnIChiTietCtoAdapter {
        void clickRowChiTietCtoAdapter(int pos, int sizeFitle);
    }

    public static class DataChiTietCtoAdapter {
        private String maCto;
        private String soCto;
        private String tenKH;
        private String diachiKH;
        private String maGCS;
        private String maTram;
        private String chiso;
        private int idbbantrth;
        private int idbbantuti;
        private String ngaytrth;
        private String sobban;

        private Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU;
        private Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU_TUTI;


        public String getSobban() {
            return sobban;
        }

        public void setSobban(String sobban) {
            this.sobban = sobban;
        }

        public String getNgaytrth() {
            return ngaytrth;
        }

        public void setNgaytrth(String ngaytrth) {
            this.ngaytrth = ngaytrth;
        }

        public int getIdbbantuti() {
            return idbbantuti;
        }

        public void setIdbbantuti(int idbbantuti) {
            this.idbbantuti = idbbantuti;
        }

        public int getIdbbantrth() {
            return idbbantrth;
        }

        public void setIdbbantrth(int idbbantrth) {
            this.idbbantrth = idbbantrth;
        }

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


        public TRANG_THAI_DU_LIEU getTRANG_THAI_DU_LIEU() {
            return TRANG_THAI_DU_LIEU;
        }

        public void setTRANG_THAI_DU_LIEU(Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU) {
            this.TRANG_THAI_DU_LIEU = TRANG_THAI_DU_LIEU;
        }

        public void setTRANG_THAI_DU_LIEU_TUTI(Common.TRANG_THAI_DU_LIEU TRANG_THAI_DU_LIEU_TUTI) {
            this.TRANG_THAI_DU_LIEU_TUTI = TRANG_THAI_DU_LIEU_TUTI;
        }

        public Common.TRANG_THAI_DU_LIEU  getTRANG_THAI_DU_LIEU_TUTI() {
            return TRANG_THAI_DU_LIEU_TUTI;
        }
    }
}
