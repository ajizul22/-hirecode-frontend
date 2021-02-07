package com.example.hirecodeandroid.engineer.home

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
import com.example.hirecodeandroid.company.home.HomeCompanyContract
import com.example.hirecodeandroid.company.home.HomeCompanyPresenter
import com.example.hirecodeandroid.databinding.FragmentHomeEngineerBinding
import com.example.hirecodeandroid.engineer.EngineerApiService
import com.example.hirecodeandroid.engineer.detailprofileengineer.DetailProfileEngineerActivity
import com.example.hirecodeandroid.listengineer.ListEngineerAdapter
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class FragmentHomeEngineer: Fragment(), ListEngineerAdapter.OnListEngineerClickListener,
    HomeCompanyContract.View {

    private lateinit var binding: FragmentHomeEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    var listEngineer = ArrayList<ListEngineerModel>()
    private lateinit var sharePref: SharePrefHelper

    private var presenter: HomeCompanyContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_engineer,container,false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        sharePref = SharePrefHelper(requireContext())

        presenter =
            HomeCompanyPresenter(
                coroutineScope,
                service
            )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.callListEngineerService()
        binding.rvHome.adapter = ListEngineerAdapter(listEngineer,this)
        binding.rvHome.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onResultSuccess(list: List<ListEngineerModel>) {
        (binding.rvHome.adapter as ListEngineerAdapter).addList(list)
        binding.rvHome.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvHome.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvHome.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onEngineerItemClicked(position: Int) {
        val intent = Intent(requireContext(), DetailProfileEngineerActivity::class.java)

        sharePref.put(SharePrefHelper.ENG_ID_CLICKED, listEngineer[position].engineerId!!)
        startActivity(intent)
    }

    override fun onStart() {
        presenter?.bindToView(this)
        presenter?.callListEngineerService()
        super.onStart()
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }




}