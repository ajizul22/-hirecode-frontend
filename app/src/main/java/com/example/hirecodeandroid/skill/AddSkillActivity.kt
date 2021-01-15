package com.example.hirecodeandroid.skill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddSkillBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class AddSkillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: AddSkillViewModel
    private lateinit var sharedPref: SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_skill)
        sharedPref = SharePrefHelper(this)

        val service = ApiClient.getApiClient(this)?.create(SkillApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        viewModel = ViewModelProvider(this).get(AddSkillViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddSkill.setOnClickListener {
            viewModel.addSkill(sharedPref.getString(SharePrefHelper.ENG_ID)!!.toInt(), binding.etSkill.text.toString())
            subscribeLiveData()
        }
    }

    fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Success Add Skill", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
            } else {
                Toast.makeText(this, "Add Skill Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}