package com.example.hirecodeandroid.company

import com.example.hirecodeandroid.listengineer.ListEngineerModel

interface HomeCompanyContract {

    interface View {
        fun onResultSuccess(list: List<ListEngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callListEngineerService()
    }

}