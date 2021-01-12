package com.example.hirecodeandroid.project.addproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.databinding.ActivityAddProjectBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class AddProjectViewModel: ViewModel(), CoroutineScope {

    val isAddProjectLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var service: ProjectApiService

    fun setService(service: ProjectApiService) {
        this.service = service
    }

    fun setBinding(binding: ActivityAddProjectBinding) {
        this.binding = binding
    }

    fun setSharePref(sharePref: SharePrefHelper) {
        this.sharePref = sharePref
    }



    fun callProjectApi(image: MultipartBody.Part) {
        launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    val idCompany = sharePref.getString(SharePrefHelper.COM_ID).toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etProjectDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etProjectDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    service?.addProject(idCompany, projectName, projectDesc, projectDeadline, image)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isAddProjectLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isAddProjectLiveData.value = true
                } else {
                    isAddProjectLiveData.value = false
                }

            }
        }

    }
}