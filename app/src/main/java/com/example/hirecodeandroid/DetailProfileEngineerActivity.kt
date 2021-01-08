package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.adapter.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.ActivityDetailProfileEngineerBinding
import com.example.hirecodeandroid.hire.AddHireActivity
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.SkillAdapter
import com.example.hirecodeandroid.skill.SkillApiService
import com.example.hirecodeandroid.skill.SkillModel
import com.example.hirecodeandroid.skill.SkillResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity
import kotlinx.coroutines.*

class DetailProfileEngineerActivity : AppCompatActivity(), SkillAdapter.OnItemSkillClickListener {

    private lateinit var binding : ActivityDetailProfileEngineerBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: SkillApiService
    private lateinit var sharedPref: SharePrefHelper
    var listSkill = ArrayList<SkillModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_detail_profile_engineer
        )

        service = ApiClient.getApiClient(this)!!.create(SkillApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = SharePrefHelper(this)


        val name = intent.getStringExtra("name")
        binding.tvName.text = name
        val jobTitle = intent.getStringExtra("jobTitle")
        binding.tvJobType.text = jobTitle
        val location = intent.getStringExtra("location")
        binding.tvAddress.text = location
        binding.tvEmailAddress.text = intent.getStringExtra("acEmail")
        binding.tvAddress.text = intent.getStringExtra("location")
        val image = intent.getStringExtra("image")
        val img = "http://3.80.223.103:4000/image/$image"

        Glide.with(binding.ivAvatar)
            .load(img)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(binding.ivAvatar)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tvGit.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnHire.setOnClickListener {
            val intent = Intent(this, AddHireActivity::class.java)
            startActivity(intent)
        }

        pagerAdapter = EngineerTabPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.rvSkill.adapter = SkillAdapter(listSkill, this)
        binding.rvSkill.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        getDataSkillEngineer(sharedPref.getString(SharePrefHelper.ENG_ID_CLICKED)?.toInt())

    }

    private fun getDataSkillEngineer(id: Int?) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getSkillByEngineer(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is SkillResponse) {
                Log.d("dataskill", result.toString())
                if(result.success) {
                    val list = result.data.map {
                        SkillModel(it.skillId, it.engineerId, it.skillName)
                    }
                    (binding.rvSkill.adapter as SkillAdapter).addList(list)
                }
            }
        }
    }

    override fun onItemClicked(position: Int) {

    }
}