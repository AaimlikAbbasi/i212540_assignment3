package com.example.assignment2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MainActivity11 : AppCompatActivity() {
    private var rating = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main11)

        val star1 = findViewById<ImageView>(R.id.star1)
        val star2 = findViewById<ImageView>(R.id.star2)
        val star3 = findViewById<ImageView>(R.id.star3)
        val star4 = findViewById<ImageView>(R.id.star4)
        val star5 = findViewById<ImageView>(R.id.star5)
        val feedbackEditText = findViewById<EditText>(R.id.editText)

        // Set click listeners for each star
        star1.setOnClickListener { setRating(1) }
        star2.setOnClickListener { setRating(2) }
        star3.setOnClickListener { setRating(3) }
        star4.setOnClickListener { setRating(4) }
        star5.setOnClickListener { setRating(5)}

        // Save button click listener
        val save= findViewById<TextView>(R.id.loginButton3)
       save.setOnClickListener {
            val feedback = feedbackEditText.text.toString()
            // Store the rating and feedback in Firebase
            saveRatingAndFeedback(rating, feedback,feedbackEditText)
        }
    }


    private fun setRating(selectedRating: Int) {
        // Update the rating variable
        rating = selectedRating

        // Update star images based on the selected rating
        val star1 = findViewById<ImageView>(R.id.star1)
        val star2 = findViewById<ImageView>(R.id.star2)
        val star3 = findViewById<ImageView>(R.id.star3)
        val star4 = findViewById<ImageView>(R.id.star4)
        val star5 = findViewById<ImageView>(R.id.star5)

        // Set all stars to empty
        star1.setImageResource(R.drawable.star_empty_svgrepo_com)
        star2.setImageResource(R.drawable.star_empty_svgrepo_com)
        star3.setImageResource(R.drawable.star_empty_svgrepo_com)
        star4.setImageResource(R.drawable.star_empty_svgrepo_com)
        star5.setImageResource(R.drawable.star_empty_svgrepo_com)

        // Fill stars up to the selected rating
        when (selectedRating) {
            1 -> star1.setImageResource(R.drawable.star_full)
            2 -> {
                star1.setImageResource(R.drawable.star_full)
                star2.setImageResource(R.drawable.star_full)
            }

            3 -> {
                star1.setImageResource(R.drawable.star_full)
                star2.setImageResource(R.drawable.star_full)
                star3.setImageResource(R.drawable.star_full)
            }

            4 -> {
                star1.setImageResource(R.drawable.star_full)
                star2.setImageResource(R.drawable.star_full)
                star3.setImageResource(R.drawable.star_full)
                star4.setImageResource(R.drawable.star_full)
            }

            5 -> {
                star1.setImageResource(R.drawable.star_full)
                star2.setImageResource(R.drawable.star_full)
                star3.setImageResource(R.drawable.star_full)
                star4.setImageResource(R.drawable.star_full)
                star5.setImageResource(R.drawable.star_full)
            }
        }
    }



    private fun saveRatingAndFeedback(rating: Int, feedback: String,feedbackEditText:EditText) {
        val database = FirebaseDatabase.getInstance()
        val feedbackRef = database.getReference("feedback")

        // Create a unique key for each feedback entry
        val feedbackId = feedbackRef.push().key!!

        // Create a Feedback object to store rating and feedback
        val feedbackObject = Feedback(rating, feedback)

        // Store the feedback object in the database under the unique key
        feedbackRef.child(feedbackId).setValue(feedbackObject)
            .addOnSuccessListener {
                // Feedback saved successfully
                feedbackEditText.text.clear() // Clear the feedback EditText after saving
                // Optionally, show a toast or perform any other action to indicate successful saving
                Toast.makeText(this, "FeedBack Send Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity11, MainActivity21::class.java)
                startActivity(intent)
                NotificationHelper(this,feedback).Notification()
                val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)

            }
            .addOnFailureListener { exception ->
                // Failed to save feedback
                // Handle the failure, such as showing an error message to the user
            }
        feedbackRef.keepSynced(true)
    }









}

data class Feedback(val rating: Int, val feedback: String)
