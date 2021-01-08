package com.example.hirecodeandroid.project

import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.MultipartBody
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
        @Part("pj_deadline") projectDeadline: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

}