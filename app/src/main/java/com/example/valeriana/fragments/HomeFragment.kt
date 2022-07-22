package com.example.valeriana.fragments

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valeriana.*
import com.example.valeriana.Adapters.Adapter_Calendar
import com.example.valeriana.Adapters.Adapter_Home
import com.example.valeriana.R
import com.example.valeriana.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnUserClickListenerHome {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var fragmentView : View? = null
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>
    private lateinit var cld : ConnectionLiveData
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkNetworkConnection()
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isReadPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
        }
        requestPermission()
    }

    private fun requestPermission(){
        isReadPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val permissionRequest : MutableList<String> = ArrayList()
        if(!isReadPermissionGranted){
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }


    private fun checkNetworkConnection(){
        cld = ConnectionLiveData(requireActivity().application)
        cld.observe(this) { isConnected ->
            if (isConnected) {
                //Toast.makeText(context, "Si hay internet", Toast.LENGTH_SHORT).show()
            } else {
                val dialogBinding = layoutInflater.inflate(R.layout.alert_internet, null)
                val myDialog = Dialog(requireContext())
                myDialog.setContentView(dialogBinding)
                myDialog.setCancelable(true)
                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                myDialog.show()
                val yesbtn = dialogBinding.findViewById<Button>(R.id.alert_yes)
                yesbtn.setOnClickListener {
                    myDialog.dismiss()
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater , container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()
        getImageProfile()
        val date = getCurrentDateTime()
        val dateInString = date.toString("MMM d, yyyy")
        Log.d("HORA", dateInString)
        userRecyclerView = view.findViewById(R.id.recyclerviewHome)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<user_cita>()
        userArrayList.clear()
        userRecyclerView.adapter?.notifyDataSetChanged()
        userRecyclerView.adapter = Adapter_Home(userArrayList, this@HomeFragment)
        getUserData()


    }

    private fun getUser() {
        val firebaseUser = firebaseAuth.currentUser!!
        val refUser = FirebaseDatabase.getInstance().getReference("users")
        refUser.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //progressDialog.dismiss()
                    val user = snapshot.child("name").value
                    val hello = "¡Hola"
                    binding.tvSaludo.text = hello.plus(" ").plus(user.toString()).plus("!")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    private fun getUserData(){
        userArrayList.clear()
        userRecyclerView.adapter?.notifyDataSetChanged()
        userRecyclerView.adapter = Adapter_Home(userArrayList, this@HomeFragment)
        val firebaseUser = firebaseAuth.currentUser!!
        val refData = FirebaseDatabase.getInstance().getReference("pacientes")
        val date = getCurrentDateTime()
        val dateInString = date.toString("MMM d, yyyy")
        refData.child(firebaseUser.uid!!).orderByChild("date").equalTo(dateInString)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    userArrayList.clear()
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(user_cita::class.java)
                        userArrayList.add(user!!)
                    }

                    userRecyclerView.adapter = Adapter_Home(userArrayList, this@HomeFragment)
                    userRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun getImageProfile(){
        val uid = firebaseAuth.uid
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileImage = "${snapshot.child("profileImage").value}"
                    try {
                        Glide.with(this@HomeFragment)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_profiledefault)
                            .into(binding.ivPhotoProfile)
                    }
                    catch (e: Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String{
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date{
        return Calendar.getInstance().time
    }

    override fun onUserItemClicked(position: Int) {
        val intent = Intent(context,DetailedActivity::class.java)
        intent.putExtra("name",userArrayList[position].name)
        intent.putExtra("tag",userArrayList[position].tag)
        intent.putExtra("date",userArrayList[position].date)
        intent.putExtra("time",userArrayList[position].time)
        intent.putExtra("asunto",userArrayList[position].asuntoCita)
        intent.putExtra("descripcion",userArrayList[position].detalles)
        intent.putExtra("indicaciones",userArrayList[position].indicaciones)
        intent.putExtra("prescripcion",userArrayList[position].prescripcion)
        intent.putExtra("phone",userArrayList[position].phone)
        intent.putExtra("key",userArrayList[position].key)
        intent.putExtra("imageUrl",userArrayList[position].imageUrl)
        startActivity(intent)
    }
}

