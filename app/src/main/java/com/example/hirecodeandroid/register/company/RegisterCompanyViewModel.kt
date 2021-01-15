package com.example.hirecodeandroid.register.company

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.register.RegisterApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterCompanyViewModel: ViewModel(), CoroutineScope {

    val isLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: RegisterApiService

    fun setService(service: RegisterApiService) {
        this.service = service
    }

    fun callRegisterApi(name: String, email: String, companyName: String, position: String, phone:String, password:String, level: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.RegisterCompanyRequest(name,email,phone,password, level, companyName,position)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
                    }
                }
            }

            if (result is RegisterCompanyResponse) {
                if (result.success) {
                    isLiveData.value = true
                }
            }
        }
    }


}