package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity5 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        auth = FirebaseAuth.getInstance() // Initialize the FirebaseAuth instance

        val emailEditText = findViewById<EditText>(R.id.email)
        val verifyButton = findViewById<TextView>(R.id.verifyButton)

        verifyButton.setOnClickListener {
            sendPasswordResetEmail()
        }
    }


    private fun sendPasswordResetEmail() {
        val emailEditText = findViewById<EditText>(R.id.email)
        val email = emailEditText.text.toString()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()

                    // Move to the next activity and pass email as an extra
                    val intent = Intent(this,MainActivity6::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish() // Optional: Close the current activity if you don't want to come back to it
                } else {
                    // Failed to send password reset email
                    Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }





}