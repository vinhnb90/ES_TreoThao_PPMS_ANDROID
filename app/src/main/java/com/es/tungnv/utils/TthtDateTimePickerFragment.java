package com.es.tungnv.utils;


/**
 * Created by VinhNB on 10/27/2016.
 */


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.es.tungnv.views.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TthtDateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static int sChooseButton = 0;

    private EditText etNgayBatDau;

    public TthtDateTimePickerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sChooseButton = getArguments().getInt("CHOOSE_BUTTON");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


        if (sChooseButton == 1) {
            TextView tv = (TextView) getActivity().findViewById(R.id.et_ngay);
            tv.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            TthtCommon.setTthtDateChon(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    }



    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        Calendar c = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(c.getTime());

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = monthFormat.format(c.getTime());

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(c.getTime());
        int monthReal = Integer.parseInt(month) - 1;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, Integer.parseInt(year), monthReal, Integer.parseInt(date));
    }

    @Override
    public void onDestroy() {
        sChooseButton = 0;
        super.onDestroy();

    }
}
