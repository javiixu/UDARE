package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.udare.R
import com.example.udare.data.model.User
import com.example.udare.data.model.UserSingleton
import com.example.udare.data.repositories.Implementations.UserRepository
import com.example.udare.services.interfaces.IUserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
//    private lateinit var name: EditText
    private lateinit var mLogin: Button
    private lateinit var newAccount: TextView
    private lateinit var recoverPassword: TextView
    private var currentUser: FirebaseUser? = null
    private lateinit var loadingBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnBack: Button

    @Inject
    lateinit var userService: IUserService

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

//        val actionBar = supportActionBar
//        actionBar!!.title = "Login"
//        actionBar.setDisplayShowHomeEnabled(true)
//        actionBar.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mLogin = findViewById(R.id.button_login)

        email = findViewById(R.id.editTextPersonName)
        password = findViewById(R.id.editTextPassword)
        newAccount = findViewById(R.id.ya_tienes_c)
        recoverPassword = findViewById(R.id.te_apetece_)
        btnBack = findViewById(R.id.buttonBack)

        loadingBar = ProgressBar(this)

        if (mAuth.currentUser != null) {
            currentUser = mAuth.currentUser
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mLogin.setOnClickListener {
            val usernamee = email.text.toString()
            val passwordd = password.text.toString()

            if(usernamee.isEmpty()){
                email.error = "email is required"
                email.requestFocus()
                return@setOnClickListener
            } else if(!Patterns.EMAIL_ADDRESS.matcher(usernamee).matches()){
                email.error = "Please enter a valid email"
                email.requestFocus()
                return@setOnClickListener
            } else if(passwordd.isEmpty()){
                password.error = "Password is required"
                password.requestFocus()
                return@setOnClickListener
            } else if(passwordd.length < 6){
                password.error = "Password must be at least 6 characters long"
                password.requestFocus()
                return@setOnClickListener
            } else {
                loginUser(usernamee, passwordd)
            }
        }

        newAccount.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        recoverPassword.setOnClickListener {
            showRecoverPasswordDialog()
        }

    }

    private fun showRecoverPasswordDialog(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Recover Password")

        val linearLayout = LinearLayout(this)
        val emailEt = EditText(this)
        emailEt.hint = "Email"
        emailEt.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        emailEt.minEms = 16

        linearLayout.addView(emailEt)
        linearLayout.setPadding(10, 10, 10, 10)

        builder.setView(linearLayout)

        builder.setPositiveButton("Recover") { dialog, which ->
            val email = emailEt.text.toString().trim()
            beginRecovery(email)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun beginRecovery(email: String) {
        loadingBar.visibility = ProgressBar.VISIBLE
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                loadingBar.visibility = ProgressBar.GONE
                if (task.isSuccessful) {
                    val message = "Email sent"
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                } else {
                    val message = task.exception!!.message
                    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        loadingBar.visibility = ProgressBar.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loadingBar.visibility = ProgressBar.GONE
                    val user = mAuth.currentUser
                    val email = user?.email
                    val uid = user?.uid
                    Log.d("tag-user", email + " " + uid)
                    if (task.result?.additionalUserInfo?.isNewUser == true) {
                        val email = user?.email
                        val uid = user?.uid
                        Log.d("tag-user", email + " " + uid)
                        val hashMap = HashMap<String, String>()
                        hashMap["email"] = email ?: ""
                        hashMap["uid"] = uid ?: ""
                        hashMap["name"] = ""
                        hashMap["onlineStatus"] = "online"
                        hashMap["typingTo"] = "noOne"
                        hashMap["phone"] = ""
                        hashMap["image"] = ""
                        hashMap["cover"] = ""
                        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                        val reference: DatabaseReference = database.getReference("Users")
                        reference.child(uid ?: "").setValue(hashMap)
                    }

                    userService.getUserByUid(user?.uid, object : UserRepository.callbackGetUserByUid {
                        override fun onSuccess(user: User) {
                            UserSingleton.obtenerInstancia().iniciarSesion(user)
                            UserSingleton.obtenerInstancia().actualizarChallengeCompleted(false)
                            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, Inicio::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("userLogged", user?.uid)
                            Log.d("tag-user", user?.uid + "")
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(mensajeError: String?) {
                            Log.d("tag-comments", "Error in getUserByUid: $mensajeError")
                        }
                    })

                } else {
                    loadingBar.visibility = ProgressBar.GONE
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressedDispatcher.onBackPressed()
//        return super.onSupportNavigateUp()
//    }
}