package com.example.hirecodeandroid.experience

import com.example.hirecodeandroid.util.GeneralResponse
import retrofit2.http.*

interface ExperienceApiService {

    @GET("experience/{id}")
    suspend fun getExpByIdEng(@Path("id") engineerId: Int?) : ExperienceResponse

    @FormUrlEncoded
    @POST("experience")
    suspend fun addExperience(
        @Field("enId") engineerId: Int?,
        @Field("exPosisi") expPosition: String?,
        @Field("exPerusahaan") expCompany: String?,
        @Field("exStart") expStart: String?,
        @Field("exEnd") expEnd: String?,
        @Field("exDesc") expDesc: String?
    ) : GeneralResponse

    @DELETE("experience/{id}")
    suspend fun deleteExperience(@Path("id") expId: Int?) : GeneralResponse

    @PUT("experience/{id}")
    suspend fun updateExperience(
        @Path("id") expId: Int?,
        @Field("exPosisi") expPosition: String?,
        @Field("exPerusahaan") expCompany: String?,
        @Field("exStart") expStart: String?,
        @Field("exEnd") expEnd: String?,
        @Field("exDesc") expDesc: String?
    ) : GeneralResponse

}