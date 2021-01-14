package com.example.hirecodeandroid.experience.addexperience

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddExperienceBinding
import com.example.hirecodeandroid.experience.ExperienceApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharePref : SharePrefHelper
    private lateinit var c: Calendar
    private lateinit var dateStart: DatePickerDialog.OnDateSetListener
    private lateinit var dateEnd: DatePickerDialog.OnDateSetListener
    private lateinit var viewModel: AddExperienceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(ExperienceApiService::class.java)

        viewModel = ViewModelProvider(this).get(AddExperienceViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        c = Calendar.getInstance()

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

            viewModel.callExperienceApi(enId!!.toInt(), expPosition, expCompany, expStart, expEnd, expDesc)
            subscribeLiveData()
        }

        binding.etStart.setOnClickListener {
            DatePickerDialog(
                this, dateStart, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etEnd.setOnClickListener {
            DatePickerDialog(
                this, dateEnd, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        datePicker()
    }

    private fun datePicker() {
        dateStart = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val start = findViewById<TextView>(R.id.et_start)
            val Format = "yyyyMMdd"
            val sdf = SimpleDateFormat(Format, Locale.US)

            start.text = sdf.format(c.time)
        }

        dateEnd = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val end = findViewById<TextView>(R.id.et_end)
            val Format = "yyyyMMdd"
            val sdf = SimpleDateFormat(Format, Locale.US)

            end.text = sdf.format(c.time)
        }
    }

    private fun subscribeLiveData() {
        viewModel.isAddExperienceLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                Toast.makeText(this@AddExperienceActivity, "Success Add Experience", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddExperienceActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@AddExperienceActivity, "Failed to Add Experience", Toast.LENGTH_SHORT).show()
            }
        })
    }

}