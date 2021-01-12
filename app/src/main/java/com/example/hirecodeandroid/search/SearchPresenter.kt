package com.example.hirecodeandroid.search

import android.util.Log
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import retrofit2.HttpException

class SearchPresenter(private val coroutineScope: CoroutineScope,
                      private val service: EngineerApiService) : SearchContract.Presenter {

    private var view: SearchContract.View? = null

    override fun bindToView(view: SearchContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callServiceSearch(search: String?, filter: Int?) {
            coroutineScope.launch {
                view?.showLoading()
                val result = withContext(Dispatchers.IO) {
                    try {
                        service?.getAllEngineerSearch(search = search, filter = filter)
                    } catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            view?.hideLoading()

                            when {
                                e.code() == 404 -> {
                                    view?.onResultFail("Engineer Not Found!")
                                }
                                e.code() == 400 -> {
                                    view?.onResultFail("Please Re-login")
                                }
                                else -> {
                                    view?.onResultFail("Server Under Maintenance")
                                }
                            }
                        }
                    }
                }
                if (result is ListEngineerResponse) {
                    view?.hideLoading()
                    if (result.success) {
                        val list = result.data.map {
                            ListEngineerModel(it.engineerId, it.accountId, it.accountName,it.accountEmail,it.accountPhone, it.engineerJobTitle, it.engineerJobType,
                                it.engineerDomicilie, it.engineerDesc, it.engineerProfilePict,it.engineerCreated, it.engineerUpdate, it.skillEngineer)
                        }
                        view?.onResultSuccess(list)
                    } else {
                        view?.onResultFail(result.message)
                    }
                }
            }

    }

}