package com.example.udare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button

class SeleccionarRetoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_reto)

        //starts the timer for the challenge
        val tvChallengeTimer = findViewById<Button>(R.id.tvChallengeTimer)
        val timer = object: CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {...}
        }
        timer.start()
    }

    
}