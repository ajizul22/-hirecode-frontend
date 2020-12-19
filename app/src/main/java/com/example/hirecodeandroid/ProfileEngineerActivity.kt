package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityProfileEngineerBinding
import com.example.hirecodeandroid.adapter.EngineerTabPagerAdapter
import kotlinx.android.synthetic.main.activity_profile_engineer.*

class ProfileEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileEngineerBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile_engineer)

        val tvEmail = findViewById<TextView>(R.id.tv_email_address)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val email = intent.getStringExtra("email")
        tvEmail.text = email

        pagerAdapter = EngineerTabPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}