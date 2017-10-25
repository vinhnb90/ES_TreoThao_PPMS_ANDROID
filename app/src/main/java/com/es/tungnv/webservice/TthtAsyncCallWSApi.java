package com.es.tungnv.webservice;

import android.os.AsyncTask;

import com.es.tungnv.utils.TthtCommon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by TUNGNV on 8/22/2016.
 */
public class TthtAsyncCallWSApi {

    //region Khởi tạo
    private static final TthtAsyncCallWSApi wsJson = new TthtAsyncCallWSApi();

    private static String URL_SERVICE;

    private TthtAsyncCallWSApi() {
    }

    public static TthtAsyncCallWSApi getInstance() {
        URL_SERVICE = "http://" + TthtCommon.getIP_SERVER_1() + TthtCommon.getServerName();
//        URL_SERVICE = "http://192.168.68.103:8888" + TthtCommon.getServerName();

        return wsJson;
    }
    //endregion

    //region GET

    public JSONObject WS_REQUEST_LOG_CALL(JSONObject jsonObject) {
        WS_REQUEST_LOG ws_request_log = new WS_REQUEST_LOG(jsonObject);
        ws_request_log.execute();
        try {
            return ws_request_log.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_REQUEST_LOG extends AsyncTask<Void, Void, JSONObject>
    {
        JSONObject jsonObject;
        String json;

        public WS_REQUEST_LOG(JSONObject aJsonObject){
            this.jsonObject = aJsonObject;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                final String METHOD = "LogHistory";
                URL url = new URL(URL_SERVICE + METHOD);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    json = jsonObject.toString();
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
                    JSONObject jsonObjectResult= new JSONObject(stringBuilder.toString());
                    return jsonObjectResult;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
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

    // ------------ GET BIEN BAN ------------
    public ArrayList<JSONObject> WS_GET_BIEN_BAN_CALL(String MaDonVi, String MaNhanVien) {
        WS_GET_BIEN_BAN ws_get_bien_ban = new WS_GET_BIEN_BAN(MaDonVi, MaNhanVien);
        ws_get_bien_ban.execute();
        try {
            return ws_get_bien_ban.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_BIEN_BAN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MaDonVi;
        String MaNhanVien;

        public WS_GET_BIEN_BAN(String MaDonVi, String MaNhanVien){
            this.MaDonVi = MaDonVi;
            this.MaNhanVien = MaNhanVien;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
//                final String METHOD = "GetBienBanTreoThao";
                final String METHOD = "GeT_BBAN";
//                URL url = new URL(URL_SERVICE + METHOD + "/" + MaDonVi + "/" + MaNhanVien);
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MaDonVi + "&manhanvien=" + MaNhanVien);
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

    // ------------ GET CONG TO ------------
    public ArrayList<JSONObject> WS_GET_CONG_TO_CALL(String MaDonVi, String IdBienBan) {
        WS_GET_CONG_TO ws_get_cong_to = new WS_GET_CONG_TO(MaDonVi, IdBienBan);
        ws_get_cong_to.execute();
        try {
            return ws_get_cong_to.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_CONG_TO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MaDonVi;
        String IdBienBan;

        public WS_GET_CONG_TO(String MaDonVi, String IdBienBan){
            this.MaDonVi = MaDonVi;
            this.IdBienBan = IdBienBan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_cto";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + MaDonVi + "&idBienBan=" + IdBienBan);
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

    // ------------ GET TU ------------
    public ArrayList<JSONObject> WS_GET_TU_CALL(String MaDonVi, String IdBienBan) {
        WS_GET_TU ws_get_tu = new WS_GET_TU(MaDonVi, IdBienBan);
        ws_get_tu.execute();
        try {
            return ws_get_tu.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_TU extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MaDonVi;
        String IdBienBan;

        public WS_GET_TU(String MaDonVi, String IdBienBan){
            this.MaDonVi = MaDonVi;
            this.IdBienBan = IdBienBan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_tu";
                URL url = new URL(URL_SERVICE + METHOD + "?MaDonVi=" + MaDonVi + "&IdBienBan" + IdBienBan);
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

    // ------------ GET TI ------------
    public ArrayList<JSONObject> WS_GET_TI_CALL(String MaDonVi, String IdBienBan) {
        WS_GET_TI ws_get_ti = new WS_GET_TI(MaDonVi, IdBienBan);
        ws_get_ti.execute();
        try {
            return ws_get_ti.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_TI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String MaDonVi;
        String IdBienBan;

        public WS_GET_TI(String MaDonVi, String IdBienBan){
            this.MaDonVi = MaDonVi;
            this.IdBienBan = IdBienBan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_tu";
                URL url = new URL(URL_SERVICE + METHOD + "?MaDonVi=" + MaDonVi + "&IdBienBan" + IdBienBan);
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


    // -----------  GET TRAM-----------
    public ArrayList<JSONObject> WS_GET_TRAM_CALL() {
        WS_GET_TRAM ws_get_tram = new WS_GET_TRAM();
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
        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "GetTram";
                URL url = new URL(URL_SERVICE + METHOD + "?maDonViQuanLy=" + TthtCommon.getMaDviqly());
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

    // -----------  GET BBAN TU TI-----------
    public ArrayList<JSONObject> WS_GET_BBan_TU_TI_CALL() {
        WS_GET_BB_TU_TI ws_get_bb_tu_ti = new WS_GET_BB_TU_TI();
        ws_get_bb_tu_ti.execute();
        try {
            return ws_get_bb_tu_ti.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_BB_TU_TI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_bban_TUTI";
//               http://localhost:3966/api/serviceMTB/Get_bban_TUTI?madonvi=PD0100&manhanvien=TT_04
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + TthtCommon.getMaDviqly() + "&" + "manhanvien=" + TthtCommon.getMaNvien());
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


    // -----------  GET TU TI-----------
    public ArrayList<JSONObject> WS_GET_CHITIET_TU_TI_CALL(int id_chitiet_tu_ti) {
        WS_GET_TU_TI ws_get_tu_ti = new WS_GET_TU_TI(id_chitiet_tu_ti);
        ws_get_tu_ti.execute();
        try {
            return ws_get_tu_ti.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_TU_TI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        private int ID_CHITIET_TU_TI;

        public WS_GET_TU_TI(int id_chitiet_tu_ti) {
            ID_CHITIET_TU_TI = id_chitiet_tu_ti;
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "Get_TUTI";
//               http://localhost:3966/api/serviceMTB/Get_TUTI?madonvi=PD0100&idTuTi=1
//                http://localhost:3966/api/serviceMTB/Get_TUTI?madonvi=PD0100
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + TthtCommon.getMaDviqly() + "&" + "idTuTi=" + ID_CHITIET_TU_TI);
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

    // ------------ UPDATE DATA SERVER ------------
    public ArrayList<JSONObject> WS_GET_ALL_DATA_LOAI_CONGTO() {
        WS_GET_DATA_LOAI_CONGTO ws_get_loai_cong_to = new WS_GET_DATA_LOAI_CONGTO();
        ws_get_loai_cong_to.execute();
        try {
            return ws_get_loai_cong_to.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DATA_LOAI_CONGTO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "LayDuLieuLoaiCongTo";
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
                } finally {
                    urlConnection.disconnect();
                }
                return listJSon;
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }


    public ArrayList<JSONObject> WS_GET_DATA_CMIS_CALL(String madonvi, String manhanvien) {
        WS_GET_DATA_CMIS ws_get_ti = new WS_GET_DATA_CMIS(madonvi, manhanvien);
        ws_get_ti.execute();
        try {
            return ws_get_ti.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class WS_GET_DATA_CMIS extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        String madonvi;
        String manhanvien;

        public WS_GET_DATA_CMIS(String madonvi, String manhanvien){
            this.madonvi = madonvi;
            this.manhanvien = manhanvien;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
                ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
                final String METHOD = "LayDuLieuCMIS";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + madonvi + "&manhanvien=" + manhanvien);
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
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }
    //endregion

    // region POST
    // ------------ Gửi all công tơ -------------
    public ArrayList<JSONObject> WS_UPDATE_ALL_CONG_TO_CALL(JSONArray jsonArr) {

        WS_UPDATE_ONLY_CONG_TO ws_update = new WS_UPDATE_ONLY_CONG_TO(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }


    }

//    public ArrayList<String> WS_UPDATE_ALL_CONG_TO_CALL(List<JSONArray> jsonArrayList) {
//        ArrayList<String> resultArrayList = new ArrayList<>();
//        try
//        {
//        for(int i = 0; i<jsonArrayList.size(); i++) {
//            WS_UPDATE_ONLY_CONG_TO ws_update = new WS_UPDATE_ONLY_CONG_TO(jsonArrayList.get(i));
//            ws_update.execute();
//            ArrayList<JSONObject> resultJsonArray = new ArrayList<>();
//
//            resultJsonArray = ws_update.get();
//            if (resultJsonArray.get(0).getString("RESULT").equals("OK"))
//                resultArrayList.add(resultJsonArray.get(0).getS("RESULT"));
//        }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//            e.printStackTrace();
//        }

    //khi chắc chắn dữ liệu trả về OK thì tiếp tục post dữ liệu ảnh với TthtSentImage

//        }
//        return  resultArrayList;
//    }




    //    public class WS_UPDATE_ONLY_CONG_TO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    public class WS_UPDATE_ONLY_CONG_TO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_ONLY_CONG_TO(JSONArray jsonArr){
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
                final String METHOD = "PostMTBWithImage";
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

    /*public class WS_UPDATE_ONLY_CONG_TO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {
        JSONArray jsonArr;
        String json;

        public WS_UPDATE_ONLY_CONG_TO(JSONArray jsonArr){
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
                final String METHOD = "PostMTB";
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
    }*/

    // ------------ Gửi biên bản -------------
    public ArrayList<JSONObject> WS_UPDATE_BIEN_BAN_CALL(JSONArray jsonArr) {
        WS_UPDATE_BIEN_BAN ws_update = new WS_UPDATE_BIEN_BAN(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_BIEN_BAN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        JSONArray jsonArr;
        String json;

        public WS_UPDATE_BIEN_BAN(JSONArray jsonArr){
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
                final String METHOD = "MTB_PostBBanModel";
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

    // ------------ Gửi công tơ -------------
    public ArrayList<JSONObject> WS_UPDATE_CONG_TO_CALL(JSONArray jsonArr) {
        WS_UPDATE_CONG_TO ws_update = new WS_UPDATE_CONG_TO(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_CONG_TO extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        JSONArray jsonArr;
        String json;

        public WS_UPDATE_CONG_TO(JSONArray jsonArr){
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
                final String METHOD = "MTB_PostCtoModel";
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

    // ------------ Gửi tu -------------
    public ArrayList<JSONObject> WS_UPDATE_TU_CALL(JSONArray jsonArr) {
        WS_UPDATE_TU ws_update = new WS_UPDATE_TU(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_TU extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        JSONArray jsonArr;
        String json;

        public WS_UPDATE_TU(JSONArray jsonArr){
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
                final String METHOD = "Post_mtb_tu";
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

    // ------------ Gửi ti -------------
    public ArrayList<JSONObject> WS_UPDATE_TI_CALL(JSONArray jsonArr) {
        WS_UPDATE_TI ws_update = new WS_UPDATE_TI(jsonArr);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_UPDATE_TI extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        JSONArray jsonArr;
        String json;

        public WS_UPDATE_TI(JSONArray jsonArr){
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
                final String METHOD = "Post_mtb_ti";
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

    //endregion

    //region CHECK
    // ------------ LOGIN ------------
    public JSONObject WS_LOGIN_CALL(String madonvi, String username, String password, String imei) {
        WS_LOGIN ws_get_ti = new WS_LOGIN(madonvi, username, password, imei);
        ws_get_ti.execute();
        try {
            return ws_get_ti.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_LOGIN extends AsyncTask<Void, Void, JSONObject>
    {
        String madonvi;
        String username;
        String password;
        String imei;

        public WS_LOGIN(String madonvi, String username, String password, String imei){
            this.madonvi = madonvi;
            this.username = username;
            this.password = password;
            this.imei = imei;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                final String METHOD = "Get_LoginMTB";
                URL url = new URL(URL_SERVICE + METHOD + "?madonvi=" + madonvi + "&username=" + username
                        + "&password=" + password + "&imei=" + imei);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    JSONObject arr = new JSONObject(stringBuilder.toString());
                    return arr;
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
        }
    }
    //endregion

}
