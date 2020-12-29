package com.example.hirecodeandroid.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.temporary.LoginCompanyActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterCompanyBinding

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_company
        )

        binding.tvLogin.setOnClickListener {
            val intentRegister = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentRegister)
        }
    }
}