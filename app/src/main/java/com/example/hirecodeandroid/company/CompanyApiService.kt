package com.example.hirecodeandroid.company

import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyApiService {

    @GET("company/{id}")
    suspend fun getDataCompany(@Path("id") companyId: Int?) : CompanyResponse
}