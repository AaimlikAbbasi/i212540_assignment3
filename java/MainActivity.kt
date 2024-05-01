package com.example.assignment2
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import java.util.logging.Handler
import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),ScreenshotDetectionDelegate.ScreenshotDetectionListener {

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
    }


    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)

    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.assignment2.R.layout.activity_main)

        checkReadExternalStoragePermission()
        val text="mentorme"
        val mspanningString= SpannableString(text)
        val myellow = ForegroundColorSpan(Color.parseColor("#FFD700"))

        mspanningString.setSpan(myellow,6,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.main).text = mspanningString

        val secondActivityBtn=findViewById<TextView>(com.example.assignment2.R.id.mentor2)

        secondActivityBtn.setOnClickListener {
            // Code to execute when the button is clicked
            val intent = Intent(this, mentorlogin::class.java)
            startActivity(intent)
        }


        val user=findViewById<TextView>(com.example.assignment2.R.id.user)

      user.setOnClickListener {
            // Code to execute when the button is clicked
            val intent = Intent(this,  MainActivity2::class.java)
            startActivity(intent)
        }



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
            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
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
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
    }

    private fun showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "Read external storage permission has denied", Toast.LENGTH_SHORT).show()
    }

}