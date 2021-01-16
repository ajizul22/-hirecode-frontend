package com.example.hirecodeandroid.engineer.detailprofileengineer

import android.util.Log
import com.example.hirecodeandroid.engineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.skill.SkillModel
import com.example.hirecodeandroid.skill.SkillResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DetailProfileEngineerPresenter(private val coroutineScope: CoroutineScope,
                                     private val service: EngineerApiService,
                                     private val serviceSkill: SkillApiService) : DetailProfileEngineerContract.Presenter {

    private var view: DetailProfileEngineerContract.View? = null

    override fun bindToView(view: DetailProfileEngineerContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callServiceEngineer(engId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataEngById(engId.toString())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ListEngineerResponse){
                view?.hideLoading()
                if (result.success) {
                    val data = result.data[0]

                    view?.onResultSuccessEngineer(data)
                }
            }
        }
    }

    override fun callServiceSkill(engId: Int) {
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    serviceSkill?.getSkillByEngineer(engId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()

                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("Engineer has not been added skill")
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
            if (result is SkillResponse) {
                view?.hideLoading()
                Log.d("dataskill", result.toString())
                if(result.success) {
                    val list = result.data.map {
                        SkillModel(it.skillId, it.engineerId, it.skillName)
                    }
                    view?.onResultSuccessSkill(list)
                }
            }
        }
    }
}