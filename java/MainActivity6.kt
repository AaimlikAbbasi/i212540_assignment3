package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity6 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)


        val signupText = findViewById<TextView>(com.example.assignment2.R.id.verifyButton)

        // Set an OnClickListener for the "Reset Password" TextView
        signupText.setOnClickListener {
            // Reset the password
            resetPassword()
        }


        val intent = intent
        val email = intent.getStringExtra("email")
        val loginbtn=findViewById<TextView>(R.id.phoneNumber1)
        loginbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }



    private fun resetPassword() {
        val passwordEditText = findViewById<EditText>(R.id.email)
        val confirmPasswordEditText = findViewById<EditText>(R.id.password)

        val newPassword = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (newPassword == confirmPassword) {
            val intent = intent
            val email = intent.getStringExtra("email")

            if (email != null) {
                // Sign in the user using email and password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, newPassword)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            // If sign-in is successful, change the password
                            val user = FirebaseAuth.getInstance().currentUser
                            user?.updatePassword(newPassword)
                                ?.addOnCompleteListener { passwordUpdateTask ->
                                    if (passwordUpdateTask.isSuccessful) {
                                        // Password updated successfully
                                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Failed to update password
                                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // Failed to sign in
                            Toast.makeText(this, "Failed to sign in: ${signInTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            // Passwords do not match, display an error message
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }
    }


}