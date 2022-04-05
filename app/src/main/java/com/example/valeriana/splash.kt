package com.example.valeriana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.valeriana.databinding.ActivityMainBinding
import com.example.valeriana.databinding.ActivitySplashBinding
import kotlin.concurrent.thread

class splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        thread {
            Thread.sleep(3000)
            val intent = Intent(this@splash, MainActivity::class.java)
            startActivity(intent)
            //sacamos de la pila
            finish()
        }
    }
}