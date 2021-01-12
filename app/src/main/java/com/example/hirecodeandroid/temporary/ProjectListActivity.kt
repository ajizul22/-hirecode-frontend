package com.example.hirecodeandroid.temporary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityProjectListBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.project.ProjectModel
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*

class ProjectListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectListBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    val listProject = ArrayList<ProjectModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_list)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)

//        binding.recyclerView.adapter =
//            ProjectListAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


//        getAllProject()
    }

//    fun getAllProject() {
//        coroutineScope.launch {
//            Log.d("android2", "Start: ${Thread.currentThread().name}")
//
//            val result = withContext(Dispatchers.IO) {
//                Log.d("android2", "CallApi: ${Thread.currentThread().name}")
//                try {
//                    service?.getAllProject()
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            if (result is ProjectResponse) {
//                Log.d("android2", result.toString())
//                val list = result.data?.map {
//                    ProjectModel(
//                        it.projectId,
//                        it.companyId,
//                        it.projectName,
//                        it.projectDesc,
//                        it.projectDeadline,
//                        it.projectImage,
//                        it.projectCreated,
//                        it.projectUpdated
//                    )
//                }
//                (binding.recyclerView.adapter as ProjectListAdapter).addList(list)
//            }
//
//        }
//    }
//
//    override fun onDestroy() {
//        coroutineScope.cancel()
//        super.onDestroy()
//    }
}