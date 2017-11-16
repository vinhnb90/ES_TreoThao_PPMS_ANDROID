package es.vinhnb.ttht.server;

import java.util.List;

import es.vinhnb.ttht.entity.api.D_DVIQLYModel;
import es.vinhnb.ttht.entity.api.UserMtb;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TthtHnApiInterface {
    @GET("Get_LoginMTB")
    Call<UserMtb> Get_LoginMTB(@Query("madonvi") String madonvi, @Query("username") String username, @Query("password") String password, @Query("imei") String imei);

    @GET("Get_d_dviqly")
    Call<List<D_DVIQLYModel>> Get_d_dviqly();


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