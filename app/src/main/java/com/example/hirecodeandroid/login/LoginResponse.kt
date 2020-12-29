package com.example.hirecodeandroid.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(val success: Boolean, val message: String, val data: Data?) {
    data class Data(
        @SerializedName("ac_id") val accountId: String?,
        @SerializedName("ac_email") val accountEmail: String?,
        @SerializedName("ac_level") val accountLevel: Int?,
        val token: String?
    )
}