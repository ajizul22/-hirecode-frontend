package com.example.hirecodeandroid.experience.addexperience


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.experience.ExperienceApiService
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddExperienceViewModel: ViewModel(), CoroutineScope {

    val isAddExperienceLiveData = MutableLiveData<Boolean>()

    private lateinit var service: ExperienceApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service: ExperienceApiService){
        this.service = service
    }

   fun callExperienceApi(enId: Int, expPosition: String, expCompany: String, expStart: String, expEnd: String, expDesc: String) {
       launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.addExperience(enId,expPosition,expCompany,expStart,expEnd,expDesc)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isAddExperienceLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isAddExperienceLiveData.value = true
                }
            }
        }
    }
}