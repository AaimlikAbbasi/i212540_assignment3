package com.example.assignment2
import retrofit2.Callback

import APIService
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.WindowInsetsAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.Notification.Client
import com.example.assignment2.Notification.Data
import com.example.assignment2.Notification.Myresponse
import com.example.assignment2.Notification.Sender
import com.example.assignment2.Notification.Token
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException




import retrofit2.Call
import retrofit2.http.Query

class MainActivity15 : AppCompatActivity() {

    private lateinit var username: TextView
    private lateinit var Fuser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var btn_send: ImageView
    private lateinit var text_send: EditText
    private  val GALLERY_REQUEST_CODE = 1001
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mChat: MutableList<Chat>
    private lateinit var recyclerView: RecyclerView

    private var isRecording = false
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var recorder: MediaRecorder
    private var outputFile: File? = null

    var notify: Boolean = false

    //for notification
    var apiService: APIService? = null

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main15)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        val signupText = findViewById<ImageView>(R.id.icon3)
        val videocall = findViewById<ImageView>(R.id.imageView15)
        val voicenote = findViewById<ImageView>(R.id.imageView19)
        val camera = findViewById<ImageView>(R.id.imageView29)
        val audiocall = findViewById<ImageView>(R.id.imageView16)
        mediaPlayer = MediaPlayer()
        recorder = MediaRecorder()


        voicenote.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService::class.java)




        signupText.setOnClickListener {
            startActivity(Intent(this, MainActivity18::class.java))
        }

        videocall.setOnClickListener {
            startActivity(Intent(this, MainActivity21::class.java))
        }

        audiocall.setOnClickListener {
            startActivity(Intent(this, MainActivity22::class.java))
        }

        username = findViewById(R.id.username)
        btn_send = findViewById(R.id.imageView20)
        text_send = findViewById(R.id.textsend)

        // Retrieve mentor ID from intent
        val mentorId = intent.getStringExtra("mentorid")
        if (mentorId.isNullOrEmpty()) {
            // Handle the case where mentorId is null or empty
            Toast.makeText(this, "Mentor ID not found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if mentorId is not available
            return
        }

        camera.setOnClickListener {
            val intent = Intent(this, MainActivity19::class.java).apply {
                putExtra("mentorid", mentorId)
            }
            startActivity(intent)
        }



        val userId = intent.getStringExtra("mentorid")!!
        Fuser = FirebaseAuth.getInstance().currentUser!!
        if (Fuser == null) {
            // Handle the case where user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if user is not authenticated
            return
        }

        reference = FirebaseDatabase.getInstance().getReference("mentors").child(mentorId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                username.text = user?.name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
            }
        })

        updateToken(FirebaseAuth.getInstance().currentUser?.uid!!)



        val attachFileImageView = findViewById<ImageView>(R.id.imageView25)
        attachFileImageView.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)

        }

        btn_send.setOnClickListener {
            notify=true
            val msg = text_send.text.toString()
            if (msg.isNotEmpty()) {
                sendMessage(Fuser.uid, userId, msg)
            } else {
                Toast.makeText(this@MainActivity15, "You can't send empty message", Toast.LENGTH_SHORT).show()
            }
            text_send.text.clear()
        }

        readMessage(userId)



    }
    private fun sendMessage(sender: String, receiver: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference

        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        reference.child("Chats").push().setValue(hashMap)
        NotificationHelper(this,message).Notification()
        val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        /**ok set Text msg*/
    }


    private fun uploadImage(imageUri: Uri) {
        val userId = intent.getStringExtra("mentorid")!!
        val storageReference = FirebaseStorage.getInstance().reference.child("images")
        val imageFileName = "${System.currentTimeMillis()}.${getFileExtension(imageUri)}"
        val imageRef = storageReference.child(imageFileName)

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Once image is uploaded, send message with image URL
                sendMessageWithImage(Fuser.uid, userId, downloadUri.toString())
            } else {
                // Handle failures
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun sendMessageWithImage(sender: String, receiver: String, imageUrl: String) {
        val reference = FirebaseDatabase.getInstance().reference

        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["imageUrl"] = imageUrl

        reference.child("Chats").push().setValue(hashMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                // Upload the selected image to Firebase Storage
                uploadImage(selectedImageUri)
            }
        }
    }


    private fun readMessage(userId: String) {
        mChat = mutableListOf()
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mChat.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if ((chat?.receiver == Fuser.uid && chat.sender == userId) || (chat?.receiver == userId && chat.sender == Fuser.uid)) {
                        chat?.let { mChat.add(it) }
                    }
                }
                val messageid = intent.getStringExtra("mentorid")!!
                messageAdapter = MessageAdapter(mChat, this@MainActivity15, userId,object : MessageAdapter.onClickMessage {
                    override fun onItemClick(key: String, isSent: Boolean, view: ImageView) {

                        if(isSent) {
                            deletemymessage(key,view)
                        }
                        else
                        {
                            deletereceivemessage(key,view)
                        }
                    }

                })
                recyclerView.adapter = messageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
        reference.keepSynced(true)
    }



    private fun deletemymessage(key: String,imageView: ImageView){
        var popupMenu = PopupMenu(this@MainActivity15,imageView)
        popupMenu.menuInflater.inflate(R.menu.chatmenu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                when(p0!!.itemId){

                    R.id.delete_menu ->{
                        val reference = FirebaseDatabase.getInstance().getReference("Chats").child(key)
                        reference.removeValue()
                    }
                    R.id.delete_everyone ->{
                        val reference = FirebaseDatabase.getInstance().getReference("Chats").child(key)
                        reference.removeValue()
                    }
                }
                return true
            }

        })
        popupMenu.show()
    }

    private fun deletereceivemessage(key: String,imageView: ImageView){

    }

    private fun startRecording() {
        try {
            // Create a temporary file to store the recorded voice note
            outputFile = File.createTempFile("voice_note", ".3gp", cacheDir)

            // Set up the MediaRecorder
            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                outputFile?.let { setOutputFile(it.absolutePath) }
                prepare()
                start()
            }

            isRecording = true
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("MainActivity15", "Error starting recording: ${e.message}", e)
            // Handle IOException
        } catch (e: IllegalStateException) {
            Log.e("MainActivity15", "Error starting recording: ${e.message}", e)
            // Handle IllegalStateException
        } catch (e: Exception) {
            Log.e("MainActivity15", "Error starting recording: ${e.message}", e)
            // Handle other exceptions
        }
    }


    private fun stopRecording() {
        try {
            recorder.stop()
            recorder.release()
            isRecording = false
            outputFile?.let { outputFile ->
                // Upload the recorded audio file to Firebase Storage
                uploadAudio(outputFile)
            }
        } catch (e: IllegalStateException) {
            Log.e("MainActivity15", "Error stopping recording: ${e.message}", e)
            // Handle IllegalStateException properly
        } catch (e: Exception) {
            Log.e("MainActivity15", "Error stopping recording: ${e.message}", e)
            // Handle other exceptions
        }
    }






    private fun uploadAudio(audioFile: File) {
        val userId = intent.getStringExtra("mentorid")!!
        val storageReference = FirebaseStorage.getInstance().reference.child("audio")
        val audioFileName = "${System.currentTimeMillis()}.3gp"
        val audioRef = storageReference.child(audioFileName)

        val uploadTask = audioRef.putFile(Uri.fromFile(audioFile))
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            audioRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Once audio is uploaded, send message with audio URL
                sendMessageWithAudio(Fuser.uid, userId, downloadUri.toString())
            } else {
                // Handle failures
            }
        }

    }

    private fun sendMessageWithAudio(sender: String, receiver: String, audioUrl: String) {
        val reference = FirebaseDatabase.getInstance().reference

        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["audioUrl"] = audioUrl

        reference.child("Chats").push().setValue(hashMap)
        reference.keepSynced(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        recorder.release()
    }


    private fun updateToken(refreshToken: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(refreshToken)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.uid?.let { reference.child(it).setValue(token) }
    }



}
