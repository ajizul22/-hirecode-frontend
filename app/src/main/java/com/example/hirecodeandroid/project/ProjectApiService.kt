package com.example.hirecodeandroid.project

import com.example.hirecodeandroid.project.detailproject.DetailProjectResponse
import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProjectApiService {

    @GET("project/{id}")
    suspend fun getProjectByCompanyId(@Path("id") companyId: String?): ProjectResponse

    @GET("project/detail/{id}")
    suspend fun getProjectByProjectId(@Path("id") projectId: Int?) : DetailProjectResponse

    @DELETE("project/{id}")
    suspend fun deleteProject(@Path("id") projectId: Int?) : GeneralResponse

    @Multipart
    @POST("project")
    suspend fun addProject(
        @Part("cn_id") companyId: RequestBody,
        @Part("pj_nama_project") projectName: RequestBody,
        @Part("pj_deskripsi") projectDesc: RequestBody,
        @Part("pj_deadline") projectDeadline: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

    @Multipart
    @PUT("project/{id}")
    suspend fun updateProject(
        @Path("id") projectId: Int?,
        @Part("pj_nama_project") projectName: RequestBody,
        @Part("pj_deskripsi") projectDesc: RequestBody,
        @Part("pj_deadline") projectDeadline: RequestBody
    ) : GeneralResponse

    @Multipart
    @PUT("project/{id}")
    suspend fun updateProjectWithImage(
        @Path("id") projectId: Int?,
        @Part("pj_nama_project") projectName: RequestBody,
        @Part("pj_deskripsi") projectDesc: RequestBody,
        @Part("pj_deadline") projectDeadline: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

}