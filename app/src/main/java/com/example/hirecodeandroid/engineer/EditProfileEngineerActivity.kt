package com.example.hirecodeandroid.engineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityEditProfileEngineerBinding
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class EditProfileEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    private lateinit var sharedPref: SharePrefHelper
    val img = "http://3.80.223.103:4000/image/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_engineer)
        sharedPref = SharePrefHelper(this)

        service = ApiClient.getApiClient(this)!!.create(EngineerApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val id = sharedPref.getString(SharePrefHelper.ENG_ID)

        getDataEngineer(id!!)
    }

    private fun getDataEngineer(id: String) {
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
                Glide.with(this@EditProfileEngineerActivity).load(img + result.data[0].engineerProfilePict).placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile).into(binding.ivAvatar)
            }
        }
    }
}