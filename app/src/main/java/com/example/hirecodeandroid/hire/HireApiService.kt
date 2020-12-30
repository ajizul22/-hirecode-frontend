package com.example.hirecodeandroid.hire

import retrofit2.http.GET

interface HireApiService {

    @GET("hire/engineer/1")
    suspend fun getHireByEngineerId() : HireResponse

}