package com.inshyft.subtub.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.inshyft.subtub.MainActivity
import com.inshyft.subtub.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
//    lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.adminLogin.setOnClickListener {
            startActivity(Intent(this, LoginAdminActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            signInUser()
        }
    }

    private fun notEmpty(): Boolean =  binding.emailEdittext.text!!.isNotEmpty() && binding.passEdittext.text!!.isNotEmpty()


    private fun signInUser() {
        val signInEmail = binding.emailEdittext.text.toString().trim()
        val signInPassword = binding.passEdittext.text.toString().trim()

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, UserActivity::class.java))

                        Toast.makeText(applicationContext,"signed in successfully !",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext,"sign in failed due to : "+signIn.exception.toString(),
                            Toast.LENGTH_SHORT).show()

                    }
                }
        } else {
//            signInInputsArray.forEach { input ->
//                if (input.text.toString().trim().isEmpty()) {
//                    input.error = "${input.hint} is required"
//                }
//            }
        }
    }
}