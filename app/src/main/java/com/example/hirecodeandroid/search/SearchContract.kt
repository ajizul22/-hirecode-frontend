package com.example.hirecodeandroid.search

import com.example.hirecodeandroid.listengineer.ListEngineerModel

interface SearchContract {

    interface View {
        fun onResultSuccess(list: List<ListEngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callServiceSearch(search: String?)
    }

}