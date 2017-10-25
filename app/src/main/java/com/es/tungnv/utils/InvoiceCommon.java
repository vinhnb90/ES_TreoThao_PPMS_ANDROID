package com.es.tungnv.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;

import com.es.tungnv.entity.InvoiceConfigInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TUNGNV on 2/24/2016.
 */
public class InvoiceCommon {

    public static int pos_activity = 0;
    public static InvoiceConfigInfo cfgInfo;
    public static String IP_SERVER_1 = "";
    public static String VERSION = "YB";
    public static String USERNAME = "";
    public static InvoicePrinter printer = null;

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
        InvoiceCommon.VERSION = VERSION;
    }

    public static void setUSERNAME(String USERNAME) {
        InvoiceCommon.USERNAME = USERNAME;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public InvoiceCommon() {
        if (printer == null) {
            printer = new InvoicePrinter("00:12:F3:19:49:CD");
        }
    }

    public static boolean CreateFileConfig(InvoiceConfigInfo cfg, File cfgFile, String serial) {
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

            for (int i = 0; i < InvoiceConstantVariables.CFG_COLUMN.length; i++) {
                serializer.startTag(null, InvoiceConstantVariables.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, InvoiceConstantVariables.CFG_COLUMN[i]);

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

    public static InvoiceConfigInfo GetFileConfig() {
        InvoiceConfigInfo cfgInfo = new InvoiceConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(Environment.getExternalStorageDirectory()
                    + InvoiceConstantVariables.PROGRAM_PATH + InvoiceConstantVariables.CFG_FILENAME));

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
            File file_db = new File(Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_DB_PATH);
            String[] allFilesDb = file_db.list();
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_DB_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            String[] allFiles = new String [] {Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH + InvoiceConstantVariables.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + InvoiceConstantVariables.PROGRAM_PATH)));
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

}
