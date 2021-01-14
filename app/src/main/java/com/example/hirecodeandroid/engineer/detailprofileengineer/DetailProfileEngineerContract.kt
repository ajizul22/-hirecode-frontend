package com.example.hirecodeandroid.engineer.detailprofileengineer

import com.example.hirecodeandroid.engineer.profile.ProfileEngineerContract
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.skill.SkillModel

interface DetailProfileEngineerContract {

    interface View{
        fun onResultSuccessEngineer(data: ListEngineerResponse.Engineer)
        fun onResultSuccessSkill(list: List<SkillModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unBind()
        fun callServiceEngineer(engId: Int)
        fun callServiceSkill(engId: Int)
    }

}