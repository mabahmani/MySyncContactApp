package com.example.mysynccontactapp.retrofit;

import com.example.mysynccontactapp.retrofit.req.RegisterReqBody;
import com.example.mysynccontactapp.retrofit.res.RegisterUserResBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("registerUser/")
    Call<RegisterUserResBody> registerUser(@Body RegisterReqBody registerReqBody);

}
