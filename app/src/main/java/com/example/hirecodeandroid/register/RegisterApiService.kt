package com.example.hirecodeandroid.register

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterApiService {


    @FormUrlEncoded
    @POST("account/register")
    suspend fun RegisterEngineerRequest(
        @Field("acName") name: String,
        @Field("acEmail") email: String,
        @Field("acPhone") phone: String,
        @Field("acPassword") password: String,
        @Field("acLevel") level: Int
    ) : RegisterEngineerResponse

    @FormUrlEncoded
    @POST("account/register")
    suspend fun RegisterCompanyRequest(
        @Field("acName") name: String,
        @Field("acEmail") email: String,
        @Field("acPhone") phone: String,
        @Field("acPassword") password: String,
        @Field("acLevel") level: Int,
        @Field("acPerusahaan") companyName: String,
        @Field("acJabatan") position: String
    ) : RegisterCompanyResponse
}