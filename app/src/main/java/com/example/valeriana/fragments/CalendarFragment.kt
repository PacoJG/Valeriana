package com.example.valeriana.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.valeriana.Adapters.Adapter_Calendar
import com.example.valeriana.Adapters.Adapter_Home
import com.example.valeriana.Add_Paciente
import com.example.valeriana.R
import com.example.valeriana.databinding.FragmentCalendarBinding
import com.example.valeriana.user_cita
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener
import org.jetbrains.annotations.Nullable
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var dayDatePicker: DayScrollDatePicker
    private lateinit var SelectedDate: String
    private lateinit var dayOfDate: String
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var fragmentView : View? = null
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList : ArrayList<user_cita>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater , container ,false)
        binding.btnAddCita.setOnClickListener {
            val intent = Intent(this@CalendarFragment.requireContext(), Add_Paciente::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<user_cita>()
        dayDatePicker = view.findViewById(R.id.dayDatePicker)
        dayDatePicker.setStartDate(21,7,2022)
        dayDatePicker.getSelectedDate(object : OnDateSelectedListener {
            override fun onDateSelected(@Nullable date: Date?) {
                SelectedDate = date.toString()
                Toast.makeText(context, SelectedDate, Toast.LENGTH_SHORT).show()
                //Log.d("FECHAAAAAA",SimpleDateFormat("MMM d, yyyy").format(date))
                dayOfDate = SimpleDateFormat("MMM d, yyyy").format(date)
                getData()
            }
        })
    }

    private fun getData() {
        val firebaseUser = firebaseAuth.currentUser!!
        val refData = FirebaseDatabase.getInstance().getReference("pacientes")
        //val date = getCurrentDateTime()
        //val dateInString = date.toString("MMM d, yyyy")
        refData.child(firebaseUser.uid!!).orderByChild("date").equalTo(dayOfDate)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(user_cita::class.java)
                            userArrayList.add(user!!)
                        }

                        userRecyclerView.adapter = Adapter_Calendar(userArrayList, this@CalendarFragment)
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