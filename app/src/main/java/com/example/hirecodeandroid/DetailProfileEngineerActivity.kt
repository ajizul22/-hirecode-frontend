package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.adapter.EngineerTabPagerAdapter
import com.example.hirecodeandroid.databinding.ActivityDetailProfileEngineerBinding
import com.example.hirecodeandroid.webview.WebViewActivity

class DetailProfileEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailProfileEngineerBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_detail_profile_engineer
        )

        val name = intent.getStringExtra("name")
        binding.tvName.text = name
        val jobTitle = intent.getStringExtra("jobTitle")
        binding.tvJobType.text = jobTitle
        val location = intent.getStringExtra("location")
        binding.tvAddress.text = location
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


    }
}