package com.example.hirecodeandroid.search

import android.util.Log
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import kotlinx.coroutines.*

class SearchPresenter(private val coroutineScope: CoroutineScope,
                      private val service: EngineerApiService) : SearchContract.Presenter {

    private var view: SearchContract.View? = null

    override fun bindToView(view: SearchContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callServiceSearch(search: String?) {
            coroutineScope.launch {
                view?.showLoading()
                val result = withContext(Dispatchers.IO) {
                    try {
                        service?.getAllEngineerSearch(search = search)
                    } catch (e: Throwable) {
                        e.printStackTrace()
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