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

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: ProjectApiService
    private lateinit var binding: ActivityUpdateProjectBinding

    fun setProjectService(service: ProjectApiService) {
        this.service = service
    }

    fun setBinding(binding:ActivityUpdateProjectBinding) {
        this.binding = binding
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
                    binding.model = result.data[0]
                    val deadLine = result.data[0].projectDeadline.split("T")[0]
                    binding.etProjectDeadline.setText(deadLine)
                    val img = "http://3.80.223.103:4000/image/"
                    Glide.with(binding.ivUploadImage)
                        .load(img + result.data[0].projectImage)
                        .placeholder(R.drawable.ic_project)
                        .error(R.drawable.ic_project)
                        .into(binding.ivUploadImage)
                    isProjectLiveData.value = true
                } else {
                    isProjectLiveData.value = false
                }
            }
        }
    }

    fun updateProject(id: Int, image: MultipartBody.Part) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val projectName = createPartFromString(binding.etProjectName.text.toString())
                    val projectDeadline = createPartFromString(binding.etProjectDeadline.text.toString())
                    val projectDesc = createPartFromString(binding.etProjectDesc.text.toString())
                    service.updateProject(id, projectName, projectDesc, projectDeadline, image)
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

    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
            .toRequestBody(mediaType)
    }

}