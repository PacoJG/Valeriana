package com.example.valeriana

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.valeriana.calendar.DatePickerFragment
import com.example.valeriana.calendar.TimePickerFragment
import com.example.valeriana.databinding.ActivityAddPacienteBinding
import com.example.valeriana.fragments.HomeFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Add_Paciente : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityAddPacienteBinding
    private lateinit var database : DatabaseReference
    private var imageUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var name = ""
    private var tag = ""
    private var urlImage = ""
    private var asunto = ""
    private var detalle = ""
    private var prescripcion = ""
    private var indicacion = ""
    private var numero = ""
    private var fecha = ""
    private var time = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPacienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()

        val tags = resources.getStringArray(R.array.tags_list)
        val adapter = ArrayAdapter(this@Add_Paciente, R.layout.items_tag, tags)
        val ButtonBack : ImageButton = findViewById(R.id.btnBackAddCita)

        binding.profileImage.setOnClickListener {
            showImageAttachMenu()
        }

        binding.tieHorario.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tieTime.setOnClickListener {
            showTimePickerDialog()
        }

        ButtonBack.setOnClickListener {
            onBackPressed()
        }

        with(binding.actvTag){
            setAdapter(adapter)
            onItemClickListener = this@Add_Paciente
        }

        binding.btnAddBD.setOnClickListener{
            /*name = binding.tieNombre.text.toString().trim()
            tag = binding.actvTag.text.toString().trim()
            asunto = binding.tieAsuntoCita.text.toString().trim()
            detalle = binding.tieDetalles.text.toString().trim()
            prescripcion = binding.tiePrescripcion.text.toString().trim()
            indicacion = binding.tieIndicacion.text.toString().trim()
            numero = binding.tiePhone.text.toString().trim()
            fecha = binding.tieHorario.text.toString().trim()
            time = binding.tieTime.text.toString().trim()*/

            /*if (name.isEmpty()){
                Toast.makeText(this,"Ingresa el nombre del paciente",Toast.LENGTH_SHORT).show()
            } else if (tag.isEmpty()){
                Toast.makeText(this,"Ingresa una categoria de consulta",Toast.LENGTH_SHORT).show()
            } else if (asunto.isEmpty()){
                Toast.makeText(this,"Ingresa el asunto",Toast.LENGTH_SHORT).show()
            }else if(numero.isEmpty()){
                Toast.makeText(this,"Ingresa algun numero celular",Toast.LENGTH_SHORT).show()
            }else if(fecha.isEmpty()){
                Toast.makeText(this,"Ingresa la fecha de la cita",Toast.LENGTH_SHORT).show()
            }
            else if(time.isEmpty()){
                Toast.makeText(this,"Ingrese la hora de la cita",Toast.LENGTH_SHORT).show()
            }
            else{
                if (imageUri == null){
                    addProfile("")
                }
                else{
                    uploadImage()
                }
            }*/

            /*database = FirebaseDatabase.getInstance().getReference("pacientes")
            val User = user_cita(fecha,name, asunto, detalle, tag, numero, prescripcion, indicacion)
            database.child(name).setValue(User).addOnSuccessListener {
                binding.tieHorario.text?.clear()
                binding.tieNombre.text?.clear()
                binding.tieAsuntoCita.text?.clear()
                binding.tieDetalles.text?.clear()
                binding.actvTag.text?.clear()

                Toast.makeText(this@Add_Paciente, "Registro exitosos", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this@Add_Paciente, "No se pudo realizar el registro", Toast.LENGTH_LONG).show()
            }*/
            validateData()
        }

    }

    private fun validateData() {
        //get data
        name = binding.tieNombre.text.toString().trim()
        tag = binding.actvTag.text.toString().trim()
        asunto = binding.tieAsuntoCita.text.toString().trim()
        detalle = binding.tieDetalles.text.toString().trim()
        prescripcion = binding.tiePrescripcion.text.toString().trim()
        indicacion = binding.tieIndicacion.text.toString().trim()
        numero = binding.tiePhone.text.toString().trim()
        fecha = binding.tieHorario.text.toString()
        time = binding.tieTime.text.toString()
        if (name.isEmpty()){
            Toast.makeText(this,"Ingresa el nombre del paciente",Toast.LENGTH_SHORT).show()
        } else if (tag.isEmpty()){
            Toast.makeText(this,"Ingresa una categoria de consulta",Toast.LENGTH_SHORT).show()
        } else if (asunto.isEmpty()){
            Toast.makeText(this,"Ingresa el asunto",Toast.LENGTH_SHORT).show()
        }else if(numero.isEmpty()){
            Toast.makeText(this,"Ingresa algun numero celular",Toast.LENGTH_SHORT).show()
        }else if(fecha.isEmpty()){
            Toast.makeText(this,"Ingresa la fecha de la cita",Toast.LENGTH_SHORT).show()
        }
        else if(time.isEmpty()){
            Toast.makeText(this,"Ingrese la hora de la cita",Toast.LENGTH_SHORT).show()
        }
        else{
            if (imageUri == null){
                addProfile("")
            }
            else{
                uploadImage()
            }
        }
    }

    private fun addProfile(uploadImageUri: String) {
        progressDialog.setMessage("Creando cita")
        val firebaseUser = firebaseAuth.currentUser!!
        val id = UUID.randomUUID().toString()
        //setup info
        val User = user_cita(uploadImageUri, name, tag, fecha, time, asunto, detalle, prescripcion, indicacion, numero, id )
        /*val hashMmap: HashMap<String, Any> = HashMap()
        hashMmap["name"] = "$name"
        hashMmap["tag"] = "$tag"
        hashMmap["asunto"] = "$asunto"
        hashMmap["descripcion"] = "$detalle"
        hashMmap["prescripcion"] = "$prescripcion"
        hashMmap["indicaciones"] = "$indicacion"
        hashMmap["numero"] = "$numero"
        hashMmap["fecha"] = "$fecha"
        hashMmap["time"] = "$time"
        if (imageUri != null){
            hashMmap["profileImage"] = uploadImageUri
        }*/
        val reference = FirebaseDatabase.getInstance().getReference("pacientes")
        reference.child(firebaseUser.uid).child(id).setValue(User)
            .addOnSuccessListener {
                binding.tieHorario.text?.clear()
                binding.tieTime.text?.clear()
                binding.tiePrescripcion.text?.clear()
                binding.tieIndicacion.text?.clear()
                binding.tiePhone.text?.clear()
                binding.tieNombre.text?.clear()
                binding.tieAsuntoCita.text?.clear()
                binding.tieDetalles.text?.clear()
                //profile update
                progressDialog.dismiss()
                Toast.makeText(this,"Cita agregada",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Error al crear la cita",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()
        val nombreImage = binding.tieNombre.text.toString()
        val filePathAndName = "ProfileImages/"+nombreImage+"file.png"
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->
                //obtener url de la imagen
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"
                addProfile(uploadedImageUrl)
            }
            .addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(this,"Error al subir la imagen",Toast.LENGTH_SHORT).show()
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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p2)
        {
            0 ->{
                Toast.makeText(this@Add_Paciente,"Hola", Toast.LENGTH_LONG).show()
            }
            1 ->{
                Toast.makeText(this@Add_Paciente,"Hola", Toast.LENGTH_LONG).show()
            }
            2 ->{
                Toast.makeText(this@Add_Paciente,"Hola", Toast.LENGTH_LONG).show()
            }
            3 ->{
                Toast.makeText(this@Add_Paciente,"Hola", Toast.LENGTH_LONG).show()
            }
            4 ->{
                Toast.makeText(this@Add_Paciente,"Hola", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showImageAttachMenu(){
        val popupMenu = PopupMenu(this, binding.profileImage)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { item->
            val id = item.itemId
            if (id == 0){
                //Camera
                pickImageCamera()
            }
            else if (id == 1){
                pickImageGallery()
            }
            true
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK){
                val data = result.data
                //imageUri = data!!.data

                binding.profileImage.setImageURI(imageUri)
            }
            else{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                imageUri = data!!.data

                binding.profileImage.setImageURI(imageUri)
            }
            else{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )
}