package com.es.tungnv.measuredistance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.es.tungnv.views.R;

public class MtdLayoutHeight extends View{

	private float w = 0f;
	private float h = 0f;
	
	private float top_height = 0f;
	private float max_height = 0f;
	private float max_height_sensor = 0f;
	private float pos_sensor = 0f;
	
	private Paint paint_height;
	private Paint paint_text;
	private Paint paint_text2;
	
	private float x1_button = 0f;
	private float y1_button = 0f;
	private float x2_button = 0f;
	private float y2_button = 0f;
	
	private float x1_sensor = 0f;
	private float y1_sensor = 0f;
	private float x2_sensor = 0f;
	private float y2_sensor = 0f;
	
	private boolean isTouchButton = false;
	private boolean isTouchButtonSensor = false;
	
	public MtdLayoutHeight(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint_height = new Paint();
		paint_height.setAntiAlias(true);
		paint_height.setColor(Color.WHITE);
		paint_height.setStyle(Paint.Style.FILL);
		paint_height.setStrokeJoin(Paint.Join.ROUND);
		
		paint_text = new Paint();
		paint_text.setColor(Color.GREEN); 
		paint_text.setTextSize(50);
		paint_text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		
		paint_text2 = new Paint();
		paint_text2.setColor(Color.GREEN); 
		paint_text2.setTextSize(20);
		paint_text2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
	}

	Bitmap bm_land;
	Bitmap bm_muiten;
	Bitmap bm_button;
	
	@Override
	protected void onDraw(Canvas canvas) {
		w = getWidth();
		h = getHeight();
		max_height = h/2;
		max_height_sensor = h/2 - w/16;
		
		// Vẽ thanh điều chỉnh độ cao
		bm_land = BitmapFactory.decodeResource(getResources(), R.drawable.mtd_land);
		bm_muiten = BitmapFactory.decodeResource(getResources(), R.drawable.mtd_muiten);
		bm_button = BitmapFactory.decodeResource(getResources(), R.drawable.mtd_button2);
		
		top_height = (float) (max_height*MtdMainActivity.height_device/MtdMainActivity.max_len_height);
		canvas.drawBitmap(bm_land, w/32, 3*h/4, null);
		canvas.drawBitmap(bm_muiten, w/32 + bm_land.getWidth()/2 - bm_muiten.getWidth()/2, 3*h/4, null);
		canvas.drawRect(w/32 + bm_land.getWidth()/2 - bm_muiten.getWidth()/4, 3*h/4 - top_height, w/32 + bm_land.getWidth()/2 + bm_muiten.getWidth()/4, 3*h/4, paint_height);
		canvas.drawBitmap(bm_button, w/32 + bm_land.getWidth()/2 - bm_button.getWidth()/2, 3*h/4 - top_height - bm_button.getHeight(), null);
		
		canvas.drawText(MtdMainActivity.roundTwoDecimals(MtdMainActivity.height_device) + " m", w/32 + bm_land.getWidth()/2 - 4*bm_button.getWidth()/5, 3*h/4 - top_height - bm_button.getHeight() - bm_button.getHeight()/3, paint_text);
		
		x1_button = w/32 + bm_land.getWidth()/2 - bm_button.getWidth()/2;
		y1_button = 3*h/4 - top_height - bm_button.getHeight();
		x2_button = w/32 + bm_land.getWidth()/2 + bm_button.getWidth()/2;
		y2_button = 3*h/4 - top_height;
		// Vẽ thanh điều chỉnh cảm biến
//		pos_sensor = (float) (max_height_sensor*(MainActivity.rate_sensor - MainActivity.min_sensor)/(MainActivity.max_sensor - MainActivity.min_sensor));
//		canvas.drawRect(w - w/32 - bm_land.getWidth()/2 - bm_muiten.getWidth()/4, h/4 + w/32, w - w/32 - bm_land.getWidth()/2 + bm_muiten.getWidth()/4, 3*h/4 - w/32, paint_height);
//		canvas.drawBitmap(bm_button, w - w/32 - bm_land.getWidth()/2 - bm_button.getWidth()/2, 3*h/4 - w/32 - pos_sensor - bm_button.getHeight()/2, null);
//		canvas.drawText(" 1.5", w - w/16 - bm_land.getWidth()/2 - bm_button.getWidth()/2 - w/32, h/4 + w/32 + paint_text2.getTextSize()/3, paint_text2);
//		canvas.drawText("1.25", w - w/16 - bm_land.getWidth()/2 - bm_button.getWidth()/2 - w/32, h/2 + w/64 - paint_text2.getTextSize()/3, paint_text2);
//		canvas.drawText(" 1.0", w - w/16 - bm_land.getWidth()/2 - bm_button.getWidth()/2 - w/32, 3*h/4 - paint_text2.getTextSize(), paint_text2);
//		
//		x1_sensor = w - w/32 - bm_land.getWidth()/2 - bm_button.getWidth()/2;
//		y1_sensor = 3*h/4 - w/32 - pos_sensor - bm_button.getHeight()/2;
//		x2_sensor = w - w/32 - bm_land.getWidth()/2 + bm_button.getWidth()/2;
//		y2_sensor = 3*h/4 - w/32 - pos_sensor + bm_button.getHeight()/2;
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(event.getX() >= x1_button && event.getX() <= x2_button && event.getY() >= y1_button && event.getY() <= y2_button){
				isTouchButton = true;
			}
//			if(event.getX() >= x1_sensor && event.getX() <= x2_sensor && event.getY() >= y1_sensor && event.getY() <= y2_sensor){
//				isTouchButtonSensor = true;
//			}
			break;

		case MotionEvent.ACTION_MOVE:
			if(isTouchButton){
				top_height = 3*h/4 - (Math.abs(event.getY() + bm_button.getHeight()/2));
				MtdMainActivity.height_device = top_height*MtdMainActivity.max_len_height/max_height;
				
				if(MtdMainActivity.height_device >= MtdMainActivity.max_len_height){
					MtdMainActivity.height_device = MtdMainActivity.max_len_height;
				}
				if(MtdMainActivity.height_device <= MtdMainActivity.min_len_height){
					MtdMainActivity.height_device = MtdMainActivity.min_len_height;
				}
				invalidate();
			}
			
//			if(isTouchButtonSensor){
//				pos_sensor = 3*h/4 - w/32 - (Math.abs(event.getY() + bm_button.getHeight()/2));
//				MainActivity.rate_sensor = pos_sensor*(MainActivity.max_sensor - MainActivity.min_sensor)/max_height_sensor + MainActivity.min_sensor;
//				
//				if(MainActivity.rate_sensor >= MainActivity.max_sensor){
//					MainActivity.rate_sensor = MainActivity.max_sensor;
//				}
//				if(MainActivity.rate_sensor <= MainActivity.min_sensor){
//					MainActivity.rate_sensor = MainActivity.min_sensor;
//				}
//				invalidate();
//			}
			break;

		case MotionEvent.ACTION_UP:
			isTouchButton = false;
//			isTouchButtonSensor = false;
			break;
		}
		return true;
	}
	
}
