package com.example.hirecodeandroid.engineer.profile

import android.util.Log
import com.example.hirecodeandroid.engineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.skill.SkillModel
import com.example.hirecodeandroid.skill.SkillResponse
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProfileEngineerPresenter(private val coroutineScope: CoroutineScope,
                               private val service: EngineerApiService,
                               private val serviceSkill: SkillApiService) : ProfileEngineerContract.Presenter {

    private var view: ProfileEngineerContract.View? = null

    override fun bindToView(view: ProfileEngineerContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callServiceEngineer(engId: Int) {
        coroutineScope.launch {
            view?.showLoading()

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
                                view?.onResultFail("You haven't added skill yet")
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

    override fun callServiceDeleteSkill(skillId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    serviceSkill?.deleteSkill(skillId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    view?.onResultSuccessDeleteSkill()
                }
            }
        }
    }
}