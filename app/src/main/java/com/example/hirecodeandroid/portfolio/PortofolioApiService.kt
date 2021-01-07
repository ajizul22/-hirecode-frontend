package com.example.hirecodeandroid.portfolio

import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface PortofolioApiService {

    @GET("portofolio/{id}")
    suspend fun getPortfolioEngineer(@Path("id") engineerId: Int?) : PortofolioResponse

    @Multipart
    @POST("portofolio")
    suspend fun addPortfolio(
        @Part("en_id") engineerId: RequestBody,
        @Part("pr_aplikasi") portAppName: RequestBody,
        @Part("pr_deskripsi") portDesc: RequestBody,
        @Part("pr_link_pub") portLinkPub: RequestBody,
        @Part("pr_link_repo") portLinkRepo: RequestBody,
        @Part("pr_tp_kerja") portWorkPlace: RequestBody,
        @Part("pr_tipe") portType: RequestBody
    ) : GeneralResponse

}