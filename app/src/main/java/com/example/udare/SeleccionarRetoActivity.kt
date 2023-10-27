package com.example.udare

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SeleccionarRetoActivity : AppCompatActivity() {
    //variables for cameraX
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    private lateinit var cameraController: LifecycleCameraController
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO Check in Database if the challenge has already been done

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_reto)

        //TIMER MANAGMENT
        //current Calendar
        var current = Calendar.getInstance()

        //Difference in Miliseconds between our current time an the end of the challenge time
        val difference : Long

        //first day of challenge
        if(current.get(Calendar.HOUR_OF_DAY) == 6){
            difference = 0
        }
        else if(current.get(Calendar.HOUR_OF_DAY) > 6){
            // set a calendar to the following date at 6 am
            var followingDate = current.clone() as Calendar

            // Set the time to 6:00 AM, on the next day
            followingDate.set(Calendar.HOUR_OF_DAY, 6)
            followingDate.set(Calendar.MINUTE, 0)
            followingDate.set(Calendar.SECOND, 0)
            followingDate.set(Calendar.MILLISECOND, 0)
            followingDate.add(Calendar.DAY_OF_YEAR, 1)

            difference = followingDate.timeInMillis - current.timeInMillis
        }
        //second day of challenge
        else{
            // set a calendar to 6 am, do not change date, we are on the second day of the challenge
            var followingDate = current.clone() as Calendar
            followingDate.set(Calendar.HOUR_OF_DAY, 6)
            followingDate.set(Calendar.MINUTE, 0)
            followingDate.set(Calendar.SECOND, 0)
            followingDate.set(Calendar.MILLISECOND, 0)

            difference = followingDate.timeInMillis - current.timeInMillis
        }

        //starts the timer for the challenge
        val tvChallengeTimer = findViewById<TextView>(R.id.tvChallengeTimer)
        doTimer(difference, tvChallengeTimer)


        //CAMERA MANGAMENT
        if(allPermissionsGranted()){
            startCamera()
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 0)
        }


        //BUTTON workflow
        var btnSocialChallenge = findViewById<Button>(R.id.btnSocialChallenge)
        var btnCultureChallenge = findViewById<Button>(R.id.btnCultureChallenge)
        var btnSportChallenge = findViewById<Button>(R.id.btnSportChallenge)
        var btnCookingChallenge = findViewById<Button>(R.id.btnCookingChallenge)
        var btnGrowthChallenge = findViewById<Button>(R.id.btnGrowthChallenge)


        /*
         //TODO set the name of the buttons via the database
        btnSocialChallenge.text =
        btnCultureChallenge.text =
        btnSportChallenge.text =
        btnCookingChallenge.text =
        btnGrowthChallenge.text =

         */

        var choosenChallenge: String  = "choosen Challenge"


        //for every challenge handle what happens when this challenge gets clicked
        btnCookingChallenge.setOnClickListener(){
            choosenChallenge = btnCookingChallenge.text.toString()
            Intent(this,HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                startActivity(it)
            }

        }

        btnGrowthChallenge.setOnClickListener(){
            choosenChallenge = btnGrowthChallenge.text.toString()
            Intent(this,HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                startActivity(it)
            }
        }

        btnSocialChallenge.setOnClickListener(){
            choosenChallenge = btnSocialChallenge.text.toString()
            Intent(this,HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                startActivity(it)
            }
        }

        btnCultureChallenge.setOnClickListener(){
            choosenChallenge = btnSocialChallenge.text.toString()
            Intent(this,HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                startActivity(it)
            }
       }

        btnSportChallenge.setOnClickListener(){
            choosenChallenge = btnSocialChallenge.text.toString()
            Intent(this,HacerFotoActivity::class.java).also{
                it.putExtra("EXTRA_CHOOSEN_CHALLENGE",choosenChallenge)
                startActivity(it)
            }
        }

    }


    //function to manage timer
    private fun doTimer(difference : Long, tvTimer : TextView){
        var countDownTimer = object : CountDownTimer(difference, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60


                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                tvTimer.text =
                    "$elapsedHours:$elapsedMinutes:$elapsedSeconds"
            }

            //restarts the timer at 6 o clock in the morning
            override fun onFinish() {
                var diff : Long = 24 * 60 * 60 * 1000
                doTimer(diff, tvTimer)
            }
        }.start()
    }

    //start the camera preview
    private fun startCamera(){
        val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder)
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        previewView.controller = cameraController
    }


    //check if all necessary permissions are granted
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}

