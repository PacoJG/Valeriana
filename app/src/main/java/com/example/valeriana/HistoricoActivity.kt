package com.example.valeriana

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valeriana.Adapters.Adapter_Historico
import com.example.valeriana.Adapters.Adapter_Home
import com.example.valeriana.databinding.ActivityHistoricoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.net.URLEncoder
import java.util.ArrayList

class HistoricoActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>

    private lateinit var binding: ActivityHistoricoBinding

    private var tmpName = ""
    private var tmpKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()

        val key = intent.getStringExtra("key")
        tmpKey = key.toString()
        //Log.d("KEYYYYYYYY", tmpKey)
        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        /*val tag = intent.getStringExtra("tag")
        val asunto = intent.getStringExtra("asunto")
        val date = intent.getStringExtra("date")
        val descripcion = intent.getStringExtra("descripcion")
        val time = intent.getStringExtra("time")
        val prescripcion = intent.getStringExtra("prescripcion")
        val indicaciones = intent.getStringExtra("indicaciones")*/
        val phone = intent.getStringExtra("phone")

        binding.tvName.text = name
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_profiledefault).into(binding.profileImage)

        userRecyclerView = findViewById(R.id.recyclerviewHistorico)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<user_cita>()
        userArrayList.clear()
        userRecyclerView.adapter?.notifyDataSetChanged()
        getUserData()

        binding.btnCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            val numero = phone.toString()
            callIntent.data = Uri.parse("tel:"+numero)
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
                Toast.makeText(this,"Otorga permisos a Valeriana para poder llamar", Toast.LENGTH_SHORT).show()

            }
        }
        binding.btnBackDeatl.setOnClickListener {
            onBackPressed()
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

    private fun getUserData(){
        tmpName = binding.tvName.text.toString().trim()
        val firebaseUser = firebaseAuth.currentUser!!
        val refData = FirebaseDatabase.getInstance().getReference("pacientes")
        //val date = getCurrentDateTime()
        //val dateInString = date.toString("MMM d, yyyy")
        refData.child(firebaseUser.uid!!).orderByChild("name").equalTo(tmpName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(user_cita::class.java)
                            userArrayList.add(user!!)
                        }

                        userRecyclerView.adapter = Adapter_Historico(userArrayList, this@HistoricoActivity)
                        userRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun onUserItemClicked(position: Int) {

    }
}