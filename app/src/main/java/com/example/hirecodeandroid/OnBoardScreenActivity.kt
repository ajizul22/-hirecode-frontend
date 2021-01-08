package com.example.hirecodeandroid

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityOnBoardScreenBinding
import com.example.hirecodeandroid.databinding.LayoutDialogRegisterBinding
import com.example.hirecodeandroid.login.LoginActivity
import com.example.hirecodeandroid.register.RegisterCompanyActivity
import com.example.hirecodeandroid.register.RegisterEngineerActivity
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

        binding.btnRegister.setOnClickListener {
            showDialogThree()
        }

    }

    private fun showDialogThree() {
        val rootView = DataBindingUtil.inflate<LayoutDialogRegisterBinding>(layoutInflater, R.layout.layout_dialog_register, null, false)

        val dialog = AlertDialog.Builder(this)
            .setView(rootView.root)
            .setCancelable(false)
            .create()
        dialog.show()
        rootView.engineer.setOnClickListener {
            val intent = Intent(this, RegisterEngineerActivity::class.java)
            startActivity(intent)
        }

        rootView.tvEngineer.setOnClickListener {
            val intent = Intent(this, RegisterEngineerActivity::class.java)
            startActivity(intent)
        }

        rootView.company.setOnClickListener {
            val intent = Intent(this, RegisterCompanyActivity::class.java)
            startActivity(intent)
        }

        rootView.tvCompany.setOnClickListener {
            val intent = Intent(this, RegisterCompanyActivity::class.java)
            startActivity(intent)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
    }

}