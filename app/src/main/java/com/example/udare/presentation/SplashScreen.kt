package com.example.udare.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import com.example.udare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SplashScreen : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null

    //    private lateinit var auth: FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loadingProgressBar2: ContentLoadingProgressBar
    private var progressStatus = 0
    private var handler = Handler();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        loadingProgressBar = findViewById(R.id.progressBar)

        Thread(Runnable {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post(Runnable {
                    loadingProgressBar.progress = progressStatus
                })
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            val mainIntent = Intent(this, LandingPageActivity::class.java)
            startActivity(mainIntent)
            finish()
        }).start()


//        Handler(Looper.getMainLooper()).postDelayed({
//            val user = mAuth.currentUser
//            //if (user == null) {
//                val mainIntent = Intent(this, LandingPageActivity::class.java)
//                startActivity(mainIntent)
//            /*} else {
//                val mainIntent = Intent(this, RegistrationActivity::class.java)
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(mainIntent)
//            }*/
//            finish()
//        }, 2000)

//
    }
}