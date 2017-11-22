package es.vinhnb.ttht.server;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import es.vinhnb.ttht.entity.api.D_DVIQLYModel;
import es.vinhnb.ttht.entity.api.MtbBbanModel;
import es.vinhnb.ttht.entity.api.UpdateStatus;
import es.vinhnb.ttht.entity.api.UserMtb;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TthtHnApiInterface {
    @GET("Get_LoginMTB")
    Call<UserMtb> Get_LoginMTB(@Query("madonvi") String madonvi, @Query("username") String username, @Query("password") String password, @Query("imei") String imei);

    @GET("Get_d_dviqly")
    Call<List<D_DVIQLYModel>> Get_d_dviqly();

    @GET("LayDuLieuCmis")
    Call<List<UpdateStatus>> LayDuLieuCmis(@Query("maDonVi") String maDonVi, @Query("maNhanVien") String maNhanVien);

    @GET("GeT_BBAN")
    Call<List<MtbBbanModel>> GeT_BBAN(@Query("maDonVi") String maDonVi, @Query("maNhanVien") String maNhanVien);



    public static class AsyncApi extends AsyncTask<Void, Void, Bundle>
    {
        IAsync iAsync;

        public AsyncApi(IAsync iAsync) {
            this.iAsync = iAsync;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iAsync.onPreExecute();
        }

        @Override
        protected Bundle doInBackground(Void... voids) {
            return iAsync.doInBackground();
        }

        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);
//            iAsync.onPostExecute(result);
        }


    }
    abstract class IAsync{
        public static final String STATUS_CODE = "STATUS_CODE";
        public static final String BUNDLE_DATA = "BUNDLE_DATA";

        public abstract void onPreExecute();
        public abstract Bundle doInBackground();
//        public abstract void onPostExecute(Bundle result);
    }


//    @POST("/api/users")
//    Call<User> createUser(@Body User user);
//
//    @GET("/api/users?")
//    Call<UserList> doGetUserList(@Query("page") String page);
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}