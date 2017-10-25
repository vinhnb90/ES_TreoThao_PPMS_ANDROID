package com.es.tungnv.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestScreenCongToActivity extends AppCompatActivity {
    boolean isClickNhiThu, isClickNiemPhong, isClickCongTo, isClickNhap, isClickThem;
    LinearLayout llNhiThu, llNiemPhong, llCongTo, llNhap, llThem;
    Button bt2_1, bt2_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cong_to);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        llNhiThu = (LinearLayout) findViewById(R.id.ll_nhiThu2);
        llNiemPhong = (LinearLayout) findViewById(R.id.ll_niemPhong2);
        llCongTo = (LinearLayout) findViewById(R.id.ll_congTo2);
        llNhap = (LinearLayout) findViewById(R.id.ll_nhap2);
        llThem = (LinearLayout) findViewById(R.id.ll_them2);
        bt2_1 = (Button) findViewById(R.id.bt_2_1);
        bt2_2 = (Button) findViewById(R.id.bt_2_2);
    }

    public void onClickChupAnhNhiThu2(View view) {
        if (!isClickNhiThu) {
            llNhiThu.setVisibility(View.GONE);
        } else {
            llNhiThu.setVisibility(View.VISIBLE);
        }
        isClickNhiThu = !isClickNhiThu;
    }

    public void onClickChupAnhNiemPhong2(View view) {
        if (!isClickNiemPhong) {
            llNiemPhong.setVisibility(View.GONE);
        } else {
            llNiemPhong.setVisibility(View.VISIBLE);
        }
        isClickNiemPhong = !isClickNiemPhong;
    }

    public void onClickChupAnhCongTo2(View view) {
        if (!isClickCongTo) {
            llCongTo.setVisibility(View.GONE);
        } else {
            llCongTo.setVisibility(View.VISIBLE);
        }
        isClickCongTo = !isClickCongTo;
    }

    public void onClickNhap2(View view) {
        if (!isClickNhap) {
            llNhap.setVisibility(View.GONE);
        } else {
            llNhap.setVisibility(View.VISIBLE);
        }
        isClickNhap = !isClickNhap;
    }

    public void onClickThem2(View view) {
        if (!isClickThem) {
            llThem.setVisibility(View.GONE);
        } else {
            llThem.setVisibility(View.VISIBLE);
        }
        isClickThem = !isClickThem;
    }

    public void click2_1(View view) {

    }

    public void click2_2(View view) {
        Intent intent = new Intent(TestScreenCongToActivity.this, TestScreenTUTIActivity.class);
        startActivity(intent);
    }


}
