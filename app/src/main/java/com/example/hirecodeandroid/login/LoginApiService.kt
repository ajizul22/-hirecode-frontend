package com.example.hirecodeandroid.login

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(
        @Field("ac_email") email: String,
        @Field("ac_password") password: String) : LoginResponse

}