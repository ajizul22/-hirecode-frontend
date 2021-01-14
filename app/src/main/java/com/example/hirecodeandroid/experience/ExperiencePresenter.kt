package com.example.hirecodeandroid.experience

import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ExperiencePresenter(private val coroutineScope: CoroutineScope,
                          private val service: ExperienceApiService): ExperienceContract.Presenter {

    private var view: ExperienceContract.View? = null

    override fun bindToView(view: ExperienceContract.View) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callExperienceService(engineerId: Int) {
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getExpByIdEng(engineerId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Experience haven't been add!")
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

            if (result is ExperienceResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data?.map {
                        ExperienceModel(it.experienceId, it.engineerId, it.experiencePosition, it.experienceCompany, it.experienceStart,it.experienceEnd,it.experienceDesc)
                    }
                    view?.onResultSuccess(list)
                } else {
                    view?.onResultFail(result.message)
                }
            }
        }
    }

    override fun callDeleteExperienceByIdService(experienceId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deleteExperience(experienceId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    view?.onResultSuccessDeleteExperience()
                }
            }
        }
    }
}