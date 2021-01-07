package com.example.hirecodeandroid.experience

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityUpdateExperienceBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateExperienceBinding
    private lateinit var service: ExperienceApiService
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var c: Calendar
    private lateinit var dateStart: DatePickerDialog.OnDateSetListener
    private lateinit var dateEnd: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_experience)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ExperienceApiService::class.java)

        c = Calendar.getInstance()

        val id = intent.getIntExtra("id", 0)
        val posText = intent.getStringExtra("position")
        binding.etPosition.setText(posText)
        val compText = intent.getStringExtra("company")
        binding.etCompanyName.setText(compText)
        val startText = intent.getStringExtra("start")!!.split("T")[0].split("-").joinToString("")
        binding.etStart.setText(startText)
        val endText = intent.getStringExtra("end")!!.split("T")[0].split("-").joinToString("")
        binding.etEnd.setText(endText)
        val descText = intent.getStringExtra("desc")
        binding.etShortDescExp.setText(descText)

        binding.btnUpdateExp.setOnClickListener {
            updateExperience(id, binding.etPosition.text.toString(), binding.etCompanyName.text.toString(),
                binding.etStart.text.toString(), binding.etEnd.text.toString(), binding.etShortDescExp.text.toString())
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

    fun updateExperience(id: Int, expPosition: String, expCompany: String, expStart: String, expEnd: String, expDesc: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.updateExperience(id, expPosition, expCompany, expStart, expEnd, expDesc)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("tidak update?", result.toString())
            if (result is GeneralResponse) {
                    Log.d("data update", result.toString())
                    Toast.makeText(this@UpdateExperienceActivity, "Update experience success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateExperienceActivity, HomeActivity::class.java)
                    startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}