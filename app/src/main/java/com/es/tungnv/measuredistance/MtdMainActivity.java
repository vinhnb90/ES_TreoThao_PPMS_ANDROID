package com.es.tungnv.measuredistance;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.EsspSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.views.R;

public class MtdMainActivity extends Activity implements SensorEventListener{

	private Camera cameraObject;
	private Camera.Parameters parameter;
	private FrameLayout preview;
	private MtdShowCamera showCamera;
	
	private ImageView ivSensor;
	private TextView tvDistance;
	private ImageButton ibtMenu, ibtCapture, ibtFocus;
	private Button btDoVong;
	private LinearLayout lnBackground;
	
	private LayoutInflater controlInflater = null;
	private LayoutInflater controlInflater_height = null;
	
	private SensorManager sManager;
	
	private long angle_measure_distance;//Góc nghiêng của thiết bị
	public static Double height_device = 1.2d;//Chiều cao của thiết bị tính bằng mét
	public static Double max_len_height = 2.0d;
	public static Double min_len_height = 0.5d;
	
	public static Double max_sensor = 1.5d;
	public static Double min_sensor = 1.0d;
	public static Double rate_sensor = 1.0d;
	
	public static Double corner_down = 0.0d;
	public static Double corner_up = 0.0d;
	
	public static boolean isMeasure = true;
	public static boolean isMeasureSettingUp = true;
	public static boolean isMeasureSettingDown = true;
	
	public EsspSqliteConnection connection;
	
	public TextView tvGocDuoi, tvGocTren;
	public Dialog dialog;
	
	public String MA_YCAU_KNAI = "";
	public int size = 0;
	public float do_vong = 0f;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.essp_activity_mtd);
			Display d = getWindowManager().getDefaultDisplay();
			size = d.getWidth() - 65;
			
			connection = EsspSqliteConnection.getInstance(MtdMainActivity.this);
			
			isMeasure = true;
			isMeasureSettingUp = true;
			isMeasureSettingDown = true;
			
			Intent intent = getIntent();
			Bundle b = intent.getBundleExtra("MA");
			MA_YCAU_KNAI = b.getString("MA_YCAU_KNAI");

	        controlInflater_height = LayoutInflater.from(getBaseContext());
	        View viewControl_height = controlInflater_height.inflate(R.layout.essp_mtd_layout_height, null);
	        LayoutParams layoutParamsControl_height = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        this.addContentView(viewControl_height, layoutParamsControl_height);
	        
	        controlInflater = LayoutInflater.from(getBaseContext());
	        View viewControl = controlInflater.inflate(R.layout.essp_mtd_controls, null);
	        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        this.addContentView(viewControl, layoutParamsControl);
	        
	        preview = (FrameLayout)findViewById(R.id.camera_preview);
	        ivSensor = (ImageView)findViewById(R.id.ivSensor);
	        tvDistance = (TextView)findViewById(R.id.tvDistance);
	        ibtMenu = (ImageButton)findViewById(R.id.ibtMenu);
//	        ibtCapture = (ImageButton)findViewById(R.id.ibtCapture);
//	        ibtFocus = (ImageButton)findViewById(R.id.ibtFocus);
	        lnBackground = (LinearLayout)findViewById(R.id.lnBackground);
	        btDoVong = (Button)findViewById(R.id.btDoVong);
	        
	        dialog = new Dialog(this);
	        
	        lnBackground.getBackground().setAlpha(80);
	        
	        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        
	        cameraObject = isCameraAvailiable();
			showCamera = new MtdShowCamera(this, cameraObject);
			parameter = cameraObject.getParameters();
			setDisplayOrientation(cameraObject, 90);
			
			preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(showCamera);
			setCameraFocus(myAutoFocusCallback);
			
//			ibtCapture.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					isMeasure = !isMeasure;
//					snapIt();
//				}
//			});
			
			ibtMenu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CreateDialogMenu();
				}
			});
			
//			ibtFocus.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					cameraObject.autoFocus(myAutoFocusCallback);
//				}
//			});
			
			btDoVong.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CreateDialogDoVong();
				}
			});
		} catch(Exception ex) {
			ex.toString();
		}
	}

	/**Tạo dialog menu
	 * 
	 */
	private void CreateDialogMenu(){
		try{
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.essp_mtd_dialog_menu);
			dialog.getWindow().setLayout(
					android.app.ActionBar.LayoutParams.MATCH_PARENT,
					android.app.ActionBar.LayoutParams.WRAP_CONTENT);
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			final ListView lsvMenu = (ListView) dialog.findViewById(R.id.lsvMenu);

			String[] list_menu = {"Cài đặt", "Hướng dẫn"};
			int[] img_menu = {R.drawable.mtd_gcs_config, R.drawable.mtd_gcs_help};
			MtdAdapterMenu adapter = new MtdAdapterMenu(MtdMainActivity.this, R.layout.essp_mtd_listview_menu, list_menu, img_menu);
			lsvMenu.setAdapter(adapter);
			
			lsvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	            	try{
	            		switch (position) {
						case 0:
							CreateDialogSetting();
							break;

						case 1:
							
							break;
						}
	            		dialog.dismiss();
	            	} catch(Exception ex) {
	            		ex.toString();
	            	}
	            }
	        });
			dialog.show();
		} catch(Exception ex) {
			Toast.makeText(MtdMainActivity.this, "Không mở được menu", Toast.LENGTH_LONG).show();
		}
	}
	
	private void CreateDialogDoVong(){
		try{
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.essp_mtd_dialog_dovong);
			dialog.getWindow().setLayout(
					android.app.ActionBar.LayoutParams.MATCH_PARENT,
					android.app.ActionBar.LayoutParams.WRAP_CONTENT);
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			final EditText etDoVong = (EditText) dialog.findViewById(R.id.etDoVong);
			Button btOK = (Button) dialog.findViewById(R.id.btOK);
			
			etDoVong.requestFocus();
			etDoVong.setText("" + do_vong);
			
			btOK.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					do_vong = Float.parseFloat(etDoVong.getText().toString().trim());
					btDoVong.setText("Độ võng " + etDoVong.getText().toString().trim() + "m");
					
					SharedPreferences pre = getSharedPreferences("config", MODE_PRIVATE);
					SharedPreferences.Editor editor=pre.edit();
					editor.putString("DO_VONG", etDoVong.getText().toString().trim());
					editor.commit();
					
					dialog.dismiss();
				}
			});

			dialog.show();
		} catch(Exception ex) {
			Toast.makeText(MtdMainActivity.this, "Không mở được menu", Toast.LENGTH_LONG).show();
		}
	}
	
	/**Tạo dialog cài đặt
	 * 
	 */
	private void CreateDialogSetting(){
		try{
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.essp_mtd_dialog_setting);
			dialog.getWindow().setLayout(
					android.app.ActionBar.LayoutParams.MATCH_PARENT,
					android.app.ActionBar.LayoutParams.WRAP_CONTENT);
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			final Button btnLuu = (Button) dialog.findViewById(R.id.btnLuu);
			final Button btnDatLaiDuoi = (Button) dialog.findViewById(R.id.btnDatLaiDuoi);
			final Button btnDatLaiTren = (Button) dialog.findViewById(R.id.btnDatLaiTren);
			final Button btnDatLai = (Button) dialog.findViewById(R.id.btnDatLai);
			tvGocDuoi = (TextView) dialog.findViewById(R.id.tvGocDuoi);
			tvGocTren = (TextView) dialog.findViewById(R.id.tvGocTren);

			btnLuu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try{
						SharedPreferences pre = getSharedPreferences("config", MODE_PRIVATE);
						SharedPreferences.Editor editor=pre.edit();
						editor.putString("CORNER_UP", tvGocTren.getText().toString().replace("°", ""));
						editor.putString("CORNER_DOWN", tvGocDuoi.getText().toString().replace("°", ""));
						editor.commit();
						dialog.dismiss();
						Toast.makeText(MtdMainActivity.this, "Lưu cấu hình thành công", Toast.LENGTH_LONG).show();
					} catch(Exception ex) {
						Toast.makeText(MtdMainActivity.this, "Lỗi khi lưu cấu hình", Toast.LENGTH_LONG).show();
					}
				}
			});
			
			btnDatLaiDuoi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isMeasureSettingDown = !isMeasureSettingDown;
				}
			});
			
			btnDatLaiTren.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isMeasureSettingUp = !isMeasureSettingUp;
				}
			});
			
			btnDatLai.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tvGocTren.setText("90°");
					tvGocDuoi.setText("0°");
					isMeasureSettingDown = false;
					isMeasureSettingUp = false;
				}
			});
			
			dialog.show();
		} catch(Exception ex) {
			Toast.makeText(MtdMainActivity.this, "Không mở được menu", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Open camera
	 * 
	 * @return
	 */
	public static Camera isCameraAvailiable() {
		Camera object = null;
		try {
			object = Camera.open();
		} catch (Exception e) {
		}
		return object;
	}
	
	/**
	 * Xoay khung nhìn hình ảnh
	 * 
	 * @param camera: Camera
	 * @param angle: góc xoay
	 */
	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {

		}
	}
	
	/**
	 * Tự động focus
	 * 
	 * @param autoFocus
	 *            : AutoFocusCallback
	 */
	@SuppressWarnings("static-access")
	public void setCameraFocus(AutoFocusCallback autoFocus) {
		if (cameraObject.getParameters().getFocusMode().equals(cameraObject.getParameters().FOCUS_MODE_AUTO) || cameraObject.getParameters().getFocusMode().equals(cameraObject.getParameters().FOCUS_MODE_MACRO)) {
			cameraObject.autoFocus(autoFocus);
		}
	}
	
	/**
	 * auto focus
	 * 
	 */
	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
//			ibtCapture.setEnabled(true);
		}
	};

	/**
	 * Chụp ảnh
	 * 
	 */
	private PictureCallback capturedIt = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
//			Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
//			try {
//				OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
//				imageFileOS.write(data);
//				imageFileOS.flush();
//				imageFileOS.close();
//			} catch (FileNotFoundException e) {
//				e.toString();
//			} catch (IOException e) {
//				e.toString();
//			}
//			try {
//				Bitmap dmKC = Common.decodeBase64Byte(data);
//				Bitmap dmSensor = BitmapFactory.decodeResource(getResources(), R.drawable.mtd_arrow);
//
//				float w = dmKC.getWidth();
//				float h = dmKC.getHeight();
//				if(w > h){
//					Matrix matrix = new Matrix();
//					matrix.postRotate(90);
//					dmKC = Bitmap.createBitmap(dmKC, 0, 0, dmKC.getWidth(), dmKC.getHeight(), matrix, true);
//				}
//				dmKC = overlay(dmKC, dmSensor);
//				dmKC = drawTextToBitmap(MtdMainActivity.this, getResizedBitmap(dmKC, 800, 600), "" + roundTwoDecimals(height_device*Math.tan(Math.toRadians(angle_measure_distance)) + do_vong));
//				byte[] image_bytes = Common.encodeTobyte(dmKC);
//
//				String MA_YCAU_KNAI = entity_ks.getMA_YCAU_KNAI();
//				String GHI_CHU = "";
//				String TINH_TRANG = "0";
//				String KHOANG_CACH = "" + roundTwoDecimals(height_device*Math.tan(Math.toRadians(angle_measure_distance)) + do_vong);
//				if(connection.insertDataKC(MA_YCAU_KNAI, image_bytes, GHI_CHU, TINH_TRANG, KHOANG_CACH) != -1){
//					Common.ShowToast(MtdMainActivity.this, "Thêm khoảng cách thành công", Toast.LENGTH_LONG, size);
//					MtdMainActivity.this.finish();
//				} else {
//					Common.ShowToast(MtdMainActivity.this, "Không thêm được khoảng cách", Toast.LENGTH_LONG, size);
//					isMeasure = !isMeasure;
//				}
//			} catch (Exception e) {
//				Common.msbox("Lỗi", e.toString(), MtdMainActivity.this);
//			}
			camera.startPreview();
		}
	};
	
	private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
//		bmp2 = getResizedBitmap(bmp2, bmp1.getWidth()/2, bmp1.getHeight()/2);
		bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth()/2, bmp1.getHeight()/2, true);
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, bmp1.getWidth()/2 - bmp2.getWidth()/2, bmp1.getHeight()/2, null);
        return bmOverlay;
    }
	
	public Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap, String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;

		Bitmap.Config bitmapConfig = bitmap.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		paint.setTextSize((int) (34 * scale));
		paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
		
		Rect bounds = new Rect();
		gText = gText + " m";
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = bitmap.getWidth()/2 - bounds.width()/2;
		int y = bitmap.getHeight() - 2*bounds.height();

		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);
	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	public void snapIt() {
		cameraObject.takePicture(null, null, capturedIt);
	}
	
	/**
	 * sau khi chụp xong
	 * 
	 */
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
	
		}
	};
	
	@Override
	protected void onResume() 
	{
		try{
			restoringPreferences();
			btDoVong.setText("Độ võng " + do_vong + "m");
			super.onResume();
			sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
		} catch(Exception ex) {
			ex.toString();
		}
	}
	
    //When this Activity isn't visible anymore
	@Override
	protected void onStop() 
	{
		try{
			sManager.unregisterListener(this);
			super.onStop();
		} catch(Exception ex) {
			ex.toString();
		}
	}
	
	@Override
	protected void onPause() {
		savingPreferences();
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		try{
			float aX= event.values[0];
			float aY= event.values[1];
			float aZ= event.values[2];

			if(aX <= 0) {
				aX = 0.01f;
			}
			if(aY <= 0) {
				aY = 0.01f;
			}
			if(aZ <= 0) {
				aZ = 0.01f;
			}

			if(isMeasure){
				float rate_corner = (float) ((corner_up - corner_down)/90);
		        angle_measure_distance = (long)(Math.abs((Math.atan2(aY, aZ)/(Math.PI/180)) - corner_down)/rate_corner);
		        tvDistance.setText(roundTwoDecimals(height_device*Math.tan(Math.toRadians(angle_measure_distance))) + do_vong + " m");
			}
			if(dialog.isShowing()){
				if(angle_measure_distance < 45){
					if(isMeasureSettingDown){
						tvGocDuoi.setText(roundTwoDecimals(Math.atan2(aY, aZ)/(Math.PI/180)) + "°");
					}
					if(isMeasureSettingUp){
						tvGocTren.setText("90°");
					}
				} else {
					if(isMeasureSettingDown){
						tvGocDuoi.setText("0°");
					}
					if(isMeasureSettingUp){
						tvGocTren.setText(roundTwoDecimals(Math.atan2(aY, aZ)/(Math.PI/180)) + "°");
					}
				}
			}
			long angle_rotate_device = (long) (Math.atan2(aX, aY)/(Math.PI/180)) - 2;
		    ivSensor.setRotation(angle_rotate_device);
		} catch(Exception ex) {
			ex.toString();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	public static double roundTwoDecimals(double d)
	{
//	    DecimalFormat twoDForm = new DecimalFormat("#.##");
//	    return Double.valueOf(twoDForm.format(d));
		return Math.round(d*100.0)/100.0;
	}
	
	/**Lưu cấu hình
	 * 
	 */
	public void savingPreferences()	{
		try{
			SharedPreferences pre = getSharedPreferences("config", MODE_PRIVATE);
			SharedPreferences.Editor editor=pre.edit();
			editor.putString("HEIGHT", "" + height_device);
			editor.putString("DO_VONG", "" + do_vong);
			editor.commit();
		} catch(Exception ex) {
			ex.toString();
		}
	}
	
	/**
	 * hàm đọc trạng thái đã lưu trước đó
	 */
	public void restoringPreferences() {
		try{
			SharedPreferences pre = getSharedPreferences("config", MODE_PRIVATE);
			height_device = Double.parseDouble(pre.getString("HEIGHT", "1.2"));
//			rate_sensor = Double.parseDouble(pre.getString("SENSOR", "1.0"));
			corner_down = Double.parseDouble(pre.getString("CORNER_DOWN", "0"));
			corner_up = Double.parseDouble(pre.getString("CORNER_UP", "90"));
			do_vong = Float.parseFloat(pre.getString("DO_VONG", "0"));
		} catch(Exception ex) {
			ex.toString();
		}
	}
}
