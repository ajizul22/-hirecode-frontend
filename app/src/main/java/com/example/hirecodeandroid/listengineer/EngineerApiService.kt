package com.example.hirecodeandroid.listengineer

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface EngineerApiService {

    @GET("engineer/detail")
    suspend fun getAllEngineer(): ListEngineerResponse

    @GET("engineer/{id}")
    suspend fun getDataEngById(@Path("id") engineerId: String?) : ListEngineerResponse

}