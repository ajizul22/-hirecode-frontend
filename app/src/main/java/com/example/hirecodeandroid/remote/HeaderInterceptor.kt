package com.example.hirecodeandroid.remote

import android.content.Context
import com.example.hirecodeandroid.util.SharePrefHelper
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context: Context): Interceptor {

    private lateinit var sharePref: SharePrefHelper

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        sharePref = SharePrefHelper(context)
        val token = sharePref.getString(SharePrefHelper.TOKEN)
        val tokenAuth = token
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}