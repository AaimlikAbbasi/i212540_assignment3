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

class MainActivity22 : AppCompatActivity() {
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
        return ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAudioSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)

            // Disable video module
            agoraEngine!!.disableVideo()

            // Enable audio module
            agoraEngine!!.enableAudio()

        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main22)



        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        setupAudioSDKEngine();

        joinAudioCall()
        val leavecall=findViewById<ImageView>(R.id.imageView12)
        leavecall.setOnClickListener{
            // Start MainActivity3
            leaveAudioCall()

        }

    }


    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            showMessage("Remote user joined $uid")

            // Set the remote video view
            runOnUiThread { setupRemoteAudio(uid) }
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



    private fun leaveAudioCall() {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine!!.leaveChannel()
            showMessage("You left the channel")
            // Hide audio-related views if needed
            isJoined = false
        }
        showMessage("Audio call started") // Add this line to display a message
    }

    private fun joinAudioCall() {
        if (checkSelfPermission()) {
            val options = ChannelMediaOptions()
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            // Configure for audio calls only

            // Setup local audio (if needed)

            agoraEngine!!.joinChannel(token, channelName, uid, options)
            showMessage("Audio call started") // Add this line to display a message
        } else {
            Toast.makeText(applicationContext, "Permissions were not granted", Toast.LENGTH_SHORT)
                .show()
        }
    }

// Implement setupRemoteAudio and setupLocalAudio functions accordingly if needed
private fun setupRemoteAudio(uid: Int) {
    // Setup remote audio rendering
    agoraEngine?.setDefaultAudioRoutetoSpeakerphone(true) // Set to true if you want to route audio to the speakerphone
}

    private fun setupLocalAudio() {
        // Setup local audio settings
        agoraEngine?.enableAudio()
        agoraEngine?.adjustRecordingSignalVolume(100) // Adjust microphone volume
    }


}
