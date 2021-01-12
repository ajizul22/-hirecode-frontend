package com.example.hirecodeandroid.project.detailproject.listhirebyproject.approve

import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListHireApprovePresenter(private var coroutineScope: CoroutineScope,
                               private var service: HireApiService) : ListHireApproveContract.Presenter {

    private var view: ListHireApproveContract.View? = null

    override fun bindToView(view: ListHireApproveContract.View) {
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
                    mutable.removeAll { it.hireStatus != "approve"}
                    view?.addListHire(mutable)
                } else {
                    view?.onResultFail(result.message)
                }
            }
        }
    }
}