package com.example.hirecodeandroid.login

import com.google.gson.annotations.SerializedName

class GetCompanyIdResponse(val success : Boolean, val message : String, val data : Data) {
    data class Data(
        @SerializedName("ac_id") val accountId: String,
        @SerializedName("cn_id") val companyId: String,
        @SerializedName("ac_name") val accountName: String,
        @SerializedName("ac_email") val accountEmail: String
    )
}