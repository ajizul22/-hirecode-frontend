package com.example.hirecodeandroid.register

import com.google.gson.annotations.SerializedName

data class RegisterCompanyResponse(val success: Boolean, val message: String, val data: Data?) {
    data class Data(
        @SerializedName("id") val id: String,
        @SerializedName("ac_name") val name: String,
        @SerializedName("ac_email") val email: String,
        @SerializedName("ac_phone") val phone: String,
        @SerializedName("ac_level") val level: Int,
        @SerializedName("ac_created_at") val accountCreated: String
    )
}