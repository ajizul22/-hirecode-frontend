package com.example.hirecodeandroid.engineer.detailprofileengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.engineer.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.ActivityDetailProfileEngineerBinding
import com.example.hirecodeandroid.hire.addhire.AddHireActivity
import com.example.hirecodeandroid.engineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.SkillAdapter
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.skill.SkillModel
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.*

class DetailProfileEngineerActivity : AppCompatActivity(), SkillAdapter.OnItemSkillClickListener, DetailProfileEngineerContract.View {

    private lateinit var binding : ActivityDetailProfileEngineerBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: SkillApiService
    private lateinit var serviceEngineer: EngineerApiService
    private lateinit var sharedPref: SharePrefHelper
    private var presenter: DetailProfileEngineerContract.Presenter? = null
    var listSkill = ArrayList<SkillModel>()
    val img = "http://3.80.223.103:4000/image/"
    var image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_detail_profile_engineer
        )

        service = ApiClient.getApiClient(this)!!.create(SkillApiService::class.java)
        serviceEngineer = ApiClient.getApiClient(this)!!.create(EngineerApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = SharePrefHelper(this)

        presenter = DetailProfileEngineerPresenter(coroutineScope, serviceEngineer, service)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            binding.btnHire.visibility = View.GONE
        }

        binding.tvGit.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnHire.setOnClickListener {
            val intent = Intent(this, AddHireActivity::class.java)
            startActivity(intent)
        }

        pagerAdapter =
            EngineerTabPagerAdapter(
                supportFragmentManager
            )
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

//        binding.rvSkill.adapter = SkillAdapter(listSkill, this)
//        binding.rvSkill.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        binding.rvSkill.layoutManager = FlexboxLayoutManager(this)
        val adapter = SkillAdapter(listSkill, this)
        binding.rvSkill.adapter = adapter

    }

    override fun onResultSuccessEngineer(data: ListEngineerResponse.Engineer) {
        binding.model = data
        Glide.with(binding.root).load(img + data.engineerProfilePict).placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile).into(binding.ivAvatar)
    }

    override fun onResultSuccessSkill(list: List<SkillModel>) {
        (binding.rvSkill.adapter as SkillAdapter).addList(list)
        binding.rvSkill.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvSkill.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    override fun onItemSkillClicked(position: Int) {

    }

    override fun onStart() {
        super.onStart()
        val engineerId = sharedPref.getString(SharePrefHelper.ENG_ID_CLICKED)

        presenter?.bindToView(this)
        presenter?.callServiceEngineer(engineerId!!.toInt())
        presenter?.callServiceSkill(engineerId!!.toInt())
    }

    override fun onStop() {
        super.onStop()
        presenter?.unBind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}