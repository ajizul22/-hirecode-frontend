package com.example.hirecodeandroid.experience

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
import com.example.hirecodeandroid.adapter.ExperienceRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentExperienceBinding

class FragmentExperience : Fragment() {

    private lateinit var binding : FragmentExperienceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        binding.rvExperience.adapter = ExperienceRecyclerViewAdapter()
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddExp.setOnClickListener {
            val intent = Intent(requireContext(), AddExperienceActivity::class.java)
            startActivity(intent)
        }

    }

}