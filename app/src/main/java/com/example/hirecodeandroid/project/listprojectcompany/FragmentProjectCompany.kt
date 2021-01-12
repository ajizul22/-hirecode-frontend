package com.example.hirecodeandroid.project.listprojectcompany

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProjectCompanyBinding
import com.example.hirecodeandroid.project.*
import com.example.hirecodeandroid.project.addproject.AddProjectActivity
import com.example.hirecodeandroid.project.detailproject.DetailProjectActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentProjectCompany: Fragment(),
    ProjectListAdapter.OnListProjectClickListener,
    ProjectContract.View {

    private lateinit var binding: FragmentProjectCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var sharePref: SharePrefHelper
    private var listProject = ArrayList<ProjectModel>()
    private var presenter: ProjectPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ProjectApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        binding.rvProject.adapter =
            ProjectListAdapter(
                listProject,
                this
            )
        binding.rvProject.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        presenter =
            ProjectPresenter(
                coroutineScope,
                service,
                sharePref
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddProject.setOnClickListener {
            val intent = Intent(requireContext(), AddProjectActivity:: class.java)
            startActivity(intent)
        }
    }

    override fun addListProject(list: List<ProjectModel>) {
        (binding.rvProject.adapter as ProjectListAdapter).addList(list)
        binding.rvProject.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
        binding.rvProject.visibility = View.GONE
    }

    override fun showLoading() {
        binding.rvProject.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onProjectItemClicked(position: Int) {
        val intent = Intent(requireContext(),
            DetailProjectActivity::class.java)
        intent.putExtra("project_id", listProject[position].projectId)
        intent.putExtra("image", listProject[position].projectImage)
        sharePref.put(SharePrefHelper.PROJECT_ID_COMPANY_CLICKED, listProject[position].projectId!!)

        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
        presenter?.callProjectApi()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }


}