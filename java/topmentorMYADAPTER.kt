package com.example.assignment2
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class topmentorMYADAPTER (list:  MutableList<topmentorMODEL>, c: Context):
    RecyclerView.Adapter<topmentorMYADAPTER.MyViewHolder>() {
    var list = list
    var context = c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater
            .from(context)
            .inflate(R.layout.top_mentors, parent, false)
        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = list[position]
        holder.firsttext.text = name.name

        val sessioncost = list[position]
        holder.cost.text = sessioncost.sessionCost

        val availability = list[position]
        holder.smallCircleText.text = availability.status

       val description= list[position]
        holder.firsttext2.text=description.description

    }

    override fun getItemCount(): Int {

        return list.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firsttext: TextView = itemView.findViewById(R.id.firsttext)
        val cost: TextView = itemView.findViewById(R.id.cost)
        val smallCircleText: TextView = itemView.findViewById(R.id.smallCircleText)
        val firsttext2: TextView = itemView.findViewById(R.id.firsttext2)

        fun bind(mentor: topmentorMODEL) {
            firsttext.text = mentor.name
            firsttext2.text = mentor.description
            smallCircleText.text = mentor.status
            cost.text = mentor.sessionCost
        }
    }

}