package com.example.hirecodeandroid.project.detailproject

import com.google.gson.annotations.SerializedName

data class DetailProjectResponse(val success: Boolean, val message: String, val data: List<Project>) {
    data class Project(@SerializedName("pj_id") val projectId: Int,
                       @SerializedName("cn_id") val companyId: String,
                       @SerializedName("pj_nama_project") val projectName: String,
                       @SerializedName("pj_deskripsi") val projectDesc: String,
                       @SerializedName("pj_deadline") val projectDeadline: String,
                       @SerializedName("pj_gambar") val projectImage: String,
                       @SerializedName("cn_perusahaan") val companyName: String,
                       @SerializedName("pj_created_at") val projectCreated: String,
                       @SerializedName("pj_updated_at") val projectUpdated: String)
}