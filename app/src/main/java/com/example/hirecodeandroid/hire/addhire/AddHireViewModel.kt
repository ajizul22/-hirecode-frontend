package com.example.hirecodeandroid.hire.addhire

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.hire.HireResponse
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddHireViewModel: ViewModel(), CoroutineScope {

    val isLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: HireApiService

    fun setService(service: HireApiService) {
        this.service = service
    }

    fun callHireApi(enId: Int, pjId: Int, hirePrice: String, hireMessage: String, hireStatus: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.addHire(enId, pjId, hirePrice, hireMessage, hireStatus)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
                    }
                }
            }

            if (result is HireResponse) {
                if (result.success) {
                    isLiveData.value = true
                }
            }
        }
    }
}