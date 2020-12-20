package com.example.hirecodeandroid

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.databinding.ActivityMainBinding
import com.example.hirecodeandroid.fragment.FragmentHomeCompany
import com.example.hirecodeandroid.fragment.FragmentProfileCompany
import com.example.hirecodeandroid.fragment.FragmentSearch
import com.example.hirecodeandroid.util.SharedPrefUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(binding.topToolbar)
        sharedPref = applicationContext.getSharedPreferences(SharedPrefUtil.SHARED_PREF_NAME, Context.MODE_PRIVATE)

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
                    val fragmentProfile = FragmentProfileCompany()
                    supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentProfile).commit()
                    true
                } else -> false
            }
        }
    }

}