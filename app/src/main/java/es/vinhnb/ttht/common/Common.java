package es.vinhnb.ttht.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.vinhnb.ttht.view.TthtHnLoginActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by VinhNB on 8/9/2017.
 */

public class Common {

    public static final String PATH_DB = Environment.getExternalStorageDirectory() + File.separator + "ES_DB_TEST" + File.separator;
    public static final String NAME_FILE_DB = "ES_Database_Test.s3db";

    public enum MESSAGE {
        ex01("Gặp vấn đề với việc lấy dữ liệu share pref đăng nhập của phiên trước!");

        private String content;

        MESSAGE(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    public static final int REQUEST_CODE_PERMISSION = 100;
    public static boolean checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(activity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA,
                }, REQUEST_CODE_PERMISSION);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }


    //region date time
    public enum DATE_TIME_TYPE {
        HHmmss,
        yyyyMMdd,
        yyyyMMddHHmmssSSS,
        yyyyMMddHHmmssSSZ,
        MMddyyyyHHmmssa,
        MMyyyy,
        ddMMyyyyHHmm,
        ddMMyyyy,
        ddMMyyyyHHmmss,
        FULL;

        @Override
        public String toString() {
            if (this == HHmmss)
                return "HHmmss";
            if (this == yyyyMMdd)
                return "yyyyMMdd";
            if (this == yyyyMMddHHmmssSSS)
                return "yyyyMMddHHmmss";
            if (this == yyyyMMddHHmmssSSZ)
                return "yyyy-MM-dd'T'hh:mm:ssZ";
            if (this == MMyyyy)
                return "MM/yyyy";
            if (this == ddMMyyyy)
                return "dd/MM/yyyy";
            if (this == ddMMyyyyHHmmss)
                return "dd/MM/yyyy HH:mm:ss";
            if (this == ddMMyyyyHHmm)
                return "dd/MM/yyyy HH'h'mm";
            if (this == MMddyyyyHHmmssa)
                //2017-08-01T00:00:00+07:00
                return "MM/dd/yyyy HH:mm:ss a";
            if (this == FULL)
                return "yyyy-MM-dd HH:mm:ss";
            return super.toString();
        }
    }

    public static String getDateTimeNow(Common.DATE_TIME_TYPE formatDate) {
        SimpleDateFormat df = new SimpleDateFormat(formatDate.toString());
        return df.format(Calendar.getInstance().getTime());
    }

    public static long convertDateToLong(String date, DATE_TIME_TYPE typeDefault) {
        SimpleDateFormat formatter = new SimpleDateFormat(typeDefault.toString());
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateParse;
        try {
            dateParse = (Date) formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        return dateParse.getTime();
    }

    public static String convertLongToDate(long time, Common.DATE_TIME_TYPE format) {
        if (time < 0)
            return null;
        if (format == null)
            return null;

        SimpleDateFormat df2 = new SimpleDateFormat(format.toString());
//        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(time);
        return df2.format(date);
    }
    //endregion

}
