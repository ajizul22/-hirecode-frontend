package com.example.hirecodeandroid.experience.updateexperience

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityUpdateExperienceBinding
import com.example.hirecodeandroid.experience.ExperienceApiService
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateExperienceBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var c: Calendar
    private lateinit var dateStart: DatePickerDialog.OnDateSetListener
    private lateinit var dateEnd: DatePickerDialog.OnDateSetListener
    private lateinit var viewModel: UpdateExperienceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_experience)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(this)?.create(ExperienceApiService::class.java)

        viewModel = ViewModelProvider(this).get(UpdateExperienceViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        c = Calendar.getInstance()

        val id = intent.getIntExtra("id", 0)
        viewModel.getDataExperience(id)
        subscribeLiveData()

        binding.btnUpdateExp.setOnClickListener {
            viewModel.updateExperience(id, binding.etPosition.text.toString(), binding.etCompanyName.text.toString(),
                binding.etStart.text.toString(), binding.etEnd.text.toString(), binding.etShortDescExp.text.toString())

            subscribeLiveDataUpdate()
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

        dateStartPicker()
        dateEndPicker()
    }

    fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            }
        })

        viewModel.listModel.observe(this, androidx.lifecycle.Observer {
            val start = it[0].experienceStart!!.split("T")[0].split("-").joinToString("")
            val end = it[0].experienceEnd!!.split("T")[0].split("-").joinToString("")

            binding.model = it[0]
            binding.etStart.setText(start)
            binding.etEnd.setText(end)

        })
    }

    private fun subscribeLiveDataUpdate() {
        viewModel.isUpdateLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(this, "Success Update Experience", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to Update Experience", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun dateStartPicker() {
        dateStart = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val start = findViewById<TextView>(R.id.et_start)
            val Format = "yyyyMMdd"
            val sdf = SimpleDateFormat(Format, Locale.US)

            start.text = sdf.format(c.time)
        }
    }

    private fun dateEndPicker() {
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

//    fun updateExperience(id: Int, expPosition: String, expCompany: String, expStart: String, expEnd: String, expDesc: String) {
//        coroutineScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    service?.updateExperience(id, expPosition, expCompany, expStart, expEnd, expDesc)
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            Log.d("tidak update?", result.toString())
//            if (result is GeneralResponse) {
//                    Log.d("data update", result.toString())
//                    Toast.makeText(this@UpdateExperienceActivity, "Update experience success!", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@UpdateExperienceActivity, HomeActivity::class.java)
//                    startActivity(intent)
//                finish()
//            }
//        }
//    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}