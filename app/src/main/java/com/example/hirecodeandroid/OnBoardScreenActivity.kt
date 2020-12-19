package com.example.hirecodeandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityOnBoardScreenBinding
import com.example.hirecodeandroid.util.SharedPrefUtil
import kotlinx.android.synthetic.main.activity_on_board_screen.*

class OnBoardScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardScreenBinding
    lateinit var sharedPref: SharedPreferences
    var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board_screen)
        sharedPref = applicationContext.getSharedPreferences(SharedPrefUtil.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        isRemembered = sharedPref.getBoolean(SharedPrefUtil.KEY_LOGIN, false)
        if (isRemembered) {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


        val btnLoginActivity = findViewById<Button>(R.id.btn_login_engineer)
        val btnLoginActivityCompany = findViewById<Button>(R.id.btn_login_company)

        btnLoginActivity.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        btnLoginActivityCompany.setOnClickListener {
            val intentLogin = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentLogin)
        }

        tv_or.setOnClickListener {
            val intent= Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}