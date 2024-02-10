package com.inshyft.subtub.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inshyft.subtub.R
import com.inshyft.subtub.data.ShiftData
import com.inshyft.subtub.databinding.ActivityAllShiftBinding
import com.inshyft.subtub.utils.appUtils

class AllShiftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllShiftBinding
    private var list = ArrayList<ShiftData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllShiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialise()
    }

    private fun initialise() {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val uid: String = firebaseAuth.currentUser?.uid.toString()

        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val jobRef = ref.child("shifts").child(uid)

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val shifts = ds.getValue(ShiftData::class.java)
                    if (shifts != null) {
                        list.add(shifts)
                        binding.rvTask.adapter = AdapterShift(list)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                appUtils.log("error reading database  :: $error")

            }

        }
        jobRef.addValueEventListener(menuListener)

//        Toast.makeText(
//            requireContext(), "userid  :::  $uid",
//            Toast.LENGTH_SHORT
//        ).show()

    }
}