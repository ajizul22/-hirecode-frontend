package com.example.hirecodeandroid.hire

import android.content.Intent
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
import com.example.hirecodeandroid.databinding.FragmentHireEngineerBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentHireEngineer: Fragment(), HireListAdapter.OnListHireClickListener {

    private lateinit var binding: FragmentHireEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: HireApiService
    private lateinit var sharePref: SharePrefHelper
    var listHire = ArrayList<HireModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hire_engineer,container,false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(HireApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        binding.rvHire.adapter = HireListAdapter(listHire, this)
        binding.rvHire.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListHire()
    }

    private fun getListHire() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getHireByEngineerId(sharePref.getString(SharePrefHelper.ENG_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is HireResponse) {
                Log.d("hire list", result.toString())
                val list = result.data?.map {
                    HireModel(it.hireId, it.engineerId, it.projectId, it.hirePrice, it.hireMessage, it.hireStatus,it.hireDateConfirm, it.hireCreated, it.companyName, it.projectName, it.projectDeadline)
                }
                (binding.rvHire.adapter as HireListAdapter).addList(list)
            }
        }
    }

    override fun onHireRejectClicked(position: Int) {
        val hireId = listHire[position].hireId
        updateHireStatus(hireId!!, "reject")
    }

    override fun onHireApproveClicked(position: Int) {
        val hireId = listHire[position].hireId
        updateHireStatus(hireId!!, "approve")
    }

    private fun updateHireStatus(id: String, status: String) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.responseHire(id, status)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}