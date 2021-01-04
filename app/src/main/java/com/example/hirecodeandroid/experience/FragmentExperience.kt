package com.example.hirecodeandroid.experience

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.adapter.ExperienceRecyclerViewAdapter
import com.example.hirecodeandroid.databinding.FragmentExperienceBinding
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentExperience : Fragment() {

    private lateinit var binding : FragmentExperienceBinding
    private lateinit var sharePref: SharePrefHelper
    var listExperience = ArrayList<ExperienceModel>()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        sharePref = SharePrefHelper(requireContext())

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ExperienceApiService::class.java)

        binding.rvExperience.adapter = ExperienceRecyclerViewAdapter(listExperience)
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            binding.btnAddExp.visibility = View.GONE
        }

        binding.btnAddExp.setOnClickListener {
            val intent = Intent(requireContext(), AddExperienceActivity::class.java)
            startActivity(intent)
        }

        val id = sharePref.getString(SharePrefHelper.ENG_ID)
        getListExperience(id!!.toInt())
    }

    private fun getListExperience(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                 service?.getExpByIdEng(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExperienceResponse) {
                val list = result.data?.map {
                    ExperienceModel(it.experienceId, it.engineerId, it.experiencePosition, it.experienceCompany, it.experienceStart,it.experienceEnd,it.experienceDesc)
                }
                (binding.rvExperience.adapter as ExperienceRecyclerViewAdapter).addList(list)
            }
        }
    }

}