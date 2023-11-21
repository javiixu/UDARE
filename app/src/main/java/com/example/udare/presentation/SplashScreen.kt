package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashScreen : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null

    //    private lateinit var auth: FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser


        Handler(Looper.getMainLooper()).postDelayed({
            val user = mAuth.currentUser
            //if (user == null) {
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
            /*} else {
                val mainIntent = Intent(this, RegistrationActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainIntent)
            }*/
            finish()
        }, 2000)

//        auth = FirebaseAuth.getInstance()
//        if (auth != null) {
//            currentUser = auth.currentUser!!
//        }
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            if (currentUser != null) {
//                val mainIntent = Intent(this, RegistrationActivity::class.java)
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(mainIntent)
//            } else {
//                val mainIntent = Intent(this, LoginActivity::class.java)
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(mainIntent)
//            }
//            finish()
//        }, 2000)
    }
}