package com.example.assignment2


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MainActivity24 : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    // Views
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var cityEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main24)
        val signupText = findViewById<TextView>(R.id.loginButton3)

        // Set an OnClickListener for the "Signup" TextView
        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity25::class.java))

        }


        // Initialize Views
        nameEditText = findViewById(R.id.name)
        emailEditText = findViewById(R.id.name1)
        contactEditText = findViewById(R.id.name2)
        countryEditText = findViewById(R.id.name22)
        cityEditText = findViewById(R.id.name23)

        // Button to update profile
        val updateProfileButton = findViewById<TextView>(R.id.loginButton3)
        updateProfileButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val contact = contactEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            updateProfile(name,email,contact,country,city)
        }



    }
    private fun updateProfile(name: String, email: String, contact: String, country: String, city: String) {
        // Get current user's UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            // Reference to the current user's data using UID
            val userRef = FirebaseDatabase.getInstance().getReference("USER").child(uid)

            // Create an EmployeeModel instance with updated data
            val employee = EmployeeModel(uid, name, email, "", contact, country, city)

            // Update the user's profile data using .set
            userRef.setValue(employee)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // UID is not available (user not authenticated)
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

}