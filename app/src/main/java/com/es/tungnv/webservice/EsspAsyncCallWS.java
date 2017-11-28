package com.es.tungnv.webservice;

import android.content.Context;
import android.os.AsyncTask;

import com.es.tungnv.entity.EsspEntityDviQly;
import com.es.tungnv.entity.EsspEntityTram;
import com.es.tungnv.entity.EsspEntityVatTu;
import com.es.tungnv.utils.EsspCommon;
import com.es.tungnv.utils.EsspConstantVariables;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by TUNGNV on 3/31/2016.
 */
public class EsspAsyncCallWS {

    private EsspCommon comm = new EsspCommon();
    private String URL_SERVICE;

    public EsspAsyncCallWS() {
        this.URL_SERVICE = "http://" + EsspCommon.getIP_SERVER_1() + EsspCommon.getServerName();
    }
    // ------------ LOGIN -----------------
    public String WS_USER_LOGIN_CALL(String username, String pass, int id,String imei) {
        WS_USER_LOGIN ws_userLogin = new WS_USER_LOGIN(username, pass, id,imei);
        ws_userLogin.execute();
        try {
            return ws_userLogin.get();
        } catch (InterruptedException e) {
            return "Đăng nhập thất bại";
        } catch (ExecutionException e) {
            return "Đăng nhập thất bại";
        }
    }

    private class WS_USER_LOGIN extends AsyncTask<String, Integer, String> {
        int Id;
        String Username;
        String Password;
        String imei;
        public WS_USER_LOGIN(String Username, String Password, int Id,String imei) {
            this.Id = Id;
            this.Username = Username;
            this.Password = Password;
            this.imei = imei;
        }

        protected String doInBackground(String... params) {
            String METHOD_NAME = "MTB_LOGIN_NHANVIENKHAOSAT";
            SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
            request.addProperty("username", Username);
            request.addProperty("password", Password);
            request.addProperty("dvqlId", Id);
            request.addProperty("imei", imei);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

            try {
                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                Boolean result = Boolean.parseBoolean(resultsRequestSOAP.toString());
                if (result)
                    return null;
                else
                    throw new Exception("Đăng nhập thất bại");
            } catch (Exception e) {
                if (e.getMessage() != null
                        && e.getMessage().contains("HTTP status: 404"))
                    return "Sai địa chỉ web service";
                else if (e.getMessage() != null
                        && e.getMessage().contains("HTTP status: 500"))
                    return "Kiểm tra lại kết nối";
                else
                    return "Đăng nhập thất bại";
            }
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

    }

    // ------------ GET DVIQLY ------------
    public ArrayList<LinkedHashMap<String, String>> WS_GET_MA_DVIQLY_CALL() {
        WS_GET_MA_DVIQLY ws_get_ma_dviqly = new WS_GET_MA_DVIQLY();
        ws_get_ma_dviqly.execute();
        try {
            return ws_get_ma_dviqly.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_MA_DVIQLY extends
            AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {

        public WS_GET_MA_DVIQLY() {

        }

        @Override
        protected void onPostExecute(ArrayList<LinkedHashMap<String, String>> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<LinkedHashMap<String, String>> doInBackground(String... params) {
            String METHOD_NAME = "SP_SELECT_D_DVIQLY";
            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        EsspEntityDviQly ent_ma_dviqly = new EsspEntityDviQly();
                        try {
                            if (tableRow.getProperty("D_DVIQLY_ID") != null && tableRow.getProperty("D_DVIQLY_ID")
                                    .toString().trim().length() > 0)
                                ent_ma_dviqly.ID = Integer.parseInt(tableRow.getProperty("D_DVIQLY_ID").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_DVIQLY") != null)
                                ent_ma_dviqly.MA_DVIQLY = tableRow.getProperty("MA_DVIQLY").toString();
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("TEN_DVIQLY") != null)
                                ent_ma_dviqly.TEN_DVIQLY = tableRow.getProperty("TEN_DVIQLY").toString();
                        } catch (Exception e) {
                        }
                        ListViewData.add(ent_ma_dviqly.ToLinkedHashMap());
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // ------------ GET VAT TU -------------
    public ArrayList<LinkedHashMap<String, String>> WS_GET_VAT_TU_CALL(int dvqlId) {
        WS_GET_VAT_TU ws_vat_tu = new WS_GET_VAT_TU(dvqlId);
        ws_vat_tu.execute();
        try {
            return ws_vat_tu.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_VAT_TU extends AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {

        int dvqlId;
        public WS_GET_VAT_TU(int dvqlId) {
            this.dvqlId = dvqlId;
        }

        @Override
        protected void onPostExecute(
                ArrayList<LinkedHashMap<String, String>> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<LinkedHashMap<String, String>> doInBackground(
                String... params) {
            String METHOD_NAME = "SP_SELECT_D_VTU";
            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("dviqly_id", dvqlId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        EsspEntityVatTu ent_vattu = new EsspEntityVatTu();
                        try {
                            if (tableRow.getProperty("MA_VTU") != null)
                                ent_vattu.setMA_VTU(tableRow.getProperty("MA_VTU").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("TEN_VTU") != null)
                                ent_vattu.setTEN_VTU(tableRow.getProperty("TEN_VTU").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_LOAI_CPHI") != null)
                                ent_vattu.setMA_LOAI_CPHI(tableRow.getProperty("MA_LOAI_CPHI").toString().equals("anyType{}")?"":tableRow.getProperty("MA_LOAI_CPHI").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("DON_GIA") != null)
                                ent_vattu.setDON_GIA(tableRow.getProperty("DON_GIA").toString().equals("anyType{}")?"":tableRow.getProperty("DON_GIA").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("DVI_TINH") != null)
                                ent_vattu.setDVI_TINH(tableRow.getProperty("DVI_TINH").toString().equals("anyType{}")?"":tableRow.getProperty("DVI_TINH").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("DON_GIA_KH") != null)
                                ent_vattu.setDON_GIA_KH(tableRow.getProperty("DON_GIA_KH").toString().equals("anyType{}")?"":tableRow.getProperty("DON_GIA_KH").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("LOAI") != null)
                                ent_vattu.setLOAI(Integer.parseInt(tableRow.getProperty("LOAI").toString().equals("anyType{}")?"":tableRow.getProperty("LOAI").toString()));
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("CHUNG_LOAI") != null)
                                ent_vattu.setCHUNG_LOAI(Integer.parseInt(tableRow.getProperty("CHUNG_LOAI").toString().equals("anyType{}")?"":tableRow.getProperty("CHUNG_LOAI").toString()));
                        } catch (Exception e) {
                        }
                        ListViewData.add(ent_vattu.ToLinkedHashMap());
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // ------------ GET TRO NGAI -------------
    public ArrayList<LinkedHashMap<String, String>> WS_GET_MA_TNGAI_CALL(int dviqly_id) {
        WS_GET_MA_TNGAI ws_get_ma_tngai = new WS_GET_MA_TNGAI(dviqly_id);
        ws_get_ma_tngai.execute();
        try {
            return ws_get_ma_tngai.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_MA_TNGAI
            extends
            AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {

        int dvqlyId;

        public WS_GET_MA_TNGAI(int dvqlyId) {
            this.dvqlyId = dvqlyId;
        }

        @Override
        protected void onPostExecute(
                ArrayList<LinkedHashMap<String, String>> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<LinkedHashMap<String, String>> doInBackground(
                String... params) {
            String METHOD_NAME = "GET_D_TRO_NGAI_CMIS";
            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("id_dviqly", dvqlyId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                        try {
                            if (tableRow.getProperty("MA_TNGAI") != null
                                    && tableRow.getProperty("MA_TNGAI").toString().trim().length() > 0)
                                map.put("MA_TNGAI", tableRow.getProperty("MA_TNGAI").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("TEN_TNGAI") != null
                                    && tableRow.getProperty("TEN_TNGAI").toString().trim().length() > 0)
                                map.put("TEN_TNGAI", tableRow.getProperty("TEN_TNGAI").toString().equals("anyType{}")?"":tableRow.getProperty("TEN_TNGAI").toString());
                        } catch (Exception e) {
                        }
                        ListViewData.add(map);
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // ------------ GET TRAM -----------------
    public ArrayList<LinkedHashMap<String, String>> WS_GET_TRAM_CALL(int dvqlyId) {
        WS_GET_TRAM ws_get_tram = new WS_GET_TRAM(dvqlyId);
        ws_get_tram.execute();
        try {
            return ws_get_tram.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_TRAM extends AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {

        int dvqlyId;

        public WS_GET_TRAM(int dvqlyId) {
            this.dvqlyId = dvqlyId;
        }

        @Override
        protected void onPostExecute(
                ArrayList<LinkedHashMap<String, String>> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<LinkedHashMap<String, String>> doInBackground(
                String... params) {
            String METHOD_NAME = "SP_SELECT_D_TRAM_BY_DVQL";
            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("dvqlId", dvqlyId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        EsspEntityTram ent_tram = new EsspEntityTram();
                        try {
                            if (tableRow.getProperty("D_DVIQLY_ID") != null)
                                ent_tram.setMA_DVIQLY(tableRow.getProperty("D_DVIQLY_ID").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_TRAM") != null)
                                ent_tram.setMA_TRAM(tableRow.getProperty("MA_TRAM").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("TEN_TRAM") != null)
                                ent_tram.setTEN_TRAM(tableRow.getProperty("TEN_TRAM").toString().equals("anyType{}")?"":tableRow.getProperty("TEN_TRAM").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("LOAI_TRAM") != null)
                                ent_tram.setLOAI_TRAM(tableRow.getProperty("LOAI_TRAM").toString().equals("anyType{}")?"":tableRow.getProperty("LOAI_TRAM").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("CSUAT_TRAM") != null)
                                ent_tram.setCSUAT_TRAM(tableRow.getProperty("CSUAT_TRAM").toString().equals("anyType{}")?"":tableRow.getProperty("CSUAT_TRAM").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_CAP_DA") != null)
                                ent_tram.setMA_CAPDA(tableRow.getProperty("MA_CAP_DA").toString().equals("anyType{}")?"":tableRow.getProperty("MA_CAP_DA").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_CAP_DA_RA") != null)
                                ent_tram.setMA_CAPDA_RA(tableRow.getProperty("MA_CAP_DA_RA").toString().equals("anyType{}")?"":tableRow.getProperty("MA_CAP_DA_RA").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("D_TRAM_ID") != null)
                                ent_tram.setD_TRAM_ID(Integer.parseInt(tableRow.getProperty("D_TRAM_ID").toString().equals("anyType{}")?"":tableRow.getProperty("D_TRAM_ID").toString()));
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("DINH_DANH") != null)
                                ent_tram.setDINH_DANH((tableRow.getProperty("DINH_DANH").toString().equals("anyType{}")?"":tableRow.getProperty("DINH_DANH").toString()));
                        } catch (Exception e) {
                        }
                        ListViewData.add(ent_tram.ToLinkedHashMap());
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // ------------- GET SO GCS ------------------
    public ArrayList<LinkedHashMap<String, String>> WS_GET_MA_SO_CALL(int dviqly_id, Context context) {
        WS_GET_MA_SO ws_get_ma_so = new WS_GET_MA_SO(dviqly_id, context);
        ws_get_ma_so.execute();
        try {
            return ws_get_ma_so.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_MA_SO extends AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {

        int dvqlyId;
        Context context;

        public WS_GET_MA_SO(int dvqlyId, Context context) {
            this.dvqlyId = dvqlyId;
            this.context = context;
        }

        @Override
        protected void onPostExecute(
                ArrayList<LinkedHashMap<String, String>> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<LinkedHashMap<String, String>> doInBackground(
                String... params) {
            String METHOD_NAME = "SP_SELECT_D_SO_GCS";
            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("dvqly_id", dvqlyId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                        try {
                            if (tableRow.getProperty("D_DVIQLY_ID") != null
                                    && tableRow.getProperty("D_DVIQLY_ID").toString().trim().length() > 0)
                                map.put("D_DVIQLY_ID", tableRow.getProperty("D_DVIQLY_ID").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("TEN_SO") != null
                                    && tableRow.getProperty("TEN_SO").toString().trim().length() > 0)
                                map.put("TEN_SO", tableRow.getProperty("TEN_SO").toString().equals("anyType{}")?"":tableRow.getProperty("TEN_SO").toString());
                        } catch (Exception e) {
                        }
                        try {
                            if (tableRow.getProperty("MA_SO") != null
                                    && tableRow.getProperty("MA_SO").toString().trim().length() > 0)
                                map.put("MA_SO", tableRow.getProperty("MA_SO").toString().equals("anyType{}")?"":tableRow.getProperty("MA_SO").toString());
                        } catch (Exception e) {
                        }
                        ListViewData.add(map);
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // ------------- GET HO SO -------------------
//    public ArrayList<LinkedHashMap<String, String>> WS_GET_HO_SO_CALL(int dvqlyId, String username) {
//        WS_GET_HO_SO ws_get_ho_so = new WS_GET_HO_SO(dvqlyId, username);
//        ws_get_ho_so.execute();
//        try {
//            return ws_get_ho_so.get();
//        } catch (InterruptedException e) {
//            return null;
//        } catch (ExecutionException e) {
//            return null;
//        }
//    }
//
//    private class WS_GET_HO_SO extends AsyncTask<String, Integer, ArrayList<LinkedHashMap<String, String>>> {
//
//        int dvqlyId;
//        String username;
//
//        public WS_GET_HO_SO(int dvqlyId, String username) {
//            this.dvqlyId = dvqlyId;
//            this.username = username;
//        }
//
//        @Override
//        protected void onPostExecute(
//                ArrayList<LinkedHashMap<String, String>> result) {
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//        }
//
//        @Override
//        protected ArrayList<LinkedHashMap<String, String>> doInBackground(
//                String... params) {
//            String METHOD_NAME = "MTB_SELECT_D_PHANCONG_NHANVIEN";
//            ArrayList<LinkedHashMap<String, String>> ListViewData = new ArrayList<LinkedHashMap<String, String>>();
//            try {
//                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
//                request.addProperty("dvqlyId", dvqlyId);
//                request.addProperty("username", username);
//
//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//                envelope.dotNet = true;
//                envelope.setOutputSoapObject(request);
//                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);
//
//                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);
//
//                SoapObject tableRow = null;
//                SoapObject responseBody = (SoapObject) envelope.getResponse();
//                responseBody = (SoapObject) responseBody.getProperty(1);
//                SoapObject table = (SoapObject) responseBody.getProperty(0);
//
//                for (int i = 0; i < table.getPropertyCount(); i++) {
//                    tableRow = (SoapObject) table.getProperty(i);
//
//                    if (tableRow != null) {
//                        EsspEntityHoSo ent_ks = new EsspEntityHoSo();
//                        try {
//                            if (tableRow.getProperty("D_HOSO_ID") != null)
//                                ent_ks.setID(Integer.parseInt(tableRow.getProperty("D_HOSO_ID").toString()));
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_DVIQLY") != null)
//                                ent_ks.setMA_DVIQLY(tableRow.getProperty(
//                                        "MA_DVIQLY").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_YCAU_KNAI") != null)
//                                ent_ks.setMA_YCAU_KNAI(tableRow.getProperty(
//                                        "MA_YCAU_KNAI").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("TEN_KHHANG") != null)
//                                ent_ks.setTEN_KHHANG(tableRow.getProperty(
//                                        "TEN_KHHANG").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("SO_NHA") != null)
//                                ent_ks.setSO_NHA(tableRow.getProperty("SO_NHA").toString().equals("anyType{}")?"":tableRow.getProperty("SO_NHA").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DUONG_PHO") != null)
//                                ent_ks.setDUONG_PHO(tableRow.getProperty(
//                                        "DUONG_PHO").toString().equals("anyType{}")?"":tableRow.getProperty("DUONG_PHO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NOI_DUNG_YCAU") != null)
//                                ent_ks.setNOI_DUNG_YCAU(tableRow.getProperty(
//                                        "NOI_DUNG_YCAU").toString().equals("anyType{}")?"":tableRow.getProperty("NOI_DUNG_YCAU").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_LOAI_YCAU") != null)
//                                ent_ks.setMA_LOAI_YCAU(tableRow.getProperty(
//                                        "MA_LOAI_YCAU").toString().equals("anyType{}")?"":tableRow.getProperty("MA_LOAI_YCAU").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("TINH_TRANG") != null)
//                                ent_ks.setTINH_TRANG(tableRow.getProperty(
//                                        "TINH_TRANG").toString().equals("anyType{}")?"":tableRow.getProperty("TINH_TRANG").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_LOAIHD") != null)
//                                ent_ks.setMA_LOAIHD(tableRow.getProperty(
//                                        "MA_LOAIHD").toString().equals("anyType{}")?"":tableRow.getProperty("MA_LOAIHD").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAY_GCS") != null)
//                                ent_ks.setNGAY_GCS(tableRow.getProperty("NGAY_GCS")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("NGAY_GCS").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MASO_THUE") != null)
//                                ent_ks.setMASO_THUE(tableRow.getProperty(
//                                        "MASO_THUE").toString().equals("anyType{}")?"":tableRow.getProperty("MASO_THUE").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("TLE_THUE") != null)
//                                ent_ks.setTLE_THUE(tableRow.getProperty("TLE_THUE")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("TLE_THUE").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("LOAI_KHANG") != null)
//                                ent_ks.setLOAI_KHANG(tableRow.getProperty(
//                                        "LOAI_KHANG").toString().equals("anyType{}")?"":tableRow.getProperty("LOAI_KHANG").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("LOAI_DDO") != null)
//                                ent_ks.setLOAI_DDO(tableRow.getProperty("LOAI_DDO")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("LOAI_DDO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("TAI_KHOAN") != null)
//                                ent_ks.setTAI_KHOAN(tableRow.getProperty(
//                                        "TAI_KHOAN").toString().equals("anyType{}")?"":tableRow.getProperty("TAI_KHOAN").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAN_HANG") != null)
//                                ent_ks.setNGAN_HANG(tableRow.getProperty(
//                                        "NGAN_HANG").toString().equals("anyType{}")?"":tableRow.getProperty("NGAN_HANG").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_CAPDA") != null)
//                                ent_ks.setMA_CAPDA(tableRow.getProperty("MA_CAPDA")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("MA_CAPDA").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_TRAM") != null)
//                                ent_ks.setMA_TRAM(tableRow.getProperty("MA_TRAM")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("MA_TRAM").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MA_SOGCS") != null)
//                                ent_ks.setMA_SOGCS(tableRow.getProperty("MA_SOGCS")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("MA_SOGCS").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("STT") != null)
//                                ent_ks.setSTT(tableRow.getProperty("STT")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("STT").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("KIMUA_CSPK") != null)
//                                ent_ks.setKIMUA_CSPK(tableRow.getProperty(
//                                        "KIMUA_CSPK").toString().equals("anyType{}")?"":tableRow.getProperty("KIMUA_CSPK").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("CSUAT") != null)
//                                ent_ks.setCSUAT(tableRow.getProperty("CSUAT")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("CSUAT").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("MUC_DICH") != null)
//                                ent_ks.setMUC_DICH(tableRow.getProperty("MUC_DICH")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("MUC_DICH").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("SO_PHA") != null)
//                                ent_ks.setSO_PHA(tableRow.getProperty("SO_PHA")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("SO_PHA").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("SO_HO") != null)
//                                ent_ks.setSO_HO(tableRow.getProperty("SO_HO")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("SO_HO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("SO_CMT") != null)
//                                ent_ks.setSO_CMT(tableRow.getProperty("SO_CMT")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("SO_CMT").toString());
//                        } catch (Exception e) {
//                            e.toString();
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAY_CAP") != null)
//                                ent_ks.setNGAY_CAP(tableRow.getProperty("NGAY_CAP")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("NGAY_CAP").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DTHOAI_DD") != null)
//                                ent_ks.setDTHOAI_DD(tableRow.getProperty(
//                                        "DTHOAI_DD").toString().equals("anyType{}")?"":tableRow.getProperty("DTHOAI_DD").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DTHOAI_CD") != null)
//                                ent_ks.setDTHOAI_CD(tableRow.getProperty(
//                                        "DTHOAI_CD").toString().equals("anyType{}")?"":tableRow.getProperty("DTHOAI_CD").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("FAX") != null)
//                                ent_ks.setFAX(tableRow.getProperty("FAX")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("FAX").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("EMAIL") != null)
//                                ent_ks.setEMAIL(tableRow.getProperty("EMAIL")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("EMAIL").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("WEBSITE") != null)
//                                ent_ks.setWEBSITE(tableRow.getProperty("WEBSITE")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("WEBSITE").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("GHI_CHU") != null)
//                                ent_ks.setGHI_CHU(tableRow.getProperty("GHI_CHU")
//                                        .toString().equals("anyType{}")?"":tableRow.getProperty("GHI_CHU").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGUON_TIEPNHAN") != null)
//                                ent_ks.setNGUON_TIEPNHAN(Integer.parseInt(tableRow.getProperty("NGUON_TIEPNHAN").toString().equals("anyType{}")?"":tableRow.getProperty("NGUON_TIEPNHAN").toString()));
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("PHUONG_XA") != null)
//                                ent_ks.setPHUONG_XA(tableRow.getProperty("PHUONG_XA").toString().equals("anyType{}")?"":tableRow.getProperty("PHUONG_XA").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("COT_SO") != null)
//                                ent_ks.setCOT_SO(tableRow.getProperty("COT_SO").toString().equals("anyType{}")?"":tableRow.getProperty("COT_SO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DOI_QL") != null)
//                                ent_ks.setDOI_QL(tableRow.getProperty("DOI_QL").toString().equals("anyType{}")?"":tableRow.getProperty("DOI_QL").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("TEN_CTRINH") != null)
//                                ent_ks.setTEN_CTRINH(tableRow.getProperty("TEN_CTRINH").toString().equals("anyType{}")?"":tableRow.getProperty("TEN_CTRINH").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAY_TAO") != null)
//                                ent_ks.setSO_CTO_THAO_API(tableRow.getProperty("NGAY_TAO").toString().equals("anyType{}")?"":tableRow.getProperty("NGAY_TAO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAY_SUA") != null)
//                                ent_ks.setSO_BBAN_TUTI_API(tableRow.getProperty("NGAY_SUA").toString().equals("anyType{}")?"":tableRow.getProperty("NGAY_SUA").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NOI_CAP") != null)
//                                ent_ks.setNOI_CAP(tableRow.getProperty("NOI_CAP").toString().equals("anyType{}")?"":tableRow.getProperty("NOI_CAP").toString());
//                        } catch (Exception e) {
//                        }
//
//                        try {
//                            if (tableRow.getProperty("SO_NHA_DDO") != null)
//                                ent_ks.setSO_NHA_DDO(tableRow.getProperty("SO_NHA_DDO").toString().equals("anyType{}")?"":tableRow.getProperty("SO_NHA_DDO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DUONG_PHO_DDO") != null)
//                                ent_ks.setDUONG_PHO_DDO(tableRow.getProperty("DUONG_PHO_DDO").toString().equals("anyType{}")?"":tableRow.getProperty("DUONG_PHO_DDO").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("DCHI_NGUOIYCAU") != null)
//                                ent_ks.setDCHI_NGUOIYCAU(tableRow.getProperty("DCHI_NGUOIYCAU").toString().equals("anyType{}")?"":tableRow.getProperty("DCHI_NGUOIYCAU").toString());
//                        } catch (Exception e) {
//                        }
//                        try {
//                            if (tableRow.getProperty("NGAYHEN_KSAT") != null)
//                                ent_ks.setNGAYHEN_KSAT(tableRow.getProperty("NGAYHEN_KSAT").toString().equals("anyType{}")?"":tableRow.getProperty("NGAYHEN_KSAT").toString());
//                        } catch (Exception e) {
//                        }
//                        LinkedHashMap<String, String> map = ent_ks.ToLinkedHashMap();
//                        try {
//                            if (tableRow.getProperty("MA_NVIEN") != null
//                                    && tableRow.getProperty("MA_NVIEN").toString().trim().length() > 0)
//                                map.put("BUNDLE_MA_NVIEN", tableRow.getProperty("BUNDLE_MA_NVIEN").toString().equals("anyType{}")?"":tableRow.getProperty("BUNDLE_MA_NVIEN").toString());
//                        } catch (Exception e) {
//                        }
//                        ListViewData.add(map);
//                    }
//                }
//
//                return ListViewData;
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//    }

    // -------------- GET MA HO SO ------------------
    public ArrayList<String> WS_GET_MA_HO_SO_CALL() {
        WS_GET_MA_HO_SO ws_get_ma_ho_so = new WS_GET_MA_HO_SO();
        ws_get_ma_ho_so.execute();
        try {
            return ws_get_ma_ho_so.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private class WS_GET_MA_HO_SO extends AsyncTask<String, Integer, ArrayList<String>> {

        public WS_GET_MA_HO_SO() {
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String METHOD_NAME = "SP_SELECT_ALL_D_HOSO";
            ArrayList<String> ListViewData = new ArrayList<String>();
            try {
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapObject tableRow = null;
                SoapObject responseBody = (SoapObject) envelope.getResponse();
                responseBody = (SoapObject) responseBody.getProperty(1);
                SoapObject table = (SoapObject) responseBody.getProperty(0);

                for (int i = 0; i < table.getPropertyCount(); i++) {
                    tableRow = (SoapObject) table.getProperty(i);

                    if (tableRow != null) {
                        try {
                            if (tableRow.getProperty("MA_YCAU_KNAI") != null)
                                ListViewData.add(tableRow.getProperty("MA_YCAU_KNAI").toString());
                        } catch (Exception e) {
                        }
                    }
                }

                return ListViewData;
            } catch (Exception e) {
                return null;
            }
        }

    }

    // -------------- UPDATE MA HO SO ----------------
    public boolean WS_UPDATE_MA_HS_CALL(int hoso_id, String mayeucau) {
        WS_UPDATE_MA_HS ws_updatemahs = new WS_UPDATE_MA_HS(hoso_id, mayeucau);
        ws_updatemahs.execute();
        try {
            return ws_updatemahs.get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private class WS_UPDATE_MA_HS extends AsyncTask<String, Integer, Boolean> {
        int hoso_id;
        String mayeucau;

        public WS_UPDATE_MA_HS(int hoso_id, String mayeucau) {
            this.hoso_id = hoso_id;
            this.mayeucau = mayeucau;
        }

        protected Boolean doInBackground(String... params) {
            try {
                String METHOD_NAME = "MTB_UPDATE_MA_HOSO";
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("hoso_id", hoso_id);
                request.addProperty("mayeucau", mayeucau);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

                boolean result = Boolean.parseBoolean(resultsRequestSOAP.toString());

                return result;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

    }

    // --------------- UPDATE TRẠNG THÁI HO SO ---------------
    public boolean WS_CHECK_STATUS_CALL(int hosoId, int dvqlId, int tinhtrang) {
        WS_CHECK_STATUS ws_checkStatus = new WS_CHECK_STATUS(hosoId, dvqlId,
                tinhtrang);
        ws_checkStatus.execute();
        try {
            return ws_checkStatus.get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private class WS_CHECK_STATUS extends AsyncTask<String, Integer, Boolean> {
        int hosoId;
        int dvqlId;
        int tinhtrang;

        public WS_CHECK_STATUS(int hosoId, int dvqlId, int tinhtrang) {
            this.hosoId = hosoId;
            this.dvqlId = dvqlId;
            this.tinhtrang = tinhtrang;
        }

        protected Boolean doInBackground(String... params) {
            try {
                String METHOD_NAME = "SP_UPDATE_TINHTRANG_D_HOSO";
                SoapObject request = new SoapObject(EsspConstantVariables.NAMESPACE, METHOD_NAME);
                request.addProperty("hosoId", hosoId);
                request.addProperty("dvqlId", dvqlId);
                request.addProperty("tinhtrang", tinhtrang);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(EsspAsyncCallWS.this.URL_SERVICE);

                androidHttpTransport.call(EsspConstantVariables.NAMESPACE + METHOD_NAME, envelope);

                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();

                boolean result = Boolean.parseBoolean(resultsRequestSOAP.toString());

                return result;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

    }


}
