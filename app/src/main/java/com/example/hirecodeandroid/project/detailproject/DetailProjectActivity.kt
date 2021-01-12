package com.example.hirecodeandroid.project.detailproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var pagerAdapter: DetailProjectTabPagerAdapter
    private lateinit var sharePref: SharePrefHelper
    private lateinit var viewModel: DetailProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(this)?.create(ProjectApiService::class.java)

        viewModel = ViewModelProvider(this).get(DetailProjectViewModel::class.java)
        viewModel.setBinding(binding)

        if (service != null) {
            viewModel.setService(service)
        }

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
        viewModel.getDataproject(projectId)
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isProjectLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            }
        })
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

}