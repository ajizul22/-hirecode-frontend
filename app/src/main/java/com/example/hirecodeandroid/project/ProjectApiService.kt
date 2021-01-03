package com.example.hirecodeandroid.project

import android.content.SharedPreferences
import com.example.hirecodeandroid.util.SharePrefHelper
import okhttp3.RequestBody
import retrofit2.http.*

interface ProjectApiService {

    @GET("project/{id}")
    suspend fun getProjectByCompanyId(@Path("id") companyId: String?): ProjectResponse

    @Multipart
    @POST("project")
    suspend fun addProject(
        @Part("cn_id") companyId: RequestBody,
        @Part("pj_nama_project") projectName: RequestBody,
        @Part("pj_deskripsi") projectDesc: RequestBody,
        @Part("pj_deadline") projectDeadline: RequestBody
    ) : AddProjectResponse

}