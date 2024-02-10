package com.inshyft.subtub.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.UserLayoutBinding

class AdapterUser(private val list: ArrayList<UserData>, var listener: OnUserClick,
                  var listenerDel: OnDeleteClick):
    RecyclerView.Adapter<UsersVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersVH {
        val binding = UserLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        val holder = UsersVH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: UsersVH, position: Int) {
        holder.binding.userLayoutMain.setOnClickListener {
            listener.onClick(list[position])
        }

        holder.binding.ivDelete.setOnClickListener {
            listenerDel.onClick(list[position])
        }
        holder.bind(list)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class UsersVH(val binding: UserLayoutBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(list: ArrayList<UserData>) {
        binding.tvUsername.text = list[adapterPosition].name
        binding.tvUserid.text = list[adapterPosition].userid
    }
}

fun interface OnUserClick {
    fun onClick(user: UserData)
}

fun interface OnDeleteClick {
    fun onClick(user: UserData)
}