package com.example.hirecodeandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.hirecodeandroid.fragment.FragmentExperience
import com.example.hirecodeandroid.fragment.FragmentPortofolio

class EngineerTabPagerAdapter(fragment: FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragment = arrayOf(FragmentPortofolio(), FragmentExperience())

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Portofolio"
            1 -> "Experience"
            else -> ""
        }
    }

}