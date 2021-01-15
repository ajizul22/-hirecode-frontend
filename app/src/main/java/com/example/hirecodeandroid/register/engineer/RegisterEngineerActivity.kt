package com.example.hirecodeandroid.register.engineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterEngineerBinding
import com.example.hirecodeandroid.register.RegisterApiService
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: RegisterEngineerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_engineer
        )

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(RegisterApiService::class.java)

        viewModel = ViewModelProvider(this).get(RegisterEngineerViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        binding.tvLogin.setOnClickListener {
            val intentRegister = Intent(this, LoginActivity::class.java)
            startActivity(intentRegister)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val noHp = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || noHp.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All field must be fill!", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword){
                Toast.makeText(this, "Password & Confrim Password not same", Toast.LENGTH_LONG).show()
            } else {
                viewModel.callRegisterApi(name, email, noHp, password,0)
                subscribeLiveData()
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email has been registered", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

}