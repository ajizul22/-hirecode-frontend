package com.example.hirecodeandroid.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.OnBoardScreenActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityLoginBinding
import com.example.hirecodeandroid.register.RegisterEngineerActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.reset_password.ResetPasswordActivity
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharePref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: LoginApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        sharePref = SharePrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(LoginApiService:: class.java)

        
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
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()

                callLoginApi(binding.etEmail.text.toString(), binding.etPassword.text.toString())
//                sharePref.put(SharePrefHelper.KEY_LOGIN, true)
//                sharePref.put(SharePrefHelper.AC_LEVEL, 1)
//                val intent = Intent(this, HomeActivity::class.java)
//                startActivity(intent)
//                finish()
            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnBoardScreenActivity::class.java)
        startActivity(intent)
    }

    private fun callLoginApi(email: String ,password: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is LoginResponse) {
                if (result?.success) {
                    sharePref.put(SharePrefHelper.AC_LEVEL, result.data?.accountLevel!!)
                    sharePref.put(SharePrefHelper.KEY_LOGIN, true)
                    sharePref.put(SharePrefHelper.TOKEN, result.data.token!!)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


}