package com.example.hirecodeandroid.experience

import com.example.hirecodeandroid.company.home.HomeCompanyContract

interface ExperienceContract {

    interface View {
        fun onResultSuccess(list: List<ExperienceModel>)
        fun onResultFail(message: String)
        fun onResultSuccessDeleteExperience()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun callExperienceService(engineerId: Int)
        fun callDeleteExperienceByIdService(experienceId: Int)
    }
}