package com.example.hirecodeandroid.temporary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hirecodeandroid.R

class EditProfileCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_company)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)

        btnCancel.setOnClickListener {
            val intentBack = Intent(this, ProfileCompanyActivity::class.java)
            startActivity(intentBack)
        }

    }
}