package com.example.assignment2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity8 : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchList: MutableList<User>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)




        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = mutableListOf()

        readUsers()

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
                userAdapter = UserAdapter(this@MainActivity8, userList,sourceActivity = 8)
                recyclerView.adapter = userAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancelled event if needed
            }
        })

        reference.keepSynced(true)
    }


}


