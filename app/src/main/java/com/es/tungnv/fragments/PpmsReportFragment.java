package com.es.tungnv.fragments;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityNhanVien;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.views.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PpmsReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    //TODO Khai báo đối tượng UI
    private LineChart lineChartPhanCongPhucTraTrongThang;
    private TextView tvChooseMonth;


    //TODO Khai báo đối tượng
    private PpmsSqliteConnection connection;
    private static PpmsEntityNhanVien sEmp;
    private static String[] sDateInMonth = null;
    private int monthReport, yearReport;
    private List<ILineDataSet> iLineDataSetPhanCongPhucTraList = new ArrayList<ILineDataSet>();

    public PpmsReportFragment() {
        // Required empty public constructor
    }

    //region region method override
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = PpmsSqliteConnection.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.ppms_fragment_report, container, false);
        tvChooseMonth = (TextView) rootView.findViewById(R.id.ppms_fragment_report_tv_month_year);
        lineChartPhanCongPhucTraTrongThang = (LineChart) rootView.findViewById(R.id.ppms_fragment_report_phucTra_PhanCong_chart);

        //TODO get bundle from activity
        sEmp = getArguments().getParcelable("EMPLOYEE");
        if (sEmp == null) {
            return null;
        }

        //TODO set action textView change on Report Fragment
        tvChooseMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO check fragment is ReportFrag and call set data chart
                if (monthReport == 0 || yearReport == 0)
                    return;
                initDataChart();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return rootView;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int formatedYeah, int formatedMonth, int formatedDate) {
        //TODO formatedMonth start from index 0
        monthReport = formatedMonth;
        yearReport = formatedYeah;

        tvChooseMonth.setText(formatedMonth + "/" + formatedYeah);
    }
    //endregion

    //region method onclick

    public void clickViewInFragment(View view) {
        switch (view.getId()) {
            case R.id.ppms_btn_choose_month_fragment_report:
                PpmsMonthTimePickerFragment pd = new PpmsMonthTimePickerFragment();
                pd.setListener(this);
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
                break;
        }

    }

    //endregion

    //region region method common


    private void initDataChart() {
        //TODO get all date in month
        sDateInMonth = null;
        sDateInMonth = getDateInMonth();

        if (lineChartPhanCongPhucTraTrongThang.getLineData() != null) {
            lineChartPhanCongPhucTraTrongThang.getLineData().clearValues();
            lineChartPhanCongPhucTraTrongThang.notifyDataSetChanged();
            lineChartPhanCongPhucTraTrongThang.invalidate();
        }

        //TODO create data for lineChart ngayPhanCong/ngayPhucTra
        //TODO init var
        int[] countPhanCongInDateArray = new int[sDateInMonth.length], countPhucTraInDateArray = new int[sDateInMonth.length];
        List<Entry> ngayPhanCongEntryList = new ArrayList<Entry>();
        List<Entry> ngayPhucTraEntryList = new ArrayList<Entry>();

        //TODO create data count PhanCong and count PhucTra
        for (int i = 0; i < sDateInMonth.length; i++) {
            StringBuilder stringBuilderDate = new StringBuilder();
            stringBuilderDate.append(String.valueOf(yearReport));
            stringBuilderDate.append("-");
            if (monthReport < 10) {
                stringBuilderDate.append("0" + monthReport);
            } else {
                stringBuilderDate.append(String.valueOf(monthReport));
            }

            stringBuilderDate.append("-");
            if (i < 10) {
                stringBuilderDate.append("0" + String.valueOf(i + 1));
            } else {
                stringBuilderDate.append(String.valueOf(i + 1));
            }

            //TODO convert dateIndex from 1  to 2016:11:01 and query with this Date
            countPhanCongInDateArray[i] = getCountTotalPhanCongPhucTraInDate(stringBuilderDate.toString(), true);
            countPhucTraInDateArray[i] = getCountTotalPhanCongPhucTraInDate(stringBuilderDate.toString(), false);

        }

        //TODO create list entry data
        for (int i = 0; i < sDateInMonth.length; i++) {
            ngayPhanCongEntryList.add(new Entry(i, countPhanCongInDateArray[i]));
            ngayPhucTraEntryList.add(new Entry(i, countPhucTraInDateArray[i]));
        }

        //TODO init property LineData and pass value list entry on it
        LineDataSet phanCongLineDataSet = new LineDataSet(ngayPhanCongEntryList, "SỐ PHÂN CÔNG");
        phanCongLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        phanCongLineDataSet.setColors(ColorTemplate.rgb("#9400D3"));
        phanCongLineDataSet.setLineWidth(3);
        phanCongLineDataSet.setValueFormatter(new MyValueFormatter());
        phanCongLineDataSet.setDrawCircles(true);

        LineDataSet phucTraLineDataSet = new LineDataSet(ngayPhucTraEntryList, "SỐ PHÚC TRA");
        phucTraLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        phucTraLineDataSet.setColor(ColorTemplate.rgb("#f76204"));
        phucTraLineDataSet.setValueFormatter(new MyValueFormatter());
        phucTraLineDataSet.setLineWidth(3);

        // TODO use the interface ILineDataSet add LineData above
        iLineDataSetPhanCongPhucTraList.add(phanCongLineDataSet);
        iLineDataSetPhanCongPhucTraList.add(phucTraLineDataSet);

        //TODO get lable mark to lineChart
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (Math.round(value) <= sDateInMonth.length)
                    return sDateInMonth[(int) value];
                return "Null";
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        //TODO init property LineChart
        XAxis xAxis = lineChartPhanCongPhucTraTrongThang.getXAxis();
        xAxis.setGranularity(1); // indentiy auto increment
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //TODO remove lable
        Description lable = new Description();
        lable.setEnabled(false);
        lineChartPhanCongPhucTraTrongThang.setDescription(lable);

        //TODO remove rightAxis
        lineChartPhanCongPhucTraTrongThang.getAxisRight().setDrawLabels(false);

        //TODO set leftAxis to int value
        YAxis yAxisLeft = lineChartPhanCongPhucTraTrongThang.getAxisLeft();
        yAxisLeft.setValueFormatter(new MyValueFormatterAxist());
        yAxisLeft.setGranularity(1);
        yAxisLeft.setAxisMinValue(0);
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setDrawZeroLine(false);

        //TODO remove gridview line background
        lineChartPhanCongPhucTraTrongThang.getAxisLeft().setDrawGridLines(false);
        lineChartPhanCongPhucTraTrongThang.getXAxis().setDrawGridLines(false);

        //TODO show data on LineChart
        LineData data = new LineData(iLineDataSetPhanCongPhucTraList);
        lineChartPhanCongPhucTraTrongThang.setData(data);
        lineChartPhanCongPhucTraTrongThang.notifyDataSetChanged();
        lineChartPhanCongPhucTraTrongThang.invalidate(); // refresh

    }

    private int getCountTotalPhanCongPhucTraInDate(String date, boolean isCountPhanCong) {
        if (date.equals("")) {
            PpmsCommon.showTitleByDialog(getActivity(), "Lỗi", "getCountTotalPhanCongPhucTraInDate(): " + "date null!", false, 3000);
            return 0;
        }

        String query = "";
        int count = 0;
        if (isCountPhanCong) {
            //TODO get all task with NGAY_PHAN_CONG = date
            query = connection.getsQueryGetCountTaskWithDate(sEmp.getNhanVienId(), date, "NGAY_PHAN_CONG");
        } else {
            query = connection.getsQueryGetCountTaskWithDate(sEmp.getNhanVienId(), date, "NGAY_PHUC_TRA");
        }

        Cursor cursor = connection.runQueryReturnCursor(query);
        if (cursor != null) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    private String[] getDateInMonth() {
        //TODO get count day with GregorianCalendar
        //TODO NOTE in new GregorianCalendar() month index from 0 so month - 1
        Calendar mycal = new GregorianCalendar(yearReport, monthReport - 1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] dates = new String[daysInMonth];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = i + 1 + "/" + monthReport;
        }
        return dates;
    }

    //endregion

    //region region class inner

    public class MyValueFormatterAxist implements IAxisValueFormatter {

        public MyValueFormatterAxist() {
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return Math.round(value) + " Biên bản";
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }

    public class MyValueFormatter implements IValueFormatter {
        public MyValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            return Math.round(value) + "";
        }

    }
    //endregion
}
