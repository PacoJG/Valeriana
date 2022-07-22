package com.example.valeriana.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.*
import com.example.valeriana.Adapters.Adapter_Directorio
import com.example.valeriana.Adapters.Adapter_Home
import com.example.valeriana.R
import com.example.valeriana.databinding.FragmentHomeBinding
import com.example.valeriana.databinding.FragmentUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var fragmentView : View? = null
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>
    val nombres: ArrayList<String> = ArrayList()
    private lateinit var cld : ConnectionLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkNetworkConnection()

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
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater , container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRecyclerView = view.findViewById(R.id.recyclerviewDirectorio)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<user_cita>()
        userRecyclerView.adapter = Adapter_Directorio(requireActivity(),userArrayList, this@UsersFragment)
        getUserData()
        Log.d("Cadena", nombres.toString())
    }

    private fun getUserData(){
        userArrayList.clear()
        userRecyclerView.adapter?.notifyDataSetChanged()
        userRecyclerView.adapter = Adapter_Directorio(requireActivity(),userArrayList, this@UsersFragment)
        val firebaseUser = firebaseAuth.currentUser!!
        val refData = FirebaseDatabase.getInstance().getReference("pacientes")
        //val date = getCurrentDateTime()
        //val dateInString = date.toString("MMM d, yyyy")
        refData.child(firebaseUser.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(user_cita::class.java)
                            userArrayList.add(user!!)
                        }

                        userRecyclerView.adapter = Adapter_Directorio(requireActivity(), userArrayList, this@UsersFragment)
                        userRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun onUserItemClicked(position: Int) {
        val intent = Intent(context, HistoricoActivity::class.java)
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