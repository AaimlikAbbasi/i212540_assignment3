package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class MainActivity23 : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main23)



        // Set onClickListeners for the icons
        val home = findViewById<ImageView>(R.id.icon1)
        home.setOnClickListener {
            startActivity(Intent(this, MainActivity7::class.java))
        }



        val profile = findViewById<ImageView>(R.id.icon2)
        profile.setOnClickListener {
            startActivity(Intent(this, MainActivity23::class.java))
        }

        val search = findViewById<ImageView>(R.id.icon3)
        search.setOnClickListener {
            startActivity(Intent(this, MainActivity8::class.java))
        }
        val booksession=findViewById<Button>(R.id.loginButton)
        booksession.setOnClickListener {
            startActivity(Intent(this, MainActivity8::class.java))
        }


        val editimage= findViewById<ImageView>(R.id.imageView34)
        editimage.setOnClickListener {
            startActivity(Intent(this, EDITPROFILEimage::class.java))
        }
        val editprofile= findViewById<ImageView>(R.id.imageView35)
        editprofile.setOnClickListener {
            startActivity(Intent(this, MainActivity24::class.java))
        }


        val feedback= findViewById<TextView>(R.id.main2)
        feedback.setOnClickListener {
            startActivity(Intent(this, MainActivity11::class.java))
        }

    }

}
