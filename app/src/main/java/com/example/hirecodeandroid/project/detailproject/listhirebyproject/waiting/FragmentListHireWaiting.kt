package com.example.hirecodeandroid.project.detailproject.listhirebyproject.waiting

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentListHireWaitingBinding
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.HireByProjectModel
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.ListHireByProjectRecyclerViewAdapter
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentListHireWaiting: Fragment(),
    ListHireWaitingContract.View, ListHireByProjectRecyclerViewAdapter.OnListHireInProjectClickListener {

    private lateinit var binding: FragmentListHireWaitingBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: HireApiService
    private lateinit var sharePref: SharePrefHelper
    var listHire = ArrayList<HireByProjectModel>()

    private var presenter: ListHireWaitingContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_waiting,container,false)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(HireApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        presenter =
            ListHireWaitingPresenter(
                coroutineScope,
                service
            )

        binding.rvListHireWaiting.adapter =
            ListHireByProjectRecyclerViewAdapter(
                listHire, this
            )
        binding.rvListHireWaiting.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectId = sharePref.getInteger(SharePrefHelper.PROJECT_ID_COMPANY_CLICKED)
        presenter?.callHireApi(projectId)
    }

    override fun addListHire(list: MutableList<HireByProjectModel>) {
        (binding.rvListHireWaiting.adapter as ListHireByProjectRecyclerViewAdapter).addList(list)
        binding.rvListHireWaiting.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultDeleteSuccess() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        Toast.makeText(requireContext(), "Success Delete Hire", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    override fun onResultFail(message: String) {
        binding.rvListHireWaiting.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvListHireWaiting.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onHireDelete(position: Int) {
        showDialogDelete(position)
    }

    private fun showDialogDelete(position: Int) {
        val id = listHire[position].hireId
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Hire")
        builder.setMessage("Do you want to deleted this Hire?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            presenter?.deleteHire(id!!.toInt())
        }
        builder.setNegativeButton("No") { dialogInterface : DialogInterface, i : Int ->}
        builder.show()
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