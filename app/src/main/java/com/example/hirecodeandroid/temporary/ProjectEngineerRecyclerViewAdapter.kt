package com.example.hirecodeandroid.temporary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.dataclass.ListProjectEngineerData

class ProjectEngineerRecyclerViewAdapter(private val itemList: List<ListProjectEngineerData>, var clickListener: OnItemClickListener)
    : RecyclerView.Adapter<ProjectEngineerRecyclerViewAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
       return ProjectViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.item_list_project, parent, false)
       )
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ProjectViewHolder, i: Int) {
//        val currentItem = itemList[i]
//
//        holder.image.setImageResource(currentItem.imageProject)
//        holder.title.text = currentItem.title
//        holder.company.text = currentItem.company
//        holder.deadline.text = currentItem.deadline

        holder.initialize(itemList.get(i), clickListener)
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.iv_project)
        var title: TextView = itemView.findViewById(R.id.tv_project_title)
        var company: TextView = itemView.findViewById(R.id.tv_project_company)
        var deadline: TextView = itemView.findViewById(R.id.tv_project_deadline)

        fun initialize(item: ListProjectEngineerData, action: OnItemClickListener) {
            image.setImageResource(item.imageProject)
            title.text = item.title
            company.text = item.company
            deadline.text = item.deadline

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: ListProjectEngineerData, position: Int)
    }


}