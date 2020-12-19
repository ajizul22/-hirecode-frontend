package com.example.hirecodeandroid.fragment

import android.content.Context
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
import com.example.hirecodeandroid.util.SharedPrefUtil


class FragmentProfile: Fragment() {


    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        sharedPref = this.activity!!.getSharedPreferences(SharedPrefUtil.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        pagerAdapter = EngineerTabPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sharedPref.getString(SharedPrefUtil.KEY_EMAIL,"email")
        binding.tvEmailAddress.text = email
    }
}