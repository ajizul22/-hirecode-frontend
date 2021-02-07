package com.example.hirecodeandroid.portfolio.detailportfolio

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailPortfolioBinding
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.portfolio.updateportfolio.UpdatePortfolioActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class DetailPortfolioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPortfolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharePref: SharePrefHelper
    private lateinit var viewModel: DetailPortfolioViewModel
    var idPort: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(this)?.create(PortofolioApiService::class.java)

        viewModel = ViewModelProvider(this).get(DetailPortfolioViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        val id = intent.getIntExtra("id", 0)
        viewModel.getDataPortfolio(id)
        subscribeLiveData()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            binding.button.visibility = View.GONE
        }

        binding.btnDelete.setOnClickListener {
            showDialogDelete()
        }

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdatePortfolioActivity::class.java)
            intent.putExtra("id", idPort)
            startActivity(intent)
        }

    }

    private fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.GONE
                binding.content.visibility = View.VISIBLE

                if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                    binding.button.visibility = View.VISIBLE
                }

            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.content.visibility = View.GONE
                binding.button.visibility = View.GONE
            }
        })

        viewModel.listModel.observe(this, Observer {
            binding.model = it[0]
            idPort = it[0].portoId
            val img = "http://3.80.223.103:4000/image/"
            Glide.with(binding.root)
                .load(img+it[0].portoImage)
                .placeholder(R.drawable.ic_project)
                .error(R.drawable.ic_project)
                .into(binding.ivPortfolio)
        })
    }

    private fun subscribeDeleteLiveData() {
        viewModel.isDeleteLiveData.observe(this, Observer {
            if (it) {
                showMessage("Portfolio success to deleted!")
            } else {
                showMessage("Portfolio failed to deleted!")
            }
        })
    }

    private fun showDialogDelete() {
        val id = intent.getIntExtra("id", 0)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Portfolio")
        builder.setMessage("Do you want to deleted this portfolio?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            viewModel.deletePortfolio(id)
            subscribeDeleteLiveData()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface : DialogInterface, i : Int ->}
        builder.show()
    }

    private fun showMessage(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}