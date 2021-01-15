package com.example.hirecodeandroid.portfolio.updateportfolio

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityUpdatePortfolioBinding
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

class UpdatePortfolioActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var binding: ActivityUpdatePortfolioBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: UpdatePortfolioViewModel
    private var imgPortfolio: MultipartBody.Part? = null
    private var image: String = ""
    private var portType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(PortofolioApiService::class.java)

        viewModel = ViewModelProvider(this).get(UpdatePortfolioViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        val portId = intent.getIntExtra("id", 0)
        viewModel.getDataPortfolio(portId)
        subscribeLiveData()

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

        binding.btnUpdatePortfolio.setOnClickListener {

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
            val portoType = portType?.toRequestBody("text/plain".toMediaTypeOrNull())

            if (image != "") {
                viewModel.setImage(imgPortfolio!!)
                viewModel.updatePortfolio(1, portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType)
                subscribeLiveDataUpdate()
            } else {
                viewModel.updatePortfolio(0, portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType)
                subscribeLiveDataUpdate()
            }
        }

    }

    private fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            }
        })

        viewModel.listModel.observe(this, Observer {
            binding.model = it[0]
            val img = "http://3.80.223.103:4000/image/"
            Glide.with(binding.root)
            .load(img+it[0].portoImage)
            .placeholder(R.drawable.ic_project)
            .error(R.drawable.ic_project)
            .into(binding.ivUploadImage)

            portType = it[0].portoType!!
            if (it[0].portoType == "aplikasi mobile") {
            binding.radioMobile.isChecked = true
            } else if (it[0].portoType == "aplikasi web") {
            binding.radioWeb.isChecked = true
            }
        })
    }

    private fun subscribeLiveDataUpdate() {
        viewModel.isUpdateLiveData.observe(this, Observer {
            if (it) {
                val intent = Intent(this@UpdatePortfolioActivity, HomeActivity::class.java)
                Toast.makeText(this@UpdatePortfolioActivity, "Success Update Portfolio", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@UpdatePortfolioActivity, "Failed to Update Portfolio", Toast.LENGTH_SHORT).show()
            }
        })
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
            if (filePath != null) {
                image = filePath
            }

            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            imgPortfolio = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
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

//    private fun updatePortfolio(image: MultipartBody.Part) {
//        coroutineScope.launch {
//
//            val result = withContext(Dispatchers.IO) {
//                try {
//                    val portId = intent.getIntExtra("id", 0)
//                    val portAppName = binding.etAppName.text.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
//                    val portDesc = binding.etShortDescApp.text.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
//                    val portLinkPub = binding.etLinkPub.text.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
//                    val portLinkRepo = binding.etLinkRepo.text.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
//                    val portWorkPlace = binding.etWorkplaceApp.text.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
//                    val portoType = portType?.toRequestBody("text/plain".toMediaTypeOrNull())
//
//                    service?.updatePortfolio(portId, portAppName, portDesc, portLinkPub, portLinkRepo, portWorkPlace, portoType, image)
//                } catch (e:Throwable) {
//                    e.printStackTrace()
//                }
//            }
//            Log.d("cek Data gagal masuk", result.toString())
//            if (result is GeneralResponse) {
//                Log.d("cek Data masuk", result.toString())
//                if (result.success) {
//
//                    val intent = Intent(this@UpdatePortfolioActivity, HomeActivity::class.java)
//                    Toast.makeText(this@UpdatePortfolioActivity, "Success update portfolio", Toast.LENGTH_SHORT).show()
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}