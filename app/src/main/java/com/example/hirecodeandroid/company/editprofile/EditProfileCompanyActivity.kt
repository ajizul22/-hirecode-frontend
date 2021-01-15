package com.example.hirecodeandroid.company.editprofile

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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.company.CompanyApiService
import com.example.hirecodeandroid.databinding.ActivityEditProfileCompanyBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileCompanyActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharedPref: SharePrefHelper
    private lateinit var binding: ActivityEditProfileCompanyBinding
    private lateinit var viewModel: EditProfileCompanyViewModel
    private var img: MultipartBody.Part? = null
    private var image: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_company)
        sharedPref = SharePrefHelper(this)

        val service = ApiClient.getApiClient(this)?.create(CompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        viewModel = ViewModelProvider(this).get(EditProfileCompanyViewModel::class.java)
        viewModel.setBinding(binding)
        viewModel.setSharePref(sharedPref)

        if (service != null) {
            viewModel.setCompanyService(service)
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val accountId = sharedPref.getString(SharePrefHelper.AC_ID)
        val id = sharedPref.getString(SharePrefHelper.COM_ID)
        viewModel.getDataCompany(id!!.toInt())
        viewModel.getAccountData(accountId!!.toInt())
        subscribeLiveData()

        binding.btnUpdateAccount.setOnClickListener {
            val name = binding.etAcName.text.toString()
            val email = binding.etAcEmail.text.toString()
            val phone = binding.etAcPhone.text.toString()
            val password = binding.etAcPassword.text.toString()

            viewModel.updateAccount(accountId.toInt(), name, email, phone, password)
            subscribeLiveDataUpdate()
        }

        binding.btnSave.setOnClickListener {
            val companyName = createPartFromString(binding.etCompanyName.text.toString())
            val companyField = createPartFromString(binding.etCompanyField.text.toString())
            val position = createPartFromString(binding.etPosition.text.toString())
            val city = createPartFromString(binding.etCity.text.toString())
            val instagram = createPartFromString(binding.etIg.text.toString())
            val linkedIn = createPartFromString(binding.etLinkedin.text.toString())
            val desc = createPartFromString(binding.etShortDesc.text.toString())

            if (image != "") {
                viewModel.setImage(img!!)
                viewModel.updateDataCompany(1, id.toInt(), companyName, position, companyField, city, desc, instagram, linkedIn)
                subscribeLiveDataUpdate()
            } else {
                viewModel.updateDataCompany(0, id.toInt(), companyName, position, companyField, city, desc, instagram, linkedIn)
                subscribeLiveDataUpdate()
            }
        }

        binding.tvEdit.setOnClickListener {
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
        viewModel.isCompanyLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            }
        })
    }

    private fun subscribeLiveDataUpdate() {
        viewModel.isUpdateCompanyLiveData.observe(this, Observer {
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                Toast.makeText(this@EditProfileCompanyActivity, "Success update profile", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                Toast.makeText(this@EditProfileCompanyActivity, "Edit profile failed", Toast.LENGTH_SHORT).show()
            }
        })
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
            binding.ivAvatar.setImageURI(selectedImage)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)
            if (filePath != null) {
                image = filePath
            }

            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }
        }
    }

    private fun getPath(context: Context, contentUri: Uri) : String? {
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

    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
            .toRequestBody(mediaType)
    }
}