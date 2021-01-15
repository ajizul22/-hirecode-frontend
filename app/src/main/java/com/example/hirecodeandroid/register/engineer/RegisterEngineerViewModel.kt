package com.example.hirecodeandroid.register.engineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.register.RegisterApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterEngineerViewModel: ViewModel(), CoroutineScope {

    val isLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: RegisterApiService

    fun setService(service: RegisterApiService) {
        this.service = service
    }

    fun callRegisterApi(name:String, email:String, noHp: String, password:String, level: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.RegisterEngineerRequest(name, email, noHp, password, level)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
                    }
                }
            }

            if (result is RegisterEngineerResponse) {
                if (result.success) {
                    isLiveData.value = true
                }
            }
        }
    }
}