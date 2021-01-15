package com.example.hirecodeandroid.hire

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ListHireEngineerViewModel: ViewModel(), CoroutineScope {

    var isLiveData = MutableLiveData<Boolean>()
    var listHire = MutableLiveData<List<HireModel>>()
    var resultFail = MutableLiveData<Boolean>()
    var isUpdateLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: HireApiService

    fun setService(service: HireApiService){
        this.service = service
    }

    fun getListHire(id: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getHireByEngineerId(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
                        resultFail.value = true
                    }
                }
            }
            if (result is HireResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        HireModel(it.hireId, it.engineerId, it.projectId, it.hirePrice, it.hireMessage, it.hireStatus,it.hireDateConfirm, it.hireCreated, it.companyName, it.projectName, it.projectDeadline)
                    }
                    listHire.value = list
                    isLiveData.value = true
                } else {
                    isLiveData.value = false
                    resultFail.value = true
                }
            }
        }
    }

    fun updateHireStatus(id: String, status: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.responseHire(id, status)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isUpdateLiveData.value = true
                }
            }
        }
    }
}