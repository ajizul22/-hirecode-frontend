package com.example.hirecodeandroid.project.detailproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentListHireApproveBinding
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentListHireApprove : Fragment() {

    private lateinit var binding: FragmentListHireApproveBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: HireApiService
    private lateinit var sharePref: SharePrefHelper
    var listHire = ArrayList<HireByProjectModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_approve,container,false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(HireApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        binding.rvListHireApprove.adapter = ListHireByProjectRecyclerViewAdapter(listHire)
        binding.rvListHireApprove.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectId = sharePref.getInteger(SharePrefHelper.PROJECT_ID_COMPANY_CLICKED)

        getListHireByProject(projectId)
    }

    private fun getListHireByProject(id: Int) {
        var mutable: MutableList<HireByProjectModel>
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getHireByProjectId(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is HireByProjectResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        HireByProjectModel(it.hireId, it.engineerId, it.projectId, it.hirePrice, it.hireStatus, it.hireDateConfirm, it.hireCreated, it.engineerName, it.engineerJobTitle, it.engineerPhoto)
                    }
                    mutable = list!!.toMutableList()
                    mutable.removeAll { it.hireStatus != "approve"}
                    (binding.rvListHireApprove.adapter as ListHireByProjectRecyclerViewAdapter).addList(mutable)
                }
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}