package com.example.hirecodeandroid.engineer.profile

import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.skill.SkillModel

interface ProfileEngineerContract {

    interface View{
        fun onResultSuccessEngineer(data: ListEngineerResponse.Engineer)
        fun onResultSuccessSkill(list: List<SkillModel>)
        fun onResultSuccessDeleteSkill()
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unBind()
        fun callServiceEngineer(engId: Int)
        fun callServiceSkill(engId: Int)
        fun callServiceDeleteSkill(skillId: Int)
    }

}