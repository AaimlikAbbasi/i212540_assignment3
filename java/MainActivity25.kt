package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity25 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main25)
        val signupText = findViewById<TextView>(R.id.main)

        // Set an OnClickListener for the "Signup" TextView
        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity26::class.java))

        }

    }

}