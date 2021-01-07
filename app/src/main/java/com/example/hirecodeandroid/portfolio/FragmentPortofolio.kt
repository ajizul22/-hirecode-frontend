package com.example.hirecodeandroid.portfolio

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
import com.example.hirecodeandroid.databinding.FragmentPortofolioBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import retrofit2.create

class FragmentPortofolio : Fragment() {

    private lateinit var binding : FragmentPortofolioBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    var listPortfolio = ArrayList<PortfolioModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portofolio, container, false)
        sharePref = SharePrefHelper(requireContext())

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(PortofolioApiService::class.java)


        binding.rvPortofolio.adapter =
            PortofolioRecyclerViewAdapter(listPortfolio)
        binding.rvPortofolio.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            binding.btnAddPortfolio.visibility = View.GONE
        }

        binding.btnAddPortfolio.setOnClickListener {
            val intent = Intent(requireContext(), AddPortfolioActivity::class.java)
            startActivity(intent)
        }

        val id = sharePref.getString(SharePrefHelper.ENG_ID)
        val idClick = sharePref.getString(SharePrefHelper.ENG_ID_CLICKED)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            getListPortfolio(id!!.toInt())
        } else if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            getListPortfolio(idClick!!.toInt())
        }

    }

    fun getListPortfolio(engineerId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getPortfolioEngineer(engineerId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is PortofolioResponse) {
                if (result.success) {
                    val list = result.data?.map {
                        PortfolioModel(it.portoId, it.engineerId, it.portoAppName, it.portoDesc, it.portoLinkPub, it.portoLinkRepo, it.portoWorkPlace, it.portoType, it.portoImage)
                    }

                    (binding.rvPortofolio.adapter as PortofolioRecyclerViewAdapter).addList(list)
                }
            }
        }
    }
}