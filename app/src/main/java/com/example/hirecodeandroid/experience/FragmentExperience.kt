package com.example.hirecodeandroid.experience

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
import com.example.hirecodeandroid.HomeActivity
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentExperienceBinding
import com.example.hirecodeandroid.experience.addexperience.AddExperienceActivity
import com.example.hirecodeandroid.experience.updateexperience.UpdateExperienceActivity
import com.example.hirecodeandroid.remote.ApiClient
import com.example.hirecodeandroid.util.GeneralResponse
import com.example.hirecodeandroid.util.SharePrefHelper
import kotlinx.coroutines.*

class FragmentExperience : Fragment(), ExperienceRecyclerViewAdapter.OnListExpClickListener, ExperienceContract.View {

    private lateinit var binding : FragmentExperienceBinding
    private lateinit var sharePref: SharePrefHelper
    var listExperience = ArrayList<ExperienceModel>()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: ExperienceApiService
    private var presenter: ExperienceContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        sharePref = SharePrefHelper(requireContext())

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(requireContext())!!.create(ExperienceApiService::class.java)

        presenter = ExperiencePresenter(coroutineScope, service)

        binding.rvExperience.adapter =
            ExperienceRecyclerViewAdapter(listExperience, this)
        binding.rvExperience.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            binding.btnAddExp.visibility = View.GONE
        }

        binding.btnAddExp.setOnClickListener {
            val intent = Intent(requireContext(), AddExperienceActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResultSuccess(list: List<ExperienceModel>) {
        (binding.rvExperience.adapter as ExperienceRecyclerViewAdapter).addList(list)
        binding.rvExperience.visibility = View.VISIBLE
        binding.tvDataNotFound.visibility = View.GONE
    }

    override fun onResultFail(message: String) {
        binding.rvExperience.visibility = View.GONE
        binding.tvDataNotFound.visibility = View.VISIBLE
        binding.message = message
    }

    override fun onResultSuccessDeleteExperience() {
        showMessage("Experience success to deleted!")
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvExperience.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onHireDelete(position: Int) {
        showDialogDelete(position)
    }

    override fun onHireEdit(position: Int) {
        Toast.makeText(context, "Update ${listExperience[position].expId}", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdateExperienceActivity::class.java)
        intent.putExtra("id", listExperience[position].expId)
        intent.putExtra("position", listExperience[position].expPosition)
        intent.putExtra("company", listExperience[position].expCompany)
        intent.putExtra("start", listExperience[position].expStart)
        intent.putExtra("end", listExperience[position].expEnd)
        intent.putExtra("desc", listExperience[position].expDesc)
        startActivity(intent)
    }

    private fun showDialogDelete(position: Int) {
        val id = listExperience[position].expId
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Experience")
        builder.setMessage("Do you want to deleted this experience?")
        builder.setPositiveButton("Yes") { dialogInterface : DialogInterface, i : Int ->
            presenter?.callDeleteExperienceByIdService(id!!)
            moveActivity()
        }
        builder.setNegativeButton("No") { dialogInterface : DialogInterface, i : Int ->}
        builder.show()
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
        presenter?.bindToView(this)

        val id = sharePref.getString(SharePrefHelper.ENG_ID)
        val idForCompany = sharePref.getString(SharePrefHelper.ENG_ID_CLICKED)

        if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 0) {
            presenter?.callExperienceService(id!!.toInt())
        } else if (sharePref.getInteger(SharePrefHelper.AC_LEVEL) == 1) {
            presenter?.callExperienceService(idForCompany!!.toInt())
        }
    }

    override fun onStop() {
        presenter?.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}