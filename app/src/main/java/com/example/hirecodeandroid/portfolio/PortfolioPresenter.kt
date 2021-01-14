package com.example.hirecodeandroid.portfolio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PortfolioPresenter(private val coroutineScope: CoroutineScope,
                         private val service: PortofolioApiService): PortfolioContract.Presenter {

    private var view: PortfolioContract.View? = null

    override fun bindToView(view: PortfolioContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callPortfolioService(engineerId: Int) {
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getPortfolioEngineer(engineerId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Portfolio haven't been add!")
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

            if (result is PortofolioResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data?.map {
                        PortfolioModel(it.portoId, it.engineerId, it.portoAppName, it.portoDesc, it.portoLinkPub, it.portoLinkRepo, it.portoWorkPlace, it.portoType, it.portoImage)
                    }
                    view?.onResultSuccess(list)
                }
            }
        }
    }
}