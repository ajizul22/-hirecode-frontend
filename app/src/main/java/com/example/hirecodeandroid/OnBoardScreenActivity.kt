package com.example.hirecodeandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityOnBoardScreenBinding
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.temporary.LoginCompanyActivity
import com.example.hirecodeandroid.util.SharePrefHelper

class OnBoardScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardScreenBinding
    lateinit var sharePref: SharePrefHelper
    var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board_screen)
        sharePref = SharePrefHelper(this)

        isRemembered = sharePref.getBoolean(SharePrefHelper.KEY_LOGIN)
        if (isRemembered) {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLoginEngineer.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        binding.btnLoginCompany.setOnClickListener {
            val intentLogin = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentLogin)
        }

    }
}