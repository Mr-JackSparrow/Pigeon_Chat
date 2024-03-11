package com.example.kabootar

import MainViewPagerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kabootar.databinding.ActivityMainBinding
import com.example.kabootar.databinding.CustomDeleteUserDialogboxBinding
import com.example.kabootar.databinding.CustomDialogboxAdduserBinding
import com.example.kabootar.fragments.CallLogs
import com.example.kabootar.fragments.Chats
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding
    private lateinit var mainViewPagerAdapter: MainViewPagerAdapter
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.progressBar.visibility = View.VISIBLE

        checkAuthenticationState()

        //Toolbar
        setSupportActionBar(bind.tbMainToolBar)

        databaseReference = FirebaseDatabase.getInstance().getReference()




        //viewPager
        mainViewPagerAdapter = MainViewPagerAdapter(this)
        mainViewPagerAdapter.addFragment(Chats(), "Chats")
        mainViewPagerAdapter.addFragment(CallLogs(), "Calls")
        bind.vPgr2.adapter = mainViewPagerAdapter

        TabLayoutMediator(bind.tbLayout, bind.vPgr2) { tab, position ->
            tab.text = mainViewPagerAdapter.getPageTitle(position)
        }.attach()



        databaseReference.child("Users").child(auth.currentUser?.uid.toString()).get().addOnSuccessListener {
            if (it.exists()){

                bind.progressBar.visibility = View.GONE
                bind.tbMainToolBar.title = it.child("userName").value.toString()

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itmProfile -> {
                startActivity(Intent(this, Profile::class.java))
                true
            }
            R.id.itmSignout -> {
                auth.signOut()
                true
            }

            R.id.itmDelete ->{
                deleteAlertBox()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun checkAuthenticationState() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
    }


    private fun deleteAlertBox() {


        val bindAlert : CustomDeleteUserDialogboxBinding = CustomDeleteUserDialogboxBinding.inflate(layoutInflater)

        val customDialogBuilder = AlertDialog.Builder(this@MainActivity)
        customDialogBuilder.setView(bindAlert.root)

        val alertDialog = customDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.shape_round)

        alertDialog.show()

        bindAlert.confirm.setOnClickListener {

            val email = bindAlert.tvUserEmail.text.toString()

            databaseReference
                .child("Users")
                .child(auth.currentUser?.uid.toString())
                .child("email")
                .get()
                .addOnSuccessListener {

                    if(it.value != email)
                    {
                        Toast.makeText(this@MainActivity, "Email Id is not yours", Toast.LENGTH_LONG).show()
                    }else{
                        databaseReference
                            .child("Users")
                            .child(auth.currentUser?.uid.toString())
                            .removeValue()
                            .addOnSuccessListener {

                                Toast.makeText(this@MainActivity, "User is Deleted", Toast.LENGTH_LONG).show()

                                startActivity(Intent(this, SignInActivity::class.java))
                            }
                            .addOnFailureListener{
                                Log.e("Delete User", it.message.toString())
                            }
                    }
                }
                .addOnFailureListener{
                    Log.e("Delete User", it.message.toString())
                }
        }

        bindAlert.Cancel.setOnClickListener {
            alertDialog.dismiss()
        }


    }
}
