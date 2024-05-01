package com.example.assignment2

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class MainActivity7 : AppCompatActivity(),ScreenshotDetectionDelegate.ScreenshotDetectionListener{
    private lateinit var auth: FirebaseAuth
    private lateinit var mentorList : MutableList<topmentorMODEL>
    private lateinit var mentorAdapter: topmentorMYADAPTER
    private lateinit var dbRef: DatabaseReference
    private lateinit var mentorRecyclerView: RecyclerView
    private val TAG = "MainActivity7"

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
    }


    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(com.example.assignment2.R.layout.activity_main7)


        val editTextText = findViewById<EditText>(com.example.assignment2.R.id.editTextText)


        val aliColor = Color.parseColor("#0B7C71")
        // Get the current user's ID from Firebase Authentication
        val helloAliText = "Hello, Ali"


        // Create a SpannableString to apply different styles to different parts of the text
        val spannableString = SpannableString(helloAliText)

        // Set color for "Ali"
        spannableString.setSpan(ForegroundColorSpan(aliColor), 7, helloAliText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the SpannableString to the EditText
        editTextText.setText(spannableString)








        val signupText = findViewById<ImageView>(com.example.assignment2.R.id.icon3)

        // Set an OnClickListener for the "Signup" TextView
        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity8::class.java))

        }



          val profile=findViewById<ImageView>(R.id.icon2)
        profile.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity23::class.java))

        }
        mentorRecyclerView = findViewById<RecyclerView>(R.id.topmentor)

        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mentorRecyclerView.layoutManager = horizontalLayoutManager
        mentorList = ArrayList() // Initialize mentorList


// Sample mentor data
        getmentordata()




        val addmentor = findViewById<ImageView>(com.example.assignment2.R.id.imageView5)

        // Set an OnClickListener for the "Signup" TextView
        addmentor.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity12::class.java))

        }

        val chat = findViewById<ImageView>(com.example.assignment2.R.id.icon4)

        // Set an OnClickListener for the "Signup" TextView
        chat.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity14::class.java))

        }




    }

private fun getmentordata() {


    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    dbRef = FirebaseDatabase.getInstance().getReference("mentors")
    dbRef.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            mentorList.clear()
            if(snapshot.exists()){
                for(mentorSnapshot in snapshot.children){
                    val mentor = mentorSnapshot.getValue(topmentorMODEL::class.java)
                    if (mentor != null) {
                        mentorList.add(mentor)
                    }
                }

                val adapter = topmentorMYADAPTER(mentorList, this@MainActivity7)
                mentorRecyclerView.adapter = adapter

            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e(TAG, "Failed to read value.", error.toException())
        }

    })

    dbRef.keepSynced(true)

}


    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }
    override fun onScreenCaptured(path: String) {
        Log.d(TAG, "Screenshot captured: $path")
        NotificationHelper(this,"screen shot").Notification()
        val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        /**ok set Text msg*/

    }



    override fun onScreenCapturedWithDeniedPermission() {
        Log.d(TAG, "Screenshot captured but was denied permission")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MainActivity7.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_DENIED) {
                    showReadExternalStoragePermissionDeniedMessage()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission()
        }
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            MainActivity7.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION
        )
    }

    private fun showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "Read external storage permission has denied", Toast.LENGTH_SHORT).show()
    }








}


