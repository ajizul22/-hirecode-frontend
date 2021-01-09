package com.example.hirecodeandroid.engineer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.hirecodeandroid.experience.FragmentExperience
import com.example.hirecodeandroid.portfolio.FragmentPortofolio

class EngineerTabPagerAdapter(fragment: FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragment = arrayOf(
        FragmentPortofolio(),
        FragmentExperience()
    )

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Portfolio"
            1 -> "Experience"
            else -> ""
        }
    }

}