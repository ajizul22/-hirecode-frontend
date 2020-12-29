package com.example.hirecodeandroid.fragment

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
import com.example.hirecodeandroid.adapter.HomeRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentSearchBinding
import com.example.hirecodeandroid.dataclass.ListEngineerDataClass
import com.example.hirecodeandroid.DetailProfileEngineerActivity

class FragmentSearch: Fragment(), HomeRecyclerViewAdapter.OnItemClickListener {

//    private lateinit var communicator: Communicator
    private lateinit var binding : FragmentSearchBinding
    private var engineerModel = ArrayList<ListEngineerDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEngineerList()

//        communicator = activity as Communicator
        binding.rvSearch.adapter = HomeRecyclerViewAdapter(engineerModel, this)
        binding.rvSearch.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }



    private fun generateDummyList(size : Int) : List<ListEngineerDataClass> {
        val list = ArrayList<ListEngineerDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%3) {
                0 -> R.drawable.avatar
                1 -> R.drawable.ic_github
                else -> R.drawable.jane
            }

            val item = ListEngineerDataClass(drawable, "Billy", "Android Developer", "Kotlin", "Node Js", "Java", "3+")
            list += item
        }
        return list
    }

    private fun getEngineerList() {
        engineerModel = ArrayList()

        engineerModel.add(ListEngineerDataClass(
            R.drawable.avatar,
                "Ajizul",
                "Android Developer",
                "Kotlin",
                "Node Js",
                "Javascript",
                "3+"
        ))
    }

    override fun onItemClick(item: ListEngineerDataClass, position: Int) {
//        communicator.passDataEng(item.imageProfile, item.name, item.jobTitle, item.skillOne, item.skillTwo,item.skillThree)
        val intent = Intent(requireContext(), DetailProfileEngineerActivity::class.java)
        intent.putExtra("image", engineerModel[position].imageProfile)
        intent.putExtra("name", engineerModel[position].name)
        intent.putExtra("title", engineerModel[position].jobTitle)
        intent.putExtra("skill1", engineerModel[position].skillOne)
        intent.putExtra("skill2", engineerModel[position].skillTwo)
        intent.putExtra("skill3", engineerModel[position].skillThree)
        startActivity(intent)
    }
}