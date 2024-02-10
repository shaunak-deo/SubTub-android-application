package com.inshyft.subtub.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.admin.OnShiftClick
import com.inshyft.subtub.admin.ShiftVH
import com.inshyft.subtub.data.ShiftData
import com.inshyft.subtub.databinding.LayoutShiftBinding

class AdapterShift(private val list: ArrayList<ShiftData>) :
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
        holder.bind(list)
        holder.binding.ivDelete.isVisible = false
    }

    override fun getItemCount(): Int {
        return list.size
    }
}