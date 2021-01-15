package com.example.hirecodeandroid.portfolio.detailportfolio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.portfolio.PortofolioResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailPortfolioViewModel: ViewModel(), CoroutineScope {

    val listModel = MutableLiveData<List<PortofolioResponse.Portofolio>>()
    val isLiveData = MutableLiveData<Boolean>()
    val isDeleteLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: PortofolioApiService

    fun setService(service: PortofolioApiService) {
        this.service = service
    }

    fun getDataPortfolio(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getPortfolioByIdPort(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLiveData.value = false
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

    fun deletePortfolio(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deletePortfolio(id)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isDeleteLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isDeleteLiveData.value = true
                }
            }
        }
    }
}