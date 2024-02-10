package com.inshyft.subtub.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.data.ShiftDaily
import com.inshyft.subtub.data.ShiftDay
import com.inshyft.subtub.databinding.LayoutDaysBinding

class AdapterDays(private val list: ArrayList<ShiftDay>, var listener: OnDayClick) :
    RecyclerView.Adapter<DaysVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysVH {
        val binding = LayoutDaysBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val holder = DaysVH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: DaysVH, position: Int) {
        holder.binding.cardDay.setOnClickListener {
            listener.onClick(list[position])
        }
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class DaysVH(val binding: LayoutDaysBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(list: ArrayList<ShiftDay>) {
        binding.tvDay.text = list[adapterPosition].name
    }
}

fun interface OnDayClick {
    fun onClick(data: ShiftDay)
}