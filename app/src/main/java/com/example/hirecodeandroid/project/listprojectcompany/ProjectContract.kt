package com.example.hirecodeandroid.project.listprojectcompany

import com.example.hirecodeandroid.project.ProjectModel

interface ProjectContract {

    interface View {
        fun addListProject(list: List<ProjectModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun callProjectApi()
    }
}