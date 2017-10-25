package com.es.tungnv.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.es.tungnv.views.PpmsLoginActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.es.tungnv.utils.TthtCommon.SIZE_HEIGHT_IMAGE;
import static net.sourceforge.zbar.Config.ASCII;

/**
 * Created by VinhNB on 11/10/2016.
 */

public class PpmsCommon {
    public static final int REQUEST_CODE_DETAIL_TO_CAMERA = 1;
    public static final int REQUEST_CODE_DOISOAT_TO_DETAIL = 1111;
    public static final int RESPONSE_CODE_DETAIL_TO_DOISOAT = 2222;


    public static final String PROGRAM_DB_PATH = "/PPMS/DB/";
    public static final String PROGRAM_DB_BACKUP_PATH = "/PPMS/DB/BACKUP/";
    public static final String DATABASE_NAME = "PPMS.s3db";
    public static final String PROGRAM_PATH = "/PPMS/";
    public static final String CFG_FILENAME = "PPMS.cfg";
    public static final String PROGRAM_PHOTOS_PATH = "/PPMS/PHOTOS/";

    private static final String TRANSFORMATION_PADDING = "DESede/CBC/PKCS5Padding";
    private static final String ALGORITHM = "md5";

    public static String convertDateTimeType(String dateTime, int type) {
        if (dateTime.equals(""))
            return dateTime;
        else {
            String[] _dateTime = null;
            String date = null;
            String time = null;
            String[] _date = null;
            String[] _time = null;
            if (dateTime.contains("T")) {
                _dateTime = dateTime.trim().split("T");
                date = _dateTime[0];
                time = _dateTime[1];
                _date = date.split("-");
                _time = time.split(":");

                // from 2016-07-11T00:00:00.000 to 11/07/2016
                if (type == 1) {
                    return _date[2] + "/" + _date[1] + "/" + _date[0];
                }
                // from 2016-07-11T15:30:00.000 to 15:30 - 11/07/2016
                if (type == 2) {
                    return _time[0] + ":" + _time[1] + " - " + _date[2] + "/" + _date[1] + "/" + _date[0];
                }
                // from 2016-07-11T15:30:00.000 to 20161107_1530
                if (type == 3) {
                    return _date[0] + _date[1] + _date[1] + "_" + _time[0] + _time[1];
                }
                // from 2016-07-11T15:30:00.000 to 07
                if (type == 4) {
                    return _date[1];
                }
                return "";
            } else {
                //convert 11/07/2016 to 2016-07-11
                _dateTime = dateTime.trim().split("/");
                if (type == 4) {
                    if (_dateTime.length == 3) {
                        if (_dateTime[0].length() == 1)
                            _dateTime[0] = "0" + _dateTime[0];
                        if (_dateTime[1].length() == 1)
                            _dateTime[1] = "0" + _dateTime[1];
                        return _dateTime[2] + "-" + _dateTime[1] + "-" + _dateTime[0];
                    } else return "";
                } else return "";
            }
        }
    }

    public static String getJSONData(String urlString) {
        // lấy chuỗi JSON từ server
        BufferedReader reader = null;
        StringBuffer buffer = null;
        InputStreamReader isr;
        URL url = null;
        try {
            url = new URL(urlString);
            isr = new InputStreamReader(url.openStream());
            reader = new BufferedReader(isr);

            buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public static boolean isConnectingWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            return false;
        } else return true;
    }
/*
    public static void LoadFolder2(Context context)
    {
        // snippet taken from question
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        path = new File(path, PpmsCommon.PROGRAM_DB_PATH);
        path.mkdirs();

// initiate media scan and put the new things into the path array to
// make the scanner aware of the location and the files you want to see
        MediaScannerConnection.scanFile(context, new String[] {path.toString()}, null, null);
    }*/

    /*public static void LoadFolder(ContextWrapper ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File file_db = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH);

            FilenameFilter fileNameFilter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (!name.equals("BACKUP")) {
                        return true;
                    }
                    return false;
                }
            };

            String[] allFilesDb = file_db.list(fileNameFilter);
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            File file_db_backup = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_BACKUP_PATH);
            String[] allFilesDbBackup = file_db_backup.list();
            if (allFilesDbBackup.length > 0) {
                for (int i = 0; i < allFilesDbBackup.length; i++) {
                    allFilesDbBackup[i] = Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_BACKUP_PATH + allFilesDbBackup[i];
                }
                if (allFilesDbBackup != null)
                    scanFile(ctx, allFilesDbBackup);
            }

//            String[] allFiles = new String[]{Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME};
//            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PATH)));
        }
    }

    public static void scanFile(Context ctx, String[] allFiles) {
        MediaScannerConnection.scanFile(ctx, allFiles, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("ExternalStorage", "Scanned " + path + ":");
                        Log.d("ExternalStorage", "uri=" + uri);
                    }
                });
    }*/


    public static void LoadFolder(ContextWrapper ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File file_db = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH);

            FilenameFilter fileNameFilter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (!name.equals("BACKUP")) {
                        return true;
                    }
                    return false;
                }
            };

            String[] allFilesDb = file_db.list(fileNameFilter);
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            File file_db_backup = new File(Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_BACKUP_PATH);
            String[] allFilesDbBackup = file_db_backup.list();
            if (allFilesDbBackup.length > 0) {
                for (int i = 0; i < allFilesDbBackup.length; i++) {
                    allFilesDbBackup[i] = Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_DB_BACKUP_PATH + allFilesDbBackup[i];
                }
                if (allFilesDbBackup != null)
                    scanFile(ctx, allFilesDbBackup);
            }

            String[] allFiles = new String[]{Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PATH + PpmsCommon.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + PpmsCommon.PROGRAM_PATH)));
        }
    }

    public static void scanFile(Context ctx, String[] allFiles) {
            MediaScannerConnection.scanFile(ctx, allFiles, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("ExternalStorage", "Scanned " + path + ":");
                        Log.d("ExternalStorage", "uri=" + uri);
                    }
                });
    }


    public static String getDateNow(int type) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = null;
        if (type == 1)
            df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (type == 2)
            //TODO return for image name
            df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        if (type == 3)
            //TODO return for link Image
            df = new SimpleDateFormat("ddMMyyyyy");
        String dateTimeNow = df.format(calendar.getTime());
        return dateTimeNow;
    }

    public static Bitmap getImageWith(String pathImage) {
        File imgFile = new File(pathImage);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;
        }
        return null;
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

    public static void showTitleByDialog(final Context context, String title, String message, boolean hasButton, final long timeVisible) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message);
        if (hasButton) {
            dialog.setPositiveButton("Chấp nhận!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
        }
        final AlertDialog alert = dialog.create();
        alert.show();

// Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    if (alert.isShowing() && timeVisible!=0) {
                        alert.dismiss();
                    }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        if (timeVisible != 0)
            handler.postDelayed(runnable, timeVisible);
        else
            handler.post(runnable);
    }

    public static String decode64String(String input) {
//        byte[] encodedBytes = Base64.encodeBase64(input.getBytes());
//
//        return new String(encodedBytes).toString();
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        return base64;
    }

    public static String endecode64String(String inputCode) {
        String text = "";
        byte[] data = Base64.decode(inputCode, Base64.NO_WRAP);
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String convertToMd5(final String text) {
        StringBuffer sb = null;
        try {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(text.getBytes("UTF-8"));
            sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (final java.security.NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void scaleImage(String fileName, Context context) {
        File file = new File(fileName);

        BufferedOutputStream bos = null;
        try {
            if (!file.exists()) {
                throw new IOException("Không tìm thấy ảnh");
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

            if (bitmap != null) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight();
                if (h < w) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = TthtCommon.scaleDown(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), TthtCommon.SIZE_HEIGHT_IMAGE, true);
                } else {
                    bitmap = TthtCommon.scaleDown(bitmap, TthtCommon.SIZE_HEIGHT_IMAGE, true);
                }
                //TODO w lúc nào cũng lớn hơn h và sau khi resize thì h luôn = 600, w > 0, ảnh được xoay nếu w < h tạo ra hình chữ nhật

                bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bos.write(Common.encodeTobase64Byte(bitmap));
                bos.close();
                TthtCommon.scanFile(context, new String[]{fileName});
            }
        } catch (IOException ex) {
            Common.showAlertDialogGreen(context, "Lỗi scale image khi ghi ảnh", Color.RED, "Lỗi lưu ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    public static Bitmap drawTextOnBitmapCongTo(Bitmap bmRoot, String VI_TRI_1, String VI_TRI_3, String VI_TRI_4_1, String VI_TRI_4_2) throws Exception {
        if (bmRoot == null)
            throw new Exception("Không có ảnh chụp phúc tra");

        //TODO Tạo paint để vẽ nền đen
        Paint paint_background = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_background.setColor(Color.BLACK);

        //TODO tạo paint text để vẽ text
        final int text_size = 20;
        final int paddingBetweenText = 10;
        Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_text.setColor(Color.WHITE);
        paint_text.setTextSize(text_size);//textSize = 20
        final int textHeight = Math.round(paint_text.descent() - paint_text.ascent());

        // TODO tính dòng sẽ được vẽ của chuỗi dài chưa xác định TEN_KH, không cần xác định x, y
        int soDongCuaTextVI_TRI_1 = TthtCommon.drawTextAndBreakLine(true, null, paint_text, 0, 0, textHeight, bmRoot.getWidth(), VI_TRI_1, paddingBetweenText);

        // TODO tính dòng sẽ được vẽ của chuỗi dài chưa xác định CHI_SO
        int soDongCuaTextVI_TRI_3 = TthtCommon.drawTextAndBreakLine(true, null, paint_text, 0, 0, textHeight, bmRoot.getWidth(), VI_TRI_3, paddingBetweenText);

        //TODO tạo bitmap với diện tích như khung chứa để vẽ ảnh và thông tin
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmapResult = null;
        //TODO do fix cứng h của nội dung ảnh là SIZE_HEIGHT_IMAGE, nên khi resize thì w thay đổi,
        //TODO nếu < 1 giá trị nào đó thì không đủ bề ngang w để ghi thông tin, (SIZE_HEIGHT_IMAGE >600 thì ghi đủ), ta chèn thêm viền đen vào nếu không đủ ghi
        if (bmRoot.getWidth() > TthtCommon.SIZE_WIDTH_IMAGE_BASIC) {
            bitmapResult = Bitmap.createBitmap(
                    bmRoot.getWidth(),
                    paddingBetweenText / 2
                            + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                            + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText)
                            + SIZE_HEIGHT_IMAGE
                            + paddingBetweenText
                            + textHeight
                            + paddingBetweenText / 2
                    , conf);
        } else
            bitmapResult = Bitmap.createBitmap(
                    TthtCommon.SIZE_WIDTH_IMAGE_BASIC,
                    paddingBetweenText / 2
                            + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                            + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText)
                            + SIZE_HEIGHT_IMAGE
                            + paddingBetweenText
                            + textHeight
                            + paddingBetweenText / 2
                    , conf);

        //TODO khởi tạo canvas
        Canvas canvas = new Canvas(bitmapResult);


        //TODO vẽ ảnh lên khung tại tọa độ với trên ảnh (soDongCuaText  + 1dòng TYPE_IMAGE + 1 dòng chỉ số...) + (+ 1 dòng MA_DDO)
        //TODO kiểm tra nếu anh có chiều cao lớn hơn bình thường
        //TODO tức đã được đính kèm info thì ta lấy vị trí ảnh từ
        int x0 = 0;
        int y0 = paddingBetweenText / 2
                + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText); //vẽ từ vị trí trên TthtCommon.SIZE_HEIGHT_IMAGE

        //TODO tạo khung chứa bitmap từ tọa độ x0, y0 tới .. ...
        RectF frameBitmap = new RectF(x0, y0, x0 + bmRoot.getWidth(), y0 + SIZE_HEIGHT_IMAGE);


        //TODO vẽ full ảnh resultBimap với màu đen làm nền
        canvas.drawRect(0, 0, bitmapResult.getWidth(), bitmapResult.getHeight(), paint_background);

        //TODO nếu đã được ghi
        if (bmRoot.getHeight() > SIZE_HEIGHT_IMAGE) {
            Rect src = new Rect(x0, bmRoot.getHeight() - textHeight - paddingBetweenText - Math.round(paddingBetweenText / 2) - SIZE_HEIGHT_IMAGE, bmRoot.getWidth(), bmRoot.getHeight() - textHeight - paddingBetweenText - Math.round(paddingBetweenText / 2));
            Rect des = null;
            //TODO nếu ảnh có w <= 600 thì vẽ ảnh ở giữa
            if (bmRoot.getWidth() <= TthtCommon.SIZE_WIDTH_IMAGE_BASIC) {
                des = new Rect(Math.round((TthtCommon.SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0, TthtCommon.SIZE_WIDTH_IMAGE_BASIC - Math.round((TthtCommon.SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0 + SIZE_HEIGHT_IMAGE);
            } else {
                des = new Rect(x0, y0, bitmapResult.getWidth(), y0 + SIZE_HEIGHT_IMAGE);
            }
            //TODO ngược lại vẽ full ảnh
            //TODO vẽ một phần chính của ảnh bitmap trên trên khung chứa bitmap với bút paint màu đen
            canvas.drawBitmap(bmRoot, src, des, paint_background);
        } else {
            //TODO vẽ full ở giữa
            //TODO nếu ảnh có w <= 600 thì vẽ ảnh ở giữa
            RectF frameBitmapCenter = null;
            if (bmRoot.getWidth() <= TthtCommon.SIZE_WIDTH_IMAGE_BASIC) {
                frameBitmapCenter = new RectF(Math.round((TthtCommon.SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0, TthtCommon.SIZE_WIDTH_IMAGE_BASIC - Math.round((TthtCommon.SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0 + SIZE_HEIGHT_IMAGE);
            } else {
                frameBitmapCenter = new RectF(x0, y0, bitmapResult.getWidth(), y0 + SIZE_HEIGHT_IMAGE);
            }
            //TODO vẽ full bitmap trên trên khung chứa bitmap với bút paint màu đen
            canvas.drawBitmap(bmRoot, null, frameBitmapCenter, paint_background);
        }

        //TODO vẽ VI_TRI_1
        TthtCommon.drawTextAndBreakLine(false, canvas, paint_text, 0, paddingBetweenText / 2 + textHeight, textHeight, bmRoot.getWidth(), VI_TRI_1, paddingBetweenText);

        int y_VI_TRI_3 = soDongCuaTextVI_TRI_1 * (textHeight + paddingBetweenText);

        //TODO vẽ CHI_SO
        TthtCommon.drawTextAndBreakLine(false, canvas, paint_text, 0, y_VI_TRI_3 + textHeight + paddingBetweenText, textHeight, bmRoot.getWidth(), VI_TRI_3, paddingBetweenText);


        //TODO điền SO_CTO
        Rect khungSO_CTO = new Rect();
        paint_text.getTextBounds(VI_TRI_4_1, 0, (VI_TRI_4_1).length(), khungSO_CTO);
        int x_SO_CTO = 0;
        int y_SO_CTO = bitmapResult.getHeight();
        canvas.drawRect(0, y_SO_CTO - textHeight - paddingBetweenText, VI_TRI_4_1.length(), y_SO_CTO, paint_background);

        //Vẽ text SO_CTO cách thêm 1 khoảng dưới cùng 1 đoạn paddingBetweenText/2
        canvas.drawText(VI_TRI_4_1, x_SO_CTO, y_SO_CTO - paddingBetweenText / 2, paint_text);

        //TODO điền MA_DDO
        Rect khungMA_DDO = new Rect();
        paint_text.getTextBounds(VI_TRI_4_2, 0, VI_TRI_4_2.length(), khungMA_DDO);
        int textWidthMA_DDO = Math.round(paint_text.measureText(VI_TRI_4_2));
        int x_MA_DDO = bitmapResult.getWidth() - textWidthMA_DDO;
        int y_MA_DDO = y_SO_CTO;
        canvas.drawRect(x_MA_DDO, y_MA_DDO - textHeight - paddingBetweenText, bitmapResult.getWidth(), y_MA_DDO, paint_background);

        //Vẽ text MA_DDO cách thêm 1 khoảng dưới cùng 1 đoạn paddingBetweenText/2
        canvas.drawText(VI_TRI_4_2, x_MA_DDO, y_MA_DDO - paddingBetweenText / 2, paint_text);

        return bitmapResult;


    }
}
