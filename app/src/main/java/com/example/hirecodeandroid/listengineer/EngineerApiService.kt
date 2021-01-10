package com.example.hirecodeandroid.listengineer

import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EngineerApiService {

    @GET("engineer/detail")
    suspend fun getAllEngineer(): ListEngineerResponse

    @GET("engineer/{id}")
    suspend fun getDataEngById(@Path("id") engineerId: String?) : ListEngineerResponse

    @GET("engineer")
    suspend fun getAllEngineerSearch(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("filter") filter: Int? = null
    ) : ListEngineerResponse

    @Multipart
    @PUT("engineer/{id}")
    suspend fun updateEngineer(
        @Path("id") engineerId: Int?,
        @Part("en_job_title") jobTitle: RequestBody,
        @Part("en_job_type") jobType: RequestBody,
        @Part("en_domisili") engineerDomicilie: RequestBody,
        @Part("en_deskripsi") engineerDesc: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

}