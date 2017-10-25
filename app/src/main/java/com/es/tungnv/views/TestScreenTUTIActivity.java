package com.es.tungnv.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class TestScreenTUTIActivity extends AppCompatActivity {
    boolean isClickSauLapTuTi, isClickLapTuTi, isClickThaoTuTi;
    LinearLayout llThaoTuTI, llLapTuTI, llSauLap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_screen_tu_ti);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        llThaoTuTI = (LinearLayout) findViewById(R.id.ll_thao_tu_ti);
        llLapTuTI = (LinearLayout) findViewById(R.id.ll_lap_tu_ti);
        llSauLap = (LinearLayout) findViewById(R.id.ll_sau_lap_tu_ti);
    }

    public void onClickSauLap_tu_ti(View view) {
        if (!isClickSauLapTuTi) {
            llSauLap.setVisibility(View.GONE);
        } else {
            llSauLap.setVisibility(View.VISIBLE);
        }
        isClickSauLapTuTi = !isClickSauLapTuTi;
    }

    public void onClickThao_TU_TI(View view) {
        if (!isClickLapTuTi) {
            llThaoTuTI.setVisibility(View.GONE);
        } else {
            llThaoTuTI.setVisibility(View.VISIBLE);
        }
        isClickLapTuTi = !isClickLapTuTi;
    }

    public void onClickLap_TU_TI(View view) {
        if (!isClickThaoTuTi) {
            llLapTuTI.setVisibility(View.GONE);
        } else {
            llLapTuTI.setVisibility(View.VISIBLE);
        }
        isClickThaoTuTi = !isClickThaoTuTi;
    }




    public void click1_1(View view) {
        Intent intent = new Intent(TestScreenTUTIActivity.this, TestScreenCongToActivity.class);
        startActivity(intent);
    }

    public void click1_2(View view) {

    }
}
