package com.example.hirecodeandroid.project

import android.util.Log
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectPresenter(private val coroutineScope: CoroutineScope,
                       private val service: ProjectApiService,
                       private val sharePref: SharePrefHelper ) : ProjectContract.Presenter {

    private var view: ProjectContract.View? = null

    override fun bindToView(view: ProjectContract.View) {
        this.view = view
    }

    override fun unBind() {
        this.view = null
    }

    override fun callProjectApi() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCompanyId(sharePref.getString(SharePrefHelper.COM_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is ProjectResponse) {
                val list = result.data?.map {
                    ProjectModel(it.projectId,it.companyId,it.projectName,it.projectDesc,it.projectDeadline,it.projectImage,it.projectCreated,it.projectUpdated)
                }
                view?.addListProject(list)
            }

        }
    }
}