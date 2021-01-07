package com.example.hirecodeandroid.portfolio

import android.app.Activity
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
import com.example.hirecodeandroid.databinding.ActivityAddPortfolioBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.project.addproject.AddProjectActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AddPortfolioActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    private lateinit var binding: ActivityAddPortfolioBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: PortofolioApiService
    private lateinit var portType: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(PortofolioApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.ivUploadImage.setOnClickListener {
            openImageChooser()
        }

        binding.btnAddPortfolio.setOnClickListener {
            callPortfolioApi()
        }


    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
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

    private fun callPortfolioApi() {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    val engineerId = sharePref.getString(SharePrefHelper.ENG_ID).toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
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

                    service?.addPortfolio(engineerId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("cek Data gagal masuk", result.toString())
            if (result is GeneralResponse) {
                Log.d("cek Data masuk", result.toString())
                if (result.success) {

                    val intent = Intent(this@AddPortfolioActivity, HomeActivity::class.java)
                    Toast.makeText(this@AddPortfolioActivity, "Success add portfolio", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_CODE_IMAGE_PICKER
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICKER) {
            // I'M GETTING THE URI OF THE IMAGE AS DATA AND SETTING IT TO THE IMAGEVIEW
            val selectedImage = data?.data
            binding.ivUploadImage.setImageURI(selectedImage)
            Log.d("imagee", selectedImage.toString())

        }
    }


}