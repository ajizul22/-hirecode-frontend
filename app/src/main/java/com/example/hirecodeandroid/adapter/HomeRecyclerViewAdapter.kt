package com.example.hirecodeandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.dataclass.ListEngineerDataClass

class HomeRecyclerViewAdapter(private val itemList: List<ListEngineerDataClass>, var clickListener: OnItemClickListener)
    : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {


    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_engineer, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentItem = itemList[position]
//
//        holder.image.setImageResource(currentItem.imageProfile)
//        holder.name.text = currentItem.name
//        holder.jobType.text  = currentItem.jobTitle
//        holder.skillOne.text = currentItem.skillOne
//        holder.skillTwo.text = currentItem.skillTwo
//        holder.skillThree.text = currentItem.skillThree
//        holder.moreSkill.text = currentItem.moreSkill

        holder.initialize(itemList.get(position), clickListener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.iv_engineer)
        var name: TextView = itemView.findViewById(R.id.tv_name_talent)
        var jobType: TextView = itemView.findViewById(R.id.tv_type_talent)
        var skillOne: TextView = itemView.findViewById(R.id.tv_skill1)
        var skillTwo: TextView = itemView.findViewById(R.id.tv_skill2)
        var skillThree: TextView = itemView.findViewById(R.id.tv_skill3)
        var moreSkill: TextView = itemView.findViewById(R.id.tv_more_skill)

        fun initialize(item: ListEngineerDataClass, action: OnItemClickListener) {
            image.setImageResource(item.imageProfile)
            name.text = item.name
            jobType.text = item.jobTitle
            skillOne.text = item.skillOne
            skillTwo.text = item.skillTwo
            skillThree.text = item.skillThree
            moreSkill.text = item.moreSkill

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ListEngineerDataClass, position: Int)
    }

}