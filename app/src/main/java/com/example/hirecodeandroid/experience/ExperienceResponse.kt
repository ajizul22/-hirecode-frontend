package com.example.hirecodeandroid.experience

import com.google.gson.annotations.SerializedName

data class ExperienceResponse(val success: Boolean, val message: String, val data: List<Experience>) {
    data class Experience(
        @SerializedName("ex_id") val experienceId: Int?,
        @SerializedName("en_id") val engineerId: Int?,
        @SerializedName("ex_posisi") val experiencePosition: String?,
        @SerializedName("ex_perusahaan") val experienceCompany: String?,
        @SerializedName("ex_start") val experienceStart: String?,
        @SerializedName("ex_end") val experienceEnd: String?,
        @SerializedName("ex_deskripsi") val experienceDesc: String?
    )
}