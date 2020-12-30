package com.example.hirecodeandroid.company

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
import com.example.hirecodeandroid.adapter.HomeRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentHomeBinding
import com.example.hirecodeandroid.dataclass.ListEngineerDataClass
import com.example.hirecodeandroid.DetailProfileEngineerActivity
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerAdapter
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*

class FragmentHomeCompany : Fragment(), ListEngineerAdapter.OnListEngineerClickListener {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    var listEngineer = ArrayList<ListEngineerModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
//        getListEngineer()
//        binding.rvHome.adapter = HomeRecyclerViewAdapter(engineerModel, this)
        getAllEngineer()
        binding.rvHome.adapter = ListEngineerAdapter(listEngineer,this)
        binding.rvHome.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        return binding.root
    }

    fun getAllEngineer() {
        coroutineScope.launch {
            Log.d("listengineer", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("listengineer", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is ListEngineerResponse) {
                val list = result.data.map {
                    ListEngineerModel(it.engineerId, it.accountId, it.accountName, it.engineerJobTitle, it.engineerJobType, it.engineerDomicilie, it.engineerProfilePict, it.skillEngineer)
                }
                (binding.rvHome.adapter as ListEngineerAdapter).addList(list)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onEngineerItemClicked(position: Int) {
        val intent = Intent(requireContext(), DetailProfileEngineerActivity::class.java)
        intent.putExtra("name", listEngineer[position].accountName)
        intent.putExtra("jobTitle", listEngineer[position].engineerJobTitle)
        intent.putExtra("jobType", listEngineer[position].engineerJobType)
        intent.putExtra("image", listEngineer[position].engineerProfilePict)
        intent.putExtra("location", listEngineer[position].engineerDomicilie)

        startActivity(intent)
    }
}