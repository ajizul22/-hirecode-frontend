package com.example.hirecodeandroid.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddProjectBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AddProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project)
        sharePref = SharePrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAddProject.setOnClickListener {
            callProjectApi()
            val intent = Intent(this, HomeActivity::class.java)
            Toast.makeText(this, "Success Add Project", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    private fun callProjectApi() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    val idCompany = sharePref.getString(SharePrefHelper.COM_ID).toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etProjectDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etProjectDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    service?.addProject(idCompany, projectName, projectDesc, projectDeadline)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is AddProjectResponse) {
                Log.d("Data", result.toString())
            }
        }

    }
}