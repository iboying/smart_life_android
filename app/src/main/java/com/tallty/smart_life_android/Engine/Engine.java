package com.tallty.smart_life_android.Engine;

import com.tallty.smart_life_android.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by kang on 16/8/1.
 * base_url: http://220.163.125.158:8081
 * 业务接口
 */

public interface Engine {
    // 登录
    @FormUrlEncoded
    @Headers ("Accept: application/json")
    @POST("users/sign_in")
    Call<User> getUser(@Field("user[phone]") String phone, @Field("user[password]") String password);

}