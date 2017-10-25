package com.es.tungnv.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.es.tungnv.utils.InvoiceCommon;
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
 * Created by TUNGNV on 2/29/2016.
 */
public class InvoiceAsyncCallWS {

    private String URL_SERVICE;

    public InvoiceAsyncCallWS() {
        this.URL_SERVICE = "http://" + InvoiceCommon.IP_SERVER_1 + "/WS_KHONO.asmx";
    }

    public JSONObject WS_REGISTER_CALL(String MA_DVIQLY, String MA_MAY, String MA_TNGAN, String TEN_TNGAN, String MAT_KHAU, String DTHOAI_TNGAN) {
        WS_REGISTER ws_register = new WS_REGISTER(MA_DVIQLY, MA_MAY, MA_TNGAN, TEN_TNGAN, MAT_KHAU, DTHOAI_TNGAN);
        ws_register.execute();
        try {
            return ws_register.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public ArrayList<JSONObject> WS_GET_PS_CALL(String MA_DVIQLY, String MA_MAY, String MA_TNGAN) {
        WS_GET_PS ws_get_ps = new WS_GET_PS(MA_DVIQLY, MA_MAY, MA_TNGAN);
        ws_get_ps.execute();
        try {
            return ws_get_ps.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public String WS_UPDATE_TRANG_THAI_GETPS_CALL(String MA_DVIQLY, String MA_TNGAN, int TT_HDON) {
        WS_UPDATE_TRANG_THAI_GETPS ws_update = new WS_UPDATE_TRANG_THAI_GETPS(MA_DVIQLY, MA_TNGAN, TT_HDON);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public String WS_UPDATE_CHAM_NO_CALL(String MA_DVIQLY, String MA_TNGAN, String TT_HDON) {
        WS_UPDATE_CHAM_NO ws_update = new WS_UPDATE_CHAM_NO(MA_DVIQLY, MA_TNGAN, TT_HDON);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public String WS_UPDATE_HUY_CHAM_CALL(String MA_DVIQLY, String MA_TNGAN, String TT_HDON) {
        WS_UPDATE_HUY_CHAM ws_update = new WS_UPDATE_HUY_CHAM(MA_DVIQLY, MA_TNGAN, TT_HDON);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    public String WS_UPDATE_QUYET_TOAN_CALL(String MA_DVIQLY, String MA_TNGAN, String TT_BBGIAO) {
        WS_UPDATE_QUYET_TOAN ws_update = new WS_UPDATE_QUYET_TOAN(MA_DVIQLY, MA_TNGAN, TT_BBGIAO);
        ws_update.execute();
        try {
            return ws_update.get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    /*-----------------------------------------------------------------------------------------------
    * -----------------------------------------------------------------------------------------------
    * -----------------------------------------------------------------------------------------------
    * -----------------------------------------------------------------------------------------------
    * -----------------------------------------------------------------------------------------------*/

    public class WS_REGISTER extends AsyncTask<Void, Void, JSONObject>
    {

        private String MA_DVIQLY;
        private String MA_MAY;
        private String MA_TNGAN;
        private String TEN_TNGAN;
        private String MAT_KHAU;
        private String DTHOAI_TNGAN;

        public WS_REGISTER(String MA_DVIQLY, String MA_MAY, String MA_TNGAN, String TEN_TNGAN, String MAT_KHAU, String DTHOAI_TNGAN){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_MAY = MA_MAY;
            this.MA_TNGAN = MA_TNGAN;
            this.TEN_TNGAN = TEN_TNGAN;
            this.MAT_KHAU = MAT_KHAU;
            this.DTHOAI_TNGAN = DTHOAI_TNGAN;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject listJSon = new JSONObject();
            final String METHOD="POS_REGISTER_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_MAY", MA_MAY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
            request.addProperty("TEN_TNGAN", TEN_TNGAN);
            request.addProperty("MAT_KHAU", MAT_KHAU);
            request.addProperty("DTHOAI_TNGAN", DTHOAI_TNGAN);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport=new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                String jsonText = data.toString();
                JSONArray arr = new JSONArray(jsonText);
                listJSon = arr.getJSONObject(0);
            } catch(Exception e) {
                Log.e("Register error", "Register error: " + e.toString());
                return null;
            }
            return listJSon;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
        }
    }

    public class WS_GET_PS extends AsyncTask<Void, Void, ArrayList<JSONObject>>
    {

        private String MA_DVIQLY;
        private String MA_MAY;
        private String MA_TNGAN;

        public WS_GET_PS(String MA_DVIQLY, String MA_MAY, String MA_TNGAN){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_MAY = MA_MAY;
            this.MA_TNGAN = MA_TNGAN;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            ArrayList<JSONObject> listJSon = new ArrayList<JSONObject>();
            final String METHOD="POS_GET_PS_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_MAY", MA_MAY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
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

    public class WS_UPDATE_TRANG_THAI_GETPS extends AsyncTask<Void, Void, String>
    {

        private String MA_DVIQLY;
        private String MA_TNGAN;
        private int TONG_HDON;

        public WS_UPDATE_TRANG_THAI_GETPS(String MA_DVIQLY, String MA_TNGAN, int TONG_HDON){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_TNGAN = MA_TNGAN;
            this.TONG_HDON = TONG_HDON;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            final String METHOD="POS_UPDATE_TRANG_THAI_GETPS_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
            request.addProperty("TONG_HDON", TONG_HDON);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                result = data.toString();
            } catch(Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class WS_UPDATE_CHAM_NO extends AsyncTask<Void, Void, String>
    {

        private String MA_DVIQLY;
        private String MA_TNGAN;
        private String TT_HDON;

        public WS_UPDATE_CHAM_NO(String MA_DVIQLY, String MA_TNGAN, String TT_HDON){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_TNGAN = MA_TNGAN;
            this.TT_HDON = TT_HDON;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            final String METHOD="POS_UPDATE_CHAM_NO_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
            request.addProperty("TT_HDON", TT_HDON);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                result = data.toString();
            } catch(Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class WS_UPDATE_HUY_CHAM extends AsyncTask<Void, Void, String>
    {

        private String MA_DVIQLY;
        private String MA_TNGAN;
        private String TT_HDON;

        public WS_UPDATE_HUY_CHAM(String MA_DVIQLY, String MA_TNGAN, String TT_HDON){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_TNGAN = MA_TNGAN;
            this.TT_HDON = TT_HDON;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            final String METHOD="POS_UPDATE_HUY_CHAM_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
            request.addProperty("TT_HDON", TT_HDON);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                result = data.toString();
            } catch(Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class WS_UPDATE_QUYET_TOAN extends AsyncTask<Void, Void, String>
    {

        private String MA_DVIQLY;
        private String MA_TNGAN;
        private String TT_BBGIAO;

        public WS_UPDATE_QUYET_TOAN(String MA_DVIQLY, String MA_TNGAN, String TT_BBGIAO){
            this.MA_DVIQLY = MA_DVIQLY;
            this.MA_TNGAN = MA_TNGAN;
            this.TT_BBGIAO = TT_BBGIAO;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            final String METHOD="POS_QUYET_TOAN_YB";
            final String SOAPACTION = InvoiceConstantVariables.NAMESPACE + METHOD;
            SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            SoapObject request=new SoapObject(InvoiceConstantVariables.NAMESPACE, METHOD);
            request.addProperty("MA_DVIQLY", MA_DVIQLY);
            request.addProperty("MA_TNGAN", MA_TNGAN);
            request.addProperty("TT_BBGIAO", TT_BBGIAO);
            envelope.setOutputSoapObject(request);
            HttpTransportSE transport = new HttpTransportSE(URL_SERVICE);
            try {
                transport.call(SOAPACTION, envelope);
                SoapPrimitive data = (SoapPrimitive) envelope.getResponse();
                result = data.toString();
            } catch(Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}
