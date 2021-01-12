package com.example.hirecodeandroid.company

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.company.editprofilecompany.EditProfileCompanyActivity
import com.example.hirecodeandroid.databinding.FragmentProfileCompanyBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentProfileCompany: Fragment() {

    private lateinit var binding: FragmentProfileCompanyBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: ProfileCompanyViewModel
    val img = "http://3.80.223.103:4000/image/"
    var image: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_company,container,false)
        sharePref = SharePrefHelper(requireContext())

        val service = ApiClient.getApiClient(requireContext())?.create(CompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        viewModel = ViewModelProvider(this@FragmentProfileCompany).get(ProfileCompanyViewModel::class.java)
        viewModel.setBinding(binding)

        if (service != null) {
            viewModel.setCompanyService(service)
        }

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

        val id = sharePref.getString(SharePrefHelper.COM_ID)
        viewModel.callCompanyApi(id!!.toInt())
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        val progress: Int = 0
        viewModel.isCompanyLiveData.observe(this@FragmentProfileCompany, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
                binding.progressBar.progress = progress
            }
        })
    }

}