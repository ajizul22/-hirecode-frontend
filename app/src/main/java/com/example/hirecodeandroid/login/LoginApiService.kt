package com.example.hirecodeandroid.login

import retrofit2.http.*

interface LoginApiService {

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(
        @Field("ac_email") email: String,
        @Field("ac_password") password: String) : LoginResponse

    @GET("engineer/account/{id}")
    suspend fun getEngineerIdByAccountId(@Path("id") enId: String?) : GetEngineerIdResponse

    @GET("company/account/{id}")
    suspend fun getCompanyIdByAccountId(@Path("id") cnId: String?) : GetCompanyIdResponse



}