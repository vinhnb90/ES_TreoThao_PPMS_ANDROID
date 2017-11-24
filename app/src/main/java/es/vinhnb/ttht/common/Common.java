package es.vinhnb.ttht.common;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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

    public static final long DELAY = 1000;
    public static final long DELAY_PROGESS_PBAR = 20;
    public static String URLServer = "";

    public static void setURLServer(String andressServer) {
        URLServer = "http://" + andressServer + "/api/ServiceMTB/";
    }

    public static void copyTextClipBoard(Context context, String message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", message);
        clipboard.setPrimaryClip(clip);
    }

    public enum MESSAGE {
        ex01("Gặp vấn đề với việc lấy dữ liệu share pref đăng nhập của phiên trước!"),
        ex02("Không có dữ liệu trả về!"),
        ex03("Lỗi khởi tạo!"),
        ex04("Lỗi hiển thị!"),
        ex05("Gặp vấn đề đồng bộ!"),
        ex06("Gặp vấn đề kết nối, vui lòng thử lại!"),
        ex07("Gặp vấn đề khi ghi dữ liệu!"),
        ex0x("Xảy ra lỗi bất ngờ!");

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


    //region data table sql decraled
    //IS_TU trong bảng TABLE_CHITIET_TUTI
    public enum IS_TU {
        TU(true, "TU"),
        TI(false, "TI");

        public boolean code;
        public String content;

        IS_TU(boolean code, String content) {
            this.content = content;
            this.code = code;
        }

        public static IS_TU findIS_TU(String content) {
            for (IS_TU isTu : values()) {
                if (isTu.content.equalsIgnoreCase(content))
                    return isTu;
            }
            return null;
        }
    }

    //Mã biến động
    public enum MA_BDONG {
        B("B", "Treo"),
        E("E", "Tháo");

        public String code;
        public String content;

        MA_BDONG(String code, String content) {
            this.content = content;
            this.code = code;
        }

        public static MA_BDONG findMA_BDONG(String content) {
            for (MA_BDONG maBdong : values()) {
                if (maBdong.content.equalsIgnoreCase(content))
                    return maBdong;
            }
            return null;
        }
    }

    //trạng thái dữ liệu
    public enum TRANG_THAI_DU_LIEU {
        CHUA_TON_TAI("Chưa tồn tại"),
        CHUA_GHI("Chưa ghi"),
        DA_GHI("Đã ghi"),
        DA_GUI("Đã gửi");

        public String content;

        TRANG_THAI_DU_LIEU(String content) {
            this.content = content;
        }

        public static TRANG_THAI_DU_LIEU findTRANG_THAI_DU_LIEU(String content) {
            for (TRANG_THAI_DU_LIEU trangThaiDuLieu : values()) {
                if (trangThaiDuLieu.content.equalsIgnoreCase(content))
                    return trangThaiDuLieu;
            }
            return null;
        }
    }


    //Kiểu gọi TYPE_CALL_API trong TABLE_HISTORY
    public enum TYPE_CALL_API {
        DOWNLOAD("DOWNLOAD"),
        UPLOAD("UPLOAD");

        public String content;

        TYPE_CALL_API(String content) {
            this.content = content;
        }

        public static TYPE_CALL_API findTYPE_CALL_API(String content) {
            for (TYPE_CALL_API typeCallApi : values()) {
                if (typeCallApi.content.equalsIgnoreCase(content))
                    return typeCallApi;
            }
            return null;
        }
    }

    //Kiểu gọi TYPE_RESULT trong TABLE_HISTORY
    public enum TYPE_RESULT {
        SUCCESS("THÀNH CÔNG"),
        ERROR("CÓ LỖI");

        public String content;

        TYPE_RESULT(String content) {
            this.content = content;
        }

        public static TYPE_RESULT findTYPE_RESULT(String content) {
            for (TYPE_RESULT typeCallApi : values()) {
                if (typeCallApi.content.equalsIgnoreCase(content))
                    return typeCallApi;
            }
            return null;
        }
    }


    //endregion


    //region tvDate time
    public enum DATE_TIME_TYPE {
        type1("HHmmss"),
        type2("yyyyMMdd"),
        type3("yyyyMMddHHmmss"),
        type4("yyyy-MM-dd'T'hh:mm:ssZ"),
        type5("MM/yyyy"),
        type6("dd/MM/yyyy"),
        type7("dd/MM/yyyy HH:mm:ss"),
        type8("dd/MM/yyyy HH:mm"),
        type9("dd/MM/yyyy HH'h'mm"),
        type10("MM/dd/yyyy HH:mm:ss a"),
        type11("yyyy-MM-dd HH:mm:ss"),
        //2017-11-23T22:18
        sqlite1("yyyy-MM-dd'T'HH:mm"),
        sqlite2("yyyy-MM-dd'T'HH:mm:ss"),


        typeEx("typeEx");

        public String content;

        DATE_TIME_TYPE(String content) {
            this.content = content;
        }

        public static DATE_TIME_TYPE findDATE_TIME_TYPE(String content) {
            for (DATE_TIME_TYPE dateTimeType : values()) {
                if (dateTimeType.content.equalsIgnoreCase(content))
                    return dateTimeType;
            }
            return null;
        }
    }

    public static String getDateTimeNow(DATE_TIME_TYPE formatDate) {
        SimpleDateFormat df = new SimpleDateFormat(formatDate.content);
        return df.format(Calendar.getInstance().getTime());
    }

    public static long convertDateToLong(String date, DATE_TIME_TYPE typeDefault) {
        SimpleDateFormat formatter = new SimpleDateFormat(typeDefault.content);
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

        SimpleDateFormat df2 = new SimpleDateFormat(format.content);
//        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(time);
        return df2.format(date);
    }

    public static String convertDateToDate(String time, DATE_TIME_TYPE typeDefault, DATE_TIME_TYPE typeConvert) {
        if (time == null || time.trim().isEmpty())
            return "";
        long longDate = Common.convertDateToLong(time, typeDefault);
        String dateByDefaultType = Common.convertLongToDate(longDate, typeConvert);
        return dateByDefaultType;
    }
    //endregion


}
