package com.example.hirecodeandroid.project.listprojectcompany

import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.project.ProjectModel
import com.example.hirecodeandroid.project.ProjectResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProjectPresenter(private val coroutineScope: CoroutineScope,
                       private val service: ProjectApiService,
                       private val sharePref: SharePrefHelper ) :
    ProjectContract.Presenter {

    private var view: ProjectContract.View? = null

    override fun bindToView(view: ProjectContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callProjectApi() {
        coroutineScope.launch {
            view?.showLoading()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCompanyId(sharePref.getString(SharePrefHelper.COM_ID))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main){
                        view?.hideLoading()
                        when {
                            e.code() == 404 -> {
                                view?.onResultFail("You not have a project")
                            }
                            e.code() == 400 -> {
                                view?.onResultFail("Please Re-login")
                            }
                            else -> {
                                view?.onResultFail("Server Under Maintenance")
                            }
                        }
                    }
                }
            }
            if (result is ProjectResponse) {
                view?.hideLoading()
                if (result.success) {
                    val list = result.data?.map {
                        ProjectModel(
                            it.projectId,
                            it.companyId,
                            it.projectName,
                            it.projectDesc,
                            it.projectDeadline,
                            it.projectImage,
                            it.projectCreated,
                            it.projectUpdated
                        )
                    }
                    view?.addListProject(list)
                } else {
                    view?.onResultFail(result.message)
                }
            }
        }
    }
}