package com.example.hirecodeandroid.company.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.company.CompanyApiService
import com.example.hirecodeandroid.company.CompanyResponse
import com.example.hirecodeandroid.databinding.FragmentProfileCompanyBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileCompanyViewModel: ViewModel(), CoroutineScope {

    val isCompanyLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: CompanyApiService
    private lateinit var binding: FragmentProfileCompanyBinding


    fun setCompanyService(service: CompanyApiService) {
        this.service = service
    }

    fun setBinding(binding: FragmentProfileCompanyBinding) {
        this.binding = binding
    }

    fun callCompanyApi(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataCompany(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isCompanyLiveData.value = false
                    }
                }
            }

            if (result is CompanyResponse) {
                if (result.success) {
                    binding.model = result.data[0]
                    val img = "http://3.80.223.103:4000/image/"
                    Glide.with(binding.root).load(img + result.data[0].companyPhotoProfile).placeholder(
                        R.drawable.ic_profile)
                        .error(R.drawable.ic_profile).into(binding.ivAvatar)
                    isCompanyLiveData.value = true
                } else {
                    isCompanyLiveData.value = false
                }

            }
        }
    }
}