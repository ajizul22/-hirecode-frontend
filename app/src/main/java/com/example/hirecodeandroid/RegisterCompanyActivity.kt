package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_company)

        val tvRegister = findViewById<TextView>(R.id.tv_login)

        tvRegister.setOnClickListener {
            val intentRegister = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentRegister)
        }
    }
}