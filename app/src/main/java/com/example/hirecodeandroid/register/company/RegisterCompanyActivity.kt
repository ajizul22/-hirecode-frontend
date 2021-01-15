package com.example.hirecodeandroid.register.company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterCompanyBinding
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.register.RegisterApiService
import com.example.hirecodeandroid.register.engineer.RegisterEngineerViewModel
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel: RegisterCompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_company
        )
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(context = this)?.create(RegisterApiService::class.java)

        viewModel = ViewModelProvider(this).get(RegisterCompanyViewModel::class.java)
        if (service != null) {
            viewModel.setService(service)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val companyName = binding.etCompany.text.toString()
            val position = binding.etPosition.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || companyName.isEmpty() || position.isEmpty()) {
                Toast.makeText(this, "All field must be fill!", Toast.LENGTH_LONG).show()
            } else if (password != confirmPassword){
                Toast.makeText(this, "Password & Confrim Password not same", Toast.LENGTH_LONG).show()
            } else {
                viewModel.callRegisterApi(name, email, companyName, position, phone, password,1)
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