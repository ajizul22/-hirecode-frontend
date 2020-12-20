package com.example.hirecodeandroid.webview

interface WebViewListener {

    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverrideUrl(redirectUrl: String)
    fun onProgressChange(progress: Int)

}