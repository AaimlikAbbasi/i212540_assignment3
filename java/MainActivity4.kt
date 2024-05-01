package com.example.assignment2

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.assignment2.R.layout.activity_main4)



        val verifyButton = findViewById<TextView>(com.example.assignment2.R.id.verifyButton)
        val otpEditTexts = arrayOf(
            findViewById<EditText>(com.example.assignment2.R.id.ring1),
            findViewById<EditText>(com.example.assignment2.R.id.ring2),
            findViewById<EditText>(com.example.assignment2.R.id.ring3),
            findViewById<EditText>(com.example.assignment2.R.id.ring4),
            findViewById<EditText>(com.example.assignment2.R.id.ring5),
            findViewById<EditText>(com.example.assignment2.R.id.ring6)
        )




    }




}