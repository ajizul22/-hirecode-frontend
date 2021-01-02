package com.example.hirecodeandroid.hire

import retrofit2.http.GET
import retrofit2.http.Path

interface HireApiService {

    @GET("hire/engineer/{id}")
    suspend fun getHireByEngineerId(@Path("id") engineerId: String?) : HireResponse

}