package es.vinhnb.ttht.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

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

    public static String URLServer = "";
    public static void setURLServer(String andressServer) {
        URLServer = "http://" + andressServer + "/api/ServiceMTB/";
    }

    public enum MESSAGE {
        ex01("Gặp vấn đề với việc lấy dữ liệu share pref đăng nhập của phiên trước!"),
        ex02("Không có dữ liệu trả về!"),
        ex03("Lỗi khởi tạo!"),
        ex04("Lỗi hiển thị!"),
        ex05("Gặp vấn đề đồng bộ!"),
        ex06("Gặp vấn đề kết nối, vui lòng thử lại!"),

        ex0x("Xảy ra lỗi bất ngờ!")
        ;

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


    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    public static boolean isNetworkConnected(Context context) throws Exception {
        if (context == null)
            return false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }


    public static String getVersion(Context context) throws Exception {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
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

    public static String getDateTimeNow(DATE_TIME_TYPE formatDate) {
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

    public static String convertLongToDate(long time, DATE_TIME_TYPE format) {
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
