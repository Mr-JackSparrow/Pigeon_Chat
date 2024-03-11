package com.example.kabootar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.kabootar.DataModels.UserDataModel
import com.example.kabootar.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class SignInActivity : AppCompatActivity() {

    private lateinit var signInBinding: ActivitySigninBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        auth = Firebase.auth

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        signInBinding.signInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manageSignInResult(task)
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun manageSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            } else {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        addUserToDatabase(user)
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(user: FirebaseUser) {
        val userData = UserDataModel(
            user.uid,
            user.displayName ?: "",
            user.email ?: "",
            user.phoneNumber ?: "",
            user.photoUrl?.toString() ?: "",
            "Hey there, I am using Pigeon",
            SimpleDateFormat("dd/MM/yyyy").format(Date()),
            SimpleDateFormat("HH:mm").format(Date()),
            ServerValue.TIMESTAMP
        )

        databaseReference = Firebase.database.reference

        databaseReference.child("Users").child(user.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    if (!snapshot.exists()) {
                        databaseReference.child("Users").child(user.uid)
                            .setValue(userData)
                            .addOnSuccessListener {
                                Log.d("SignInActivity", "User added to database")
                                startActivity(Intent(this, Profile::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("SignInActivity", "Failed to add user to database", e)
                            }
                    }else{
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                } else {
                    Log.e("SignInActivity", "Error getting user data", task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}