package com.example.kabootar.userManagement

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserDataModel {

    companion object UserDetails {

        fun fetchData(firebaseAuth: FirebaseAuth, databaseReference: DatabaseReference) {
            // Get the currently logged-in user
            val currentUser = firebaseAuth.currentUser

            // Check if a user is logged in
            if (currentUser != null) {
                // User is logged in, fetch user's chats from Realtime Database
                val userId = currentUser.uid
                val userChatsReference = databaseReference.child("chats").child(userId)

                userChatsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Iterate through each chat of the user
                        for (chatSnapshot in dataSnapshot.children) {
                            // Get the chat ID
                            val chatId = chatSnapshot.key

                            // Fetch details of the users involved in the chat
                            val participants = chatSnapshot.children.map { it.child("uId").getValue(String::class.java) }

                            // Log the retrieved chat details
                            Log.d("ChatData", "Chat ID: $chatId, Participants: $participants")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                        Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<", "Failed to read user chats.", databaseError.toException())
                    }
                })
            } else {
                Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<", "User not logged in")
            }
        }
    }
}
