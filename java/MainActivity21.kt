package com.example.assignment2

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.assignment2.databinding.ActivityMainBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

class MainActivity21 : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val appId = "945e7560b9774465b4844abaa92cf76e"
    private val channelName = "mentor"
    private val uid = 0
    private var isJoined = false

    private var agoraEngine: RtcEngine? = null
    private val token="007eJxTYJAotJMw6wyRX/7V0ryz/tL9Sc+Ld+4R1FLgNtlWbP1ugqwCg6WJaaq5qZlBkqW5uYmJmWmSiYWJSWJSYqKlUXKauVnqVaHfqQ2BjAyyxvuZGBkgEMRnY8hNzSvJL2JgAADVjh4i"
    private var localSurfaceView: SurfaceView? = null

    private var remoteSurfaceView: SurfaceView? = null

    private val PERMISSION_REQ_ID = 12
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA
    )
    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    REQUESTED_PERMISSIONS[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }

    fun showMessage(message: String?) {
        runOnUiThread {
              Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main21)


        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        setupVideoSDKEngine();


        val signupText = findViewById<ImageView>(R.id.imageView23)

        // Set an OnClickListener for the "Signup" TextView
        signupText.setOnClickListener {
            // Start MainActivity3
            startActivity(Intent(this, MainActivity22::class.java))

        }

        joinCall()
        val leavecall=findViewById<ImageView>(R.id.imageView12)
        leavecall.setOnClickListener{
            // Start MainActivity3
           leavecall()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine!!.stopPreview()
        agoraEngine!!.leaveChannel()

        Thread {
            RtcEngine.destroy()
            agoraEngine = null
        }.start()
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            showMessage("Remote user joined $uid")

            // Set the remote video view
            runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
            runOnUiThread { remoteSurfaceView!!.visibility = View.GONE }
        }
    }




    private fun leavecall() {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine!!.leaveChannel()
            showMessage("You left the channel")
            if (remoteSurfaceView != null) remoteSurfaceView!!.visibility = View.GONE
            if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE
            isJoined = false
        }
    }
    private fun joinCall() {
        if (checkSelfPermission()) {
            val options = ChannelMediaOptions()

            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            setupLocalVideo()
            localSurfaceView!!.visibility = View.VISIBLE
            agoraEngine!!.startPreview()
            agoraEngine!!.joinChannel(token, channelName, uid, options)
        } else {
            Toast.makeText(applicationContext, "Permissions was not granted", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun setupRemoteVideo(uid: Int) {
        val remoteSurfaceView = SurfaceView(this)
        remoteSurfaceView.setZOrderMediaOverlay(true)

        val remoteVideoContainer = findViewById<FrameLayout>(R.id.remote)
        remoteVideoContainer.addView(remoteSurfaceView)

        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )

        remoteSurfaceView.visibility = View.VISIBLE
    }





    private fun setupLocalVideo() {
        localSurfaceView = SurfaceView(baseContext)
        val localVideoViewContainer = findViewById<FrameLayout>(R.id.rectangle)
        localVideoViewContainer.addView(localSurfaceView)

        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
        )
    }


}