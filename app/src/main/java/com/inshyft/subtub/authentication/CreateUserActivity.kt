package com.inshyft.subtub.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.inshyft.subtub.admin.AdminActivity
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.ActivityCreateUserBinding
import com.inshyft.subtub.utils.appUtils

class CreateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateUserBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("subtub")

        setClickListener()
    }

    private fun setClickListener() {
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnCreateAccount.setOnClickListener {
            signIn()
        }
    }

    private fun notEmpty(): Boolean = binding.emailEdittext.text.toString().trim().isNotEmpty() &&
            binding.passEdittext.text.toString().trim().isNotEmpty()

//    private fun identicalPassword(): Boolean {
//        var identical = false
//        if (notEmpty() &&
//            binding.passEdittext.text.toString().trim() == binding.passEdittext.text.toString().trim()
//        ) {
//            identical = true
//        } else if (!notEmpty()) {
//            createAccountInputsArray.forEach { input ->
//                if (input.text.toString().trim().isEmpty()) {
//                    input.error = "${input.hint} is required"
//                }
//            }
//        } else {
//            toast("passwords are not matching !")
//        }
//        return identical
//    }

    private fun signIn() {

        binding.progressbar.isVisible = true
        val pass = binding.passEdittext.text.toString()
        if (pass.length >= 6) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            val userEmail = binding.emailEdittext.text.toString().trim()
            val userPassword = binding.passEdittext.text.toString().trim()
            val name = binding.nameEdittext.text.toString()
            val userId = binding.useridEdittext.text.toString()

            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        val firebaseUser = firebaseAuth.currentUser
//                        val profile = mapOf(
//                            userEmail to "email",
//                            userPassword to "number",
//                            userId to "userid",
//                            name to "name"
//                        )
                        var uid = firebaseAuth.currentUser?.uid
                        var fuid: String = task.result.user?.uid.toString()

                        appUtils.log("uid  ::  $uid   fuid  :::  $fuid")

                        val userData = UserData(name, userEmail, userPassword, userId, fuid, 0)

                        database.child("users").child(fuid).setValue(userData)
                        Toast.makeText(
                            applicationContext, "created account successfully !",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressbar.isVisible = false
                        startActivity(Intent(this, AdminActivity::class.java))
                        finish()
                    } else {

                        Toast.makeText(
                            applicationContext, task.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("error", task.exception.toString())
                    }
                }
        }
    }
}