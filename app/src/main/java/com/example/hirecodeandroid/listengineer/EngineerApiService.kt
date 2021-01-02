package com.example.hirecodeandroid.listengineer

import retrofit2.http.GET
import retrofit2.http.Path

interface EngineerApiService {

    @GET("engineer")
    suspend fun getAllEngineer(): ListEngineerResponse



}