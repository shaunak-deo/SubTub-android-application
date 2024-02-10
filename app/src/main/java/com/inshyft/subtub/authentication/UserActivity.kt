package com.inshyft.subtub.authentication

import android.os.Bundle
import android.annotation.SuppressLint
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

import com.inshyft.subtub.users.UsersFragment

import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.inshyft.subtub.R
//import com.inshyft.subtub.authentication.databinding.ActivityUserBinding
import com.inshyft.subtub.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialise();
        setSupportActionBar(binding.appBarUser.toolbar)

        binding.appBarUser.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_user)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_user)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    @SuppressLint("ResourceType")
    private fun initialise() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, UsersFragment()
        ).commit()


//        binding.chipPmt.setChipBackgroundColorResource(R.drawable.bg_chips)
//        =
//            ContextCompat.getColorStateList(this, R.drawable.bg_chips)
    }
}