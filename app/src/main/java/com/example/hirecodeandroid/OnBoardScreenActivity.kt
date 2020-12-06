package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OnBoardScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board_screen)

        val btnLoginActivity = findViewById<Button>(R.id.btn_login_engineer)
        val btnLoginActivityCompany = findViewById<Button>(R.id.btn_login_company)

        btnLoginActivity.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        btnLoginActivityCompany.setOnClickListener {
            val intentLogin = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentLogin)
        }
    }
}