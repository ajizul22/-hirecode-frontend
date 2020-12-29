package com.example.hirecodeandroid.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.temporary.LoginCompanyActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterCompanyBinding
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.remote.ApiClient
import kotlinx.coroutines.*

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterCompanyBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: RegisterApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_company
        )
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegisterApiService::class.java)

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
                callRegistrasiApi(name, email, companyName, position, phone, password,1)
            }
        }
    }

    private fun callRegistrasiApi(name: String, email: String, companyName: String, position: String, phone:String, password:String, level: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.RegisterCompanyRequest(name,email,phone,password, level, companyName,position)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is RegisterCompanyResponse) {
                if (result.success) {
                    val intent = Intent(this@RegisterCompanyActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}