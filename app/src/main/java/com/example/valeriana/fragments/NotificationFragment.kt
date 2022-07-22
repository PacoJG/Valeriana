package com.example.valeriana.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.valeriana.ConnectionLiveData
import com.example.valeriana.R

class NotificationFragment : Fragment() {
    private lateinit var cld : ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

}