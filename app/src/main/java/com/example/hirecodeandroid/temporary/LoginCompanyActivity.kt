package com.example.hirecodeandroid.temporary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.hirecodeandroid.MainActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.register.company.RegisterCompanyActivity
import com.example.hirecodeandroid.reset_password.ResetPasswordActivity

class LoginCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_company)

        val btnLoginActivity = findViewById<Button>(R.id.btn_login)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val tvResetPw = findViewById<TextView>(R.id.tv_forgotpw)

        tvResetPw.setOnClickListener {
            val intentResetPw = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intentResetPw)
        }

        tvRegister.setOnClickListener {
            val intentRegister = Intent(this, RegisterCompanyActivity::class.java)
            startActivity(intentRegister)
        }

        btnLoginActivity.setOnClickListener {
            val intentProfile = Intent(this, MainActivity::class.java)
            val email = etEmail.text.toString()
            intentProfile.putExtra("email", email)
            startActivity(intentProfile)
            finish()
        }
    }
}