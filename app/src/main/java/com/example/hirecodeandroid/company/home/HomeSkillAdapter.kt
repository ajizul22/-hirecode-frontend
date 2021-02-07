package com.example.hirecodeandroid.company.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListSkillBinding
import com.example.hirecodeandroid.skill.SkillModel

class HomeSkillAdapter : RecyclerView.Adapter<HomeSkillAdapter.RecyclerViewHolder>() {
    private lateinit var bind: ItemListSkillBinding
    private var items = mutableListOf<SkillModel>()

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(skill: SkillModel) {
            bind.skill = skill
            bind.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_skill,
            parent,
            false
        )
        return RecyclerViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return if (items.size > 3) {
            3
        } else {
            items.size
        }
    }

    fun addList(list: List<SkillModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}