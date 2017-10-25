package com.es.tungnv.webservice;

import android.os.AsyncTask;

import com.es.tungnv.utils.EsspCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by TUNGNV on 5/12/2016.
 */
public class EsspAsyncCallWSJson {

    //region Khởi tạo
    private static final EsspAsyncCallWSJson wsJson = new EsspAsyncCallWSJson();
    private static String URL_SERVICE;

    private EsspAsyncCallWSJson() {
    }

    public static EsspAsyncCallWSJson getInstance() {
        URL_SERVICE = "http://" + EsspCommon.getIP_SERVER_1() + EsspCommon.getServerName();
        return wsJson;
    }
    //endregion

    //region GET
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

    // ------------ LOGIN ------------
    public String WS_LOGIN_CALL(String MA_DVIQLY, String USERNAME, String PASSWORD, String IMEI) {
        WS_LOGIN ws_login = new WS_LOGIN(MA_DVIQLY, USERNAME, PASSWORD, IMEI);
        ws_login.execute();
        try {
            return ws_login.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_LOGIN extends AsyncTask<Void, Void, String>
    {

        String MA_DVIQLY;
        String USERNAME;
        String PASSWORD;
        String IMEI;
        public WS_LOGIN(String MA_DVIQLY, String USERNAME, String PASSWORD, String IMEI){
            this.MA_DVIQLY = MA_DVIQLY;
            this.USERNAME = USERNAME;
            this.PASSWORD = PASSWORD;
            this.IMEI = IMEI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                final String METHOD = "Get_LoginMTB";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY + "&username=" + USERNAME +
                        "&password=" + PASSWORD + "&imei=" + IMEI);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return "false";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET TRAM ------------
    public ArrayList<JSONObject> WS_GET_TRAM_CALL(String MA_DVIQLY) {
        WS_GET_TRAM ws_get_tram = new WS_GET_TRAM(MA_DVIQLY);
        ws_get_tram.execute();
        try {
            return ws_get_tram.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_TRAM extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MA_DVIQLY;

        public WS_GET_TRAM(String MA_DVIQLY){
            this.MA_DVIQLY = MA_DVIQLY;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_tram";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY);
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

    // ------------ GET VAT TU ------------
    public ArrayList<JSONObject> WS_GET_VTU_CALL(String MA_DVIQLY) {
        WS_GET_VTU ws_get_vtu = new WS_GET_VTU(MA_DVIQLY);
        ws_get_vtu.execute();
        try {
            return ws_get_vtu.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_VTU extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MA_DVIQLY;

        public WS_GET_VTU(String MA_DVIQLY){
            this.MA_DVIQLY = MA_DVIQLY;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_vtu";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY);
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

    // ------------ GET SO GCS ------------
    public ArrayList<JSONObject> WS_GET_SO_GCS_CALL(String MA_DVIQLY) {
        WS_GET_SO_GCS ws_get_so_gcs = new WS_GET_SO_GCS(MA_DVIQLY);
        ws_get_so_gcs.execute();
        try {
            return ws_get_so_gcs.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_SO_GCS extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MA_DVIQLY;

        public WS_GET_SO_GCS(String MA_DVIQLY){
            this.MA_DVIQLY = MA_DVIQLY;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_so_gcs";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY);
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

    // ------------ GET TRO NGAI ------------
    public ArrayList<JSONObject> WS_GET_TRO_NGAI_CALL() {
        WS_GET_TRO_NGAI ws_get_tro_ngai = new WS_GET_TRO_NGAI();
        ws_get_tro_ngai.execute();
        try {
            return ws_get_tro_ngai.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_TRO_NGAI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_TRO_NGAI(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_tngai_cdien";
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

    // ------------ GET NGAN HANG ------------
    public ArrayList<JSONObject> WS_GET_NGAN_HANG_CALL() {
        WS_GET_NGAN_HANG ws_get_ngan_hang = new WS_GET_NGAN_HANG();
        ws_get_ngan_hang.execute();
        try {
            return ws_get_ngan_hang.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_NGAN_HANG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_NGAN_HANG(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_d_ngan_hang";
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

    // ------------ GET HO SO ------------
    public ArrayList<JSONObject> WS_GET_HO_SO_CALL(String MA_DVIQLY, String USERNAME) {
        WS_GET_HO_SO ws_get_ho_so = new WS_GET_HO_SO(MA_DVIQLY, USERNAME);
        ws_get_ho_so.execute();
        try {
            return ws_get_ho_so.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_HO_SO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MA_DVIQLY;
        String USERNAME;

        public WS_GET_HO_SO(String MA_DVIQLY, String USERNAME){
            this.MA_DVIQLY = MA_DVIQLY;
            this.USERNAME = USERNAME;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_hoso";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY + "&user=" + USERNAME);
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

    // ------------ GET HO SO THI CONG ------------
    public ArrayList<JSONObject> WS_GET_HO_SO_THI_CONG_CALL(String MA_DVIQLY, String USERNAME) {
        WS_GET_HO_SO_THI_CONG ws_get_ho_so = new WS_GET_HO_SO_THI_CONG(MA_DVIQLY, USERNAME);
        ws_get_ho_so.execute();
        try {
            return ws_get_ho_so.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_HO_SO_THI_CONG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MA_DVIQLY;
        String USERNAME;

        public WS_GET_HO_SO_THI_CONG(String MA_DVIQLY, String USERNAME){
            this.MA_DVIQLY = MA_DVIQLY;
            this.USERNAME = USERNAME;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_hoso_xacnhan";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MA_DVIQLY + "&user=" + USERNAME);
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
    //endregion

    //region POST
    // ------------ Gửi hồ sơ -------------
    public ArrayList<JSONObject> WS_UPDATE_HO_SO_CALL(JSONArray jsonArr) {
        WS_UPDATE_HO_SO ws_update = new WS_UPDATE_HO_SO(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_HO_SO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        JSONArray jsonArr;
        String json;

        public WS_UPDATE_HO_SO(JSONArray jsonArr){
            this.jsonArr = jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Post_mtb_hoso";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    //wr.writeBytes(json);
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi phương án -------------
    public ArrayList<JSONObject> WS_UPDATE_PHUONG_AN_CALL(JSONArray jsonArr) {
        WS_UPDATE_PHUONG_AN ws_update = new WS_UPDATE_PHUONG_AN(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_PHUONG_AN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_PHUONG_AN(JSONArray jsonArr){
            this.jsonArr = jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Post_mtb_phuongan_capdien";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi dự toán -------------
    public ArrayList<JSONObject> WS_UPDATE_DU_TOAN_CALL(String METHOD, JSONArray jsonArr) {
        WS_UPDATE_DU_TOAN ws_update = new WS_UPDATE_DU_TOAN(METHOD, jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_DU_TOAN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;
        String METHOD;

        public WS_UPDATE_DU_TOAN(String METHOD, JSONArray jsonArr){
            this.jsonArr = jsonArr;
            this.METHOD = METHOD;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
//                final String METHOD = "Post_mtb_dutoan";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi công suất -------------
    public ArrayList<JSONObject> WS_UPDATE_CONG_SUAT_CALL(JSONArray jsonArr) {
        WS_UPDATE_CONG_SUAT ws_update = new WS_UPDATE_CONG_SUAT(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_CONG_SUAT extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_CONG_SUAT(JSONArray jsonArr){
            this.jsonArr = jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Post_mtb_bieudo_csuat";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi thiết bị -------------
    public ArrayList<JSONObject> WS_UPDATE_THIET_BI_CALL(JSONArray jsonArr) {
        WS_UPDATE_THIET_BI ws_update = new WS_UPDATE_THIET_BI(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_THIET_BI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_THIET_BI(JSONArray jsonArr){
            this.jsonArr = jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Post_mtb_hoso_thiet_bi";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi vật tư ngoài ht -------------
    public ArrayList<JSONObject> WS_UPDATE_VT_NHT_CALL(String METHOD, JSONArray jsonArr) {
        WS_UPDATE_VT_NHT ws_update = new WS_UPDATE_VT_NHT(METHOD, jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_VT_NHT extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;
        String METHOD;

        public WS_UPDATE_VT_NHT(String METHOD, JSONArray jsonArr)
        {
            this.jsonArr = jsonArr;
            this.METHOD = METHOD;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
//                final String METHOD = "Post_VTU_KHANG_TTUC";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Gửi tình trạng -------------
    public String WS_UPDATE_TINH_TRANG_CALL(int HOSO_ID, int TINH_TRANG) {
        WS_UPDATE_TINH_TRANG ws_update = new WS_UPDATE_TINH_TRANG(HOSO_ID, TINH_TRANG);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_TINH_TRANG extends AsyncTask<Void, Void, String>
    {

        private int HOSO_ID;
        private int TINH_TRANG;
        String json;

        public WS_UPDATE_TINH_TRANG(int HOSO_ID, int TINH_TRANG){
            this.HOSO_ID = HOSO_ID;
            this.TINH_TRANG = TINH_TRANG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                final String METHOD = "Post_mtb_hoso_tinhtrang";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = "[" + DATAtoJSONTinhTrang2(HOSO_ID, TINH_TRANG).toString() + "]";
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.writeBytes(json);
                    wr.flush();
                    wr.close();
                    //receive data from request
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    bufferedReader.close();

                    return String.valueOf(urlConnection.getResponseCode());
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    // ------------ Gửi tình trạng -------------
    public String WS_UPDATE_TINH_TRANG_THI_CONG_CALL(int HOSO_ID, int TINH_TRANG) {
        WS_UPDATE_TINH_TRANG_THI_CONG ws_update = new WS_UPDATE_TINH_TRANG_THI_CONG(HOSO_ID, TINH_TRANG);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_TINH_TRANG_THI_CONG extends AsyncTask<Void, Void, String>
    {

        private int HOSO_ID;
        private int TINH_TRANG;
        String json;

        public WS_UPDATE_TINH_TRANG_THI_CONG(int HOSO_ID, int TINH_TRANG){
            this.HOSO_ID = HOSO_ID;
            this.TINH_TRANG = TINH_TRANG;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                final String METHOD = "Post_Trang_Thai_Xuat_BBan";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = "[" + DATAtoJSONTinhTrang(HOSO_ID, TINH_TRANG).toString() + "]";
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.writeBytes(json);
                    wr.flush();
                    wr.close();
                    //receive data from request
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    bufferedReader.close();

                    return "" + urlConnection.getResponseCode();
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public JSONObject DATAtoJSONTinhTrang(int HOSO_ID, int TINH_TRANG) {
        //API corresponding keys
        String TripId_Key_HOSO_ID = "HoSoId";
        String TripId_Key_TINH_TRANG = "TRANG_THAI";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(TripId_Key_HOSO_ID,HOSO_ID);
            jsonObject.accumulate(TripId_Key_TINH_TRANG,TINH_TRANG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject DATAtoJSONTinhTrang2(int HOSO_ID, int TINH_TRANG) {
        //API corresponding keys
        String TripId_Key_HOSO_ID = "HOSO_ID";
        String TripId_Key_TINH_TRANG = "TINH_TRANG";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate(TripId_Key_HOSO_ID,HOSO_ID);
            jsonObject.accumulate(TripId_Key_TINH_TRANG,TINH_TRANG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // ------------ Gửi biên bản nghiệm thu -------------
    public ArrayList<JSONObject> WS_UPDATE_HO_SO_THI_CONG_CALL(JSONArray jsonArr) {
        WS_UPDATE_HO_SO_THI_CONG ws_update = new WS_UPDATE_HO_SO_THI_CONG(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_HO_SO_THI_CONG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_HO_SO_THI_CONG(JSONArray jsonArr){
            this.jsonArr = jsonArr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Post_ho_so_xacnhan";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonArr.toString();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    // Send post request
                    urlConnection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    //request parameters
                    wr.write(json.getBytes("UTF-8"));
                    wr.flush();
                    wr.close();
                    //receive data from request
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

    // ------------ Upload ảnh -----------------
//    public ArrayList<JSONObject> WS_UPDATE_ANH_CALL(JSONArray jsonArr) {
//        WS_UPDATE_ANH ws_update = new WS_UPDATE_ANH(jsonArr);
//        ws_update.execute();
//        try {
//            return ws_update.get();
//        } catch (InterruptedException e) {
//            return null;
//        } catch (ExecutionException e) {
//            return null;
//        }
//    }
//
//    public class WS_UPDATE_ANH extends AsyncTask<Void, Void, ArrayList<JSONObject>>
//    {
//        JSONArray jsonArr;
//        String json;
//
//        public WS_UPDATE_ANH(JSONArray jsonArr){
//            this.jsonArr = jsonArr;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList<JSONObject> doInBackground(Void... params) {
//            try {
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "*****";
//                String Tag="fSnd";
//                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
//                final String METHOD = "Post_mtb_hoso_thiet_bi";
//                URL url = new URL(URL_SERVICE + METHOD);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                try {
//                    json = jsonArr.toString();
//                    urlConnection.setRequestMethod("POST");
//                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
//                    urlConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
//                    // Send post request
//                    urlConnection.setDoInput(true);
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setUseCaches(false);
//                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//                    //request parameters
//
//                    wr.writeBytes(twoHyphens + boundary + lineEnd);
//                    wr.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
//                    wr.writeBytes(lineEnd);
//                    wr.writeBytes("sent image");
//                    wr.writeBytes(lineEnd);
//                    wr.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    wr.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
//                    wr.writeBytes(lineEnd);
//                    wr.writeBytes(Description);
//                    wr.writeBytes(lineEnd);
//                    wr.writeBytes(twoHyphens + boundary + lineEnd);
//
//                    wr.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName + "\"" + lineEnd);
//                    wr.writeBytes(lineEnd);
//
//                    int bytesAvailable = fileInputStream.available();
//
//                    int maxBufferSize = 1024;
//                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    byte[ ] buffer = new byte[bufferSize];
//
//                    // read file and write it into form...
//                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                    while (bytesRead > 0)
//                    {
//                        dos.write(buffer, 0, bufferSize);
//                        bytesAvailable = fileInputStream.available();
//                        bufferSize = Math.min(bytesAvailable,maxBufferSize);
//                        bytesRead = fileInputStream.read(buffer, 0,bufferSize);
//                    }
//                    dos.writeBytes(lineEnd);
//                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                    // close streams
//                    fileInputStream.close();
//
//                    dos.flush();
//
//                    Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));
//
//                    InputStream is = conn.getInputStream();
//
//                    // retrieve the response from server
//                    int ch;
//
//                    StringBuffer b =new StringBuffer();
//                    while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
//                    String s=b.toString();
//                    Log.i("Response",s);
//                    dos.close();
//                    return listJSon;
//                } finally {
//                    urlConnection.disconnect();
//                }
//            } catch(Exception ex) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<JSONObject> result) {
//            super.onPostExecute(result);
//        }
//    }
    //endregion

}
