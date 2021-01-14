package com.example.hirecodeandroid.portfolio.detailportfolio

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailPortfolioBinding
import com.example.hirecodeandroid.portfolio.PortofolioApiService
import com.example.hirecodeandroid.portfolio.PortofolioResponse
import com.example.hirecodeandroid.portfolio.UpdatePortfolioActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class DetailPortfolioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPortfolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharePref: SharePrefHelper
    private lateinit var service: PortofolioApiService
    val img = "http://3.80.223.103:4000/image/"
    var idPort: Int? = null
    var appName: String? = null
    var desc: String? = null
    var linkPub: String? = null
    var linkRepo: String? = null
    var workplace: String? = null
    var portType: String? = null
    var image: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portfolio)
        sharePref = SharePrefHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(PortofolioApiService::class.java)

        val id = intent.getIntExtra("id", 0)
        getDataPortfolio(id)

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
            intent.putExtra("name", appName)
            intent.putExtra("desc", desc)
            intent.putExtra("pub", linkPub)
            intent.putExtra("repo", linkRepo)
            intent.putExtra("workplace", workplace)
            intent.putExtra("type", portType)
            intent.putExtra("image", image)
            startActivity(intent)
        }


    }

    private fun getDataPortfolio(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getPortfolioByIdPort(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is PortofolioResponse) {
                if (result.success) {
                    binding.model = result.data[0]
                    idPort = result.data[0].portoId
                    appName = result.data[0].portoAppName
                    desc = result.data[0].portoDesc
                    linkPub = result.data[0].portoLinkPub
                    linkRepo = result.data[0].portoLinkRepo
                    workplace = result.data[0].portoWorkPlace
                    portType = result.data[0].portoType
                    image = result.data[0].portoImage
                    Glide.with(this@DetailPortfolioActivity).load(img + result.data[0].portoImage).placeholder(R.drawable.ic_project)
                        .error(R.drawable.ic_project).into(binding.ivPortfolio)
                }
            }
        }
    }

    private fun deletePortofolio(id: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.deletePortfolio(id)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    showMessage("Portfolio success to deleted!")
                }
            }
        }
    }

    private fun showDialogDelete() {
        val id = intent.getIntExtra("id", 0)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Portfolio")
        builder.setMessage("Do you want to deleted this portfolio?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            deletePortofolio(id)
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