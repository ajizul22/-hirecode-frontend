package com.example.hirecodeandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R

class PortofolioRecyclerViewAdapter : RecyclerView.Adapter<PortofolioRecyclerViewAdapter.PortofolioHolder>() {

    val listImage = intArrayOf(R.drawable.port1, R.drawable.port2, R.drawable.port3, R.drawable.port4)

    class PortofolioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image :ImageView = itemView.findViewById(R.id.iv_port)
    }

    override fun getItemCount(): Int = listImage.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortofolioHolder {
        return PortofolioHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_portofolio, parent, false))
    }

    override fun onBindViewHolder(holder: PortofolioHolder, position: Int) {
        holder.image.setImageResource(listImage[position])
    }

}