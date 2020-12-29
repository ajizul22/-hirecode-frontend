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
import com.example.hirecodeandroid.adapter.PortofolioRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentPortofolioBinding

class FragmentPortofolio : Fragment() {

    private lateinit var binding : FragmentPortofolioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portofolio, container, false)
        binding.rvPortofolio.adapter = PortofolioRecyclerViewAdapter()
        binding.rvPortofolio.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPortfolio.setOnClickListener {
            val intent = Intent(requireContext(), AddPortfolioActivity::class.java)
            startActivity(intent)
        }

    }
}