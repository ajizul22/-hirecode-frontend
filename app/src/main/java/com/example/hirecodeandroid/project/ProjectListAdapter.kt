package com.example.hirecodeandroid.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemProjectBinding

class ProjectListAdapter(private val listProject: ArrayList<ProjectModel>, private val onListProjectClickListener: OnListProjectClickListener): RecyclerView.Adapter<ProjectListAdapter.ProjectHolder>() {


    fun addList(list: List<ProjectModel>) {
        listProject.clear()
        listProject.addAll(list)
        notifyDataSetChanged()
    }

    class ProjectHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_project, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        val item = listProject[position]
        val img = "http://3.80.223.103:4000/image/${item.projectImage}"
        holder.binding.tvProjectTitle.text = item.projectName
        holder.binding.tvProjectCompany.text = item.companyId
        holder.binding.tvProjectDeadline.text = item.projectDeadline

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.ic_project)
            .error(R.drawable.ic_project)
            .into(holder.binding.ivProject)

        holder.itemView.setOnClickListener {
            onListProjectClickListener.onProjectItemClicked(position)
        }


    }

    override fun getItemCount(): Int = listProject.size

    interface OnListProjectClickListener {
        fun onProjectItemClicked(position : Int)
    }
}