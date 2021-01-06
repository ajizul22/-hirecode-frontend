package com.example.hirecodeandroid.experience

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirecodeandroid.R
import com.example.hirecodeandroid.databinding.ItemListExperienceBinding
import com.example.hirecodeandroid.util.SharePrefHelper
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ExperienceRecyclerViewAdapter(private val listExp: ArrayList<ExperienceModel>,
private val onListExpClickListener: OnListExpClickListener ) : RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ExperienceHolder>() {

    fun addList(list: List<ExperienceModel>) {
        listExp.clear()
        listExp.addAll(list)
        notifyDataSetChanged()
    }

    class ExperienceHolder(val binding: ItemListExperienceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = listExp.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceHolder {
        return ExperienceHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_experience,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExperienceHolder, position: Int) {
        val sharePref = SharePrefHelper(holder.itemView.context)

        val level = sharePref.getInteger(SharePrefHelper.AC_LEVEL)


        val item = listExp[position]

        val dateStart = item.expStart!!.split("T")[0]
        val dateEnd = item.expEnd!!.split("T")[0]
        val sum = ChronoUnit.MONTHS.between(LocalDate.parse(dateStart).withDayOfMonth(1), LocalDate.parse(dateEnd).withDayOfMonth(1))

        holder.binding.tvJobTypeEx.text = item.expPosition
        holder.binding.tvCompany.text = item.expCompany
        holder.binding.tvStart.text = dateStart
        holder.binding.tvEnd.text = dateEnd
        holder.binding.tvDescEx.text = item.expDesc
        holder.binding.tvDuration.text = "$sum Month"

        if (level == 1) {
            holder.binding.btnDeleteUpdate.visibility = View.GONE
        }

        holder.binding.btnDelete.setOnClickListener {
            onListExpClickListener.onHireDelete(position)
        }

        holder.binding.btnUpdate.setOnClickListener {
            onListExpClickListener.onHireEdit(position)
        }

    }

    interface OnListExpClickListener {
        fun onHireDelete(position: Int)
        fun onHireEdit(position: Int)
    }
}