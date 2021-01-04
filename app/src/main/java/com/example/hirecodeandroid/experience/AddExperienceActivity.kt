package com.example.hirecodeandroid.experience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddExperienceBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService
    private lateinit var sharePref : SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ExperienceApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddExp.setOnClickListener {
            val enId = sharePref.getString(SharePrefHelper.ENG_ID)
            val expPosition = binding.etPosition.text.toString()
            val expDesc = binding.etShortDescExp.text.toString()
            val expStart = binding.etStart.text.toString()
            val expEnd = binding.etEnd.text.toString()
            val expCompany = binding.etCompanyName.text.toString()

            callExperienceApi(enId!!.toInt(), expPosition, expCompany, expStart, expEnd, expDesc)
        }
    }

    private fun callExperienceApi(enId: Int, expPosition: String, expCompany: String, expStart: String, expEnd: String, expDesc: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.addExperience(enId,expPosition,expCompany,expStart,expEnd,expDesc)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ExperienceResponse) {
                Log.d("masuk ga", result.toString())
                Toast.makeText(this@AddExperienceActivity, "Success Add Experience", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddExperienceActivity, HomeActivity::class.java)
                startActivity(intent)

            }
        }
    }
}