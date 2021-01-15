package com.example.hirecodeandroid.engineer.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityEditProfileEngineerBinding
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.util.UpdateAccountResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class EditProfileEngineerViewModel: ViewModel(), CoroutineScope {

    val isEngineerLiveData = MutableLiveData<Boolean>()
    val isUpdateEngineerLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private lateinit var service: EngineerApiService
    private lateinit var binding: ActivityEditProfileEngineerBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var image: MultipartBody.Part

    fun setSharePref(sharePref: SharePrefHelper) {
        this.sharePref = sharePref
    }

    fun setImage(image: MultipartBody.Part) {
        this.image = image
    }

    fun setService(service: EngineerApiService) {
        this.service = service
    }

    fun setBinding(binding: ActivityEditProfileEngineerBinding) {
        this.binding = binding
    }

    fun getDataEngineer(id: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataEngById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isEngineerLiveData.value = false
                    }
                }
            }
            if (result is ListEngineerResponse) {
                if (result.success) {
                    binding.model = result.data[0]
                    val img = "http://3.80.223.103:4000/image/"
                    Glide.with(binding.root).load(img + result.data[0].engineerProfilePict).placeholder(
                        R.drawable.ic_profile)
                        .error(R.drawable.ic_profile).into(binding.ivAvatar)
                    isEngineerLiveData.value = true
                }else {
                    isEngineerLiveData.value = false
                }
            }
        }
    }

    fun updateEngineer(type: Int, id: Int, jobTitle: RequestBody, jobType: RequestBody, domicile: RequestBody, desc: RequestBody) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    if (type == 1) {
                        service?.updateEngineerWithImage(id, jobTitle, jobType, domicile, desc, image)
                    } else {
                        service?.updateEngineer(id, jobTitle, jobType, domicile, desc)
                    }

                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateEngineerLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                isUpdateEngineerLiveData.value = result.success
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
                        isEngineerLiveData.value = false
                    }
                }
            }

            if (result is UpdateAccountResponse) {
                if (result.success) {
                    binding.account = result.data[0]
                    binding.etAcPassword.setText(sharePref.getString(SharePrefHelper.KEY_PASSWORD))
                    isEngineerLiveData.value = true
                } else {
                    isEngineerLiveData.value = false
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
                        isUpdateEngineerLiveData.value = false
                    }
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    sharePref.put(SharePrefHelper.KEY_PASSWORD, binding.etAcPassword.text.toString())
                    isUpdateEngineerLiveData.value = true
                } else {
                    isUpdateEngineerLiveData.value = false
                }
            }
        }
    }


}