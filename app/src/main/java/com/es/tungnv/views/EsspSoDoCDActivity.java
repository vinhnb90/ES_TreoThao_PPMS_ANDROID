package com.es.tungnv.views;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.draws.EsspCanvasView;
import com.es.tungnv.enums.EsspGroupType;
import com.es.tungnv.enums.EsspShapeType;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.utils.EsspShape;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class EsspSoDoCDActivity extends AppCompatActivity implements View.OnClickListener {

    //region Khai báo biến
    private EsspCanvasView esspCanvasView;
    private ImageButton ibLine, ibShape, ibPoles, ibElectricityMeters, ibText, ibUndo, ibEdit, ibDeleteLayer, ibSetting, ibExit;
    public RecyclerView rvLayer;
    public static int HOSO_ID = 0;
    public static int POS_SELECTED_LAYER = -1;
    public static float SIZE_CROP_VIEW = 0;
    public static boolean isDrawTop = true;
    public static boolean isShowSnapPoint = false;
    public static EsspSqliteConnection connection;
    public static EsspLayerAdapter layerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrCap = new ArrayList<>();
    public static float wStreet = 0f;
    public static float wLane = 0f;

    public static String SO_COT, TEN_KHANG, DIA_CHI, TEN_TRAM;
    //endregion

    //region Khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            HOSO_ID = getIntent().getExtras().getInt("HOSO_ID");
            connection = EsspSqliteConnection.getInstance(EsspSoDoCDActivity.this);
            SIZE_CROP_VIEW = getWindowManager().getDefaultDisplay().getHeight() / 6;
            wStreet = getWindowManager().getDefaultDisplay().getHeight() / 20;
            wLane = getWindowManager().getDefaultDisplay().getHeight() / 28;
            SO_COT = EsspSoDoCDActivity.connection.getSoCotPAByHoSoID(EsspSoDoCDActivity.HOSO_ID);
            TEN_KHANG = EsspSoDoCDActivity.connection.getTenKH(EsspSoDoCDActivity.HOSO_ID);
            DIA_CHI = EsspSoDoCDActivity.connection.getDiaChi(EsspSoDoCDActivity.HOSO_ID);
            TEN_TRAM = EsspSoDoCDActivity.connection.getTenTram(EsspSoDoCDActivity.connection.getMaTram(EsspSoDoCDActivity.HOSO_ID));
            connection.close();

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

            setContentView(R.layout.activity_sodocd);
            esspCanvasView = (EsspCanvasView) findViewById(R.id.cvCanvas);
            EsspCommon.textSize = getWindowManager().getDefaultDisplay().getHeight() / 38;
            initComponents();
            setLayer();
            getSupportActionBar().hide();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            EsspCommon.isBuildPole = false;
            EsspCommon.isBuildBox = false;
            POS_SELECTED_LAYER = -1;

            esspCanvasView.destroyDrawingCache();
            esspCanvasView = null;
            HOSO_ID = 0;
            SIZE_CROP_VIEW = 0;
            isDrawTop = true;
            connection.close();
            connection = null;
            layerAdapter = null;
            arrCap = null;
            SO_COT = "";
            TEN_KHANG = "";
            DIA_CHI = "";
            TEN_TRAM = "";
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setImmersiveMode() {
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void initComponents() {
        ibLine = (ImageButton) findViewById(R.id.ibLine);
        ibShape = (ImageButton) findViewById(R.id.ibShape);
        ibPoles = (ImageButton) findViewById(R.id.ibPoles);
        ibElectricityMeters = (ImageButton) findViewById(R.id.ibElectricityMeters);
        ibText = (ImageButton) findViewById(R.id.ibText);
        ibUndo = (ImageButton) findViewById(R.id.ibUndo);
        ibEdit = (ImageButton) findViewById(R.id.ibEdit);
        ibDeleteLayer = (ImageButton) findViewById(R.id.ibDeleteLayer);
        ibSetting = (ImageButton) findViewById(R.id.ibSetting);
        ibExit = (ImageButton) findViewById(R.id.ibExit);
        rvLayer = (RecyclerView) findViewById(R.id.rvLayer);

        ibLine.setBackgroundResource(R.drawable.border_draw_icon_green);

        layoutManager = new LinearLayoutManager(this);
        rvLayer.setHasFixedSize(true);
        rvLayer.setLayoutManager(layoutManager);

        ibLine.setOnClickListener(this);
        ibShape.setOnClickListener(this);
        ibPoles.setOnClickListener(this);
        ibElectricityMeters.setOnClickListener(this);
        ibText.setOnClickListener(this);
        ibUndo.setOnClickListener(this);
        ibEdit.setOnClickListener(this);
        ibDeleteLayer.setOnClickListener(this);
        ibSetting.setOnClickListener(this);
        ibExit.setOnClickListener(this);
    }
    //endregion

    //region Xử lý sự kiện
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibLine:
                try {
                    popupMenuBrush(EsspGroupType.BRUSH);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibShape:
                try {
                    popupMenuBrush(EsspGroupType.SHAPE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibPoles:
                try {
                    popupMenuBrush(EsspGroupType.POLES);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibElectricityMeters:
                try {
                    popupMenuBrush(EsspGroupType.METER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibText:
                try {
                    popupMenuBrush(EsspGroupType.TEXT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibUndo:
                try {
                    POS_SELECTED_LAYER = -1;
                    if (esspCanvasView.pLineEsspShapes.size() > 0) {
                        if(esspCanvasView.posText == esspCanvasView.pLineEsspShapes.size() - 1){
                            esspCanvasView.posText = -1;
                        }
                        if (esspCanvasView.pLineEsspShapes.get(esspCanvasView.pLineEsspShapes.size() - 1).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE ||
                                esspCanvasView.pLineEsspShapes.get(esspCanvasView.pLineEsspShapes.size() - 1).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE) {
                            EsspCommon.isBuildPole = false;
                        }
                        if (esspCanvasView.pLineEsspShapes.get(esspCanvasView.pLineEsspShapes.size() - 1).getEsspShapeType() == EsspShapeType.WALL) {
                            EsspCommon.isBuildWall = false;
                        }
                        for (int i = esspCanvasView.pSnapPoint.size() - 1; i >= 0; i--) {
                            if(esspCanvasView.pSnapPoint.get(i).getId()
                                    == esspCanvasView.pLineEsspShapes.get(esspCanvasView.pLineEsspShapes.size() - 1).getId()) {
                                esspCanvasView.pSnapPoint.remove(i);
                            }
                        }
                        esspCanvasView.pLineEsspShapes.remove(esspCanvasView.pLineEsspShapes.size() - 1);
                        layerAdapter.updateList(esspCanvasView.pLineEsspShapes);

                        esspCanvasView.invalidate();
                    }
                } catch (Exception ex) {
                    EsspCommon.isBuildPole = true;
                    Toast.makeText(EsspSoDoCDActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ibEdit:
                try {
                    popupMenuBrush(EsspGroupType.EDIT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibDeleteLayer:
                try {
                    if (POS_SELECTED_LAYER != -1) {
                        if(esspCanvasView.posText == POS_SELECTED_LAYER){
                            esspCanvasView.posText = -1;
                            for (int i = 0; i < esspCanvasView.pLineEsspShapes.size(); i++) {
                                if(esspCanvasView.pLineEsspShapes.get(i).getEsspShapeType() == EsspShapeType.TEXT){
                                    esspCanvasView.posText = i;
                                    return;
                                }
                            }
                        }
                        if (esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_CIRCLE ||
                                esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.POWER_POLES_SQUARE) {
                            EsspCommon.isBuildPole = false;
                        }
                        if (esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).getEsspShapeType() == EsspShapeType.WALL) {
                            EsspCommon.isBuildWall = false;
                        }
                        for (int i = esspCanvasView.pSnapPoint.size() - 1; i >= 0; i--) {
                            if(esspCanvasView.pSnapPoint.get(i).getId()
                                    == esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).getId()) {
                                esspCanvasView.pLineEsspShapes.remove(i);
                            }
                        }
                        esspCanvasView.pLineEsspShapes.remove(POS_SELECTED_LAYER);
                        layerAdapter.updateList(esspCanvasView.pLineEsspShapes);
                        esspCanvasView.invalidate();
                        POS_SELECTED_LAYER = -1;
                        EsspCommon.esspShapeType = EsspShapeType.BRUSH;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.ibSetting:
                popupSetting();
                break;
            case R.id.ibExit:
                if (esspCanvasView.checkSave() || EsspCommon.check_save) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EsspSoDoCDActivity.this);
                    builder.setMessage("Bạn có muốn lưu không?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (saveSD()) {
                                EsspSoDoCDActivity.this.finish();
                            }
                        }
                    });
                    builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EsspSoDoCDActivity.this.finish();
                        }
                    });
                    builder.setNeutralButton("Cancel", null);
                    builder.show();
                }
                break;
        }
    }

    private void setSelectedBrush(EsspGroupType type) {
        ibLine.setBackgroundResource(R.drawable.border_draw_icon);
        ibShape.setBackgroundResource(R.drawable.border_draw_icon);
        ibPoles.setBackgroundResource(R.drawable.border_draw_icon);
        ibElectricityMeters.setBackgroundResource(R.drawable.border_draw_icon);
        ibText.setBackgroundResource(R.drawable.border_draw_icon);
        switch (type) {
            case BRUSH:
                ibLine.setBackgroundResource(R.drawable.border_draw_icon_green);
                break;
            case SHAPE:
                ibShape.setBackgroundResource(R.drawable.border_draw_icon_green);
                break;
            case POLES:
                ibPoles.setBackgroundResource(R.drawable.border_draw_icon_green);
                break;
            case METER:
                ibElectricityMeters.setBackgroundResource(R.drawable.border_draw_icon_green);
                break;
            case TEXT:
                ibText.setBackgroundResource(R.drawable.border_draw_icon_green);
                break;
        }
    }

    private boolean saveSD() {
        esspCanvasView.isViewDraw = false;
        isShowSnapPoint = false;
        esspCanvasView.invalidate();
        if (connection.insertDataBanVe(HOSO_ID, new StringBuilder(EsspCommon.getMaDviqly()).append("_").append(HOSO_ID).toString(), "", "0") != -1) {
            esspCanvasView.setDrawingCacheEnabled(true);
            esspCanvasView.buildDrawingCache();
            Bitmap well = Bitmap.createBitmap(esspCanvasView.getDrawingCache(), 0, 0, esspCanvasView.getWidth(), esspCanvasView.getHeight());
            String TEN_ANH = new StringBuilder(EsspCommon.getMaDviqly()).append("_").append(HOSO_ID).toString() + ".jpg";
            String pathImage = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH;
            Common.SaveImage(pathImage, TEN_ANH, well);
            Toast.makeText(EsspSoDoCDActivity.this, "Đã lưu ảnh!", Toast.LENGTH_LONG).show();
            EsspCommon.check_save = false;
            well.recycle();
            well = null;
            return true;
        }
        return false;
    }

    private void popupText(final EsspShapeType TYPE) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_text);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

            final EditText etText = (EditText) dialog.findViewById(R.id.etText);
            Button btOK = (Button) dialog.findViewById(R.id.btOK);
            final Spinner spTextType = (Spinner) dialog.findViewById(R.id.spTextType);
            final Spinner spTextSize = (Spinner) dialog.findViewById(R.id.spTextSize);
            final Spinner spLoaiCap = (Spinner) dialog.findViewById(R.id.spLoaiCap);
            final CheckBox cbCoSan = (CheckBox) dialog.findViewById(R.id.cbCoSan);

            final String[] arrTextType = new String[] {"Chọn chữ có sẵn", "Tên khách hàng", "Địa chỉ", "Trạm biến áp",
                    "Cáp trước công tơ", "Cáp sau công tơ", "Số cột", "Vị trí treo công tơ", "Vị trí đặt hòm công tơ mới"};
            ArrayAdapter<String> adapterTextType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrTextType);
            spTextType.setAdapter(adapterTextType);

            final String[] arrTextSize = new String[] {"14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "28", "32", "36", "40"};
            ArrayAdapter<String> adapterTextSize = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrTextSize);
            spTextSize.setAdapter(adapterTextSize);

            for (int i = 0; i < arrTextSize.length; i++){
                if(Integer.parseInt(arrTextSize[i]) == EsspCommon.textSize){
                    spTextSize.setSelection(i);
                    i = arrTextSize.length;
                }
            }

            if(TYPE == EsspShapeType.EDIT_TEXT) {
                if(POS_SELECTED_LAYER != -1) {
                    etText.setText(esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).getText()[0]);
                }
            }

            spTextType.post(new Runnable() {
                @Override
                public void run() {
                    spTextType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText("");
                                    break;
                                case 1:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(TEN_KHANG);
                                    break;
                                case 2:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(DIA_CHI);
                                    break;
                                case 3:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(TEN_TRAM);
                                    break;
                                case 4:
                                    arrCap = new ArrayList<String>();
                                    spLoaiCap.setVisibility(View.VISIBLE);
                                    cbCoSan.setVisibility(View.VISIBLE);
                                    Cursor c = connection.getVTDuToanByIdHS(HOSO_ID, 1, 2);
                                    if(c.moveToFirst()){
                                        do {
                                            arrCap.add(c.getString(c.getColumnIndex("TEN_VTU")));
                                        } while(c.moveToNext());
                                    }
                                    final ArrayAdapter<String> adapterCap = new ArrayAdapter<String>(EsspSoDoCDActivity.this, android.R.layout.simple_list_item_1, arrCap);
                                    spLoaiCap.setAdapter(adapterCap);
                                    break;
                                case 5:
                                    arrCap = new ArrayList<String>();
                                    spLoaiCap.setVisibility(View.VISIBLE);
                                    cbCoSan.setVisibility(View.VISIBLE);
                                    Cursor c2 = connection.getVTDuToanByIdHS(HOSO_ID, 1, 1);
                                    if(c2.moveToFirst()){
                                        do {
                                            arrCap.add(c2.getString(c2.getColumnIndex("TEN_VTU")));
                                        } while(c2.moveToNext());
                                    }
                                    final ArrayAdapter<String> adapterCap2 = new ArrayAdapter<String>(EsspSoDoCDActivity.this, android.R.layout.simple_list_item_1, arrCap);
                                    spLoaiCap.setAdapter(adapterCap2);
                                    break;
                                case 6:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(SO_COT);
                                    break;
                                case 7:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(arrTextType[7]);
                                    break;
                                case 8:
                                    spLoaiCap.setVisibility(View.GONE);
                                    cbCoSan.setVisibility(View.GONE);
                                    etText.setText(arrTextType[8]);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            spLoaiCap.post(new Runnable() {
                @Override
                public void run() {
                    spLoaiCap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            etText.setText(spLoaiCap.getItemAtPosition(position).toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

            cbCoSan.post(new Runnable() {
                @Override
                public void run() {
                    cbCoSan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                etText.setText(etText.getText().toString().trim() + " (Có sẵn)");
                            } else {
                                if(etText.getText().toString().trim().contains("(Có sẵn)")){
                                    etText.setText(etText.getText().toString().trim().replace(" (Có sẵn)", ""));
                                }
                            }
                        }
                    });
                }
            });

            btOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String note = etText.getText().toString();
                        if (!note.isEmpty()) {
                            if (TYPE != EsspShapeType.EDIT_TEXT) {
                                EsspCommon.esspShapeType = TYPE;
                                EsspCommon.text = note;
                                EsspCommon.textSize = Integer.parseInt(spTextSize.getSelectedItem().toString());
                            } else {
                                esspCanvasView.pLineEsspShapes.get(POS_SELECTED_LAYER).setText(new String[]{note});
                                esspCanvasView.invalidate();
                                POS_SELECTED_LAYER = -1;
                                layerAdapter.notifyDataSetChanged();
                            }
                        }
                        setImmersiveMode();
                        dialog.dismiss();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void popupSetting() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_setting);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

            CheckBox cbDrawTop = (CheckBox) dialog.findViewById(R.id.cbDrawTop);
            CheckBox cbAllowViewDraw = (CheckBox) dialog.findViewById(R.id.cbAllowViewDraw);
            CheckBox cbShowSnapPoint = (CheckBox) dialog.findViewById(R.id.cbShowSnapPoint);
            ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ibClose);

            cbDrawTop.setChecked(isDrawTop);
            cbAllowViewDraw.setChecked(esspCanvasView.isAllowViewDraw);
            cbShowSnapPoint.setChecked(isShowSnapPoint);

            cbDrawTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isDrawTop = isChecked;
                }
            });

            cbAllowViewDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    esspCanvasView.isAllowViewDraw = isChecked;
                }
            });

            cbShowSnapPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isShowSnapPoint = isChecked;
                }
            });

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    esspCanvasView.invalidate();
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    esspCanvasView.invalidate();
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void popupMenuBrush(final EsspGroupType type) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_choose_item);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

            GridView gvItem = (GridView) dialog.findViewById(R.id.gvItem);
            ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ibClose);

            ImageAdapter imageAdapter = null;

            switch (type) {
                case BRUSH:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_BRUSH_LABELS, EsspConstantVariables.ARR_BRUSH_ICONS);
                    break;
                case SHAPE:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_SHAPE_LABELS, EsspConstantVariables.ARR_SHAPE_ICONS);
                    break;
                case POLES:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_POLE_LABELS, EsspConstantVariables.ARR_POLE_ICONS);
                    break;
                case METER:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_METER_BOX_LABELS, EsspConstantVariables.ARR_METER_BOX_ICONS);
                    break;
                case TEXT:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_TEXT_LABELS, EsspConstantVariables.ARR_TEXT_ICONS);
                    break;
                case EDIT:
                    imageAdapter = new ImageAdapter(this, EsspConstantVariables.ARR_EDIT_LABELS, EsspConstantVariables.ARR_EDIT_ICONS);
                    break;
            }

            gvItem.setAdapter(imageAdapter);

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            gvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    esspCanvasView.invalidate();
                    switch (type) {
                        case BRUSH:
                            POS_SELECTED_LAYER = -1;
                            EsspCommon.esspShapeType = EsspConstantVariables.ARR_BRUSH_TYPES[position];
                            break;
                        case SHAPE:
                            POS_SELECTED_LAYER = -1;
                            EsspCommon.esspShapeType = EsspConstantVariables.ARR_SHAPE_TYPES[position];
                            break;
                        case POLES:
                            POS_SELECTED_LAYER = -1;
                            EsspCommon.esspShapeType = EsspConstantVariables.ARR_POLE_TYPES[position];
                            break;
                        case METER:
                            POS_SELECTED_LAYER = -1;
                            switch (EsspConstantVariables.ARR_METER_BOX_TYPES[position]) {
                                case SUBSTATION:
                                    EsspCommon.esspShapeType = EsspShapeType.SUBSTATION;
                                    break;
                                case SUBSTATION_CIRCLE:
                                    EsspCommon.esspShapeType = EsspShapeType.SUBSTATION_CIRCLE;
                                    break;
                                case HOUSE:
                                    EsspCommon.esspShapeType = EsspShapeType.HOUSE;
                                    break;
                                case TOP_HOUSE_1:
                                    EsspCommon.esspShapeType = EsspShapeType.TOP_HOUSE_1;
                                    break;
                                case TOP_HOUSE_2:
                                    EsspCommon.esspShapeType = EsspShapeType.TOP_HOUSE_2;
                                    break;
                                case POWER_POLES_CIRCLE_TOP:
                                    EsspCommon.esspShapeType = EsspShapeType.POWER_POLES_CIRCLE_TOP;
                                    break;
                                case POWER_POLES_TOP:
                                    EsspCommon.esspShapeType = EsspShapeType.POWER_POLES_TOP;
                                    break;
                                case POWER_POLES_EXTRA_TOP:
                                    EsspCommon.esspShapeType = EsspShapeType.POWER_POLES_EXTRA_TOP;
                                    break;
                                case BOX6:
                                    if (EsspCommon.isBuildPole || EsspCommon.isBuildWall)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX6;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện, tường hoặc tủ", Toast.LENGTH_LONG).show();
                                    break;
                                case BOX4:
                                    if (EsspCommon.isBuildPole || EsspCommon.isBuildWall)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX4;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện, tường hoặc tủ", Toast.LENGTH_LONG).show();
                                    break;
                                case BOX2:
                                    if (EsspCommon.isBuildPole || EsspCommon.isBuildWall)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX2;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện, tường hoặc tủ", Toast.LENGTH_LONG).show();
                                    break;
                                case BOX1:
                                    if (EsspCommon.isBuildPole || EsspCommon.isBuildWall)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX1;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện, tường hoặc tủ", Toast.LENGTH_LONG).show();
                                    break;
                                case BOX3F:
                                    if (EsspCommon.isBuildPole || EsspCommon.isBuildWall)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX3F;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện, tường hoặc tủ", Toast.LENGTH_LONG).show();
                                    break;
                                case BOX_BEHIND:
                                    if (EsspCommon.isBuildPole)
                                        EsspCommon.esspShapeType = EsspShapeType.BOX_BEHIND;
                                    else
                                        Toast.makeText(EsspSoDoCDActivity.this, "Bạn chưa vẽ cột điện", Toast.LENGTH_LONG).show();
                                    break;
                                case ARROW_NOTE:
                                    popupText(EsspShapeType.ARROW_NOTE);
                                    break;
                            }
                            break;
                        case TEXT:
                            switch (EsspConstantVariables.ARR_TEXT_TYPES[position]) {
                                case TEXT:
                                    POS_SELECTED_LAYER = -1;
                                    popupText(EsspShapeType.TEXT);
                                    break;
                                case EDIT_TEXT:
                                    if(POS_SELECTED_LAYER != -1) {
                                        popupText(EsspShapeType.EDIT_TEXT);
                                    }
                                    break;
                            }
                            break;
                        case EDIT:
                            switch (EsspConstantVariables.ARR_EDIT_TYPES[position]) {
                                case SAVE:
                                    POS_SELECTED_LAYER = -1;
                                    esspCanvasView.invalidate();
                                    if (esspCanvasView.checkSave() || EsspCommon.check_save) {
                                        saveSD();
                                    }
                                    break;
                                case CLEAR:
                                    POS_SELECTED_LAYER = -1;
                                    esspCanvasView.clear();
                                    break;
                            }
                            break;
                    }
                    dialog.dismiss();
                    setSelectedBrush(type);
                }
            });

            dialog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setLayer() {
        try {
            layerAdapter = new EsspLayerAdapter(esspCanvasView.pLineEsspShapes);
            rvLayer.setAdapter(layerAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region Tạo JSON
    public JSONObject DATAtoJSONTemplate(EsspShape shape) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("PAINT",shape.getPaint());
            jsonObject.accumulate("SHAPE_TYPE",shape.getEsspShapeType());
            jsonObject.accumulate("PATH", shape.getPath());
            jsonObject.accumulate("START_POINT", shape.getStartPoint());
            jsonObject.accumulate("END_POINT", shape.getEndPoint());
            jsonObject.accumulate("TEXT", shape.getText());
            jsonObject.accumulate("DRAWABLE", shape.getDrawable());
            jsonObject.accumulate("DX", shape.getDx());
            jsonObject.accumulate("DY", shape.getDy());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void savePDF(Bitmap [] bmp) {
        Document document=new Document();
        try {
            String outpath= Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + "/PTN.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(outpath));
            document.open();
            for(Bitmap b : bmp) {
                Image image = Image.getInstance(Common.encodeTobase64Byte(b));
                document.add(image);
            }
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Class adapter
    class ImageAdapter extends BaseAdapter {

        private Context context;
        private final String[] labelValues;
        private final Integer[] iconValues;

        public ImageAdapter(Context context, String[] labelValues, Integer[] iconValues) {
            this.context = context;
            this.labelValues = labelValues;
            this.iconValues = iconValues;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView;
            if (convertView == null) {
                gridView = new View(context);
                gridView = inflater.inflate(R.layout.essp_row_draw, null);
                TextView textView = (TextView) gridView.findViewById(R.id.tvLabel);
                ImageView imageView = (ImageView) gridView.findViewById(R.id.ivIcon);

                textView.setText(labelValues[position]);
                imageView.setImageResource(iconValues[position]);
            } else {
                gridView = (View) convertView;
            }
            return gridView;
        }

        @Override
        public int getCount() {
            return labelValues.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    public class EsspLayerAdapter extends RecyclerView.Adapter<EsspLayerAdapter.RecyclerViewHolder> {

        List<EsspShape> listData = new ArrayList<>();

        public EsspLayerAdapter(List<EsspShape> listData) {
            this.listData = listData;
        }

        public void updateList(List<EsspShape> data) {
            listData = data;
            notifyDataSetChanged();
            rvLayer.scrollToPosition(data.size() - 1);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.essp_row_layer_sodo, parent, false);
            return new RecyclerViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            try {
                holder.ivHinhAnh.setImageResource(listData.get(position).getDrawable());
                if (POS_SELECTED_LAYER == position) {
                    holder.itemView.setBackgroundResource(R.drawable.border_draw_icon_gray);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.border_draw_icon);
                }
            } catch (Exception ex) {
                Toast.makeText(EsspSoDoCDActivity.this, "Lỗi hiển thị ảnh", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public void addItem(int position, EsspShape data) {
            listData.add(position, data);
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            listData.remove(position);
            notifyItemRemoved(position);
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

            public ImageView ivHinhAnh;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                ivHinhAnh = (ImageView) itemView.findViewById(R.id.essp_row_layer_iv_layer);
                ivHinhAnh.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                try {
                    POS_SELECTED_LAYER = getPosition();
                    EsspCommon.esspShapeType = EsspShapeType.MOVE;
                    notifyDataSetChanged();
                    esspCanvasView.invalidate();
                } catch (Exception ex) {
                    Toast.makeText(EsspSoDoCDActivity.this, "Lỗi sự kiện", Toast.LENGTH_LONG).show();
                }
            }

        }

    }
    //endregion

}
