package com.example.assignment2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
class UserAdapter(private val mContext: Context, private val mUsers: List<User>, private val sourceActivity: Int) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.username)
        var profileImage: CircleImageView = itemView.findViewById(R.id.user_profile)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = mUsers[position]
                    val intent = Intent(mContext, if (sourceActivity == 14) MainActivity15::class.java else MainActivity13::class.java).apply {
                        putExtra("mentorid", user.mentorid)
                    }
                    mContext.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUsers[position]
        holder.username.text = user.name
        if (user.imageUrl.isNullOrEmpty()) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(mContext).load(user.imageUrl).into(holder.profileImage)
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
}
