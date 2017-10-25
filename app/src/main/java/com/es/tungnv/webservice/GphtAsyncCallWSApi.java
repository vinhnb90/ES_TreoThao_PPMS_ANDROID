package com.es.tungnv.webservice;

import android.os.AsyncTask;

import com.es.tungnv.utils.GphtCommon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by TUNGNV on 7/19/2016.
 */
public class GphtAsyncCallWSApi {

    private String URL_SERVICE;

    public GphtAsyncCallWSApi() {
        this.URL_SERVICE = "http://" + GphtCommon.getIpServer() + GphtCommon.getServerName();
    }

    // ------------ GET DVIQLY ------------
    public ArrayList<JSONObject> WS_GET_DON_VI_CALL() {
        WS_GET_DON_VI ws_get_donvi = new WS_GET_DON_VI();
        ws_get_donvi.execute();
        try {
            return ws_get_donvi.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DON_VI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_DON_VI(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_dviqly";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONArray arr = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        listJSon.add(jsonObj);
                    }
                    return listJSon;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DM HE THONG ------------
    public ArrayList<JSONObject> WS_GET_DM_HE_THONG_CALL() {
        WS_GET_DM_HE_THONG ws_get_dm = new WS_GET_DM_HE_THONG();
        ws_get_dm.execute();
        try {
            return ws_get_dm.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DM_HE_THONG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_DM_HE_THONG(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_dm_hethong";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONArray arr = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        listJSon.add(jsonObj);
                    }
                    return listJSon;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DM USER ------------
    public ArrayList<JSONObject> WS_GET_DM_USER_CALL() {
        WS_GET_DM_USER ws_get_user = new WS_GET_DM_USER();
        ws_get_user.execute();
        try {
            return ws_get_user.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DM_USER extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_DM_USER(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_dm_user";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONArray arr = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        listJSon.add(jsonObj);
                    }
                    return listJSon;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DM NGUOI DUNG ------------
    public ArrayList<JSONObject> WS_GET_DM_NGUOI_DUNG_CALL() {
        WS_GET_DM_NGUOI_DUNG ws_get_dm_nguoi_dung = new WS_GET_DM_NGUOI_DUNG();
        ws_get_dm_nguoi_dung.execute();
        try {
            return ws_get_dm_nguoi_dung.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DM_NGUOI_DUNG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_DM_NGUOI_DUNG(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_dm_nguoiDung";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONArray arr = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        listJSon.add(jsonObj);
                    }
                    return listJSon;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET QUYEN ------------
    public ArrayList<JSONObject> WS_GET_QUYEN_CALL() {
        WS_GET_QUYEN ws_get_quyen = new WS_GET_QUYEN();
        ws_get_quyen.execute();
        try {
            return ws_get_quyen.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_QUYEN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_QUYEN(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_quyen";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONArray arr = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonObj = arr.getJSONObject(i);
                        listJSon.add(jsonObj);
                    }
                    return listJSon;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

}
