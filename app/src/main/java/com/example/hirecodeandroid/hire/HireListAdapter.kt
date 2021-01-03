package com.example.hirecodeandroid.hire

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemHireBinding

class HireListAdapter(private val listHire: ArrayList<HireModel>, private val onListHireClickListener: OnListHireClickListener): RecyclerView.Adapter<HireListAdapter.HireHolder>() {

    fun addList(list: List<HireModel>) {
        listHire.clear()
        listHire.addAll(list)
        notifyDataSetChanged()
    }

    class HireHolder(val binding: ItemHireBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireHolder {
        return HireHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hire, parent, false))
    }

    override fun getItemCount(): Int = listHire.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HireHolder, i: Int) {
        val item = listHire[i]
        holder.binding.tvProjectTitle.text = item.hireMessage
        holder.binding.tvHireCreated.text = item.hirePrice.toString()
        holder.binding.tvHireStatus.text = item.hireStatus

        if (item.hireStatus == "wait" || item.hireStatus == "") {
            holder.binding.tvHireStatus.text = "wait your response"
        } else if (item.hireStatus == "approve") {
            holder.binding.tvHireStatus.setTextColor(Color.rgb(60, 122, 62))
        }

        holder.binding.btnApprove.setOnClickListener {
            onListHireClickListener.onHireApproveClicked(i)
        }

        holder.binding.btnReject.setOnClickListener {
            onListHireClickListener.onHireRejectClicked(i)
        }

    }

    interface OnListHireClickListener {
        fun onHireRejectClicked(position : Int)
        fun onHireApproveClicked(position : Int)
    }
}