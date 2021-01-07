package com.example.hirecodeandroid.skill

import com.example.hirecodeandroid.util.GeneralResponse
import retrofit2.http.*

interface SkillApiService {

    @GET("skill/{id}")
    suspend fun getSkillByEngineer(@Path("id") engineerId: Int?) : SkillResponse

    @FormUrlEncoded
    @POST("skill")
    suspend fun createSkill(@Field("enId") engineerId: Int?, @Field("skNamaSkill") skillName: String?) : GeneralResponse

    @DELETE("skill/{id}")
    suspend fun deleteSkill(@Path("id") skillId: Int?) : GeneralResponse

}