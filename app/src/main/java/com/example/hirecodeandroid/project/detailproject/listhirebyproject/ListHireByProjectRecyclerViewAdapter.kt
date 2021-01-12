package com.example.hirecodeandroid.project.detailproject.listhirebyproject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListHireByProjectBinding

class ListHireByProjectRecyclerViewAdapter(private val listHire: ArrayList<HireByProjectModel>) :
    RecyclerView.Adapter<ListHireByProjectRecyclerViewAdapter.HireListHolder>(){

    fun addList(list: List<HireByProjectModel>) {
        listHire.clear()
        listHire.addAll(list)
        notifyDataSetChanged()
    }

    class HireListHolder(val binding: ItemListHireByProjectBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireListHolder {
        return HireListHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_hire_by_project,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listHire.size

    override fun onBindViewHolder(holder: HireListHolder, position: Int) {
        val item = listHire[position]
        val img = "http://3.80.223.103:4000/image/${item.engineerPhoto}"

        holder.binding.tvNameTalent.text = item.engineerName
        holder.binding.tvPrice.text = item.hirePrice.toString()
        holder.binding.tvStatus.text = item.hireStatus
        holder.binding.tvTypeTalent.text = item.engineerJobTitle

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(holder.binding.ivEngineer)

        if (item.hireStatus == "wait" || item.hireStatus == "") {
            holder.binding.tvStatus.text = "Wait for engineer response"
            holder.binding.tvStatus.setTextColor(Color.rgb(188, 15, 15))
        } else if (item.hireStatus == "approve") {
            holder.binding.tvStatus.setTextColor(Color.rgb(60, 122, 62))
        }
    }
}