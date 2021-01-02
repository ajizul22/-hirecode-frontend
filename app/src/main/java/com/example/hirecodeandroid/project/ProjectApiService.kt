package com.example.hirecodeandroid.project

import android.content.SharedPreferences
import com.example.hirecodeandroid.util.SharePrefHelper
import retrofit2.http.GET
import retrofit2.http.Path

interface ProjectApiService {

    @GET("project/{id}")
    suspend fun getProjectByCompanyId(@Path("id") companyId: String?): ProjectResponse

}