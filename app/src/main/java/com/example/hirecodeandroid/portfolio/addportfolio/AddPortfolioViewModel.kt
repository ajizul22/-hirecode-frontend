package com.example.hirecodeandroid.portfolio.addportfolio

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class AddPortfolioViewModel: ViewModel(), CoroutineScope {

    val isAddPortfolioLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortofolioApiService

    fun setService(service: PortofolioApiService) {
        this.service = service
    }

    fun callPortfolioApi(engineerId: RequestBody,portAppName: RequestBody, portDesc: RequestBody, portLinkPub: RequestBody,
                                 portLinkRepo: RequestBody, portWorkPlace: RequestBody, portoType: RequestBody, image: MultipartBody.Part) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.addPortfolio(engineerId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType, image)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isAddPortfolioLiveData.value = false
                    }
                }
            }
            if (result is GeneralResponse) {
                if (result.success) {
                    isAddPortfolioLiveData.value = true
                }
            }
        }
    }
}