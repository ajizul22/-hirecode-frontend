package com.example.hirecodeandroid.hire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemHireBinding

class HireListAdapter: RecyclerView.Adapter<HireListAdapter.HireHolder>() {

    private var items = mutableListOf<HireModel>()

    fun addList(list: List<HireModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class HireHolder(val binding: ItemHireBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireHolder {
        return HireHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hire, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HireHolder, i: Int) {
        val item = items[i]
        holder.binding.tvProjectTitle.text = item.hireMessage
        holder.binding.tvHireCreated.text = item.hirePrice.toString()
        holder.binding.tvHireStatus.text = item.hireStatus
    }
}