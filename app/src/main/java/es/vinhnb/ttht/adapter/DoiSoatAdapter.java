package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.view.TthtHnBaseActivity;
import es.vinhnb.ttht.view.TthtHnChiTietCtoFragment;

import static es.vinhnb.ttht.common.Common.LOAI_CTO.D1;
import static es.vinhnb.ttht.common.Common.LOAI_CTO.DT;
import static es.vinhnb.ttht.common.Common.LOAI_CTO.HC;
import static es.vinhnb.ttht.common.Common.LOAI_CTO.VC;
import static es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.showChiso;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class DoiSoatAdapter extends RecyclerView.Adapter<DoiSoatAdapter.ViewHolder> {

    private Context context;
    private List<DataDoiSoatAdapter> listData = new ArrayList<>();
    private OnIDataDoiSoatAdapter iIteractor;

    public DoiSoatAdapter(Context context, List<DataDoiSoatAdapter> listData
//            , OnIDataDoiSoatAdapter iIteractor
    ) {
        this.context = context;
        this.listData.clear();
        this.listData = Common.cloneList(listData);
//        this.iIteractor = iIteractor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_tththn_upload, null);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            DataDoiSoatAdapter doiSoatAdapter = listData.get(position);

            holder.rlRow.setBackgroundColor(doiSoatAdapter.trangThaiDoiSoat.color);
            holder.tvStt.setText(String.valueOf(position + 1));

            holder.tvTenKH.setText(doiSoatAdapter.TEN_KH);
            holder.tvDiaChiKH.setText(doiSoatAdapter.DIA_CHI_HOADON);


            String pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + doiSoatAdapter.TEN_ANH_THAO;
            Bitmap bitmap = Common.getBitmapFromUri(pathAnh);
            if (bitmap != null)
                holder.ivDoiSoatThao.setImageBitmap(bitmap);

            showChiso(holder.viewBOChisoThao, doiSoatAdapter.LOAI_CTO_THAO.code, doiSoatAdapter.CHI_SO_THAO, Common.TRANG_THAI_DU_LIEU.DA_GUI);


            pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + doiSoatAdapter.TEN_ANH_TREO;
            bitmap = Common.getBitmapFromUri(pathAnh);
            if (bitmap != null)
                holder.ivDoiSoatTreo.setImageBitmap(bitmap);

            showChiso(holder.viewBOChisoTreo, doiSoatAdapter.LOAI_CTO_TREO.code, doiSoatAdapter.CHI_SO_TREO, Common.TRANG_THAI_DU_LIEU.DA_GUI);

        } catch (Exception e) {
            e.printStackTrace();
            ((TthtHnBaseActivity) context).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
        }

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


    public void refresh(List<DataDoiSoatAdapter> listData) {
        this.listData.clear();
        this.listData = Common.cloneList(listData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btnDoiSoatChitiet;
        public Button btnSelectUpload;
        public RelativeLayout rlRow;
        public TextView tvStt;

        public TextView tvTenKH;
        public TextView tvDiaChiKH;

        public ImageView ivDoiSoatThao;
        TthtHnChiTietCtoFragment.ViewBO_CHISO viewBOChisoThao;

        public ImageView ivDoiSoatTreo;
        TthtHnChiTietCtoFragment.ViewBO_CHISO viewBOChisoTreo;


        public ViewHolder(View itemView) {
            super(itemView);

            rlRow = (RelativeLayout) itemView.findViewById(R.id.rl_left);
            tvStt = (TextView) itemView.findViewById(R.id.tv_stt_upload);
            tvTenKH = (TextView) itemView.findViewById(R.id.tv_dl_tenKH);
            tvDiaChiKH = (TextView) itemView.findViewById(R.id.tv_diachi_tenKH);


            ivDoiSoatThao = (ImageView) itemView.findViewById(R.id.iv_doisoat_cto_treo);

            viewBOChisoThao.tvCS1 = (TextView) itemView.findViewById(R.id.tv_41a_doisoat_CS1_thao);
            viewBOChisoThao.tvCS2 = (TextView) itemView.findViewById(R.id.tv_42a_doisoat_CS2_thao);
            viewBOChisoThao.tvCS3 = (TextView) itemView.findViewById(R.id.tv_43a_doisoat_CS3_thao);
            viewBOChisoThao.tvCS4 = (TextView) itemView.findViewById(R.id.tv_44a_doisoat_CS4_thao);
            viewBOChisoThao.tvCS5 = (TextView) itemView.findViewById(R.id.tv_45a_doisoat_CS5_thao);


            viewBOChisoThao.etCS1 = (EditText) itemView.findViewById(R.id.et_41b_doisoat_CS1_thao);
            viewBOChisoThao.etCS2 = (EditText) itemView.findViewById(R.id.et_42b_doisoat_CS2_thao);
            viewBOChisoThao.etCS3 = (EditText) itemView.findViewById(R.id.et_43b_doisoat_CS3_thao);
            viewBOChisoThao.etCS4 = (EditText) itemView.findViewById(R.id.et_44b_doisoat_CS4_thao);
            viewBOChisoThao.etCS5 = (EditText) itemView.findViewById(R.id.et_45b_doisoat_CS5_thao);


            ivDoiSoatTreo = (ImageView) itemView.findViewById(R.id.iv_doisoat_cto_treo);

            viewBOChisoThao.tvCS1 = (TextView) itemView.findViewById(R.id.tv_41a_doisoat_CS1_treo);
            viewBOChisoThao.tvCS2 = (TextView) itemView.findViewById(R.id.tv_42a_doisoat_CS2_treo);
            viewBOChisoThao.tvCS3 = (TextView) itemView.findViewById(R.id.tv_43a_doisoat_CS3_treo);
            viewBOChisoThao.tvCS4 = (TextView) itemView.findViewById(R.id.tv_44a_doisoat_CS4_treo);
            viewBOChisoThao.tvCS5 = (TextView) itemView.findViewById(R.id.tv_45a_doisoat_CS5_treo);


            viewBOChisoThao.etCS1 = (EditText) itemView.findViewById(R.id.et_41b_doisoat_CS1_treo);
            viewBOChisoThao.etCS2 = (EditText) itemView.findViewById(R.id.et_42b_doisoat_CS2_treo);
            viewBOChisoThao.etCS3 = (EditText) itemView.findViewById(R.id.et_43b_doisoat_CS3_treo);
            viewBOChisoThao.etCS4 = (EditText) itemView.findViewById(R.id.et_44b_doisoat_CS4_treo);
            viewBOChisoThao.etCS5 = (EditText) itemView.findViewById(R.id.et_45b_doisoat_CS5_treo);

            btnDoiSoatChitiet = (Button) itemView.findViewById(R.id.btn_doisoat_chitiet);
            btnSelectUpload = (Button) itemView.findViewById(R.id.btn_doisoat_chon);


            ivDoiSoatTreo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //get bitmap
                        Bitmap bitmap = (ivDoiSoatTreo.getDrawable() == null) ? null : ((BitmapDrawable) ivDoiSoatTreo.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }


                        //zoom
                        Common.zoomImage(context, bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((TthtHnBaseActivity) context).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
                    }
                }
            });


            ivDoiSoatThao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //get bitmap
                        Bitmap bitmap = (ivDoiSoatThao.getDrawable() == null) ? null : ((BitmapDrawable) ivDoiSoatThao.getDrawable()).getBitmap();
                        if (bitmap == null) {
                            return;
                        }


                        //zoom
                        Common.zoomImage(context, bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ((TthtHnBaseActivity) context).showSnackBar(Common.MESSAGE.ex04.getContent(), e.getMessage(), null);
                    }
                }
            });


            btnSelectUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }

    public interface OnIDataDoiSoatAdapter {
        void doClickBtnMessageHistory(int pos, DataDoiSoatAdapter historyAdapter);
    }

    public static class DataDoiSoatAdapter {

        public String TEN_KH;
        public String DIA_CHI_HOADON;

        public String CHI_SO_TREO;
        public Common.LOAI_CTO LOAI_CTO_TREO;

        public String CHI_SO_THAO;
        public Common.LOAI_CTO LOAI_CTO_THAO;

        public String TEN_ANH_TREO;
        public String TEN_ANH_THAO;

        public Common.TRANG_THAI_DOI_SOAT trangThaiDoiSoat;

    }
}
