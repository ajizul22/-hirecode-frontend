package com.example.hirecodeandroid.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListExperienceBinding
import com.example.hirecodeandroid.experience.ExperienceModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ExperienceRecyclerViewAdapter(private val listExp: ArrayList<ExperienceModel>) : RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ExperienceHolder>() {

    fun addList(list: List<ExperienceModel>) {
        listExp.clear()
        listExp.addAll(list)
        notifyDataSetChanged()
    }

    class ExperienceHolder(val binding: ItemListExperienceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listExp.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceHolder {
        return ExperienceHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_list_experience, parent,false))
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExperienceHolder, position: Int) {
        val item = listExp[position]

        val dateStart = item.expStart!!.split("T")[0]
        val dateEnd = item.expEnd!!.split("T")[0]
        val sum = ChronoUnit.MONTHS.between(LocalDate.parse(dateStart).withDayOfMonth(1), LocalDate.parse(dateEnd).withDayOfMonth(1))

        holder.binding.tvJobTypeEx.text = item.expPosition
        holder.binding.tvCompany.text = item.expCompany
        holder.binding.tvStart.text = dateStart
        holder.binding.tvEnd.text = dateEnd
        holder.binding.tvDescEx.text = item.expDesc
        holder.binding.tvDuration.text = "$sum Month"
    }
}