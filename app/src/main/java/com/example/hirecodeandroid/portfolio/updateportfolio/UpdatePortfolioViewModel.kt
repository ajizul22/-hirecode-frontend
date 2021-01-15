package com.example.hirecodeandroid.portfolio.updateportfolio


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.portfolio.PortofolioResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class UpdatePortfolioViewModel: ViewModel(), CoroutineScope {

    val listModel = MutableLiveData<List<PortofolioResponse.Portofolio>>()
    val isLiveData = MutableLiveData<Boolean>()
    val isUpdateLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortofolioApiService
    private lateinit var image: MultipartBody.Part


    fun setImage(image: MultipartBody.Part) {
        this.image = image
    }

    fun setService(service: PortofolioApiService) {
        this.service = service
    }

   fun updatePortfolio(type: Int, portId: Int, portAppName: RequestBody, portDesc: RequestBody, portLinkPub: RequestBody,
                                portLinkRepo: RequestBody, portWorkPlace: RequestBody, portoType: RequestBody) {
        launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service.updatePortfolioWithImage(portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType, image)
                    } else {
                        service.updatePortfolio(portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType)
                    }
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

    fun getDataPortfolio(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getPortfolioByIdPort(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false
                    }
                }
            }

            if (result is PortofolioResponse) {
                if (result.success) {
                    listModel.value = result.data
                    isLiveData.value = true
                }
            }
        }
    }
}