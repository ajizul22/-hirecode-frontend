package com.example.hirecodeandroid.project.addproject

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddProjectBinding
import com.example.hirecodeandroid.project.ProjectApiService
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
import java.text.SimpleDateFormat
import java.util.*

class AddProjectActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 100;
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var sharePref : SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var deadlineProject: DatePickerDialog.OnDateSetListener
    private lateinit var c: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectApiService::class.java)

        c = Calendar.getInstance()

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

        binding.etProjectDeadline.setOnClickListener {
            DatePickerDialog(
                this, deadlineProject, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        deadlineProject()
    }

    private fun deadlineProject() {
        deadlineProject = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_project_deadline)
            val Format = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(Format, Locale.US)

            day.text = sdf.format(c.time)
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
            Log.d("image", file.name)

            var img: MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile: RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }

            Log.d("image", img.toString())

            binding.btnAddProject.setOnClickListener {
                if (img != null) {
                    callProjectApi(img)
                }
            }
        }
    }


    private fun callProjectApi(image: MultipartBody.Part) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    val idCompany = sharePref.getString(SharePrefHelper.COM_ID).toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etProjectDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etProjectDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    service?.addProject(idCompany, projectName, projectDesc, projectDeadline, image)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("Data tdk masuk", result.toString())

            if (result is GeneralResponse) {
                Log.d("Data", result.toString())
                if (result.success) {
                    val intent = Intent(this@AddProjectActivity, HomeActivity::class.java)
                    Toast.makeText(this@AddProjectActivity, "Success Add Project", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
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

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}