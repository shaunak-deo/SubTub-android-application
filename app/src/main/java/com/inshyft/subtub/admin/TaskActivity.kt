package com.inshyft.subtub.admin

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.inshyft.subtub.data.ShiftData
import com.inshyft.subtub.data.ShiftDay
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.ActivityTaskBinding
import com.inshyft.subtub.users.AdapterDays
import com.inshyft.subtub.users.OnDayClick
import com.inshyft.subtub.utils.appUtils
import java.text.SimpleDateFormat
import java.util.*


class TaskActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityTaskBinding
    var cal: Calendar = Calendar.getInstance()
    private var datePickerDialog: DatePickerDialog? = null
    var slots = arrayOf("Select Slot", "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Slot 5", "Slot 6")
    var slot = ""
    private lateinit var database: DatabaseReference
    var uid = ""
    private var list = ArrayList<ShiftDay>()
    var userInfo = UserData("", "", "", "", "", 0)
    var point = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = intent.getStringExtra("uid").toString()

//        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show()

        database = FirebaseDatabase.getInstance().getReference("subtub")
        binding.spinnerSlot.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, R.layout.simple_spinner_item, slots)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.spinnerSlot.setAdapter(aa)

        setClickListener()
        getUserDataFromDb()
        getDetails()
//        updatePoint()
    }

    private fun updatePoint(points: Int) {
        appUtils.log("point in func  ::  $points")
        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val userRef = ref.child("users").child(uid)

        userRef.child("point").setValue(points).addOnCompleteListener {
            if (it.isSuccessful){
                appUtils.log("posted the point to user")
                getDetails()
            }
        }

    }

    private fun getDetails() {
        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val userRef = ref.child("users")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (ds in snapshot.children) {
                    if (ds.getValue(UserData::class.java)?.uid.toString() == uid) {
                        userInfo = ds.getValue(UserData::class.java) as UserData
                    }
                }
                point = userInfo.point!!.toInt()
                binding.tvStars.text = point.toString()
                appUtils.log("$userInfo")
                setUserData(userInfo)
            }

            override fun onCancelled(error: DatabaseError) {
                appUtils.log("error reading database  :: $error")

            }

        }
        userRef.addValueEventListener(menuListener)

    }

    private fun setUserData(userInfo: UserData) {
        binding.tvUsername.text = userInfo.name
        binding.tvUserid.text = userInfo.userid
        binding.tvDate.text = "Disciplinary Points: " + userInfo.point

    }

    private fun getUserDataFromDb() {
//        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        val uid: String = firebaseAuth.currentUser?.uid.toString()

        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val jobRef = ref.child("shiftDay")

        binding.rvTask.layoutManager = GridLayoutManager(this, 3)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children) {
                    val shifts = ds.getValue(ShiftDay::class.java)
                    if (shifts != null) {
                        list.add(shifts)
                    }
                    appUtils.log("ds   ::  $shifts")
                }
                binding.rvTask.adapter = AdapterDays(list, onDayClickListener)
            }

            override fun onCancelled(error: DatabaseError) {
                appUtils.log("error reading database  :: $error")

            }

        }
        jobRef.addValueEventListener(menuListener)

//        Toast.makeText(
//            this, "userid  :::  $uid",
//            Toast.LENGTH_SHORT
//        ).show()
    }

    private val onDayClickListener = OnDayClick {
        appUtils.log("day data  ::  $it")
        val intent = Intent(this, AdminShiftActivity::class.java)
        intent.putExtra("id", it.id)
        intent.putExtra("date", it.name)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    private fun setClickListener() {

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        binding.btnSelect.setOnClickListener {
            DatePickerDialog(
                this@TaskActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnCreate.setOnClickListener {
            val date = binding.tvDate.text.toString()
            val sName = binding.etShiftName.text?.trim().toString()
            val slotNum = slot

            val key: String = database.push().key.toString()
            val shiftData = ShiftData(date, sName, slotNum, key)

            database.child("shifts").child(uid).child(key).setValue(shiftData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Task Assigned", Toast.LENGTH_SHORT).show()
                    }

                }
        }

        binding.btnAdd.setOnClickListener {
            appUtils.log("points  ::  $point")
            if (point < 5) {
                updatePoint(point + 1)
            }
        }

        binding.btnSubtract.setOnClickListener {
            appUtils.log("points  ::  $point")
            if (point > 0) {
                updatePoint(point - 1)
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd MMMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.tvDate.text = sdf.format(cal.getTime())
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        slot = slots[position]
        Toast.makeText(this, slot, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}