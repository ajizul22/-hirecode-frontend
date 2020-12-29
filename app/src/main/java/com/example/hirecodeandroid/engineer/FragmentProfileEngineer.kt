package com.example.hirecodeandroid.engineer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.adapter.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.FragmentProfileBinding
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity


class FragmentProfileEngineer: Fragment() {


    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var sharedPref: SharePrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        sharedPref = SharePrefHelper(requireContext())
        pagerAdapter = EngineerTabPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileEngineerActivity::class.java)
            startActivity(intent)
        }

        binding.tvGit.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddSkill.setOnClickListener {
            val intent = Intent(activity, AddSkillActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sharedPref.getString(SharePrefHelper.KEY_EMAIL)
        binding.tvEmailAddress.text = email
    }
}