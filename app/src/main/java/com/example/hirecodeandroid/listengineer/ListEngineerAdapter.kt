package com.example.hirecodeandroid.listengineer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListEngineerBinding

class ListEngineerAdapter(private val listEngineer: ArrayList<ListEngineerModel>,private val onListEngineerClickListener: OnListEngineerClickListener)
    : RecyclerView.Adapter<ListEngineerAdapter.ListEngineerHolder>() {

    fun addList(list: List<ListEngineerModel>) {
        listEngineer.clear()
        listEngineer.addAll(list)
        notifyDataSetChanged()
    }

    class ListEngineerHolder(val binding: ItemListEngineerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEngineerHolder {
        return ListEngineerHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_engineer, parent, false))
    }

    override fun getItemCount(): Int = listEngineer.size

    override fun onBindViewHolder(holder: ListEngineerHolder, position: Int) {
        val item = listEngineer[position]
        val img = "http://3.80.223.103:4000/image/${item.engineerProfilePict}"

        holder.binding.tvNameTalent.text = item.accountName
        holder.binding.tvTypeTalent.text = item.engineerJobTitle
        holder.binding.tvDomicile.text = item.engineerDomicilie
        holder.binding.tvFulltime.text = item.engineerJobType

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(holder.binding.ivEngineer)


        holder.itemView.setOnClickListener {
            onListEngineerClickListener.onEngineerItemClicked(position)
        }

    }

    interface OnListEngineerClickListener {
        fun onEngineerItemClicked(position : Int)
    }
}