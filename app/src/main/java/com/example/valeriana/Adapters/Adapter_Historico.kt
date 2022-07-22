package com.example.valeriana.Adapters

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valeriana.Base.BaseViewHolder
import com.example.valeriana.HistoricoActivity
import com.example.valeriana.R
import com.example.valeriana.fragments.UsersFragment
import com.example.valeriana.user_cita
import java.lang.IllegalArgumentException

class Adapter_Historico(private val userList: ArrayList<user_cita>, private val onUserClickListenerHome: HistoricoActivity): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_historico, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int){
        when(holder){
            is MyViewHolder -> holder.bind(userList[position],position)
            else -> throw IllegalArgumentException("Error de viewHolder")
        }

        holder.itemView.setOnClickListener {
            onUserClickListenerHome.onUserItemClicked(position)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class MyViewHolder(itemView: View) : BaseViewHolder<user_cita>(itemView){
        //val name : TextView = itemView.findViewById(R.id.name_user_CardHome)
        //val image : ImageView = itemView.findViewById(R.id.imageProfile)
        //val btnCall : ImageButton = itemView.findViewById(R.id.btnCallUser)
        override fun bind(item: user_cita, position: Int) {
            val date : TextView = itemView.findViewById(R.id.date_CardHome)
            date.text = item.date
            val time : TextView = itemView.findViewById(R.id.time_CardHome)
            time.text = item.time
            val asunto : TextView = itemView.findViewById(R.id.asuntoCita_CardHome)
            asunto.text = item.asuntoCita
            val tag : TextView = itemView.findViewById(R.id.tvTag)
            tag.text = item.tag

        }

    }


}