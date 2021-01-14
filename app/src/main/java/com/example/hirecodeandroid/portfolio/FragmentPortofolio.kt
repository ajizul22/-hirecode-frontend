package com.example.hirecodeandroid.portfolio

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
import com.example.hirecodeandroid.databinding.FragmentPortofolioBinding
import com.example.hirecodeandroid.portfolio.addportfolio.AddPortfolioActivity
import com.example.hirecodeandroid.portfolio.detailportfolio.DetailPortfolioActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentPortofolio : Fragment(), PortofolioRecyclerViewAdapter.OnListPortfolioClickListener, PortfolioContract.View {

    private lateinit var binding : FragmentPortofolioBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    var listPortfolio = ArrayList<PortfolioModel>()
    private var presenter: PortfolioContract.Presenter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portofolio, container, false)
        sharePref = SharePrefHelper(requireContext())

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(PortofolioApiService::class.java)

        presenter = PortfolioPresenter(coroutineScope, service)

        binding.rvPortofolio.adapter =
            PortofolioRecyclerViewAdapter(listPortfolio, this)
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

    }

    override fun onResultSuccess(list: List<PortfolioModel>) {
        (binding.rvPortofolio.adapter as PortofolioRecyclerViewAdapter).addList(list)
        binding.rvPortofolio.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvPortofolio.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvPortofolio.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)

        val id = sharePref.getString(SharePrefHelper.ENG_ID)
        val idClick = sharePref.getString(SharePrefHelper.ENG_ID_CLICKED)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            presenter?.callPortfolioService(id!!.toInt())
        } else if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            presenter?.callPortfolioService(idClick!!.toInt())
        }
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onPortfolioItemClick(position: Int) {
        val intent = Intent(requireContext(), DetailPortfolioActivity:: class.java)
        intent.putExtra("id", listPortfolio[position].portoId)
        startActivity(intent)
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}