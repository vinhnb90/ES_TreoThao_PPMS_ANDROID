package com.es.tungnv.draws;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;
import com.es.tungnv.views.EsspDrawImageActivity;
import com.es.tungnv.views.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SingleTouchViewNew extends SurfaceView {

	float X;
	float Y;
	float X_UP;
	float Y_UP;
	String text = "";
	public byte[] so_do;
	public ArrayList<Path> paths = new ArrayList<Path>();
	public ArrayList<Path> undonePaths = new ArrayList<Path>();
	public ArrayList<Paint> paints = new ArrayList<Paint>();
	public ArrayList<Paint> undonepaints = new ArrayList<Paint>();
	public ArrayList<Paint> paints_text = new ArrayList<Paint>();
	public ArrayList<Paint> undonepaints_text = new ArrayList<Paint>();
	public ArrayList<Integer> state_undos = new ArrayList<Integer>();
	public ArrayList<Integer> state_redos = new ArrayList<Integer>();
	public ArrayList<String> string_text = new ArrayList<String>();
	public ArrayList<String> undo_text = new ArrayList<String>();
	public ArrayList<Float> pos_x_text = new ArrayList<Float>();
	public ArrayList<Float> pos_y_text = new ArrayList<Float>();
	public ArrayList<Float> pos_x_text_rotate = new ArrayList<Float>();
	public ArrayList<Float> pos_y_text_rotate = new ArrayList<Float>();
	public ArrayList<Float> undo_pos_x_text = new ArrayList<Float>();
	public ArrayList<Float> undo_pos_y_text = new ArrayList<Float>();
	public ArrayList<Integer> angles = new ArrayList<Integer>();
	public ArrayList<Integer> undo_angles = new ArrayList<Integer>();
	public ArrayList<ArrayList<LinkedHashMap<String, Float>>> arr_pixel = new ArrayList<ArrayList<LinkedHashMap<String, Float>>>();
	public ArrayList<LinkedHashMap<String, Float>> arr_mimax_rec = new ArrayList<LinkedHashMap<String,Float>>();
	public ArrayList<LinkedHashMap<String, Float>> arr_pos_center = new ArrayList<LinkedHashMap<String, Float>>();
	public ArrayList<LinkedHashMap<String, Float>> arr_pos_center_rotate = new ArrayList<LinkedHashMap<String, Float>>();
	public ArrayList<ArrayList<LinkedHashMap<String, Float>>> arr_snap_point = new ArrayList<ArrayList<LinkedHashMap<String, Float>>>();
	public ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
	public ArrayList<Integer> arr_image = new ArrayList<Integer>();
	public ArrayList<Integer> arr_type = new ArrayList<Integer>();
	public ArrayList<Integer> path_virtual = new ArrayList<Integer>();
	public ArrayList<Integer> pos_text = new ArrayList<Integer>();
	public ArrayList<Integer> angle_text = new ArrayList<Integer>();
	public ArrayList<Double> angles_rotate = new ArrayList<Double>();
	public ArrayList<Integer> arr_check_rate = new ArrayList<Integer>();
	public ArrayList<Boolean> arr_check_rate_bool = new ArrayList<Boolean>();
	public ArrayList<LinkedHashMap<String, Float>> arr = new ArrayList<LinkedHashMap<String, Float>>();
	
	public float x_move_obj = 0f;
	public float y_move_obj = 0f;
	
	// Lưu ảnh
	public Paint paint = new Paint();
	
	// vẽ nét tự do
	public Paint paint_brush = new Paint();
	public Paint paint_brush1 = new Paint();
	public Paint paint_brush2 = new Paint();
	public Paint paint_brush3 = new Paint();
	public Paint paint_brush4 = new Paint();
	public Paint paint_rec_select = new Paint();
	public Paint paint_rec_select2 = new Paint();
	public Path path_brush = new Path();
	private Path path_brush_show = new Path();
	// tẩy
	private Paint paint_Erasers = new Paint();
	private Path path_Erasers = new Path();
	private Path path_Erasers_show = new Path();
	// vẽ nét thẳng
//	private Paint paint_Line = new Paint();
	private Path path_Line = new Path();
	private Path path_Line_show = new Path();
	private Path path_pash_show = new Path();
	private Path path_arrow = new Path();
	private Path path_Rec_Select = new Path();
	private Path path_Rec_Select2 = new Path();
	// Vẽ hình tròn
//	private Paint paint_Cicle_line = new Paint();
	private Path path_Cicle_Line = new Path();
	// Vẽ hình chữ nhật
//	private Paint paint_rec = new Paint();
	private Path path_rec = new Path();
	private Path path_ho = new Path();
	private Path path_rec_show = new Path();
	private Path path_rec_show1 = new Path();
	// Vẽ hình tròn rỗng
//	private Paint paint_cicle = new Paint();
	private Path path_cicle = new Path();
	private Path path_cicle_show = new Path();
	private Paint paint_cicle_dot = new Paint();
	private Path path_cicle_dot = new Path();
	private Path path_cicle_dot_show = new Path();
	// Vẽ dấu chấm
//	private Paint paint_dot = new Paint();
	private Path path_dot = new Path();
	// Vẽ chữ
//	private Paint paint_text = new Paint();
	
	public Bitmap mBitmap, bm, bm_sample;
	public Canvas mCanvas;
	private Paint mBitmapPaint;
	
	public Boolean clearCanvas = false;
	
	// Vẽ trạm
	Paint paint_rec_show2 = new Paint();
	Path path_rec_show2 = new Path();
	// Vẽ công tơ
	Path path_ct_show = new Path();
	// Kiểm tra kiểu nét vẽ tự do hay thẳng
	boolean check_line = false;
	float Xtam = 0f;
	float Ytam = 0f;
	
	String text_rate = "";
	float x_rate = 0f;
	float y_rate = 0f;
	Paint paint_text = new Paint();
	float real_size = 0f;
	float size_line = 0f;

//	static Path path_brush2 = new Path(), path_Erasers2 = new Path(), path_Line2 = new Path(), path_Cicle_Line2 = new Path(),
//			path_rec2 = new Path(), path_cicle2 = new Path(), path_cicle_dot2 = new Path(), path_dot2 = new Path();
//	static ArrayList<Path> paths2 = new ArrayList<Path>(), undonePaths2 = new ArrayList<Path>();
//	static ArrayList<Paint> paints2 = new ArrayList<Paint>(), undonepaints2 = new ArrayList<Paint>();
//	static ArrayList<Integer> state_undos2 = new ArrayList<Integer>(), state_redos2 = new ArrayList<Integer>(), 
//			angles2 = new ArrayList<Integer>(), undo_angles2 = new ArrayList<Integer>();
//	static String SD;
	boolean check_undo = false;
	boolean check_redo = false;
	String text_show = "";
	float x_show, y_show;
	int dem = 1;
	int dem_tam = 1;
	ArrayList<Float> arr_x_show = new ArrayList<Float>();
	ArrayList<Float> arr_y_show = new ArrayList<Float>();
	float x_m2 = 0f;
	float y_m2 = 0f;
	
	public boolean isMove = false;
	public boolean isRotate = false;
	public boolean isChangeLayer = false;

	public SingleTouchViewNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(Color.BLACK);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		paint_text.setFakeBoldText(true);

		if(EsspCommon.TYPE.equals("ANH") && !EsspCommon.IMAGE_NAME.isEmpty()){
			String path = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH)
					.append(EsspCommon.IMAGE_NAME).append(".jpg").toString();
			File fImage = new File(path);
			if(fImage.exists()) {
				bm = Common.scaleDown(BitmapFactory.decodeFile(path), 1200, false);
//				bm = BitmapFactory.decodeFile(path);
				if (bm.getWidth() > bm.getHeight()) {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
				}
			}
		}

		if(EsspCommon.TYPE.equals("BAN_VE") && !EsspCommon.IMAGE_NAME.isEmpty()){
			String path = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(EsspConstantVariables.PROGRAM_PHOTO_BV_PATH)
					.append(EsspCommon.IMAGE_NAME).append(".jpg").toString();
			File fImage = new File(path);
			if(fImage.exists()) {
				bm = Common.scaleDown(BitmapFactory.decodeFile(path), 1200, false);
//				if (bm.getWidth() > bm.getHeight()) {
//					Matrix matrix = new Matrix();
//					matrix.postRotate(90);
//					bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//				}
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}
	
	private float x_move = 0f;
	private float y_move = 0f;
	private float width_scale = 1f;
	private float height_scale = 1f;
	public float width;
	public float height;
	public Float xr1;
	public Float xr2;
	public Float yr1;
	public Float yr2;
	Matrix m = new Matrix();
	double goc_bt = 0d;
	
	float x_snap = 0;
	float y_snap = 0;
	private double dis_snap = 50;
	private double dis_min = 1000;
	double goc_cu = 0;
	float xd;
	float yd;
	float xc;
	float yc;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		try{
			canvas.translate(x_move, y_move);
		    canvas.scale(width_scale, height_scale);
		    width = canvas.getWidth();
		    height = canvas.getHeight();

			Paint paint2 = new Paint();
			paint2.setStyle(Style.FILL);
			paint2.setColor(EsspCommon.color_background);
			canvas.drawPaint(paint2);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			// Tạo paint cho text hiển thị số đo độ dài đường dây khi vẽ hình ảnh capture từ map
			Paint paint_text2 = new Paint();
			paint_text2.setAntiAlias(true);
			paint_text2.setStrokeWidth(EsspCommon.width_brush);
			paint_text2.setColor(Color.BLUE);
			paint_text2.setStrokeJoin(Paint.Join.ROUND);
			paint_text2.setStyle(Style.FILL);
			paint_text2.setTextSize(getWidth()/55);
			// -----------------Vẽ hình ảnh chụp lên canvas
			float tu = (float)bm.getWidth();
			float mau = (float)bm.getHeight();
			float tg = tu / mau;
			float height = ((float)getWidth()/tg)+((float)getWidth()*20/100);
			if(!EsspCommon.IMAGE_NAME.isEmpty()){
				canvas.drawBitmap(bm, null, new Rect(0, 0, getWidth(), bm.getHeight()*getWidth()/bm.getWidth()), paint);
				EsspCommon.check_save = true;
				if(EsspCommon.TYPE.equals("BAN_VE")){
					// Fill tên KH
					Rect rectName = new Rect();
					paint.getTextBounds(EsspCommon.TEN_KH, 0, EsspCommon.TEN_KH.length(), rectName);
					StringBuilder[] stringBuilderName = Common.splitMultiText(EsspCommon.TEN_KH, 3*EsspDrawImageActivity.width/5, rectName.width());
					for (int k = 0; k < stringBuilderName.length; k++) {
						Rect rectName2 = new Rect();
						paint.getTextBounds(stringBuilderName[k].toString(), 0, stringBuilderName[k].toString().length(), rectName2);
						canvas.drawText(stringBuilderName[k].toString(), EsspDrawImageActivity.width/6,
								height/8 + k * rectName.height(), paint_text2);
					}
//					canvas.drawText(EsspCommon.TEN_KH, getWidth()/2 - rectName.width()/2, height/8, paint_text2);

					// Fill trạm
					Rect rectTram = new Rect();
					paint.getTextBounds(EsspCommon.TRAM, 0, EsspCommon.TRAM.length(), rectTram);
					float xTram = 0f, yTram = 0f;

					// Fill cáp sau
					Rect rectCapSau = new Rect();
					paint.getTextBounds(("Cáp sau " + EsspCommon.CAP_SAU), 0, ("Cáp sau " + EsspCommon.CAP_SAU).length(), rectCapSau);
					float yCapSau1 = 0f;

					// Fill dia chi
					Rect rectDiaChi = new Rect();
					paint.getTextBounds(EsspCommon.DIA_CHI, 0, EsspCommon.DIA_CHI.length(), rectDiaChi);
					StringBuilder[] stringBuilderDC = Common.splitMultiText(EsspCommon.DIA_CHI, 2 * EsspDrawImageActivity.width/3, rectDiaChi.width());
					float yDiaChi = 0f;

					// Fill cáp trước 2
					Rect rectTitCapTruoc2 = new Rect();
					paint.getTextBounds("Cáp vào công tơ", 0, "Cáp vào công tơ".length(), rectTitCapTruoc2);
					float xTitCapTruoc2 = 0f, yTitCapTruoc2 = 0f;

					Rect rectCapTruoc2 = new Rect();
					paint.getTextBounds(EsspCommon.CAP_TRUOC, 0, EsspCommon.CAP_TRUOC.length(), rectCapTruoc2);
					float xCapTruoc2 = 0f, yCapTruoc2 = 0f;

					// Fill cáp sau 2
					Rect rectTitCapSau2 = new Rect();
					paint.getTextBounds("Cáp sau công tơ", 0, "Cáp sau công tơ".length(), rectTitCapSau2);
					float xTitCapSau2 = 0f, yTitCapSau2 = 0f;

					Rect rectCapSau2 = new Rect();
					paint.getTextBounds(EsspCommon.CAP_SAU, 0, EsspCommon.CAP_SAU.length(), rectCapSau2);
					float xCapSau2 = 0f, yCapSau2 = 0f;

					switch (EsspCommon.LOAI_BV){
						case 0://Treo cột hòm 1
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = 2.85f * getWidth() / 4;
							yTitCapTruoc2 = height / 2.8f;
							xTitCapSau2 = getWidth() / 4;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = 2.85f * getWidth() / 4;
							yCapTruoc2 = height / 2.6f;
							xCapSau2 = getWidth() / 4;
							yCapSau2 = height / 2.3f;
							break;
						case 1://Treo cột hòm 1 có Ti
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = 2.85f * getWidth() / 4;
							yTitCapTruoc2 = height / 2.8f;
							xTitCapSau2 = getWidth() / 4;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = 2.85f * getWidth() / 4;
							yCapTruoc2 = height / 2.6f;
							xCapSau2 = getWidth() / 4;
							yCapSau2 = height / 2.3f;
							break;
						case 2://Treo cột hòm 2 có sẵn
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 3://Treo cột hòm 2 lắp mới
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 4://Treo cột hòm 4 có sẵn
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 5://Treo cột hòm 4 lắp mới
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height / 4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 6://Treo cột hòm 6 có sẵn
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height/4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 7://Treo cột hòm 6 lắp mới
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height/4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = getWidth() / 3.6f;
							yTitCapTruoc2 = height / 2.5f;
							xTitCapSau2 = 2.05f * getWidth() / 3;
							yTitCapSau2 = height / 2.5f;
							xCapTruoc2 = getWidth() / 3.6f;
							yCapTruoc2 = height / 2.3f;
							xCapSau2 = 2.05f * getWidth() / 3;
							yCapSau2 = height / 2.3f;
							break;
						case 8://Treo tường 1 pha
							xTram = getWidth() - rectTram.width() - 50;
							yTram = height/3.9f;
							yCapSau1 = height / 5;
							yDiaChi = height / 3.7f;
							xTitCapTruoc2 = 5.1f * getWidth() / 8;
							yTitCapTruoc2 = height / 2.2f;
							xTitCapSau2 = getWidth() / 4;
							yTitCapSau2 = height / 2.35f;
							xCapTruoc2 = 5.1f * getWidth() / 8;
							yCapTruoc2 = height / 2.08f;
							xCapSau2 = getWidth() / 4;
							yCapSau2 = height / 2.2f;
							break;
						case 9://Treo tường 3 pha
							xTram = getWidth() - rectTram.width() - 50;
							yTram = height/3.9f;
							yCapSau1 = height / 5;
							yDiaChi = height / 3.7f;
							xTitCapTruoc2 = 5.1f * getWidth() / 8;
							yTitCapTruoc2 = height / 2.2f;
							xTitCapSau2 = getWidth() / 4;
							yTitCapSau2 = height / 2.35f;
							xCapTruoc2 = 5.1f * getWidth() / 8;
							yCapTruoc2 = height / 2.08f;
							xCapSau2 = getWidth() / 4;
							yCapSau2 = height / 2.2f;
							break;
						case 10://Hạ ngầm
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height/4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapSau2 = 7 * getWidth() / 12;
							yTitCapSau2 = 23.4f * height / 32;
							xCapSau2 = 7 * getWidth() / 12;
							yCapSau2 = 24.3f * height / 32;
							break;
						case 11://Hạ ngầm treo tường
							xTram = getWidth() - rectTram.width() - 200;
							yTram = height/4.8f;
							yCapSau1 = height / 4;
							yDiaChi = height / 3.4f;
							xTitCapTruoc2 = 1.15f * getWidth() / 4;
							yTitCapTruoc2 = 23f * height / 32;
							xTitCapSau2 = 10;
							yTitCapSau2 = height / 2.75f;
							xCapTruoc2 = 1.15f * getWidth() / 4;
							yCapTruoc2 = 24f * height / 32;
							xCapSau2 = 10;
							yCapSau2 = height / 2.55f;
							break;
					}

					// Fill trạm
					if(EsspCommon.LOAI_BV == 8 || EsspCommon.LOAI_BV == 9){
						if(rectTram.width() < (float) canvas.getWidth() / 7.5f){
							canvas.drawText(EsspCommon.TRAM, xTram, yTram, paint_text2);
						} else {
							StringBuilder s1 = new StringBuilder();
							StringBuilder s2 = new StringBuilder();
							Common.splitText(EsspCommon.TRAM, s1, s2, (float) canvas.getWidth() / 7.5f);

							canvas.drawText(s1.toString(), xTram, yTram, paint_text2);
							canvas.drawText(s2.toString(), xTram, (int)(yTram + 1.5*rectTram.height()), paint_text2);
						}
					} else{
						canvas.drawText(EsspCommon.TRAM, xTram, yTram, paint_text2);
					}
					// Fill Cap sau 1
					canvas.drawText(("Cáp sau " + EsspCommon.CAP_SAU), 10, yCapSau1, paint_text2);
					// Fill dia chi
					for (int k = 0; k < stringBuilderDC.length; k++) {
						canvas.drawText(stringBuilderDC[k].toString(), getWidth()/8,
								yDiaChi + k * rectDiaChi.height(), paint_text2);
					}
//					canvas.drawText(EsspCommon.DIA_CHI, getWidth()/2 - rectDiaChi.width()/2 - 100, yDiaChi, paint_text2);
					// Fill cap trước 2
					if(EsspCommon.LOAI_BV != 10) {
						canvas.drawText("Cáp vào công tơ", xTitCapTruoc2, yTitCapTruoc2, paint_text2);
						yCapTruoc2 = (int)(yTitCapTruoc2 + 1.5*rectCapSau2.height());
						if(rectCapSau2.width() < (float) canvas.getWidth() / 5.5f){
							canvas.drawText(EsspCommon.CAP_TRUOC, xCapTruoc2, yCapTruoc2, paint_text2);
						} else {
							StringBuilder s1 = new StringBuilder();
							StringBuilder s2 = new StringBuilder();
							Common.splitText(EsspCommon.CAP_TRUOC, s1, s2, (float) canvas.getWidth() / 5.5f);

							canvas.drawText(s1.toString(), xCapTruoc2, yCapTruoc2, paint_text2);
							canvas.drawText(s2.toString(), xCapTruoc2, (int)(yCapTruoc2 + 1.5*rectCapSau2.height()), paint_text2);
						}
					}
					// Fill cap sau 2
					canvas.drawText("Cáp sau công tơ", xTitCapSau2, yTitCapSau2, paint_text2);
					yCapSau2 = (int)(yTitCapSau2 + 1.5*rectCapTruoc2.height());
					if(rectCapTruoc2.width() < (float) canvas.getWidth() / 5.5f) {
						canvas.drawText(EsspCommon.CAP_SAU, xCapSau2, yCapSau2, paint_text2);
					} else {
						StringBuilder s1 = new StringBuilder();
						StringBuilder s2 = new StringBuilder();
						Common.splitText(EsspCommon.CAP_SAU, s1, s2, (float) canvas.getWidth() / 5.5f);

						canvas.drawText(s1.toString(), xCapSau2, yCapSau2, paint_text2);
						canvas.drawText(s2.toString(), xCapSau2, (int)(yCapSau2 + 1.5*rectCapTruoc2.height()), paint_text2);
					}
				} else {

				}
			}

			// ---------------------Vẽ hình ảnh capture từ bản đồ lên canvas
//			if(Common.bmMap != null){
//				bm = Common.bmMap;
//				if(bm.getWidth() > bm.getHeight()){
//					Matrix matrix = new Matrix();
//					matrix.postRotate(-90);
//					bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//				}
//				canvas.drawBitmap(bm, null, new Rect(0, 0, EsspDrawImageActivity.width, EsspDrawImageActivity.height), paint);
//				canvas.drawText(Common.DoDai, 30, 100, paint_text2);
//				Common.check_save = true;
//			}
			// Vòng lặp vẽ các đối tượng lên canvas
			for (int i = 0; i < paths.size(); i++) {
				if(paths.get(i) != null){
					canvas.save();
					canvas.rotate((float)(angles_rotate.get(i)*1.0f), arr_pos_center_rotate.get(i).get("x"), arr_pos_center_rotate.get(i).get("y"));
					canvas.drawPath(paths.get(i), paints.get(i));
					canvas.restore();
				} else {
					for (int j = 0; j < string_text.size(); j++) {
						canvas.save();
						Rect rect = new Rect();
					    paint.getTextBounds(string_text.get(j), 0, string_text.get(j).length(), rect);
					    canvas.rotate(angles.get(j), pos_x_text_rotate.get(j), pos_y_text_rotate.get(j));
						canvas.drawText(string_text.get(j), pos_x_text.get(j) - rect.exactCenterX(), pos_y_text.get(j) + rect.exactCenterY(), paints_text.get(j));
						canvas.restore();
					}
				}
	        }
			// Vẽ các đối tượng hiển thị tạm thời khi di chuyển trên màn hình
			canvas.drawPath(path_brush_show, paint_brush);
			canvas.drawPath(path_Line_show, paint_brush);
			canvas.drawPath(path_rec_show, paint_brush);
			canvas.drawPath(path_arrow, paint_brush4);
			canvas.drawPath(path_rec_show2, paint_rec_show2);
			canvas.drawPath(path_pash_show, paint_brush3);
			canvas.drawPath(path_ct_show, paint_brush2);
			canvas.drawPath(path_cicle_show, paint_brush);
			canvas.drawPath(path_cicle_dot_show, paint_cicle_dot);
			canvas.drawPath(path_Erasers_show, paint_Erasers);
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			canvas.drawText(text_rate, x_rate, y_rate, paint_text);
			if(!isMove){
				canvas.drawText(text_show, x_show, y_show, paint_text);
			}
			canvas.drawPath(path_rec_show1, paint_brush1);
			if(isMove){
				int pos_layer = 0;
				for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
					pos_layer += path_virtual.get(i);
				}
				canvas.save();
				canvas.rotate((float)(angles_rotate.get(pos_layer)*1.0f), arr_pos_center_rotate.get(pos_layer).get("x"), arr_pos_center_rotate.get(pos_layer).get("y"));
				canvas.drawPath(path_Rec_Select, paint_rec_select);
				canvas.drawPath(path_Rec_Select2, paint_rec_select2);
				canvas.restore();
			}
		} catch(Exception ex) {
			ex.toString();
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

	}

	public Bitmap getBitmap() {
		this.setDrawingCacheEnabled(true);
		this.buildDrawingCache();
		Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
		this.setDrawingCacheEnabled(false);
		return bmp;
	}

	public void clear() {
		mBitmap.eraseColor(Color.GREEN);
		invalidate();
		System.gc();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();
		undonePaths.clear();
		switch (EsspCommon.state_draw) {
		case 1:
			drawPoint(event, eventX, eventY);
			break;
			
		case 2:
			drawErasers(event, eventX, eventY);
			break;

		case 3:
			drawBrush(event, eventX, eventY);
			break;
			
		case 4:
			drawLine(event, eventX, eventY);
			break;
			
		case 5:
			drawLineb(event, eventX, eventY);
			break;
			
		case 6:
			drawRec(event, eventX, eventY);
			break;
			
		case 7:
			drawCicle(event, eventX, eventY);
			break;
			
		case 8:
			drawCicleB(event, eventX, eventY);
			break;
			
		case 9:
			drawDot(event, eventX, eventY);
			break;
			
		case 10:
			drawText(event, eventX, eventY);
			break;
			
		case 11:
			drawText(event, eventX, eventY);
			break;
			
		case 12:
			drawText(event, eventX, eventY);
			break;
			
		case 13:
			drawText(event, eventX, eventY);
			break;
			
		case 14:
			drawHo(event, eventX, eventY);
			break;
			
		case 15:
			drawTram(event, eventX, eventY);
			break;
			
		case 16:
			drawHop(event, eventX, eventY);
			break;
			
		case 17:
			drawCongTo(event, eventX, eventY);
			break;
			
		case 18:
			drawDuong(event, eventX, eventY);
			break;
			
		case 19:
			drawDash(event, eventX, eventY);
			break;
			
		case 20:
//			drawLineCustom(event, eventX, eventY);
			drawLineSmooth(event, eventX, eventY);
			break;
			
		case 21:
//			drawArrow(event, eventX, eventY);
			break;
			
		case 22:
			Hand(event, eventX, eventY);
			break;
			
		case 23:
			ZoomIn(event, eventX, eventY);
			break;
			
		case 24:
			ZoomOut(event, eventX, eventY);
			break;
			
		case 25:
			drawDayCap(event, eventX, eventY);
			break;
		}
		
		check_redo = false;
		invalidate();
		return true;
	}

	/** Xóa trắng màn hình
	 * 
	 */
	public void cleaner(){
		check_undo = true;
		
		undonePaths.clear();
		undonepaints.clear();
		undo_angles.clear();
		if(EsspDrawImageActivity.TYPE != null)
			if(EsspDrawImageActivity.TYPE.equals("ANH"))
				EsspCommon.IMAGE_NAME = "";
		EsspCommon.bmMap = null;
		while (paths.size() > 0) {
			for (int i = 0; i < state_undos.get(state_undos.size() - 1); i++) {
        		if(paths.get(paths.size()-1) == null){
        			undo_text.add(string_text.remove(string_text.size()-1));
        			undo_pos_x_text.add(pos_x_text.remove(pos_x_text.size()-1));
        			undo_pos_y_text.add(pos_y_text.remove(pos_y_text.size()-1));
        			undonepaints_text.add(paints_text.remove(paints_text.size()-1));
        			undo_angles.add(angles.remove(angles.size()-1));
        		}
        		undonePaths.add(paths.remove(paths.size()-1));
        		undonepaints.add(paints.remove(paints.size()-1));
			}
        	state_redos.add(state_undos.remove(state_undos.size() - 1));
		}
		check_line = false;
		invalidate();
	}
	
	public void undo () { 
		try{
			if(check_undo){
				check_undo = false;
				check_redo = true;
				while (undonePaths.size() > 0) {
					for (int i = 0; i < state_redos.get(state_redos.size() - 1); i++) {
		        		if(undonePaths.get(undonePaths.size()-1) == null){
		        			string_text.add(undo_text.remove(undo_text.size()-1));
		        			pos_x_text.add(undo_pos_x_text.remove(undo_pos_x_text.size()-1));
		        			pos_y_text.add(undo_pos_y_text.remove(undo_pos_y_text.size()-1));
		        			paints_text.add(undonepaints_text.remove(undonepaints_text.size()-1));
		        			angles.add(undo_angles.remove(undo_angles.size()-1));
		        		}
		        		paths.add(undonePaths.remove(undonePaths.size()-1));
		        		paints.add(undonepaints.remove(undonepaints.size()-1));
		        	}
		            invalidate();
		            state_undos.add(state_redos.remove(state_redos.size() - 1));
				}
			} else {
		        if (paths.size()>0) { 
		        	for (int i = 0; i < state_undos.get(state_undos.size() - 1); i++) {
		        		if(paths.get(paths.size()-1) == null){
		        			undo_text.add(string_text.remove(string_text.size()-1));
		        			undo_pos_x_text.add(pos_x_text.remove(pos_x_text.size()-1));
		        			undo_pos_y_text.add(pos_y_text.remove(pos_y_text.size()-1));
		        			undonepaints_text.add(paints_text.remove(paints_text.size()-1));
		        			undo_angles.add(angles.remove(angles.size()-1));
		        		}
		        		undonePaths.add(paths.remove(paths.size()-1));
		        		undonepaints.add(paints.remove(paints.size()-1));
					}
		        	invalidate();
		        	state_redos.add(state_undos.remove(state_undos.size() - 1));
		        } else {
		
		        }
			}
			dem_tam = dem;
			dem = 0;
		} catch(Exception ex) {
			ex.toString();
		}
    }
	
	public void redo (){
		try{
			if(check_redo){
				cleaner();
				check_redo = false;
				check_undo = true;
			} else {
		        if (undonePaths.size()>0) { 
		        	for (int i = 0; i < state_redos.get(state_redos.size() - 1); i++) {
		        		if(undonePaths.get(undonePaths.size()-1) == null){
		        			string_text.add(undo_text.remove(undo_text.size()-1));
		        			pos_x_text.add(undo_pos_x_text.remove(undo_pos_x_text.size()-1));
		        			pos_y_text.add(undo_pos_y_text.remove(undo_pos_y_text.size()-1));
		        			paints_text.add(undonepaints_text.remove(undonepaints_text.size()-1));
		        			angles.add(undo_angles.remove(undo_angles.size()-1));
		        		}
		        		paths.add(undonePaths.remove(undonePaths.size()-1));
		        		paints.add(undonepaints.remove(undonepaints.size()-1));
		        	}
		            invalidate();
		            state_undos.add(state_redos.remove(state_redos.size() - 1));
		        } else {
		
		        }
			}
			dem = dem_tam;
			dem_tam = 0;
		} catch(Exception ex) {
			
		}
    }
	
	/** Kiểm tra trên màn hình đã có nét vẽ nào chưa, nếu có mới cho lưu
	 * @return
	 */
	public boolean checkSave(){
		if(paths.size() == 0){
			return false;
		} else {
			return true;
		}
	}
	
	/** Con trỏ chuột
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawPoint(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush1.setAntiAlias(true);
		paint_brush1.setStrokeWidth(2f);
		paint_brush1.setColor(Color.BLACK);
		paint_brush1.setStyle(Style.STROKE);
		paint_brush1.setStrokeJoin(Paint.Join.ROUND);
		paint_brush1.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = (event.getX() - x_m2)/width_scale;
			Y = (event.getY() - y_m2)/height_scale;
			return true;
		case MotionEvent.ACTION_MOVE:
			path_rec_show1 = new Path();
			path_rec_show1.moveTo(X, Y);
			path_rec_show1.lineTo((eventX - x_m2)/width_scale, Y);
			path_rec_show1.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			path_rec_show1.lineTo(X, (eventY - y_m2)/height_scale);
			path_rec_show1.lineTo(X, Y);
			break;
		case MotionEvent.ACTION_UP:
			path_rec_show1 = new Path();
			invalidate();
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Tẩy
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawErasers(MotionEvent event, float eventX, float eventY){
		paint_Erasers.setAntiAlias(true);
		paint_Erasers.setStrokeWidth(30f);
		paint_Erasers.setColor(Color.WHITE);
		paint_Erasers.setStyle(Style.STROKE);
		paint_Erasers.setStrokeJoin(Paint.Join.ROUND);
		
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Erasers = new Paint();
		paint_Erasers.setAntiAlias(true);
		paint_Erasers.setStrokeWidth(30f);
		paint_Erasers.setColor(Color.WHITE);
		paint_Erasers.setStyle(Style.STROKE);
		paint_Erasers.setStrokeJoin(Paint.Join.ROUND);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path_Erasers_show.moveTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			path_Erasers.moveTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			return true;
		case MotionEvent.ACTION_MOVE:
			path_Erasers.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			path_Erasers_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			break;
		case MotionEvent.ACTION_UP:
			path_Erasers_show = new Path();
			paths.add(path_Erasers);
			paints.add(paint_Erasers);
			path_Erasers = new Path();
			state_undos.add(1);
			break;
		default:
			return false;
		}
		return true;
	}
	
	public void ZoomIn(){
		if(width_scale < 2 && height_scale < 2){
			width_scale+=0.25;
			height_scale+=0.25;
			
			if(x_move > 0)
				x_move = 0;
			if(x_move < width - width * width_scale)
				x_move = width - width * width_scale;
			if(y_move > 0)
				y_move = 0;
			if(y_move < height - height * height_scale)
				y_move = height - height * height_scale;
			
			x_m2 = x_move;
			y_m2 = y_move;
		}
	}
	
	private boolean ZoomIn(MotionEvent event, float eventX, float eventY){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(width_scale < 2 && height_scale < 2){
				width_scale+=0.25;
				height_scale+=0.25;
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}
	
	public void ZoomOut(){
		if(width_scale > 1 && height_scale > 1){
			width_scale-=0.25;
			height_scale-=0.25;
			
			if(x_move > 0)
				x_move = 0;
			if(x_move < width - width * width_scale)
				x_move = width - width * width_scale;
			if(y_move > 0)
				y_move = 0;
			if(y_move < height - height * height_scale)
				y_move = height - height * height_scale;
			
			x_m2 = x_move;
			y_m2 = y_move;
		}
	}
	
	private boolean ZoomOut(MotionEvent event, float eventX, float eventY){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(width_scale > 1 && height_scale > 1){
				width_scale-=0.25;
				height_scale-=0.25;
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}
	
	/** chuyển con trỏ chuột sang dạng di chuyển ảnh khi phóng to ảnh
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean Hand(MotionEvent event, float eventX, float eventY){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = event.getX();
			Y = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			float x_m = 0f;
			float y_m = 0f;
			x_m = eventX - X;
			y_m = eventY - Y;
			x_move = x_m + x_m2;
			y_move = y_m + y_m2;
			
			if(x_move > 0)
				x_move = 0;
			if(x_move < width - width * width_scale)
				x_move = width - width * width_scale;
			if(y_move > 0)
				y_move = 0;
			if(y_move < height - height * height_scale)
				y_move = height - height * height_scale;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			x_m2 = x_move;
			y_m2 = y_move;
			break;
		default:
			return false;
		}
		return true;
	}
	
	private double disSnapPoint(float x1, float y1, float x2, float y2){
		try{
			double d = Math.sqrt((x2 - x1)*(x2 - x1)+(y2 - y1)*(y2 - y1));
			return d;
		} catch(Exception ex) {
			return 1000;
		}
	}
	
	/** Vẽ nét tự do
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawBrush(MotionEvent event, float eventX, float eventY){
		try{
			mCanvas = new Canvas();
			paint_brush.setAntiAlias(true);
			paint_brush.setStrokeWidth(EsspCommon.width_brush);
			paint_brush.setColor(EsspCommon.color);
			paint_brush.setStyle(Style.STROKE);
			paint_brush.setStrokeJoin(Paint.Join.ROUND);
			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(EsspCommon.width_brush);
			paint.setColor(EsspCommon.color);
			paint.setStyle(Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			
			if(!isMove){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					X = (event.getX() - x_m2)/width_scale;
					Y = (event.getY() - y_m2)/height_scale;
					dis_min = 1000;
					
					if(arr_snap_point.size() > 0){
						for (int i = 0; i < arr_snap_point.size(); i++) {
							ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
							if(arr_p != null){
								for (int j = 0; j < arr_p.size(); j++) {
									LinkedHashMap<String, Float> p = arr_p.get(j);
									float x = p.get("x");
									float y = p.get("y");
									if(dis_min > disSnapPoint(x, y, X, Y)){
										dis_min = disSnapPoint(x, y, X, Y);
										x_snap = x;
										y_snap = y;
									}
								}
							}
						}
						if(dis_min <= dis_snap){
							X = x_snap;
							Y = y_snap;
						}
					}
					
					path_brush.moveTo(X, Y);
					path_brush_show.moveTo(X, Y);
					
					LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
					map.put("x", X);
					map.put("y", Y);
					arr.add(map);
					LinkedHashMap<String, Float> p = new LinkedHashMap<String, Float>();
					p.put("x", X);
					p.put("y", Y);
					arr_point.add(p);
					return true;
				case MotionEvent.ACTION_MOVE:
					path_brush.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					path_brush_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					
					if(check_rate()){
						arr_x_show.add((eventX - x_m2)/width_scale);
						arr_y_show.add((eventY - y_m2)/height_scale);
						size_line += (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
						real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
						text_show = (float)Math.round(real_size * 10)/10 + " cm";
						x_show = arr_x_show.get((int)arr_x_show.size()/2);
						y_show = arr_y_show.get((int)arr_y_show.size()/2);
					}
					X = (eventX - x_m2)/width_scale;
					Y = (eventY - y_m2)/height_scale;
					
					LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
					map2.put("x", (eventX - x_m2)/width_scale);
					map2.put("y", (eventY - y_m2)/height_scale);
					arr.add(map2);
					
					break;
				case MotionEvent.ACTION_UP:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					dis_min = 1000;
					X = (event.getX() - x_m2)/width_scale;
					Y = (event.getY() - y_m2)/height_scale;
					
					if(arr_snap_point.size() > 0){
						for (int i = 0; i < arr_snap_point.size(); i++) {
							ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
							if(arr_p != null){
								for (int j = 0; j < arr_p.size(); j++) {
									LinkedHashMap<String, Float> p2 = arr_p.get(j);
									float x = p2.get("x");
									float y = p2.get("y");
									if(dis_min > disSnapPoint(x, y, X, Y)){
										dis_min = disSnapPoint(x, y, X, Y);
										x_snap = x;
										y_snap = y;
									}
								}
							}
						}
						if(dis_min <= dis_snap){
							X = x_snap;
							Y = y_snap;
						}
					}
					
					path_brush.lineTo(X, Y);
					text_show = "";
					path_brush_show = new Path();
					paths.add(path_brush);
					paints.add(paint);
					path_brush = new Path();
					pos_text.add(0);
					angle_text.add(0);
					
					LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
					p2.put("x", X);
					p2.put("y", Y);
					arr_point.add(p2);
					arr_snap_point.add(arr_point);
					arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					
					if(check_rate()){
						EsspDrawImageActivity.text = (float)Math.round(real_size * 10)/10 + " cm";
						addTextRate(x_show, y_show);
						state_undos.add(2);
						
						LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
						map3.put("x", X);
						map3.put("y", Y);
						arr.add(map3);
						
						map3 = new LinkedHashMap<String, Float>();
						map3.put("x", x_show);
						map3.put("y", y_show);
						arr.add(map3);
						
						arr_pixel.add(arr);
						arr = new ArrayList<LinkedHashMap<String,Float>>();
						
						arr_image.add(0, R.mipmap.ic_draw);
						arr_type.add(3);
						path_virtual.add(2);
						angles_rotate.add(0d);
						angles_rotate.add(0d);
						arr_check_rate.add(1);
						arr_check_rate.add(1);
						arr_check_rate_bool.add(true);
						arr_check_rate_bool.add(true);
						
						EsspDrawImageActivity.setListLayer(getContext());
						
						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(arr_pixel.size() - 1);
						for (LinkedHashMap<String, Float> m : arr_pos) {
							float x = m.get("x");
							float y = m.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(m2);
						arr_pos_center_rotate.add(m2);
						pos_x_text_rotate.add((xr2 + xr1)/2);
						pos_y_text_rotate.add((yr2 + yr1)/2);
						arr_snap_point.add(null);
					} else {
						state_undos.add(1);
						
						LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
						map3.put("x", X);
						map3.put("y", Y);
						arr.add(map3);
						arr_pixel.add(arr);
						arr = new ArrayList<LinkedHashMap<String,Float>>();
						
						arr_image.add(0, R.mipmap.ic_draw);
						arr_type.add(3);
						path_virtual.add(1);
						angles_rotate.add(0d);
						arr_check_rate.add(0);
						arr_check_rate_bool.add(false);
						EsspDrawImageActivity.setListLayer(getContext());
						
						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(arr_pixel.size() - 1);
						for (LinkedHashMap<String, Float> m : arr_pos) {
							float x = m.get("x");
							float y = m.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(m2);
					}
					size_line = 0f;
					invalidate();
					arr_x_show.clear();
					arr_y_show.clear();
					
					break;
				default:
					return false;
				}
			} else {
				if(isRotate){
					double x_tam = (xr2 + xr1)/2;
					double y_tam = (yr2 + yr1)/2;
					double goc = 0d;
					double goc_kt = 0d;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_bt = 90;
							} else {
								goc_bt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_bt = 0;
							} else {
								goc_bt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_bt = goc_bt%360;
						
						int pos_layer1 = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer1 += path_virtual.get(i);
						}
						goc_cu = angles_rotate.get(pos_layer1);
						break;
					case MotionEvent.ACTION_MOVE:
						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}
						
						angles_rotate.remove(pos_layer);
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_kt = 90;
							} else {
								goc_kt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_kt = 0;
							} else {
								goc_kt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_kt = goc_kt%360;
						goc = goc_kt - goc_bt + goc_cu;
						angles_rotate.add(pos_layer, goc%360);
						if(arr_check_rate_bool.get(pos_layer)){
							angles_rotate.remove(pos_layer + 1);
							angles_rotate.add(pos_layer + 1, goc%360);
							angles.remove((int)(angle_text.get(pos_layer + 1)));
							angles.add((int)(angle_text.get(pos_layer + 1)), (int)(goc%360));
						} else {
//							xd = arr_snap_point.get(pos_layer).get(0).get("x");
//							yd = arr_snap_point.get(pos_layer).get(0).get("y");
//							xc = arr_snap_point.get(pos_layer).get(arr_snap_point.get(pos_layer).size() - 1).get("x");
//							yc = arr_snap_point.get(pos_layer).get(arr_snap_point.get(pos_layer).size() - 1).get("y"); 
//							
//							float canh_huyen_tren = (float) Math.sqrt((xd - x_tam)*(xd - x_tam) + (yd - y_tam)*(yd - y_tam));
//							double goc_ban_dau = Math.atan((eventX - x_tam)/(eventY - y_tam)*(180 / Math.PI));
//							if(yd < y_tam){
//								goc_ban_dau = (goc_ban_dau + 360)%360;
//							} else if(yd > y_tam){
//								goc_ban_dau = (180 - goc_ban_dau)%360;
//							} else {
//								if(xd > x_tam){
//									goc_ban_dau = 90;
//								} else {
//									goc_ban_dau = 270;
//								}
//							}
//							float x_new = 0f;
//							float y_new = 0f;
//							double goc_da_xoay = (goc + goc_ban_dau)%360;
//							if(xd == x_tam){
//								if(yd > y_tam){
//									goc_da_xoay = 0;
//									x_new = 0;
//									y_new = canh_huyen_tren;
//								} else {
//									goc_da_xoay = 180;
//									x_new = 0;
//									y_new = -canh_huyen_tren;
//								}
//							} else if(yd == y_tam){
//								if(xd > x_tam){
//									goc_da_xoay = 90;
//									x_new = canh_huyen_tren;
//									y_new = 0;
//								} else {
//									goc_da_xoay = 270;
//									x_new = -canh_huyen_tren;
//									y_new = 0;
//								}
//							} else if(xd > x_tam){
//								if(yd > y_tam){//góc 2/4
//									goc_da_xoay = 180 - goc_da_xoay;
//									x_new = (float) (canh_huyen_tren*Math.sin(Math.toRadians(goc_da_xoay)));
//									y_new = (float) (canh_huyen_tren*Math.cos(Math.toRadians(goc_da_xoay)));
//								} else {//góc 1/4
//									x_new = (float) (canh_huyen_tren*Math.sin(Math.toRadians(goc_da_xoay)));
//									y_new = (float) (-1*canh_huyen_tren*Math.cos(Math.toRadians(goc_da_xoay)));
//								}
//							} else if(xd < x_tam){
//								if(yd > y_tam){//góc 3/4
//									goc_da_xoay -= 180;
//									x_new = (float) (-1*canh_huyen_tren*Math.sin(Math.toRadians(goc_da_xoay)));
//									y_new = (float) (canh_huyen_tren*Math.cos(Math.toRadians(goc_da_xoay)));
//								} else {//góc 4/4
//									goc_da_xoay = 360 - goc_da_xoay;
//									x_new = (float) (-1*canh_huyen_tren*Math.sin(Math.toRadians(goc_da_xoay)));
//									y_new = (float) (-1*canh_huyen_tren*Math.cos(Math.toRadians(goc_da_xoay)));
//								}
//							}
//							
//							ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
//							arr_point = arr_snap_point.get(pos_layer);
//							for (int i = 0; i < arr_point.size(); i++) {
//								arr_point.get(i).put("x", (float)(x_tam + x_new));
//								arr_point.get(i).put("y", (float)(y_tam + y_new));
//							}
						}
						invalidate();
						break;
					case MotionEvent.ACTION_UP:
						
						break;
					}
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						break;
					case MotionEvent.ACTION_MOVE:
						xr1 = width;
						xr2 = 0f;
						yr1 = height;
						yr2 = 0f;
						float x_move = (eventX - x_m2)/width_scale - x_move_obj;
						float y_move = (eventY - y_m2)/height_scale - y_move_obj;
						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
						for (int i = 0; i < arr_pos.size(); i++) {
							LinkedHashMap<String, Float> map = arr_pos.get(i);
							float x_new = map.get("x") + x_move;
							float y_new = map.get("y") + y_move;
							map.put("x", x_new);
							map.put("y", y_new);
						}
						
						for (LinkedHashMap<String, Float> map : arr_pos) {
							float x = map.get("x");
							float y = map.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}
						
						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}
						
						ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
						arr_point = arr_snap_point.get(pos_layer);
						if(arr_point != null){
							for (int i = 0; i < arr_point.size(); i++) {
								LinkedHashMap<String, Float> p = arr_point.get(i);
								p.put("x", p.get("x") + x_move);
								p.put("y", p.get("y") + y_move);
							}
						}
						
						paths.remove(pos_layer);
						path_brush.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						if(arr_check_rate.get(pos_layer) == 1){
							for (int i = 1; i < arr_pos.size() - 1; i++) {
								path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
							}
							pos_x_text.remove((int)(pos_text.get(pos_layer + 1)));
							pos_y_text.remove((int)(pos_text.get(pos_layer + 1)));
							pos_x_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("x"));
							pos_y_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("y"));
							
							pos_x_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
							pos_y_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
							pos_x_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (xr2 + xr1)/2);
							pos_y_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (yr2 + yr1)/2);
						} else {
							for (int i = 1; i < arr_pos.size(); i++) {
								path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
							}
						}
						paths.add(pos_layer, path_brush);
						path_brush = new Path();
						invalidate();
						
						arr_pos_center_rotate.remove(pos_layer);
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(pos_layer, m2);
						if(arr_check_rate_bool.get(pos_layer)){
							arr_pos_center_rotate.remove(pos_layer + 1);
							arr_pos_center_rotate.add(pos_layer + 1, m2);
						}
						drawRecSelect();
						
						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						break;
					case MotionEvent.ACTION_UP:
						break;
					}
				}
			}
		} catch(Exception ex) {
			ex.toString();
		}
		return true;
	}
	
	/** Vẽ nét trơn
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawLineSmooth(MotionEvent event, float eventX, float eventY){
		try{
			mCanvas = new Canvas();
			paint_brush.setAntiAlias(true);
			paint_brush.setStrokeWidth(EsspCommon.width_brush);
			paint_brush.setColor(EsspCommon.color);
			paint_brush.setStyle(Style.STROKE);
			paint_brush.setStrokeJoin(Paint.Join.ROUND);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(EsspCommon.width_brush);
			paint.setColor(EsspCommon.color);
			paint.setStyle(Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);

			if(!isMove){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					X = (event.getX() - x_m2)/width_scale;
					Y = (event.getY() - y_m2)/height_scale;
					dis_min = 1000;

					if(arr_snap_point.size() > 0){
						for (int i = 0; i < arr_snap_point.size(); i++) {
							ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
							if(arr_p != null){
								for (int j = 0; j < arr_p.size(); j++) {
									LinkedHashMap<String, Float> p = arr_p.get(j);
									float x = p.get("x");
									float y = p.get("y");
									if(dis_min > disSnapPoint(x, y, X, Y)){
										dis_min = disSnapPoint(x, y, X, Y);
										x_snap = x;
										y_snap = y;
									}
								}
							}
						}
						if(dis_min <= dis_snap){
							X = x_snap;
							Y = y_snap;
						}
					}

					path_brush_show.moveTo(X, Y);

					LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
					map.put("x", X);
					map.put("y", Y);
					arr.add(map);
					LinkedHashMap<String, Float> p = new LinkedHashMap<String, Float>();
					p.put("x", X);
					p.put("y", Y);
					arr_point.add(p);
					return true;
				case MotionEvent.ACTION_MOVE:
					path_brush_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);

					if(check_rate()){
						arr_x_show.add((eventX - x_m2)/width_scale);
						arr_y_show.add((eventY - y_m2)/height_scale);
						size_line += (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
						real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
						text_show = (float)Math.round(real_size * 10)/10 + " cm";
						x_show = arr_x_show.get((int)arr_x_show.size()/2);
						y_show = arr_y_show.get((int)arr_y_show.size()/2);
					}
					X = (eventX - x_m2)/width_scale;
					Y = (eventY - y_m2)/height_scale;

					LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
					map2.put("x", (eventX - x_m2)/width_scale);
					map2.put("y", (eventY - y_m2)/height_scale);
					arr.add(map2);

					break;
				case MotionEvent.ACTION_UP:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					text_show = "";
					dis_min = 1000;
					X = (eventX - x_m2)/width_scale;
					Y = (eventY - y_m2)/height_scale;

					if(arr_snap_point.size() > 0){
						for (int i = 0; i < arr_snap_point.size(); i++) {
							ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
							if(arr_p != null){
								for (int j = 0; j < arr_p.size(); j++) {
									LinkedHashMap<String, Float> p2 = arr_p.get(j);
									float x = p2.get("x");
									float y = p2.get("y");
									if(dis_min > disSnapPoint(x, y, X, Y)){
										dis_min = disSnapPoint(x, y, X, Y);
										x_snap = x;
										y_snap = y;
									}
								}
							}
						}
						if(dis_min <= dis_snap){
							X = x_snap;
							Y = y_snap;
						}
					}

					path_brush_show = new Path();
					pos_text.add(0);
					angle_text.add(0);

					LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
					p2.put("x", X);
					p2.put("y", Y);
					arr_point.add(p2);
					arr_snap_point.add(arr_point);
					arr_point = new ArrayList<LinkedHashMap<String, Float>>();

					if(check_rate()){
						EsspDrawImageActivity.text = (float)Math.round(real_size * 10)/10 + " cm";

						state_undos.add(2);

						LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
						map3.put("x", X);
						map3.put("y", Y);
						arr.add(map3);

						map3 = new LinkedHashMap<String, Float>();
						map3.put("x", x_show);
						map3.put("y", y_show);
						arr.add(map3);

						arr_pixel.add(arr);
						arr = new ArrayList<LinkedHashMap<String,Float>>();

						arr_image.add(0, R.mipmap.ic_day_cap);
						arr_type.add(20);
						path_virtual.add(2);
						angles_rotate.add(0d);
						angles_rotate.add(0d);
						arr_check_rate.add(1);
						arr_check_rate.add(1);
						arr_check_rate_bool.add(true);
						arr_check_rate_bool.add(true);

						EsspDrawImageActivity.setListLayer(getContext());

						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(arr_pixel.size() - 1);
						path_brush.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						path_brush.quadTo(arr_pos.get((int)(arr_pos.size()/2)).get("x"), arr_pos.get((int)(arr_pos.size()/2)).get("y"), arr_pos.get(arr_pos.size() - 2).get("x"), arr_pos.get(arr_pos.size() - 2).get("y"));
//						for (int i = 1; i < arr_pos.size() - 2; i++) {
//							path_brush.quadTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"), arr_pos.get(i + 1).get("x"), arr_pos.get(i + 1).get("y"));
//						}
						for (LinkedHashMap<String, Float> m : arr_pos) {
							float x = m.get("x");
							float y = m.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(m2);
						arr_pos_center_rotate.add(m2);
						pos_x_text_rotate.add((xr2 + xr1)/2);
						pos_y_text_rotate.add((yr2 + yr1)/2);
						paths.add(path_brush);
						paints.add(paint);
						addTextRate(x_show, y_show);
						arr_snap_point.add(null);
					} else {
						state_undos.add(1);

						LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
						map3.put("x", X);
						map3.put("y", Y);
						arr.add(map3);
						arr_pixel.add(arr);
						arr = new ArrayList<LinkedHashMap<String,Float>>();

						arr_image.add(0, R.mipmap.ic_day_cap);
						arr_type.add(20);
						path_virtual.add(1);
						angles_rotate.add(0d);
						arr_check_rate.add(0);
						arr_check_rate_bool.add(false);
						EsspDrawImageActivity.setListLayer(getContext());

						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(arr_pixel.size() - 1);
						path_brush.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						path_brush.quadTo(arr_pos.get((int)(arr_pos.size()/2)).get("x"), arr_pos.get((int)(arr_pos.size()/2)).get("y"), arr_pos.get(arr_pos.size() - 1).get("x"), arr_pos.get(arr_pos.size() - 1).get("y"));
						for (LinkedHashMap<String, Float> m : arr_pos) {
							float x = m.get("x");
							float y = m.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(m2);
						paths.add(path_brush);
						paints.add(paint);
					}
					path_brush = new Path();
					size_line = 0f;
					invalidate();
					arr_x_show.clear();
					arr_y_show.clear();

					break;
				default:
					return false;
				}
			} else {
				if(isRotate){
					double x_tam = (xr2 + xr1)/2;
					double y_tam = (yr2 + yr1)/2;
					double goc = 0d;
					double goc_kt = 0d;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_bt = 90;
							} else {
								goc_bt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_bt = 0;
							} else {
								goc_bt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_bt = goc_bt%360;
						int pos_layer1 = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer1 += path_virtual.get(i);
						}
						goc_cu = angles_rotate.get(pos_layer1);
						break;
					case MotionEvent.ACTION_MOVE:
						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}
						angles_rotate.remove(pos_layer);
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_kt = 90;
							} else {
								goc_kt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_kt = 0;
							} else {
								goc_kt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_kt = goc_kt%360;
						goc = goc_kt - goc_bt + goc_cu;
						angles_rotate.add(pos_layer, goc%360);
						if(arr_check_rate_bool.get(pos_layer)){
							angles_rotate.remove(pos_layer + 1);
							angles_rotate.add(pos_layer + 1, goc%360);
							angles.remove((int)(angle_text.get(pos_layer + 1)));
							angles.add((int)(angle_text.get(pos_layer + 1)), (int)(goc%360));
						}
						invalidate();
						break;
					case MotionEvent.ACTION_UP:

						break;
					}
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						break;
					case MotionEvent.ACTION_MOVE:
						xr1 = width;
						xr2 = 0f;
						yr1 = height;
						yr2 = 0f;
						float x_move = (eventX - x_m2)/width_scale - x_move_obj;
						float y_move = (eventY - y_m2)/height_scale - y_move_obj;
						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
						for (int i = 0; i < arr_pos.size(); i++) {
							LinkedHashMap<String, Float> map = arr_pos.get(i);
							float x_new = map.get("x") + x_move;
							float y_new = map.get("y") + y_move;
							map.put("x", x_new);
							map.put("y", y_new);
						}

						for (LinkedHashMap<String, Float> map : arr_pos) {
							float x = map.get("x");
							float y = map.get("y");
							if(xr1 > x)
								xr1 = x;
							if(xr2 < x)
								xr2 = x;
							if(yr1 > y)
								yr1 = y;
							if(yr2 < y)
								yr2 = y;
						}

						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}

						ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
						arr_point = arr_snap_point.get(pos_layer);
						if(arr_point != null){
							for (int i = 0; i < arr_point.size(); i++) {
								LinkedHashMap<String, Float> p = arr_point.get(i);
								p.put("x", p.get("x") + x_move);
								p.put("y", p.get("y") + y_move);
							}
						}

						paths.remove(pos_layer);
						path_brush.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						if(arr_check_rate.get(pos_layer) == 1){
							path_brush.quadTo(arr_pos.get((int)(arr_pos.size()/2)).get("x"), arr_pos.get((int)(arr_pos.size()/2)).get("y"), arr_pos.get(arr_pos.size() - 2).get("x"), arr_pos.get(arr_pos.size() - 2).get("y"));
//							for (int i = 1; i < arr_pos.size() - 1; i++) {
//								path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
//							}
							pos_x_text.remove((int)(pos_text.get(pos_layer + 1)));
							pos_y_text.remove((int)(pos_text.get(pos_layer + 1)));
							pos_x_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("x"));
							pos_y_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("y"));

							pos_x_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
							pos_y_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
							pos_x_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (xr2 + xr1)/2);
							pos_y_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (yr2 + yr1)/2);
						} else {
							path_brush.quadTo(arr_pos.get((int)(arr_pos.size()/2)).get("x"), arr_pos.get((int)(arr_pos.size()/2)).get("y"), arr_pos.get(arr_pos.size() - 1).get("x"), arr_pos.get(arr_pos.size() - 1).get("y"));
//							for (int i = 1; i < arr_pos.size(); i++) {
//								path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
//							}
						}
						paths.add(pos_layer, path_brush);
						path_brush = new Path();
						invalidate();

						arr_pos_center_rotate.remove(pos_layer);
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(pos_layer, m2);
						if(arr_check_rate_bool.get(pos_layer)){
							arr_pos_center_rotate.remove(pos_layer + 1);
							arr_pos_center_rotate.add(pos_layer + 1, m2);
						}
						drawRecSelect();

						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						break;
					case MotionEvent.ACTION_UP:
						break;
					}
				}
			}
		} catch(Exception ex) {
			ex.toString();
		}
		return true;
	}
	
	/** Vẽ đường thẳng
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawLine(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true);
		paint_Line.setStrokeWidth(EsspCommon.width_brush);
		paint_Line.setColor(EsspCommon.color);
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setStrokeJoin(Paint.Join.ROUND);
		
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(Color.RED);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		paint_text.setFakeBoldText(true);// chữ đậm
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				dis_min = 1000;
				
				if(arr_snap_point.size() > 0){
					for (int i = 0; i < arr_snap_point.size(); i++) {
						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
						if(arr_p != null){
							for (int j = 0; j < arr_p.size(); j++) {
								LinkedHashMap<String, Float> p = arr_p.get(j);
								float x = p.get("x");
								float y = p.get("y");
								if(dis_min > disSnapPoint(x, y, X, Y)){
									dis_min = disSnapPoint(x, y, X, Y);
									x_snap = x;
									y_snap = y;
								}
							}
						}
					}
					if(dis_min <= dis_snap){
						X = x_snap;
						Y = y_snap;
					}
				}
				
				path_Line.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", X);
				map.put("y", Y);
				arr.add(map);
				LinkedHashMap<String, Float> p = new LinkedHashMap<String, Float>();
				p.put("x", X);
				p.put("y", Y);
				arr_point.add(p);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_Line_show = new Path();
				path_Line_show.moveTo(X, Y);
				path_Line_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				
				if(check_rate()){
					float x = ((eventX - x_m2)/width_scale + X)/2;
					float y = ((eventY - y_m2)/height_scale + Y)/2;
					float size_line = (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
					real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
					text_show = (float)Math.round(real_size * 100)/10 + " cm";
					x_show = (x - x_m2)/width_scale;
					y_show = (y - y_m2)/height_scale;
				}
				
				break;
			case MotionEvent.ACTION_UP:
				text_show = "";
				dis_min = 1000;
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				
				if(arr_snap_point.size() > 0){
					for (int i = 0; i < arr_snap_point.size(); i++) {
						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
						if(arr_p != null){
							for (int j = 0; j < arr_p.size(); j++) {
								LinkedHashMap<String, Float> p2 = arr_p.get(j);
								float x = p2.get("x");
								float y = p2.get("y");
								if(dis_min > disSnapPoint(x, y, X, Y)){
									dis_min = disSnapPoint(x, y, X, Y);
									x_snap = x;
									y_snap = y;
								}
							}
						}
					}
					if(dis_min <= dis_snap){
						X = x_snap;
						Y = y_snap;
					}
				}
				path_Line_show = new Path();
				path_Line.lineTo(X, Y);
				paths.add(path_Line);
				paints.add(paint_Line);
				path_Line = new Path();
				pos_text.add(0);
				angle_text.add(0);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", X);
				p2.put("y", Y);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				
				if(check_rate()){
					EsspDrawImageActivity.text = (float)Math.round(real_size * 10)/10 + " cm";
					addTextRate(x_show, y_show);
					state_undos.add(2);
					
					LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
					map3.put("x", X);
					map3.put("y", Y);
					arr.add(map3);
					
					map3 = new LinkedHashMap<String, Float>();
					map3.put("x", x_show);
					map3.put("y", y_show);
					arr.add(map3);
					
					arr_pixel.add(arr);
					arr = new ArrayList<LinkedHashMap<String,Float>>();
					
					arr_image.add(0, R.mipmap.ic_line);
					arr_type.add(4);
					path_virtual.add(2);
					angles_rotate.add(0d);
					angles_rotate.add(0d);
					arr_check_rate.add(1);
					arr_check_rate.add(1);
					arr_check_rate_bool.add(true);
					arr_check_rate_bool.add(true);
					EsspDrawImageActivity.setListLayer(getContext());
					
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(arr_pixel.size() - 1);
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(m2);
					arr_pos_center_rotate.add(m2);
					pos_x_text_rotate.add((xr2 + xr1)/2);
					pos_y_text_rotate.add((yr2 + yr1)/2);
					arr_snap_point.add(null);
				} else {
					state_undos.add(1);
					
					LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
					map3.put("x", X);
					map3.put("y", Y);
					arr.add(map3);
					arr_pixel.add(arr);
					arr = new ArrayList<LinkedHashMap<String,Float>>();
					
					arr_image.add(0, R.mipmap.ic_line);
					arr_type.add(4);
					path_virtual.add(1);
					angles_rotate.add(0d);
					arr_check_rate.add(0);
					arr_check_rate_bool.add(false);
					EsspDrawImageActivity.setListLayer(getContext());
					
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(arr_pixel.size() - 1);
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(m2);
				}
				size_line = 0f;
				invalidate();
				arr_x_show.clear();
				arr_y_show.clear();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					if(arr_check_rate_bool.get(pos_layer)){
						angles_rotate.remove(pos_layer + 1);
						angles_rotate.add(pos_layer + 1, goc%360);
						angles.remove((int)(angle_text.get(pos_layer + 1)));
						angles.add((int)(angle_text.get(pos_layer + 1)), (int)(goc%360));
					}
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_Line.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_Line.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					
					if(arr_check_rate.get(pos_layer) == 1){
						pos_x_text.remove((int)(pos_text.get(pos_layer + 1)));
						pos_y_text.remove((int)(pos_text.get(pos_layer + 1)));
						pos_x_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("x"));
						pos_y_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("y"));
						
						pos_x_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
						pos_y_text_rotate.remove((int)(pos_text.get(pos_layer + 1)));
						pos_x_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (xr2 + xr1)/2);
						pos_y_text_rotate.add((int)(pos_text.get(pos_layer + 1)), (yr2 + yr1)/2);
					}
//					else {
//						for (int i = 1; i < arr_pos.size(); i++) {
//							path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
//						}
//					}
					
					paths.add(pos_layer, path_Line);
					path_Line = new Path();
					invalidate();
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					
					if(arr_check_rate_bool.get(pos_layer)){
						arr_pos_center_rotate.remove(pos_layer + 1);
						arr_pos_center_rotate.add(pos_layer + 1, m2);
					}
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
//					arr_pos_center_rotate.remove(pos_layer);
//					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
//					m2.put("x", (xr2 + xr1)/2);
//					m2.put("y", (yr2 + yr1)/2);
//					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			}
		}
		return true;
	}
	
	/** Vẽ dây cáp
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawDayCap(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true);
		paint_Line.setStrokeWidth(EsspCommon.width_brush);
		paint_Line.setColor(EsspCommon.color);
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setStrokeJoin(Paint.Join.ROUND);
		
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(Color.RED);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		paint_text.setFakeBoldText(true);// chữ đậm
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = (event.getX() - x_m2)/width_scale;
			Y = (event.getY() - y_m2)/height_scale;
			path_Line.moveTo(X, Y);
//			if(EsspCommon.map2 != null){
//				EsspCommon.map = new LinkedHashMap<String, String>();
//				EsspCommon.map = EsspCommon.map2;
//			}
			return true;
		case MotionEvent.ACTION_MOVE:
			path_Line_show = new Path();
			path_Line_show.moveTo(X, Y);
			path_Line_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			
			if(check_rate()){
				float x = ((eventX - x_m2)/width_scale + X)/2;
				float y = ((eventY - y_m2)/height_scale + Y)/2;
				float size_line = (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
				real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
				text_show = (float)Math.round(real_size * 10)/10 + " cm";
				x_show = (x - x_m2)/width_scale;
				y_show = (y - y_m2)/height_scale;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			text_show = "";
			path_Line_show = new Path();
			path_Line.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			paths.add(path_Line);
			paints.add(paint_Line);
			path_Line = new Path();
			
			if(check_rate()){
				EsspDrawImageActivity.text = (float)Math.round(real_size * 10)/10 + " cm";
				addTextRate(x_show, y_show);
				state_undos.add(2);
				
				if(EsspCommon.map != null){
					float old_width = 0f;
					try{
						old_width = Float.parseFloat(EsspCommon.map.get("soluong"));
					} catch(Exception ex) {
						old_width = 0f;
					}
					EsspCommon.map.put("soluong", (old_width + (float)Math.round(real_size * 10)/10) + "");
					if(old_width == 0){
//						EsspDrawImageActivity.arr_dc.add(EsspCommon.map);
					}
				}
			} else {
				state_undos.add(1);
			}
			
			invalidate();
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Vẽ đường thẳng có 2 vòng tròn 2 đầu
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawLineb(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Line2 = new Paint();
		paint_Line2.setAntiAlias(true);
		paint_Line2.setStrokeWidth(EsspCommon.width_brush);
		paint_Line2.setColor(EsspCommon.color);
		paint_Line2.setStyle(Style.STROKE);
		paint_Line2.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Cicle_line = new Paint();
		paint_Cicle_line.setAntiAlias(true);
		paint_Cicle_line.setStrokeWidth(EsspCommon.width_brush);
		paint_Cicle_line.setColor(EsspCommon.color);
		paint_Cicle_line.setStyle(Style.STROKE);
		paint_Cicle_line.setStrokeJoin(Paint.Join.ROUND);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = (event.getX() - x_m2)/width_scale;
			Y = (event.getY() - y_m2)/height_scale;
			path_Line.moveTo(X, Y);
			path_Cicle_Line.addCircle((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale, 15, Direction.CW);
			paths.add(path_Cicle_Line);
			paints.add(paint_Cicle_line);
			path_Cicle_Line = new Path();
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			path_Line_show = new Path();
			path_Line_show.moveTo(X, Y);
			path_Line_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			break;
		case MotionEvent.ACTION_UP:
			path_Line_show = new Path();
			path_Line.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
			path_Cicle_Line.addCircle((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale, 15, Direction.CW);
			paths.add(path_Line);
			paths.add(path_Cicle_Line);
			paints.add(paint_Line2);
			paints.add(paint_Cicle_line);
			path_Line = new Path();
			path_Cicle_Line = new Path();
			state_undos.add(3);
			invalidate();
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Vẽ 2 đường thằng song song
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawDuong(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true);
		paint_Line.setStrokeWidth(EsspCommon.width_brush);
		paint_Line.setColor(EsspCommon.color);
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setStrokeJoin(Paint.Join.ROUND);
		
		float R1 = 15f;
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
//				dis_min = 1000;
//				
//				if(arr_snap_point.size() > 0){
//					for (int i = 0; i < arr_snap_point.size(); i++) {
//						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
//						for (int j = 0; j < arr_p.size(); j++) {
//							LinkedHashMap<String, Float> p = arr_p.get(j);
//							float x = p.get("x");
//							float y = p.get("y");
//							if(dis_min > disSnapPoint(x, y, X, Y)){
//								dis_min = disSnapPoint(x, y, X, Y);
//								x_snap = x;
//								y_snap = y;
//							}
//						}
//					}
//					if(dis_min <= dis_snap){
//						X = x_snap;
//						Y = y_snap;
//					}
//				}
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", X);
				map.put("y", Y);
				arr.add(map);
//				LinkedHashMap<String, Float> p = new LinkedHashMap<String, Float>();
//				p.put("x", X);
//				p.put("y", Y);
//				arr_point.add(p);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_Line_show = new Path();
				
				float tlx = 1f;
				float tly = 1f;
				
				float doi = Math.abs((eventY - y_m2)/height_scale - Y);
				float ke = Math.abs((eventX - x_m2)/width_scale - X);
				float huyen = (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y)); 
				float sinA = doi/huyen;
				float cosA = ke/huyen;
				
				tlx = R1 * sinA;
				tly = R1 * cosA;
				
				if((eventY - y_m2)/height_scale > Y){
					tlx = -tlx;
				} else if((eventY - y_m2)/height_scale < Y){
					tlx = tlx;
				} else {
					tlx = X;
				}
				if((eventX - x_m2)/width_scale > X){
					tly = tly;
				} else if((eventX - x_m2)/width_scale < X){
					tly = -tly;
				} else {
					tly = Y;
				}
				
				path_Line_show.moveTo(X - tlx, Y - tly);
				path_Line_show.lineTo((eventX - x_m2)/width_scale - tlx, (eventY - y_m2)/height_scale - tly);
				
				path_Line_show.moveTo(X + tlx, Y + tly);
				path_Line_show.lineTo((eventX - x_m2)/width_scale + tlx, (eventY - y_m2)/height_scale + tly);
				
				break;
			case MotionEvent.ACTION_UP:
//				dis_min = 1000;
//				X = (event.getX() - x_m2)/width_scale;
//				Y = (event.getY() - y_m2)/height_scale;
//				
//				if(arr_snap_point.size() > 0){
//					for (int i = 0; i < arr_snap_point.size(); i++) {
//						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
//						for (int j = 0; j < arr_p.size(); j++) {
//							LinkedHashMap<String, Float> p1 = arr_p.get(j);
//							float x = p1.get("x");
//							float y = p1.get("y");
//							if(dis_min > disSnapPoint(x, y, X, Y)){
//								dis_min = disSnapPoint(x, y, X, Y);
//								x_snap = x;
//								y_snap = y;
//							}
//						}
//					}
//					if(dis_min <= dis_snap){
//						X = x_snap;
//						Y = y_snap;
//					}
//				}
				
				path_Line_show = new Path();
				
				float tlx2 = 1f;
				float tly2 = 1f;
				
				float doi2 = Math.abs((eventY - y_m2)/height_scale - Y);
				float ke2 = Math.abs((eventX - x_m2)/width_scale - X);
				float huyen2 = (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y)); 
				float sinA2 = doi2/huyen2;
				float cosA2 = ke2/huyen2;
				
				tlx2 = R1 * sinA2;
				tly2 = R1 * cosA2;
				
				if((eventY - y_m2)/height_scale > Y){
					tlx2 = -tlx2;
				} else if((eventY - y_m2)/height_scale < Y){
					tlx2 = tlx2;
				} else {
					tlx2 = X;
				}
				if((eventX - x_m2)/width_scale > X){
					tly2 = tly2;
				} else if((eventX - x_m2)/width_scale < X){
					tly2 = -tly2;
				} else {
					tly2 = Y;
				}
				
				path_Line.moveTo(X - tlx2, Y - tly2);
				path_Line.lineTo((eventX - x_m2)/width_scale - tlx2, (eventY - y_m2)/height_scale - tly2);
				
				path_Line.moveTo(X + tlx2, Y + tly2);
				path_Line.lineTo((eventX - x_m2)/width_scale + tlx2, (eventY - y_m2)/height_scale + tly2);
				
				paths.add(path_Line);
				paints.add(paint_Line);
				path_Line = new Path();
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(1);
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (eventX - x_m2)/width_scale);
				map2.put("y", (eventY - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_duong);
				arr_type.add(18);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
				m2.put("x", (xr2 + xr1)/2);
				m2.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m2);
				
				size_line = 0f;
				invalidate();
				arr_x_show.clear();
				arr_y_show.clear();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					paths.remove(pos_layer);
					
					float tlx2 = 1f;
					float tly2 = 1f;
					
					float doi2 = Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"));
					float ke2 = Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"));
					float huyen2 = (float) Math.sqrt((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x")) + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))); 
					float sinA2 = doi2/huyen2;
					float cosA2 = ke2/huyen2;
					
					tlx2 = R1 * sinA2;
					tly2 = R1 * cosA2;
					
					if(arr_pos.get(1).get("y") > arr_pos.get(0).get("y")){
						tlx2 = -tlx2;
					} else if(arr_pos.get(1).get("y") < arr_pos.get(0).get("y")){
	//					tlx2 = tlx2;
					} else {
						tlx2 = arr_pos.get(0).get("x");
					}
					if(arr_pos.get(1).get("x") > arr_pos.get(0).get("x")){
	//					tly2 = tly2;
					} else if(arr_pos.get(1).get("x") < arr_pos.get(0).get("x")){
						tly2 = -tly2;
					} else {
						tly2 = arr_pos.get(0).get("y");
					}
					
					path_Line.moveTo(arr_pos.get(0).get("x") - tlx2, arr_pos.get(0).get("y") - tly2);
					path_Line.lineTo(arr_pos.get(1).get("x") - tlx2, arr_pos.get(1).get("y") - tly2);
					
					path_Line.moveTo(arr_pos.get(0).get("x") + tlx2, arr_pos.get(0).get("y") + tly2);
					path_Line.lineTo(arr_pos.get(1).get("x") + tlx2, arr_pos.get(1).get("y") + tly2);
					
					paths.add(pos_layer, path_Line);
					path_Line = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			}
		}
		return true;
	}
	
	/** Vẽ đường thẳng dạng nét đứt
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawDash(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush3.setAntiAlias(true);
		paint_brush3.setStrokeWidth(EsspCommon.width_brush);
		paint_brush3.setColor(EsspCommon.color);
		paint_brush3.setStyle(Style.STROKE);
		paint_brush3.setStrokeJoin(Paint.Join.ROUND);
		paint_brush3.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
		
		Paint paint_Line = new Paint();
		paint_Line.setAntiAlias(true);
		paint_Line.setStrokeWidth(EsspCommon.width_brush);
		paint_Line.setColor(EsspCommon.color);
		paint_Line.setStyle(Style.STROKE);
		paint_Line.setStrokeJoin(Paint.Join.ROUND);
		paint_Line.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				dis_min = 1000;
				
				if(arr_snap_point.size() > 0){
					for (int i = 0; i < arr_snap_point.size(); i++) {
						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
						if(arr_p != null){
							for (int j = 0; j < arr_p.size(); j++) {
								LinkedHashMap<String, Float> p = arr_p.get(j);
								float x = p.get("x");
								float y = p.get("y");
								if(dis_min > disSnapPoint(x, y, X, Y)){
									dis_min = disSnapPoint(x, y, X, Y);
									x_snap = x;
									y_snap = y;
								}
							}
						}
					}
					if(dis_min <= dis_snap){
						X = x_snap;
						Y = y_snap;
					}
				}
				
				path_Line.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", X);
				map.put("y", Y);
				arr.add(map);
				LinkedHashMap<String, Float> p = new LinkedHashMap<String, Float>();
				p.put("x", X);
				p.put("y", Y);
				arr_point.add(p);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_pash_show = new Path();
				path_pash_show.moveTo(X, Y);
				path_pash_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				break;
			case MotionEvent.ACTION_UP:
				dis_min = 1000;
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				
				if(arr_snap_point.size() > 0){
					for (int i = 0; i < arr_snap_point.size(); i++) {
						ArrayList<LinkedHashMap<String, Float>> arr_p = arr_snap_point.get(i);
						if(arr_p != null){
							for (int j = 0; j < arr_p.size(); j++) {
								LinkedHashMap<String, Float> p2 = arr_p.get(j);
								float x = p2.get("x");
								float y = p2.get("y");
								if(dis_min > disSnapPoint(x, y, X, Y)){
									dis_min = disSnapPoint(x, y, X, Y);
									x_snap = x;
									y_snap = y;
								}
							}
						}
					}
					if(dis_min <= dis_snap){
						X = x_snap;
						Y = y_snap;
					}
				}
				path_pash_show = new Path();
				path_Line.lineTo(X, Y);
				paths.add(path_Line);
				paints.add(paint_Line);
				path_Line = new Path();
				state_undos.add(1);
				pos_text.add(0);
				angle_text.add(0);
				
				LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
				map3.put("x", X);
				map3.put("y", Y);
				arr.add(map3);
				arr_pixel.add(arr);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", X);
				p2.put("y", Y);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				
				arr_image.add(0, R.mipmap.ic_dashpath);
				arr_type.add(19);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
				m2.put("x", (xr2 + xr1)/2);
				m2.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m2);
				invalidate();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_Line.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_Line.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					
					paths.add(pos_layer, path_Line);
					path_Line = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
			}
		}
		return true;
	}
	
	/** Vẽ đường thẳng kết hợp đường tự do
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
//	private boolean drawLineCustom(MotionEvent event, float eventX, float eventY){
//		mCanvas = new Canvas();
//		paint_brush.setAntiAlias(true);
//		paint_brush.setStrokeWidth(EsspCommon.width_brush);
//		paint_brush.setColor(EsspCommon.color);
//		paint_brush.setStyle(Style.STROKE);
//		paint_brush.setStrokeJoin(Paint.Join.ROUND);
//
//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setStrokeWidth(EsspCommon.width_brush);
//		paint.setColor(EsspCommon.color);
//		paint.setStyle(Style.STROKE);
//		paint.setStrokeJoin(Paint.Join.ROUND);
//
//		if(!isMove){
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				X = (event.getX() - x_m2)/width_scale;
//				Y = (event.getY() - y_m2)/height_scale;
//
//				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
//				map.put("x", (eventX - x_m2)/width_scale);
//				map.put("y", (eventY - y_m2)/height_scale);
//				arr.add(map);
//				if(check_line){
//					text_show = "";
//					path_brush_show.moveTo(Xtam, Ytam);
//					path_brush_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//					path_brush.moveTo(Xtam, Ytam);
//					path_brush.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//
//					if(check_rate()){
//						removeText();
//						arr_x_show.add((eventX - x_m2)/width_scale);
//						arr_y_show.add((eventY - y_m2)/height_scale);
//						float x = (Xtam + (eventX - x_m2)/width_scale)/2;
//						float y = (Ytam + (eventY - y_m2)/height_scale)/2;
//						size_line += (float) Math.sqrt((Xtam - (eventX - x_m2)/width_scale)*(Xtam - (eventX - x_m2)/width_scale) + (Ytam - (eventY - y_m2)/height_scale)*(Ytam - (eventY - y_m2)/height_scale));
//						real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
//						text_show = (float)Math.round(real_size * 10)/10 + " cm";
//						x_show = arr_x_show.get((int)arr_x_show.size()/2);
//						y_show = arr_y_show.get((int)arr_y_show.size()/2);
//					}
//				} else {
//					path_brush.moveTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//					path_brush_show.moveTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//				}
//				invalidate();
//				return true;
//			case MotionEvent.ACTION_MOVE:
//				path_brush.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//				path_brush_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//
//				if(check_rate()){
//					arr_x_show.add((eventX - x_m2)/width_scale);
//					arr_y_show.add((eventY - y_m2)/height_scale);
//					float x = ((eventX - x_m2)/width_scale + X)/2;
//					float y = ((eventY - y_m2)/height_scale + Y)/2;
//					size_line += (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
//					real_size = EsspDrawImageActivity.rate * EsspDrawImageActivity.convertpx(size_line);
//					text_show = (float)Math.round(real_size * 10)/10 + " cm";
//					x_show = arr_x_show.get((int)arr_x_show.size()/2);
//					y_show = arr_y_show.get((int)arr_y_show.size()/2);
//				}
//				X = (eventX - x_m2)/width_scale;
//				Y = (eventY - y_m2)/height_scale;
//
//				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
//				map2.put("x", (eventX - x_m2)/width_scale);
//				map2.put("y", (eventY - y_m2)/height_scale);
//				arr.add(map2);
//
//				break;
//			case MotionEvent.ACTION_UP:
//				text_show = "";
//				Xtam = (eventX - x_m2)/width_scale;
//				Ytam = (eventY - y_m2)/height_scale;
//				check_line = true;
//				path_brush_show = new Path();
//
//				if(check_rate()){
//					dem++;
//					EsspDrawImageActivity.text = (float)Math.round(real_size * 10)/10 + " cm";
//					addTextRate(x_show, y_show);
//					state_undos.add(dem);
//
//					LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
//					map3.put("x", (eventX - x_m2)/width_scale);
//					map3.put("y", (eventY - y_m2)/height_scale);
//					arr.add(map3);
//
//					map3 = new LinkedHashMap<String, Float>();
//					map3.put("x", x_show);
//					map3.put("y", y_show);
//					arr.add(map3);
//
//					if(!isChangeLayer){
//						pos_text.add(0);
//						paths.add(path_brush);
//						paints.add(paint);
//						arr_pixel.add(arr);
////						arr = new ArrayList<LinkedHashMap<String,Float>>();
//						arr_image.add(0, R.mipmap.ic_custom);
//						arr_type.add(20);
//						path_virtual.add(dem);
//					} else {
//						path_virtual.remove(path_virtual.size() - 1);
//						path_virtual.add(dem);
//						arr_pixel.remove(arr_pixel.size() - 1);
//						arr_pixel.remove(arr_pixel.size() - 1);
//						arr_pixel.add(arr);
//						path_brush.moveTo(arr_pixel.get(arr_pixel.size() - 1).get(0).get("x"), arr_pixel.get(arr_pixel.size() - 1).get(0).get("y"));
//						for (int i = 1; i < arr_pixel.get(arr_pixel.size() - 1).size(); i++) {
//							path_brush.lineTo(arr_pixel.get(arr_pixel.size() - 1).get(i).get("x"), arr_pixel.get(arr_pixel.size() - 1).get(i).get("y"));
//						}
//						paths.remove(paths.size() - 1);
//						paths.add(path_brush);
////						arr = new ArrayList<LinkedHashMap<String,Float>>();
//					}
//					EsspDrawImageActivity.setListLayer(getContext());
//				} else {
//					state_undos.add(1);
//
//					LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
//					map3.put("x", (eventX - x_m2)/width_scale);
//					map3.put("y", (eventY - y_m2)/height_scale);
//					arr.add(map3);
//
//					if(!isChangeLayer){
//						pos_text.add(0);
//						paths.add(path_brush);
//						paints.add(paint);
//						arr_pixel.add(arr);
////						arr = new ArrayList<LinkedHashMap<String,Float>>();
//						arr_image.add(0, R.mipmap.ic_custom);
//						arr_type.add(20);
//						path_virtual.add(1);
//					} else {
//						arr_pixel.remove(arr_pixel.size() - 1);
//						arr_pixel.add(arr);
//						path_brush.moveTo(arr_pixel.get(arr_pixel.size() - 1).get(0).get("x"), arr_pixel.get(arr_pixel.size() - 1).get(0).get("y"));
//						for (int i = 1; i < arr_pixel.get(arr_pixel.size() - 1).size(); i++) {
//							path_brush.lineTo(arr_pixel.get(arr_pixel.size() - 1).get(i).get("x"), arr_pixel.get(arr_pixel.size() - 1).get(i).get("y"));
//						}
//						paths.remove(paths.size() - 1);
//						paths.add(path_brush);
////						arr = new ArrayList<LinkedHashMap<String,Float>>();
//					}
//					EsspDrawImageActivity.setListLayer(getContext());
//				}
//				path_brush = new Path();
//				invalidate();
//				isChangeLayer = true;
//				break;
//			default:
//				return false;
//			}
//		} else {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				x_move_obj = (eventX - x_m2)/width_scale;
//				y_move_obj = (eventY - y_m2)/height_scale;
//				break;
//			case MotionEvent.ACTION_MOVE:
//				xr1 = width;
//				xr2 = 0f;
//				yr1 = height;
//				yr2 = 0f;
//				float x_move = (eventX - x_m2)/width_scale - x_move_obj;
//				float y_move = (eventY - y_m2)/height_scale - y_move_obj;
//				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
//				arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
//				for (int i = 0; i < arr_pos.size(); i++) {
//					LinkedHashMap<String, Float> map = arr_pos.get(i);
//					float x_new = map.get("x") + x_move;
//					float y_new = map.get("y") + y_move;
//					map.put("x", x_new);
//					map.put("y", y_new);
//				}
//
//				int pos_layer = 0;
//				for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
//					pos_layer += path_virtual.get(i);
//				}
//				paths.remove(pos_layer);
//				path_brush.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
//				if(arr_check_rate.get(EsspDrawImageActivity.pos_layer) == 1){
//					for (int i = 1; i < arr_pos.size() - 1; i++) {
//						path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
//					}
//					pos_x_text.remove((int)(pos_text.get(pos_layer + 1)));
//					pos_y_text.remove((int)(pos_text.get(pos_layer + 1)));
//					pos_x_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("x"));
//					pos_y_text.add(pos_text.get(pos_layer + 1), arr_pos.get(arr_pos.size() - 1).get("y"));
//				} else {
//					for (int i = 1; i < arr_pos.size(); i++) {
//						path_brush.lineTo(arr_pos.get(i).get("x"), arr_pos.get(i).get("y"));
//					}
//				}
//				paths.add(pos_layer, path_brush);
//				path_brush = new Path();
//				invalidate();
//
//				for (LinkedHashMap<String, Float> map : arr_pos) {
//					float x = map.get("x");
//					float y = map.get("y");
//					if(xr1 > x)
//						xr1 = x;
//					if(xr2 < x)
//						xr2 = x;
//					if(yr1 > y)
//						yr1 = y;
//					if(yr2 < y)
//						yr2 = y;
//				}
//				drawRecSelect();
//
//				x_move_obj = (eventX - x_m2)/width_scale;
//				y_move_obj = (eventY - y_m2)/height_scale;
//				break;
//			case MotionEvent.ACTION_UP:
//				break;
//			}
//		}
//		return true;
//	}
	
	/** Vẽ đoạn thẳng đo tỷ lệ
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
//	private boolean drawArrow(MotionEvent event, float eventX, float eventY){
//		mCanvas = new Canvas();
//		paint_brush4.setAntiAlias(true);
//		paint_brush4.setStrokeWidth(EsspCommon.width_brush);
//		paint_brush4.setColor(Color.RED);
//		paint_brush4.setStyle(Style.STROKE);
//		paint_brush4.setStrokeJoin(Paint.Join.ROUND);
//		paint_brush4.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
//
//		Paint paint_Line = new Paint();
//		paint_Line.setAntiAlias(true);
//		paint_Line.setStrokeWidth(EsspCommon.width_brush);
//		paint_Line.setColor(EsspCommon.color);
//		paint_Line.setStyle(Style.STROKE);
//		paint_Line.setStrokeJoin(Paint.Join.ROUND);
//		paint_Line.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
//
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			X = (event.getX() - x_m2)/width_scale;
//			Y = (event.getY() - y_m2)/height_scale;
//			return true;
//		case MotionEvent.ACTION_MOVE:
//			path_arrow = new Path();
//			path_arrow.moveTo(X, Y);
//			path_arrow.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
//
//			path_arrow.addCircle(X, Y, 4, Direction.CCW);
//			path_arrow.addCircle((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale, 4, Direction.CCW);
//
//			break;
//		case MotionEvent.ACTION_UP:
//			float x = ((eventX - x_m2)/width_scale + X)/2;
//			float y = ((eventY - y_m2)/height_scale + Y)/2;
//			EsspDrawImageActivity.size_line = (float) Math.sqrt(((eventX - x_m2)/width_scale - X)*((eventX - x_m2)/width_scale - X) + ((eventY - y_m2)/height_scale - Y)*((eventY - y_m2)/height_scale - Y));
//			EsspDrawImageActivity.createDialogRate(getContext(), x, y, true, true);
//			invalidate();
//			break;
//		default:
//			return false;
//		}
//		return true;
//	}
	
	/** Vẽ hình chữ nhật
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawRec(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_rec = new Paint();
		paint_rec.setAntiAlias(true);
		paint_rec.setStrokeWidth(EsspCommon.width_brush);
		paint_rec.setColor(EsspCommon.color);
		paint_rec.setStyle(Style.STROKE);
		paint_rec.setStrokeJoin(Paint.Join.ROUND);
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				path_rec.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (eventX - x_m2)/width_scale);
				map.put("y", (eventY - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_rec_show = new Path();
				path_rec_show.moveTo(X, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, Y);
				break;
			case MotionEvent.ACTION_UP:
				path_rec_show = new Path();
				path_rec.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, Y);
				paths.add(path_rec);
				paints.add(paint_rec);
				path_rec = new Path();
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(1);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (eventX - x_m2)/width_scale);
				map2.put("y", (eventY - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_rec);
				arr_type.add(6);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				m3.put("x", (xr2 + xr1)/2);
				m3.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_rec.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					
					paths.add(pos_layer, path_rec);
					path_rec = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	/** Vẽ hình quy định hộ dân
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawHo(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_rec = new Paint();
		paint_rec.setAntiAlias(true);
		paint_rec.setStrokeWidth(EsspCommon.width_brush);
		paint_rec.setColor(EsspCommon.color);
		paint_rec.setStyle(Style.STROKE);
		paint_rec.setStrokeJoin(Paint.Join.ROUND);
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				path_ho.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (event.getX() - x_m2)/width_scale);
				map.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_rec_show = new Path();
				path_rec_show.moveTo(X, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, Y);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) >= Math.abs((eventY - y_m2)/height_scale - Y)){
					// Vẽ >
					path_rec_show.lineTo(X + ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_rec_show.lineTo(X, (eventY - y_m2)/height_scale);
					// Vẽ <
					path_rec_show.moveTo((eventX - x_m2)/width_scale, Y);
					path_rec_show.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					// Vẽ -
					path_rec_show.moveTo(X + ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_rec_show.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
				} else {
					// Vẽ >
					path_rec_show.lineTo((X + (eventX - x_m2)/width_scale)/2, Y + ((eventY - y_m2)/height_scale - Y)/5);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y);
					// Vẽ <
					path_rec_show.moveTo(X, (eventY - y_m2)/height_scale);
					path_rec_show.lineTo((X + (eventX - x_m2)/width_scale)/2, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					// Vẽ -
					path_rec_show.moveTo((X + (eventX - x_m2)/width_scale)/2, Y + ((eventY - y_m2)/height_scale - Y)/5);
					path_rec_show.lineTo((X + (eventX - x_m2)/width_scale)/2, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5);
				}
				break;
			case MotionEvent.ACTION_UP:
				path_rec_show = new Path();
				path_ho.lineTo((eventX - x_m2)/width_scale, Y);
				path_ho.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_ho.lineTo(X, (eventY - y_m2)/height_scale);
				path_ho.lineTo(X, Y);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) >= Math.abs((eventY - y_m2)/height_scale - Y)){
					// Vẽ >
					path_ho.lineTo(X + ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_ho.lineTo(X, (eventY - y_m2)/height_scale);
					// Vẽ <
					path_ho.moveTo((eventX - x_m2)/width_scale, Y);
					path_ho.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_ho.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					// Vẽ -
					path_ho.moveTo(X + ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
					path_ho.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5, (Y + (eventY - y_m2)/height_scale)/2);
				} else {
					// Vẽ >
					path_ho.lineTo((X + (eventX - x_m2)/width_scale)/2, Y + ((eventY - y_m2)/height_scale - Y)/5);
					path_ho.lineTo((eventX - x_m2)/width_scale, Y);
					// Vẽ <
					path_ho.moveTo(X, (eventY - y_m2)/height_scale);
					path_ho.lineTo((X + (eventX - x_m2)/width_scale)/2, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5);
					path_ho.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
					// Vẽ -
					path_ho.moveTo((X + (eventX - x_m2)/width_scale)/2, Y + ((eventY - y_m2)/height_scale - Y)/5);
					path_ho.lineTo((X + (eventX - x_m2)/width_scale)/2, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5);
				}
				
				paths.add(path_ho);
				paints.add(paint_rec);
				path_ho = new Path();
				state_undos.add(1);
				pos_text.add(0);
				angle_text.add(0);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (eventX - x_m2)/width_scale);
				map2.put("y", (eventY - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_tram);
				arr_type.add(14);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				m3.put("x", (xr2 + xr1)/2);
				m3.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				break; 
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_ho.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_ho.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
					path_ho.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					path_ho.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
					path_ho.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					
					if(Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x")) >= Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))){
						// Vẽ >
						path_ho.lineTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(0).get("y") + arr_pos.get(1).get("y"))/2);
						path_ho.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
						// Vẽ <
						path_ho.moveTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
						path_ho.lineTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(0).get("y") + arr_pos.get(1).get("y"))/2);
						path_ho.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
						// Vẽ -
						path_ho.moveTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(0).get("y") + arr_pos.get(1).get("y"))/2);
						path_ho.lineTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(0).get("y") + arr_pos.get(1).get("y"))/2);
					} else {
						// Vẽ >
						path_ho.lineTo((arr_pos.get(0).get("x") + arr_pos.get(1).get("x"))/2, arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5);
						path_ho.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
						// Vẽ <
						path_ho.moveTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
						path_ho.lineTo((arr_pos.get(0).get("x") + arr_pos.get(1).get("x"))/2, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5);
						path_ho.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
						// Vẽ -
						path_ho.moveTo((arr_pos.get(0).get("x") + arr_pos.get(1).get("x"))/2, arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5);
						path_ho.lineTo((arr_pos.get(0).get("x") + arr_pos.get(1).get("x"))/2, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5);
					}
					
					paths.add(pos_layer, path_ho);
					path_ho = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	/** Vẽ hình quy định trạm biến áp
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawTram(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_rec = new Paint();
		paint_rec.setAntiAlias(true);
		paint_rec.setStrokeWidth(EsspCommon.width_brush);
		paint_rec.setColor(EsspCommon.color);
		paint_rec.setStyle(Style.STROKE);
		paint_rec.setStrokeJoin(Paint.Join.ROUND);
		
		paint_rec_show2.setAntiAlias(true);
		paint_rec_show2.setStrokeWidth(EsspCommon.width_brush);
		paint_rec_show2.setColor(EsspCommon.color);
		paint_rec_show2.setStyle(Style.FILL);
		paint_rec_show2.setStrokeJoin(Paint.Join.ROUND);
		
		Path path_rec2 = new Path();
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				path_rec.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (event.getX() - x_m2)/width_scale);
				map.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_rec_show = new Path();
				path_rec_show.moveTo(X, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, Y);
	
				path_rec_show2 = new Path();
				if(Math.abs((eventX - x_m2)/width_scale - X) >= Math.abs((eventY - y_m2)/height_scale - Y)){
					path_rec_show2.moveTo(X, Y);
					path_rec_show2.lineTo(((eventX - x_m2)/width_scale + X)/2, (eventY - y_m2)/height_scale);
					path_rec_show2.lineTo((eventX - x_m2)/width_scale, Y);
					path_rec_show2.lineTo(X, Y);
				} else {
					path_rec_show2.moveTo(X, Y);
					path_rec_show2.lineTo((eventX - x_m2)/width_scale, ((eventY - y_m2)/height_scale + Y)/2);
					path_rec_show2.lineTo(X, (eventY - y_m2)/height_scale);
					path_rec_show2.lineTo(X, Y);
				}
				break;
			case MotionEvent.ACTION_UP:
				path_rec_show2 = new Path();
				path_rec_show = new Path();
				path_rec.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, Y);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) >= Math.abs((eventY - y_m2)/height_scale - Y)){
					path_rec2.moveTo(X, Y);
					path_rec2.lineTo(((eventX - x_m2)/width_scale + X)/2, (eventY - y_m2)/height_scale);
					path_rec2.lineTo((eventX - x_m2)/width_scale, Y);
					path_rec2.lineTo(X, Y);
				} else {
					path_rec2.moveTo(X, Y);
					path_rec2.lineTo((eventX - x_m2)/width_scale, ((eventY - y_m2)/height_scale + Y)/2);
					path_rec2.lineTo(X, (eventY - y_m2)/height_scale);
					path_rec2.lineTo(X, Y);
				}
				
				paths.add(path_rec);
				paths.add(path_rec2);
				paints.add(paint_rec);
				paints.add(paint_rec_show2);
				path_rec = new Path();
				pos_text.add(0);
				angle_text.add(0);
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(2);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (eventX - x_m2)/width_scale);
				map2.put("y", (eventY - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_ho);
				arr_type.add(15);
				path_virtual.add(2);
				angles_rotate.add(0d);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				m3.put("x", (xr2 + xr1)/2);
				m3.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m3);
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					angles_rotate.remove(pos_layer + 1);
					angles_rotate.add(pos_layer + 1, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_rec.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					
					paths.add(pos_layer, path_rec);
					paths.remove(pos_layer + 1);
					path_rec2 = new Path();
					if(Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x")) >= Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))){
						path_rec2.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						path_rec2.lineTo((arr_pos.get(1).get("x") + arr_pos.get(0).get("x"))/2, arr_pos.get(1).get("y"));
						path_rec2.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
						path_rec2.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					} else {
						path_rec2.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
						path_rec2.lineTo(arr_pos.get(1).get("x"), (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2);
						path_rec2.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
						path_rec2.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					}
					
					paths.add(pos_layer + 1, path_rec2);
					path_rec = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1) / 2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					arr_pos_center_rotate.remove(pos_layer + 1);
//					LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
//					m3.put("x", (xr2 + xr1)/2);
//					m3.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer + 1, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	/** Vẽ hình quy định hộp công tơ
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawHop(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_rec = new Paint();
		paint_rec.setAntiAlias(true);
		paint_rec.setStrokeWidth(EsspCommon.width_brush);
		paint_rec.setColor(EsspCommon.color);
		paint_rec.setStyle(Style.STROKE);
		paint_rec.setStrokeJoin(Paint.Join.ROUND);
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				path_rec.moveTo(X, Y);
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (event.getX() - x_m2)/width_scale);
				map.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_rec_show = new Path();
				path_rec_show.moveTo(X, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec_show.lineTo(X, Y);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) <= Math.abs((eventY - y_m2)/height_scale - Y)){
					float x1 = X + ((eventX - x_m2)/width_scale - X)/5;
					float y1 = Y + ((eventY - y_m2)/height_scale - Y)/8;
					float x2 = (eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5;
					float y2 = (eventY - y_m2)/height_scale - 3*((eventY - y_m2)/height_scale - Y)/5;
					
					path_rec_show.moveTo(x1, y1);
					path_rec_show.lineTo(x2, y1);
					path_rec_show.lineTo(x2, y2);
					path_rec_show.lineTo(x1, y2);
					path_rec_show.lineTo(x1, y1);
					
					path_rec_show.moveTo(X, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					
					path_rec_show.moveTo(X + ((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo(X + ((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec_show.moveTo(X + 2*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo(X + 2*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec_show.moveTo(X + 3*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo(X + 3*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec_show.moveTo(X + 4*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo(X + 4*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec_show.moveTo(X + 5*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec_show.lineTo(X + 5*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
				} else {
					float x1 = X + ((eventX - x_m2)/width_scale - X)/8;
					float y1 = Y + ((eventY - y_m2)/height_scale - Y)/5;
					float x2 = (eventX - x_m2)/width_scale - 3*((eventX - x_m2)/width_scale - X)/5;
					float y2 = (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5;
					
					path_rec_show.moveTo(x1, y1);
					path_rec_show.lineTo(x2, y1);
					path_rec_show.lineTo(x2, y2);
					path_rec_show.lineTo(x1, y2);
					path_rec_show.lineTo(x1, y1);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y);
					path_rec_show.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, (eventY - y_m2)/height_scale);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + ((eventY - y_m2)/height_scale - Y)/6);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y + ((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 2*((eventY - y_m2)/height_scale - Y)/6);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y + 2*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 3*((eventY - y_m2)/height_scale - Y)/6);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y + 3*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 4*((eventY - y_m2)/height_scale - Y)/6);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y + 4*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec_show.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 5*((eventY - y_m2)/height_scale - Y)/6);
					path_rec_show.lineTo((eventX - x_m2)/width_scale, Y + 5*((eventY - y_m2)/height_scale - Y)/6);
				}
				break;
			case MotionEvent.ACTION_UP:
				path_rec_show = new Path();
				path_rec.lineTo((eventX - x_m2)/width_scale, Y);
				path_rec.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, (eventY - y_m2)/height_scale);
				path_rec.lineTo(X, Y);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) <= Math.abs((eventY - y_m2)/height_scale - Y)){
					float x1 = X + ((eventX - x_m2)/width_scale - X)/5;
					float y1 = Y + ((eventY - y_m2)/height_scale - Y)/8;
					float x2 = (eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/5;
					float y2 = (eventY - y_m2)/height_scale - 3*((eventY - y_m2)/height_scale - Y)/5;
					
					path_rec.moveTo(x1, y1);
					path_rec.lineTo(x2, y1);
					path_rec.lineTo(x2, y2);
					path_rec.lineTo(x1, y2);
					path_rec.lineTo(x1, y1);
					
					path_rec.moveTo(X, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					
					path_rec.moveTo(X + ((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo(X + ((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec.moveTo(X + 2*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo(X + 2*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec.moveTo(X + 3*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo(X + 3*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec.moveTo(X + 4*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo(X + 4*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
					
					path_rec.moveTo(X + 5*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/8);
					path_rec.lineTo(X + 5*((eventX - x_m2)/width_scale - X)/6, (eventY - y_m2)/height_scale);
				} else {
					float x1 = X + ((eventX - x_m2)/width_scale - X)/8;
					float y1 = Y + ((eventY - y_m2)/height_scale - Y)/5;
					float x2 = (eventX - x_m2)/width_scale - 3*((eventX - x_m2)/width_scale - X)/5;
					float y2 = (eventY - y_m2)/height_scale - ((eventY - y_m2)/height_scale - Y)/5;
					
					path_rec.moveTo(x1, y1);
					path_rec.lineTo(x2, y1);
					path_rec.lineTo(x2, y2);
					path_rec.lineTo(x1, y2);
					path_rec.lineTo(x1, y1);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y);
					path_rec.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, (eventY - y_m2)/height_scale);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + ((eventY - y_m2)/height_scale - Y)/6);
					path_rec.lineTo((eventX - x_m2)/width_scale, Y + ((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 2*((eventY - y_m2)/height_scale - Y)/6);
					path_rec.lineTo((eventX - x_m2)/width_scale, Y + 2*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 3*((eventY - y_m2)/height_scale - Y)/6);
					path_rec.lineTo((eventX - x_m2)/width_scale, Y + 3*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 4*((eventY - y_m2)/height_scale - Y)/6);
					path_rec.lineTo((eventX - x_m2)/width_scale, Y + 4*((eventY - y_m2)/height_scale - Y)/6);
					
					path_rec.moveTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/8, Y + 5*((eventY - y_m2)/height_scale - Y)/6);
					path_rec.lineTo((eventX - x_m2)/width_scale, Y + 5*((eventY - y_m2)/height_scale - Y)/6);
				}
				
				paths.add(path_rec);
				paints.add(paint_rec);
				path_rec = new Path();
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(1);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (eventX - x_m2)/width_scale);
				map2.put("y", (eventY - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_hop);
				arr_type.add(16);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				m3.put("x", (xr2 + xr1)/2);
				m3.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_rec.moveTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y"));
					path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y"));
					path_rec.lineTo(arr_pos.get(0).get("x"), arr_pos.get(0).get("y"));
					
					if(Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x")) <= Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))){
						float x1 = arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5;
						float y1 = arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8;
						float x2 = arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5;
						float y2 = arr_pos.get(1).get("y") - 3*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5;
						
						path_rec.moveTo(x1, y1);
						path_rec.lineTo(x2, y1);
						path_rec.lineTo(x2, y2);
						path_rec.lineTo(x1, y2);
						path_rec.lineTo(x1, y1);
						
						path_rec.moveTo(arr_pos.get(0).get("x"), arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						
						path_rec.moveTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y"));
						
						path_rec.moveTo(arr_pos.get(0).get("x") + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(0).get("x") + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y"));
						
						path_rec.moveTo(arr_pos.get(0).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(0).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y"));
						
						path_rec.moveTo(arr_pos.get(0).get("x") + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(0).get("x") + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y"));
						
						path_rec.moveTo(arr_pos.get(0).get("x") + 5*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/8);
						path_rec.lineTo(arr_pos.get(0).get("x") + 5*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/6, arr_pos.get(1).get("y"));
					} else {
						float x1 = arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8;
						float y1 = arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5;
						float x2 = arr_pos.get(1).get("x") - 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5;
						float y2 = arr_pos.get(1).get("y") - (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/5;
						
						path_rec.moveTo(x1, y1);
						path_rec.lineTo(x2, y1);
						path_rec.lineTo(x2, y2);
						path_rec.lineTo(x1, y2);
						path_rec.lineTo(x1, y1);
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y"));
						path_rec.lineTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(1).get("y"));
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y") + (arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y") + 2*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y") + 2*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y") + 3*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y") + 3*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y") + 4*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y") + 4*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						
						path_rec.moveTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/8, arr_pos.get(0).get("y") + 5*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
						path_rec.lineTo(arr_pos.get(1).get("x"), arr_pos.get(0).get("y") + 5*(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/6);
					}
					
					paths.add(pos_layer, path_rec);
					path_rec = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	/** Vẽ hình tròn có chấm ở tâm
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawCicle(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		paint_cicle_dot.setColor(EsspCommon.color);
		
		Paint paint_cicle = new Paint();
		paint_cicle.setAntiAlias(true);
		paint_cicle.setStrokeWidth(EsspCommon.width_brush);
		paint_cicle.setColor(EsspCommon.color);
		paint_cicle.setStyle(Style.STROKE);
		paint_cicle.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_cicle_dot = new Paint();
		paint_cicle_dot.setAntiAlias(true);
		paint_cicle_dot.setStrokeWidth(EsspCommon.width_brush);
		paint_cicle_dot.setColor(EsspCommon.color);
		paint_cicle_dot.setStyle(Style.FILL);
		paint_cicle_dot.setStrokeJoin(Paint.Join.ROUND);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			X = (event.getX() - x_m2)/width_scale;
			Y = (event.getY() - y_m2)/height_scale;
			return true;
		case MotionEvent.ACTION_MOVE:
			path_cicle_show = new Path();
			path_cicle_dot_show = new Path();
			path_cicle_show.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
			path_cicle_dot_show.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/30), Direction.CW);
			break;
		case MotionEvent.ACTION_UP:
			path_cicle_show = new Path();
			path_cicle_dot_show = new Path();
			path_cicle.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
			path_cicle_dot.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/30), Direction.CW);
			paths.add(path_cicle);
			paths.add(path_cicle_dot);
			paints.add(paint_cicle);
			paints.add(paint_cicle_dot);
			path_cicle = new Path();
			path_cicle_dot = new Path();
			state_undos.add(2);
			invalidate();
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Vẽ hình quy định công tơ
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawCongTo(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		paint_brush2.setAntiAlias(true);
		paint_brush2.setStrokeWidth(EsspCommon.width_brush);
		paint_brush2.setColor(EsspCommon.color);
		paint_brush2.setStyle(Style.STROKE);
		paint_brush2.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_cicle2 = new Paint();
		paint_cicle2.setAntiAlias(true);
		paint_cicle2.setStrokeWidth(EsspCommon.width_brush);
		paint_cicle2.setColor(EsspCommon.color);
		paint_cicle2.setStyle(Style.STROKE);
		paint_cicle2.setStrokeJoin(Paint.Join.ROUND);
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (event.getX() - x_m2)/width_scale);
				map.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_ct_show = new Path();
				path_ct_show.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) <= Math.abs((eventY - y_m2)/height_scale - Y)){
					path_ct_show.addCircle(X + ((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 2*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 3*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 4*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_ct_show.addCircle(X + ((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 2*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 3*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle(X + 4*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_ct_show.moveTo(X + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*Math.abs(((eventX - x_m2)/width_scale - X)/2));
					path_ct_show.lineTo(X + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/10)));
					path_ct_show.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/10)));
					path_ct_show.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*Math.abs(((eventX - x_m2)/width_scale - X)/2));
				} else {
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + ((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 2*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 3*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 4*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + ((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 2*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 3*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_ct_show.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 4*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_ct_show.moveTo((eventX - x_m2)/width_scale , ((eventY - y_m2)/height_scale + Y)/2 - Math.abs((eventX - x_m2)/width_scale - X)/2 + Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_ct_show.lineTo((eventX - x_m2)/width_scale + 3*((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - Math.abs((eventX - x_m2)/width_scale - X)/2 + Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_ct_show.lineTo((eventX - x_m2)/width_scale + 3*((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + Math.abs((eventX - x_m2)/width_scale - X)/2 - Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_ct_show.lineTo((eventX - x_m2)/width_scale , ((eventY - y_m2)/height_scale + Y)/2 + Math.abs((eventX - x_m2)/width_scale - X)/2 - Math.abs(((eventX - x_m2)/width_scale - X)/10));
				}
				break;
			case MotionEvent.ACTION_UP:
				path_ct_show = new Path();
				path_cicle.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
				
//				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
//				map.put("x", Math.abs(((eventX - x_m2)/width_scale + X)/2) - Math.abs(((eventX - x_m2)/width_scale - X)/2));
//				map.put("y", Math.abs(((eventY - y_m2)/height_scale + Y)/2) - Math.abs(((eventX - x_m2)/width_scale - X)/2));
//				arr.add(map);
				
				if(Math.abs((eventX - x_m2)/width_scale - X) <= Math.abs((eventY - y_m2)/height_scale - Y)){
					path_cicle.addCircle(X + ((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 2*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 3*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 4*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_cicle.addCircle(X + ((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 2*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 3*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle(X + 4*((eventX - x_m2)/width_scale - X)/5, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + (5/2)*Math.abs(((eventX - x_m2)/width_scale - X)/2)/5), Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_cicle.moveTo(X + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*Math.abs(((eventX - x_m2)/width_scale - X)/2));
					path_cicle.lineTo(X + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/10)));
					path_cicle.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*(Math.abs(((eventX - x_m2)/width_scale - X)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/10)));
					path_cicle.lineTo((eventX - x_m2)/width_scale - ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + (Math.abs((eventY - y_m2)/height_scale - Y)/((eventY - y_m2)/height_scale - Y))*Math.abs(((eventX - x_m2)/width_scale - X)/2));
					
//					LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
//					map2.put("x", Math.abs(((eventX - x_m2)/width_scale + X)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2));
//					map2.put("y", Math.abs(((eventY - y_m2)/height_scale + Y)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/2)/2);
//					arr.add(map2);
				} else {
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + ((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 2*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 3*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 4*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + ((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 2*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 3*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					path_cicle.addCircle((eventX - x_m2)/width_scale + (((eventX - x_m2)/width_scale - X)/2)/5 + ((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - ((eventX - x_m2)/width_scale - X)/2 + 4*((eventX - x_m2)/width_scale - X)/5, Math.abs(((eventX - x_m2)/width_scale - X)/22), Direction.CW);
					
					path_cicle.moveTo((eventX - x_m2)/width_scale , ((eventY - y_m2)/height_scale + Y)/2 - Math.abs((eventX - x_m2)/width_scale - X)/2 + Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_cicle.lineTo((eventX - x_m2)/width_scale + 3*((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 - Math.abs((eventX - x_m2)/width_scale - X)/2 + Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_cicle.lineTo((eventX - x_m2)/width_scale + 3*((eventX - x_m2)/width_scale - X)/10, ((eventY - y_m2)/height_scale + Y)/2 + Math.abs((eventX - x_m2)/width_scale - X)/2 - Math.abs(((eventX - x_m2)/width_scale - X)/10));
					path_cicle.lineTo((eventX - x_m2)/width_scale , ((eventY - y_m2)/height_scale + Y)/2 + Math.abs((eventX - x_m2)/width_scale - X)/2 - Math.abs(((eventX - x_m2)/width_scale - X)/10));
					
//					LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
//					map2.put("x", Math.abs(((eventX - x_m2)/width_scale + X)/2) + 3*Math.abs(((eventX - x_m2)/width_scale - X)/2)/2);
//					map2.put("y", Math.abs(((eventY - y_m2)/height_scale + Y)/2) + Math.abs(((eventX - x_m2)/width_scale - X)/2));
//					arr.add(map2);
				}
				
				paths.add(path_cicle);
				paints.add(paint_cicle2);
				path_cicle = new Path();
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(1);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (event.getX() - x_m2)/width_scale);
				map2.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_congto);
				arr_type.add(17);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				if(yr2 - yr1 >= xr2 - xr1){
					m3.put("x", (xr2 + xr1)/2);
					m3.put("y", (((yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10))) + ((yr2 + yr1)/2 - (xr2 - xr1)/2))/2);
				} else {
					m3.put("x", ((xr2 + 3*(xr2 - xr1)/10) + ((xr2 + xr1)/2 - (xr2 - xr1)/2))/2);
					m3.put("y", (((yr2 + yr1)/2 + (xr2 - xr1)/2) + ((yr2 + yr1)/2 - (xr2 - xr1)/2))/2);
				}
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr1);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", yr2);
				arr_point.add(p2);
				p2 = new LinkedHashMap<String, Float>();
				p2.put("x", xr1);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_cicle.addCircle(Math.abs((arr_pos.get(1).get("x") + arr_pos.get(0).get("x"))/2), Math.abs((arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2), Direction.CW);
					
					if(Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x")) <= Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))){
						path_cicle.addCircle(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						
						path_cicle.addCircle(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + (5/2)*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + (5/2)*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + (5/2)*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(0).get("x") + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + (5/2)*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						
						path_cicle.moveTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2));
						path_cicle.lineTo(arr_pos.get(0).get("x") + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + 3*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10)));
						path_cicle.lineTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*(Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2) + 3*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10)));
						path_cicle.lineTo(arr_pos.get(1).get("x") - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + (Math.abs(arr_pos.get(1).get("y") - arr_pos.get(0).get("y"))/(arr_pos.get(1).get("y") - arr_pos.get(0).get("y")))*Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2));
					} else {
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 2*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						path_cicle.addCircle(arr_pos.get(1).get("x") + ((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2)/5 + (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - (arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + 4*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/5, Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/22), Direction.CW);
						
						path_cicle.moveTo(arr_pos.get(1).get("x") , (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10));
						path_cicle.lineTo(arr_pos.get(1).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 - Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 + Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10));
						path_cicle.lineTo(arr_pos.get(1).get("x") + 3*(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10, (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 - Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10));
						path_cicle.lineTo(arr_pos.get(1).get("x") , (arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2 + Math.abs(arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2 - Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/10));
					}
					
					paths.add(pos_layer, path_cicle);
					path_cicle = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}
	
	/** Vẽ hình tròn
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawCicleB(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_cicle2 = new Paint();
		paint_cicle2.setAntiAlias(true);
		paint_cicle2.setStrokeWidth(EsspCommon.width_brush);
		paint_cicle2.setColor(EsspCommon.color);
		paint_cicle2.setStyle(Style.STROKE);
		paint_cicle2.setStrokeJoin(Paint.Join.ROUND);
		
		if(!isMove){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				X = (event.getX() - x_m2)/width_scale;
				Y = (event.getY() - y_m2)/height_scale;
				
				LinkedHashMap<String, Float> map = new LinkedHashMap<String, Float>();
				map.put("x", (event.getX() - x_m2)/width_scale);
				map.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map);
				return true;
			case MotionEvent.ACTION_MOVE:
				path_cicle_show = new Path();
				path_cicle_show.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
				break;
			case MotionEvent.ACTION_UP:
				path_cicle_show = new Path();
				path_cicle.addCircle(Math.abs(((eventX - x_m2)/width_scale + X)/2), Math.abs(((eventY - y_m2)/height_scale + Y)/2), Math.abs(((eventX - x_m2)/width_scale - X)/2), Direction.CW);
				paths.add(path_cicle);
				paints.add(paint_cicle2);
				path_cicle = new Path();
				pos_text.add(0);
				angle_text.add(0);
				state_undos.add(1);
				invalidate();
				
				LinkedHashMap<String, Float> map2 = new LinkedHashMap<String, Float>();
				map2.put("x", (event.getX() - x_m2)/width_scale);
				map2.put("y", (event.getY() - y_m2)/height_scale);
				arr.add(map2);
				arr_pixel.add(arr);
				LinkedHashMap<String, Float> m1 = arr.get(0);
				LinkedHashMap<String, Float> m2 = arr.get(1);
				LinkedHashMap<String, Float> m_center = new LinkedHashMap<String, Float>();
				m_center.put("x", (m1.get("x") + m2.get("x"))/2);
				m_center.put("y", (m1.get("y") + m2.get("y"))/2);
				arr_pos_center.add(m_center);
				arr = new ArrayList<LinkedHashMap<String,Float>>();
				
				arr_image.add(0, R.mipmap.ic_cicle_a);
				arr_type.add(8);
				path_virtual.add(1);
				angles_rotate.add(0d);
				arr_check_rate.add(0);
				arr_check_rate_bool.add(false);
				EsspDrawImageActivity.setListLayer(getContext());
				
				ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
				arr_pos = arr_pixel.get(arr_pixel.size() - 1);
				xr1 = arr_pos.get(0).get("x");
				xr2 = arr_pos.get(1).get("x");
				yr1 = arr_pos.get(0).get("y");
				yr2 = arr_pos.get(1).get("y");
				LinkedHashMap<String, Float> m3 = new LinkedHashMap<String, Float>();
				m3.put("x", (xr2 + xr1)/2);
				m3.put("y", (yr2 + yr1)/2);
				arr_pos_center_rotate.add(m3);
				
				LinkedHashMap<String, Float> p2 = new LinkedHashMap<String, Float>();
				p2.put("x", (xr2 + xr1)/2);
				p2.put("y", (yr2 + yr1)/2);
				arr_point.add(p2);
				arr_snap_point.add(arr_point);
				arr_point = new ArrayList<LinkedHashMap<String, Float>>();
				
				break;
			default:
				return false;
			}
		} else {
			if(isRotate){
				double x_tam = (xr2 + xr1)/2;
				double y_tam = (yr2 + yr1)/2;
				double goc = 0d;
				double goc_kt = 0d;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_bt = 90;
						} else {
							goc_bt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_bt = 0;
						} else {
							goc_bt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_bt = goc_bt%360;
					int pos_layer1 = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer1 += path_virtual.get(i);
					}
					goc_cu = angles_rotate.get(pos_layer1);
					break;
				case MotionEvent.ACTION_MOVE:
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					angles_rotate.remove(pos_layer);
					if(event.getX() == x_tam){
						if(event.getY() > y_tam){
							goc_kt = 90;
						} else {
							goc_kt = 270;
						}
					} else if(event.getY() == y_tam){
						if(event.getX() > x_tam){
							goc_kt = 0;
						} else {
							goc_kt = 180;
						}
					} else if(event.getX() > x_tam){
						goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					} else if(event.getX() < x_tam){
						goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
					}
					goc_kt = goc_kt%360;
					goc = goc_kt - goc_bt + goc_cu;
					angles_rotate.add(pos_layer, goc%360);
					invalidate();
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				}
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_MOVE:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					float x_move = (eventX - x_m2)/width_scale - x_move_obj;
					float y_move = (eventY - y_m2)/height_scale - y_move_obj;
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
					for (int i = 0; i < arr_pos.size(); i++) {
						LinkedHashMap<String, Float> map = arr_pos.get(i);
						float x_new = map.get("x") + x_move;
						float y_new = map.get("y") + y_move;
						map.put("x", x_new);
						map.put("y", y_new);
					}
					
					int pos_layer = 0;
					for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
						pos_layer += path_virtual.get(i);
					}
					ArrayList<LinkedHashMap<String, Float>> arr_point = new ArrayList<LinkedHashMap<String, Float>>();
					arr_point = arr_snap_point.get(pos_layer);
					if(arr_point != null){
						for (int i = 0; i < arr_point.size(); i++) {
							LinkedHashMap<String, Float> p = arr_point.get(i);
							p.put("x", p.get("x") + x_move);
							p.put("y", p.get("y") + y_move);
						}
					}
					paths.remove(pos_layer);
					
					path_cicle.addCircle(Math.abs((arr_pos.get(1).get("x") + arr_pos.get(0).get("x"))/2), Math.abs((arr_pos.get(1).get("y") + arr_pos.get(0).get("y"))/2), Math.abs((arr_pos.get(1).get("x") - arr_pos.get(0).get("x"))/2), Direction.CW);
					
					paths.add(pos_layer, path_cicle);
					path_cicle = new Path();
					invalidate();
					
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					drawRecSelect();
					
					x_move_obj = (eventX - x_m2)/width_scale;
					y_move_obj = (eventY - y_m2)/height_scale;
					
					arr_pos_center_rotate.remove(pos_layer);
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(pos_layer, m2);
					break;
				case MotionEvent.ACTION_UP:
					
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}

	/** Vẽ dấu chấm
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawDot(MotionEvent event, float eventX, float eventY){
		mCanvas = new Canvas();
		paint_brush.setAntiAlias(true);
		paint_brush.setStrokeWidth(EsspCommon.width_brush);
		paint_brush.setColor(EsspCommon.color);
		paint_brush.setStyle(Style.STROKE);
		paint_brush.setStrokeJoin(Paint.Join.ROUND);
		
		Paint paint_dot = new Paint();
		paint_dot.setAntiAlias(true);
		paint_dot.setStrokeWidth(EsspCommon.width_brush);
		paint_dot.setColor(EsspCommon.color);
		paint_dot.setStyle(Style.FILL);
		paint_dot.setStrokeJoin(Paint.Join.ROUND);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path_dot.addCircle((eventX - x_m2)/width_scale, (eventY - y_m2)/height_scale, 5, Direction.CW);
			paths.add(path_dot);
			paints.add(paint_dot);
			path_dot = new Path();
			state_undos.add(1);
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			return false;
		}
		return true;
	}
	
	/** Gọi hộp thoại nhập text để vẽ text lên màn hình
	 * @param event
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean drawText(MotionEvent event, float eventX, float eventY){
		try{
			if(!isMove){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					text_show = EsspDrawImageActivity.text;
					x_show = (eventX - x_m2)/width_scale;
					y_show = (eventY - y_m2)/height_scale;
					return true;
				case MotionEvent.ACTION_MOVE:
					text_show = EsspDrawImageActivity.text;
					x_show = (eventX - x_m2)/width_scale;
					y_show = (eventY - y_m2)/height_scale;
					break;
				case MotionEvent.ACTION_UP:
					xr1 = width;
					xr2 = 0f;
					yr1 = height;
					yr2 = 0f;
					addText(eventX, eventY);
					
					Rect bounds = new Rect();
					paints_text.get(paints_text.size() - 1).getTextBounds(EsspDrawImageActivity.text, 0, EsspDrawImageActivity.text.length(), bounds);
					
					LinkedHashMap<String, Float> map3 = new LinkedHashMap<String, Float>();
					map3.put("x", (eventX - bounds.width()/4 - x_m2)/width_scale);
					map3.put("y", (eventY - bounds.height() - y_m2)/height_scale);
					arr.add(map3);
					map3 = new LinkedHashMap<String, Float>();
					map3.put("x", (eventX + 3*bounds.width()/4 - x_m2)/width_scale);
					map3.put("y", (eventY - y_m2)/height_scale);
					arr.add(map3);
					map3 = new LinkedHashMap<String, Float>();
					map3.put("x", (eventX - x_m2)/width_scale);
					map3.put("y", (eventY - y_m2)/height_scale);
					arr.add(map3);
					arr_pixel.add(arr);
					arr = new ArrayList<LinkedHashMap<String,Float>>();
					
					arr_image.add(0, R.mipmap.ic_text);
					arr_type.add(10);
					path_virtual.add(1);
					angles_rotate.add(0d);
					arr_check_rate.add(0);
					arr_check_rate_bool.add(false);
					EsspDrawImageActivity.setListLayer(getContext());
					
					ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
					arr_pos = arr_pixel.get(arr_pixel.size() - 1);
					xr1 = arr_pos.get(0).get("x");
					xr2 = arr_pos.get(1).get("x");
					yr1 = arr_pos.get(0).get("y");
					yr2 = arr_pos.get(1).get("y");
					LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
					m2.put("x", (xr2 + xr1)/2);
					m2.put("y", (yr2 + yr1)/2);
					arr_pos_center_rotate.add(m2);
					pos_x_text_rotate.add((xr2 + xr1)/2);
					pos_y_text_rotate.add((yr2 + yr1)/2);
					text_show = "";
					break;
				default:
					return false;
				}
			} else {
				if(isRotate){
					double x_tam = (xr2 + xr1)/2;
					double y_tam = (yr2 + yr1)/2;
					double goc = 0d;
					double goc_bt = 0d;
					double goc_kt = 0d;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_bt = 90;
							} else {
								goc_bt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_bt = 0;
							} else {
								goc_bt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_bt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_bt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_bt = goc_bt%360;
						break;
					case MotionEvent.ACTION_MOVE:
						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}
						angles_rotate.remove(pos_layer);
						if(event.getX() == x_tam){
							if(event.getY() > y_tam){
								goc_kt = 90;
							} else {
								goc_kt = 270;
							}
						} else if(event.getY() == y_tam){
							if(event.getX() > x_tam){
								goc_kt = 0;
							} else {
								goc_kt = 180;
							}
						} else if(event.getX() > x_tam){
							goc_kt = 360 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						} else if(event.getX() < x_tam){
							goc_kt = 180 + Math.atan((event.getY() - y_tam)/(event.getX() - x_tam))*(180 / Math.PI);
						}
						goc_kt = goc_kt%360;
						goc = goc_kt - goc_bt;
						angles_rotate.add(pos_layer, goc%360);
						angles.remove((int)(angle_text.get(pos_layer)));
						angles.add((int)(angle_text.get(pos_layer)), (int)(goc%360));
						invalidate();
						break;
					case MotionEvent.ACTION_UP:
						
						break;
					}
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						break;
					case MotionEvent.ACTION_MOVE:
						xr1 = width;
						xr2 = 0f;
						yr1 = height;
						yr2 = 0f;
						float x_move = (eventX - x_m2)/width_scale - x_move_obj;
						float y_move = (eventY - y_m2)/height_scale - y_move_obj;
						ArrayList<LinkedHashMap<String, Float>> arr_pos = new ArrayList<LinkedHashMap<String,Float>>();
						arr_pos = arr_pixel.get(EsspDrawImageActivity.pos_layer);
						for (int i = 0; i < arr_pos.size(); i++) {
							LinkedHashMap<String, Float> map = arr_pos.get(i);
							float x_new = map.get("x") + x_move;
							float y_new = map.get("y") + y_move;
							map.put("x", x_new);
							map.put("y", y_new);
						}
						
						int pos_layer = 0;
						for (int i = 0; i < EsspDrawImageActivity.pos_layer; i++) {
							pos_layer += path_virtual.get(i);
						}
						pos_x_text.remove((int)(pos_text.get(pos_layer)));
						pos_y_text.remove((int)(pos_text.get(pos_layer)));
						addText2((int)(pos_text.get(pos_layer)), arr_pos.get(2).get("x"), arr_pos.get(2).get("y"));
						
						invalidate();
						
						xr1 = arr_pos.get(0).get("x");
						xr2 = arr_pos.get(1).get("x");
						yr1 = arr_pos.get(0).get("y");
						yr2 = arr_pos.get(1).get("y");
						drawRecSelect();
						
						pos_x_text.remove((int)(pos_text.get(pos_layer)));
						pos_y_text.remove((int)(pos_text.get(pos_layer)));
						pos_x_text.add(pos_text.get(pos_layer), arr_pos.get(arr_pos.size() - 1).get("x"));
						pos_y_text.add(pos_text.get(pos_layer), arr_pos.get(arr_pos.size() - 1).get("y"));
						
						pos_x_text_rotate.remove((int)(pos_text.get(pos_layer)));
						pos_y_text_rotate.remove((int)(pos_text.get(pos_layer)));
						pos_x_text_rotate.add((int)(pos_text.get(pos_layer)), (xr2 + xr1)/2);
						pos_y_text_rotate.add((int)(pos_text.get(pos_layer)), (yr2 + yr1)/2);
						
						x_move_obj = (eventX - x_m2)/width_scale;
						y_move_obj = (eventY - y_m2)/height_scale;
						
						arr_pos_center_rotate.remove(pos_layer);
						LinkedHashMap<String, Float> m2 = new LinkedHashMap<String, Float>();
						m2.put("x", (xr2 + xr1)/2);
						m2.put("y", (yr2 + yr1)/2);
						arr_pos_center_rotate.add(pos_layer, m2);
						break;
					case MotionEvent.ACTION_UP:
						
						break;
					default:
						return false;
					}
				}
			}
			invalidate();
			return true;
		} catch(Exception ex) {
			ex.toString();
			return false;
		}
	}
	
	/** Vẽ text lên màn hình
	 * @param eventX
	 * @param eventY
	 */
	public void addText(float eventX, float eventY){
		Paint paint_text = new Paint();
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(EsspCommon.color);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		
		string_text.add(EsspDrawImageActivity.text);
		pos_x_text.add((eventX - x_m2)/width_scale);
		pos_y_text.add((eventY - y_m2)/height_scale);
		pos_text.add(pos_x_text.size() - 1);
		angle_text.add(pos_x_text.size() - 1);
		paths.add(null);
		paints.add(paint_text);
		paints_text.add(paint_text);
		angles.add(0);
		state_undos.add(1);
		invalidate();
	}

	public void addText2(float eventX, float eventY){
		Paint paint_text = new Paint();
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(EsspCommon.color);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);

		string_text.add(EsspDrawImageActivity.text);
		pos_x_text.add((eventX - x_m2)/width_scale);
		pos_y_text.add((eventY - y_m2)/height_scale);
		pos_text.add(pos_x_text.size() - 1);
		angle_text.add(pos_x_text.size() - 1);
		paths.add(null);
		paints.add(paint_text);
		paints_text.add(paint_text);
		angles.add(0);
		state_undos.add(1);
//		invalidate();
	}
	
	public void addText2(int pos, float eventX, float eventY){
		pos_x_text.add(pos, (eventX - x_m2)/width_scale);
		pos_y_text.add(pos, (eventY - y_m2)/height_scale);
	}
	
	/** Vẽ text đo tỷ lệ
	 * @param eventX
	 * @param eventY
	 */
	public void addTextRate(float eventX, float eventY){
		Paint paint_text = new Paint();
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(Color.RED);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		paint_text.setFakeBoldText(true);// chữ đậm
		
		string_text.add(EsspDrawImageActivity.text);
		pos_x_text.add((eventX - x_m2)/width_scale);
		pos_y_text.add((eventY - y_m2)/height_scale);
		pos_text.add(pos_x_text.size() - 1);
		angle_text.add(pos_x_text.size() - 1);
		paths.add(null);
		paints.add(paint_text);
		paints_text.add(paint_text);
//		angles.add(Common.angle);
		angles.add(0);
		invalidate();
	}
	
	/** Xóa text
	 * 
	 */
	public void removeText(){
		string_text.remove(string_text.size() - 1);
		pos_x_text.remove(pos_x_text.size() - 1);
		pos_y_text.remove(pos_y_text.size() - 1);
		paths.remove(paths.size() - 1);
		paints.remove(paints.size() - 1);
		paints_text.remove(paints_text.size() - 1);
		angles.remove(angles.size() - 1);
	}
	
	/** Vẽ text thể hiện tỷ lệ
	 * @param eventX
	 * @param eventY
	 */
	public void addRate(float eventX, float eventY){
		paint_text.setAntiAlias(true);
		paint_text.setStrokeWidth(EsspCommon.width_brush);
		paint_text.setColor(Color.RED);
		paint_text.setStrokeJoin(Paint.Join.ROUND);
		paint_text.setStyle(Style.FILL);
		paint_text.setTextSize(25);
		paint_text.setFakeBoldText(true);// chữ đậm
		
		text_rate = (float)Math.round(EsspDrawImageActivity.size_rate * 10)/10 + " cm";
		x_rate = eventX;
		y_rate = eventY;
		invalidate();
	}
	
	/** Vẽ text quay 90 độ
	 * @param eventX
	 * @param eventY
	 */
	public void addTextA90(float eventX, float eventY){
		Paint paint_text_A90 = new Paint();
		paint_text_A90.setAntiAlias(true);
		paint_text_A90.setStrokeWidth(EsspCommon.width_brush);
		paint_text_A90.setColor(EsspCommon.color);
		paint_text_A90.setStrokeJoin(Paint.Join.ROUND);
		paint_text_A90.setStyle(Style.FILL);
		paint_text_A90.setTextSize(25);
		
		string_text.add(EsspDrawImageActivity.text);
		pos_x_text.add(eventX);
		pos_y_text.add(eventY);
		paths.add(null);
		paints.add(paint_text_A90);
		paints_text.add(paint_text_A90);
		state_undos.add(1);
		invalidate();
	}
	
	public void drawRecSelect(){
		try{
			paint_rec_select.setAntiAlias(true);
			paint_rec_select.setStrokeWidth(2f);
			paint_rec_select.setColor(Color.RED);
			paint_rec_select.setStyle(Style.STROKE);
			paint_rec_select.setStrokeJoin(Paint.Join.ROUND);
//			paint_rec_select.setPathEffect(new DashPathEffect(new float[] {10,10}, 0));
			
			paint_rec_select2.setAntiAlias(true);
			paint_rec_select2.setStrokeWidth(2f);
			paint_rec_select2.setColor(Color.RED);
			paint_rec_select2.setStyle(Style.FILL);
			paint_rec_select2.setStrokeJoin(Paint.Join.ROUND);

			if(EsspCommon.state_draw == 8){// Vẽ ô vuông quanh hình tròn
				path_Rec_Select = new Path();
				path_Rec_Select.addRect(xr1 - 2, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2, xr2 + 3, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3, Direction.CW);
				
				path_Rec_Select2 = new Path();
				path_Rec_Select2.addRect(xr1 - 2 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, xr1 - 2 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 + 3 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, xr2 + 3 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr1 - 2 - 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 - 5, xr1 - 2 + 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 + 3 - 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 - 5, xr2 + 3 + 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 + 5, Direction.CW);
			} else if(EsspCommon.state_draw == 17) {// Vẽ ô vuông quanh công tơ
				if(yr2 - yr1 >= xr2 - xr1){
					path_Rec_Select = new Path();
					path_Rec_Select.addRect(xr1 - 2, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2, xr2 + 3, (yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10)) + 3, Direction.CW);
					
					path_Rec_Select2 = new Path();
					path_Rec_Select2.addRect(xr1 - 2 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, xr1 - 2 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
					path_Rec_Select2.addRect(xr2 + 3 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, xr2 + 3 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
					path_Rec_Select2.addRect(xr1 - 2 - 5, (yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10)) + 3 - 5, xr1 - 2 + 5, (yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10)) + 3 + 5, Direction.CW);
					path_Rec_Select2.addRect(xr2 + 3 - 5, (yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10)) + 3 - 5, xr2 + 3 + 5, (yr2 + yr1)/2 + (Math.abs(yr2 - yr1)/(yr2 - yr1))*(Math.abs((xr2 - xr1)/2) + 3*Math.abs((xr2 - xr1)/10)) + 3 + 5, Direction.CW);
				} else {
					path_Rec_Select = new Path();
					path_Rec_Select.addRect((xr2 + xr1)/2 - (xr2 - xr1)/2 - 2, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2, xr2 + 3*(xr2 - xr1)/10 + 3, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3, Direction.CW);
					
					path_Rec_Select2 = new Path();
					path_Rec_Select2.addRect((xr2 + xr1)/2 - (xr2 - xr1)/2 - 2 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, (xr2 + xr1)/2 - (xr2 - xr1)/2 - 2 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
					path_Rec_Select2.addRect(xr2 + 3*(xr2 - xr1)/10 + 3 - 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 - 5, xr2 + 3*(xr2 - xr1)/10 + 3 + 5, (yr2 + yr1)/2 - (xr2 - xr1)/2 - 2 + 5, Direction.CW);
					path_Rec_Select2.addRect((xr2 + xr1)/2 - (xr2 - xr1)/2 - 2 - 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 - 5, (xr2 + xr1)/2 - (xr2 - xr1)/2 - 2 + 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 + 5, Direction.CW);
					path_Rec_Select2.addRect(xr2 + 3*(xr2 - xr1)/10 + 3 - 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 - 5, xr2 + 3*(xr2 - xr1)/10 + 3 + 5, (yr2 + yr1)/2 + (xr2 - xr1)/2 + 3 + 5, Direction.CW);
				}
			} else if(EsspCommon.state_draw == 18) {
				float tlx2 = 1f;
				float tly2 = 1f;
				float R = 15f;
				
				float doi2 = Math.abs(yr2 - yr1);
				float ke2 = Math.abs(xr2 - xr1);
				float huyen2 = (float) Math.sqrt((xr2 - xr1)*(xr2 - xr1) + (yr2 - yr1)*(yr2 - yr1)); 
				float sinA2 = doi2/huyen2;
				float cosA2 = ke2/huyen2;
				
				tlx2 = R * sinA2;
				tly2 = R * cosA2;
				
				if(yr2 > yr1){
					tlx2 = -tlx2;
				} else if(yr2 < yr1){
				} else {
					tlx2 = xr1;
				}
				if(xr2 > xr1){
				} else if(xr2 < xr1){
					tly2 = -tly2;
				} else {
					tly2 = yr1;
				}
				
				if(xr1 <= xr2){
					xr1 = xr1 - Math.abs(tlx2);
					xr2 = xr2 + Math.abs(tlx2);
				} else {
					xr1 = xr1 + Math.abs(tlx2);
					xr2 = xr2 - Math.abs(tlx2);
				}
				
				if(yr1 <= yr2){
					yr1 = yr1 - Math.abs(tly2);
					yr2 = yr2 + Math.abs(tly2);
				} else {
					yr1 = yr1 + Math.abs(tly2);
					yr2 = yr2 - Math.abs(tly2);
				}
				
				path_Rec_Select = new Path();
				path_Rec_Select.moveTo(xr1, yr1);
				path_Rec_Select.lineTo(xr2, yr1);
				path_Rec_Select.lineTo(xr2, yr2);
				path_Rec_Select.lineTo(xr1, yr2);
				path_Rec_Select.lineTo(xr1, yr1);
				
				path_Rec_Select2 = new Path();
				path_Rec_Select2.addRect(xr1 - 5, yr1 - 5, xr1 + 5, yr1 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 - 5, yr1 - 5, xr2 + 5, yr1 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr1 - 5, yr2 - 5, xr1 + 5, yr2 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 - 5, yr2 - 5, xr2 + 5, yr2 + 5, Direction.CW);
			} else {
				path_Rec_Select = new Path();
				path_Rec_Select.moveTo(xr1, yr1);
				path_Rec_Select.lineTo(xr2, yr1);
				path_Rec_Select.lineTo(xr2, yr2);
				path_Rec_Select.lineTo(xr1, yr2);
				path_Rec_Select.lineTo(xr1, yr1);
				
				path_Rec_Select2 = new Path();
				path_Rec_Select2.addRect(xr1 - 5, yr1 - 5, xr1 + 5, yr1 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 - 5, yr1 - 5, xr2 + 5, yr1 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr1 - 5, yr2 - 5, xr1 + 5, yr2 + 5, Direction.CW);
				path_Rec_Select2.addRect(xr2 - 5, yr2 - 5, xr2 + 5, yr2 + 5, Direction.CW);
			}
			invalidate();
		} catch(Exception ex) {
			ex.toString();
		}
	}
	
	/** Kiểm tra có đo tỷ lệ đường vẽ hay không
	 * @return
	 */
	public boolean check_rate(){
		if(EsspDrawImageActivity.rate != 0 && EsspDrawImageActivity.size_rate != 0){
			return true;
		} else {
			return false;
		}
	}
	
}
