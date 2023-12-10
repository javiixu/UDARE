package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Registration2Activity : AppCompatActivity() {

    private lateinit var btnStart : Button

    private lateinit var user : FirebaseUser
    private lateinit var uid : String
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)

        btnStart = findViewById(R.id.button_empezar)

        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser!!
        uid = user.uid

        btnStart.setOnClickListener {
            val mainIntent = Intent(this, Inicio::class.java)
            mainIntent.putExtra("userLogged", uid)
            startActivity(mainIntent)
        }





    }
}