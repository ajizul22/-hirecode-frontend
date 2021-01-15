package com.example.hirecodeandroid.skill

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddSkillViewModel: ViewModel(), CoroutineScope {

    val isLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: SkillApiService

    fun setService(service: SkillApiService) {
        this.service = service
    }

    fun addSkill(id: Int, skillName: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.createSkill(id, skillName)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isLiveData.value = true
                }
            }
        }
    }

}