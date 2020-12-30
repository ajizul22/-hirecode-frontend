package com.example.hirecodeandroid.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityDetailProjectBinding

class DetailProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project)

        val title = intent.getStringExtra("title")
        binding.tvTitlePj.text = title
        val company = intent.getStringExtra("company")
        binding.tvCompanyPj.text = company
        val deadline = intent.getStringExtra("deadline")
        binding.tvDeadlinePj.text = deadline
        val desc = intent.getStringExtra("desc")
        binding.tvDescPj.text = desc
        val image = intent.getStringExtra("image")
        val img = "http://3.80.223.103:4000/image/$image"

        Glide.with(binding.ivProject)
            .load(img)
            .placeholder(R.drawable.ic_project)
            .error(R.drawable.ic_project)
            .into(binding.ivProject)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}