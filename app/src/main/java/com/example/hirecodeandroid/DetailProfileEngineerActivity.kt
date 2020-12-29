package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
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

        val image = intent.getIntExtra("image",0)
        binding.ivAvatar.setImageResource(image)
        val name = intent.getStringExtra("name")
        binding.tvName.text = name
        val jobTitle = intent.getStringExtra("title")
        binding.tvJobType.text = jobTitle
        val skillOne = intent.getStringExtra("skill1")
        binding.tvSkill1.text = skillOne
        val skillTwo = intent.getStringExtra("skill2")
        binding.tvSkill2.text = skillTwo
        val skillThree = intent.getStringExtra("skill3")
        binding.tvSkill3.text = skillThree

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