package com.example.hirecodeandroid.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProjectCompanyBinding
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*

class FragmentProjectCompany: Fragment() {

    private lateinit var binding: FragmentProjectCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ProjectApiService::class.java)

        binding.rvProject.adapter = ProjectListAdapter()
        binding.rvProject.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllProject()
    }

    fun getAllProject() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getAllProject()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is ProjectResponse) {
                Log.d("android2", result.toString())
                val list = result.data?.map {
                    ProjectModel(it.projectId,it.companyId,it.projectName,it.projectDesc,it.projectDeadline,it.projectImage,it.projectCreated,it.projectUpdated)
                }
                (binding.rvProject.adapter as ProjectListAdapter).addList(list)
            }

        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

//    override fun onItemClick(item: ListProjectEngineerData, position: Int) {
//        val intent = Intent(requireContext(), DetailProjectActivity::class.java)
//        intent.putExtra("image", projectModel[position].imageProject)
//        intent.putExtra("title", projectModel[position].title)
//        intent.putExtra("company", projectModel[position].company)
//        intent.putExtra("deadline", projectModel[position].deadline)
//        startActivity(intent)
//    }

}