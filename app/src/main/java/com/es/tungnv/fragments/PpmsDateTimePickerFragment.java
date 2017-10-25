package com.es.tungnv.fragments;


/**
 * Created by VinhNB on 10/27/2016.
 */


import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
public class PpmsDateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextView etNgay;
    boolean isChooseDate = false;
    public PpmsDateTimePickerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int idTextView = this.getArguments().getInt("TEXTVIEW_DATE");
        etNgay = (TextView) getActivity().findViewById(idTextView);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String d = (dayOfMonth<10)?"0" +dayOfMonth: ""+ dayOfMonth;
        String m = (monthOfYear+1<10)?"0" +String.valueOf(monthOfYear+1): ""+ String.valueOf(monthOfYear+1);
        etNgay.setText(d + "/" + m + "/" + year);
        isChooseDate = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(!isChooseDate)
            etNgay.setText("");
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
        super.onDestroy();

    }
}
