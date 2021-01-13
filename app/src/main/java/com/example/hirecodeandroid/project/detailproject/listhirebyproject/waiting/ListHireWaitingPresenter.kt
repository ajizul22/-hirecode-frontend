package com.example.hirecodeandroid.project.detailproject.listhirebyproject.waiting

import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListHireWaitingPresenter(private var coroutineScope: CoroutineScope,
                               private var service: HireApiService) : ListHireWaitingContract.Presenter {

    private var view: ListHireWaitingContract.View? = null

    override fun bindToView(view: ListHireWaitingContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callHireApi(id: Int) {
        var mutable: MutableList<HireByProjectModel>
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getHireByProjectId(id)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("You haven't hired engineer")
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

            if (result is HireByProjectResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data?.map {
                        HireByProjectModel(
                            it.hireId,
                            it.engineerId,
                            it.projectId,
                            it.hirePrice,
                            it.hireStatus,
                            it.hireDateConfirm,
                            it.hireCreated,
                            it.engineerName,
                            it.engineerJobTitle,
                            it.engineerPhoto
                        )
                    }
                    mutable = list!!.toMutableList()
                    mutable.removeAll { it.hireStatus != "wait"}
                    view?.addListHire(mutable)
                } else {
                    view?.onResultFail(result.message)
                }
            }
        }
    }

    override fun deleteHire(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deleteHire(id)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("You haven't hired engineer")
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

            if (result is GeneralResponse) {
                if (result.success) {
                    view?.onResultDeleteSuccess()
                }
            }
        }
    }
}