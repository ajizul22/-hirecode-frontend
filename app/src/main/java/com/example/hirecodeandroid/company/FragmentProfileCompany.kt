package com.example.hirecodeandroid.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProfileCompanyBinding

class FragmentProfileCompany: Fragment() {

    private lateinit var binding: FragmentProfileCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_company,container,false)
        binding.btnEditProfile.setOnClickListener {
            val fragment =
                FragmentEditProfileCompany()
            fragmentManager!!.beginTransaction().replace(R.id.fg_container, fragment).commit()
        }
        return binding.root
    }

}