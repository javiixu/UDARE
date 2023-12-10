package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LandingPageActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null

    private lateinit var mAuth: FirebaseAuth

    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

        btnLogin = findViewById(R.id.btniniciarses)
        btnRegister = findViewById(R.id.btnregistrarse)

        btnLogin.setOnClickListener {
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
        }

        btnRegister.setOnClickListener {
            val mainIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(mainIntent)
        }

    }
}