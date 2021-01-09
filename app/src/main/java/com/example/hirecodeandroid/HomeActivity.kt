package com.example.hirecodeandroid

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.company.FragmentHomeCompany
import com.example.hirecodeandroid.company.FragmentProfileCompany
import com.example.hirecodeandroid.databinding.ActivityHomeBinding
import com.example.hirecodeandroid.engineer.FragmentHomeEngineer
import com.example.hirecodeandroid.engineer.FragmentProfileEngineer
import com.example.hirecodeandroid.search.*
import com.example.hirecodeandroid.hire.FragmentHireEngineer
import com.example.hirecodeandroid.project.FragmentProjectCompany
import com.example.hirecodeandroid.temporary.FragmentDetailProject
import com.example.hirecodeandroid.util.PassDataProject
import com.example.hirecodeandroid.util.SharePrefHelper

class HomeActivity : AppCompatActivity(), PassDataProject {

    private lateinit var binding : ActivityHomeBinding
    lateinit var sharedPref: SharePrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sharedPref = SharePrefHelper(this)

        setSupportActionBar(binding.topToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val fragmentHomeCom = FragmentHomeCompany()
        val fragmentHomeEng =
            FragmentHomeEngineer()

        val name = sharedPref.getString(SharePrefHelper.ENG_NAME)



        if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            val bundle = Bundle()
            bundle.putString("name", name)
            fragmentHomeEng.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentHomeEng).commit()
            binding.tvToolbarTitle.text = ""
        } else if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.fg_container, fragmentHomeCom).commit()
            binding.tvToolbarTitle.text = "Home"
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentHomeCom).commit()
                        binding.tvToolbarTitle.text = "Home"
                    } else if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                        val bundle = Bundle()
                        bundle.putString("name", name)
                        fragmentHomeEng.arguments = bundle
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container, fragmentHomeEng).commit()
                        binding.tvToolbarTitle.text = ""
                    }
                    true
                }
                R.id.page_2 -> {
                    val fragmentSearch = FragmentSearch()
                    supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentSearch).commit()
                    binding.tvToolbarTitle.text = "Search"
                    true
                }
                R.id.page_3 -> {
                    val fragmentHireEngineer =
                        FragmentHireEngineer()
                    val fragmentProjectCom = FragmentProjectCompany()

                    if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentHireEngineer).commit()
                        binding.tvToolbarTitle.text = "Hire"
                    } else if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container, fragmentProjectCom).commit()
                        binding.tvToolbarTitle.text = "Project"
                    }
                    true
                }
                R.id.page_4 -> {
                    val fragmentProfile =
                        FragmentProfileEngineer()
                    val fragmentCompany =
                        FragmentProfileCompany()

                    if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentProfile).commit()
                    } else if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container, fragmentCompany).commit()
                    }
                    binding.tvToolbarTitle.text = "Profile"
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
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Log Out")
        builder.setMessage("are you sure? Logging out will remove all data from this device.")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            sharedPref.clear()
            val intent = Intent(this,OnBoardScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialogInterface : DialogInterface, i : Int ->}
        builder.show()
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun passDataProject(image: Int, title: String, company: String, deadline: String) {
        val bundle = Bundle()
        bundle.putInt("image", image)
        bundle.putString("title", title)
        bundle.putString("company", company)
        bundle.putString("deadline", deadline)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentDetail =
            FragmentDetailProject()
        fragmentDetail.arguments = bundle
        binding.tvToolbarTitle.text = "Detail Project"
        transaction.replace(R.id.fg_container, fragmentDetail)
        transaction.commit()
    }
}