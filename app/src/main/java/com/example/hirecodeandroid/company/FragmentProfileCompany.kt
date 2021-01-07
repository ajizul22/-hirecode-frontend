package com.example.hirecodeandroid.company

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProfileCompanyBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentProfileCompany: Fragment() {

    private lateinit var binding: FragmentProfileCompanyBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: CompanyApiService
    val img = "http://3.80.223.103:4000/image/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_company,container,false)
        sharePref = SharePrefHelper(requireContext())

        service = ApiClient.getApiClient(requireContext())!!.create(CompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

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
        getDataCompany(id!!.toInt())
    }

    private fun getDataCompany(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataCompany(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is CompanyResponse) {
                Log.d("data company by id", result.toString())
                binding.model = result.data[0]
                Glide.with(requireContext()).load(img + result.data[0].companyPhotoProfile).placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile).into(binding.ivAvatar)
            }
        }
    }

}