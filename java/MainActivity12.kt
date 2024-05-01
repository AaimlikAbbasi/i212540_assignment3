package com.example.assignment2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.database.Logger
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MainActivity12 : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private var imageUrl: String = ""


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            // Get a reference to the root of your Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference

            // Generate a unique filename for the image based on the current time
            val filename = "${System.currentTimeMillis()}_dp.jpg"

            // Get a reference to the folder where you want to upload the image (e.g., "images")
            val folderRef = storageRef.child("images")

            // Get a reference to the file inside the folder
            val fileRef = folderRef.child(filename)

            // Upload the image file to Firebase Storage
            fileRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Image upload successful, get the download URL
                    fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Store the download URL in imageUrl
                        imageUrl = downloadUri.toString()
                    }.addOnFailureListener { exception ->
                        // Handle failure to retrieve image URL
                        Toast.makeText(this, "Failed to get download URL: $exception", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Image upload failed
                    Toast.makeText(this, "Failed to Upload: $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }









    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main12)


        val spinner: Spinner = findViewById(R.id.name2)
        val cities = arrayOf("Enter status", "Available", "Unavailable") // Example data
        // Set an OnClickListener for the "Signup" TextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        dbRef= FirebaseDatabase.getInstance().getReference("mentors")

        val signupText = findViewById<ImageView>(R.id.icon3)
        val upload = findViewById<TextView>(R.id.loginButton3)
        val nameEditText = findViewById<EditText>(R.id.name)
        val descriptionEditText = findViewById<EditText>(R.id.name1)
        val statusSpinner = findViewById<Spinner>(R.id.name2)
        val sessionCostEditText = findViewById<EditText>(R.id.name3)
        val dp=findViewById<ImageView>(R.id.imageView8)
        val video=findViewById<ImageView>(R.id.imageView7)
        val icon1=findViewById<ImageView>(R.id.icon1)



        val addmentor = findViewById<ImageView>(com.example.assignment2.R.id.imageView5)

        // Set an OnClickListener for the "Signup" TextView
        addmentor.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity12::class.java))

        }




        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity11::class.java))

        }


        icon1.setOnClickListener {
            // Start MainActivity3

            val intent = Intent(this@MainActivity12, MainActivity7::class.java)
            intent.putExtra("name", nameEditText.text.toString())
            startActivity(intent)
        }






        var pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                video.setImageURI(it)
                var storageRef = FirebaseStorage.getInstance()
                var filename = System.currentTimeMillis().toString() + "video.mp4"
                var ref = storageRef.getReference("videos/$filename")
                ref.putFile(it)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            Toast.makeText(this, "Video Uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        video.setOnClickListener {
            pickVideo.launch("video/*")
        }

        upload.setOnClickListener {
            Toast.makeText(this@MainActivity12, "button clicked successfully", Toast.LENGTH_SHORT).show()
            // Retrieve data from EditText and Spinner
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val status = statusSpinner.selectedItem.toString()
            val sessionCost = sessionCostEditText.text.toString().trim()

            if (imageUrl.isNotBlank()) {
                // Write the retrieved data to Firebase
                writeDataToFirebase(name, description, status, sessionCost, imageUrl)
            } else {
                // Handle case where image URL is blank
                Toast.makeText(this@MainActivity12, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
            NotificationHelper(this,"New Mentor Added").Notification()
            val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }



        dp.setOnClickListener {
            // Launch the activity for picking an image
            pickImage.launch("image/*")
        }


    }

    private fun writeDataToFirebase(name: String, description: String, status: String, sessionCost: String, imageUrl: String) {
        Toast.makeText(this@MainActivity12, "enter in function", Toast.LENGTH_SHORT).show()

        // Generate a unique mentor ID
        val mentorId = dbRef.push().key!!

        // Create a realtimemodel object with the provided data
        val mentor = realtimemodel(mentorId, name, description, status, sessionCost, imageUrl)

        // Find views by their IDs
        val nameEditText = findViewById<EditText>(R.id.name)
        val descriptionEditText = findViewById<EditText>(R.id.name1)
        val statusSpinner = findViewById<Spinner>(R.id.name2)
        val sessionCostEditText = findViewById<EditText>(R.id.name3)
        val dp = findViewById<ImageView>(R.id.imageView8)
        val video = findViewById<ImageView>(R.id.imageView7)

        // Write mentor data to Firebase Realtime Database
        dbRef.child(mentorId).setValue(mentor)
            .addOnSuccessListener {
                // Display success message
                Toast.makeText(this@MainActivity12, "Mentor added successfully", Toast.LENGTH_SHORT)
                    .show()
                // Clear input fields
                nameEditText.text.clear()
                descriptionEditText.text.clear()
                sessionCostEditText.text.clear()
                statusSpinner.setSelection(0) // Reset spinner selection
                // Reset image views
                dp.setImageResource(R.drawable.camera_svgrepo_com)
                video.setImageResource(android.R.drawable.presence_video_online)
            }
            .addOnFailureListener { exception ->
                // Log and display error message if failed to add mentor
                Log.e(TAG, "Failed to add mentor", exception)
                Toast.makeText(this@MainActivity12, "Failed to add mentor", Toast.LENGTH_SHORT)
                    .show()
            }
    }


}