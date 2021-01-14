package com.example.hirecodeandroid.util

import com.google.gson.annotations.SerializedName

data class UpdateAccountResponse(val success: Boolean, val message: String, val data: List<Data?>) {
    data class Data(
        @SerializedName("ac_id") val id: String,
        @SerializedName("ac_name") val accountName: String,
        @SerializedName("ac_email") val accountEmail: String,
        @SerializedName("ac_phone") val accountPhone: String
    )
}