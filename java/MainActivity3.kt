package com.example.assignment2

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.TimeUnit


class MainActivity3 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    private lateinit var dbRef: DatabaseReference
    private lateinit var username: EditText
    private lateinit var usercity: Spinner
    private lateinit var userphoneno: EditText
    private lateinit var usercountry: Spinner
    private lateinit var useremail: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val text = "mentorme"
        val mspanningString = SpannableString(text)
        val myellow = ForegroundColorSpan(Color.parseColor("#FFD700"))
        mspanningString.setSpan(myellow, 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.main).text = mspanningString

        val spinner: Spinner = findViewById(R.id.citySpinner)
        val cities = arrayOf("Enter City", "New York", "London", "Tokyo") // Example data
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val countrySpinner: Spinner = findViewById(R.id.countrySpinner)
        val countries = arrayOf("Enter Country", "USA", "UK", "Japan", "France") // Example data
        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = countryAdapter

        val signupButton = findViewById<TextView>(R.id.loginButton)



        signupButton.setOnClickListener {

            val name=findViewById<EditText>(R.id.name).text.toString()
            val email = findViewById<TextView>(R.id.email).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()
            val city = spinner.selectedItem.toString()
            val country = countrySpinner.selectedItem.toString()
            val contactNumber = findViewById<EditText>(R.id.contactnumber).text.toString()

            // Validate email, password, city, country, and contact number fields
            if (email.isEmpty() || password.isEmpty() || city == "Enter City" || country == "Enter Country" || contactNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            // Sign up the user
            registerUser(email, password,city, country, contactNumber)
            saveUserData(name,email, password, contactNumber, city, country)



            val defaultImageUri = Uri.parse("android.resource://${packageName}/${R.drawable.rod}")

            val storageRef = FirebaseStorage.getInstance().reference


            val personName= name


            val filename = "${System.currentTimeMillis()}_dp.jpg"


            val folderRef = storageRef.child(personName)


            val fileRef = folderRef.child(filename)


            fileRef.putFile(defaultImageUri)
                .addOnSuccessListener {
                    // Image upload successful
                    Toast.makeText(this, "Default Image Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Image upload failed
                    Toast.makeText(this, "Failed to Upload Default Image: $exception", Toast.LENGTH_SHORT).show()
                }







        }

        val loginText = findViewById<TextView>(R.id.signupText)
        loginText.setOnClickListener {
            startActivity(Intent(this, MainActivity4::class.java))
            finish()
        }




        dbRef = FirebaseDatabase.getInstance().getReference("USER")



    }


    private fun registerUser(email: String, password: String, phoneNumber: String, city: String, country: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser

                    // Update user profile with additional information
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName("$city, $country")
                        .build()
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                Log.d("Registration", "User profile updated.")
                            } else {
                                Log.w("Registration", "Failed to update user profile.", profileUpdateTask.exception)
                            }
                        }

                    // Send OTP for phone number verification
                    Toast.makeText(this@MainActivity3, "Sign up successfull:", Toast.LENGTH_SHORT).show()
                    sendOTP(phoneNumber)
                } else {
                    // Registration failed
                    Log.w("Registration", "User registration failed", task.exception)
                    Toast.makeText(this@MainActivity3, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendOTP(phoneNumber: String) {
        // Construct options for phone number verification
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    // Navigate to the next page where OTP can be entered
                    val intent = Intent(this@MainActivity3, MainActivity4::class.java)
                    intent.putExtra("verificationId", verificationId)
                    startActivity(intent)
                    finish() // Finish current activity to prevent going back to registration page
                }


                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval of verification code is triggered, so this method may not be called
                    // But you can handle verification completion if needed
                    Toast.makeText(baseContext, "OTP send.", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle verification failure
                    Toast.makeText(baseContext, "Failed to send OTP.", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        // Start phone number verification
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    private fun saveUserData(name:String,email: String, password: String, phoneNumber: String, city: String, country: String) {

        //getting values

        val empId =  FirebaseDatabase.getInstance().reference.child("USER").push().key ?: ""

        val employee = EmployeeModel(empId, name,email, password, phoneNumber, city,country)


        val citySpinner = findViewById<Spinner>(R.id.citySpinner)
        val countrySpinner = findViewById<Spinner>(R.id.countrySpinner)
        val cityIndex = citySpinner.selectedItemPosition
        val countryIndex = countrySpinner.selectedItemPosition

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                findViewById<EditText>(R.id.name).text.clear()
                findViewById<EditText>(R.id.email).text.clear()
                findViewById<EditText>(R.id.password).text.clear()
                findViewById<EditText>(R.id.contactnumber).text.clear()


                citySpinner.setSelection(cityIndex)
                countrySpinner.setSelection(countryIndex)

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }


}

data class EmployeeModel(
   val  empId: String,
   val  name: String,
   val  email: String,
   val  password: String,
   val  phoneNumber: String,
   val  city: String,
    val country: String
)

