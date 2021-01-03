package com.example.hirecodeandroid.project

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentProjectCompany: Fragment(), ProjectListAdapter.OnListProjectClickListener {

    private lateinit var binding: FragmentProjectCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var sharePref: SharePrefHelper
    var listProject = ArrayList<ProjectModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ProjectApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        binding.rvProject.adapter = ProjectListAdapter(listProject, this)
        binding.rvProject.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.btnAddProject.setOnClickListener {
            val intent = Intent(requireContext(), AddProjectActivity:: class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProjectByCompanyId()
    }

    fun getProjectByCompanyId() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCompanyId(sharePref.getString(SharePrefHelper.COM_ID))
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

    override fun onProjectItemClicked(position: Int) {
        val intent = Intent(requireContext(),DetailProjectActivity::class.java)
        intent.putExtra("image", listProject[position].projectImage)
        intent.putExtra("title", listProject[position].projectName)
        intent.putExtra("company", listProject[position].companyId)
        intent.putExtra("desc", listProject[position].projectDesc)
        intent.putExtra("deadline", listProject[position].projectDeadline)

        startActivity(intent)
    }
}