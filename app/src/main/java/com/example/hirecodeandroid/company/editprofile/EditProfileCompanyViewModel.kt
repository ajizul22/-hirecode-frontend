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
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.util.UpdateAccountResponse
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
    private lateinit var sharePref: SharePrefHelper
    private lateinit var image: MultipartBody.Part


    fun setImage(image: MultipartBody.Part) {
        this.image = image
    }

    fun setSharePref(sharePref: SharePrefHelper) {
        this.sharePref = sharePref
    }

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

    fun updateDataCompany(type: Int, id: Int, companyName: RequestBody, position: RequestBody, companyField: RequestBody, city: RequestBody, desc: RequestBody,
                          instagram: RequestBody, linkedIn: RequestBody) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service?.updateCompanyWithImage(id,companyName, position, companyField, city, desc, instagram, linkedIn, image)
                    } else {
                        service?.updateCompany(id, companyName, position, companyField, city, desc, instagram, linkedIn)
                    }
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateCompanyLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                isUpdateCompanyLiveData.value = result.success
            }
        }
    }

    fun getAccountData(id: Int) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAccountData(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isCompanyLiveData.value = false
                    }
                }
            }

            if (result is UpdateAccountResponse) {
                if (result.success) {
                    binding.account = result.data[0]
                    binding.etAcPassword.setText(sharePref.getString(SharePrefHelper.KEY_PASSWORD))
                    isCompanyLiveData.value = true
                } else {
                    isCompanyLiveData.value = false
                }
            }
        }
    }

    fun updateAccount(id: Int, name: String, email: String, phone:String, password:String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.updateAccount(id, name, email, phone, password)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateCompanyLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    sharePref.put(SharePrefHelper.KEY_PASSWORD, binding.etAcPassword.text.toString())
                    isUpdateCompanyLiveData.value = true
                } else {
                    isUpdateCompanyLiveData.value = false
                }
            }
        }
    }


}