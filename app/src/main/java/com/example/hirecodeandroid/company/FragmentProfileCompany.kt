package com.example.hirecodeandroid.company

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProfileCompanyBinding
import com.example.hirecodeandroid.temporary.EditProfileCompanyActivity
import com.example.hirecodeandroid.util.SharePrefHelper

class FragmentProfileCompany: Fragment() {

    private lateinit var binding: FragmentProfileCompanyBinding
    private lateinit var sharePref: SharePrefHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_company,container,false)
        sharePref = SharePrefHelper(requireContext())

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileCompanyActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sharePref.getString(SharePrefHelper.KEY_EMAIL)
        binding.tvEmailAddress.text = email
    }

}