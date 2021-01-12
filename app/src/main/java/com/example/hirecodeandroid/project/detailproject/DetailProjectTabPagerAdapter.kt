package com.example.hirecodeandroid.project.detailproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.FragmentListHireApprove
import com.example.hirecodeandroid.project.detailproject.listhirebyproject.FragmentListHireWaiting

class DetailProjectTabPagerAdapter(fragment: FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragment = arrayOf(
        FragmentListHireApprove(),
        FragmentListHireWaiting()
    )

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int = fragment.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "List Engineer"
            1 -> "Waiting Engineer Response"
            else -> ""
        }
    }
}