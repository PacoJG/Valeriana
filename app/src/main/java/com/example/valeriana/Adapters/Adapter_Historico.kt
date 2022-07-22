package com.example.valeriana.Adapters

import android.R
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.HistoricoActivity
import com.example.valeriana.user_cita


class Adapter_Historico(private val userList: ArrayList<user_cita>, private val onUserClickListenerHome: HistoricoActivity): RecyclerView.Adapter<Adapter_Historico.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Historico.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.example.valeriana.R.layout.card_historico, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val currentitem = userList[position]
        holder.date.text = currentitem.date
        holder.time.text = currentitem.time
        holder.asunto.text = currentitem.asuntoCita
        holder.tag.text = currentitem.tag


        holder.itemView.setOnClickListener(View.OnClickListener { v ->
            val builder = AlertDialog.Builder(v.rootView.context)
            val dialogView: View = LayoutInflater.from(v.rootView.context).inflate(com.example.valeriana.R.layout.custom_dialog, null)
            val dateD : TextView = dialogView.findViewById(com.example.valeriana.R.id.date_CardHome)
            val timeD : TextView = dialogView.findViewById(com.example.valeriana.R.id.time_CardHome)
            val tagD : TextView = dialogView.findViewById(com.example.valeriana.R.id.tvTag)
            val asuntoD : TextView = dialogView.findViewById(com.example.valeriana.R.id.tvAsunto)
            val prescripcionD : TextView = dialogView.findViewById(com.example.valeriana.R.id.tvPrescripcion)
            val indicacionesD : TextView = dialogView.findViewById(com.example.valeriana.R.id.tvIndicaciones)
            dateD.text = currentitem.date
            timeD.text = currentitem.time
            tagD.text = currentitem.tag
            asuntoD.text = currentitem.asuntoCita
            prescripcionD.text = currentitem.prescripcion
            indicacionesD.text = currentitem.indicaciones
            builder.setView(dialogView)
            builder.setCancelable(true)
            builder.show()
        })



    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val name : TextView = itemView.findViewById(R.id.name_user_CardHome)
        //val image : ImageView = itemView.findViewById(R.id.imageProfile)
        //val btnCall : ImageButton = itemView.findViewById(R.id.btnCallUser)
        val date : TextView = itemView.findViewById(com.example.valeriana.R.id.date_CardHome)
        val time : TextView = itemView.findViewById(com.example.valeriana.R.id.time_CardHome)
        val asunto : TextView = itemView.findViewById(com.example.valeriana.R.id.asuntoCita_CardHome)
        val tag : TextView = itemView.findViewById(com.example.valeriana.R.id.tvTag)

    }


}