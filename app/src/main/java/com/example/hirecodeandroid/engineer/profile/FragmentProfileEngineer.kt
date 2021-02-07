package com.example.hirecodeandroid.engineer.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
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
import com.example.hirecodeandroid.company.home.HomeSkillAdapter
import com.example.hirecodeandroid.databinding.FragmentProfileBinding
import com.example.hirecodeandroid.engineer.editprofile.EditProfileEngineerActivity
import com.example.hirecodeandroid.engineer.EngineerTabPagerAdapter
import com.example.hirecodeandroid.engineer.EngineerApiService
import com.example.hirecodeandroid.listengineer.ListEngineerResponse
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.skill.*
import com.example.hirecodeandroid.util.SharePrefHelper
import com.example.hirecodeandroid.webview.WebViewActivity
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.coroutines.*


class FragmentProfileEngineer: Fragment(), SkillAdapter.OnItemSkillClickListener, ProfileEngineerContract.View {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: EngineerApiService
    private lateinit var serviceSkill: SkillApiService
    private lateinit var binding : FragmentProfileBinding
    private lateinit var pagerAdapter: EngineerTabPagerAdapter
    private lateinit var sharedPref: SharePrefHelper
    val img = "http://3.80.223.103:4000/image/"
    var listSkill = ArrayList<SkillModel>()
    var image: String? = null
    private var presenter: ProfileEngineerContract.Presenter? = null

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

        presenter = ProfileEngineerPresenter(coroutineScope, service, serviceSkill)

        pagerAdapter =
            EngineerTabPagerAdapter(
                childFragmentManager
            )
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

//        binding.rvSkill.adapter = SkillAdapter(listSkill, this)
//        binding.rvSkill.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.rvSkill.layoutManager = FlexboxLayoutManager(requireContext())
        val adapter = SkillAdapter(listSkill, this)
        binding.rvSkill.adapter = adapter

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResultSuccessEngineer(data: ListEngineerResponse.Engineer) {
        binding.model = data
        Glide.with(requireContext()).load(img + data.engineerProfilePict).placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile).into(binding.ivAvatar)

    }

    override fun onResultSuccessSkill(list: List<SkillModel>) {
        (binding.rvSkill.adapter as SkillAdapter).addList(list)
        binding.rvSkill.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvSkill.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }

    override fun onResultSuccessDeleteSkill() {
        showMessage("Skill success to deleted!")
    }

    private fun showDialogDeleteSkill(position: Int) {
        val id = listSkill[position].skillId
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Skill")
        builder.setMessage("Do you want to delete ${listSkill[position].skillName} from your skill?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            presenter?.callServiceDeleteSkill(id!!)
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

    override fun onStart() {
        super.onStart()
        val engineerId = sharedPref.getString(SharePrefHelper.ENG_ID)

        presenter?.bindToView(this)
        presenter?.callServiceEngineer(engineerId!!.toInt())
        presenter?.callServiceSkill(engineerId!!.toInt())
    }

    override fun onStop() {
        presenter?.unBind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}