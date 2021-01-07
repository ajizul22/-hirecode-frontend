package com.example.hirecodeandroid.engineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddSkillBinding
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class AddSkillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: SkillApiService
    private lateinit var sharedPref: SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_skill)
        sharedPref = SharePrefHelper(this)

        service = ApiClient.getApiClient(this)!!.create(SkillApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddSkill.setOnClickListener {
            addSkill(sharedPref.getString(SharePrefHelper.ENG_ID)!!.toInt(), binding.etSkill.text.toString())
        }
    }

    private fun addSkill(id: Int, skillName: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.createSkill(id, skillName)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    Toast.makeText(this@AddSkillActivity, "Success Add Skill", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddSkillActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}