package com.example.udare.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser

    private lateinit var myuid : String
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        actionBar = supportActionBar!!
        actionBar.title = "Perfil"

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.commit()

    }

    private fun checkUser() {
        firebaseUser = firebaseAuth.currentUser!!
        myuid = firebaseUser.uid
    }
}