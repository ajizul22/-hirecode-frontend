package com.example.hirecodeandroid

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.hirecodeandroid.company.FragmentHomeCompany
import com.example.hirecodeandroid.company.FragmentProfileCompany
import com.example.hirecodeandroid.databinding.ActivityHomeBinding
import com.example.hirecodeandroid.databinding.LayoutDialogLogoutBinding
import com.example.hirecodeandroid.fragment.*
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
        val fragmentHomeEng = FragmentHomeEngineer()

        if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
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
                    val fragmentProjectEng = FragmentProjectEngineer()
                    val fragmentProjectCom = FragmentProjectCompany()

                    if(sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container,fragmentProjectEng).commit()
                    } else if (sharedPref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
//                        val intent = Intent(this, ProjectListActivity::class.java)
//                        startActivity(intent)
                        supportFragmentManager.beginTransaction().replace(R.id.fg_container, fragmentProjectCom).commit()
                    }
                    binding.tvToolbarTitle.text = "Project"
                    true
                }
                R.id.page_4 -> {
                    val fragmentProfile = FragmentProfileEngineer()
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
            sharedPref.put(SharePrefHelper.KEY_LOGIN, false)
            val intent = Intent(this,OnBoardScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

//    override fun passDataEng(
//        image: Int,
//        name: String,
//        jobTitle: String,
//        skillOne: String,
//        skillTwo: String,
//        skillThree: String
//    )  {
//        val bundle = Bundle()
//        bundle.putInt("image", image)
//        bundle.putString("name", name)
//        bundle.putString("title", jobTitle)
//        bundle.putString("skill1", skillOne)
//        bundle.putString("skill2", skillTwo)
//        bundle.putString("skill3", skillThree)
//
//        val transaction = this.supportFragmentManager.beginTransaction()
//        val fragmentDetail = FragmentDetailProfileEngineer()
//        fragmentDetail.arguments = bundle
//        binding.tvToolbarTitle.text = "Detail Engineer"
//        transaction.replace(R.id.fg_container, fragmentDetail).addToBackStack("tag")
//        transaction.commit()
//    }

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