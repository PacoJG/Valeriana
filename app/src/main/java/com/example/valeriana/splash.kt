package com.example.valeriana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.valeriana.databinding.ActivityMainBinding
import com.example.valeriana.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.concurrent.thread

class splash : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thread {
            Thread.sleep(3000)
            firebaseAuth = FirebaseAuth.getInstance()
            checkUser()

        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            //no esta loggeado
            val intent = Intent(this@splash, LoginActivity::class.java)
            startActivity(intent)
            //sacamos de la pila
            finish()
        }
        else{
            val intent2 = Intent(this@splash, MainActivity::class.java)
            startActivity(intent2)
            //sacamos de la pila
            finish()
        }
    }
}