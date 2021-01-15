package com.example.hirecodeandroid.portfolio

import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PortofolioApiService {

    @GET("portofolio/{id}")
    suspend fun getPortfolioEngineer(@Path("id") engineerId: Int?) : PortofolioResponse

    @GET("portofolio/detail/{id}")
    suspend fun getPortfolioByIdPort(@Path("id") portofolioId: Int?) : PortofolioResponse

    @Multipart
    @POST("portofolio")
    suspend fun addPortfolio(
        @Part("en_id") engineerId: RequestBody,
        @Part("pr_aplikasi") portAppName: RequestBody,
        @Part("pr_deskripsi") portDesc: RequestBody,
        @Part("pr_link_pub") portLinkPub: RequestBody,
        @Part("pr_link_repo") portLinkRepo: RequestBody,
        @Part("pr_tp_kerja") portWorkPlace: RequestBody,
        @Part("pr_tipe") portType: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

    @DELETE("portofolio/{id}")
    suspend fun deletePortfolio(@Path("id") portfolioId: Int?) : GeneralResponse

    @Multipart
    @PUT("portofolio/{id}")
    suspend fun updatePortfolio(
        @Path("id") portfolioId: Int?,
        @Part("pr_aplikasi") portAppName: RequestBody,
        @Part("pr_deskripsi") portDesc: RequestBody,
        @Part("pr_link_pub") portLinkPub: RequestBody,
        @Part("pr_link_repo") portLinkRepo: RequestBody,
        @Part("pr_tp_kerja") portWorkPlace: RequestBody,
        @Part("pr_tipe") portType: RequestBody?
    ) : GeneralResponse

    @Multipart
    @PUT("portofolio/{id}")
    suspend fun updatePortfolioWithImage(
        @Path("id") portfolioId: Int?,
        @Part("pr_aplikasi") portAppName: RequestBody,
        @Part("pr_deskripsi") portDesc: RequestBody,
        @Part("pr_link_pub") portLinkPub: RequestBody,
        @Part("pr_link_repo") portLinkRepo: RequestBody,
        @Part("pr_tp_kerja") portWorkPlace: RequestBody,
        @Part("pr_tipe") portType: RequestBody?,
        @Part image: MultipartBody.Part
    ) : GeneralResponse

}