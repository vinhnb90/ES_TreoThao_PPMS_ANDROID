package com.es.tungnv.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.es.tungnv.adapters.LayerAdapter;
import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.draws.SingleTouchViewNew;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by TUNGNV on 4/8/2016.
 */
public class EsspDrawImageActivity extends Activity{

    private ImageButton ibtSave, ibtUndo;
//    private static ImageButton ibtSaveSample;
//    private static ImageButton ibtSelectSample;
    private static ImageButton ibtRotate;
    private ImageButton ibtCamera;
    private ImageButton ibtMap;
    private static ImageButton ibtCursor;
    private static ImageButton ibtClean;
    private static ImageButton ibtDraw;
    private static ImageButton ibtLine;
    private static ImageButton ibtLine3;
    private static ImageButton ibtLineDash;
    private static ImageButton ibtLineCustom;
    private static ImageButton ibtRec;
    private static ImageButton ibtTram;
    private static ImageButton ibtHo;
    private static ImageButton ibtHop;
    private static ImageButton ibtCongTo;
    private static ImageButton ibtCiclea;
    private static ImageButton ibtText;
    private static ImageButton ibtColor;
    private static ImageButton ibtDeleteLayer;
    public static ListView lsvLayer;
    private static SingleTouchViewNew stv;
    public EsspSqliteConnection connection;

    private static int size = 0;
    public static int width = 0;
    public static int height = 0;
    public static String text = "";
    public static String TYPE = "";
    public static float size_rate = 0f;
    public static float rate = 0f;
    public static DisplayMetrics metrics;
    public static float size_line = 0f;

    public static int HOSO_ID = 0;
    private static String MA_DVIQLY = "";
//    private int pos = 0;
//    public static int pos_map = 0;
    public static int pos_layer = 0;
    public static int pos_layer_show = 0;

    private Bitmap photo;
    private static final int CAMERA_REQUEST = 1888;

//    public static GPSTracker gps;

//    private String[] arrPX = {"2px", "4px", "6px", "8px", "10px", "12px"};
//    private Integer[] arrIMPX = {R.drawable.b2px,
//            R.drawable.b4px,
//            R.drawable.b6px,
//            R.drawable.b8px,
//            R.drawable.b10px,
//            R.drawable.b12px};

//    public static ArrayList<LinkedHashMap<String, String>> arr_daycap = null;
    public static LayerAdapter adapter_layer;

//    Common comm = new Common();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            connection = EsspSqliteConnection.getInstance(this);
            setContentView(R.layout.essp_activity_draw);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            metrics = getResources().getDisplayMetrics();
            Display d = getWindowManager().getDefaultDisplay();
            size = d.getWidth() - 65;
            width = d.getWidth();
            height = d.getHeight() - 90;
            EsspCommon.bmMap = null;
//            gps = new GPSTracker(this);
            Bundle b = getIntent().getBundleExtra("INFO");
            HOSO_ID = b.getInt("HOSO_ID");
            MA_DVIQLY = connection.getMaDviByID(EsspCommon.getIdDviqly());
            TYPE = b.getString("TYPE");

            initComponents();
            eventButton();
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
                    "Lỗi khởi tạo:\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    private void initComponents(){
        stv = (SingleTouchViewNew)findViewById(R.id.draw);
        ibtSave = (ImageButton)findViewById(R.id.ibtSave);
        ibtUndo = (ImageButton)findViewById(R.id.ibtUndo);
//        ibtSaveSample = (ImageButton)findViewById(R.id.ibtSaveSample);
//        ibtSelectSample = (ImageButton)findViewById(R.id.ibtSelectSample);
        ibtRotate = (ImageButton)findViewById(R.id.ibtRotate);
        ibtCamera = (ImageButton)findViewById(R.id.ibtCamera);
        ibtMap = (ImageButton)findViewById(R.id.ibtMap);
        ibtCursor = (ImageButton)findViewById(R.id.ibtCursor);
        ibtClean = (ImageButton)findViewById(R.id.ibtClean);
        ibtDraw = (ImageButton)findViewById(R.id.ibtDraw);
        ibtLine = (ImageButton)findViewById(R.id.ibtLine);
        ibtLine3 = (ImageButton)findViewById(R.id.ibtLine3);
        ibtLineDash = (ImageButton)findViewById(R.id.ibtLineDash);
        ibtLineCustom = (ImageButton)findViewById(R.id.ibtLineCustom);
        ibtRec = (ImageButton)findViewById(R.id.ibtRec);
        ibtTram = (ImageButton)findViewById(R.id.ibtTram);
        ibtHo = (ImageButton)findViewById(R.id.ibtHo);
        ibtHop = (ImageButton)findViewById(R.id.ibtHop);
        ibtCongTo = (ImageButton)findViewById(R.id.ibtCongTo);
        ibtCiclea = (ImageButton)findViewById(R.id.ibtCiclea);
        ibtText = (ImageButton)findViewById(R.id.ibtText);
        ibtColor = (ImageButton)findViewById(R.id.ibtColor);
        ibtDeleteLayer = (ImageButton)findViewById(R.id.ibtDeleteLayer);
        lsvLayer = (ListView)findViewById(R.id.lsvLayer);
    }

    private void eventButton(){
        ibtDeleteLayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearLayer(pos_layer, pos_layer_show, stv.isMove);
            }
        });

        ibtSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    if(stv.checkSave() || EsspCommon.check_save){
//                        Bitmap save = Bitmap.createBitmap(800, 1066, Bitmap.Config.ARGB_8888);
//                        Paint paint = new Paint();
//                        paint.setColor(Color.WHITE);
//                        Canvas now = new Canvas(save);
//                        now.drawRect(new Rect(0, 0, 800, 1066), paint);
//                        now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 800, 1066), null);

                        if(EsspCommon.TYPE.equals("ANH")){
                            Bitmap well = stv.getBitmap();
                            if(!EsspCommon.IMAGE_NAME.equals("")){
                                String TEN_ANH = EsspCommon.IMAGE_NAME + ".jpg";
                                String pathImage = Environment.getExternalStorageDirectory() +
                                        EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH;
                                Common.SaveImage(pathImage, TEN_ANH, well);
                                Toast.makeText(EsspDrawImageActivity.this, "Đã lưu ảnh!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Bitmap well = Bitmap.createBitmap(stv.getBitmap(), 0, 0, stv.getBitmap().getWidth(), (int)(stv.getBitmap().getWidth()/1.216f));
                            if(!EsspCommon.IMAGE_NAME.equals("")){
                                String TEN_ANH = EsspCommon.IMAGE_NAME + ".jpg";
                                String pathImage = Environment.getExternalStorageDirectory() +
                                        EsspConstantVariables.PROGRAM_PHOTO_BV_PATH;
                                Common.SaveImage(pathImage, TEN_ANH, well);
                                Toast.makeText(EsspDrawImageActivity.this, "Đã lưu ảnh!", Toast.LENGTH_LONG).show();
                            }
                        }
                        EsspCommon.check_save = false;
                    }
                } catch(Exception ex) {
                    Toast.makeText(EsspDrawImageActivity.this, "Lỗi lưu bản vẽ", Toast.LENGTH_LONG).show();
                }
            }
        });
        ibtUndo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				stv.undo();
                clearLayer(adapter_layer.getCount() - 1, 0, true);
            }
        });

//        ibtSaveSample.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(stv.checkSave() || Common.check_save){
//                    createDialogSample();
//                }
//            }
//        });
//
//        ibtSelectSample.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                createDialogSelectSample();
//            }
//        });

        ibtRotate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(adapter_layer.getCount() > 0){
                    resetButton(EsspDrawImageActivity.this);
                    ibtRotate.setImageDrawable(getResources().getDrawable(R.mipmap.ic_rotate2));
                    //				pos_layer = lsvLayer.getSelectedItemPosition();
                    EsspCommon.state_draw = stv.arr_type.get(pos_layer);
                    stv.isMove = true;
                    stv.isRotate = true;
                }
            }
        });
        ibtCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        ibtMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
//                    if(Common.isConn(EsspDrawImageActivity.this)){
//                        double latitude = 0; // vĩ độ
//                        double longitude = 0; // kinh độ
//                        if (EsspDrawImageActivity.gps.canGetLocation()) {
//                            latitude = EsspDrawImageActivity.gps.getLatitude();
//                            longitude = EsspDrawImageActivity.gps.getLongitude();
//                            Bundle b = new Bundle();
//                            b.putString("LAT", "" + latitude);
//                            b.putString("LONG", "" + longitude);
//                            b.putString("TITLE", "");
//                            b.putString("dc", "");
//                            Intent intent = new Intent(EsspDrawImageActivity.this, EsspDrawImageActivity.class);
//                            intent.putExtra("POS", b);
//                            startActivity(intent);
//                        }
//                    } else {
//                        Common.ShowToast(EsspDrawImageActivity.this, "Thiết bị chưa có kết nối internet", Toast.LENGTH_LONG, Activity_Vi_Tri.size);
//                    }
                } catch(Exception ex) {

                }
            }
        });
        ibtCursor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    stv.isChangeLayer = false;
                    stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                    resetButton(EsspDrawImageActivity.this);
                    ibtCursor.setImageDrawable(getResources().getDrawable(R.mipmap.ic_curso2));
//					pos_layer = lsvLayer.getSelectedItemPosition();
                    EsspCommon.state_draw = stv.arr_type.get(pos_layer);

                    stv.xr1 = stv.width;
                    stv.xr2 = 0f;
                    stv.yr1 = stv.height;
                    stv.yr2 = 0f;

                    ArrayList<LinkedHashMap<String, Float>> arr = stv.arr_pixel.get(pos_layer);
                    for (LinkedHashMap<String, Float> map : arr) {
                        float x = map.get("x");
                        float y = map.get("y");
                        if(stv.xr1 > x)
                            stv.xr1 = x;
                        if(stv.xr2 < x)
                            stv.xr2 = x;
                        if(stv.yr1 > y)
                            stv.yr1 = y;
                        if(stv.yr2 < y)
                            stv.yr2 = y;
                    }
                    stv.isMove = true;
                    stv.isRotate = false;

                    stv.drawRecSelect();
                } catch(Exception ex) {
                    ex.toString();
                }
            }
        });
        ibtClean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				stv.cleaner();
                cleaner();
            }
        });
        ibtDraw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtDraw.setImageDrawable(getResources().getDrawable(R.mipmap.ic_draw2));
                EsspCommon.state_draw = 3;
                stv.invalidate();
            }
        });
        ibtLine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtLine.setImageDrawable(getResources().getDrawable(R.mipmap.ic_line2));
                EsspCommon.state_draw = 4;
                stv.invalidate();
            }
        });
        ibtLine3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtLine3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_duong2));
                EsspCommon.state_draw = 18;
                stv.invalidate();
            }
        });
        ibtLineDash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtLineDash.setImageDrawable(getResources().getDrawable(R.mipmap.ic_dashpath2));
                EsspCommon.state_draw = 19;
                stv.invalidate();
            }
        });
        ibtLineCustom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtLineCustom.setImageDrawable(getResources().getDrawable(R.mipmap.ic_day_cap2));
                EsspCommon.state_draw = 20;
                stv.invalidate();
            }
        });
//        ibtArrow.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                stv.isChangeLayer = false;
//                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
//                stv.isMove = false;
//                resetButton(Activity_Draw_New.this);
//                ibtArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow2));
//                Common.state_draw = 21;
//            }
//        });
        ibtRec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtRec.setImageDrawable(getResources().getDrawable(R.mipmap.ic_rec2));
                EsspCommon.state_draw = 6;
                stv.invalidate();
            }
        });
        ibtTram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtTram.setImageDrawable(getResources().getDrawable(R.mipmap.ic_tram2));
                EsspCommon.state_draw = 15;
                stv.invalidate();
            }
        });
        ibtHo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtHo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_ho2));
                EsspCommon.state_draw = 14;
                stv.invalidate();
            }
        });
        ibtHop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtHop.setImageDrawable(getResources().getDrawable(R.mipmap.ic_hop2));
                EsspCommon.state_draw = 16;
                stv.invalidate();
            }
        });
        ibtCongTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtCongTo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_congto2));
                EsspCommon.state_draw = 17;
                stv.invalidate();
            }
        });
        ibtCiclea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stv.isChangeLayer = false;
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                stv.isMove = false;
                resetButton(EsspDrawImageActivity.this);
                ibtCiclea.setImageDrawable(getResources().getDrawable(R.mipmap.ic_cicle_a2));
                EsspCommon.state_draw = 8;
                stv.invalidate();
            }
        });

        ibtText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogGhiChu(EsspDrawImageActivity.this, 1);
            }
        });

        ibtColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialogColor(0);
            }
        });
//        etRate2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                createDialogRate(EsspDrawImageActivity.this, 0, 0, false, false);
//            }
//        });
//
//        etSize.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                createDialogRate(EsspDrawImageActivity.this, 0, 0, false, true);
//            }
//        });
//		ibtArrow.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetButton(Activity_Draw_New.this);
//				ibtArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow2));
//				Common.state_draw = 21;
//			}
//		});

//        etDoVong.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        lsvLayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                stv.isChangeLayer = false;
                int c = adapter_layer.getCount() - 1;
                pos_layer_show = position;
                pos_layer = c - position;
                adapter_layer.notifyDataSetChanged();
                lsvLayer.invalidate();

                resetButton(EsspDrawImageActivity.this);
                if(stv.isRotate){
                    ibtRotate.setImageDrawable(getResources().getDrawable(R.mipmap.ic_rotate2));
                } else {
                    ibtCursor.setImageDrawable(getResources().getDrawable(R.mipmap.ic_curso2));
                }

                EsspCommon.state_draw = stv.arr_type.get(pos_layer);

                stv.xr1 = stv.width;
                stv.xr2 = 0f;
                stv.yr1 = stv.height;
                stv.yr2 = 0f;

                ArrayList<LinkedHashMap<String, Float>> arr = stv.arr_pixel.get(pos_layer);
                for (LinkedHashMap<String, Float> map : arr) {
                    float x = map.get("x");
                    float y = map.get("y");
                    if(stv.xr1 > x)
                        stv.xr1 = x;
                    if(stv.xr2 < x)
                        stv.xr2 = x;
                    if(stv.yr1 > y)
                        stv.yr1 = y;
                    if(stv.yr2 < y)
                        stv.yr2 = y;
                }
                stv.isMove = true;

                stv.drawRecSelect();
                stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
            }
        });
    }

    protected void cleaner() {
        try{
            stv.path_virtual.clear();
            stv.state_undos.clear();
            stv.arr_type.clear();
            stv.pos_x_text_rotate.clear();
            stv.pos_y_text_rotate.clear();
            stv.pos_x_text.clear();
            stv.pos_y_text.clear();
            stv.angles.clear();
            stv.string_text.clear();
            stv.paints_text.clear();
            stv.pos_text.clear();
            stv.angle_text.clear();
            stv.state_undos.clear();
            stv.arr_snap_point.clear();
            stv.paths.clear();
            stv.paints.clear();
            stv.angles_rotate.clear();
            stv.arr_check_rate.clear();
            stv.arr_check_rate_bool.clear();
            stv.arr_pos_center_rotate.clear();
            stv.arr_pixel.clear();
            stv.arr_image.clear();
            stv.path_virtual.clear();
            EsspDrawImageActivity.this.pos_layer = 0;
            EsspDrawImageActivity.this.pos_layer_show = 0;
            stv.invalidate();
            adapter_layer.notifyDataSetChanged();
            lsvLayer.invalidate();
        } catch(Exception ex) {
            ex.toString();
        }
    }

    private boolean saveImageToFile(Bitmap bmp_result, String name){
        try{
            if(bmp_result != null){
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp_result.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + "/ESSP/sample/" + name + ".jpg");
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
                return true;
            } else {
                return false;
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
                    "Không chụp được hình ảnh", Color.WHITE, "OK", Color.RED);
            return false;
        }
    }

    private void clearLayer(int pos_layer, int pos_layer_show, boolean check) {
        try{
            if(adapter_layer.getCount() > 0 && check){
                int pos = 0;
                for (int i = 0; i < pos_layer; i++) {
                    pos += stv.path_virtual.get(i);
                }

                if(stv.state_undos.get(pos_layer) > 1 && (stv.arr_type.get(pos_layer) == 3 || stv.arr_type.get(pos_layer) == 4 || stv.arr_type.get(pos_layer) == 20)){
                    try {
                        stv.pos_x_text_rotate.remove((int)(stv.pos_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_y_text_rotate.remove((int)(stv.pos_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_x_text.remove((int)(stv.pos_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_y_text.remove((int)(stv.pos_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.angles.remove((int)(stv.angle_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.string_text.remove((int)(stv.angle_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.paints_text.remove((int)(stv.angle_text.get(pos + 1)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                }

                if(stv.arr_type.get(pos_layer) == 10){
                    try {
                        stv.pos_x_text_rotate.remove((int)(stv.pos_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_y_text_rotate.remove((int)(stv.pos_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_x_text.remove((int)(stv.pos_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.pos_y_text.remove((int)(stv.pos_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.angles.remove((int)(stv.angle_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.string_text.remove((int)(stv.angle_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.paints_text.remove((int)(stv.angle_text.get(pos)));
                    } catch (Exception ex) {
                        ex.toString();
                    }
                }

                for (int j = stv.state_undos.get(pos_layer) - 1; j >= 0 ; j--) {
                    try {
                        stv.arr_snap_point.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.paths.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.paints.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.angles_rotate.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.arr_check_rate.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.arr_check_rate_bool.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.arr_pos_center_rotate.remove(pos + j);
                    } catch (Exception ex) {
                        ex.toString();
                    }

                }
                try {
                    stv.state_undos.remove(pos_layer);
                } catch (Exception ex) {
                    ex.toString();
                }
                try {
                    stv.arr_pixel.remove(pos_layer);
                } catch (Exception ex) {
                    ex.toString();
                }
                try {
                    stv.arr_image.remove(pos_layer_show);
                } catch (Exception ex) {
                    ex.toString();
                }
                try {
                    stv.arr_type.remove(pos_layer);
                } catch (Exception ex) {
                    ex.toString();
                }
                try {
                    stv.path_virtual.remove(pos_layer);
                } catch (Exception ex) {
                    ex.toString();
                }

//				pos_layer = 0;
                if(pos_layer_show > 0){
                    pos_layer_show--;
                } else {
                    pos_layer_show = 0;
                }
                pos_layer = adapter_layer.getCount() - 1 - pos_layer_show;
                EsspDrawImageActivity.this.pos_layer = adapter_layer.getCount() - 1 - pos_layer_show;
                lsvLayer.setSelection(pos_layer);

                stv.invalidate();
                EsspDrawImageActivity.setListLayer(EsspDrawImageActivity.this);
                if(adapter_layer.getCount() == 0){
                    try {
                        stv.pos_text.clear();
                    } catch (Exception ex) {
                        ex.toString();
                    }
                    try {
                        stv.angle_text.clear();
                    } catch (Exception ex) {
                        ex.toString();
                    }

                    stv.isChangeLayer = false;
                    stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                    stv.isMove = false;
                    resetButton(EsspDrawImageActivity.this);
                    ibtDraw.setImageDrawable(getResources().getDrawable(R.mipmap.ic_draw2));
                    EsspCommon.state_draw = 3;
                    stv.invalidate();
                }
            }
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
                    "Không xóa được layer", Color.WHITE, "OK", Color.RED);
        }
    }

    public static void setListLayer(Context ctx){
        try{
            adapter_layer = new LayerAdapter(ctx, R.layout.essp_row_layer, stv.arr_image);
            lsvLayer.setAdapter(adapter_layer);
            if(adapter_layer.getCount() > 1){
                pos_layer_show++;
            }
        } catch(Exception ex){
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED,
                    "Lỗi tạo lớp", Color.WHITE, "OK", Color.RED);
        }
    }

//    public void insertVT(LinkedHashMap<String, String> map){
//        try{
//            String ma_vtu = map.get("MA_VTU");
//            String ma_loai_cphi = map.get("MA_LOAI_CPHI");
//            float so_luong = Float.parseFloat(map.get("soluong"));
//            double don_gia = Double.parseDouble(map.get("DON_GIA"));
//            double thanh_tien = don_gia*so_luong;
//            List<String> lstMa = connection.getMaDTByMaHS(MA_YCAU_KNAI);
//            if(lstMa.contains(ma_vtu)){// Nếu trong dự toán đã có vật tư này rồi thì cộng thêm độ dài cho dây cáp
//                so_luong = so_luong + connection.getSoLuongDT(MA_YCAU_KNAI, ma_vtu);
//                thanh_tien = don_gia*so_luong;
//                if(connection.updateSLDT(connection.getIDDT(MA_YCAU_KNAI, ma_vtu), "" + so_luong) != -1){
//
//                } else {
//                    Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
//                            "Không thể thêm vật tư", Color.WHITE, "OK", Color.RED);
//                }
//            } else {
//                if(connection.insertDataDuToan(MA_YCAU_KNAI, ma_dviqly, ma_vtu, ma_loai_cphi, "" + so_luong, "" + don_gia, "1", "0", "" + thanh_tien, "0", "0") != -1){
//                    // insert dữ liệu thành công
//                } else {
//                    Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
//                            "Không thể thêm vật tư", Color.WHITE, "OK", Color.RED);
//                }
//            }
//        } catch(Exception ex) {
//
//        }
//    }

    private static void resetButton(Context ctx){
        ibtCursor.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_curso));
        ibtRotate.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_rotate));
        ibtDraw.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_draw));
        ibtLine.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_line));
        ibtLine3.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_duong));
        ibtLineDash.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_dashpath));
        ibtLineCustom.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_day_cap));
//        ibtArrow.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_arrow));
        ibtRec.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_rec));
        ibtTram.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_tram));
        ibtHo.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_ho));
        ibtHop.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_hop));
        ibtCongTo.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_congto));
        ibtCiclea.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_cicle_a));
        ibtText.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_text));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try{
            if(stv.checkSave() || EsspCommon.check_save){
                if(EsspCommon.TYPE.equals("ANH")){
                    Bitmap well = stv.getBitmap();
                    if(!EsspCommon.IMAGE_NAME.equals("")){
                        String TEN_ANH = EsspCommon.IMAGE_NAME + ".jpg";
                        String pathImage = Environment.getExternalStorageDirectory() +
                                EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH;
                        Common.SaveImage(pathImage, TEN_ANH, well);
                        Toast.makeText(EsspDrawImageActivity.this, "Đã lưu ảnh!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Bitmap well = Bitmap.createBitmap(stv.getBitmap(), 0, 0, stv.getBitmap().getWidth(), (int)(stv.getBitmap().getWidth()/1.216f));
                    if(!EsspCommon.IMAGE_NAME.equals("")){
                        String TEN_ANH = EsspCommon.IMAGE_NAME + ".jpg";
                        String pathImage = Environment.getExternalStorageDirectory() +
                                EsspConstantVariables.PROGRAM_PHOTO_BV_PATH;
                        Common.SaveImage(pathImage, TEN_ANH, well);
                        Toast.makeText(EsspDrawImageActivity.this, "Đã lưu ảnh!", Toast.LENGTH_LONG).show();
                    }
                }
                EsspCommon.check_save = false;
            }
        } catch(Exception ex) {
            Toast.makeText(EsspDrawImageActivity.this, "Lỗi lưu bản vẽ", Toast.LENGTH_LONG).show();
        }

        stv.cleaner();
        EsspCommon.state_draw = 3;
        rate = 0f;
        size_line = 0f;
        pos_layer = 0;
        pos_layer_show = 0;

//        Cursor c = connection.getAllDataHinhAnhByMaHS(MA_YCAU_KNAI);
//        if(c.moveToFirst()){
//            do{
//                String TEN_ANH = c.getString(c.getColumnIndex("TEN_ANH"));
//                String pathImage = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH + TEN_ANH + ".jpg";
//                File fImage = new File(pathImage);
//                if(connection.deleteAllDataHinhAnh(MA_YCAU_KNAI) != -1) {
//                    if(fImage.exists()){
//                        fImage.delete();
//                    }
//                }
//            } while(c.moveToNext());
//        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            try{
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                photo = Common.decodeSampledBitmapFromFile(file.getAbsolutePath(), 960, 1280);

                float w = photo.getWidth();
                float h = photo.getHeight();
                if(w > h){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                }
                photo = getResizedBitmap(photo, 640, 480);
                EsspCommon.IMAGE_NAME = Common.encodeTobase64(photo);
                EsspCommon.bmMap = null;
                stv.invalidate();
                file.delete();
            } catch(Exception ex){
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static float convertpx(float px){
        float cm = 0f;
        cm = 10*px/(metrics.xdpi*25.4f);
        return cm;
    }

//    public void createDialogSelectSample(){
//        try{
//            final Dialog dialog = new Dialog(EsspDrawImageActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_select_sample);
//            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//            dialog.setCanceledOnTouchOutside(false);
//            HorizontalListView hlvListSample = (HorizontalListView)dialog.findViewById(R.id.hlvListSample);
//            ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
//
//            final ArrayList<LinkedHashMap<String, String>> arr_sample = new ArrayList<LinkedHashMap<String,String>>();
//
//            File file_sample = new File(Environment.getExternalStorageDirectory() + "/ESSP/sample");
//            String[] allFilesSample = file_sample.list();
//            for (int i = 0; i < allFilesSample.length; i++) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                String path = Environment.getExternalStorageDirectory() + "/ESSP/sample/" + allFilesSample[i];
//                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//
//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                if(allFilesSample[i].contains(".")){
//                    map.put("name", allFilesSample[i].replace(".", "//").split("//")[0].toString());
//                } else {
//                    map.put("name", allFilesSample[i]);
//                }
//                map.put("sample", Common.encodeTobase64(bitmap));
//                arr_sample.add(map);
//            }
//
//            Adapter_Sample adapter = new Adapter_Sample(Activity_Draw_New.this, R.layout.listview_sample, arr_sample);
//            hlvListSample.setAdapter(adapter);
//
//            hlvListSample.setOnItemClickListener(new OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                    map = arr_sample.get(position);
//                    stv.bm_sample = Common.decodeBase64(map.get("sample"));
//                    stv.invalidate();
//                    dialog.dismiss();
//                }
//            });
//
//            ibtDong.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        } catch(Exception ex) {
//            Common.ShowToast(Activity_Draw_New.this, "Lỗi không chọn được mẫu sơ đồ", Toast.LENGTH_LONG, size);
//        }
//    }

//    public void createDialogSample(){
//        try{
//            final Dialog dialog = new Dialog(Activity_Draw_New.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_add_sample);
//            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//            dialog.setCanceledOnTouchOutside(false);
//            final EditText etNameSample = (EditText)dialog.findViewById(R.id.etNameSample);
//            Button btnThem = (Button)dialog.findViewById(R.id.btnThem);
//            Button btnXoa = (Button)dialog.findViewById(R.id.btnXoa);
//            ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
//
//            int index = 1;
//            String name = "Sample" + index;
//            File file = new File(Environment.getExternalStorageDirectory() + "/ESSP/sample/" + name + ".jpg");
//            while(file.exists()){
//                index++;
//                name = "Sample" + index;
//                file = new File(Environment.getExternalStorageDirectory() + "/ESSP/sample/" + name + ".jpg");
//            }
//
//            etNameSample.requestFocus();
//            etNameSample.setText(name);
//            etNameSample.selectAll();
//
//            btnThem.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    try{
//                        if(stv.checkSave() || Common.check_save){
//                            Bitmap well = stv.getBitmap();
//                            Bitmap save = Bitmap.createBitmap(800, 1066, Config.ARGB_8888);
//                            Paint paint = new Paint();
//                            paint.setColor(Color.WHITE);
//                            Canvas now = new Canvas(save);
//                            now.drawRect(new Rect(0, 0, 800, 1066), paint);
//                            now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 800, 1066), null);
//
//                            if(key.equals("SO_DO")){
//                                if(!etNameSample.getText().toString().equals("")){
//                                    if(saveImageToFile(save, etNameSample.getText().toString())){
//                                        Common.ShowToast(Activity_Draw_New.this, "Bạn đã lưu mẫu sơ đồ", Toast.LENGTH_SHORT, size);
//                                        comm.LoadFolder(Activity_Draw_New.this);
//                                        dialog.dismiss();
//                                    } else {
//                                        Common.ShowToast(Activity_Draw_New.this, "Bạn chưa lưu được mẫu sơ đồ", Toast.LENGTH_SHORT, size);
//                                    }
//                                }
//                            }
//                            Common.check_save = false;
//                        }
//                    } catch(Exception ex) {
//                        Common.ShowToast(Activity_Draw_New.this, "Lỗi lưu mẫu sơ đồ", Toast.LENGTH_SHORT, size);
//                    }
//                }
//            });
//
//            btnXoa.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    etNameSample.setText("");
//                }
//            });
//
//            ibtDong.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        } catch(Exception ex) {
//            Common.ShowToast(Activity_Draw_New.this, "Lỗi khi lưu mẫu sơ đồ", Toast.LENGTH_LONG, size);
//        }
//    }

//    public static void createDialogRate(final Context context, final float eventX, final float eventY, final boolean check, final boolean check_control){
//        try{
//            final Dialog dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_add_rate);
//            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//            dialog.setCanceledOnTouchOutside(false);
//            final EditText etRate = (EditText)dialog.findViewById(R.id.etRate);
//            Button btnRate = (Button)dialog.findViewById(R.id.btnRate);
//            ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
//
//            btnRate.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    if(!etRate.getText().toString().equals("")){
//                        if(check_control){
//                            if(check){
//                                x_rate = eventX;
//                                y_rate = eventY;
//                                size_rate = Float.parseFloat(etRate.getText().toString());
//                                stv.addRate(eventX, eventY);
//                                rate = Activity_Draw_New.size_rate/convertpx(size_line);
//                                etRate2.setText("" + rate);
//                                etSize.setText(size_rate + " cm");
//                            } else {
//                                size_rate = Float.parseFloat(etRate.getText().toString());
//                                stv.addRate(x_rate, y_rate);
//                                rate = Activity_Draw_New.size_rate/convertpx(size_line);
//                                etRate2.setText("" + rate);
//                                etSize.setText(size_rate + " cm");
//                            }
//                        } else {
//                            rate = Float.parseFloat(etRate.getText().toString());
//                            etRate2.setText(etRate.getText().toString());
//                        }
//                        dialog.dismiss();
//                    }
//
//                }
//            });
//            ibtDong.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//        } catch(Exception ex) {
//            Common.ShowToast(context, "Không thêm được số đo thực tế", Toast.LENGTH_LONG, size);
//        }
//    }

    private void setColor(Dialog dialog, int status, int color, Drawable d){
        if(status == 0){
            EsspCommon.color = color;
            ibtColor.setImageDrawable(d);
            dialog.dismiss();
        } else {
            EsspCommon.color_background = color;
            stv.invalidate();
            dialog.dismiss();
        }
    }

    public void createDialogColor(final int status){
        try{
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_color);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);
            ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
            Button btnBlack = (Button)dialog.findViewById(R.id.btnBlack);
            Button btnWhite = (Button)dialog.findViewById(R.id.btnWhite);
            final Button btnGray = (Button)dialog.findViewById(R.id.btnGray);
            Button btnRed = (Button)dialog.findViewById(R.id.btnRed);
            Button btnBrown = (Button)dialog.findViewById(R.id.btnBrown);
            Button btnYellow = (Button)dialog.findViewById(R.id.btnYellow);
            Button btnDarkGreen = (Button)dialog.findViewById(R.id.btnDarkGreen);
            Button btnGreen = (Button)dialog.findViewById(R.id.btnGreen);
            Button btnBlue = (Button)dialog.findViewById(R.id.btnBlue);
            Button btnLightBlue = (Button)dialog.findViewById(R.id.btnLightBlue);
            Button btnPurple = (Button)dialog.findViewById(R.id.btnPurple);
            Button btnPink = (Button)dialog.findViewById(R.id.btnPink);

            btnBlack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.BLACK, getResources().getDrawable(R.mipmap.ic_color_black));
                }
            });

            btnWhite.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.WHITE, getResources().getDrawable(R.mipmap.ic_color_white));
                }
            });

            btnGray.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#777777"), getResources().getDrawable(R.mipmap.ic_color_gray));
                }
            });

            btnRed.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#FF0000"), getResources().getDrawable(R.mipmap.ic_color_red));
                }
            });

            btnBrown.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#440000"), getResources().getDrawable(R.mipmap.ic_color_brown));
                }
            });

            btnYellow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#FFFF00"), getResources().getDrawable(R.mipmap.ic_color_yellow));
                }
            });

            btnDarkGreen.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#003300"), getResources().getDrawable(R.mipmap.ic_color_dark_green));
                }
            });

            btnGreen.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#00CD00"), getResources().getDrawable(R.mipmap.ic_color_light_green));
                }
            });

            btnBlue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#0033FF"), getResources().getDrawable(R.mipmap.ic_color_blue));
                }
            });

            btnLightBlue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#0099FF"), getResources().getDrawable(R.mipmap.ic_color_light_blue));
                }
            });

            btnPurple.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#663399"), getResources().getDrawable(R.mipmap.ic_color_purple));
                }
            });

            btnPink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setColor(dialog, status, Color.parseColor("#FF66FF"), getResources().getDrawable(R.mipmap.ic_color_pink));
                }
            });

            ibtDong.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch(Exception ex) {
            Common.showAlertDialogGreen(this, "Lỗi", Color.RED,
                    "Không thêm được màu", Color.WHITE, "OK", Color.RED);
        }
    }

//    public void createDialogWidthBrush(){
//        try{
//            final Dialog dialog = new Dialog(this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_width_brush);
//            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//            dialog.setCanceledOnTouchOutside(false);
//            ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
//            ListView lsvWidthBrush = (ListView)dialog.findViewById(R.id.lsvWidthBrush);
//
//            ArrayList<LinkedHashMap<String, String>> listviewData = new ArrayList<LinkedHashMap<String,String>>();
//            for (int i = 0; i < arrPX.length; i++) {
//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                map.put("px", arrPX[i]);
//                map.put("impx", "" + arrIMPX[i]);
//                listviewData.add(map);
//            }
//
//            Adapter_Width_Brush adapter = new Adapter_Width_Brush(Activity_Draw_New.this, R.id.tvpx, listviewData);
//            lsvWidthBrush.setAdapter(adapter);
//
//            ibtDong.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            lsvWidthBrush.setOnItemClickListener(new OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    switch (position) {
//                        case 0:
//                            Common.width_brush = 2f;
//                            break;
//
//                        case 1:
//                            Common.width_brush = 4f;
//                            break;
//
//                        case 2:
//                            Common.width_brush = 6f;
//                            break;
//
//                        case 3:
//                            Common.width_brush = 8f;
//                            break;
//
//                        case 4:
//                            Common.width_brush = 10f;
//                            break;
//
//                        case 5:
//                            Common.width_brush = 12f;
//                            break;
//                    }
//
//                    dialog.dismiss();
//                }
//            });
//
//            dialog.show();
//        } catch(Exception ex) {
//            Common.ShowToast(this, "Không chọn được màu", Toast.LENGTH_LONG, size);
//        }
//    }

//    public void createDialogDayCap(){
//        try{
//			final Dialog dialog = new Dialog(Activity_Draw_New.this);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			dialog.setContentView(R.layout.dialog_list_daycap);
//			dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,android.app.ActionBar.LayoutParams.WRAP_CONTENT);
//			dialog.setCanceledOnTouchOutside(false);
//			ImageButton ibtDong = (ImageButton)dialog.findViewById(R.id.ibtDong);
//			ListView lvDayCap = (ListView)dialog.findViewById(R.id.lvDayCap);
//
//			arr_daycap = new ArrayList<LinkedHashMap<String,String>>();
//			Cursor c = connection.getDayCap();
//			if(c.moveToFirst()){
//				do {
//					LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//					for (int i = 0; i < Common.arrVT.length; i++) {
//						map.put(Common.arrVT[i], c.getString(i));
//					}
//					arr_daycap.add(map);
//				} while (c.moveToNext());
//			}
//			Adapter_Day_Cap adapter_dc = new Adapter_Day_Cap(Activity_Draw_New.this, R.id.tvSTT, arr_daycap);
//			lvDayCap.setAdapter(adapter_dc);
//			lvDayCap.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					Common.map = new LinkedHashMap<String, String>();
//					Common.map = arr_daycap.get(position);
//					pos_map = position;
//					dialog.dismiss();
//					resetButton(Activity_Draw_New.this);
//					ibtDayCap.setImageDrawable(getResources().getDrawable(R.drawable.ic_day_cap2));
//					Common.state_draw = 25;
//				}
//			});
//
//			ibtDong.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			dialog.show();
//        } catch(Exception ex) {
//            Common.ShowToast(Activity_Draw_New.this, "Không thêm được chữ", Toast.LENGTH_LONG, size);
//        }
//    }

    public static void createDialogGhiChu(final Context ctx, final int check){
        try{
            final Dialog dialog = new Dialog(ctx);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_note);
            dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            final EditText etGhiChu = (EditText)dialog.findViewById(R.id.essp_dialog_note_et_ghichu);
            Button btnLuu = (Button)dialog.findViewById(R.id.essp_dialog_note_bt_luu);

            if(check == 0){
                etGhiChu.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }

            btnLuu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(!etGhiChu.getText().toString().equals("")){
                        stv.isChangeLayer = false;
                        stv.arr = new ArrayList<LinkedHashMap<String,Float>>();
                        stv.isMove = false;
                        resetButton(ctx);
                        EsspCommon.angle = 0;
                        ibtText.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_text2));

                        if(check != 0){
                            text = etGhiChu.getText().toString();
                            EsspCommon.text = etGhiChu.getText().toString();
                            EsspCommon.text2 = etGhiChu.getText().toString();
                        } else {
                            text = etGhiChu.getText().toString();
                            EsspCommon.text = "+" + etGhiChu.getText().toString();
                            EsspCommon.text2 = "+" + etGhiChu.getText().toString();
                        }

                        dialog.dismiss();
                        EsspCommon.state_draw = 10;
                        stv.invalidate();
                    }
                }
            });

            dialog.show();
        } catch(Exception ex) {
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED,
                    "Không thêm được chữ", Color.WHITE, "OK", Color.RED);
        }
    }

    @Override
    protected void onResume() {
        try{
            super.onResume();
            stv.invalidate();
//            Common.clearApplicationData(this);
        } catch(Exception ex) {
            ex.toString();
        }
    }

    @Override
    protected void onDestroy() {
        try{
            super.onDestroy();
            EsspCommon.IMAGE_NAME = "";
        } catch(Exception ex) {
            ex.toString();
        }
    }

}
