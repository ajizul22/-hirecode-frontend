package com.example.hirecodeandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_engineer)

        val tvEmail = findViewById<TextView>(R.id.tv_email_address)

        val email = intent.getStringExtra("email")
        tvEmail.text = email
    }
}