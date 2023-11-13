package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var name : EditText

    private lateinit var mRegister : Button
    private lateinit var existAccount: TextView
    private lateinit var mProgress : ProgressBar
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.register_email)
        password = findViewById(R.id.register_password)
        name = findViewById(R.id.register_name)

        mRegister = findViewById(R.id.register_button)
        existAccount = findViewById(R.id.homepage)
        mProgress = ProgressBar(this)
        mProgress.visibility = ProgressBar.GONE

        existAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        mRegister.setOnClickListener {
            val emaill = email.text.toString().trim()
            val passwordd = password.text.toString().trim()
            val namee = name.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
                email.error = "Email is not valid"
                email.requestFocus()
                return@setOnClickListener
            } else if (passwordd.length < 6){
                password.error = "Password must be at least 6 characters"
                password.requestFocus()
                return@setOnClickListener
            } else if (namee.isEmpty()){
                name.error = "Name is required"
                name.requestFocus()
                return@setOnClickListener
            } else {
                registerUser(emaill, passwordd, namee)
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String){
        mProgress.visibility = ProgressBar.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mProgress.visibility = ProgressBar.GONE
                    val user: FirebaseUser? = mAuth.currentUser
                    val email : String = user?.email.toString()
                    val uid : String = user?.uid.toString()
                    val hashMap : HashMap<String, String> = HashMap()
                    hashMap.put("email", email)
                    hashMap.put("uid", uid)
                    hashMap.put("name", name)
                    hashMap.put("image", "")
                    hashMap.put("onlineStatus", "online")

                    val db: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val ref: DatabaseReference = db.getReference("Users")
                    ref.child(uid).setValue(hashMap)

                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                    val mainIntent = Intent(this, Inicio::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(mainIntent)
                    finish()
                } else {
                    mProgress.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                mProgress.visibility = ProgressBar.GONE
                Toast.makeText(this, "" + exception.message, Toast.LENGTH_SHORT).show() }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressedDispatcher.onBackPressed()
//        return super.onSupportNavigateUp()
//    }
}

