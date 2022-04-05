package com.example.valeriana

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.valeriana.databinding.ActivityAddPacienteBinding
import com.example.valeriana.databinding.ActivityMainBinding
import com.example.valeriana.fragments.CalendarFragment
import com.example.valeriana.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_Paciente : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityAddPacienteBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPacienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tags = resources.getStringArray(R.array.tags_list)
        val adapter = ArrayAdapter(this@Add_Paciente, R.layout.items_tag, tags)
        val ButtonBack : ImageButton = findViewById(R.id.btnBackAddCita)
        ButtonBack.setOnClickListener {
            /*val myFragment = CalendarFragment()
            val fragment : Fragment? =
            supportFragmentManager.findFragmentByTag(CalendarFragment::class.java.simpleName)

            if (fragment !is CalendarFragment){
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame, myFragment, CalendarFragment::class.java.simpleName)
                    .commit()
            }*/
            onBackPressed()
        }

        with(binding.actvTag){
            setAdapter(adapter)
            onItemClickListener = this@Add_Paciente
        }

        binding.btnAddBD.setOnClickListener{
            val date = binding.tieHorario.text.toString()
            val name = binding.tieNombre.text.toString()
            val asuntoCita = binding.tieAsuntoCita.text.toString()
            val detalles = binding.tieDetalles.text.toString()
            val tag = binding.actvTag.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = user_cita(date,name, asuntoCita, detalles, tag)
            database.child(name).setValue(User).addOnSuccessListener {
                binding.tieHorario.text?.clear()
                binding.tieNombre.text?.clear()
                binding.tieAsuntoCita.text?.clear()
                binding.tieDetalles.text?.clear()
                binding.actvTag.text?.clear()

                Toast.makeText(this@Add_Paciente, "Registro exitosos", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                Toast.makeText(this@Add_Paciente, "No se pudo realizar el registro", Toast.LENGTH_LONG).show()
            }

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
}