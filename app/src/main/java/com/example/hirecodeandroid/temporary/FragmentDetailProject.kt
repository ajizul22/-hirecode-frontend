package com.example.hirecodeandroid.temporary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentDetailProjectBinding

class FragmentDetailProject: Fragment() {

    private lateinit var binding: FragmentDetailProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_project, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageProject = arguments?.getInt("image")
        val title = arguments?.getString("title")
        val company = arguments?.getString("company")
        val deadline = arguments?.getString("deadline")

        binding.ivProject.setImageResource(imageProject!!)
        binding.tvTitlePj.text = title
        binding.tvCompanyPj.text = company
        binding.tvDeadlinePj.text = deadline
    }
}