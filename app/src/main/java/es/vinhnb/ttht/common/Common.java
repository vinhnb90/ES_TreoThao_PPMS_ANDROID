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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.es.tungnv.utils.TthtCommon;
import com.es.tungnv.views.R;
import com.es.tungnv.zoomImage.ImageViewTouch;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by VinhNB on 8/9/2017.
 */

public class Common {

    public static final long DELAY = 1000;
    public static final long DELAY_PROGESS_PBAR = 20;
    public static final long DELAY_ANIM = 250;
    public static final String PROGRAM_PHOTOS_PATH = "/ES_TTHT_HN/PHOTOS/";
    private static final int SIZE_HEIGHT_IMAGE = 600;
    private static final int SIZE_WIDTH_IMAGE_BASIC = 500;
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
        String path = Environment.getExternalStorageDirectory().getPath() + Common.PROGRAM_PHOTOS_PATH;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
            f.mkdirs();
        }

        File f1 = new File(path, folderName);
        if (!f1.exists()) {
            f1.mkdir();
            f1.mkdirs();
        }

        return f1.getPath();
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

    public static HashMap<String, String> spilitCHI_SO(LOAI_CTO loaiCto, String chi_so) {
        HashMap<String, String> result = new HashMap<>();
        String[] bochiso = chi_so.split(";");

        for (BO_CHISO boChiso : loaiCto.bochiso) {
            for (String chiso : bochiso) {
                if (chiso.contains(boChiso.code)) {
                    String[] values = chiso.split(":");
                    result.put(values[0], values[1]);
                }
            }
        }

        return result;
    }


    public enum FOLDER_NAME {
        FOLDER_ANH_CONG_TO, FOLDER_ANH_TU, FOLDER_ANH_TI
    }

    public static String getStringChiSo(String etCS1Text, String etCS2Text, String etCS3Text, String etCS4Text, String etCS5Text, LOAI_CTO loaiCongTo) {
        StringBuilder CHI_SO = new StringBuilder();
        String[] sEtCS = new String[]{etCS1Text == null ? "0" : etCS1Text, etCS2Text == null ? "0" : etCS2Text, etCS3Text == null ? "0" : etCS3Text, etCS4Text == null ? "0" : etCS4Text, etCS5Text == null ? "0" : etCS5Text};

        for (int i = 0; i < loaiCongTo.bochiso.length; i++) {
            CHI_SO.append(loaiCongTo.bochiso[i].code + ":").append(TextUtils.isEmpty(sEtCS[i]) ? "0" : sEtCS[i]).append(";");
        }

        CHI_SO.replace(CHI_SO.length() - ";".length(), CHI_SO.length(), "");
        return CHI_SO.toString();
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
        ex09("Gặp vấn đề khi xử lý dữ liệu để gửi!"),
        ex10("Gặp vấn đề trong quá trình gửi dữ liệu!"),
        ex0x("Xảy ra lỗi bất ngờ!");

        private String content;

        MESSAGE(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }


    public enum TYPE_SEARCH_BBAN {
        MA_TRAM("Mã trạm"), NGAY_TRTH("Ngày treo tháo"), TEN_KH("Tên KH"), MA_GCS("Mã GCS"), SO_BBAN("Số Biên bản");
        private String content;

        TYPE_SEARCH_BBAN(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static String[] getArray() {
            ArrayList<String> result = new ArrayList<>();

            for (TYPE_SEARCH_BBAN typeSearch : values()) {
                result.add(typeSearch.content);
            }

            return result.toArray(new String[result.size()]);
        }


        public static TYPE_SEARCH_BBAN findTYPE_SEARCH(String content) {
            for (TYPE_SEARCH_BBAN typeSearch : values()) {
                if (typeSearch.content.equalsIgnoreCase(content))
                    return typeSearch;
            }
            return null;
        }

        public static int getPosInArray(String content) {
            int i = 0;
            for (String s : TYPE_SEARCH_BBAN.getArray()) {
                if (s.equalsIgnoreCase(content))
                    break;
                i++;
            }
            return i;
        }
    }


    public enum TYPE_TRANG_THAI_MTB_ResultModel_NEW {
        GUI_CMIS_THATBAI("2"),
        DANG_CHO_CMIS_XACNHAN("3"),
        DA_TON_TAI_GUI_TRUOC_DO("31"),
        CMIS_XACNHAN_OK("4"),
        HET_HIEU_LUC("21"),
        LOI_BAT_NGO("");

        private String content;

        TYPE_TRANG_THAI_MTB_ResultModel_NEW(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }


        public static TYPE_TRANG_THAI_MTB_ResultModel_NEW find(String content) {
            if (TextUtils.isEmpty(content))
                return LOI_BAT_NGO;

            for (TYPE_TRANG_THAI_MTB_ResultModel_NEW typeSearchCto : values()) {
                if (typeSearchCto.content.equalsIgnoreCase(content))
                    return typeSearchCto;
            }
            return null;
        }

    }


    public enum TYPE_SEARCH_CTO {
        MA_TRAM("Mã trạm"), NGAY_TRTH("Ngày treo tháo"), TEN_KH("Tên KH"), MA_GCS("Mã GCS"), SO_BBAN("Số Biên bản"), MA_CTO("Mã công tơ"), SO_CTO("Số công tơ");
        private String content;

        TYPE_SEARCH_CTO(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static String[] getArray() {
            ArrayList<String> result = new ArrayList<>();

            for (TYPE_SEARCH_CTO typeSearch : values()) {
                result.add(typeSearch.content);
            }

            return result.toArray(new String[result.size()]);
        }


        public static TYPE_SEARCH_CTO findTYPE_SEARCH(String content) {
            for (TYPE_SEARCH_CTO typeSearchCto : values()) {
                if (typeSearchCto.content.equalsIgnoreCase(content))
                    return typeSearchCto;
            }
            return null;
        }

        public static int getPosInArray(String content) {
            int i = 0;
            for (String s : TYPE_SEARCH_CTO.getArray()) {
                if (s.equalsIgnoreCase(content))
                    break;
                i++;
            }
            return i;
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

    public enum TYPE_SEARCH_TRAM {
        MA_TRAM("Mã trạm"), DINH_DANH("Định danh"), MA_DVIQLY("Mã đơn vị"), TEN_TRAM("Tên trạm");
        private String content;

        TYPE_SEARCH_TRAM(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static String[] getArray() {
            ArrayList<String> result = new ArrayList<>();

            for (TYPE_SEARCH_TRAM typeSearch : values()) {
                result.add(typeSearch.content);
            }

            return result.toArray(new String[result.size()]);
        }


        public static TYPE_SEARCH_TRAM findTYPE_SEARCH(String content) {
            for (TYPE_SEARCH_TRAM typeSearchCto : values()) {
                if (typeSearchCto.content.equalsIgnoreCase(content))
                    return typeSearchCto;
            }
            return null;
        }

        public static int getPosInArray(String content) {
            int i = 0;
            for (String s : TYPE_SEARCH_TRAM.getArray()) {
                if (s.equalsIgnoreCase(content))
                    break;
                i++;
            }
            return i;
        }
    }

    public enum TYPE_SEARCH_HISTORY {
        TYPE_CALL_API("Kiểu lịch sử"),
        DATE_CALL_API("Ngày đồng bộ");
        private String content;

        TYPE_SEARCH_HISTORY(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static String[] getArray() {
            ArrayList<String> result = new ArrayList<>();

            for (TYPE_SEARCH_HISTORY typeSearch : values()) {
                result.add(typeSearch.content);
            }

            return result.toArray(new String[result.size()]);
        }


        public static TYPE_SEARCH_HISTORY findTYPE_SEARCH(String content) {
            for (TYPE_SEARCH_HISTORY typeSearchCto : values()) {
                if (typeSearchCto.content.equalsIgnoreCase(content))
                    return typeSearchCto;
            }
            return null;
        }

        public static int getPosInArray(String content) {
            int i = 0;
            for (String s : TYPE_SEARCH_HISTORY.getArray()) {
                if (s.equalsIgnoreCase(content))
                    break;
                i++;
            }
            return i;
        }
    }


    public enum TYPE_SEARCH_CLOAI {
        TEN_LOAI_CTO("Chủng loại"), VH_CONG("Vô - Hữu công"), MA_HANG("Mã hãng"), TEN_NUOC("Tên nước");
        private String content;

        TYPE_SEARCH_CLOAI(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static String[] getArray() {
            ArrayList<String> result = new ArrayList<>();

            for (TYPE_SEARCH_CLOAI typeSearch : values()) {
                result.add(typeSearch.content);
            }

            return result.toArray(new String[result.size()]);
        }


        public static TYPE_SEARCH_CLOAI findTYPE_SEARCH(String content) {
            for (TYPE_SEARCH_CLOAI typeSearchCto : values()) {
                if (typeSearchCto.content.equalsIgnoreCase(content))
                    return typeSearchCto;
            }
            return null;
        }

        public static int getPosInArray(String content) {
            int i = 0;
            for (String s : TYPE_SEARCH_CLOAI.getArray()) {
                if (s.equalsIgnoreCase(content))
                    break;
                i++;
            }
            return i;
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

        public static MA_BDONG findMA_BDONGByContent(String content) {
            for (MA_BDONG maBdong : values()) {
                if (maBdong.content.equalsIgnoreCase(content))
                    return maBdong;
            }
            return null;
        }

        public static MA_BDONG findMA_BDONGByCode(String code) {
            for (MA_BDONG maBdong : values()) {
                if (maBdong.code.equalsIgnoreCase(code))
                    return maBdong;
            }
            return null;
        }
    }

    //Loai công tơ
    public enum LOAI_CTO {

        D1("D1", "Điện tử 1 biểu giá", new BO_CHISO[]{BO_CHISO.KT, BO_CHISO.VC}),

        DT("DT", "Điện tử 5 biểu giá", new BO_CHISO[]{BO_CHISO.KT, BO_CHISO.VC, BO_CHISO.TD, BO_CHISO.SG, BO_CHISO.VC}),

        VC("VC", "Vô công", new BO_CHISO[]{BO_CHISO.VC}),

        HC("HC", "Hữu công", new BO_CHISO[]{BO_CHISO.KT});

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


    public enum TRANG_THAI_DOI_SOAT {
        CHUA_DOISOAT("CHƯA ĐỐI SOÁT"),
        DA_DOISOAT("ĐÃ ĐỐI SOÁT");

        public String content;

        TRANG_THAI_DOI_SOAT(String content) {
            this.content = content;
        }

        public static TRANG_THAI_DOI_SOAT findTRANG_THAI_DOI_SOAT(String content) {
            for (TRANG_THAI_DOI_SOAT trangThaiDoiSoat : values()) {
                if (trangThaiDoiSoat.content.equalsIgnoreCase(content))
                    return trangThaiDoiSoat;
            }
            return null;
        }
    }


    //trạng thái dữ liệu của web, chỉ sửa khi update mới từ web về
    public enum TRANG_THAI {
        CHUA_TON_TAI("Chưa tồn tại - 0", 0),
        DA_XUAT_RA_WEB("Đã xuất ra web - 1", 1),
        DA_XUAT_RA_MTB("Đã xuất ra máy tính bảng - 2", 2),
        DANG_CHO_XAC_NHAN_CMIS("Đã đẩy lên CMIS - 3", 3),
        XAC_NHAN_TREN_CMIS("Xác nhận trên CMIS - 4", 4),
        HET_HIEU_LUC("Hết hiệu lực - 21", 21);

        public String content;
        public int code;

        TRANG_THAI(String content, int code) {
            this.content = content;
            this.code = code;
        }

        public static TRANG_THAI findTRANG_THAI(int code) {
            for (TRANG_THAI trangThai : values()) {
                if (trangThai.code == code)
                    return trangThai;
            }
            return null;
        }

        public static TRANG_THAI findTRANG_THAI(String content) {
            for (TRANG_THAI trangThai : values()) {
                if (trangThai.content.equalsIgnoreCase(content))
                    return trangThai;
            }
            return null;
        }
    }


    //trạng thái dữ liệu của MTB
//    DA_DOISOAT("ĐÃ ĐỐI SOÁT", R.color.tththn_doisoat_dadoisoat),


    public enum TRANG_THAI_DU_LIEU {
        CHUA_TON_TAI("CHƯA TỒN TẠI", R.color.text_white),
        CHUA_GHI("CHƯA GHI", R.color.tththn_trangthai_dulieu_chuaghi),
        DA_GHI("ĐÃ GHI", R.color.tththn_trangthai_dulieu_da_ghi),
        GUI_THAT_BAI("GỬI THẤT BẠI", R.color.tththn_trangthai_dulieu_da_ghi),
        DANG_CHO_XAC_NHAN_CMIS("ĐANG CHỜ XÁC NHẬN CMIS", R.color.tththn_trangthai_dulieu_dangcho_xacnhan),
        DA_TON_TAI_GUI_TRUOC_DO("ĐÃ TỒN TẠI TRƯỚC ĐÓ", R.color.tththn_trangthai_dulieu_da_tontai_truocdo),
        DA_XAC_NHAN_TREN_CMIS("ĐÃ XÁC NHẬN TRÊN CMIS", R.color.tththn_trangthai_dulieu_da_xacnhan_cmis),
        HET_HIEU_LUC("HẾT HIỆU LỰC", R.color.tththn_trangthai_dulieu_het_hieuluc);

        public String content;
        public int color;

        TRANG_THAI_DU_LIEU(String content, int color) {
            this.content = content;
            this.color = color;
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
        //UI
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
        IMAGE_CONG_TO("IMAGE_CONG_TO", "ẢNH CÔNG TƠ"),
        IMAGE_CONG_TO_NIEM_PHONG("IMAGE_CONG_TO_NIEM_PHONG", "ẢNH NIÊM PHONG"),
        IMAGE_TU("IMAGE_TU", "ẢNH TU TREO"),
        IMAGE_TI("IMAGE_TI", "ẢNH TI TREO"),
        IMAGE_MACH_NHI_THU_TU("IMAGE_MACH_NHI_THU_TU", "ẢNH NHỊ THỨ TU TREO"),
        IMAGE_MACH_NHI_THU_TI("IMAGE_MACH_NHI_THU_TI", "ẢNH NHỊ THỨ TI TREO"),
        IMAGE_NIEM_PHONG_TU("IMAGE_NIEM_PHONG_TU", "ẢNH NIÊM PHONG TU TREO"),
        IMAGE_NIEM_PHONG_TI("IMAGE_NIEM_PHONG_TI", "ẢNH NIÊM PHONG TI TREO");

        public String code;
        public String nameImage;


        TYPE_IMAGE(String code, String nameImage) {
            this.code = code;
            this.nameImage = nameImage;
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

    /**
     * @param context
     * @param PATH_ANH
     * @param VI_TRI_1:   dòng đầu
     * @param VI_TRI_2_1  : dòng thứ 2 bên trái
     * @param VI_TRI_2_2  : dòng thứ 2 bên phải
     * @param VI_TRI_3    : dòng thứ 3 bên trái
     * @param VI_TRI_4_1: dòng thứ 4 dưới cùng bên trái
     * @param VI_TRI_4_2: dòng thứ 4 dưới cùng bên phải
     * @return
     */
    public static Bitmap drawTextOnBitmapCongTo(Context context, String PATH_ANH, String VI_TRI_1, String VI_TRI_2_1, String VI_TRI_2_2, String VI_TRI_3, String VI_TRI_4_1, String VI_TRI_4_2) throws Exception {
        String fileName = PATH_ANH;
        File fBitmap = new File(fileName);
        if (fBitmap.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmRoot = BitmapFactory.decodeFile(fileName, options);
            if (bmRoot != null) {
                Bitmap.Config bmConfig = bmRoot.getConfig();
                if (bmConfig == null) {
                    bmConfig = android.graphics.Bitmap.Config.ARGB_8888;
                }
                bmRoot = bmRoot.copy(bmConfig, true);

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
                int soDongCuaTextVI_TRI_1 = drawTextAndBreakLine(true, null, paint_text, 0, 0, textHeight, bmRoot.getWidth(), VI_TRI_1, paddingBetweenText);

                // TODO tính dòng sẽ được vẽ của chuỗi dài chưa xác định CHI_SO
                int soDongCuaTextVI_TRI_3 = drawTextAndBreakLine(true, null, paint_text, 0, 0, textHeight, bmRoot.getWidth(), VI_TRI_3, paddingBetweenText);

                //TODO tạo bitmap với diện tích như khung chứa để vẽ ảnh và thông tin
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bitmapResult = null;
                //TODO do fix cứng h của nội dung ảnh là SIZE_HEIGHT_IMAGE, nên khi resize thì w thay đổi,
                //TODO nếu < 1 giá trị nào đó thì không đủ bề ngang w để ghi thông tin, (SIZE_HEIGHT_IMAGE >600 thì ghi đủ), ta chèn thêm viền đen vào nếu không đủ ghi
                if (bmRoot.getWidth() > SIZE_WIDTH_IMAGE_BASIC) {
                    bitmapResult = Bitmap.createBitmap(
                            bmRoot.getWidth(),
                            paddingBetweenText / 2
                                    + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                                    + textHeight
                                    + paddingBetweenText
                                    + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText)
                                    + SIZE_HEIGHT_IMAGE
                                    + paddingBetweenText
                                    + textHeight
                                    + paddingBetweenText / 2
                            , conf);
                } else
                    bitmapResult = Bitmap.createBitmap(
                            SIZE_WIDTH_IMAGE_BASIC,
                            paddingBetweenText / 2
                                    + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                                    + textHeight
                                    + paddingBetweenText
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
                        + textHeight
                        + paddingBetweenText
                        + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText); //vẽ từ vị trí trên SIZE_HEIGHT_IMAGE

                //TODO tạo khung chứa bitmap từ tọa độ x0, y0 tới .. ...
                RectF frameBitmap = new RectF(x0, y0, x0 + bmRoot.getWidth(), y0 + SIZE_HEIGHT_IMAGE);


                //TODO vẽ full ảnh resultBimap với màu đen làm nền
                canvas.drawRect(0, 0, bitmapResult.getWidth(), bitmapResult.getHeight(), paint_background);

                //TODO nếu đã được ghi
                if (bmRoot.getHeight() > SIZE_HEIGHT_IMAGE) {
                    Rect src = new Rect(x0, bmRoot.getHeight() - textHeight - paddingBetweenText - Math.round(paddingBetweenText / 2) - SIZE_HEIGHT_IMAGE, bmRoot.getWidth(), bmRoot.getHeight() - textHeight - paddingBetweenText - Math.round(paddingBetweenText / 2));
                    Rect des = null;
                    //TODO nếu ảnh có w <= 600 thì vẽ ảnh ở giữa
                    if (bmRoot.getWidth() <= SIZE_WIDTH_IMAGE_BASIC) {
                        des = new Rect(Math.round((SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0, SIZE_WIDTH_IMAGE_BASIC - Math.round((SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0 + SIZE_HEIGHT_IMAGE);
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
                    if (bmRoot.getWidth() <= SIZE_WIDTH_IMAGE_BASIC) {
                        frameBitmapCenter = new RectF(Math.round((SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0, SIZE_WIDTH_IMAGE_BASIC - Math.round((SIZE_WIDTH_IMAGE_BASIC - bmRoot.getWidth()) / 2), y0 + SIZE_HEIGHT_IMAGE);
                    } else {
                        frameBitmapCenter = new RectF(x0, y0, bitmapResult.getWidth(), y0 + SIZE_HEIGHT_IMAGE);
                    }
                    //TODO vẽ full bitmap trên trên khung chứa bitmap với bút paint màu đen
                    canvas.drawBitmap(bmRoot, null, frameBitmapCenter, paint_background);
                }

                //TODO vẽ TEN_KH
                drawTextAndBreakLine(false, canvas, paint_text, 0, paddingBetweenText / 2 + textHeight, textHeight, bmRoot.getWidth(), VI_TRI_1, paddingBetweenText);

                //TODO vẽ TYPE IMAGE
                //Vẽ 1 khung chứa TYPE IMAGE
                Rect khungTYPE_IMAGE = new Rect();
                paint_text.getTextBounds(VI_TRI_2_1, 0, VI_TRI_2_1.length(), khungTYPE_IMAGE);
                int x_TYPE_IMAGE = 0;
                int y_TYPE_IMAGE = soDongCuaTextVI_TRI_1 * (textHeight + paddingBetweenText) + textHeight;
                canvas.drawRect(x_TYPE_IMAGE, y_TYPE_IMAGE - textHeight, VI_TRI_2_1.length(), y_TYPE_IMAGE + paddingBetweenText, paint_background);

                //Vẽ text TYPE IMAGE
                canvas.drawText(VI_TRI_2_1, x_TYPE_IMAGE, y_TYPE_IMAGE, paint_text);

                //TODO vẽ Ngày
//                    String VI_TRI_2_2 = getDateNow(TYPE_DATENOW.ddMMyyyyHHmmss_Slash_Space_Colon.toString());
                //Vẽ 1 khung chứa DATENOW
                Rect khungDATENOW = new Rect();
                paint_text.getTextBounds(VI_TRI_2_2, 0, VI_TRI_2_2.length(), khungDATENOW);
                int textWidthDATENOW = Math.round(paint_text.measureText(VI_TRI_2_2));
                int x_DATENOW = bitmapResult.getWidth() - textWidthDATENOW;
                int y_DATENOW = y_TYPE_IMAGE;
                canvas.drawRect(x_DATENOW, y_DATENOW - textHeight, bitmapResult.getWidth(), y_DATENOW + paddingBetweenText, paint_background);

                //Vẽ text DATENOW
                canvas.drawText(VI_TRI_2_2, x_DATENOW, y_DATENOW, paint_text);

                //TODO vẽ CHI_SO
                drawTextAndBreakLine(false, canvas, paint_text, 0, y_TYPE_IMAGE + textHeight + paddingBetweenText, textHeight, bmRoot.getWidth(), VI_TRI_3, paddingBetweenText);

                 /*   //Vẽ 1 khung chứa CHI_SO
                    Rect khungCHI_SO = new Rect();
                    paint_text.getTextBounds(VI_TRI_3, 0, (VI_TRI_3).length(), khungCHI_SO);
                    int x_CHI_SO = 0;
                    int y_CHI_SO = y_TYPE_IMAGE + textHeight + paddingBetweenText;
                    canvas.drawRect(x_CHI_SO, y_CHI_SO - textHeight, bitmapResult.getWidth(), y_CHI_SO + paddingBetweenText, paint_background);
                    //Vẽ text CHI_SO
                    canvas.drawText(VI_TRI_3, x_CHI_SO, y_CHI_SO, paint_text);*/

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

                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(fileName));
                    bos.write(com.es.tungnv.utils.Common.encodeTobase64Byte(bitmapResult));
                    bos.close();
                    scanFile(context, new String[]{fileName});
                } catch (IOException ex) {
                    throw new Exception(ex.getMessage());
                }
                return bitmapResult;
            } else {
                throw new Exception("Lỗi khi xử lý ảnh!");
            }
        } else {
            throw new Exception("Không có ảnh trong máy!");
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

    /**
     * @param isCountLine: true: chỉ muốn lấy số dòng sẽ được vẽ
     *                     false: vẽ luôn
     * @param canvas:      null: nếu isCountLine == true
     *                     canvas: else
     * @param paint
     * @param x
     * @param textHeight
     * @param maxWidth
     * @param text
     */
    public static int drawTextAndBreakLine(final boolean isCountLine, final Canvas canvas, final Paint paint,
                                           float x, float y, float textHeight, float maxWidth, final String text, int paddingBetweenText) {
        String textToDisplay = text;
        String tempText = "";
        int countLine = 0;
        char[] chars;
        float lastY = y;
        int nextPos = 0;
        int lengthBeforeBreak = textToDisplay.length();
        do {
            countLine++;
            lengthBeforeBreak = textToDisplay.length();
            chars = textToDisplay.toCharArray();
            nextPos = paint.breakText(chars, 0, chars.length, maxWidth, null);
            tempText = textToDisplay.substring(0, nextPos);
            textToDisplay = textToDisplay.substring(nextPos, textToDisplay.length());
            if (!isCountLine) {
                canvas.drawText(tempText, x, lastY, paint);
            }
            lastY += textHeight + paddingBetweenText;
        } while (nextPos < lengthBeforeBreak);

        if (isCountLine)
            return countLine;
        else return -1;
    }

    public static void scaleImage(String fileName, Context context) throws Exception {
        File file = new File(fileName);

        BufferedOutputStream bos = null;
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
            bos.write(com.es.tungnv.utils.Common.encodeTobase64Byte(bitmap));
            bos.close();
            TthtCommon.scanFile(context, new String[]{fileName});
        }
    }

    public static void runAnimationClickView(final View view, int idAnimation, long timeDelayAnim) {
        if (view == null)
            return;
        if (idAnimation <= 0)
            return;

        Animation animation = AnimationUtils.loadAnimation(view.getContext(), idAnimation);
        if (timeDelayAnim > 0)
            animation.setDuration(timeDelayAnim);

        view.startAnimation(animation);
    }


    public static String removeAccent(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static char removeAccent(char ch) {

        int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
        if (index >= 0) {
            ch = REPLACEMENTS[index];
        }
        return ch;
    }

    //region search tiếng việt
    private static char[] SPECIAL_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò',
            'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê',
            'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ',
            'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ',
            'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ',
            'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế',
            'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị',
            'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ',
            'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ',
            'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự',};

    private static char[] REPLACEMENTS = {'A', 'A', 'A', 'A', 'E', 'E', 'E',
            'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a',
            'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A',
            'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I',
            'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u',};

    public static <T> List<T> cloneList(List<T> listcloneList) {
        ArrayList<T> newArrayList = (ArrayList<T>) ((ArrayList<T>) listcloneList).clone();

        List<T> cloneListResult = new ArrayList<>();
        cloneListResult.addAll(newArrayList);

        return newArrayList;
    }


    public static String convertBitmapToByte64(String pathImage) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(pathImage);

        Bitmap immagex = imageBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String byteBimap = Base64.encodeToString(imageByte, Base64.NO_WRAP);
        return byteBimap;
    }
}
