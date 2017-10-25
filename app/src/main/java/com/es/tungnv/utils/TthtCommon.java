package com.es.tungnv.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.Window;

import com.es.tungnv.entity.InfoPhieuTreoThao;
import com.es.tungnv.entity.TthtConfigInfo;
import com.es.tungnv.views.R;
import com.es.tungnv.zoomImage.ImageViewTouch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TUNGNV on 3/16/2016.
 */
public class TthtCommon {

    public static int pos_activity = 0;
    public static TthtConfigInfo cfgInfo;
    //padding right left 16dp when set witdth auto complex textview TRAM
    public static final int PADDING_WIDTH_AUTO_TEXT = 32;
    public static String IP_SERVER_1 = "";
    public static String VERSION = "HN";
    private static String MA_DVIQLY = "";
    private static String MA_NVIEN = "";
    private static String USERNAME = "";
    private static String TEN_NVIEN ="";
    private static String SERVER_NAME = "/api/serviceMTB/";
    public static boolean isDownload = false;
    private static String TTHT_DATE_CHON = "";
    private static String MA_TRAM_SELECTED = "";
    private static String TEN_TRAM_SELECTED = "";
    private static List<InfoPhieuTreoThao> listPhieu = new ArrayList<>();

    public final static int SIZE_HEIGHT_IMAGE = 600; //có thể thay đổi
    public final static int SIZE_WIDTH_IMAGE_BASIC = 500; //lưu ý không thay đổi được vì chiều ngang 600 mới ghi đủ thông tin cơ bản.
    public final static String[] ARRAY_DOI_SOAT = {"ĐỐI SOÁT", "BỎ ĐỐI SOÁT"};
    public static String[] arrTinhTrangNiemPhong = {"Đầy đủ, dây chì nguyên vẹn, thể hiện rõ mã hiệu ở hai mặt viên chì", "Khác..."};
    public static String[] arrSoVien = {"0", "1", "2", "3", "4", "5"};
    public static String[] arrLoaiHom = {"0", "2", "4", "8"};
    public static String[] arrFilter = {"Treo", "Tháo"};
    public static String[] arrMaBDong = {"B", "E"};
    public static final int CAMERA_REQUEST_CONGTO = 1111;
    public static final int CAMERA_REQUEST_TU = 2222;
    public static final int CAMERA_REQUEST_TI = 3333;
    public static final int CAMERA_REQUEST_NHI_THU_TU = 4444;
    public static final int CAMERA_REQUEST_NHI_THU_TI = 5555;
    public static final int CAMERA_REQUEST_NIEM_PHONG_TU = 6666;
    public static final int CAMERA_REQUEST_NIEM_PHONG_TI = 7777;

    public enum FILTER_DATA_FILL {
        DO_NOT_WRITE,
        WRITE_DO_NOT_SUBMIT,
        SUBMIT,
        ALL,
        TREO,
        THAO,
        PHIEU_TREO_THAO_HISTORY,
        PHIEU_TREO_THAO_TODAY;

        @Override
        public String toString() {
            if (this == TREO)
                return "B";
            if (this == THAO)
                return "E";
            return super.toString();
        }
    }

    public enum TYPE_DATENOW {
        ddMMyyyy, ddMMyyyy_Underscore, ddMMyyyy_Slash, ddMMyyyyHHmmss_Slash_Space_Colon, yyyyMMddTHHmmss_Slash_Colon;

        @Override
        public String toString() {
            if (this == ddMMyyyy) return "ddMMyyyy";
            else if (this == ddMMyyyy_Underscore) return "dd_MM_yyyy";
            else if (this == ddMMyyyy_Slash) return "dd/MM/yyyy";
            else if (this == ddMMyyyyHHmmss_Slash_Space_Colon) return "dd/MM/yyyy HH:mm:ss";
            else if (this == yyyyMMddTHHmmss_Slash_Colon)
                return "yyyy-MM-dd'T'HH:mm:ss";//2016-08-15T15:45:01
            return super.toString();
        }
    }

    public enum TYPE_IMAGE {
        IMAGE_CONG_TO, IMAGE_TU, IMAGE_TI, IMAGE_MACH_NHI_THU_TU, IMAGE_MACH_NHI_THU_TI, IMAGE_NIEM_PHONG_TU, IMAGE_NIEM_PHONG_TI;

        @Override
        public String toString() {
            if (this == IMAGE_CONG_TO) return "1";
            else if (this == IMAGE_TU) return "2";
            else if (this == IMAGE_TI) return "3";
            else if (this == IMAGE_MACH_NHI_THU_TU) return "4";
            else if (this == IMAGE_MACH_NHI_THU_TI) return "5";
            else if (this == IMAGE_NIEM_PHONG_TU) return "6";
            else if (this == IMAGE_NIEM_PHONG_TI) return "7";
            return super.toString();
        }
    }

    public enum FOLDER_NAME {
        FOLDER_ANH_CONG_TO, FOLDER_ANH_TU, FOLDER_ANH_TI
    }

    /*
            * TRANG_THAI_DU_LIEU == 0; chưa ghi dữ liệu
            * TRANG_THAI_DU_LIEU == 1; ghi dữ liệu nhưng chưa gửi
            * TRANG_THAI_DU_LIEU == 2; ghi dữ liệu và đã gửi
            * */
    public enum TRANG_THAI_DU_LIEU {
        BAN_DAU, DA_GHI, DA_GUI;

        @Override
        public String toString() {
            if (this == BAN_DAU) return "0";
            else if (this == DA_GHI) return "1";
            else if (this == DA_GUI) return "2";
            return super.toString();
        }
    }

    public static String getMaTramSelected() {
        return MA_TRAM_SELECTED;
    }

    public static List<InfoPhieuTreoThao> getListPhieu() {
        return listPhieu;
    }

    public static void setListPhieu(List<InfoPhieuTreoThao> listPhieu) {
        TthtCommon.listPhieu = listPhieu;
    }

    public enum LOAI_CONGTO {
        D1, DT, HC, VC;

        @Override
        public String toString() {
            if (this == D1) return "Điện tử 5 biểu giá";
            else if (this == DT) return "Điện tử 2 biểu giá";
            else if (this == HC) return "Hữu công";
            else if (this == VC) return "Vô công";
            return super.toString();
        }
    }

    public static void setMaTramSelected(String maTramSelected) {
        MA_TRAM_SELECTED = maTramSelected;
    }

    public static String getRecordDirectoryFolder(String folderName) {
        String path = Environment.getExternalStorageDirectory().getPath() + TthtConstantVariables.PROGRAM_PHOTOS_PATH + "/" + folderName;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    public static String getImageName(String TYPE_IMAGE, String DATETIME, String MA_DVIQLY, String MA_TRAM, int ID_BBAN_TRTH, String MA_CTO) {
        //Image name: {TYPE_IMAGE}_{DATETIME}_{MA_DVIQLY}_{MA_TRAM}_{ID_BBAN_TRTH}_{MA_CTO}
        StringBuilder name = new StringBuilder()
                .append(TYPE_IMAGE).append("_")
                .append(DATETIME).append("_")
                .append(MA_DVIQLY).append("_")
                .append(MA_TRAM).append("_")
                .append(ID_BBAN_TRTH).append("_")
                .append(MA_CTO).append(".jpg");
        return name.toString();
    }

    public static String getDateNow(String TYPE_DATENOW) {
        SimpleDateFormat sdf = new SimpleDateFormat(TYPE_DATENOW);
        Calendar cal = Calendar.getInstance();
        String dateNow = sdf.format(cal.getTime());
        return dateNow;
    }

    public static String getTenTramSelected() {
        return TEN_TRAM_SELECTED;
    }

    public static void setTenTramSelected(String tenTramSelected) {
        TEN_TRAM_SELECTED = tenTramSelected;
    }

    public static String getTenNvien() {
        return TEN_NVIEN;
    }

    public static void setTenNvien(String tenNvien) {
        TEN_NVIEN = tenNvien;
    }

    public static String getMaNvien() {
        return MA_NVIEN;
    }

    public static String getTthtDateChon() {
        return TTHT_DATE_CHON;
    }

    public static void setTthtDateChon(String tthtDateChon) {
        TTHT_DATE_CHON = tthtDateChon;
    }

    public static void setMaNvien(String maNvien) {
        MA_NVIEN = maNvien;
    }

    public static String getMaDviqly() {
        return MA_DVIQLY;
    }

    public static void setMaDviqly(String maDviqly) {
        MA_DVIQLY = maDviqly;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setUSERNAME(String USERNAME) {
        TthtCommon.USERNAME = USERNAME;
    }

    public static void setServerName(String serverName) {
        SERVER_NAME = serverName;
    }

    public static String getServerName() {
        return SERVER_NAME;
    }

    public static String getIP_SERVER_1() {
        return IP_SERVER_1;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static void setIP_SERVER_1(String iP_SERVER_1) {
        IP_SERVER_1 = iP_SERVER_1;
    }

    public static void setVERSION(String VERSION) {
        TthtCommon.VERSION = VERSION;
    }

    public TthtCommon() {

    }

    public static boolean CreateFileConfig(TthtConfigInfo cfg, File cfgFile, String serial) {
        FileOutputStream fos;
        XmlSerializer serializer = Xml.newSerializer();
        String[] tagValue = new String[]{cfg.getIP_SV_1(), cfg.getVERSION()};
        try {
            cfgFile.createNewFile();
            fos = new FileOutputStream(cfgFile, false);

            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "Table1");

            for (int i = 0; i < TthtConstantVariables.CFG_COLUMN.length; i++) {
                serializer.startTag(null, TthtConstantVariables.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, TthtConstantVariables.CFG_COLUMN[i]);
            }
            serializer.endTag(null, "Table1");
            serializer.endDocument();

            serializer.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static TthtConfigInfo GetFileConfig() {
        TthtConfigInfo cfgInfo = new TthtConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(Environment.getExternalStorageDirectory()
                    + TthtConstantVariables.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME));

            NodeList nodeList = doc.getElementsByTagName("Table1");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) node;
                    NodeList nodelst_ip1 = elm.getElementsByTagName("IP_SV_1");
                    if (nodelst_ip1 != null && nodelst_ip1.item(0) != null) {
                        Element sub_elm = (Element) nodelst_ip1.item(0);
                        cfgInfo.setIP_SV_1(sub_elm.getTextContent());
                    }

                    NodeList nodelst_ver = elm.getElementsByTagName("VERSION");
                    if (nodelst_ver != null && nodelst_ver.item(0) != null) {
                        Element sub_elm = (Element) nodelst_ver.item(0);
                        cfgInfo.setVERSION(sub_elm.getTextContent());
                    }
                }
            }
        } catch (SAXException e) {
            cfgInfo = null;
        } catch (IOException e) {
            cfgInfo = null;
        } catch (ParserConfigurationException e1) {
            cfgInfo = null;
        }
        return cfgInfo;
    }

    public static void LoadFolder(ContextWrapper ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File file_db = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH);

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
                allFilesDb[i] = Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            File file_db_backup = new File(Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_BACKUP_PATH);
            String[] allFilesDbBackup = file_db_backup.list();
            if (allFilesDbBackup.length > 0) {
                for (int i = 0; i < allFilesDbBackup.length; i++) {
                    allFilesDbBackup[i] = Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_DB_BACKUP_PATH + allFilesDbBackup[i];
                }
                if (allFilesDbBackup != null)
                    scanFile(ctx, allFilesDbBackup);
            }

            String[] allFiles = new String[]{Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH + TthtConstantVariables.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + TthtConstantVariables.PROGRAM_PATH)));
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

    public static String convertBitmapToByte64(String pathImage) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(pathImage);

        Bitmap immagex = imageBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String byteBimap = Base64.encodeToString(imageByte, Base64.NO_WRAP);
        return byteBimap;
    }

    //endregion
    public static boolean CheckDbExist() {
        String filePath = Environment.getExternalStorageDirectory()
                + "/TTHT/DB/TTHT.s3db";
        File cfgFile = new File(filePath);
        if (!cfgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }


    public static int compairDateString(String str1, String str2) {
        int result = 0;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


            Date date1 = formatter.parse(str1);

            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2) < 0) {
                //System.out.println("date2 is Greater than my date1");
                result = 1;
            } else if (date1.compareTo(date2) == 0) {
                result = 0;
            } else result = -1;

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    // convert date from type 2016-08-15T15:45:01 to type 24/08/2016 and type 14:45-24/08/2016
    public static String convertDateTime(String dateTime, int type) {
        if (dateTime.equals(""))
            return dateTime;
        else {
            String[] _dateTime = dateTime.split("T");
            String date = _dateTime[0];
            String time = _dateTime[1];
            String[] _date = date.split("-");
            String[] _time = time.split(":");
            // to type date
            if (type == 1) {
                return _date[2] + "/" + _date[1] + "/" + _date[0];
            }
            // to type date time
            else if (type == 2) {
                return _time[0] + ":" + _time[1] + "-" + _date[2] + "/" + _date[1]
                        + "/" + _date[0];
            }
            // to type month
            else if (type == 3) {
                return _date[1] + "/" + _date[0];
            } else return "";
        }

    }

    public static String reConvertDateTime(String dateTime, int type) {

        if (dateTime.equals(""))
            return dateTime;
            // convert from type date 24/08/2016 to 2016-08-15T00:00:00
        else if (type == 1) {
            String[] _reDate = dateTime.split("/");
            return _reDate[2] + "-" + _reDate[1] + "-" + _reDate[0] + "T00:00:00";
        }
        // convert from type date time 14:45-24/08/2016 to 2016-08-15T00:00:00
        else if (type == 2) {
            String[] _reDateTime = dateTime.split("-");
            String[] _reDate = _reDateTime[1].split("/");
            String[] _reTime = _reDateTime[0].split(":");
            String result = _reDate[2] + "-" + _reDate[1] + "-" + _reDate[0] + "T" + _reTime[0] + ":"
                    + _reTime[1] + ":00";
            return result;
        } else return "";
    }

    public static Bitmap getBitmapFromUri(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(uri, options);
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

//    public static Bitmap scaleDown(Bitmap realImage, float sizeHeight,
//                                   boolean filter) {
//        //TODO tạo một ảnh scale có chiều cao là sizeHeight
//        float ratio = sizeHeight / (float) realImage.getHeight();
//        int width = Math.round(ratio * (float) realImage.getWidth());
//        int height = Math.round(sizeHeight);
//        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
//                height, filter);
//        return newBitmap;
//    }

    public static Bitmap scaleDown(Bitmap realImage, float sizeHeightFixed,
                                   boolean filter) {
        int width = Math.round(sizeHeightFixed * (float) realImage.getWidth() / (float) realImage.getHeight());
        int height = Math.round(sizeHeightFixed);

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
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

    public static String getStringChiSo(String etCS1Text, String etCS2Text, String etCS3Text, String etCS4Text, String etCS5Text, String loaiCongTo) {
        StringBuilder CHI_SO = new StringBuilder();
        if (loaiCongTo.equalsIgnoreCase(TthtCommon.LOAI_CONGTO.D1.name())) {
            return CHI_SO.append("BT:").append(etCS1Text.isEmpty() ? "0" : etCS1Text).append(";").
                    append("CD:").append(etCS2Text.isEmpty() ? "0" : etCS2Text).append(";").
                    append("TD:").append(etCS3Text.isEmpty() ? "0" : etCS3Text).append(";").
                    append("SG:").append(etCS4Text.isEmpty() ? "0" : etCS4Text).append(";").
                    append("VC:").append(etCS5Text.isEmpty() ? "0" : etCS5Text).toString();
        }
        if (loaiCongTo.equalsIgnoreCase(TthtCommon.LOAI_CONGTO.HC.name()) || loaiCongTo.equalsIgnoreCase(TthtCommon.LOAI_CONGTO.VC.name()) || loaiCongTo.equalsIgnoreCase(TthtCommon.LOAI_CONGTO.DT.name())) {
            return CHI_SO.append("KT:").append(etCS1Text.isEmpty() ? "0" : etCS1Text).append(";").
                    append("VC:").append(etCS2Text.isEmpty() ? "0" : etCS2Text).append(";").toString();
        }
        return "";
    }

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
    public static Bitmap drawTextOnBitmapCongTo(Context context, String PATH_ANH, String VI_TRI_1, String VI_TRI_2_1, String VI_TRI_2_2, String VI_TRI_3, String VI_TRI_4_1, String VI_TRI_4_2) {
        try {
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
                                        + textHeight
                                        + paddingBetweenText
                                        + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText)
                                        + TthtCommon.SIZE_HEIGHT_IMAGE
                                        + paddingBetweenText
                                        + textHeight
                                        + paddingBetweenText / 2
                                , conf);
                    } else
                        bitmapResult = Bitmap.createBitmap(
                                TthtCommon.SIZE_WIDTH_IMAGE_BASIC,
                                paddingBetweenText / 2
                                        + (soDongCuaTextVI_TRI_1) * (textHeight + paddingBetweenText)
                                        + textHeight
                                        + paddingBetweenText
                                        + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText)
                                        + TthtCommon.SIZE_HEIGHT_IMAGE
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
                            + (soDongCuaTextVI_TRI_3) * (textHeight + paddingBetweenText); //vẽ từ vị trí trên TthtCommon.SIZE_HEIGHT_IMAGE

                    //TODO tạo khung chứa bitmap từ tọa độ x0, y0 tới .. ...
                    RectF frameBitmap = new RectF(x0, y0, x0 + bmRoot.getWidth(), y0 + TthtCommon.SIZE_HEIGHT_IMAGE);


                    //TODO vẽ full ảnh resultBimap với màu đen làm nền
                    canvas.drawRect(0, 0, bitmapResult.getWidth(), bitmapResult.getHeight(), paint_background);

                    //TODO nếu đã được ghi
                    if (bmRoot.getHeight() > TthtCommon.SIZE_HEIGHT_IMAGE) {
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

                    //TODO vẽ TEN_KH
                    TthtCommon.drawTextAndBreakLine(false, canvas, paint_text, 0, paddingBetweenText / 2 + textHeight, textHeight, bmRoot.getWidth(), VI_TRI_1, paddingBetweenText);

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
//                    String VI_TRI_2_2 = TthtCommon.getDateNow(TthtCommon.TYPE_DATENOW.ddMMyyyyHHmmss_Slash_Space_Colon.toString());
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
                    TthtCommon.drawTextAndBreakLine(false, canvas, paint_text, 0, y_TYPE_IMAGE + textHeight + paddingBetweenText, textHeight, bmRoot.getWidth(), VI_TRI_3, paddingBetweenText);

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
                        bos.write(Common.encodeTobase64Byte(bitmapResult));
                        bos.close();
                        TthtCommon.scanFile(context, new String[]{fileName});
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

        } catch (Exception ex) {
            Common.showAlertDialogGreen(context, "Lỗi", Color.RED, "Lỗi ghi lên ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
            return null;
        }
    }

    public static void zoomImage(Context context, Bitmap bmImage) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.essp_dialog_viewimage);
            dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            final ImageViewTouch ivtImage = (ImageViewTouch) dialog.findViewById(R.id.essp_dialog_viewimage_ivt_image);

            ivtImage.setImageBitmapReset(bmImage, 0, true);

            dialog.show();
        } catch (Exception ex) {
            Common.showAlertDialogGreen(context,
                    "Lỗi", Color.RED, "Lỗi hiển thị ảnh", Color.WHITE, "OK", Color.RED);
        }
    }

    public static String checkStringNull(String input) {
        return input == null ? "" : input;
    }
}
