package com.example.udare.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import android.widget.Button

class DashboardActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser : FirebaseUser

    private lateinit var myuid : String
    private lateinit var actionBar: ActionBar

    private lateinit var btnAddBlogs : Button
    private lateinit var btnProfile : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        actionBar = supportActionBar!!

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val homeFragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, homeFragment)
        transaction.commit()

        btnAddBlogs = findViewById(R.id.btnAddBlogs)
        btnProfile = findViewById(R.id.btnProfile)


        btnAddBlogs.setOnClickListener {
            val addPostFragment = AddPostFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content, addPostFragment)
            transaction.commit()
        }

        btnProfile.setOnClickListener {
            val profileFragment = ProfileFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content, profileFragment)
            transaction.commit()
        }

    }

    private fun checkUser() {
        firebaseUser = firebaseAuth.currentUser!!
        myuid = firebaseUser.uid
    }
}