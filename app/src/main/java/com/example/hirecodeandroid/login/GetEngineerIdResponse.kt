package com.example.hirecodeandroid.login

import com.google.gson.annotations.SerializedName

data class GetEngineerIdResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(
        @SerializedName("en_id") val engineerId: String,
        @SerializedName("ac_id") val accountId: String,
        @SerializedName("ac_name") val accountName: String,
        @SerializedName("ac_email") val accountEmail: String
    )
}