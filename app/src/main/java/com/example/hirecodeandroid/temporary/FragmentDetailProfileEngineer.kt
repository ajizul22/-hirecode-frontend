package com.example.hirecodeandroid.temporary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.engineer.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.FragmentDetailProfileEngBinding

class FragmentDetailProfileEngineer: Fragment() {

    private lateinit var binding : FragmentDetailProfileEngBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_profile_eng, container, false)

        pagerAdapter =
            EngineerTabPagerAdapter(
                childFragmentManager
            )
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var imageProfile = arguments?.getInt("image")
        var name = arguments?.getString("name")
        var jobTitle = arguments?.getString("title")
        var skillOne = arguments?.getString("skill1")
        var skillTwo = arguments?.getString("skill2")
        var skillThree = arguments?.getString("skill3")

        binding.ivAvatar.setImageResource(imageProfile!!)
        binding.tvName.text = name
        binding.tvJobType.text = jobTitle
        binding.tvSkill1.text = skillOne
        binding.tvSkill2.text = skillTwo
        binding.tvSkill3.text = skillThree



    }

}