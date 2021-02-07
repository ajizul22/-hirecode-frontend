package com.example.hirecodeandroid.hire.addhire

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddHireBinding
import com.example.hirecodeandroid.hire.HireApiService
import com.example.hirecodeandroid.hire.HireResponse
import com.example.hirecodeandroid.hire.ListHireEngineerViewModel
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
    private lateinit var viewModel: AddHireViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_add_hire
        )
        sharePref = SharePrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)
        val serviceHire = ApiClient.getApiClient(context = this)?.create(HireApiService::class.java)

        viewModel = ViewModelProvider(this).get(AddHireViewModel::class.java)
        if (serviceHire != null) {
            viewModel.setService(serviceHire)
        }

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
                viewModel.callHireApi(engineerId!!.toInt(), projectId, hirePrice, hireMessage, "wait")
                subscribeLiveData()
            }
        }
    }

    fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Success Hire", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to Hire Engineer", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun listProjectSpinner() {
        binding.spinnerListProject
        val serviceHire = ApiClient.getApiClient(context = this)?.create(HireApiService::class.java)
        coroutineScope.launch {
            val engineerId = sharePref.getString(SharePrefHelper.ENG_ID_CLICKED)
            val companyId = sharePref.getString(SharePrefHelper.COM_ID)
            val listProject = mutableListOf<Long>()


            val resultCheck = withContext(Dispatchers.IO) {
                try {
                    serviceHire?.getHireByEngineerId(engineerId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (resultCheck is HireResponse) {
                resultCheck.data.map {
                    if (it.companyId == companyId && it.engineerId == engineerId) {
                        listProject.add(it.projectId!!.toLong())
                    }
                }
            }

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCompanyId(sharePref.getString(SharePrefHelper.COM_ID))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is ProjectResponse) {
                val list = result.data?.map {
                    ProjectModel(
                        it.projectId,
                        it.companyId,
                        it.projectName,
                        it.projectDesc,
                        it.projectDeadline,
                        it.projectImage,
                        it.projectCreated,
                        it.projectUpdated
                    )
                }

                val mutableListProject = list.toMutableList()

                for (i in 0 until listProject.size) {
                    mutableListProject.removeIf { it.projectId == listProject[i].toInt() }
                }

                val projectName =
                    arrayOfNulls<String>(mutableListProject.size)
                val projectId =
                    arrayOfNulls<Int>(mutableListProject.size)

                for (i in 0 until mutableListProject.size) {
                    projectName[i] = mutableListProject.get(i).projectName
                    projectId[i] = mutableListProject.get(i).projectId
                }

                if (mutableListProject.isNullOrEmpty()) {
                    binding.btnAddHire.visibility = View.GONE
                    Toast.makeText(this@AddHireActivity, "You don't have any project to hire this engineer!", Toast.LENGTH_SHORT).show()
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
                        sharePref.put(SharePrefHelper.PROJECT_ID_SELECTED, projectId[position]!!)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}