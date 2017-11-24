package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.List;

import es.vinhnb.ttht.common.Common;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<DataHistoryAdapter> listData = new ArrayList<>();
    private OnIDataHistoryAdapter iIteractor;

    public HistoryAdapter(Context context, List<DataHistoryAdapter> listData, OnIDataHistoryAdapter iIteractor) {
        this.context = context;
        this.listData = listData;
        this.iIteractor = iIteractor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_history_type_download, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataHistoryAdapter history = listData.get(position);

        String dateUI = Common.convertDateToDate(String.valueOf(history.date), Common.DATE_TIME_TYPE.sqlite2, Common.DATE_TIME_TYPE.type7);
        holder.tvDate.setText(dateUI);
        holder.tvNotify.setText(String.valueOf(history.notify));
        holder.tvSoBB.setText(String.valueOf(history.soBB));
        holder.tvSoTreo.setText(String.valueOf(history.soTreo));
        holder.tvSoThao.setText(String.valueOf(history.soThao));
        holder.tvSoBBTuTi.setText(String.valueOf(history.soBBTuTi));
        holder.tvSoTu.setText(String.valueOf(history.soTu));
        holder.tvSoTi.setText(String.valueOf(history.soTi));
        holder.tvSoTram.setText(String.valueOf(history.soTram));
        holder.tvSoChungLoai.setText(String.valueOf(history.soChungLoai));

        if (!TextUtils.isEmpty(history.message))
            holder.btnMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataHistoryAdapter> listData) {
        listData.clear();
        this.listData = listData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvNotify;
        public TextView tvSoBB;
        public TextView tvSoTreo;
        public TextView tvSoThao;
        public TextView tvSoBBTuTi;
        public TextView tvSoTu;
        public TextView tvSoTi;
        public TextView tvSoTram;
        public TextView tvSoChungLoai;
        public Button btnMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tv_date_download_history);
            tvNotify = (TextView) itemView.findViewById(R.id.tv_notify_download);
            tvSoBB = (TextView) itemView.findViewById(R.id.tv_so_bb);
            tvSoTreo = (TextView) itemView.findViewById(R.id.tv_so_treo);
            tvSoThao = (TextView) itemView.findViewById(R.id.tv_so_thao);
            tvSoBBTuTi = (TextView) itemView.findViewById(R.id.tv_so_bb_tuti);
            tvSoTu = (TextView) itemView.findViewById(R.id.tv_so_tu);
            tvSoTi = (TextView) itemView.findViewById(R.id.tv_so_ti);
            tvSoTram = (TextView) itemView.findViewById(R.id.tv_so_tram);
            tvSoChungLoai = (TextView) itemView.findViewById(R.id.tv_so_chungloai);
            btnMessage = (Button) itemView.findViewById(R.id.btn_error_download_history);


            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    iIteractor.doClickBtnMessageHistory(pos, listData.get(pos));
                }
            });
        }
    }

    public interface OnIDataHistoryAdapter {
        void doClickBtnMessageHistory(int pos, DataHistoryAdapter historyAdapter);
    }

    public static class DataHistoryAdapter {
        public String date;
        public String notify;
        public int soBB;
        public int soTreo;
        public int soThao;
        public int soBBTuTi;
        public int soTu;
        public int soTi;
        public int soTram;
        public int soChungLoai;
        public String message;
    }
}
