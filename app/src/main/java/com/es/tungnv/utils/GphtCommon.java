package com.es.tungnv.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.es.tungnv.entity.GphtConfigInfo;
import com.es.tungnv.entity.GphtConfigInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TUNGNV on 7/19/2016.
 */
public class GphtCommon {

    private static String VERSION = "HN";
    private static String IP_SERVER = "";
    private static String SERVER_NAME = "/Service1.asmx";
    public static GphtConfigInfo cfgInfo;

    public static String getIpServer() {
        return IP_SERVER;
    }

    public static void setIpServer(String ipServer) {
        IP_SERVER = ipServer;
    }

    public static void setVERSION(String VERSION) {
        GphtCommon.VERSION = VERSION;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static String getServerName() {
        return SERVER_NAME;
    }

    public static void setServerName(String serverName) {
        SERVER_NAME = serverName;
    }

    public static GphtConfigInfo GetFileConfig() {
        GphtConfigInfo cfgInfo = new GphtConfigInfo();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(Environment.getExternalStorageDirectory()
                    + GphtConstantVariables.PROGRAM_PATH + GphtConstantVariables.CFG_FILENAME));

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

    public static boolean CreateFileConfig(GphtConfigInfo cfg, File cfgFile, String serial) {
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

            for (int i = 0; i < GphtConstantVariables.CFG_COLUMN.length; i++) {
                serializer.startTag(null, GphtConstantVariables.CFG_COLUMN[i]);
                serializer.text(tagValue[i]);
                serializer.endTag(null, GphtConstantVariables.CFG_COLUMN[i]);
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

    public static void LoadFolder(ContextWrapper ctx) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // Load thư mục data
            File file_db = new File(Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH);
            String[] allFilesDb = file_db.list();
            for (int i = 0; i < allFilesDb.length; i++) {
                allFilesDb[i] = Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH + allFilesDb[i];
            }
            if (allFilesDb != null)
                scanFile(ctx, allFilesDb);

            String[] allFiles = new String [] {Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH + GphtConstantVariables.CFG_FILENAME};
            scanFile(ctx, allFiles);
        } else {
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                    .parse("file://" + Environment.getExternalStorageDirectory() + GphtConstantVariables.PROGRAM_PATH)));
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
