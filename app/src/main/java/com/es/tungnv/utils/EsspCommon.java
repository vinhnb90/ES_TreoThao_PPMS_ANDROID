package com.es.tungnv.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.es.tungnv.entity.EsspConfigInfo;
import com.es.tungnv.enums.EsspShapeType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TUNGNV on 3/30/2016.
 */
public class EsspCommon {

    public static boolean isPanAndZoom = false;
    public static EsspShapeType esspShapeType = EsspShapeType.BRUSH;
    public static int textSize = 18;
    public static int textSizeView = 18;
    public static float width = 1.5f;
    public static boolean isBuildPole = false;
    public static boolean isBuildWall = false;
    public static boolean isBuildBox = false;

    public static int pos_activity = 0;
    private static String IP_SERVER_1 = "";
    private static String VERSION = "HN";
    private static int ID_DVIQLY = 0;
    private static String MA_DVIQLY = "";
    private static String USERNAME = "";
    //private static String SERVER_NAME = "/service/ServiceCapDien.asmx";
    private static String SERVER_NAME = "/api/servicemtb/";
    public static EsspConfigInfo cfgInfo;
    public static int TINH_TRANG = 3;

    public static int state_draw = 3;
    public static float width_brush = 2f;
    public static int color = Color.BLACK;
    public static int color_background = Color.WHITE;
    public static String IMAGE_NAME = "";
    public static Bitmap bmMap = null;
    public static boolean check_save = false;
//    public static boolean check_pan = false;
    public static String text = "";
    public static String text2 = "";
    public static int angle = 0;
    public static String TYPE = "";
    public static String CAP_TRUOC = "";
    public static String CAP_SAU = "";
    public static int LOAI_BV = 0;
    public static String TEN_KH = "";
    public static String DIA_CHI = "";
    public static String TRAM = "";
    public static LinkedHashMap<String, String> map = null;

    public static void setMaDviqly(String maDviqly) {
        MA_DVIQLY = maDviqly;
    }

    public static String getMaDviqly() {
        return MA_DVIQLY;
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
        EsspCommon.VERSION = VERSION;
    }

    public static void setServerName(String serverName) {
        SERVER_NAME = serverName;
    }

    public static String getServerName() {
        return SERVER_NAME;
    }

    public static void setUSERNAME(String USERNAME) {
        EsspCommon.USERNAME = USERNAME;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static void setIdDviqly(int idDviqly) {
        ID_DVIQLY = idDviqly;
    }

    public static int getIdDviqly() {
        return ID_DVIQLY;
    }

    public EsspCommon() {

    }

    public static boolean CreateFileConfig(EsspConfigInfo cfg, File cfgFile, String serial) {
        FileOutputStream fos;
        XmlSerializer serializer = Xml.newSerializer();
        String[] tagValue = new String[] { cfg.getIP_SV_1(), cfg.getVERSION() };
        try {
            cfgFile.createNewFile();
            fos = new FileOutputStream(cfgFile, false);

            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "Table1");

            for (int i = 0; i < EsspConstantVariables.CFG_COLUMN.length; i++) {
                serializer.startTag(null, EsspConstantVariables.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, EsspConstantVariables.CFG_COLUMN[i]);
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

    public static EsspConfigInfo GetFileConfig() {
        EsspConfigInfo cfgInfo = new EsspConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(Environment.getExternalStorageDirectory()
                    + EsspConstantVariables.PROGRAM_PATH + EsspConstantVariables.CFG_FILENAME));

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
            // Load thư mục data
            File file_db = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH);
            String[] allFilesDb = file_db.list();
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_DATA_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            // Load thư mục photo của bảng D_ANH
            File file_anh = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH);
            String[] allFilesAnh = file_anh.list();
            for (int i = 0; i < allFilesAnh.length; i++) {
                allFilesAnh[i] = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_ANH_PATH + allFilesAnh[i];
            }
            if (allFilesAnh != null)
                scanFile(ctx, allFilesAnh);

            // Load thư mục photo của bảng D_BANVE
            File file_bv = new File(Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH);
            String[] allFilesBv = file_bv.list();
            for (int i = 0; i < allFilesBv.length; i++) {
                allFilesBv[i] = Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PHOTO_BV_PATH + allFilesBv[i];
            }
            if (allFilesBv != null)
                scanFile(ctx, allFilesBv);

            String[] allFiles = new String [] {Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH + EsspConstantVariables.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + EsspConstantVariables.PROGRAM_PATH)));
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

    public static boolean CheckDbExist() {
        String filePath = Environment.getExternalStorageDirectory()
                + "/ESSPV2/DATA/ESSP.s3db";
        File cfgFile = new File(filePath);
        if (!cfgFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String centerText(String text, float width, Paint paint) {
        try {
            Rect rectText = new Rect();
            paint.getTextBounds(text, 0, text.length(), rectText);

            String widthWord = "-";
            StringBuilder textBuider = new StringBuilder(" ");
            Rect rectSpace = new Rect();
            paint.getTextBounds(widthWord, 0, widthWord.length(), rectSpace);

            float spaceWidth = (width - rectText.width()) / 2;
            int spaceNumber = (int) (spaceWidth / rectSpace.width());
            for (int i = 0; i < spaceNumber; i++) {
                textBuider.append(" ");
            }
            return textBuider.append(text).toString();
        } catch(Exception ex) {
            return text;
        }
    }

}
