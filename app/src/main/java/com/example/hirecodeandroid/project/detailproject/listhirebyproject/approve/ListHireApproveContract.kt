package com.example.hirecodeandroid.project.detailproject.listhirebyproject.approve

import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel

interface ListHireApproveContract {

    interface View {
        fun addListHire(list: MutableList<HireByProjectModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun callHireApi(id: Int)
    }

}