package com.example.hirecodeandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewAdapter.NameViewHolder>() {

    val listName = listOf("Ajizul Hakim", "Farhan", "Ikrima", "Al Lukman","Indra")
    val listImage = intArrayOf(R.drawable.avatar, R.drawable.jane, R.drawable.ic_github, R.drawable.jane, R.drawable.avatar)

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.iv_engineer)
        var name: TextView = itemView.findViewById(R.id.tv_name_talent)
    }

    override fun getItemCount(): Int = listName.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        return NameViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_engineer, parent, false))
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.name.text = listName[position]
        holder.image.setImageResource(listImage[position])

    }

}