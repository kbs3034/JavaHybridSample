package com.example.javahybridsample.api;

import com.example.javahybridsample.api.data.sample.SampleUser;
import com.example.javahybridsample.api.data.sample.SampleUserList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    /**
     * Sample
     * @throws Exception
     */
    @GET("/api/unknown")
    Call<ResponseBody> doGetListResources();

    /**
     * Sample
     * @throws Exception
     */
    @POST("/api/users")
    Call<ResponseBody> createUser(@Body SampleUser user);

    /**
     * Sample
     * @throws Exception
     */
    @GET("/api/users?")
    Call<SampleUserList> doGetUserList(@Query("page") String page);

    /**
     * Sample
     * @throws Exception
     */
    @GET("/api/users?")
    Call<ResponseBody> doGetUserListForJsonObject(@Query("page") String page);

    /**
     * Sample
     * @throws Exception
     */
    @FormUrlEncoded
    @POST("/api/users?")
    Call<ResponseBody> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
