package com.example.hirecodeandroid.hire

import com.example.hirecodeandroid.util.GeneralResponse
import retrofit2.http.*

interface HireApiService {

    @GET("hire/engineer/{id}")
    suspend fun getHireByEngineerId(@Path("id") engineerId: String?) : HireResponse

    @FormUrlEncoded
    @POST("hire")
    suspend fun addHire(
        @Field("en_id") engineerId: Int,
        @Field("pj_id") projectId: Int,
        @Field("hr_price") hirePrice: String,
        @Field("hr_message") hireMessage: String,
        @Field("hr_status") hireStatus: String
    ) : HireResponse

    @FormUrlEncoded
    @PUT("hire/{id}")
    suspend fun responseHire(
        @Path("id") hireId: String?,
        @Field("hr_status") hireStatus: String
    ): GeneralResponse

}