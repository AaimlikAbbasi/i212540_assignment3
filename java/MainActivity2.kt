package com.example.assignment2
import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class MainActivity2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // UI elements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.assignment2.R.layout.activity_main2)

        val text="mentorme"
        val mspanningString= SpannableString(text)
        val myellow = ForegroundColorSpan(Color.parseColor("#FFD700"))

        mspanningString.setSpan(myellow,6,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(com.example.assignment2.R.id.main).text = mspanningString
        auth = FirebaseAuth.getInstance()

        // Retrieve references to UI elements
        val emailEditText = findViewById<EditText>(com.example.assignment2.R.id.email)
        val passwordEditText = findViewById<EditText>(com.example.assignment2.R.id.password)
        val loginButton = findViewById<Button>(com.example.assignment2.R.id.loginButton)
        val forgetPasswordText = findViewById<TextView>(com.example.assignment2.R.id.forgetPasswordText)
        val signupText=findViewById<TextView>(com.example.assignment2.R.id.signupText)
        // Set click listener for login button

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email and password fields are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Please enter both email and password.",
                    Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with sign-in if fields are not empty
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser

                            ///new code for the phone i am writing check this out
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val phoneNumber = currentUser?.phoneNumber
                            if (phoneNumber != null) {
                                // Phone number found, you can use it here
                                Log.d("PhoneNumber", "User's phone number: $phoneNumber")
                            } else {
                                // Phone number not found
                                Log.d("PhoneNumber", "User's phone number not found")
                            }


                            //till here
                            startActivity(Intent(this, MainActivity7::class.java))
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


        // Set click listener for "Forget Your Password?" TextView
        forgetPasswordText.setOnClickListener {
            startActivity(Intent(this, MainActivity5::class.java))
        }

        signupText.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity7::class.java))
            finish()
        }
    }



}
