package com.example.hirecodeandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityLoginBinding
import com.example.hirecodeandroid.fragment.FragmentProfile
import com.example.hirecodeandroid.util.SharedPrefUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPref: SharedPreferences
    var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        sharedPref = applicationContext.getSharedPreferences(SharedPrefUtil.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        isRemembered = sharedPref.getBoolean(SharedPrefUtil.KEY_LOGIN, false)
        if (isRemembered) {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvForgotpw.setOnClickListener {
            val intentResetPw = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intentResetPw)
        }

        binding.tvRegister.setOnClickListener {
            val intentRegister = Intent(this, RegisterEngineerActivity::class.java)
            startActivity(intentRegister)
        }

        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            } else {
                sharedPref.edit().putString(SharedPrefUtil.KEY_EMAIL, email).apply()
                sharedPref.edit().putString(SharedPrefUtil.KEY_PASSWORD, password).apply()
                sharedPref.edit().putBoolean(SharedPrefUtil.KEY_LOGIN, true).apply()

                Toast.makeText(this, "Information Saved!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}