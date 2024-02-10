package com.inshyft.subtub.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.data.DailyShiftData
import com.inshyft.subtub.databinding.ItemAdminShiftBinding

class AdapterAssignShift(
    private val list: ArrayList<DailyShiftData>,
    var listener: OnAssignClick
) :
    RecyclerView.Adapter<AdminShiftVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminShiftVH {
        val binding = ItemAdminShiftBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val holder = AdminShiftVH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: AdminShiftVH, position: Int) {
        holder.binding.btnAssign.setOnClickListener {
            listener.onClick(list[position])
        }
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class AdminShiftVH(val binding: ItemAdminShiftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(list: ArrayList<DailyShiftData>) {
        binding.tvSlot.text = list[adapterPosition].name
        binding.tvTime.text = list[adapterPosition].time
        val filled: Boolean = list[adapterPosition].filled!!
        binding.btnAssign.isVisible = !filled
    }
}

fun interface OnAssignClick {
    fun onClick(data: DailyShiftData)
}
