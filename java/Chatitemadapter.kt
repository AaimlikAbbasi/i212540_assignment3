package com.example.assignment2


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.CartItem
import com.example.assignment2.R
import de.hdodenhof.circleimageview.CircleImageView

class Chatitemadapter(private val context: Context, private val itemList: List<CartItem>) : RecyclerView.Adapter<Chatitemadapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.username)
        var profileImage: CircleImageView = itemView.findViewById(R.id.user_profile)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = itemList[position]
                    val intent = Intent(context, MainActivity15::class.java).apply {
                       // putExtra("mentorid", user.mentorid)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return TODO("Provide the return value")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}