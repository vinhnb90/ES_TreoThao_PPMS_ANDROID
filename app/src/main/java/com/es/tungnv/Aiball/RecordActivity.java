package com.es.tungnv.Aiball;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.tungnv.db.TthtSQLiteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.views.R;

public class RecordActivity extends Activity implements IVideoSink, IAudioSink  {

	private static final int REQUEST_WIFI_SETTING = 1008;
	/**
	 * Data section
	 */
	//taken from DetectDifference.java
	private Bitmap mBitmapRefFrame;
	private Bitmap mBmDiffFrame;

	private ByteBuffer mBbRefBf;
	private ByteBuffer mBbDiffBf;

	private byte[] mARefStore;
	private byte[] mADiffStore;
	private byte[] mARefBytes;
	private byte[] mADiffBytes;

	/**
	 * Dialog section
	 */
	private ProgressDialog mPd;
	public static ProgressDialog mPdConnect =null;
	//private DetectDifference _detectdifference;// = new DetectDifference();
	public AlertDialog.Builder mDdBuilder;

	/**
	 * String section
	 */
	private int CHANGE = 3;
//		private int COUNT = 921600;
	private int BUFFER_LOAD_CAMERA = 1229000;
	private int MOTION_LEVEL = 0;
	public int MOTION_LEVEL_FINAL = 0;
	private int RESOLUTION_JPG = Define.RESOLUTON_VGA;
	private int THRES_HOLD = 20;
	private long MAX_SIZE;

	private boolean ENABLE_AUDIO; 
	private boolean SNAPSHOT;
	private boolean RECORD_IN_PROGRESS = false; 

	private String IP = null, PASS_WORD = null,USER_NAME=null;
	private String RECORD_FILE_NAME = null; 

	private final String TAG = RecordActivity.class.getSimpleName();
	private TthtSQLiteConnection connection;
	private String MA_DDO, TREO_THAO;

	/**
	 * View section
	 */
	private Button btCapture ;
	private Button btConnect;
	private ImageView mIvLoadVideoArea;
	private TextView mTvRecordStatus;
	private TextView mTvText ;
	private TextView  mTvStatus;

	/**
	 * Other section
	 */
	private VideoStreamer mStreamingVideoRecorder;
	private BufferedOutputStream mBufferedOutputStream;
	private PCMPlayer mPCMPlayer;
	private AviRecord mAviRecord;
	private NetworkInfo mNetworkInfo;

	private ThumbnailCache mCache;

	/**
	 * @see Activity#onCreate(Bundle)
	 */
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aiball_recorder);

		mTvText = (TextView) findViewById (R.id.TextView01);
		mTvStatus = (TextView) findViewById (R.id.tv_record_status);
		mIvLoadVideoArea = (ImageView) findViewById(R.id.iv_load_video_area);
		mTvRecordStatus = (TextView) findViewById(R.id.tv_record_status);
		btCapture = (Button)findViewById(R.id.btCapture);
		btConnect = (Button)findViewById(R.id.btConnect);

		connection = TthtSQLiteConnection.getInstance(RecordActivity.this);
		MA_DDO = getIntent().getExtras().getString("MA_DDO");
		TREO_THAO = getIntent().getExtras().getString("TREO_THAO");
		WifiManager wifiManager = (WifiManager) RecordActivity.this.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		btConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickGotoWiFiSetting();
			}
		});

		btCapture.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SNAPSHOT = true;
			}
		});

		mARefStore = new byte[BUFFER_LOAD_CAMERA];
		mADiffStore = new byte[BUFFER_LOAD_CAMERA];
		mARefBytes = new byte[BUFFER_LOAD_CAMERA];
		mADiffBytes = new byte[BUFFER_LOAD_CAMERA];

//		SharedPreferences settings = getSharedPreferences(Define.PREFS_NAME, 0);

		IP = "http://192.168.2.1";
		USER_NAME = "";
		PASS_WORD = "";

		mTvStatus.setText(getString(R.string.initial));
		THRES_HOLD = 5*10;

		if(THRES_HOLD == 0 )
			CHANGE = 5;
		else if (THRES_HOLD == 300){
			THRES_HOLD += 30000;
			CHANGE = 4;
		}else {
			THRES_HOLD+=1850;
		}

		try {
			mStreamingVideoRecorder = new VideoStreamer(new URL(IP+"/?action=appletvastream"), USER_NAME, PASS_WORD);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		mPCMPlayer = new PCMPlayer();

		ENABLE_AUDIO = false;
		mStreamingVideoRecorder.enableAudio(ENABLE_AUDIO);
		mStreamingVideoRecorder.addVideoSink(this);

		final Handler changeText = new Handler();
		changeText.post(new Runnable() {

			@Override
			public void run() {
				if(CHANGE == 1){
					CHANGE = 3;
					mTvStatus.postInvalidate();
					mTvStatus.setText(getString(R.string.record));

				}else if (CHANGE == 0){
					CHANGE = 3;
					mTvStatus.postInvalidate();
					mTvStatus.setText(getString(R.string.detectMotion));

				}else if (CHANGE ==4){

					mTvStatus.postInvalidate();
					mTvStatus.setText(getString(R.string.disabled));
				}else if (CHANGE == 5){

					mTvStatus.postInvalidate();
					mTvStatus.setText(getString(R.string.always));
				}

				changeText.postDelayed(this,2000);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (level >= TRIM_MEMORY_MODERATE) { // 60
			mCache.evictAll();
		} else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
			mCache.trimToSize(mCache.size() / 2);
		}
	}

	public void onClickGotoWiFiSetting() {
		try{
			String pac = "com.android.settings";
			Intent i = new Intent();

			i.setClassName(pac, pac + ".wifi.p2p.WifiP2pSettings");
			try {
//	            startActivity(i);
//				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), REQUEST_WIFI_SETTING);
			} catch (ActivityNotFoundException e) {
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH + 1) {
//					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), REQUEST_WIFI_SETTING);
				} else {
					i.setClassName(pac, pac + ".wifi.WifiSettings");
					try {
//						startActivity(i);
						startActivityForResult(i, REQUEST_WIFI_SETTING);
					} catch (ActivityNotFoundException e2) {

					}
				}
			}
		} catch(Exception ex) {
			ex.toString();
		}
	}

	final byte[] EXIF_header = {
			(byte)0xFF, (byte)0xD8 , (byte)0xFF ,(byte) 0xE1 , 0x00 , 0x5A , 0x45 , 0x78, 0x69 
			, 0x66 , 0x00 , 0x00, 0x4D, 0x4D, 0x00, 0x2A
			, 0x00, 0x00, 0x00, 0x08, 0x00, 0x02, 0x01, 0x32, 0x00, 0x02
			, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x2A, (byte)0x87, 0x69
			, 0x00, 0x04, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x3E
			, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x32, 0x30
			, 0x31, 0x30, 0x3A, 0x31, 0x32, 0x3A, 0x32, 0x34, 0x20, 0x31
			, 0x36, 0x3A, 0x30, 0x38, 0x3A, 0x32, 0x34, 0x00, 0x00, 0x01
			, (byte)0x90, 0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00
			, 0x00, 0x2A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	/**
	 * @see Activity#onStart()
	 */
	@Override
	protected void onStart() {
		FRAME_INDEX = 0;
		DIFF_FRAME=120;
		MOTION=0;
		FRAME_INDEX_LAST_MOTION=-120;
		FRAME_COUNT=0;
		//_detectdifference = new DetectDifference();
		super.onStart();
		new Thread(mStreamingVideoRecorder).start();
		new Thread(mPCMPlayer).start();

		//new Thread(_detectdifference).start();

	}

	//Handler handler = new Handler();;
	static int INDEX = 0;
	static int MOTION = 0;
	//private int threshold=20;// = Rmotion_sensitivity_threshold;//40;
	static boolean FLAG = false;
	static int FRAME_INDEX = 0;
	static byte[] FRAME_1 = null;
	static int FRAME_INDEX_LAST_MOTION=-120;
	static int FRAME_COUNT=0;
	static int DIFF_FRAME = 120;

	/*SharedPreferences settings = getSharedPreferences(PublicDefine.PREFS_NAME, 0);
     threshold = Integer.toString(settings.getString("sensitivity", ""));
	 */
	@Override
	public void onFrame( byte[] frame,  byte[] pcm) {
		final Bitmap b = BitmapFactory.decodeByteArray(frame, 0, frame.length);
//		if(INDEX == 0)
//			ref_img(b);
//		else if (INDEX==4)
//			diff_img(b);

		INDEX++;
		INDEX = INDEX % 8;
		if(RECORD_IN_PROGRESS) {
			if(mAviRecord.getCurrentRecordSize() >= MAX_SIZE) {
				mIvLoadVideoArea.post(new Runnable() {
					public void run() {
					}
				});
				RECORD_IN_PROGRESS = false;
				return;
			}

			if(mStreamingVideoRecorder.getResetFlag() == 1)
				mAviRecord.resetCounter();

			if(mBbRefBf == null || mBbDiffBf == null)
				MOTION = 0;
			else if ((INDEX == 5) 
					&& (Math.abs(FRAME_COUNT-FRAME_INDEX_LAST_MOTION)>DIFF_FRAME)) {
				//only check if _frameindex is greater than previous _frameindex_motiondetect by 10s = 170 frames
				mBbRefBf.rewind();
				mBbDiffBf.rewind();
				mARefBytes = mBbRefBf.array();
				mADiffBytes = mBbDiffBf.array();

//				int choice=0;
				MOTION_LEVEL = 0;
				FLAG = false;

				for(int i = 0; i < BUFFER_LOAD_CAMERA; i += 24) {	
//					int chosen = choice%3;
//					int diff = Math.abs(mARefBytes[i+chosen] - mADiffBytes[i+chosen]);
//					if ( diff > 100 )
						MOTION_LEVEL++;
//					choice++;
				}
				
				Log.i(TAG, "THRE_SHOLD - " + THRES_HOLD + " CHANGE - " + CHANGE
						+ " MOTION_LEVEL " + MOTION_LEVEL);

				if(THRES_HOLD > 30000)
					CHANGE = 4;
				else if (THRES_HOLD == 0){
					CHANGE = 5;
					FRAME_INDEX_LAST_MOTION = FRAME_COUNT;
					FLAG = true;
					mAviRecord.stop();
					mAviRecord.start();
				} else if (MOTION_LEVEL > THRES_HOLD) {
					/**
					 * Using Ai-ball, start recording video now
					 */
					CHANGE = 1;
					FRAME_INDEX_LAST_MOTION = FRAME_COUNT;
					FLAG = true;
					mAviRecord.stop();
					mAviRecord.start();
				} else {
					CHANGE = 0;
//					mAviRecord.stop();
				}
			}

			// TODO
			Log.i(TAG, "FLAG " + FLAG);

			if(FLAG) {
				//motion > threshold)
				if(pcm.length > 0) {
					if(mAviRecord.getRecordFlag() == Define.RUN)
					{
						mAviRecord.getAudio(pcm, pcm.length, mStreamingVideoRecorder.getResetAudioBufferCount());
					}
				}

				if(mAviRecord.getRecordFlag() == Define.RUN)
				{
					FRAME_1 = new byte[frame.length + 92];

					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.");
					byte[] to_be_inserted_bytes = formatter.format(date).getBytes();
					System.arraycopy(to_be_inserted_bytes, 0, EXIF_header, 54, 20);

					System.arraycopy(EXIF_header, 0, FRAME_1, 0, 94);
					System.arraycopy(frame, 2, FRAME_1, 94, frame.length - 2);
					mAviRecord.getImage(FRAME_1, FRAME_1.length,FRAME_INDEX);

					FRAME_INDEX++;
				}
			}
			else
			{
			}
			FRAME_COUNT++;
		} else {
			btCapture.setEnabled(true);
			FRAME_INDEX = 0;
		}

		if(SNAPSHOT) {
			SNAPSHOT = false;
			saveImage(frame);
			RecordActivity.this.finish();
		}

		mIvLoadVideoArea.post(new Runnable() {
			public void run() {
				mIvLoadVideoArea.setImageBitmap(b);
			}
		});

		if(pcm != null) {
			mPCMPlayer.writePCM(pcm);
		}

	}

	private void saveImage(byte[] frame){
		String name = "";
		final String fileName = Util.getSnapshotFileName(name);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(fileName));
			bos.write(frame);
			bos.close();
			drawTextOnBitmap();
			mIvLoadVideoArea.post(new Runnable() {
				public void run() {
//					Toast.makeText(RecordActivity.this, "Captured: " + fileName, Toast.LENGTH_LONG).show();
				}
			});
		} catch (IOException e) {
			//todo: error handling
		}
	}

	private void drawTextOnBitmap(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String NGAY_CHUP = sdf.format(cal.getTime());
			String SO_CTO = "";
			String TEN_KHANG = "";
			String name = "";
			String fileName = Util.getSnapshotFileName(name);
			File fBitmap = new File(fileName);
			if (fBitmap.exists()) {
				Cursor c = null;
				if(c.moveToFirst()) {
					TEN_KHANG = c.getString(0);
					if (TREO_THAO.equals("TREO")) {
						SO_CTO = "";
					} else {
						SO_CTO = c.getString(1);
					}
				}

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bmRoot = BitmapFactory.decodeFile(fileName, options);
				if(bmRoot != null){
					Bitmap.Config bmConfig = bmRoot.getConfig();
					if (bmConfig == null) {
						bmConfig = android.graphics.Bitmap.Config.ARGB_8888;
					}
					bmRoot = bmRoot.copy(bmConfig, true);

					Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
					paint_text.setColor(Color.WHITE);
					paint_text.setTextSize(bmRoot.getHeight()/20);

					Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
					paint_rec.setColor(Color.BLACK);

					String[] arrName = TEN_KHANG.trim().split(" ");
					String name1 = "";
					String name2 = "";

					for (String sName : arrName) {
						Rect bounds_cut = new Rect();
						paint_text.getTextBounds(name1, 0, name1.length(), bounds_cut);
						if(bounds_cut.width() < bmRoot.getWidth()){
							name1 += sName + " ";
						} else {
							name2 += sName + " ";
						}
					}

					TEN_KHANG = name1.trim();

					Rect bounds_name = new Rect();
					paint_text.getTextBounds(TEN_KHANG, 0, TEN_KHANG.length(), bounds_name);
					int x_name = 10;
					int y_name_1 = bounds_name.height();
					int y_name_2 = 2*bounds_name.height();

					// ---------------Tạo khung ảnh mới, chèn thêm vùng đen trên và dưới ảnh để điền chữ, trên 3 dòng, dưới 1 dòng
					Bitmap.Config conf = Bitmap.Config.ARGB_8888;
					Bitmap bmp = Bitmap.createBitmap(bmRoot.getWidth(), bmRoot.getHeight() + 4 * bounds_name.height() + 30, conf);
					Canvas cBitmap = new Canvas(bmp);

					// ---------------Điền tên lên ảnh
					cBitmap.drawRect(0, 0, bmp.getWidth(), 2 * bounds_name.height() + 15, paint_rec);
					cBitmap.drawRect(0, bmp.getHeight() - 2 * bounds_name.height() - 15, bmp.getWidth(), bmp.getHeight(), paint_rec);
					cBitmap.drawText(TEN_KHANG, x_name, y_name_1, paint_text);
					if(!name2.equals(""))
						cBitmap.drawText(name2, x_name, y_name_2, paint_text);
					// Vẽ ảnh lên khung mới có viền đen
					cBitmap.drawBitmap(bmRoot, 0, 3 * bounds_name.height() + 15, paint_rec);

					// ---------------Điền giờ lên ảnh
					Rect bounds_date = new Rect();
					paint_text.getTextBounds(NGAY_CHUP, 0, NGAY_CHUP.length(), bounds_date);
					cBitmap.drawRect(0, 2 * bounds_name.height() + 15, bounds_date.width() + 15, 3 * bounds_name.height() + 15, paint_rec);

					int x_date = 10;
					int y_date = 2 * bounds_name.height() + 30;
					cBitmap.drawText(NGAY_CHUP, x_date, y_date, paint_text);

					// ---------------Điền mã điểm đo lên ảnh
					Rect bounds_maddo = new Rect();
					paint_text.getTextBounds(MA_DDO, 0, MA_DDO.length(), bounds_maddo);
					int x_maddo = 10;
					int y_maddo = bmp.getHeight() - 10;
					cBitmap.drawText(MA_DDO, x_maddo, y_maddo, paint_text);

					// ---------------Điền số công tơ lên ảnh
					if(!SO_CTO.equals("")) {
						Rect bounds_socto = new Rect();
						paint_text.getTextBounds(SO_CTO, 0, SO_CTO.length(), bounds_socto);
						int x_socto = bmp.getWidth() - 10 - bounds_socto.width();
						int y_socto = bmp.getHeight() - 10;
						cBitmap.drawText(SO_CTO, x_socto, y_socto, paint_text);
					}

					saveImage(bmp, MA_DDO, TREO_THAO);
				}
			}
		} catch(Exception ex) {
			Common.showAlertDialogGreen(RecordActivity.this, "Lỗi", Color.RED, "Lỗi ghi lên ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
		}
	}

	private void saveImage(Bitmap bmp, String MA_DDO, String TREO_THAO){
		String name = "";
		final String fileName = Util.getSnapshotFileName(name);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(fileName));
			bos.write(Common.encodeTobase64Byte(bmp));
			bos.close();
			TthtCommon.scanFile(RecordActivity.this, new String[]{fileName});
		} catch (IOException ex) {
			Common.showAlertDialogGreen(RecordActivity.this, "Lỗi", Color.RED, "Lỗi lưu ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
		}
	}

	//Taken from DetectDifference.jav
	public void ref_img(Bitmap frame)
	{
		mBitmapRefFrame = frame;

		mBbRefBf = ByteBuffer.wrap(mARefStore);
		mBbRefBf.rewind();

		mBitmapRefFrame.copyPixelsFromBuffer(mBbRefBf);
	}

	public void diff_img(Bitmap frame)
	{
		mBmDiffFrame = frame;
		mBbDiffBf = ByteBuffer.wrap(mADiffStore);
		mBbDiffBf.clear();
		mBmDiffFrame.copyPixelsToBuffer(mBbDiffBf);
	}

	private final int DURATION = 1;//0.005;// seconds
	private final int SAMPLE_RATE = 505;//8000;
	private final int NUM_SAMPLES = DURATION * SAMPLE_RATE;
	private final double sample[] = new double[NUM_SAMPLES];
	private final double FRE_OF_TONE = 440; // hz
	private final byte generatedSnd[] = new byte[2 * NUM_SAMPLES];

	void genTone(){         
		// fill out the array         
		for (int i = 0; i < NUM_SAMPLES; ++i) 
		{             
			sample[i] = Math.sin(2 * Math.PI * i / (SAMPLE_RATE/FRE_OF_TONE));         
		}          
		// convert to 16 bit pcm sound array        
		// assumes the sample buffer is normalised.         
		int idx = 0;         
		for (final double dVal : sample) {             
			// scale to maximum amplitude             
			final short val = (short) ((dVal * 32767));             
			// in 16 bit wav PCM, first byte is the low order byte             
			generatedSnd[idx++] = (byte) (val & 0x00ff);             
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);          

		}
	}

	void playSound(){         
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,  SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,  
				AudioFormat.ENCODING_PCM_16BIT, NUM_SAMPLES,                
				AudioTrack.MODE_STATIC);         
		audioTrack.write(generatedSnd, 0, generatedSnd.length);         
		audioTrack.play();    
	}	
	@Override
	public void onPCM(byte[] pcmData) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		super.onStop();
		
		INDEX=0;
		MOTION = 0;
		FRAME_COUNT=0;
		FRAME_INDEX_LAST_MOTION=-120;
		FRAME_1=null;
		FLAG = false;
		try
		{
			mStreamingVideoRecorder.stop();

			mPCMPlayer.stop();
		}
		catch (Exception e)
		{
			//todo: error handling
		}
		//_detectdifference.stop();
		FRAME_INDEX = 0;

		if(RECORD_IN_PROGRESS) {
			RECORD_IN_PROGRESS = false;
			mAviRecord.setRecordFlag(Define.STOP);
		}		
	}


	@Override
	public void onInitError(String error) {
		Log.i(TAG, "error - " + error);
		
//		mIvLoadVideoArea.post(new Runnable() {
//			public void run() {
//				mPd.dismiss();
				//Toast.makeText(RecordActivity.this, "Error connecting to the iBall", Toast.LENGTH_LONG).show();
//				mIbnRecordButton.setEnabled(false);
//				mIbnStopButton.setEnabled(false);
//				mIbnSnapshotButton.setEnabled(false);
//			}
//		});
	}

	@Override
	public void onVideoEnd() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode == REQUEST_WIFI_SETTING) {
				Intent it = new Intent(RecordActivity.this, RecordActivity.class);
				it.putExtra("MA_DDO", MA_DDO);
				it.putExtra("TREO_THAO", TREO_THAO);
				startActivity(it);
				RecordActivity.this.finish();
			}
		} catch(Exception ex) {
			Common.showAlertDialogGreen(RecordActivity.this, "Lỗi", Color.RED, "Lỗi quét mã vạch\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
		}
	}

	public static class ThumbnailCache extends LruCache<Long, Bitmap> {
		public ThumbnailCache(int maxSizeBytes) {
			super(maxSizeBytes);
		}

		@Override
		protected int sizeOf(Long key, Bitmap value) {
			return value.getByteCount();
		}
	}
}
