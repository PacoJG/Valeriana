package com.example.valeriana

import android.Manifest.permission.CALL_PHONE
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.valeriana.calendar.DatePickerFragment
import com.example.valeriana.calendar.TimePickerFragment
import com.example.valeriana.databinding.ActivityDetailedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.net.URLEncoder


class DetailedActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityDetailedBinding

    private var tmpAsunto = ""
    private var tmpDate = ""
    private var tmpTime = ""
    private var tmpDetalle = ""
    private var tmpPrescripcion = ""
    private var tmpIndicaciones = ""
    private var tmpPhone = ""
    private var tmpKey = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()

        val key = intent.getStringExtra("key")
        tmpKey = key.toString()
        Log.d("KEYYYYYYYY", tmpKey)
        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val tag = intent.getStringExtra("tag")
        val asunto = intent.getStringExtra("asunto")
        val date = intent.getStringExtra("date")
        val descripcion = intent.getStringExtra("descripcion")
        val time = intent.getStringExtra("time")
        val prescripcion = intent.getStringExtra("prescripcion")
        val indicaciones = intent.getStringExtra("indicaciones")
        val phone = intent.getStringExtra("phone")

        binding.tvName.text = name
        binding.TagDeatiled.text = tag
        binding.tieAsuntoCita.setText(asunto)
        binding.tieHorario.setText(date)
        binding.tieTime.setText(time)
        binding.tieDetalles.setText(descripcion)
        binding.tiePrescripcion.setText(prescripcion)
        binding.tieIndicacion.setText(indicaciones)
        binding.tiePhone.setText(phone)

        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_profiledefault).into(binding.profileImage)

        binding.tieHorario.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tieTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnBackDeatl.setOnClickListener {
            onBackPressed()
        }

        binding.btnEdit.setOnClickListener {
            validateData()
        }

        binding.btnCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            val numero = phone.toString()
            callIntent.data = Uri.parse("tel:"+numero)
            if (ContextCompat.checkSelfPermission(applicationContext, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent)
            } else {
                requestPermissions(arrayOf(CALL_PHONE), 1)
                Toast.makeText(this,"Otorga permisos a Valeriana para poder llamar",Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnMessage.setOnClickListener {
            val numero = phone.toString()
            whatsApp("+52", numero)
        }

    }

    private fun whatsApp(code: String, phoneNumber: String) {

        try {

            val packageManager = this.packageManager
            val i = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=" +"$code $phoneNumber" + "&text=" +
                    URLEncoder.encode("Hello soy el dentista Paco")
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null){
                startActivity(i)
            }else{
                Toast.makeText(this, "Please Install Whats App", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Toast.makeText(this, ""+e.stackTrace, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun onTimeSelected(time:String){
        binding.tieTime.setText("$time")
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{ day, month, year -> onDateSelected(day,month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month:Int, year: Int){
        if (month == 0){
            binding.tieHorario.setText("jan. $day, $year")
        }else if(month == 1){
            binding.tieHorario.setText("feb. $day, $year")
        }else if(month == 2){
            binding.tieHorario.setText("mar. $day, $year")
        }else if(month == 3){
            binding.tieHorario.setText("apr. $day, $year")
        }else if(month == 4){
            binding.tieHorario.setText("may. $day, $year")
        }else if(month == 5){
            binding.tieHorario.setText("jun. $day, $year")
        }else if(month == 6){
            binding.tieHorario.setText("jul. $day, $year")
        }else if(month == 7){
            binding.tieHorario.setText("aug. $day, $year")
        }else if(month == 8){
            binding.tieHorario.setText("sep. $day, $year")
        }else if(month == 9){
            binding.tieHorario.setText("oct. $day, $year")
        }else if(month == 10){
            binding.tieHorario.setText("nov. $day, $year")
        }else if(month == 11){
            binding.tieHorario.setText("dec. $day, $year")
        }
    }

    private fun validateData() {
        //get data
        tmpAsunto = binding.tieAsuntoCita.text.toString().trim()
        tmpDate = binding.tieHorario.text.toString().trim()
        tmpDetalle = binding.tieDetalles.text.toString().trim()
        tmpIndicaciones = binding.tieIndicacion.text.toString().trim()
        tmpPrescripcion = binding.tiePrescripcion.text.toString().trim()
        tmpPhone = binding.tiePhone.text.toString().trim()
        tmpTime = binding.tieTime.text.toString().trim()
        if (tmpAsunto.isEmpty()){
            Toast.makeText(this,"Ingresa el asunto de la cita",Toast.LENGTH_SHORT).show()
        } else if (tmpDate.isEmpty()){
            Toast.makeText(this,"Ingresa la fecha de la cita",Toast.LENGTH_SHORT).show()
        } else if (tmpTime.isEmpty()){
            Toast.makeText(this,"Ingresa la hora de la cita",Toast.LENGTH_SHORT).show()
        }
        else if (tmpPhone.isEmpty()){
            Toast.makeText(this,"Ingresa algun numero celular",Toast.LENGTH_SHORT).show()
        }
        else{
           updateUser()
        }
    }

    private fun updateUser() {
        progressDialog.setMessage("Actualizando Cita....")
        val firebaseUser = firebaseAuth.currentUser!!
        //setup info
        val hashMmap: HashMap<String, Any> = HashMap()
        hashMmap["asuntoCita"] = "$tmpAsunto"
        hashMmap["date"] = "$tmpDate"
        hashMmap["time"] = "$tmpTime"
        hashMmap["detalles"] = "$tmpDetalle"
        hashMmap["indicaciones"] = "$tmpIndicaciones"
        hashMmap["prescripcion"] = "$tmpPrescripcion"
        hashMmap["phone"] = "$tmpPhone"

        val reference = FirebaseDatabase.getInstance().getReference("pacientes")
        reference.child(firebaseAuth.uid!!).child(tmpKey)
            .updateChildren(hashMmap)
            .addOnSuccessListener {
                //profile update
                progressDialog.dismiss()
                Toast.makeText(this,"Consulta actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Error al actualizar la cita", Toast.LENGTH_SHORT).show()
            }
    }
}

