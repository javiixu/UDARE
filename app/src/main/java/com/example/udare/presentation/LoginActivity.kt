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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var mLogin: Button
    private lateinit var newAccount: TextView
    private lateinit var recoverPassword: TextView
    private var currentUser: FirebaseUser? = null
    private lateinit var loadingBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        actionBar!!.title = "Login"
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mLogin = findViewById(R.id.login_button)

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        newAccount = findViewById(R.id.needs_new_account)
        recoverPassword = findViewById(R.id.forgetp)

        loadingBar = ProgressBar(this)

        if (mAuth.currentUser != null) {
            currentUser = mAuth.currentUser
        }

        mLogin.setOnClickListener {
            val emaill = email.text.toString()
            val passwordd = password.text.toString()

            if(!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
                email.error = "Email is not valid"
                email.requestFocus()
                return@setOnClickListener
            } else if(passwordd.isEmpty() || passwordd.length < 6){
                password.error = "Password is not valid"
                password.requestFocus()
                return@setOnClickListener
            } else {
                loginUser(emaill, passwordd)
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
                    if (task.result?.additionalUserInfo?.isNewUser == true) {
                        val email = user?.email
                        val uid = user?.uid
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
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Inicio::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("userLogged", user?.uid)
                    Log.d("tag-user", user?.uid + "")
                    startActivity(intent)
                    finish()
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