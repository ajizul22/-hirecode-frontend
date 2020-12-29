package com.example.hirecodeandroid.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemProjectBinding

class ProjectListAdapter: RecyclerView.Adapter<ProjectListAdapter.ProjectHolder>() {

    private var items = mutableListOf<ProjectModel>()

    fun addList(list: List<ProjectModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ProjectHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_project, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        val item = items[position]
        holder.binding.tvName.text = item.projectName
        holder.binding.tvDesc.text = item.projectDesc
        holder.binding.tvType.text = item.projectDeadline
    }

    override fun getItemCount(): Int = items.size
}