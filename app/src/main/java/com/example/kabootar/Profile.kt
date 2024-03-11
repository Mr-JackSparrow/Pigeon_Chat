package com.example.kabootar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kabootar.databinding.ProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class Profile : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var profileBinding: ProfileBinding
    private lateinit var firebaseStorage : FirebaseStorage
    private var imgUri : Uri? = null
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        checkAuthenticationState()

        profileBinding = ProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)


        progressBar = profileBinding.progressBar
        progressBar.visibility = View.GONE


// Launcher for Storage images
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if (result.resultCode == RESULT_OK)
            {
                firebaseStorage = Firebase.storage

                imgUri = result.data?.data
                profileBinding.ivProfileImg.setImageURI(imgUri)

                firebaseStorage
                    .reference
                    .child(auth.currentUser?.uid.toString())
                    .child("Profile-Pictures")
                    .listAll()
                    .addOnSuccessListener {

                        progressBar.visibility = View.VISIBLE

                        if(it.items.isEmpty()){

                            firebaseStorage
                                .reference
                                .child(auth.currentUser?.uid.toString())
                                .child("Profile-Pictures")
                                .child(auth.currentUser?.uid.toString())
                                .putFile(imgUri!!)
                                .addOnSuccessListener {
                                    Log.e("UserData", "Image Uploaded to DataStorage")
                                }
                                .addOnFailureListener{
                                        exception ->
                                    Log.e("UserData", exception.message.toString())
                                }

                        }else{

                            firebaseStorage
                                .reference
                                .child(auth.currentUser?.uid.toString())
                                .child("Profile-Pictures")
                                .child(auth.currentUser?.uid.toString())
                                .delete()
                                .addOnSuccessListener {
                                    Log.e("User Data Profile", "Past Profile Pic deleteed from DataStorage")
                                }

                            firebaseStorage
                                .reference
                                .child(auth.currentUser?.uid.toString())
                                .child("Profile-Pictures")
                                .child(auth.currentUser?.uid.toString())
                                .putFile(imgUri!!)
                                .addOnSuccessListener {
                                    Log.e("User Data Profile", "Image Uploaded to DataStorage")
                                    progressBar.visibility = View.GONE
                                }
                                .addOnFailureListener{
                                        exception ->
                                    Log.e("User Data Profile", exception.message.toString())
                                }

                        }

                    }



                Log.e("ImgUri", imgUri.toString())
            }
        }


// DataBase reference
        databaseReference = FirebaseDatabase
            .getInstance()
            .getReference()


        databaseReference
            .child("Users")
            .child(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                profileBinding.etUserName.setText(it.child("userName").getValue(String::class.java))
                profileBinding.etAbout.setText(it.child("about").getValue(String::class.java))
                Glide
                    .with(this)
                    .load(
                        it.child("profileImgUrl").getValue(String::class.java)
                    )
                    .into(
                        profileBinding
                            .ivProfileImg
                    )
            }



// User Updating Button
        profileBinding.btnUpdateUser.setOnClickListener {
            val userName = profileBinding.etUserName.text.toString()
            val about = profileBinding.etAbout.text.toString()

            progressBar.visibility = View.VISIBLE

            firebaseStorage = Firebase.storage

            // Retrieve download URL asynchronously
            firebaseStorage
                .reference
                .child(auth.currentUser?.uid.toString())
                .child("Profile-Pictures")
                .child(auth.currentUser?.uid.toString())
                .downloadUrl
                .addOnSuccessListener { uri ->
                    // Once download URL is obtained, update user data
                    val userData = HashMap<String, Any>()
                    userData["userName"] = userName
                    userData["about"] = about
                    userData["profileImgUrl"] = uri.toString() // Use uri.toString() to get the string representation of the URI

                    // Update user data in the database
                    databaseReference
                        .child("Users")
                        .child(auth.currentUser?.uid.toString())
                        .updateChildren(userData)
                        .addOnSuccessListener {

                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Information Updated Successfully", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnCanceledListener {
                            Toast.makeText(this, "Information not Added", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle failure to retrieve download URL
                    Log.e("UserData", "Failed to retrieve download URL: ${exception.message}")
                    Toast.makeText(this, "Failed to retrieve download URL", Toast.LENGTH_LONG).show()
                }
        }


// Taking Pictures from storage
        profileBinding.btnSelectImage.setOnClickListener{
                launcher.launch(Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                })
        }


    }



    override fun onStart() {
        super.onStart()
        checkAuthenticationState()
    }

    private fun checkAuthenticationState() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}
