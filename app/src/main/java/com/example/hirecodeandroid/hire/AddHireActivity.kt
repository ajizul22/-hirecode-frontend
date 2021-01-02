package com.example.hirecodeandroid.hire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddHireBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.project.ProjectModel
import com.example.hirecodeandroid.project.ProjectResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class AddHireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddHireBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var sharePref : SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_add_hire
        )
        sharePref = SharePrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)
        listProjectSpinner()


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun listProjectSpinner() {
        binding.spinnerListProject

        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCompanyId(sharePref.getString(SharePrefHelper.COM_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                val list = result.data?.map {
                    ProjectModel(it.projectId, it.companyId,it.projectName,it.projectDesc,it.projectDeadline,it.projectImage,it.projectCreated,it.projectUpdated)
                }
                list as ArrayList<ProjectModel>
                var data: MutableList<String> = ArrayList()
                list.forEach {
                    data.add(0, it.projectName!!)
                }
                binding.spinnerListProject.adapter = ArrayAdapter<String>(this@AddHireActivity, R.layout.support_simple_spinner_dropdown_item, data)
            }
        }
    }
}