package com.example.kabootar.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabootar.DataModel
import com.example.kabootar.R
import com.example.kabootar.adapters.ChatRecyclerViewAdapter
import com.example.kabootar.databinding.CustomDialogboxAdduserBinding
import com.example.kabootar.databinding.FragmentChatsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Chats : Fragment() {

    private lateinit var arr : MutableList<DataModel>
    private lateinit var chatRecyclerViewAdapter: ChatRecyclerViewAdapter
    private lateinit var fragmentBind : FragmentChatsBinding
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBind = FragmentChatsBinding.inflate(inflater, container, false)
        return fragmentBind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arr = mutableListOf()

        databaseReference
            .child("Users")
            .child(firebaseAuth.currentUser?.uid.toString())
            .child("contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    arr.clear() // Clear the list before populating it again
                    for (snapshot in dataSnapshot.children) {
                        databaseReference
                            .child("Users")
                            .child(snapshot.child("uid").value.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(userSnapshot: DataSnapshot) {
                                    val displayName = userSnapshot.child("userName").value.toString()
                                    val imgLink = userSnapshot.child("profileImgUrl").value.toString()
                                    arr.add(DataModel(imgLink, "Yash There", displayName))

                                    chatRecyclerViewAdapter.notifyDataSetChanged() // Notify adapter of data changes
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e("UserData", "Error fetching user data: $error")
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserData", "Error fetching contacts: $error")
                }
            })

        chatRecyclerViewAdapter = ChatRecyclerViewAdapter(arr, requireContext())

        fragmentBind.rvChatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRecyclerViewAdapter
        }

        fragmentBind.btnAddUser.setOnClickListener {
            alertBox()
        }
    }



    private fun alertBox() {
        val bindAlert : CustomDialogboxAdduserBinding = CustomDialogboxAdduserBinding.inflate(layoutInflater)

        val customDialogBuilder = AlertDialog.Builder(requireContext())
        customDialogBuilder.setView(bindAlert.root)

        val alertDialog = customDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.shape_round)
        alertDialog.show()

        bindAlert.btnAdd.setOnClickListener {
            val newUser = bindAlert.tvUserEmail.text


            databaseReference
                .child("Users")
                .orderByChild("email")
                .equalTo(newUser.toString())
                .get()
                .addOnSuccessListener { dataSnapshot ->

                    val snapshot = dataSnapshot.children.firstOrNull()


                    if(snapshot != null)
                    {
                        val contactData = HashMap<String, Any>()
                        contactData["uid"] = snapshot.child("uid").value.toString()
                        contactData["email"] = snapshot.child("email").value.toString()


                        databaseReference
                            .child("Users")
                            .child(firebaseAuth.currentUser?.uid.toString())
                            .child("contacts")
                            .child(
                                snapshot
                                    .child("uid")
                                    .value.toString()
                            )
                            .get()
                            .addOnSuccessListener {
                                if(!it.exists()){

                                    databaseReference
                                        .child("Users")
                                        .child(firebaseAuth.currentUser?.uid.toString())
                                        .child("contacts")
                                        .child(
                                            snapshot
                                                .child("uid")
                                                .value.toString()
                                        )
                                        .updateChildren(contactData)
                                        .addOnSuccessListener {
                                            alertDialog.dismiss()
                                            Toast.makeText(requireContext(), "User Added", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnCanceledListener {
                                            Toast.makeText(requireContext(), "User Not Added", Toast.LENGTH_LONG).show()
                                        }

                                }else{

                                    Toast.makeText(context, "Can't Add this Contact again", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener{
                                Log.e("Chat Contact Data", it.message.toString())
                            }
                    }else{
                        Toast.makeText(context, "User == Null", Toast.LENGTH_LONG).show()
                        Log.e("Profile User Data","UserDataNotAvailable")
                    }


                }
                .addOnFailureListener{
                    Log.e("Profile User Data", it.message.toString())
                }

        }

    }
}
