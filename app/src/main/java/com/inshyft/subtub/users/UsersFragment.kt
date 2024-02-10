package com.inshyft.subtub.users

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inshyft.subtub.R
import com.inshyft.subtub.admin.OnUserClick
import com.inshyft.subtub.admin.TaskActivity
import com.inshyft.subtub.authentication.CreateUserActivity
import com.inshyft.subtub.authentication.UserActivity
import com.inshyft.subtub.data.DailyShiftData
import com.inshyft.subtub.data.ShiftDaily
import com.inshyft.subtub.data.ShiftData
import com.inshyft.subtub.data.ShiftDay
import com.inshyft.subtub.databinding.FragmentUsersBinding
import com.inshyft.subtub.utils.appUtils


class UsersFragment : Fragment(), OnDayClick {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private var list = ArrayList<ShiftDay>()
    private var position = 0
    private var listDay = ArrayList<DailyShiftData>()
    var uid = ""
    var idx = 1
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
//        init()
        getShifts(idx, type)
    }

    private fun getShifts(index: Int, adapterType: String) {
        binding.progressbar.isVisible = true
        binding.tvSlotDate.text = "$idx January, 2023"
        binding.tvDateBelow.text = "$idx January, 2023"

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.currentUser?.uid.toString()
        val id = index.toString()


        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val jobRef = ref.child("shift").child(id).child("dayShift")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listDay.clear()
                for (ds in snapshot.children) {
                    val shift = ds.getValue(DailyShiftData::class.java)
                    if (shift != null) {
                        listDay.add(shift)

                        binding.rvDays.adapter = AdapterAllShift(listDay,
                            onDropClickListener, onPickClickListener, uid, adapterType )
                        binding.progressbar.isVisible = false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                appUtils.log("error reading database  :: $error")

            }

        }
        jobRef.addValueEventListener(menuListener)
    }

    private val onPickClickListener = OnPickClick {
        appUtils.log(" pick   :::  ${it.id}")
        appUtils.log(" pick   :: $it")

        val id = idx.toString()
        var nodeId: String = (it.id?.toInt()?.minus(1)).toString()

        val ref = FirebaseDatabase.getInstance().getReference("subtub")
            .child("shift").child(id).child("dayShift")

        appUtils.log(" pick node id :::  $nodeId ,  id  ::  $id")

        ref.child(nodeId).child("filled").setValue(true).addOnCompleteListener {
            if (it.isSuccessful){
                ref.child(nodeId).child("uid").setValue(uid)
                getShifts(idx, type)
                Toast.makeText(requireContext(), "Shift assigned", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private val onDropClickListener = OnDropClick {

        var nodeId: String = (it.id?.toInt()?.minus(1)).toString()
        appUtils.log(" drop  :: $it")
        appUtils.log(" drop  :: ${it.id}")

        val id = idx.toString()

        val ref = FirebaseDatabase.getInstance().getReference("subtub")
            .child("shift").child(id).child("dayShift")

        appUtils.log(" pick node id  :::  $nodeId ,  id  ::  $id")

        ref.child(nodeId).child("filled").setValue(false).addOnCompleteListener {
            if (it.isSuccessful){
                ref.child(nodeId).child("uid").setValue("")
                ref.child(nodeId).child("permanent").setValue(0)
                getShifts(idx, type)
                Toast.makeText(requireContext(), "Shift assigned", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun setShiftUI(position: Int) {
//        updateAllSlot()
//        var slot: String = list[position].slot.toString()
//        binding.tvSlotDate.text = list[position].date
//        binding.tvDateBelow.text = list[position].date
//        if (slot == "Slot 1") {
//            binding.layoutSlot1.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }else if (slot == "Slot 2") {
//            binding.layoutSlot2.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }else if (slot == "Slot 3") {
//            binding.layoutSlot3.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }else if (slot == "Slot 4") {
//            binding.layoutSlot4.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }else if (slot == "Slot 5") {
//            binding.layoutSlot5.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }else if (slot == "Slot 6") {
//            binding.layoutSlot6.setBackgroundColor(resources.getColor(R.color.light_grey))
//        }
//    }

//    private fun updateAllSlot() {
//        binding.layoutSlot1.setBackgroundColor(resources.getColor(R.color.orange_light))
//        binding.layoutSlot2.setBackgroundColor(resources.getColor(R.color.orange_light))
//        binding.layoutSlot3.setBackgroundColor(resources.getColor(R.color.orange_light))
//        binding.layoutSlot4.setBackgroundColor(resources.getColor(R.color.orange_light))
//        binding.layoutSlot5.setBackgroundColor(resources.getColor(R.color.orange_light))
//        binding.layoutSlot6.setBackgroundColor(resources.getColor(R.color.orange_light))
//    }

//    private fun init() {
//        binding.progressbar.isVisible = true
//        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        val uid: String = firebaseAuth.currentUser?.uid.toString()
//
//        var ref = FirebaseDatabase.getInstance().getReference("subtub")
//        val jobRef = ref.child("shift")
//
//        binding.rvDays.layoutManager = GridLayoutManager(requireContext(), 3)
//        val menuListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                list.clear()
//                for (ds in snapshot.children) {
//                    val shifts = ds.getValue(ShiftDay::class.java)
//                    if (shifts != null) {
//                        list.add(shifts)
//                        binding.rvDays.adapter = AdapterDays(list, onDayClickListener)
//                        binding.progressbar.isVisible = false
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                appUtils.log("error reading database  :: $error")
//
//            }
//
//        }
//        jobRef.addValueEventListener(menuListener)
//
//        Toast.makeText(
//            requireContext(), "userid  :::  $uid",
//            Toast.LENGTH_SHORT
//        ).show()
//    }

    private val onDayClickListener = OnDayClick {
       appUtils.log("day data  ::  $it")
        it.id
        val intent = Intent(requireContext(), DailyShiftActivity::class.java)
        intent.putExtra("id", it.id)
        intent.putExtra("date", it.name)
        startActivity(intent)
    }

    private fun setClickListener() {

        binding.chipPick.setOnClickListener {
            binding.chipPick.setBackgroundColor(Color.parseColor("#EB7A00"))
            binding.chipDrop.setBackgroundColor(Color.parseColor("#E3C18C"))
            binding.chipPmt.setBackgroundColor(Color.parseColor("#E3C18C"))


            type = "pick"
            getShifts(idx, type)
            appUtils.log("pick   :::  $type")
        }

        binding.chipDrop.setOnClickListener {
            binding.chipDrop.setBackgroundColor(Color.parseColor("#EB7A00"))
            binding.chipPick.setBackgroundColor(Color.parseColor("#E3C18C"))
            binding.chipPmt.setBackgroundColor(Color.parseColor("#E3C18C"))

            type = "drop"
            getShifts(idx, type)
            appUtils.log("drop   :::  $type")
        }
        binding.chipPmt.setOnClickListener {
            binding.chipPmt.setBackgroundColor(Color.parseColor("#EB7A00"))
            binding.chipPick.setBackgroundColor(Color.parseColor("#E3C18C"))
            binding.chipDrop.setBackgroundColor(Color.parseColor("#E3C18C"))

            type = ""
            getShifts(idx, "")
        }

        binding.tvViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), AllShiftActivity::class.java))
        }
        binding.profileImg.setOnClickListener {

            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        binding.ivLeft.setOnClickListener {

            if (idx > 1) {
                idx -= 1
                getShifts(idx, type)
            }else{
//                position -=1
            }
        }

        binding.ivRight.setOnClickListener {
            if (idx in 1..6) {
                idx += 1
                appUtils.log("idx   :::  $idx")
                getShifts(idx, type)
            }else{
//                position+=1
            }
        }
    }

    override fun onClick(data: ShiftDay) {

    }

}