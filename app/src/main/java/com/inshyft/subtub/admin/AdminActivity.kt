package com.inshyft.subtub.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.*
import com.inshyft.subtub.authentication.CreateUserActivity
import com.inshyft.subtub.data.DailyShiftData
import com.inshyft.subtub.data.ShiftDaily
import com.inshyft.subtub.data.ShiftDay
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.ActivityAdminBinding
import com.inshyft.subtub.utils.appUtils
import com.inshyft.subtub.R

class AdminActivity : AppCompatActivity(), OnUserClick {
    private lateinit var binding: ActivityAdminBinding
    private var list = ArrayList<UserData>()
    private lateinit var database: DatabaseReference
    private var listShift = ArrayList<DailyShiftData>()
    private var listDay = ArrayList<ShiftDaily>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("subtub").child("shift")

        getDataFromDb()
        makeSlot()
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.fragment_map, MapsMarkerActivity, "fragment_name")
//            .commit()

        binding.ivAdd.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
    }

    private fun makeSlot() {

//        val dailyShiftData1 = DailyShiftData("Shift 1", "1", "7am -9am",false,""  )
//        val dailyShiftData2 = DailyShiftData("Shift 2", "2", "9am -11am",false,""  )
//        val dailyShiftData3 = DailyShiftData("Shift 3", "3", "11am -1pm",false,""  )
//        val dailyShiftData4 = DailyShiftData("Shift 4", "4", "1pm -3pm",false,""  )
//        val dailyShiftData5 = DailyShiftData("Shift 5", "5", "3pm -5pm",false,""  )
//        val dailyShiftData6 = DailyShiftData("Shift 6", "6", "5pm -7pm",false,""  )
//        val dailyShiftData7 = DailyShiftData("Shift 7", "7", "7pm -9pm",false,""  )
//        val dailyShiftData8 = DailyShiftData("Shift 8", "8", "9pm -11pm",false,""  )
//
//        listShift.add(dailyShiftData1)
//        listShift.add(dailyShiftData2)
//        listShift.add(dailyShiftData3)
//        listShift.add(dailyShiftData4)
//        listShift.add(dailyShiftData5)
//        listShift.add(dailyShiftData6)
//        listShift.add(dailyShiftData7)
//        listShift.add(dailyShiftData8)


        val key: String = database.push().key.toString()
        val shiftDay = ShiftDay("7", "7 Jan", key)


        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val shRef = ref.child("shiftDay")
        shRef.child("7").setValue(shiftDay)
//        val menuListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (ds in snapshot.children) {
//                    val shift = ds.getValue(ShiftDaily::class.java)
//                    if (shift != null) {
//                        listDay.add(shift)
//                    }
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                appUtils.log("error reading database  :: $error")
//
//            }
//
//        }


//        shRef.addValueEventListener(menuListener)

//        database.child("7").setValue(shiftDaily).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
//            }
//
//        }
    }

    private fun getDataFromDb() {

        binding.progressbar.isVisible = true

        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val jobRef = ref.child("users")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children) {
                    val users = ds.getValue(UserData::class.java)
                    if (users != null) {
                        list.add(users)
                        binding.rvUsers.adapter = AdapterUser(list, onUserClickListener, onDeleteClickListener)
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

    private val onUserClickListener = OnUserClick {
        if (it.uid?.length!! > 0) {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra("uid", it.uid)
            startActivity(intent)
        }
    }

    private val onDeleteClickListener = OnDeleteClick {
        val uid = it.uid!!
        if (uid.isNotEmpty()) {
            var ref = FirebaseDatabase.getInstance().getReference("subtub")
            val userRef = ref.child("users").child(uid)
            val builder = AlertDialog.Builder(this)

            // Set the message show for the Alert time
            builder.setMessage("Do you want to delete user ?")

            // Set Alert Title
            builder.setTitle("Alert !")

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false)

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("Yes") {
                // When the user click yes button then app will close
                    dialog, which -> userRef.removeValue()
            }

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("No") {
                // If user click no then dialog box is canceled.
                    dialog, which -> dialog.cancel()
            }

            // Create the Alert dialog
            val alertDialog = builder.create()
            // Show the Alert Dialog box
            alertDialog.show()


            Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show()

            getDataFromDb()

        }
    }

    override fun onClick(user: UserData) {

    }
}