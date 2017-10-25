package com.es.tungnv.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.es.tungnv.entity.GcsConfigInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TUNGNV on 6/7/2016.
 */
public class GcsCommon {

    public static int pos_activity = 0;
    public static GcsConfigInfo cfgInfo;
    private static String IP_SERVER = "";
    private static String VERSION = "HN";

    private static String MA_DVIQLY = null;
    private static String MA_NVGCS = null;
    private static String MA_DQL = null;
    private static String IMEI = null;

    public static void setMaDviqly(String maDviqly) {
        MA_DVIQLY = maDviqly;
    }

    public static String getMaDviqly() {
        return MA_DVIQLY;
    }

    public static void setMaNvgcs(String maNvgcs) {
        MA_NVGCS = maNvgcs;
    }

    public static String getMaNvgcs() {
        return MA_NVGCS;
    }

    public static void setMaDql(String maDql) {
        MA_DQL = maDql;
    }

    public static String getMaDql() {
        return MA_DQL;
    }

    public static void setIMEI(String IMEI) {
        GcsCommon.IMEI = IMEI;
    }

    public static String getIMEI() {
        return IMEI;
    }

    public GcsConfigInfo getCfgInfo() {
        return cfgInfo;
    }

    public static String getIpServer() {
        return IP_SERVER;
    }

    public static void setIpServer(String ipServer) {
        IP_SERVER = ipServer;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static void setVERSION(String VERSION) {
        GcsCommon.VERSION = VERSION;
    }

    public void setCfgInfo(GcsConfigInfo cfgInfo) {
        this.cfgInfo = cfgInfo;
    }

    public static GcsConfigInfo GetFileConfig() {
        GcsConfigInfo cfgInfo = new GcsConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(Environment
                    .getExternalStorageDirectory()
                    + GcsConstantVariables.PROGRAM_PATH
                    + GcsConstantVariables.CFG_FILENAME));

            NodeList nodeList = doc.getElementsByTagName("Config");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) node;

                    NodeList nodelst11 = elm
                            .getElementsByTagName("WarningEnable3");
                    if (nodelst11 != null && nodelst11.item(0) != null) {
                        Element sub_elm = (Element) nodelst11.item(0);
                        cfgInfo.setWarningEnable3(Boolean.valueOf(sub_elm
                                .getTextContent()));
                    }

                    NodeList nodelst = elm
                            .getElementsByTagName("WarningEnable");
                    if (nodelst != null && nodelst.item(0) != null) {
                        Element sub_elm = (Element) nodelst.item(0);
                        cfgInfo.setWarningEnable(Boolean.valueOf(sub_elm
                                .getTextContent()));
                    }

                    NodeList nodelst8 = elm
                            .getElementsByTagName("WarningEnable2");
                    if (nodelst8 != null && nodelst8.item(0) != null) {
                        Element sub_elm8 = (Element) nodelst8.item(0);
                        cfgInfo.setWarningEnable2(Boolean.valueOf(sub_elm8
                                .getTextContent()));
                    }

                    NodeList nodelst1 = elm.getElementsByTagName("VuotMuc");
                    if (nodelst1 != null && nodelst1.item(0) != null) {
                        Element sub_elm1 = (Element) nodelst1.item(0);
                        cfgInfo.setVuotDinhMuc(Integer.valueOf(sub_elm1
                                .getTextContent()));
                    }

                    NodeList nodelst2 = elm.getElementsByTagName("DuoiDinhMuc");
                    if (nodelst2 != null && nodelst2.item(0) != null) {
                        Element sub_elm2 = (Element) nodelst2.item(0);
                        cfgInfo.setDuoiDinhMuc(Integer.valueOf(sub_elm2
                                .getTextContent()));
                    }
                    NodeList nodelst9 = elm.getElementsByTagName("VuotMuc2");
                    if (nodelst9 != null && nodelst9.item(0) != null) {
                        Element sub_elm9 = (Element) nodelst9.item(0);
                        cfgInfo.setVuotDinhMuc2(Integer.valueOf(sub_elm9
                                .getTextContent()));
                    }

                    NodeList nodelst10 = elm
                            .getElementsByTagName("DuoiDinhMuc2");
                    if (nodelst10 != null && nodelst10.item(0) != null) {
                        Element sub_elm10 = (Element) nodelst10.item(0);
                        cfgInfo.setDuoiDinhMuc2(Integer.valueOf(sub_elm10
                                .getTextContent()));
                    }

                    NodeList nodelst6 = elm.getElementsByTagName("IP");
                    if (nodelst6.item(0) != null) {
                        Element sub_elm6 = (Element) nodelst6.item(0);
                        cfgInfo.setIP_SERVICE(sub_elm6.getTextContent());
                    }

                    NodeList nodelst13 = elm.getElementsByTagName("DVIQLY");
                    if (nodelst13 != null && nodelst13.item(0) != null) {
                        Element sub_elm13 = (Element) nodelst13.item(0);
                        cfgInfo.setDVIQLY(sub_elm13.getTextContent());
                    }

                    NodeList nodelst14 = elm.getElementsByTagName("VERSION");
                    if (nodelst14 != null && nodelst14.item(0) != null) {
                        Element sub_elm14 = (Element) nodelst14.item(0);
                        cfgInfo.setDVIQLY(sub_elm14.getTextContent());
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

    public static boolean CreateFileConfig(GcsConfigInfo cfg, File cfgFile) {
        FileOutputStream fos;
        XmlSerializer serializer = Xml.newSerializer();
        String[] tagValue = new String[] {
                String.valueOf(cfg.isWarningEnable3()),
                String.valueOf(cfg.isWarningEnable()),
                String.valueOf(cfg.isWarningEnable2()),
                String.valueOf(cfg.getVuotDinhMuc()),
                String.valueOf(cfg.getDuoiDinhMuc()),
                String.valueOf(cfg.getVuotDinhMuc2()),
                String.valueOf(cfg.getDuoiDinhMuc2()),
                cfg.getIP_SERVICE(),
                cfg.getDVIQLY(),
                cfg.getVERSION()};
        try {
            cfgFile.createNewFile();
            fos = new FileOutputStream(cfgFile, false);

            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature(
                    "http://xmlpull.org/v1/doc/features.html#indent-output",
                    true);
            serializer.startTag(null, "Config");

            for (int i = 0; i < GcsConstantVariables.CFG_COLUMN.length; i++) {
                serializer.startTag(null, GcsConstantVariables.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, GcsConstantVariables.CFG_COLUMN[i]);
            }

            serializer.endTag(null, "Config");
            serializer.endDocument();

            serializer.flush();

            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void LoadFolder(ContextWrapper ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // Load thư mục data
            File file_db = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH);
            String[] allFilesDb = file_db.list();
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            // Load thư mục backup
            File file_backup = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_BACKUP_PATH);
            String[] allFilesBackup = file_backup.list();
            for (int i = 0; i < allFilesBackup.length; i++) {
                allFilesBackup[i] = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_BACKUP_PATH + allFilesBackup[i];
            }
            if (allFilesBackup != null)
                scanFile(ctx, allFilesBackup);

            // Load thư mục temp
            File file_temp = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_TEMP_PATH);
            String[] allFilesTemp = file_temp.list();
            for (int i = 0; i < allFilesTemp.length; i++) {
                allFilesTemp[i] = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_TEMP_PATH + allFilesTemp[i];
            }
            if (allFilesTemp != null)
                scanFile(ctx, allFilesTemp);

            // Load thư mục photo
//            File file_photo = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH);
//            String[] allFilesPhoto = file_photo.list();
//            for (int i = 0; i < allFilesPhoto.length; i++) {
//                allFilesPhoto[i] = Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PHOTO_PATH + allFilesPhoto[i];
//            }
//            if (allFilesPhoto != null)
//                scanFile(ctx, allFilesPhoto);

            String[] allFiles = new String [] {Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PATH + GcsConstantVariables.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_PATH)));
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

    public static boolean isSave(float CS_MOI, String TTR_MOI){
        if(CS_MOI != 0 || (TTR_MOI != null && !TTR_MOI.equals(""))){
            return true;
        }
        return false;
    }

    public static int getDaGhi(Cursor c){
        int sum = 0;
        if(c.moveToFirst()){
            do {
                try {
                    if ((c.getString(1) != null && c.getString(1).trim().length() > 0)
                            || (c.getString(0) != null && c.getString(0).trim().length() > 0
                            && (c.getString(0) != null && c.getFloat(0) > 0))) {
                        sum++;
                    }
                } catch(Exception ex) {

                }
            } while (c.moveToNext());
        }
        return sum;
    }

    public static String getPhotoName(String MA_QUYEN, String MA_CTO, int NAM,
                                      int THANG, int KY, String MA_DDO, String LOAI_BCS){
        return MA_QUYEN + "_" + MA_CTO + "_" + NAM + "_" + THANG + "_" + KY + "_" + MA_DDO + "_" + LOAI_BCS + ".jpg";
    }

    public static void saveImage(Context ctx, String fileName){
        BufferedOutputStream bos = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

            if(bitmap != null) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight();
                if(w < h){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Common.scaleDown(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), 600, true);
                } else {
                    bitmap = Common.scaleDown(bitmap, 600, true);
                }

                bos = new BufferedOutputStream(new FileOutputStream(fileName));
                bos.write(Common.encodeTobase64Byte(bitmap));
                bos.close();
                GcsCommon.scanFile(ctx, new String[]{fileName});
            }
        } catch (IOException ex) {
            Common.showAlertDialogGreen(ctx, "Lỗi", Color.RED, "Lỗi lưu ảnh\n" + ex.toString(), Color.WHITE, "OK", Color.RED);
        }
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static boolean CreateBackup(String filename_suffix){
        // Chuyển th�?i gian hiện tại thành string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        String currentDateandTime = sdf.format(new Date());

        //-> copy db vào folder backup
        File file_src = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH + "/ESGCS.s3db");
        if(!file_src.exists()){
            return true;
        }
        File str_file_dst = null;
        if(filename_suffix != null){
            filename_suffix = "_"+filename_suffix.trim();
        }else{
            filename_suffix = "";
        }

        str_file_dst = new File(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_BACKUP_PATH + "/ESGCS_"+currentDateandTime+filename_suffix+".s3db");

//		str_file_dst = new File(Environment.getExternalStorageDirectory() +common.BackupFolderPath + "/ESGCS_"+currentDateandTime+".s3db");

        return copy(file_src, str_file_dst);
    }

    public static boolean copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void scanZipPhotoFiles(ArrayList<String> arr_selected){
        try{
            File file_photo = new File(Environment.getExternalStorageDirectory() + "/ESGCS/Photo");
            String[] allFilesPhoto = file_photo.list();
            ArrayList<String> arr = new ArrayList<String>();

            for (int i = 0; i < allFilesPhoto.length; i++) {
                if(arr_selected.contains(allFilesPhoto[i].split("_")[0].toString())){
                    allFilesPhoto[i] = Environment.getExternalStorageDirectory() + "/ESGCS/Photo/" + allFilesPhoto[i];
                    arr.add(allFilesPhoto[i]);
                }
            }

            for (int i = 0; i < arr.size(); i++) {
                File f = new File(arr.get(i));
                String[] allFiles = f.list();
                for (int j = 0; j < allFiles.length; j++) {
                    allFiles[j] = arr.get(i) + "/" + allFiles[j];
                }
                zip(allFiles, arr.get(i) + ".zip");
            }

            arr.clear();
            String[] allFilesPhoto2 = file_photo.list();
            for (int i = 0; i < allFilesPhoto2.length; i++) {
                if(allFilesPhoto2[i].contains(".zip")){
                    allFilesPhoto2[i] = Environment.getExternalStorageDirectory() + "/ESGCS/Photo/" + allFilesPhoto2[i];
                    arr.add(allFilesPhoto2[i]);
                }
            }

            arr.add(Environment.getExternalStorageDirectory() + GcsConstantVariables.PROGRAM_DATA_PATH + "/ESGCS.s3db");
            allFilesPhoto2 = arr.toArray(new String[arr.size()]);
            zip(allFilesPhoto2, Environment.getExternalStorageDirectory() + "/ESGCS/ESGCS.zip");
        } catch(Exception ex) {
            ex.toString();
        }
    }

    public static void zip(String[] _files, String zipFileName) {
        try {
            final int BUFFER = 2048;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
