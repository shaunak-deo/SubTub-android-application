package com.inshyft.subtub.users

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inshyft.subtub.data.DailyShiftData
import com.inshyft.subtub.databinding.ActivityDailyShiftBinding
import com.inshyft.subtub.utils.appUtils

class DailyShiftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyShiftBinding
    var id = ""
    var date = ""
    private var listDay = ArrayList<DailyShiftData>()
    var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyShiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        date = intent.getStringExtra("date").toString()

        initialise()
    }

    private fun initialise() {

        binding.tvSlotDate.text = "$date, 2023"

        binding.progressbar.isVisible = true
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.currentUser?.uid.toString()

        appUtils.log("uid  ::  $uid")

        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val jobRef = ref.child("shift").child(id).child("dayShift")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listDay.clear()
                for (ds in snapshot.children) {
                    val shift = ds.getValue(DailyShiftData::class.java)
                    if (shift != null) {
                        listDay.add(shift)

                        binding.rvShift.adapter = AdapterAllShift(listDay,
                            onDropClickListener, onPickClickListener, uid, "")
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

        var nodeId: String = (it.id?.toInt()?.minus(1)).toString()

        val ref = FirebaseDatabase.getInstance().getReference("subtub")
            .child("shift").child(id).child("dayShift")

        appUtils.log(" pick node id :::  $nodeId ,  id  ::  $id")

        ref.child(nodeId).child("filled").setValue(true).addOnCompleteListener {
            if (it.isSuccessful){
                ref.child(nodeId).child("uid").setValue(uid)
                initialise()
                Toast.makeText(this, "Shift assigned", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private val onDropClickListener = OnDropClick {

        var nodeId: String = (it.id?.toInt()?.minus(1)).toString()
        appUtils.log(" drop  :: $it")
        appUtils.log(" drop  :: ${it.id}")

        val ref = FirebaseDatabase.getInstance().getReference("subtub")
            .child("shift").child(id).child("dayShift")

        appUtils.log(" pick node id  :::  $nodeId ,  id  ::  $id")

        ref.child(nodeId).child("filled").setValue(false).addOnCompleteListener {
            if (it.isSuccessful){
                ref.child(nodeId).child("uid").setValue("")
                ref.child(nodeId).child("permanent").setValue(0)
                initialise()
                Toast.makeText(this, "Shift assigned", Toast.LENGTH_SHORT).show()
            }
        }
    }

}