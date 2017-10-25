package com.es.tungnv.webservice;

import android.os.AsyncTask;

import com.es.tungnv.utils.GphtCommon;
import com.es.tungnv.utils.InvoiceConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by TUNGNV on 8/12/2016.
 */
public class GphtAsyncCallWSJson {

    private static final GphtAsyncCallWSJson wsJson = new GphtAsyncCallWSJson();

    private static String URL_SERVICE;

    private GphtAsyncCallWSJson() {

    }

    public static GphtAsyncCallWSJson getInstance() {
        URL_SERVICE = "http://" + GphtCommon.getIpServer() + GphtCommon.getServerName();
        return wsJson;
    }

    // ------------ CHECK CHANGE ------------
    public boolean WS_CHECK_CHANGE_CALL(int id, String time) {
        WS_CHECK_CHANGE ws_check_change = new WS_CHECK_CHANGE(id, time);
        ws_check_change.execute();
        try {
            return ws_check_change.get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public class WS_CHECK_CHANGE extends AsyncTask<Void, Void, Boolean>
    {

        private int id;
        private String time;

        public WS_CHECK_CHANGE(int id, String time){
            this.id = id;
            this.time = time;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String METHOD="Check_Log_Quyen";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request = new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("id", id);
            request.addProperty("time", time);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                if("true, false".contains(jsonText))
                    return Boolean.parseBoolean(jsonText);
                else
                    return false;
            } catch(Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DVIQLY ------------
    public ArrayList<JSONObject> WS_GET_DVI_QLY_CALL() {
        WS_GET_DVI_QLY ws_get_dvi_qly = new WS_GET_DVI_QLY();
        ws_get_dvi_qly.execute();
        try {
            return ws_get_dvi_qly.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_DVI_QLY extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_DVI_QLY(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_DM";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DANH MUC HE THONG ------------
    public ArrayList<JSONObject> WS_GET_HE_THONG_CALL() {
        WS_GET_HE_THONG ws_get_he_thong = new WS_GET_HE_THONG();
        ws_get_he_thong.execute();
        try {
            return ws_get_he_thong.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_HE_THONG extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {


        public WS_GET_HE_THONG(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_HeThong_ByDonVi";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET DANH MUC HE THONG ------------
    public ArrayList<JSONObject> WS_GET_USER_CHA_CALL() {
        WS_GET_USER_CHA ws_get_user_cha = new WS_GET_USER_CHA();
        ws_get_user_cha.execute();
        try {
            return ws_get_user_cha.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_USER_CHA extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_USER_CHA(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_UserCha";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
//            request.addProperty("ID_DVIQLY", ID_DVIQLY);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET User con ------------
    public ArrayList<JSONObject> WS_GET_USER_CON_CALL() {
        WS_GET_USER_CON ws_get_user_con= new WS_GET_USER_CON();
        ws_get_user_con.execute();
        try {
            return ws_get_user_con.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_USER_CON extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_USER_CON(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_UserCon";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET quyền ------------
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
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_Quyen_HeThong";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }

    // ------------ GET quyền ------------
    public ArrayList<JSONObject> WS_GET_LOG_QUYEN_CALL() {
        WS_GET_LOG_QUYEN ws_get_log_quyen = new WS_GET_LOG_QUYEN();
        ws_get_log_quyen.execute();
        try {
            return ws_get_log_quyen.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public class WS_GET_LOG_QUYEN extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        public WS_GET_LOG_QUYEN(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="Get_Log_Quyen";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonObj=arr.getJSONObject(i);
                    listJSon.add(jsonObj);
                }
            } catch(Exception e) {
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
            super.onPostExecute(result);
        }
    }
}
