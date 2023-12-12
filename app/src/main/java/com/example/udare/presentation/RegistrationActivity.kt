package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.udare.R
import com.example.udare.data.model.Profile
import com.example.udare.data.model.User
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.implementations.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    @Inject
    public lateinit var userService: UserService

    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var name : EditText
    private lateinit var username : EditText
    private lateinit var passwordAgain : EditText

    private lateinit var mRegister : Button
    private lateinit var existAccount: TextView
    private lateinit var mProgress : ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var user : User
    private lateinit var uid : String

    private lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.editTextEmail)
        password = findViewById(R.id.editTextPassword)
        name = findViewById(R.id.editTextName)
        username = findViewById(R.id.editTextUsername)
        passwordAgain = findViewById(R.id.editTextConfirmPassword)

        mRegister = findViewById(R.id.button_register)
        existAccount = findViewById(R.id.ya_tienes_c)
        mProgress = ProgressBar(this)
        mProgress.visibility = ProgressBar.GONE

        btnBack = findViewById(R.id.buttonBack)

        existAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mRegister.setOnClickListener {
            val emaill = email.text.toString().trim()
            val passwordd = password.text.toString().trim()
            val namee = name.text.toString().trim()
            val passwordAgainn = passwordAgain.text.toString().trim()
            val usernamee = username.text.toString().trim()

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
            } else if (usernamee.isEmpty()){
                username.error = "Username is required"
                username.requestFocus()
                return@setOnClickListener
            } else if (passwordAgainn != passwordd){
                passwordAgain.error = "Passwords must be the same"
                passwordAgain.requestFocus()
                return@setOnClickListener
            } else {
                registerUser(emaill, passwordd, namee, usernamee)
//                Print that the User is going to send to backend
                //sendUserToBack(emaill, passwordd, namee, uid)
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String, username : String ){
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
                    hashMap.put("username", username)
                    sendUserToBack(email, password, name, uid, username, hashMap)



//                    val db: FirebaseDatabase = FirebaseDatabase.getInstance()
//                    val ref: DatabaseReference = db.getReference("Users")
//                    ref.child(uid).setValue(hashMap)
//
//                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
//
//                    val mainIntent = Intent(this, Registration2Activity::class.java)
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    mainIntent.putExtra("userLogged", uid)
//                    startActivity(mainIntent)
//                    finish()
                } else {
                    mProgress.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                mProgress.visibility = ProgressBar.GONE
                Toast.makeText(this, "" + exception.message, Toast.LENGTH_SHORT).show() }
    }

    private fun sendUserToBack(email: String, password: String, name: String, uid: String, username: String, hashMap: HashMap<String, String>) {
//        Empty Array of posts
        var posts = emptyArray<String>()

        var profile : Profile = Profile(name,"","https://testudare.s3.eu-west-3.amazonaws.com/faceless-businessman-user-profile-icon-business-leader-profile-picture-portrait-user-member-people-icon-in-flat-style-circle-button-with-avatar-photo-silhouette-free-png.png", emptyArray<String>(), emptyArray<String>(),0,0,0,0,0)

        user = User(username, password, uid, email, posts, profile, false)
        Log.d("tag-prueba", "User to send: $user")


        userService.createUser(object : UserRepository.callbackPostUser {
            override fun onSuccess(user: User) {
//                Toast.makeText(this@RegistrationActivity, "User created", Toast.LENGTH_SHORT).show()

                val db: FirebaseDatabase = FirebaseDatabase.getInstance()
                val ref: DatabaseReference = db.getReference("Users")
                ref.child(uid).setValue(hashMap)

                Toast.makeText(this@RegistrationActivity, "Registration Successful", Toast.LENGTH_SHORT).show()

                val mainIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                mainIntent.putExtra("userLogged", uid)
                startActivity(mainIntent)
                finish()
            }

            override fun onError(mensajeError: String?) {
                Toast.makeText(this@RegistrationActivity, "Error: $mensajeError", Toast.LENGTH_SHORT).show()
            }
        }, user)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressedDispatcher.onBackPressed()
//        return super.onSupportNavigateUp()
//    }
}

