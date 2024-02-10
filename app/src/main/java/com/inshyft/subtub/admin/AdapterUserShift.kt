package com.inshyft.subtub.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.data.ShiftData
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.LayoutShiftBinding

class AdapterUserShift(private val list: ArrayList<ShiftData>) :
    RecyclerView.Adapter<ShiftVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftVH {
        val binding = LayoutShiftBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val holder = ShiftVH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: ShiftVH, position: Int) {

//        holder.binding.ivDelete.setOnClickListener {
//            listener.onClick(list[position].key.toString())
//        }
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ShiftVH(val binding: LayoutShiftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(list: ArrayList<ShiftData>) {
        binding.tvSname.text = list[adapterPosition].shiftName
        binding.tvSlot.text = list[adapterPosition].slot
        binding.tvStime.text = list[adapterPosition].date
    }
}

fun interface OnShiftClick {
    fun onClick(key: String)
}

//, var listener:OnShiftClick