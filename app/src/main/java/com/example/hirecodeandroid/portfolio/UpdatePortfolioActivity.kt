package com.example.hirecodeandroid.portfolio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityUpdatePortfolioBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UpdatePortfolioActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    private lateinit var binding: ActivityUpdatePortfolioBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    private lateinit var portType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(PortofolioApiService::class.java)

        val lastPortType = intent.getStringExtra("type")

        binding.etAppName.setText(intent.getStringExtra("name"))
        binding.etShortDescApp.setText(intent.getStringExtra("desc"))
        binding.etLinkPub.setText(intent.getStringExtra("pub"))
        binding.etLinkRepo.setText(intent.getStringExtra("repo"))
        binding.etWorkplaceApp.setText(intent.getStringExtra("workplace"))

        if (lastPortType == "aplikasi mobile") {
            binding.radioMobile.isChecked = true
        } else if (lastPortType == "aplikasi web") {
            binding.radioWeb.isChecked = true
        }

        binding.btnUpdatePortfolio.setOnClickListener {
            updatePortfolio()
        }


    }

    private fun updatePortfolio() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    val portId = intent.getIntExtra("id", 0)
                    val portAppName = binding.etAppName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val portDesc = binding.etShortDescApp.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val portLinkPub = binding.etLinkPub.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val portLinkRepo = binding.etLinkRepo.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val portWorkPlace = binding.etWorkplaceApp.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val portoType = portType.toRequestBody("text/plain".toMediaTypeOrNull())

                    service?.updatePortfolio(portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("cek Data gagal masuk", result.toString())
            if (result is GeneralResponse) {
                Log.d("cek Data masuk", result.toString())
                if (result.success) {

                    val intent = Intent(this@UpdatePortfolioActivity, HomeActivity::class.java)
                    Toast.makeText(this@UpdatePortfolioActivity, "Success update portfolio", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_mobile ->
                    if (checked) {
                        portType = "aplikasi mobile"
                    }
                R.id.radio_web ->
                    if (checked) {
                        portType = "aplikasi web"
                    }
            }
        }
    }
}