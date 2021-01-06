package com.example.hirecodeandroid.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {

    val isLoginLiveData = MutableLiveData<Boolean>()
    val isGetEngineerId = MutableLiveData<Boolean>()
    val isGetCompanyId = MutableLiveData<Boolean>()

    private lateinit var service: LoginApiService
    private lateinit var sharePref: SharePrefHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPreferences(sharePref: SharePrefHelper) {
        this.sharePref = sharePref
    }

    fun setLoginService(service: LoginApiService) {
        this.service = service
    }

    fun callLoginApi(email: String, password: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLoginLiveData.value = false
                    }
                }
            }

            if (result is LoginResponse) {
                if (result.success) {
                    sharePref.put(SharePrefHelper.AC_LEVEL, result.data?.accountLevel!!)
                    sharePref.put(SharePrefHelper.KEY_LOGIN, true)
                    sharePref.put(SharePrefHelper.TOKEN, result.data.token!!)
                    sharePref.put(SharePrefHelper.KEY_EMAIL, result.data.accountEmail!!)
                    sharePref.put(SharePrefHelper.AC_ID, result.data.accountId!!)

                    isLoginLiveData.value = true

                    if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                        getEngineerId()
                    } else {
                        getCompanyId()
                    }
                } else {
                    isLoginLiveData.value = false
                }
            }
        }
    }

    private fun getEngineerId() {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerIdByAccountId(sharePref.getString(SharePrefHelper.AC_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isGetEngineerId.value = false
                    }
                }
            }

            if (result is GetEngineerIdResponse) {
                if (result.success) {
                    sharePref.put(SharePrefHelper.ENG_ID, result.data.engineerId)
                    sharePref.put(SharePrefHelper.ENG_NAME, result.data.accountName)

                    isGetEngineerId.value = true
                } else {
                    isGetEngineerId.value = false
                }
            }
        }
    }

    private fun getCompanyId() {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getCompanyIdByAccountId(sharePref.getString(SharePrefHelper.AC_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isGetCompanyId.value = false
                    }
                }
            }

            if (result is GetCompanyIdResponse) {
                if (result.success) {
                    sharePref.put(SharePrefHelper.COM_ID, result.data.companyId)
                    isGetCompanyId.value = true
                } else {
                   isGetCompanyId.value = false
                }
            }
        }
    }


}