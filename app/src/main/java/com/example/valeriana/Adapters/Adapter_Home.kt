package com.example.valeriana.Adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.OnUserClickListenerHome
import com.example.valeriana.R
import com.example.valeriana.fragments.CalendarFragment
import com.example.valeriana.fragments.HomeFragment
import com.example.valeriana.user_cita

class Adapter_Home(private val userList: ArrayList<user_cita>, private val onUserClickListenerHome: HomeFragment): RecyclerView.Adapter<Adapter_Home.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_descriptive_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val currentitem = userList[position]
        holder.date.text = currentitem.date
        holder.time.text = currentitem.time
        holder.name.text = currentitem.name
        holder.asuntoCita.text = currentitem.asuntoCita
        holder.tag.text = currentitem.tag
        holder.image.setImageResource(R.drawable.image_cita)

        holder.itemView.setOnClickListener {
            onUserClickListenerHome.onUserItemClicked(position)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val date : TextView = itemView.findViewById(R.id.date_CardHome)
        val time : TextView = itemView.findViewById(R.id.time_CardHome)
        val name : TextView = itemView.findViewById(R.id.name_user_CardHome)
        val asuntoCita : TextView = itemView.findViewById(R.id.asuntoCita_CardHome)
        val tag : TextView = itemView.findViewById(R.id.Tag_CardHome)
        val image : ImageView = itemView.findViewById(R.id.item_image_carddescriptive)
    }


}