package com.example.assignment2


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity20 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main20)
        val signupText = findViewById<ImageView>(R.id.imageView27)

        // Set an OnClickListener for the "Signup" TextView
        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity21::class.java))

        }

    }

}