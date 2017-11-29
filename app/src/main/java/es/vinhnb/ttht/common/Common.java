package es.vinhnb.ttht.common;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Window;

import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.utils.TthtConstantVariables;
import com.es.tungnv.views.R;
import com.es.tungnv.zoomImage.ImageViewTouch;

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
    public static final String PROGRAM_PHOTOS_PATH = "/TTHT/PHOTOS/";
    public static String URLServer = "";
    public static String[] arrSoVien = {"0", "1", "2", "3", "4", "5"};
    public static String[] arrLoaiHom = {"0", "2", "4", "8"};

    public static void setURLServer(String andressServer) {
        URLServer = "http://" + andressServer + "/api/ServiceMTB/";
    }

    public static void copyTextClipBoard(Context context, String message) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", message);
        clipboard.setPrimaryClip(clip);
    }

    public static String getRecordDirectoryFolder(String folderName) {
        String path = Environment.getExternalStorageDirectory().getPath() + Common.PROGRAM_PHOTOS_PATH + "/" + folderName;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    public static void zoomImage(Context context, Bitmap bmImage) throws Exception {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.essp_dialog_viewimage);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        final ImageViewTouch ivtImage = (ImageViewTouch) dialog.findViewById(R.id.essp_dialog_viewimage_ivt_image);

        ivtImage.setImageBitmapReset(bmImage, 0, true);

        dialog.show();
    }

    public static void renameFile(String pathOld, String pathNew) {
        File from = new File(pathOld);
        File to = new File(pathNew);
        if (from.exists())
            from.renameTo(to);
    }

    public enum FOLDER_NAME {
        FOLDER_ANH_CONG_TO, FOLDER_ANH_TU, FOLDER_ANH_TI
    }

    public static String getStringChiSo(String etCS1Text, String etCS2Text, String etCS3Text, String etCS4Text, String etCS5Text, LOAI_CTO loaiCongTo) {
        StringBuilder CHI_SO = new StringBuilder();
        switch (loaiCongTo) {
            case D1:
            case DT:
                return CHI_SO.append("BT:").append(etCS1Text.isEmpty() ? "0" : etCS1Text).append(";").
                        append("CD:").append(etCS2Text.isEmpty() ? "0" : etCS2Text).append(";").
                        append("TD:").append(etCS3Text.isEmpty() ? "0" : etCS3Text).append(";").
                        append("SG:").append(etCS4Text.isEmpty() ? "0" : etCS4Text).append(";").
                        append("VC:").append(etCS5Text.isEmpty() ? "0" : etCS5Text).toString();

            case HC:
            case VC:
                return CHI_SO.append("KT:").append(etCS1Text.isEmpty() ? "0" : etCS1Text).append(";").
                        append("VC:").append(etCS2Text.isEmpty() ? "0" : etCS2Text).append(";").toString();

            default:
                return CHI_SO.toString();
        }
    }

    public static String getImageName(String TYPE_IMAGE, String DATETIME, String MA_DVIQLY, String MA_TRAM, int ID_BBAN_TRTH, String MA_CTO) {
        //Image name: {TYPE_IMAGE}_{DATETIME}_{MA_NVIEN}_{MA_TRAM}_{ID_BBAN_TRTH}_{MA_CTO}
        StringBuilder name = new StringBuilder()
                .append(TYPE_IMAGE).append("_")
                .append(DATETIME).append("_")
                .append(MA_DVIQLY).append("_")
                .append(MA_TRAM).append("_")
                .append(ID_BBAN_TRTH).append("_")
                .append(MA_CTO).append(".jpg");
        return name.toString();
    }


    public enum MESSAGE {
        ex01("Gặp vấn đề với việc lấy dữ liệu share pref đăng nhập của phiên trước!"),
        ex02("Không có dữ liệu trả về!"),
        ex03("Lỗi khởi tạo!"),
        ex04("Lỗi hiển thị!"),
        ex05("Gặp vấn đề đồng bộ!"),
        ex06("Gặp vấn đề kết nối, vui lòng thử lại!"),
        ex07("Gặp vấn đề khi ghi dữ liệu!"),
        ex08("Gặp vấn đề khi chụp ảnh!"),
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

    //Loai công tơ
    public enum LOAI_CTO {

        D1("D1", "Điện tử 5 biểu giá", new BO_CHISO[]{BO_CHISO.BT, BO_CHISO.CD, BO_CHISO.TD, BO_CHISO.SG, BO_CHISO.VC}),

        DT("DT", "Điện tử 2 biểu giá", new BO_CHISO[]{BO_CHISO.KT, BO_CHISO.VC}),

        VC("VC", "Hữu công", new BO_CHISO[]{BO_CHISO.KT, BO_CHISO.VC}),

        HC("HC", "Vô công", new BO_CHISO[]{BO_CHISO.KT, BO_CHISO.VC});

        public String code;
        public String content;
        public BO_CHISO[] bochiso;

        LOAI_CTO(String code, String content, BO_CHISO[] bochiso) {
            this.code = code;
            this.content = content;
            this.bochiso = bochiso;
        }

        public static LOAI_CTO findLOAI_CTO(String code) {
            for (LOAI_CTO loaiCto : values()) {
                if (loaiCto.code.equalsIgnoreCase(code))
                    return loaiCto;
            }
            return null;
        }
    }

    //bộ chỉ số
    public enum BO_CHISO {
        BT("BT"),
        CD("CD"),
        TD("TD"),
        SG("SG"),
        VC("VC"),
        KT("KT"),;

        public String code;

        BO_CHISO(String code) {
            this.code = code;
        }

        public static BO_CHISO findBO_CHISO(String code) {
            for (BO_CHISO boChiso : values()) {
                if (boChiso.code.equalsIgnoreCase(code))
                    return boChiso;
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
    public static Bitmap getBitmapFromUri(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(uri, options);
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
        type12("yyyyMMddHHmm"),
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

    public enum TYPE_IMAGE {
        IMAGE_CONG_TO("IMAGE_CONG_TO"),
        IMAGE_CONG_TO_NIEM_PHONG("IMAGE_CONG_TO_NIEM_PHONG"),
        IMAGE_TU("IMAGE_TU"),
        IMAGE_TI("IMAGE_TI"),
        IMAGE_MACH_NHI_THU_TU("IMAGE_MACH_NHI_THU_TU"),
        IMAGE_MACH_NHI_THU_TI("IMAGE_MACH_NHI_THU_TI"),
        IMAGE_NIEM_PHONG_TU("IMAGE_NIEM_PHONG_TU"),
        IMAGE_NIEM_PHONG_TI("IMAGE_NIEM_PHONG_TI");

        public String code;

        TYPE_IMAGE(String code) {
            this.code = code;
        }

        public static TYPE_IMAGE findTYPE_IMAGE(String code) {
            for (TYPE_IMAGE typeImage : values()) {
                if (typeImage.code.equalsIgnoreCase(code))
                    return typeImage;
            }
            return null;
        }

    }
    //endregion


}
