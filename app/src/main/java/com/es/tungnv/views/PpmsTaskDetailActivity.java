package com.es.tungnv.views;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.es.tungnv.db.PpmsSqliteConnection;
import com.es.tungnv.entity.PpmsEntityTask;
import com.es.tungnv.utils.Common;
import com.es.tungnv.utils.PpmsCommon;
import com.es.tungnv.utils.aiball.AviRecord;
import com.es.tungnv.utils.aiball.Define;
import com.es.tungnv.utils.aiball.IAudioSink;
import com.es.tungnv.utils.aiball.IVideoSink;
import com.es.tungnv.utils.aiball.PCMPlayer;
import com.es.tungnv.utils.aiball.VideoStreamer;
import com.es.tungnv.zoomImage.ImageViewTouch;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.es.tungnv.utils.PpmsCommon.drawTextOnBitmapCongTo;

public class PpmsTaskDetailActivity extends AppCompatActivity implements IVideoSink, IAudioSink {

    //TODO initial object UI
    private PpmsEntityTask sTask;
    private int flagStatusChinhXac = 0;//= 0 if not change, = 1 if chinh xac, = 2 if co sai lech
    private String CHI_SO_PHUC_TRA = "", NHAN_XET = "", TINH_TRANG_NIEM_PHONG = "";
    private int typeCallTaskDetail;
    private int positionTask;
    private TextView tvTenKH, tvDiaChiDungDien, tvMaSoGCS, tvChiSoCu, tvChiSoMoi, tvThoiGianPhucTra, tvStatusConnectCamera, tvNgayPhanCong,
            tvNgayGhiDien,
            tvThangKTra,
            tvMucDichSD,
            tvTinhTrangSD,
            tvSanLuongTB,
            tvTinhTrangHopCto,
            tvSoCotCto,
            tvSoHopCto,
            tvSoOCto,
            tvLoaiCto,
            tvLoaiHopCto;

    private EditText etTinhTrangNiemPhong, etNhanXet, etChiSoPhucTra;
    private Button btnKetNoi, btnChupAnh, btnChupLai, btnXoa, btnCamera, btnGhi, btnExpandChiTiet, btnExpandThongTin;
    private ImageView ivFrame, ivImage;
    private LinearLayout llExpandThongTin, llExpandChiTiet;
    private Switch swtCoSaiLech;//setChecked(false) = ChinhXac


    //TODO initial object
    private PpmsSqliteConnection connection;

    //region region initial object camera
    //TODO initial object Camera
    private static final int REQUEST_WIFI_SETTING = 1008;
    private boolean SNAPSHOT;
    private int BUFFER_LOAD_CAMERA = 1229000;
    private byte[] mARefStore;
    private byte[] mADiffStore;
    private static String sIP = null, sPASS_WORD = null, sUSER_NAME = null;
    private int THRES_HOLD = 20;
    private int CHANGE = 3;
    private VideoStreamer mStreamingVideoRecorder;
    private PCMPlayer mPCMPlayer;
    private boolean ENABLE_AUDIO = false;
    private ThumbnailCache mCache;

    final byte[] EXIF_header = {
            (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1, 0x00, 0x5A, 0x45, 0x78, 0x69
            , 0x66, 0x00, 0x00, 0x4D, 0x4D, 0x00, 0x2A
            , 0x00, 0x00, 0x00, 0x08, 0x00, 0x02, 0x01, 0x32, 0x00, 0x02
            , 0x00, 0x00, 0x00, 0x14, 0x00, 0x00, 0x00, 0x2A, (byte) 0x87, 0x69
            , 0x00, 0x04, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x3E
            , 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x32, 0x30
            , 0x31, 0x30, 0x3A, 0x31, 0x32, 0x3A, 0x32, 0x34, 0x20, 0x31
            , 0x36, 0x3A, 0x30, 0x38, 0x3A, 0x32, 0x34, 0x00, 0x00, 0x01
            , (byte) 0x90, 0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0x14, 0x00, 0x00
            , 0x00, 0x2A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    static int FRAME_INDEX = 0;
    static byte[] FRAME_1 = null;
    static int FRAME_INDEX_LAST_MOTION = -120;
    static int FRAME_COUNT = 0;
    static int DIFF_FRAME = 120;
    static int MOTION = 0;
    static int INDEX = 0;
    private boolean RECORD_IN_PROGRESS = false;
    private AviRecord mAviRecord;
    private long MAX_SIZE;
    private ByteBuffer mBbRefBf;
    private ByteBuffer mBbDiffBf;
    private byte[] mARefBytes;
    private byte[] mADiffBytes;
    private int MOTION_LEVEL = 0;
    static boolean FLAG = false;
    private String name_cut = "";
    private String sPath = "", sPathTemp = "";
    private String sDateNowCapture = "";


    public static class ThumbnailCache extends LruCache<Long, Bitmap> {
        public ThumbnailCache(int maxSizeBytes) {
            super(maxSizeBytes);
        }

        @Override
        protected int sizeOf(Long key, Bitmap value) {
            return value.getByteCount();
        }
    }

    //endregion

    //region region ovrride method camera

    @Override
    public void onPCM(byte[] pcmData) {

    }

    @Override
    public void onFrame(byte[] frame, byte[] pcm) {

        try {
            final Bitmap bitmapOnFrame = BitmapFactory.decodeByteArray(frame, 0, frame.length);
            INDEX++;
            INDEX = INDEX % 8;
            if (RECORD_IN_PROGRESS) {
                if (mAviRecord.getCurrentRecordSize() >= MAX_SIZE) {
                    ivFrame.post(new Runnable() {
                        public void run() {
                        }
                    });
                    RECORD_IN_PROGRESS = false;
                    return;
                }

                if (mStreamingVideoRecorder.getResetFlag() == 1)
                    mAviRecord.resetCounter();

                if (mBbRefBf == null || mBbDiffBf == null)
                    MOTION = 0;
                else if ((INDEX == 5)
                        && (Math.abs(FRAME_COUNT - FRAME_INDEX_LAST_MOTION) > DIFF_FRAME)) {
                    //only check if _frameindex is greater than previous _frameindex_motiondetect by 10s = 170 frames
                    mBbRefBf.rewind();
                    mBbDiffBf.rewind();
                    mARefBytes = mBbRefBf.array();
                    mADiffBytes = mBbDiffBf.array();

//				int choice=0;
                    MOTION_LEVEL = 0;
                    FLAG = false;

                    for (int i = 0; i < BUFFER_LOAD_CAMERA; i += 24) {
//					int chosen = choice%3;
//					int diff = Math.abs(mARefBytes[i+chosen] - mADiffBytes[i+chosen]);
//					if ( diff > 100 )
                        MOTION_LEVEL++;
//					choice++;
                    }

                    if (THRES_HOLD > 30000)
                        CHANGE = 4;
                    else if (THRES_HOLD == 0) {
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

                if (FLAG) {
                    //motion > threshold)
                    if (pcm.length > 0) {
                        if (mAviRecord.getRecordFlag() == Define.RUN) {
                            mAviRecord.getAudio(pcm, pcm.length, mStreamingVideoRecorder.getResetAudioBufferCount());
                        }
                    }

                    if (mAviRecord.getRecordFlag() == Define.RUN) {
                        FRAME_1 = new byte[frame.length + 92];

                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.");
                        byte[] to_be_inserted_bytes = formatter.format(date).getBytes();
                        System.arraycopy(to_be_inserted_bytes, 0, EXIF_header, 54, 20);

                        System.arraycopy(EXIF_header, 0, FRAME_1, 0, 94);
                        System.arraycopy(frame, 2, FRAME_1, 94, frame.length - 2);
                        mAviRecord.getImage(FRAME_1, FRAME_1.length, FRAME_INDEX);

                        FRAME_INDEX++;
                    }
                } else {
                }
                FRAME_COUNT++;
            } else {
//                btnChupAnh.setEnabled(true);
                FRAME_INDEX = 0;
            }

            if (SNAPSHOT) {
                SNAPSHOT = false;
                saveImage(frame);
/*
                ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        ivImage.setVisibility(View.VISIBLE);
                    }
                });

                ivFrame.post(new Runnable() {
                    @Override
                    public void run() {
                        ivFrame.setVisibility(View.GONE);
                    }
                });*/
/*
                finish();
                onDestroy();*/
            }

            ivFrame.post(new Runnable() {
                public void run() {
                    ivFrame.setImageBitmap(bitmapOnFrame);
                }
            });

            if (pcm != null) {
                mPCMPlayer.writePCM(pcm);
            }
        } catch (Exception e) {
            Log.d("TAG", "onFrame: " + e.getMessage());
        }

    }


    private void saveImage(byte[] frame) {
        try {
            BufferedOutputStream bos = null;
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + PpmsCommon.PROGRAM_PHOTOS_PATH);
            if (!myDir.exists())
                myDir.mkdirs();

            //Todo get datenow set to sTask
            String dateNowConvertToNameImage = PpmsCommon.convertDateTimeType(sDateNowCapture, 3);
            String dateNowConvertToTextDrawOnImage = PpmsCommon.convertDateTimeType(sDateNowCapture, 1);

            //TODO get name AnhCto = {maNvien}_{soCongTo}.jpg
            String fname = sTask.getNhanVienId() + "_" + sTask.getSoCongTo() + ".jpg";

            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            file.createNewFile();

            //TODO set sPath static get path image
            sPath = root + PpmsCommon.PROGRAM_PHOTOS_PATH + fname;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmRoot = BitmapFactory.decodeByteArray(frame, 0, frame.length, options);
            bos = new BufferedOutputStream(new FileOutputStream(sPath));

            //TODO draw info
            bmRoot = drawTextOnBitmapCongTo(
                    bmRoot,
                    "Khách hàng: " + sTask.getTenKhachHang(),
                    "Số Công tơ: " + sTask.getSoCongTo(),
                    "Ngày Phúc Tra: " + dateNowConvertToTextDrawOnImage,
                    "Chỉ số mới :" + sTask.getChiSoMoi());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmRoot.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageByte = baos.toByteArray();

            bos.write(imageByte);
            bos.close();
            final Bitmap congTo = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
//            PpmsTaskDetailActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ivImage.setVisibility(View.GONE);
//                    ivImage.setVisibility(View.VISIBLE);
//                    ivImage.setImageBitmap(congTo);
//                }
//            });

            ivImage.post(new Runnable() {
                @Override
                public void run() {
                    ivImage.setImageBitmap(congTo);
                }
            });

//            ivImage.setVisibility(View.GONE);
//            ivImage.setVisibility(View.VISIBLE);
//            ivImage.setImageBitmap(congTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInitError(String errorMessage) {
        Log.d("TAG", "onInitError: " + errorMessage);
    }

    @Override
    public void onVideoEnd() {

    }
    //endregion

    //region region override method PpmsTaskDetailActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Biên bản phúc tra");

        setContentView(R.layout.ppms_activity_task_detail);
        initData();
        if (sTask == null)
            return;
        initView();

        //TODO initial object camera
        sIP = "http://192.168.2.1";
        sUSER_NAME = "";
        sPASS_WORD = "";
        mARefStore = new byte[BUFFER_LOAD_CAMERA];
        mADiffStore = new byte[BUFFER_LOAD_CAMERA];
        mARefBytes = new byte[BUFFER_LOAD_CAMERA];
        mADiffBytes = new byte[BUFFER_LOAD_CAMERA];

        setAction();

        //TODO check wifi
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        setThresHold();

        try {
            mStreamingVideoRecorder = new VideoStreamer(new URL(sIP + "/?action=appletvastream"), sUSER_NAME, sPASS_WORD);
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
                if (CHANGE == 1) {
                    CHANGE = 3;
                    tvStatusConnectCamera.postInvalidate();
                    tvStatusConnectCamera.setText(getString(R.string.record));

                } else if (CHANGE == 0) {
                    CHANGE = 3;
                    tvStatusConnectCamera.postInvalidate();
                    tvStatusConnectCamera.setText(getString(R.string.detectMotion));

                } else if (CHANGE == 4) {
                    tvStatusConnectCamera.postInvalidate();
                    tvStatusConnectCamera.setText(getString(R.string.disabled));
                    Toast.makeText(PpmsTaskDetailActivity.this, getString(R.string.disabled), Toast.LENGTH_SHORT).show();
                } else if (CHANGE == 5) {
                    tvStatusConnectCamera.postInvalidate();
                    tvStatusConnectCamera.setText(getString(R.string.always));
                    Toast.makeText(PpmsTaskDetailActivity.this, getString(R.string.always), Toast.LENGTH_SHORT).show();
                }
                changeText.postDelayed(this, 2000);
            }
        });
        setDataOnView();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO set stream camera
        FRAME_INDEX = 0;
        DIFF_FRAME = 120;
        MOTION = 0;
        FRAME_INDEX_LAST_MOTION = -120;
        FRAME_COUNT = 0;
        Thread a = new Thread(mStreamingVideoRecorder);
        a.start();
        new Thread(mPCMPlayer).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataOnView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            //TODO clear and stop stream camera
            INDEX = 0;
            MOTION = 0;
            FRAME_COUNT = 0;
            FRAME_INDEX_LAST_MOTION = -120;
            FRAME_1 = null;
            FLAG = false;
            try {
                mStreamingVideoRecorder.stop();
                mPCMPlayer.stop();
            } catch (Exception e) {
                //todo: error handling
            }
            //_detectdifference.stop();
            FRAME_INDEX = 0;

            if (RECORD_IN_PROGRESS) {
                RECORD_IN_PROGRESS = false;
                mAviRecord.setRecordFlag(Define.STOP);
            }
        } catch (Exception e) {
            Log.d("TAG", "onStop: " + e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //TODO clear static variable
        /*sTask = null;
        sPath = "";
        sDateNowCapture = "";*/
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        try {
            // Giải phóng bộ nhớ giúp tăng hiệu năng hệ thống và
            // giúp giảm thiểu khả năng bị system kill khi thiếu bộ nhớ.
            if (level >= TRIM_MEMORY_MODERATE) { // 60
                // Giải phóng bộ nhớ giúp tăng hiệu năng hệ thống và
                // giúp giảm thiểu khả năng bị system kill khi thiếu bộ nhớ.
                mCache.evictAll();
            } else if (level >= TRIM_MEMORY_BACKGROUND) { // 40
                // Giải phóng bộ nhớ giúp tăng hiệu năng hệ thống và
                // giúp giảm thiểu khả năng bị system kill khi thiếu bộ nhớ.
                mCache.trimToSize(mCache.size() / 2);
            }
        } catch (Exception e) {
            Log.d("TAG", "onStop: " + e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO recallback connect camera by wifi
        try {
            if (requestCode == REQUEST_WIFI_SETTING) {
                Intent it = new Intent(PpmsTaskDetailActivity.this, PpmsTaskDetailActivity.class);

                //TODO recall PpmsTaskDetailActivity to refresh stream thread restart, so call onCreate() again
                //TODO then all object pass to it clear data, we set again data by themeself
                Bundle bundle = new Bundle();
                bundle.putParcelable("TASK", sTask);
                bundle.putInt("FLAG_STATUS_CHINH_XAC", flagStatusChinhXac);
                bundle.putString("CHI_SO_PHUC_TRA", etChiSoPhucTra.getText().toString());
                bundle.putString("NHAN_XET", etNhanXet.getText().toString());
                bundle.putString("TINH_TRANG_NIEM_PHONG", etTinhTrangNiemPhong.getText().toString());
                it.putExtras(bundle);
                startActivity(it);
                PpmsTaskDetailActivity.this.finish();
            }

            if (requestCode == PpmsCommon.REQUEST_CODE_DETAIL_TO_CAMERA
                    && resultCode == RESULT_OK) {
                File imgFileTemp = new File(sPathTemp);
                PpmsCommon.scaleImage(sPathTemp, this);
                Bitmap bitmapTemp = BitmapFactory.decodeFile(imgFileTemp.getAbsolutePath());
                String dateNowConvertToTextDrawOnImage = PpmsCommon.convertDateTimeType(sDateNowCapture, 1);
                bitmapTemp = drawTextOnBitmapCongTo(
                        bitmapTemp,
                        "Khách hàng: " + sTask.getTenKhachHang(),
                        "Số Công tơ: " + sTask.getSoCongTo(),
                        "Ngày Phúc Tra: " + dateNowConvertToTextDrawOnImage,
                        "Chỉ số mới :" + sTask.getChiSoMoi());


                final Bitmap bitmap = bitmapTemp;
                ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        ivFrame.setVisibility(View.GONE);
                        ivImage.setVisibility(View.VISIBLE);
                        ivImage.setImageBitmap(bitmap);
                    }
                });
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Lỗi kết nối camera" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO nếu là đối soát
        if(typeCallTaskDetail == 2)
        {
            Intent callBackIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", positionTask);
            bundle.putParcelable("TASK", sTask);
            callBackIntent.putExtras(bundle);
            setResult(PpmsCommon.RESPONSE_CODE_DETAIL_TO_DOISOAT, callBackIntent);
        }
    }
*/
    //region region method action on click
    public void clickBtnExpandChiTiet(View view) {
        if (llExpandChiTiet.getVisibility() == View.VISIBLE) {
            llExpandChiTiet.setVisibility(View.GONE);
            btnExpandChiTiet.setBackgroundResource(R.drawable.title_elasticity_full);
        } else {
            llExpandChiTiet.setVisibility(View.VISIBLE);
            btnExpandChiTiet.setBackgroundResource(R.drawable.title_elasticity);
        }
        btnExpandChiTiet.setPadding(10, 0, 0, 0);
        btnExpandChiTiet.setCompoundDrawablePadding(10);
    }

    public void clickBtnExpandThongTin(View view) {
        if (llExpandThongTin.getVisibility() == View.VISIBLE) {
            llExpandThongTin.setVisibility(View.GONE);
            btnExpandThongTin.setBackgroundResource(R.drawable.title_elasticity_full);
        } else {
            llExpandThongTin.setVisibility(View.VISIBLE);
            btnExpandThongTin.setBackgroundResource(R.drawable.title_elasticity);
        }
        btnExpandThongTin.setPadding(10, 0, 0, 0);
        btnExpandThongTin.setCompoundDrawablePadding(10);
    }

    public void clickBtnKetNoi(View view) {
        onClickGotoWiFiSetting();
    }

    public void clickBtnCamera(View view) {
        try {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + PpmsCommon.PROGRAM_PHOTOS_PATH);
            if (!myDir.exists())
                myDir.mkdirs();
            String dateNow = PpmsCommon.getDateNow(3);
            //Todo file temp
            sPath = root + PpmsCommon.PROGRAM_PHOTOS_PATH + sTask.getNhanVienId() + "_" + sTask.getSoCongTo() + ".jpg";
            sPathTemp = root + PpmsCommon.PROGRAM_PHOTOS_PATH + sTask.getNhanVienId() + "_" + sTask.getSoCongTo() + "_" + dateNow + ".jpg";
            File file = new File(sPathTemp);
            if (file.exists()) file.delete();
            file.createNewFile();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            this.startActivityForResult(cameraIntent, PpmsCommon.REQUEST_CODE_DETAIL_TO_CAMERA);
        } catch (Exception e) {
            Common.showAlertDialogGreen(this, "Thông báo!", Color.WHITE, "Gặp vấn đề khi chụp ảnh!", Color.WHITE, "OK", Color.WHITE);
        }
    }

    public void clickBtnChupAnh(View view) {
        SNAPSHOT = true;
        ivImage.setVisibility(View.VISIBLE);
        ivFrame.setVisibility(View.GONE);
    }

    public void clickBtnChupLai(View view) {
        ivImage.setVisibility(View.GONE);
        ivFrame.setVisibility(View.VISIBLE);
    }

    public void clickBtnXoaDuLieu(View view) {
        //TODO check bitmap, if have not bitmap to clear fill, else clear fill and clear data in sqlite and delete image source
        Bitmap bitmap = (ivImage.getDrawable() == null) ? null : ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        if (bitmap == null) {
            etChiSoPhucTra.setText("");
            etNhanXet.setText("");
            etTinhTrangNiemPhong.setText("");
        } else {

            //TODO question do you want delete data.
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Thông báo:");
            builder.setMessage("Bạn muốn xóa dữ liệu đã được đã ghi?");
            builder.setPositiveButton("Không", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Có", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //TODO call clear UI
                    sDateNowCapture = PpmsCommon.getDateNow(1);
                    String thoiGianPhucTra = PpmsCommon.convertDateTimeType(sDateNowCapture, 2);
                    tvThoiGianPhucTra.setText(thoiGianPhucTra);
                    etChiSoPhucTra.setText("");
                    etNhanXet.setText("");
                    etTinhTrangNiemPhong.setText("");
                    ivImage.setVisibility(View.GONE);
                    ivFrame.setVisibility(View.VISIBLE);
                    //TODO delete source
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + PpmsCommon.PROGRAM_PHOTOS_PATH);
                    //TODO get name AnhCto = {maNvien}_{soCongTo}.jpg
                    String fname = sTask.getNhanVienId() + "_" + sTask.getSoCongTo() + ".jpg";

                    File file = new File(myDir, fname);
                    if (file.exists()) file.delete();

                    //TODO call clear data
                    sTask.setCoSaiChiSo("");
                    sTask.setChiSoPhucTra(0);
                    sTask.setNhanXetDeXuat("");
                    sTask.setTinhTrangNiemPhong("");
                    sTask.setNgayPhucTra("");
                    sTask.setAnhCongTo("");

                    etChiSoPhucTra.setText("");
                    etChiSoPhucTra.setHint("");
                    swtCoSaiLech.setChecked(true);
                    flagStatusChinhXac = 2;
                    etNhanXet.setHint("");
                    etNhanXet.setText("");
                    etTinhTrangNiemPhong.setHint("");
                    etTinhTrangNiemPhong.setText("");
                    ivImage.setImageBitmap(null);
                    ivImage.setBackgroundColor(Color.parseColor("#000000"));


                    //TODO clear data sqlite with flag isDelete
                    insertOrUpdateDataTaskToSqlite(sTask, true);

                    dialog.cancel();
                }
            });
            builder.show();
        }

    }

    public void clickBtnGhiDulieu(View view) {

        try {
            //TODO check input
            String CHI_SO_PHUC_TRA = etChiSoPhucTra.getText().toString();
            if (CHI_SO_PHUC_TRA.equals("")) {
                CHI_SO_PHUC_TRA = etChiSoPhucTra.getHint().toString();
            }
            if (CHI_SO_PHUC_TRA.equals("")) {
                etChiSoPhucTra.setError("Chưa có dữ liệu!");
                etChiSoPhucTra.requestFocus();
                return;
            }

            Bitmap bitmap = (ivImage.getDrawable() == null) ? null : ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
            if (bitmap == null) {
                Common.showAlertDialogGreen(this, "Thông báo", Color.WHITE, "Bạn chưa có ảnh!", Color.WHITE, "OK", Color.WHITE);
                return;
            }

            //TODO  update collumn  ANH_CTO, SAI_CHI_SO, NGAY_PHUC_TRA, NHAN_XET_PHUC_TRA, CHI_SO_PHUC_TRA, TINH_TRANG_NIEM_PHONG
            if (sDateNowCapture.equals(""))
                return;

            String root = Environment.getExternalStorageDirectory().toString();
            String fname = sTask.getNhanVienId() + "_" + sTask.getSoCongTo() + ".jpg";
            //TODO set sPath static get path image
            sPath = root + PpmsCommon.PROGRAM_PHOTOS_PATH + fname;
            sTask.setAnhCongTo(sPath);
            sTask.setChiSoPhucTra(Integer.parseInt(CHI_SO_PHUC_TRA));

            sTask.setCoSaiChiSo(swtCoSaiLech.isChecked() ? "true" : "false");

            String tinhTrangNiemPhong = (etTinhTrangNiemPhong.getText().toString().equals("")) ? etTinhTrangNiemPhong.getHint().toString() : etTinhTrangNiemPhong.getText().toString();
            String nhanXetDeXuat = (etNhanXet.getText().toString().equals("")) ? etNhanXet.getHint().toString() : etNhanXet.getText().toString();
            sTask.setNgayPhucTra(sDateNowCapture);
            sTask.setTinhTrangNiemPhong(tinhTrangNiemPhong);
            sTask.setNhanXetDeXuat(nhanXetDeXuat);

            if (!sPathTemp.equals("")) {
                //TODO check dir
                File myDir = new File(root + PpmsCommon.PROGRAM_PHOTOS_PATH);
                if (!myDir.exists())
                    myDir.mkdirs();
                //TODO check file
                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                file.createNewFile();
                //TODO write file
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(sPath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageByte = baos.toByteArray();

                bos.write(imageByte);
                bos.close();

                //Todo del fileTemp to file
                File fileTemp = new File(sPathTemp);
                if (!fileTemp.exists()) {
                    fileTemp.delete();
                }
                sPathTemp = "";
            }
            //TODO insert or update with flag isDelete false
            insertOrUpdateDataTaskToSqlite(sTask, false);


            Toast.makeText(this, "Ghi thành công", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Common.showAlertDialogGreen(this, "Lỗi ghi dữ liệu!", Color.WHITE, "Không ghi được ảnh chụp: Lỗi : " + e.getMessage(), Color.WHITE, "OK", Color.WHITE);
        }
    }

    private void clickIvTouchAnhCto() {
        Bitmap bitmap = (ivImage.getDrawable() == null) ? null : ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        if (bitmap == null) {
            return;
        }
        createDialogShowImage(bitmap);
    }
    //endregion

    //region region method camera
    private void onClickGotoWiFiSetting() {
        try {
            String pac = "com.android.settings";
            Intent i = new Intent();

            i.setClassName(pac, pac + ".wifi.p2p.WifiP2pSettings");
            try {
                // startActivity(i);
                // startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),
                        REQUEST_WIFI_SETTING);
            } catch (ActivityNotFoundException e) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH + 1) {
                    // startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS),
                            REQUEST_WIFI_SETTING);
                } else {
                    i.setClassName(pac, pac + ".wifi.WifiSettings");
                    try {
                        // startActivity(i);
                        startActivityForResult(i, REQUEST_WIFI_SETTING);
                    } catch (ActivityNotFoundException e2) {

                    }
                }
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    private void setThresHold() {
        THRES_HOLD = 5 * 10;

        if (THRES_HOLD == 0)
            CHANGE = 5;
        else if (THRES_HOLD == 300) {
            THRES_HOLD += 30000;
            CHANGE = 4;
        } else {
            THRES_HOLD += 1850;
        }
    }

    public Bitmap drawTextToBitmap(Bitmap bitmap, String ten_kh, String so_cto, String ngay_phuc_tra, String chi_so_moi) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

//		Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(bitmap.getHeight() / 20);

        Paint paint_rec = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_rec.setColor(Color.BLACK);

        // Xử lý cắt chuỗi tên xuống dòng
        String[] arrName = ten_kh.trim().split(" ");
        String name1 = "";
        String name2 = "";
        for (String sName : arrName) {
            Rect bounds_cut = new Rect();
            paint.getTextBounds(name1, 0, name1.length(), bounds_cut);
            if (bounds_cut.width() < 3 * bitmap.getWidth() / 4) {
                name1 += sName + " ";
            } else {
                name2 += sName + " ";
            }
        }
//		name = name1.trim() + "\n" + name2.trim();
        ten_kh = name1.trim();
        name_cut = name1.trim() + "\n" + name2.trim();

        Rect bounds1 = new Rect();
        paint.getTextBounds(ten_kh, 0, ten_kh.length(), bounds1);
        int x1 = 10;
        int y1 = bounds1.height();
        int y1_2 = 2 * bounds1.height();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + 5 * bounds1.height(), conf); // this creates a MUTABLE bitmap
        Canvas c = new Canvas(bmp);
        // Vẽ tên
        c.drawRect(0, 0, bmp.getWidth(), 2 * bounds1.height() + 15, paint_rec);
        c.drawRect(0, bmp.getHeight() - 2 * bounds1.height() - 15, bmp.getWidth(), bmp.getHeight(), paint_rec);
        c.drawText(ten_kh, x1, y1, paint);
        if (!name2.equals(""))
            c.drawText(name2, x1, y1_2, paint);
        // Vẽ ảnh lên khung mới có viền đen
        c.drawBitmap(bitmap, 0, 2 * bounds1.height(), paint_rec);

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());*/

        /*Rect bounds0 = new Rect();
        paint.getTextBounds(ngay_phuc_tra, 0, ngay_phuc_tra.length(), bounds0);
        c.drawRect(0, 2 * bounds1.height() + 15, bounds0.width() + 15, 3 * bounds1.height() + 15, paint_rec);

        int x5 = 10;
        int y5 = 2 * bounds1.height() + 30;
        c.drawText(ngay_phuc_tra, x5, y5, paint);*/

//        Rect bounds6 = new Rect();
//        paint.getTextBounds(ngay_phuc_tra, 0, ngay_phuc_tra.length(), bounds6);
//        int x6 = 5;//bmp.getWidth() - 10 - bounds6.width()
//        int y6 = bmp.getHeight() - 20 - bounds6.height();
//        c.drawText(ngay_phuc_tra, x6, y6, paint);

        Rect bounds2 = new Rect();
        paint.getTextBounds(ngay_phuc_tra, 0, ngay_phuc_tra.length(), bounds2);
        int x2 = 10;
        int y2 = bmp.getHeight() - 10;
        c.drawText(ngay_phuc_tra, x2, y2, paint);

        Rect bounds3 = new Rect();
        paint.getTextBounds(so_cto, 0, so_cto.length(), bounds3);
        int x3 = bmp.getWidth() - 10 - bounds3.width();
        int y3 = bmp.getHeight() - 10;
        c.drawText(so_cto, x3, y3, paint);

        // Vẽ CS_MOI lên góc trên bên phải
        Rect bounds4 = new Rect();
        paint.getTextBounds(chi_so_moi, 0, chi_so_moi.length(), bounds4);
        int x4 = bmp.getWidth() - 10 - bounds4.width();
        int y4 = bounds4.height() + 10;
        c.drawText(chi_so_moi, x4, y4, paint);

        return bmp;
    }
    //endregion

    //region region method used
    private void setDataOnView() {
        //TODO set data on view
        tvTenKH.setText(sTask.getTenKhachHang());
        tvDiaChiDungDien.setText(sTask.getDiaChiDungDien());
        tvMaSoGCS.setText(sTask.getMaSoGcs());
        tvChiSoCu.setText(String.valueOf(sTask.getChiSoCu()));
        tvChiSoMoi.setText(String.valueOf(sTask.getChiSoMoi()));
        sDateNowCapture = PpmsCommon.getDateNow(1);
        String thoiGianPhucTra = sTask.getNgayPhucTra();
        if (thoiGianPhucTra.equals("")) {
            thoiGianPhucTra = PpmsCommon.convertDateTimeType(sDateNowCapture, 2);
        } else {
            thoiGianPhucTra = PpmsCommon.convertDateTimeType(thoiGianPhucTra, 1);
        }
        tvThoiGianPhucTra.setText(thoiGianPhucTra);

//        sDateNowCapture = sTask.getNgayPhanCong();

        Cursor cursorGetImage = connection.runQueryReturnCursor(connection.getsQueryGetRowsTaskWith(sTask.getNhanVienId(), sTask.getPhanCongId()));
        sPath = cursorGetImage.getString(cursorGetImage.getColumnIndex("ANH_CTO"));
        Bitmap bitmapAnhCto = null;

        //TODO get Image with path
        if (!sPath.equals("")) {
            bitmapAnhCto = PpmsCommon.getImageWith(sPath);
        }
        if (bitmapAnhCto != null) {
            ivImage.setImageBitmap(bitmapAnhCto);
            ivImage.setVisibility(View.VISIBLE);
            ivFrame.setVisibility(View.GONE);
        } else {
            ivImage.setBackgroundColor(Color.parseColor("#000000"));
            ivImage.setVisibility(View.GONE);
            ivFrame.setVisibility(View.VISIBLE);
        }

        tvNgayPhanCong.setText(sTask.getNgayPhanCong().equals("") ? "" : PpmsCommon.convertDateTimeType(sTask.getNgayPhanCong(), 1));
        tvNgayGhiDien.setText(sTask.getNgayGhiDien().equals("") ? "" : PpmsCommon.convertDateTimeType(sTask.getNgayGhiDien(), 1));
        tvThangKTra.setText(PpmsCommon.convertDateTimeType(sTask.getThangKiemTra(), 4));
        tvMucDichSD.setText(sTask.getMucDichSuDungDien());
        tvTinhTrangSD.setText(sTask.getTinhTrangSuDungDien());
        tvSanLuongTB.setText(String.valueOf(sTask.getSanLuongTB()));
        tvTinhTrangHopCto.setText(sTask.getTinhTrangHopCongTo());
        tvSoCotCto.setText(String.valueOf(sTask.getSoCongTo()));
        tvSoHopCto.setText(String.valueOf(sTask.getSoHopCongTo()));
        tvSoOCto.setText(String.valueOf(sTask.getSoOCongTo()));
        tvLoaiCto.setText(sTask.getLoaiCongTo());
        tvLoaiHopCto.setText(sTask.getLoaiHopCongTo());

        etChiSoPhucTra.setHint(sTask.getChiSoPhucTra() == 0 ? "0" : String.valueOf(sTask.getChiSoPhucTra()));
        if (!CHI_SO_PHUC_TRA.isEmpty()) {
            etChiSoPhucTra.setText(CHI_SO_PHUC_TRA);
        }

        etNhanXet.setHint(sTask.getNhanXetDeXuat());
        if (!NHAN_XET.isEmpty()) {
            etNhanXet.setText(NHAN_XET);
        }

        etTinhTrangNiemPhong.setHint(sTask.getTinhTrangNiemPhong());
        if (!TINH_TRANG_NIEM_PHONG.isEmpty()) {
            etTinhTrangNiemPhong.setText(TINH_TRANG_NIEM_PHONG);
        }
        if (flagStatusChinhXac == 0) {
            if (sTask.getCoSaiChiSo() == null) {
                swtCoSaiLech.setChecked(true);
                flagStatusChinhXac = 2;
            } else {
                swtCoSaiLech.setChecked((sTask.getCoSaiChiSo().equals("true")) ? true : false);
                flagStatusChinhXac = (sTask.getCoSaiChiSo().equals("true")) ? 2 : 1;
            }
        } else {
            swtCoSaiLech.setChecked((flagStatusChinhXac == 1) ? false : true);
        }
        //TODO check state TRANG_THAI to enable views
        int TRANG_THAI = -1;
        String queryGetTRANG_THAI = connection.getsQueryGetTRANG_THAIWith(sTask.getNhanVienId(), sTask.getPhanCongId());
        Cursor cursorTRANG_THAI = connection.runQueryReturnCursor(queryGetTRANG_THAI);
        if (cursorTRANG_THAI != null) {
            TRANG_THAI = cursorTRANG_THAI.getInt(cursorTRANG_THAI.getColumnIndex("TRANG_THAI"));
        }

        //TODO if TRANG_THAI = 1 then show
        if (TRANG_THAI == 0 || TRANG_THAI == 1) {
            setEnableView(1);
        }
        if (TRANG_THAI == 2) {
            setEnableView(2);
        }

    }

    private void initView() {
        tvTenKH = (TextView) findViewById(R.id.ppms_activity_task_detail_tvTenKH);
        tvDiaChiDungDien = (TextView) findViewById(R.id.ppms_activity_task_detail_tvDiaChi);
        tvMaSoGCS = (TextView) findViewById(R.id.ppms_activity_task_detail_tvMaGCS);
        tvChiSoCu = (TextView) findViewById(R.id.ppms_activity_task_detail_tvChiSoCu);
        tvChiSoMoi = (TextView) findViewById(R.id.ppms_activity_task_detail_tvChiSoMoi);
        tvThoiGianPhucTra = (TextView) findViewById(R.id.ppms_activity_task_detail_tvThoiGianPhucTra);
        tvNgayPhanCong = (TextView) findViewById(R.id.ppms_activity_task_detail_tvNgayPhanCong);
        tvNgayGhiDien = (TextView) findViewById(R.id.ppms_activity_task_detail_tvNgayGhiDien);
        tvThangKTra = (TextView) findViewById(R.id.ppms_activity_task_detail_tvThangKTra);
        tvMucDichSD = (TextView) findViewById(R.id.ppms_activity_task_detail_tvMucDichSD);
        tvTinhTrangSD = (TextView) findViewById(R.id.ppms_activity_task_detail_tvTinhTrangSD);
        tvSanLuongTB = (TextView) findViewById(R.id.ppms_activity_task_detail_tvSanLuongTB);
        tvTinhTrangHopCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvTinhTrangHopCto);
        tvSoCotCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvSoCotCto);
        tvSoHopCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvSoHopCto);
        tvSoOCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvSoOCto);
        tvLoaiCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvLoaiCto);
        tvLoaiHopCto = (TextView) findViewById(R.id.ppms_activity_task_detail_tvLoaiHopCto);

        tvStatusConnectCamera = (TextView) findViewById(R.id.ppms_detail_task_tv_record_status);

        etTinhTrangNiemPhong = (EditText) findViewById(R.id.ppms_activity_task_detail_etTinhTrangMoi);
        etNhanXet = (EditText) findViewById(R.id.ppms_activity_task_detail_etNhanXet);
        etChiSoPhucTra = (EditText) findViewById(R.id.ppms_activity_task_detail_etChiSoPhucTra);

        btnKetNoi = (Button) findViewById(R.id.ppms_activity_task_detail_btnKetNoi);
        btnChupAnh = (Button) findViewById(R.id.ppms_activity_task_detail_btnChupAnh);
        btnChupLai = (Button) findViewById(R.id.ppms_activity_task_detail_btnChupLai);
        btnCamera = (Button) findViewById(R.id.ppms_activity_task_detail_btnCamera);
        btnGhi = (Button) findViewById(R.id.ppms_activity_task_detail_btnGhi);
        btnXoa = (Button) findViewById(R.id.ppms_activity_task_detail_btnXoa);
        btnExpandChiTiet = (Button) findViewById(R.id.ppms_activity_task_detail_btnExpandChiTiet);
        btnExpandThongTin = (Button) findViewById(R.id.ppms_activity_task_detail_btnExpandThongTin);

        ivImage = (ImageView) findViewById(R.id.ppms_activity_task_detail_ivImage);
        ivFrame = (ImageView) findViewById(R.id.ppms_activity_task_detail_ivFrame);

        swtCoSaiLech = (Switch) findViewById(R.id.ppms_activity_task_detail_swtSaiLech);

        llExpandThongTin = (LinearLayout) findViewById(R.id.ppms_activity_task_detail_llExpandThongTin);
        llExpandChiTiet = (LinearLayout) findViewById(R.id.ppms_activity_task_detail_llExpandChiTiet);

        llExpandThongTin.setVisibility(View.VISIBLE);
        btnExpandThongTin.setBackgroundResource(R.drawable.title_elasticity);
        btnExpandThongTin.setCompoundDrawablePadding(10);
        btnExpandThongTin.setPadding(10, 0, 0, 0);

        llExpandChiTiet.setVisibility(View.VISIBLE);
        btnExpandChiTiet.setBackgroundResource(R.drawable.title_elasticity);
        btnExpandChiTiet.setCompoundDrawablePadding(10);
        btnExpandChiTiet.setPadding(10, 0, 0, 0);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        sTask = null;
        sTask = (PpmsEntityTask) bundle.get("TASK");
        typeCallTaskDetail = bundle.getInt("TYPE_CALL_TASK_DETAIL", -1);
        positionTask = bundle.getInt("POSITION", -1);
        if (bundle.containsKey("FLAG_STATUS_CHINH_XAC"))
            flagStatusChinhXac = bundle.getInt("FLAG_STATUS_CHINH_XAC", 0);

        if (bundle.containsKey("CHI_SO_PHUC_TRA"))
            CHI_SO_PHUC_TRA = bundle.getString("CHI_SO_PHUC_TRA", "");

        if (bundle.containsKey("NHAN_XET"))
            NHAN_XET = bundle.getString("NHAN_XET", "");

        if (bundle.containsKey("TINH_TRANG_NIEM_PHONG"))
            TINH_TRANG_NIEM_PHONG = bundle.getString("TINH_TRANG_NIEM_PHONG", "");
//        if(bundle.containsKey("DATE_NOW")){
//            sDateNowCapture = bundle.getString("DATE_NOW");
//        }
//        if(bundle.containsKey("PATH_IMAGE")){
//            sPath = bundle.getString("PATH_IMAGE");
//        }
        connection = PpmsSqliteConnection.getInstance(this);
    }

    private void setAction() {
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIvTouchAnhCto();
            }
        });
        swtCoSaiLech.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (flagStatusChinhXac != 0) {
                    flagStatusChinhXac = (swtCoSaiLech.isChecked()) ? 2 : 1;
                }
            }
        });
    }

    private void createDialogShowImage(Bitmap bmRoot) {
        try {
            //TODO create dialog
            final Dialog dialogImageZoom = new Dialog(this);
            dialogImageZoom.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogImageZoom.setContentView(R.layout.ppms_diaglog_view_image);
            dialogImageZoom.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogImageZoom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogImageZoom.setCanceledOnTouchOutside(false);

            //TODO set image
            final ImageViewTouch ivImageTouch = (ImageViewTouch) dialogImageZoom.findViewById(R.id.ppms_dialog_ivTouch_anhCto);
            ivImageTouch.setImageBitmapReset(bmRoot, 0, true);
            dialogImageZoom.show();
        } catch (Exception ex) {
            Toast.makeText(this, "Lỗi:" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void insertOrUpdateDataTaskToSqlite(PpmsEntityTask task, boolean isDelete) {
        try {
            //TODO insert into task or update task
            //TODO check task exist
            String checkExistRowTask = connection.getQueryCheckTaskExistWithMA_PHAN_CONG(String.valueOf(task.getPhanCongId()));
            boolean isExistRowTask = connection.isRowExistData(checkExistRowTask);
            long rowsAffectTask = 0;

            ContentValues contentValues = new ContentValues();
            contentValues.put("MA_PHAN_CONG", task.getPhanCongId());
            contentValues.put("MA_DVIQLY", task.getMaDonViQLy());
            contentValues.put("MA_DDO", task.getMaDiemDo());
            contentValues.put("MA_SO_GCS", task.getMaSoGcs());
            contentValues.put("MA_KH", task.getMaKhachHang());
            contentValues.put("CHI_SO_MOI", task.getChiSoMoi());
            contentValues.put("CHI_SO_CU", task.getChiSoCu());
            contentValues.put("SAN_LUONG", task.getSanLuong());
            contentValues.put("SO_CTO", task.getSoCongTo());
            contentValues.put("MA_CTO", task.getMaCongTo());
            contentValues.put("NGAY_PHAN_CONG", task.getNgayPhanCong());
            contentValues.put("THANG_KIEM_TRA", task.getThangKiemTra());
            contentValues.put("MA_NVIEN", task.getNhanVienId());
            contentValues.put("TY_LE_CHENH_LECH", task.getTyLeChenhLech());
            contentValues.put("SAN_LUONG_TB", task.getSanLuongTB());
            contentValues.put("TEN_TRAM", task.getTenTram());
            contentValues.put("HS_NHAN", task.getHeSoNhan());
            contentValues.put("ANH_CTO", task.getAnhCongTo());
            contentValues.put("SAI_CHI_SO", task.getCoSaiChiSo());
            contentValues.put("TEN_KH", task.getTenKhachHang());
            contentValues.put("DIA_CHI_DUNG_DIEN", task.getDiaChiDungDien());
            contentValues.put("SO_O", task.getSoOCongTo());
            contentValues.put("SO_COT", task.getSoCotCongTo());
            contentValues.put("LOAI_HOP", task.getLoaiHopCongTo());
            contentValues.put("LOAI_CTO", task.getLoaiCongTo());
            contentValues.put("TINH_TRANG_CTO", task.getTinhTrangHopCongTo());
            contentValues.put("NGAY_PHUC_TRA", task.getNgayPhucTra());
            contentValues.put("NGAY_GHI_DIEN", task.getNgayGhiDien());
            contentValues.put("MUC_DICH_SD_DIEN", task.getMucDichSuDungDien());
            contentValues.put("TINH_TRANG_SD_DIEN", task.getTinhTrangSuDungDien());
            contentValues.put("NHAN_XET_PHUC_TRA", task.getNhanXetDeXuat());
            contentValues.put("CHI_SO_PHUC_TRA", task.getChiSoPhucTra());
            contentValues.put("TINH_TRANG_NIEM_PHONG", task.getTinhTrangNiemPhong());

            //TODO insert with TRANG_THAI = 1 is update
            if (isDelete) {
                contentValues.put("TRANG_THAI", 0);
            } else {
                contentValues.put("TRANG_THAI", 1);
            }

            if (isExistRowTask) {
                rowsAffectTask = connection.updateData(contentValues, connection.TABLE_TASK, connection.MA_PHAN_CONG, String.valueOf(task.getPhanCongId()));
            } else {
                rowsAffectTask = connection.insertData(contentValues, connection.TABLE_TASK);
            }

            if (rowsAffectTask == 0) {
                Common.showAlertDialogGreen(this, "Lỗi ghi dữ liệu!", Color.WHITE, "Không có biên bản phúc tra nào được cập nhật!", Color.WHITE, "OK", Color.WHITE);
            }

        } catch (SQLiteException sqlex) {
            Common.showAlertDialogGreen(this, "Lỗi ghi dữ liệu!", Color.WHITE, sqlex.getMessage(), Color.WHITE, "OK", Color.WHITE);
            return;
        }
    }

    //endregion

    /**
     * enable view
     *
     * @param i = 1: when not commit then show all. if i = 2: when uploaded
     */
    private void setEnableView(int i) {

        btnChupAnh.setEnabled(true);
        btnChupLai.setEnabled(true);
        btnGhi.setEnabled(true);
        btnXoa.setEnabled(true);
        btnCamera.setEnabled(true);
        btnKetNoi.setEnabled(true);
        etChiSoPhucTra.setEnabled(true);
        etTinhTrangNiemPhong.setEnabled(true);
        etNhanXet.setEnabled(true);

        if (i == 2) {
            btnChupAnh.setEnabled(false);
            btnChupLai.setEnabled(false);
            btnGhi.setEnabled(false);
            btnXoa.setEnabled(false);
            btnCamera.setEnabled(false);
            btnKetNoi.setEnabled(false);

            etChiSoPhucTra.setEnabled(false);
            etTinhTrangNiemPhong.setEnabled(false);
            etNhanXet.setEnabled(false);
        }
    }

}

