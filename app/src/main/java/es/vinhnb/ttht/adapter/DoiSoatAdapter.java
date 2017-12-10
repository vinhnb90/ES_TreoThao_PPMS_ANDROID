package es.vinhnb.ttht.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import es.vinhnb.ttht.common.Common;
import es.vinhnb.ttht.view.TthtHnBaseActivity;
import es.vinhnb.ttht.view.TthtHnChiTietCtoFragment;

import static es.vinhnb.ttht.view.TthtHnChiTietCtoFragment.showChiso;

/**
 * Created by VinhNB on 11/22/2017.
 */

public class DoiSoatAdapter extends RecyclerView.Adapter<DoiSoatAdapter.ViewHolder> {

    public static Drawable xml_tththn_rectangle11_type1;
    public static Drawable xml_tththn_rectangle11;
    public static int tththn_button;
    public static int text_white;
    public static Drawable ic_tththn_mark;
    public static Drawable ic_tththn_unmark;

    private Context context;
    private List<DataDoiSoatAdapter> listData = new ArrayList<>();
    private OnIDataDoiSoatAdapter iIteractor;

    public DoiSoatAdapter(Context context, List<DataDoiSoatAdapter> listData
            , OnIDataDoiSoatAdapter iIteractor
    ) {
        this.context = context;
        this.listData.clear();
        this.listData = Common.cloneList(listData);
        this.iIteractor = iIteractor;

        if (xml_tththn_rectangle11_type1 == null)
            xml_tththn_rectangle11_type1 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle11_type1);


        if (xml_tththn_rectangle11 == null)
            xml_tththn_rectangle11 = ContextCompat.getDrawable(context, R.drawable.xml_tththn_rectangle11);


        if (tththn_button == 0)
            tththn_button = ContextCompat.getColor(context, R.color.tththn_button);

        if (text_white == 0)
            text_white = ContextCompat.getColor(context, R.color.text_white);

        if (ic_tththn_mark == null)
            ic_tththn_mark = ContextCompat.getDrawable(context, R.drawable.ic_tththn_mark);

        if (ic_tththn_unmark == null)
            ic_tththn_unmark = ContextCompat.getDrawable(context, R.drawable.ic_tththn_unmark);

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

            holder.rlRow.setBackgroundColor(ContextCompat.getColor(context, doiSoatAdapter.TRANG_THAI_DOISOAT.color));
            holder.tvStt.setText(String.valueOf(position + 1));

            holder.tvTenKH.setText(doiSoatAdapter.TEN_KH);
            holder.tvDiaChiKH.setText(doiSoatAdapter.DIA_CHI_HOADON);

            //default set chua doi soat
            holder.btnSelectUpload.setVisibility(View.VISIBLE);
            holder.btnSelectUpload.setBackground(xml_tththn_rectangle11_type1);
            holder.btnSelectUpload.setTextColor(tththn_button);
            holder.btnSelectUpload.setText("CHỌN GỬI");
            holder.btnSelectUpload.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_tththn_unmark, null);
            holder.tvDoiSoatTrangThai.setText(doiSoatAdapter.TRANG_THAI_DOISOAT.content);
            holder.tvDoiSoatTrangThai.setBackgroundColor(ContextCompat.getColor(context, doiSoatAdapter.TRANG_THAI_DOISOAT.color));
            switch (doiSoatAdapter.TRANG_THAI_DOISOAT) {
                case CHUA_DOISOAT:
                    break;
                case GUI_THAT_BAI:
                case DA_DOISOAT:
                    holder.btnSelectUpload.setVisibility(View.VISIBLE);
                    holder.btnSelectUpload.setBackground(xml_tththn_rectangle11);
                    holder.btnSelectUpload.setTextColor(text_white);
                    holder.btnSelectUpload.setText("ĐÃ CHỌN GỬI");


                    holder.btnSelectUpload.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_tththn_mark, null);
                    break;
                case GUI_THANH_CONG:
                    holder.btnSelectUpload.setVisibility(View.GONE);
                    break;
            }


            String pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + doiSoatAdapter.TEN_ANH_THAO;
            Bitmap bitmap = Common.getBitmapFromUri(pathAnh);
            if (bitmap != null)
                holder.ivDoiSoatThao.setImageBitmap(bitmap);

            showChiso(holder.viewBOChisoThao, doiSoatAdapter.LOAI_CTO_THAO.code, doiSoatAdapter.CHI_SO_THAO, Common.TRANG_THAI_DU_LIEU.DA_GUI, false);


            pathAnh = Common.getRecordDirectoryFolder(Common.FOLDER_NAME.FOLDER_ANH_CONG_TO.name()) + "/" + doiSoatAdapter.TEN_ANH_TREO;
            bitmap = Common.getBitmapFromUri(pathAnh);
            if (bitmap != null)
                holder.ivDoiSoatTreo.setImageBitmap(bitmap);

            showChiso(holder.viewBOChisoTreo, doiSoatAdapter.LOAI_CTO_TREO.code, doiSoatAdapter.CHI_SO_TREO, Common.TRANG_THAI_DU_LIEU.DA_GUI, false);

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


        public TextView tvDoiSoatTrangThai;
        public Button btnSelectUpload;
        public RelativeLayout rlRow;
        public TextView tvStt;

        public TextView tvTenKH;
        public TextView tvDiaChiKH;

        public ImageView ivDoiSoatThao;
        TthtHnChiTietCtoFragment.ViewBO_CHISO viewBOChisoThao;

        public ImageView ivDoiSoatTreo;
        TthtHnChiTietCtoFragment.ViewBO_CHISO viewBOChisoTreo;

        public TextView tvCS1;
        public TextView tvCS2;
        public TextView tvCS3;
        public TextView tvCS4;
        public TextView tvCS5;

        public EditText etCS1;
        public EditText etCS2;
        public EditText etCS3;
        public EditText etCS4;
        public EditText etCS5;

        public ViewHolder(View itemView) {
            super(itemView);

            rlRow = (RelativeLayout) itemView.findViewById(R.id.rl_left);
            tvStt = (TextView) itemView.findViewById(R.id.tv_stt_upload);
            tvTenKH = (TextView) itemView.findViewById(R.id.tv_dl_tenKH);
            tvDiaChiKH = (TextView) itemView.findViewById(R.id.tv_diachi_tenKH);


            ivDoiSoatThao = (ImageView) itemView.findViewById(R.id.iv_doisoat_cto_thao);

            tvCS1 = (TextView) itemView.findViewById(R.id.tv_41a_doisoat_CS1_thao);
            tvCS2 = (TextView) itemView.findViewById(R.id.tv_42a_doisoat_CS2_thao);
            tvCS3 = (TextView) itemView.findViewById(R.id.tv_43a_doisoat_CS3_thao);
            tvCS4 = (TextView) itemView.findViewById(R.id.tv_44a_doisoat_CS4_thao);
            tvCS5 = (TextView) itemView.findViewById(R.id.tv_45a_doisoat_CS5_thao);


            etCS1 = (EditText) itemView.findViewById(R.id.et_41b_doisoat_CS1_thao);
            etCS2 = (EditText) itemView.findViewById(R.id.et_42b_doisoat_CS2_thao);
            etCS3 = (EditText) itemView.findViewById(R.id.et_43b_doisoat_CS3_thao);
            etCS4 = (EditText) itemView.findViewById(R.id.et_44b_doisoat_CS4_thao);
            etCS5 = (EditText) itemView.findViewById(R.id.et_45b_doisoat_CS5_thao);

            viewBOChisoThao = new TthtHnChiTietCtoFragment.ViewBO_CHISO();
            viewBOChisoThao.tvCS1 = tvCS1;
            viewBOChisoThao.tvCS2 = tvCS2;
            viewBOChisoThao.tvCS3 = tvCS3;
            viewBOChisoThao.tvCS4 = tvCS4;
            viewBOChisoThao.tvCS5 = tvCS5;


            viewBOChisoThao.etCS1 = etCS1;
            viewBOChisoThao.etCS2 = etCS2;
            viewBOChisoThao.etCS3 = etCS3;
            viewBOChisoThao.etCS4 = etCS4;
            viewBOChisoThao.etCS5 = etCS5;


            ivDoiSoatTreo = (ImageView) itemView.findViewById(R.id.iv_doisoat_cto_treo);

            viewBOChisoTreo = new TthtHnChiTietCtoFragment.ViewBO_CHISO();
            viewBOChisoTreo.tvCS1 = (TextView) itemView.findViewById(R.id.tv_41a_doisoat_CS1_treo);
            viewBOChisoTreo.tvCS2 = (TextView) itemView.findViewById(R.id.tv_42a_doisoat_CS2_treo);
            viewBOChisoTreo.tvCS3 = (TextView) itemView.findViewById(R.id.tv_43a_doisoat_CS3_treo);
            viewBOChisoTreo.tvCS4 = (TextView) itemView.findViewById(R.id.tv_44a_doisoat_CS4_treo);
            viewBOChisoTreo.tvCS5 = (TextView) itemView.findViewById(R.id.tv_45a_doisoat_CS5_treo);


            viewBOChisoTreo.etCS1 = (EditText) itemView.findViewById(R.id.et_41b_doisoat_CS1_treo);
            viewBOChisoTreo.etCS2 = (EditText) itemView.findViewById(R.id.et_42b_doisoat_CS2_treo);
            viewBOChisoTreo.etCS3 = (EditText) itemView.findViewById(R.id.et_43b_doisoat_CS3_treo);
            viewBOChisoTreo.etCS4 = (EditText) itemView.findViewById(R.id.et_44b_doisoat_CS4_treo);
            viewBOChisoTreo.etCS5 = (EditText) itemView.findViewById(R.id.et_45b_doisoat_CS5_treo);

            tvDoiSoatTrangThai = (TextView) itemView.findViewById(R.id.tv_doisoat_trangthai);
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
                    iIteractor.doClickChonGui(pos, listData.get(pos));

                }
            });
        }
    }

    public interface OnIDataDoiSoatAdapter {
        void doClickChonGui(int pos, DataDoiSoatAdapter dataDoiSoatAdapter);


    }

    public static class DataDoiSoatAdapter implements Cloneable {

        public String TEN_KH;
        public String DIA_CHI_HOADON;

        public String CHI_SO_TREO;
        public Common.LOAI_CTO LOAI_CTO_TREO;

        public String CHI_SO_THAO;
        public Common.LOAI_CTO LOAI_CTO_THAO;

        public String TEN_ANH_TREO;
        public String TEN_ANH_THAO;

        public Common.TRANG_THAI_DOI_SOAT TRANG_THAI_DOISOAT;
        public int ID_BBAN_TRTH;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public static class DataDoiSoat implements Cloneable {

        public String TEN_KH;
        public String DIA_CHI_HOADON;

        public String CHI_SO;
        public Common.LOAI_CTO LOAI_CTO;

        public String TEN_ANH;

        public Common.MA_BDONG MA_BDONG;
        public int ID_BBAN_TRTH;

        public Common.TRANG_THAI_DOI_SOAT TRANG_THAI_DOISOAT;

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }


}
