package com.example.hirecodeandroid.listengineer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListEngineerBinding

class ListEngineerAdapter: RecyclerView.Adapter<ListEngineerAdapter.ListEngineerHolder>() {

    val items = mutableListOf<ListEngineerModel>()

    fun addList(list: List<ListEngineerModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ListEngineerHolder(val binding: ItemListEngineerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEngineerHolder {
        return ListEngineerHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_engineer, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListEngineerHolder, position: Int) {
        val item = items[position]

        holder.binding.tvNameTalent.text = item.accountName
        holder.binding.tvTypeTalent.text = item.engineerJobTitle

    }
}