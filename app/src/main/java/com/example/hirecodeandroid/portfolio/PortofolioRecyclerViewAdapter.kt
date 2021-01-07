package com.example.hirecodeandroid.portfolio

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListPortofolioBinding
import com.example.hirecodeandroid.experience.ExperienceModel

class PortofolioRecyclerViewAdapter(private val listPortfolio: ArrayList<PortfolioModel>) : RecyclerView.Adapter<PortofolioRecyclerViewAdapter.PortofolioHolder>() {

    fun addList(list: List<PortfolioModel>) {
        listPortfolio.clear()
        listPortfolio.addAll(list)
        notifyDataSetChanged()
    }

    class PortofolioHolder(val binding: ItemListPortofolioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listPortfolio.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortofolioHolder {
        return PortofolioHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_portofolio,
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: PortofolioHolder, position: Int) {
        val item = listPortfolio[position]
        val img = "http://3.80.223.103:4000/image/${item.portoImage}"

        holder.binding.tvAppName.text = item.portoAppName
        holder.binding.tvPortfolioType.text = item.portoType

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.ic_project)
            .error(R.drawable.ic_project)
            .into(holder.binding.ivPort)
    }

}