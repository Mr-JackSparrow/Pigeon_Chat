package com.example.kabootar

import MainViewPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kabootar.databinding.ActivityMainBinding
import com.example.kabootar.fragments.Chats
import com.example.kabootar.fragments.CallLogs
import com.example.kabootar.userManagement.UserDataModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var bind: ActivityMainBinding
    lateinit var mainViewPagerAdapter: MainViewPagerAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)

        setContentView(bind.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        UserDataModel.fetchData(firebaseAuth, databaseReference)

        mainViewPagerAdapter = MainViewPagerAdapter(this)


        mainViewPagerAdapter.addFragment(Chats(), "Chats")
        mainViewPagerAdapter.addFragment(CallLogs(), "Calls")


        bind.vPgr2.adapter = mainViewPagerAdapter

        TabLayoutMediator(bind.tbLayout, bind.vPgr2){tab, position ->
            tab.text = mainViewPagerAdapter.getPageTitle(position)
        }.attach()

    }

}

