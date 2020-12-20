package com.example.hirecodeandroid.temporary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.hirecodeandroid.LoginCompanyActivity
import com.example.hirecodeandroid.R

class ProfileCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_company)

        val tvEmail = findViewById<TextView>(R.id.tv_email_address)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)
        val ivArrowBack = findViewById<ImageButton>(R.id.iv_ic_back)

        ivArrowBack.setOnClickListener {
            val intentLogin = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentLogin)
        }

        btnEditProfile.setOnClickListener {
            val intentEditProfile = Intent(this, EditProfileCompanyActivity::class.java)
            startActivity(intentEditProfile)
        }

        val email = intent.getStringExtra("email")
        tvEmail.text = email
    }
}