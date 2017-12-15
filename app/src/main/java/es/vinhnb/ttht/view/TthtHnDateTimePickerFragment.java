package es.vinhnb.ttht.view;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.es.tungnv.views.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TthtHnDateTimePickerFragment extends DialogFragment {
    private static final int MAX_YEAR = 2099;
    private static final int MIN_YEAR = 2000;
    private static NumberPicker sDatePicker, sMonthPicker, sYearPicker;
    private Button mBtOk, mBtCancel;
    private static int sDate, sMonth, sYear;
    private OnDateSetListener listener;

    public void setListener(OnDateSetListener listener) {
        this.listener = listener;
    }

    public TthtHnDateTimePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tththn_dialog_datetime_picker, container, false);

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.tththn_dialog_datetime_picker, null);
        sDatePicker = (NumberPicker) dialog.findViewById(R.id.picker_day);
        sMonthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        sYearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

        mBtOk = (Button) dialog.findViewById(R.id.btn_ok);
        mBtCancel = (Button) dialog.findViewById(R.id.btn_cancel);

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

        mBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDateSet(null, sYearPicker.getValue(), sMonthPicker.getValue(), sDatePicker.getValue());
                TthtHnDateTimePickerFragment.this.getDialog().cancel();
            }
        });

        mBtCancel.setVisibility(View.GONE);

//        mBtCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TthtHnDateTimePickerFragment.this.getDialog().cancel();
//            }
//        });


        builder.setView(dialog);
                // Add action buttons
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        listener.onDateSet(null, sYearPicker.getValue(), sMonthPicker.getValue(), sDatePicker.getValue());
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        TthtHnDateTimePickerFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }
}
