package com.example.hirecodeandroid.hire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
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

        binding.btnAddHire.setOnClickListener {
            val projectId = sharePref.getInteger(SharePrefHelper.PROJECT_ID_SELECTED)
            val engineerId = sharePref.getString(SharePrefHelper.ENG_ID_CLICKED)
            val hirePrice = binding.etHirePrice.text.toString()
            val hireMessage = binding.etHireMessage.text.toString()

            if (binding.etHireMessage.text.isEmpty() || binding.etHirePrice.text.isEmpty()) {
                Toast.makeText(this, "All field must be filled!", Toast.LENGTH_SHORT).show()
            } else {
                callHireApi(engineerId!!.toInt(), projectId, hirePrice, hireMessage, "wait")
            }
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
                val projectName =
                    arrayOfNulls<String>(list.size)
                val projectId =
                    arrayOfNulls<Int>(list.size)

                for (i in 0 until list.size) {
                    projectName[i] = list.get(i).projectName
                    projectId[i] = list.get(i).projectId
                }

                binding.spinnerListProject.adapter = ArrayAdapter<String>(this@AddHireActivity, R.layout.support_simple_spinner_dropdown_item, projectName)

                binding.spinnerListProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Toast.makeText(this@AddHireActivity, "${projectId[position]} clicked", Toast.LENGTH_LONG).show()
                        sharePref.put(SharePrefHelper.PROJECT_ID_SELECTED, projectId[position]!!)
                    }
                }
            }
        }
    }

    fun callHireApi(enId: Int, pjId: Int, hirePrice: String, hireMessage: String, hireStatus: String) {
        val service = ApiClient.getApiClient(context = this)?.create(HireApiService::class.java)
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.addHire(enId, pjId, hirePrice, hireMessage, hireStatus)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is HireResponse) {
                if (result.success) {
                    Toast.makeText(this@AddHireActivity, "Success Hire", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddHireActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}