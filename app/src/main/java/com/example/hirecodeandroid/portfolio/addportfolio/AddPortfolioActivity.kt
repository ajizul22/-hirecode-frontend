package com.example.hirecodeandroid.portfolio.addportfolio

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddPortfolioBinding
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddPortfolioActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var binding: ActivityAddPortfolioBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var portType: String
    private lateinit var viewModel: AddPortfolioViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(PortofolioApiService::class.java)

        viewModel = ViewModelProvider(this).get(AddPortfolioViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.ivUploadImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    );
                }
                else{
                    //permission already granted
                    openImageChooser()
                }
            }
            else{
                //system OS is < Marshmallow
                openImageChooser()
            }
        }

    }

    private fun subscribeLiveData() {
        viewModel.isAddPortfolioLiveData.observe(this, androidx.lifecycle.Observer {
            if (it) {
                Toast.makeText(this@AddPortfolioActivity, "Success Add Portfolio", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddPortfolioActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@AddPortfolioActivity, "Failed to Add Portfolio", Toast.LENGTH_SHORT).show()
            }
        })
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    openImageChooser()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            REQUEST_CODE_IMAGE_PICKER
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICKER) {
            // I'M GETTING THE URI OF THE IMAGE AS DATA AND SETTING IT TO THE IMAGEVIEW
            val selectedImage = data?.data
            binding.ivUploadImage.setImageURI(selectedImage)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }

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

            binding.btnAddPortfolio.setOnClickListener {
                if (img != null) {
                    viewModel.callPortfolioApi(engineerId,portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType, img)
                    subscribeLiveData()
                }
            }
        }
    }

    fun getPath(context: Context, contentUri: Uri) : String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }

        return result
    }


}