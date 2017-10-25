package com.es.tungnv.Aiball;

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
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.es.tungnv.db.GcsSqliteConnection;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.GcsCommon;
import com.es.tungnv.utils.GcsConstantVariables;
import com.es.tungnv.views.R;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GcsRecordActivity extends Activity implements IVideoSink, IAudioSink  {

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

	private final String TAG = GcsRecordActivity.class.getSimpleName();
	private GcsSqliteConnection connection;
	private String _maQuyen;
	private String _maCto;
	private String _LoaiBCS;

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
	private String name_cut = "";

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

		connection = new GcsSqliteConnection(GcsRecordActivity.this);
		_maQuyen = getIntent().getExtras().getString("MA_QUYEN");
		_maCto = getIntent().getExtras().getString("MA_CTO");
		_LoaiBCS = getIntent().getExtras().getString("LOAI_BCS");
		WifiManager wifiManager = (WifiManager) GcsRecordActivity.this.getSystemService(Context.WIFI_SERVICE);
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
			GcsRecordActivity.this.finish();
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
		BufferedOutputStream bos = null;
		try {
			String TEN_FILE = connection.getTenFileByMaQuyen(_maQuyen);
			TEN_FILE = TEN_FILE.toLowerCase().contains(".xml")?TEN_FILE.substring(0, TEN_FILE.length() - 4):TEN_FILE;
			Cursor c = connection.getDataByMaQuyen(_maQuyen, _maCto, _LoaiBCS);
			if(c.moveToFirst()) {
				String fileName = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH
						+ TEN_FILE + "/" + GcsCommon.getPhotoName(
						c.getString(c.getColumnIndex("MA_QUYEN")),
						c.getString(c.getColumnIndex("MA_CTO")),
						c.getInt(c.getColumnIndex("NAM")),
						c.getInt(c.getColumnIndex("THANG")),
						c.getInt(c.getColumnIndex("KY")),
						c.getString(c.getColumnIndex("MA_DDO")),
						c.getString(c.getColumnIndex("LOAI_BCS")));

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bmRoot = BitmapFactory.decodeByteArray(frame, 0, frame.length, options);

				bos = new BufferedOutputStream(new FileOutputStream(fileName));
				bmRoot = drawTextToBitmap(bmRoot, c.getString(c.getColumnIndex("TEN_KHANG")),
						c.getString(c.getColumnIndex("MA_DDO")), c.getString(c.getColumnIndex("SERY_CTO")),
						c.getString(c.getColumnIndex("CS_MOI")), c.getString(c.getColumnIndex("CHUOI_GIA")));
				bos.write(Common.encodeTobase64Byte(bmRoot));
				bos.close();

				mIvLoadVideoArea.post(new Runnable() {
					public void run() {
					}
				});
			}
		} catch (IOException e) {
		}
	}

	public Bitmap drawTextToBitmap(Bitmap bitmap, String name, String ma_dd, String so_cto, String cs_moi, String chuoi_gia) {
		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);

//		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTextSize(bitmap.getHeight()/20);

		Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_rec.setColor(Color.BLACK);

		// Xử lý cắt chuỗi tên xuống dòng
		String[] arrName = name.trim().split(" ");
		String name1 = "";
		String name2 = "";
		for (String sName : arrName) {
			Rect bounds_cut = new Rect();
			paint.getTextBounds(name1, 0, name1.length(), bounds_cut);
			if(bounds_cut.width() < 3*bitmap.getWidth()/4){
				name1 += sName + " ";
			} else {
				name2 += sName + " ";
			}
		}
//		name = name1.trim() + "\n" + name2.trim();
		name = name1.trim();
		name_cut = name1.trim() + "\n" + name2.trim();

		Rect bounds1 = new Rect();
		paint.getTextBounds(name, 0, name.length(), bounds1);
		int x1 = 10;
		int y1 = bounds1.height();
		int y1_2 = 2*bounds1.height();

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + 5 * bounds1.height() + 30, conf); // this creates a MUTABLE bitmap
		Canvas c = new Canvas(bmp);
		// Vẽ tên
		c.drawRect(0, 0, bmp.getWidth(), 2 * bounds1.height() + 15, paint_rec);
		c.drawRect(0, bmp.getHeight() - 2 * bounds1.height() - 15, bmp.getWidth(), bmp.getHeight(), paint_rec);
		c.drawText(name, x1, y1, paint);
		if(!name2.equals(""))
			c.drawText(name2, x1, y1_2, paint);
		// Vẽ ảnh lên khung mới có viền đen
		c.drawBitmap(bitmap, 0, 3 * bounds1.height() + 15, paint_rec);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());

		Rect bounds0 = new Rect();
		paint.getTextBounds(currentDateandTime, 0, currentDateandTime.length(), bounds0);
		c.drawRect(0, 2 * bounds1.height() + 15, bounds0.width() + 15, 3*bounds1.height() + 15, paint_rec);

		int x5 = 10;
		int y5 = 2 * bounds1.height() + 30;
		c.drawText(currentDateandTime, x5, y5, paint);

		Rect bounds6 = new Rect();
		paint.getTextBounds(chuoi_gia, 0, chuoi_gia.length(), bounds6);
		int x6 = 5;//bmp.getWidth() - 10 - bounds6.width()
		int y6 = bmp.getHeight() - 20 - bounds6.height();
		c.drawText(chuoi_gia, x6, y6, paint);

		Rect bounds2 = new Rect();
		paint.getTextBounds(ma_dd, 0, ma_dd.length(), bounds2);
		int x2 = 10;
		int y2 = bmp.getHeight() - 10;
		c.drawText(ma_dd, x2, y2, paint);

		Rect bounds3 = new Rect();
		paint.getTextBounds(so_cto, 0, so_cto.length(), bounds3);
		int x3 = bmp.getWidth() - 10 - bounds3.width();
		int y3 = bmp.getHeight() - 10;
		c.drawText(so_cto, x3, y3, paint);

		// Vẽ CS_MOI lên góc trên bên phải
		Rect bounds4 = new Rect();
		paint.getTextBounds(cs_moi, 0, cs_moi.length(), bounds4);
		int x4 = bmp.getWidth() - 10 - bounds4.width();
		int y4 = bounds4.height() + 10;
		c.drawText(cs_moi, x4, y4, paint);

		return bmp;
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
				Intent it = new Intent(GcsRecordActivity.this, GcsRecordActivity.class);
				it.putExtra("MA_QUYEN", _maQuyen);
				startActivity(it);
				GcsRecordActivity.this.finish();
			}
		} catch(Exception ex) {
			Common.showAlertDialogGreen(GcsRecordActivity.this, "Lỗi", Color.RED, "Lỗi quét mã vạch\n" + ex.toString(), Color.WHITE, "OK", Color.WHITE);
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
