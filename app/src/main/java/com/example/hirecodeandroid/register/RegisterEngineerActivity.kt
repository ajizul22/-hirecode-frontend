package com.example.hirecodeandroid.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterEngineerBinding
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    private lateinit var sharepref: SharePrefHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: RegisterApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_engineer
        )

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegisterApiService::class.java)
        sharepref = SharePrefHelper(this)

        binding.tvLogin.setOnClickListener {
            val intentRegister = Intent(this, LoginActivity::class.java)
            startActivity(intentRegister)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val noHp = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confrimPassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || noHp.isEmpty() || password.isEmpty() || confrimPassword.isEmpty()) {
                Toast.makeText(this, "All field must be fill!", Toast.LENGTH_LONG).show()
            } else if (password != confrimPassword){
                Toast.makeText(this, "Password & Confrim Password not same", Toast.LENGTH_LONG).show()
            } else {
//                saveSession(name, email, password)
                callRegisterApi(name, email, noHp, password,0)
            }
        }
    }

    private fun callRegisterApi(name:String, email:String, noHp: String, password:String, level: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.RegisterEngineerRequest(name, email, noHp, password, level)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is RegisterEngineerResponse) {
                if (result.success) {
                    val intent = Intent(this@RegisterEngineerActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


//    private fun saveSession(name: String, email: String, password: String){
//        sharepref.put(SharePrefHelper.KEY_EMAIL, email)
//        sharepref.put(SharePrefHelper.AC_NAME, name)
//        sharepref.put(SharePrefHelper.KEY_PASSWORD, password)
//    }
}