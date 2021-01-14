package com.example.hirecodeandroid.company

import com.example.hirecodeandroid.hire.HireResponse
import com.google.gson.annotations.SerializedName

data class CompanyResponse(val success: Boolean, val message: String, val data: List<Company>) {
    data class Company(
        @SerializedName("cn_id") val companyId: Int?,
        @SerializedName("ac_id") val accountId: Int?,
        @SerializedName("cn_perusahaan") val companyName: String?,
        @SerializedName("ac_email") val accountEmail: String?,
        @SerializedName("cn_jabatan") val companyPosition: String?,
        @SerializedName("cn_bidang") val companyField: String?,
        @SerializedName("cn_kota") val companyDom: String?,
        @SerializedName("cn_deskripsi") val companyDesc: String?,
        @SerializedName("cn_instagram") val companyIg: String?,
        @SerializedName("cn_linkedin") val companyLinkedin: String?,
        @SerializedName("cn_ft_profil") val companyPhotoProfile: String?,
        @SerializedName("cn_created_at") val companyCreated: String?,
        @SerializedName("cn_updated_at") val companyUpdated: String?
    )
}