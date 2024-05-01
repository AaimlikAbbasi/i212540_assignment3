package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity14 : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var recyclerView: RecyclerView

    private lateinit var chatAdapter: Chatitemadapter // Use appropriate adapter
    private lateinit var chatItemList: MutableList<CartItem> // cartitem is adapter and model class is cartitem
    private lateinit var chatRecyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main14)

        val signupText = findViewById<ImageView>(R.id.icon3)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = mutableListOf()




        chatRecyclerView = findViewById(R.id.recycler_view2)
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatItemList = mutableListOf()
        chatAdapter = Chatitemadapter(this, chatItemList)
        chatRecyclerView.adapter = chatAdapter





        readUsers()

        // Find other views by their IDs
        val profile1 = findViewById<ImageView>(R.id.profileImage1)
        val profile2 = findViewById<ImageView>(R.id.man22)
        val profile3 = findViewById<ImageView>(R.id.man33)
        val profile4 = findViewById<ImageView>(R.id.man44)
        val profile5 = findViewById<ImageView>(R.id.man55)
    }

    private fun readUsers() {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("mentors")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    val user: User? = snapshot.getValue(User::class.java)
                    if (user != null && firebaseUser != null && user.mentorid != firebaseUser.uid) {
                        userList.add(user)
                    }
                }
                // Initialize the adapter and set it to the RecyclerView
                userAdapter = UserAdapter(this@MainActivity14, userList,sourceActivity = 14)
                recyclerView.adapter = userAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancelled event if needed
            }
        })
        reference.keepSynced(true)
    }


}


data class CartItem(
    val name: String? = null
)

