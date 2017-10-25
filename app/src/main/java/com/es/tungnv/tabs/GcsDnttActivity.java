package com.es.tungnv.tabs;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by TUNGNV on 6/24/2016.
 */
public class GcsDnttActivity extends Fragment{

    private View mRoot;
    private Button btTinh;
    public EditText etDNDN;
    private RecyclerView rvTram;

    private GcsSqliteConnection connection;
    private RecyclerView.LayoutManager layoutManager;

    public int _pos_Selected = 0;
    private List<LinkedHashMap<String, String>> lstDNTT;
    private GcsAdapterDNTT adapterDNTT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            mRoot = inflater.inflate(R.layout.gcs_activity_dntt, null);
            initComponents(mRoot);
            initRecycleView();
            connection = new GcsSqliteConnection(this.getActivity());
            initData();
            return mRoot;
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo:\n"
                    + ex.toString(), Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    private void initComponents(View mRoot) {
        btTinh = (Button) mRoot.findViewById(R.id.gcs_activity_dntt_btTinh);
        etDNDN = (EditText) mRoot.findViewById(R.id.gcs_activity_dntt_etDNDN);
        rvTram = (RecyclerView) mRoot.findViewById(R.id.gcs_activity_dntt_rvTram);

        etDNDN.requestFocus();
        showKeyboard(etDNDN);

        btTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    etDNDN.setError(null);
                    String DNDN = etDNDN.getText().toString();
                    if (TextUtils.isEmpty(DNDN)) {
                        etDNDN.setError("Bạn chưa nhập điện năng đầu nguồn");
                        etDNDN.requestFocus();
                    } else if (Float.parseFloat(DNDN) < Float.parseFloat(lstDNTT.get(_pos_Selected).get("DNTT"))) {
                        etDNDN.setError("Điện năng đầu nguồn phải lớn hơn điện năng tiêu thụ");
                        etDNDN.requestFocus();
                    } else {
                        lstDNTT.get(_pos_Selected).put("DNDN", DNDN);
                        float DNTTHAT = Float.parseFloat(DNDN) - Float.parseFloat(lstDNTT.get(_pos_Selected).get("DNTT"));
                        lstDNTT.get(_pos_Selected).put("DN_TTHAT", String.valueOf(DNTTHAT));
                        double dien_ton_that_percent = ((double) DNTTHAT / Double.parseDouble(DNDN)) * 100;
                        lstDNTT.get(_pos_Selected).put("PTRAM_TTHAT", String.valueOf(dien_ton_that_percent));
                        adapterDNTT.updateList(lstDNTT);
                    }
                } catch (Exception ex) {
                    Common.showAlertDialogGreen(GcsDnttActivity.this.getActivity(), "Lỗi", Color.RED, "Lỗi tính điện năng tổn thất:\n"
                            + ex.toString(), Color.WHITE, "OK", Color.RED);
                }
            }
        });
    }

    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(this.getActivity());
        rvTram.setHasFixedSize(true);
        rvTram.setLayoutManager(layoutManager);
    }

    private void initData() {
        try{
            int stt = 0;
            lstDNTT = new ArrayList<>();
            Cursor c = connection.getDataTram();
            if(c.moveToFirst()){
                do {
                    stt++;
                    LinkedHashMap<String, String> map = new LinkedHashMap<>();
                    map.put("STT", String.valueOf(stt));
                    String MA_TRAM = c.getString(0);
                    float DNTT = connection.sumDTTByTram(MA_TRAM);
                    int daghi = GcsCommon.getDaGhi(connection.getCSoAndTTRByMaTram(MA_TRAM));
                    int chuaghi = connection.getSoLuongBanGhiByMaTram(MA_TRAM);
                    String DA_GHI = daghi + "/" + chuaghi;
                    String DNDN = "";
                    String DNTTHAT = "";
                    String PTTT = "";
                    map.put("MA_TRAM", MA_TRAM);
                    map.put("DNTT", String.valueOf(DNTT));
                    map.put("DA_GHI", DA_GHI);
                    map.put("DNDN", DNDN);
                    map.put("DN_TTHAT", DNTTHAT);
                    map.put("PTRAM_TTHAT", PTTT);
                    lstDNTT.add(map);
                } while (c.moveToNext());
                adapterDNTT = new GcsAdapterDNTT(lstDNTT);
                rvTram.setAdapter(adapterDNTT);
            }
        } catch (Exception ex) {
            Common.showAlertDialogGreen(this.getActivity(), "Lỗi", Color.RED, "Lỗi khởi tạo dữ liệu:\n"
                    + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void showKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    class GcsAdapterDNTT extends RecyclerView.Adapter<GcsAdapterDNTT.RecyclerViewHolder> {

        List<LinkedHashMap<String, String>> lstDNTT = new ArrayList<>();

        public GcsAdapterDNTT(List<LinkedHashMap<String, String>> lstDNTT){
            this.lstDNTT = lstDNTT;
        }

        public void updateList(List<LinkedHashMap<String, String>> data) {
            lstDNTT = data;
            notifyDataSetChanged();
        }

        public void moveItem(int fromPosition, int toPosition) {
            LinkedHashMap<String, String> model = lstDNTT.remove(fromPosition);
            lstDNTT.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);

        }

        public void addItem(int position, LinkedHashMap<String, String> data) {
            lstDNTT.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            lstDNTT.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.gcs_row_dntt, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            try{
                holder.tvSTT.setText(lstDNTT.get(position).get("STT"));
                holder.tvTram.setText(lstDNTT.get(position).get("MA_TRAM"));
                holder.tvDaGhi.setText(lstDNTT.get(position).get("DA_GHI"));
                holder.tvDNTT.setText(TextUtils.isEmpty(lstDNTT.get(position).get("DNTT"))?"0":Common.formatMoney(lstDNTT.get(position).get("DNTT")));
                holder.tvDNDN.setText(TextUtils.isEmpty(lstDNTT.get(position).get("DNDN"))?"0":Common.formatMoney(lstDNTT.get(position).get("DNDN")));
                holder.tvDNTThat.setText(TextUtils.isEmpty(lstDNTT.get(position).get("DN_TTHAT"))?"0":Common.formatMoney(lstDNTT.get(position).get("DN_TTHAT")));
                holder.tvPhanTramTT.setText(lstDNTT.get(position).get("PTRAM_TTHAT")==null?"0":Common.formatMoney(lstDNTT.get(position).get("PTRAM_TTHAT")) + "%");

                if(_pos_Selected == position){
                    holder.itemView.setBackgroundResource(R.drawable.bg_listitem_green);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.bg_listitem_white);
                }
            } catch(Exception ex) {
                Log.e("Error bind view", ex.toString());
            }
        }

        @Override
        public int getItemCount() {
            return lstDNTT.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {

            public TextView tvSTT, tvTram, tvDaGhi, tvDNTT, tvDNDN, tvDNTThat, tvPhanTramTT;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                tvSTT = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvSTT);
                tvTram = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvTram);
                tvDaGhi = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvDaGhi);
                tvDNTT = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvDNTT);
                tvDNDN = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvDNDN);
                tvDNTThat = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvDNTThat);
                tvPhanTramTT = (TextView) itemView.findViewById(R.id.gcs_row_dntt_tvPhanTramTT);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            _pos_Selected = getPosition();
                            notifyDataSetChanged();
                            etDNDN.setText(lstDNTT.get(_pos_Selected).get("DNDN"));
                            etDNDN.selectAll();
                            etDNDN.requestFocus();
                            showKeyboard(etDNDN);
                        } catch(Exception ex) {
                            ex.toString();
                        }
                    }
                });
            }
        }
    }

}
