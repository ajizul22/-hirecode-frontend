package com.example.hirecodeandroid.company.editprofile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.company.CompanyApiService
import com.example.hirecodeandroid.company.CompanyResponse
import com.example.hirecodeandroid.databinding.ActivityEditProfileCompanyBinding
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class EditProfileCompanyViewModel: ViewModel(), CoroutineScope {

    val isCompanyLiveData = MutableLiveData<Boolean>()
    val isUpdateCompanyLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: CompanyApiService
    private lateinit var binding: ActivityEditProfileCompanyBinding

    fun setCompanyService(service: CompanyApiService) {
        this.service = service
    }

    fun setBinding(binding: ActivityEditProfileCompanyBinding) {
        this.binding = binding
    }

    fun getDataCompany(id: Int) {
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

    fun updateDataCompany(id: Int, image: MultipartBody.Part) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val companyName = createPartFromString(binding.etCompanyName.text.toString())
                    val companyField = createPartFromString(binding.etCompanyField.text.toString())
                    val position = createPartFromString(binding.etPosition.text.toString())
                    val city = createPartFromString(binding.etCity.text.toString())
                    val instagram = createPartFromString(binding.etIg.text.toString())
                    val linkedIn = createPartFromString(binding.etLinkedin.text.toString())
                    val desc = createPartFromString(binding.etShortDesc.text.toString())
                    service?.updateCompany(id,companyName, position, companyField, city, desc, instagram, linkedIn, image)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateCompanyLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    isUpdateCompanyLiveData.value = true
                } else {
                    isUpdateCompanyLiveData.value = false
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