package com.example.hirecodeandroid.project.detailproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailProjectBinding
import com.example.hirecodeandroid.project.ProjectApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*


class DetailProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProjectBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ProjectApiService
    private lateinit var pagerAdapter: DetailProjectTabPagerAdapter
    private lateinit var sharePref: SharePrefHelper
    val img = "http://3.80.223.103:4000/image/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(ProjectApiService::class.java)

        val image = intent.getStringExtra("image")


//        Glide.with(binding.ivProject)
//            .load(img)
//            .placeholder(R.drawable.ic_project)
//            .error(R.drawable.ic_project)
//            .into(binding.ivProject)

        pagerAdapter = DetailProjectTabPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            binding.listHire.visibility = View.GONE
        }

        val projectId = intent.getIntExtra("project_id", 0)
        getDataproject(projectId)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.detail_project_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val delete = menu.findItem(R.id.delete)
        val update = menu.findItem(R.id.update)
        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            delete.isVisible = false
            update.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> Toast.makeText(this, "Delete Project Clicked", Toast.LENGTH_SHORT).show()
            R.id.update -> Toast.makeText(this, "Update Project Clicked", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getDataproject(id: Int) {
        coroutineScope.launch {

            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByProjectId(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is DetailProjectResponse) {
                if (result.success) {
                    binding.model = result.data[0]
                    val image = result.data[0].projectImage

                    Glide.with(binding.ivProject)
                        .load(img+image)
                        .placeholder(R.drawable.ic_project)
                        .error(R.drawable.ic_project)
                        .into(binding.ivProject)

                }
            }

        }
    }

}