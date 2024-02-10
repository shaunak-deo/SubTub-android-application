package com.inshyft.subtub.users

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.inshyft.subtub.R
import com.inshyft.subtub.authentication.ui.MapsMarkerActivity
import com.inshyft.subtub.authentication.ui.TrainingVideoActivity
import com.inshyft.subtub.data.UserData
import com.inshyft.subtub.databinding.ActivityProfileBinding
import com.inshyft.subtub.utils.appUtils

class ProfileActivity : AppCompatActivity() {

    val PICK_PHOTO_REQUEST_CODE = 0
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding: ActivityProfileBinding
    var userInfo = UserData("", "", "", "", "", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetails()
        setClickListeners()
        val buttonClick = findViewById<Button>(R.id.button)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MapsMarkerActivity::class.java)
            startActivity(intent)
        }
        val buttonClick2 = findViewById<Button>(R.id.button2)
        buttonClick2.setOnClickListener {
            val intent = Intent(this, TrainingVideoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDetails() {
        var ref = FirebaseDatabase.getInstance().getReference("subtub")
        val userRef = ref.child("users")

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val uid: String = firebaseAuth.currentUser?.uid.toString()

        val menuListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (ds in snapshot.children) {
                    if (ds.getValue(UserData::class.java)?.uid.toString() == uid) {
                        userInfo = ds.getValue(UserData::class.java) as UserData
                    }
                }
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
        binding.tvPhone.text = userInfo.email
        binding.tvName.text = userInfo.name
        binding.tvEmail.text = userInfo.userid
        binding.tvDob.text = "Points: " + userInfo.point

    }

    private fun setClickListeners() {
        binding.profileImage.setOnClickListener {
            /*This Dialogbox is used to ask user whether he wants to uplaod an existing image or capture new image*/
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        // Capture Image option selected
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        // Upload Image option selected
                        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhotoIntent, PICK_PHOTO_REQUEST_CODE)
                    }
                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Choose an option")
                .setPositiveButton("Capture Image", dialogClickListener)
                .setNegativeButton("Upload Image", dialogClickListener)
                .show()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*This if block will be executed if the user chooses to Capture an image using camera*/
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            /*This code sets image directly in the image view
                * Make changes here to upload in the firebase*/
            binding.profileImage.setImageBitmap(imageBitmap)
        }
        /*This if block will be executed if the user chooses to Upload an Existing image*/
        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                /*This code sets image directly in the image view
                * Make changes here to upload in the firebase*/
                binding.profileImage.setImageBitmap(bitmap)
            }
        }
    }
}