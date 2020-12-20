package com.example.hirecodeandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityHomeBinding
import com.example.hirecodeandroid.databinding.LayoutDialogLogoutBinding
import com.example.hirecodeandroid.fragment.FragmentHomeCompany
import com.example.hirecodeandroid.fragment.FragmentProfileEngineer
import com.example.hirecodeandroid.fragment.FragmentSearch
import com.example.hirecodeandroid.util.SharedPrefUtil

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = applicationContext.getSharedPreferences(SharedPrefUtil.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        setSupportActionBar(binding.topToolbar)

        val fragmentHome = FragmentHomeCompany()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fg_container, fragmentHome).commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fg_container, fragmentHome).commit()
                    true
                }
                R.id.page_2 -> {
                    val fragmentSearch = FragmentSearch()
                    supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentSearch).commit()
                    true
                }
                R.id.page_4 -> {
                    val fragmentProfile = FragmentProfileEngineer()
                    supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentProfile).commit()
                    true
                } else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> showDialogLogout()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialogLogout() {
        val rootView = DataBindingUtil.inflate<LayoutDialogLogoutBinding>(layoutInflater, R.layout.layout_dialog_logout,null,false)
        val dialog = AlertDialog.Builder(this)
            .setView(rootView.root)
            .setCancelable(false)
            .create()
        dialog.show()
        rootView.btnCancelLogout.setOnClickListener {
            dialog.dismiss()
        }
        rootView.btnOkLogout.setOnClickListener {
            sharedPref.edit().clear().apply()
            val intent = Intent(this,OnBoardScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}