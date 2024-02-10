package com.inshyft.subtub.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inshyft.subtub.admin.AdminActivity
import com.inshyft.subtub.databinding.ActivityLoginAdminBinding

class LoginAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListener()
    }

    private fun setClickListener() {
        binding.btnAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }

        binding.tvCreateUser.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}