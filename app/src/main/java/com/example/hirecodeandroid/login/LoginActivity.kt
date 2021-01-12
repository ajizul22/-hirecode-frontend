package com.example.hirecodeandroid.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        sharePref = SharePrefHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(LoginApiService:: class.java)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setSharedPreferences(sharePref)

        if (service != null) {
            viewModel.setLoginService(service)
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
                Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()

                viewModel.callLoginApi(email, password)

            }
        }

        subscribeLiveData()
        subscribeEngineerIdLiveData()
        subscribeCompanyIdLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isLoginLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeEngineerIdLiveData() {
        viewModel.isGetEngineerId.observe(this, Observer {
            Log.d("subscribeEngLiveData", "$it")

            if (it) {
                Toast.makeText(this, "Engineer Id Get", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Engineer Id failed to Get", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun subscribeCompanyIdLiveData() {
        viewModel.isGetCompanyId.observe(this, Observer {
            Log.d("subscribeCompLiveData", "$it")

            if (it) {
                Toast.makeText(this, "Company Id Get", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Company Id failed to Get", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnBoardScreenActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}