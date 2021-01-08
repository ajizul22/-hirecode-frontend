package com.example.hirecodeandroid.company

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
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityEditProfileCompanyBinding
import com.example.hirecodeandroid.engineer.EditProfileEngineerActivity
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
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
    private lateinit var service: CompanyApiService
    private lateinit var sharedPref: SharePrefHelper
    private lateinit var binding: ActivityEditProfileCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_company)
        sharedPref = SharePrefHelper(this)

        service = ApiClient.getApiClient(this)!!.create(CompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

            val img = "http://3.80.223.103:4000/image/"
            Glide.with(binding.root)
                .load(img+intent.getStringExtra("image"))
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.ivAvatar)


        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val id = sharedPref.getString(SharePrefHelper.COM_ID)
        getDataCompany(id!!.toInt())

        binding.tvEdit.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
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

    private fun getDataCompany(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataCompany(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is CompanyResponse) {
                Log.d("data company by id", result.toString())
                binding.model = result.data[0]
            }
        }
    }

    private fun updateCompany(id: Int, image: MultipartBody.Part) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val companyName = createPartFromString(binding.etCompanyName.text.toString())
                    val companyField = createPartFromString(binding.etCompanyField.text.toString())
                    val position = createPartFromString(binding.etPosition.text.toString())
                    val city = createPartFromString(binding.etCity.text.toString())
                    val instagram = createPartFromString(binding.etIg.text.toString())
                    val linkedIn = createPartFromString(binding.etLinkedin.text.toString())
                    val desc = createPartFromString(binding.etShortDesc.text.toString())
                    service?.updateCompany(id,companyName, position, companyField, city, desc, instagram, linkedIn, image)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    val intent = Intent(this@EditProfileCompanyActivity, HomeActivity::class.java)
                    Toast.makeText(this@EditProfileCompanyActivity, "Success update profile", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "multipart/form-data".toMediaType()
        return json
            .toRequestBody(mediaType)
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
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER
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
            Log.d("image", file.name)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }
            val id = sharedPref.getString(SharePrefHelper.COM_ID)

            binding.btnSave.setOnClickListener {
                if (img != null) {
                    updateCompany(id!!.toInt(), img)
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