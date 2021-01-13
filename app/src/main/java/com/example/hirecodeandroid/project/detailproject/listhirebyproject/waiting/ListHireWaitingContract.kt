package com.example.hirecodeandroid.project.detailproject.listhirebyproject.waiting

import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel

interface ListHireWaitingContract {

    interface View {
        fun addListHire(list: MutableList<HireByProjectModel>)
        fun onResultFail(message: String)
        fun onResultDeleteSuccess()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun callHireApi(id: Int)
        fun deleteHire(id: Int)
    }

}