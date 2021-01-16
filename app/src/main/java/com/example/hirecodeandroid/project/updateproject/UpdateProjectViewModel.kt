package com.example.hirecodeandroid.project.updateproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityUpdateProjectBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.project.ProjectResponse
import com.example.hirecodeandroid.project.detailproject.DetailProjectResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class UpdateProjectViewModel: ViewModel(), CoroutineScope {

    val isProjectLiveData = MutableLiveData<Boolean>()
    val isUpdateLiveData = MutableLiveData<Boolean>()
    val listDataModel = MutableLiveData<List<DetailProjectResponse.Project>>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ProjectApiService
    private lateinit var image: MultipartBody.Part

    fun setImage(image: MultipartBody.Part) {
        this.image = image
    }

    fun setProjectService(service: ProjectApiService) {
        this.service = service
    }

    fun getDataProject(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByProjectId(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isProjectLiveData.value = false
                    }
                }
            }
            if (result is DetailProjectResponse) {
                if (result.success) {
                    listDataModel.value = result.data
                    isProjectLiveData.value = true
                } else {
                    isProjectLiveData.value = false
                }
            }
        }
    }

    fun updateProject(type: Int, id: Int, projectName: RequestBody, projectDesc: RequestBody, projectDeadline: RequestBody ) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service.updateProjectWithImage(id, projectName, projectDesc, projectDeadline, image)
                    } else {
                        service.updateProject(id, projectName, projectDesc, projectDeadline)
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isUpdateLiveData.value = true
                } else {
                    isUpdateLiveData.value = false
                }
            }
        }
    }

}