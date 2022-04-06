package com.example.valeriana.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.Adapters.Adapter_Home
import com.example.valeriana.R
import com.example.valeriana.databinding.FragmentCalendarBinding
import com.example.valeriana.databinding.FragmentHomeBinding
import com.example.valeriana.user_cita
import com.google.firebase.database.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbref : DatabaseReference
    var fragmentView : View? = null
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

       userRecyclerView = view.findViewById(R.id.recyclerviewHome)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<user_cita>()
        getUserData()


    }


    private fun getUserData(){
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(user_cita::class.java)
                        userArrayList.add(user!!)
                    }

                    userRecyclerView.adapter = Adapter_Home(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

