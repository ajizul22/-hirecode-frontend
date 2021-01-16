package com.example.hirecodeandroid.company.home

import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeCompanyPresenter(private val coroutineScope: CoroutineScope,
                           private val service: EngineerApiService) : HomeCompanyContract.Presenter {

    private var view: HomeCompanyContract.View? = null

    override fun bindToView(view: HomeCompanyContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListEngineerService() {
        coroutineScope.launch {
            view?.showLoading()

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllEngineer()
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Data engineer Not Found!")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("Please re-login")
                            }
                            else -> {
                                view?.onResultFail("Server under maintenance")
                            }
                        }

                    }
                }
            }

            if (result is ListEngineerResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data.map {
                        ListEngineerModel(it.engineerId, it.accountId, it.accountName,it.accountEmail,it.accountPhone, it.engineerJobTitle, it.engineerJobType, it.engineerDomicilie, it.engineerDesc, it.engineerProfilePict,it.engineerCreated, it.engineerUpdate, it.skillEngineer)
                    }
                    val mutable = list.toMutableList()
                    mutable.removeAll { it.engineerJobTitle == null || it.engineerJobType == null || it.engineerProfilePict == null }
                    view?.onResultSuccess(mutable)
                } else {
                    view?.onResultFail(result.message)
                }
            }
        }
    }
}