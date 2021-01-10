package com.example.hirecodeandroid.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListEngineerBinding
import com.example.hirecodeandroid.dataclass.ListEngineerDataClass
import com.example.hirecodeandroid.listengineer.ListEngineerAdapter
import com.example.hirecodeandroid.listengineer.ListEngineerModel

class SearchAdapter(private val listEngineer: ArrayList<ListEngineerModel>)
    : RecyclerView.Adapter<SearchAdapter.ListEngineerHolder>() {

    fun addList(list: List<ListEngineerModel>) {
        listEngineer.clear()
        listEngineer.addAll(list)
        notifyDataSetChanged()
    }

    class ListEngineerHolder(val binding: ItemListEngineerBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listEngineer.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEngineerHolder {
        return ListEngineerHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.item_list_engineer, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListEngineerHolder, position: Int) {
        val item = listEngineer[position]
        val img = "http://3.80.223.103:4000/image/${item.engineerProfilePict}"

        holder.binding.tvNameTalent.text = item.accountName
        holder.binding.tvTypeTalent.text = item.engineerJobTitle

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(holder.binding.ivEngineer)
    }
}