package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class mentorlogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorlogin)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference.child("mentors")

        // Retrieve references to UI elements
        val emailEditText = findViewById<EditText>(com.example.assignment2.R.id.email)
        val passwordEditText = findViewById<EditText>(com.example.assignment2.R.id.password)
        val loginButton = findViewById<Button>(com.example.assignment2.R.id.loginButton)
        val forgetPasswordText = findViewById<TextView>(com.example.assignment2.R.id.forgetPasswordText)
        val signupText=findViewById<TextView>(com.example.assignment2.R.id.signupText)

        // Set click listener for login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email and password fields are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Please enter both email and password.",
                    Toast.LENGTH_SHORT).show()
            } else {
                // Sign in with email and password
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, retrieve additional mentor information from the database
                            val user = auth.currentUser
                            user?.let { currentUser ->
                                database.child(currentUser.uid).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        // Retrieve mentor information from the database
                                        val mentor = snapshot.getValue(mentor_login::class.java)

                                        // Check if mentor information is retrieved successfully
                                        if (mentor != null) {
                                            // Start MainActivity7 with mentor information
                                            val intent = Intent(this@mentorlogin, MainActivity7::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // Mentor information not found
                                            Toast.makeText(baseContext, "Mentor information not found.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Database error occurred
                                        Toast.makeText(baseContext, "Failed to retrieve mentor information.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Set click listener for "Forget Your Password?" TextView
        forgetPasswordText.setOnClickListener {
            startActivity(Intent(this, MainActivity5::class.java))
        }

        signupText.setOnClickListener {
            startActivity(Intent(this, mentorsignup::class.java))
        }

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, redirect to MainActivity7
            startActivity(Intent(this, MainActivity7::class.java))
            finish()
        }
    }
}