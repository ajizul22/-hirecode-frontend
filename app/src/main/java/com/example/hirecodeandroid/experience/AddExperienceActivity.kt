package com.example.hirecodeandroid.experience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityAddExperienceBinding

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExperienceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_experience)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}