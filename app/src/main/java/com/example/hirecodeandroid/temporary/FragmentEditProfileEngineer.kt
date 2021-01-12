package com.example.hirecodeandroid.temporary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.FragmentEditProfileEngineerBinding
import com.example.hirecodeandroid.engineer.profile.FragmentProfileEngineer

class FragmentEditProfileEngineer : Fragment() {

    private lateinit var binding: FragmentEditProfileEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile_engineer,container,false)
        binding.btnCancel.setOnClickListener {
            val fragment =
                FragmentProfileEngineer()
            fragmentManager!!.beginTransaction().replace(R.id.fg_container, fragment).commit()
        }
        return binding.root
    }
    
}