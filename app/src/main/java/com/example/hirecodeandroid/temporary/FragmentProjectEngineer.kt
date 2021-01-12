package com.example.hirecodeandroid.temporary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.temporary.ProjectEngineerRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentProjectBinding
import com.example.hirecodeandroid.dataclass.ListProjectEngineerData
import com.example.hirecodeandroid.util.PassDataProject

class FragmentProjectEngineer: Fragment(), ProjectEngineerRecyclerViewAdapter.OnItemClickListener {

    private lateinit var binding: FragmentProjectBinding
    private var projectModel = ArrayList<ListProjectEngineerData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListProject()
        binding.rvProject.adapter =
            ProjectEngineerRecyclerViewAdapter(
                projectModel,
                this
            )
        binding.rvProject.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    fun getListProject() {
        projectModel = ArrayList()

        projectModel.add(ListProjectEngineerData(
            R.drawable.port1,
                "Membuat Aplikasi Company Chat",
                "PT Tokopedia",
                "December 2020"
        ))

        projectModel.add(ListProjectEngineerData(
                R.drawable.port3,
                "Membuat Web Company Profile",
                "PT Bukalapak",
                "August 2020"
            ))

        projectModel.add(ListProjectEngineerData(
            R.drawable.port2,
            "Membuat Aplikasi Catur",
            "PT OLX",
            "September 2020"
        ))

        projectModel.add(ListProjectEngineerData(
            R.drawable.port4,
            "Membuat Web Jual Beli Online",
            "PT Shopee",
            "Februari 2021"
        ))
    }

    override fun onItemClick(item: ListProjectEngineerData, position: Int) {
    }
}