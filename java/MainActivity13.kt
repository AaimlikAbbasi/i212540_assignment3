package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase

class MainActivity13 : AppCompatActivity() {
    private var selectedTime: String = ""
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main13)
        // Assuming you have initialized your TextView
        val timeSlots = listOf<TextView>(
            findViewById(R.id.o1), findViewById(R.id.t2), findViewById(R.id.t3),
            findViewById(R.id.t4), findViewById(R.id.f5), findViewById(R.id.s6),
            findViewById(R.id.s7)
        )

        // Set OnClickListener to each TextView for time slots
        timeSlots.forEach { textView ->
            textView.setOnClickListener {
                // Get the time slot text when clicked
                selectedTime = textView.text.toString()
                // Highlight the selected time slot or perform any other action
                textView.background = ContextCompat.getDrawable(this, R.drawable.lightbluecircle)
                // Un-highlight other time slots if needed
                timeSlots.filterNot { it == textView }.forEach { it.background = null }
            }
        }

        // Set OnClickListener to each TextView for dates
        val dateSlots = listOf<TextView>(
            findViewById(R.id.button), findViewById(R.id.button3), findViewById(R.id.button4)
        )

        dateSlots.forEach { textView ->
            textView.setOnClickListener {
                // Get the date text when clicked
                selectedDate = textView.text.toString()
                // Highlight the selected date or perform any other action
                textView.background = ContextCompat.getDrawable(this, R.drawable.activity10_rec)
                // Un-highlight other dates if needed
                dateSlots.filterNot { it == textView }.forEach { it.background = null }
            }
        }

        // Upload button click listener
        val uploadButton = findViewById<TextView>(R.id.loginButton3)
        uploadButton.setOnClickListener {
            // Retrieve mentor ID from intent
            val mentorId = intent.getStringExtra("mentorid")

            // Check if both a time slot and a date are selected
            if (selectedTime.isNotEmpty() && selectedDate.isNotEmpty()) {
                // Book the mentor in the database
                bookMentor(mentorId, selectedDate, selectedTime)
            } else {
                // Inform the user to select both a time slot and a date
                Toast.makeText(this, "Please select both a date and a time slot", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bookMentor(mentorId: String?,selectedTime : String,selectedDate : String) {
        // Check if mentorId is not null before proceeding
        mentorId?.let { nonNullMentorId ->
            // Write the booking information to the Firebase Realtime Database
            val reference = FirebaseDatabase.getInstance().reference
            val bookingId = reference.child("bookmentor").push().key // Generate unique booking ID
            val bookingData = HashMap<String, Any>()
            bookingData["mentorId"] = nonNullMentorId
            bookingData["date"] = selectedDate
            bookingData["timeSlot"] = selectedTime
            // Add other booking information if needed

            bookingId?.let { nonNullBookingId ->
                reference.child("bookmentor").child(nonNullBookingId).setValue(bookingData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } ?: run {
            // Handle the case where mentorId is null
            Toast.makeText(this, "Mentor ID is null", Toast.LENGTH_SHORT).show()
        }
    }
}