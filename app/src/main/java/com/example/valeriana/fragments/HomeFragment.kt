package com.example.valeriana.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.R
import com.example.valeriana.databinding.FragmentCalendarBinding
import com.example.valeriana.databinding.FragmentHomeBinding
import com.example.valeriana.user_cita
import com.google.firebase.database.DatabaseReference

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container ,false)
        userRecyclerView =

        return binding.root
    }
}