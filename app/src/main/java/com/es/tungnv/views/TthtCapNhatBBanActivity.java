package com.es.tungnv.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.fragments.TthtTimePickerFragment;
import com.es.tungnv.utils.TthtCommon;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TthtCapNhatBBanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Button btCapNhat;
    private EditText etNgayChon;
    private DialogFragment newFragmentStart;

    private static TthtSQLiteConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttht_cap_nhat_bban);
        btCapNhat = (Button) findViewById(R.id.bt_capnhat);
        etNgayChon = (EditText) findViewById(R.id.et_ngay);

        if(TthtCommon.getTthtDateChon().equals("")) {
            //TODO get tvDate now and set tvDate
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            String dateNow = sdf.format(cal.getTime());
            etNgayChon.setText(dateNow);
            TthtCommon.setTthtDateChon(dateNow);
        }else {
            etNgayChon.setText(TthtCommon.getTthtDateChon());
        }

        connection = TthtSQLiteConnection.getInstance(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        String dateSelected = TthtCommon.getTthtDateChon();
        etNgayChon.setText(dateSelected);
//        String maTramSelected = TthtCommon.getMaTramSelected();

/*        int positionMaTramListSelected = 0;
        for(int i = 0 ; i< maTramList.size(); i++){
            if(maTramSelected.equals(maTramList.get(i))){
                positionMaTramListSelected = i;
            }
        }
        if(!dateSelected.equals(""))
        {
            etNgayChon.setText(dateSelected);
            spinChonTram.setSelection(positionMaTramListSelected);
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        TthtCommon.setMaTramSelected("");
    }

    public void onClickTextViewDate(View view) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("CHOOSE_BUTTON", 1);

        /*PpmsDateTimePickerFragment newFragmentStart = new PpmsDateTimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TEXTVIEW_DATE", R.id.et_ngay_batdau);
        newFragmentStart.setArguments(bundle);
        newFragmentStart.show(getSupportFragmentManager(), "FRAG_PICK_DATE");
*/

        TthtTimePickerFragment pd = new TthtTimePickerFragment();
        pd.setListener(this);
        pd.show(getSupportFragmentManager(), "DateMonthYearPickerFragment");

        /*newFragmentStart = new TthtDateTimePickerFragment();
        newFragmentStart.setArguments(bundle);
        newFragmentStart.show(getSupportFragmentManager(), "Date Picker Start");*/

    }


    public void onClickCapNhat(View view) {
        //TODO check data
        TthtCommon.setTthtDateChon(etNgayChon.getText().toString());
        finish();
//        String NGAY_TRTH = TthtCommon.reConvertDateTime(TthtCommon.getTthtDateChon(), 1);
//        int countBBanWithDate = connection.countBBanWithDateSelectedFULLMA_TRAM(NGAY_TRTH, TthtCommon.getMaNvien(), TthtCommon.getMaDviqly());
//
//        if (countBBanWithDate == 0) {
//            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(TthtCapNhatBBanActivity.this);
//            builder.setMessage("Không có dữ liệu của ngày:\n "
//                    + TthtCommon.getTthtDateChon() + "\n\tBạn có muốn tải dữ liệu mới? ");
//            builder.setPositiveButton("Không", new Dialog.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.setNegativeButton("Có", new Dialog.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    TthtCommon.isDownload = true;
//                    finish();
//                }
//            });
//            builder.show();
//        }else {
//            TthtCommon.setMaTramSelected("");
//            finish();
//        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
        //TODO formatedMonth start from index 0
        StringBuilder time = new StringBuilder();
        if(formatedDate <10){
            time.append("0" + formatedDate);
        }else time.append(formatedDate);
        time.append("/");

        if(formatedMonth <10){
            time.append("0" + formatedMonth);
        }else time.append(formatedMonth);
        time.append("/");

        time.append(formatedYeah);

        etNgayChon.setText(time.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
