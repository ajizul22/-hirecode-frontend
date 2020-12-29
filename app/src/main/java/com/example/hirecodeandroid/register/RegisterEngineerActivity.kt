package com.example.hirecodeandroid.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ActivityRegisterEngineerBinding
import com.example.hirecodeandroid.util.SharePrefHelper

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterEngineerBinding
    private lateinit var sharepref: SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_register_engineer
        )
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
                saveSession(name, email, password)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
    private fun saveSession(name: String, email: String, password: String){
        sharepref.put(SharePrefHelper.KEY_EMAIL, email)
        sharepref.put(SharePrefHelper.AC_NAME, name)
        sharepref.put(SharePrefHelper.KEY_PASSWORD, password)
    }
}