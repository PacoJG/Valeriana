package com.example.valeriana.Adapters

import android.Manifest
import android.Manifest.permission.CALL_PHONE
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valeriana.Base.BaseViewHolder
import com.example.valeriana.R
import com.example.valeriana.fragments.UsersFragment
import com.example.valeriana.user_cita
import java.lang.IllegalArgumentException

class Adapter_Directorio(private val context: Context, private val userList: ArrayList<user_cita>, private val onUserClickListenerHome: UsersFragment): RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_directorio, parent, false)
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
            val name : TextView = itemView.findViewById(R.id.name_user_CardHome)
            name.text = item.name
            val image : ImageView = itemView.findViewById(R.id.imageProfile)
            Glide.with(context).load(item.imageUrl).into(image)
            val btnCall : ImageButton = itemView.findViewById(R.id.btnCallUser)
            btnCall.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL)
                val numero = item.phone
                callIntent.data = Uri.parse("tel:"+numero)
                val result = ContextCompat.checkSelfPermission(context, CALL_PHONE)
                if (result == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(callIntent)
                } else {
                    //context.requestForCallPermission()
                }
            }
        }

    }


}