package com.es.tungnv.webservice;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EsspSentImage implements Runnable {
    URL connectURL;
    String ghichu;
    FileInputStream fileInputStream = null;

    public EsspSentImage(String urlString, String vDesc) {
        try {
            connectURL = new URL(urlString);
            ghichu = vDesc;
        } catch (Exception ex) {
        }
    }

    public String Send_Now(FileInputStream fStream, String iFileName, int HOSO_ID) {
        fileInputStream = fStream;
        return Sending(iFileName, HOSO_ID);
    }

    String Sending(String iFileName, int HOSO_ID) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        if(ghichu == null)
            ghichu = "";
        try {
            // Open a HTTP connection to the URL
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"HOSO_ID\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("" + HOSO_ID);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"GHI_CHU\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(ghichu);//==null?"":ghichu
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"HINH_ANH\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            InputStream is = conn.getInputStream();

            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            dos.close();
            return b.toString();
        } catch (MalformedURLException ex) {
            return ex.toString();
        } catch (IOException ioe) {
            return ioe.toString();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}