package com.example.hirecodeandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R

class ExperienceRecyclerViewAdapter : RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ExperienceHolder>() {

    val listImage = intArrayOf(R.drawable.tokopedia, R.drawable.ic_github, R.drawable.ic_linkedin, R.drawable.ic_ig, R.drawable.ic_chat)
    val listJobType = listOf("Web Developer", "Engineer", "Android Developer", "UI/UX", "Software Engineer")
    val listCompany = listOf("Tokopedia", "Github", "Linked In", "Instagram", "Whatsapp")
    val listDuration = listOf("6 Month", "8 Month", "18 Month", "3 Month", "6 Month")

    class ExperienceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.iv_icon)
        var jobType: TextView = itemView.findViewById(R.id.tv_job_type_ex)
        var CompanyName: TextView = itemView.findViewById(R.id.tv_company)
        var duration: TextView = itemView.findViewById(R.id.tv_duration)
    }

    override fun getItemCount(): Int = listImage.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceHolder {
        return ExperienceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_experience, parent,false))
    }

    override fun onBindViewHolder(holder: ExperienceHolder, position: Int) {
        holder.image.setImageResource(listImage[position])
        holder.jobType.text = listJobType[position]
        holder.CompanyName.text = listCompany[position]
        holder.duration.text = listDuration[position]
    }
}