package com.example.hirecodeandroid.portfolio

import com.example.hirecodeandroid.project.ProjectResponse
import com.google.gson.annotations.SerializedName

data class PortofolioResponse(val success: Boolean, val message: String, val data: List<Portofolio>) {
    data class Portofolio(
        @SerializedName("pr_id") val portoId: Int?,
        @SerializedName("en_id") val engineerId: Int?,
        @SerializedName("pr_aplikasi") val portoAppName: String?,
        @SerializedName("pr_deskripsi") val portoDesc: String?,
        @SerializedName("pr_link_pub") val portoLinkPub: String?,
        @SerializedName("pr_link_repo") val portoLinkRepo: String?,
        @SerializedName("pr_tp_kerja") val portoWorkPlace: String?,
        @SerializedName("pr_tipe") val portoType: String?,
        @SerializedName("pr_gambar") val portoImage: String?
    )
}