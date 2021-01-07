package com.example.hirecodeandroid.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityEditProfileCompanyBinding

class EditProfileCompanyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_company)

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}