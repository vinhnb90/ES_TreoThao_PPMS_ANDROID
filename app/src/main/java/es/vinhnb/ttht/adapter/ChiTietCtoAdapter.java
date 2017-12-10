package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;
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

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_chitiet_cto_adapter, null);
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


        //background select
        holder.mRlMain.setBackgroundColor(Color.WHITE);
        if (position == posClick) {
            holder.mRlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.tththn_background_lv5));
        }

        Common.TRANG_THAI_DU_LIEU TRANG_THAI = Common.TRANG_THAI_DU_LIEU.CHUA_GHI;

        //check bban cong to
        TRANG_THAI = Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(data.getTRANG_THAI_DULIEU());

        //check bb tuti
        //nếu data.getTRANG_THAI_DULIEU_TUTI() == null thì tức công tơ này ko có bb tu ti
        if (data.getIdbbantuti() != 0 && !TextUtils.isEmpty(data.getTRANG_THAI_DULIEU_TUTI())) {
            if (Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(data.getTRANG_THAI_DULIEU_TUTI()) == Common.TRANG_THAI_DU_LIEU.CHUA_GHI)
                TRANG_THAI = Common.TRANG_THAI_DU_LIEU.CHUA_GHI;
            else if (Common.TRANG_THAI_DU_LIEU.findTRANG_THAI_DU_LIEU(data.getTRANG_THAI_DULIEU_TUTI()) == Common.TRANG_THAI_DU_LIEU.DA_GUI)
                TRANG_THAI = Common.TRANG_THAI_DU_LIEU.DA_GUI;
            else
                TRANG_THAI = Common.TRANG_THAI_DU_LIEU.DA_GHI;
        }


        switch (TRANG_THAI) {
            case CHUA_GHI:
                holder.mLLView.setBackgroundColor(ContextCompat.getColor(context, R.color.tththn_background_chua_ghi));
                break;

            case DA_GHI:
                holder.mLLView.setBackgroundColor(ContextCompat.getColor(context, R.color.tththn_background_da_ghi));
                break;

            case DA_GUI:
                holder.mLLView.setBackgroundColor(ContextCompat.getColor(context, R.color.tththn_background_da_gui));
                break;

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
        public TextView tvstt;
        public TextView tvmaCto;
        public TextView tvsoCto;
        public TextView tvtenKH;
        public TextView tvdiachiKH;
        public TextView tvmaGCS;
        public TextView tvmaTram;
        public TextView tvchiso;

        public LinearLayout mLLView;
        public RelativeLayout mRlMain;

        public ViewHolder(View itemView) {
            super(itemView);
            tvstt = itemView.findViewById(R.id.tv_stt);
            tvmaCto = itemView.findViewById(R.id.tv_ma_cto);
            tvsoCto = itemView.findViewById(R.id.tv_so_no_cto);
            tvtenKH = itemView.findViewById(R.id.tv_tenKH);
            tvdiachiKH = itemView.findViewById(R.id.tv_diachi);
            tvmaGCS = itemView.findViewById(R.id.tv_magcs);
            tvmaTram = itemView.findViewById(R.id.tv_matram);
            tvchiso = itemView.findViewById(R.id.tv_chiso);
            mRlMain = itemView.findViewById(R.id.rl_main_row_chitiet_cto);
            mLLView = itemView.findViewById(R.id.ll_main_row_chitiet_cto);

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

        private String TRANG_THAI_DULIEU;
        private String TRANG_THAI_DULIEU_TUTI;


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

        public String getTRANG_THAI_DULIEU() {
            return TRANG_THAI_DULIEU;
        }

        public void setTRANG_THAI_DULIEU(String TRANG_THAI_DULIEU) {
            this.TRANG_THAI_DULIEU = TRANG_THAI_DULIEU;
        }

        public void setTRANG_THAI_DULIEU_TUTI(String TRANG_THAI_DULIEU_TUTI) {
            this.TRANG_THAI_DULIEU_TUTI = TRANG_THAI_DULIEU_TUTI;
        }

        public String getTRANG_THAI_DULIEU_TUTI() {
            return TRANG_THAI_DULIEU_TUTI;
        }
    }
}
