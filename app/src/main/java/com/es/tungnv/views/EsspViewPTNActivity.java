package com.es.tungnv.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.draws.EsspPTNView;
import com.es.tungnv.entity.EsspEntityHoSo;
import com.es.tungnv.utils.EsspCommon;

/**
 * Created by TUNGNV on 9/13/2016.
 */
public class EsspViewPTNActivity extends AppCompatActivity implements View.OnClickListener {

    private EsspPTNView vView;
    private int HOSO_ID;

    public static EsspSqliteConnection connection;
    public static EsspEntityHoSo entityHoSo;

    public static float leftMargin = 0;
    public static float rightMargin = 0;
    public static float topMargin = 0;
    public static float bottomMargin = 0;
    public static float width = 0;
    public static float height = 0;
    public static float widthPage = 0;
    public static float heightPage = 0;

    public static float TONG_CS = 0f;
    public static float CAP_DA = 0f;
    public static float CS_MAX = 0f;
    public static float CS_TB = 0f;
    public static float HESO_DT = 1f;

    public static float wCol1Table1 = 0f;
    public static float wCol2Table1 = 0f;
    public static float wCol3Table1 = 0f;
    public static float wCol4Table1 = 0f;
    public static float wCol5Table1 = 0f;
    public static float wCol6Table1 = 0f;
    public static float numberRow = 0f;
    public static float topTable1 = 0f;
    public static float bottom1Table1 = 0f;
    public static float bottom2Table1 = 0f;

    public static Cursor cTB;

    public static String TEN_DVIQLY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setImmersiveMode();
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        setImmersiveMode();
                    }
                }
            });

            connection = EsspSqliteConnection.getInstance(this);

            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();
            leftMargin = width/15;
            rightMargin = width/20;
            topMargin = width/20;
            bottomMargin = width/20;
            widthPage = width - leftMargin - rightMargin;
            heightPage = height - topMargin - bottomMargin;
            EsspCommon.textSizeView = (int)(width/45);
            TONG_CS = 0f;
            CAP_DA = 220f;
            CS_MAX = 0f;
            CS_TB = 0f;
            HESO_DT = 1f;
            HOSO_ID = getIntent().getExtras().getInt("HOSO_ID");
            entityHoSo = connection.getDataHoSoById(HOSO_ID, EsspCommon.getUSERNAME());
            TEN_DVIQLY = connection.getTenDviByMa(EsspCommon.getMaDviqly()).toUpperCase();
            wCol1Table1 = 2*widthPage/10;
            wCol2Table1 = 4*widthPage/10;
            wCol3Table1 = widthPage/10;
            wCol4Table1 = widthPage/10;
            wCol5Table1 = widthPage/10;
            wCol6Table1 = widthPage/10;
            cTB = connection.getAllDataThietBiByMaHS(HOSO_ID);

            setContentView(R.layout.essp_activity_ptn_view);
            vView = (EsspPTNView) findViewById(R.id.vView);

            getSupportActionBar().hide();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    public void setImmersiveMode() {
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
