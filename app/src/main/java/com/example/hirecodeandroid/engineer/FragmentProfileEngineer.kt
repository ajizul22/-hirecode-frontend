package com.example.hirecodeandroid.engineer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.adapter.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.FragmentProfileBinding
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerModel
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity
import kotlinx.coroutines.*


class FragmentProfileEngineer: Fragment() {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var sharedPref: SharePrefHelper
    val img = "http://3.80.223.103:4000/image/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        sharedPref = SharePrefHelper(requireContext())

        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

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

        val id = sharedPref.getString(SharePrefHelper.ENG_ID)
        getDataEngineer(id!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sharedPref.getString(SharePrefHelper.KEY_EMAIL)
        binding.tvEmailAddress.text = email
    }

    fun getDataEngineer(id: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataEngById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is ListEngineerResponse) {
                Log.d("data engineer by id", result.toString())
                binding.model = result.data[0]
                Glide.with(requireContext()).load(img + result.data[0].engineerProfilePict).placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile).into(binding.ivAvatar)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}