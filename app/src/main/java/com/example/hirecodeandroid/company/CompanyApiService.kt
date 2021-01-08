package com.example.hirecodeandroid.company

import com.example.hirecodeandroid.util.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CompanyApiService {

    @GET("company/{id}")
    suspend fun getDataCompany(@Path("id") companyId: Int?) : CompanyResponse

    @Multipart
    @PUT("company/{id}")
    suspend fun updateCompany(
        @Path("id") companyId: Int?,
        @Part("cn_perusahaan") companyName: RequestBody,
        @Part("cn_jabatan") position: RequestBody,
        @Part("cn_bidang") companyField: RequestBody,
        @Part("cn_kota") companyCity: RequestBody,
        @Part("cn_deskripsi") companyDesc: RequestBody,
        @Part("cn_instagram") companyIg: RequestBody,
        @Part("cn_linkedin") companyLinkedIn: RequestBody,
        @Part image: MultipartBody.Part
    ) : GeneralResponse
}