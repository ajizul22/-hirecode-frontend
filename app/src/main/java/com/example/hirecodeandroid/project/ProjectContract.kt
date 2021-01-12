package com.example.hirecodeandroid.project

interface ProjectContract {

    interface View {
        fun addListProject(list: List<ProjectModel>)
//        fun showProgressBar()
//        fun hideProgressBar()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unBind()
        fun callProjectApi()
    }
}