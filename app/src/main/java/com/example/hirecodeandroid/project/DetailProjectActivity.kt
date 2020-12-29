package com.example.hirecodeandroid.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailProjectBinding

class DetailProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)

        val image = intent.getIntExtra("image",0)
        binding.ivProject.setImageResource(image)
        val title = intent.getStringExtra("title")
        binding.tvTitlePj.text = title
        val company = intent.getStringExtra("company")
        binding.tvCompanyPj.text = company
        val deadline = intent.getStringExtra("deadline")
        binding.tvDeadlinePj.text = deadline

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}