package com.example.hirecodeandroid.project.detailproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailProjectBinding
import com.example.hirecodeandroid.project.ProjectApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailProjectViewModel: ViewModel(), CoroutineScope {

    val isProjectLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var service: ProjectApiService

    fun setBinding(binding:ActivityDetailProjectBinding) {
        this.binding = binding
    }

    fun setService(service: ProjectApiService) {
        this.service = service
    }

    fun getDataproject(id: Int) {
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
                    val img = "http://3.80.223.103:4000/image/"
                    Glide.with(binding.ivProject)
                        .load(img + result.data[0].projectImage)
                        .placeholder(R.drawable.ic_project)
                        .error(R.drawable.ic_project)
                        .into(binding.ivProject)
                    isProjectLiveData.value = true
                } else {
                    isProjectLiveData.value = false
                }
            }

        }
    }

}