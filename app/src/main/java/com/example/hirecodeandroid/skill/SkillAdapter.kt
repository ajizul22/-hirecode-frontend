package com.example.hirecodeandroid.skill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListSkillBinding

class SkillAdapter(private val listSkill: ArrayList<SkillModel>,
                   private val onItemSkillClickListener: OnItemSkillClickListener) : RecyclerView.Adapter<SkillAdapter.SkillHolder>() {

    fun addList(list: List<SkillModel>) {
        listSkill.clear()
        listSkill.addAll(list)
        notifyDataSetChanged()
    }

    class SkillHolder(val binding: ItemListSkillBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(skill: SkillModel) {
            binding.skill = skill
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillHolder {
        return SkillHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_skill,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listSkill.size

    override fun onBindViewHolder(holder: SkillHolder, position: Int) {
        val item = listSkill[position]

        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemSkillClickListener.onItemSkillClicked(position)
        }

    }

    interface OnItemSkillClickListener {
        fun onItemSkillClicked(position: Int)
    }
}