package com.example.hirecodeandroid.experience.updateexperience

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.experience.ExperienceApiService
import com.example.hirecodeandroid.experience.ExperienceResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UpdateExperienceViewModel: ViewModel(), CoroutineScope {

    val listModel = MutableLiveData<List<ExperienceResponse.Experience>>()
    val isLiveData = MutableLiveData<Boolean>()
    val isUpdateLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ExperienceApiService

    fun setService(service: ExperienceApiService) {
        this.service = service
    }

    fun getDataExperience(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getExperienceById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false
                    }
                }
            }

            if (result is ExperienceResponse) {
                if (result.success) {
                    listModel.value = result.data
                    isLiveData.value = true
                }
            }
        }
    }

    fun updateExperience(id: Int, expPosition: String, expCompany: String, expStart: String, expEnd: String, expDesc: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateExperience(id, expPosition, expCompany, expStart, expEnd, expDesc)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false
                    }
                }
            }
            if (result is GeneralResponse) {
                isUpdateLiveData.value = result.success
            }
        }
    }


}