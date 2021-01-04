package com.example.hirecodeandroid.experience

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
    ) : ExperienceResponse

}