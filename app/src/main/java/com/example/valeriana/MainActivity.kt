package com.example.valeriana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.valeriana.databinding.ActivityMainBinding
import com.example.valeriana.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        navView = binding.navView
        navView.itemIconTintList = null

        val homeFragment = HomeFragment()
        val notificationFragment = NotificationFragment()
        val calendarFragment = CalendarFragment()
        val usersFragment = UsersFragment()
        val profileFragment = ProfileFragment()
        setThatFragments(homeFragment)

        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_icon ->{
                    setThatFragments(homeFragment)
                }
                R.id.notification_icon ->{
                    setThatFragments(notificationFragment)
                }
                R.id.calendar_icon ->{
                    setThatFragments(calendarFragment)
                }
                R.id.users_icon ->{
                    setThatFragments(usersFragment)
                }
                R.id.profile_icon ->{
                    setThatFragments(profileFragment)
                }
            }
            true
        }



    }

    private fun setThatFragments(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.frame,fragment)
        commit()
    }
}