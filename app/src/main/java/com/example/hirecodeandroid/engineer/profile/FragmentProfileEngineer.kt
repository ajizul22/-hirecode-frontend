package com.example.hirecodeandroid.engineer.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentProfileBinding
import com.example.hirecodeandroid.engineer.editprofile.EditProfileEngineerActivity
import com.example.hirecodeandroid.engineer.EngineerTabPagerAdapter
import com.example.hirecodeandroid.listengineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.*
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity
import kotlinx.coroutines.*


class FragmentProfileEngineer: Fragment(), SkillAdapter.OnItemSkillClickListener {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    private lateinit var serviceSkill: SkillApiService
    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var sharedPref: SharePrefHelper
    val img = "http://3.80.223.103:4000/image/"
    var listSkill = ArrayList<SkillModel>()
    var image: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        sharedPref = SharePrefHelper(requireContext())

        service = ApiClient.getApiClient(requireContext())!!.create(EngineerApiService::class.java)
        serviceSkill = ApiClient.getApiClient(requireContext())!!.create(SkillApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        pagerAdapter =
            EngineerTabPagerAdapter(
                childFragmentManager
            )
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.rvSkill.adapter = SkillAdapter(listSkill, this)
        binding.rvSkill.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileEngineerActivity::class.java)
            intent.putExtra("image", image)
            startActivity(intent)
        }

        binding.tvGit.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddSkill.setOnClickListener {
            val intent = Intent(activity, AddSkillActivity::class.java)
            startActivity(intent)
        }

        val id = sharedPref.getString(SharePrefHelper.ENG_ID)
        getDataEngineer(id!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = sharedPref.getString(SharePrefHelper.KEY_EMAIL)
        binding.tvEmailAddress.text = email

        getDataSkillEngineer(sharedPref.getString(SharePrefHelper.ENG_ID)?.toInt())
    }

    private fun getDataEngineer(id: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getDataEngById(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is ListEngineerResponse) {
                Log.d("data engineer by id", result.toString())
                binding.model = result.data[0]
                image = result.data[0].engineerProfilePict
                Glide.with(requireContext()).load(img + result.data[0].engineerProfilePict).placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile).into(binding.ivAvatar)
            }
        }
    }

    private fun getDataSkillEngineer(id: Int?) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    serviceSkill?.getSkillByEngineer(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is SkillResponse) {
                Log.d("dataskill", result.toString())
                if(result.success) {
                    val list = result.data.map {
                        SkillModel(it.skillId, it.engineerId, it.skillName)
                    }
                    (binding.rvSkill.adapter as SkillAdapter).addList(list)
                }
            }
        }
    }

    private fun deleteSkill(skillId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    serviceSkill?.deleteSkill(skillId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                if (result.success) {
                    showMessage("Skill success to deleted!")
                }
            }
        }
    }

    private fun showDialogDeleteSkill(position: Int) {
        val id = listSkill[position].skillId
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Skill")
        builder.setMessage("Do you want to delete ${listSkill[position].skillName} from your skill?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            deleteSkill(id!!)
            moveActivity()
        }
        builder.setNegativeButton("No") { dialogInterface : DialogInterface, i : Int ->}
        builder.show()
    }

    override fun onItemSkillClicked(position: Int) {
        showDialogDeleteSkill(position)
    }

    private fun showMessage(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        activity?.finish()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}