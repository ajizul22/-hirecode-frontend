package com.example.hirecodeandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentEditProfileCompanyBinding

class FragmentEditProfileCompany: Fragment() {

    private lateinit var binding: FragmentEditProfileCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile_company,container,false)

        binding.btnCancel.setOnClickListener {
            val fragment = FragmentProfileCompany()
            fragmentManager!!.beginTransaction().replace(R.id.fg_container, fragment).commit()
        }
        return binding.root
    }

}