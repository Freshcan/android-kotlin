package com.bangkit.freshcanapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.ui.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intentMaps = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intentMaps)
    }
}