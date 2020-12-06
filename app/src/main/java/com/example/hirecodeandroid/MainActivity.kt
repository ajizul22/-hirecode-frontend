package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "activity: onCreate()", Toast.LENGTH_SHORT).show()
        
        val btnLoginActivity = findViewById<Button>(R.id.btn_login)
        val btnRegisterEngineerActivity = findViewById<Button>(R.id.btn_register_engineer)
        val btnRegisterCompanyActivity = findViewById<Button>(R.id.btn_register_company)
        val btnResetPasswordActivity = findViewById<Button>(R.id.btn_reset_password)
        val btnOnBoardScreenActivity = findViewById<Button>(R.id.btn_onboard)
        val btnProfileEngineerActivity = findViewById<Button>(R.id.btn_profile_engineer)
        val btnProfileCompanyActivity = findViewById<Button>(R.id.btn_profile_company)

        btnProfileCompanyActivity.setOnClickListener {
            val intentProfileComp = Intent(this, ProfileCompanyActivity::class.java)
            startActivity(intentProfileComp)
        }

        btnProfileEngineerActivity.setOnClickListener {
            val intentProfileEng = Intent(this, ProfileEngineerActivity::class.java)
            startActivity(intentProfileEng)
        }

        btnLoginActivity.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        btnRegisterEngineerActivity.setOnClickListener {
            val intentRegisterEng = Intent(this, RegisterEngineerActivity::class.java)
            startActivity(intentRegisterEng)
        }

        btnRegisterCompanyActivity.setOnClickListener {
            val intentRegisterCom = Intent(this, RegisterCompanyActivity::class.java)
            startActivity(intentRegisterCom)
        }

        btnResetPasswordActivity.setOnClickListener {
            val intentResetPassword = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intentResetPassword)
        }

        btnOnBoardScreenActivity.setOnClickListener {
            val intentOnBoard = Intent(this, OnBoardScreenActivity::class.java)
            startActivity(intentOnBoard)
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("lifecycle", "onStart()")
        Toast.makeText(this, "activity: onStart()", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

        Log.d("lifecycle", "onResume()")
        Toast.makeText(this, "activity: onResume()", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()

        Log.d("lifecycle", "onPause()")
        Toast.makeText(this, "activity: onPause()", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()

        Log.d("lifecycle", "onStop()")
        Toast.makeText(this, "activity: onStop()", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()

        Log.d("lifecycle", "onRestart()")
        Toast.makeText(this, "activity: onRestart()", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("lifecycle", "onDestroy()")
        Toast.makeText(this, "activity: onDestroy()", Toast.LENGTH_SHORT).show()
    }
}