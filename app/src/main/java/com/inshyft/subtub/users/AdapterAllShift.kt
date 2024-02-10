package com.inshyft.subtub.users

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.data.DailyShiftData
import com.inshyft.subtub.databinding.ItemShiftBinding
import com.inshyft.subtub.utils.appUtils

class AdapterAllShift(
    private val list: ArrayList<DailyShiftData>, var listenerDrop: OnDropClick,
    var listener: OnPickClick, val uid: String, var type: String
) :
    RecyclerView.Adapter<AllShiftVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllShiftVH {
        val binding = ItemShiftBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val holder = AllShiftVH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: AllShiftVH, position: Int) {
        holder.binding.btnPick.setOnClickListener {
            listener.onClick(list[position])
        }

        holder.binding.btnDrop.setOnClickListener {
            listenerDrop.onClick(list[position])
        }
        holder.bind(list, uid, type)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class AllShiftVH(val binding: ItemShiftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(list: ArrayList<DailyShiftData>, uid: String, type: String) {
        binding.tvSlot.text = list[adapterPosition].name
        binding.tvTime.text = list[adapterPosition].time
        val filled: Boolean = list[adapterPosition].filled!!

        if (type == "pick") {
            if (!filled) {
                binding.btnPick.isVisible = true
                binding.btnDrop.isVisible = false
            }
        } else if (type == "drop") {
            if (filled && list[adapterPosition].uid == uid) {
                binding.btnDrop.isVisible = true
                binding.btnPick.isVisible = false
//                binding.tvPShift.isVisible = true
//                binding.tvPShift.text = "You're working!"
//                binding.layoutSlot.setBackgroundColor(Color.parseColor("#B8B8B8"))
//                appUtils.log("ispermanent  :::"+list[adapterPosition] )
                if (list[adapterPosition].permanent == 1) {
                    binding.btnDrop.isVisible = false
                    binding.btnPick.isVisible = false

                }
            }
        } else {
            binding.btnDrop.isVisible = false
            binding.btnPick.isVisible = false
            if (filled && list[adapterPosition].uid == uid) {

                binding.tvPShift.isVisible = true
                binding.tvPShift.text = "You're working!"
                binding.layoutSlot.setBackgroundColor(Color.parseColor("#B8B8B8"))

                appUtils.log("ispermanent  :::" + list[adapterPosition])
                if (list[adapterPosition].permanent == 1) {
                    binding.tvPShift.isVisible = true
                    binding.tvPShift.text = "You're working!"
                    binding.layoutSlot.setBackgroundColor(Color.parseColor("#B8B8B8"))
                }
            }
        }
    }
}

fun interface OnPickClick {
    fun onClick(data: DailyShiftData)
}

fun interface OnDropClick {
    fun onClick(data: DailyShiftData)
}