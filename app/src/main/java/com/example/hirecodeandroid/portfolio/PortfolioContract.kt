package com.example.hirecodeandroid.portfolio

import com.example.hirecodeandroid.experience.ExperienceModel

interface PortfolioContract {

    interface View {
        fun onResultSuccess(list: List<PortfolioModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter{
        fun bindToView(view: View)
        fun unbind()
        fun callPortfolioService(engineerId: Int)
    }
}