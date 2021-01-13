package com.example.hirecodeandroid.project.detailproject.listhirebyproject.approve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentListHireApproveBinding
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectResponse
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.ListHireByProjectRecyclerViewAdapter
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentListHireApprove : Fragment(), ListHireApproveContract.View, ListHireByProjectRecyclerViewAdapter.OnListHireInProjectClickListener {

    private lateinit var binding: FragmentListHireApproveBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: HireApiService
    private lateinit var sharePref: SharePrefHelper
    var listHire = ArrayList<HireByProjectModel>()

    private var presenter: ListHireApproveContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_approve,container,false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(HireApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        presenter = ListHireApprovePresenter(coroutineScope, service)

        binding.rvListHireApprove.adapter =
            ListHireByProjectRecyclerViewAdapter(
                listHire, this
            )
        binding.rvListHireApprove.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectId = sharePref.getInteger(SharePrefHelper.PROJECT_ID_COMPANY_CLICKED)

        presenter?.callHireApi(projectId)
    }

    override fun addListHire(list: MutableList<HireByProjectModel>) {
        (binding.rvListHireApprove.adapter as ListHireByProjectRecyclerViewAdapter).addList(list)
        binding.rvListHireApprove.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvListHireApprove.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvListHireApprove.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onHireDelete(position: Int) {
        Toast.makeText(requireContext(), "You can't delete hire because engineer has accept this project", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        val projectId = sharePref.getInteger(SharePrefHelper.PROJECT_ID_COMPANY_CLICKED)
        presenter?.bindToView(this)
        presenter?.callHireApi(projectId)
        super.onStart()
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}