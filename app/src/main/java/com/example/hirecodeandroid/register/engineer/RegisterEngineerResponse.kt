package com.example.hirecodeandroid.register.engineer

import com.google.gson.annotations.SerializedName

data class RegisterEngineerResponse(val success: Boolean, val message: String, val data: Data?) {
    data class Data(
        @SerializedName("id") val id: String,
        @SerializedName("ac_name") val accountName: String,
        @SerializedName("ac_email") val accountEmail: String,
        @SerializedName("ac_phone") val accountPhone: String,
        @SerializedName("ac_level") val accountLevel: String,
        @SerializedName("ac_created_at") val accountCreated: String
    )
}