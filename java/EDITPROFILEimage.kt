package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage

class EDITPROFILEimage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofileimage)

        val nameEditText = findViewById<EditText>(R.id.name)
        val dp = findViewById<ImageView>(R.id.imageView8)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                dp.setImageURI(uri)

                // Get a reference to the root of your Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference

                // Get the name of the person from the EditText
                val personName = nameEditText.text.toString()

                // Generate a unique filename for the image based on the current time
                val filename = "${System.currentTimeMillis()}_dp.jpg"

                // Get a reference to the folder where the picture is stored
                val folderRef = storageRef.child(personName)

                // Get a reference to the existing picture file
                folderRef.listAll().addOnSuccessListener { result ->
                    for (item in result.items) {
                        // Image upload successful
                        Toast.makeText(this, "enter teh loop", Toast.LENGTH_SHORT).show()
                        // Delete the existing picture
                        item.delete().addOnSuccessListener {
                            // Image upload successful
                            Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
                            // Upload the new picture file to the specified folder
                            val fileRef = folderRef.child(filename)
                            fileRef.putFile(uri)
                                .addOnSuccessListener {
                                    // Image upload successful
                                    Toast.makeText(this, "Image Updated", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { exception ->
                                    // Image upload failed
                                    Toast.makeText(this, "Failed to Update Image: $exception", Toast.LENGTH_SHORT).show()
                                }
                        }.addOnFailureListener { exception ->
                            // Error occurred while deleting existing picture
                            Toast.makeText(this, "Failed to Delete Existing Image: $exception", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    // Error occurred while retrieving existing picture
                    Toast.makeText(this, "Failed to Retrieve Existing Image: $exception", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dp.setOnClickListener {
            // Launch the activity for picking an image
            pickImage.launch("image/*")
        }




        val addmentor2 = findViewById<TextView>(com.example.assignment2.R.id.loginButton3)

        // Set an OnClickListener for the "Signup" TextView
        addmentor2.setOnClickListener {
            // Start MainActivity3
            Toast.makeText(this, "Succesfully update the image", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity23::class.java))

        }


    }
}