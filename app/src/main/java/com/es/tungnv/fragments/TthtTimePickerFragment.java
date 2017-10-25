package com.es.tungnv.fragments;


import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.es.tungnv.views.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TthtTimePickerFragment extends DialogFragment {
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 2000;
    private static NumberPicker sDatePicker, sMonthPicker, sYearPicker;
    private static int sDate, sMonth, sYear;
    private OnDateSetListener listener;

    public void setListener(OnDateSetListener listener) {
        this.listener = listener;
    }

    public TthtTimePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ttth_time_picker, container, false);

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.fragment_ttth_time_picker, null);
        sDatePicker = (NumberPicker) dialog.findViewById(R.id.ttht_picker_day);
        sMonthPicker = (NumberPicker) dialog.findViewById(R.id.ttht_picker_month);
        sYearPicker = (NumberPicker) dialog.findViewById(R.id.ttht_picker_year);

        sMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Calendar mycal = new GregorianCalendar(sYear, i1 - 1, 1);
                final int maxdateNew = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDatePicker.post(new Runnable() {
                    @Override
                    public void run() {
                        sDatePicker.setMaxValue(maxdateNew);
                    }
                });
            }
        });

        sYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Calendar mycal = new GregorianCalendar(sYear, i1 - 1, 1);
                final int maxdateNew = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                sDatePicker.post(new Runnable() {
                    @Override
                    public void run() {
                        sDatePicker.setMaxValue(maxdateNew);
                    }
                });
            }
        });

        sDate = cal.get(Calendar.DATE);
        int maxDate = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        sDatePicker.setMinValue(1);
        sDatePicker.setMaxValue(maxDate);
        sDatePicker.setValue(sDate);

        sMonth = cal.get(Calendar.MONTH);
        sMonthPicker.setMinValue(1);
        sMonthPicker.setMaxValue(12);
        sMonthPicker.setValue(sMonth + 1);

        sYear = cal.get(Calendar.YEAR);
        sYearPicker.setMinValue(MIN_YEAR);
        sYearPicker.setMaxValue(MAX_YEAR);
        sYearPicker.setValue(sYear);


        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, sYearPicker.getValue(), sMonthPicker.getValue(), sDatePicker.getValue());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TthtTimePickerFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
