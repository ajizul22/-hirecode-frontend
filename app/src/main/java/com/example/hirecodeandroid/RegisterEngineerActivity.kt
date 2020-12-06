package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_engineer)

        val tvRegister = findViewById<TextView>(R.id.tv_login)

        tvRegister.setOnClickListener {
            val intentRegister = Intent(this, LoginActivity::class.java)
            startActivity(intentRegister)
        }
    }
}